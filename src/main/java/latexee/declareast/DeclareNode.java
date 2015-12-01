package main.java.latexee.declareast;

import java.util.HashMap;

public abstract class DeclareNode {
	protected String contentDictionary;
	protected Object meaning;
	protected HashMap<String,String> miscellaneous = new HashMap<String,String>();
	protected String id;
	//This will create a string that will act as a fragment of the ANTLR grammar for parsing formulas.
	abstract public String toGrammarRule();
	public String getContentDictionary() {
		return contentDictionary;
	}
	public Object getMeaning() {
		return meaning;
	}
	public HashMap<String, String> getMiscellaneous() {
		return miscellaneous;
	}
	public String getId() {
		return id;
	}
}
