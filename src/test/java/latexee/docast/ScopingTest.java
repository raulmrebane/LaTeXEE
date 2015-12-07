package test.java.latexee.docast;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.parsers.DocumentParser;
import main.java.latexee.parsers.FormulaParser;

import org.junit.Before;
import org.junit.Test;

public class ScopingTest {
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
	public void scopingTest1() throws FileNotFoundException {
		ParsedStatement AST = DocumentParser.parse("src/test/antlr/scoping1.tex");
		fp.parse(AST);
		//Expected output: all formulas parsed
	}
	
	@Test
	public void scopingTest2() throws FileNotFoundException {
		ParsedStatement AST = DocumentParser.parse("src/test/antlr/scoping2.tex");
		fp.parse(AST);
		//Expected output: none of the formulas are parsed.
	}
	
	@Test
	public void scopingTest3() throws FileNotFoundException {
		ParsedStatement AST = DocumentParser.parse("src/test/antlr/scoping3.tex");
		fp.parse(AST);
		//Expected output: the formulas except for 5/5 and 11*90 are parsed.
	}
	
	@Test
	public void scopingTest4() throws FileNotFoundException {
		ParsedStatement AST = DocumentParser.parse("src/test/antlr/scoping4.tex");
		fp.parse(AST);
		//Expected output: the formulas except for 1+2 and a+b are parsed.
	}

}
