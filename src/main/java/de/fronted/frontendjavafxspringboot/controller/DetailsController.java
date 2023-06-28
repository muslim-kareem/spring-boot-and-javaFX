package de.fronted.frontendjavafxspringboot.controller;

import de.fronted.frontendjavafxspringboot.todo.Todo;
import de.fronted.frontendjavafxspringboot.todo.TodoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class DetailsController implements Initializable {

    static String id = null;
    private TodoService todoService = new TodoService();
    private Todo todoToUpdate = todoService.getTodoById(id);

    @FXML
    private TextField updateTextField;

    @FXML
    private Button saveButton;
    @FXML
    private MenuItem openItem, inProgressButton, doneButton;
    @FXML
    private MenuButton statusButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTextField.setText(todoToUpdate.getDescription());
        statusButton.setText(todoToUpdate.getStatus().toString());


        updateTextField.setOnKeyTyped(event -> {
            String input = updateTextField.getText();
            todoToUpdate.setDescription(input);
        });
    }

    public void saveTodo(ActionEvent e) throws IOException {
        this.todoService.updateTodo(todoToUpdate);
        FXMLLoader root = new FXMLLoader(getClass().getResource("/todo_fxml/Dashboard.fxml"));
        // get the actual stage from ActionEvent
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }

    public void setStatus(ActionEvent event) {
        var selectedMenuItem = (MenuItem) event.getSource();
        todoToUpdate.setStatusAsString(selectedMenuItem.getText());
        statusButton.setText(selectedMenuItem.getText());
    }

}
