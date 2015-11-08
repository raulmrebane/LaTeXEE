package test.java.latexee.docast;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.utils.DeclarationParser;
import main.java.latexee.utils.DocumentParser;
import main.java.latexee.utils.FormulaParser;

import org.apache.commons.io.FileUtils;
import org.junit.Test;


public class OpenMathTest {

	
@Test
public void BasicWithScopingTest() throws IOException{
	 ParsedStatement AST = DocumentParser.parse("src/test/antlr/basic_with_scoping.tex");
     DeclarationParser.declarationFinder(AST);
     FormulaParser.setFilename("output.xml");
     FormulaParser.parse(AST, new ArrayList<DeclareNode>());
     FormulaParser.donePrinting();
     File outputFile = new File("output.xml"); 
     File expectedOutputFile = new File("src/test/openmath/basic_with_scoping.xml");
     assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
     
}

@Test
public void BasicWithNonsemanticTest() throws IOException{
	 ParsedStatement AST = DocumentParser.parse("src/test/antlr/basic_with_nonsemantic.tex");
     DeclarationParser.declarationFinder(AST);
     FormulaParser.setFilename("output.xml");
     FormulaParser.parse(AST, new ArrayList<DeclareNode>());
     FormulaParser.donePrinting();
     File outputFile = new File("output.xml"); 
     File expectedOutputFile = new File("src/test/openmath/basic_with_nonsemantic.xml");
     assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
}

@Test
public void BasicParsingSmall() throws IOException{
	 ParsedStatement AST = DocumentParser.parse("src/test/antlr/basic_parsing_small.tex");
     DeclarationParser.declarationFinder(AST);
     FormulaParser.setFilename("output.xml");
     FormulaParser.parse(AST, new ArrayList<DeclareNode>());
     FormulaParser.donePrinting();
     File outputFile = new File("output.xml"); 
     File expectedOutputFile = new File("src/test/openmath/basic_parsing_small.xml");
     assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
}

@Test
public void GrammarGenerator() throws IOException{
	 ParsedStatement AST = DocumentParser.parse("src/test/antlr/grammar_generator.tex");
     DeclarationParser.declarationFinder(AST);
     FormulaParser.setFilename("output.xml");
     FormulaParser.parse(AST, new ArrayList<DeclareNode>());
     FormulaParser.donePrinting();
     File outputFile = new File("output.xml"); 
     File expectedOutputFile = new File("src/test/openmath/grammar_generator.xml");
     assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
}

}
