package main.java.latexee.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import main.java.latexee.logging.Logger;

public class ParserClassLoader extends ClassLoader{
    public ParserClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class loadClass(String name, String pathName, String packageName) throws ClassNotFoundException {
        FileInputStream fis;
		try {
			File file = new File(name);
				fis = new FileInputStream(file);
			
	        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	        
	        int data = fis.read();
	        
	        while(data != -1){
	            buffer.write(data);
	            data = fis.read();
	        }
	        fis.close();
	        byte[] classData = buffer.toByteArray();
	        String fullName = file.getPath().replace(pathName+File.separator, "");
	        String noExtension = fullName.substring(0, fullName.length()-6);
	        return defineClass(packageName+"."+noExtension,
	                classData, 0, classData.length);
		} catch (IOException e) {
			Logger.log("Could not access file: "+name);
		}

        return null;
    }
}
