package mainPackage;

import java.util.ArrayList;

public class LemmaStatement extends ParsedStatement {
	
	private ArrayList<ParsedStatement> children;

	public LemmaStatement(String content, int characterLocation,
			ArrayList<ParsedStatement> children) {
		super(content, characterLocation);
		this.children = children;
	}

	public ArrayList<ParsedStatement> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<ParsedStatement> children) {
		this.children = children;
	}
	
	

}
