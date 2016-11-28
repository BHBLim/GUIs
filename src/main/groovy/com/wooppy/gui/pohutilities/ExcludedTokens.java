package com.wooppy.gui.pohutilities; /******************************************************************************
 *  com.wooppy.tagger.pohutilities.ExcludedTokens.java [Created by C Y Poh 30 Oct 2016 @ 1:43am]
 *
 *  Compilation:  javac -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. ./com.wooppy.tagger.pohutilities.Parser.java
 *  Execution:    java -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. com.wooppy.tagger.pohutilities.Parser
 *
 *  A class that encapsulates all tokens to be ignored by the entity tagging
 *  process.
 *
 *  The tokens are specified in a text file named excludedtokens.txt and placed in
 *  the Data directory.
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
import java.util.List;
import java.util.stream.Collectors;

public class ExcludedTokens {

    protected List<String> allexcludedtokens = null;
    protected List<String> uniqueexcludedtokens = null;

    protected String inputfilename = "";

    public ExcludedTokens(String inputfilename) {
        this.inputfilename = inputfilename;

        try {
            allexcludedtokens = Files.readAllLines(Paths.get(inputfilename));

            int index = inputfilename.lastIndexOf('.');
            String uniqueexcludedtokensfilename = inputfilename.substring(0, index) + "-unique" + inputfilename.substring(index, inputfilename.length());

            BufferedWriter uniqueexcludedtokensfile = Files.newBufferedWriter(Paths.get(uniqueexcludedtokensfilename));

            uniqueexcludedtokens = allexcludedtokens.stream().distinct().collect(Collectors.toList());
            for (String temp : uniqueexcludedtokens) {
                uniqueexcludedtokensfile.write(temp);
                uniqueexcludedtokensfile.newLine();

            }
            uniqueexcludedtokensfile.close();

            System.out.println("Updated the file: " + uniqueexcludedtokensfilename);

        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);

        }

    }

    public List<String> getExcludedTokens() {
        return allexcludedtokens;
    }

    public List<String> getUniqueExcludedTokens() {
        return uniqueexcludedtokens;
    }

    public int writeExcludedTokensToFile(List<String> excludedtokens) {

        List<String> writtenexcludedtokens = null;

        int index = inputfilename.lastIndexOf('.');
        String stemp = inputfilename.substring(0, index) + "-" + Instant.now().getEpochSecond() + inputfilename.substring(index, inputfilename.length());

        try {
            BufferedWriter outputfile = Files.newBufferedWriter(Paths.get(stemp));

            writtenexcludedtokens = excludedtokens.stream().distinct().collect(Collectors.toList());
            for (String temp : writtenexcludedtokens) {
                outputfile.write(temp);
                outputfile.newLine();

            }
            outputfile.close();

            System.out.println("Updated Excluded Tokens file @ " + Instant.now());

        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);

        }

        return 0;
    }


    public int compareDictionary(String dictionaryfilename) {
        List<String> alltokentagpairs = null;

        try {
            alltokentagpairs = Files.readAllLines(Paths.get(dictionaryfilename));

            int i = 0;
            for (String temp : alltokentagpairs) {
                String[] arrayoftempparts = temp.split("\t\t");
                String token = "";
                String tag = "";

                if (arrayoftempparts.length == 2) {
                    token = arrayoftempparts[0].trim().toLowerCase();
                    tag = arrayoftempparts[1].trim().toUpperCase();

                } else {
                    System.out.println("Line " + (i + 1) + " of file [" + dictionaryfilename + "] is malformed. Exiting...");
                    System.exit(1);

                }

                for (String temp2 : uniqueexcludedtokens) {
                    if (token.equals(temp2.trim().toLowerCase())) {
                        System.out.println("Line " + i + " of the Lookup Dictionary contains the tokenText [" + arrayoftempparts[0] + "] that matches the Excluded tokenText [" + temp2 + "]");

                    }
                }

                i++;

            }


        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);

        }

        return 0;

    }
}
