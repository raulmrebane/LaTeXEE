package main.antlr.latexee.declareast;

import main.antlrgen.DeclarationGrammarParser.ArgSpecComponentContext;
import main.antlrgen.DeclarationGrammarParser.CodeComponentContext;
import main.antlrgen.DeclarationGrammarParser.MacroComponentContext;
import main.antlrgen.DeclarationGrammarParser.MeaningComponentContext;

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
		//Just in case, there is a check to make sure all fields are instantiated.
		if(		this.meaning == null||
				this.macroName == null||
				this.arguments == null){
			//However, this should never happen. As I write this, I'm sure it will some day.
			throw new RuntimeException("Not all fields were instantiated on macro declaration: "+tree.getText());
		}
	}
	
	private void fillAttributes(ParseTree tree){
		if(tree instanceof MacroComponentContext){
			this.macroName = tree.getChild(1).getText();
		}
		if(tree instanceof MeaningComponentContext){
			this.meaning = tree.getChild(1).getText();
		}
		if(tree instanceof ArgSpecComponentContext){
			this.arguments = Integer.parseInt(tree.getChild(2).getText());
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
