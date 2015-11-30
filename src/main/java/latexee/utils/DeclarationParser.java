package main.java.latexee.utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import main.java.antlrgen.DeclarationGrammarLexer;
import main.java.antlrgen.DeclarationGrammarParser;
import main.java.antlrgen.DeclarationGrammarParser.MiscPairContext;
import main.java.antlrgen.DeclarationGrammarParser.SyntaxBracketContext;
import main.java.latexee.declareast.DeclarationInitialisationException;
import main.java.latexee.declareast.MacroDeclaration;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.LemmaStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.docast.TheoremStatement;
import main.java.latexee.logging.Logger;

public class DeclarationParser {
	//goes through the AST and parses each DeclarationStatement. 
	//Can be later integrated into a function that does everything in one run through the AST for performance.
	public static void declarationFinder(ParsedStatement node){
		declarationFinder(node, 0);
	}
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
		for(int i=0;i<children.size();i++){
			childMax = declarationFinder(children.get(i),childMax);
		}
		maxId = childMax;
		return maxId;
		
	}

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
	    if (del.foundErrors()) //TODO: logimine hiljem? Makrode/dekl-de juures?
	    	Logger.log("Parsing finished with errors.");
	    else
	    	Logger.log("Parsing successful.\n");
	    return tree;
	}

	//Document parsing is broken, so I have to resort to this. This will later join other similar test cases.
	public static ParsedStatement giveMeTheTestCase(){
		ParsedStatement root = new ParsedStatement("placeholder", 0);
		
		TheoremStatement theorem1 = new TheoremStatement("$2+3$$$2+5$$",20);
		theorem1.getChildren().add(new DeclareStatement("{syntax={infix,7,\"/\",l}, meaning=artih1.divide}", 23));
		theorem1.getChildren().add(new DeclareStatement("{syntax={infix,5,\"+\",l}, meaning=arith1.plus}", 23));
		theorem1.getChildren().add(new DeclareStatement("{syntax={infix,5,\"-\",l}, meaning=arith1.minus}", 23));
		theorem1.getChildren().add(new DeclareStatement("{syntax={infix,7,\"*\",l}, meaning=arith1.times}", 23));
		theorem1.getChildren().add(new DeclareStatement("{macro=\\gcd, meaning=arith1.gcd,    argspec=[2][optional], code={...}}", 80));
		theorem1.getChildren().add(new FormulaStatement("$2+7$", 23));
		theorem1.getChildren().add(new FormulaStatement("$$2+5$$", 33));
		
		LemmaStatement lemma = new LemmaStatement("placeholder",30);
		lemma.getChildren().add(new DeclareStatement("{macro=\\gcd, meaning=arith1.gcd,    argspec=[2][optional], code={...}}", 80));
		lemma.getChildren().add(new DeclareStatement("{macro=\\gcd2, meaning=arith1.gcd2,    argspec=[2][optional], code={...}}", 80));

		//lemma.getChildren().add(new DeclareStatement("\\declare{macro=asd, meaning=asdasd,    argspec=[2], code={\\alright{then}{\\what{is}{\\nesting}}}", 80));
		lemma.getChildren().add(new FormulaStatement("$$2+5$$", 33));
		
		root.getChildren().addAll(Arrays.asList(theorem1,lemma));
		return root;
	}
}
