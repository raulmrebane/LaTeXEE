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
	: FormulaLiteral
	| DoubleFormulaLiteral
	| MACROFORMULA
	;

fileInclusion
	:	FILEINCLUSION
	;
	
declaration
	: '\\declare' BraceLiteral
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

FILEINCLUSION
	:	'\\InputIfFileExists{' .*? '}'
	;

BraceLiteral
  : UnterminatedBraceLiteral '}'
  ;

UnterminatedBraceLiteral
  : '{' (~[\\}] | '\\' (. | EOF)|BraceLiteral)* 
  ;

FormulaLiteral
  : UnterminatedFormulaLiteral '$'
  ;

UnterminatedFormulaLiteral
  : '$' ('$' (~[\\$] | '\\' (. | EOF))* '$'|(~[\\$] | '\\' (. | EOF))*)
  ;  

OTHER : .->skip;