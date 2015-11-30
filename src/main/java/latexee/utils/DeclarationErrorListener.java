package main.java.latexee.utils;

import main.java.latexee.logging.Logger;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class DeclarationErrorListener extends BaseErrorListener {
	//public static DeclarationErrorListener INSTANCE = new DeclarationErrorListener();
	private DeclarationParser dp;
	
	public DeclarationErrorListener(DeclarationParser dp) {
		this.dp = dp;
	}
	
	@Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, 
    		int charPositionInLine, String msg, RecognitionException e) {
		
		//DeclarationParser.foundErrors = true;
		dp.foundError();
		
		charPositionInLine++;
		
		if (msg.contains("extraneous input")) { //example: \declare{syntax={infix,7,"+",l}, meaning=artih1.sum, misc=&abc"}
			String input = getInput(msg);
    		String expectedElements = getExpectedElements(msg);
    		msg = "Extraneous input: " + input + ", were expecting one of the following: " + expectedElements;
		}
		else if (msg.contains("no viable alternative at input")) { //example: \declare{syntax={infix,7,"+",l}, meaning=artih1.sum, misc={&abc"} (???)
			msg = "Something missing from the declaration, possibly a brace or a key-value pair.";
		}
		else if (msg.contains("missing")) {
			msg = msg.replaceAll("missing", "Missing character:");
		}
		msg = msg.replaceAll("WS", "whitespace");
		msg = msg.replaceAll("<EOF>", "the end of the file");

		Logger.log("Syntax error at character " + charPositionInLine + ": " + msg);
		
	}
	
	public static String getInput(String msg) { //TODO: viia see ja getExpEl ühisesse ülemklassi?
    	String end = msg.substring(msg.indexOf('\'')+1, msg.length()-2);
		return "'" + end.substring(0, end.indexOf('\'')) + "'";
    }
    
    public static String getExpectedElements(String msg) {
    	String expected = msg.substring(msg.indexOf('{')+1, msg.length()-1);
		String expectedElements = "";
		for (String s : expected.split(",")) {
			expectedElements += s +", "; //TODO: et kaks tühikut ei jääks.
		}
		return expectedElements.substring(0, expectedElements.length()-2);
    }

}
