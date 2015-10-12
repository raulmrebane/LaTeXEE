package main.java.latexee.docast;

import java.util.ArrayList;

public class ParsedStatement {
	
	private String content; 
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
	
	public int getChildCount() {
		return children.size();
	}
	
	public ParsedStatement getChild(int i) {
		return children.get(i);
	}
	
	public String toString() {
    	StringBuilder sb = new StringBuilder();
        print("", true, sb);
        return sb.toString();
    }
	//Credit to Vasya Novikov - http://stackoverflow.com/a/8948691
    private void print(String prefix, boolean isTail, StringBuilder sb) {
        sb.append(prefix + (isTail ? "└── " : "├── ") + this.getClass().getSimpleName()+"\n");
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false, sb);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1).print(prefix + (isTail ?"    " : "│   "), true, sb);
        }
    }

}
