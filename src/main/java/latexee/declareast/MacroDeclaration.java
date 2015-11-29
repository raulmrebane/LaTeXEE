package main.java.latexee.declareast;

import main.java.antlrgen.DeclarationGrammarParser.ImportantPairContext;
import main.java.antlrgen.DeclarationGrammarParser.MiscPairContext;
import main.java.antlrgen.DeclarationGrammarParser.PairContext;
import main.java.latexee.logging.Logger;

import org.antlr.v4.runtime.tree.ParseTree;

public class MacroDeclaration extends DeclareNode {
	private String macroName; //the name of the macro - the characters following \
	private Integer arguments;
	private boolean hasOptionalArgument = false;
	private String optionalValue;
	
	public MacroDeclaration(String meaning, String macroname, int arguments, int identifier) {
		super();
		this.meaning=meaning;
		this.macroName=macroname;
		this.arguments=arguments;
		this.id="MACRO"+Integer.toString(identifier);
	}
	
	public MacroDeclaration(ParseTree tree, int identifier) throws DeclarationInitialisationException{
		fillAttributes(tree);
		if(		this.meaning == null || this.contentDictionary==null){
			//Yesterday, this was a formality. Today it guards us from the hell of nullpointers.
			Logger.log("OpenMath meaning was not instantiated on macro declaration: "+tree.getText());
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
	
	public String getMacroName() {
		return macroName;
	}

	public boolean isHasOptionalArgument() {
		return hasOptionalArgument;
	}

	private void fillAttributes(ParseTree tree){
		if(tree instanceof ImportantPairContext){
			
			String key = tree.getChild(0).getText();
			String value = tree.getChild(2).getText();
			
			switch(key){
			case "macro":
				this.macroName=tree.getChild(3).getText();
				break;
			case "argspec":
				String nrOfArgs = tree.getChild(3).getText();
				this.arguments=Integer.parseInt(nrOfArgs);
				if(tree.getChildCount()==8){
					this.hasOptionalArgument=true;
					this.optionalValue = tree.getChild(6).getText();
				}
				break;
			case "meaning":
				this.contentDictionary=value;
				this.meaning=tree.getChild(4).getText();
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
	
	public boolean hasOptionalArgument() {
		return hasOptionalArgument;
	}

	public String getOptionalValue() {
		return optionalValue;
	}
	public Integer getArguments() {
		return arguments;
	}
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
			sb.append(" #"+this.id+"Optional\n");
		}
		return sb.toString();
	}

}
