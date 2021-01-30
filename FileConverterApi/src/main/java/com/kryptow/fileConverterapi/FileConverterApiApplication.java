package com.kryptow.fileConverterapi;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kryptow.fileConverterapi.tools.FileWatcher;

@SpringBootApplication
public class FileConverterApiApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(FileConverterApiApplication.class, args);
		//FileWatcher.start();
	}

}
