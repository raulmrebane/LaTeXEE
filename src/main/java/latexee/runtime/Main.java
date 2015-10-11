package main.java.latexee.runtime;

import java.util.ArrayList;

import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.utils.DeclarationParser;
import main.java.latexee.utils.DocumentParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
//import org.apache.commons.cli.PosixParser; // Default or posix?
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
//import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;



public class Main {
	public static boolean logStatus = false;
    
    
	public static void main(String[] args) {
        
        // Testing command line parser
        Options options = new Options();
        
        Option outputFile = OptionBuilder.withArgName( "file" )
                                .hasArg()
                                .withDescription( "output will be here")
                                .create( "o" );
        
        options.addOption(outputFile);
        options.addOption("v", "be verbose");
        options.addOption("h", "do you need help?");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd; 
        
        try {
            cmd = parser.parse(options, args);
            
            // If asked for help
            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "latexee", options );
            }
            // If verbose is enabled
            if (cmd.hasOption("v")) {
                System.out.println("Verbose option enable. Enjoy your text");
            }
                        
            if (cmd.hasOption ("o")) {
                // Check if can write there, etc... Legal path, warning
                String outFile = cmd.getOptionValue("o");
                System.out.println("Outputfile is: " + outFile);
            } 
            else {
                // Default outputfile ?
            //    System.out.println("No outputfile specified");
            }

            
        }
        catch (ParseException parseException) {
            System.err.println("Encountered exception while parsing arguments\n"  
                + parseException.getMessage());
        }
        
       
        
        
        
		//quick way to enable fast testing during writing. Uncomment to test.
		args = new String[] {"src/test/antlr/basic_with_declare.tex"};
		
		logStatus = false;
		
		String filename = args[0];
		
		ParsedStatement AST = DocumentParser.parse(filename);
		DeclarationParser.declarationFinder(AST);
	}
}
