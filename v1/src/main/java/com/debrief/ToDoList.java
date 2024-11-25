package com.debrief;

import java.util.ArrayList;
import javafx.scene.layout.HBox;

public class ToDoList {
    ArrayList<HBox> list;
    public ToDoList(){
        list = new ArrayList<>(10);
    }
    public ArrayList<HBox> getToDoList(){
        return this.list;
    }
    /**
     * 
     * Takes in TextField and adds to list
     * @param field
     */
    public void add(HBox field){
        list.add(field);
    }

    /**
     * 
     * Removes based on index
     * @param index
     */
    public void remove(HBox box){
        //first get index of the item
        int i = list.indexOf(box);
        //removes box
        list.remove(box);
        Main.dbManager.deleteToDosColumn(i+1);
    }
}
