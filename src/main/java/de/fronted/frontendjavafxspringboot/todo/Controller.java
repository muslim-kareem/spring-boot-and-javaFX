package de.fronted.frontendjavafxspringboot.todo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
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
    private Button deleteButton, saveButton;
    @FXML
    private TextField inputField;

    private Stage stage;


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
        List<Todo> theSorted = todoService.getTodos().stream().filter(todo -> todo.getStatus().equals(status)).collect(Collectors.toList());
        setTodosOnClick(theSorted);
    }

    private void setTodosOnClick(List<Todo> todos) {
        todoListView.getItems().clear();
        todoListView.getItems().addAll(todos);
    }

    private void setBackgroundColor(Label label) {
        label.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff99dd"), CornerRadii.EMPTY, null)));
        if (openLabel.equals(label)) {
            inProgressLabel.setBackground(null);
            doneLabel.setBackground(null);
        } else if (inProgressLabel.equals(label)) {
            openLabel.setBackground(null);
            doneLabel.setBackground(null);
        } else {
            inProgressLabel.setBackground(null);
            openLabel.setBackground(null);
        }
    }

    public void deleteTodoById() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        var selectionModel = todoListView.getSelectionModel();
        Todo selectedTodo = selectionModel.getSelectedItem();
        if (selectedTodo != null) {
            alert.setTitle("DELETE");
            alert.setHeaderText("You're about to delete this task!");
            alert.setContentText("Do you want to delete? ");
            if (alert.showAndWait().get() == ButtonType.OK) {
                this.todoService.deleteTodoById(selectedTodo.getId());
            }
            setSortedTodos(selectedStatus);
        }else{
            alert.setTitle("SELECT");
            alert.setHeaderText("Please select Item before trying to delete!");
            alert.showAndWait();
        }
    }

    public void saveTodo() {
        String description = inputField.getText();
        if(!description.isBlank()){
            this.todoService.createTodo(new Todo(null,description,TodoStatus.OPEN));
            setOpens();
            inputField.clear();;
        }

    }

}
