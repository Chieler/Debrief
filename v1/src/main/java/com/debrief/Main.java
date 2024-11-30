package com.debrief;

import java.io.FileInputStream;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.text.Font;
public class Main extends javafx.application.Application{
    //TODO: implement font for emoji
    //TODO: implement database delete logic for tags
    public static DatabaseManage dbManager;
    
    @Override
    public void start(Stage primaryStage){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Base.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.setTitle("Debrief");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            Font font = Font.loadFont(new FileInputStream("src/main/resources/Montserrat/static/Montserrat-Regular.ttf"), 12);
           
            if (font == null) {
                System.out.println("Font failed to load!");
            } else {
                System.out.println("Font loaded successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        createDatabase();
        dbManager.printUrlTable();
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