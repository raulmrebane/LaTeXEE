package test.java.latexee.docast;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.parsers.DeclarationParser;
import main.java.latexee.parsers.DocumentParser;
import main.java.latexee.parsers.FormulaParser;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;


public class OpenMathTest {

private FormulaParser fp;

@Before
public void setUp(){
	try {
		try {
			fp = new FormulaParser("output.xml");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Couldn't use utf-8 encoding for output file.");
			e.printStackTrace();
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
}

@Test
public void BasicWithScopingTest() throws IOException{
	 ParsedStatement AST = DocumentParser.parse("src/test/antlr/basic_with_scoping.tex");
     DeclarationParser.declarationFinder(AST);
     fp.parse(AST);
     File outputFile = new File("output.xml"); 
     File expectedOutputFile = new File("src/test/openmath/basic_with_scoping.xml");
     assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
     
}

@Test
public void BasicWithNonsemanticTest() throws IOException{
	 ParsedStatement AST = DocumentParser.parse("src/test/antlr/basic_with_nonsemantic.tex");
     DeclarationParser.declarationFinder(AST);
     fp.parse(AST);
     File outputFile = new File("output.xml"); 
     File expectedOutputFile = new File("src/test/openmath/basic_with_nonsemantic.xml");
     assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
}

@Test
public void BasicParsingSmall() throws IOException{
	 ParsedStatement AST = DocumentParser.parse("src/test/antlr/basic_parsing_small.tex");
     DeclarationParser.declarationFinder(AST);
     fp.parse(AST);
     File outputFile = new File("output.xml"); 
     File expectedOutputFile = new File("src/test/openmath/basic_parsing_small.xml");
     assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
}

@Test
public void GrammarGenerator() throws IOException{
	 ParsedStatement AST = DocumentParser.parse("src/test/antlr/grammar_generator.tex");
     DeclarationParser.declarationFinder(AST);
     fp.parse(AST);
     File outputFile = new File("output.xml"); 
     File expectedOutputFile = new File("src/test/openmath/grammar_generator.xml");
     assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
}

 @Test
 public void CommentTest() throws IOException{
      ParsedStatement AST = DocumentParser.parse("src/test/antlr/grammar_comment.tex");
      DeclarationParser.declarationFinder(AST);
      fp.parse(AST);
      File outputFile = new File("output.xml");
      File expectedOutputFile = new File("src/test/openmath/grammar_generator.xml");
      assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
 }
 
 @Test
 public void InlineTest1() throws IOException{
      ParsedStatement AST = DocumentParser.parse("src/test/antlr/inlinetest1.tex");
      DeclarationParser.declarationFinder(AST);
      fp.parse(AST);
      File outputFile = new File("output.xml");
      File expectedOutputFile = new File("src/test/openmath/inlinetest1.xml");
      assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
 }
 
 @Test
 public void InlineTest2() throws IOException{
      ParsedStatement AST = DocumentParser.parse("src/test/antlr/inlinetest2.tex");
      DeclarationParser.declarationFinder(AST);
      fp.parse(AST);
      File outputFile = new File("output.xml");
      File expectedOutputFile = new File("src/test/openmath/inlinetest2.xml");
      assertTrue(FileUtils.contentEqualsIgnoreEOL(outputFile, expectedOutputFile, null));
 }
 
}
