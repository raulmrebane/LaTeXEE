package main.java.latexee.runtime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.logging.Logger;
import main.java.latexee.utils.DeclarationParser;
import main.java.latexee.utils.DocumentParser;
import main.java.latexee.utils.FormulaParser;
import main.java.latexee.utils.GrammarCompiler;
import main.java.latexee.utils.GrammarGenerator;
import main.java.latexee.utils.OutputWriter;

import org.apache.commons.cli.*;


public class Main {
    public static String outputFile;
    public static String inputFile;
    public static boolean verbose = false;
    
    
    /* Construct options for the command line parser. */

    private
    static Options constructOptions() {
        final Options options = new Options();
        Option o = Option.builder("o").hasArg().desc("output file name").longOpt("output").build();
        options.addOption(o);
        options.addOption("v", "add verbosity to output");
        options.addOption("h", "display this menu");
        
        return options;
    }
    /* Use the command line parser */
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

                    if (cmd.hasOption ("o")) {
                        // Check if can write there, etc... Legal path, warning
                        outputFile = cmd.getOptionValue("o");
                        System.out.println("Outputfile is: " + outputFile);
                    }
                    else {
                        // Default outputfile is the same as input file, but change extension.
                        // If no extension, then just add extension
                        if (inputFile.indexOf('.') == -1) {
                            outputFile = inputFile + ".txt";
                        } else {
                            outputFile = inputFile.substring(0, inputFile.lastIndexOf(".")) + ".txt";
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
    
    
	public static void main(String[] args) throws IOException {
        
        // Call parser.
        useParser(args);   
        
		//quick way to enable fast testing during writing. Uncomment to test.
		//args = new String[] {"src/test/antlr/basic_with_declare.tex"};
		//String outputFile = "output.txt";
		//String inputFile = args[0];
		if (inputFile != null) {
            ParsedStatement AST = DocumentParser.parse(inputFile);
            System.out.println("AST: \n");
            DeclarationParser.declarationFinder(AST);
            System.out.println(AST.toString()+"\n");
            OutputWriter.formulasToTXT(AST, outputFile);
            FormulaParser.parse(AST, new ArrayList<DeclareNode>());
        }
	}
}
