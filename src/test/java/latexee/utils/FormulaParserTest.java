package test.java.latexee.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Arrays;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.parsers.DeclarationParser;
import main.java.latexee.utils.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;



public class FormulaParserTest {

    /* Tests: FormulaString -> ParseTree*/
	private GrammarCompiler gc = new GrammarCompiler();

    @Test
    public void FormulaStringToParsetree1() {
        String formulaSt = "└──DEFAULT0Context└──DEFAULT1Context└──Op0Context├──Op0Context│├──DEFAULT2Context││└──DEFAULT3Context││└──DEFAULT4Context││└──DEFAULT5Context││└──TerminalNodeImpl:1│├──TerminalNodeImpl:+│└──DEFAULT3Context│└──DEFAULT4Context│└──DEFAULT5Context│└──TerminalNodeImpl:1├──TerminalNodeImpl:+└──DEFAULT3Context└──DEFAULT4Context└──DEFAULT5Context└──TerminalNodeImpl:1";
        ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
                new DeclareStatement("{syntax={infix, 5, \"+\", l}, meaning=arith1.sum}", 0),
                new DeclareStatement("{syntax={infix, 5, \"-\", l}, meaning=arith1.minus}", 0)
        )));

        DeclarationParser.declarationFinder(ps);
        ArrayList<DeclareNode> existingRules = new ArrayList<DeclareNode>();
        ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(ps, existingRules);

        String grammar = GrammarGenerator.createGrammar(nodes);
        String ans = "";
        try {
            ParseTree formulaTree = gc.compile(grammar, "1+1+1");
            ans = OutputWriter.prettyParseTree(formulaTree).replaceAll(" ", "").replaceAll("\n", "");
        } catch (IOException r)  {
            System.out.println("FormulaParserTest failed to IO");
        }
        assertTrue(ans.replaceAll("\\s+","").equals(formulaSt.replaceAll("\\s+","")));

    }
    @Test
    public void FormulaStringToParsetree2() {

        String formulaSt = "└──DEFAULT0Context└──DEFAULT1Context└──Op0Context├──Op0Context│├──Op0Context││├──DEFAULT2Context│││└──DEFAULT3Context│││└──DEFAULT4Context│││└──DEFAULT5Context│││└──TerminalNodeImpl:1││├──TerminalNodeImpl:/││└──DEFAULT3Context││└──DEFAULT4Context││└──PARENSContext││├──TerminalNodeImpl:(││├──DEFAULT0Context│││└──DEFAULT1Context│││└──Op0Context│││├──DEFAULT2Context││││└──DEFAULT3Context││││└──DEFAULT4Context││││└──DEFAULT5Context││││└──TerminalNodeImpl:1│││├──TerminalNodeImpl:/│││└──DEFAULT3Context│││└──DEFAULT4Context│││└──DEFAULT5Context│││└──TerminalNodeImpl:1││└──TerminalNodeImpl:)│├──TerminalNodeImpl:/│└──DEFAULT3Context│└──DEFAULT4Context│└──DEFAULT5Context│└──TerminalNodeImpl:a├──TerminalNodeImpl:/└──DEFAULT3Context└──DEFAULT4Context└──DEFAULT5Context└──TerminalNodeImpl:x";
        ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
                new DeclareStatement("{syntax={infix, 7, \"/\", l}, meaning=arith1.divide}", 0)
        )));

        DeclarationParser.declarationFinder(ps);
        ArrayList<DeclareNode> existingRules = new ArrayList<DeclareNode>();
        ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(ps, existingRules);

        String grammar = GrammarGenerator.createGrammar(nodes);
        String ans = "";
        try {
            ParseTree formulaTree = gc.compile(grammar, "1/(1/1)/a/x");
            ans = OutputWriter.prettyParseTree(formulaTree).replaceAll(" ", "").replaceAll("\n", "");
        } catch (IOException r)  {
            System.out.println("FormulaParserTest failed to IO");
        }

        assertTrue(ans.replaceAll("\\s+","").equals(formulaSt.replaceAll("\\s+","")));

    }

    @Test
    public void FormulaStringToParsetree3() {

        String formulaSt = "└──DEFAULT0Context└──DEFAULT1Context└──Op4Context├──DEFAULT2Context│└──Op1Context│├──Op2Context││├──DEFAULT3Context│││└──DEFAULT4Context│││└──DEFAULT5Context│││└──DEFAULT6Context│││└──DEFAULT7Context│││└──TerminalNodeImpl:1││├──TerminalNodeImpl:+││└──Op0Context││├──DEFAULT4Context│││└──DEFAULT5Context│││└──DEFAULT6Context│││└──PARENSContext│││├──TerminalNodeImpl:(│││├──DEFAULT0Context││││└──DEFAULT1Context││││└──DEFAULT2Context││││└──DEFAULT3Context││││└──Op3Context││││├──DEFAULT4Context│││││└──DEFAULT5Context│││││└──DEFAULT6Context│││││└──DEFAULT7Context│││││└──TerminalNodeImpl:5││││├──TerminalNodeImpl:/││││└──DEFAULT5Context││││└──DEFAULT6Context││││└──DEFAULT7Context││││└──TerminalNodeImpl:a│││└──TerminalNodeImpl:)││├──TerminalNodeImpl:*││└──DEFAULT5Context││└──DEFAULT6Context││└──DEFAULT7Context││└──TerminalNodeImpl:4│├──TerminalNodeImpl:-│└──DEFAULT4Context│└──DEFAULT5Context│└──DEFAULT6Context│└──DEFAULT7Context│└──TerminalNodeImpl:199├──TerminalNodeImpl:=└──DEFAULT3Context└──DEFAULT4Context└──INVISIBLETIMESContext├──DEFAULT5Context│└──DEFAULT6Context│└──DEFAULT7Context│└──TerminalNodeImpl:r└──DEFAULT6Context└──DEFAULT7Context└──TerminalNodeImpl:a";
        ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
                new DeclareStatement("{syntax={infix, 7, \"*\", l}, meaning=arith1.times}", 0),
                new DeclareStatement("{syntax={infix, 5, \"-\", l}, meaning=arith1.minus}", 0),
                new DeclareStatement("{syntax={infix, 5, \"+\", l}, meaning=arith1.plus}", 0),
                new DeclareStatement("{syntax={infix, 7, \"/\", l}, meaning=arith1.divide}", 0),
                new DeclareStatement("{syntax={infix, 1, \"=\", l}, meaning=relation.eq}", 0)
        )));

        DeclarationParser.declarationFinder(ps);
        ArrayList<DeclareNode> existingRules = new ArrayList<DeclareNode>();
        ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(ps, existingRules);

        String grammar = GrammarGenerator.createGrammar(nodes);
        String ans = "";
        try {
            ParseTree formulaTree = gc.compile(grammar, "1+(5/a)*4-199=ra");
            ans = OutputWriter.prettyParseTree(formulaTree).replaceAll(" ", "").replaceAll("\n", "");
        } catch (IOException r)  {
            System.out.println("FormulaParserTest failed to IO");
        }
        assertTrue(ans.replaceAll("\\s+","").equals(formulaSt.replaceAll("\\s+","")));

    }
    /* More complex test */
    @Test
    public void FormulaStringToParsetree4() {

        String formulaSt = "└──DEFAULT0Context└──DEFAULT1Context└──Op3Context├──DEFAULT2Context│└──DEFAULT3Context│└──DEFAULT4Context│└──Op5Context│├──DEFAULT6Context││└──DEFAULT7Context││└──MACRO4Context││├──TerminalNodeImpl:\\frac││├──TerminalNodeImpl:{││├──DEFAULT0Context│││└──DEFAULT1Context│││└──DEFAULT2Context│││└──DEFAULT3Context│││└──DEFAULT4Context│││└──DEFAULT5Context│││└──DEFAULT6Context│││└──DEFAULT7Context│││└──DEFAULT8Context│││└──TerminalNodeImpl:5││├──TerminalNodeImpl:}││├──TerminalNodeImpl:{││├──DEFAULT0Context│││└──DEFAULT1Context│││└──DEFAULT2Context│││└──DEFAULT3Context│││└──DEFAULT4Context│││└──DEFAULT5Context│││└──DEFAULT6Context│││└──DEFAULT7Context│││└──DEFAULT8Context│││└──TerminalNodeImpl:2││└──TerminalNodeImpl:}│├──TerminalNodeImpl:*│├──TerminalNodeImpl:*│└──DEFAULT5Context│└──DEFAULT6Context│└──DEFAULT7Context│└──DEFAULT8Context│└──TerminalNodeImpl:a├──TerminalNodeImpl:=└──Op1Context├──DEFAULT3Context│└──Op2Context│├──Op0Context││├──DEFAULT4Context│││└──DEFAULT5Context│││└──DEFAULT6Context│││└──DEFAULT7Context│││└──DEFAULT8Context│││└──TerminalNodeImpl:7││├──TerminalNodeImpl:*││└──DEFAULT5Context││└──DEFAULT6Context││└──DEFAULT7Context││└──DEFAULT8Context││└──TerminalNodeImpl:2│├──TerminalNodeImpl:/│└──DEFAULT5Context│└──DEFAULT6Context│└──DEFAULT7Context│└──DEFAULT8Context│└──TerminalNodeImpl:5├──TerminalNodeImpl:+└──DEFAULT4Context└──DEFAULT5Context└──DEFAULT6Context└──DEFAULT7Context└──DEFAULT8Context└──TerminalNodeImpl:o";
        ParsedStatement ps = new ParsedStatement("", 0, new ArrayList<ParsedStatement>(Arrays.asList(
                new DeclareStatement("{syntax={infix, 7, \"*\", l}, meaning=arith1.times}", 0),
                new DeclareStatement("{syntax={infix, 5, \"+\", l}, meaning=arith1.plus}", 0),
                new DeclareStatement("{syntax={infix, 7, \"/\", l}, meaning=arith1.divide}", 0),
                new DeclareStatement("{syntax={infix, 1, \"=\", l}, meaning=relation.eq}", 0),
                new DeclareStatement("{macro=\\frac, meaning=arith1.divide, argspec=[2], code={...}}", 0),
                new DeclareStatement("{syntax={infix, 9, \"**\", r}, meaning=transc1.exp}", 0)
        )));

        DeclarationParser.declarationFinder(ps);
        ArrayList<DeclareNode> existingRules = new ArrayList<DeclareNode>();
        ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(ps, existingRules);

        String grammar = GrammarGenerator.createGrammar(nodes);
        String ans = "";
        try {
            ParseTree formulaTree = gc.compile(grammar, "\\frac{5}{2}**a=7*2/5+o");
            ans = OutputWriter.prettyParseTree(formulaTree).replaceAll(" ", "").replaceAll("\n", "");
        } catch (IOException r) {
            System.out.println("FormulaParserTest failed to IO");
        }


        assertTrue(ans.replaceAll("\\s+","").equals(formulaSt.replaceAll("\\s+","")));

    }

}
