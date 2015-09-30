package latexee;

import java.util.ArrayList;

public class ProofStatement extends ParsedStatement {
	
	private ArrayList<ParsedStatement> children;

	public ProofStatement(String content, int characterLocation,
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
