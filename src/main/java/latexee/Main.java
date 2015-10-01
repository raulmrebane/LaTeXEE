package main.java.latexee;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

import antlrgen.GrammarParser.*;



public class Main {

	public static void main(String[] args) {
		System.out.println("Test");
	}
	
	//NB: characterLocationis vaja 0 asemele midagi muud panna.
	
	public ParsedStatement parseRecursively (ParseTree tree) {
		if (tree instanceof FormulaContext) {
			return new FormulaStatement(tree.getText(), 0);
		}
		
		else if (tree instanceof ProofContext) {
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				children.add(parseRecursively(tree.getChild(i)));
			}
			return new ProofStatement(tree.getText(), 0, children);
		}
		
		else if (tree instanceof TheoremContext) {
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				children.add(parseRecursively(tree.getChild(i)));
			}
			return new TheoremStatement(tree.getText(), 0, children);
		}
		
		else if (tree instanceof DeclarationContext) {
			return new DeclareStatement(tree.getText(), 0); //TODO
		}
		
		else if (tree instanceof LemmaContext) {
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				children.add(parseRecursively(tree.getChild(i)));
			}
			return new LemmaStatement(tree.getText(), 0, children);
		}
		
		
		//TODO
		return null;
	}
    
	
}
