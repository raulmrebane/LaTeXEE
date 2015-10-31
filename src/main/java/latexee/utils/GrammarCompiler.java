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
	private static ParserClassLoader pcl = new ParserClassLoader(GrammarCompiler.class.getClassLoader());
	private static int packageIncrement = 0;
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
        ClassInfo pair = loadClasses(tempDir);
        
        Constructor lexerCtor = pair.getLexer();
        Constructor parserCtor = pair.getParser();
        Class parserClass = pair.getParserClass();
        

        Lexer lexer = null;
        Parser parser = null;
        ANTLRInputStream antlrInput = new ANTLRInputStream(formula);
        
        try {
			lexer = (Lexer) lexerCtor.newInstance(antlrInput);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			parser = (Parser) parserCtor.newInstance(tokens);
			Method[] allMethods = parserClass.getMethods();
			Object o = null;
			for(Method m : allMethods){
				if(m.getName().equals("highestLevel")){
					m.setAccessible(true);
					o = m.invoke(parser);
				}
				
			}
			tree = (ParseTree) o;			
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //tempDir.toFile().deleteOnExit();
        
        return tree;
	}
	
	private static void compileSourceFolder(Path path) throws IOException{
		ArrayList<File> sourceFiles = new ArrayList<File>();
		Files.walk(path.toAbsolutePath()).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		    	if(filePath.toString().replaceAll("^.*\\.(.*)$", "$1").equals("java")){
		    		sourceFiles.add(new File(filePath.toString()));
		    	};
		    }
		});
		
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits1 =
            fileManager.getJavaFileObjectsFromFiles(sourceFiles);
        compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
        fileManager.close();
		
	}
	
	private static void createSource(Path path, String grammar) throws IOException{
		String tempPath = path.toString();
		Writer writer = null;
        try{
        	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempPath + File.separator+"RuntimeGrammar.g4"), "utf-8"));
        	writer.write(grammar);
        }		
        catch (Exception e){
			Logger.log("Could not access specified file.");
			System.exit(1);
		}finally{
			writer.close();
		}
        
        Logger.log("Finished without errors");
        String packageString = "LaTeXEE"+Integer.toString(packageIncrement);
        String[] args2 = {"-o",tempPath,"-encoding","UTF-8","-package",packageString,tempPath+File.separator+"RuntimeGrammar.g4"};
		Tool antlr = new Tool(args2);
		
		antlr.processGrammarsOnCommandLine();
	}
	
	private static ClassInfo loadClasses(Path path) throws IOException{
		String pathString = path.toString();
		ArrayList<File> classFiles = new ArrayList<File>();
		Files.walk(path.toAbsolutePath()).forEach(filePath -> {
			//filePath.toFile().deleteOnExit();
		    if (Files.isRegularFile(filePath)) {
		    	if(filePath.toString().replaceAll("^.*\\.(.*)$", "$1").equals("class")){
		    		classFiles.add(new File(filePath.toString()));
		    	};
		    }
		});
        
        ArrayList<Class> loadedClasses = new ArrayList<Class>();
        
        Class lexerClass = null;
        Constructor lexerCtor = null;
        Class parserClass = null;
        Constructor parserCtor = null;
        ClassInfo pair = null;
        String packageString = "LaTeXEE"+Integer.toString(packageIncrement);
        try{ 
        	ArrayList<String> loadManually = new ArrayList<String>();
        	String rgls = pathString+File.separator+"RuntimeGrammarListener.class"; 
        	String pcls = pathString+File.separator+"RuntimeGrammarParser$LowestLevelContext.class"; 
        	String hnc = pathString+File.separator+"RuntimeGrammarParser$HighestNumberContext.class"; 
        	
        	loadManually.addAll(Arrays.asList(rgls, pcls, hnc));
        	
        	for(String i: loadManually){ 
        		Class manual = pcl.loadClass(i,pathString,packageString); 
        		loadedClasses.add(manual); 
        	} 
        	
        	ArrayList<File> toLoadLater = new ArrayList<File>(); 
        	
        	for(File i:classFiles){ 
        		String className = i.toString(); 
        		if(!loadManually.contains(className)){ 
        			if(!className.contains("Level")){ 
        				toLoadLater.add(i); 
        			}
        			else{
	        			Class loadedClass = pcl.loadClass(i.getAbsolutePath(),pathString,packageString);
	            		loadedClasses.add(loadedClass);
	            		if(className.contains("RuntimeGrammarLexer.class")){
	            			lexerClass = loadedClass;
	            		}
	            		if(className.contains("RuntimeGrammarParser.class")){
	            			parserClass = loadedClass;
	            		}
        			}
        		}
        	}
        	for(File i:toLoadLater){
        		String className = i.toString();
    			Class loadedClass = pcl.loadClass(i.getAbsolutePath(),pathString,packageString);
        		loadedClasses.add(loadedClass);
        		if(className.contains("Lexer")){
        			lexerClass = loadedClass;
        		}
        		if(className.contains("Parser.class")){
        			parserClass = loadedClass;
        		}
        	}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			lexerCtor = lexerClass.getDeclaredConstructor(CharStream.class);
			lexerCtor.setAccessible(true);
			parserCtor = parserClass.getDeclaredConstructor(TokenStream.class);
			parserCtor.setAccessible(true);
			pair =  new ClassInfo(parserCtor, lexerCtor, parserClass);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
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
