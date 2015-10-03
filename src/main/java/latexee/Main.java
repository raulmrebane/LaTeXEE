package main.java.latexee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import antlrgen.GrammarLexer;
import antlrgen.GrammarParser;
import antlrgen.GrammarParser.*;



public class Main {

	/*public static void main(String[] args) {
		//quick way to enable fast testing during writing. Uncomment to test.
		args = new String[] {"src/test/antlr/basic.tex"};
		
		File file = new File(args[0]);
		ANTLRInputStream AIS = null;
		try {
			AIS = new ANTLRInputStream(new FileInputStream(file));
		}
		catch (Exception e){
			System.out.println("Could not find specified file.");
			System.exit(1);
		}
		GrammarLexer lexer = new GrammarLexer(AIS);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GrammarParser parser = new GrammarParser(tokens);
		ParseTree parseTree = parser.document();
		ParsedStatement pst = parseRecursively(parseTree);
		System.out.println(parseTree.getText());
	}*/
	
	
	
	
	//NB1: $valem1$$valem2$ ei parsi
	//NB2: ei parsi s�mboleid /, { ja $ tekstina. (vaja grammatikas TEXTi muuta)
	
	public static void main(String[] args) {
		
		String filePath = "C://Users/Hiie/TVP/LaTeX_file_2.tex";
		//String fileContent = getFileContent(filePath);
		
		String fileContent = "a\\InputIfFileExists{C://Users/Hiie/TVP/LaTeX_file_3.tex}\\begin{proof}" + 
				"$ii$\\begin{lemma}$$oo$$\\end{lemma}vahetekst\\begin{lemma}\\declare{sth here}$valem$" + 
				"\\end{lemma}$valem2$\\end{proof}";
		
		ParsedStatement ps = parse(fileContent);
		System.out.println(ps);
		
	}
	
	
	//main parsing method (also gaters and parses all included documents, but doesn't do anything else with them yet)
	public static ParsedStatement parse (String fileContent) {
		ParseTree tree = parseText(fileContent);
		ParsedStatement ps = parseRecursively(tree);
		ArrayList<IncludeStatement> includedDocs = findIncludeStatements(ps);
		ArrayList<ParsedStatement> sts = parseIncludedDocuments(includedDocs); //TODO: try&catch (in case some included files can't be parsed)
		return ps;
	}
	
	
	//LaTeX tekst -> ParseTree
	public static ParseTree parseText (String text) {
		ANTLRInputStream antlrInput = new ANTLRInputStream(text);
        GrammarLexer lexer = new GrammarLexer(antlrInput);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokens);
        ParseTree tree = parser.document();
        return tree;
	}
	
	
	//parses all included documents
	public static ArrayList<ParsedStatement> parseIncludedDocuments (ArrayList<IncludeStatement> fileInclusions) {
		ArrayList<ParsedStatement> sts = new ArrayList<>();
		for (int i = 0; i < fileInclusions.size(); i++) {
			String filePath = fileInclusions.get(i).getContent();
			String fileContent = getFileContent(filePath);
	        ParseTree tree = parseText (fileContent);
	        sts.add(parseRecursively(tree));
		}
		return sts;
	}
	
	
	//gathers all IncludeStatements from a given tree
	public static ArrayList<IncludeStatement> findIncludeStatements (ParsedStatement tree) {
		ArrayList<IncludeStatement> includeStatements = new ArrayList<>();
		if (tree instanceof IncludeStatement) {
			includeStatements.add((IncludeStatement) tree);
			return includeStatements;
			
			/*//adds parsed document as child to the current IncludeStatement. return type to ParsedStatement in that case.
			String url = ((IncludeStatement) tree).getContent();
			String fileContent = getFileContent(url);
			ParsedStatement ps = parse(fileContent);
			((IncludeStatement) tree).addChild(ps);*/
			
		}
		else {
			ArrayList<ParsedStatement> children = tree.getChildren();
			for (int i = 0; i < children.size(); i++) {
				ArrayList<IncludeStatement> temp = findIncludeStatements(children.get(i));
				for (int j = 0; j < temp.size(); j++) {
					includeStatements.add(temp.get(j));
				}
			}
			return includeStatements;
		}
	}
	
		
	//puts all file contents into a String
	public static String getFileContent (String filePath) {
		StringBuffer sb = new StringBuffer();
		try {
			Scanner sc = new Scanner(new File(filePath));
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
			}
			sc.close();
		}
		
		catch (FileNotFoundException e) {
			System.out.println("Couldn't find " + filePath);
		}
		return sb.toString();
	}
		

	public static ParsedStatement parseRecursively (ParseTree tree) {
		int startIndex = 0;
		//All contexts inherit from ParserRuleContext, so for each case the index will be set here.
		if(tree instanceof ParserRuleContext){
			startIndex = ((ParserRuleContext) tree).getStart().getStartIndex();
		}
		
		if (tree instanceof DocumentContext){
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				ParsedStatement child = parseRecursively(tree.getChild(i));
				if(child!=null){
					children.add(child);
				}
			}
			return new ParsedStatement(tree.getText(), startIndex, children);
		}
		
		if (tree instanceof FormulaContext) {
			return new FormulaStatement(tree.getText().replace("$", ""), startIndex);
		}
		
		else if (tree instanceof ProofContext) {
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				ParsedStatement child = parseRecursively(tree.getChild(i));
				if(child!=null){
					children.add(child);
				}
			}
			return new ProofStatement("", startIndex, children);
		}
		
		else if (tree instanceof TheoremContext) {
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				ParsedStatement child = parseRecursively(tree.getChild(i));
				if(child!=null){
					children.add(child);
				}
			}
			return new TheoremStatement("", startIndex, children);
		}
		
		else if (tree instanceof DeclarationContext) {
			String text = tree.getText();
			String declaration = text.substring(text.indexOf('{')+1, text.length()-1);
			return new DeclareStatement(declaration, startIndex);
		}
		
		else if (tree instanceof FileInclusionContext) {
			String text = tree.getText();
			String url = text.substring(text.indexOf('{')+1, text.length()-1);
			return new IncludeStatement(url, startIndex);
		}
		
		else if (tree instanceof LemmaContext) {
			ArrayList<ParsedStatement> children = new ArrayList<>();
			for (int i = 0; i < tree.getChildCount(); i++) {
				ParsedStatement child = parseRecursively(tree.getChild(i));
				if(child!=null){
					children.add(child);
				}
			}
			return new LemmaStatement("", startIndex, children);
		}
		return null;
		
	}
	
}
