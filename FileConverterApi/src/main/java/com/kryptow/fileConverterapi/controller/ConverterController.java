package com.kryptow.fileConverterapi.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import com.kryptow.fileConverterapi.tools.FileBaseName;
import com.kryptow.fileConverterapi.tools.FileWatcher;
import com.kryptow.fileConverterapi.tools.PushNotificationServiceImpl;

@RestController
@RequestMapping("/convert")
public class ConverterController {
	private static String UPLOADED_FOLDER = "/var/www/html/fileconverter/upload/";
	private static String OUTPUT_FOLDER = "/var/www/html/fileconverter/output/";
	private static String shFolder = "/home/converter.sh";
	private static String URL = "http://148.251.168.11/fileconverter/output/";
	private String fileName;
	
	@PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("ext") String ext,@RequestParam String token,@RequestParam String languageCode) throws IOException, InterruptedException {
        if (file.isEmpty()) {
            return "uploadStatus";
        }
        fileName = file.getOriginalFilename();
       
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            File newFile = path.toFile();
            int i=1;
            while(newFile.exists()) {
            	fileName = FileBaseName.getUniqName(fileName,i);
            	path = Paths.get(UPLOADED_FOLDER + fileName);
            	newFile = path.toFile();
            	i++;
            }
    		Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
				if (file.getInputStream() != null) {
					file.getInputStream().close();
				}
		
				try {
					executeScript(fileName,FileBaseName.getBaseName(fileName),ext,token,languageCode);
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/* Runnable r = new Runnable() {
			         public void run() {
			        	  
			         }
			     };
			     new Thread(r).start(); */
}
        return URL+FileBaseName.getBaseName(fileName)+"."+ext;
    }
	
	private void executeScript(String fileName,String outputName,String ext,String token,String languageCode) throws IOException, InterruptedException {
		 Process proc = Runtime.getRuntime().exec("sh "+shFolder+" "+UPLOADED_FOLDER+fileName+" "+OUTPUT_FOLDER+outputName+"."+ext);                    
		 //proc.waitFor();
		 

		 long freeE1s;
		 try (BufferedReader buf =
				 new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
			 freeE1s = buf.lines().filter(line -> line.contains("Idle")).count();
		 }

		 proc.waitFor();

		 System.out.println(freeE1s);
		 
		 new PushNotificationServiceImpl().sendPushNotification(token,URL+outputName+"."+ext,languageCode);
	}
	
}
