package main.java.latexee.runtime;

import java.util.ArrayList;

import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.logging.Logger;
import main.java.latexee.utils.DeclarationParser;
import main.java.latexee.utils.DocumentParser;
import main.java.latexee.utils.GrammarGenerator;
import main.java.latexee.utils.OutputWriter;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.PosixParser; // Default or posix?
//import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
//import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;



public class Main {
    public static String outputFile;
    public static String inputFile;
    public static boolean verbose = false;
    
    
    /* Construct options for the command line parser. */
    private static Options constructOptions() {
        final Options options = new Options();
        
        Option o = OptionBuilder.withArgName( "file" )
                                .hasArg()
                                .withDescription( "output will be here")
                                .create( "o" );
        
        options.addOption(o);
        options.addOption("v", "be verbose");
        options.addOption("h", "do you need help?");
        
        return options;
    }
    /* Use the command line parser */
    private static void useParser(final String[] args) {
        final CommandLineParser parser = new PosixParser();    
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
                    System.out.println("Inputfile is: " + inputFile);
                    
                    // If verbose is enabled
                    if (cmd.hasOption("v")) {
                        verbose = true;
                        System.out.println("Verbose option enable. Enjoy your text");
                    }
                                
                    if (cmd.hasOption ("o")) {
                        // Check if can write there, etc... Legal path, warning
                        outputFile = cmd.getOptionValue("o");
                        System.out.println("Outputfile is: " + outputFile);
                    }
                    else {
                        // Default outputfile ?
                    //    System.out.println("No outputfile specified");
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
    
    
	public static void main(String[] args) {
        
        // Call parser.
        useParser(args);   
        
		//quick way to enable fast testing during writing. Uncomment to test.
		args = new String[] {"src/test/antlr/basic_with_declare.tex"};
		String outputFile = "output.txt";
		String filename = args[0];
		
		ParsedStatement AST = DocumentParser.parse(filename);
		DeclarationParser.declarationFinder(AST);
		OutputWriter.formulasToTXT(AST, outputFile);
		Logger.log("Finished without errors");
	}
}
