package main.java.latexee.parsers;

import java.util.ArrayList;
import java.util.Arrays;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import main.java.antlrgen.DeclarationGrammarLexer;
import main.java.antlrgen.DeclarationGrammarParser;
import main.java.antlrgen.DeclarationGrammarParser.MiscPairContext;
import main.java.antlrgen.DeclarationGrammarParser.SyntaxBracketContext;
import main.java.latexee.declareast.MacroDeclaration;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.LemmaStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.docast.TheoremStatement;
import main.java.latexee.errorlisteners.DeclarationErrorListener;
import main.java.latexee.exceptions.DeclarationInitialisationException;
import main.java.latexee.logging.Logger;

/**
 * DeclarationParser class contains methods for finding and  parsing the operator and macro declarations.
 * Example of a macro declaration \declare{macro=\frac, meaning=arith1.divide, argspec=[2], code={...}}
 * Example of a operator declaration \declare{syntax={infix, 7, /, l}, meaning=arith1.divide}
 * Declarations are after translated to grammar rules.
 */
public class DeclarationParser {
	//goes through the AST and parses each DeclarationStatement. 
	//Can be later integrated into a function that does everything in one run through the AST for performance.

	/**
	 * This method traverses the given AST and finds all declarations.
	 * Work is delegated to different method with same signature.
	 * @param node entry node to tree containing all statements
	 */
	public static void declarationFinder(ParsedStatement node){
		declarationFinder(node, 0);
	}

	/**
     * This method is only used for testing purposes.
	 * Static method to find all the macro and operator declarations in the statement tree.
	 * @param node entry node to statements branch
	 * @param maxId amount of declarations parsed
	 * @return integer of how many operator or macro declarations were found
	 */
	public static Integer declarationFinder(ParsedStatement node, Integer maxId){
		if(node instanceof DeclareStatement){
			DeclareStatement castNode = (DeclareStatement) node;
			ParseTree parseTree = parseDeclaration(castNode.getContent());
			boolean operatorStyle = isOperatorSyntax(parseTree);
			if (operatorStyle){
				try {
					Logger.log("Parsing an operator."); //TODO: change?
					OperatorDeclaration opDec = new OperatorDeclaration(parseTree,maxId);
					maxId++;
					castNode.setNode(opDec);
					Logger.log("Parsing successful.\n");
				}
				catch (DeclarationInitialisationException die) {
					Logger.log("Parsing finished with errors.\n");
				}
			}
			else {
				try {
					Logger.log("Parsing a macro.");
					castNode.setNode(new MacroDeclaration(parseTree,maxId));
					maxId++;
					Logger.log("Parsing successful.\n");
				}
				catch (DeclarationInitialisationException die) {
					Logger.log("Parsing finished with errors.\n");
				}
			}			
		}
		ArrayList<ParsedStatement> children = node.getChildren();
		Integer childMax = maxId;
		for (ParsedStatement child : children) {
			childMax = declarationFinder(child,childMax);
		}
		maxId = childMax;
		return maxId;
		
	}

	/**
     * This method is only used for testing purposes.
	 * Method to find if the declaration contains operator syntax.
	 * @param tree statement node
	 * @return true if node is operator statement, false if not operator then it should be macro statement
	 */
	public static boolean isOperatorSyntax(ParseTree tree){
		boolean foundOperator = false;
		if(tree instanceof SyntaxBracketContext){
			foundOperator = true;
		}
		else if (tree instanceof MiscPairContext && tree.getChild(0).getText().equals("syntax")) //for relevant error exceptions
			foundOperator = true;
		int childCount = tree.getChildCount();
		for(int i=0;i<childCount;i++){
			foundOperator = foundOperator || isOperatorSyntax(tree.getChild(i));
		}
		return foundOperator;
	}
	
	//Generates the ANTLR parse tree for each declaration string

	/**
	 * After we have found our declarations we will generate ANTLR parse tree for each declaration string.
	 * @param rule declaration in string format
	 * @return parse tree of the declaration.
	 */
	public static ParseTree parseDeclaration(String rule){
		ANTLRInputStream antlrInput = new ANTLRInputStream(rule);
	    DeclarationGrammarLexer lexer = new DeclarationGrammarLexer(antlrInput);
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	    DeclarationGrammarParser parser = new DeclarationGrammarParser(tokens);
	    
	    Logger.log("Parsing declaration " + rule);
	    
	    DeclarationErrorListener del = new DeclarationErrorListener();
	    parser.removeErrorListeners();
		lexer.removeErrorListeners();
		parser.addErrorListener(del);
		lexer.addErrorListener(del);
		
	    ParseTree tree = parser.declarationGrammar();
	    if (del.foundErrors())
	    	Logger.log("Parsing finished with errors.");
	    else
	    	Logger.log("Parsing successful.\n");
	    return tree;
	}

}
