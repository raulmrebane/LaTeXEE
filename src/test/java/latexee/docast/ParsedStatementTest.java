package test.java.latexee.docast;

import java.util.ArrayList;

import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.utils.DocumentParser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/* This class is just an example. Remove in production. */
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
	
	@Test
	public void ExampleTest3() { //TODO: kõik testid kaustas src/test/antlr vaja testida. .toString() vaja muuta.
		ParsedStatement ps = DocumentParser.parse("$1-8/a$", new ArrayList<>());
		System.out.println(ps.toString());
	}
	

}
