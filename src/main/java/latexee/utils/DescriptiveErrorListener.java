package main.java.latexee.utils;

//credit: http://stackoverflow.com/questions/18132078/handling-errors-in-antlr4

import java.util.ArrayList;

import main.java.latexee.logging.Logger;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class DescriptiveErrorListener extends BaseErrorListener {
    public static DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();
    public static ArrayList<Object[]> locationData; //1. - line, 2. - charPos, 3. - error message

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, 
    		int charPositionInLine, String msg, RecognitionException e) {
    	
    	GrammarCompiler.foundErrors = true;
    	
    	if (msg.contains("token recognition error at:")) {
    		char symbol = msg.charAt(msg.length()-2);
    		msg = "Undeclared operator '" + symbol + "'";
    	}
    	else if (msg.contains("extraneous input")) {
    		String input = getInput(msg);
    		String expectedElements = getExpectedElements(msg);
    		msg = "Extraneous input: " + input + ", were expecting one of the following: " + expectedElements;
    	}
    	else if (msg.contains("no viable alternative at input")) {
    		String input = msg.substring(msg.indexOf('\'')+1, msg.length()-1);
    		msg = "Unexpected input: " + input;
    	}
    	else if (msg.contains("mismatched input")) {
    		String input = getInput(msg);
    		String expectedElements = getExpectedElements(msg);
    		msg = "Mismatched input: " + input + ", were expecting one of the following: " + expectedElements;
    	}
    	//TODO: millised errorid veel võimalikud?
    	msg = msg.replaceAll("<EOF>", "the end of the file");
        Object[] data = {line, charPositionInLine, msg};
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
