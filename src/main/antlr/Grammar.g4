grammar Grammar;

document
	:	 ( TEXT? (proof | theorem | declaration | lemma | fileInclusion | formula))* TEXT?
	;

	
proof
	:	'\\begin{proof}' (TEXT? (formula | declaration | lemma ))* TEXT? '\\end{proof}'
	;
	
theorem
	:	'\\begin{theorem}' (TEXT? (formula | declaration))* TEXT? '\\end{theorem}'
	;
	
declaration
	:	'\\declare{' TEXT '}'
	;
	
lemma
	:	'\\begin{lemma}' (TEXT? (formula | declaration))* TEXT? '\\end{lemma}'
	;
	
formula
	:	'$' TEXT '$'
	|	'$$' TEXT '$$'
	|	'\\begin{equation}' TEXT '\\end{equation}'
	;
	
fileInclusion
	:	'\\InputIfFileExists{' TEXT '}'
	;

TEXT
	:	(~('$' | '\\' | '}'))+ /*Really needs to be changed! */
	;
	
WS : [ \t\n\r]+ -> skip;