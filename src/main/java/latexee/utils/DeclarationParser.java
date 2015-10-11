package main.java.latexee.utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import main.antlrgen.DocumentGrammarLexer;
import main.antlrgen.DocumentGrammarParser;
import main.antlrgen.DocumentGrammarParser.SyntaxBracketContext;
import main.java.latexee.declareast.MacroDeclaration;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.LemmaStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.docast.TheoremStatement;

public class DeclarationParser {
	
	//goes through the AST and parses each DeclarationStatement. 
	//Can be later integrated into a function that does everything in one run through the AST for performance.
	
	public static void declarationFinder(ParsedStatement node){
		if(node instanceof DeclareStatement){
			DeclareStatement castNode = (DeclareStatement) node;
			ParseTree parseTree = parseDeclaration(castNode.getContent());
			boolean operatorStyle = isOperatorSyntax(parseTree);
			if(operatorStyle){
				castNode.setNode(new OperatorDeclaration(parseTree));
			}else{
				castNode.setNode(new MacroDeclaration(parseTree));
			}
			
		}
		
		ArrayList<ParsedStatement> children = node.getChildren();
		for(int i=0;i<children.size();i++){
			declarationFinder(children.get(i));
		}
		
	}
	
	public static boolean isOperatorSyntax(ParseTree tree){
		boolean foundOperator = false;
		if(tree instanceof SyntaxBracketContext){
			foundOperator = true;
		}
		int childCount = tree.getChildCount();
		for(int i=0;i<childCount;i++){
			foundOperator = foundOperator || isOperatorSyntax(tree.getChild(i));
		}
		return foundOperator;
	}
	
	//Generates the ANTLR parse tree for each declaration string
	
	public static ParseTree parseDeclaration(String rule){
		ANTLRInputStream antlrInput = new ANTLRInputStream(rule);
	    DocumentGrammarLexer lexer = new DocumentGrammarLexer(antlrInput);
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	    DocumentGrammarParser parser = new DocumentGrammarParser(tokens);
	    ParseTree tree = parser.declarationGrammar();
	    return tree;
	}
	
	//Document parsing is broken, so I have to resort to this. This will later join other similar test cases.
	public static ParsedStatement giveMeTheTestCase(){
		ParsedStatement root = new ParsedStatement("placeholder", 0);
		
		TheoremStatement theorem1 = new TheoremStatement("$2+3$$$2+5$$",20);
		theorem1.getChildren().add(new DeclareStatement("\\declare{syntax={infix,7,\"/\",l}, meaning=artih1.divide}", 23));
		theorem1.getChildren().add(new DeclareStatement("\\declare{syntax={prefix,7,\"/\"}, meaning=artih1.divide}", 23));
		theorem1.getChildren().add(new FormulaStatement("$2+3$", 23));
		theorem1.getChildren().add(new FormulaStatement("$$2+5$$", 33));
		
		LemmaStatement lemma = new LemmaStatement("placeholder",30);
		lemma.getChildren().add(new DeclareStatement("\\declare{macro=asd, meaning=asdasd,    argspec=[2], code={...}}", 80));
		lemma.getChildren().add(new DeclareStatement("\\declare{macro=asd, meaning=asdasd,    argspec=[2], code={\\alright{then}{\\what{is}{\\nesting}}}", 80));
		theorem1.getChildren().add(new FormulaStatement("$$2+5$$", 33));
		
		root.getChildren().addAll(Arrays.asList(theorem1,lemma));
		return root;
	}
}
