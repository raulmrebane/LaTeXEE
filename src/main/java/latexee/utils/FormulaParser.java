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
	private boolean ambiguityChecking;
	private int parsedFormulas;
	private int successfullyParsedFormulas;
	private int parsedDeclarations; //TODO: või makrodele ja operaatoritele eraldi?
	private int successfullyParsedDeclarations;
	
	public FormulaParser(String filename) throws FileNotFoundException{
		this.treePrinter = new TreePrinterImpl(new XMLPrinter(new FileOutputStream(filename)));
		this.cp = new GrammarCompiler();
		this.nodeId = 0;
		this.ambiguityChecking=false;
		this.parsedFormulas = 0;
		this.successfullyParsedFormulas = 0;
		this.parsedDeclarations = 0;
		this.successfullyParsedDeclarations = 0;
	}
	public void parse(ParsedStatement root){
		parseImpl(root,new HashMap<String,DeclareNode>());
		treePrinter.endPrint();
		Logger.log(successfullyParsedDeclarations + "/" + parsedDeclarations + " declarations parsed successfully.");
		Logger.log(successfullyParsedFormulas + "/" + parsedFormulas + " formulas parsed successfully.");
	}
	
	public void parseImpl(ParsedStatement root,Map<String,DeclareNode> declarations){
		
		if(root instanceof DeclareStatement){
			
			parsedDeclarations++;
			DeclareStatement castNode = (DeclareStatement) root;
			DeclarationParser dp = new DeclarationParser();
			dp.parseDeclaration(castNode.getContent());
			ParseTree parseTree = dp.getDeclaration();
			//ParseTree parseTree = DeclarationParser.parseDeclaration(castNode.getContent());
			boolean operatorStyle = DeclarationParser.isOperatorSyntax(parseTree);
			
			DeclareNode node = null;
			
			if (operatorStyle){
				try {
					Logger.log("Parsing an operator."); //TODO: change? tegelt parsimine on tehtud juba, ainult käsitsi parsimine veel
					node = new OperatorDeclaration(parseTree,nodeId);
					nodeId++;
					String id = node.getId();
					declarations.put(id, node);
					Logger.log("Parsing successful.\n");
					successfullyParsedDeclarations++;
				}
				catch (DeclarationInitialisationException die) {
					Logger.log("Parsing finished with errors.\n");
				}
			}
			else {
				try {
					Logger.log("Parsing a macro.");
					node = new MacroDeclaration(parseTree,nodeId);
					nodeId++;
					String id = node.getId();
					declarations.put(id, node);
					Logger.log("Parsing successful.\n");
					successfullyParsedDeclarations++;
				}
				catch (DeclarationInitialisationException die) {
					Logger.log("Parsing finished with errors.\n");
				}
			}
			
			
			
		}
		else if(root instanceof FormulaStatement){
			List<DeclareNode> nodes = new ArrayList<DeclareNode>(declarations.values());
			String grammar = GrammarGenerator.createGrammar(nodes);
			try {
				parsedFormulas++;
				ParseTree formulaTree = cp.compile(grammar, root.getContent());
				if (formulaTree != null) {
					successfullyParsedFormulas++;
					Node formulaNode = OpenMathTranslator.parseToOM(formulaTree, declarations);
					Node formulaRootNode = new NodeImpl(Node.OM_OBJECT);
					formulaRootNode.appendChild(formulaNode);
					treePrinter.printTree(formulaRootNode);
					if(ambiguityChecking){
						AmbiguityChecker.check(formulaTree,declarations);
					}
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
	public void enableAmbiguityChecking(){
		this.ambiguityChecking=true;
	}
}
