// Generated from Grammar.g4 by ANTLR 4.5
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, TEXT=13, WS=14;
	public static final int
		RULE_document = 0, RULE_formula = 1, RULE_proof = 2, RULE_theorem = 3, 
		RULE_declaration = 4, RULE_lemma = 5;
	public static final String[] ruleNames = {
		"document", "formula", "proof", "theorem", "declaration", "lemma"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'$'", "'$$'", "'\\begin{equation}'", "'\\end{equation}'", "'\\begin{proof}'", 
		"'\\end{proof}'", "'\\begin{theorem}'", "'\\end{theorem}'", "'\\declare{'", 
		"'}'", "'\\begin{lemma}'", "'\\end{lemma}'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "TEXT", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Grammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public GrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class DocumentContext extends ParserRuleContext {
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public List<ProofContext> proof() {
			return getRuleContexts(ProofContext.class);
		}
		public ProofContext proof(int i) {
			return getRuleContext(ProofContext.class,i);
		}
		public List<TheoremContext> theorem() {
			return getRuleContexts(TheoremContext.class);
		}
		public TheoremContext theorem(int i) {
			return getRuleContext(TheoremContext.class,i);
		}
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<LemmaContext> lemma() {
			return getRuleContexts(LemmaContext.class);
		}
		public LemmaContext lemma(int i) {
			return getRuleContext(LemmaContext.class,i);
		}
		public List<TerminalNode> TEXT() { return getTokens(GrammarParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(GrammarParser.TEXT, i);
		}
		public DocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_document; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterDocument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitDocument(this);
		}
	}

	public final DocumentContext document() throws RecognitionException {
		DocumentContext _localctx = new DocumentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_document);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(27);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__4) | (1L << T__6) | (1L << T__8) | (1L << T__10) | (1L << TEXT))) != 0)) {
				{
				{
				setState(13);
				_la = _input.LA(1);
				if (_la==TEXT) {
					{
					setState(12);
					match(TEXT);
					}
				}

				setState(20);
				switch (_input.LA(1)) {
				case T__0:
				case T__1:
				case T__2:
					{
					setState(15);
					formula();
					}
					break;
				case T__4:
					{
					setState(16);
					proof();
					}
					break;
				case T__6:
					{
					setState(17);
					theorem();
					}
					break;
				case T__8:
					{
					setState(18);
					declaration();
					}
					break;
				case T__10:
					{
					setState(19);
					lemma();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(23);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(22);
					match(TEXT);
					}
					break;
				}
				}
				}
				setState(29);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormulaContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(GrammarParser.TEXT, 0); }
		public FormulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formula; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterFormula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitFormula(this);
		}
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_formula);
		try {
			setState(39);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(30);
				match(T__0);
				setState(31);
				match(TEXT);
				setState(32);
				match(T__0);
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(33);
				match(T__1);
				setState(34);
				match(TEXT);
				setState(35);
				match(T__1);
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 3);
				{
				setState(36);
				match(T__2);
				setState(37);
				match(TEXT);
				setState(38);
				match(T__3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProofContext extends ParserRuleContext {
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<LemmaContext> lemma() {
			return getRuleContexts(LemmaContext.class);
		}
		public LemmaContext lemma(int i) {
			return getRuleContext(LemmaContext.class,i);
		}
		public List<TerminalNode> TEXT() { return getTokens(GrammarParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(GrammarParser.TEXT, i);
		}
		public ProofContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_proof; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterProof(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitProof(this);
		}
	}

	public final ProofContext proof() throws RecognitionException {
		ProofContext _localctx = new ProofContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_proof);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			match(T__4);
			setState(53); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(43);
				_la = _input.LA(1);
				if (_la==TEXT) {
					{
					setState(42);
					match(TEXT);
					}
				}

				setState(48);
				switch (_input.LA(1)) {
				case T__0:
				case T__1:
				case T__2:
					{
					setState(45);
					formula();
					}
					break;
				case T__8:
					{
					setState(46);
					declaration();
					}
					break;
				case T__10:
					{
					setState(47);
					lemma();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(51);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					setState(50);
					match(TEXT);
					}
					break;
				}
				}
				}
				setState(55); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__8) | (1L << T__10) | (1L << TEXT))) != 0) );
			setState(57);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TheoremContext extends ParserRuleContext {
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<TerminalNode> TEXT() { return getTokens(GrammarParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(GrammarParser.TEXT, i);
		}
		public TheoremContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_theorem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterTheorem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitTheorem(this);
		}
	}

	public final TheoremContext theorem() throws RecognitionException {
		TheoremContext _localctx = new TheoremContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_theorem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(T__6);
			setState(70); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(61);
				_la = _input.LA(1);
				if (_la==TEXT) {
					{
					setState(60);
					match(TEXT);
					}
				}

				setState(65);
				switch (_input.LA(1)) {
				case T__0:
				case T__1:
				case T__2:
					{
					setState(63);
					formula();
					}
					break;
				case T__8:
					{
					setState(64);
					declaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(68);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(67);
					match(TEXT);
					}
					break;
				}
				}
				}
				setState(72); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__8) | (1L << TEXT))) != 0) );
			setState(74);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(GrammarParser.TEXT, 0); }
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitDeclaration(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			match(T__8);
			setState(77);
			match(TEXT);
			setState(78);
			match(T__9);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LemmaContext extends ParserRuleContext {
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<TerminalNode> TEXT() { return getTokens(GrammarParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(GrammarParser.TEXT, i);
		}
		public LemmaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lemma; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).enterLemma(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GrammarListener ) ((GrammarListener)listener).exitLemma(this);
		}
	}

	public final LemmaContext lemma() throws RecognitionException {
		LemmaContext _localctx = new LemmaContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_lemma);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			match(T__10);
			setState(91); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(82);
				_la = _input.LA(1);
				if (_la==TEXT) {
					{
					setState(81);
					match(TEXT);
					}
				}

				setState(86);
				switch (_input.LA(1)) {
				case T__0:
				case T__1:
				case T__2:
					{
					setState(84);
					formula();
					}
					break;
				case T__8:
					{
					setState(85);
					declaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(89);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(88);
					match(TEXT);
					}
					break;
				}
				}
				}
				setState(93); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__8) | (1L << TEXT))) != 0) );
			setState(95);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\20d\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\5\2\20\n\2\3\2\3\2\3\2\3\2\3\2"+
		"\5\2\27\n\2\3\2\5\2\32\n\2\7\2\34\n\2\f\2\16\2\37\13\2\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\5\3*\n\3\3\4\3\4\5\4.\n\4\3\4\3\4\3\4\5\4\63\n\4"+
		"\3\4\5\4\66\n\4\6\48\n\4\r\4\16\49\3\4\3\4\3\5\3\5\5\5@\n\5\3\5\3\5\5"+
		"\5D\n\5\3\5\5\5G\n\5\6\5I\n\5\r\5\16\5J\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3"+
		"\7\5\7U\n\7\3\7\3\7\5\7Y\n\7\3\7\5\7\\\n\7\6\7^\n\7\r\7\16\7_\3\7\3\7"+
		"\3\7\2\2\b\2\4\6\b\n\f\2\2s\2\35\3\2\2\2\4)\3\2\2\2\6+\3\2\2\2\b=\3\2"+
		"\2\2\nN\3\2\2\2\fR\3\2\2\2\16\20\7\17\2\2\17\16\3\2\2\2\17\20\3\2\2\2"+
		"\20\26\3\2\2\2\21\27\5\4\3\2\22\27\5\6\4\2\23\27\5\b\5\2\24\27\5\n\6\2"+
		"\25\27\5\f\7\2\26\21\3\2\2\2\26\22\3\2\2\2\26\23\3\2\2\2\26\24\3\2\2\2"+
		"\26\25\3\2\2\2\27\31\3\2\2\2\30\32\7\17\2\2\31\30\3\2\2\2\31\32\3\2\2"+
		"\2\32\34\3\2\2\2\33\17\3\2\2\2\34\37\3\2\2\2\35\33\3\2\2\2\35\36\3\2\2"+
		"\2\36\3\3\2\2\2\37\35\3\2\2\2 !\7\3\2\2!\"\7\17\2\2\"*\7\3\2\2#$\7\4\2"+
		"\2$%\7\17\2\2%*\7\4\2\2&\'\7\5\2\2\'(\7\17\2\2(*\7\6\2\2) \3\2\2\2)#\3"+
		"\2\2\2)&\3\2\2\2*\5\3\2\2\2+\67\7\7\2\2,.\7\17\2\2-,\3\2\2\2-.\3\2\2\2"+
		".\62\3\2\2\2/\63\5\4\3\2\60\63\5\n\6\2\61\63\5\f\7\2\62/\3\2\2\2\62\60"+
		"\3\2\2\2\62\61\3\2\2\2\63\65\3\2\2\2\64\66\7\17\2\2\65\64\3\2\2\2\65\66"+
		"\3\2\2\2\668\3\2\2\2\67-\3\2\2\289\3\2\2\29\67\3\2\2\29:\3\2\2\2:;\3\2"+
		"\2\2;<\7\b\2\2<\7\3\2\2\2=H\7\t\2\2>@\7\17\2\2?>\3\2\2\2?@\3\2\2\2@C\3"+
		"\2\2\2AD\5\4\3\2BD\5\n\6\2CA\3\2\2\2CB\3\2\2\2DF\3\2\2\2EG\7\17\2\2FE"+
		"\3\2\2\2FG\3\2\2\2GI\3\2\2\2H?\3\2\2\2IJ\3\2\2\2JH\3\2\2\2JK\3\2\2\2K"+
		"L\3\2\2\2LM\7\n\2\2M\t\3\2\2\2NO\7\13\2\2OP\7\17\2\2PQ\7\f\2\2Q\13\3\2"+
		"\2\2R]\7\r\2\2SU\7\17\2\2TS\3\2\2\2TU\3\2\2\2UX\3\2\2\2VY\5\4\3\2WY\5"+
		"\n\6\2XV\3\2\2\2XW\3\2\2\2Y[\3\2\2\2Z\\\7\17\2\2[Z\3\2\2\2[\\\3\2\2\2"+
		"\\^\3\2\2\2]T\3\2\2\2^_\3\2\2\2_]\3\2\2\2_`\3\2\2\2`a\3\2\2\2ab\7\16\2"+
		"\2b\r\3\2\2\2\23\17\26\31\35)-\62\659?CFJTX[_";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}