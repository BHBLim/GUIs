package com.wooppy.gui.groovyjavafx

import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.process.CoreLabelTokenFactory
import edu.stanford.nlp.process.PTBTokenizer
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

    /**
     * Initialize the controller. By default, all methods and variables are public
     * if nothing is specified
     */

    void initialize() {

        model = new TaggerModel()

        //Bindings for various UI components
        lineNumberMaxLabel.textProperty().bind(model.lineListSizeProperty.asString())
        queryNumberMaxLabel.textProperty().bind(model.queryListSizeProperty.asString())
        queryTextArea.editableProperty().bindBidirectional(editQueryToggleButton.selectedProperty())

        //Can't bind because it doesn't allow text area to be editable, using a change listener instead
        // queryTextArea.textProperty().bind(model.currentQueryTextProperty)
        //  queryNumberTextField.textProperty().bind(model.currentQueryIndexProperty.asString())

        //Bind changes to various properties when index changes
        //Also, displays starting from 1 not 0
        model.currentQueryIndexProperty.addListener(new ChangeListener() {
            @Override
            void changed(ObservableValue o, Object oldVal,
                         Object newVal) {
                model.currentQuery = model.queryList[(int) newVal]
                model.currentQueryTextProperty.set(model.currentQuery.currentText)
                queryNumberTextField.setText(((int) newVal + 1).toString())
            }
        })

        /**
         * Listen for changes to currentQueryText and save them to model and UI text
         */
        model.currentQueryTextProperty.addListener(new ChangeListener() {
            @Override
            void changed(ObservableValue o, Object oldVal,
                         Object newVal) {
                queryTextArea.setText(model.currentQueryTextProperty.get())
                model.currentQuery.setCurrentText(model.currentQueryTextProperty.get())
                tokenizeQuery()
            }
        })

    }

    TaggerModel model

    //region Methods to modify bound properties
    void increaseLineNumberMax(){
        model.lineListSizeProperty.setValue(model.lineListSizeProperty.getValue()+1)
    }

    void decreaseLineNumberMax(){
        model.lineListSizeProperty.setValue(model.lineListSizeProperty.getValue()-1)
    }

    void increaseQueryNumberMax(){
        model.lineListSizeProperty.setValue(model.queryListSizeProperty.getValue()+1)
    }

    void decreaseQueryNumberMax(){
        model.lineListSizeProperty.setValue(model.queryListSizeProperty.getValue()-1)
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

    //Index start from 1
    @FXML TextField queryNumberTextField
    @FXML Label queryNumberMaxLabel
    @FXML TextField lineNumberTextField
    @FXML Label lineNumberMaxLabel
    @FXML ToggleButton editQueryToggleButton
    @FXML ToggleButton editTokenToggleButton
    @FXML Button previousLineButton
    @FXML Button nextLineButton
    @FXML Button saveTokenEditsButton

    // End FXML injection

    /**
     * Load a query file and loads contents into memory.
     * Tokenization doesn't occur at this stage and is only done when a specific query is loaded.
     * @param actionEvent
     */
    void handleLoadQueryFileMenuItem(ActionEvent actionEvent) {
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
                            model.getQueryList().add(new TaggerModel.Query(queryBuffer))
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
        model.currentQuery = model.queryList[0]
        model.queryListSizeProperty.setValue(model.queryList.size())
        model.currentQueryTextProperty.set(model.currentQuery.getCurrentText())
        queryNumberTextField.setText("1")
    }

    void handleLoadEntityTagsMenuItem(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.setTitle("Open Entity Tags File")
        File file = fileChooser.showOpenDialog(topBox.getScene().getWindow())
        if (file!=null) {
            try {
                println "Opening " + file.toString()

            } catch (IOException e) {
                println "Could not open the file!"
                e.printStackTrace()
            }
        }
    }

    void saveQueryEdits() {
        model.currentQueryTextProperty.set(queryTextArea.getText())
    }

    /**
     * Pretag a list of tokens according to dictionary and excluded tokens
     */
    void pretag() {
        //TODO finish pretagging part
    }


    void handlePreviousQueryButton(ActionEvent actionEvent) {
        goToPreviousQuery()
    }

    void handleNextQueryButton(ActionEvent actionEvent) {
        goToNextQuery()
    }

    void handleTokenizeButton(ActionEvent actionEvent) {
        tokenizeQuery()
    }

    void handlePreviousLineButton(ActionEvent actionEvent) {}

    void handleNextLineButton(ActionEvent actionEvent) {}

    void handleSaveTokenEditsButton(ActionEvent actionEvent) {}

    void tokenizeQuery(){
        StringReader stringReader = new StringReader(model.currentQuery.currentText)
        PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(stringReader,
                new CoreLabelTokenFactory(), "")

        model.currentQuery.taggedTokens.clear()
        ptbt.each() { label->
            model.currentQuery.taggedTokens.add(new TaggerModel.TokenTagPair(token:label.toString()) )
            pretag()
        }
        model.currentQuery.taggedTokens.each(){
            println it.getToken()
        }
    }

    void goToPreviousQuery(){
        if (model.currentQueryTextProperty.get()!=queryTextArea.getText())
        {
            String saveAndResume = "Save, Tokenize and Resume"
            String saveAndProceed = "Save and Proceed"
            String discardChanges = "Discard Changes and Proceed"
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>(saveAndResume, saveAndProceed, discardChanges )
            choiceDialog.setContentText("You've made some changes to the query text but have not saved them. What do you want to do?")
            //Ask if you want to save edits
            choiceDialog.showAndWait().ifPresent() { response ->
                switch (response) {
                    case saveAndResume:
                        saveQueryEdits()
                        break
                    case saveAndProceed:
                        saveQueryEdits()
                        if (model.currentQueryIndexProperty.get() != 0) {
                            model.currentQueryIndexProperty.set(model.currentQueryIndexProperty.get() - 1)
                        }
                        break
                    case discardChanges:
                        if (model.currentQueryIndexProperty.get() != 0) {
                            model.currentQueryIndexProperty.set(model.currentQueryIndexProperty.get() - 1)
                        }
                        break
                    default:
                        break
                }
            }
        } else {
            if (model.currentQueryIndexProperty.get() != 0) {
                model.currentQueryIndexProperty.set(model.currentQueryIndexProperty.get() - 1)
            }
        }
    }

    void goToNextQuery(){
        if (model.currentQueryTextProperty.get()!=queryTextArea.getText())
        {
            String saveAndResume = "Save, Tokenize and Resume"
            String saveAndProceed = "Save and Proceed"
            String discardChanges = "Discard Changes and Proceed"
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>(saveAndResume, saveAndProceed, discardChanges )
            choiceDialog.setContentText("You've made some changes to the query text but have not saved them. What do you want to do?")
            //Ask if you want to save edits
            choiceDialog.showAndWait().ifPresent() { response ->
                switch (response) {
                    case saveAndResume:
                        model.currentQueryTextProperty.set(queryTextArea.getText())
                        break
                    case saveAndProceed:
                        model.currentQueryTextProperty.set(queryTextArea.getText())
                        int maxIndex = model.queryList.size() - 1
                        if (model.currentQueryIndexProperty.get() != maxIndex)
                            model.currentQueryIndexProperty.set(model.currentQueryIndexProperty.get() + 1)
                        break
                    case discardChanges:
                        int maxIndex = model.queryList.size() - 1
                        if (model.currentQueryIndexProperty.get() != maxIndex)
                            model.currentQueryIndexProperty.set(model.currentQueryIndexProperty.get() + 1)
                        break
                    default:
                        break
                }
            }
        } else {
            int maxIndex = model.queryList.size() - 1
            if (model.currentQueryIndexProperty.get() != maxIndex)
                model.currentQueryIndexProperty.set(model.currentQueryIndexProperty.get() + 1)
        }
    }
}
