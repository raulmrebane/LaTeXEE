package main.java.latexee.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.declareast.MacroDeclaration;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.LemmaStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.docast.ProofStatement;
import main.java.latexee.docast.TheoremStatement;
import main.java.latexee.logging.Logger;

public class FormulaParser {
	public static void parse(ParsedStatement root,List<DeclareNode> declarations){
		
		if(root instanceof DeclareStatement){
			
			DeclareStatement castNode = (DeclareStatement) root;
			ParseTree parseTree = DeclarationParser.parseDeclaration(castNode.getContent());
			boolean operatorStyle = DeclarationParser.isOperatorSyntax(parseTree);
			
			DeclareNode node = null;
			
			if(operatorStyle){
				node = new OperatorDeclaration(parseTree);
			}else{
				node = new MacroDeclaration(parseTree);
			}
			
			declarations.add(node);
			
		}
		else if(root instanceof FormulaStatement){
			String grammar = GrammarGenerator.createGrammar(declarations);
			System.out.println(grammar);
			System.out.println("----");
			
			try {
				ParseTree formulaTree = GrammarCompiler.compile(declarations, grammar, root.getContent());
				System.out.println(OutputWriter.prettyParseTree(formulaTree));
			} catch (IOException e) {
				Logger.log("IO exception when parsing formula: "+root.getContent());
				e.printStackTrace();
			}
			
			
		}
		else if(root instanceof TheoremStatement ||
				root instanceof LemmaStatement ||
				root instanceof ProofStatement){
			declarations = new ArrayList<DeclareNode>(declarations);
		}
		for(ParsedStatement child : root.getChildren()){
			parse(child,declarations);
		}
			
	}
}
