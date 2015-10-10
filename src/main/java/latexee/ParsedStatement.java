package main.java.latexee;

import java.util.ArrayList;

public class ParsedStatement {
	
	private String content; //TODO: remove? (add only to formulas, declarations and inclusions)
	private int characterLocation;
	protected ArrayList<ParsedStatement> children;

	public ParsedStatement(String content, int characterLocation) {
		this.content = content;
		this.characterLocation = characterLocation;
		this.children = new ArrayList<ParsedStatement>();
	}
	public ParsedStatement(String content, int characterLocation, ArrayList<ParsedStatement> children) {
		this.content = content;
		this.characterLocation = characterLocation;
		this.children = children;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCharacterLocation() {
		return characterLocation;
	}

	public void setCharacterLocation(int characterLocation) {
		this.characterLocation = characterLocation;
	}
	
	public ArrayList<ParsedStatement> getChildren() {
		return children;
	}
	
	public void addChild(ParsedStatement child){
		children.add(child);
	}
	
	public String toString() {
		String children = "";
		for (int i = 0; i < this.getChildren().size(); i++) {
			children += this.getChildren().get(i).toString();
		}
		return content + " " + children + "]"; //ainult ajutine
	}

}
