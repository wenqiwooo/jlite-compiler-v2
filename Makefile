#  
# Author: Wu Wenqi    A0124278A
#
JAVA = java
JAVAC = javac
JFLEX = jflex
# Replace this with path to java-cup-11a.jar
CUP_JAR = /Users/Wenqi/jflex-1.6.1/lib/java-cup-11a.jar
CUP = $(JAVA) -jar $(CUP_JAR)
CLASS_PATH = .:$(CUP_JAR):classes
TEST_SCRIPT = ./test.sh
PROJ_PATH = src/wwu/compiler

test: parser.class
	$(TEST_SCRIPT) $(CLASS_PATH)

parser.class: Lexer.java parser.java
	find $(PROJ_PATH) -type f -name '*.java' > sources
	$(JAVAC) -cp $(CLASS_PATH) -d classes/ @sources

Lexer.java: lexer.flex
	$(JFLEX) -d $(PROJ_PATH) lexer.flex

parser.java: parser.cup
	$(CUP) -destdir $(PROJ_PATH) parser.cup

.PHONY: clean

clean:
	find . -type f \( \
	-name '*.class' -or \
	-name 'parser.java' -or \
	-name 'sym.java' -or \
	-name 'Lexer.java' -or \
	-name 'sources' -or \
	-name '*~' \) \
	-delete
	rm -rf classes
	# rm -f parser.java Lexer.java sym.java output.txt *.class *~
