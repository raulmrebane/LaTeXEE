grammar DocumentGrammar;

document
	:	 ( .*? (proof | theorem | declaration | lemma | fileInclusion | formula))* .*?
	;

	
proof
	:	'\\begin{proof}' (.*? (formula | declaration | lemma ))* .*? '\\end{proof}'
	;
	
theorem
	:	'\\begin{theorem}' (.*? (formula | declaration))* .*? '\\end{theorem}'
	;
	
lemma
	:	'\\begin{lemma}' (.*? (formula | declaration))* .*? '\\end{lemma}'
	;

formula
	: DOLLARFORMULA
	| MACROFORMULA
	;

fileInclusion
	:	FILEINCLUSION
	;
	
declaration
	: DECLARATION
	;
DOLLARFORMULA
	:	'$' SUBFORMULA '$'
	|	SUBFORMULA
	;
	
MACROFORMULA
	: BEGINFRAGMENT .*? ENDFRAGMENT
	;
	
DECLARATION
	:	'\\declare{' .*? '}'
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

WS : [ \t\n\r]+ -> skip;
OTHER : .->skip;