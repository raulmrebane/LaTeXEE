package main.java.latexee.parsers;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.symcomp.openmath.OpenMathBase;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.declareast.MacroDeclaration;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.LemmaStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.docast.ProofStatement;
import main.java.latexee.docast.TheoremStatement;
import main.java.latexee.exceptions.DeclarationInitialisationException;
import main.java.latexee.exceptions.TemplateFillException;
import main.java.latexee.logging.Logger;
import main.java.latexee.utils.AmbiguityChecker;
import main.java.latexee.utils.GrammarCompiler;
import main.java.latexee.utils.GrammarGenerator;
import main.java.latexee.utils.OpenMathTranslator;
import main.java.latexee.utils.OutputWriter;
/**
 * This class contains most of the main logic of the application.
 * Class constructor is given a file where the xml or popcorn is later written.
 * parseImpl method calls methods for declaration parsing, grammar generating and after
 * parsing formulas to xml or popcorn.
 *
 * Also keeps count of parsed formulas and declarations.
 */
public class FormulaParser {
	private Writer writer;
	private GrammarCompiler cp;
	
	private int nodeId;
	
	private boolean ambiguityChecking;
	private boolean popcornOutput;	
	
	private int parsedFormulas;
	private int successfullyParsedFormulas;
	private int parsedDeclarations; //TODO: või makrodele ja operaatoritele eraldi?
	private int successfullyParsedDeclarations;

	/**
	 * FormulaParser constructor.
	 * Creates instance of output writer, grammar compiler.
	 * @param filename file name where the output of the program is written to
	 * @throws FileNotFoundException mostly thrown when output location cant be accessed
	 * @throws UnsupportedEncodingException thrown when encoding is not supported.
	 */
	public FormulaParser(String filename) throws FileNotFoundException, UnsupportedEncodingException{
		this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
		this.cp = new GrammarCompiler();
		this.nodeId = 0;
		
		this.ambiguityChecking=false;
		this.popcornOutput=false;
		
		this.parsedFormulas = 0;
		this.successfullyParsedFormulas = 0;
		this.parsedDeclarations = 0;
		this.successfullyParsedDeclarations = 0;
	}

	/**
	 * After the Document is parsed for statements this method will be called.
	 * This method calls method parseImpl(), which does the statement tree parsing.
	 * which parses the document tree, looks for declarations, compiles grammar from them and
	 * then parses the formulas and writes to output file.
	 *
	 * @param root root
	 */
	public void parse(ParsedStatement root){
		parseImpl(root,new HashMap<String,DeclareNode>());
		try {
			writer.close();
		} catch (IOException e) {
			System.out.println("Error: Could not close output writer.");
			e.printStackTrace();
		}
		System.out.println(successfullyParsedDeclarations + "/" + parsedDeclarations + " declarations parsed successfully.");
		System.out.println(successfullyParsedFormulas + "/" + parsedFormulas + " formulas parsed successfully.");
	}

	/**
	 *
	 * This is a recursive method, which does the main document tree parsing.
	 * looks for declarations, when finding a formula, compiles grammar and
	 * then parses the formulas and writes to output file.
	 * @param root ParsedStatement node, which is traversed.
	 * @param declarations operator and macro declarations in current scope
	 */
	public void parseImpl(ParsedStatement root,Map<String,DeclareNode> declarations){

		if(root instanceof DeclareStatement){

			parsedDeclarations++;
			DeclareStatement castNode = (DeclareStatement) root;
			ParseTree parseTree = DeclarationParser.parseDeclaration(castNode.getContent());
			if (parseTree != null) {
				boolean operatorStyle = DeclarationParser.isOperatorSyntax(parseTree);

				DeclareNode node = null;

				if (operatorStyle){
					try {
						Logger.log("Parsing an operator.");
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
		}
		else if(root instanceof FormulaStatement){
			List<DeclareNode> nodes = new ArrayList<DeclareNode>(declarations.values());
			String grammar = GrammarGenerator.createGrammar(nodes);
			try {
				parsedFormulas++;
				ParseTree formulaTree = cp.compile(grammar, root.getContent());
				if (formulaTree != null) {
					successfullyParsedFormulas++;
					OpenMathBase formulaNode = OpenMathTranslator.parseToOM(formulaTree, declarations);
					OpenMathBase wrapped = formulaNode.toOMObject();
					if(popcornOutput){
						//Currently adding a newline to each formula for readability
						String line = wrapped.toPopcorn()+"\n";
						writer.write(line);
					} else {
						String indented = OutputWriter.indentXML(wrapped.toXml());
						writer.write(indented);
					}
					if(ambiguityChecking){
						AmbiguityChecker.check(formulaTree,declarations);
					}
				}
			} catch (IOException e) {
				Logger.log("IO exception when parsing formula: "+root.getContent());
				e.printStackTrace();
			} catch (TemplateFillException e){
				Logger.log("Template fill problem in formula: "+root.getContent());
			}finally{
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

	/**
	 * Method to enable ambiguity checking
	 */
	public void enableAmbiguityChecking(){
		this.ambiguityChecking=true;
	}

	/**
	 * Method to enable popcorn output.
	 */
	public void enablePopcornOutput(){
		this.popcornOutput=true;
	}
}
