package com.kryptow.fileConverterapi.tools;

public class FileBaseName {
	public static String getBaseName(String fileName) {
	    int index = fileName.lastIndexOf('.');
	    if (index == -1) {
	        return fileName;
	    } else {
	        return fileName.substring(0, index);
	    }
	}
	
	public static String getUniqName(String fileName,int count) {
	    int index = fileName.lastIndexOf('.');
	    if (index == -1) {
	        return fileName;
	    } else {
	        return  fileName.substring(0, index) + "-" + count + fileName.substring(index, fileName.length());
	    }
	}
}
