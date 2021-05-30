package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
public class GuiView extends Application implements Runnable{
    public static void main(String[] args) {
        launch(args);
    }
    public void run(){}
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("test.fxml"));
        Scene newScene = new Scene(root, 1000, 900);
        primaryStage.setTitle("Test");
        primaryStage.setScene(newScene);
        primaryStage.show();
    }
}