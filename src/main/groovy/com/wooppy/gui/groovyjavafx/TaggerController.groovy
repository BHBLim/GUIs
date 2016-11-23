package com.wooppy.gui.groovyjavafx

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.FileChooser

/**
 * Handles application logic and the interaction between the UI and background model
 */
public class TaggerController {
    TaggerModel model


    //region Methods to modify bound properties
    void increaseLineNumberMax(){
        model.lineNumberMaxProperty.setValue(model.lineNumberMaxProperty.getValue()+1)
    }

    void decreaseLineNumberMax(){
        model.lineNumberMaxProperty.setValue(model.lineNumberMaxProperty.getValue()-1)
    }

    void increaseQueryNumberMax(){
        model.lineNumberMaxProperty.setValue(model.queryNumberMaxProperty.getValue()+1)
    }

    void decreaseQueryNumberMax(){
        model.lineNumberMaxProperty.setValue(model.queryNumberMaxProperty.getValue()-1)
    }

    //endregion

    //----------------FXML dependency injection part with the @FXML annotation------------//
    //Each par
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
    // End FXML injection

    /**
     * Load a query file and loads contents into memory.
     * Tokenization doesn't occur at this stage and is only done when a specific query is loaded.
     * @param actionEvent
     */
    void loadQueryFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.setTitle("Open Query File")
        File file = fileChooser.showOpenDialog(topBox.getScene().getWindow())
        if (file!=null) {
            try {
                println "Opening " + file.toString()
                model.getAllOriginalText().clear()
                file.eachLine(){ it ->
                    model.getAllOriginalText() << it
                }

                if (model.getAllOriginalText().last().trim().isEmpty()) {
                    model.getAllOriginalText().dropRight(1)
                    model.getAllOriginalText().add("");
                } else {
                    model.getAllOriginalText().add("");
                }
            } catch (IOException e) {
                println "Could not open the file!"
                e.printStackTrace()
            }

            try {
                model.queryList.clear()
                def queryBuffer = new String()
                model.getAllOriginalText().each() { line ->
                    if (!line.isAllWhitespace())
                        queryBuffer += line + " "
                    else {
                        //Ignore if the buffer is all whitespace
                        if (!queryBuffer.isAllWhitespace()) {
                            //I'm assuming there's a "" at the end of each query, which should have been added anyway
                            model.getQueryList().add(new TaggerModel.Query(originalText: queryBuffer))
                        }
                        queryBuffer = ''
                    }
                }
                model.setCurrentQueryIndex(0)
                model.setCurrentLineIndex(0)
                model.setCurrentTokenIndex(0)
                println model.queryList.first().getOriginalText()
                model.setCurrentQueryText(model.queryList.first().getOriginalText())
                model.queryNumberMaxProperty.setValue(model.queryList.size()-1)
                updateQueryTextFieldFromModel()

            } catch (Exception e) {
                println "You done fecked up when loading the file..."
                println e.getMessage()
            }
        }
    }

    void loadEntityTagsFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.setTitle("Open Entity Tags File")
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

    void updateQueryTextFieldFromModel() {
        queryTextArea.setText(model.getCurrentQueryText())
    }

    void saveQueryEdits() {

    }

    /**
     * Pretag a list of tokens according to dictionary and excluded tokens
     * @param tokens
     */
    static void pretag(ArrayList<TaggerModel.TokenTagPair> tokens) {

    }

    /**
     * Initialize the controller. By default, all methods and variables are public
     * if nothing is specified
     */
    void initialize() {

        model = new TaggerModel()

        //Bindings for various UI components
        lineNumberMaxLabel.textProperty().bind(model.lineNumberMaxProperty.asString())
        queryNumberMaxLabel.textProperty().bind(model.queryNumberMaxProperty.asString())

    }


}
