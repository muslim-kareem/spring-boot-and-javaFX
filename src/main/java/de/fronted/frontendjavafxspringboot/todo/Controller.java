package de.fronted.frontendjavafxspringboot.todo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    private TodoStatus selectedStatus = TodoStatus.OPEN;

    @FXML
    private ListView<Todo> todoListView;
    @FXML
    private Label openLabel, inProgressLabel, doneLabel;
    @FXML
    private Button deleteButton;

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
    }

    public void setOpens() {
        selectedStatus = TodoStatus.OPEN;
        setBackgroundColor(openLabel);
        setSortedTodos(TodoStatus.OPEN);
    }

    public void setDone() {
        selectedStatus = TodoStatus.DONE;
        setBackgroundColor(doneLabel);
        setSortedTodos(TodoStatus.DONE);
    }

    public void setInProgress() {
        selectedStatus = TodoStatus.IN_PROGRESS;
        setBackgroundColor(inProgressLabel);
        setSortedTodos(TodoStatus.IN_PROGRESS);
    }

    public void setSortedTodos(TodoStatus status) {
        setTodosOnClick(todoService.getTodos().stream().filter(todo -> todo.getStatus().equals(status)).collect(Collectors.toList()));
    }

    private void setTodosOnClick(List<Todo> todos) {
        todoListView.getItems().clear();
        todoListView.getItems().addAll(todos);
    }

    private void setBackgroundColor(Label label) {
        if (openLabel.equals(label)) {
            openLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff99dd"), CornerRadii.EMPTY, null)));
            inProgressLabel.setBackground(null);
            doneLabel.setBackground(null);
        } else if (inProgressLabel.equals(label)) {
            inProgressLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff99dd"), CornerRadii.EMPTY, null)));
            openLabel.setBackground(null);
            doneLabel.setBackground(null);
        } else {
            doneLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff99dd"), CornerRadii.EMPTY, null)));
            inProgressLabel.setBackground(null);
            openLabel.setBackground(null);
        }
    }

    public void deleteTodoById() {
        var selectionModel = todoListView.getSelectionModel();
        if (selectionModel != null) {
            Todo selectedTodo = selectionModel.getSelectedItem();
            this.todoService.deleteTodoById(selectedTodo.getId());
            // to remove the item from Frontendlist

            setSortedTodos(selectedStatus);
        }
    }
}
