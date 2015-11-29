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
	| 'meaning' '=' NAME '.' NAME
	| 'argspec' '=' '['NUMBERS']'('['.*?']')?
	;
	
miscPair
	: .*? '=' value
	;
	
value
	: NAME
	| CHARACTERS
	| valueInBraces
	;
	
valueInBraces
	: '{' (valueInBraces|.)*? '}'
	;
	
TYPE
	: 'infix'
	| 'prefix'
	| 'postfix'
	;
	
NOTANOPENBRACE
	: '\\{'
	;
	
NOTACLOSINGBRACE
	: '\\}'
	;
	
NOTAQUOTATIONMARK
	: '\\"'
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
	: [a-zA-Z0-9]+
	;
	

WS : [ \t\n\r] -> skip; 
OTHER
	: .
	;
