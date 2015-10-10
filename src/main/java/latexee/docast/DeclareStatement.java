package main.java.latexee.docast;

import java.util.ArrayList;

public class DeclareStatement extends ParsedStatement {

	public DeclareStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}
	
	public String toString() {
		return "[Declaration: " + super.toString();
	}

}
