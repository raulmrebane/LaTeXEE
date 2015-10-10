package main.java.latexee.logging;

import main.java.latexee.runtime.Main;

public class Logger {
	//Broken code gets changed, bad code stays forever.
	public static void log(String message){
		if(Main.logStatus){
			System.out.println(message);
		}
	}
}
