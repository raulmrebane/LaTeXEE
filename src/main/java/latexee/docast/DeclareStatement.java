package main.java.latexee.docast;

import main.java.latexee.declareast.DeclareNode;

public class DeclareStatement extends ParsedStatement {
	//TODO: find a better name for this field.
	//This field is the parsed declaration.
	private DeclareNode node;
	
	public DeclareStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}
	
	public String toString() {
		return "[Declaration: " + super.toString();
	}

	public DeclareNode getNode() {
		return node;
	}

	public void setNode(DeclareNode node) {
		this.node = node;
	}
	
}
