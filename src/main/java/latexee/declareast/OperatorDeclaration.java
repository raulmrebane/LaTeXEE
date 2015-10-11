package main.java.latexee.declareast;

import main.antlrgen.DocumentGrammarParser.LastpairContext;
import main.antlrgen.DocumentGrammarParser.PairContext;
import main.antlrgen.DocumentGrammarParser.SyntaxBracketContext;

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
		
		//Yesterday, this was a formality. Today it guards us from the hell of nullpointers.
		if(this.type == null){
			throw new RuntimeException("Type field was not instantiated on operator declaration: "+tree.getText());
		}
		if(this.priority == null){
			throw new RuntimeException("Priority field was not instantiated on operator declaration: "+tree.getText());
		}
		if(this.meaning == null){
			throw new RuntimeException("Meaning field was not instantiated on operator declaration: "+tree.getText());
		}
		if(this.operator == null){
			throw new RuntimeException("Operator character field was not instantiated on operator declaration: "+tree.getText());
		}
		if(this.associativity == null && type.equals("infix")){
			throw new RuntimeException("Associativity field was not instantiated on infix operator declaration: "+tree.getText());
		}
			
			
	}

	private void fillAttributes(ParseTree tree){
		if(tree instanceof PairContext || tree instanceof LastpairContext){
			String key = tree.getChild(0).getText();
			String value = tree.getChild(2).getText();
			if(key.equals("meaning")){
				this.meaning=value;
			}
		}
		if(tree instanceof SyntaxBracketContext){
			this.type = tree.getChild(1).getText();
			this.priority = Integer.parseInt(tree.getChild(3).getText());
			String roughOp = tree.getChild(5).getText();
			this.operator = roughOp.substring(1, roughOp.length()-1);
			if(tree.getChildCount()==9){
				this.associativity = tree.getChild(7).getText();
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
