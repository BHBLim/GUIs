package com.wooppy.gui.pohutilities; /******************************************************************************
 *  com.wooppy.tagger.pohutilities.LookupDictionary.java [Created by C Y Poh 31 Oct 2016 @ 5:39pm]
 *
 *  Compilation:  javac -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. ./com.wooppy.tagger.pohutilities.Parser.java
 *  Execution:    java -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. com.wooppy.tagger.pohutilities.Parser
 *
 *  A class that encapsulates all entity-tagged tokens in the form of
 *  (token)-(entity-tag) pairs. These entity-tagged tokens are found in a text
 *  file named dictionary.txt and placed in the Data directory.
 *
 *  This class is used to pre-tag new training data
 *
 *
 *
 *
 *
 ******************************************************************************/

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LookupDictionary {
    protected final String ANSI_ESC = "\u001B";
    protected final String ANSI_RESET = "\u001B[0m";

    protected final String NOTAG = "O";
    protected final String DATA_SEPARATOR = "\t\t";

    protected final String DEFAULT_BADTOKENTAGPAIRS_FILENAME = "Data/badtokentagpairs.txt";

    protected String dictionaryfilename = "";

    protected List<String> alltokentagpairs = null;
    protected Map<String, String> mapoftokentagpairs = new TreeMap<String, String>();
    protected List<String> badtokentagpairs = new ArrayList<String>();

    public LookupDictionary(String dictionaryfilename, Map<String, Integer> entitytaglookup) {
        try {

            this.dictionaryfilename = dictionaryfilename;

            alltokentagpairs = Files.readAllLines(Paths.get(dictionaryfilename));

            System.out.println("Total number of lines read from [" + dictionaryfilename + "]: " + alltokentagpairs.size());

            int i = 0;
            for (String temp : alltokentagpairs) {
                String[] arrayoftempparts = temp.split("\t\t");

                if (arrayoftempparts.length == 2) {
                    String token = arrayoftempparts[0].trim();
                    String tag = arrayoftempparts[1].trim().toUpperCase();

                    if (!tag.equals(NOTAG) & entitytaglookup.containsKey(tag)) {
                        mapoftokentagpairs.putIfAbsent(token, tag);
                    } else {
                        if (!tag.equals(NOTAG)) {
                            badtokentagpairs.add(token + DATA_SEPARATOR + tag);

                            // System.out.println("Not entered into the Lookup Dictionary. Token: " + token + " Tag: " + ANSI_ESC + "[31;47m:" + tag + ANSI_RESET);

                        }

                    }

                } else {
                    System.out.println("Line " + (i + 1) + " of file [" + dictionaryfilename + "] is malformed. Exiting...");
                    System.exit(1);

                }

                i++;

            }


        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);

        }

    }


    public Map<String, String> getLookupDictionary() {
        return mapoftokentagpairs;
    }


    public int writeLookupDictionaryToFile() {

        int index = dictionaryfilename.lastIndexOf('.');
        String stemp = dictionaryfilename.substring(0, index) + "-" + Instant.now().getEpochSecond() + dictionaryfilename.substring(index, dictionaryfilename.length());

        try {
            BufferedWriter outputfile = Files.newBufferedWriter(Paths.get(stemp));

            mapoftokentagpairs.forEach((key, value) -> {
                try {
                    outputfile.write(key.toLowerCase() + DATA_SEPARATOR + value + System.lineSeparator());
                } catch (IOException x) {
                    System.err.format("IOException: %s%n", x);
                }
            });

            outputfile.flush();
            outputfile.close();

            System.out.println("Updated Lookup Dictionary file @ " + Instant.now());

            BufferedWriter badtokentagpairsfile = Files.newBufferedWriter(Paths.get(DEFAULT_BADTOKENTAGPAIRS_FILENAME));

            for (String temp : badtokentagpairs) {
                badtokentagpairsfile.write(temp);
                badtokentagpairsfile.newLine();

            }

            badtokentagpairsfile.flush();
            badtokentagpairsfile.close();


        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);

        }

        return 0;
    }


    public int writeLookupDictionaryToFile(Map<String, String> newmapoftokentagpairs) {

        int index = dictionaryfilename.lastIndexOf('.');
        String stemp = dictionaryfilename.substring(0, index) + "-" + Instant.now().getEpochSecond() + dictionaryfilename.substring(index, dictionaryfilename.length());

        try {
            BufferedWriter outputfile = Files.newBufferedWriter(Paths.get(stemp));

            newmapoftokentagpairs.forEach((key, value) -> {
                try {
                    outputfile.write(key.toLowerCase() + DATA_SEPARATOR + value + System.lineSeparator());
                } catch (IOException x) {
                    System.err.format("IOException: %s%n", x);
                }
            });

            outputfile.flush();
            outputfile.close();

            System.out.println("Updated Lookup Dictionary file @ " + Instant.now());

            BufferedWriter badtokentagpairsfile = Files.newBufferedWriter(Paths.get(DEFAULT_BADTOKENTAGPAIRS_FILENAME));

            for (String temp : badtokentagpairs) {
                badtokentagpairsfile.write(temp);
                badtokentagpairsfile.newLine();

            }

            badtokentagpairsfile.flush();
            badtokentagpairsfile.close();


        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);

        }

        return 0;
    }


}
