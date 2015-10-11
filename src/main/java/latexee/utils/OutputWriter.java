package main.java.latexee.utils;




import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.logging.Logger;

public class OutputWriter {
	public static void formulasToTXT(ParsedStatement tree, String filename){
		Writer writer = null;
		try {
			StringBuilder fileContent = new StringBuilder();
			gatherFormulas(tree,fileContent);
			Logger.log("Attempting to open file: "+filename);
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8")); 
			Logger.log("... OK");
			Logger.log("Writing to output file..");
		    writer.write(fileContent.toString());
		    Logger.log("... OK");
		    writer.close();
		}
		catch (Exception e){
			Logger.log("Could not access specified file.");
			System.exit(1);
		}
	}
	public static void gatherFormulas(ParsedStatement tree, StringBuilder sb){
		if(tree instanceof FormulaStatement){
			sb.append(tree.getContent()+"\n");
		}
		for(ParsedStatement child : tree.getChildren()){
			gatherFormulas(child, sb);
		}
	}
}
