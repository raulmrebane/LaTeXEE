grammar DeclarationGrammar;

declarationGrammar 
	: '{' keyValuePairs '}'
	;
	
keyValuePairs
	:	(WS* pair WS* ',' WS*)* WS* pair WS*
	;

syntaxBracket
	: '{' WS* TYPE WS* ',' WS* NUMBERS WS* ',' WS* CHARACTERS  WS* (',' WS* ('l'|'r') WS*)? '}'
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
	
NONWS
	: ~[ \t\n\r]
	;
WS : [ \t\n\r]; 

