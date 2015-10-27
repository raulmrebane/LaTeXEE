grammar GeneratorTarget;

highestLevel
	: highestNumber
	;
	
highestNumber
	:
	;
	
lowestLevel
	: '{' highestLevel '}' | LEXERRULE
	;

LEXERRULE : [0-9]+;