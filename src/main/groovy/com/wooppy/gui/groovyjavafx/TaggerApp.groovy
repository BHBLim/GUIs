package com.wooppy.gui.groovyjavafx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

public class TaggerApp extends Application {

     @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(getClass());

        Parent root = null

         FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("tagger.fxml"))

         root = (Parent)loader.load()

         TaggerController controller = (TaggerController)loader.getController()
        primaryStage.setTitle("Tagger GUI");
        primaryStage.setScene(new Scene(root, 900, 650));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(TaggerApp.class, args);
    }
}
