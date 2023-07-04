// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Get the first number
@R0
D=M

// Store it in variable n
@n
M=D

// Get the second number
@R1
D=M

// Store it in variable m
@m
M=D

// Store the result
@mult
M=0 // Initialize to 0

// Add n to mult m times
@i
M=1 // Initialize counter

(LOOP)
@i
D=M
@m
D=D-M
@STOP
D;JGT // if i > m goto STOP

// mult = mult + n
@mult
D=M
@n
D=D+M
@mult
M=D

@i // i++
M=M+1

@LOOP // goto LOOP
0;JMP

// End Loop
(STOP)
@mult
D=M
@R2
M=D

// Null instruction / infinite loop
(END)
@END
0;JMP