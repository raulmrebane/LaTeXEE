package main.java.latexee.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilderFactory;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

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
    //Indents the generated XML. Code from http://stackoverflow.com/a/11519668
    //Thanks to Steve McLeod and DaoWen.
    public static String indentXML(String xml) {

        try {
            final InputSource src = new InputSource(new StringReader(xml));
            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            final Boolean keepDeclaration = Boolean.valueOf(xml.startsWith("<?xml"));

        //May need this: System.setProperty(DOMImplementationRegistry.PROPERTY,"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");


            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration); // Set this to true if the declaration is needed to be outputted.

            return writer.writeToString(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
