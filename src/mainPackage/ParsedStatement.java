package mainPackage;

import java.util.ArrayList;

public class ParsedStatement {
	
	private String content;
	private int characterLocation;

	public ParsedStatement(String content, int characterLocation) {
		super();
		this.content = content;
		this.characterLocation = characterLocation;
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
	

}
