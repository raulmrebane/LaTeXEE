// Generated from Grammar.g4 by ANTLR 4.5
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GrammarLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, TEXT=13, WS=14;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "TEXT", "WS"
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


	public GrammarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Grammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\20\u00ae\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\3\3\3\3\3\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\16\6\16\u00a4\n\16\r\16\16\16\u00a5\3\17\6\17\u00a9\n\17"+
		"\r\17\16\17\u00aa\3\17\3\17\2\2\20\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n"+
		"\23\13\25\f\27\r\31\16\33\17\35\20\3\2\4\t\2##\'\'+<>>@AC`c\u0080\5\2"+
		"\13\f\17\17\"\"\u00af\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2"+
		"\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3\37\3\2"+
		"\2\2\5!\3\2\2\2\7$\3\2\2\2\t\65\3\2\2\2\13D\3\2\2\2\rR\3\2\2\2\17^\3\2"+
		"\2\2\21n\3\2\2\2\23|\3\2\2\2\25\u0086\3\2\2\2\27\u0088\3\2\2\2\31\u0096"+
		"\3\2\2\2\33\u00a3\3\2\2\2\35\u00a8\3\2\2\2\37 \7&\2\2 \4\3\2\2\2!\"\7"+
		"&\2\2\"#\7&\2\2#\6\3\2\2\2$%\7^\2\2%&\7d\2\2&\'\7g\2\2\'(\7i\2\2()\7k"+
		"\2\2)*\7p\2\2*+\7}\2\2+,\7g\2\2,-\7s\2\2-.\7w\2\2./\7c\2\2/\60\7v\2\2"+
		"\60\61\7k\2\2\61\62\7q\2\2\62\63\7p\2\2\63\64\7\177\2\2\64\b\3\2\2\2\65"+
		"\66\7^\2\2\66\67\7g\2\2\678\7p\2\289\7f\2\29:\7}\2\2:;\7g\2\2;<\7s\2\2"+
		"<=\7w\2\2=>\7c\2\2>?\7v\2\2?@\7k\2\2@A\7q\2\2AB\7p\2\2BC\7\177\2\2C\n"+
		"\3\2\2\2DE\7^\2\2EF\7d\2\2FG\7g\2\2GH\7i\2\2HI\7k\2\2IJ\7p\2\2JK\7}\2"+
		"\2KL\7r\2\2LM\7t\2\2MN\7q\2\2NO\7q\2\2OP\7h\2\2PQ\7\177\2\2Q\f\3\2\2\2"+
		"RS\7^\2\2ST\7g\2\2TU\7p\2\2UV\7f\2\2VW\7}\2\2WX\7r\2\2XY\7t\2\2YZ\7q\2"+
		"\2Z[\7q\2\2[\\\7h\2\2\\]\7\177\2\2]\16\3\2\2\2^_\7^\2\2_`\7d\2\2`a\7g"+
		"\2\2ab\7i\2\2bc\7k\2\2cd\7p\2\2de\7}\2\2ef\7v\2\2fg\7j\2\2gh\7g\2\2hi"+
		"\7q\2\2ij\7t\2\2jk\7g\2\2kl\7o\2\2lm\7\177\2\2m\20\3\2\2\2no\7^\2\2op"+
		"\7g\2\2pq\7p\2\2qr\7f\2\2rs\7}\2\2st\7v\2\2tu\7j\2\2uv\7g\2\2vw\7q\2\2"+
		"wx\7t\2\2xy\7g\2\2yz\7o\2\2z{\7\177\2\2{\22\3\2\2\2|}\7^\2\2}~\7f\2\2"+
		"~\177\7g\2\2\177\u0080\7e\2\2\u0080\u0081\7n\2\2\u0081\u0082\7c\2\2\u0082"+
		"\u0083\7t\2\2\u0083\u0084\7g\2\2\u0084\u0085\7}\2\2\u0085\24\3\2\2\2\u0086"+
		"\u0087\7\177\2\2\u0087\26\3\2\2\2\u0088\u0089\7^\2\2\u0089\u008a\7d\2"+
		"\2\u008a\u008b\7g\2\2\u008b\u008c\7i\2\2\u008c\u008d\7k\2\2\u008d\u008e"+
		"\7p\2\2\u008e\u008f\7}\2\2\u008f\u0090\7n\2\2\u0090\u0091\7g\2\2\u0091"+
		"\u0092\7o\2\2\u0092\u0093\7o\2\2\u0093\u0094\7c\2\2\u0094\u0095\7\177"+
		"\2\2\u0095\30\3\2\2\2\u0096\u0097\7^\2\2\u0097\u0098\7g\2\2\u0098\u0099"+
		"\7p\2\2\u0099\u009a\7f\2\2\u009a\u009b\7}\2\2\u009b\u009c\7n\2\2\u009c"+
		"\u009d\7g\2\2\u009d\u009e\7o\2\2\u009e\u009f\7o\2\2\u009f\u00a0\7c\2\2"+
		"\u00a0\u00a1\7\177\2\2\u00a1\32\3\2\2\2\u00a2\u00a4\t\2\2\2\u00a3\u00a2"+
		"\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6"+
		"\34\3\2\2\2\u00a7\u00a9\t\3\2\2\u00a8\u00a7\3\2\2\2\u00a9\u00aa\3\2\2"+
		"\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ad"+
		"\b\17\2\2\u00ad\36\3\2\2\2\5\2\u00a5\u00aa\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}