package main.java.latexee.declareast;

import main.java.antlrgen.DeclarationGrammarParser.ImportantPairContext;
import main.java.antlrgen.DeclarationGrammarParser.MiscPairContext;
import main.java.antlrgen.DeclarationGrammarParser.PairContext;

import org.antlr.v4.runtime.tree.ParseTree;

public class MacroDeclaration extends DeclareNode {
	private String macroName; //the name of the macro - the characters following \
	private Integer arguments;
	private boolean hasOptionalArgument = false;
	private String optionalValue;
	
	public MacroDeclaration(String meaning, String macroname, int arguments) {
		super();
		this.meaning=meaning;
		this.macroName=macroname;
		this.arguments=arguments;
		this.id="MACRO"+identifier;
	}
	
	public MacroDeclaration(ParseTree tree){
		fillAttributes(tree);
		if(		this.meaning == null || this.contentDictionary==null){
			//Yesterday, this was a formality. Today it guards us from the hell of nullpointers.
			throw new RuntimeException("OpenMath meaning was not instantiated on macro declaration: "+tree.getText());
		}
		if(this.arguments==null){
			throw new RuntimeException("Number of arguments was not instantiated on macro declaration: "+tree.getText());
		}
		if(this.macroName==null){
			throw new RuntimeException("Macro name was not instantiated on macro declaration: "+tree.getText());
		}
		if(this.arguments<0){
			throw new RuntimeException("Negative amount of arguments given for macro declaration: "+tree.getText());
		}
		this.id="MACRO"+identifier;
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
