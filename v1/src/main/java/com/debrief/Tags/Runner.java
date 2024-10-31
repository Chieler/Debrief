package com.debrief.Tags;

import java.util.HashMap;

public class Runner{
    String tag;
    public Runner(String tag){
        this.tag=tag;
    }
    public String run(){
        SeTess scrape = new SeTess();
        HashMap<String, String> map = scrape.init(tag);
        ConvertPy convert = new ConvertPy();
        convert.convertToJson(map);
        String output = convert.runPyScript();
        return output;
    }

}