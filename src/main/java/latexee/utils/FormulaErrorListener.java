package main.java.latexee.utils;

//credit: http://stackoverflow.com/questions/18132078/handling-errors-in-antlr4

import java.util.ArrayList;
import java.util.Arrays;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class FormulaErrorListener extends BaseErrorListener {
    public static FormulaErrorListener INSTANCE = new FormulaErrorListener();
    public static ArrayList<Object[]> locationData; //1. - charPos, 2. - error message

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, 
    		int charPositionInLine, String msg, RecognitionException e) {
    	
    	GrammarCompiler.foundErrors = true;
    	
    	charPositionInLine++;
    	
    	if (msg.contains("token recognition error at:")) { //example: $*$
    		String operator = msg.substring(msg.indexOf('\'')+1, msg.length()-1);
    		msg = "Undeclared or extraneous operator '" + operator + "'";
    	}
    	else if (msg.contains("extraneous input")) { //example: $2*$ or $2)$
    		String input = getInput(msg);
    		String expectedElements = getExpectedElements(msg);
    		msg = "Extraneous input: " + input + ", were expecting one of the following: " + expectedElements;
    	}
    	else if (msg.contains("no viable alternative at input")) { //example: $*$
    		String input = msg.substring(msg.indexOf('\'')+1, msg.length()-1);
    		msg = "Unexpected input: " + input;
    	}
    	else if (msg.contains("mismatched input")) {
    		String input = getInput(msg);
    		String expectedElements = getExpectedElements(msg);
    		msg = "Mismatched input: " + input + ", were expecting one of the following: " + expectedElements;
    	}
    	else if (msg.contains("missing")) { //example: $(2$
    		msg = msg.replaceAll("missing", "Missing character:");
    	}
    	msg = msg.replaceAll("<EOF>", "the end of the file");
    	ArrayList<String> lexerTokens = new ArrayList<>(Arrays.asList("variable name", "integer constant"));
    	msg = msg.replaceAll("LEXERRULE", lexerTokens.toString());
        Object[] data = {charPositionInLine, msg};
        locationData.add(data);
    }
    
    public static String getInput(String msg) {
    	String end = msg.substring(msg.indexOf('\'')+1, msg.length()-2);
		return end.substring(0, end.indexOf('\''));
    }
    
    public static String getExpectedElements(String msg) {
    	String expected = msg.substring(msg.indexOf('{')+1, msg.length()-1);
		String expectedElements = "";
		for (String s : expected.split(",")) {
			expectedElements += s +", ";
		}
		return expectedElements.substring(0, expectedElements.length()-2);
    }
}
