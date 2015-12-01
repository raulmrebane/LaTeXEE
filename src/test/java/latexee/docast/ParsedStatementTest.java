package test.java.latexee.docast;

import java.util.ArrayList;
import java.util.Arrays;

import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.IncludeStatement;
import main.java.latexee.docast.LemmaStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.docast.ProofStatement;
import main.java.latexee.docast.TheoremStatement;
import main.java.latexee.parsers.DocumentParser;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParsedStatementTest {
	
	@Test
    public void ExampleTest() {
        ParsedStatement ex = new ParsedStatement("abc", 5);
        
        assertEquals("Testing if constructor working properly", "abc", ex.getContent());
        assertEquals("Character location must be correct", 5, ex.getCharacterLocation());
        ex.setCharacterLocation(10);
        assertEquals("Test after re-setting character location", 10, ex.getCharacterLocation());
    }
    
	@Test
    public void ExampleTest2() {
        ParsedStatement ex = new ParsedStatement("abcd", 5);
        
        assertEquals("Testing if constructor working properly2", 5, ex.getCharacterLocation());
    }
	
	
	
	
	@Test //tests dollar formulas
	public void ParsingTest1() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new FormulaStatement("1-8/a", 0),
			new FormulaStatement("444", 0),
			new FormulaStatement("1", 0),
			new FormulaStatement("2", 0),
			new FormulaStatement("3", 0),
			new FormulaStatement("formula1", 0),
			new FormulaStatement("formula2", 0),
			new FormulaStatement("formula3", 0),
			new FormulaStatement("formula4", 0)
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing1.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests formulas (all types)
	public void ParsingTest2() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new FormulaStatement("7*5-(4-2/5)", 0),
			new FormulaStatement("1-2-3-4-5-6-7", 0),
			new FormulaStatement("A", 0),
			new FormulaStatement("B", 0),
			new FormulaStatement("a-bb=x+2", 0),
			new FormulaStatement("a^0b+1", 0),
			new FormulaStatement("\\\\begin\\{equation\\}That's planned\\\\end\\{equation\\}", 0), //could do real syntax, but .tex wouldn't compile
			new FormulaStatement("0.6 * x^8", 0)
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing2.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests theorems, lemmas and proofs
	public void ParsingTest3() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new TheoremStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new FormulaStatement("x+5=6", 0)	
			))),
			new TheoremStatement("", 0, new ArrayList<ParsedStatement>()),
			new ProofStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new FormulaStatement(":)", 0),
				new FormulaStatement(":/", 0)
			))),
			new LemmaStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new FormulaStatement("test", 0)	
			))),
			new LemmaStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new FormulaStatement("formula1", 0),
				new FormulaStatement("formula2", 0)
			))),
			new ProofStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new FormulaStatement("4*4/4=y", 0)
			)))
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing3.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests nested thorems, lemmas and proofs
	public void ParsingTest4() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new TheoremStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new LemmaStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new FormulaStatement("12345", 0),
					new FormulaStatement("54321", 0)
				))),
				new FormulaStatement("3+4", 0)
			))),
			new FormulaStatement("x=5t", 0),
			new ProofStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new LemmaStatement("", 0, new ArrayList<ParsedStatement>()),
				new LemmaStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new FormulaStatement("someFormula", 0)
				)))	
			)))
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing4.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests brackets (not in the case of declarations!)
	public void ParsingTest5() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new FormulaStatement("something{here}", 0),
			new FormulaStatement("{{sd}}}", 0),
			new ProofStatement("",0)
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing5.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests \$ within formulas
	public void ParsingTest6() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new FormulaStatement("\\$", 0),
			new FormulaStatement("a\\$", 0),
			new FormulaStatement("\\$a", 0),
			new FormulaStatement("a\\$b", 0),
			new FormulaStatement("\\$", 0),
			new FormulaStatement("a\\$", 0),
			new FormulaStatement("\\$a", 0),
			new FormulaStatement("a\\$b", 0),
			new FormulaStatement("\\$", 0),
			new FormulaStatement("a\\$", 0),
			new FormulaStatement("\\$a", 0),
			new FormulaStatement("a\\$b", 0),
			new FormulaStatement("valem", 0),
			new FormulaStatement("\\$", 0),
			new ProofStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new FormulaStatement("6+7", 0)	
			))),
			new FormulaStatement("4+5", 0),
			new FormulaStatement("8", 0)
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing6.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests declarations
	public void ParsingTest7() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new DeclareStatement("{syntax={infix, 7, \"/\", l}, meaning=arith1.divide}", 0),
			new DeclareStatement("{syntax={infix, 7, \"/\", r}, meaning=arith1.divide}", 0),
			new DeclareStatement("{syntax={prefix, 7, \"/\"}, meaning=arith1.divide}", 0),
			new DeclareStatement("{syntax={postfix, 7, \"/\"}, meaning=arith1.divide}", 0),
			new DeclareStatement("{macro=\\frac, meaning=arith1.divide, argspec=[2], code={...}}", 0),
			new DeclareStatement("{macro=\\frac2, meaning=arith1.divide, argspec=2, code={...}}", 0),
			new DeclareStatement("{macro=\\tuple, meaning=ecc.Tuple, argspec=[2], code={#1, \\ldots, #2}}", 0),
			new DeclareStatement("{syntax={infix, -2, \"/\", l}, meaning=arith1.divide}", 0),
			new DeclareStatement("{syntax={infix, 7, \"/\", m}, meaning=arith1.divide}", 0),
			new DeclareStatement("{syntax={infi, 7, \"/\", l}, meaning=arith1.divide}", 0),
			new DeclareStatement("{syntax={prefix, 7, \"/\", l}, meaning=arith1.divide}", 0),
			new DeclareStatement("{syntax={postfix, 7, \"/\", l}, meaning=arith1.divide}", 0),
			new DeclareStatement("{syntax={infi, 7, \"/\", l}}", 0),
			new DeclareStatement("{macro=\\frac3, argspec=2, code={...}}", 0)
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing7.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests declarations within proofs, theorems and lemmas
	public void ParsingTest8() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new TheoremStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new DeclareStatement("{syntax={infix, 7, \"/\", l}, meaning=arith1.divide}", 0)	
			))),
			new ProofStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new LemmaStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new DeclareStatement("{syntax={infix, 7, \"/\", l}, meaning=arith1.divide}", 0)	
				)))
			)))
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing8.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests fileInclusions within proofs, theorems and lemmas (should not be allowed)
	public void ParsingTest9() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new TheoremStatement("", 0, new ArrayList<ParsedStatement>()),
			new ProofStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new FormulaStatement("just a small formula", 0),
				new FormulaStatement("an even smaller formula (although now it became longer)", 0)
			))),
			new LemmaStatement("", 0, new ArrayList<ParsedStatement>())
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing9.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests including files
	public void ParsingTest10() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new IncludeStatement("parsing11.tex", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new ParsedStatement("", 0)
			))),
			new IncludeStatement("parsing12.tex", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new ParsedStatement("", 0)	
			)))
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing10.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests including files when they have already been included
	public void ParsingTest11() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new IncludeStatement("parsing12.tex", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new ParsedStatement("", 0)	
			)))
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing11.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //the included file for two previous tests
	public void ParsingTest12() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new FormulaStatement("a random formula so the document wouldn't feel empty inside", 0)
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing12.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests including a file that includes the current file (cycle should be avoided)
	public void ParsingTest13() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new IncludeStatement("src/test/antlr/parsing14.tex", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new ParsedStatement("", 0)	
			)))
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing13.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //the second file for the previous test
	public void ParsingTest14() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new IncludeStatement("src/test/antlr/parsing13.tex", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new ParsedStatement("", 0)	
			)))
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/parsing14.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test //tests everything together
	public void LaTeX_file_2Test() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new IncludeStatement("src/test/antlr/LaTeX_file_3.tex", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new TheoremStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
						new FormulaStatement("TEISEFAILIVALEM", 0)	
					)))
				)))	
			))),
			new ProofStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new FormulaStatement("i", 0),
				new LemmaStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new FormulaStatement("o", 0)	
				))),
				new LemmaStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new DeclareStatement("{syntax={infix, 7, /, lassoc}, meaning=arith1.divide}", 0),
					new FormulaStatement("meh", 0)
				))),
				new FormulaStatement("blaa", 0)
			))),
			new IncludeStatement("src/test/antlr/LaTeX_file_4.tex", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new FormulaStatement("Mingi valem ka.", 0),
					new IncludeStatement("src/test/antlr/LaTeX_file_3.tex", 0),
					new FormulaStatement("Neljas!", 0),
					new FormulaStatement("valem", 0),
					new FormulaStatement("\\$", 0),
					new IncludeStatement("src/test/antlr/LaTeX_file_4.tex", 0)
				)))	
			)))
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/LaTeX_file_2.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test
	public void LaTeX_file_3Test() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new TheoremStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new FormulaStatement("TEISEFAILIVALEM", 0)	
			)))
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/LaTeX_file_3.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test
	public void LaTeX_file_4Test() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new FormulaStatement("Mingi valem ka.", 0),
			new IncludeStatement("src/test/antlr/LaTeX_file_3.tex", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new TheoremStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
						new FormulaStatement("TEISEFAILIVALEM", 0)	
					)))
				)))
			))),
			new FormulaStatement("Neljas!", 0),
			new FormulaStatement("valem", 0),
			new FormulaStatement("\\$", 0),
			new IncludeStatement("src/test/antlr/LaTeX_file_4.tex", 0)
		)));
		
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/LaTeX_file_4.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test
	public void basicTest() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new TheoremStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new FormulaStatement("2+3", 0),
				new FormulaStatement("2+5", 0)
			))),
			new ProofStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new FormulaStatement("1+1", 0)
			))),
			new LemmaStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new FormulaStatement("2+2", 0)
			)))				
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/basic.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	@Test
	public void basic_with_declareTest() {
		ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
			new TheoremStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
				new DeclareStatement("{syntax={infix,7,\"/\",l}, meaning=artih1.divide}", 0),
				new FormulaStatement("2+3", 0),
				new FormulaStatement("2+5", 0)
			))),
			new ProofStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new DeclareStatement("{macro=\\asd, meaning=asdasd,    argspec=[2], code={...}}", 0),
					new FormulaStatement("1+1", 0)
			))),
			new LemmaStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
					new FormulaStatement("2+2", 0)
			)))				
		)));
		ParsedStatement ps2 = DocumentParser.parse("src/test/antlr/basic_with_declare.tex");
		assertTrue(compareTrees(ps, ps2));
	}
	
	
	//only comparing if the node types are the same and in case of formulas, fileInclusions and declarations, content too.
	public boolean compareTrees(ParsedStatement actual, ParsedStatement result) {
		if (!actual.getClass().equals(result.getClass()))
			return false;
		if (actual.getChildCount() != result.getChildCount())
			return false;
		if (actual instanceof FormulaStatement || actual instanceof DeclareStatement || actual instanceof IncludeStatement) {
			return actual.getContent().equals(result.getContent());
		}
		
		for (int i = 0; i < actual.getChildCount(); i++) {
			boolean b = compareTrees(actual.getChild(i), result.getChild(i));
			if (!b)
				return b;
		}
		return true;
	}
	

}
