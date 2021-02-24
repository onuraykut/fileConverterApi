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

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import com.kryptow.fileConverterapi.tools.FileBaseName;
import com.kryptow.fileConverterapi.tools.FileWatcher;
import com.kryptow.fileConverterapi.tools.PushNotificationServiceImpl;
import com.kryptow.fileConverterapi.tools.ResponseTransfer;

@RestController
@RequestMapping("/convert")
public class ConverterController {
	private static String UPLOADED_FOLDER = "/var/www/html/fileconverter/upload/";
	private static String OUTPUT_FOLDER = "/var/www/html/fileconverter/output/";
	private static String shFolder = "/home/converter.sh";
	private static String URL = "http://148.251.168.11/fileconverter/output/";
	private String fileName;
	
	@PostMapping("/upload")
	@ResponseBody
    public ResponseTransfer singleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("ext") String ext,@RequestParam String token,@RequestParam String languageCode) throws IOException, InterruptedException {
        
        fileName = file.getOriginalFilename();
        fileName = StringUtils.trimAllWhitespace(fileName);
       // fileName = fileName.replaceAll("\\s+","");
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
					return 	new ResponseTransfer(false,"error");
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
        return 	new ResponseTransfer(false,"error");				
    }
	
	private ResponseTransfer executeScript(String fileName,String outputName,String ext,String token,String languageCode) throws IOException, InterruptedException {
		 //Process proc = Runtime.getRuntime().exec("sh "+shFolder+" "+UPLOADED_FOLDER+fileName+" "+OUTPUT_FOLDER+outputName+"."+ext);                    
		 //proc.waitFor();
		ResponseTransfer responseTransfer = new ResponseTransfer(false,"error");
		
		ProcessBuilder   ps=new ProcessBuilder("sh",shFolder,UPLOADED_FOLDER+fileName,OUTPUT_FOLDER+outputName+"."+ext);
		ps.redirectErrorStream(true);
		Process pr = ps.start();  

		BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
		    System.out.println(line);
		    if(line.contains("Output saved to"))
		    	responseTransfer = new ResponseTransfer(true, "SUCCESS",URL+FileBaseName.getBaseName(fileName)+"."+ext);
		 else if(line.contains("DRM"))
			 responseTransfer = new ResponseTransfer(false,"DRM ERROR");
		}
		pr.waitFor();
		System.out.println("ok!");

		in.close();
		 
		 new PushNotificationServiceImpl().sendPushNotification(token,URL+outputName+"."+ext,languageCode);
		 
		 return responseTransfer;
	}
	
}
