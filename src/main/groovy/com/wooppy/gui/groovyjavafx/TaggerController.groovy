package com.wooppy.gui.groovyjavafx

import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.process.CoreLabelTokenFactory
import edu.stanford.nlp.process.PTBTokenizer
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.FileChooser

/**
 * Handles application logic and the interaction between the UI and background model
 */
class TaggerController {

    //I am lazy so here are the entity tag things
    String copyPaste = """
PREFERENCE:\t[31;47m:\t\tRED_ON_WHITE:\t\tI-PREFERENCE
ITEM:\t\t[38;5;170m:\t\tPINK:\t\t\tI-ITEM
PRICE:\t\t[35;47m:\t\tPURPLE_ON_WHITE:\tI-PRICE
BRAND:\t\t[36;47m:\t\tCYAN_ON_WHITE:\t\tI-BRAND
MAINTENANCE:\t[32m:\t\t\tGREEN:\t\t\tI-MAINTENANCECOST
USEDNESS:\t[34;47m:\t\tBLUE_ON_WHITE:\t\tI-USEDNESS
USEDON:\t\t[33;47m:\t\tYELLOW_ON_WHITE:\tI-USEDON
BATTERY:\t[37;44m:\t\tWHITE_ON_BLUE:\t\tI-ECBATTERY
STEERING:\t[38;5;130m:\t\tORANGE:\t\t\tI-STEERINGSYSTEM
AGE:\t\t[34m:\t\t\tBLUE:\t\t\tI-AGE
BODYSTYLE:\t[33m:\t\t\tYELLOW:\t\t\tI-BODYSTYLE
TRANSM:\t\t[37m:\t\t\tWHITE:\t\t\tI-TRANSMISSION
DOORS:\t\t[31;40m:\t\tRED_ON_BLACK:\t\tI-DOORNUMBER
SIZE:\t\t[38;5;160m:\t\tDARKORANGE:\t\tI-OVERALLSIZE
USAGE:\t\t[33;40m:\t\tYELLOW_ON_BLACK:\tI-USAGE
USEDBY:\t\t[32;47m:\t\tGREEN_ON_WHITE:\t\tI-USEDBY
WARRANTY:\t[37;43m:\t\tWHITE_ON_YELLOW:\tI-WARRANTY
PASSENGERS:\t[38;5;150m:\t\tBIEGE:\t\t\tI-PASSENGERSIZE
EXTERIOR:\t[37;46m:\t\tWHITE_ON_CYAN:\t\tI-EXTERIOR
BRAKE:\t\t[30m:\t\t\tBLACK:\t\t\tI-BRAKE
CATEGORY:\t[32;40m:\t\tGREEN_ON_BLACK:\t\tI-CATEGORY
PERFORM:\t[37;41m:\t\tWHITE_ON_RED:\t\tI-GENERALPERFORMANCE
SUSPENS:\t[38;5;100m:\t\tOLIVE:\t\t\tI-SUSPENSION
ENGINE:\t\t[36m:\t\t\tCYAN:\t\t\tI-ENGINE
WHEELTYRE:\t[38;5;120m:\t\tLIGHTGREEN:\t\tI-TYRES
CARGO:\t\t[38;5;140m:\t\tLAVENDER:\t\tI-CARGOSIZE
LUXURY:\t\t[31m:\t\t\tRED:\t\t\tI-LUXURY
OFFROAD:\t[35;40m:\t\tPURPLE_ON_BLACK:\tI-OFFROADCAPABILITY
SAFETY:\t\t[34;40m:\t\tBLUE_ON_BLACK:\t\tI-SAFETY
INTERIOR:\t[37;42m:\t\tWHITE_ON_GREEN:\t\tI-INTERIOR
TOW:\t\t[36;40m:\t\tCYAN_ON_BLACK:\t\tI-TOWINGCAPACITY
FUEL:\t\t[30;47m:\t\tBLACK_ON_WHITE:\t\tI-FUELTYPE
LONGEVITY:\t[35m:\t\t\tPURPLE:\t\t\tI-LONGEVITY
EXTRA:\t\t[37;45m:\t\tWHITE_ON_PURPLE:\tI-EXTRACAPABILITIES
ECO:\t\t[37;40m:\t\tWHITE_ON_BLACK:\t\tI-ECO
NOT:\t\t[38;5;10m:\t\tLIME_GREEN:\t\tI-NOT"""

    ArrayList<String> tagList = new ArrayList<String>()
    /**
     * Initialize the controller. By default, all methods and variables are public
     * if nothing is specified
     */
    void initialize() {

        model = new TaggerModel()

        //Laziness is a sin, just populating buttons lazily right now
        copyPaste.eachLine(){
            ArrayList<String> parts = it.tokenize("\t")
            try {
                tagList.add(parts.first())
            } catch (Exception e) {
            }
        }

        tagList.each(){
            tagFlowPane.getChildren().add(new Button(it))
        }

        //Bindings for various UI components
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

        //Adjust width of tokens text field according to text
        tokenTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ob, String o,
                                String n) {
                // expand the textfield
                Text tempText = new Text(n)
                tempText.setFont(Font.font("Monospaced", 13))
                def width = tempText.getLayoutBounds().getWidth()
                tokenTextField.setPrefWidth(width + 10);
            }
        });

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

    //region FXML dependency injection
    /**
     * Highest level component of the window
     */
    @FXML VBox topBox
    @FXML TextArea queryTextArea
    @FXML ListView tagListView
    @FXML FlowPane tagFlowPane ;

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
    @FXML
    TextField tokenTextField

    //endregion

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

    /**
     * Tokenize currently stored query
     */
    void tokenizeQuery(){
        StringReader stringReader = new StringReader(model.currentQuery.currentText)
        PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(stringReader,
                new CoreLabelTokenFactory(), "invertible,normalizeCurrency=false,tokenizeNLs=true")

        model.currentQuery.taggedTokens.clear()
        ptbt.each() { label->
            model.currentQuery.taggedTokens.add(new TaggerModel.TokenTagPair(label))
            pretag()
        }

        StringBuilder sb = new StringBuilder()
        model.currentQuery.taggedTokens.each(){
            sb.append(it.getLabel().originalText() + "  ")
            println it.getLabel()
        }

        tokenTextField.setText(sb.toString())
    }

    void goToPreviousQuery(){
        if (model.currentQueryTextProperty.get()!=queryTextArea.getText())
        {
            String saveAndResume = "Save, Tokenize and Resume"
            String saveAndProceed = "Save and Proceed"
            String discardChanges = "Discard Changes and Proceed"
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>(saveAndResume, saveAndProceed, discardChanges )
            choiceDialog.setContentText("You've made some changes to the query text but have not saved them.\nWhat do you want to do?")
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
            choiceDialog.setContentText("You've made some changes to the query text but have not saved them.\nWhat do you want to do?")
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
