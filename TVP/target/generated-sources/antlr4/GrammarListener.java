// Generated from Grammar.g4 by ANTLR 4.5
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GrammarParser}.
 */
public interface GrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GrammarParser#document}.
	 * @param ctx the parse tree
	 */
	void enterDocument(GrammarParser.DocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#document}.
	 * @param ctx the parse tree
	 */
	void exitDocument(GrammarParser.DocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterFormula(GrammarParser.FormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitFormula(GrammarParser.FormulaContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#proof}.
	 * @param ctx the parse tree
	 */
	void enterProof(GrammarParser.ProofContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#proof}.
	 * @param ctx the parse tree
	 */
	void exitProof(GrammarParser.ProofContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#theorem}.
	 * @param ctx the parse tree
	 */
	void enterTheorem(GrammarParser.TheoremContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#theorem}.
	 * @param ctx the parse tree
	 */
	void exitTheorem(GrammarParser.TheoremContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(GrammarParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(GrammarParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GrammarParser#lemma}.
	 * @param ctx the parse tree
	 */
	void enterLemma(GrammarParser.LemmaContext ctx);
	/**
	 * Exit a parse tree produced by {@link GrammarParser#lemma}.
	 * @param ctx the parse tree
	 */
	void exitLemma(GrammarParser.LemmaContext ctx);
}