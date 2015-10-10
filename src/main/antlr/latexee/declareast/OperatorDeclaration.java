package main.antlr.latexee.declareast;

import main.antlrgen.DeclarationGrammarParser.MeaningComponentContext;
import main.antlrgen.DeclarationGrammarParser.SyntaxBracketContext;

import org.antlr.v4.runtime.tree.ParseTree;

public class OperatorDeclaration extends DeclareNode {
	
	private String type; //infix/prefix/etc
	private Integer priority; //parsing priority
	private String operator; //The actual character, for example "+"
	private String associativity; //r or l, meaning right- or left-associative
	
	public OperatorDeclaration(String meaning, String type, Integer priority, String operator,
			String associativity) {
		super();
		this.meaning = meaning;
		this.type = type;
		this.priority = priority;
		this.operator = operator;
		this.associativity = associativity;
	}
	public OperatorDeclaration(ParseTree tree){
		fillAttributes(tree);
		
		//Just in case, there is a check to make sure all fields are instantiated.
		if(
				this.type == null ||
				this.priority == null ||
				this.operator == null ||
				this.meaning == null ||
				this.associativity == null){
			//However, this should never happen. As I write this, I'm sure it will some day.
			throw new RuntimeException("Not all fields were instantiated on operator declaration: "+tree.getText());
		}
	}

	private void fillAttributes(ParseTree tree){
		if(tree instanceof SyntaxBracketContext){
			this.type = tree.getChild(0).getText();
			this.priority = Integer.parseInt(tree.getChild(2).getText());
			String roughOp = tree.getChild(4).getText();
			this.operator = roughOp.substring(1, roughOp.length()-1);
			this.associativity = tree.getChild(6).getText();
		}
		if(tree instanceof MeaningComponentContext){
			this.meaning = tree.getChild(1).getText();
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
