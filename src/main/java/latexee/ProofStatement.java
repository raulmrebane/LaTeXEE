package main.java.latexee;

import java.util.ArrayList;

public class ProofStatement extends ParsedStatement {

	public ProofStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}
	public ProofStatement(String content, int characterLocation, ArrayList<ParsedStatement> children) {
		super(content, characterLocation);
		this.children = children;
	}
	
	public String toString() {
		return "[Proof: " + super.toString();
	}

}
