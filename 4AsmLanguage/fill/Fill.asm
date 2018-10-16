// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

@SCREEN
D=A
@scr
M=D
@24575
D=A
@last
M=D

	(WHITELOOP)
		@KBD
		D=M
		@ENDW
		D;JNE
		
		
		
		@scr
		A=M
		M=0
		
		@scr
		D=M
		@last
		D=M-D
		@ENDW
		D;JEQ
		
		@scr
		M=M+1
		
		@WHITELOOP
		0;JMP
		
	(ENDW)
		@SCREEN
		D=A
		@scr
		M=D
		@BLACKLOOP
		
	(BLACKLOOP)
		@KBD
		D=M
		@ENDB
		D;JEQ
		
		@scr
		A=M
		M=-1

		@scr
		D=M
		@last
		D=M-D
		@ENDB
		D;JEQ

		@scr
		M=M+1
		
		@BLACKLOOP
		0;JMP
		
	(ENDB)
		@SCREEN
		D=A
		@scr
		M=D
		@WHITELOOP
		0;JMP
		
		
		
