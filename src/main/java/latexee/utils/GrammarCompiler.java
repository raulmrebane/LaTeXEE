package main.java.latexee.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.Tool;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.docast.ParsedStatement;
import main.java.latexee.logging.Logger;

public class GrammarCompiler {
	private static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	private static int packageIncrement = 0;
	public static boolean foundErrors;
	public static ParseTree compile(List<DeclareNode> nodes, String grammar, String formula) throws IOException{
        ParseTree tree = null;
        packageIncrement++;
        
        Path tempDir = Files.createTempDirectory("LaTeXEE", new FileAttribute[0]);
        
        //Writes the grammar string into a .g4 file in the temp directory.
        //Calls out ANTLR in the same folder
        createSource(tempDir,grammar); 
        
        //Compiles .java files in that folder
        compileSourceFolder(tempDir);
		
        //Loads compiled classes into java, ClassInfo is just a container for stuff we need for reflection
        //ClassInfo pair = loadClasses(tempDir);
        ClassInfo pair = loadClasses(tempDir);
        
        //Extracting values from wrapper class
        Constructor lexerCtor = pair.getLexer();
        Constructor parserCtor = pair.getParser();
        Class parserClass = pair.getParserClass();
        

        Lexer lexer = null;
        Parser parser = null;
        ANTLRInputStream antlrInput = new ANTLRInputStream(formula);
        
        try {
        	//Using reflected constructors and methods to initiate parsing
			lexer = (Lexer) lexerCtor.newInstance(antlrInput);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			parser = (Parser) parserCtor.newInstance(tokens);
			
			foundErrors = false;
			DescriptiveErrorListener.locationData = new ArrayList<Object[]>();
			
			//This lets us handle any ANTLR errors that occur during parsing/lexing
			parser.removeErrorListeners();
			lexer.removeErrorListeners();
			parser.addErrorListener(DescriptiveErrorListener.INSTANCE);
			lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);
			
			//The method we're invoking takes no parameters
			Class[] params = {};
			Method parsingMethod = parserClass.getMethod("highestLevel", params);
			Object rawObject = parsingMethod.invoke(parser);
			
			if(foundErrors){
				//TODO: Make our own exception type for this purpose
				Logger.log("Error in formula: "+formula+"\n");
				System.out.println("Error in formula: "+formula+":");
				for (Object[] data : DescriptiveErrorListener.locationData) {
					Integer line = (Integer) data[0];
					Integer charPositionInLine = (Integer) data[1];
					String msg = (String) data[2];
					System.out.println("Syntax error on line " + line + " character " + charPositionInLine + ": " + msg);
				}
				return null;
			}
			tree = (ParseTree) rawObject;
			
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        //tempDir.toFile().deleteOnExit();
        
        return tree;
	}
	
	private static void compileSourceFolder(Path path) throws IOException{
		
		//Filtering files in folder by extension (that's what the regex does) and adding them to a list
		ArrayList<File> sourceFiles = new ArrayList<File>();
		Files.walk(path.toAbsolutePath()).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		    	if(filePath.toString().replaceAll("^.*\\.(.*)$", "$1").equals("java")){
		    		sourceFiles.add(new File(filePath.toString()));
		    	};
		    }
		});
		
		//Compiling all the .java files
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits1 =
            fileManager.getJavaFileObjectsFromFiles(sourceFiles);
        compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
        fileManager.close();
		
	}
	
	private static void createSource(Path path, String grammar) throws IOException{
		String tempPath = path.toString();
		
		//Writing .g4 file to temp directory
		Writer writer = null;
        try{
        	Logger.log("Attempting to write grammar to temp directory "+tempPath+".");
        	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempPath + File.separator+"RuntimeGrammar.g4"), "utf-8"));
        	writer.write(grammar);
        }		
        catch (Exception e){
			Logger.log("Could not access temp directory.");
			System.exit(1);
		}finally{
			writer.close();
		}
        
        Logger.log("Finished without errors");
        
        //Add a package name to each class so we would not have issues with naming
        String packageString = "LaTeXEE"+Integer.toString(packageIncrement);
        String[] args2 = {"-o",tempPath+File.separator+packageString,"-encoding","UTF-8","-package",packageString,tempPath+File.separator+"RuntimeGrammar.g4"};
		Tool antlr = new Tool(args2);
		
		//Generate .java files
		antlr.processGrammarsOnCommandLine();
	}
	
	private static ClassInfo loadClasses(Path path) throws IOException{
		String pathString = path.toString()+File.separator;
		
		//Specifying the source folder will allow java to just search for other missing classes it needs.
		URL folder = new URL("file://"+pathString);
		URL[] urls = {folder};
		
		URLClassLoader cl = new URLClassLoader(urls, GrammarCompiler.class.getClassLoader());
		
		//Things we need to initialize in try/catch blocks
        Class lexerClass = null;
        Constructor lexerCtor = null;
        Class parserClass = null;
        Constructor parserCtor = null;
        ClassInfo pair = null;
        
		try {
			String packageName = "LaTeXEE"+Integer.toString(packageIncrement);
			
			//The only two classes we really need to access directly
			lexerClass = cl.loadClass(packageName+".RuntimeGrammarLexer");
			parserClass = cl.loadClass(packageName+".RuntimeGrammarParser");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			//Getting the necessary constructors 
			lexerCtor = lexerClass.getDeclaredConstructor(CharStream.class);
			lexerCtor.setAccessible(true);
			parserCtor = parserClass.getDeclaredConstructor(TokenStream.class);
			parserCtor.setAccessible(true);
			pair =  new ClassInfo(parserCtor, lexerCtor, parserClass);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pair;
	}
}

class ClassInfo{
	private Constructor parserCtor;
	private Constructor lexerCtor;
	private Class parserClass;
	public ClassInfo(Constructor parserCtor, Constructor lexerCtor, Class parserClass){
		this.parserCtor = parserCtor;
		this.lexerCtor = lexerCtor;
		this.parserClass = parserClass;
	}
	public Class getParserClass(){
		return parserClass;
	}
	public Constructor getParser() {
		return parserCtor;
	}
	public Constructor getLexer() {
		return lexerCtor;
	}
}
