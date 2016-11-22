package com.wooppy.gui.groovyjavafx

import groovy.beans.Bindable
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.MenuItem
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.stage.Window
import javafx.scene.Node

public class TaggerController {

    ArrayList<String> queryList = new ArrayList<String>()
    ArrayList<String> currentTokenList = new ArrayList<String>()
    String something = "Default"
    Integer lineNumberMax = 0

    IntegerProperty lineNumberMaxProperty = new SimpleIntegerProperty(0)


    @FXML
    void increaseLineNumberMax(){
        lineNumberMaxProperty.setValue(lineNumberMaxProperty.getValue()+1)

    }

    class MyModel {
        Integer test = 1
    }



    /**
     * Highest level component of the window
     */
    @FXML VBox topBox
    @FXML TextArea queryTextArea
    @FXML ListView tagList
    @FXML TextField queryNumberTextField
    @FXML Label queryNumberMaxLabel
    @FXML TextField lineNumberTextField
    @FXML Label lineNumberMaxLabel
    @FXML Button previousLineButton


    public void openQueryFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.setTitle("Open Query File")
        File file = fileChooser.showOpenDialog(topBox.getScene().getWindow())
        if (file!=null) {
            try {
                println "Opening " + file.toString()
                file
            } catch (IOException e) {
                println "Could not open the file!"
                e.printStackTrace()
            }
        }
    }

    public void openEntityTags(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.setTitle("Open Query File")
        File file = fileChooser.showOpenDialog(topBox.getScene().getWindow())
        if (file!=null) {
            try {
                println "Opening " + file.toString()
                file
            } catch (IOException e) {
                println "Could not open the file!"
                e.printStackTrace()
            }
        }
    }

    public void initialize() {

        //Bindings for various UI components
        lineNumberMaxLabel.textProperty().bind(lineNumberMaxProperty.asString());


    }


}
