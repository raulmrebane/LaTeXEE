package main.antlr.latexee.declareast;

public abstract class DeclareNode {
	protected String meaning;
	//This will create a string that will act as a fragment of the ANTLR grammar for parsing formulas.
	abstract public String toGrammarRule();
}
