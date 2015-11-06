package main.java.latexee.declareast;

import main.java.antlrgen.DeclarationGrammarParser.ImportantPairContext;
import main.java.antlrgen.DeclarationGrammarParser.MiscPairContext;
import main.java.antlrgen.DeclarationGrammarParser.SyntaxBracketContext;

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
		this.id = "Op"+Integer.toString(identifier);
		identifier++;
		//Yesterday, this was a formality. Today it guards us from the hell of nullpointers.
		if(this.type == null){
			throw new RuntimeException("Type field was not instantiated on operator declaration: "+tree.getText());
		}
		if(this.priority == null){
			throw new RuntimeException("Priority field was not instantiated on operator declaration: "+tree.getText());
		}
		if(this.meaning == null || this.contentDictionary == null){
			throw new RuntimeException("OpenMath meaning field was not instantiated on operator declaration: "+tree.getText());
		}
		if(this.operator == null){
			throw new RuntimeException("Operator character field was not instantiated on operator declaration: "+tree.getText());
		}
		if(this.associativity == null && type.equals("infix")){
			throw new RuntimeException("Associativity field was not instantiated on infix operator declaration: "+tree.getText());
		}
			
			
	}
	public String getId() {
		return id;
	}
	private void fillAttributes(ParseTree tree){
		if(tree instanceof SyntaxBracketContext){
			this.type = tree.getChild(1).getText();
			this.priority = Integer.parseInt(tree.getChild(3).getText());
			String roughOp = tree.getChild(5).getText();
			this.operator = roughOp.substring(1, roughOp.length()-1);
			if(tree.getChildCount()==9){
				this.associativity = tree.getChild(7).getText();
			}
		}
		else if(tree instanceof ImportantPairContext){
			String key = tree.getChild(0).getText();
			String value = tree.getChild(2).getText();
			if(key.equals("meaning")){
				this.contentDictionary=value;
				this.meaning = tree.getChild(4).getText();
			}
		}
		else if(tree instanceof MiscPairContext){
			String key = tree.getChild(0).getText();
			String value = tree.getChild(2).getText();
			this.miscellaneous.put(key,value);
		}	
		for(int i=0;i<tree.getChildCount();i++){
			fillAttributes(tree.getChild(i));
		}
	}
	//only increments the priority by one.
	@Override
	public String toGrammarRule() {
		StringBuilder sb = new StringBuilder();
		String operatorToken = "\'"+this.operator+"\'";
		String currentLevel = "level"+priority.toString().replace('-', '_');
		String lowerLevel = "level"+Integer.toString(priority+1).replace('-', '_');
		if(this.type.equals("infix")){
			if(this.associativity.equals("l")){
				sb.append(currentLevel);
				sb.append(operatorToken);
				sb.append(lowerLevel);
			}
			if(this.associativity.equals("r")){
				sb.append(lowerLevel);
				sb.append(operatorToken);
				sb.append(currentLevel);
			}
		}
		else if(this.type.equals("postfix")){
			sb.append(lowerLevel);
			sb.append(operatorToken);
		}
		else if(this.type.equals("prefix")){
			sb.append(operatorToken);
			sb.append(lowerLevel);
		}
		sb.append(" #"+this.id+"\n");
		return sb.toString();
	}
	
	public String toGrammarRule(Integer nextPriority) {
		StringBuilder sb = new StringBuilder();
		String operatorToken = "\'"+this.operator+"\'";
		String currentLevel = "level"+priority.toString().replace('-', '_');
		String lowerLevel = "level"+Integer.toString(nextPriority).replace('-', '_');
		if(this.type.equals("infix")){
			if(this.associativity.equals("l")){
				sb.append(currentLevel);
				sb.append(operatorToken);
				sb.append(lowerLevel);
			}
			if(this.associativity.equals("r")){
				sb.append(lowerLevel);
				sb.append(operatorToken);
				sb.append(currentLevel);
			}
		}
		else if(this.type.equals("postfix")){
			sb.append(lowerLevel);
			sb.append(operatorToken);
		}
		else if(this.type.equals("prefix")){
			sb.append(operatorToken);
			sb.append(lowerLevel);
		}
		sb.append(" #"+this.id+"\n");
		return sb.toString();
	}
	public Integer getPriority() {
		return priority;
	}
	public String getType() {
		return type;
	}
}
