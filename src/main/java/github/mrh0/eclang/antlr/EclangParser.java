// Generated from W:/Development/GitHub/eclang/src/main/antlr4/Eclang.g4 by ANTLR 4.13.1
package github.mrh0.eclang.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class EclangParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52, 
		T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, T__58=59, 
		T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, T__65=66, 
		T__66=67, T__67=68, T__68=69, T__69=70, T__70=71, T__71=72, T__72=73, 
		NL=74, BOOL=75, NAME=76, ATOM=77, INT=78, UINT=79, FLOAT=80, DOUBLE=81, 
		HEX=82, BIN=83, CHAR=84, STRING=85, EMBEDED=86, WHITESPACE=87, EMPTYLINE=88, 
		COMMENT=89, BLOCKCOMMENT=90, INDENT=91, DEDENT=92;
	public static final int
		RULE_number = 0, RULE_primitive = 1, RULE_block = 2, RULE_unOp = 3, RULE_binOp = 4, 
		RULE_expr = 5, RULE_type = 6, RULE_interface = 7, RULE_parameter = 8, 
		RULE_statement = 9, RULE_func = 10, RULE_global = 11, RULE_use = 12, RULE_program = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"number", "primitive", "block", "unOp", "binOp", "expr", "type", "interface", 
			"parameter", "statement", "func", "global", "use", "program"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'not'", "'!'", "'!!'", "'~'", "'-'", "'@'", "'+'", "'*'", "'/'", 
			"'%'", "'**'", "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&&'", 
			"'and'", "'||'", "'or'", "'^^'", "'xor'", "'==='", "'!=='", "'??'", "'<<'", 
			"'>>'", "'&'", "'|'", "'^'", "'here'", "'sizeof'", "'('", "')'", "'alignof'", 
			"'offsetof'", "'addrof'", "'if'", "'else'", "'is'", "'!is'", "'as'", 
			"'unsafe'", "'.'", "'?.'", "'['", "']'", "','", "'{'", "'='", "'}'", 
			"'?'", "'inter'", "':'", "'var'", "'val'", "'break'", "'continue'", "'pass'", 
			"'yeild'", "'defer'", "'do'", "'while'", "'for'", "'in'", "'where'", 
			"'ret'", "'declare'", "'fn'", "'type'", "'rec'", "'use'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "NL", "BOOL", "NAME", "ATOM", "INT", "UINT", "FLOAT", "DOUBLE", 
			"HEX", "BIN", "CHAR", "STRING", "EMBEDED", "WHITESPACE", "EMPTYLINE", 
			"COMMENT", "BLOCKCOMMENT", "INDENT", "DEDENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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
	public String getGrammarFileName() { return "Eclang.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public EclangParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NumberContext extends ParserRuleContext {
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
	 
		public NumberContext() { }
		public void copyFrom(NumberContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumberIntContext extends NumberContext {
		public TerminalNode INT() { return getToken(EclangParser.INT, 0); }
		public NumberIntContext(NumberContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterNumberInt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitNumberInt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitNumberInt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumberDoubleContext extends NumberContext {
		public TerminalNode DOUBLE() { return getToken(EclangParser.DOUBLE, 0); }
		public NumberDoubleContext(NumberContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterNumberDouble(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitNumberDouble(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitNumberDouble(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumberFloatContext extends NumberContext {
		public TerminalNode FLOAT() { return getToken(EclangParser.FLOAT, 0); }
		public NumberFloatContext(NumberContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterNumberFloat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitNumberFloat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitNumberFloat(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumberHexContext extends NumberContext {
		public TerminalNode HEX() { return getToken(EclangParser.HEX, 0); }
		public NumberHexContext(NumberContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterNumberHex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitNumberHex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitNumberHex(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumberBinContext extends NumberContext {
		public TerminalNode BIN() { return getToken(EclangParser.BIN, 0); }
		public NumberBinContext(NumberContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterNumberBin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitNumberBin(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitNumberBin(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_number);
		try {
			setState(33);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				_localctx = new NumberIntContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(28);
				match(INT);
				}
				break;
			case FLOAT:
				_localctx = new NumberFloatContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(29);
				match(FLOAT);
				}
				break;
			case DOUBLE:
				_localctx = new NumberDoubleContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(30);
				match(DOUBLE);
				}
				break;
			case HEX:
				_localctx = new NumberHexContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(31);
				match(HEX);
				}
				break;
			case BIN:
				_localctx = new NumberBinContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(32);
				match(BIN);
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

	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveContext extends ParserRuleContext {
		public PrimitiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitive; }
	 
		public PrimitiveContext() { }
		public void copyFrom(PrimitiveContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveBoolContext extends PrimitiveContext {
		public TerminalNode BOOL() { return getToken(EclangParser.BOOL, 0); }
		public PrimitiveBoolContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterPrimitiveBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitPrimitiveBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitPrimitiveBool(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveNumberContext extends PrimitiveContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public PrimitiveNumberContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterPrimitiveNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitPrimitiveNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitPrimitiveNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveCharContext extends PrimitiveContext {
		public TerminalNode CHAR() { return getToken(EclangParser.CHAR, 0); }
		public PrimitiveCharContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterPrimitiveChar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitPrimitiveChar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitPrimitiveChar(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveStringContext extends PrimitiveContext {
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public PrimitiveStringContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterPrimitiveString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitPrimitiveString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitPrimitiveString(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveAtomContext extends PrimitiveContext {
		public TerminalNode ATOM() { return getToken(EclangParser.ATOM, 0); }
		public PrimitiveAtomContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterPrimitiveAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitPrimitiveAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitPrimitiveAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveContext primitive() throws RecognitionException {
		PrimitiveContext _localctx = new PrimitiveContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_primitive);
		try {
			setState(40);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
			case FLOAT:
			case DOUBLE:
			case HEX:
			case BIN:
				_localctx = new PrimitiveNumberContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(35);
				number();
				}
				break;
			case BOOL:
				_localctx = new PrimitiveBoolContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(36);
				match(BOOL);
				}
				break;
			case STRING:
				_localctx = new PrimitiveStringContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(37);
				match(STRING);
				}
				break;
			case CHAR:
				_localctx = new PrimitiveCharContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(38);
				match(CHAR);
				}
				break;
			case ATOM:
				_localctx = new PrimitiveAtomContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(39);
				match(ATOM);
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

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public StatementContext statement;
		public List<StatementContext> statements = new ArrayList<StatementContext>();
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			match(INDENT);
			setState(44); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(43);
				((BlockContext)_localctx).statement = statement();
				((BlockContext)_localctx).statements.add(((BlockContext)_localctx).statement);
				}
				}
				setState(46); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 9152441403580612734L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 4159507L) != 0) );
			setState(48);
			match(DEDENT);
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

	@SuppressWarnings("CheckReturnValue")
	public static class UnOpContext extends ParserRuleContext {
		public UnOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterUnOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitUnOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitUnOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnOpContext unOp() throws RecognitionException {
		UnOpContext _localctx = new UnOpContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_unOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 126L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	@SuppressWarnings("CheckReturnValue")
	public static class BinOpContext extends ParserRuleContext {
		public BinOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterBinOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitBinOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitBinOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinOpContext binOp() throws RecognitionException {
		BinOpContext _localctx = new BinOpContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_binOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4294967200L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprAsUnsafeContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExprAsUnsafeContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprAsUnsafe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprAsUnsafe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprAsUnsafe(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprNestContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public ExprNestContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprNest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprNest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprNest(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprCastNotNullContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprCastNotNullContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprCastNotNull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprCastNotNull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprCastNotNull(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprAccessNameNullishCoalescingContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprAccessNameNullishCoalescingContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprAccessNameNullishCoalescing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprAccessNameNullishCoalescing(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprAccessNameNullishCoalescing(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprInlineIfContext extends ExprContext {
		public ExprContext condition;
		public ExprContext body;
		public ExprContext elseBody;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ExprInlineIfContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprInlineIf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprInlineIf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprInlineIf(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprFunctionCallWithArgsContext extends ExprContext {
		public List<ExprContext> args = new ArrayList<ExprContext>();
		public ExprContext expr;
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public List<TerminalNode> NL() { return getTokens(EclangParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(EclangParser.NL, i);
		}
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public ExprFunctionCallWithArgsContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprFunctionCallWithArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprFunctionCallWithArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprFunctionCallWithArgs(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprHereContext extends ExprContext {
		public ExprHereContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprHere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprHere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprHere(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprNamedContext extends ExprContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprNamedContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprNamed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprNamed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprNamed(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprCreateRecordContext extends ExprContext {
		public Token recordType;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public List<TerminalNode> NL() { return getTokens(EclangParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(EclangParser.NL, i);
		}
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public ExprCreateRecordContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprCreateRecord(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprCreateRecord(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprCreateRecord(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprAsContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExprAsContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprAs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprAs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprAs(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprOffsetOfContext extends ExprContext {
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public ExprOffsetOfContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprOffsetOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprOffsetOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprOffsetOf(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprAlignOfContext extends ExprContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public ExprAlignOfContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprAlignOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprAlignOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprAlignOf(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprBinOpContext extends ExprContext {
		public ExprContext left;
		public ExprContext right;
		public BinOpContext binOp() {
			return getRuleContext(BinOpContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ExprBinOpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprBinOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprBinOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprBinOp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprAccessorContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public ExprAccessorContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprAccessor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprAccessor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprAccessor(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprIsContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprIsContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprIs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprIs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprIs(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprSizeOfContext extends ExprContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public ExprSizeOfContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprSizeOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprSizeOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprSizeOf(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprAddressOfContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public ExprAddressOfContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprAddressOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprAddressOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprAddressOf(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprPrimitiveContext extends ExprContext {
		public PrimitiveContext primitive() {
			return getRuleContext(PrimitiveContext.class,0);
		}
		public ExprPrimitiveContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprPrimitive(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprPrimitive(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprPrimitive(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprFunctionCallNoArgsContext extends ExprContext {
		public List<ExprContext> args = new ArrayList<ExprContext>();
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprFunctionCallNoArgsContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprFunctionCallNoArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprFunctionCallNoArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprFunctionCallNoArgs(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprAccessNameContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprAccessNameContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprAccessName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprAccessName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprAccessName(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprIsNotContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprIsNotContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprIsNot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprIsNot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprIsNot(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprUnOpContext extends ExprContext {
		public UnOpContext unOp() {
			return getRuleContext(UnOpContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprUnOpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprUnOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprUnOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprUnOp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExprCreateRecordNamedContext extends ExprContext {
		public Token recordType;
		public Token NAME;
		public List<Token> names = new ArrayList<Token>();
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> NAME() { return getTokens(EclangParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(EclangParser.NAME, i);
		}
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public List<TerminalNode> NL() { return getTokens(EclangParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(EclangParser.NL, i);
		}
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public ExprCreateRecordNamedContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprCreateRecordNamed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprCreateRecordNamed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprCreateRecordNamed(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				_localctx = new ExprHereContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(55);
				match(T__31);
				}
				break;
			case 2:
				{
				_localctx = new ExprUnOpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(56);
				unOp();
				setState(57);
				expr(35);
				}
				break;
			case 3:
				{
				_localctx = new ExprSizeOfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(59);
				match(T__32);
				setState(60);
				match(T__33);
				setState(61);
				type(0);
				setState(62);
				match(T__34);
				}
				break;
			case 4:
				{
				_localctx = new ExprSizeOfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(64);
				match(T__32);
				setState(65);
				match(T__33);
				setState(66);
				match(INDENT);
				setState(67);
				type(0);
				setState(68);
				match(NL);
				setState(69);
				match(DEDENT);
				setState(70);
				match(T__34);
				}
				break;
			case 5:
				{
				_localctx = new ExprAlignOfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(72);
				match(T__35);
				setState(73);
				match(T__33);
				setState(74);
				type(0);
				setState(75);
				match(T__34);
				}
				break;
			case 6:
				{
				_localctx = new ExprAlignOfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(77);
				match(T__35);
				setState(78);
				match(T__33);
				setState(79);
				match(INDENT);
				setState(80);
				type(0);
				setState(81);
				match(NL);
				setState(82);
				match(DEDENT);
				setState(83);
				match(T__34);
				}
				break;
			case 7:
				{
				_localctx = new ExprOffsetOfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(85);
				match(T__36);
				setState(86);
				match(T__33);
				setState(87);
				match(INDENT);
				setState(88);
				type(0);
				setState(89);
				match(NL);
				setState(90);
				match(DEDENT);
				setState(91);
				match(T__34);
				}
				break;
			case 8:
				{
				_localctx = new ExprOffsetOfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(93);
				match(T__36);
				setState(94);
				match(T__33);
				setState(95);
				match(INDENT);
				setState(96);
				type(0);
				setState(97);
				match(NL);
				setState(98);
				match(DEDENT);
				setState(99);
				match(T__34);
				}
				break;
			case 9:
				{
				_localctx = new ExprAddressOfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(101);
				match(T__37);
				setState(102);
				match(T__33);
				setState(103);
				expr(0);
				setState(104);
				match(T__34);
				}
				break;
			case 10:
				{
				_localctx = new ExprAddressOfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(106);
				match(T__37);
				setState(107);
				match(T__33);
				setState(108);
				match(INDENT);
				setState(109);
				expr(0);
				setState(110);
				match(NL);
				setState(111);
				match(DEDENT);
				setState(112);
				match(T__34);
				}
				break;
			case 11:
				{
				_localctx = new ExprNestContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(114);
				match(T__33);
				setState(115);
				expr(0);
				setState(116);
				match(T__34);
				}
				break;
			case 12:
				{
				_localctx = new ExprNestContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(118);
				match(T__33);
				setState(119);
				match(INDENT);
				setState(120);
				expr(0);
				setState(121);
				match(NL);
				setState(122);
				match(DEDENT);
				setState(123);
				match(T__34);
				}
				break;
			case 13:
				{
				_localctx = new ExprPrimitiveContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(125);
				primitive();
				}
				break;
			case 14:
				{
				_localctx = new ExprNamedContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(126);
				match(NAME);
				}
				break;
			case 15:
				{
				_localctx = new ExprInlineIfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(127);
				match(T__38);
				setState(128);
				match(T__33);
				setState(129);
				((ExprInlineIfContext)_localctx).condition = expr(0);
				setState(130);
				match(T__34);
				setState(131);
				((ExprInlineIfContext)_localctx).body = expr(0);
				setState(132);
				match(T__39);
				setState(133);
				((ExprInlineIfContext)_localctx).elseBody = expr(22);
				}
				break;
			case 16:
				{
				_localctx = new ExprFunctionCallNoArgsContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(135);
				match(NAME);
				setState(136);
				match(T__33);
				setState(137);
				match(T__34);
				}
				break;
			case 17:
				{
				_localctx = new ExprFunctionCallWithArgsContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(138);
				match(NAME);
				setState(139);
				match(T__33);
				setState(140);
				((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
				setState(145);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(141);
					match(T__48);
					setState(142);
					((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
					((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
					}
					}
					setState(147);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(148);
				match(T__34);
				}
				break;
			case 18:
				{
				_localctx = new ExprFunctionCallWithArgsContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(150);
				match(NAME);
				setState(151);
				match(T__33);
				setState(152);
				match(INDENT);
				setState(153);
				((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
				setState(159);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(154);
					match(T__48);
					setState(155);
					match(NL);
					setState(156);
					((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
					((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
					}
					}
					setState(161);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(162);
				match(NL);
				setState(163);
				match(DEDENT);
				setState(164);
				match(T__34);
				}
				break;
			case 19:
				{
				_localctx = new ExprCreateRecordNamedContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(167);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(166);
					((ExprCreateRecordNamedContext)_localctx).recordType = match(NAME);
					}
				}

				setState(169);
				match(T__49);
				setState(170);
				((ExprCreateRecordNamedContext)_localctx).NAME = match(NAME);
				((ExprCreateRecordNamedContext)_localctx).names.add(((ExprCreateRecordNamedContext)_localctx).NAME);
				setState(171);
				match(T__50);
				setState(172);
				expr(0);
				setState(179);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(173);
					match(T__48);
					setState(174);
					((ExprCreateRecordNamedContext)_localctx).NAME = match(NAME);
					((ExprCreateRecordNamedContext)_localctx).names.add(((ExprCreateRecordNamedContext)_localctx).NAME);
					setState(175);
					match(T__50);
					setState(176);
					expr(0);
					}
					}
					setState(181);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(182);
				match(T__51);
				}
				break;
			case 20:
				{
				_localctx = new ExprCreateRecordNamedContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(185);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(184);
					((ExprCreateRecordNamedContext)_localctx).recordType = match(NAME);
					}
				}

				setState(187);
				match(T__49);
				setState(188);
				match(INDENT);
				setState(189);
				((ExprCreateRecordNamedContext)_localctx).NAME = match(NAME);
				((ExprCreateRecordNamedContext)_localctx).names.add(((ExprCreateRecordNamedContext)_localctx).NAME);
				setState(190);
				match(T__50);
				setState(191);
				expr(0);
				setState(199);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(192);
					match(T__48);
					setState(193);
					match(NL);
					setState(194);
					((ExprCreateRecordNamedContext)_localctx).NAME = match(NAME);
					((ExprCreateRecordNamedContext)_localctx).names.add(((ExprCreateRecordNamedContext)_localctx).NAME);
					setState(195);
					match(T__50);
					setState(196);
					expr(0);
					}
					}
					setState(201);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(202);
				match(NL);
				setState(203);
				match(DEDENT);
				setState(204);
				match(T__51);
				}
				break;
			case 21:
				{
				_localctx = new ExprCreateRecordContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(207);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(206);
					((ExprCreateRecordContext)_localctx).recordType = match(NAME);
					}
				}

				setState(209);
				match(T__49);
				setState(210);
				expr(0);
				setState(215);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(211);
					match(T__48);
					setState(212);
					expr(0);
					}
					}
					setState(217);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(218);
				match(T__51);
				}
				break;
			case 22:
				{
				_localctx = new ExprCreateRecordContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(221);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(220);
					((ExprCreateRecordContext)_localctx).recordType = match(NAME);
					}
				}

				setState(223);
				match(T__49);
				setState(224);
				match(INDENT);
				setState(225);
				expr(0);
				setState(231);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(226);
					match(T__48);
					setState(227);
					match(NL);
					setState(228);
					expr(0);
					}
					}
					setState(233);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(234);
				match(NL);
				setState(235);
				match(DEDENT);
				setState(236);
				match(T__51);
				}
				break;
			case 23:
				{
				_localctx = new ExprNestContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(238);
				match(T__33);
				setState(239);
				expr(0);
				setState(240);
				match(T__34);
				}
				break;
			case 24:
				{
				_localctx = new ExprNestContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(242);
				match(T__33);
				setState(243);
				match(INDENT);
				setState(244);
				expr(0);
				setState(245);
				match(NL);
				setState(246);
				match(DEDENT);
				setState(247);
				match(T__34);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(328);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(326);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
					case 1:
						{
						_localctx = new ExprBinOpContext(new ExprContext(_parentctx, _parentState));
						((ExprBinOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(251);
						if (!(precpred(_ctx, 36))) throw new FailedPredicateException(this, "precpred(_ctx, 36)");
						setState(252);
						binOp();
						setState(253);
						((ExprBinOpContext)_localctx).right = expr(37);
						}
						break;
					case 2:
						{
						_localctx = new ExprIsContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(255);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(256);
						match(T__40);
						setState(257);
						match(NAME);
						}
						break;
					case 3:
						{
						_localctx = new ExprIsNotContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(258);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(259);
						match(T__41);
						setState(260);
						match(NAME);
						}
						break;
					case 4:
						{
						_localctx = new ExprAsContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(261);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(262);
						match(T__42);
						setState(263);
						type(0);
						}
						break;
					case 5:
						{
						_localctx = new ExprAsUnsafeContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(264);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(265);
						match(T__42);
						setState(266);
						match(T__43);
						setState(267);
						type(0);
						}
						break;
					case 6:
						{
						_localctx = new ExprCastNotNullContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(268);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(269);
						match(T__1);
						}
						break;
					case 7:
						{
						_localctx = new ExprAccessNameContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(270);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(271);
						match(T__44);
						setState(272);
						match(NAME);
						}
						break;
					case 8:
						{
						_localctx = new ExprAccessNameNullishCoalescingContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(273);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(274);
						match(T__45);
						setState(275);
						match(NAME);
						}
						break;
					case 9:
						{
						_localctx = new ExprAccessorContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(276);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(277);
						match(T__46);
						setState(278);
						expr(0);
						setState(279);
						match(T__47);
						}
						break;
					case 10:
						{
						_localctx = new ExprAccessorContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(281);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(282);
						match(T__46);
						setState(283);
						match(INDENT);
						setState(284);
						expr(0);
						setState(285);
						match(NL);
						setState(286);
						match(DEDENT);
						setState(287);
						match(T__47);
						}
						break;
					case 11:
						{
						_localctx = new ExprFunctionCallNoArgsContext(new ExprContext(_parentctx, _parentState));
						((ExprFunctionCallNoArgsContext)_localctx).args.add(_prevctx);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(289);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(290);
						match(T__44);
						setState(291);
						match(NAME);
						setState(292);
						match(T__33);
						setState(293);
						match(T__34);
						}
						break;
					case 12:
						{
						_localctx = new ExprFunctionCallWithArgsContext(new ExprContext(_parentctx, _parentState));
						((ExprFunctionCallWithArgsContext)_localctx).args.add(_prevctx);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(294);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(295);
						match(T__44);
						setState(296);
						match(NAME);
						setState(297);
						match(T__33);
						setState(298);
						((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
						((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
						setState(303);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==T__48) {
							{
							{
							setState(299);
							match(T__48);
							setState(300);
							((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
							((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
							}
							}
							setState(305);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(306);
						match(T__34);
						}
						break;
					case 13:
						{
						_localctx = new ExprFunctionCallWithArgsContext(new ExprContext(_parentctx, _parentState));
						((ExprFunctionCallWithArgsContext)_localctx).args.add(_prevctx);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(308);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(309);
						match(T__44);
						setState(310);
						match(NAME);
						setState(311);
						match(T__33);
						setState(312);
						match(INDENT);
						setState(313);
						((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
						((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
						setState(319);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==T__48) {
							{
							{
							setState(314);
							match(T__48);
							setState(315);
							match(NL);
							setState(316);
							((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
							((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
							}
							}
							setState(321);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(322);
						match(NL);
						setState(323);
						match(DEDENT);
						setState(324);
						match(T__34);
						}
						break;
					}
					} 
				}
				setState(330);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	 
		public TypeContext() { }
		public void copyFrom(TypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TypeUnionContext extends TypeContext {
		public List<TypeContext> types = new ArrayList<TypeContext>();
		public TypeContext type;
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TypeUnionContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterTypeUnion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitTypeUnion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitTypeUnion(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TypeAddressByNameContext extends TypeContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeAddressByNameContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterTypeAddressByName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitTypeAddressByName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitTypeAddressByName(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TypeEnumContext extends TypeContext {
		public List<TypeContext> types = new ArrayList<TypeContext>();
		public TypeContext type;
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TypeEnumContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterTypeEnum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitTypeEnum(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitTypeEnum(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TypeNullableContext extends TypeContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeNullableContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterTypeNullable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitTypeNullable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitTypeNullable(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TypeByNameContext extends TypeContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public TypeByNameContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterTypeByName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitTypeByName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitTypeByName(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TypeAtomContext extends TypeContext {
		public TerminalNode ATOM() { return getToken(EclangParser.ATOM, 0); }
		public TypeAtomContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterTypeAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitTypeAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitTypeAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TypeNestContext extends TypeContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeNestContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterTypeNest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitTypeNest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitTypeNest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		return type(0);
	}

	private TypeContext type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeContext _localctx = new TypeContext(_ctx, _parentState);
		TypeContext _prevctx = _localctx;
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_type, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAME:
				{
				_localctx = new TypeByNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(332);
				match(NAME);
				}
				break;
			case T__5:
				{
				_localctx = new TypeAddressByNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(333);
				match(T__5);
				setState(334);
				type(6);
				}
				break;
			case T__33:
				{
				_localctx = new TypeNestContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(335);
				match(T__33);
				setState(336);
				type(0);
				setState(337);
				match(T__34);
				}
				break;
			case ATOM:
				{
				_localctx = new TypeAtomContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(339);
				match(ATOM);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(366);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(364);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new TypeNullableContext(new TypeContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(342);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(343);
						match(T__52);
						}
						break;
					case 2:
						{
						_localctx = new TypeEnumContext(new TypeContext(_parentctx, _parentState));
						((TypeEnumContext)_localctx).types.add(_prevctx);
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(344);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(345);
						match(T__29);
						setState(346);
						((TypeEnumContext)_localctx).type = type(0);
						((TypeEnumContext)_localctx).types.add(((TypeEnumContext)_localctx).type);
						setState(351);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
						while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
							if ( _alt==1 ) {
								{
								{
								setState(347);
								match(T__29);
								setState(348);
								((TypeEnumContext)_localctx).type = type(0);
								((TypeEnumContext)_localctx).types.add(((TypeEnumContext)_localctx).type);
								}
								} 
							}
							setState(353);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
						}
						}
						break;
					case 3:
						{
						_localctx = new TypeUnionContext(new TypeContext(_parentctx, _parentState));
						((TypeUnionContext)_localctx).types.add(_prevctx);
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(354);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(355);
						match(T__28);
						setState(356);
						((TypeUnionContext)_localctx).type = type(0);
						((TypeUnionContext)_localctx).types.add(((TypeUnionContext)_localctx).type);
						setState(361);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
						while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
							if ( _alt==1 ) {
								{
								{
								setState(357);
								match(T__28);
								setState(358);
								((TypeUnionContext)_localctx).type = type(0);
								((TypeUnionContext)_localctx).types.add(((TypeUnionContext)_localctx).type);
								}
								} 
							}
							setState(363);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
						}
						}
						break;
					}
					} 
				}
				setState(368);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InterfaceContext extends ParserRuleContext {
		public InterfaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interface; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterInterface(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitInterface(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitInterface(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InterfaceContext interface_() throws RecognitionException {
		InterfaceContext _localctx = new InterfaceContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_interface);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(369);
			match(T__53);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterContext extends ParserRuleContext {
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
	 
		public ParameterContext() { }
		public void copyFrom(ParameterContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParameterTypedContext extends ParameterContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ParameterTypedContext(ParameterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterParameterTyped(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitParameterTyped(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitParameterTyped(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParameterDefaultContext extends ParameterContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParameterDefaultContext(ParameterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterParameterDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitParameterDefault(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitParameterDefault(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParameterTypedDefaultContext extends ParameterContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParameterTypedDefaultContext(ParameterContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterParameterTypedDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitParameterTypedDefault(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitParameterTypedDefault(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_parameter);
		try {
			setState(383);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				_localctx = new ParameterTypedContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(371);
				match(NAME);
				setState(372);
				match(T__54);
				setState(373);
				type(0);
				}
				break;
			case 2:
				_localctx = new ParameterTypedDefaultContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(374);
				match(NAME);
				setState(375);
				match(T__54);
				setState(376);
				type(0);
				setState(377);
				match(T__50);
				setState(378);
				expr(0);
				}
				break;
			case 3:
				_localctx = new ParameterDefaultContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(380);
				match(NAME);
				setState(381);
				match(T__50);
				setState(382);
				expr(0);
				}
				break;
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

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementFunctionCallWithArgsContext extends StatementContext {
		public ExprContext expr;
		public List<ExprContext> args = new ArrayList<ExprContext>();
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public List<TerminalNode> NL() { return getTokens(EclangParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(EclangParser.NL, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public StatementFunctionCallWithArgsContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementFunctionCallWithArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementFunctionCallWithArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementFunctionCallWithArgs(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementAssignmentContext extends StatementContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public StatementAssignmentContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementAssignment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementAssignment(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementDefineTypedContext extends StatementContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public StatementDefineTypedContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementDefineTyped(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementDefineTyped(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementDefineTyped(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementContinueContext extends StatementContext {
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public StatementContinueContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementContinue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementContinue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementContinue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementReturnContext extends StatementContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public StatementReturnContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementReturn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementReturn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementReturn(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementBreakContext extends StatementContext {
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public StatementBreakContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementBreak(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementBreak(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementBreak(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementForInContext extends StatementContext {
		public BlockContext body;
		public BlockContext elseBody;
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public StatementForInContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementForIn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementForIn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementForIn(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementDeferContext extends StatementContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public StatementDeferContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementDefer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementDefer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementDefer(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementFunctionCallNoArgsContext extends StatementContext {
		public ExprContext expr;
		public List<ExprContext> args = new ArrayList<ExprContext>();
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementFunctionCallNoArgsContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementFunctionCallNoArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementFunctionCallNoArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementFunctionCallNoArgs(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementDefineConstTypedContext extends StatementContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public StatementDefineConstTypedContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementDefineConstTyped(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementDefineConstTyped(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementDefineConstTyped(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementDefineConstContext extends StatementContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public StatementDefineConstContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementDefineConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementDefineConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementDefineConst(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementDefineContext extends StatementContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public StatementDefineContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementDefine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementDefine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementDefine(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementPassContext extends StatementContext {
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public StatementPassContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementPass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementPass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementPass(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementYeildContext extends StatementContext {
		public StatementYeildContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementYeild(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementYeild(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementYeild(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementWhileContext extends StatementContext {
		public ExprContext condition;
		public BlockContext body;
		public BlockContext elseBody;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public StatementWhileContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementWhile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementWhile(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementWhile(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementIfContext extends StatementContext {
		public ExprContext expr;
		public List<ExprContext> conditions = new ArrayList<ExprContext>();
		public BlockContext block;
		public List<BlockContext> bodies = new ArrayList<BlockContext>();
		public BlockContext elseBody;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public StatementIfContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementIf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementIf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementIf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_statement);
		int _la;
		try {
			int _alt;
			setState(575);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				_localctx = new StatementDefineContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(385);
				match(T__55);
				setState(386);
				match(NAME);
				setState(387);
				match(T__50);
				setState(388);
				expr(0);
				setState(389);
				match(NL);
				}
				break;
			case 2:
				_localctx = new StatementDefineConstContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(391);
				match(T__56);
				setState(392);
				match(NAME);
				setState(393);
				match(T__50);
				setState(394);
				expr(0);
				setState(395);
				match(NL);
				}
				break;
			case 3:
				_localctx = new StatementDefineTypedContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(397);
				match(T__55);
				setState(398);
				match(NAME);
				setState(401);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__54) {
					{
					setState(399);
					match(T__54);
					setState(400);
					type(0);
					}
				}

				setState(403);
				match(T__50);
				setState(404);
				expr(0);
				setState(405);
				match(NL);
				}
				break;
			case 4:
				_localctx = new StatementDefineConstTypedContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(407);
				match(T__56);
				setState(408);
				match(NAME);
				setState(411);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__54) {
					{
					setState(409);
					match(T__54);
					setState(410);
					type(0);
					}
				}

				setState(413);
				match(T__50);
				setState(414);
				expr(0);
				setState(415);
				match(NL);
				}
				break;
			case 5:
				_localctx = new StatementAssignmentContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(417);
				match(NAME);
				setState(418);
				match(T__50);
				setState(419);
				expr(0);
				setState(420);
				match(NL);
				}
				break;
			case 6:
				_localctx = new StatementBreakContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(422);
				match(T__57);
				setState(423);
				match(NL);
				}
				break;
			case 7:
				_localctx = new StatementContinueContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(424);
				match(T__58);
				setState(425);
				match(NL);
				}
				break;
			case 8:
				_localctx = new StatementPassContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(426);
				match(T__59);
				setState(427);
				match(NL);
				}
				break;
			case 9:
				_localctx = new StatementYeildContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(428);
				match(T__60);
				}
				break;
			case 10:
				_localctx = new StatementDeferContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(429);
				match(T__61);
				setState(430);
				statement();
				}
				break;
			case 11:
				_localctx = new StatementIfContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(431);
				match(T__38);
				setState(433);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
				case 1:
					{
					setState(432);
					match(T__33);
					}
					break;
				}
				setState(435);
				((StatementIfContext)_localctx).expr = expr(0);
				((StatementIfContext)_localctx).conditions.add(((StatementIfContext)_localctx).expr);
				setState(437);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__34) {
					{
					setState(436);
					match(T__34);
					}
				}

				setState(439);
				match(T__62);
				setState(440);
				((StatementIfContext)_localctx).block = block();
				((StatementIfContext)_localctx).bodies.add(((StatementIfContext)_localctx).block);
				setState(455);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(441);
						match(T__39);
						setState(442);
						match(T__38);
						setState(444);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
						case 1:
							{
							setState(443);
							match(T__33);
							}
							break;
						}
						setState(446);
						((StatementIfContext)_localctx).expr = expr(0);
						((StatementIfContext)_localctx).conditions.add(((StatementIfContext)_localctx).expr);
						setState(448);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==T__34) {
							{
							setState(447);
							match(T__34);
							}
						}

						setState(450);
						match(T__62);
						setState(451);
						((StatementIfContext)_localctx).block = block();
						((StatementIfContext)_localctx).bodies.add(((StatementIfContext)_localctx).block);
						}
						} 
					}
					setState(457);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				}
				setState(460);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__39) {
					{
					setState(458);
					match(T__39);
					setState(459);
					((StatementIfContext)_localctx).elseBody = block();
					}
				}

				}
				break;
			case 12:
				_localctx = new StatementWhileContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(462);
				match(T__63);
				setState(464);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
				case 1:
					{
					setState(463);
					match(T__33);
					}
					break;
				}
				setState(466);
				((StatementWhileContext)_localctx).condition = expr(0);
				setState(468);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__34) {
					{
					setState(467);
					match(T__34);
					}
				}

				setState(470);
				match(T__62);
				setState(471);
				((StatementWhileContext)_localctx).body = block();
				setState(474);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__39) {
					{
					setState(472);
					match(T__39);
					setState(473);
					((StatementWhileContext)_localctx).elseBody = block();
					}
				}

				}
				break;
			case 13:
				_localctx = new StatementForInContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(476);
				match(T__64);
				setState(478);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__33) {
					{
					setState(477);
					match(T__33);
					}
				}

				setState(480);
				match(NAME);
				setState(481);
				match(T__65);
				setState(482);
				expr(0);
				setState(485);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__66) {
					{
					setState(483);
					match(T__66);
					setState(484);
					expr(0);
					}
				}

				setState(488);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__34) {
					{
					setState(487);
					match(T__34);
					}
				}

				setState(490);
				match(T__62);
				setState(491);
				((StatementForInContext)_localctx).body = block();
				setState(494);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__39) {
					{
					setState(492);
					match(T__39);
					setState(493);
					((StatementForInContext)_localctx).elseBody = block();
					}
				}

				}
				break;
			case 14:
				_localctx = new StatementFunctionCallNoArgsContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(496);
				match(NAME);
				setState(497);
				match(T__33);
				setState(498);
				match(T__34);
				setState(499);
				match(NL);
				}
				break;
			case 15:
				_localctx = new StatementFunctionCallWithArgsContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(500);
				match(NAME);
				setState(501);
				match(T__33);
				setState(502);
				((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
				setState(507);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(503);
					match(T__48);
					setState(504);
					((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
					((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
					}
					}
					setState(509);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(510);
				match(T__34);
				setState(511);
				match(NL);
				}
				break;
			case 16:
				_localctx = new StatementFunctionCallWithArgsContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(513);
				match(NAME);
				setState(514);
				match(T__33);
				setState(515);
				match(INDENT);
				setState(516);
				((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
				setState(522);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(517);
					match(T__48);
					setState(518);
					match(NL);
					setState(519);
					((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
					((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
					}
					}
					setState(524);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(525);
				match(NL);
				setState(526);
				match(DEDENT);
				setState(527);
				match(T__34);
				setState(528);
				match(NL);
				}
				break;
			case 17:
				_localctx = new StatementFunctionCallNoArgsContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(530);
				((StatementFunctionCallNoArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallNoArgsContext)_localctx).args.add(((StatementFunctionCallNoArgsContext)_localctx).expr);
				setState(531);
				match(T__44);
				setState(532);
				match(NAME);
				setState(533);
				match(T__33);
				setState(534);
				match(T__34);
				setState(535);
				match(NL);
				}
				break;
			case 18:
				_localctx = new StatementFunctionCallWithArgsContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(537);
				((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
				setState(538);
				match(T__44);
				setState(539);
				match(NAME);
				setState(540);
				match(T__33);
				setState(541);
				((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
				setState(546);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(542);
					match(T__48);
					setState(543);
					((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
					((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
					}
					}
					setState(548);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(549);
				match(T__34);
				setState(550);
				match(NL);
				}
				break;
			case 19:
				_localctx = new StatementFunctionCallWithArgsContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(552);
				((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
				setState(553);
				match(T__44);
				setState(554);
				match(NAME);
				setState(555);
				match(T__33);
				setState(556);
				match(INDENT);
				setState(557);
				((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
				setState(563);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(558);
					match(T__48);
					setState(559);
					match(NL);
					setState(560);
					((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
					((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
					}
					}
					setState(565);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(566);
				match(NL);
				setState(567);
				match(DEDENT);
				setState(568);
				match(T__34);
				setState(569);
				match(NL);
				}
				break;
			case 20:
				_localctx = new StatementReturnContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(571);
				match(T__67);
				setState(572);
				expr(0);
				setState(573);
				match(NL);
				}
				break;
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

	@SuppressWarnings("CheckReturnValue")
	public static class FuncContext extends ParserRuleContext {
		public FuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func; }
	 
		public FuncContext() { }
		public void copyFrom(FuncContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionInlineContext extends FuncContext {
		public Token name;
		public ParameterContext parameter;
		public List<ParameterContext> params = new ArrayList<ParameterContext>();
		public TypeContext returnType;
		public ExprContext expression;
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public FunctionInlineContext(FuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterFunctionInline(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitFunctionInline(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitFunctionInline(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionBlockContext extends FuncContext {
		public Token name;
		public ParameterContext parameter;
		public List<ParameterContext> params = new ArrayList<ParameterContext>();
		public TypeContext returnType;
		public BlockContext body;
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public FunctionBlockContext(FuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterFunctionBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitFunctionBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitFunctionBlock(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDeclareContext extends FuncContext {
		public Token externalName;
		public Token name;
		public ParameterContext parameter;
		public List<ParameterContext> params = new ArrayList<ParameterContext>();
		public TypeContext returnType;
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public FunctionDeclareContext(FuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterFunctionDeclare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitFunctionDeclare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitFunctionDeclare(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncContext func() throws RecognitionException {
		FuncContext _localctx = new FuncContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_func);
		int _la;
		try {
			setState(643);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				_localctx = new FunctionDeclareContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(577);
				match(T__68);
				setState(578);
				match(T__69);
				setState(581);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(579);
					((FunctionDeclareContext)_localctx).externalName = match(STRING);
					setState(580);
					match(T__42);
					}
				}

				setState(583);
				((FunctionDeclareContext)_localctx).name = match(NAME);
				setState(584);
				match(T__33);
				setState(586);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(585);
					((FunctionDeclareContext)_localctx).parameter = parameter();
					((FunctionDeclareContext)_localctx).params.add(((FunctionDeclareContext)_localctx).parameter);
					}
				}

				setState(592);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(588);
					match(T__48);
					setState(589);
					((FunctionDeclareContext)_localctx).parameter = parameter();
					((FunctionDeclareContext)_localctx).params.add(((FunctionDeclareContext)_localctx).parameter);
					}
					}
					setState(594);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(595);
				match(T__34);
				setState(598);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__54) {
					{
					setState(596);
					match(T__54);
					setState(597);
					((FunctionDeclareContext)_localctx).returnType = type(0);
					}
				}

				setState(600);
				match(NL);
				}
				break;
			case 2:
				_localctx = new FunctionBlockContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(601);
				match(T__69);
				setState(602);
				((FunctionBlockContext)_localctx).name = match(NAME);
				setState(603);
				match(T__33);
				setState(605);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(604);
					((FunctionBlockContext)_localctx).parameter = parameter();
					((FunctionBlockContext)_localctx).params.add(((FunctionBlockContext)_localctx).parameter);
					}
				}

				setState(611);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(607);
					match(T__48);
					setState(608);
					((FunctionBlockContext)_localctx).parameter = parameter();
					((FunctionBlockContext)_localctx).params.add(((FunctionBlockContext)_localctx).parameter);
					}
					}
					setState(613);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(614);
				match(T__34);
				setState(617);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__54) {
					{
					setState(615);
					match(T__54);
					setState(616);
					((FunctionBlockContext)_localctx).returnType = type(0);
					}
				}

				setState(619);
				match(T__62);
				setState(620);
				((FunctionBlockContext)_localctx).body = block();
				}
				break;
			case 3:
				_localctx = new FunctionInlineContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(621);
				match(T__69);
				setState(622);
				((FunctionInlineContext)_localctx).name = match(NAME);
				setState(623);
				match(T__33);
				setState(625);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(624);
					((FunctionInlineContext)_localctx).parameter = parameter();
					((FunctionInlineContext)_localctx).params.add(((FunctionInlineContext)_localctx).parameter);
					}
				}

				setState(631);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__48) {
					{
					{
					setState(627);
					match(T__48);
					setState(628);
					((FunctionInlineContext)_localctx).parameter = parameter();
					((FunctionInlineContext)_localctx).params.add(((FunctionInlineContext)_localctx).parameter);
					}
					}
					setState(633);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(634);
				match(T__34);
				setState(637);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__54) {
					{
					setState(635);
					match(T__54);
					setState(636);
					((FunctionInlineContext)_localctx).returnType = type(0);
					}
				}

				setState(639);
				match(T__50);
				setState(640);
				((FunctionInlineContext)_localctx).expression = expr(0);
				setState(641);
				match(NL);
				}
				break;
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

	@SuppressWarnings("CheckReturnValue")
	public static class GlobalContext extends ParserRuleContext {
		public GlobalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_global; }
	 
		public GlobalContext() { }
		public void copyFrom(GlobalContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalDefineConstTypedContext extends GlobalContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public GlobalDefineConstTypedContext(GlobalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterGlobalDefineConstTyped(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitGlobalDefineConstTyped(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitGlobalDefineConstTyped(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalDefineConstContext extends GlobalContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public GlobalDefineConstContext(GlobalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterGlobalDefineConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitGlobalDefineConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitGlobalDefineConst(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalDeclareConstContext extends GlobalContext {
		public Token externalName;
		public Token name;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public GlobalDeclareConstContext(GlobalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterGlobalDeclareConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitGlobalDeclareConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitGlobalDeclareConst(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalRecordDeclareDefineContext extends GlobalContext {
		public Token externalName;
		public Token name;
		public Token NAME;
		public List<Token> names = new ArrayList<Token>();
		public TypeContext type;
		public List<TypeContext> types = new ArrayList<TypeContext>();
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public List<TerminalNode> NAME() { return getTokens(EclangParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(EclangParser.NAME, i);
		}
		public List<TerminalNode> NL() { return getTokens(EclangParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(EclangParser.NL, i);
		}
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public GlobalRecordDeclareDefineContext(GlobalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterGlobalRecordDeclareDefine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitGlobalRecordDeclareDefine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitGlobalRecordDeclareDefine(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalRecordDefineContext extends GlobalContext {
		public Token name;
		public Token NAME;
		public List<Token> names = new ArrayList<Token>();
		public TypeContext type;
		public List<TypeContext> types = new ArrayList<TypeContext>();
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public List<TerminalNode> NAME() { return getTokens(EclangParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(EclangParser.NAME, i);
		}
		public List<TerminalNode> NL() { return getTokens(EclangParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(EclangParser.NL, i);
		}
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public GlobalRecordDefineContext(GlobalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterGlobalRecordDefine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitGlobalRecordDefine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitGlobalRecordDefine(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalDefineContext extends GlobalContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public GlobalDefineContext(GlobalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterGlobalDefine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitGlobalDefine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitGlobalDefine(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalDefineTypedContext extends GlobalContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public GlobalDefineTypedContext(GlobalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterGlobalDefineTyped(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitGlobalDefineTyped(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitGlobalDefineTyped(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalTypeRecordDeclareDefineContext extends GlobalContext {
		public Token externalName;
		public Token name;
		public Token NAME;
		public List<Token> names = new ArrayList<Token>();
		public TypeContext type;
		public List<TypeContext> types = new ArrayList<TypeContext>();
		public TerminalNode INDENT() { return getToken(EclangParser.INDENT, 0); }
		public TerminalNode DEDENT() { return getToken(EclangParser.DEDENT, 0); }
		public List<TerminalNode> NAME() { return getTokens(EclangParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(EclangParser.NAME, i);
		}
		public List<TerminalNode> NL() { return getTokens(EclangParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(EclangParser.NL, i);
		}
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public GlobalTypeRecordDeclareDefineContext(GlobalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterGlobalTypeRecordDeclareDefine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitGlobalTypeRecordDeclareDefine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitGlobalTypeRecordDeclareDefine(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalTypeDefineContext extends GlobalContext {
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public GlobalTypeDefineContext(GlobalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterGlobalTypeDefine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitGlobalTypeDefine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitGlobalTypeDefine(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GlobalDeclareDefineContext extends GlobalContext {
		public Token externalName;
		public Token name;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public GlobalDeclareDefineContext(GlobalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterGlobalDeclareDefine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitGlobalDeclareDefine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitGlobalDeclareDefine(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GlobalContext global() throws RecognitionException {
		GlobalContext _localctx = new GlobalContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_global);
		int _la;
		try {
			setState(771);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				_localctx = new GlobalDefineContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(645);
				match(T__55);
				setState(646);
				match(NAME);
				setState(647);
				match(T__50);
				setState(648);
				expr(0);
				setState(649);
				match(NL);
				}
				break;
			case 2:
				_localctx = new GlobalDefineConstContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(651);
				match(T__56);
				setState(652);
				match(NAME);
				setState(653);
				match(T__50);
				setState(654);
				expr(0);
				setState(655);
				match(NL);
				}
				break;
			case 3:
				_localctx = new GlobalDefineTypedContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(657);
				match(T__55);
				setState(658);
				match(NAME);
				setState(661);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__54) {
					{
					setState(659);
					match(T__54);
					setState(660);
					type(0);
					}
				}

				setState(663);
				match(T__50);
				setState(664);
				expr(0);
				setState(665);
				match(NL);
				}
				break;
			case 4:
				_localctx = new GlobalDefineConstTypedContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(667);
				match(T__56);
				setState(668);
				match(NAME);
				setState(671);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__54) {
					{
					setState(669);
					match(T__54);
					setState(670);
					type(0);
					}
				}

				setState(673);
				match(T__50);
				setState(674);
				expr(0);
				setState(675);
				match(NL);
				}
				break;
			case 5:
				_localctx = new GlobalDeclareDefineContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(677);
				match(T__68);
				setState(678);
				match(T__55);
				setState(681);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(679);
					((GlobalDeclareDefineContext)_localctx).externalName = match(STRING);
					setState(680);
					match(T__42);
					}
				}

				setState(683);
				((GlobalDeclareDefineContext)_localctx).name = match(NAME);
				setState(684);
				match(T__54);
				setState(685);
				type(0);
				setState(686);
				match(NL);
				}
				break;
			case 6:
				_localctx = new GlobalDeclareConstContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(688);
				match(T__68);
				setState(689);
				match(T__56);
				setState(692);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(690);
					((GlobalDeclareConstContext)_localctx).externalName = match(STRING);
					setState(691);
					match(T__42);
					}
				}

				setState(694);
				((GlobalDeclareConstContext)_localctx).name = match(NAME);
				setState(695);
				match(T__54);
				setState(696);
				type(0);
				setState(697);
				match(NL);
				}
				break;
			case 7:
				_localctx = new GlobalTypeDefineContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(699);
				match(T__68);
				setState(700);
				match(T__70);
				setState(701);
				match(NAME);
				setState(702);
				match(T__50);
				setState(703);
				type(0);
				setState(704);
				match(NL);
				}
				break;
			case 8:
				_localctx = new GlobalRecordDefineContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(706);
				match(T__71);
				setState(707);
				((GlobalRecordDefineContext)_localctx).name = match(NAME);
				setState(708);
				match(T__42);
				setState(709);
				match(INDENT);
				setState(715); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(710);
					((GlobalRecordDefineContext)_localctx).NAME = match(NAME);
					((GlobalRecordDefineContext)_localctx).names.add(((GlobalRecordDefineContext)_localctx).NAME);
					setState(711);
					match(T__54);
					setState(712);
					((GlobalRecordDefineContext)_localctx).type = type(0);
					((GlobalRecordDefineContext)_localctx).types.add(((GlobalRecordDefineContext)_localctx).type);
					setState(713);
					match(NL);
					}
					}
					setState(717); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==NAME );
				setState(719);
				match(DEDENT);
				}
				break;
			case 9:
				_localctx = new GlobalRecordDeclareDefineContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(721);
				match(T__68);
				setState(722);
				match(T__71);
				setState(725);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(723);
					((GlobalRecordDeclareDefineContext)_localctx).externalName = match(STRING);
					setState(724);
					match(T__42);
					}
				}

				setState(727);
				((GlobalRecordDeclareDefineContext)_localctx).name = match(NAME);
				setState(728);
				match(T__42);
				setState(729);
				match(INDENT);
				setState(735); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(730);
					((GlobalRecordDeclareDefineContext)_localctx).NAME = match(NAME);
					((GlobalRecordDeclareDefineContext)_localctx).names.add(((GlobalRecordDeclareDefineContext)_localctx).NAME);
					setState(731);
					match(T__54);
					setState(732);
					((GlobalRecordDeclareDefineContext)_localctx).type = type(0);
					((GlobalRecordDeclareDefineContext)_localctx).types.add(((GlobalRecordDeclareDefineContext)_localctx).type);
					setState(733);
					match(NL);
					}
					}
					setState(737); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==NAME );
				setState(739);
				match(DEDENT);
				}
				break;
			case 10:
				_localctx = new GlobalTypeRecordDeclareDefineContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(741);
				match(T__68);
				setState(742);
				match(T__70);
				setState(743);
				match(T__71);
				setState(746);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(744);
					((GlobalTypeRecordDeclareDefineContext)_localctx).externalName = match(STRING);
					setState(745);
					match(T__42);
					}
				}

				setState(748);
				((GlobalTypeRecordDeclareDefineContext)_localctx).name = match(NAME);
				setState(749);
				match(T__42);
				setState(750);
				match(INDENT);
				setState(756); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(751);
					((GlobalTypeRecordDeclareDefineContext)_localctx).NAME = match(NAME);
					((GlobalTypeRecordDeclareDefineContext)_localctx).names.add(((GlobalTypeRecordDeclareDefineContext)_localctx).NAME);
					setState(752);
					match(T__54);
					setState(753);
					((GlobalTypeRecordDeclareDefineContext)_localctx).type = type(0);
					((GlobalTypeRecordDeclareDefineContext)_localctx).types.add(((GlobalTypeRecordDeclareDefineContext)_localctx).type);
					setState(754);
					match(NL);
					}
					}
					setState(758); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==NAME );
				setState(760);
				match(DEDENT);
				}
				break;
			case 11:
				_localctx = new GlobalTypeRecordDeclareDefineContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(762);
				match(T__68);
				setState(763);
				match(T__70);
				setState(764);
				match(T__71);
				setState(767);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(765);
					((GlobalTypeRecordDeclareDefineContext)_localctx).externalName = match(STRING);
					setState(766);
					match(T__42);
					}
				}

				setState(769);
				((GlobalTypeRecordDeclareDefineContext)_localctx).name = match(NAME);
				setState(770);
				match(NL);
				}
				break;
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

	@SuppressWarnings("CheckReturnValue")
	public static class UseContext extends ParserRuleContext {
		public Token from;
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public UseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_use; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterUse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitUse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitUse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UseContext use() throws RecognitionException {
		UseContext _localctx = new UseContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_use);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(773);
			match(T__72);
			setState(774);
			((UseContext)_localctx).from = match(STRING);
			setState(775);
			match(NL);
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

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public FuncContext func;
		public List<FuncContext> functions = new ArrayList<FuncContext>();
		public GlobalContext global;
		public List<GlobalContext> globals = new ArrayList<GlobalContext>();
		public UseContext use;
		public List<UseContext> uses = new ArrayList<UseContext>();
		public TerminalNode EOF() { return getToken(EclangParser.EOF, 0); }
		public List<FuncContext> func() {
			return getRuleContexts(FuncContext.class);
		}
		public FuncContext func(int i) {
			return getRuleContext(FuncContext.class,i);
		}
		public List<GlobalContext> global() {
			return getRuleContexts(GlobalContext.class);
		}
		public GlobalContext global(int i) {
			return getRuleContext(GlobalContext.class,i);
		}
		public List<UseContext> use() {
			return getRuleContexts(UseContext.class);
		}
		public UseContext use(int i) {
			return getRuleContext(UseContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(782);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 56)) & ~0x3f) == 0 && ((1L << (_la - 56)) & 221187L) != 0)) {
				{
				setState(780);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
				case 1:
					{
					setState(777);
					((ProgramContext)_localctx).func = func();
					((ProgramContext)_localctx).functions.add(((ProgramContext)_localctx).func);
					}
					break;
				case 2:
					{
					setState(778);
					((ProgramContext)_localctx).global = global();
					((ProgramContext)_localctx).globals.add(((ProgramContext)_localctx).global);
					}
					break;
				case 3:
					{
					setState(779);
					((ProgramContext)_localctx).use = use();
					((ProgramContext)_localctx).uses.add(((ProgramContext)_localctx).use);
					}
					break;
				}
				}
				setState(784);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(785);
			match(EOF);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 5:
			return expr_sempred((ExprContext)_localctx, predIndex);
		case 6:
			return type_sempred((TypeContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 36);
		case 1:
			return precpred(_ctx, 21);
		case 2:
			return precpred(_ctx, 20);
		case 3:
			return precpred(_ctx, 19);
		case 4:
			return precpred(_ctx, 18);
		case 5:
			return precpred(_ctx, 17);
		case 6:
			return precpred(_ctx, 16);
		case 7:
			return precpred(_ctx, 15);
		case 8:
			return precpred(_ctx, 14);
		case 9:
			return precpred(_ctx, 13);
		case 10:
			return precpred(_ctx, 9);
		case 11:
			return precpred(_ctx, 8);
		case 12:
			return precpred(_ctx, 7);
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 13:
			return precpred(_ctx, 5);
		case 14:
			return precpred(_ctx, 4);
		case 15:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\\\u0314\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0003\u0000\"\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0003\u0001)\b\u0001\u0001\u0002\u0001\u0002"+
		"\u0004\u0002-\b\u0002\u000b\u0002\f\u0002.\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u0090\b\u0005\n\u0005\f\u0005"+
		"\u0093\t\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u009e\b\u0005"+
		"\n\u0005\f\u0005\u00a1\t\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0003\u0005\u00a8\b\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005"+
		"\u0005\u00b2\b\u0005\n\u0005\f\u0005\u00b5\t\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0003\u0005\u00ba\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0005\u0005\u00c6\b\u0005\n\u0005\f\u0005\u00c9\t\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u00d0"+
		"\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u00d6"+
		"\b\u0005\n\u0005\f\u0005\u00d9\t\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0003\u0005\u00de\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0005\u0005\u00e6\b\u0005\n\u0005\f\u0005\u00e9"+
		"\t\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u00fa\b\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0005\u0005\u012e\b\u0005\n\u0005\f\u0005\u0131\t\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005"+
		"\u013e\b\u0005\n\u0005\f\u0005\u0141\t\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0005\u0005\u0147\b\u0005\n\u0005\f\u0005\u014a\t\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u0155\b\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0005\u0006\u015e\b\u0006\n\u0006\f\u0006\u0161\t\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u0168\b\u0006\n"+
		"\u0006\f\u0006\u016b\t\u0006\u0005\u0006\u016d\b\u0006\n\u0006\f\u0006"+
		"\u0170\t\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003"+
		"\b\u0180\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003"+
		"\t\u0192\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0003\t\u019c\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u01b2\b\t\u0001\t\u0001\t\u0003"+
		"\t\u01b6\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u01bd\b\t"+
		"\u0001\t\u0001\t\u0003\t\u01c1\b\t\u0001\t\u0001\t\u0001\t\u0005\t\u01c6"+
		"\b\t\n\t\f\t\u01c9\t\t\u0001\t\u0001\t\u0003\t\u01cd\b\t\u0001\t\u0001"+
		"\t\u0003\t\u01d1\b\t\u0001\t\u0001\t\u0003\t\u01d5\b\t\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0003\t\u01db\b\t\u0001\t\u0001\t\u0003\t\u01df\b\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u01e6\b\t\u0001\t\u0003\t\u01e9"+
		"\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u01ef\b\t\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0005\t\u01fa"+
		"\b\t\n\t\f\t\u01fd\t\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0005\t\u0209\b\t\n\t\f\t\u020c\t\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0005\t\u0221\b\t\n\t\f\t\u0224\t\t\u0001\t\u0001\t\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0005"+
		"\t\u0232\b\t\n\t\f\t\u0235\t\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u0240\b\t\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0003\n\u0246\b\n\u0001\n\u0001\n\u0001\n\u0003\n\u024b\b\n"+
		"\u0001\n\u0001\n\u0005\n\u024f\b\n\n\n\f\n\u0252\t\n\u0001\n\u0001\n\u0001"+
		"\n\u0003\n\u0257\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u025e"+
		"\b\n\u0001\n\u0001\n\u0005\n\u0262\b\n\n\n\f\n\u0265\t\n\u0001\n\u0001"+
		"\n\u0001\n\u0003\n\u026a\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0003\n\u0272\b\n\u0001\n\u0001\n\u0005\n\u0276\b\n\n\n\f\n\u0279\t"+
		"\n\u0001\n\u0001\n\u0001\n\u0003\n\u027e\b\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0003\n\u0284\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003"+
		"\u000b\u0296\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u02a0\b\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0003\u000b\u02aa\b\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u02b5\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0004"+
		"\u000b\u02cc\b\u000b\u000b\u000b\f\u000b\u02cd\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u02d6\b\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0004\u000b\u02e0\b\u000b\u000b\u000b\f\u000b"+
		"\u02e1\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0003\u000b\u02eb\b\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0004"+
		"\u000b\u02f5\b\u000b\u000b\u000b\f\u000b\u02f6\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b"+
		"\u0300\b\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u0304\b\u000b\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0005\r\u030d\b\r\n"+
		"\r\f\r\u0310\t\r\u0001\r\u0001\r\u0001\r\u0000\u0002\n\f\u000e\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u0000\u0002"+
		"\u0001\u0000\u0001\u0006\u0002\u0000\u0005\u0005\u0007\u001f\u0391\u0000"+
		"!\u0001\u0000\u0000\u0000\u0002(\u0001\u0000\u0000\u0000\u0004*\u0001"+
		"\u0000\u0000\u0000\u00062\u0001\u0000\u0000\u0000\b4\u0001\u0000\u0000"+
		"\u0000\n\u00f9\u0001\u0000\u0000\u0000\f\u0154\u0001\u0000\u0000\u0000"+
		"\u000e\u0171\u0001\u0000\u0000\u0000\u0010\u017f\u0001\u0000\u0000\u0000"+
		"\u0012\u023f\u0001\u0000\u0000\u0000\u0014\u0283\u0001\u0000\u0000\u0000"+
		"\u0016\u0303\u0001\u0000\u0000\u0000\u0018\u0305\u0001\u0000\u0000\u0000"+
		"\u001a\u030e\u0001\u0000\u0000\u0000\u001c\"\u0005N\u0000\u0000\u001d"+
		"\"\u0005P\u0000\u0000\u001e\"\u0005Q\u0000\u0000\u001f\"\u0005R\u0000"+
		"\u0000 \"\u0005S\u0000\u0000!\u001c\u0001\u0000\u0000\u0000!\u001d\u0001"+
		"\u0000\u0000\u0000!\u001e\u0001\u0000\u0000\u0000!\u001f\u0001\u0000\u0000"+
		"\u0000! \u0001\u0000\u0000\u0000\"\u0001\u0001\u0000\u0000\u0000#)\u0003"+
		"\u0000\u0000\u0000$)\u0005K\u0000\u0000%)\u0005U\u0000\u0000&)\u0005T"+
		"\u0000\u0000\')\u0005M\u0000\u0000(#\u0001\u0000\u0000\u0000($\u0001\u0000"+
		"\u0000\u0000(%\u0001\u0000\u0000\u0000(&\u0001\u0000\u0000\u0000(\'\u0001"+
		"\u0000\u0000\u0000)\u0003\u0001\u0000\u0000\u0000*,\u0005[\u0000\u0000"+
		"+-\u0003\u0012\t\u0000,+\u0001\u0000\u0000\u0000-.\u0001\u0000\u0000\u0000"+
		".,\u0001\u0000\u0000\u0000./\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000"+
		"\u000001\u0005\\\u0000\u00001\u0005\u0001\u0000\u0000\u000023\u0007\u0000"+
		"\u0000\u00003\u0007\u0001\u0000\u0000\u000045\u0007\u0001\u0000\u0000"+
		"5\t\u0001\u0000\u0000\u000067\u0006\u0005\uffff\uffff\u00007\u00fa\u0005"+
		" \u0000\u000089\u0003\u0006\u0003\u00009:\u0003\n\u0005#:\u00fa\u0001"+
		"\u0000\u0000\u0000;<\u0005!\u0000\u0000<=\u0005\"\u0000\u0000=>\u0003"+
		"\f\u0006\u0000>?\u0005#\u0000\u0000?\u00fa\u0001\u0000\u0000\u0000@A\u0005"+
		"!\u0000\u0000AB\u0005\"\u0000\u0000BC\u0005[\u0000\u0000CD\u0003\f\u0006"+
		"\u0000DE\u0005J\u0000\u0000EF\u0005\\\u0000\u0000FG\u0005#\u0000\u0000"+
		"G\u00fa\u0001\u0000\u0000\u0000HI\u0005$\u0000\u0000IJ\u0005\"\u0000\u0000"+
		"JK\u0003\f\u0006\u0000KL\u0005#\u0000\u0000L\u00fa\u0001\u0000\u0000\u0000"+
		"MN\u0005$\u0000\u0000NO\u0005\"\u0000\u0000OP\u0005[\u0000\u0000PQ\u0003"+
		"\f\u0006\u0000QR\u0005J\u0000\u0000RS\u0005\\\u0000\u0000ST\u0005#\u0000"+
		"\u0000T\u00fa\u0001\u0000\u0000\u0000UV\u0005%\u0000\u0000VW\u0005\"\u0000"+
		"\u0000WX\u0005[\u0000\u0000XY\u0003\f\u0006\u0000YZ\u0005J\u0000\u0000"+
		"Z[\u0005\\\u0000\u0000[\\\u0005#\u0000\u0000\\\u00fa\u0001\u0000\u0000"+
		"\u0000]^\u0005%\u0000\u0000^_\u0005\"\u0000\u0000_`\u0005[\u0000\u0000"+
		"`a\u0003\f\u0006\u0000ab\u0005J\u0000\u0000bc\u0005\\\u0000\u0000cd\u0005"+
		"#\u0000\u0000d\u00fa\u0001\u0000\u0000\u0000ef\u0005&\u0000\u0000fg\u0005"+
		"\"\u0000\u0000gh\u0003\n\u0005\u0000hi\u0005#\u0000\u0000i\u00fa\u0001"+
		"\u0000\u0000\u0000jk\u0005&\u0000\u0000kl\u0005\"\u0000\u0000lm\u0005"+
		"[\u0000\u0000mn\u0003\n\u0005\u0000no\u0005J\u0000\u0000op\u0005\\\u0000"+
		"\u0000pq\u0005#\u0000\u0000q\u00fa\u0001\u0000\u0000\u0000rs\u0005\"\u0000"+
		"\u0000st\u0003\n\u0005\u0000tu\u0005#\u0000\u0000u\u00fa\u0001\u0000\u0000"+
		"\u0000vw\u0005\"\u0000\u0000wx\u0005[\u0000\u0000xy\u0003\n\u0005\u0000"+
		"yz\u0005J\u0000\u0000z{\u0005\\\u0000\u0000{|\u0005#\u0000\u0000|\u00fa"+
		"\u0001\u0000\u0000\u0000}\u00fa\u0003\u0002\u0001\u0000~\u00fa\u0005L"+
		"\u0000\u0000\u007f\u0080\u0005\'\u0000\u0000\u0080\u0081\u0005\"\u0000"+
		"\u0000\u0081\u0082\u0003\n\u0005\u0000\u0082\u0083\u0005#\u0000\u0000"+
		"\u0083\u0084\u0003\n\u0005\u0000\u0084\u0085\u0005(\u0000\u0000\u0085"+
		"\u0086\u0003\n\u0005\u0016\u0086\u00fa\u0001\u0000\u0000\u0000\u0087\u0088"+
		"\u0005L\u0000\u0000\u0088\u0089\u0005\"\u0000\u0000\u0089\u00fa\u0005"+
		"#\u0000\u0000\u008a\u008b\u0005L\u0000\u0000\u008b\u008c\u0005\"\u0000"+
		"\u0000\u008c\u0091\u0003\n\u0005\u0000\u008d\u008e\u00051\u0000\u0000"+
		"\u008e\u0090\u0003\n\u0005\u0000\u008f\u008d\u0001\u0000\u0000\u0000\u0090"+
		"\u0093\u0001\u0000\u0000\u0000\u0091\u008f\u0001\u0000\u0000\u0000\u0091"+
		"\u0092\u0001\u0000\u0000\u0000\u0092\u0094\u0001\u0000\u0000\u0000\u0093"+
		"\u0091\u0001\u0000\u0000\u0000\u0094\u0095\u0005#\u0000\u0000\u0095\u00fa"+
		"\u0001\u0000\u0000\u0000\u0096\u0097\u0005L\u0000\u0000\u0097\u0098\u0005"+
		"\"\u0000\u0000\u0098\u0099\u0005[\u0000\u0000\u0099\u009f\u0003\n\u0005"+
		"\u0000\u009a\u009b\u00051\u0000\u0000\u009b\u009c\u0005J\u0000\u0000\u009c"+
		"\u009e\u0003\n\u0005\u0000\u009d\u009a\u0001\u0000\u0000\u0000\u009e\u00a1"+
		"\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u009f\u00a0"+
		"\u0001\u0000\u0000\u0000\u00a0\u00a2\u0001\u0000\u0000\u0000\u00a1\u009f"+
		"\u0001\u0000\u0000\u0000\u00a2\u00a3\u0005J\u0000\u0000\u00a3\u00a4\u0005"+
		"\\\u0000\u0000\u00a4\u00a5\u0005#\u0000\u0000\u00a5\u00fa\u0001\u0000"+
		"\u0000\u0000\u00a6\u00a8\u0005L\u0000\u0000\u00a7\u00a6\u0001\u0000\u0000"+
		"\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8\u00a9\u0001\u0000\u0000"+
		"\u0000\u00a9\u00aa\u00052\u0000\u0000\u00aa\u00ab\u0005L\u0000\u0000\u00ab"+
		"\u00ac\u00053\u0000\u0000\u00ac\u00b3\u0003\n\u0005\u0000\u00ad\u00ae"+
		"\u00051\u0000\u0000\u00ae\u00af\u0005L\u0000\u0000\u00af\u00b0\u00053"+
		"\u0000\u0000\u00b0\u00b2\u0003\n\u0005\u0000\u00b1\u00ad\u0001\u0000\u0000"+
		"\u0000\u00b2\u00b5\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000"+
		"\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000\u00b4\u00b6\u0001\u0000\u0000"+
		"\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000\u00b6\u00b7\u00054\u0000\u0000"+
		"\u00b7\u00fa\u0001\u0000\u0000\u0000\u00b8\u00ba\u0005L\u0000\u0000\u00b9"+
		"\u00b8\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000\u00ba"+
		"\u00bb\u0001\u0000\u0000\u0000\u00bb\u00bc\u00052\u0000\u0000\u00bc\u00bd"+
		"\u0005[\u0000\u0000\u00bd\u00be\u0005L\u0000\u0000\u00be\u00bf\u00053"+
		"\u0000\u0000\u00bf\u00c7\u0003\n\u0005\u0000\u00c0\u00c1\u00051\u0000"+
		"\u0000\u00c1\u00c2\u0005J\u0000\u0000\u00c2\u00c3\u0005L\u0000\u0000\u00c3"+
		"\u00c4\u00053\u0000\u0000\u00c4\u00c6\u0003\n\u0005\u0000\u00c5\u00c0"+
		"\u0001\u0000\u0000\u0000\u00c6\u00c9\u0001\u0000\u0000\u0000\u00c7\u00c5"+
		"\u0001\u0000\u0000\u0000\u00c7\u00c8\u0001\u0000\u0000\u0000\u00c8\u00ca"+
		"\u0001\u0000\u0000\u0000\u00c9\u00c7\u0001\u0000\u0000\u0000\u00ca\u00cb"+
		"\u0005J\u0000\u0000\u00cb\u00cc\u0005\\\u0000\u0000\u00cc\u00cd\u0005"+
		"4\u0000\u0000\u00cd\u00fa\u0001\u0000\u0000\u0000\u00ce\u00d0\u0005L\u0000"+
		"\u0000\u00cf\u00ce\u0001\u0000\u0000\u0000\u00cf\u00d0\u0001\u0000\u0000"+
		"\u0000\u00d0\u00d1\u0001\u0000\u0000\u0000\u00d1\u00d2\u00052\u0000\u0000"+
		"\u00d2\u00d7\u0003\n\u0005\u0000\u00d3\u00d4\u00051\u0000\u0000\u00d4"+
		"\u00d6\u0003\n\u0005\u0000\u00d5\u00d3\u0001\u0000\u0000\u0000\u00d6\u00d9"+
		"\u0001\u0000\u0000\u0000\u00d7\u00d5\u0001\u0000\u0000\u0000\u00d7\u00d8"+
		"\u0001\u0000\u0000\u0000\u00d8\u00da\u0001\u0000\u0000\u0000\u00d9\u00d7"+
		"\u0001\u0000\u0000\u0000\u00da\u00db\u00054\u0000\u0000\u00db\u00fa\u0001"+
		"\u0000\u0000\u0000\u00dc\u00de\u0005L\u0000\u0000\u00dd\u00dc\u0001\u0000"+
		"\u0000\u0000\u00dd\u00de\u0001\u0000\u0000\u0000\u00de\u00df\u0001\u0000"+
		"\u0000\u0000\u00df\u00e0\u00052\u0000\u0000\u00e0\u00e1\u0005[\u0000\u0000"+
		"\u00e1\u00e7\u0003\n\u0005\u0000\u00e2\u00e3\u00051\u0000\u0000\u00e3"+
		"\u00e4\u0005J\u0000\u0000\u00e4\u00e6\u0003\n\u0005\u0000\u00e5\u00e2"+
		"\u0001\u0000\u0000\u0000\u00e6\u00e9\u0001\u0000\u0000\u0000\u00e7\u00e5"+
		"\u0001\u0000\u0000\u0000\u00e7\u00e8\u0001\u0000\u0000\u0000\u00e8\u00ea"+
		"\u0001\u0000\u0000\u0000\u00e9\u00e7\u0001\u0000\u0000\u0000\u00ea\u00eb"+
		"\u0005J\u0000\u0000\u00eb\u00ec\u0005\\\u0000\u0000\u00ec\u00ed\u0005"+
		"4\u0000\u0000\u00ed\u00fa\u0001\u0000\u0000\u0000\u00ee\u00ef\u0005\""+
		"\u0000\u0000\u00ef\u00f0\u0003\n\u0005\u0000\u00f0\u00f1\u0005#\u0000"+
		"\u0000\u00f1\u00fa\u0001\u0000\u0000\u0000\u00f2\u00f3\u0005\"\u0000\u0000"+
		"\u00f3\u00f4\u0005[\u0000\u0000\u00f4\u00f5\u0003\n\u0005\u0000\u00f5"+
		"\u00f6\u0005J\u0000\u0000\u00f6\u00f7\u0005\\\u0000\u0000\u00f7\u00f8"+
		"\u0005#\u0000\u0000\u00f8\u00fa\u0001\u0000\u0000\u0000\u00f96\u0001\u0000"+
		"\u0000\u0000\u00f98\u0001\u0000\u0000\u0000\u00f9;\u0001\u0000\u0000\u0000"+
		"\u00f9@\u0001\u0000\u0000\u0000\u00f9H\u0001\u0000\u0000\u0000\u00f9M"+
		"\u0001\u0000\u0000\u0000\u00f9U\u0001\u0000\u0000\u0000\u00f9]\u0001\u0000"+
		"\u0000\u0000\u00f9e\u0001\u0000\u0000\u0000\u00f9j\u0001\u0000\u0000\u0000"+
		"\u00f9r\u0001\u0000\u0000\u0000\u00f9v\u0001\u0000\u0000\u0000\u00f9}"+
		"\u0001\u0000\u0000\u0000\u00f9~\u0001\u0000\u0000\u0000\u00f9\u007f\u0001"+
		"\u0000\u0000\u0000\u00f9\u0087\u0001\u0000\u0000\u0000\u00f9\u008a\u0001"+
		"\u0000\u0000\u0000\u00f9\u0096\u0001\u0000\u0000\u0000\u00f9\u00a7\u0001"+
		"\u0000\u0000\u0000\u00f9\u00b9\u0001\u0000\u0000\u0000\u00f9\u00cf\u0001"+
		"\u0000\u0000\u0000\u00f9\u00dd\u0001\u0000\u0000\u0000\u00f9\u00ee\u0001"+
		"\u0000\u0000\u0000\u00f9\u00f2\u0001\u0000\u0000\u0000\u00fa\u0148\u0001"+
		"\u0000\u0000\u0000\u00fb\u00fc\n$\u0000\u0000\u00fc\u00fd\u0003\b\u0004"+
		"\u0000\u00fd\u00fe\u0003\n\u0005%\u00fe\u0147\u0001\u0000\u0000\u0000"+
		"\u00ff\u0100\n\u0015\u0000\u0000\u0100\u0101\u0005)\u0000\u0000\u0101"+
		"\u0147\u0005L\u0000\u0000\u0102\u0103\n\u0014\u0000\u0000\u0103\u0104"+
		"\u0005*\u0000\u0000\u0104\u0147\u0005L\u0000\u0000\u0105\u0106\n\u0013"+
		"\u0000\u0000\u0106\u0107\u0005+\u0000\u0000\u0107\u0147\u0003\f\u0006"+
		"\u0000\u0108\u0109\n\u0012\u0000\u0000\u0109\u010a\u0005+\u0000\u0000"+
		"\u010a\u010b\u0005,\u0000\u0000\u010b\u0147\u0003\f\u0006\u0000\u010c"+
		"\u010d\n\u0011\u0000\u0000\u010d\u0147\u0005\u0002\u0000\u0000\u010e\u010f"+
		"\n\u0010\u0000\u0000\u010f\u0110\u0005-\u0000\u0000\u0110\u0147\u0005"+
		"L\u0000\u0000\u0111\u0112\n\u000f\u0000\u0000\u0112\u0113\u0005.\u0000"+
		"\u0000\u0113\u0147\u0005L\u0000\u0000\u0114\u0115\n\u000e\u0000\u0000"+
		"\u0115\u0116\u0005/\u0000\u0000\u0116\u0117\u0003\n\u0005\u0000\u0117"+
		"\u0118\u00050\u0000\u0000\u0118\u0147\u0001\u0000\u0000\u0000\u0119\u011a"+
		"\n\r\u0000\u0000\u011a\u011b\u0005/\u0000\u0000\u011b\u011c\u0005[\u0000"+
		"\u0000\u011c\u011d\u0003\n\u0005\u0000\u011d\u011e\u0005J\u0000\u0000"+
		"\u011e\u011f\u0005\\\u0000\u0000\u011f\u0120\u00050\u0000\u0000\u0120"+
		"\u0147\u0001\u0000\u0000\u0000\u0121\u0122\n\t\u0000\u0000\u0122\u0123"+
		"\u0005-\u0000\u0000\u0123\u0124\u0005L\u0000\u0000\u0124\u0125\u0005\""+
		"\u0000\u0000\u0125\u0147\u0005#\u0000\u0000\u0126\u0127\n\b\u0000\u0000"+
		"\u0127\u0128\u0005-\u0000\u0000\u0128\u0129\u0005L\u0000\u0000\u0129\u012a"+
		"\u0005\"\u0000\u0000\u012a\u012f\u0003\n\u0005\u0000\u012b\u012c\u0005"+
		"1\u0000\u0000\u012c\u012e\u0003\n\u0005\u0000\u012d\u012b\u0001\u0000"+
		"\u0000\u0000\u012e\u0131\u0001\u0000\u0000\u0000\u012f\u012d\u0001\u0000"+
		"\u0000\u0000\u012f\u0130\u0001\u0000\u0000\u0000\u0130\u0132\u0001\u0000"+
		"\u0000\u0000\u0131\u012f\u0001\u0000\u0000\u0000\u0132\u0133\u0005#\u0000"+
		"\u0000\u0133\u0147\u0001\u0000\u0000\u0000\u0134\u0135\n\u0007\u0000\u0000"+
		"\u0135\u0136\u0005-\u0000\u0000\u0136\u0137\u0005L\u0000\u0000\u0137\u0138"+
		"\u0005\"\u0000\u0000\u0138\u0139\u0005[\u0000\u0000\u0139\u013f\u0003"+
		"\n\u0005\u0000\u013a\u013b\u00051\u0000\u0000\u013b\u013c\u0005J\u0000"+
		"\u0000\u013c\u013e\u0003\n\u0005\u0000\u013d\u013a\u0001\u0000\u0000\u0000"+
		"\u013e\u0141\u0001\u0000\u0000\u0000\u013f\u013d\u0001\u0000\u0000\u0000"+
		"\u013f\u0140\u0001\u0000\u0000\u0000\u0140\u0142\u0001\u0000\u0000\u0000"+
		"\u0141\u013f\u0001\u0000\u0000\u0000\u0142\u0143\u0005J\u0000\u0000\u0143"+
		"\u0144\u0005\\\u0000\u0000\u0144\u0145\u0005#\u0000\u0000\u0145\u0147"+
		"\u0001\u0000\u0000\u0000\u0146\u00fb\u0001\u0000\u0000\u0000\u0146\u00ff"+
		"\u0001\u0000\u0000\u0000\u0146\u0102\u0001\u0000\u0000\u0000\u0146\u0105"+
		"\u0001\u0000\u0000\u0000\u0146\u0108\u0001\u0000\u0000\u0000\u0146\u010c"+
		"\u0001\u0000\u0000\u0000\u0146\u010e\u0001\u0000\u0000\u0000\u0146\u0111"+
		"\u0001\u0000\u0000\u0000\u0146\u0114\u0001\u0000\u0000\u0000\u0146\u0119"+
		"\u0001\u0000\u0000\u0000\u0146\u0121\u0001\u0000\u0000\u0000\u0146\u0126"+
		"\u0001\u0000\u0000\u0000\u0146\u0134\u0001\u0000\u0000\u0000\u0147\u014a"+
		"\u0001\u0000\u0000\u0000\u0148\u0146\u0001\u0000\u0000\u0000\u0148\u0149"+
		"\u0001\u0000\u0000\u0000\u0149\u000b\u0001\u0000\u0000\u0000\u014a\u0148"+
		"\u0001\u0000\u0000\u0000\u014b\u014c\u0006\u0006\uffff\uffff\u0000\u014c"+
		"\u0155\u0005L\u0000\u0000\u014d\u014e\u0005\u0006\u0000\u0000\u014e\u0155"+
		"\u0003\f\u0006\u0006\u014f\u0150\u0005\"\u0000\u0000\u0150\u0151\u0003"+
		"\f\u0006\u0000\u0151\u0152\u0005#\u0000\u0000\u0152\u0155\u0001\u0000"+
		"\u0000\u0000\u0153\u0155\u0005M\u0000\u0000\u0154\u014b\u0001\u0000\u0000"+
		"\u0000\u0154\u014d\u0001\u0000\u0000\u0000\u0154\u014f\u0001\u0000\u0000"+
		"\u0000\u0154\u0153\u0001\u0000\u0000\u0000\u0155\u016e\u0001\u0000\u0000"+
		"\u0000\u0156\u0157\n\u0005\u0000\u0000\u0157\u016d\u00055\u0000\u0000"+
		"\u0158\u0159\n\u0004\u0000\u0000\u0159\u015a\u0005\u001e\u0000\u0000\u015a"+
		"\u015f\u0003\f\u0006\u0000\u015b\u015c\u0005\u001e\u0000\u0000\u015c\u015e"+
		"\u0003\f\u0006\u0000\u015d\u015b\u0001\u0000\u0000\u0000\u015e\u0161\u0001"+
		"\u0000\u0000\u0000\u015f\u015d\u0001\u0000\u0000\u0000\u015f\u0160\u0001"+
		"\u0000\u0000\u0000\u0160\u016d\u0001\u0000\u0000\u0000\u0161\u015f\u0001"+
		"\u0000\u0000\u0000\u0162\u0163\n\u0003\u0000\u0000\u0163\u0164\u0005\u001d"+
		"\u0000\u0000\u0164\u0169\u0003\f\u0006\u0000\u0165\u0166\u0005\u001d\u0000"+
		"\u0000\u0166\u0168\u0003\f\u0006\u0000\u0167\u0165\u0001\u0000\u0000\u0000"+
		"\u0168\u016b\u0001\u0000\u0000\u0000\u0169\u0167\u0001\u0000\u0000\u0000"+
		"\u0169\u016a\u0001\u0000\u0000\u0000\u016a\u016d\u0001\u0000\u0000\u0000"+
		"\u016b\u0169\u0001\u0000\u0000\u0000\u016c\u0156\u0001\u0000\u0000\u0000"+
		"\u016c\u0158\u0001\u0000\u0000\u0000\u016c\u0162\u0001\u0000\u0000\u0000"+
		"\u016d\u0170\u0001\u0000\u0000\u0000\u016e\u016c\u0001\u0000\u0000\u0000"+
		"\u016e\u016f\u0001\u0000\u0000\u0000\u016f\r\u0001\u0000\u0000\u0000\u0170"+
		"\u016e\u0001\u0000\u0000\u0000\u0171\u0172\u00056\u0000\u0000\u0172\u000f"+
		"\u0001\u0000\u0000\u0000\u0173\u0174\u0005L\u0000\u0000\u0174\u0175\u0005"+
		"7\u0000\u0000\u0175\u0180\u0003\f\u0006\u0000\u0176\u0177\u0005L\u0000"+
		"\u0000\u0177\u0178\u00057\u0000\u0000\u0178\u0179\u0003\f\u0006\u0000"+
		"\u0179\u017a\u00053\u0000\u0000\u017a\u017b\u0003\n\u0005\u0000\u017b"+
		"\u0180\u0001\u0000\u0000\u0000\u017c\u017d\u0005L\u0000\u0000\u017d\u017e"+
		"\u00053\u0000\u0000\u017e\u0180\u0003\n\u0005\u0000\u017f\u0173\u0001"+
		"\u0000\u0000\u0000\u017f\u0176\u0001\u0000\u0000\u0000\u017f\u017c\u0001"+
		"\u0000\u0000\u0000\u0180\u0011\u0001\u0000\u0000\u0000\u0181\u0182\u0005"+
		"8\u0000\u0000\u0182\u0183\u0005L\u0000\u0000\u0183\u0184\u00053\u0000"+
		"\u0000\u0184\u0185\u0003\n\u0005\u0000\u0185\u0186\u0005J\u0000\u0000"+
		"\u0186\u0240\u0001\u0000\u0000\u0000\u0187\u0188\u00059\u0000\u0000\u0188"+
		"\u0189\u0005L\u0000\u0000\u0189\u018a\u00053\u0000\u0000\u018a\u018b\u0003"+
		"\n\u0005\u0000\u018b\u018c\u0005J\u0000\u0000\u018c\u0240\u0001\u0000"+
		"\u0000\u0000\u018d\u018e\u00058\u0000\u0000\u018e\u0191\u0005L\u0000\u0000"+
		"\u018f\u0190\u00057\u0000\u0000\u0190\u0192\u0003\f\u0006\u0000\u0191"+
		"\u018f\u0001\u0000\u0000\u0000\u0191\u0192\u0001\u0000\u0000\u0000\u0192"+
		"\u0193\u0001\u0000\u0000\u0000\u0193\u0194\u00053\u0000\u0000\u0194\u0195"+
		"\u0003\n\u0005\u0000\u0195\u0196\u0005J\u0000\u0000\u0196\u0240\u0001"+
		"\u0000\u0000\u0000\u0197\u0198\u00059\u0000\u0000\u0198\u019b\u0005L\u0000"+
		"\u0000\u0199\u019a\u00057\u0000\u0000\u019a\u019c\u0003\f\u0006\u0000"+
		"\u019b\u0199\u0001\u0000\u0000\u0000\u019b\u019c\u0001\u0000\u0000\u0000"+
		"\u019c\u019d\u0001\u0000\u0000\u0000\u019d\u019e\u00053\u0000\u0000\u019e"+
		"\u019f\u0003\n\u0005\u0000\u019f\u01a0\u0005J\u0000\u0000\u01a0\u0240"+
		"\u0001\u0000\u0000\u0000\u01a1\u01a2\u0005L\u0000\u0000\u01a2\u01a3\u0005"+
		"3\u0000\u0000\u01a3\u01a4\u0003\n\u0005\u0000\u01a4\u01a5\u0005J\u0000"+
		"\u0000\u01a5\u0240\u0001\u0000\u0000\u0000\u01a6\u01a7\u0005:\u0000\u0000"+
		"\u01a7\u0240\u0005J\u0000\u0000\u01a8\u01a9\u0005;\u0000\u0000\u01a9\u0240"+
		"\u0005J\u0000\u0000\u01aa\u01ab\u0005<\u0000\u0000\u01ab\u0240\u0005J"+
		"\u0000\u0000\u01ac\u0240\u0005=\u0000\u0000\u01ad\u01ae\u0005>\u0000\u0000"+
		"\u01ae\u0240\u0003\u0012\t\u0000\u01af\u01b1\u0005\'\u0000\u0000\u01b0"+
		"\u01b2\u0005\"\u0000\u0000\u01b1\u01b0\u0001\u0000\u0000\u0000\u01b1\u01b2"+
		"\u0001\u0000\u0000\u0000\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3\u01b5"+
		"\u0003\n\u0005\u0000\u01b4\u01b6\u0005#\u0000\u0000\u01b5\u01b4\u0001"+
		"\u0000\u0000\u0000\u01b5\u01b6\u0001\u0000\u0000\u0000\u01b6\u01b7\u0001"+
		"\u0000\u0000\u0000\u01b7\u01b8\u0005?\u0000\u0000\u01b8\u01c7\u0003\u0004"+
		"\u0002\u0000\u01b9\u01ba\u0005(\u0000\u0000\u01ba\u01bc\u0005\'\u0000"+
		"\u0000\u01bb\u01bd\u0005\"\u0000\u0000\u01bc\u01bb\u0001\u0000\u0000\u0000"+
		"\u01bc\u01bd\u0001\u0000\u0000\u0000\u01bd\u01be\u0001\u0000\u0000\u0000"+
		"\u01be\u01c0\u0003\n\u0005\u0000\u01bf\u01c1\u0005#\u0000\u0000\u01c0"+
		"\u01bf\u0001\u0000\u0000\u0000\u01c0\u01c1\u0001\u0000\u0000\u0000\u01c1"+
		"\u01c2\u0001\u0000\u0000\u0000\u01c2\u01c3\u0005?\u0000\u0000\u01c3\u01c4"+
		"\u0003\u0004\u0002\u0000\u01c4\u01c6\u0001\u0000\u0000\u0000\u01c5\u01b9"+
		"\u0001\u0000\u0000\u0000\u01c6\u01c9\u0001\u0000\u0000\u0000\u01c7\u01c5"+
		"\u0001\u0000\u0000\u0000\u01c7\u01c8\u0001\u0000\u0000\u0000\u01c8\u01cc"+
		"\u0001\u0000\u0000\u0000\u01c9\u01c7\u0001\u0000\u0000\u0000\u01ca\u01cb"+
		"\u0005(\u0000\u0000\u01cb\u01cd\u0003\u0004\u0002\u0000\u01cc\u01ca\u0001"+
		"\u0000\u0000\u0000\u01cc\u01cd\u0001\u0000\u0000\u0000\u01cd\u0240\u0001"+
		"\u0000\u0000\u0000\u01ce\u01d0\u0005@\u0000\u0000\u01cf\u01d1\u0005\""+
		"\u0000\u0000\u01d0\u01cf\u0001\u0000\u0000\u0000\u01d0\u01d1\u0001\u0000"+
		"\u0000\u0000\u01d1\u01d2\u0001\u0000\u0000\u0000\u01d2\u01d4\u0003\n\u0005"+
		"\u0000\u01d3\u01d5\u0005#\u0000\u0000\u01d4\u01d3\u0001\u0000\u0000\u0000"+
		"\u01d4\u01d5\u0001\u0000\u0000\u0000\u01d5\u01d6\u0001\u0000\u0000\u0000"+
		"\u01d6\u01d7\u0005?\u0000\u0000\u01d7\u01da\u0003\u0004\u0002\u0000\u01d8"+
		"\u01d9\u0005(\u0000\u0000\u01d9\u01db\u0003\u0004\u0002\u0000\u01da\u01d8"+
		"\u0001\u0000\u0000\u0000\u01da\u01db\u0001\u0000\u0000\u0000\u01db\u0240"+
		"\u0001\u0000\u0000\u0000\u01dc\u01de\u0005A\u0000\u0000\u01dd\u01df\u0005"+
		"\"\u0000\u0000\u01de\u01dd\u0001\u0000\u0000\u0000\u01de\u01df\u0001\u0000"+
		"\u0000\u0000\u01df\u01e0\u0001\u0000\u0000\u0000\u01e0\u01e1\u0005L\u0000"+
		"\u0000\u01e1\u01e2\u0005B\u0000\u0000\u01e2\u01e5\u0003\n\u0005\u0000"+
		"\u01e3\u01e4\u0005C\u0000\u0000\u01e4\u01e6\u0003\n\u0005\u0000\u01e5"+
		"\u01e3\u0001\u0000\u0000\u0000\u01e5\u01e6\u0001\u0000\u0000\u0000\u01e6"+
		"\u01e8\u0001\u0000\u0000\u0000\u01e7\u01e9\u0005#\u0000\u0000\u01e8\u01e7"+
		"\u0001\u0000\u0000\u0000\u01e8\u01e9\u0001\u0000\u0000\u0000\u01e9\u01ea"+
		"\u0001\u0000\u0000\u0000\u01ea\u01eb\u0005?\u0000\u0000\u01eb\u01ee\u0003"+
		"\u0004\u0002\u0000\u01ec\u01ed\u0005(\u0000\u0000\u01ed\u01ef\u0003\u0004"+
		"\u0002\u0000\u01ee\u01ec\u0001\u0000\u0000\u0000\u01ee\u01ef\u0001\u0000"+
		"\u0000\u0000\u01ef\u0240\u0001\u0000\u0000\u0000\u01f0\u01f1\u0005L\u0000"+
		"\u0000\u01f1\u01f2\u0005\"\u0000\u0000\u01f2\u01f3\u0005#\u0000\u0000"+
		"\u01f3\u0240\u0005J\u0000\u0000\u01f4\u01f5\u0005L\u0000\u0000\u01f5\u01f6"+
		"\u0005\"\u0000\u0000\u01f6\u01fb\u0003\n\u0005\u0000\u01f7\u01f8\u0005"+
		"1\u0000\u0000\u01f8\u01fa\u0003\n\u0005\u0000\u01f9\u01f7\u0001\u0000"+
		"\u0000\u0000\u01fa\u01fd\u0001\u0000\u0000\u0000\u01fb\u01f9\u0001\u0000"+
		"\u0000\u0000\u01fb\u01fc\u0001\u0000\u0000\u0000\u01fc\u01fe\u0001\u0000"+
		"\u0000\u0000\u01fd\u01fb\u0001\u0000\u0000\u0000\u01fe\u01ff\u0005#\u0000"+
		"\u0000\u01ff\u0200\u0005J\u0000\u0000\u0200\u0240\u0001\u0000\u0000\u0000"+
		"\u0201\u0202\u0005L\u0000\u0000\u0202\u0203\u0005\"\u0000\u0000\u0203"+
		"\u0204\u0005[\u0000\u0000\u0204\u020a\u0003\n\u0005\u0000\u0205\u0206"+
		"\u00051\u0000\u0000\u0206\u0207\u0005J\u0000\u0000\u0207\u0209\u0003\n"+
		"\u0005\u0000\u0208\u0205\u0001\u0000\u0000\u0000\u0209\u020c\u0001\u0000"+
		"\u0000\u0000\u020a\u0208\u0001\u0000\u0000\u0000\u020a\u020b\u0001\u0000"+
		"\u0000\u0000\u020b\u020d\u0001\u0000\u0000\u0000\u020c\u020a\u0001\u0000"+
		"\u0000\u0000\u020d\u020e\u0005J\u0000\u0000\u020e\u020f\u0005\\\u0000"+
		"\u0000\u020f\u0210\u0005#\u0000\u0000\u0210\u0211\u0005J\u0000\u0000\u0211"+
		"\u0240\u0001\u0000\u0000\u0000\u0212\u0213\u0003\n\u0005\u0000\u0213\u0214"+
		"\u0005-\u0000\u0000\u0214\u0215\u0005L\u0000\u0000\u0215\u0216\u0005\""+
		"\u0000\u0000\u0216\u0217\u0005#\u0000\u0000\u0217\u0218\u0005J\u0000\u0000"+
		"\u0218\u0240\u0001\u0000\u0000\u0000\u0219\u021a\u0003\n\u0005\u0000\u021a"+
		"\u021b\u0005-\u0000\u0000\u021b\u021c\u0005L\u0000\u0000\u021c\u021d\u0005"+
		"\"\u0000\u0000\u021d\u0222\u0003\n\u0005\u0000\u021e\u021f\u00051\u0000"+
		"\u0000\u021f\u0221\u0003\n\u0005\u0000\u0220\u021e\u0001\u0000\u0000\u0000"+
		"\u0221\u0224\u0001\u0000\u0000\u0000\u0222\u0220\u0001\u0000\u0000\u0000"+
		"\u0222\u0223\u0001\u0000\u0000\u0000\u0223\u0225\u0001\u0000\u0000\u0000"+
		"\u0224\u0222\u0001\u0000\u0000\u0000\u0225\u0226\u0005#\u0000\u0000\u0226"+
		"\u0227\u0005J\u0000\u0000\u0227\u0240\u0001\u0000\u0000\u0000\u0228\u0229"+
		"\u0003\n\u0005\u0000\u0229\u022a\u0005-\u0000\u0000\u022a\u022b\u0005"+
		"L\u0000\u0000\u022b\u022c\u0005\"\u0000\u0000\u022c\u022d\u0005[\u0000"+
		"\u0000\u022d\u0233\u0003\n\u0005\u0000\u022e\u022f\u00051\u0000\u0000"+
		"\u022f\u0230\u0005J\u0000\u0000\u0230\u0232\u0003\n\u0005\u0000\u0231"+
		"\u022e\u0001\u0000\u0000\u0000\u0232\u0235\u0001\u0000\u0000\u0000\u0233"+
		"\u0231\u0001\u0000\u0000\u0000\u0233\u0234\u0001\u0000\u0000\u0000\u0234"+
		"\u0236\u0001\u0000\u0000\u0000\u0235\u0233\u0001\u0000\u0000\u0000\u0236"+
		"\u0237\u0005J\u0000\u0000\u0237\u0238\u0005\\\u0000\u0000\u0238\u0239"+
		"\u0005#\u0000\u0000\u0239\u023a\u0005J\u0000\u0000\u023a\u0240\u0001\u0000"+
		"\u0000\u0000\u023b\u023c\u0005D\u0000\u0000\u023c\u023d\u0003\n\u0005"+
		"\u0000\u023d\u023e\u0005J\u0000\u0000\u023e\u0240\u0001\u0000\u0000\u0000"+
		"\u023f\u0181\u0001\u0000\u0000\u0000\u023f\u0187\u0001\u0000\u0000\u0000"+
		"\u023f\u018d\u0001\u0000\u0000\u0000\u023f\u0197\u0001\u0000\u0000\u0000"+
		"\u023f\u01a1\u0001\u0000\u0000\u0000\u023f\u01a6\u0001\u0000\u0000\u0000"+
		"\u023f\u01a8\u0001\u0000\u0000\u0000\u023f\u01aa\u0001\u0000\u0000\u0000"+
		"\u023f\u01ac\u0001\u0000\u0000\u0000\u023f\u01ad\u0001\u0000\u0000\u0000"+
		"\u023f\u01af\u0001\u0000\u0000\u0000\u023f\u01ce\u0001\u0000\u0000\u0000"+
		"\u023f\u01dc\u0001\u0000\u0000\u0000\u023f\u01f0\u0001\u0000\u0000\u0000"+
		"\u023f\u01f4\u0001\u0000\u0000\u0000\u023f\u0201\u0001\u0000\u0000\u0000"+
		"\u023f\u0212\u0001\u0000\u0000\u0000\u023f\u0219\u0001\u0000\u0000\u0000"+
		"\u023f\u0228\u0001\u0000\u0000\u0000\u023f\u023b\u0001\u0000\u0000\u0000"+
		"\u0240\u0013\u0001\u0000\u0000\u0000\u0241\u0242\u0005E\u0000\u0000\u0242"+
		"\u0245\u0005F\u0000\u0000\u0243\u0244\u0005U\u0000\u0000\u0244\u0246\u0005"+
		"+\u0000\u0000\u0245\u0243\u0001\u0000\u0000\u0000\u0245\u0246\u0001\u0000"+
		"\u0000\u0000\u0246\u0247\u0001\u0000\u0000\u0000\u0247\u0248\u0005L\u0000"+
		"\u0000\u0248\u024a\u0005\"\u0000\u0000\u0249\u024b\u0003\u0010\b\u0000"+
		"\u024a\u0249\u0001\u0000\u0000\u0000\u024a\u024b\u0001\u0000\u0000\u0000"+
		"\u024b\u0250\u0001\u0000\u0000\u0000\u024c\u024d\u00051\u0000\u0000\u024d"+
		"\u024f\u0003\u0010\b\u0000\u024e\u024c\u0001\u0000\u0000\u0000\u024f\u0252"+
		"\u0001\u0000\u0000\u0000\u0250\u024e\u0001\u0000\u0000\u0000\u0250\u0251"+
		"\u0001\u0000\u0000\u0000\u0251\u0253\u0001\u0000\u0000\u0000\u0252\u0250"+
		"\u0001\u0000\u0000\u0000\u0253\u0256\u0005#\u0000\u0000\u0254\u0255\u0005"+
		"7\u0000\u0000\u0255\u0257\u0003\f\u0006\u0000\u0256\u0254\u0001\u0000"+
		"\u0000\u0000\u0256\u0257\u0001\u0000\u0000\u0000\u0257\u0258\u0001\u0000"+
		"\u0000\u0000\u0258\u0284\u0005J\u0000\u0000\u0259\u025a\u0005F\u0000\u0000"+
		"\u025a\u025b\u0005L\u0000\u0000\u025b\u025d\u0005\"\u0000\u0000\u025c"+
		"\u025e\u0003\u0010\b\u0000\u025d\u025c\u0001\u0000\u0000\u0000\u025d\u025e"+
		"\u0001\u0000\u0000\u0000\u025e\u0263\u0001\u0000\u0000\u0000\u025f\u0260"+
		"\u00051\u0000\u0000\u0260\u0262\u0003\u0010\b\u0000\u0261\u025f\u0001"+
		"\u0000\u0000\u0000\u0262\u0265\u0001\u0000\u0000\u0000\u0263\u0261\u0001"+
		"\u0000\u0000\u0000\u0263\u0264\u0001\u0000\u0000\u0000\u0264\u0266\u0001"+
		"\u0000\u0000\u0000\u0265\u0263\u0001\u0000\u0000\u0000\u0266\u0269\u0005"+
		"#\u0000\u0000\u0267\u0268\u00057\u0000\u0000\u0268\u026a\u0003\f\u0006"+
		"\u0000\u0269\u0267\u0001\u0000\u0000\u0000\u0269\u026a\u0001\u0000\u0000"+
		"\u0000\u026a\u026b\u0001\u0000\u0000\u0000\u026b\u026c\u0005?\u0000\u0000"+
		"\u026c\u0284\u0003\u0004\u0002\u0000\u026d\u026e\u0005F\u0000\u0000\u026e"+
		"\u026f\u0005L\u0000\u0000\u026f\u0271\u0005\"\u0000\u0000\u0270\u0272"+
		"\u0003\u0010\b\u0000\u0271\u0270\u0001\u0000\u0000\u0000\u0271\u0272\u0001"+
		"\u0000\u0000\u0000\u0272\u0277\u0001\u0000\u0000\u0000\u0273\u0274\u0005"+
		"1\u0000\u0000\u0274\u0276\u0003\u0010\b\u0000\u0275\u0273\u0001\u0000"+
		"\u0000\u0000\u0276\u0279\u0001\u0000\u0000\u0000\u0277\u0275\u0001\u0000"+
		"\u0000\u0000\u0277\u0278\u0001\u0000\u0000\u0000\u0278\u027a\u0001\u0000"+
		"\u0000\u0000\u0279\u0277\u0001\u0000\u0000\u0000\u027a\u027d\u0005#\u0000"+
		"\u0000\u027b\u027c\u00057\u0000\u0000\u027c\u027e\u0003\f\u0006\u0000"+
		"\u027d\u027b\u0001\u0000\u0000\u0000\u027d\u027e\u0001\u0000\u0000\u0000"+
		"\u027e\u027f\u0001\u0000\u0000\u0000\u027f\u0280\u00053\u0000\u0000\u0280"+
		"\u0281\u0003\n\u0005\u0000\u0281\u0282\u0005J\u0000\u0000\u0282\u0284"+
		"\u0001\u0000\u0000\u0000\u0283\u0241\u0001\u0000\u0000\u0000\u0283\u0259"+
		"\u0001\u0000\u0000\u0000\u0283\u026d\u0001\u0000\u0000\u0000\u0284\u0015"+
		"\u0001\u0000\u0000\u0000\u0285\u0286\u00058\u0000\u0000\u0286\u0287\u0005"+
		"L\u0000\u0000\u0287\u0288\u00053\u0000\u0000\u0288\u0289\u0003\n\u0005"+
		"\u0000\u0289\u028a\u0005J\u0000\u0000\u028a\u0304\u0001\u0000\u0000\u0000"+
		"\u028b\u028c\u00059\u0000\u0000\u028c\u028d\u0005L\u0000\u0000\u028d\u028e"+
		"\u00053\u0000\u0000\u028e\u028f\u0003\n\u0005\u0000\u028f\u0290\u0005"+
		"J\u0000\u0000\u0290\u0304\u0001\u0000\u0000\u0000\u0291\u0292\u00058\u0000"+
		"\u0000\u0292\u0295\u0005L\u0000\u0000\u0293\u0294\u00057\u0000\u0000\u0294"+
		"\u0296\u0003\f\u0006\u0000\u0295\u0293\u0001\u0000\u0000\u0000\u0295\u0296"+
		"\u0001\u0000\u0000\u0000\u0296\u0297\u0001\u0000\u0000\u0000\u0297\u0298"+
		"\u00053\u0000\u0000\u0298\u0299\u0003\n\u0005\u0000\u0299\u029a\u0005"+
		"J\u0000\u0000\u029a\u0304\u0001\u0000\u0000\u0000\u029b\u029c\u00059\u0000"+
		"\u0000\u029c\u029f\u0005L\u0000\u0000\u029d\u029e\u00057\u0000\u0000\u029e"+
		"\u02a0\u0003\f\u0006\u0000\u029f\u029d\u0001\u0000\u0000\u0000\u029f\u02a0"+
		"\u0001\u0000\u0000\u0000\u02a0\u02a1\u0001\u0000\u0000\u0000\u02a1\u02a2"+
		"\u00053\u0000\u0000\u02a2\u02a3\u0003\n\u0005\u0000\u02a3\u02a4\u0005"+
		"J\u0000\u0000\u02a4\u0304\u0001\u0000\u0000\u0000\u02a5\u02a6\u0005E\u0000"+
		"\u0000\u02a6\u02a9\u00058\u0000\u0000\u02a7\u02a8\u0005U\u0000\u0000\u02a8"+
		"\u02aa\u0005+\u0000\u0000\u02a9\u02a7\u0001\u0000\u0000\u0000\u02a9\u02aa"+
		"\u0001\u0000\u0000\u0000\u02aa\u02ab\u0001\u0000\u0000\u0000\u02ab\u02ac"+
		"\u0005L\u0000\u0000\u02ac\u02ad\u00057\u0000\u0000\u02ad\u02ae\u0003\f"+
		"\u0006\u0000\u02ae\u02af\u0005J\u0000\u0000\u02af\u0304\u0001\u0000\u0000"+
		"\u0000\u02b0\u02b1\u0005E\u0000\u0000\u02b1\u02b4\u00059\u0000\u0000\u02b2"+
		"\u02b3\u0005U\u0000\u0000\u02b3\u02b5\u0005+\u0000\u0000\u02b4\u02b2\u0001"+
		"\u0000\u0000\u0000\u02b4\u02b5\u0001\u0000\u0000\u0000\u02b5\u02b6\u0001"+
		"\u0000\u0000\u0000\u02b6\u02b7\u0005L\u0000\u0000\u02b7\u02b8\u00057\u0000"+
		"\u0000\u02b8\u02b9\u0003\f\u0006\u0000\u02b9\u02ba\u0005J\u0000\u0000"+
		"\u02ba\u0304\u0001\u0000\u0000\u0000\u02bb\u02bc\u0005E\u0000\u0000\u02bc"+
		"\u02bd\u0005G\u0000\u0000\u02bd\u02be\u0005L\u0000\u0000\u02be\u02bf\u0005"+
		"3\u0000\u0000\u02bf\u02c0\u0003\f\u0006\u0000\u02c0\u02c1\u0005J\u0000"+
		"\u0000\u02c1\u0304\u0001\u0000\u0000\u0000\u02c2\u02c3\u0005H\u0000\u0000"+
		"\u02c3\u02c4\u0005L\u0000\u0000\u02c4\u02c5\u0005+\u0000\u0000\u02c5\u02cb"+
		"\u0005[\u0000\u0000\u02c6\u02c7\u0005L\u0000\u0000\u02c7\u02c8\u00057"+
		"\u0000\u0000\u02c8\u02c9\u0003\f\u0006\u0000\u02c9\u02ca\u0005J\u0000"+
		"\u0000\u02ca\u02cc\u0001\u0000\u0000\u0000\u02cb\u02c6\u0001\u0000\u0000"+
		"\u0000\u02cc\u02cd\u0001\u0000\u0000\u0000\u02cd\u02cb\u0001\u0000\u0000"+
		"\u0000\u02cd\u02ce\u0001\u0000\u0000\u0000\u02ce\u02cf\u0001\u0000\u0000"+
		"\u0000\u02cf\u02d0\u0005\\\u0000\u0000\u02d0\u0304\u0001\u0000\u0000\u0000"+
		"\u02d1\u02d2\u0005E\u0000\u0000\u02d2\u02d5\u0005H\u0000\u0000\u02d3\u02d4"+
		"\u0005U\u0000\u0000\u02d4\u02d6\u0005+\u0000\u0000\u02d5\u02d3\u0001\u0000"+
		"\u0000\u0000\u02d5\u02d6\u0001\u0000\u0000\u0000\u02d6\u02d7\u0001\u0000"+
		"\u0000\u0000\u02d7\u02d8\u0005L\u0000\u0000\u02d8\u02d9\u0005+\u0000\u0000"+
		"\u02d9\u02df\u0005[\u0000\u0000\u02da\u02db\u0005L\u0000\u0000\u02db\u02dc"+
		"\u00057\u0000\u0000\u02dc\u02dd\u0003\f\u0006\u0000\u02dd\u02de\u0005"+
		"J\u0000\u0000\u02de\u02e0\u0001\u0000\u0000\u0000\u02df\u02da\u0001\u0000"+
		"\u0000\u0000\u02e0\u02e1\u0001\u0000\u0000\u0000\u02e1\u02df\u0001\u0000"+
		"\u0000\u0000\u02e1\u02e2\u0001\u0000\u0000\u0000\u02e2\u02e3\u0001\u0000"+
		"\u0000\u0000\u02e3\u02e4\u0005\\\u0000\u0000\u02e4\u0304\u0001\u0000\u0000"+
		"\u0000\u02e5\u02e6\u0005E\u0000\u0000\u02e6\u02e7\u0005G\u0000\u0000\u02e7"+
		"\u02ea\u0005H\u0000\u0000\u02e8\u02e9\u0005U\u0000\u0000\u02e9\u02eb\u0005"+
		"+\u0000\u0000\u02ea\u02e8\u0001\u0000\u0000\u0000\u02ea\u02eb\u0001\u0000"+
		"\u0000\u0000\u02eb\u02ec\u0001\u0000\u0000\u0000\u02ec\u02ed\u0005L\u0000"+
		"\u0000\u02ed\u02ee\u0005+\u0000\u0000\u02ee\u02f4\u0005[\u0000\u0000\u02ef"+
		"\u02f0\u0005L\u0000\u0000\u02f0\u02f1\u00057\u0000\u0000\u02f1\u02f2\u0003"+
		"\f\u0006\u0000\u02f2\u02f3\u0005J\u0000\u0000\u02f3\u02f5\u0001\u0000"+
		"\u0000\u0000\u02f4\u02ef\u0001\u0000\u0000\u0000\u02f5\u02f6\u0001\u0000"+
		"\u0000\u0000\u02f6\u02f4\u0001\u0000\u0000\u0000\u02f6\u02f7\u0001\u0000"+
		"\u0000\u0000\u02f7\u02f8\u0001\u0000\u0000\u0000\u02f8\u02f9\u0005\\\u0000"+
		"\u0000\u02f9\u0304\u0001\u0000\u0000\u0000\u02fa\u02fb\u0005E\u0000\u0000"+
		"\u02fb\u02fc\u0005G\u0000\u0000\u02fc\u02ff\u0005H\u0000\u0000\u02fd\u02fe"+
		"\u0005U\u0000\u0000\u02fe\u0300\u0005+\u0000\u0000\u02ff\u02fd\u0001\u0000"+
		"\u0000\u0000\u02ff\u0300\u0001\u0000\u0000\u0000\u0300\u0301\u0001\u0000"+
		"\u0000\u0000\u0301\u0302\u0005L\u0000\u0000\u0302\u0304\u0005J\u0000\u0000"+
		"\u0303\u0285\u0001\u0000\u0000\u0000\u0303\u028b\u0001\u0000\u0000\u0000"+
		"\u0303\u0291\u0001\u0000\u0000\u0000\u0303\u029b\u0001\u0000\u0000\u0000"+
		"\u0303\u02a5\u0001\u0000\u0000\u0000\u0303\u02b0\u0001\u0000\u0000\u0000"+
		"\u0303\u02bb\u0001\u0000\u0000\u0000\u0303\u02c2\u0001\u0000\u0000\u0000"+
		"\u0303\u02d1\u0001\u0000\u0000\u0000\u0303\u02e5\u0001\u0000\u0000\u0000"+
		"\u0303\u02fa\u0001\u0000\u0000\u0000\u0304\u0017\u0001\u0000\u0000\u0000"+
		"\u0305\u0306\u0005I\u0000\u0000\u0306\u0307\u0005U\u0000\u0000\u0307\u0308"+
		"\u0005J\u0000\u0000\u0308\u0019\u0001\u0000\u0000\u0000\u0309\u030d\u0003"+
		"\u0014\n\u0000\u030a\u030d\u0003\u0016\u000b\u0000\u030b\u030d\u0003\u0018"+
		"\f\u0000\u030c\u0309\u0001\u0000\u0000\u0000\u030c\u030a\u0001\u0000\u0000"+
		"\u0000\u030c\u030b\u0001\u0000\u0000\u0000\u030d\u0310\u0001\u0000\u0000"+
		"\u0000\u030e\u030c\u0001\u0000\u0000\u0000\u030e\u030f\u0001\u0000\u0000"+
		"\u0000\u030f\u0311\u0001\u0000\u0000\u0000\u0310\u030e\u0001\u0000\u0000"+
		"\u0000\u0311\u0312\u0005\u0000\u0000\u0001\u0312\u001b\u0001\u0000\u0000"+
		"\u0000D!(.\u0091\u009f\u00a7\u00b3\u00b9\u00c7\u00cf\u00d7\u00dd\u00e7"+
		"\u00f9\u012f\u013f\u0146\u0148\u0154\u015f\u0169\u016c\u016e\u017f\u0191"+
		"\u019b\u01b1\u01b5\u01bc\u01c0\u01c7\u01cc\u01d0\u01d4\u01da\u01de\u01e5"+
		"\u01e8\u01ee\u01fb\u020a\u0222\u0233\u023f\u0245\u024a\u0250\u0256\u025d"+
		"\u0263\u0269\u0271\u0277\u027d\u0283\u0295\u029f\u02a9\u02b4\u02cd\u02d5"+
		"\u02e1\u02ea\u02f6\u02ff\u0303\u030c\u030e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}