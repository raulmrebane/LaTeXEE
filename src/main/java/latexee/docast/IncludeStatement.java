package main.java.latexee.docast;

import java.util.ArrayList;

/**
 * Statements, which describe the files included are instances of this class.
 */
public class IncludeStatement extends ParsedStatement {

	/**
	 * Constructor method to build the IncludeStatement node.
	 * @param content string content of the include statement
	 * @param characterLocation starting location of the include statement
	 * @param children children nodes of IncludeStatement (other file's nodes)
	 */
	public IncludeStatement(String content, int characterLocation,
			ArrayList<ParsedStatement> children) {
		super(content, characterLocation, children);
	}


	/**
	 * Constructor method to build the IncludeStatement node
	 * @param content string content of the include statement
	 * @param characterLocation starting location of the include statement
	 */
	public IncludeStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}


	/**
	 * toString method to get the string representation of the IncludeStatement.
	 * @return string representing IncludeStatement node.
	 */
	public String toString() {
		return "[Include: " + super.toString();
	}
	
	

}
