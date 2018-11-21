CS4212 Project Assignment 3
Author: Wu Wenqi        
Matric No: A0124278A

Compiler implementation for the JLite programming language. 
Tested on macOS with Java 10.0, JFlex 1.6.1 and CUP 0.11a.

-----------------------------------------------------------------------------------------
INSTRUCTIONS
-----------------------------------------------------------------------------------------

Update the following variables for Makefile:
    - CUP_JAR: Change this to the path of your CUP jar file (preferably version 0.11a).
    - JFLEX: Change this to the path of jflex or add jflex to your PATH. 

In the terminal, navigate to project directory and run "make".
This will generate all the test asm files and save them in build/ directory.

Compile without optimization:
java -cp .:<path_to_cup_jar>:classes wwu.compiler.Jlc <input_file> > <output_file>

Compile with optimization
java -cp .:<path_to_cup_jar>:classes wwu.compiler.Jlc <input_file> -opt > <output_file>

-----------------------------------------------------------------------------------------
FILES
-----------------------------------------------------------------------------------------

This submission consists of the following files and directories:

CS4212_Project_Assignment_3_Report.pdf
Project report.

tests/
This directory contains the test and test output files. The test cases for Assignment 3 are 
found in tests/my_testcases/ directory.

src/
This directory contains the source files.

lexer.flex
Lexer specification file.

parser.cup
Parser specification file. Operator precedences are adapted from Java.

test.sh
This shell script is used by Makefile to generate the test asm files.

Makefile
Makefile for the project.

-----------------------------------------------------------------------------------------
SOURCE CODE
-----------------------------------------------------------------------------------------

The bulk of the compiler source code can be found in src/wwu/compiler/. This is split 
into the following packages:

wwu.compiler.ast
Contains code for AST and type-checking.

wwu.compiler.ir3
Contains code for for construction of IR3 data structure. 

wwu.compiler.arm
Contains code for generating ARM assembly.

wwu.compiler.cfg
Contains code for data flow analysis framework.

wwu.compiler.common
wwu.compiler.exception
wwu.compiler.util
Common files used by other packages.
