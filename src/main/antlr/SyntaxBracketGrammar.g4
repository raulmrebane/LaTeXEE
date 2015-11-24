grammar SyntaxBracketGrammar;

syntaxBracket
	: '{' TYPE ',' .*? ',' .*?  ',' ('l'|'r') '}'
	;
	
a:'a';
TYPE
	: 'infix'
	| 'prefix'
	| 'postfix'
	;
	
NAME
	: [a-zA-Z0-9_]+
	;

CHARACTERS
	: '"' .*? '"'
	;