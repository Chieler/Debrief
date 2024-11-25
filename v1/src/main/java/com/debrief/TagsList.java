package com.debrief;

import java.util.ArrayList;
import javafx.scene.control.TextField;

public class TagsList {
    ArrayList<TextField> list = null;
    public TagsList(){
        list = new ArrayList<>(10);
    }
    /**
     * Takes in TextField and adds to list
     * @param field
     */
    public void add(TextField field){
        list.add(field);
    }
    /** 
     * Removes based on index
     * @param index
     */
    public void remove(TextField field){
        //first get index of the item
        int i = list.indexOf(field);
        //removes box
        list.remove(field);
        Main.dbManager.deleteTagsColumn(i+1);
    }
}
