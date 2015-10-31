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
		else if(treeName.equals("BRACKETContext")){
			return parseToOM(tree.getChild(1),declarations, true);
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
				
				
				//TODO: Add non-semantic data
				//TODO: Add parenthesis
				HashMap<String,String> miscellaneous = declaration.getMiscellaneous();
				//This is where adding that would go.
				
				if(brackets){
					//TODO: do stuff with brackets
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
	
	
	
	
	//This is an ugly (hopefully temporary) hack.
	//Hopefully in the release version this will be done by the ANTLR grammar tags
	
	private static Pattern OMI = Pattern.compile("[0-9]+");
	private static Pattern OMV = Pattern.compile("[a-z]");
	

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
