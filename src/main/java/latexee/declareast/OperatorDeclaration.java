package main.java.latexee.declareast;

import main.java.antlrgen.DeclarationGrammarParser.ImportantPairContext;
import main.java.antlrgen.DeclarationGrammarParser.MiscPairContext;
import main.java.antlrgen.DeclarationGrammarParser.SyntaxBracketContext;
import main.java.antlrgen.DeclarationGrammarParser.ValueInBracesContext;
import main.java.latexee.exceptions.DeclarationInitialisationException;
import main.java.latexee.logging.Logger;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * OperatorDeclaration is a class, which inherits from DeclareNode.
 * OperatorDeclaration holds information about user-defined operators, like
 * \declare{syntax={infix, 7, /, l}, meaning=arith1.divide}
 * Instances of this class hold information about operator's meaning, type (infix/prefix/etc),
 * parsing priority, character(s) used by the operator, associativity (left- or right-associative)
 * content dictionary and misc information.
 */
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

	/**
	 * Constructor for OperatorDeclaration.
	 * @param meaning meaning of the operator (meaning=arith1.divide)
	 * @param type type of the operator (syntax={infix,...}
	 * @param priority priority of the operator (syntax={.., 7, ..})
	 * @param operator characters representing the (syntax={...,/,...}
	 * @param associativity associativity of the operator (syntax={..., l})
	 */
	public OperatorDeclaration(String meaning, String type, Integer priority, String operator,
			String associativity) {
		super();
		this.meaning = meaning;
		this.type = type;
		this.priority = priority;
		this.operator = operator;
		this.associativity = associativity;
	}

	/**
	 * Constructor for OperatorDeclaration, which parses the given parse tree to get the required information.
	 * @param tree parse tree of the OperatorDeclaration
	 * @param identifier identifier of the instance
	 * @throws DeclarationInitialisationException unfit parameter(s) for OperatorDeclaration or unable to get the required information from parse tree.
	 */
	public OperatorDeclaration(ParseTree tree, int identifier) throws DeclarationInitialisationException{
		fillAttributes(tree);
		this.id = "Op"+Integer.toString(identifier);
		//Yesterday, this was a formality. Today it guards us from the hell of nullpointers.
		if(this.type == null){
			Logger.log("Type field was not instantiated on operator declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.priority == null){
			Logger.log("Priority field was not instantiated on operator declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.meaning == null && this.contentDictionary == null){
			Logger.log("OpenMath meaning field was not instantiated on operator declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.meaning instanceof String && this.contentDictionary == null){
			Logger.log("OpenMath meaning given but content dictionary missing on declaration: "+tree.getText());
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

	/**
	 * Gets the {@link String} instance of OperatorDeclaration's instance's name.
	 * @return {@link String} instance of OperatorDeclaration's instance's name.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Private function (recursive) used by the OperatorDeclaration's constructor.
	 * Gets the required information (meaning, name, etc) by traversing the parse tree which is used to build the instance of OperatorDeclaration.
	 * @param tree parse tree of the OperatorDeclaration
	 * @throws DeclarationInitialisationException unable to get the required information to build the instance of OperatorDeclaration.
	 */
	private void fillAttributes(ParseTree tree) throws DeclarationInitialisationException{
		if(tree instanceof SyntaxBracketContext){
			//This is a loop to essentially ignore whitespace in the syntax bracket.
			//indexCounter only increases as non-whitespace characters are found
			//therefore it acts as an index for non-whitespace characters
			//However as the { is counted as a non-whitespace character, it is 1-indexed
			//Instead of 0-indexed like other tree nodes
			
			if (this.type != null || this.priority != null || this.operator != null) {
				Logger.log("Multiple instances of syntax.");
				throw new DeclarationInitialisationException();
			}
			
			int indexCounter = 0;
			
			for(int i=0;i<tree.getChildCount();i++){

				if(!WHITESPACECHARS.contains(tree.getChild(i).getText())){
					indexCounter++;
					String text = tree.getChild(i).getText();
					switch (indexCounter){
					case TYPEINDEX:
						this.type = text;
						break;
					case PRIORITYINDEX:
						this.priority = Integer.parseInt(text);
						break;
					case OPERATORINDEX:
						String roughOp = text;
						if (text.length() < 3) {
							Logger.log("No operator given.");
							throw new DeclarationInitialisationException();
						}
						if (text.indexOf('$') > -1) {
							Logger.log("No '$' allowed within operators.");
							throw new DeclarationInitialisationException();
						}
						if (text.length() - text.replaceAll("\\%", "").length() > 0) {
							Logger.log("No '%' allowed within operators.");
							throw new DeclarationInitialisationException();
						}
						this.operator = roughOp.substring(1, roughOp.length()-1);
						break;
					case ASSOCIATIVITYINDEX:
						if (this.type.equals("infix"))
							this.associativity = text;
						else {
							Logger.log("No associativity allowed for a " + this.type + " operator.");
							throw new DeclarationInitialisationException();
						}
						break;
					}
				}
			}
		}
		else if(tree instanceof ImportantPairContext){
			String key = tree.getChild(0).getText();
			ParseTree valueNode = tree.getChild(2);
			String value = valueNode.getText();
			if(key.equals("meaning")){
				if (this.meaning != null) {
					Logger.log("Multiple instances of meaning.");
				}
				if(valueNode instanceof ValueInBracesContext){
					this.meaning=getTree(value);
				}else{
					this.contentDictionary=value;
					this.meaning=tree.getChild(4).getText();
				}
			}
		}
		else if(tree instanceof MiscPairContext){
			String key = tree.getChild(0).getText();
			if (this.miscellaneous.get(key) != null) {
				Logger.log("Multiple instances of " + key +".");
				throw new DeclarationInitialisationException();
			}
			if (key.equals("syntax")) {
				Logger.log("Faulty syntax. Should be {infix, <Integer>, \"<operator>\", (l|r)} or {(prefix|postfix), <Integer>, \"<operator>\"}");
				throw new DeclarationInitialisationException();
			}
			String value = tree.getChild(2).getText();
			this.miscellaneous.put(key,value);
		}	
		for(int i=0;i<tree.getChildCount();i++){
			fillAttributes(tree.getChild(i));
		}
	}
	//only increments the priority by one.

	/**
	 * Method to translate the OperatorDeclaration object to a grammar rule, which
	 * is used to write the grammar rules.
	 * Increments the operator priority counter by one, so we know how to link the priorities in grammar.
	 * @return string representation of the OperatorDeclaration's grammar rule.
	 */
	@Override
	public String toGrammarRule() {
		StringBuilder sb = new StringBuilder();
		String operatorToken = "";
		char[] tokens = operator.toCharArray();
		for (char c : tokens) {
			operatorToken += "\'" + c + "\'";
		}
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
			sb.append(currentLevel);
			sb.append(operatorToken);
		}
		else if(this.type.equals("prefix")){
			sb.append(operatorToken);
			sb.append(currentLevel);
		}
		sb.append(" #"+this.id+"\n");
		return sb.toString();
	}

	/**
	 *
	 * Method to translate the OperatorDeclaration object to a grammar rule, which
	 * is used to write the grammar rules.
	 * @param nextPriority priority of the next grammar rule - which level to point to.
	 * @return string representation of the OperatorDeclaration's grammar rule.
	 */
	public String toGrammarRule(Integer nextPriority) {
		StringBuilder sb = new StringBuilder();
		String operatorToken = "";
		char[] tokens = operator.toCharArray();
		for (char c : tokens) {
			operatorToken += "\'" + c + "\'";
		}
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
			sb.append(currentLevel);
			sb.append(operatorToken);
		}
		else if(this.type.equals("prefix")){
			sb.append(operatorToken);
			sb.append(currentLevel);
		}
		sb.append(" #"+this.id+"\n");
		return sb.toString();
	}

	/**
	 * @return priority of the operator
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @return type of the operator
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return character(s) which correspond to the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @return associativity of the operator
	 */
	public String getAssociativity() {
		return associativity;
	}
}
