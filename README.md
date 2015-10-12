# LaTeXEE
Project for course "Software Project": Extraction and checking of LaTeX mathematics

### What it does  

LaTeXEE is a command-line only application which parses a valid .tex document, which contains [special \declare commands](https://github.com/Abercus/LaTeXEE/wiki/Declaration-language), parses the mathematical formulas inside the document, and outputs an OpenMath .xml file. A more detailed overview of the process can be found [here.](https://github.com/Abercus/LaTeXEE/wiki/Parsing-process-specification)

### How to use it?

1. Compile the application using [the guidelines](https://github.com/Abercus/LaTeXEE/wiki/Building-project)
2. Run the compiled jar from terminal with command "java -jar LaTeXEE filename.tex"
3. Output will be generated in the same folder where input is with name: filename.txt

### Flags

Available flags are available:

* "-o filename" change output filename
* "-h" help
* "-v" terminal output more verbose


### The team  

Joonas Puura  
Raul-Martin Rebane  
Kristine Leetberg  
Hiie Vill  
