package de.fronted.frontendjavafxspringboot.controller;

import de.fronted.frontendjavafxspringboot.todo.Todo;
import de.fronted.frontendjavafxspringboot.todo.TodoService;
import de.fronted.frontendjavafxspringboot.todo.TodoStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class DashboardController implements Initializable {
    private final TodoService todoService = new TodoService();

    private TodoStatus selectedStatus = TodoStatus.OPEN;


    private Scene scene;
    private Stage stage;
    private FXMLLoader root;

    @FXML
    private ListView<Todo> todoListView;
    @FXML
    private Label openLabel, inProgressLabel, doneLabel;
    @FXML
    private Button deleteButton, saveButton, updateButton;
    @FXML
    private TextField inputField;

    @FXML
    private Label selectedLabel;


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

        // just to show the selected Todo in the selectedLabel
        todoListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Double-click
                Todo selectedItem = todoListView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    selectedLabel.setTextFill(Color.BLUEVIOLET);
                    selectedLabel.setText("Selected Todo:   " + selectedItem.getDescription());
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
                selectedLabel.setText("No selected Item! ");
                selectedLabel.setTextFill(Color.RED);
            }
            setSortedTodos(selectedStatus);
        } else {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("SELECTION");
            alert.setHeaderText("Please select Item before trying to delete!");
            alert.showAndWait();
        }
    }

    public void saveTodo() {
        String description = inputField.getText();
        if (!description.isBlank()) {
            this.todoService.createTodo(new Todo(null, description, TodoStatus.OPEN));
            setOpens();
            inputField.clear();
            ;
        }
    }

    public void switchToDetailsController(ActionEvent e) throws IOException {
        var selectionModel = todoListView.getSelectionModel();
        Todo selectedTodo = selectionModel.getSelectedItem();

        if (selectedTodo != null) {
            DetailsController.id = selectedTodo.getId();
            root = new FXMLLoader(getClass().getResource("/todo_fxml/TodoDetails.fxml"));
            // get the actual stage from ActionEvent
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root.load());
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SELECTION");
            alert.setHeaderText("Please select Item before trying to update!");
            alert.showAndWait();
        }

    }

}
