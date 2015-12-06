package main.java.latexee.runtime;

import java.io.IOException;

import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.logging.Logger;
import main.java.latexee.parsers.DocumentParser;
import main.java.latexee.parsers.FormulaParser;

import org.apache.commons.cli.*;

/**
 * This class contains the main method for running the application.
 * Main method of the class accepts arguments from command line which are then passed to
 * command line arguments parser. After parsing the arguments the main application is run.
 */
public class Main {
    public static String outputFile;
    public static String inputFile;
    public static boolean verbose = false;
    public static boolean ambiguityChecking = false;
    public static boolean ambiguityCheckingWithPriority = false;
    public static boolean popcornOutput = false;


    /**
     * Options builder method.
     * @return returns object belonging to Options class, which is required by the command line arguments parser.
     */
    private
    static Options constructOptions() {
        final Options options = new Options();
        Option o = Option.builder("o").hasArg().desc("output file name").longOpt("output").build();
        options.addOption(o);
        options.addOption("v", "add verbosity to output");
        options.addOption("h", "display this menu");
        options.addOption("a", "check for ambiguity");
        options.addOption("b", "check for ambiguity with priorities");
        options.addOption("p", "output in Popcorn");
        
        return options;
    }

    /**
     * Static method to parse command line arguments.
     * @param args command line arguments passed from the main method.
     */
    private static void useParser(final String[] args) {
        final CommandLineParser parser = new DefaultParser();
        final Options options = constructOptions();  

        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            // If asked for help or there are no arguments.
            if (args.length == 0 || cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "LaTeXEE [inputFile]", options );
            }
            if (args.length > 0) {
                // input file can not start with "-"
                if (args[0].charAt(0) != '-') {
                    inputFile = args[0];
                    Logger.log("Inputfile is: " + inputFile);
                    
                    // If verbose is enabled
                    if (cmd.hasOption("v")) {
                        verbose = true;
                        System.out.println("Verbose option is enabled.");
                    }
                    if (cmd.hasOption("p")) {
                    	popcornOutput = true;
                    	System.out.println("Popcorn output mode is enabled.");
                    }
                    if (cmd.hasOption("a") && !cmd.hasOption("b")) {
                        ambiguityChecking = true;
                        System.out.println("Ambiguity checking is enabled.");
                    }
                    
                    if (cmd.hasOption("b")) {
                    	ambiguityCheckingWithPriority = true;
                    	System.out.println("Ambiguity checking with priorities is enabled.");
                    }

                    if (cmd.hasOption ("o")) {
                        // Check if can write there, etc... Legal path, warning
                        outputFile = cmd.getOptionValue("o");
                        System.out.println("Outputfile is: " + outputFile);
                    }
                    else {
                        // Default outputfile is the same as input file, but change extension.
                        // If no extension, then just add extension
                        if (inputFile.indexOf('.') == -1) {
                            outputFile = inputFile + ".xml3";
                        } else {
                            outputFile = inputFile.substring(0, inputFile.lastIndexOf(".")) + ".xml";
                        }
                        //System.out.println("Outputfile is: " + outputFile);
                    }
                }
                else {
                    System.out.println("No inputfile specified");
                }
               
            }

            
        }
        catch (ParseException parseException) {
            System.err.println("Encountered exception while parsing arguments\n"  
                + parseException.getMessage());
        }
    }

    /**
     * Entry point to the program.
     * @param args command line arguments passed from the command line.
     */
	public static void main(String[] args) {
        
        // Call parser.
        useParser(args);   
        
		//quick way to enable fast testing during writing. Uncomment to test.
		//args = new String[] {"src/test/antlr/basic_with_declare.tex"};
		//String outputFile = "output.txt";
		//String inputFile = args[0];
        try{
    		if (inputFile != null) {
                ParsedStatement AST = DocumentParser.parse(inputFile);
                FormulaParser fp = new FormulaParser(outputFile);
                if(ambiguityChecking){
                	fp.enableAmbiguityChecking();
                }
                else if (ambiguityCheckingWithPriority)
                	fp.enableAmbiguityCheckingWithPriority();
                if(popcornOutput){
                	fp.enablePopcornOutput();
                }
                fp.parse(AST);
    		}
        } catch (IOException e){
        	System.out.println("Error creating output file. Using utf-8.");
        }

	}
}
