grammar Grammar;

document
	:	(TEXT? (formula | proof | theorem | declaration | lemma) TEXT?)*
	;
	
formula
	:	'$' TEXT '$'
	|	'$$' TEXT '$$'
	|	'\\begin{equation}' TEXT '\\end{equation}'
	;
	
TEXT
	:	[A-Za-z0-9+-/:*^%|=-()[\]{}!?~\\<>]+ /*siia vaja hunnik juurde panna. Ka deklareeritud operaatorid jms. Päris wildcard'i ei saa. */
	;
	
proof
	:	'\\begin{proof}' (TEXT? (formula | declaration | lemma) TEXT?)+ '\\end{proof}'
	;
	
theorem
	:	'\\begin{theorem}' (TEXT? (formula | declaration) TEXT?)+ '\\end{theorem}'
	;
	
declaration
	:	'\\declare{' TEXT '}'
	;
	
lemma
	:	'\\begin{lemma}' (TEXT? (formula | declaration) TEXT?)+ '\\end{lemma}'
	;
	
WS : [ \t\n\r]+ -> skip;