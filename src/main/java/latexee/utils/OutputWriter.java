package main.java.latexee.utils;




import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.FormulaStatement;
import main.java.latexee.docast.IncludeStatement;
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
	public static String prettyParseTree(ParseTree tree){
    	StringBuilder sb = new StringBuilder();
        print("", true, sb,tree);
		return sb.toString();
	}
    private static void print(String prefix, boolean isTail, StringBuilder sb, ParseTree tree) {
    	String text = "";
    	if (tree instanceof TerminalNodeImpl)
    		text = ": " + tree.getText();
        sb.append(prefix + (isTail ? "└── " : "├── ") +tree.getClass().getSimpleName()+text+"\n");
        for (int i = 0; i < tree.getChildCount() - 1; i++) {
            print(prefix + (isTail ? "    " : "│   "), false, sb,tree.getChild(i));
        }
        if (tree.getChildCount() > 0) {
            print(prefix + (isTail ?"    " : "│   "), true, sb,tree.getChild(tree.getChildCount()-1));
        }
    }
}
