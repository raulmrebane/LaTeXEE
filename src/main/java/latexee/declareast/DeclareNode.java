package main.java.latexee.declareast;

import java.util.HashMap;

public abstract class DeclareNode {
	protected String meaning;
	protected HashMap<String,String> miscellaneous;
	//This will create a string that will act as a fragment of the ANTLR grammar for parsing formulas.
	abstract public String toGrammarRule();
}
