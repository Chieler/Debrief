package com.debrief;


import com.debrief.Tags.Runner;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Controller {
    @FXML
    private VBox ToDoBox;
    @FXML
    private AnchorPane tagAnchor;
    @FXML
    private TextField tagTextField;
    @FXML
    private TextFlow tagTextDisplay;
    @FXML
    private WebView webView;

    private WebEngine engine;
    @FXML
    public void initialize(){   
        tagTextFieldFunc();
        displauWebView();

    }
    public void displauWebView(){
        engine = webView.getEngine();
        engine.load("https://www.google.com/");
    }
    public void tagTextFieldFunc(){
        tagTextField.setStyle("""
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-padding: 8;
            -fx-border-color: #d3d3d3;
        """);
        tagTextField.setOnKeyPressed(event->createTag(event));
    }
    private void createTag(KeyEvent event){
        if(event.getCode()==KeyCode.ENTER){
            String tag = tagTextField.getText();
            tagTextField.clear();
            Text tagContent = new Text("\n  #: "+ tag + "\n");
            tagTextDisplay.getChildren().add(tagContent);

            ProgressIndicator indicator = new ProgressIndicator();
            indicator.setMaxSize(20, 20);
            tagTextDisplay.getChildren().add(indicator);
            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception{
                    Runner runner = new Runner(tag);
                    return runner.run();
                }
            };

            task.setOnSucceeded(e->{
                Platform.runLater(() ->{
                    String result = task.getValue();
                    // Remove loading indicator
                    tagTextDisplay.getChildren().remove(indicator);
                    // Add result text
                    Text resultText = new Text(result + "\n");
                    tagTextDisplay.getChildren().add(resultText);
                });
            });
            
            task.setOnFailed(e ->{
                Platform.runLater(() -> {
                    // Remove loading indicator
                    tagTextDisplay.getChildren().remove(indicator);
                    // Show error message
                    Text errorText = new Text("Error processing tag");
                    errorText.setFill(Color.RED);
                    tagTextDisplay.getChildren().add(errorText);
                });
            });

            new Thread(task).start();
            };
            
        }
}
