package de.fronted.frontendjavafxspringboot.todo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import org.springframework.stereotype.Component;

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
     private Label openLabel,inProgressLabel, doneLabel;
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
            }
        });

    };

    public List<Todo> setOpens(){
        setSortedTodos(openLabel,TodoStatus.OPEN);
        return sortedTodos;
    }
    public List<Todo> setDone(){
        setSortedTodos(doneLabel,TodoStatus.DONE);
        return sortedTodos;
    }  public List<Todo> setInProgress(){
         setSortedTodos(inProgressLabel,TodoStatus.IN_PROGRESS);
        return sortedTodos;
    }

    public void setSortedTodos(Label statusLabel, TodoStatus status){
        setBackgroundColor(statusLabel);
        sortedTodos = todoService.getTodos().stream().filter( todo -> todo.getStatus().equals(status)).collect(Collectors.toList());
        setTodosOnClick();
    }

    private void setTodosOnClick(){
        todoListView.getItems().clear();
        todoListView.getItems().addAll(sortedTodos);
    }

    private void setBackgroundColor(Label label){
        if(openLabel.equals(label)){
            openLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff99dd"), CornerRadii.EMPTY, null)));
            inProgressLabel.setBackground(null);
            doneLabel.setBackground(null);
        }else if (inProgressLabel.equals(label)){
            inProgressLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff99dd"), CornerRadii.EMPTY, null)));
            openLabel.setBackground(null);
            doneLabel.setBackground(null);
        }else{
            doneLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff99dd"), CornerRadii.EMPTY, null)));
            inProgressLabel.setBackground(null);
            openLabel.setBackground(null);
        }
    }

  public void deleteTodoById(){
        if(id != null){
            this.todoService.deleteTodoById(id);
            // to remove the item from Frontendlist
            sortedTodos.forEach( td -> {
                        if(td.getId().equals(id)){
                            if(td.getStatus().equals(TodoStatus.OPEN)){
                                id = null;
                                todoListView.getItems().clear();
                                setOpens();
                            }
                            else if(td.getStatus().equals(TodoStatus.IN_PROGRESS)){
                                id = null;
                                todoListView.getItems().clear();
                                setInProgress();
                            }else {
                                id = null;
                                todoListView.getItems().clear();
                                setDone();
                            }
                        }
                    }
            );
      }


  }
}