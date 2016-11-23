package com.wooppy.gui.pohutilities; /******************************************************************************
 *  com.wooppy.tagger.pohutilities.Formatter.java
 *  [Created by C Y Poh 19 Nov 2016 @ 6:00pm]
 *
 *  Compilation:  javac -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. ./Tagger.java
 *  Execution:    java -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. Tagger
 *
 *  The Java com.wooppy.tagger.pohutilities.Formatter class contains all fields and methods associated with
 *  token/tag formatting functionality.
 *
 *
 *
 *
 *
 ******************************************************************************/

import java.util.Map;


public class Formatter {

    protected static final String DATA_SEPARATOR = "\t\t";
    protected static final String NOTAG_TAG = "O";


    public static String getMarker(String token) {
        Double dtemp = Math.ceil(token.length() / 2);
        int itemp = dtemp.intValue();

        String blankpreline = "";
        for (int j = 0; j < itemp; j++) {
            blankpreline = blankpreline + " ";
        }

        String blankpostline = "";
        for (int k = 0; k < (token.length() - itemp - 1); k++) {
            blankpostline = blankpostline + " ";
        }

        return (blankpreline + "^" + blankpostline);

    }


    public static String updateTag(String tokentag, int tag) {

        String beginpart = tokentag.substring(0, (tokentag.indexOf("(") + 1));
        String endpart = ")";

        String updatedtokentag = beginpart + String.format("%02d", tag) + endpart;

        if (updatedtokentag.length() < tokentag.length()) {
            String blankline = "";
            for (int j = 0; j < (tokentag.length() - updatedtokentag.length()); j++) {
                blankline = blankline + " ";
            }

            updatedtokentag = updatedtokentag + blankline;
        }

        return updatedtokentag;

    }


    public static String updateTag(String tokentag, String dashdash) {

        String beginpart = tokentag.substring(0, (tokentag.indexOf("(") + 1));
        String endpart = ")";

        String updatedtokentag = beginpart + dashdash + endpart;

        if (updatedtokentag.length() < tokentag.length()) {
            String blankline = "";
            for (int j = 0; j < (tokentag.length() - updatedtokentag.length()); j++) {
                blankline = blankline + " ";
            }

            updatedtokentag = updatedtokentag + blankline;
        }

        return updatedtokentag;

    }


    public static String getTokenTag(String tokentag) {

        String result = "";

        if (!tokentag.trim().isEmpty()) {

            if (tokentag.contains("(")) {

                String token = tokentag.substring(0, tokentag.indexOf("("));
                String tag = tokentag.substring((tokentag.indexOf("(") + 1), tokentag.indexOf(")"));

                result = token + DATA_SEPARATOR + tag;

            } else {
                System.out.println("No tag. Exiting...");
                System.exit(1);

            }

        }

        return result;

    }


    public static String getTag(String tokentag) {

        String result = "";

        if (!tokentag.trim().isEmpty()) {

            if (tokentag.contains("(")) {

                String token = tokentag.substring(0, tokentag.indexOf("("));
                String tag = tokentag.substring((tokentag.indexOf("(") + 1), tokentag.indexOf(")"));

                result = tag;

            } else {
                System.out.println("No tag. Exiting...");
                System.exit(1);

            }

        }

        return result;

    }

    public static String getToken(String tokentag) {

        String result = "";

        if (!tokentag.trim().isEmpty()) {

            if (tokentag.contains("(")) {

                String token = tokentag.substring(0, tokentag.indexOf("("));
                String tag = tokentag.substring((tokentag.indexOf("(") + 1), tokentag.indexOf(")"));

                result = token;

            } else {
                System.out.println("No tag. Exiting...");
                System.exit(1);

            }

        }

        return result;

    }


    public static String getTokenTag(Map<Integer, String> maprevofentitytags, String tokentag) {

        String result = "";

        if (!tokentag.trim().isEmpty()) {

            if (tokentag.contains("(")) {

                String token = tokentag.substring(0, tokentag.indexOf("("));
                String tag = tokentag.substring((tokentag.indexOf("(") + 1), tokentag.indexOf(")"));

                if (!tag.equals("--")) {
                    try {
                        int itemp2 = Integer.parseInt(tag);

                        result = token + DATA_SEPARATOR + maprevofentitytags.get(itemp2);

                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid tag. Exiting...");
                        System.exit(1);

                    }
                } else {
                    result = token + DATA_SEPARATOR + NOTAG_TAG;

                }

            } else {
                result = tokentag + DATA_SEPARATOR + NOTAG_TAG;

            }

        }

        return result;

    }


} // END: com.wooppy.tagger.pohutilities.Formatter class.
// ***********************************************


// Codes for debugging:

// System.out.println("setTokenTag - token: " + token);
// System.out.println("setTokenTag - tag: " + tag);
