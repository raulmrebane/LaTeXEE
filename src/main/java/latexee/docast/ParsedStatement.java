package main.java.latexee.docast;

import java.util.ArrayList;

/**
 * Main class for the statements, which are extracted from the document.
 * All other statement classes (FormulaStatement, IncludeStatement, ...) are extended from this class.
 * Contains fields for content string, start location of the statement and recursive data structure
 * for statements which are children of a statement (formula in a lemma and lemma can be in a proof).
 */
public class ParsedStatement {
	
	private String content; 
	private int characterLocation;
	protected ArrayList<ParsedStatement> children;

	/**
	 * Constructor using content and character location. Initializes with empty children list
	 * @param content string content of the statement
	 * @param characterLocation starting location of the statement
	 */
	public ParsedStatement(String content, int characterLocation) {
		this.content = content;
		this.characterLocation = characterLocation;
		this.children = new ArrayList<ParsedStatement>();
	}

	/**
	 * Constructor using content of the statement, starting location of the statement and a list of children, which
	 * are to be added as the children of the statement
	 * @param content string content of the statement
	 * @param characterLocation starting location of the statement
	 * @param children List of statements, which are children of the instance of ParsedStatement.
	 */
	public ParsedStatement(String content, int characterLocation, ArrayList<ParsedStatement> children) {
		this.content = content;
		this.characterLocation = characterLocation;
		this.children = children;
	}

	/**
	 * Getter function the get the content of the statement
	 * @return string which is the content of the statement
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Setter function to set the string content of the statement
	 * @param content string which is the content of the statement
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets the starting location of the statement
	 * @return character location where the statement begins in the document
	 */
	public int getCharacterLocation() {
		return characterLocation;
	}

	/**
	 * Setter function to set the starting location of the statement
	 * @param characterLocation starting location of the statement
	 */
	public void setCharacterLocation(int characterLocation) {
		this.characterLocation = characterLocation;
	}

	/**
	 * Gets all ParsedStatement's instance's children
	 * @return a list containing all the children of the statement
	 */
	public ArrayList<ParsedStatement> getChildren() {
		return children;
	}

	/**
	 * Instance method to add a child to a statement
	 * @param child ParsedStatement instance which is added
	 */
	public void addChild(ParsedStatement child){
		children.add(child);
	}
	
	public int getChildCount() {
		return children.size();
	}

	/**
	 * Gets the child of the statement with index i.
	 * @param i index of the child
	 * @return a child of statement with index i
	 */
	public ParsedStatement getChild(int i) {
		return children.get(i);
	}

	/**
	 * @return returns string representation of the document's syntax tree
	 */
	public String toString() {
    	StringBuilder sb = new StringBuilder();
        print("", true, sb);
        return sb.toString();
    }
	//Credit to Vasya Novikov - http://stackoverflow.com/a/8948691
	/*private void print(String prefix, boolean isTail, StringBuilder sb) {
        sb.append(prefix + (isTail ? "└── " : "├── ") + this.getClass().getSimpleName()+"\n");
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false, sb);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1).print(prefix + (isTail ?"    " : "│   "), true, sb);
        }
    }*/

	/**
	 * This print method is called from toString method.
	 * This method recursively builds a beautified tree structure of the document's syntax tree.
	 * @param prefix text appended before the node information, such as spacing
	 * @param isTail used to show the parent-children relationship if such exist.
	 * @param sb string that is built using the function
	 */
    private void print(String prefix, boolean isTail, StringBuilder sb) {
    	String text = "";
    	if (this instanceof FormulaStatement || this instanceof IncludeStatement || this instanceof DeclareStatement)
    		text = ": " + this.getContent();
        sb.append(prefix + (isTail ? "└── " : "├── ") + this.getClass().getSimpleName()+text+"\n");
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false, sb);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1).print(prefix + (isTail ?"    " : "│   "), true, sb);
        }
    }

}
