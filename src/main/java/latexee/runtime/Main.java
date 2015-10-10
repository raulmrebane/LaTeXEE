package main.java.latexee.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import main.antlrgen.GrammarLexer;
import main.antlrgen.GrammarParser;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.utils.DeclarationParser;
import main.java.latexee.utils.DocumentParser;


public class Main {


	public static void main(String[] args) {
		//quick way to enable fast testing during writing. Uncomment to test.
		args = new String[] {"src/test/antlr/basic.tex"};
		
		File file = new File(args[0]);
		ANTLRInputStream AIS = null;
		try {
			AIS = new ANTLRInputStream(new FileInputStream(file));
		}
		catch (Exception e){
			System.out.println("Could not find specified file.");
			System.exit(1);
		}
		GrammarLexer lexer = new GrammarLexer(AIS);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GrammarParser parser = new GrammarParser(tokens);
		ParseTree parseTree = parser.document();
		
	}
}
	
	
	
	
	//NB1: $valem1$$valem2$ ei parsi
	//NB2: ei parsi sümboleid /, { ja $ tekstina. (vaja grammatikas TEXTi muuta)
