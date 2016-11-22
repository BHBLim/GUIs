package com.wooppy.gui.groovyjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(getClass());
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 700, 350));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(Main.class, args);
    }
}
