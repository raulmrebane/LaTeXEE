package main.java.latexee.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import main.antlrgen.DocumentGrammarLexer;
import main.antlrgen.DocumentGrammarParser;
import main.antlrgen.DocumentGrammarParser.DeclarationGrammarContext;
import main.antlrgen.DocumentGrammarParser.DocumentContext;
import main.antlrgen.DocumentGrammarParser.FileInclusionContext;
import main.antlrgen.DocumentGrammarParser.FormulaContext;
import main.antlrgen.DocumentGrammarParser.LemmaContext;
import main.antlrgen.DocumentGrammarParser.ProofContext;
import main.antlrgen.DocumentGrammarParser.TheoremContext;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.IncludeStatement;
import main.java.latexee.docast.LemmaStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.docast.ProofStatement;
import main.java.latexee.docast.TheoremStatement;
import main.java.latexee.logging.Logger;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;


public class DocumentParser {
		public static ParsedStatement parse (String filename){
			File file = new File(filename);
			ANTLRInputStream AIS = null;
			try {
				Logger.log("Attempting to open file: "+filename);
				AIS = new ANTLRInputStream(new FileInputStream(file));
				Logger.log("... OK");
			}
			catch (Exception e){
				Logger.log("Could not access specified file.");
				System.exit(1);
			}
			DocumentGrammarLexer lexer = new DocumentGrammarLexer(AIS);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			DocumentGrammarParser parser = new DocumentGrammarParser(tokens);
			ParseTree parseTree = parser.document();
			ParsedStatement AST = parseRecursively(parseTree, new ArrayList<String>());
			return AST;
		}
		//main parsing method (also parses all included documents)
		public static ParsedStatement parse (String fileContent, ArrayList<String> includedFiles) {
			ParseTree tree = parseText(fileContent);
			ParsedStatement ps = parseRecursively(tree, includedFiles);
			return ps;
		}
		
		
		//LaTeX tekst -> ParseTree
		public static ParseTree parseText (String text) {
			ANTLRInputStream antlrInput = new ANTLRInputStream(text);
			DocumentGrammarLexer lexer = new DocumentGrammarLexer(antlrInput);
	        CommonTokenStream tokens = new CommonTokenStream(lexer);
	        DocumentGrammarParser parser = new DocumentGrammarParser(tokens);
	        ParseTree tree = parser.document();
	        return tree;
		}
		
			
		//puts all file contents into a String
		public static String getFileContent (String filePath) {
			StringBuffer sb = new StringBuffer();
			try {
				Logger.log("Attempting to open file: "+filePath);
				Scanner sc = new Scanner(new File(filePath));
				Logger.log("... OK");
				while (sc.hasNextLine()) {
					sb.append(sc.nextLine()); //või reavahetus ka juurde?
				}
				sc.close();
			}
			
			catch (FileNotFoundException e) {
				Logger.log("Could not access specified file.");
			}
			return sb.toString();
		}
		
			
		public static ParsedStatement parseRecursively (ParseTree tree, ArrayList<String> includedFiles) {
			int startIndex = 0;
			//All contexts inherit from ParserRuleContext, so for each case the index will be set here.
			if(tree instanceof ParserRuleContext){
				startIndex = ((ParserRuleContext) tree).getStart().getStartIndex();
			}
			
			if (tree instanceof DocumentContext){
				ArrayList<ParsedStatement> children = new ArrayList<>();
				for (int i = 0; i < tree.getChildCount(); i++) {
					ParsedStatement child = parseRecursively(tree.getChild(i), includedFiles);
					if(child!=null){
						children.add(child);
					}
				}
				return new ParsedStatement(tree.getText(), startIndex, children);
			}
			
			else if (tree instanceof FormulaContext) {
				String text = tree.getText();
				if (text.charAt(0) == '$') {
					text = text.substring(1, text.length() - 1);
					if (text.length() > 1 && text.charAt(0) == '$')
						text = text.substring(1, text.length() - 1);
				}
				else
					text = text.substring(text.indexOf('}')+1, text.length()-"\\end{equation}".length());
				return new FormulaStatement(text, startIndex);
			}
			
			else if (tree instanceof ProofContext) {
				ArrayList<ParsedStatement> children = new ArrayList<>();
				for (int i = 0; i < tree.getChildCount(); i++) {
					ParsedStatement child = parseRecursively(tree.getChild(i), includedFiles);
					if(child!=null){
						children.add(child);
					}
				}
				return new ProofStatement("", startIndex, children);
			}
			
			else if (tree instanceof TheoremContext) {
				ArrayList<ParsedStatement> children = new ArrayList<>();
				for (int i = 0; i < tree.getChildCount(); i++) {
					ParsedStatement child = parseRecursively(tree.getChild(i), includedFiles);
					if(child!=null){
						children.add(child);
					}
				}
				return new TheoremStatement("", startIndex, children);
			}
			
			else if (tree instanceof DeclarationGrammarContext) {
				String text = tree.getText();
				System.out.println(text);
				
				return new DeclareStatement(text, startIndex);
			}
			
			else if (tree instanceof FileInclusionContext) {
				String text = tree.getChild(0).getText();
				String url = text.substring(text.indexOf('{')+1, text.length()-1);
				if (!contains(includedFiles, url)) {
					includedFiles.add(url);
					String fileContent = getFileContent(url);
					ParsedStatement child = parse(fileContent, includedFiles);
					return new IncludeStatement(url, startIndex, new ArrayList<ParsedStatement>(Arrays.asList(child)));
				}
				return new IncludeStatement(url, startIndex);
			}
			
			else if (tree instanceof LemmaContext) {
				ArrayList<ParsedStatement> children = new ArrayList<>();
				for (int i = 0; i < tree.getChildCount(); i++) {
					ParsedStatement child = parseRecursively(tree.getChild(i), includedFiles);
					if(child!=null){
						children.add(child);
					}
				}
				return new LemmaStatement("", startIndex, children);
			}
			return null;
		}
		
		
		public static boolean contains(ArrayList<String> list, String el) { //TODO: Object instead of String to fit any type?
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(el))
					return true;
			}
			return false;
		}

}