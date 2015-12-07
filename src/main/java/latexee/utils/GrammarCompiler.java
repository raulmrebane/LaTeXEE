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
import java.util.HashMap;
import java.util.Map;

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

import main.java.latexee.errorlisteners.FormulaErrorListener;
import main.java.latexee.logging.Logger;

/**
 * GrammarCompiler class methods are used to compile the grammar to class files which can later be loaded during runtime of the application.
 * Takes ANTLR string, creates a temp location for the grammar to be stored and is then compiled.
 * Compiled grammar is later used to parse the formulas in the document.
 */
public class GrammarCompiler {
	private static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	private Map<String,ClassInfo> grammarMap;
	private int packageIncrement;

	/**
	 * Constructor for GrammarCompiler. Initializes HashMap, which maps grammar to compiled classes.
	 */
	public GrammarCompiler(){
		this.packageIncrement = 0;
		this.grammarMap = new HashMap<String,ClassInfo>();
	}

	/**
	 * This method takes input of grammar string which is generate by grammar generator and a string which is going to be parsed by the gramamr.
	 * Creates new temporary directory to store the grammar files, creates .g4 files, generates ANTLR .java files and then compiles the
	 * source folder where the ANTLR generated java files are.
	 * @param grammar generated input grammar from grammar generator
	 * @param formula formula which is parsed by the grammar
	 * @return parse tree of the the parsed formula
	 * @throws IOException usually thrown when not enough rights to create new folders or files
	 */
	public ParseTree compile(String grammar, String formula) throws IOException{
		ClassInfo pair = null;
		ParseTree tree = null;
		pair = grammarMap.get(grammar);
		
		if (pair==null) {
	        packageIncrement++;
	        
	        Path tempDir = Files.createTempDirectory("LaTeXEE", new FileAttribute[0]);
	        
	        //Writes the grammar string into a .g4 file in the temp directory.
	        //Calls out ANTLR in the same folder
	        createSource(tempDir,grammar); 
	        
	        //Compiles .java files in that folder
	        compileSourceFolder(tempDir);
			
	        //Loads compiled classes into java, ClassInfo is just a container for stuff we need for reflection
	        //ClassInfo pair = loadClasses(tempDir);
	        pair = loadClasses(tempDir);
	        grammarMap.put(grammar, pair);
	        markForDeletion(tempDir.toFile());
		}
		
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
			
			//This lets us handle any ANTLR errors that occur during parsing/lexing
			parser.removeErrorListeners();
			lexer.removeErrorListeners();
			FormulaErrorListener fel = new FormulaErrorListener();
			parser.addErrorListener(fel);
			lexer.addErrorListener(fel);
			
			Logger.log("Parsing formula " + formula);
			
			//The method we're invoking takes no parameters
			Class[] params = {};
			Method parsingMethod = parserClass.getMethod("highestLevel", params);
			Object rawObject = parsingMethod.invoke(parser);
			
			if(fel.foundErrors()){
				Logger.log("Parsing finished with errors.\n");
				return null;
			}
			Logger.log("Parsing successful.\n");
			tree = (ParseTree) rawObject;
			
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
        
        return tree;
	}

	/**
	 * This method compiles java files in the given path.
	 * Checks if the file is java file, calls the compiler to compile them.
	 * @param path location of the folder which is to be compiled.
	 * @throws IOException
	 */
	private void compileSourceFolder(Path path) throws IOException{
		
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

	/**
	 * Writes ANTLR grammar to a temporary folder and generates java classes which are later compiled
	 * @param path location of the temporary folder created in compile method.
	 * @param grammar grammar which is saved as .g4 file and from which .java file is generated
	 * @throws IOException usually thrown when not enough rights at the given location.
	 */
	private void createSource(Path path, String grammar) throws IOException{
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
        
        Logger.log("Finished without errors.\n");
        
        //Add a package name to each class so we would not have issues with naming
        String packageString = "LaTeXEE"+Integer.toString(packageIncrement);
        String[] args2 = {"-o",tempPath+File.separator+packageString,"-encoding","UTF-8","-package",packageString,tempPath+File.separator+"RuntimeGrammar.g4"};
		Tool antlr = new Tool(args2);
		
		//Generate .java files
		antlr.processGrammarsOnCommandLine();
	}

	/**
	 * This method is used to load classes in runtime using Java class loader.
	 * Loads the generated grammar classes, so they can be used to parse the formulas
	 * @param path location of the class to be loaded
	 * @return ClassInfo instance which contains all the required constructors to use them in runtime
	 * @throws IOException usually thrown when the given class file does not exist or unable to load classes.
	 */
	private ClassInfo loadClasses(Path path) throws IOException{
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
			e.printStackTrace();
		}
		return pair;
	}

	/**
	 * Method to mark which files are to be deleted after the JVM exits.
	 * @param file filepath to be marked for deletion after the JVM exits.
	 */
	private void markForDeletion(File file) {
		file.deleteOnExit();
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            markForDeletion(f);
	        }
	    }	    
	}
}

/**
 * Class to hold constructors of the generated, compiled and loaded grammar source files
 */
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
