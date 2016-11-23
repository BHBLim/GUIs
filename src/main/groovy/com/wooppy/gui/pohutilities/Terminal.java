package com.wooppy.gui.pohutilities; /******************************************************************************
 *  com.wooppy.tagger.pohutilities.Terminal.java
 *  [Created by C Y Poh 19 Nov 2016 @ 1:07am]
 *
 *  Compilation:  javac -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. ./Tagger.java
 *  Execution:    java -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. Tagger
 *
 *  The Java com.wooppy.tagger.pohutilities.Terminal class contains all fields and methods associated with a
 *  virtual terminal of TERMINAL_WIDTH characters and TERMINAL_ROWS rows.
 *
 *  The class includes methods to handle user inputs.
 *
 *
 *
 *
 ******************************************************************************/

import java.util.List;


public class Terminal {
    protected final static int TERMINAL_WIDTH = 110;
    protected final static int TERMINAL_ROWS = 40;
    protected final static int ZERO_POSITION = 9;
    protected final static int ACTIVE_ROWS = 7;
    protected final static int DISPLAY_WIDTH = TERMINAL_WIDTH - (ZERO_POSITION + 1);
    protected final static int DISPLAY_ROWS = TERMINAL_ROWS - ACTIVE_ROWS;
    protected final String ANSI_ESC = "\u001B";
    protected final String ANSI_RESET = "\u001B[0m";
    protected final String ANSI_MARKER_COLOUR_CODE = "[30;43m";
    protected final String ANSI_BAR_COLOUR_CODE = "[40m";
    protected final String ANSI_SENTENCEID_COLOUR_CODE = "[37;45m";
    protected final String CLEAR_SCREEN_CODE = "\033[H\033[2J";
    List<String> entitytaglines = null;


    public Terminal(List<String> entitytaglines) {
        this.entitytaglines = entitytaglines;

    }


    private String drawMarkerBar(int position, String marker) {

        String indent = "";
        String blankpreline = "";
        String blankpostline = "";
        String barline = "";

        for (int p = 0; p < ZERO_POSITION; p++) {
            indent = indent + " ";
        }

        if (!marker.isEmpty() & position >= 0 & position <= (DISPLAY_WIDTH - marker.length())) {

            blankpreline = "";
            for (int j = 0; j < position; j++) {
                blankpreline = blankpreline + " ";
            }

            blankpostline = "";
            for (int k = 0; k < (DISPLAY_WIDTH - marker.length() - position); k++) {
                blankpostline = blankpostline + " ";
            }

            barline = indent + ANSI_ESC + ANSI_BAR_COLOUR_CODE + blankpreline + ANSI_ESC + ANSI_MARKER_COLOUR_CODE + marker + ANSI_ESC + ANSI_BAR_COLOUR_CODE + blankpostline + ANSI_RESET;

        }

        return barline;

    }


    private String drawUpdateBar(String[] arrayofparsedtokens, String[] arrayofdeltas) {

        String indent = "";

        for (int p = 0; p < ZERO_POSITION; p++) {
            indent = indent + " ";
        }

        String barline = indent;
        String blankline = "";
        int position = 0;

        for (int j = 0; j < arrayofparsedtokens.length; j++) {
            String parsedtoken = arrayofparsedtokens[j];
            String delta = arrayofdeltas[j];

            if (delta == null) {

                blankline = "";
                for (int k = 0; k < parsedtoken.length(); k++) {
                    blankline = blankline + " ";
                }
                blankline = blankline + " ";

                barline = barline + ANSI_ESC + ANSI_BAR_COLOUR_CODE + blankline + ANSI_RESET;

            } else if (delta.isEmpty()) {

                blankline = "";
                for (int k = 0; k < parsedtoken.length(); k++) {
                    blankline = blankline + " ";
                }
                blankline = blankline + " ";

                barline = barline + ANSI_ESC + ANSI_BAR_COLOUR_CODE + blankline + ANSI_RESET;


            } else {

                barline = barline + ANSI_ESC + ANSI_MARKER_COLOUR_CODE + delta + ANSI_RESET + ANSI_ESC + ANSI_BAR_COLOUR_CODE + " " + ANSI_RESET;

            }

            position = position + parsedtoken.length() + 1;

        }

        if (position < DISPLAY_WIDTH) {
            blankline = "";
            for (int p = 0; p < (DISPLAY_WIDTH - position); p++) {
                blankline = blankline + " ";
            }

            barline = barline + ANSI_ESC + ANSI_BAR_COLOUR_CODE + blankline + ANSI_RESET;

        }

        return barline;

    }


    public void drawScreen(int sentenceid, List<String> preparsedlines, List<String> parsedlines) {

        int sizepreparsed = preparsedlines.size();
        int sizeparsed = parsedlines.size();

        int sizeentitytags = entitytaglines.size();

        int sizeblank = (DISPLAY_ROWS - 2) - sizeentitytags - sizepreparsed - sizeparsed;

        if (sizeblank < 0) {
            System.out.println("The number of rows for the current com.wooppy.tagger.pohutilities.Terminal = " + DISPLAY_ROWS + " is insufficient. Exiting...");
            System.exit(1);

        }

        System.out.print(CLEAR_SCREEN_CODE); // Clear screen.
        System.out.flush();

        System.out.println(ANSI_ESC + ANSI_SENTENCEID_COLOUR_CODE + "Sentence #" + sentenceid + "... " + ANSI_RESET);

        for (int i = 0; i < sizeblank; i++) {
            System.out.println("");
        }

        int j = 0;
        for (String tempj : preparsedlines) {
            System.out.println(tempj);

            j++;

        }

        System.out.println(" ");

        int k = 0;
        for (String tempk : parsedlines) {
            System.out.println(tempk);

            k++;

        }

        System.out.println(" ");

        int l = 0;
        for (String templ : entitytaglines) {
            System.out.println(templ);

            l++;

        }

        System.out.println(" ");

    }


    public void drawScreen(int sentenceid, List<String> preparsedlines, List<String> parsedlines,
                           String currentline,
                           int position, String marker,
                           String[] arrayofparsedtokens, String[] arrayofdeltas) {

        drawScreen(sentenceid, preparsedlines, parsedlines);

        System.out.println(currentline);

        String stemp = drawMarkerBar(position, marker);
        System.out.println(stemp);

        stemp = drawUpdateBar(arrayofparsedtokens, arrayofdeltas);
        System.out.println(stemp);

    }


} // END: com.wooppy.tagger.pohutilities.Terminal class.
// ***********************************************
