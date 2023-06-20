package de.fronted.frontendjavafxspringboot.todo;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class Controller implements Initializable {
    private final TodoService todoService = new TodoService();

    private List<Todo>  sortedTodos;



     @FXML
     private ListView<Todo> todoListView;
     @FXML
     private Label todoLabel,inProgressLabel, doneLabel;
     @FXML
     private Button deleteButton;

    String id = null;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setOpens();



        // Set a cell factory to display the description field of each one
        todoListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Todo todo, boolean empty) {
                super.updateItem(todo, empty);
                if (empty || todo == null) {
                    setText(null);
                } else {
                    setText(todo.getDescription());
                }
            }
        });


        todoListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Todo todo = todoListView.getSelectionModel().getSelectedItem();
               id = todo.getId();
                System.out.println(id);
            }
        });

    };

    public List<Todo> setOpens(){
        setBackgroundColor(todoLabel);
        sortedTodos = todoService.getTodos().stream().filter( todo -> todo.getStatus().equals(TodoStatus.OPEN)).collect(Collectors.toList());
        setTodosOnClick();
        return sortedTodos;
    }
    public List<Todo> setDone(){
        setBackgroundColor(doneLabel);
        sortedTodos = todoService.getTodos().stream().filter( todo -> todo.getStatus().equals(TodoStatus.DONE)).collect(Collectors.toList());
        setTodosOnClick();
        return sortedTodos;
    }  public List<Todo> setInProgress(){
         setBackgroundColor(inProgressLabel);
         sortedTodos = todoService.getTodos().stream().filter( todo -> todo.getStatus().equals(TodoStatus.IN_PROGRESS)).collect(Collectors.toList());
         setTodosOnClick();
        return sortedTodos;
    }


    private void setTodosOnClick(){
        todoListView.getItems().clear();
        todoListView.getItems().addAll(sortedTodos);
    }

    private void setBackgroundColor(Label label){
        if(todoLabel.equals(label)){
            todoLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff99dd"), CornerRadii.EMPTY, null)));
            inProgressLabel.setBackground(null);
            doneLabel.setBackground(null);
        }else if (inProgressLabel.equals(label)){
            inProgressLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff99dd"), CornerRadii.EMPTY, null)));
            todoLabel.setBackground(null);
            doneLabel.setBackground(null);
        }else{
            doneLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff99dd"), CornerRadii.EMPTY, null)));
            inProgressLabel.setBackground(null);
            todoLabel.setBackground(null);
        }
    }


  public void deleteTodoById(){
      this.todoService.deleteTodoById(id);
      todoListView.getItems().clear();
      setOpens();
      System.out.println("deleterTodoById excited");
  }
}