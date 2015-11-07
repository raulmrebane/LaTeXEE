package test.java.latexee.docast;

import java.util.ArrayList;

import java.util.Arrays;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.IncludeStatement;
import main.java.latexee.docast.LemmaStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.docast.ProofStatement;
import main.java.latexee.docast.TheoremStatement;
import main.java.latexee.utils.DeclarationParser;
import main.java.latexee.utils.DocumentParser;
import main.java.latexee.utils.GrammarGenerator;

import org.junit.Test;

import fr.inria.openmath.omapi.TreePrinter;
import fr.inria.openmath.omapi.implementation.TreePrinterImpl;

import static org.junit.Assert.*;



public class GrammarTest {
	
	
	@Test
	public void Parsing1Test(){
		DeclareNode.identifier = 0;
		ParsedStatement tree = DocumentParser.parse("src/test/antlr/parsing1.tex");
		DeclarationParser.declarationFinder(tree);
		ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(tree, new ArrayList<DeclareNode>());

		String generatedGrammar = GrammarGenerator.createGrammar(nodes);
		generatedGrammar = generatedGrammar.replaceAll(" ", "");
		generatedGrammar = generatedGrammar.replaceAll("\n", "");
		String grammar = "grammarRuntimeGrammar;"+
		"highestLevel:highestNumber#DEFAULT0;"+
		"highestNumber:level100#DEFAULT1;"
		+ "level100:level100level101#INVISIBLETIMES"
		+ "|level101#DEFAULT2;"
		+ "level101:lowestLevel#DEFAULT3;"
		+ "lowestLevel:'{'highestLevel'}'#BRACKETS"
		+ "|LEXERRULE#DEFAULT4;"
		+ "LEXERRULE:[0-9]+|[a-z];";
		grammar = grammar.replaceAll(" ", "");
		grammar = grammar.replaceAll("\n", "");
		assertTrue(generatedGrammar.equals(grammar));
	}

	@Test
	public void Parsing8Test(){
		DeclareNode.identifier = 0;
		ParsedStatement tree = DocumentParser.parse("src/test/antlr/parsing8.tex");
		DeclarationParser.declarationFinder(tree);
		ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(tree, new ArrayList<DeclareNode>());

		String generatedGrammar = GrammarGenerator.createGrammar(nodes);
		generatedGrammar = generatedGrammar.replaceAll(" ", "");
		generatedGrammar = generatedGrammar.replaceAll("\n", "");
		String grammar = "grammarRuntimeGrammar;"
				+ "highestLevel : highestNumber #DEFAULT0;"
				+ "highestNumber : level7 #DEFAULT1;"
				+ "level7 : level7'/'level100 #Op0"
				+ "|level7'/'level100 #Op1"
				+ "|level100 #DEFAULT2;"
				+ "level100 : level100 level101 #INVISIBLETIMES"
				+ "|level101 #DEFAULT3;"
				+ "level101 : lowestLevel #DEFAULT4;"
				+ "lowestLevel : '{'highestLevel'}' #BRACKETS"
				+ "|LEXERRULE #DEFAULT5;"
				+ "LEXERRULE : [0-9]+|[a-z];";
		grammar = grammar.replaceAll(" ", "");
		grammar = grammar.replaceAll("\n", "");
		
		assertTrue(generatedGrammar.equals(grammar));
	}
	
	@Test
	public void Parsing9Test(){
		DeclareNode.identifier = 0;
		ParsedStatement tree = DocumentParser.parse("src/test/antlr/parsing9.tex");
		DeclarationParser.declarationFinder(tree);
		ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(tree, new ArrayList<DeclareNode>());

		String generatedGrammar = GrammarGenerator.createGrammar(nodes);
		generatedGrammar = generatedGrammar.replaceAll(" ", "");
		generatedGrammar = generatedGrammar.replaceAll("\n", "");
		String grammar = "grammarRuntimeGrammar;"
				+ "highestLevel : highestNumber #DEFAULT0;"
				+ "highestNumber : level100 #DEFAULT1;"
				+ "level100 : level100 level101 #INVISIBLETIMES"
				+ "|level101 #DEFAULT2;"
				+ "level101 : lowestLevel #DEFAULT3;"
				+ "lowestLevel : '{'highestLevel'}' #BRACKETS"
				+ "|LEXERRULE #DEFAULT4;"
				+ "LEXERRULE : [0-9]+|[a-z];";
		grammar = grammar.replaceAll(" ", "");
		grammar = grammar.replaceAll("\n", "");
		
		assertTrue(generatedGrammar.equals(grammar));
	}
	@Test
	public void BasicWithAllDeclaresTest(){
		DeclareNode.identifier = 0;
		ParsedStatement tree = DocumentParser.parse("src/test/antlr/basic_with_all_declares.tex");
		DeclarationParser.declarationFinder(tree);
		ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(tree, new ArrayList<DeclareNode>());

		String generatedGrammar = GrammarGenerator.createGrammar(nodes);
		
		generatedGrammar = generatedGrammar.replaceAll(" ", "");
		generatedGrammar = generatedGrammar.replaceAll("\n", "");
		String grammar = "grammarRuntimeGrammar;"
				+ "highestLevel : highestNumber #DEFAULT0;"
				+ "highestNumber : level5 #DEFAULT1;"
				+ "level5 : level5'+'level7 #Op1"
				+ "|level5'-'level7 #Op2"
				+ "|level7 #DEFAULT2;"
				+ "level7:level7'/'level100 #Op0"
				+ "|level7'*'level100 #Op3"
				+ "|level100 #DEFAULT3;"
				+ "level100:level100 level101 #INVISIBLETIMES"
				+ "|level101 #DEFAULT4;"
				+ "level101 : lowestLevel #DEFAULT5;"
				+ "lowestLevel : '{'highestLevel'}' #BRACKETS"
				+ "|'\\\\gcd''{'highestLevel'}''{'highestLevel'}' #MACRO4"
				+ "|LEXERRULE#DEFAULT6;"
				+ "LEXERRULE:[0-9]+|[a-z];";
		grammar = grammar.replaceAll(" ", "");
		grammar = grammar.replaceAll("\n", "");
		assertTrue(generatedGrammar.equals(grammar));
	}
	@Test
	public void BasicWithNonsemanticTest(){
		DeclareNode.identifier = 0;
		ParsedStatement tree = DocumentParser.parse("src/test/antlr/basic_with_nonsemantic.tex");
		DeclarationParser.declarationFinder(tree);
		ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(tree, new ArrayList<DeclareNode>());

		String generatedGrammar = GrammarGenerator.createGrammar(nodes);
		
		generatedGrammar = generatedGrammar.replaceAll(" ", "");
		generatedGrammar = generatedGrammar.replaceAll("\n", "");
		String grammar = "grammarRuntimeGrammar;"
				+ "highestLevel : highestNumber #DEFAULT0;"
				+ "highestNumber : level99 #DEFAULT1;"
				+ "level99 : level99'+'level100 #Op0"
				+ "|level99'-'level100 #Op2"
				+ "|level100 #DEFAULT2;"
				+ "level100 : level100 level101 #INVISIBLETIMES"
				+ "|level100'/'level101 #Op1"
				+ "|level100'*'level10 1#Op3"
				+ "|level101 #DEFAULT3"
				+ ";level101 : lowestLevel #DEFAULT4;"
				+ "lowestLevel:'{'highestLevel'}' #BRACKETS"
				+ "|'\\\\gcd''{'highestLevel'}''{'highestLevel'}' #MACRO4"
				+ "|'\\\\gcd2''{'highestLevel'}''{'highestLevel'}' #MACRO5"
				+ "|LEXERRULE #DEFAULT5;"
				+ "LEXERRULE:[0-9]+|[a-z];";
		grammar = grammar.replaceAll(" ", "");
		grammar = grammar.replaceAll("\n", "");
		assertTrue(generatedGrammar.equals(grammar));
	}
	
	@Test
	public void BasicWithScopingTest(){
		DeclareNode.identifier = 0;
		ParsedStatement tree = DocumentParser.parse("src/test/antlr/basic_with_scoping.tex");
		DeclarationParser.declarationFinder(tree);
		ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(tree, new ArrayList<DeclareNode>());

		String generatedGrammar = GrammarGenerator.createGrammar(nodes);
		
		generatedGrammar = generatedGrammar.replaceAll(" ", "");
		generatedGrammar = generatedGrammar.replaceAll("\n", "");
		String grammar = "grammarRuntimeGrammar;"
				+ "highestLevel : highestNumber #DEFAULT0;"
				+ "highestNumber : level5 #DEFAULT1;"
				+ "level5 : level5'+'level7 #Op0"
				+ "|level5'-'level7 #Op2"
				+ "|level7 #DEFAULT2;"
				+ "level7 : level7'/'level100 #Op1"
				+ "|level7'*'level100 #Op3"
				+ "|level100 #DEFAULT3;"
				+ "level100 : level100 level101 #INVISIBLETIMES"
				+ "|level101 #DEFAULT4;"
				+ "level101 : lowestLevel #DEFAULT5;"
				+ "lowestLevel : '{'highestLevel'}' #BRACKETS"
				+ "|'\\\\gcd''{'highestLevel'}''{'highestLevel'}' #MACRO4"
				+ "|'\\\\gcd2''{'highestLevel'}''{'highestLevel'}' #MACRO5"
				+ "|LEXERRULE#DEFAULT6;"
				+ "LEXERRULE:[0-9]+|[a-z];";
		grammar = grammar.replaceAll(" ", "");
		grammar = grammar.replaceAll("\n", "");
		assertTrue(generatedGrammar.equals(grammar));
	}
	
	@Test
	public void GrammarGeneratorTest(){
		DeclareNode.identifier = 0;
		ParsedStatement tree = DocumentParser.parse("src/test/antlr/grammar_generator.tex");
		DeclarationParser.declarationFinder(tree);
		ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(tree, new ArrayList<DeclareNode>());

		String generatedGrammar = GrammarGenerator.createGrammar(nodes);
		generatedGrammar = generatedGrammar.replaceAll(" ", "");
		generatedGrammar = generatedGrammar.replaceAll("\n", "");
		String grammar = "grammarRuntimeGrammar;"
				+ "highestLevel : highestNumber #DEFAULT0;"
				+ "highestNumber : level7 #DEFAULT1;"
				+ "level7 : level7'*'level100 #Op3"
				+ "|level100 #DEFAULT2;"
				+ "level100 : level100 level101 #INVISIBLETIMES"
				+ "|level100'+'level101 #Op0"
				+ "|level100'-'level101 #Op2"
				+ "|level101 #DEFAULT3;"
				+ "level101 : level101'/'level102 #Op1"
				+ "|level102 #DEFAULT4;"
				+ "level102 : lowestLevel #DEFAULT5;"
				+ "lowestLevel : '{'highestLevel'}' #BRACKETS"
				+ "|'\\\\gcd''{'highestLevel'}''{'highestLevel'}' #MACRO4"
				+ "|'\\\\gcd2''{'highestLevel'}''{'highestLevel'}' #MACRO5"
				+ "|LEXERRULE #DEFAULT6;"
				+ "LEXERRULE:[0-9]+|[a-z];";
		grammar = grammar.replaceAll(" ", "");
		grammar = grammar.replaceAll("\n", "");
		assertTrue(generatedGrammar.equals(grammar));
	}
	
}
