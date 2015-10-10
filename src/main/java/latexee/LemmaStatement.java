package main.java.latexee;

import java.util.ArrayList;

public class LemmaStatement extends ParsedStatement {

	public LemmaStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}
	public LemmaStatement(String content, int characterLocation, ArrayList<ParsedStatement> children) {
		super(content, characterLocation);
		this.children = children;
	}
	
	public String toString() {
		return "[Lemma: " + super.toString();
	}

}
