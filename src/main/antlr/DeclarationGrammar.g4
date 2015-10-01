grammar DeclarationGrammar;

declarationGrammar 
	: DECLARE (macroSyntax | operatorSyntax) '}'
	;

// Macro-style syntax fragments

macroSyntax
	: macroComponent ',' WS+ 
	  meaningComponent ',' WS+
	  argSpecComponent ',' WS+
	  codeComponent WS*
	;
	
macroComponent
	: 'macro=' NAME
	;

meaningComponent
	: 'meaning=' NAME
	;

argSpecComponent
	: 'argspec=' '[' NUMBERS ']'
	;
	
codeComponent
	: 'code=' CODE
	;
	
//Infix/prefix/postfix operators

operatorSyntax
	: 'syntax={' syntaxBracket '}' ',' WS+ meaningComponent
	;

syntaxBracket
	: TYPE WS+ NUMBERS WS+ CHARACTERS WS+ ('l'|'r') WS*
	;




//Lexer rules

TYPE
	: 'infix'
	| 'prefix'
	| 'postfix'
	;
	

NUMBERS
	: [1-9]|([1-9][0-9]*)
	;
WS
	: [ \t\n\r]
	;

DECLARE
	: '\\declare{'
	;
	
NAME
	: [._a-zA-Z0-9]+
	;
CODE
	: '{' (~[}])+ '}'
	;
	
CHARACTERS
	: '"'(~['"'])+'"'
	;