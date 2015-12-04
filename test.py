# Test script for LaTeXEE application.
# Run from CMD: python3 test.py


START = 'java -jar ../build/libs/LaTeXEE.jar '
INPUT = "../src/test/antlr/basic_with_nonsemantic.tex"

from scripttest.script import TestFileEnvironment
 
nr = 0
        
# Basic Tester. Takes nr of 
def tester(arg, patterns, name, env):
    global nr
    nr += 1
    
    t = str(env.run(arg))
    for pattern in patterns:
        if pattern not in patterns:
            print("Test #{0}: {1}. FAIL".format(nr, name))
            return
            
    print("Test #{0}: {1}. PASS".format(nr, name))

    
# Function takes input file, parses it to .xml file and then compares to given test file.
def file_compare(arg, out_file, compare_file, name, env):
        global nr
        nr += 1
        t1 =str(env.run(arg))
        #print(t1)
        with open("test-output/" + out_file) as f:
             text1 = f.read()
        with open("scripttest/testfiles/" + compare_file) as f:
             text2 = f.read()
        if text1 == text2:
             print("Test #{0}: {1}. PASS".format(nr, name))
        else:
             print("Test #{0}: {1}. FAIL".format(nr, name))

        print("Length test: {0}, Length compare: {1}".format(len(text1), len(text2)))
	 
        
if __name__ == "__main__":
    env = TestFileEnvironment('./test-output')
    # Expected result. Display help menu and no input
    tester(START, ["usage"], "No arguments", env)
    # Expected result: Displays help
    tester(START + "-h", ["usage"], "Help flag", env)
    # Expected result: No inputfile specified
    tester(START + "-v", ["No inputfile"], "Missing input file", env)
    # Tests output flag and parsing process. Expected result: created file is the same as the test file.
    file_compare(START + "../src/test/antlr/basic_with_nonsemantic.tex -o output_test.xml -v", "output_test.xml", "output_test_1.test", "Parsing test", env)
    # Popcorn test
    file_compare(START + "../src/test/antlr/basic_with_nonsemantic.tex -p -o popcorn.pop", "popcorn.pop", "popcorn.test", "Popcorn test", env)