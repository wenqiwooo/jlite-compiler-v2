CS4212 Project Assignment 2
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

-----------------------------------------------------------------------------------------
FILES
-----------------------------------------------------------------------------------------

This submission consists of the following files and directories:

tests/
This directory contains the test and test output files. The test cases for Assignment 2 are 
found in tests/semantic/ directory.

src/
This directory contains the source files.

lexer.flex
Lexer specification file.

parser.cup
Parser specification file. Operator precedences are adapted from Java.

test.sh
This shell script is used by Makefile to test the parser with the test files.

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

wwu.compiler.common, wwu.compiler.exception, wwu.compiler.util
Common files used by other packages.

-----------------------------------------------------------------------------------------
METHOD OVERLOADING
-----------------------------------------------------------------------------------------

No two methods within a class declaration can have the same name and method signature. 

Overloading of methods is supported. Overloaded methods must have the same name, same 
return type and unique tuple of parameter types (different from all other methods which 
it overloads).

Every method is assigned a unique key when it is added to the ClassBuilder, using its 
simple name and its parameter types. For example the method Void run(Bool x) is assigned 
the key func_run_Bool.

Whenever a new method is added, its key is generated and checked to ensure there is no 
redeclaration. As long as methods with same name and return type have different argument 
lists (which is required for overloading), every valid method has a unique key within 
the class it is declared in, i.e. one-to-one mapping.

If a method overloads existing methods, we need to ensure that the method's return type 
is the same as that of the methods which it overloads.

Whenever a method is called and no null arguments is passed, we can use the method's 
simple name and its argument types to generate the same key to retrieve the 
corresponding method from the method table.

However, if there is a null object in the argument list, it may be ambiguious which 
method to call if the argument list is valid for multiple methods, since null is a subtype 
of String and all user-defined types. 

For example given the following two overloaded methods:

Void doWork(Dog x);

Void doWork(Person x);

doWork(null) matches both methods' signatures. In Java, the most specific applicable 
version of a method that's available is selected. In JLite, the compiler will select
the method that is declared first in the source file.

See src/wwu/compiler/ir3/IR3ClassBuilder.java for more details on the implementation.

-----------------------------------------------------------------------------------------
VARIABLE SCOPING
-----------------------------------------------------------------------------------------

Local variable shadowing is allowed. The following code is valid:

class A {
    Int x;
    Void run(Int x) {
        String x;
        // Do stuff
    }
}

-----------------------------------------------------------------------------------------
NULL
-----------------------------------------------------------------------------------------

Null is a special type that is used to indicate the absence of a value. 

You cannot declare a variable to be of Null type as it is not treated like a normal 
built-in type. 

You may however assign a null literal to any variable that is of String or any 
user-defined type. Null is implemented to behave as a subtype of String and any 
user-defined types.
