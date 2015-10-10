package main.java.latexee.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import main.antlrgen.GrammarLexer;
import main.antlrgen.GrammarParser;
import main.antlrgen.GrammarParser.DeclarationContext;
import main.antlrgen.GrammarParser.DocumentContext;
import main.antlrgen.GrammarParser.FileInclusionContext;
//import main.antlrgen.GrammarParser.FileInclusionContext;
import main.antlrgen.GrammarParser.FormulaContext;
import main.antlrgen.GrammarParser.LemmaContext;
import main.antlrgen.GrammarParser.ProofContext;
import main.antlrgen.GrammarParser.TheoremContext;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.IncludeStatement;
import main.java.latexee.docast.LemmaStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.docast.ProofStatement;
import main.java.latexee.docast.TheoremStatement;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

//NB1: $valem1$$valem2$ ei parsi
//NB2: ei parsi sümboleid /, { ja $ tekstina. (vaja grammatikas TEXTi muuta)

public class DocumentParser {

	//main parsing method (also parses all included documents)
		public static ParsedStatement parse (String fileContent, ArrayList<String> includedFiles) {
			ParseTree tree = parseText(fileContent);
			ParsedStatement ps = parseRecursively(tree, includedFiles);
			return ps;
		}
		
		
		//LaTeX tekst -> ParseTree
		public static ParseTree parseText (String text) {
			ANTLRInputStream antlrInput = new ANTLRInputStream(text);
	        GrammarLexer lexer = new GrammarLexer(antlrInput);
	        CommonTokenStream tokens = new CommonTokenStream(lexer);
	        GrammarParser parser = new GrammarParser(tokens);
	        ParseTree tree = parser.document();
	        return tree;
		}
		
			
		//puts all file contents into a String
		public static String getFileContent (String filePath) {
			StringBuffer sb = new StringBuffer();
			try {
				Scanner sc = new Scanner(new File(filePath));
				while (sc.hasNextLine()) {
					sb.append(sc.nextLine()); //või reavahetus ka juurde?
				}
				sc.close();
			}
			
			catch (FileNotFoundException e) {
				System.out.println("Couldn't find " + filePath);
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
			
			if (tree instanceof FormulaContext) {
				String text = tree.getText();
				text = text.substring(1, text.length() - 1);
				if (text.charAt(0) == '$')
					text = text.substring(1, text.length() - 1);
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
			
			else if (tree instanceof DeclarationContext) {
				String text = tree.getText();
				String declaration = text.substring(text.indexOf('{')+1, text.length()-1);
				return new DeclareStatement(declaration, startIndex);
			}
			
			else if (tree instanceof FileInclusionContext) {
				String text = tree.getText();
				String url = text.substring(text.indexOf('{')+1, text.length()-1);
				if (!contains(includedFiles, url)) {
					String fileContent = getFileContent(url);
					ParsedStatement child = parse(fileContent, includedFiles);
					includedFiles.add(url);
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