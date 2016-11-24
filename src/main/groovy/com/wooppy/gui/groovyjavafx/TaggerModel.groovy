/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wooppy.gui.groovyjavafx

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

    IntegerProperty currentQueryIndexProperty = new SimpleIntegerProperty(0)
    IntegerProperty currentLineIndexProperty = new SimpleIntegerProperty(0)
    IntegerProperty currentTokenIndexProperty = new SimpleIntegerProperty(0)

    StringProperty currentQueryTextProperty = new SimpleStringProperty()

    /**
     * Bound to number of lines shown on UI
     */
    IntegerProperty lineNumberMaxProperty = new SimpleIntegerProperty(0)

    /**
     * Bound to number of queries shown on UI
     */
    IntegerProperty queryNumberMaxProperty = new SimpleIntegerProperty(0)
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
     * List of e
     */

    /**
     * Load available tags into file
     */
    ArrayList<String> availableTags = new ArrayList<String>()

    /**
     * List of current tokens to be tagged, just after tokenization
     */
    ArrayList<TokenTagPair> currentTokenList = new ArrayList<TokenTagPair>()

    /**
     * An inner class to store anything associated for a given token
     */
    static class TokenTagPair {
        String token
        /**
         * Could possibly make this multiple tags?
         * TODO Look into making multi tags
         */
        String tag
        Boolean isAmbiguous = false
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
        ArrayList<TokenTagPair> taggedTokens
        Boolean isTagged = false
        Boolean hasAmbiguities = false
    }


}

