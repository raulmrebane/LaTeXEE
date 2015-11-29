package main.java.latexee.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private TreePrinterImpl treePrinter;
	private GrammarCompiler cp;
	private int nodeId;
	
	public FormulaParser(String filename) throws FileNotFoundException{
		this.treePrinter = new TreePrinterImpl(new XMLPrinter(new FileOutputStream(filename)));
		this.cp = new GrammarCompiler();
		this.nodeId = 0;
	}
	public void parse(ParsedStatement root){
		parseImpl(root,new HashMap<String,DeclareNode>());
		treePrinter.endPrint();
	}
	
	public void parseImpl(ParsedStatement root,Map<String,DeclareNode> declarations){
		
		if(root instanceof DeclareStatement){
			
			DeclareStatement castNode = (DeclareStatement) root;
			ParseTree parseTree = DeclarationParser.parseDeclaration(castNode.getContent());
			boolean operatorStyle = DeclarationParser.isOperatorSyntax(parseTree);
			
			DeclareNode node = null;
			
			if (operatorStyle){
				try {
					node = new OperatorDeclaration(parseTree,nodeId);
					nodeId++;
					String id = node.getId();
					declarations.put(id, node);
				}
				catch (DeclarationInitialisationException die) {
				}
			}
			else {
				try {
					node = new MacroDeclaration(parseTree,nodeId);
					nodeId++;
					String id = node.getId();
					declarations.put(id, node);
				}
				catch (DeclarationInitialisationException die) {
				}
			}
			
			
			
		}
		else if(root instanceof FormulaStatement){
			List<DeclareNode> nodes = new ArrayList<DeclareNode>(declarations.values());
			String grammar = GrammarGenerator.createGrammar(nodes);
			try {
				ParseTree formulaTree = cp.compile(grammar, root.getContent());
				if (formulaTree != null) {
					Node formulaNode = OpenMathTranslator.parseToOM(formulaTree, declarations);
					Node formulaRootNode = new NodeImpl(Node.OM_OBJECT);
					formulaRootNode.appendChild(formulaNode);
					treePrinter.printTree(formulaRootNode);
				}
			} catch (IOException e) {
				Logger.log("IO exception when parsing formula: "+root.getContent());
				e.printStackTrace();
			} finally{
				OpenMathTranslator.bracketFlags.clear();
			}
		}
		else if(root instanceof TheoremStatement ||
				root instanceof LemmaStatement ||
				root instanceof ProofStatement){
			declarations = new HashMap<String,DeclareNode>(declarations);
		}
		for(ParsedStatement child : root.getChildren()){
			parseImpl(child,declarations);
		}
			
	}
}
