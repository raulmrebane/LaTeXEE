package main.java.latexee.docast;

public class FormulaStatement extends ParsedStatement {

	public FormulaStatement(String content, int characterLocation) {
		super(content, characterLocation);
	}
	
	public String toString() {
		return "[Formula: " + super.toString();
	}

}