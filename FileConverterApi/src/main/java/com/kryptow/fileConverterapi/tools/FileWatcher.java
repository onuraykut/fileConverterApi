package com.kryptow.fileConverterapi.tools;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class FileWatcher {

    public static void start() throws IOException, InterruptedException {
        WatchService watchService
          = FileSystems.getDefault().newWatchService();

        Path path = Paths.get("/home/kryptow/Documents/");

        path.register(
          watchService, 
            StandardWatchEventKinds.ENTRY_CREATE);
             // StandardWatchEventKinds.ENTRY_DELETE, 
             //   StandardWatchEventKinds.ENTRY_MODIFY

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
            	 if(event.kind().name().equals("ENTRY_CREATE")){
            		 System.out.println("Oluşturuldu");
            	 }
               /* System.out.println(
                  "Event kind:" + event.kind() 
                    + ". File affected: " + event.context() + "."); */
            }
            key.reset();
        }
    }
}