@R15
D=M

@len //len = RAM[15]
M=D

//@R14
//D=M

//@start //start is the beginning address of the aray with size len
//M=D
//A=M
//D=M

@R14
D=M

@curadd
M=D



//@min // min= RAM[start]
//M=D

///////////////////////////////////////////////////////////////////////
(LOOP)
@len
D=M-1
M=M-1
@END
D;JEQ

@R15
D=M-1			//if len=1
@n

M=D
@R14
D=M

@curadd
M=D
///////////////////////////////////////////
(INSIDELOOP)

@curadd
A=M
D=M

@temp1 
M=D

@curadd
A=M+1
D=M

@temp2 
M=D

@curadd
A=M
D=M-D
@replace
D;JLT

@curadd
M=M+1


@n		  // if n !=0 the back to INSIDELOOP
M=M-1
D=M

@INSIDELOOP
D;JNE

//@len
//M=M-1
@LOOP
0;JMP

//////////////////////////
(replace)

@temp1
D=M

@curadd
A=M+1
M=D

@temp2
D=M

@curadd
A=M
M=D

@curadd
M=M+1


@n		  // if n !=0 the back to INSIDELOOP
M=M-1
D=M
@INSIDELOOP
D;JNE

//@len
//M=M-1
@LOOP
0;JMP			
/////////////////////////////

///////////////////////////////////////////

///////////////////////////////////////////////////////////////////////
(END)
//@END
//0;JMP
