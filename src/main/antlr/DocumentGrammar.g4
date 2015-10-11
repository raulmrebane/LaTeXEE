grammar DocumentGrammar;

declarationGrammar 
	: '\\declare' '{' keyValuePairs '}'
	;
	
keyValuePairs
	:	(pair ',' )* lastpair 
	;

syntaxBracket
	: '{' TYPE ',' .*? ',' .*?  (',' ('l'|'r'))? '}'
	;
	
pair
	: 'syntax' '=' syntaxBracket
	| KEY '=' VALUE
	| .*? '=' .*?
	;
lastpair
	: 'syntax' '=' syntaxBracket
	| KEY '=' VALUE
	| .*? '=' ('{' .*? '}'| .*?)
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
document
	:	 ( .*? (proof | theorem | declarationGrammar | lemma | fileInclusion | formula))* .*?
	;


proof
	:	'\\begin{proof}' (.*? (formula | declarationGrammar | lemma ))* .*? '\\end{proof}'
	;
	
theorem
	:	'\\begin{theorem}' (.*? (formula | declarationGrammar))* .*? '\\end{theorem}'
	;
	
lemma
	:	'\\begin{lemma}' (.*? (formula | declarationGrammar))* .*? '\\end{lemma}'
	;

formula
	: DOLLARFORMULA
	| MACROFORMULA
	;

fileInclusion
	:	FILEINCLUSION
	;

DOLLARFORMULA
	:	'$' SUBFORMULA '$'
	|	SUBFORMULA
	;
	
MACROFORMULA
	: BEGINFRAGMENT .*? ENDFRAGMENT
	;
	

fragment BEGINFRAGMENT
	: '\\begin{equation}'
	;
fragment ENDFRAGMENT
	: '\\end{equation}'
	;
SUBFORMULA
	:	'$' .*? '$'
	;
FILEINCLUSION
	:	'\\InputIfFileExists{' .*? '}'
	;