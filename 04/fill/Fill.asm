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

(INFINITE_LOOP)
	// Get the keyboard input
	@KBD
	D=M

	// No key pressed, skip color attribution
	@SKIP_BLACK
	D;JEQ

	// else make D black
	D=-1
	
	(SKIP_BLACK)
	@color
	M=D

	// Initialize loop variables
	@SCREEN
	D=A
	@i
	M=D
	@8192
	D=D+A
	@n
	M=D

	(INNER_LOOP)
		// Color the i-th register
		@color
		D=M
		@i
		A=M
		M=D

		// i++
		@i
		M=M+1

		// While loop condition
		@i
		D=M
		@n
		D=D-M
		@INNER_LOOP
		D;JNE

@INFINITE_LOOP
0;JMP