// Generated from Grammar.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GrammarParser#document}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocument(GrammarParser.DocumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormula(GrammarParser.FormulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#proof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProof(GrammarParser.ProofContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#theorem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTheorem(GrammarParser.TheoremContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(GrammarParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GrammarParser#lemma}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLemma(GrammarParser.LemmaContext ctx);
}