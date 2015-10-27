grammar DeclarationGrammar;

declarationGrammar 
	: '{' keyValuePairs '}'
	;
	
keyValuePairs
	:	(pair ',' )* pair 
	;

syntaxBracket
	: '{' TYPE ',' .*? ',' .*?  ',' ('l'|'r') '}'
	;
	
pair
	: importantPair
	| miscPair
	;

importantPair
	: 'syntax' '=' syntaxBracket
	| 'macro' '=' '\\'NAME
	| 'meaning' '=' NAME'.'NAME
	| 'argspec' '=' '['NUMBERS']''['.*?']'
	;
	
miscPair
	: .*? '=' .*?
	;
TYPE
	: 'infix'
	| 'prefix'
	| 'postfix'
	;
	

NUMBERS
	: [1-9]|([1-9][0-9]*)
	;

CHARACTERS
	: '"' .*? '"'
	;

NAME
	: [a-zA-Z0-9]+
	;

WS : [ \t\n\r] -> skip; 
