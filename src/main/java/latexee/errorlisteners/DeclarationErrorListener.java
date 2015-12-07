package main.java.latexee.errorlisteners;

import main.java.latexee.logging.Logger;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Class to listen for errors which are thrown by the antlr parser, while trying to parse declarations.
 * Using this class we can give meaningful error messages to the user if the declaration is not declared correctly.
 * Is extended from BaseErrorListener from antlr package.
 */
public class DeclarationErrorListener extends BaseErrorListener {
	private boolean errors;

	/**
	 * Constructor for the DeclarationErrorListener. Wraps around BaseErrorListener
	 */
	public DeclarationErrorListener(){
		super();
		errors = false;
	}

	/**
	 * Method, which overrides method from antlr package. This method, as an input, gets the offending
	 * mismatched symbol, location of the mismatch and the message returned by the antlr error listener.
	 * In our case the line is always 1.
	 * With this method we change the incoming error message to something more user friendly
	 *
	 * @param recognizer antlr parser
	 * @param offendingSymbol mismatched symbol
	 * @param line line of the mismatch
	 * @param charPositionInLine position of the character for the mismatch
	 * @param msg error message returned by antlr
	 * @param e antlr recognition exception
	 */
	@Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, 
    		int charPositionInLine, String msg, RecognitionException e) {
		
		errors = true;
		
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

	/**
	 * Used to extract the substring between quotes.
	 * @param msg string
	 * @return substring of param msg which is between quotes.
	 */
	private String getInput(String msg) {
		int i = msg.indexOf('\'');
    	return msg.substring(i, msg.indexOf('\'', i+1));
	}

	/**
	 * Extracts which symbols were expected from the input string
	 * @param msg string returned by antlr recognizer
	 * @return string formatted for use in custom error messages
	 */
    private String getExpectedElements(String msg) {
		StringBuilder sb = new StringBuilder();
    	String expected = msg.substring(msg.indexOf('{')+1, msg.length()-1);
		String[] sp = expected.split(",");
		sb.append(sp[0]);
		for (int i=1; i<sp.length; ++i) {
			sb.append(", ");
			sb.append(sp[i]);

		}
		return sb.toString();
    }

	/**
	 * Getter method to check if error occurred while trying to parse a declaration
	 * @return true if there error occurred while parsing declaration.
	 */
    public boolean foundErrors(){
    	return this.errors;
    }

}
