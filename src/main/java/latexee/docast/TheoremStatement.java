package main.java.latexee.docast;

import java.util.ArrayList;
/**
 * Parts of the document, which are theorems (surrounded with \begin{theorem} and \end{theorem}) are
 * instances of this class.
 */
public class TheoremStatement extends ParsedStatement {

	/**
	 * Constructor method to build the TheoremStatement node.
	 * @param content string content of the theorem statement
	 * @param characterLocation starting location of the theorem statement
	 */
	public TheoremStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}


	/**
	 * Constructor method to build the TheoremStatement node.
	 * @param content string content of the theorem statement
	 * @param characterLocation starting location of the theorem statement
	 * @param children children nodes of TheoremStatement (such as lemmas, formulas, proofs)
	 */
	public TheoremStatement(String content, int characterLocation, ArrayList<ParsedStatement> children) {
		super(content, characterLocation);
		this.children = children;
	}

	/**
	 * toString method to get the string representation of the TheoremStatement.
	 * @return string representing TheoermStatement node.
	 */
	public String toString() {
		return "[Theorem: " + super.toString();
	}
}
