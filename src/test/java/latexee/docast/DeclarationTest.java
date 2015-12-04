package test.java.latexee.docast;

import static org.junit.Assert.*;

import main.java.latexee.declareast.MacroDeclaration;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.exceptions.DeclarationInitialisationException;
import main.java.latexee.parsers.DeclarationParser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

public class DeclarationTest {

	@Test
	public void test1() throws DeclarationInitialisationException {
		ParseTree pt = DeclarationParser.parseDeclaration("{syntax={infix,7,\"*\",l}, meaning=arith1.times}");
		OperatorDeclaration od = new OperatorDeclaration(pt,0);		
		assertEquals("infix", od.getType());
		assertEquals("*", od.getOperator());
		assertEquals("l", od.getAssociativity());
		assertEquals("7", ""+od.getPriority());
		assertEquals("arith1", od.getContentDictionary());
		assertEquals("times", od.getMeaning());
		assertEquals("{}", ""+od.getMiscellaneous());
	}
	
	@Test(expected=DeclarationInitialisationException.class)
	public void test2() throws DeclarationInitialisationException {
		ParseTree decTree = DeclarationParser.parseDeclaration("{syntax={prefix,100,\"\\%\"}, meaning=arith1.remainder, misc=\"some misc info\"}");
		new OperatorDeclaration(decTree,0);
	}
	
	@Test
	public void test3() throws DeclarationInitialisationException {
		ParseTree pt = DeclarationParser.parseDeclaration("{macro=\\frac, meaning=arith1.divide, argspec=[2], code={...}}");
		MacroDeclaration md = new MacroDeclaration(pt,0);		
		assertEquals("frac", md.getMacroName());
		assertEquals("arith1", md.getContentDictionary());
		assertEquals("divide", md.getMeaning());
		assertEquals("false", ""+md.hasOptionalArgument());
		assertEquals("{code={...}}", ""+md.getMiscellaneous());
	}
	
	@Test
	public void test4() throws DeclarationInitialisationException { //fails because doesn't parse #
		ParseTree pt = DeclarationParser.parseDeclaration("{macro=\\tuple, meaning=ecc.Tuple, argspec=[2], code={#1, \\ldots, #2}}");
		MacroDeclaration md = new MacroDeclaration(pt,0);		
		assertEquals("tuple", md.getMacroName());
		assertEquals("ecc", md.getContentDictionary());
		assertEquals("Tuple", md.getMeaning());
		assertEquals("false", ""+md.hasOptionalArgument());
		assertEquals("{code={#1, \\ldots, #2}}", ""+md.getMiscellaneous());
	}

}
