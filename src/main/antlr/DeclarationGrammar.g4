grammar DeclarationGrammar;

declarationGrammar 
	: '\\declare' '{' keyValuePairs '}'
	;
	
keyValuePairs
	:	(pair ',' )* pair 
	;

syntaxBracket
	: '{' TYPE ',' .*? ',' .*?  (',' ('l'|'r'))? '}'
	;
	
pair
	: 'syntax' '=' syntaxBracket
	| KEY '=' VALUE
	| .*? '=' .*?
	;
KEY
	:	'argspec'
	|	'meaning'
	|	'macro'
	;

TYPE
	: 'infix'
	| 'prefix'
	| 'postfix'
	;
	
VALUE
	: NUMBERS
	| NAME
	;

NUMBERS
	: [1-9]|([1-9][0-9]*)
	;

CHARACTERS
	: '"' .*? '"'
	;

NAME
	: [._a-zA-Z0-9]+
	;

WS : [ \t\n\r] -> skip; 
OTHER : . -> skip ;
