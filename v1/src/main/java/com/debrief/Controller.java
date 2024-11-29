package com.debrief;


import java.util.Optional;
import com.debrief.Tags.Runner;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * 
 * Class that injects function into FXML objects
 * 
 */
public class Controller {
    //declares FXML elements matching to fxml file
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
    @FXML
    private ScrollPane ScrollTodo;
    @FXML
    private AnchorPane AnchorTodo;
    @FXML
    private Label todoLabel; 
    //initializes 
    protected ToDoList toDoList= new ToDoList();
    protected TagsList mainList = new TagsList();
    private WebEngine engine;
    @FXML
    public void initialize(){ 
        tagTextDisplay.getChildren().add(new Text("🌤️"));
        ToDoBox.prefHeightProperty().bind(ScrollTodo.heightProperty());  
        ToDoBox.prefWidthProperty().bind(ScrollTodo.widthProperty());
        setupToDoContextMenu();
        populateTagsTable();
        populateToDoList();
        tagTextFieldFunc();
        displayWebView();
        bindToDotextSize();

    }
    public void bindToDotextSize(){
        todoLabel.styleProperty().bind(Bindings.createStringBinding(() -> 
        String.format("-fx-font-size: %.2fpx;", ToDoBox.getWidth() * 0.092), 
        ToDoBox.widthProperty()));
    }
    public void setupTagsContextMenu(TagsList list, Text tag, Text text, TextFlow flow){
        ContextMenu menu = new ContextMenu();
        MenuItem remove = new MenuItem("remove");
        menu.getItems().addAll(remove);
        text.setOnContextMenuRequested(event->menu.show(text, event.getScreenX(), event.getScreenY()));
        remove.setOnAction(event-> {
            flow.getChildren().remove(text);
            flow.getChildren().remove(tag);
            list.remove(tag);});
    }
    public void displayWebView(){
        engine = webView.getEngine();
        engine.load("https://www.google.com/");
    }
    public void tagTextFieldFunc(){
        tagTextField.setStyle("""
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-padding: 8;
        """);
        tagTextField.setOnKeyPressed(event->createTag(event));
    }
    private void createTag(KeyEvent event){
        if(event.getCode()==KeyCode.ENTER){
            String tag = tagTextField.getText();
            DatabaseManage dbManage = Main.dbManager;
            dbManage.insertTag(tag);
            dbManage.printTagTable();
            tagTextField.clear();
            Text tagContent = new Text("\n  #:  "+ tag + "\n");
            mainList.add(tagContent);
            tagTextDisplay.getChildren().add(tagContent);
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
                    // Add result text
                    Text resultText = new Text(result);
                    ///////////////
                    setupTagsContextMenu(mainList, tagContent, resultText, tagTextDisplay);
                    tagTextDisplay.getChildren().add(resultText);
                });
            });
            
            task.setOnFailed(e ->{
                Platform.runLater(() -> {
                    // Show error message
                    Text errorText = new Text("Error processing tag");
                    errorText.setFill(Color.RED);
                    tagTextDisplay.getChildren().add(errorText);
                });
            });

            new Thread(task).start();
            };
            
        }
    public void populateTagsTable(){
        DatabaseManage dbManage = Main.dbManager;
        if(dbManage.getTagsTableSize()==0){
            return;
        }
        int tableSize = dbManage.getTagsTableSize();

        for(int i =0; i<tableSize; i++){
            String tag=dbManage.getTagsByIndex(i+1);
            System.out.println("++++++++++++++");
            System.out.println(tag);
            Text tagContent = new Text("\n  #:  "+ tag + "\n");
            mainList.add(tagContent);
            tagTextDisplay.getChildren().add(tagContent);
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
                    // Add result text
                    Text resultText = new Text(result + "\n");
                    tagTextDisplay.getChildren().add(resultText);
                    //////////////
                    setupTagsContextMenu(mainList, tagContent, resultText, tagTextDisplay);
                });
            });
            
            task.setOnFailed(e ->{
                Platform.runLater(() -> {
                    // Show error message
                    Text errorText = new Text("Error processing tag");
                    errorText.setFill(Color.RED);
                    tagTextDisplay.getChildren().add(errorText);
                });
            });

            new Thread(task).start();
        }
    }
    public void setupToDoContextMenu(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem add = new MenuItem("add to-do");
        add.setOnAction(event->showTextDialog());
        contextMenu.getItems().addAll(add);
        ToDoBox.setOnContextMenuRequested(event ->{
            contextMenu.show(ToDoBox, event.getScreenX(), event.getScreenY());
        });
        
    }
    public void showTextDialog(){
        TextInputDialog dialog = new TextInputDialog("enter to-do");
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Enter your text below:");
        dialog.setContentText("Input:");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) createToDo(result.get());
        
    }
    public void createToDo(String todo){
        DatabaseManage dbManage = Main.dbManager;
        dbManage.insertToDo(todo);
        dbManage.printToDosTable();
        HBox todoItem = new HBox();
        todoItem.setSpacing(10); // Add some spacing between elements
        // Create the TextField
        TextField toDoField = new TextField(todo);
        
        // Create a Delete Button
        Button option = new Button("...");
        option.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: transparent;
        """);
        option.setOnAction(e -> {setupToDoMenu(e, option, todoItem, toDoField);});
    
        // Add the TextField and Delete Button to the HBox
        todoItem.getChildren().addAll(toDoField, option);
    
        // Add the HBox to the ToDoBox
        ToDoBox.getChildren().add(todoItem);
    }
    public void setupToDoMenu(Event e, Button button, HBox todoItem, TextField todoField) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem remove = new MenuItem("Remove");
        MenuItem cross = new MenuItem("Cross Out");
        
        remove.setOnAction(event -> {ToDoBox.getChildren().remove(todoItem);
                                    toDoList.remove(todoItem);
            });
            
        cross.setOnAction(event -> todoField.setStyle("-fx-text-decoration: line-through;"));
    
        contextMenu.getItems().addAll(cross, remove);
        

        contextMenu.show(button, 
            button.localToScreen(button.getBoundsInLocal()).getMinX(),
            button.localToScreen(button.getBoundsInLocal()).getMaxY());
    }
    public void populateToDoList(){
        DatabaseManage dbManage = Main.dbManager;
        if(dbManage.getToDosTableSize()==-1){
            return;
        }
        int tableSize = dbManage.getToDosTableSize();
        for(int i =0; i<tableSize; i++){
            String todo = dbManage.getToDosByIndex(i+1);
            createToDoWithoutAdding(todo);
        }
    }
    private void createToDoWithoutAdding(String todo){
        HBox todoItem = new HBox();
        todoItem.setSpacing(10); // Add some spacing between elements
        // Create the TextField
        TextField toDoField = new TextField(todo);
    
        // Create a Delete Button
        Button option = new Button("...");
        option.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: transparent;
        """);
        option.setOnAction(e -> {setupToDoMenu(e, option, todoItem, toDoField); System.out.println("registered");});
    
        // Add the TextField and Delete Button to the HBox
        todoItem.getChildren().addAll(toDoField, option);
        toDoList.add(todoItem);
        System.out.println(toDoList.getToDoList().size());
        // Add the HBox to the ToDoBox
        ToDoBox.getChildren().add(todoItem);
    }
}
