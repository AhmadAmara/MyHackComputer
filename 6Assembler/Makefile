###############################################################################
#
# Makefile for Java project
#
# Students:
# ahmadam , ID 316023753, ahmad.amarh@mail.huji.ac.il
#
###############################################################################

JAVAC=javac
JAVACFLAGS=-g

SRCS=Assembler.java tablesTools.java SymbolTable.java  Code.java Parser.java
EXEC=Assembler

TAR=tar
TARFLAGS=cvf
TARNAME=project6.tar
TARSRCS=$(SRCS) $(EXEC) README Makefile

all: compile

compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -f *.class *~

