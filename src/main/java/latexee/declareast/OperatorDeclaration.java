package main.java.latexee.declareast;

import main.java.antlrgen.DeclarationGrammarParser.ImportantPairContext;
import main.java.antlrgen.DeclarationGrammarParser.MiscPairContext;
import main.java.antlrgen.DeclarationGrammarParser.SyntaxBracketContext;
import main.java.latexee.logging.Logger;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

public class OperatorDeclaration extends DeclareNode {
	
	private String type; //infix/prefix/etc
	private Integer priority; //parsing priority
	private String operator; //The actual character, for example "+"
	private String associativity; //r or l, meaning right- or left-associative
	
	private final int TYPEINDEX = 2;
	private final int PRIORITYINDEX = 4;
	private final int OPERATORINDEX = 6;
	private final int ASSOCIATIVITYINDEX = 8;
	private final List<String> WHITESPACECHARS = Arrays.asList(" ","\r","\n","\t");
	
	public OperatorDeclaration(String meaning, String type, Integer priority, String operator,
			String associativity) {
		super();
		this.meaning = meaning;
		this.type = type;
		this.priority = priority;
		this.operator = operator;
		this.associativity = associativity;
	}
	public OperatorDeclaration(ParseTree tree) throws DeclarationInitialisationException{
		fillAttributes(tree);
		this.id = "Op"+Integer.toString(identifier);
		identifier++;
		//Yesterday, this was a formality. Today it guards us from the hell of nullpointers.
		if(this.type == null){
			Logger.log("Type field was not instantiated on operator declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.priority == null){
			Logger.log("Priority field was not instantiated on operator declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.meaning == null || this.contentDictionary == null){
			Logger.log("OpenMath meaning field was not instantiated on operator declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.operator == null){
			Logger.log("Operator character field was not instantiated on operator declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.associativity == null && type.equals("infix")){
			Logger.log("Associativity field was not instantiated on infix operator declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
			
			
	}
	public String getId() {
		return id;
	}
	private void fillAttributes(ParseTree tree){
		if(tree instanceof SyntaxBracketContext){
			//This is a loop to essentially ignore whitespace in the syntax bracket.
			//indexCounter only increases as non-whitespace characters are found
			//therefore it acts as an index for non-whitespace characters
			//However as the { is counted as a non-whitespace character, it is 1-indexed
			//Instead of 0-indexed like other tree nodes
			
			int indexCounter = 0;
			
			for(int i=0;i<tree.getChildCount();i++){

				if(!WHITESPACECHARS.contains(tree.getChild(i).getText())){
					indexCounter++;
					switch (indexCounter){
					case TYPEINDEX:
						this.type = tree.getChild(i).getText();
						break;
					case PRIORITYINDEX:
						this.priority = Integer.parseInt(tree.getChild(i).getText());
						break;
					case OPERATORINDEX:
						String roughOp = tree.getChild(i).getText();
						this.operator = roughOp.substring(1, roughOp.length()-1);
						break;
					case ASSOCIATIVITYINDEX:
						this.associativity = tree.getChild(i).getText();
						break;
					}
				}
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
	public String getOperator() {
		return operator;
	}
	public String getAssociativity() {
		return associativity;
	}
}
