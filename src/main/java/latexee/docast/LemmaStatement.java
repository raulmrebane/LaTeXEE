package main.java.latexee.docast;

import java.util.ArrayList;


/**
 * Parts of the document, which are lemmas (surrounded with \begin{lemma} and \end{lemma}) are
 * instances of this class.
 */
public class LemmaStatement extends ParsedStatement {

	/**
	 * Constructor method to build the LemmaStatement node.
	 * @param content string content of the lemma statement
	 * @param characterLocation starting location of the lemma statement
	 */
	public LemmaStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}

	/**
	 * Constructor method to build the LemmaStatement node.
	 * @param content string content of the lemma statement
	 * @param characterLocation starting location of the lemma statement
	 * @param children children nodes of LemmaStatement (such as formulas)
	 */
	public LemmaStatement(String content, int characterLocation, ArrayList<ParsedStatement> children) {
		super(content, characterLocation);
		this.children = children;
	}

	/**
	 * toString method to get the string representation of the LemmaStatement.
	 * @return string representing LemmaStatement node.
	 */
	public String toString() {
		return "[Lemma: " + super.toString();
	}

}
