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
	public static void compile(ParsedStatement AST) throws IOException{
		ArrayList<DeclareNode> nodes = GrammarGenerator.getDeclareNodes(AST, new ArrayList<DeclareNode>());
        String grammar = GrammarGenerator.createGrammar(nodes);
        System.out.println("---\n");
        System.out.println(grammar);
        System.out.println("\n---\n");
        Writer writer = null;
        
        //TODO: It doesn't clean itself up even though I tell it to
        Path tempDir = Files.createTempDirectory("LaTeXEE", new FileAttribute[0]);
        
        
        String tempPath = tempDir.toString();
        
        try{
        	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempPath + "/RuntimeGrammar.g4"), "utf-8"));
        	writer.write(grammar);
        }		
        catch (Exception e){
			Logger.log("Could not access specified file.");
			System.exit(1);
		}finally{
			writer.close();
		}
        
        Logger.log("Finished without errors");
        String[] args2 = {"-o",tempPath,"-encoding","UTF-8",tempPath+"/RuntimeGrammar.g4"};
		Tool antlr = new Tool(args2);
		
		antlr.processGrammarsOnCommandLine();
		
		ArrayList<File> sourceFiles = new ArrayList<File>();
		Files.walk(tempDir.toAbsolutePath()).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		    	if(filePath.toString().replaceAll("^.*\\.(.*)$", "$1").equals("java")){
		    		sourceFiles.add(new File(filePath.toString()));
		    	};
		    }
		});
		

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits1 =
            fileManager.getJavaFileObjectsFromFiles(sourceFiles);
        compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();

        
        //TODO: Keep the compiler for efficiency, refactor
        
        
        //Iterable<? extends JavaFileObject> compilationUnits2 =
        //    fileManager.getJavaFileObjects(files2); // use alternative method
        // reuse the same file manager to allow caching of jar files
        //compiler.getTask(null, fileManager, null, null, null, compilationUnits2).call();

        fileManager.close();
		ArrayList<File> classFiles = new ArrayList<File>();
		Files.walk(tempDir.toAbsolutePath()).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		    	if(filePath.toString().replaceAll("^.*\\.(.*)$", "$1").equals("class")){
		    		classFiles.add(new File(filePath.toString()));
		    	};
		    }
		});
        
        ClassLoader parentClassLoader = GrammarCompiler.class.getClassLoader();
        ParserClassLoader pcl = new ParserClassLoader(parentClassLoader);
        
        ArrayList<Class> loadedClasses = new ArrayList<Class>();
        
        Class lexerClass = null;
        Constructor lexerCtor = null;
        Class parserClass = null;
        Constructor parserCtor = null;
        try{
        	Class rgl = pcl.loadClass(tempPath+"/RuntimeGrammarListener.class",tempPath);
        	loadedClasses.add(rgl);
        	for(File i:classFiles){
        		String className = i.toString();
        		if(!className.equals(tempPath+"/RuntimeGrammarListener.class")){
        			Class loadedClass = pcl.loadClass(i.getAbsolutePath(),tempPath);
            		loadedClasses.add(loadedClass);
            		if(className.contains("Lexer")){
            			lexerClass = loadedClass;
            		}
            		if(className.contains("Parser.class")){
            			parserClass = loadedClass;
            		}
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
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Lexer lexer = null;
        Parser parser = null;
        ANTLRInputStream antlrInput = new ANTLRInputStream("2+2");
        
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
			ParseTree tree = (ParseTree) o;
			System.out.println(tree.getText());
			
			
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //TODO: Make this actually work
        tempDir.toFile().delete();
	}
}
