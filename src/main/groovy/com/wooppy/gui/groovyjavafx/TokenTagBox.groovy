package com.wooppy.gui.groovyjavafx

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

/**
 * Created by Basil Lim on 28/11/2016.
 */

class TokenTagBox extends HBox {
    @FXML CheckBox selectedCheckbox
    @FXML Button selectSingleButton
    @FXML Label tokenIndexLabel
    @FXML Label tokenLabel
    @FXML TextField editTokenTextField
    @FXML TextField tagCodeTextField
    @FXML Label tagLabel
    @FXML MenuItem insertTokenAfterMenuItem
    @FXML MenuItem insertTokenBeforeMenuItem
    @FXML MenuItem deleteTokenMenuItem

    SimpleIntegerProperty tokenIndexProperty = new SimpleIntegerProperty(0)
    SimpleStringProperty tokenStringProperty = new SimpleStringProperty("")
    SimpleStringProperty tagStringProperty = new  SimpleStringProperty("")

    boolean isSelected = false

    void setToken(String token) {
        this.tokenStringProperty.set(token)
    }

    String getToken() {
        return this.tokenStringProperty.get()
    }

    void setTag(String tag) {
        this.tagStringProperty.set(tag)
    }

    String getTag() {
        return this.tagStringProperty.getText()
    }

    void setIndex(Integer index) {
        this.tokenIndexProperty.set(index)
    }

    Integer getIndex() {
        return this.tokenIndexProperty.get()
    }

    void handleSelectedCheckbox(ActionEvent actionEvent) {
    }

    void handleSelectSingleButton(ActionEvent actionEvent) {
    }

    void handleTagCodeField(ActionEvent actionEvent) {}

    void handleEditTokenTextField(ActionEvent actionEvent) {}

    TokenTagBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("tokenTagBox.fxml"))
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
// specified in fxml already?
        try {
            fxmlLoader.load()
        } catch (IOException e) {
            throw new RuntimeException(e)
        }


    }

    TokenTagBox(String token, Integer index, String tag) {
        this()
        this.setToken(token)
        this.setIndex(index)
        this.setTag(tag)

        this.tokenLabel.textProperty().bind(this.tokenStringProperty)
        this.tokenIndexLabel.textProperty().bind(this.tokenIndexProperty.asString())
        this.tagLabel.textProperty().bind(this.tagStringProperty)
    }

}
