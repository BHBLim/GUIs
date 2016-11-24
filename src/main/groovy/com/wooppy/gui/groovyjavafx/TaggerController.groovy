package com.wooppy.gui.groovyjavafx

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.FileChooser

/**
 * Handles application logic and the interaction between the UI and background model
 */
class TaggerController {
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
    @FXML
    ToggleButton editQueryToggleButton
    @FXML
    ToggleButton editTokenToggleButton

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
                    model.getAllOriginalText().add("")
                } else {
                    model.getAllOriginalText().add("")
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
                        queryBuffer += line + " \n"
                    else {
                        //Ignore if the buffer is all whitespace
                        if (!queryBuffer.isAllWhitespace()) {
                            //I'm assuming there's a "" at the end of each query, which should have been added anyway
                            model.getQueryList().add(new TaggerModel.Query(originalText: queryBuffer, currentText: queryBuffer))
                        }
                        queryBuffer = ''
                    }
                }

                resetAll()

            } catch (Exception e) {
                println "You done fecked up when loading the file..."
                println e.getMessage()
            }
        }
    }

    /**
     * Resets all query numbers etc. to the beginning
     */
    void resetAll() {

        model.currentQueryIndexProperty.set(0)
        model.currentLineIndexProperty.set(0)
        model.currentTokenIndexProperty.set(0)

        //Displays starting from 1
        model.queryNumberMaxProperty.setValue(model.queryList.size())
        model.currentQueryTextProperty.set(model.queryList.first().getCurrentText())

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
        queryTextArea.editableProperty().bindBidirectional(editQueryToggleButton.selectedProperty())

        //Can't bind because it doesn't allow text area to be editable, using a change listener instead
//        queryTextArea.textProperty().bind(model.currentQueryTextProperty)
//        queryNumberTextField.textProperty().bind(model.currentQueryIndexProperty.asString())

        //Bind currentQueryTextProperty and query Number to when index changes but not the other way round
        //Also, displays starting from 1 not 0
        model.currentQueryIndexProperty.addListener(new ChangeListener() {
            @Override
            void changed(ObservableValue o, Object oldVal,
                         Object newVal) {
                model.currentQueryTextProperty.set(model.queryList[(int) newVal].getCurrentText())
                queryNumberTextField.setText(((int) newVal + 1).toString())
            }
        })

        model.currentQueryTextProperty.addListener(new ChangeListener() {
            @Override
            void changed(ObservableValue o, Object oldVal,
                         Object newVal) {
                queryTextArea.setText(model.currentQueryTextProperty.get())
            }
        })

    }


    void selectPreviousQuery(ActionEvent actionEvent) {
        if (model.currentQueryIndexProperty.get() != 0) {
            model.currentQueryIndexProperty.set(model.currentQueryIndexProperty.get() - 1)
        }
    }

    void selectNextQuery(ActionEvent actionEvent) {
        int maxIndex = model.queryList.size() - 1
        if (model.currentQueryIndexProperty.get() != maxIndex)
            model.currentQueryIndexProperty.set(model.currentQueryIndexProperty.get() + 1)

    }

    void retokenizeQuery(ActionEvent actionEvent) {}

    void selectPreviousLine(ActionEvent actionEvent) {}
}
