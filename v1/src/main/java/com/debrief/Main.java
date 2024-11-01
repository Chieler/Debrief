package com.debrief;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends javafx.application.Application{

    public static DatabaseManage dbManager;
    
    @Override
    public void start(Stage primaryStage){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Base.fxml"));
            primaryStage.setTitle("Debrief");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        createDatabase();
        launch(args);
    }

    public static void createDatabase(){
        dbManager = new DatabaseManage();
        dbManager.initDatabase();
    }
    public static double getScreenWidth(){
        Rectangle2D screen =  Screen.getPrimary().getVisualBounds();
        return screen.getWidth();
    }

    public static double getScreenHeight(){
        Rectangle2D screen =  Screen.getPrimary().getVisualBounds();
        return screen.getHeight();
    }
}