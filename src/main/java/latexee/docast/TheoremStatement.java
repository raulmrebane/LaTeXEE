package main.java.latexee.docast;

import java.util.ArrayList;

public class TheoremStatement extends ParsedStatement {

	public TheoremStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}
	public TheoremStatement(String content, int characterLocation, ArrayList<ParsedStatement> children) {
		super(content, characterLocation);
		this.children = children;
	}
	
	public String toString() {
		return "[Theorem: " + super.toString();
	}
}