/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wooppy.gui.groovyjavafx

import edu.stanford.nlp.ling.CoreLabel
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

/**
 *  This is the "model" for the tagger. This by right should have pretty much all the background
 *  data for the application to run.
 * @author Basil Lim
 */
class TaggerModel {

    //region Bound Properties

    StringProperty dictionaryFileLocationProperty = new SimpleStringProperty("")
    StringProperty inputFileLocationProperty = new SimpleStringProperty("")
    StringProperty entityTagsLocationProperty = new SimpleStringProperty("")

    //Index starts from 1
    IntegerProperty currentQueryIndexProperty = new SimpleIntegerProperty(0)
    //Index starts from 1
    IntegerProperty currentLineIndexProperty = new SimpleIntegerProperty(0)
    IntegerProperty currentTokenIndexProperty = new SimpleIntegerProperty(0)
    StringProperty currentQueryTextProperty = new SimpleStringProperty()

    /**
     * Bound to number of lines shown on UI
     */
    IntegerProperty lineListSizeProperty = new SimpleIntegerProperty(0)

    /**
     * Bound to number of queries shown on UI
     */
    IntegerProperty queryListSizeProperty = new SimpleIntegerProperty(0)
    //endregion

    /**
     * List of strings representing original string
     */
    ArrayList<String> allOriginalText = new ArrayList<String>()

    /**
    * List of queries loaded into file, after separating the queries
    */
    ArrayList<Query> queryList = new ArrayList<Query>()

    /**
     * Current query, which should always be a reference to the current
     * position in the queryList object
     */
    Query currentQuery

    /**
     * Load available tags into file
     */
    ArrayList<String> availableTags = new ArrayList<String>()

    /**
     * List of current tokens to be tagged, just after tokenization
     */
    ArrayList<TokenTagPair> currentTokenList = new ArrayList<TokenTagPair>()

    /**
     * Store some entity tag info to be mapped to
     */
    static class EntityTagInfo {
        String displayText
        String actualTag

        EntityTagInfo(String displayText, String actualTag) {
            this.displayText = displayText
            this.actualTag = actualTag
        }
    }

    HashMap<String, String> dictionary = new HashMap<String, String>()

    /**
     * Maps keycodes to entities
     */
    LinkedHashMap<String, EntityTagInfo> keyMap = new LinkedHashMap<String, EntityTagInfo>()

    /**
     * An inner class to store anything associated for a given tokenText
     */
    static class TokenTagPair {
        CoreLabel label
        /**
         * Could possibly make this multiple tags?
         * Right now I'm just using a single element for simplicity and our current use case.
         * TODO Look into making multi tags
         */
        ArrayList<EntityTagInfo> tagList = new ArrayList<EntityTagInfo>()
        Boolean isAmbiguous = false

        TokenTagPair(CoreLabel inputLabel) {
            this.label = inputLabel
            this.tagList.add(new EntityTagInfo("O:", "O"))
        }
    }

    /**
     * A wrapper class to store each individual query.
     */
    static class Query {
        String originalText
        /**
         * If there are no modifications to the original text this should be the same as originalText
         */
        String currentText

        ArrayList<TokenTagPair> taggedTokens = new ArrayList<TokenTagPair>()
        Boolean isTokenized = false
        Boolean isTagged = false
        Boolean hasAmbiguities = false

        Query(String originalText) {
            this.originalText = new String(originalText)
            this.currentText = new String(originalText)
        }
    }

    TaggerModel() {
        this.queryList = new ArrayList<Query>([new Query("Herro")])

        this.currentQuery = this.queryList.first()
    }
}

