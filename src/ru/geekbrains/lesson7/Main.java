package ru.geekbrains.lesson7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat Window");
        primaryStage.setScene(new Scene(root, 200, 240));
        primaryStage.setMinHeight(150.0);
        primaryStage.setMinWidth(200.0);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
