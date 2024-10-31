package com.debrief.Tags;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ConvertPy {
    ProcessBuilder processBuilder;
    String jsonMap;
    public void convertToJson(HashMap<String, String> map){
        this.jsonMap = new com.google.gson.Gson().toJson(map);
    }
    public String runPyScript(){
        StringBuilder build = new StringBuilder();
        try{
            processBuilder=new ProcessBuilder("/Users/lichieler/deBrief_v2/v1/venv/bin/python3.12", "src/main/java/com/debrief/Tags/gemini.py",jsonMap);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                build.append(line);
            }
            while(((line= errReader.readLine())!=null)){
                System.out.println(line);
            }
            process.waitFor();
        }catch(IOException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            System.out.println("Interrupted exception!");
        }
        return build.toString();
    }
}
