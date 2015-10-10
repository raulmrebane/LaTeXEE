package main.java.latexee.runtime;

import java.util.ArrayList;

import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.utils.DocumentParser;


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
	//NB2: ei parsi sümboleid /, { ja $ tekstina. (vaja grammatikas TEXTi muuta)
	
	public static void main(String[] args) {
		
		String filePath = "src/test/antlr/LaTeX_file_2.tex";
		String fileContent = DocumentParser.getFileContent(filePath);
		
		ParsedStatement ps = DocumentParser.parse(fileContent, new ArrayList<String>());
		System.out.println(ps);
		
	}
	
	
	
	
}

