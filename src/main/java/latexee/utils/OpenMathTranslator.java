package main.java.latexee.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.declareast.MacroDeclaration;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.logging.Logger;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.symcomp.openmath.OpenMathBase;

import com.sun.corba.se.impl.protocol.MinimalServantCacheLocalCRDImpl;

import fr.inria.openmath.omapi.Node;
import fr.inria.openmath.omapi.Symbol;
import fr.inria.openmath.omapi.implementation.IntNodeImpl;
import fr.inria.openmath.omapi.implementation.NodeImpl;
import fr.inria.openmath.omapi.implementation.SymbolNodeImpl;
import fr.inria.openmath.omapi.implementation.VariableNodeImpl;

public class OpenMathTranslator {

	//TODO: Possibly change List<DeclareNode> to HashMap<String,DeclareNode> where String=ID (with optional identifier for macronames)
	//For now, it'll be O(n) to look for a matching ID.
	
	//TODO: Currently the brackets boolean is being passed, but is not used. Need to add bracket info.
	
	public static Node parseToOM(ParseTree tree, List<DeclareNode> declarations, boolean brackets){
		String treeName = tree.getClass().getSimpleName();
		if(treeName.contains("DEFAULT")){
			return parseToOM(tree.getChild(0), declarations, brackets);
		}
		else if(treeName.equalsIgnoreCase("BRACKETSContext")){
			return parseToOM(tree.getChild(1),declarations, true);
		}
		else if(treeName.equalsIgnoreCase("invisibletimescontext")){
			Node root = new NodeImpl(Node.OM_APP);
			if(brackets){
				addParens(root);
			}
			
			//There is no declaration for invisible times so we add it manually
			Symbol timesSymbol = new Symbol("arith1","times");
			Node omsNode = new SymbolNodeImpl(timesSymbol);
			
			
			ParseTree leftChild = tree.getChild(0);
			ParseTree rightChild = tree.getChild(1);
			
			Node leftNode = parseToOM(leftChild,declarations,false);
			Node rightNode = parseToOM(rightChild,declarations,false);
			
			root.appendChild(omsNode);
			root.appendChild(leftNode);
			root.appendChild(rightNode);
			

			return root;
		}
		else{
			if(tree instanceof TerminalNodeImpl){
				TerminalNodeImpl castTree = (TerminalNodeImpl) tree;
				return lexerToOM(castTree);
			}
			Node root = new NodeImpl(Node.OM_APP);
			
			//Remove "Context" from the end
			String noContext = treeName.substring(0, treeName.length()-7); 
			
			boolean optionalArgs = false;
			
			//Just in case there is an operation named "optional" we check that the word "Optional" is in the end
			if(noContext.contains("Optional")&&noContext.substring(noContext.length()-8, noContext.length()).equals("Optional")){ 
				
				//Set the flag for optional argument use and remove word "Optional" from ID.
				optionalArgs = true;
				noContext = noContext.substring(0, noContext.length()-8);
			}
			
			//Find appropriate declaration for operation
			DeclareNode declaration = null;
			for(DeclareNode i: declarations){
				if(i.getId().equals(noContext)){
					declaration = i;
				}
			}
			if(declaration!=null){				
				//Get operation info from declaration
				String contentDictionary = declaration.getContentDictionary();
				String operationName = declaration.getMeaning();
				
				//Attach it to the node we're building
				Symbol cdSymbol = new Symbol(contentDictionary, operationName);
				Node cdNode = new SymbolNodeImpl(cdSymbol);
				root.appendChild(cdNode);
				
				HashMap<String,String> miscellaneous = declaration.getMiscellaneous();
				
				//If there is nonsemantic data
				if(miscellaneous.size()>0){
					
					//Label the node where we put the nonsemantic data
					Symbol latexeeCD = new Symbol("LaTeXEE","nonsemantic");
					
					//OMA root node of all OMS'es that come from the misc. 
					//Could be made prettier in that if there is just one we can make just one OMS.
					Node omaNode = new NodeImpl(Node.OM_APP);
					
					for(String key:miscellaneous.keySet()){
						//Adding OMS'es for each data pair
						String value = miscellaneous.get(key);
						Node tmp = new SymbolNodeImpl(new Symbol(key,value));
						omaNode.appendChild(tmp);
					}
					//Adding this attribute to the operation
					root.setAttribute(latexeeCD, omaNode);
				}
				
				
				if(brackets){
					addParens(root);
				}

				List<Node> children = new ArrayList<Node>();
				if(declaration instanceof OperatorDeclaration){
					OperatorDeclaration castDeclaration = (OperatorDeclaration) declaration;
					children.addAll(operatorChildren(tree, declarations, castDeclaration));
				}
				if(declaration instanceof MacroDeclaration){
					MacroDeclaration castDeclaration = (MacroDeclaration) declaration;
					children.addAll(macroChildren(tree, declarations, castDeclaration));
					
					if(optionalArgs){
						//TODO: Handle optional values
					}
				}
				
				//Add children to root node
				for(Node i:children){
					root.appendChild(i);
				}
				return root;
			}
			return null;
		}
	}
	
	private static List<Node> operatorChildren(ParseTree tree, List<DeclareNode> declarations, OperatorDeclaration declaration){
		List<Node> children = new ArrayList<Node>();
		String type = declaration.getType();
		if(type.equals("infix")){
			Node leftChild = parseToOM(tree.getChild(0), declarations, false);
			Node rightChild = parseToOM(tree.getChild(2), declarations, false);
			children.add(leftChild);
			children.add(rightChild);
		}
		if(type.equals("prefix")){
			Node child = parseToOM(tree.getChild(1), declarations, false);
			children.add(child);
		}
		if(type.equals("postfix")){
			Node child = parseToOM(tree.getChild(0), declarations, false);
			children.add(child);
		}
		return children;
	}
	
	private static List<Node> macroChildren(ParseTree tree, List<DeclareNode> declarations, MacroDeclaration declaration){
		List<Node> children = new ArrayList<Node>();
		
		for(int i=2;i<tree.getChildCount();i=i+3){
			Node child = parseToOM(tree.getChild(i), declarations, false);
			children.add(child);
		}
		
		return children;
	}
	
	public static void addParens(Node root){
		//Labeling the attribute again
		Symbol latexeeCD = new Symbol("LaTeXEE","nonsemantic");
		
		//Setting parens and type. Currently only one type of parens exist 
		Symbol braceCDSymbol = new Symbol("LATEXEE","brace");
		Node braceNode = new SymbolNodeImpl(braceCDSymbol);
		root.setAttribute(latexeeCD, braceNode);
	}
	
	
	//This is an ugly (hopefully temporary) hack.
	//Hopefully in the release version this will be done by the ANTLR grammar tags
	
	private static Pattern OMI = Pattern.compile("[0-9]+");
	private static Pattern OMV = Pattern.compile("[a-z]");
	
	//Adds non-semantic data about the existence of parens to the node.
	//Could later be improved upon by adding a type parameter
	//In case we deal with more than {} such as ()
	public static Node lexerToOM(TerminalNodeImpl tree){
		String constant = tree.getText();
		if(OMI.matcher(constant).matches()){
			return new IntNodeImpl(Integer.parseInt(constant));
		}
		if(OMV.matcher(constant).matches()){
			return new VariableNodeImpl(constant);
		}
		else{
			Logger.log("An unknown constant was found.");
			//TODO: Make exception and handle it.
			return null;
		}
	}
}
