package main.java.latexee.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.declareast.MacroDeclaration;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.exceptions.TemplateFillException;
import main.java.latexee.logging.Logger;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.symcomp.openmath.OMApply;
import org.symcomp.openmath.OMBind;
import org.symcomp.openmath.OMError;
import org.symcomp.openmath.OMInteger;
import org.symcomp.openmath.OMObject;
import org.symcomp.openmath.OMReference;
import org.symcomp.openmath.OMSymbol;
import org.symcomp.openmath.OMVariable;
import org.symcomp.openmath.OpenMathBase;
import org.symcomp.openmath.OpenMathException;

/**
 * OpenMathTranslator class contains static methods to translate the parse tree of parsed formulas to OpenMath format.
 * To generate the OpenMathBase object, which can later be either transformed to XML or Popcorn a
 * ParseTree of formulas and a list of declarations is required.
 * Uses OpenMath library to translate nodes to popcorn and parse them.
 */
public class OpenMathTranslator {

	private static List<String> supportedBrackets = Arrays.asList("BRACESContext","PARENSContext");
	public static ArrayList<String> bracketFlags = new ArrayList<String>();

	/**
	 * Static method to translate given ParseTree of formulas and declarations to OpenMathBase object, which
	 * can be later transformed to either XML or popcorn to output files.
	 * If a supported bracket is found then it is saved and is later written to XML output.
	 * Also adds non-semantic data to the OpenMathBase object.
	 * @param tree ParseTree of formulas
	 * @param declarations list of declarations in scope
	 * @return OpenMathBase object, which can be later used to generate XML-document
	 * @throws TemplateFillException thrown when was unable to transform to OpenMathBase object
	 */
	public static OpenMathBase parseToOM(ParseTree tree, Map<String,DeclareNode> declarations) throws TemplateFillException{
		String treeName = tree.getClass().getSimpleName();
		
		if(treeName.contains("DEFAULT")){
			return parseToOM(tree.getChild(0), declarations);
		}
		else if(supportedBrackets.contains(treeName)){
			String bracketName = treeName.substring(0, treeName.length()-7).toLowerCase();
			bracketFlags.add(bracketName);
			return parseToOM(tree.getChild(1),declarations);
		}
		else if(treeName.equalsIgnoreCase("invisibletimescontext")){

			
			//Evaluate children
			
			ParseTree leftChild = tree.getChild(0);
			ParseTree rightChild = tree.getChild(1);
			
			OpenMathBase leftNode = parseToOM(leftChild,declarations);
			OpenMathBase rightNode = parseToOM(rightChild,declarations);
			
			OpenMathBase[] childArray = {leftNode,rightNode};

			//There is no declaration for invisible times so we add it manually
			OMSymbol base = new OMSymbol("arith1", "times");
			OMApply root = new OMApply(base, childArray);

			if(!bracketFlags.isEmpty()){
				addParens(root);
			}		

			return root;
		}
		
		else{
			if(tree instanceof TerminalNodeImpl){
				TerminalNodeImpl castTree = (TerminalNodeImpl) tree;
				return lexerToOM(castTree);
			}
			
			//Remove "Context" from the end
			String noContext = treeName.substring(0, treeName.length()-7); 
			
			boolean optionalArgs = false;
			
			//Just in case there is an operation named "optional" we check that the word "Optional" is in the end
			if(noContext.contains("Opt")&&noContext.substring(noContext.length()-3, noContext.length()).equals("Opt")){ 
				//Set the flag for optional argument use and remove word "Optional" from ID.
				optionalArgs = true;
				noContext = noContext.substring(0, noContext.length()-3);
			}
			
			//Find appropriate declaration for operation
			DeclareNode declaration = null;
			declaration = declarations.get(noContext);
			
			if(declaration!=null){				
				//Get operation info from declaration
				String contentDictionary = declaration.getContentDictionary();
				HashMap<String,String> miscellaneous = declaration.getMiscellaneous();
				OpenMathBase root = null;
				
				//Find children
				List<OpenMathBase> children = new ArrayList<OpenMathBase>();
				
				if(declaration instanceof OperatorDeclaration){
					OperatorDeclaration castDeclaration = (OperatorDeclaration) declaration;
					children.addAll(operatorChildren(tree, declarations, castDeclaration));
				}
				if(declaration instanceof MacroDeclaration){
					MacroDeclaration castDeclaration = (MacroDeclaration) declaration;
					children.addAll(macroChildren(tree, declarations, castDeclaration));
				}
				
				OpenMathBase[] childArray = children.toArray(new OpenMathBase[children.size()]);
				Object meaning = null;
				if(optionalArgs){
					MacroDeclaration md = (MacroDeclaration) declaration;
					meaning = md.getOptionalMeaning();
				} else{
					meaning = declaration.getMeaning();
				}
				if(meaning instanceof String){
					String operationName = (String) meaning;
					OMSymbol base = new OMSymbol(contentDictionary, operationName);
					root = new OMApply(base, childArray);
				}else{
					OpenMathBase template = (OpenMathBase) meaning;
					root = fillTemplate(template, childArray);
				}
				//If there is nonsemantic data
				if(miscellaneous.size()>0){
					
					//Label the node where we put the nonsemantic data
					OMSymbol nsOMS = new OMSymbol("LaTeXEE", "nonsemantic");
					
					//OMS-OMS pairs!
					
					for(String key:miscellaneous.keySet()){
						//Adding OMS'es for each data pair
						String value = miscellaneous.get(key);
						OMSymbol valueOMS = new OMSymbol(key, value);
						root.putAt(nsOMS, valueOMS);
					}
				}
				
				if(!bracketFlags.isEmpty()){
					addParens(root);
				}

				return root;
			}
			return null;
		}
	}

	/**
	 * Method gets all the formula nodes which are children to a given operator declaration node as OpenMathBase objects.
	 * Checks if the type of the operator is infix, prefix or postfix and acts accordingly.
	 * @param tree operator node for which the children are gathered from
	 * @param declarations declarations currently in scope
	 * @param declaration declaration of the operator
	 * @return list of OpenMathBase objects containing all of the children of the given operator node.
	 * @throws TemplateFillException thrown when was unable to transform to OpenMathBase object
	 */
	private static List<OpenMathBase> operatorChildren(ParseTree tree, Map<String,DeclareNode> declarations, OperatorDeclaration declaration) throws TemplateFillException{
		List<OpenMathBase> children = new ArrayList<OpenMathBase>();
		String type = declaration.getType();
		if(type.equals("infix")){
			OpenMathBase leftChild = parseToOM(tree.getChild(0), declarations);
			OpenMathBase rightChild = parseToOM(tree.getChild(tree.getChildCount()-1), declarations);
			children.add(leftChild);
			children.add(rightChild);
		}
		if(type.equals("prefix")){
			OpenMathBase child = parseToOM(tree.getChild(1), declarations);
			children.add(child);
		}
		if(type.equals("postfix")){
			OpenMathBase child = parseToOM(tree.getChild(0), declarations);
			children.add(child);
		}
		return children;
	}

	/**
	 * Method gets all the formula nodes which are children to a given macro declaration node as OpenMathBase objects.
	 * @param tree macro declaration for which the children are gathered from.
	 * @param declarations declarations currently in scope
	 * @param declaration declaration of a macro
	 * @return list of OpenMathBase objects containing all of the children of the given operator node.
	 * @throws TemplateFillException thrown when was unable to transform to OpenMathBase object
	 */
	private static List<OpenMathBase> macroChildren(ParseTree tree, Map<String,DeclareNode> declarations, MacroDeclaration declaration) throws TemplateFillException{
		List<OpenMathBase> children = new ArrayList<OpenMathBase>();
		
		for(int i=2;i<tree.getChildCount();i=i+3){
			OpenMathBase child = parseToOM(tree.getChild(i), declarations);
			children.add(child);
		}
		
		return children;
	}

	/**
	 * Method to add collected brackets info to OpenMathBase object, so we can present them in XML.
	 * @param root OpenMathBase object, which gets brackets added to.
	 */
	private static void addParens(OpenMathBase root){
		//Labeling the attribute again
		OMSymbol keyOMS = new OMSymbol("LaTeXEE", "parenstype");
		
		//Map these with just OMS-OMS
		for(String bracketType:bracketFlags){
			OMSymbol bracketOMS = new OMSymbol("LaTeXEE",bracketType);
			root.putAt(keyOMS, bracketOMS);				
		}
		
		//Clear any bracket flags in the end 
		bracketFlags.clear();
	}

	/**
	 * Entry method for filling out templates
	 * The method below alters the given OpenMathBase node, therefore a copy of it is made.
	 * Generates copy by translating OpenMathNode to popcorn and then parsed to OpenMathBase object
	 * @param node OpenMathBase object, which is passed to fillTemplateImpl(OpenMathBase, OpenMathBase[])
	 * @param args OpenMathBase[] arguments with what the template is filled with
	 * @return a copy of node which has its template filled
	 * @throws TemplateFillException thrown when a node was parsed more than once, which should not happen.
	 */
	private static OpenMathBase fillTemplate(OpenMathBase node, OpenMathBase[] args) throws TemplateFillException{
		OpenMathBase copy = null;
		try {
			copy = OpenMathBase.parse(node.toPopcorn());
		} catch (OpenMathException e) {
			Logger.log("Parsing failed for:"+node.toPopcorn()+" which was already parsed once. This should not happen.");
			throw new TemplateFillException();
		}
		fillTemplateImpl(copy,args);
		return copy;
		
	}

	/**
	 * A recursive method, which is used to get arguments which are used to fill OpenMathBase templates.
	 * Checks to which groups a given node belongs to and acts accordingly (OMApply, OMBind, OMError).
	 * @param node OpenMathBase node that is to be processed
	 * @param args OpenMathBase[] arguments with what the template is filled with
	 * @throws TemplateFillException thrown when if node is incorrect. Object within an object.
	 */
	private static void fillTemplateImpl(OpenMathBase node, OpenMathBase[] args) throws TemplateFillException{
		if(node instanceof OMApply){
			OMApply oma = (OMApply) node;
			OpenMathBase[] params = oma.getParams();
			processArray(params,args);
		}
		if(node instanceof OMBind){
			OMBind ombi = (OMBind) node;
			OpenMathBase param = ((OMBind) node).getParam();
			fillTemplateImpl(param,args);
			OpenMathBase[] vars = ombi.getBvars();
			processArray(vars,args);
		}
		if(node instanceof OMError){
			OMError ome = (OMError) node;
			OpenMathBase[] params = ome.getParams();
			processArray(params,args);
		}
		if(node instanceof OMObject){
			Logger.log("Object found in template - there can't be objects within an object.");
			throw new TemplateFillException();
		}
		Collection<OpenMathBase[]> attrArrays = node.getAttributions().values();
		for (OpenMathBase[] attrs : attrArrays) {
			processArray(attrs,args);
		}

	}

	/**
	 * Method to process an array of OpenMathBase objects.
	 * @param array Array of OpenMathBase objects to be processed
	 * @param args Arguments that the template is filled with.
	 * @throws TemplateFillException thrown when incorrect amount of arguments given for OpenMathBase object.
	 */
	private static void processArray(OpenMathBase[] array, OpenMathBase[] args) throws TemplateFillException{
		for(int i=0;i<array.length;i++){
			OpenMathBase child = array[i];
			if(child instanceof OMReference){
				OMReference omref = (OMReference) child;
				String content = omref.getHref();
				if(content.length()<3){
					Logger.log("Reference in template too small");
					throw new TemplateFillException();
				}
				String id = content.substring(2);
				int index = Integer.parseInt(id)-1;
				
				try{
					array[i] = args[index];
				} catch (Exception e){
					Logger.log("Argument in template higher than amount of children.");
					throw new TemplateFillException();
				}
			}else{
				fillTemplateImpl(child,args);
			}
		}
	}
	//This is an ugly (hopefully temporary) hack.
	//Hopefully in the release version this will be done by the ANTLR grammar tags
	
	private static Pattern OMI = Pattern.compile("[0-9]+");
	private static Pattern OMV = Pattern.compile("[a-z]");
	
	//Adds non-semantic data about the existence of parens to the node.
	//Could later be improved upon by adding a type parameter
	//In case we deal with more than {} such as ()

	/**
	 * Parses OMI and OMV constants in the the formula tree.
	 * @param tree tree node containing of a constant
	 * @return OpenMathBase object (right now OMInteger or OMVariable) containing a constant
	 */
	public static OpenMathBase lexerToOM(TerminalNodeImpl tree){
		String constant = tree.getText();
		if(OMI.matcher(constant).matches()){
			return new OMInteger(Integer.parseInt(constant));
		}
		if(OMV.matcher(constant).matches()){
			return new OMVariable(constant);
		}
		else{
			Logger.log("A non-OMI/OMV constant was found.");
			//TODO: Make exception and handle it.
			return null;
		}
	}
}
