package main.java.latexee.docast;

import main.java.latexee.declareast.DeclareNode;

/**
 * Statements, which are declarations are instances of this class.
 * Instances of this class hold a declaration node which are parsed from the
 * document's syntax tree.
 */
public class DeclareStatement extends ParsedStatement {
	//TODO: find a better name for this field.
	//This field is the parsed declaration.
	private DeclareNode node;

	/**
	 * Constructor method to build the DeclareStatement node
	 * @param content string content of the declaration statement
	 * @param characterLocation starting location of the declaration statement
	 */
	public DeclareStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}

	/**
	 * toString method to get the string representation of the Declaration Statement.
	 * @return string representing DeclareStatement node.
	 */
	public String toString() {
		return "[Declaration: " + super.toString();
	}

	/**
	 * Getter function to get the DeclareNode belong to the DeclareStatement.
	 * @return returns instance of DeclareNode which belongs to the DeclareStatement
	 */
	public DeclareNode getNode() {
		return node;
	}

	/**
	 * Setter function to set the DeclareNode belonging to this DeclareStatement.
	 * @param node which is parsed from the DeclareStatement's content.
	 */
	public void setNode(DeclareNode node) {
		this.node = node;
	}
	
}
