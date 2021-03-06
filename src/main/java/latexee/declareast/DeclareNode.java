package main.java.latexee.declareast;

import java.util.HashMap;

import org.symcomp.openmath.OpenMathBase;
import org.symcomp.openmath.OpenMathException;

import main.java.latexee.exceptions.DeclarationInitialisationException;
import main.java.latexee.logging.Logger;

/**
 * Abstract class for MacroDeclaration and OperatorDeclaration.
 * Describes fields and methods which are common for MacroDeclaration and OperatorDeclaration,
 * such as content dictionary, meaning, misc. information contained and ID
 */
public abstract class DeclareNode {
	protected String contentDictionary;
	protected Object meaning;
	protected HashMap<String,String> miscellaneous = new HashMap<String,String>();
	protected String id;
	//This will create a string that will act as a fragment of the ANTLR grammar for parsing formulas.
	abstract public String toGrammarRule();
	public String getContentDictionary() {
		return contentDictionary;
	}
	public Object getMeaning() {
		return meaning;
	}
	public HashMap<String, String> getMiscellaneous() {
			return miscellaneous;
	}

	/**
	 * Gets the {@link String} instance of DeclareNode's id.
	 * @return {@link String} instance of DeclareNode id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Used in operator and macro declaration functions to p
	 * @param s string to be parsed as declaration meaning
	 * @return parsed string as OpenMathBase object.
	 * @throws DeclarationInitialisationException given meaning code is either too short or unable to parse given meaning
	 */
	protected OpenMathBase getTree(String s) throws DeclarationInitialisationException{
		if(s.length()<2){
			Logger.log("Code is too short, cannot parse "+s);
			throw new DeclarationInitialisationException();
		}
		String code = s.substring(1, s.length()-1);
		OpenMathBase tree = null;
		try{
			tree = OpenMathBase.parsePopcorn(code);
		}catch (OpenMathException popcornException){
			try{
				tree = OpenMathBase.parse(code);
			} catch (OpenMathException xmlException){
				Logger.log("Could not parse the following with XML or popcorn: "+s);
				throw new DeclarationInitialisationException();
			}
		}
		
		return tree;
	}
}
