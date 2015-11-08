package main.java.latexee.declareast;

import java.util.HashMap;

public abstract class DeclareNode {
	public static int identifier = 0;
	protected String contentDictionary;
	protected String meaning;
	protected HashMap<String,String> miscellaneous = new HashMap<String,String>();
	protected String id;
	//This will create a string that will act as a fragment of the ANTLR grammar for parsing formulas.
	abstract public String toGrammarRule();
	public String getContentDictionary() {
		return contentDictionary;
	}
	public String getMeaning() {
		return meaning;
	}
	public HashMap<String, String> getMiscellaneous() {
		return miscellaneous;
	}
	public String getId() {
		return id;
	}
	public void resetIdentifier(){
		identifier = 0;
	}
}
