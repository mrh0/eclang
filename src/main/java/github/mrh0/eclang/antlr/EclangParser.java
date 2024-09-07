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
		T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, NL=66, BOOL=67, 
		NAME=68, ATOM=69, INT=70, FLOAT=71, HEX=72, BIN=73, CHAR=74, STRING=75, 
		EMBEDED=76, WHITESPACE=77, EMPTYLINE=78, COMMENT=79, BLOCKCOMMENT=80, 
		INDENT=81, DEDENT=82;
	public static final int
		RULE_number = 0, RULE_primitive = 1, RULE_block = 2, RULE_unOp = 3, RULE_binOp = 4, 
		RULE_expr = 5, RULE_type = 6, RULE_record = 7, RULE_parameter = 8, RULE_orderExpression = 9, 
		RULE_statement = 10, RULE_use = 11, RULE_func = 12, RULE_program = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"number", "primitive", "block", "unOp", "binOp", "expr", "type", "record", 
			"parameter", "orderExpression", "statement", "use", "func", "program"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'not'", "'!'", "'!!'", "'~'", "'-'", "'+'", "'*'", "'/'", "'%'", 
			"'**'", "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&&'", "'and'", 
			"'||'", "'or'", "'^^'", "'xor'", "'==='", "'!=='", "'<<'", "'>>'", "'&'", 
			"'|'", "'^'", "'here'", "'('", "')'", "'if'", "'else'", "'is'", "'!is'", 
			"'as'", "'.'", "'['", "']'", "','", "'?'", "'=>'", "'rec'", "':'", "'='", 
			"'orderby'", "'orderasc'", "'orderdesc'", "'var'", "'val'", "'break'", 
			"'continue'", "'defer'", "'do'", "'while'", "'for'", "'in'", "'where'", 
			"'ret'", "'pool'", "'from'", "'use'", "'extern'", "'fn'"
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
			null, null, null, null, null, null, "NL", "BOOL", "NAME", "ATOM", "INT", 
			"FLOAT", "HEX", "BIN", "CHAR", "STRING", "EMBEDED", "WHITESPACE", "EMPTYLINE", 
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
			setState(32);
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
			case HEX:
				_localctx = new NumberHexContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(30);
				match(HEX);
				}
				break;
			case BIN:
				_localctx = new NumberBinContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(31);
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
			setState(39);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
			case FLOAT:
			case HEX:
			case BIN:
				_localctx = new PrimitiveNumberContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(34);
				number();
				}
				break;
			case BOOL:
				_localctx = new PrimitiveBoolContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(35);
				match(BOOL);
				}
				break;
			case STRING:
				_localctx = new PrimitiveStringContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(36);
				match(STRING);
				}
				break;
			case CHAR:
				_localctx = new PrimitiveCharContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(37);
				match(CHAR);
				}
				break;
			case ATOM:
				_localctx = new PrimitiveAtomContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(38);
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
			setState(41);
			match(INDENT);
			setState(43); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(42);
				((BlockContext)_localctx).statement = statement();
				((BlockContext)_localctx).statements.add(((BlockContext)_localctx).statement);
				}
				}
				setState(45); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 3709840204857606206L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 511L) != 0) );
			setState(47);
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
			setState(49);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 62L) != 0)) ) {
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
			setState(51);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1073741792L) != 0)) ) {
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
	public static class ExprNestContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
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
	public static class ExprAsContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
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
	public static class ExprTupleContext extends ExprContext {
		public List<ExprContext> values = new ArrayList<ExprContext>();
		public ExprContext expr;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ExprTupleContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterExprTuple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitExprTuple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitExprTuple(this);
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
			setState(87);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				_localctx = new ExprHereContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(54);
				match(T__29);
				}
				break;
			case 2:
				{
				_localctx = new ExprUnOpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(55);
				unOp();
				setState(56);
				expr(15);
				}
				break;
			case 3:
				{
				_localctx = new ExprNestContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(58);
				match(T__30);
				setState(59);
				expr(0);
				setState(60);
				match(T__31);
				}
				break;
			case 4:
				{
				_localctx = new ExprPrimitiveContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(62);
				primitive();
				}
				break;
			case 5:
				{
				_localctx = new ExprNamedContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(63);
				match(NAME);
				}
				break;
			case 6:
				{
				_localctx = new ExprInlineIfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(64);
				match(T__32);
				setState(65);
				match(T__30);
				setState(66);
				((ExprInlineIfContext)_localctx).condition = expr(0);
				setState(67);
				match(T__31);
				setState(68);
				((ExprInlineIfContext)_localctx).body = expr(0);
				setState(69);
				match(T__33);
				setState(70);
				((ExprInlineIfContext)_localctx).elseBody = expr(11);
				}
				break;
			case 7:
				{
				_localctx = new ExprFunctionCallNoArgsContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(72);
				match(NAME);
				setState(73);
				match(T__30);
				setState(74);
				match(T__31);
				}
				break;
			case 8:
				{
				_localctx = new ExprFunctionCallWithArgsContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(75);
				match(NAME);
				setState(76);
				match(T__30);
				setState(77);
				((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
				setState(82);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__40) {
					{
					{
					setState(78);
					match(T__40);
					setState(79);
					((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
					((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
					}
					}
					setState(84);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(85);
				match(T__31);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(141);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(139);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
					case 1:
						{
						_localctx = new ExprBinOpContext(new ExprContext(_parentctx, _parentState));
						((ExprBinOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(89);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(90);
						binOp();
						setState(91);
						((ExprBinOpContext)_localctx).right = expr(17);
						}
						break;
					case 2:
						{
						_localctx = new ExprIsContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(93);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(94);
						match(T__34);
						setState(95);
						match(NAME);
						}
						break;
					case 3:
						{
						_localctx = new ExprIsNotContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(96);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(97);
						match(T__35);
						setState(98);
						match(NAME);
						}
						break;
					case 4:
						{
						_localctx = new ExprAsContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(99);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(100);
						match(T__36);
						setState(101);
						match(NAME);
						}
						break;
					case 5:
						{
						_localctx = new ExprTupleContext(new ExprContext(_parentctx, _parentState));
						((ExprTupleContext)_localctx).values.add(_prevctx);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(102);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(103);
						match(T__26);
						setState(104);
						((ExprTupleContext)_localctx).expr = expr(0);
						((ExprTupleContext)_localctx).values.add(((ExprTupleContext)_localctx).expr);
						setState(109);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
						while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
							if ( _alt==1 ) {
								{
								{
								setState(105);
								match(T__26);
								setState(106);
								((ExprTupleContext)_localctx).expr = expr(0);
								((ExprTupleContext)_localctx).values.add(((ExprTupleContext)_localctx).expr);
								}
								} 
							}
							setState(111);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
						}
						}
						break;
					case 6:
						{
						_localctx = new ExprAccessNameContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(112);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(113);
						match(T__37);
						setState(114);
						match(NAME);
						}
						break;
					case 7:
						{
						_localctx = new ExprAccessorContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(115);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(116);
						match(T__38);
						setState(117);
						expr(0);
						setState(118);
						match(T__39);
						}
						break;
					case 8:
						{
						_localctx = new ExprFunctionCallNoArgsContext(new ExprContext(_parentctx, _parentState));
						((ExprFunctionCallNoArgsContext)_localctx).args.add(_prevctx);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(120);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(121);
						match(T__37);
						setState(122);
						match(NAME);
						setState(123);
						match(T__30);
						setState(124);
						match(T__31);
						}
						break;
					case 9:
						{
						_localctx = new ExprFunctionCallWithArgsContext(new ExprContext(_parentctx, _parentState));
						((ExprFunctionCallWithArgsContext)_localctx).args.add(_prevctx);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(125);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(126);
						match(T__37);
						setState(127);
						match(NAME);
						setState(128);
						match(T__30);
						setState(129);
						((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
						((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
						setState(134);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==T__40) {
							{
							{
							setState(130);
							match(T__40);
							setState(131);
							((ExprFunctionCallWithArgsContext)_localctx).expr = expr(0);
							((ExprFunctionCallWithArgsContext)_localctx).args.add(((ExprFunctionCallWithArgsContext)_localctx).expr);
							}
							}
							setState(136);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(137);
						match(T__31);
						}
						break;
					}
					} 
				}
				setState(143);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
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
	public static class TypeCallSignatureContext extends TypeContext {
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TypeCallSignatureContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterTypeCallSignature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitTypeCallSignature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitTypeCallSignature(this);
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
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				_localctx = new TypeByNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(145);
				match(NAME);
				}
				break;
			case 2:
				{
				_localctx = new TypeCallSignatureContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(146);
				match(T__30);
				setState(147);
				type(0);
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__40) {
					{
					{
					setState(148);
					match(T__40);
					setState(149);
					type(0);
					}
					}
					setState(154);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(155);
				match(T__31);
				setState(156);
				match(T__42);
				setState(157);
				type(3);
				}
				break;
			case 3:
				{
				_localctx = new TypeNestContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(159);
				match(T__30);
				setState(160);
				type(0);
				setState(161);
				match(T__31);
				}
				break;
			case 4:
				{
				_localctx = new TypeAtomContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(163);
				match(ATOM);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(190);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(188);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
					case 1:
						{
						_localctx = new TypeNullableContext(new TypeContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(166);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(167);
						match(T__41);
						}
						break;
					case 2:
						{
						_localctx = new TypeEnumContext(new TypeContext(_parentctx, _parentState));
						((TypeEnumContext)_localctx).types.add(_prevctx);
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(168);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(169);
						match(T__27);
						setState(170);
						((TypeEnumContext)_localctx).type = type(0);
						((TypeEnumContext)_localctx).types.add(((TypeEnumContext)_localctx).type);
						setState(175);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
						while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
							if ( _alt==1 ) {
								{
								{
								setState(171);
								match(T__27);
								setState(172);
								((TypeEnumContext)_localctx).type = type(0);
								((TypeEnumContext)_localctx).types.add(((TypeEnumContext)_localctx).type);
								}
								} 
							}
							setState(177);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
						}
						}
						break;
					case 3:
						{
						_localctx = new TypeUnionContext(new TypeContext(_parentctx, _parentState));
						((TypeUnionContext)_localctx).types.add(_prevctx);
						pushNewRecursionContext(_localctx, _startState, RULE_type);
						setState(178);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(179);
						match(T__26);
						setState(180);
						((TypeUnionContext)_localctx).type = type(0);
						((TypeUnionContext)_localctx).types.add(((TypeUnionContext)_localctx).type);
						setState(185);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
						while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
							if ( _alt==1 ) {
								{
								{
								setState(181);
								match(T__26);
								setState(182);
								((TypeUnionContext)_localctx).type = type(0);
								((TypeUnionContext)_localctx).types.add(((TypeUnionContext)_localctx).type);
								}
								} 
							}
							setState(187);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
						}
						}
						break;
					}
					} 
				}
				setState(192);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
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
	public static class RecordContext extends ParserRuleContext {
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
		public RecordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_record; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterRecord(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitRecord(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitRecord(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RecordContext record() throws RecognitionException {
		RecordContext _localctx = new RecordContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_record);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			match(T__43);
			setState(194);
			((RecordContext)_localctx).name = match(NAME);
			setState(195);
			match(T__36);
			setState(196);
			match(INDENT);
			setState(202); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(197);
				((RecordContext)_localctx).NAME = match(NAME);
				((RecordContext)_localctx).names.add(((RecordContext)_localctx).NAME);
				setState(198);
				match(T__44);
				setState(199);
				((RecordContext)_localctx).type = type(0);
				((RecordContext)_localctx).types.add(((RecordContext)_localctx).type);
				setState(200);
				match(NL);
				}
				}
				setState(204); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==NAME );
			setState(206);
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
			setState(220);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				_localctx = new ParameterTypedContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(208);
				match(NAME);
				setState(209);
				match(T__44);
				setState(210);
				type(0);
				}
				break;
			case 2:
				_localctx = new ParameterTypedDefaultContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(211);
				match(NAME);
				setState(212);
				match(T__44);
				setState(213);
				type(0);
				setState(214);
				match(T__45);
				setState(215);
				expr(0);
				}
				break;
			case 3:
				_localctx = new ParameterDefaultContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(217);
				match(NAME);
				setState(218);
				match(T__45);
				setState(219);
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
	public static class OrderExpressionContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public OrderExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterOrderExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitOrderExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitOrderExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderExpressionContext orderExpression() throws RecognitionException {
		OrderExpressionContext _localctx = new OrderExpressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_orderExpression);
		try {
			setState(226);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__46:
				enterOuterAlt(_localctx, 1);
				{
				setState(222);
				match(T__46);
				setState(223);
				expr(0);
				}
				break;
			case T__47:
				enterOuterAlt(_localctx, 2);
				{
				setState(224);
				match(T__47);
				}
				break;
			case T__48:
				enterOuterAlt(_localctx, 3);
				{
				setState(225);
				match(T__48);
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
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
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
	public static class StatementPoolLocalContext extends StatementContext {
		public BlockContext body;
		public List<TerminalNode> NAME() { return getTokens(EclangParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(EclangParser.NAME, i);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public StatementPoolLocalContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementPoolLocal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementPoolLocal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementPoolLocal(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StatementDefineRecordContext extends StatementContext {
		public List<TerminalNode> NAME() { return getTokens(EclangParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(EclangParser.NAME, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public StatementDefineRecordContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementDefineRecord(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementDefineRecord(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementDefineRecord(this);
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
		public OrderExpressionContext orderExpression() {
			return getRuleContext(OrderExpressionContext.class,0);
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
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
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
	public static class DeferedStatementContext extends StatementContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public DeferedStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterDeferedStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitDeferedStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitDeferedStatement(this);
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
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
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
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
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
	@SuppressWarnings("CheckReturnValue")
	public static class StatementDefineRecordConstContext extends StatementContext {
		public List<TerminalNode> NAME() { return getTokens(EclangParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(EclangParser.NAME, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public StatementDefineRecordConstContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterStatementDefineRecordConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitStatementDefineRecordConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitStatementDefineRecordConst(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_statement);
		int _la;
		try {
			int _alt;
			setState(422);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				_localctx = new StatementDefineContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(228);
				match(T__49);
				setState(229);
				match(NAME);
				setState(232);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__44) {
					{
					setState(230);
					match(T__44);
					setState(231);
					type(0);
					}
				}

				setState(234);
				match(T__45);
				setState(235);
				expr(0);
				setState(236);
				match(NL);
				}
				break;
			case 2:
				_localctx = new StatementDefineConstContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(238);
				match(T__50);
				setState(239);
				match(NAME);
				setState(242);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__44) {
					{
					setState(240);
					match(T__44);
					setState(241);
					type(0);
					}
				}

				setState(244);
				match(T__45);
				setState(245);
				expr(0);
				setState(246);
				match(NL);
				}
				break;
			case 3:
				_localctx = new StatementDefineRecordContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(248);
				match(T__49);
				setState(249);
				match(NAME);
				setState(250);
				match(T__45);
				setState(251);
				match(T__43);
				setState(252);
				match(NAME);
				setState(253);
				match(T__30);
				setState(259);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAME) {
					{
					{
					setState(254);
					match(NAME);
					setState(255);
					match(T__45);
					setState(256);
					expr(0);
					}
					}
					setState(261);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(262);
				match(T__31);
				}
				break;
			case 4:
				_localctx = new StatementDefineRecordConstContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(263);
				match(T__50);
				setState(264);
				match(NAME);
				setState(265);
				match(T__45);
				setState(266);
				match(T__43);
				setState(267);
				match(NAME);
				setState(268);
				match(T__30);
				setState(274);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NAME) {
					{
					{
					setState(269);
					match(NAME);
					setState(270);
					match(T__45);
					setState(271);
					expr(0);
					}
					}
					setState(276);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(277);
				match(T__31);
				}
				break;
			case 5:
				_localctx = new StatementAssignmentContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(278);
				match(NAME);
				setState(279);
				match(T__45);
				setState(280);
				expr(0);
				setState(281);
				match(NL);
				}
				break;
			case 6:
				_localctx = new StatementBreakContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(283);
				match(T__51);
				setState(284);
				match(NL);
				}
				break;
			case 7:
				_localctx = new StatementContinueContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(285);
				match(T__52);
				setState(286);
				match(NL);
				}
				break;
			case 8:
				_localctx = new StatementDeferContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(287);
				match(T__53);
				setState(288);
				statement();
				setState(289);
				match(NL);
				}
				break;
			case 9:
				_localctx = new StatementIfContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(291);
				match(T__32);
				setState(293);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
				case 1:
					{
					setState(292);
					match(T__30);
					}
					break;
				}
				setState(295);
				((StatementIfContext)_localctx).expr = expr(0);
				((StatementIfContext)_localctx).conditions.add(((StatementIfContext)_localctx).expr);
				setState(297);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__31) {
					{
					setState(296);
					match(T__31);
					}
				}

				setState(299);
				match(T__54);
				setState(300);
				((StatementIfContext)_localctx).block = block();
				((StatementIfContext)_localctx).bodies.add(((StatementIfContext)_localctx).block);
				setState(315);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(301);
						match(T__33);
						setState(302);
						match(T__32);
						setState(304);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
						case 1:
							{
							setState(303);
							match(T__30);
							}
							break;
						}
						setState(306);
						((StatementIfContext)_localctx).expr = expr(0);
						((StatementIfContext)_localctx).conditions.add(((StatementIfContext)_localctx).expr);
						setState(308);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==T__31) {
							{
							setState(307);
							match(T__31);
							}
						}

						setState(310);
						match(T__54);
						setState(311);
						((StatementIfContext)_localctx).block = block();
						((StatementIfContext)_localctx).bodies.add(((StatementIfContext)_localctx).block);
						}
						} 
					}
					setState(317);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				}
				setState(320);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__33) {
					{
					setState(318);
					match(T__33);
					setState(319);
					((StatementIfContext)_localctx).elseBody = block();
					}
				}

				}
				break;
			case 10:
				_localctx = new StatementWhileContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(322);
				match(T__55);
				setState(324);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
				case 1:
					{
					setState(323);
					match(T__30);
					}
					break;
				}
				setState(326);
				((StatementWhileContext)_localctx).condition = expr(0);
				setState(328);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__31) {
					{
					setState(327);
					match(T__31);
					}
				}

				setState(330);
				match(T__54);
				setState(331);
				((StatementWhileContext)_localctx).body = block();
				setState(334);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__33) {
					{
					setState(332);
					match(T__33);
					setState(333);
					((StatementWhileContext)_localctx).elseBody = block();
					}
				}

				}
				break;
			case 11:
				_localctx = new StatementForInContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(336);
				match(T__56);
				setState(338);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__30) {
					{
					setState(337);
					match(T__30);
					}
				}

				setState(340);
				match(NAME);
				setState(341);
				match(T__57);
				setState(342);
				expr(0);
				setState(345);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__58) {
					{
					setState(343);
					match(T__58);
					setState(344);
					expr(0);
					}
				}

				setState(348);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 985162418487296L) != 0)) {
					{
					setState(347);
					orderExpression();
					}
				}

				setState(351);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__31) {
					{
					setState(350);
					match(T__31);
					}
				}

				setState(353);
				match(T__54);
				setState(354);
				((StatementForInContext)_localctx).body = block();
				setState(357);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__33) {
					{
					setState(355);
					match(T__33);
					setState(356);
					((StatementForInContext)_localctx).elseBody = block();
					}
				}

				}
				break;
			case 12:
				_localctx = new StatementFunctionCallNoArgsContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(359);
				match(NAME);
				setState(360);
				match(T__30);
				setState(361);
				match(T__31);
				setState(362);
				match(NL);
				}
				break;
			case 13:
				_localctx = new StatementFunctionCallWithArgsContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(363);
				match(NAME);
				setState(365);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
				case 1:
					{
					setState(364);
					match(T__30);
					}
					break;
				}
				setState(367);
				((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
				setState(369);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__31) {
					{
					setState(368);
					match(T__31);
					}
				}

				setState(371);
				match(NL);
				}
				break;
			case 14:
				_localctx = new StatementFunctionCallWithArgsContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(373);
				match(NAME);
				setState(374);
				match(T__30);
				setState(375);
				((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
				setState(380);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__40) {
					{
					{
					setState(376);
					match(T__40);
					setState(377);
					((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
					((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
					}
					}
					setState(382);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(383);
				match(T__31);
				setState(384);
				match(NL);
				}
				break;
			case 15:
				_localctx = new StatementFunctionCallNoArgsContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(386);
				((StatementFunctionCallNoArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallNoArgsContext)_localctx).args.add(((StatementFunctionCallNoArgsContext)_localctx).expr);
				setState(387);
				match(T__37);
				setState(388);
				match(NAME);
				setState(389);
				match(T__30);
				setState(390);
				match(T__31);
				setState(391);
				match(NL);
				}
				break;
			case 16:
				_localctx = new StatementFunctionCallWithArgsContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(393);
				((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
				setState(394);
				match(T__37);
				setState(395);
				match(NAME);
				setState(396);
				match(T__30);
				setState(397);
				((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
				((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
				setState(402);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__40) {
					{
					{
					setState(398);
					match(T__40);
					setState(399);
					((StatementFunctionCallWithArgsContext)_localctx).expr = expr(0);
					((StatementFunctionCallWithArgsContext)_localctx).args.add(((StatementFunctionCallWithArgsContext)_localctx).expr);
					}
					}
					setState(404);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(405);
				match(T__31);
				setState(406);
				match(NL);
				}
				break;
			case 17:
				_localctx = new StatementReturnContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(408);
				match(T__59);
				setState(409);
				expr(0);
				setState(410);
				match(NL);
				}
				break;
			case 18:
				_localctx = new StatementPoolLocalContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(412);
				match(T__60);
				setState(413);
				match(NAME);
				setState(416);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__61) {
					{
					setState(414);
					match(T__61);
					setState(415);
					match(NAME);
					}
				}

				setState(418);
				match(T__57);
				setState(419);
				((StatementPoolLocalContext)_localctx).body = block();
				}
				break;
			case 19:
				_localctx = new DeferedStatementContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(420);
				match(T__53);
				setState(421);
				statement();
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
		public UseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_use; }
	 
		public UseContext() { }
		public void copyFrom(UseContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UseModuleContext extends UseContext {
		public Token from;
		public Token as;
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public UseModuleContext(UseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterUseModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitUseModule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitUseModule(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UseAllFromModuleContext extends UseContext {
		public Token from;
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public UseAllFromModuleContext(UseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterUseAllFromModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitUseAllFromModule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitUseAllFromModule(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UseFromModuleContext extends UseContext {
		public Token NAME;
		public List<Token> exports = new ArrayList<Token>();
		public Token from;
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public List<TerminalNode> NAME() { return getTokens(EclangParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(EclangParser.NAME, i);
		}
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public UseFromModuleContext(UseContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterUseFromModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitUseFromModule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitUseFromModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UseContext use() throws RecognitionException {
		UseContext _localctx = new UseContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_use);
		int _la;
		try {
			setState(448);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				_localctx = new UseModuleContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(424);
				match(T__62);
				setState(425);
				((UseModuleContext)_localctx).from = match(STRING);
				setState(428);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__36) {
					{
					setState(426);
					match(T__36);
					setState(427);
					((UseModuleContext)_localctx).as = match(NAME);
					}
				}

				setState(430);
				match(NL);
				}
				break;
			case 2:
				_localctx = new UseAllFromModuleContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(431);
				match(T__62);
				setState(432);
				match(T__6);
				setState(433);
				match(T__61);
				setState(434);
				((UseAllFromModuleContext)_localctx).from = match(STRING);
				setState(435);
				match(NL);
				}
				break;
			case 3:
				_localctx = new UseFromModuleContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(436);
				match(T__62);
				setState(437);
				((UseFromModuleContext)_localctx).NAME = match(NAME);
				((UseFromModuleContext)_localctx).exports.add(((UseFromModuleContext)_localctx).NAME);
				setState(442);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__40) {
					{
					{
					setState(438);
					match(T__40);
					setState(439);
					((UseFromModuleContext)_localctx).NAME = match(NAME);
					((UseFromModuleContext)_localctx).exports.add(((UseFromModuleContext)_localctx).NAME);
					}
					}
					setState(444);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(445);
				match(T__61);
				setState(446);
				((UseFromModuleContext)_localctx).from = match(STRING);
				setState(447);
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
	public static class FunctionExternalContext extends FuncContext {
		public Token externalName;
		public Token name;
		public ParameterContext parameter;
		public List<ParameterContext> params = new ArrayList<ParameterContext>();
		public TypeContext returnType;
		public TerminalNode NL() { return getToken(EclangParser.NL, 0); }
		public TerminalNode STRING() { return getToken(EclangParser.STRING, 0); }
		public TerminalNode NAME() { return getToken(EclangParser.NAME, 0); }
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public FunctionExternalContext(FuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).enterFunctionExternal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EclangListener ) ((EclangListener)listener).exitFunctionExternal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EclangVisitor ) return ((EclangVisitor<? extends T>)visitor).visitFunctionExternal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncContext func() throws RecognitionException {
		FuncContext _localctx = new FuncContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_func);
		int _la;
		try {
			setState(514);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				_localctx = new FunctionExternalContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(450);
				match(T__63);
				setState(451);
				match(T__64);
				setState(452);
				((FunctionExternalContext)_localctx).externalName = match(STRING);
				setState(453);
				match(T__36);
				setState(454);
				((FunctionExternalContext)_localctx).name = match(NAME);
				setState(455);
				match(T__30);
				setState(457);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(456);
					((FunctionExternalContext)_localctx).parameter = parameter();
					((FunctionExternalContext)_localctx).params.add(((FunctionExternalContext)_localctx).parameter);
					}
				}

				setState(463);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__40) {
					{
					{
					setState(459);
					match(T__40);
					setState(460);
					((FunctionExternalContext)_localctx).parameter = parameter();
					((FunctionExternalContext)_localctx).params.add(((FunctionExternalContext)_localctx).parameter);
					}
					}
					setState(465);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(466);
				match(T__31);
				setState(469);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__44) {
					{
					setState(467);
					match(T__44);
					setState(468);
					((FunctionExternalContext)_localctx).returnType = type(0);
					}
				}

				setState(471);
				match(NL);
				}
				break;
			case 2:
				_localctx = new FunctionBlockContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(472);
				match(T__64);
				setState(473);
				((FunctionBlockContext)_localctx).name = match(NAME);
				setState(474);
				match(T__30);
				setState(476);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(475);
					((FunctionBlockContext)_localctx).parameter = parameter();
					((FunctionBlockContext)_localctx).params.add(((FunctionBlockContext)_localctx).parameter);
					}
				}

				setState(482);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__40) {
					{
					{
					setState(478);
					match(T__40);
					setState(479);
					((FunctionBlockContext)_localctx).parameter = parameter();
					((FunctionBlockContext)_localctx).params.add(((FunctionBlockContext)_localctx).parameter);
					}
					}
					setState(484);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(485);
				match(T__31);
				setState(488);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__44) {
					{
					setState(486);
					match(T__44);
					setState(487);
					((FunctionBlockContext)_localctx).returnType = type(0);
					}
				}

				setState(490);
				match(T__54);
				setState(491);
				((FunctionBlockContext)_localctx).body = block();
				}
				break;
			case 3:
				_localctx = new FunctionInlineContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(492);
				match(T__64);
				setState(493);
				((FunctionInlineContext)_localctx).name = match(NAME);
				setState(494);
				match(T__30);
				setState(496);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(495);
					((FunctionInlineContext)_localctx).parameter = parameter();
					((FunctionInlineContext)_localctx).params.add(((FunctionInlineContext)_localctx).parameter);
					}
				}

				setState(502);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__40) {
					{
					{
					setState(498);
					match(T__40);
					setState(499);
					((FunctionInlineContext)_localctx).parameter = parameter();
					((FunctionInlineContext)_localctx).params.add(((FunctionInlineContext)_localctx).parameter);
					}
					}
					setState(504);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(505);
				match(T__31);
				setState(508);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__44) {
					{
					setState(506);
					match(T__44);
					setState(507);
					((FunctionInlineContext)_localctx).returnType = type(0);
					}
				}

				setState(510);
				match(T__45);
				setState(511);
				((FunctionInlineContext)_localctx).expression = expr(0);
				setState(512);
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
	public static class ProgramContext extends ParserRuleContext {
		public FuncContext func;
		public List<FuncContext> functions = new ArrayList<FuncContext>();
		public RecordContext record;
		public List<RecordContext> records = new ArrayList<RecordContext>();
		public TerminalNode EOF() { return getToken(EclangParser.EOF, 0); }
		public List<UseContext> use() {
			return getRuleContexts(UseContext.class);
		}
		public UseContext use(int i) {
			return getRuleContext(UseContext.class,i);
		}
		public List<FuncContext> func() {
			return getRuleContexts(FuncContext.class);
		}
		public FuncContext func(int i) {
			return getRuleContext(FuncContext.class,i);
		}
		public List<RecordContext> record() {
			return getRuleContexts(RecordContext.class);
		}
		public RecordContext record(int i) {
			return getRuleContext(RecordContext.class,i);
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
			setState(519);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__62) {
				{
				{
				setState(516);
				use();
				}
				}
				setState(521);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(524); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(524);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__63:
				case T__64:
					{
					setState(522);
					((ProgramContext)_localctx).func = func();
					((ProgramContext)_localctx).functions.add(((ProgramContext)_localctx).func);
					}
					break;
				case T__43:
					{
					setState(523);
					((ProgramContext)_localctx).record = record();
					((ProgramContext)_localctx).records.add(((ProgramContext)_localctx).record);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(526); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 44)) & ~0x3f) == 0 && ((1L << (_la - 44)) & 3145729L) != 0) );
			setState(528);
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
			return precpred(_ctx, 16);
		case 1:
			return precpred(_ctx, 10);
		case 2:
			return precpred(_ctx, 9);
		case 3:
			return precpred(_ctx, 8);
		case 4:
			return precpred(_ctx, 7);
		case 5:
			return precpred(_ctx, 6);
		case 6:
			return precpred(_ctx, 5);
		case 7:
			return precpred(_ctx, 2);
		case 8:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 9:
			return precpred(_ctx, 6);
		case 10:
			return precpred(_ctx, 5);
		case 11:
			return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001R\u0213\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0003\u0000!\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0003\u0001(\b\u0001\u0001\u0002\u0001\u0002\u0004\u0002"+
		",\b\u0002\u000b\u0002\f\u0002-\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005"+
		"\u0005Q\b\u0005\n\u0005\f\u0005T\t\u0005\u0001\u0005\u0001\u0005\u0003"+
		"\u0005X\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0005\u0005l\b\u0005\n\u0005\f\u0005o\t\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0005\u0005\u0085\b\u0005\n\u0005\f\u0005\u0088\t\u0005"+
		"\u0001\u0005\u0001\u0005\u0005\u0005\u008c\b\u0005\n\u0005\f\u0005\u008f"+
		"\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0005\u0006\u0097\b\u0006\n\u0006\f\u0006\u009a\t\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0003\u0006\u00a5\b\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006"+
		"\u00ae\b\u0006\n\u0006\f\u0006\u00b1\t\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u00b8\b\u0006\n\u0006\f\u0006"+
		"\u00bb\t\u0006\u0005\u0006\u00bd\b\u0006\n\u0006\f\u0006\u00c0\t\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0004\u0007\u00cb\b\u0007\u000b\u0007"+
		"\f\u0007\u00cc\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003"+
		"\b\u00dd\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u00e3\b\t\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0003\n\u00e9\b\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u00f3\b\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0005\n\u0102\b\n\n\n\f\n\u0105\t\n\u0001\n\u0001\n\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u0111"+
		"\b\n\n\n\f\n\u0114\t\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0003\n\u0126\b\n\u0001\n\u0001\n\u0003\n\u012a\b\n\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u0131\b\n\u0001\n\u0001\n\u0003"+
		"\n\u0135\b\n\u0001\n\u0001\n\u0001\n\u0005\n\u013a\b\n\n\n\f\n\u013d\t"+
		"\n\u0001\n\u0001\n\u0003\n\u0141\b\n\u0001\n\u0001\n\u0003\n\u0145\b\n"+
		"\u0001\n\u0001\n\u0003\n\u0149\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003"+
		"\n\u014f\b\n\u0001\n\u0001\n\u0003\n\u0153\b\n\u0001\n\u0001\n\u0001\n"+
		"\u0001\n\u0001\n\u0003\n\u015a\b\n\u0001\n\u0003\n\u015d\b\n\u0001\n\u0003"+
		"\n\u0160\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u0166\b\n\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u016e\b\n\u0001\n\u0001"+
		"\n\u0003\n\u0172\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0005\n\u017b\b\n\n\n\f\n\u017e\t\n\u0001\n\u0001\n\u0001\n\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u0191\b\n\n\n\f\n\u0194\t\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0003\n\u01a1\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003"+
		"\n\u01a7\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b"+
		"\u01ad\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b"+
		"\u01b9\b\u000b\n\u000b\f\u000b\u01bc\t\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u01c1\b\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0003\f\u01ca\b\f\u0001\f\u0001\f\u0005\f\u01ce\b\f"+
		"\n\f\f\f\u01d1\t\f\u0001\f\u0001\f\u0001\f\u0003\f\u01d6\b\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0003\f\u01dd\b\f\u0001\f\u0001\f\u0005\f\u01e1"+
		"\b\f\n\f\f\f\u01e4\t\f\u0001\f\u0001\f\u0001\f\u0003\f\u01e9\b\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u01f1\b\f\u0001\f\u0001"+
		"\f\u0005\f\u01f5\b\f\n\f\f\f\u01f8\t\f\u0001\f\u0001\f\u0001\f\u0003\f"+
		"\u01fd\b\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u0203\b\f\u0001\r\u0005"+
		"\r\u0206\b\r\n\r\f\r\u0209\t\r\u0001\r\u0001\r\u0004\r\u020d\b\r\u000b"+
		"\r\f\r\u020e\u0001\r\u0001\r\u0001\r\u0000\u0002\n\f\u000e\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u0000\u0002"+
		"\u0001\u0000\u0001\u0005\u0001\u0000\u0005\u001d\u0268\u0000 \u0001\u0000"+
		"\u0000\u0000\u0002\'\u0001\u0000\u0000\u0000\u0004)\u0001\u0000\u0000"+
		"\u0000\u00061\u0001\u0000\u0000\u0000\b3\u0001\u0000\u0000\u0000\nW\u0001"+
		"\u0000\u0000\u0000\f\u00a4\u0001\u0000\u0000\u0000\u000e\u00c1\u0001\u0000"+
		"\u0000\u0000\u0010\u00dc\u0001\u0000\u0000\u0000\u0012\u00e2\u0001\u0000"+
		"\u0000\u0000\u0014\u01a6\u0001\u0000\u0000\u0000\u0016\u01c0\u0001\u0000"+
		"\u0000\u0000\u0018\u0202\u0001\u0000\u0000\u0000\u001a\u0207\u0001\u0000"+
		"\u0000\u0000\u001c!\u0005F\u0000\u0000\u001d!\u0005G\u0000\u0000\u001e"+
		"!\u0005H\u0000\u0000\u001f!\u0005I\u0000\u0000 \u001c\u0001\u0000\u0000"+
		"\u0000 \u001d\u0001\u0000\u0000\u0000 \u001e\u0001\u0000\u0000\u0000 "+
		"\u001f\u0001\u0000\u0000\u0000!\u0001\u0001\u0000\u0000\u0000\"(\u0003"+
		"\u0000\u0000\u0000#(\u0005C\u0000\u0000$(\u0005K\u0000\u0000%(\u0005J"+
		"\u0000\u0000&(\u0005E\u0000\u0000\'\"\u0001\u0000\u0000\u0000\'#\u0001"+
		"\u0000\u0000\u0000\'$\u0001\u0000\u0000\u0000\'%\u0001\u0000\u0000\u0000"+
		"\'&\u0001\u0000\u0000\u0000(\u0003\u0001\u0000\u0000\u0000)+\u0005Q\u0000"+
		"\u0000*,\u0003\u0014\n\u0000+*\u0001\u0000\u0000\u0000,-\u0001\u0000\u0000"+
		"\u0000-+\u0001\u0000\u0000\u0000-.\u0001\u0000\u0000\u0000./\u0001\u0000"+
		"\u0000\u0000/0\u0005R\u0000\u00000\u0005\u0001\u0000\u0000\u000012\u0007"+
		"\u0000\u0000\u00002\u0007\u0001\u0000\u0000\u000034\u0007\u0001\u0000"+
		"\u00004\t\u0001\u0000\u0000\u000056\u0006\u0005\uffff\uffff\u00006X\u0005"+
		"\u001e\u0000\u000078\u0003\u0006\u0003\u000089\u0003\n\u0005\u000f9X\u0001"+
		"\u0000\u0000\u0000:;\u0005\u001f\u0000\u0000;<\u0003\n\u0005\u0000<=\u0005"+
		" \u0000\u0000=X\u0001\u0000\u0000\u0000>X\u0003\u0002\u0001\u0000?X\u0005"+
		"D\u0000\u0000@A\u0005!\u0000\u0000AB\u0005\u001f\u0000\u0000BC\u0003\n"+
		"\u0005\u0000CD\u0005 \u0000\u0000DE\u0003\n\u0005\u0000EF\u0005\"\u0000"+
		"\u0000FG\u0003\n\u0005\u000bGX\u0001\u0000\u0000\u0000HI\u0005D\u0000"+
		"\u0000IJ\u0005\u001f\u0000\u0000JX\u0005 \u0000\u0000KL\u0005D\u0000\u0000"+
		"LM\u0005\u001f\u0000\u0000MR\u0003\n\u0005\u0000NO\u0005)\u0000\u0000"+
		"OQ\u0003\n\u0005\u0000PN\u0001\u0000\u0000\u0000QT\u0001\u0000\u0000\u0000"+
		"RP\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000SU\u0001\u0000\u0000"+
		"\u0000TR\u0001\u0000\u0000\u0000UV\u0005 \u0000\u0000VX\u0001\u0000\u0000"+
		"\u0000W5\u0001\u0000\u0000\u0000W7\u0001\u0000\u0000\u0000W:\u0001\u0000"+
		"\u0000\u0000W>\u0001\u0000\u0000\u0000W?\u0001\u0000\u0000\u0000W@\u0001"+
		"\u0000\u0000\u0000WH\u0001\u0000\u0000\u0000WK\u0001\u0000\u0000\u0000"+
		"X\u008d\u0001\u0000\u0000\u0000YZ\n\u0010\u0000\u0000Z[\u0003\b\u0004"+
		"\u0000[\\\u0003\n\u0005\u0011\\\u008c\u0001\u0000\u0000\u0000]^\n\n\u0000"+
		"\u0000^_\u0005#\u0000\u0000_\u008c\u0005D\u0000\u0000`a\n\t\u0000\u0000"+
		"ab\u0005$\u0000\u0000b\u008c\u0005D\u0000\u0000cd\n\b\u0000\u0000de\u0005"+
		"%\u0000\u0000e\u008c\u0005D\u0000\u0000fg\n\u0007\u0000\u0000gh\u0005"+
		"\u001b\u0000\u0000hm\u0003\n\u0005\u0000ij\u0005\u001b\u0000\u0000jl\u0003"+
		"\n\u0005\u0000ki\u0001\u0000\u0000\u0000lo\u0001\u0000\u0000\u0000mk\u0001"+
		"\u0000\u0000\u0000mn\u0001\u0000\u0000\u0000n\u008c\u0001\u0000\u0000"+
		"\u0000om\u0001\u0000\u0000\u0000pq\n\u0006\u0000\u0000qr\u0005&\u0000"+
		"\u0000r\u008c\u0005D\u0000\u0000st\n\u0005\u0000\u0000tu\u0005\'\u0000"+
		"\u0000uv\u0003\n\u0005\u0000vw\u0005(\u0000\u0000w\u008c\u0001\u0000\u0000"+
		"\u0000xy\n\u0002\u0000\u0000yz\u0005&\u0000\u0000z{\u0005D\u0000\u0000"+
		"{|\u0005\u001f\u0000\u0000|\u008c\u0005 \u0000\u0000}~\n\u0001\u0000\u0000"+
		"~\u007f\u0005&\u0000\u0000\u007f\u0080\u0005D\u0000\u0000\u0080\u0081"+
		"\u0005\u001f\u0000\u0000\u0081\u0086\u0003\n\u0005\u0000\u0082\u0083\u0005"+
		")\u0000\u0000\u0083\u0085\u0003\n\u0005\u0000\u0084\u0082\u0001\u0000"+
		"\u0000\u0000\u0085\u0088\u0001\u0000\u0000\u0000\u0086\u0084\u0001\u0000"+
		"\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u0089\u0001\u0000"+
		"\u0000\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0089\u008a\u0005 \u0000"+
		"\u0000\u008a\u008c\u0001\u0000\u0000\u0000\u008bY\u0001\u0000\u0000\u0000"+
		"\u008b]\u0001\u0000\u0000\u0000\u008b`\u0001\u0000\u0000\u0000\u008bc"+
		"\u0001\u0000\u0000\u0000\u008bf\u0001\u0000\u0000\u0000\u008bp\u0001\u0000"+
		"\u0000\u0000\u008bs\u0001\u0000\u0000\u0000\u008bx\u0001\u0000\u0000\u0000"+
		"\u008b}\u0001\u0000\u0000\u0000\u008c\u008f\u0001\u0000\u0000\u0000\u008d"+
		"\u008b\u0001\u0000\u0000\u0000\u008d\u008e\u0001\u0000\u0000\u0000\u008e"+
		"\u000b\u0001\u0000\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000\u0090"+
		"\u0091\u0006\u0006\uffff\uffff\u0000\u0091\u00a5\u0005D\u0000\u0000\u0092"+
		"\u0093\u0005\u001f\u0000\u0000\u0093\u0098\u0003\f\u0006\u0000\u0094\u0095"+
		"\u0005)\u0000\u0000\u0095\u0097\u0003\f\u0006\u0000\u0096\u0094\u0001"+
		"\u0000\u0000\u0000\u0097\u009a\u0001\u0000\u0000\u0000\u0098\u0096\u0001"+
		"\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009b\u0001"+
		"\u0000\u0000\u0000\u009a\u0098\u0001\u0000\u0000\u0000\u009b\u009c\u0005"+
		" \u0000\u0000\u009c\u009d\u0005+\u0000\u0000\u009d\u009e\u0003\f\u0006"+
		"\u0003\u009e\u00a5\u0001\u0000\u0000\u0000\u009f\u00a0\u0005\u001f\u0000"+
		"\u0000\u00a0\u00a1\u0003\f\u0006\u0000\u00a1\u00a2\u0005 \u0000\u0000"+
		"\u00a2\u00a5\u0001\u0000\u0000\u0000\u00a3\u00a5\u0005E\u0000\u0000\u00a4"+
		"\u0090\u0001\u0000\u0000\u0000\u00a4\u0092\u0001\u0000\u0000\u0000\u00a4"+
		"\u009f\u0001\u0000\u0000\u0000\u00a4\u00a3\u0001\u0000\u0000\u0000\u00a5"+
		"\u00be\u0001\u0000\u0000\u0000\u00a6\u00a7\n\u0006\u0000\u0000\u00a7\u00bd"+
		"\u0005*\u0000\u0000\u00a8\u00a9\n\u0005\u0000\u0000\u00a9\u00aa\u0005"+
		"\u001c\u0000\u0000\u00aa\u00af\u0003\f\u0006\u0000\u00ab\u00ac\u0005\u001c"+
		"\u0000\u0000\u00ac\u00ae\u0003\f\u0006\u0000\u00ad\u00ab\u0001\u0000\u0000"+
		"\u0000\u00ae\u00b1\u0001\u0000\u0000\u0000\u00af\u00ad\u0001\u0000\u0000"+
		"\u0000\u00af\u00b0\u0001\u0000\u0000\u0000\u00b0\u00bd\u0001\u0000\u0000"+
		"\u0000\u00b1\u00af\u0001\u0000\u0000\u0000\u00b2\u00b3\n\u0004\u0000\u0000"+
		"\u00b3\u00b4\u0005\u001b\u0000\u0000\u00b4\u00b9\u0003\f\u0006\u0000\u00b5"+
		"\u00b6\u0005\u001b\u0000\u0000\u00b6\u00b8\u0003\f\u0006\u0000\u00b7\u00b5"+
		"\u0001\u0000\u0000\u0000\u00b8\u00bb\u0001\u0000\u0000\u0000\u00b9\u00b7"+
		"\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000\u00ba\u00bd"+
		"\u0001\u0000\u0000\u0000\u00bb\u00b9\u0001\u0000\u0000\u0000\u00bc\u00a6"+
		"\u0001\u0000\u0000\u0000\u00bc\u00a8\u0001\u0000\u0000\u0000\u00bc\u00b2"+
		"\u0001\u0000\u0000\u0000\u00bd\u00c0\u0001\u0000\u0000\u0000\u00be\u00bc"+
		"\u0001\u0000\u0000\u0000\u00be\u00bf\u0001\u0000\u0000\u0000\u00bf\r\u0001"+
		"\u0000\u0000\u0000\u00c0\u00be\u0001\u0000\u0000\u0000\u00c1\u00c2\u0005"+
		",\u0000\u0000\u00c2\u00c3\u0005D\u0000\u0000\u00c3\u00c4\u0005%\u0000"+
		"\u0000\u00c4\u00ca\u0005Q\u0000\u0000\u00c5\u00c6\u0005D\u0000\u0000\u00c6"+
		"\u00c7\u0005-\u0000\u0000\u00c7\u00c8\u0003\f\u0006\u0000\u00c8\u00c9"+
		"\u0005B\u0000\u0000\u00c9\u00cb\u0001\u0000\u0000\u0000\u00ca\u00c5\u0001"+
		"\u0000\u0000\u0000\u00cb\u00cc\u0001\u0000\u0000\u0000\u00cc\u00ca\u0001"+
		"\u0000\u0000\u0000\u00cc\u00cd\u0001\u0000\u0000\u0000\u00cd\u00ce\u0001"+
		"\u0000\u0000\u0000\u00ce\u00cf\u0005R\u0000\u0000\u00cf\u000f\u0001\u0000"+
		"\u0000\u0000\u00d0\u00d1\u0005D\u0000\u0000\u00d1\u00d2\u0005-\u0000\u0000"+
		"\u00d2\u00dd\u0003\f\u0006\u0000\u00d3\u00d4\u0005D\u0000\u0000\u00d4"+
		"\u00d5\u0005-\u0000\u0000\u00d5\u00d6\u0003\f\u0006\u0000\u00d6\u00d7"+
		"\u0005.\u0000\u0000\u00d7\u00d8\u0003\n\u0005\u0000\u00d8\u00dd\u0001"+
		"\u0000\u0000\u0000\u00d9\u00da\u0005D\u0000\u0000\u00da\u00db\u0005.\u0000"+
		"\u0000\u00db\u00dd\u0003\n\u0005\u0000\u00dc\u00d0\u0001\u0000\u0000\u0000"+
		"\u00dc\u00d3\u0001\u0000\u0000\u0000\u00dc\u00d9\u0001\u0000\u0000\u0000"+
		"\u00dd\u0011\u0001\u0000\u0000\u0000\u00de\u00df\u0005/\u0000\u0000\u00df"+
		"\u00e3\u0003\n\u0005\u0000\u00e0\u00e3\u00050\u0000\u0000\u00e1\u00e3"+
		"\u00051\u0000\u0000\u00e2\u00de\u0001\u0000\u0000\u0000\u00e2\u00e0\u0001"+
		"\u0000\u0000\u0000\u00e2\u00e1\u0001\u0000\u0000\u0000\u00e3\u0013\u0001"+
		"\u0000\u0000\u0000\u00e4\u00e5\u00052\u0000\u0000\u00e5\u00e8\u0005D\u0000"+
		"\u0000\u00e6\u00e7\u0005-\u0000\u0000\u00e7\u00e9\u0003\f\u0006\u0000"+
		"\u00e8\u00e6\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000"+
		"\u00e9\u00ea\u0001\u0000\u0000\u0000\u00ea\u00eb\u0005.\u0000\u0000\u00eb"+
		"\u00ec\u0003\n\u0005\u0000\u00ec\u00ed\u0005B\u0000\u0000\u00ed\u01a7"+
		"\u0001\u0000\u0000\u0000\u00ee\u00ef\u00053\u0000\u0000\u00ef\u00f2\u0005"+
		"D\u0000\u0000\u00f0\u00f1\u0005-\u0000\u0000\u00f1\u00f3\u0003\f\u0006"+
		"\u0000\u00f2\u00f0\u0001\u0000\u0000\u0000\u00f2\u00f3\u0001\u0000\u0000"+
		"\u0000\u00f3\u00f4\u0001\u0000\u0000\u0000\u00f4\u00f5\u0005.\u0000\u0000"+
		"\u00f5\u00f6\u0003\n\u0005\u0000\u00f6\u00f7\u0005B\u0000\u0000\u00f7"+
		"\u01a7\u0001\u0000\u0000\u0000\u00f8\u00f9\u00052\u0000\u0000\u00f9\u00fa"+
		"\u0005D\u0000\u0000\u00fa\u00fb\u0005.\u0000\u0000\u00fb\u00fc\u0005,"+
		"\u0000\u0000\u00fc\u00fd\u0005D\u0000\u0000\u00fd\u0103\u0005\u001f\u0000"+
		"\u0000\u00fe\u00ff\u0005D\u0000\u0000\u00ff\u0100\u0005.\u0000\u0000\u0100"+
		"\u0102\u0003\n\u0005\u0000\u0101\u00fe\u0001\u0000\u0000\u0000\u0102\u0105"+
		"\u0001\u0000\u0000\u0000\u0103\u0101\u0001\u0000\u0000\u0000\u0103\u0104"+
		"\u0001\u0000\u0000\u0000\u0104\u0106\u0001\u0000\u0000\u0000\u0105\u0103"+
		"\u0001\u0000\u0000\u0000\u0106\u01a7\u0005 \u0000\u0000\u0107\u0108\u0005"+
		"3\u0000\u0000\u0108\u0109\u0005D\u0000\u0000\u0109\u010a\u0005.\u0000"+
		"\u0000\u010a\u010b\u0005,\u0000\u0000\u010b\u010c\u0005D\u0000\u0000\u010c"+
		"\u0112\u0005\u001f\u0000\u0000\u010d\u010e\u0005D\u0000\u0000\u010e\u010f"+
		"\u0005.\u0000\u0000\u010f\u0111\u0003\n\u0005\u0000\u0110\u010d\u0001"+
		"\u0000\u0000\u0000\u0111\u0114\u0001\u0000\u0000\u0000\u0112\u0110\u0001"+
		"\u0000\u0000\u0000\u0112\u0113\u0001\u0000\u0000\u0000\u0113\u0115\u0001"+
		"\u0000\u0000\u0000\u0114\u0112\u0001\u0000\u0000\u0000\u0115\u01a7\u0005"+
		" \u0000\u0000\u0116\u0117\u0005D\u0000\u0000\u0117\u0118\u0005.\u0000"+
		"\u0000\u0118\u0119\u0003\n\u0005\u0000\u0119\u011a\u0005B\u0000\u0000"+
		"\u011a\u01a7\u0001\u0000\u0000\u0000\u011b\u011c\u00054\u0000\u0000\u011c"+
		"\u01a7\u0005B\u0000\u0000\u011d\u011e\u00055\u0000\u0000\u011e\u01a7\u0005"+
		"B\u0000\u0000\u011f\u0120\u00056\u0000\u0000\u0120\u0121\u0003\u0014\n"+
		"\u0000\u0121\u0122\u0005B\u0000\u0000\u0122\u01a7\u0001\u0000\u0000\u0000"+
		"\u0123\u0125\u0005!\u0000\u0000\u0124\u0126\u0005\u001f\u0000\u0000\u0125"+
		"\u0124\u0001\u0000\u0000\u0000\u0125\u0126\u0001\u0000\u0000\u0000\u0126"+
		"\u0127\u0001\u0000\u0000\u0000\u0127\u0129\u0003\n\u0005\u0000\u0128\u012a"+
		"\u0005 \u0000\u0000\u0129\u0128\u0001\u0000\u0000\u0000\u0129\u012a\u0001"+
		"\u0000\u0000\u0000\u012a\u012b\u0001\u0000\u0000\u0000\u012b\u012c\u0005"+
		"7\u0000\u0000\u012c\u013b\u0003\u0004\u0002\u0000\u012d\u012e\u0005\""+
		"\u0000\u0000\u012e\u0130\u0005!\u0000\u0000\u012f\u0131\u0005\u001f\u0000"+
		"\u0000\u0130\u012f\u0001\u0000\u0000\u0000\u0130\u0131\u0001\u0000\u0000"+
		"\u0000\u0131\u0132\u0001\u0000\u0000\u0000\u0132\u0134\u0003\n\u0005\u0000"+
		"\u0133\u0135\u0005 \u0000\u0000\u0134\u0133\u0001\u0000\u0000\u0000\u0134"+
		"\u0135\u0001\u0000\u0000\u0000\u0135\u0136\u0001\u0000\u0000\u0000\u0136"+
		"\u0137\u00057\u0000\u0000\u0137\u0138\u0003\u0004\u0002\u0000\u0138\u013a"+
		"\u0001\u0000\u0000\u0000\u0139\u012d\u0001\u0000\u0000\u0000\u013a\u013d"+
		"\u0001\u0000\u0000\u0000\u013b\u0139\u0001\u0000\u0000\u0000\u013b\u013c"+
		"\u0001\u0000\u0000\u0000\u013c\u0140\u0001\u0000\u0000\u0000\u013d\u013b"+
		"\u0001\u0000\u0000\u0000\u013e\u013f\u0005\"\u0000\u0000\u013f\u0141\u0003"+
		"\u0004\u0002\u0000\u0140\u013e\u0001\u0000\u0000\u0000\u0140\u0141\u0001"+
		"\u0000\u0000\u0000\u0141\u01a7\u0001\u0000\u0000\u0000\u0142\u0144\u0005"+
		"8\u0000\u0000\u0143\u0145\u0005\u001f\u0000\u0000\u0144\u0143\u0001\u0000"+
		"\u0000\u0000\u0144\u0145\u0001\u0000\u0000\u0000\u0145\u0146\u0001\u0000"+
		"\u0000\u0000\u0146\u0148\u0003\n\u0005\u0000\u0147\u0149\u0005 \u0000"+
		"\u0000\u0148\u0147\u0001\u0000\u0000\u0000\u0148\u0149\u0001\u0000\u0000"+
		"\u0000\u0149\u014a\u0001\u0000\u0000\u0000\u014a\u014b\u00057\u0000\u0000"+
		"\u014b\u014e\u0003\u0004\u0002\u0000\u014c\u014d\u0005\"\u0000\u0000\u014d"+
		"\u014f\u0003\u0004\u0002\u0000\u014e\u014c\u0001\u0000\u0000\u0000\u014e"+
		"\u014f\u0001\u0000\u0000\u0000\u014f\u01a7\u0001\u0000\u0000\u0000\u0150"+
		"\u0152\u00059\u0000\u0000\u0151\u0153\u0005\u001f\u0000\u0000\u0152\u0151"+
		"\u0001\u0000\u0000\u0000\u0152\u0153\u0001\u0000\u0000\u0000\u0153\u0154"+
		"\u0001\u0000\u0000\u0000\u0154\u0155\u0005D\u0000\u0000\u0155\u0156\u0005"+
		":\u0000\u0000\u0156\u0159\u0003\n\u0005\u0000\u0157\u0158\u0005;\u0000"+
		"\u0000\u0158\u015a\u0003\n\u0005\u0000\u0159\u0157\u0001\u0000\u0000\u0000"+
		"\u0159\u015a\u0001\u0000\u0000\u0000\u015a\u015c\u0001\u0000\u0000\u0000"+
		"\u015b\u015d\u0003\u0012\t\u0000\u015c\u015b\u0001\u0000\u0000\u0000\u015c"+
		"\u015d\u0001\u0000\u0000\u0000\u015d\u015f\u0001\u0000\u0000\u0000\u015e"+
		"\u0160\u0005 \u0000\u0000\u015f\u015e\u0001\u0000\u0000\u0000\u015f\u0160"+
		"\u0001\u0000\u0000\u0000\u0160\u0161\u0001\u0000\u0000\u0000\u0161\u0162"+
		"\u00057\u0000\u0000\u0162\u0165\u0003\u0004\u0002\u0000\u0163\u0164\u0005"+
		"\"\u0000\u0000\u0164\u0166\u0003\u0004\u0002\u0000\u0165\u0163\u0001\u0000"+
		"\u0000\u0000\u0165\u0166\u0001\u0000\u0000\u0000\u0166\u01a7\u0001\u0000"+
		"\u0000\u0000\u0167\u0168\u0005D\u0000\u0000\u0168\u0169\u0005\u001f\u0000"+
		"\u0000\u0169\u016a\u0005 \u0000\u0000\u016a\u01a7\u0005B\u0000\u0000\u016b"+
		"\u016d\u0005D\u0000\u0000\u016c\u016e\u0005\u001f\u0000\u0000\u016d\u016c"+
		"\u0001\u0000\u0000\u0000\u016d\u016e\u0001\u0000\u0000\u0000\u016e\u016f"+
		"\u0001\u0000\u0000\u0000\u016f\u0171\u0003\n\u0005\u0000\u0170\u0172\u0005"+
		" \u0000\u0000\u0171\u0170\u0001\u0000\u0000\u0000\u0171\u0172\u0001\u0000"+
		"\u0000\u0000\u0172\u0173\u0001\u0000\u0000\u0000\u0173\u0174\u0005B\u0000"+
		"\u0000\u0174\u01a7\u0001\u0000\u0000\u0000\u0175\u0176\u0005D\u0000\u0000"+
		"\u0176\u0177\u0005\u001f\u0000\u0000\u0177\u017c\u0003\n\u0005\u0000\u0178"+
		"\u0179\u0005)\u0000\u0000\u0179\u017b\u0003\n\u0005\u0000\u017a\u0178"+
		"\u0001\u0000\u0000\u0000\u017b\u017e\u0001\u0000\u0000\u0000\u017c\u017a"+
		"\u0001\u0000\u0000\u0000\u017c\u017d\u0001\u0000\u0000\u0000\u017d\u017f"+
		"\u0001\u0000\u0000\u0000\u017e\u017c\u0001\u0000\u0000\u0000\u017f\u0180"+
		"\u0005 \u0000\u0000\u0180\u0181\u0005B\u0000\u0000\u0181\u01a7\u0001\u0000"+
		"\u0000\u0000\u0182\u0183\u0003\n\u0005\u0000\u0183\u0184\u0005&\u0000"+
		"\u0000\u0184\u0185\u0005D\u0000\u0000\u0185\u0186\u0005\u001f\u0000\u0000"+
		"\u0186\u0187\u0005 \u0000\u0000\u0187\u0188\u0005B\u0000\u0000\u0188\u01a7"+
		"\u0001\u0000\u0000\u0000\u0189\u018a\u0003\n\u0005\u0000\u018a\u018b\u0005"+
		"&\u0000\u0000\u018b\u018c\u0005D\u0000\u0000\u018c\u018d\u0005\u001f\u0000"+
		"\u0000\u018d\u0192\u0003\n\u0005\u0000\u018e\u018f\u0005)\u0000\u0000"+
		"\u018f\u0191\u0003\n\u0005\u0000\u0190\u018e\u0001\u0000\u0000\u0000\u0191"+
		"\u0194\u0001\u0000\u0000\u0000\u0192\u0190\u0001\u0000\u0000\u0000\u0192"+
		"\u0193\u0001\u0000\u0000\u0000\u0193\u0195\u0001\u0000\u0000\u0000\u0194"+
		"\u0192\u0001\u0000\u0000\u0000\u0195\u0196\u0005 \u0000\u0000\u0196\u0197"+
		"\u0005B\u0000\u0000\u0197\u01a7\u0001\u0000\u0000\u0000\u0198\u0199\u0005"+
		"<\u0000\u0000\u0199\u019a\u0003\n\u0005\u0000\u019a\u019b\u0005B\u0000"+
		"\u0000\u019b\u01a7\u0001\u0000\u0000\u0000\u019c\u019d\u0005=\u0000\u0000"+
		"\u019d\u01a0\u0005D\u0000\u0000\u019e\u019f\u0005>\u0000\u0000\u019f\u01a1"+
		"\u0005D\u0000\u0000\u01a0\u019e\u0001\u0000\u0000\u0000\u01a0\u01a1\u0001"+
		"\u0000\u0000\u0000\u01a1\u01a2\u0001\u0000\u0000\u0000\u01a2\u01a3\u0005"+
		":\u0000\u0000\u01a3\u01a7\u0003\u0004\u0002\u0000\u01a4\u01a5\u00056\u0000"+
		"\u0000\u01a5\u01a7\u0003\u0014\n\u0000\u01a6\u00e4\u0001\u0000\u0000\u0000"+
		"\u01a6\u00ee\u0001\u0000\u0000\u0000\u01a6\u00f8\u0001\u0000\u0000\u0000"+
		"\u01a6\u0107\u0001\u0000\u0000\u0000\u01a6\u0116\u0001\u0000\u0000\u0000"+
		"\u01a6\u011b\u0001\u0000\u0000\u0000\u01a6\u011d\u0001\u0000\u0000\u0000"+
		"\u01a6\u011f\u0001\u0000\u0000\u0000\u01a6\u0123\u0001\u0000\u0000\u0000"+
		"\u01a6\u0142\u0001\u0000\u0000\u0000\u01a6\u0150\u0001\u0000\u0000\u0000"+
		"\u01a6\u0167\u0001\u0000\u0000\u0000\u01a6\u016b\u0001\u0000\u0000\u0000"+
		"\u01a6\u0175\u0001\u0000\u0000\u0000\u01a6\u0182\u0001\u0000\u0000\u0000"+
		"\u01a6\u0189\u0001\u0000\u0000\u0000\u01a6\u0198\u0001\u0000\u0000\u0000"+
		"\u01a6\u019c\u0001\u0000\u0000\u0000\u01a6\u01a4\u0001\u0000\u0000\u0000"+
		"\u01a7\u0015\u0001\u0000\u0000\u0000\u01a8\u01a9\u0005?\u0000\u0000\u01a9"+
		"\u01ac\u0005K\u0000\u0000\u01aa\u01ab\u0005%\u0000\u0000\u01ab\u01ad\u0005"+
		"D\u0000\u0000\u01ac\u01aa\u0001\u0000\u0000\u0000\u01ac\u01ad\u0001\u0000"+
		"\u0000\u0000\u01ad\u01ae\u0001\u0000\u0000\u0000\u01ae\u01c1\u0005B\u0000"+
		"\u0000\u01af\u01b0\u0005?\u0000\u0000\u01b0\u01b1\u0005\u0007\u0000\u0000"+
		"\u01b1\u01b2\u0005>\u0000\u0000\u01b2\u01b3\u0005K\u0000\u0000\u01b3\u01c1"+
		"\u0005B\u0000\u0000\u01b4\u01b5\u0005?\u0000\u0000\u01b5\u01ba\u0005D"+
		"\u0000\u0000\u01b6\u01b7\u0005)\u0000\u0000\u01b7\u01b9\u0005D\u0000\u0000"+
		"\u01b8\u01b6\u0001\u0000\u0000\u0000\u01b9\u01bc\u0001\u0000\u0000\u0000"+
		"\u01ba\u01b8\u0001\u0000\u0000\u0000\u01ba\u01bb\u0001\u0000\u0000\u0000"+
		"\u01bb\u01bd\u0001\u0000\u0000\u0000\u01bc\u01ba\u0001\u0000\u0000\u0000"+
		"\u01bd\u01be\u0005>\u0000\u0000\u01be\u01bf\u0005K\u0000\u0000\u01bf\u01c1"+
		"\u0005B\u0000\u0000\u01c0\u01a8\u0001\u0000\u0000\u0000\u01c0\u01af\u0001"+
		"\u0000\u0000\u0000\u01c0\u01b4\u0001\u0000\u0000\u0000\u01c1\u0017\u0001"+
		"\u0000\u0000\u0000\u01c2\u01c3\u0005@\u0000\u0000\u01c3\u01c4\u0005A\u0000"+
		"\u0000\u01c4\u01c5\u0005K\u0000\u0000\u01c5\u01c6\u0005%\u0000\u0000\u01c6"+
		"\u01c7\u0005D\u0000\u0000\u01c7\u01c9\u0005\u001f\u0000\u0000\u01c8\u01ca"+
		"\u0003\u0010\b\u0000\u01c9\u01c8\u0001\u0000\u0000\u0000\u01c9\u01ca\u0001"+
		"\u0000\u0000\u0000\u01ca\u01cf\u0001\u0000\u0000\u0000\u01cb\u01cc\u0005"+
		")\u0000\u0000\u01cc\u01ce\u0003\u0010\b\u0000\u01cd\u01cb\u0001\u0000"+
		"\u0000\u0000\u01ce\u01d1\u0001\u0000\u0000\u0000\u01cf\u01cd\u0001\u0000"+
		"\u0000\u0000\u01cf\u01d0\u0001\u0000\u0000\u0000\u01d0\u01d2\u0001\u0000"+
		"\u0000\u0000\u01d1\u01cf\u0001\u0000\u0000\u0000\u01d2\u01d5\u0005 \u0000"+
		"\u0000\u01d3\u01d4\u0005-\u0000\u0000\u01d4\u01d6\u0003\f\u0006\u0000"+
		"\u01d5\u01d3\u0001\u0000\u0000\u0000\u01d5\u01d6\u0001\u0000\u0000\u0000"+
		"\u01d6\u01d7\u0001\u0000\u0000\u0000\u01d7\u0203\u0005B\u0000\u0000\u01d8"+
		"\u01d9\u0005A\u0000\u0000\u01d9\u01da\u0005D\u0000\u0000\u01da\u01dc\u0005"+
		"\u001f\u0000\u0000\u01db\u01dd\u0003\u0010\b\u0000\u01dc\u01db\u0001\u0000"+
		"\u0000\u0000\u01dc\u01dd\u0001\u0000\u0000\u0000\u01dd\u01e2\u0001\u0000"+
		"\u0000\u0000\u01de\u01df\u0005)\u0000\u0000\u01df\u01e1\u0003\u0010\b"+
		"\u0000\u01e0\u01de\u0001\u0000\u0000\u0000\u01e1\u01e4\u0001\u0000\u0000"+
		"\u0000\u01e2\u01e0\u0001\u0000\u0000\u0000\u01e2\u01e3\u0001\u0000\u0000"+
		"\u0000\u01e3\u01e5\u0001\u0000\u0000\u0000\u01e4\u01e2\u0001\u0000\u0000"+
		"\u0000\u01e5\u01e8\u0005 \u0000\u0000\u01e6\u01e7\u0005-\u0000\u0000\u01e7"+
		"\u01e9\u0003\f\u0006\u0000\u01e8\u01e6\u0001\u0000\u0000\u0000\u01e8\u01e9"+
		"\u0001\u0000\u0000\u0000\u01e9\u01ea\u0001\u0000\u0000\u0000\u01ea\u01eb"+
		"\u00057\u0000\u0000\u01eb\u0203\u0003\u0004\u0002\u0000\u01ec\u01ed\u0005"+
		"A\u0000\u0000\u01ed\u01ee\u0005D\u0000\u0000\u01ee\u01f0\u0005\u001f\u0000"+
		"\u0000\u01ef\u01f1\u0003\u0010\b\u0000\u01f0\u01ef\u0001\u0000\u0000\u0000"+
		"\u01f0\u01f1\u0001\u0000\u0000\u0000\u01f1\u01f6\u0001\u0000\u0000\u0000"+
		"\u01f2\u01f3\u0005)\u0000\u0000\u01f3\u01f5\u0003\u0010\b\u0000\u01f4"+
		"\u01f2\u0001\u0000\u0000\u0000\u01f5\u01f8\u0001\u0000\u0000\u0000\u01f6"+
		"\u01f4\u0001\u0000\u0000\u0000\u01f6\u01f7\u0001\u0000\u0000\u0000\u01f7"+
		"\u01f9\u0001\u0000\u0000\u0000\u01f8\u01f6\u0001\u0000\u0000\u0000\u01f9"+
		"\u01fc\u0005 \u0000\u0000\u01fa\u01fb\u0005-\u0000\u0000\u01fb\u01fd\u0003"+
		"\f\u0006\u0000\u01fc\u01fa\u0001\u0000\u0000\u0000\u01fc\u01fd\u0001\u0000"+
		"\u0000\u0000\u01fd\u01fe\u0001\u0000\u0000\u0000\u01fe\u01ff\u0005.\u0000"+
		"\u0000\u01ff\u0200\u0003\n\u0005\u0000\u0200\u0201\u0005B\u0000\u0000"+
		"\u0201\u0203\u0001\u0000\u0000\u0000\u0202\u01c2\u0001\u0000\u0000\u0000"+
		"\u0202\u01d8\u0001\u0000\u0000\u0000\u0202\u01ec\u0001\u0000\u0000\u0000"+
		"\u0203\u0019\u0001\u0000\u0000\u0000\u0204\u0206\u0003\u0016\u000b\u0000"+
		"\u0205\u0204\u0001\u0000\u0000\u0000\u0206\u0209\u0001\u0000\u0000\u0000"+
		"\u0207\u0205\u0001\u0000\u0000\u0000\u0207\u0208\u0001\u0000\u0000\u0000"+
		"\u0208\u020c\u0001\u0000\u0000\u0000\u0209\u0207\u0001\u0000\u0000\u0000"+
		"\u020a\u020d\u0003\u0018\f\u0000\u020b\u020d\u0003\u000e\u0007\u0000\u020c"+
		"\u020a\u0001\u0000\u0000\u0000\u020c\u020b\u0001\u0000\u0000\u0000\u020d"+
		"\u020e\u0001\u0000\u0000\u0000\u020e\u020c\u0001\u0000\u0000\u0000\u020e"+
		"\u020f\u0001\u0000\u0000\u0000\u020f\u0210\u0001\u0000\u0000\u0000\u0210"+
		"\u0211\u0005\u0000\u0000\u0001\u0211\u001b\u0001\u0000\u0000\u0000: \'"+
		"-RWm\u0086\u008b\u008d\u0098\u00a4\u00af\u00b9\u00bc\u00be\u00cc\u00dc"+
		"\u00e2\u00e8\u00f2\u0103\u0112\u0125\u0129\u0130\u0134\u013b\u0140\u0144"+
		"\u0148\u014e\u0152\u0159\u015c\u015f\u0165\u016d\u0171\u017c\u0192\u01a0"+
		"\u01a6\u01ac\u01ba\u01c0\u01c9\u01cf\u01d5\u01dc\u01e2\u01e8\u01f0\u01f6"+
		"\u01fc\u0202\u0207\u020c\u020e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}