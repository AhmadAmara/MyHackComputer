@curQB
M=1

@quotient
M=0


@R14
D=M

@curDivisor
M=D

@divsor 
M=D

@R13
D=M

@divident
M=D



@divident
D=M

@divsor
D=D-M
@END
D;JLT


(LOOP)
@divident
D=M

@curDivisor
D=D-M
@ELSE
D;JLT

/////////////////////////////////
@curDivisor
D=M

@divident
M=M-D


@curQB
D=M

@quotient
M=M+D

@curDivisor
M=M<<

@curQB
M=M<<

@divident
D=M

@divsor
D=D-M
@END
D;JLT

@LOOP
0;JMP
/////////////////////////////////

(ELSE)
@curDivisor
M=M>>

@curQB
M=M>>


@divident
D=M

@divsor
D=D-M
@END
D;JLT

@LOOP
0;JMP

(END)
@quotient
D=M

@R15
M=D

