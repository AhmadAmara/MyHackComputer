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

SRCS= VMtranslator.java  CodeWriter.java Parser.java
EXEC=VMtranslator

TAR=tar
TARFLAGS=cvf
TARNAME=project7.tar
TARSRCS=$(SRCS) $(EXEC) README Makefile

all: compile

compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -f *.class *~

