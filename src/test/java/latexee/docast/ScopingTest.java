package test.java.latexee.docast;

import static org.junit.Assert.*;

import java.util.ArrayList;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.utils.DocumentParser;
import main.java.latexee.utils.FormulaParser;

import org.junit.Test;

public class ScopingTest {

	@Test
	public void scopingTest1() {
		ParsedStatement AST = DocumentParser.parse("src/test/antlr/scoping1.tex");
		FormulaParser.parse(AST, new ArrayList<DeclareNode>());
		FormulaParser.donePrinting();
		//Expected output: all formulas parsed
	}
	
	@Test
	public void scopingTest2() {
		ParsedStatement AST = DocumentParser.parse("src/test/antlr/scoping2.tex");
		FormulaParser.parse(AST, new ArrayList<DeclareNode>());
		FormulaParser.donePrinting();
		//Expected output: none of the formulas are parsed.
	}
	
	@Test
	public void scopingTest3() {
		ParsedStatement AST = DocumentParser.parse("src/test/antlr/scoping3.tex");
		FormulaParser.parse(AST, new ArrayList<DeclareNode>());
		FormulaParser.donePrinting();
		//Expected output: the formulas except for 5/5 and 11*90 are parsed.
	}
	
	@Test
	public void scopingTest4() {
		ParsedStatement AST = DocumentParser.parse("src/test/antlr/scoping4.tex");
		FormulaParser.parse(AST, new ArrayList<DeclareNode>());
		FormulaParser.donePrinting();
		//Expected output: the formulas except for 1+2 and a+b are parsed.
	}

}
