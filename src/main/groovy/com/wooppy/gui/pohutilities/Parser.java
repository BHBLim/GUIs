package com.wooppy.gui.pohutilities; /******************************************************************************
 *  com.wooppy.tagger.pohutilities.Parser.java
 *  [Created by C Y Poh 29 Oct 2016 @ 5:22pm]
 *  [Re-architected by C Y Poh 16 Nov 2016 @ 6:31pm]
 *  [Stolen and groovyfied by Basil 23 Nov 2016]
 *
 *  Compilation:  javac -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. ./Tagger.java
 *  Execution:    java -cp ./JarFiles/stanford-corenlp-3.6.0.jar:. Tagger
 *
 *  The Java com.wooppy.tagger.pohutilities.Parser class contains all fields and methods associated with the
 *  sentences (i.e. end-user inputs) to be processed, tokenised, entity-tagged 
 *  (pre-tagging and exclusion) and formatted (for terminal display).
 *
 *  The input text file containing the sentences is named input.txt and placed 
 *  in the Data directory [Data/input.txt].
 *
 *  The output text file containing the token-tag pairs is named output.txt and 
 *  placed in the Data directory [Data/output.txt].
 *
 *
 *
 ******************************************************************************/

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Parser {
    protected final String ANSI_ESC = "\u001B";
    protected final String ANSI_RESET = "\u001B[0m";
    protected final String ANSI_PRETAGGED_COLOUR_CODE = "[37;45m";
    protected final String ANSI_EXCLUDED_COLOUR_CODE = "[34m";

    protected final String DELIMITER = "|##|";

    protected final String DATA_SEPARATOR = "\t\t";

    protected final String UNSPEC_TAG = "(--)";
    protected final String NOTAG_TAG = "O";


    String outputfilename = "";

    List<String> allinputlines = null;
    List<String> alloutputlines = new ArrayList<String>();

    Map<String, List<String>> mapofallprocessedinputlines = new TreeMap<String, List<String>>();

    List<String> entitytaglines = null;
    List<String> uniqueexcludedtokens = null;

    Map<String, Integer> mapofentitytags = null;
    Map<Integer, String> maprevofentitytags = null;
    Map<String, String> mapoftokentagpairs = null;

    EntityTags entitytags = null;
    ExcludedTokens excludedtokens = null;
    LookupDictionary lookupdictionary = null;

    public Parser(String inputfilename, String outputfilename, String entitytagsfilename, String excludedtokensfilename, String dictionaryfilename) {

        try {
            allinputlines = Files.readAllLines(Paths.get(inputfilename));

            if (allinputlines.get((allinputlines.size() - 1)).trim().isEmpty()) {
                allinputlines.remove((allinputlines.size() - 1));
                allinputlines.add("");
            } else {
                allinputlines.add("");

            }

        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);


        }

        this.outputfilename = outputfilename;

        entitytags = new EntityTags(entitytagsfilename);
        mapofentitytags = entitytags.getEntityTags();
        maprevofentitytags = entitytags.getRevEntityTags();
        entitytaglines = entitytags.displayEntityTags();

        excludedtokens = new ExcludedTokens(excludedtokensfilename);
        uniqueexcludedtokens = excludedtokens.getUniqueExcludedTokens();

        lookupdictionary = new LookupDictionary(dictionaryfilename, mapofentitytags);
        mapoftokentagpairs = lookupdictionary.getLookupDictionary();
        //lookupdictionary.writeLookupDictionaryToFile(mapoftokentagpairs);


    }


    public List<String> getEntityTagLines() {
        return entitytaglines;

    }


    public List<String> getExcludedTokens() {
        return uniqueexcludedtokens;

    }

    public Map<String, Integer> getMapOfEntityTags() {
        return mapofentitytags;

    }

    public Map<Integer, String> getMapRevOfEntityTags() {
        return maprevofentitytags;

    }

    public Map<String, String> getMapOfDictionary() {
        return mapoftokentagpairs;

    }


    public void checkDictionaryExclusionList(BufferedReader bdreaderinput) throws IOException {
        List<String> dictionarydeleteitems = new ArrayList<String>();
        List<String> excludeddeleteitems = new ArrayList<String>();

        int i = 0;
        for (String stemp : uniqueexcludedtokens) {

            if (mapoftokentagpairs.containsKey(stemp.toLowerCase())) {

                System.out.println("There is a conflict: the excluded token [" + stemp + "] is in the Dictionary.");
                System.out.println("The Dictionary entry is: [" + stemp + "\t" + mapoftokentagpairs.get(stemp.toLowerCase()) + "]");
                System.out.print("Delete either the Dictionary entry (1) or the excluded token (2): [Default is (1)]");

                // START: Codes to handle user inputs...
                boolean redoflag = true;
                do {

                    String userinput = bdreaderinput.readLine();

                    int itemp = 1;

                    if (!userinput.trim().isEmpty()) {
                        try {
                            itemp = Integer.parseInt(userinput);

                            if (itemp != 1 & itemp != 2) {
                                System.out.println("The input must either be [1] or [2]. Please re-enter...");

                            } else {

                                switch (itemp) {
                                    case 1:
                                        dictionarydeleteitems.add(stemp.toLowerCase());
                                        System.out.println("Deleted the Dictionary entry.");

                                        break;

                                    case 2:
                                        excludeddeleteitems.add(stemp.toLowerCase());
                                        System.out.println("Deleted the excluded token.");

                                        break;

                                }

                                redoflag = false; // Stop listening to user input for this token.

                            }

                        } catch (NumberFormatException nfe) {

                            if (userinput.trim().toLowerCase().equals("q")) {
                                System.out.println("Your wish is my command. Exiting...");
                                System.exit(1);

                            } else {
                                System.out.println("The input must either be [1] or [2]. Please re-enter...");

                            }

                        }

                    } else { // If user input is empty/blank...

                        dictionarydeleteitems.add(stemp.toLowerCase());
                        System.out.println("Deleted the Dictionary entry.");

                        redoflag = false; // Stop listening to user input for this token.

                    }

                } while (redoflag);
                // END: Codes to handle user inputs...

            }

            i++;

        }

        int j = 0;
        for (String tempj : dictionarydeleteitems) {
            mapoftokentagpairs.remove(tempj);

            j++;

        }

        int k = 0;
        for (String tempk : excludeddeleteitems) {
            uniqueexcludedtokens.remove(tempk);

            k++;

        }

        excludedtokens.writeExcludedTokensToFile(uniqueexcludedtokens);
        lookupdictionary.writeLookupDictionaryToFile(mapoftokentagpairs);


    }


    private Map<String, List<String>> processSentence(List<String> sentence) {

        Map<String, List<String>> mapofsentencelines = new TreeMap<String, List<String>>();

        List<String> sentencelinesdisplay = new ArrayList<String>();
        List<String> sentencelinesactual = new ArrayList<String>();
        List<String> sentencelinesdelimited = new ArrayList<String>();

        String ltemp = "";
        String l2temp = "";
        String l3temp = "";

        String stemp = "";
        String s2temp = "";
        String s3temp = "";

        String uxttemp = "";
        String pretagtemp = "";

        int i = 0;
        int j = 0;
        for (String temp : sentence) {

            PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(new StringReader(temp),
                    new CoreLabelTokenFactory(), "");


            while (ptbt.hasNext()) {
                CoreLabel label = ptbt.next();
                stemp = label.toString().trim();

                if (uniqueexcludedtokens.contains(stemp.toLowerCase())) {
                    uxttemp = ANSI_ESC + ANSI_EXCLUDED_COLOUR_CODE + stemp + ANSI_RESET;

                }

                if (mapoftokentagpairs.containsKey(stemp.toLowerCase()) & !uxttemp.isEmpty()) {

                    System.out.println("There is a conflict: the excluded token [" + stemp + "] is in the Dictionary.");
                    System.out.println("The Dictionary entry is: [" + stemp + "\t" + mapoftokentagpairs.get(stemp.toLowerCase()) + "]");
                    System.out.print("Please resolve the conflict before proceeding. Exiting...");
                    System.exit(1);

                } else if (mapoftokentagpairs.containsKey(stemp.toLowerCase())) {
                    pretagtemp = ANSI_ESC + ANSI_PRETAGGED_COLOUR_CODE + stemp + "(" + String.format("%02d", mapofentitytags.get(mapoftokentagpairs.get(stemp.toLowerCase()))) + ")" + ANSI_RESET;
                    stemp = stemp + "(" + String.format("%02d", mapofentitytags.get(mapoftokentagpairs.get(stemp.toLowerCase()))) + ")";

                }

                if (uxttemp.isEmpty() & pretagtemp.isEmpty()) {
                    s2temp = stemp + UNSPEC_TAG + " ";
                    stemp = stemp + UNSPEC_TAG;

                } else if (uxttemp.isEmpty()) {
                    s2temp = pretagtemp + " ";

                } else {
                    s2temp = uxttemp + " ";

                }

                s3temp = stemp + DELIMITER;
                stemp = stemp + " ";

                uxttemp = "";
                pretagtemp = "";

                if ((ltemp.length() + stemp.length()) <= Terminal.DISPLAY_WIDTH) {
                    ltemp = ltemp + stemp;
                    l2temp = l2temp + s2temp;
                    l3temp = l3temp + s3temp;

                } else {
                    l2temp = "Line #" + j + ": " + l2temp;

                    sentencelinesactual.add(ltemp);
                    sentencelinesdisplay.add(l2temp);
                    sentencelinesdelimited.add(l3temp);

                    ltemp = stemp;
                    l2temp = s2temp;
                    l3temp = s3temp;

                    j++;

                }

                s2temp = "";
                s3temp = "";

            }

            if (!ltemp.isEmpty()) {
                l2temp = "Line #" + j + ": " + l2temp;

                sentencelinesactual.add(ltemp);
                sentencelinesdisplay.add(l2temp);
                sentencelinesdelimited.add(l3temp);

            }

            ltemp = "";
            l2temp = "";
            l3temp = "";

            j++;
            i++;

        }

        mapofsentencelines.put("ORIGINAL", sentence);
        mapofsentencelines.put("DISPLAY", sentencelinesdisplay);
        mapofsentencelines.put("ACTUAL", sentencelinesactual);
        mapofsentencelines.put("DELIMITED", sentencelinesdelimited);

        return mapofsentencelines;

    } // END: Map<String, List<String>> processSentence(List<String> sentence) method.


    public Map<String, List<String>> processInputfile() {

        Map<String, List<String>> tempmap = new TreeMap<String, List<String>>();

        List<String> allparsedlinesdisplay = new ArrayList<String>();
        List<String> allparsedlinesactual = new ArrayList<String>();
        List<String> allparsedlinesdelimited = new ArrayList<String>();

        List<String> currentinputlines = new ArrayList<String>();

        int i = 0;
        for (String stemp : allinputlines) { // Input file level.
            if (!stemp.trim().isEmpty()) {
                currentinputlines.add(stemp);

            } else {
                tempmap = processSentence(currentinputlines);

                List<String> display = tempmap.get("DISPLAY");
                List<String> actual = tempmap.get("ACTUAL");
                List<String> delimited = tempmap.get("DELIMITED");

                int j = 0;
                for (String parsedline : display) { // Sentence level for DISPLAY.
                    allparsedlinesdisplay.add(parsedline);

                    j++;

                }
                allparsedlinesdisplay.add("");

                int k = 0;
                for (String parsedline : actual) { // Sentence level for ACTUAL.
                    allparsedlinesactual.add(parsedline);

                    k++;

                }
                allparsedlinesactual.add("");

                int l = 0;
                for (String parsedline : delimited) { // Sentence level for DELIMITED.
                    allparsedlinesdelimited.add(parsedline);

                    l++;

                }
                allparsedlinesdelimited.add("");

                currentinputlines.clear();
                i++; // Sentence counter.

            }


        } // END: file level.

        mapofallprocessedinputlines.put("ORIGINAL", allinputlines);
        mapofallprocessedinputlines.put("DISPLAY", allparsedlinesdisplay);
        mapofallprocessedinputlines.put("ACTUAL", allparsedlinesactual);
        mapofallprocessedinputlines.put("DELIMITED", allparsedlinesdelimited);

        return mapofallprocessedinputlines;


    } // END: Map<String, List<String>> processInputfile() method.


    private void addTokenTag(String tokentag) {

        String result = "";

        if (!tokentag.trim().isEmpty()) {

            if (tokentag.contains("(")) {

                String token = tokentag.substring(0, tokentag.indexOf("("));
                String tag = tokentag.substring((tokentag.indexOf("(") + 1), tokentag.indexOf(")"));

                if (!tag.equals("--")) {
                    try {
                        int itemp2 = Integer.parseInt(tag);

                        result = token + DATA_SEPARATOR + maprevofentitytags.get(itemp2);
                        alloutputlines.add(result);

                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid tag. Exiting...");
                        System.exit(1);

                    }
                } else {
                    result = token + DATA_SEPARATOR + NOTAG_TAG;
                    alloutputlines.add(result);

                }

            } else {
                result = tokentag + DATA_SEPARATOR + NOTAG_TAG;
                alloutputlines.add(result);

            }

        }

    } // END: private void addTokenTag(String tokentag) method.


    public void addForOutput(String[] arrayofparsedtokens, String[] arrayofdeltas, String lineseparator) {

        alloutputlines.add(lineseparator);

        for (int i = 0; i < arrayofparsedtokens.length; i++) {
            String parsedtoken = arrayofparsedtokens[i];
            String delta = arrayofdeltas[i];

            if (delta == null) {
                addTokenTag(parsedtoken);

            } else if (delta.isEmpty()) {
                addTokenTag(parsedtoken);

            } else {
                addTokenTag(delta);

            }

        }


    } // END: public void addForOutput(String[] arrayofparsedtokens, String[] arrayofdeltas, String lineseparator) method.


    public void writeOutputfile() {

        try {

            File file = new File(outputfilename);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file, true); // Set true to enable the append feature of FileWriter.
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            int i = 0;
            for (String temp : alloutputlines) {
                pw.println(temp);

                i++;

            }
            pw.close();

            System.out.println("Written to [" + outputfilename + "] total number of lines: " + i);

        } catch (IOException x) {
            System.out.println("Writing Outputfile failed. Exiting...");
            System.exit(1);

        }

        alloutputlines.clear();

    } // END: public void  writeOutputfile() method.


} // END: com.wooppy.tagger.pohutilities.Parser class.
// ***********************************************


// Codes for debugging:

/*
System.out.println("Last line: -->" + allinputlines.get((allinputlines.size() - 1)) + "<--");
System.out.println("Penultimate line: -->" + allinputlines.get((allinputlines.size() - 2)) + "<--");

System.out.println("Total number of lines read from [" + inputfilename + "]: " + allinputlines.size());
*/
