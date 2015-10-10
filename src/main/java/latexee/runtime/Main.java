package main.java.latexee.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import main.antlrgen.GrammarLexer;
import main.antlrgen.GrammarParser;
import main.antlrgen.GrammarParser.*;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.utils.DeclarationParser;
import main.java.latexee.utils.DocumentParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;



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
		ParsedStatement pst = DocumentParser.parse(parseTree);
		
	}
}
	
	


