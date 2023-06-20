package de.fronted.frontendjavafxspringboot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class Main extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(Main.class).run();
    }
    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
    @Override
    public void start(Stage stage) throws IOException {
        applicationContext.publishEvent(new StageReadyEvent(stage));
        Parent root = FXMLLoader.load(getClass().getResource("/todo_fxml/Dashboard.fxml"));
        stage.setScene(new Scene(root, 600,400));
        stage.show();
    }
}
