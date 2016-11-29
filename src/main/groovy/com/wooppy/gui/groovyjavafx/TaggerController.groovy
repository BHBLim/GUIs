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
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import javafx.stage.FileChooser

import java.time.Instant

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

    String defaultKeyMap =
            '''##Comments are prepended by ##
##Each column is delimited by a tab
##You can bind the same tag to multiple keys
##Display Name\tActual Tag\tKey Code
PREFERENCE:\tI-PREFERENCE\t0
ITEM\tI-ITEM\t1
PRICE\tI-PRICE\t2
BRAND\tI-BRAND\t3
MAINTENANCE\tI-MAINTENANCECOST\t4
USEDNESS\tI-USEDNESS\t5
USEDON\tI-USEDON\t6
BATTERY\tI-ECBATTERY\t7
STEERING\tI-STEERINGSYSTEM\t8
AGE\tI-AGE\t9
BODYSTYLE\tI-BODYSTYLE\t10
TRANSM\tI-TRANSMISSION\t11
DOORS\tI-DOORNUMBER\t12
SIZE\tI-OVERALLSIZE\t13
USAGE\tI-USAGE\t14
USEDBY\tI-USEDBY\t15
WARRANTY\tI-WARRANTY\t16
PASSENGERS\tI-PASSENGERSIZE\t17
EXTERIOR\tI-EXTERIOR\t18
BRAKE\tI-BRAKE\t19
CATEGORY\tI-CATEGORY\t20
PERFORM\tI-GENERALPERFORMANCE\t21
SUSPENS\tI-SUSPENSION\t22
ENGINE\tI-ENGINE\t23
WHEELTYRE\tI-TYRES\t24
CARGO\tI-CARGOSIZE\t25
LUXURY\tI-LUXURY\t26
OFFROAD\tI-OFFROADCAPABILITY\t27
SAFETY\tI-SAFETY\t28
INTERIOR\tI-INTERIOR\t29
TOW\tI-TOWINGCAPACITY\t30
FUEL\tI-FUELTYPE\t31
LONGEVITY\tI-LONGEVITY\t32
EXTRA\tI-EXTRACAPABILITIES\t33
ECO\tI-ECO\t34
NOT\tI-NOT\t35
O\tO\tx'''

    String defaultSentence = "Hello I want to buy a car that is fast and fun to drive.\nPrice is not a problem"

    ArrayList<String> tagList = new ArrayList<String>()

    /**
     * Initialize the controller. By default, all methods and variables are public
     * if nothing is specified
     */
    void initialize() {
        model = new TaggerModel()

        model.queryList.clear()
        model.queryList.add(new TaggerModel.Query(defaultSentence))

        //Initialize default keymap
        defaultKeyMap.eachLine() { it ->
            readKeyMapLine(it)
        }

        model.keyMap.each() {
            tagFlowPane.getChildren().add(new Button(it.getKey() + ": " + it.getValue().getDisplayText()))
        }

        //Laziness is a sin, just populating buttons lazily right now
//        copyPaste.eachLine(){
//            ArrayList<String> parts = it.tokenize("\t")
//            try {
//                tagList.add(parts.first())
//            } catch (Exception e) {
//                println e.getMessage()
//            }
//        }
//
//        tagList.each(){
//            tagFlowPane.getChildren().add(new Button(it))
//        }

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
                if (!model.currentQuery.isTokenized) tokenizeQuery()
                else redrawWithoutTokenizing()
            }
        })

        tokenizeQuery()
        resetAllToStart()
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
    @FXML FlowPane tagFlowPane
    @FXML ScrollPane tokenTagPairScrollPane
    @FXML HBox tokenTagPairHBox
    @FXML
    TextFlow tokenizedTextFlow

    //Index starts from 1
    @FXML TextField queryNumberTextField
    @FXML Label queryNumberMaxLabel
    @FXML TextField lineNumberTextField
    @FXML ToggleButton editQueryToggleButton
    @FXML ToggleButton editTokenToggleButton
    @FXML Button previousLineButton
    @FXML Button nextLineButton
    @FXML Button saveTokenEditsButton
    @FXML TextArea tokenizedTextArea

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

                resetAllToStart()

            } catch (Exception e) {
                println "You done fecked up when loading the file..."
                println e.getMessage()
            }
        }
    }

    void handleLoadDictionaryFileMenuItem() {
        FileChooser fileChooser = new FileChooser()
        fileChooser.setTitle("Open Query File")
        File file = fileChooser.showOpenDialog(topBox.getScene().getWindow())
        if (file != null) {
            model.dictionary.clear()
            try {
                println "Opening " + file.toString()
                file.eachLine() { line ->
                    ArrayList<String> tokenizedLine = line.tokenize('\t')
                    model.dictionary.put(tokenizedLine[0], tokenizedLine[1])
                }
            } catch (IOException e) {
                println "Could not open the file!"
                e.printStackTrace()
            } catch (NullPointerException e) {
                println "Probably improper formatting..."
                e.printStackTrace()
            }
            model.dictionaryFileLocationProperty.set(file.toString())
            pretag()
        }
    }

    void readKeyMapLine(String line) {
        if (!line.startsWith(/##/)) {
            //Assuming the format is
            //        0   ->   1 -> 2+
            //DisplayName -> Tag -> KeyCode(s) separated by tab
            List<String> args = line.tokenize('\t')
            String displayName = args[0]
            String actualTag = args[1]
            Iterator<String> it = args.listIterator(2)
            it.forEachRemaining() {
                model.keyMap.put(it, new TaggerModel.EntityTagInfo(displayName, actualTag))
            }
        }
    }

    /**
     * Resets all query numbers etc. to the beginning
     */
    void resetAllToStart() {

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
        try {
            model.currentQuery.taggedTokens.eachWithIndex { it, int index ->
                it.tagList.clear()
                String dictionaryTag = model.dictionary.get(it.getLabel().value().trim())
                if (dictionaryTag != null) {
                    it.tagList.add(new TaggerModel.EntityTagInfo(dictionaryTag, dictionaryTag))
                } else {
                    it.tagList.add(new TaggerModel.EntityTagInfo("O", "O"))
                }
                tokenTagPairHBox.getChildren()[index].setTag(it.tagList[0].getDisplayText())
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
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

    void handlePretagButton(ActionEvent actionEvent) {
        pretag()
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
        }
        tokenTagPairHBox.getChildren().clear()

        tokenizedTextFlow.getChildren().clear()

        model.currentQuery.taggedTokens.eachWithIndex{ TaggerModel.TokenTagPair pair, int i ->
            tokenTagPairHBox.getChildren().add(new InteractiveTTBox(pair.getLabel().value(), i, "UNTAGGED:"))
        }

        pretag()

        StringBuilder sb = new StringBuilder()
        model.currentQuery.taggedTokens.each(){
            sb.append(it.getLabel().originalText() + " ")
//            println it.getLabel()
        }

//        tokenizedTextArea.setText(sb.toString())

        model.currentQuery.isTokenized = true

//        tokenTextField.setText(sb.toString())
    }

    void redrawWithoutTokenizing() {
        tokenTagPairHBox.getChildren().clear()
        tokenizedTextFlow.getChildren().clear()

        model.currentQuery.taggedTokens.eachWithIndex { TaggerModel.TokenTagPair pair, int i ->
            tokenTagPairHBox.getChildren().add(new InteractiveTTBox(pair.getLabel().value(), i, pair.getTagList()[0].getDisplayText()))
        }

        StringBuilder sb = new StringBuilder()
        model.currentQuery.taggedTokens.each() {
            sb.append(it.getLabel().originalText() + " ")
//            println it.getLabel()
        }


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

    /**
     * This will overwrite the file lul
     * @param actionEvent
     */
    void handleLousyDumpSave(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser()
        fileChooser.setTitle("Save dump to:")
        fileChooser.setInitialFileName("outputDump.txt")
        File file = fileChooser.showSaveDialog(topBox.getScene().getWindow())
        if (file != null) {
            try {
                file.delete()
                println "Saving " + file.toString()
                file << "Saved at " + Instant.now() + "\n"
                model.queryList.each() { query ->
                    query.taggedTokens.each() { pair ->
                        file << pair.getLabel().value() + "\t" + pair.getTagList().first().getActualTag() + "\n"
                    }
                }
            } catch (IOException e) {
                println "Could not save the file!"
                e.printStackTrace()
            }
        }

        pretag()
    }

    class InteractiveTTBox extends TokenTagBox {
        Text tokenText = new Text()

        TaggerModel.TokenTagPair tokenTagPair

        @Override
        void handleSelectedCheckbox(ActionEvent actionEvent) {
            isSelected = this.selectedCheckbox.isSelected()
        }

        @Override
        void handleSelectSingleButton(ActionEvent actionEvent) {
            //TODO implement focus here
        }

        @Override
        void handleTagCodeField(ActionEvent actionEvent) {
            //TODO handle the code inputs here
            TaggerModel.EntityTagInfo tagInfo = model.keyMap.get(this.tagCodeTextField.getText().trim())
            if (tagInfo != null) {
                this.setTag(tagInfo.displayText)
                this.tokenTagPair.tagList.clear()
                this.tokenTagPair.tagList.add(new TaggerModel.EntityTagInfo(tagInfo.displayText, tagInfo.actualTag))
                this.tagCodeTextField.setPromptText(this.tagCodeTextField.getText())
                if (tokenTagPairHBox.getChildren()[index + 1] != null) {
                    try {
                        tokenTagPairHBox.getChildren()[index + 1].tagCodeTextField.requestFocus()
                    } catch (Exception e) {
                        println e.getMessage()
                    }
                }
            }
            this.tagCodeTextField.clear()
        }

        @Override
        void handleEditTokenTextField(ActionEvent actionEvent) {
            println actionEvent.getEventType()
            String newText = this.editTokenTextField.getText()
            this.setToken(newText)
            this.tokenText.textProperty().set(newText)
            this.editTokenTextField.setPromptText("Edited")
            this.tokenTagPair.label.setOriginalText(newText)
            this.tokenTagPair.label.setValue(newText)
            this.editTokenTextField.clear()
        }

        //TODO More overrides for menuitems here

        InteractiveTTBox(String token, Integer index, String tag) {
            super(token, index, tag)
            this.tokenTagPair = model.currentQuery.taggedTokens[index]
            this.tokenText.textProperty().set(this.tokenTagPair.getLabel().originalText())

            tokenizedTextFlow.getChildren().add(this.tokenText)
            tokenizedTextFlow.getChildren().add(new Text(" "))

            this.tagCodeTextField.focusedProperty().addListener(new ChangeListener() {
                @Override
                void changed(ObservableValue o, Object oldVal,
                             Object newVal) {
                    if (newVal == true) {
                        tokenText.setFont(Font.font("System", FontWeight.BOLD, 12))
                        tokenText.setFill(Color.RED)
                        //TODO change scrollbar location according to focus
                    } else {
                        tokenText.fontProperty().set(Font.font("System", FontWeight.NORMAL, 12))
                        tokenText.setFill(Color.BLACK)
                    }
                }
            })
        }
    }
}
