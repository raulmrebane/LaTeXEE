package main.java.latexee.docast;

import java.util.ArrayList;

public class IncludeStatement extends ParsedStatement {

	public IncludeStatement(String content, int characterLocation,
			ArrayList<ParsedStatement> children) {
		super(content, characterLocation, children);
	}

	public IncludeStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}
	
	public String toString() {
		return "[Include: " + super.toString();
	}
	
	

}
