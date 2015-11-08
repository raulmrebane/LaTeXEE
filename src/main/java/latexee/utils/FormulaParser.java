package main.java.latexee.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import fr.inria.openmath.omapi.Node;
import fr.inria.openmath.omapi.implementation.NodeImpl;
import fr.inria.openmath.omapi.implementation.TreePrinterImpl;
import fr.inria.openmath.omapi.implementation.XMLPrinter;
import main.java.latexee.declareast.DeclarationInitialisationException;
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
	private static TreePrinterImpl treePrinter;
	
	public static void parse(ParsedStatement root,List<DeclareNode> declarations){
		
		if(root instanceof DeclareStatement){
			
			DeclareStatement castNode = (DeclareStatement) root;
			ParseTree parseTree = DeclarationParser.parseDeclaration(castNode.getContent());
			boolean operatorStyle = DeclarationParser.isOperatorSyntax(parseTree);
			
			DeclareNode node = null;
			
			if (operatorStyle){
				try {
					node = new OperatorDeclaration(parseTree);
					declarations.add(node);
				}
				catch (DeclarationInitialisationException die) {
				}
			}
			else {
				try {
					node = new MacroDeclaration(parseTree);
					declarations.add(node);
				}
				catch (DeclarationInitialisationException die) {
				}
			}
			
			
			
		}
		else if(root instanceof FormulaStatement){
			String grammar = GrammarGenerator.createGrammar(declarations);
			try {
				ParseTree formulaTree = GrammarCompiler.compile(grammar, root.getContent());
				Node formulaNode = OpenMathTranslator.parseToOM(formulaTree, declarations);
				Node formulaRootNode = new NodeImpl(Node.OM_OBJECT);
				formulaRootNode.appendChild(formulaNode);
				treePrinter.printTree(formulaRootNode);
			} catch (IOException e) {
				Logger.log("IO exception when parsing formula: "+root.getContent());
				e.printStackTrace();
			} catch (NullPointerException e) {
				//Do nothing, logging errors already covered
			} finally{
				OpenMathTranslator.bracketFlags.clear();
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
	public static void setFilename(String filename) throws FileNotFoundException{
		 treePrinter = new TreePrinterImpl(new XMLPrinter(new FileOutputStream(filename)));
	}
	public static void donePrinting(){
		treePrinter.endPrint();
	}
}
