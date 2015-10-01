package main.java.latexee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import antlrgen.GrammarLexer;
import antlrgen.GrammarParser;
import antlrgen.GrammarParser.*;



public class Main {

	public static void main(String[] args) {
		//quick way to enable fast testing during writing. Uncomment to test.
		//args = new String[] {"src/test/antlr/basic.tex"};
		
		File file = new File(args[0]);
		ANTLRInputStream AIS = null;
		try{
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
		ParsedStatement pst = parseRecursively(parseTree);
	}
	
	public static ParsedStatement parseRecursively (ParseTree tree) {
		int startIndex = 0;
		//All contexts inherit from ParserRuleContext, so for each case the index will be set here.
		if(tree instanceof ParserRuleContext){
			startIndex = ((ParserRuleContext) tree).getStart().getStartIndex();
		}
		
		if (tree instanceof DocumentContext){
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				ParsedStatement child = parseRecursively(tree.getChild(i));
				if(child!=null){
					children.add(child);
				}
			}
			return new ParsedStatement(tree.getText(), startIndex, children);
		}
		if (tree instanceof FormulaContext) {
			return new FormulaStatement(tree.getText(), startIndex);
		}
		
		else if (tree instanceof ProofContext) {
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				ParsedStatement child = parseRecursively(tree.getChild(i));
				if(child!=null){
					children.add(child);
				}
			}
			return new ProofStatement(tree.getText(), startIndex, children);
		}
		
		else if (tree instanceof TheoremContext) {
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				ParsedStatement child = parseRecursively(tree.getChild(i));
				if(child!=null){
					children.add(child);
				}
			}
			return new TheoremStatement(tree.getText(), startIndex, children);
		}
		
		else if (tree instanceof DeclarationContext) {
			return new DeclareStatement(tree.getText(), startIndex);
		}
		
		else if (tree instanceof LemmaContext) {
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				ParsedStatement child = parseRecursively(tree.getChild(i));
				if(child!=null){
					children.add(child);
				}
			}
			return new LemmaStatement(tree.getText(), startIndex, children);
		}
		return null;
	}
	
}
