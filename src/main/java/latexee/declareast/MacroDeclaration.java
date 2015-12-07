package main.java.latexee.declareast;

import main.java.antlrgen.DeclarationGrammarParser.ImportantPairContext;
import main.java.antlrgen.DeclarationGrammarParser.MiscPairContext;
import main.java.antlrgen.DeclarationGrammarParser.ValueInBracesContext;
import main.java.latexee.exceptions.DeclarationInitialisationException;
import main.java.latexee.logging.Logger;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * MacroDeclaration is a class, which inherits from DeclareNode.
 * MacroDeclaration holds information about user-defined macros, like
 * \declare{macro=\tuple, meaning=ecc.Tuple, argspec=[2], code{#1, \ldots, #2}}
 * Instances of this class hold information about macro's meaning, name and argument count, misc
 * information, content dictionary.
 */
public class MacroDeclaration extends DeclareNode {
	private String macroName; //the name of the macro - the characters following \
	private Integer arguments;
	private boolean hasOptionalArgument = false;
	private Object optionalMeaning;

	/**
	 * Public constructor for MacroDeclaration instances
	 * @param meaning meaning of the Macro (meaning=ecc.Tuple)
	 * @param macroname name of the macro (macro=\tuple)
	 * @param arguments number of arguments the macro takes. (argspec=[2])
	 * @param identifier ID of the MacroDeclaration's instance.
	 */
	public MacroDeclaration(String meaning, String macroname, int arguments, int identifier) {
		super();
		this.meaning=meaning;
		this.macroName=macroname;
		this.arguments=arguments;
		this.id="MACRO"+Integer.toString(identifier);
	}

	/**
	 * Public constructor for MacroDeclaration instances.
	 * @param tree parse tree of MacroDeclaration
	 * @param identifier ID of the macro
	 * @throws DeclarationInitialisationException unable to get information from parse tree, which is required for MacroDeclaration.
	 */
	public MacroDeclaration(ParseTree tree, int identifier) throws DeclarationInitialisationException{
		fillAttributes(tree);
		if(		this.meaning == null && this.contentDictionary==null){
			//Yesterday, this was a formality. Today it guards us from the hell of nullpointers.
			Logger.log("OpenMath meaning was not instantiated on macro declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.meaning instanceof String && this.contentDictionary==null){
			Logger.log("OpenMath meaning was not instantiated on macro declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.hasOptionalArgument && this.optionalMeaning==null){
			Logger.log("Operation has optional argument but no optional meaning in macro declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.arguments==null){
			Logger.log("Number of arguments was not instantiated on macro declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.macroName==null){
			Logger.log("Macro name was not instantiated on macro declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		if(this.arguments<0){
			Logger.log("Negative amount of arguments given for macro declaration: "+tree.getText());
			throw new DeclarationInitialisationException();
		}
		this.id="MACRO"+Integer.toString(identifier);
	}

	/**
	 * Gets the {@link String} instance of MacroDeclaration's instance's name.
	 * @return {@link String} instance of MacroDeclaration's instance's name.
	 */
	public String getMacroName() {
		return macroName;
	}

	/**
	 * Private function (recursive) used by the MacroDeclaration's constructor.
	 * Gets the required information (meaning, name, etc) by traversing the parse tree which is used to build the instance of MacroDeclaration.
	 * @param tree parse tree of the MacroDeclaration
	 * @throws DeclarationInitialisationException unable to get the required information to build the instance of MacroDeclaration.
	 */
	private void fillAttributes(ParseTree tree) throws DeclarationInitialisationException{
		if(tree instanceof ImportantPairContext){
			
			String key = tree.getChild(0).getText();
			String value = tree.getChild(2).getText();
			ParseTree valueNode = tree.getChild(2);
			
			switch(key){
			case "macro":
				this.macroName=tree.getChild(3).getText();
				break;
			case "argspec":
				String nrOfArgs = tree.getChild(3).getText();
				this.arguments=Integer.parseInt(nrOfArgs);
				if(tree.getChildCount()==8){
					this.hasOptionalArgument=true;
				}
				break;
			case "meaning":
				if(valueNode instanceof ValueInBracesContext){
					this.meaning=getTree(value);
				}else{
					this.contentDictionary=value;
					this.meaning=tree.getChild(4).getText();
				}
				break;
			case "meaningOpt":
				if(valueNode instanceof ValueInBracesContext){
					this.optionalMeaning=getTree(value);
				}else{
					this.contentDictionary=value;
					this.optionalMeaning=tree.getChild(4).getText();
				}
				break;
			default:
				this.miscellaneous.put(key, value);
				break;
			}
		}
		else if(tree instanceof MiscPairContext){
			String key = tree.getChild(0).getText();
			String value = tree.getChild(2).getText();
			this.miscellaneous.put(key, value);
		}
		for(int i=0;i<tree.getChildCount();i++){
			fillAttributes(tree.getChild(i));
		}
	}

	/**
	 * @return if the MacroDeclaration instance has an optional argument.
	 */
	public boolean hasOptionalArgument() {
		return hasOptionalArgument;
	}

	/*
	 * @return MacroDeclaration's instance's optional meaning as an Object
	 */
	public Object getOptionalMeaning() {
		return this.optionalMeaning;
	}

	/**
	 * @return number of arguments the Macro uses
	 */
	public Integer getArguments() {
		return arguments;
	}

	/**
	 * Method to translate the MacroDeclaration object to a grammar rule, which
	 * is used to write the grammar rules.
	 * @return string representation of the MacroDeclaration's grammar rule
	 */
	@Override
	public String toGrammarRule() {
		String highestLevelRule = "highestLevel";
		StringBuilder sb = new StringBuilder();
		sb.append("\'\\\\"+this.macroName+"\'");
		for(int i=0;i<arguments;i++){
			sb.append("\'{\'");
			sb.append(highestLevelRule);
			sb.append("\'}\'");
		}
		sb.append(" #"+this.id+"\n");
		if(this.hasOptionalArgument){
			sb.append("|\'\\\\"+this.macroName+"\'");
			for(int i=0;i<arguments-1;i++){
				sb.append("\'{\'");
				sb.append(highestLevelRule);
				sb.append("\'}\'");
			}
			sb.append(" #"+this.id+"Opt\n");
		}
		return sb.toString();
	}
}
