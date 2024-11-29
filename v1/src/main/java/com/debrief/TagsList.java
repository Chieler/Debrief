package com.debrief;

import java.util.ArrayList;
import javafx.scene.text.Text;

public class TagsList {
    ArrayList<Text> list = null;
    public TagsList(){
        list = new ArrayList<>(10);
    }
    /**
     * Takes in TextField and adds to list
     * @param field
     */
    public void add(Text field){
        list.add(field);
    }
    /** 
     * Removes based on index
     * @param index
     */
    public void remove(Text field){
        //first get index of the item
        int i = list.indexOf(field);
        //removes box
        list.remove(field);
        Main.dbManager.deleteTagsColumn(i+1);
    }
}
