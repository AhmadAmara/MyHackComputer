###############################################################################
#
# Makefile for Java project
#
# Student:
# ahmadam , ID 316023753, ahmad.amarh@mail.huji.ac.il
#
###############################################################################

JAVAC=javac
JAVACFLAGS=-g

SRCS= CompilationEngine.java  JackAnalyzer.java Symbol.java SymbolTable.java Tokenizer.java VMWriter.java 
EXEC=JackCompiler

TAR=tar
TARFLAGS=cvf
TARNAME=project11.tar
TARSRCS=$(SRCS) $(EXEC) README Makefile

all: compile

compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -f *.class *~

