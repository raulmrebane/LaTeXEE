grammar GeneratorTarget;

highestLevel
	: macroStatement | highestNumber
	;

macroStatement
	:
	;
	
highestNumber
	:
	;
	
lowestLevel
	: '{' highestLevel '}'
	;

