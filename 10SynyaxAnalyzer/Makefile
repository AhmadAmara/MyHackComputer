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

SRCS= CompilationEngine.java  JackTokenizer.java
EXEC=JackAnalyzer

TAR=tar
TARFLAGS=cvf
TARNAME=project10.tar
TARSRCS=$(SRCS) $(EXEC) README Makefile

all: compile

compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -f *.class *~

