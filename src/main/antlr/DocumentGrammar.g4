grammar DocumentGrammar;

document
	:	 (proof | theorem | declaration | lemma | fileInclusion | formula | .)*?
	;

	
proof
	:	'\\begin{proof}' (formula | declaration | lemma | . )*?  '\\end{proof}'
	;
	
theorem
	:	'\\begin{theorem}' (formula | declaration | lemma | .)*? '\\end{theorem}'
	;
	
lemma
	:	'\\begin{lemma}' (formula | declaration | .)*? '\\end{lemma}'
	;

BLOCK_COMMENT
	:	'\\begin{comment}' .*? '\\end{comment}' -> skip
	;

LINE_COMMENT: '%' ~('\r'|'\n') -> skip;

formula
	: FormulaLiteral
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
BugFixLiteral
  : '\\$'
  | '\\{' //the other brace is handled in UnterminatedBraceLiteral
  ;
OTHER : .->skip;
