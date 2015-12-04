package main.java.latexee.logging;

import main.java.latexee.runtime.Main;

/**
 * Provides logging capability for the project.
 * Currently only supports writing to standard output.
 */
public class Logger {
	/**
	 * Method to output logging message to standard output. Currently only when the verbose flag is true.
	 * @param message string which is output to standard output
	 */
	//Broken code gets changed, bad code stays forever.
	public static void log(String message){
		if(Main.verbose){
			System.out.println(message);
		}
	}
}
