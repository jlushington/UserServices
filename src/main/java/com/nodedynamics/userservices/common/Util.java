package com.nodedynamics.userservices.common;


public class Util {


    public static String getFileExtension(String file) {
        String extension = "";
        extension = file.substring(file.lastIndexOf(".")).substring(1);
        return extension;
 
    }
    
}
