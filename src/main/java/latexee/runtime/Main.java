package main.java.latexee.runtime;

import java.util.ArrayList;

import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.utils.DeclarationParser;
import main.java.latexee.utils.DocumentParser;


public class Main {
	public static boolean logStatus = false;

	public static void main(String[] args) {
		//quick way to enable fast testing during writing. Uncomment to test.
		args = new String[] {"src/test/antlr/LaTeX_file_2.tex"};
		
		logStatus = true;
		
		String filename = args[0];
		
		ParsedStatement AST = DocumentParser.parse(filename);
		DeclarationParser.declarationFinder(DeclarationParser.giveMeTheTestCase());
	}
}
