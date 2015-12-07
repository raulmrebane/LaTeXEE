package main.java.latexee.docast;

import java.util.ArrayList;

/**
 * Parts of the document, which are proofs (surrounded with \begin{proof} and \end{proof}) are
 * instances of this class.
 */
public class ProofStatement extends ParsedStatement {

	/**
	 * Constructor method to build the ProofStatement node.
	 * @param content string content of the proof statement
	 * @param characterLocation starting location of the proof statement
	 */
	public ProofStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}


	/**
	 * Constructor method to build the ProofStatement node.
	 * @param content string content of the proof statement
	 * @param characterLocation starting location of the proof statement
	 * @param children children nodes of ProofStatement (such as lemmas, formulas)
	 */
	public ProofStatement(String content, int characterLocation, ArrayList<ParsedStatement> children) {
		super(content, characterLocation);
		this.children = children;
	}

	/**
	 * toString method to get the string representation of the ProofStatement.
	 * @return string representing ProofStatement node.
	 */
	public String toString() {
		return "[Proof: " + super.toString();
	}

}
