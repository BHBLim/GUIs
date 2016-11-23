package com.wooppy.gui.pohutilities; /******************************************************************************
 *  com.wooppy.tagger.pohutilities.EntityTags.java [Created by C Y Poh 30 Oct 2016 @ 11:35am]
 *
 *  Compilation:  javac -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. ./com.wooppy.tagger.pohutilities.Parser.java
 *  Execution:    java -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. com.wooppy.tagger.pohutilities.Parser
 *
 *  A class that encapsulates all entity tags used, and their associated screen
 *  display colours.
 *
 *  The entity tags and their associated screen display colours are specified in
 *  a text file named entitytags.txt and placed in the Data directory.
 *
 *
 *
 *
 *
 *
 ******************************************************************************/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EntityTags {

    protected final String ANSI_ESC = "\u001B";
    protected final String ANSI_RESET = "\u001B[0m";

    protected final int DEFAULT_NUM_COLUMNS = 7;
    protected final int DEFAULT_NUM_CHARS = 110;

    protected List<String> allentitytagscolours = null;
    protected String[][] arrayofentitytagscolours = null;
    protected String[][] arrayofpaddedentitytagscolours = null;
    protected Map<String, Integer> mapofentitytags = new TreeMap<String, Integer>();
    protected Map<Integer, String> maprevofentitytags = new TreeMap<Integer, String>();

    public EntityTags(String inputfilename) {
        try {
            allentitytagscolours = Files.readAllLines(Paths.get(inputfilename));

            // System.out.println("Total number of lines: " + allentitytagscolours.size());

            arrayofentitytagscolours = new String[allentitytagscolours.size()][3];
            arrayofpaddedentitytagscolours = new String[allentitytagscolours.size()][2];

            int i = 0;
            for (String temp : allentitytagscolours) {
                String[] arrayoftempparts = temp.split(":");

                if (arrayoftempparts.length == 4) {
                    String entitytagdisplay = arrayoftempparts[0].trim();
                    String ansicolourcode = arrayoftempparts[1].trim();
                    String ansicolourdesc = arrayoftempparts[2].trim();
                    String entitytag = arrayoftempparts[3].trim();

                    arrayofentitytagscolours[i][0] = entitytagdisplay;
                    arrayofentitytagscolours[i][1] = ansicolourcode;
                    arrayofentitytagscolours[i][2] = ansicolourdesc;

                    mapofentitytags.putIfAbsent(entitytag, i);
                    maprevofentitytags.putIfAbsent(i, entitytag);

                    arrayofpaddedentitytagscolours[i][0] = entitytagdisplay;
                    arrayofpaddedentitytagscolours[i][1] = ansicolourcode;

                } else {
                    System.out.println("Line " + (i + 1) + " of file [" + inputfilename + "] is malformed. Exiting...");
                    System.exit(1);

                }

                i++;

            }

        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);

        }

    }

    public Map<String, Integer> getEntityTags() {
        return mapofentitytags;
    }

    public Map<Integer, String> getRevEntityTags() {
        return maprevofentitytags;
    }


    public String[][] getArrayofEntityTagsColours() {
        return arrayofentitytagscolours;
    }

    public List<String> displayEntityTags() {

        Double dtemp = Math.ceil(DEFAULT_NUM_CHARS / DEFAULT_NUM_COLUMNS);
        int charspercolumn = dtemp.intValue();

        int numcolumns = DEFAULT_NUM_COLUMNS;

        List<String> entitytaglines = new ArrayList<String>();

        // System.out.println("Number of chars per column: " + numcharspercolumn);

        boolean redoflag = false;
        do {
            redoflag = false;

            for (int i = 0; i < arrayofentitytagscolours.length; i++) {
                String entitytagdisplay = arrayofentitytagscolours[i][0];
                String ansicolourcode = arrayofentitytagscolours[i][1];
                String ansicolourdesc = arrayofentitytagscolours[i][2];

                entitytagdisplay = entitytagdisplay + "(" + i + ") ";
                int itemp = entitytagdisplay.length();

                // System.out.println(ANSI_ESC + ansicolourcode + entitytagdisplay + ANSI_RESET);

                if (itemp > charspercolumn) {
                    numcolumns = numcolumns - 1;
                    if (numcolumns < 0) {
                        System.out.println("An entity tag is malformed. Exiting...");
                        System.exit(1);

                    }

                    dtemp = Math.ceil(DEFAULT_NUM_CHARS / numcolumns);
                    charspercolumn = dtemp.intValue();

                    redoflag = true;
                    break;
                } else {
                    arrayofpaddedentitytagscolours[i][0] = String.format("%1$-" + charspercolumn + "s", entitytagdisplay);

                    // System.out.println(ANSI_ESC + ansicolourcode + arrayofpaddedentitytagscolours[i][0] + ANSI_RESET);

                }


            }

        } while (redoflag);

        String stemp = "";
        for (int i = 0; i < arrayofpaddedentitytagscolours.length; i++) {
            stemp = stemp + ANSI_ESC + arrayofpaddedentitytagscolours[i][1] + arrayofpaddedentitytagscolours[i][0] + ANSI_RESET;

            if ((i % numcolumns) == (numcolumns - 1)) {
                entitytaglines.add(stemp);
                System.out.println(stemp);

                stemp = "";

            }

            if (i == (arrayofpaddedentitytagscolours.length - 1)) {
                entitytaglines.add(stemp);
                System.out.println(stemp);

                stemp = "";

            }


        }

        return entitytaglines;
    }

}
