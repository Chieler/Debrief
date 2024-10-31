package com.debrief.Tags;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
public class imageBytes {

    public static String fileToBytes(File file) throws IOException{
        //gets file bytes in an array
        byte[] fileContent = Files.readAllBytes(file.toPath());
        //encode the byte in a string
        String encodedimage = Base64.getEncoder().encodeToString(fileContent);
        return encodedimage;
    }
}
