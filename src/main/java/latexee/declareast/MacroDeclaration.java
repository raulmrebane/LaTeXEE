package main.java.latexee.declareast;



import main.antlrgen.DocumentGrammarParser.PairContext;

import org.antlr.v4.runtime.tree.ParseTree;

public class MacroDeclaration extends DeclareNode {
	
	private String macroName; //the name of the macro - the characters following \
	private Integer arguments; 
	
	public MacroDeclaration(String meaning, String macroname, int arguments) {
		super();
		this.meaning=meaning;
		this.macroName=macroname;
		this.arguments=arguments;
	}
	
	public MacroDeclaration(ParseTree tree){
		fillAttributes(tree);
		if(		this.meaning == null||
				this.macroName == null||
				this.arguments == null){
			//Yesterday, this was a formality. Today it guards us from the hell of nullpointers.
			throw new RuntimeException("Not all fields were instantiated on macro declaration: "+tree.getText());
		}
	}
	
	private void fillAttributes(ParseTree tree){
		if(tree instanceof PairContext){
			
			String key = tree.getChild(0).getText();
			String value = tree.getChild(2).getText();
			
			switch(key){
			case "macro":
				this.macroName=value;
				break;
			case "argspec":
				this.arguments=Integer.parseInt(value);
				break;
			case "meaning":
				this.meaning=value;
			}
		}
		for(int i=0;i<tree.getChildCount();i++){
			fillAttributes(tree.getChild(i));
		}
	}
	
	@Override
	public String toGrammarRule() {
		// TODO Auto-generated method stub
		return null;
	}

}
