grammar DeclarationGrammar;

declarationGrammar 
	: keyValuePairs
	;
	
keyValuePairs
	:	(pair ',' )* pair 
	;
	
pair
	: importantPair
	| miscPair
	;

importantPair
	: 'syntax' '=' BraceLiteral 				#syntaxpair
	| 'macro' '=' '\\'NAME						#macropair
	| 'meaning' '=' NAME '.' NAME				#meaningpair
	| 'argspec' '=' '['NUMBERS']'('['.*?']')?	#argspecpair
	;
  
miscPair
    : .*? '=' BraceLiteral	
    | .*? '=' NAME			
    ;

BraceLiteral
    : UnterminatedBraceLiteral '}'
    ;

UnterminatedBraceLiteral
    : '{' (~[\\}] | '\\' (. | EOF)|BraceLiteral)* 
    ;  	

NUMBERS
	: [1-9]|([1-9][0-9]*)
	;
NEGATIVENUMBER
	: '-' NUMBERS
	;
CHARACTERS
	: '"' .*? '"'
	;

NAME
	: [a-zA-Z0-9_]+
	;

WS : [ \t\n\r] -> skip; 
