package com.debrief.Tags;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.util.HashMap;
import java.io.File;


public class SeTess {

        //variables
        WebDriver driver;
        JavascriptExecutor executor;
        boolean notEntered;
        String categories ;
        String URL;
        HashMap<String, String> map;
        String[] list;
        public HashMap<String, String> init(String category){
        //enters when user is done inputting
            map = new HashMap<>();
            if(true){
                AppConfig.init();
                driver = AppConfig.getDrive();       
                //tesseract = AppConfig.getTesseract(); 
                //Initializes driver
                URL ="https://www.google.com/search?q=";
    
                try{
                    String temp = category.replaceAll(" ", "+");
                    driver.get(URL+temp);
                    executor = (JavascriptExecutor)driver;
                    while(!executor.executeScript("return document.readyState").equals("complete")){
                        Thread.sleep(250);
                    }
                    //Waits unti web has fully loaded
                    executor.executeScript("document.body.style.zoom = '0.5'");
                    File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                    String s =imageBytes.fileToBytes(screenshot);
                    map.put("question: what is the" + category, s);
                    
                    screenshot.delete();         
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    driver.close();
                }
                }
                return map;
        }

}
