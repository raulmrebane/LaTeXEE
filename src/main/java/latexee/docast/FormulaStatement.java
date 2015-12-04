package main.java.latexee.docast;

/**
 * Parts of the document, which are formulas (surrounded with $) are
 * instances of this class.
 */
public class FormulaStatement extends ParsedStatement {

	/**
	 * Constructor method to build the FormulaStatement node
	 * @param content string content of the formula statement
	 * @param characterLocation starting location of the formula statement
	 */
	public FormulaStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}

	/**
	 * toString method to get the string representation of the FormulaStatement.
	 * @return string representing FormulaStatement node.
	 */
	public String toString() {
		return "[Formula: " + super.toString();
	}

}
