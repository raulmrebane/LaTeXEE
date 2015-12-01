package main.java.latexee.declareast;

import main.java.antlrgen.DeclarationGrammarParser.ImportantPairContext;
import main.java.antlrgen.DeclarationGrammarParser.MiscPairContext;
import main.java.antlrgen.DeclarationGrammarParser.ValueInBracesContext;
import main.java.latexee.logging.Logger;

import org.antlr.v4.runtime.tree.ParseTree;
import org.symcomp.openmath.OpenMathBase;
import org.symcomp.openmath.OpenMathException;

public class MacroDeclaration extends DeclareNode {
	private String macroName; //the name of the macro - the characters following \
	private Integer arguments;
	private boolean hasOptionalArgument = false;
	private Object optionalMeaning;
	
	public MacroDeclaration(String meaning, String macroname, int arguments, int identifier) {
		super();
		this.meaning=meaning;
		this.macroName=macroname;
		this.arguments=arguments;
		this.id="MACRO"+Integer.toString(identifier);
	}
	
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
	
	public String getMacroName() {
		return macroName;
	}

	private void fillAttributes(ParseTree tree) throws DeclarationInitialisationException{
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
				}
				break;
			case "meaning":
				if(tree instanceof ValueInBracesContext){
					this.meaning=getTree(value);
				}else{
					this.contentDictionary=value;
					this.meaning=tree.getChild(4).getText();
				}
				break;
			case "meaningOpt":
				if(tree instanceof ValueInBracesContext){
					this.meaning=getTree(value);
				}else{
					this.contentDictionary=value;
					this.meaning=tree.getChild(4).getText();
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
	
	public boolean hasOptionalArgument() {
		return hasOptionalArgument;
	}

	public Object getOptionalMeaning() {
		return this.optionalMeaning;
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
	private OpenMathBase getTree(String s) throws DeclarationInitialisationException{
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
