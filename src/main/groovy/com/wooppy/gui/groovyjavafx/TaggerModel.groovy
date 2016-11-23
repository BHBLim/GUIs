/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wooppy.gui.groovyjavafx

/**
 *
 * @author lisab
 */
class TaggerModel {
    
    /**
    * List of queries loaded into file, delimited by double newline (Hopefully)
    */
    ArrayList<String> queryList = new ArrayList<String>()

    ArrayList<ArrayList<TokenTagPair>> savedTags = new ArrayList<ArrayList<TokenTagPair>>()

    ArrayList<String> availableTags = new ArrayList<String>()

    /**
     * List of current tokens to be tagged, just after tokenization
     */
    ArrayList<TokenTagPair> currentTokenList = new ArrayList<TokenTagPair>()





    class TokenTagPair {
        String token

        /**
         * Could possibly make this multiple tags?
         * TODO Look into making multi tags
         */
        String tag
    }
}

