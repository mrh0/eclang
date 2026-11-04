// @ts-nocheck
// Generated from src/main/antlr4/Eclang.g4 by scripts/gen-parser.mjs
// noinspection ES6UnusedImports,JSUnusedGlobalSymbols,JSUnusedLocalSymbols

import {
	ATN,
	ATNDeserializer, DecisionState, DFA, FailedPredicateException,
	RecognitionException, NoViableAltException, BailErrorStrategy,
	Parser, ParserATNSimulator,
	RuleContext, ParserRuleContext, PredictionMode, PredictionContextCache,
	TerminalNode, RuleNode,
	Token, TokenStream,
	Interval, IntervalSet
} from 'antlr4';
// for running tests with parameters, TODO: discuss strategy for typed parameters in CI
// eslint-disable-next-line no-unused-vars
type int = number;

export default class EclangParser extends Parser {
	public static readonly T__0 = 1;
	public static readonly T__1 = 2;
	public static readonly T__2 = 3;
	public static readonly T__3 = 4;
	public static readonly T__4 = 5;
	public static readonly T__5 = 6;
	public static readonly T__6 = 7;
	public static readonly T__7 = 8;
	public static readonly T__8 = 9;
	public static readonly T__9 = 10;
	public static readonly T__10 = 11;
	public static readonly T__11 = 12;
	public static readonly T__12 = 13;
	public static readonly T__13 = 14;
	public static readonly T__14 = 15;
	public static readonly T__15 = 16;
	public static readonly T__16 = 17;
	public static readonly T__17 = 18;
	public static readonly T__18 = 19;
	public static readonly T__19 = 20;
	public static readonly T__20 = 21;
	public static readonly T__21 = 22;
	public static readonly T__22 = 23;
	public static readonly T__23 = 24;
	public static readonly T__24 = 25;
	public static readonly T__25 = 26;
	public static readonly T__26 = 27;
	public static readonly T__27 = 28;
	public static readonly T__28 = 29;
	public static readonly T__29 = 30;
	public static readonly T__30 = 31;
	public static readonly T__31 = 32;
	public static readonly T__32 = 33;
	public static readonly T__33 = 34;
	public static readonly T__34 = 35;
	public static readonly T__35 = 36;
	public static readonly T__36 = 37;
	public static readonly T__37 = 38;
	public static readonly T__38 = 39;
	public static readonly T__39 = 40;
	public static readonly T__40 = 41;
	public static readonly T__41 = 42;
	public static readonly T__42 = 43;
	public static readonly T__43 = 44;
	public static readonly T__44 = 45;
	public static readonly T__45 = 46;
	public static readonly T__46 = 47;
	public static readonly T__47 = 48;
	public static readonly T__48 = 49;
	public static readonly T__49 = 50;
	public static readonly T__50 = 51;
	public static readonly T__51 = 52;
	public static readonly T__52 = 53;
	public static readonly T__53 = 54;
	public static readonly T__54 = 55;
	public static readonly T__55 = 56;
	public static readonly T__56 = 57;
	public static readonly T__57 = 58;
	public static readonly T__58 = 59;
	public static readonly T__59 = 60;
	public static readonly T__60 = 61;
	public static readonly T__61 = 62;
	public static readonly T__62 = 63;
	public static readonly T__63 = 64;
	public static readonly T__64 = 65;
	public static readonly T__65 = 66;
	public static readonly T__66 = 67;
	public static readonly T__67 = 68;
	public static readonly T__68 = 69;
	public static readonly T__69 = 70;
	public static readonly T__70 = 71;
	public static readonly T__71 = 72;
	public static readonly T__72 = 73;
	public static readonly T__73 = 74;
	public static readonly T__74 = 75;
	public static readonly T__75 = 76;
	public static readonly T__76 = 77;
	public static readonly T__77 = 78;
	public static readonly T__78 = 79;
	public static readonly T__79 = 80;
	public static readonly T__80 = 81;
	public static readonly T__81 = 82;
	public static readonly T__82 = 83;
	public static readonly T__83 = 84;
	public static readonly T__84 = 85;
	public static readonly T__85 = 86;
	public static readonly T__86 = 87;
	public static readonly T__87 = 88;
	public static readonly T__88 = 89;
	public static readonly T__89 = 90;
	public static readonly T__90 = 91;
	public static readonly T__91 = 92;
	public static readonly T__92 = 93;
	public static readonly T__93 = 94;
	public static readonly T__94 = 95;
	public static readonly T__95 = 96;
	public static readonly T__96 = 97;
	public static readonly T__97 = 98;
	public static readonly T__98 = 99;
	public static readonly T__99 = 100;
	public static readonly T__100 = 101;
	public static readonly T__101 = 102;
	public static readonly T__102 = 103;
	public static readonly T__103 = 104;
	public static readonly T__104 = 105;
	public static readonly T__105 = 106;
	public static readonly T__106 = 107;
	public static readonly T__107 = 108;
	public static readonly T__108 = 109;
	public static readonly T__109 = 110;
	public static readonly T__110 = 111;
	public static readonly T__111 = 112;
	public static readonly T__112 = 113;
	public static readonly T__113 = 114;
	public static readonly T__114 = 115;
	public static readonly NL = 116;
	public static readonly BOOL = 117;
	public static readonly NAME = 118;
	public static readonly ATOM = 119;
	public static readonly FLOAT = 120;
	public static readonly INT = 121;
	public static readonly HEX = 122;
	public static readonly BIN = 123;
	public static readonly CHAR = 124;
	public static readonly STRING = 125;
	public static readonly EMBEDED = 126;
	public static readonly WHITESPACE = 127;
	public static readonly EMPTYLINE = 128;
	public static readonly COMMENT = 129;
	public static readonly ECDOC = 130;
	public static readonly BLOCKCOMMENT = 131;
	public static readonly INDENT = 132;
	public static readonly DEDENT = 133;
	public static readonly EOF = Token.EOF;
	public static readonly RULE_number = 0;
	public static readonly RULE_primitive = 1;
	public static readonly RULE_block = 2;
	public static readonly RULE_expr = 3;
	public static readonly RULE_argument = 4;
	public static readonly RULE_jump = 5;
	public static readonly RULE_tail = 6;
	public static readonly RULE_lambdaParameter = 7;
	public static readonly RULE_type = 8;
	public static readonly RULE_interface = 9;
	public static readonly RULE_parameter = 10;
	public static readonly RULE_statement = 11;
	public static readonly RULE_destructure = 12;
	public static readonly RULE_destructurePart = 13;
	public static readonly RULE_whenPattern = 14;
	public static readonly RULE_whenValue = 15;
	public static readonly RULE_whenArm = 16;
	public static readonly RULE_whenExpr = 17;
	public static readonly RULE_whenValueArm = 18;
	public static readonly RULE_func = 19;
	public static readonly RULE_global = 20;
	public static readonly RULE_interfaceMember = 21;
	public static readonly RULE_recordField = 22;
	public static readonly RULE_use = 23;
	public static readonly RULE_interpolation = 24;
	public static readonly RULE_privateDeclaration = 25;
	public static readonly RULE_program = 26;
	public static readonly literalNames: (string | null)[] = [ null, "'here'", 
                                                            "'null'", "'it'", 
                                                            "'('", "','", 
                                                            "')'", "'sizeof'", 
                                                            "'alignof'", 
                                                            "'offsetof'", 
                                                            "'fn'", "'.'", 
                                                            "'<'", "'>'", 
                                                            "'{'", "'='", 
                                                            "'}'", "'...'", 
                                                            "'['", "']c'", 
                                                            "']'", "'?.'", 
                                                            "'..'", "'..<'", 
                                                            "'..='", "'!'", 
                                                            "'as'", "'unsafe'", 
                                                            "'!as'", "'@'", 
                                                            "'addrof'", 
                                                            "'try'", "'new'", 
                                                            "'in'", "'**'", 
                                                            "'not'", "'!!'", 
                                                            "'~'", "'-'", 
                                                            "'*'", "'/'", 
                                                            "'%'", "'+'", 
                                                            "'<<'", "'&'", 
                                                            "'^'", "'|'", 
                                                            "'..>'", "'<='", 
                                                            "'>='", "'is'", 
                                                            "'!is'", "'=='", 
                                                            "'!='", "'==='", 
                                                            "'!=='", "'&&'", 
                                                            "'and'", "'^^'", 
                                                            "'xor'", "'||'", 
                                                            "'or'", "'??'", 
                                                            "'if'", "'do'", 
                                                            "'else'", "'|>'", 
                                                            "':'", "'ret'", 
                                                            "'throw'", "'break'", 
                                                            "'continue'", 
                                                            "'?'", "'anon'", 
                                                            "'typeof'", 
                                                            "'throws'", 
                                                            "'=>'", "'inter'", 
                                                            "'var'", "'val'", 
                                                            "'vol'", "'const'", 
                                                            "'+='", "'-='", 
                                                            "'*='", "'/='", 
                                                            "'%='", "'&='", 
                                                            "'|='", "'^='", 
                                                            "'<<='", "'>>='", 
                                                            "'pass'", "'yield'", 
                                                            "'assert'", 
                                                            "'while'", "'defer'", 
                                                            "'errdefer'", 
                                                            "'when'", "'for'", 
                                                            "'to'", "'where'", 
                                                            "'catch'", "'declare'", 
                                                            "'extern'", 
                                                            "'inline'", 
                                                            "'type'", "'enum'", 
                                                            "'unit'", "'rec'", 
                                                            "'record'", 
                                                            "'extends'", 
                                                            "'struct'", 
                                                            "'module'", 
                                                            "'use'", "'priv'" ];
	public static readonly symbolicNames: (string | null)[] = [ null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             null, null, 
                                                             "NL", "BOOL", 
                                                             "NAME", "ATOM", 
                                                             "FLOAT", "INT", 
                                                             "HEX", "BIN", 
                                                             "CHAR", "STRING", 
                                                             "EMBEDED", 
                                                             "WHITESPACE", 
                                                             "EMPTYLINE", 
                                                             "COMMENT", 
                                                             "ECDOC", "BLOCKCOMMENT", 
                                                             "INDENT", "DEDENT" ];
	// tslint:disable:no-trailing-whitespace
	public static readonly ruleNames: string[] = [
		"number", "primitive", "block", "expr", "argument", "jump", "tail", "lambdaParameter", 
		"type", "interface", "parameter", "statement", "destructure", "destructurePart", 
		"whenPattern", "whenValue", "whenArm", "whenExpr", "whenValueArm", "func", 
		"global", "interfaceMember", "recordField", "use", "interpolation", "privateDeclaration", 
		"program",
	];
	public get grammarFileName(): string { return "Eclang.g4"; }
	public get literalNames(): (string | null)[] { return EclangParser.literalNames; }
	public get symbolicNames(): (string | null)[] { return EclangParser.symbolicNames; }
	public get ruleNames(): string[] { return EclangParser.ruleNames; }
	public get serializedATN(): number[] { return EclangParser._serializedATN; }

	protected createFailedPredicateException(predicate?: string, message?: string): FailedPredicateException {
		return new FailedPredicateException(this, predicate, message);
	}

	constructor(input: TokenStream) {
		super(input);
		this._interp = new ParserATNSimulator(this, EclangParser._ATN, EclangParser.DecisionsToDFA, new PredictionContextCache());
	}
	// @RuleVersion(0)
	public number_(): NumberContext {
		let localctx: NumberContext = new NumberContext(this, this._ctx, this.state);
		this.enterRule(localctx, 0, EclangParser.RULE_number);
		try {
			this.state = 58;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 121:
				localctx = new NumberIntegerContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 54;
				this.match(EclangParser.INT);
				}
				break;
			case 120:
				localctx = new NumberFloatingContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 55;
				this.match(EclangParser.FLOAT);
				}
				break;
			case 122:
				localctx = new NumberHexContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 56;
				this.match(EclangParser.HEX);
				}
				break;
			case 123:
				localctx = new NumberBinContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 57;
				this.match(EclangParser.BIN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public primitive(): PrimitiveContext {
		let localctx: PrimitiveContext = new PrimitiveContext(this, this._ctx, this.state);
		this.enterRule(localctx, 2, EclangParser.RULE_primitive);
		try {
			this.state = 65;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 120:
			case 121:
			case 122:
			case 123:
				localctx = new PrimitiveNumberContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 60;
				this.number_();
				}
				break;
			case 117:
				localctx = new PrimitiveBoolContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 61;
				this.match(EclangParser.BOOL);
				}
				break;
			case 125:
				localctx = new PrimitiveStringContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 62;
				this.match(EclangParser.STRING);
				}
				break;
			case 124:
				localctx = new PrimitiveCharContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 63;
				this.match(EclangParser.CHAR);
				}
				break;
			case 119:
				localctx = new PrimitiveAtomContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 64;
				this.match(EclangParser.ATOM);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public block(): BlockContext {
		let localctx: BlockContext = new BlockContext(this, this._ctx, this.state);
		this.enterRule(localctx, 4, EclangParser.RULE_block);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 67;
			this.match(EclangParser.INDENT);
			this.state = 69;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 68;
				localctx._statement = this.statement();
				localctx._statements.push(localctx._statement);
				}
				}
				this.state = 71;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while ((((_la) & ~0x1F) === 0 && ((1 << _la) & 3791931294) !== 0) || ((((_la - 32)) & ~0x1F) === 0 && ((1 << (_la - 32)) & 2147483769) !== 0) || ((((_la - 68)) & ~0x1F) === 0 && ((1 << (_la - 68)) & 4278205455) !== 0) || ((((_la - 117)) & ~0x1F) === 0 && ((1 << (_la - 117)) & 511) !== 0));
			this.state = 73;
			this.match(EclangParser.DEDENT);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}

	public expr(): ExprContext;
	public expr(_p: number): ExprContext;
	// @RuleVersion(0)
	public expr(_p?: number): ExprContext {
		if (_p === undefined) {
			_p = 0;
		}

		let _parentctx: ParserRuleContext = this._ctx;
		let _parentState: number = this.state;
		let localctx: ExprContext = new ExprContext(this, this._ctx, _parentState);
		let _prevctx: ExprContext = localctx;
		let _startState: number = 6;
		this.enterRecursionRule(localctx, 6, EclangParser.RULE_expr, _p);
		let _la: number;
		try {
			let _alt: number;
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 457;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 44, this._ctx) ) {
			case 1:
				{
				localctx = new ExprHereContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;

				this.state = 76;
				this.match(EclangParser.T__0);
				}
				break;
			case 2:
				{
				localctx = new ExprNullContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 77;
				this.match(EclangParser.T__1);
				}
				break;
			case 3:
				{
				localctx = new ExprItContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 78;
				this.match(EclangParser.T__2);
				}
				break;
			case 4:
				{
				localctx = new ExprNestContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 79;
				this.match(EclangParser.T__3);
				this.state = 80;
				this.expr(0);
				this.state = 85;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 81;
					this.match(EclangParser.T__4);
					this.state = 82;
					this.expr(0);
					}
					}
					this.state = 87;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 88;
				this.match(EclangParser.T__5);
				}
				break;
			case 5:
				{
				localctx = new ExprNestContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 90;
				this.match(EclangParser.T__3);
				this.state = 91;
				this.match(EclangParser.INDENT);
				this.state = 92;
				this.expr(0);
				this.state = 98;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 93;
					this.match(EclangParser.T__4);
					this.state = 94;
					this.match(EclangParser.NL);
					this.state = 95;
					this.expr(0);
					}
					}
					this.state = 100;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 101;
				this.match(EclangParser.NL);
				this.state = 102;
				this.match(EclangParser.DEDENT);
				this.state = 103;
				this.match(EclangParser.T__5);
				}
				break;
			case 6:
				{
				localctx = new ExprPrimitiveUnitContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 105;
				this.primitive();
				this.state = 106;
				this.match(EclangParser.NAME);
				}
				break;
			case 7:
				{
				localctx = new ExprPrimitiveContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 108;
				this.primitive();
				}
				break;
			case 8:
				{
				localctx = new ExprSizeOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 109;
				this.match(EclangParser.T__6);
				this.state = 110;
				this.type_(0);
				}
				break;
			case 9:
				{
				localctx = new ExprAlignOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 111;
				this.match(EclangParser.T__7);
				this.state = 112;
				this.type_(0);
				}
				break;
			case 10:
				{
				localctx = new ExprOffsetOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 113;
				this.match(EclangParser.T__8);
				this.state = 114;
				this.match(EclangParser.T__3);
				this.state = 115;
				this.type_(0);
				this.state = 116;
				this.match(EclangParser.T__4);
				this.state = 117;
				(localctx as ExprOffsetOfContext)._field = this.match(EclangParser.NAME);
				this.state = 118;
				this.match(EclangParser.T__5);
				}
				break;
			case 11:
				{
				localctx = new ExprFunctionReferenceContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 120;
				this.match(EclangParser.T__9);
				this.state = 123;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 5, this._ctx) ) {
				case 1:
					{
					this.state = 121;
					(localctx as ExprFunctionReferenceContext)._qualifier = this.match(EclangParser.NAME);
					this.state = 122;
					this.match(EclangParser.T__10);
					}
					break;
				}
				this.state = 125;
				(localctx as ExprFunctionReferenceContext)._name = this.match(EclangParser.NAME);
				}
				break;
			case 12:
				{
				localctx = new ExprFunctionCallNoArgsContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 126;
				this.match(EclangParser.NAME);
				this.state = 127;
				this.match(EclangParser.T__3);
				this.state = 128;
				this.match(EclangParser.T__5);
				}
				break;
			case 13:
				{
				localctx = new ExprFunctionCallWithArgsContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 129;
				this.match(EclangParser.NAME);
				this.state = 130;
				this.match(EclangParser.T__3);
				this.state = 131;
				(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
				this.state = 136;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 132;
					this.match(EclangParser.T__4);
					this.state = 133;
					(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 138;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 139;
				this.match(EclangParser.T__5);
				}
				break;
			case 14:
				{
				localctx = new ExprFunctionCallWithArgsContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 141;
				this.match(EclangParser.NAME);
				this.state = 142;
				this.match(EclangParser.T__3);
				this.state = 143;
				this.match(EclangParser.INDENT);
				this.state = 144;
				(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
				this.state = 150;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 145;
					this.match(EclangParser.T__4);
					this.state = 146;
					this.match(EclangParser.NL);
					this.state = 147;
					(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 152;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 153;
				this.match(EclangParser.NL);
				this.state = 154;
				this.match(EclangParser.DEDENT);
				this.state = 155;
				this.match(EclangParser.T__5);
				}
				break;
			case 15:
				{
				localctx = new ExprCreateRecordNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 175;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 159;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 8, this._ctx) ) {
					case 1:
						{
						this.state = 157;
						(localctx as ExprCreateRecordNamedContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 158;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 161;
					(localctx as ExprCreateRecordNamedContext)._recordType = this.match(EclangParser.NAME);
					this.state = 173;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 162;
						this.match(EclangParser.T__11);
						this.state = 163;
						(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
						this.state = 168;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 164;
							this.match(EclangParser.T__4);
							this.state = 165;
							(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
							}
							}
							this.state = 170;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 171;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 177;
				this.match(EclangParser.T__13);
				this.state = 178;
				(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
				(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
				this.state = 179;
				this.match(EclangParser.T__14);
				this.state = 180;
				this.expr(0);
				this.state = 187;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 181;
					this.match(EclangParser.T__4);
					this.state = 182;
					(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
					this.state = 183;
					this.match(EclangParser.T__14);
					this.state = 184;
					this.expr(0);
					}
					}
					this.state = 189;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 190;
				this.match(EclangParser.T__15);
				}
				break;
			case 16:
				{
				localctx = new ExprCreateRecordNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 210;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 194;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 13, this._ctx) ) {
					case 1:
						{
						this.state = 192;
						(localctx as ExprCreateRecordNamedContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 193;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 196;
					(localctx as ExprCreateRecordNamedContext)._recordType = this.match(EclangParser.NAME);
					this.state = 208;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 197;
						this.match(EclangParser.T__11);
						this.state = 198;
						(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
						this.state = 203;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 199;
							this.match(EclangParser.T__4);
							this.state = 200;
							(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
							}
							}
							this.state = 205;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 206;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 212;
				this.match(EclangParser.T__13);
				this.state = 213;
				this.match(EclangParser.INDENT);
				this.state = 214;
				(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
				(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
				this.state = 215;
				this.match(EclangParser.T__14);
				this.state = 216;
				this.expr(0);
				this.state = 224;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 217;
					this.match(EclangParser.T__4);
					this.state = 218;
					this.match(EclangParser.NL);
					this.state = 219;
					(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
					this.state = 220;
					this.match(EclangParser.T__14);
					this.state = 221;
					this.expr(0);
					}
					}
					this.state = 226;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 227;
				this.match(EclangParser.NL);
				this.state = 228;
				this.match(EclangParser.DEDENT);
				this.state = 229;
				this.match(EclangParser.T__15);
				}
				break;
			case 17:
				{
				localctx = new ExprCreateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 249;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 233;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 18, this._ctx) ) {
					case 1:
						{
						this.state = 231;
						(localctx as ExprCreateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 232;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 235;
					(localctx as ExprCreateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 247;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 236;
						this.match(EclangParser.T__11);
						this.state = 237;
						(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
						this.state = 242;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 238;
							this.match(EclangParser.T__4);
							this.state = 239;
							(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
							}
							}
							this.state = 244;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 245;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 251;
				this.match(EclangParser.T__13);
				this.state = 252;
				this.expr(0);
				this.state = 257;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 253;
					this.match(EclangParser.T__4);
					this.state = 254;
					this.expr(0);
					}
					}
					this.state = 259;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 260;
				this.match(EclangParser.T__15);
				}
				break;
			case 18:
				{
				localctx = new ExprCreateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 280;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 264;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 23, this._ctx) ) {
					case 1:
						{
						this.state = 262;
						(localctx as ExprCreateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 263;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 266;
					(localctx as ExprCreateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 278;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 267;
						this.match(EclangParser.T__11);
						this.state = 268;
						(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
						this.state = 273;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 269;
							this.match(EclangParser.T__4);
							this.state = 270;
							(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
							}
							}
							this.state = 275;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 276;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 282;
				this.match(EclangParser.T__13);
				this.state = 283;
				this.match(EclangParser.INDENT);
				this.state = 284;
				this.expr(0);
				this.state = 290;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 285;
					this.match(EclangParser.T__4);
					this.state = 286;
					this.match(EclangParser.NL);
					this.state = 287;
					this.expr(0);
					}
					}
					this.state = 292;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 293;
				this.match(EclangParser.NL);
				this.state = 294;
				this.match(EclangParser.DEDENT);
				this.state = 295;
				this.match(EclangParser.T__15);
				}
				break;
			case 19:
				{
				localctx = new ExprUpdateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 315;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 299;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 28, this._ctx) ) {
					case 1:
						{
						this.state = 297;
						(localctx as ExprUpdateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 298;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 301;
					(localctx as ExprUpdateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 313;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 302;
						this.match(EclangParser.T__11);
						this.state = 303;
						(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
						(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
						this.state = 308;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 304;
							this.match(EclangParser.T__4);
							this.state = 305;
							(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
							(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
							}
							}
							this.state = 310;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 311;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 317;
				this.match(EclangParser.T__13);
				this.state = 318;
				this.match(EclangParser.T__16);
				this.state = 319;
				(localctx as ExprUpdateRecordContext)._base = this.expr(0);
				this.state = 326;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 320;
					this.match(EclangParser.T__4);
					this.state = 321;
					(localctx as ExprUpdateRecordContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprUpdateRecordContext)._names.push((localctx as ExprUpdateRecordContext)._NAME);
					this.state = 322;
					this.match(EclangParser.T__14);
					this.state = 323;
					(localctx as ExprUpdateRecordContext)._expr = this.expr(0);
					(localctx as ExprUpdateRecordContext)._values.push((localctx as ExprUpdateRecordContext)._expr);
					}
					}
					this.state = 328;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 329;
				this.match(EclangParser.T__15);
				}
				break;
			case 20:
				{
				localctx = new ExprUpdateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 349;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 333;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 33, this._ctx) ) {
					case 1:
						{
						this.state = 331;
						(localctx as ExprUpdateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 332;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 335;
					(localctx as ExprUpdateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 347;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 336;
						this.match(EclangParser.T__11);
						this.state = 337;
						(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
						(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
						this.state = 342;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 338;
							this.match(EclangParser.T__4);
							this.state = 339;
							(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
							(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
							}
							}
							this.state = 344;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 345;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 351;
				this.match(EclangParser.T__13);
				this.state = 352;
				this.match(EclangParser.INDENT);
				this.state = 353;
				this.match(EclangParser.T__16);
				this.state = 354;
				(localctx as ExprUpdateRecordContext)._base = this.expr(0);
				this.state = 362;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 355;
					this.match(EclangParser.T__4);
					this.state = 356;
					this.match(EclangParser.NL);
					this.state = 357;
					(localctx as ExprUpdateRecordContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprUpdateRecordContext)._names.push((localctx as ExprUpdateRecordContext)._NAME);
					this.state = 358;
					this.match(EclangParser.T__14);
					this.state = 359;
					(localctx as ExprUpdateRecordContext)._expr = this.expr(0);
					(localctx as ExprUpdateRecordContext)._values.push((localctx as ExprUpdateRecordContext)._expr);
					}
					}
					this.state = 364;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 365;
				this.match(EclangParser.NL);
				this.state = 366;
				this.match(EclangParser.DEDENT);
				this.state = 367;
				this.match(EclangParser.T__15);
				}
				break;
			case 21:
				{
				localctx = new ExprNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 369;
				this.match(EclangParser.NAME);
				}
				break;
			case 22:
				{
				localctx = new ExprCreateArrayContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 370;
				this.match(EclangParser.T__17);
				this.state = 371;
				this.expr(0);
				this.state = 376;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 372;
					this.match(EclangParser.T__4);
					this.state = 373;
					this.expr(0);
					}
					}
					this.state = 378;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 379;
				_la = this._input.LA(1);
				if(!(_la===19 || _la===20)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				}
				break;
			case 23:
				{
				localctx = new ExprCreateArrayContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 381;
				this.match(EclangParser.T__17);
				this.state = 382;
				this.match(EclangParser.INDENT);
				this.state = 383;
				this.expr(0);
				this.state = 389;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 384;
					this.match(EclangParser.T__4);
					this.state = 385;
					this.match(EclangParser.NL);
					this.state = 386;
					this.expr(0);
					}
					}
					this.state = 391;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 392;
				this.match(EclangParser.NL);
				this.state = 393;
				this.match(EclangParser.DEDENT);
				this.state = 394;
				_la = this._input.LA(1);
				if(!(_la===19 || _la===20)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				}
				break;
			case 24:
				{
				localctx = new ExprUnOpContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 396;
				(localctx as ExprUnOpContext)._op = this.match(EclangParser.T__28);
				this.state = 397;
				this.expr(30);
				}
				break;
			case 25:
				{
				localctx = new ExprAddressOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 398;
				this.match(EclangParser.T__29);
				this.state = 399;
				this.expr(29);
				}
				break;
			case 26:
				{
				localctx = new ExprTryContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 400;
				this.match(EclangParser.T__30);
				this.state = 401;
				this.expr(28);
				}
				break;
			case 27:
				{
				localctx = new ExprNewContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 402;
				this.match(EclangParser.T__31);
				this.state = 403;
				(localctx as ExprNewContext)._value = this.expr(0);
				this.state = 404;
				this.match(EclangParser.T__32);
				this.state = 405;
				(localctx as ExprNewContext)._pool = this.expr(27);
				}
				break;
			case 28:
				{
				localctx = new ExprNewContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 407;
				this.match(EclangParser.T__31);
				this.state = 408;
				(localctx as ExprNewContext)._value = this.expr(26);
				}
				break;
			case 29:
				{
				localctx = new ExprNewArrayOfTypeContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 409;
				this.match(EclangParser.T__31);
				this.state = 410;
				this.match(EclangParser.T__3);
				this.state = 411;
				(localctx as ExprNewArrayOfTypeContext)._elementType = this.type_(0);
				this.state = 412;
				this.match(EclangParser.T__5);
				this.state = 413;
				this.match(EclangParser.T__17);
				this.state = 414;
				(localctx as ExprNewArrayOfTypeContext)._count = this.expr(0);
				this.state = 415;
				this.match(EclangParser.T__19);
				this.state = 418;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 40, this._ctx) ) {
				case 1:
					{
					this.state = 416;
					this.match(EclangParser.T__32);
					this.state = 417;
					(localctx as ExprNewArrayOfTypeContext)._pool = this.expr(0);
					}
					break;
				}
				}
				break;
			case 30:
				{
				localctx = new ExprUnOpContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 420;
				(localctx as ExprUnOpContext)._op = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(((((_la - 25)) & ~0x1F) === 0 && ((1 << (_la - 25)) & 15361) !== 0))) {
				    (localctx as ExprUnOpContext)._op = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 421;
				this.expr(23);
				}
				break;
			case 31:
				{
				localctx = new ExprInlineIfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 422;
				this.match(EclangParser.T__62);
				this.state = 423;
				(localctx as ExprInlineIfContext)._condition = this.expr(0);
				this.state = 424;
				this.match(EclangParser.T__63);
				this.state = 425;
				(localctx as ExprInlineIfContext)._body = this.expr(0);
				this.state = 426;
				this.match(EclangParser.T__64);
				this.state = 427;
				(localctx as ExprInlineIfContext)._elseBody = this.expr(5);
				}
				break;
			case 32:
				{
				localctx = new ExprInlineIfOldContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 429;
				this.match(EclangParser.T__62);
				this.state = 430;
				this.match(EclangParser.T__3);
				this.state = 431;
				(localctx as ExprInlineIfOldContext)._condition = this.expr(0);
				this.state = 432;
				this.match(EclangParser.T__5);
				this.state = 433;
				(localctx as ExprInlineIfOldContext)._body = this.expr(0);
				this.state = 434;
				this.match(EclangParser.T__64);
				this.state = 435;
				(localctx as ExprInlineIfOldContext)._elseBody = this.expr(4);
				}
				break;
			case 33:
				{
				localctx = new ExprWhenContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 437;
				this.whenExpr();
				}
				break;
			case 34:
				{
				localctx = new ExprLambdaContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 438;
				this.match(EclangParser.T__9);
				this.state = 439;
				this.match(EclangParser.T__3);
				this.state = 448;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 440;
					(localctx as ExprLambdaContext)._lambdaParameter = this.lambdaParameter();
					(localctx as ExprLambdaContext)._params.push((localctx as ExprLambdaContext)._lambdaParameter);
					this.state = 445;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===5) {
						{
						{
						this.state = 441;
						this.match(EclangParser.T__4);
						this.state = 442;
						(localctx as ExprLambdaContext)._lambdaParameter = this.lambdaParameter();
						(localctx as ExprLambdaContext)._params.push((localctx as ExprLambdaContext)._lambdaParameter);
						}
						}
						this.state = 447;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					}
				}

				this.state = 450;
				this.match(EclangParser.T__5);
				this.state = 453;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 451;
					this.match(EclangParser.T__66);
					this.state = 452;
					(localctx as ExprLambdaContext)._returnType = this.type_(0);
					}
				}

				this.state = 455;
				this.match(EclangParser.T__14);
				this.state = 456;
				(localctx as ExprLambdaContext)._body = this.expr(1);
				}
				break;
			}
			this._ctx.stop = this._input.LT(-1);
			this.state = 641;
			this._errHandler.sync(this);
			_alt = this._interp.adaptivePredict(this._input, 52, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					if (this._parseListeners != null) {
						this.triggerExitRuleEvent();
					}
					_prevctx = localctx;
					{
					this.state = 639;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 51, this._ctx) ) {
					case 1:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 459;
						if (!(this.precpred(this._ctx, 24))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 24)");
						}
						this.state = 460;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__33);
						this.state = 461;
						(localctx as ExprBinOpContext)._right = this.expr(24);
						}
						break;
					case 2:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 462;
						if (!(this.precpred(this._ctx, 22))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 22)");
						}
						this.state = 463;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(((((_la - 39)) & ~0x1F) === 0 && ((1 << (_la - 39)) & 7) !== 0))) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 464;
						(localctx as ExprBinOpContext)._right = this.expr(23);
						}
						break;
					case 3:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 465;
						if (!(this.precpred(this._ctx, 21))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 21)");
						}
						this.state = 466;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===38 || _la===42)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 467;
						(localctx as ExprBinOpContext)._right = this.expr(22);
						}
						break;
					case 4:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 468;
						if (!(this.precpred(this._ctx, 20))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 20)");
						}
						this.state = 472;
						this._errHandler.sync(this);
						switch (this._input.LA(1)) {
						case 43:
							{
							this.state = 469;
							(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__42);
							}
							break;
						case 13:
							{
							this.state = 470;
							(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__12);
							this.state = 471;
							(localctx as ExprBinOpContext)._shr = this.match(EclangParser.T__12);
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						this.state = 474;
						(localctx as ExprBinOpContext)._right = this.expr(21);
						}
						break;
					case 5:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 475;
						if (!(this.precpred(this._ctx, 19))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 19)");
						}
						this.state = 476;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__43);
						this.state = 477;
						(localctx as ExprBinOpContext)._right = this.expr(20);
						}
						break;
					case 6:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 478;
						if (!(this.precpred(this._ctx, 18))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 18)");
						}
						this.state = 479;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__44);
						this.state = 480;
						(localctx as ExprBinOpContext)._right = this.expr(19);
						}
						break;
					case 7:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 481;
						if (!(this.precpred(this._ctx, 17))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 17)");
						}
						this.state = 482;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__45);
						this.state = 483;
						(localctx as ExprBinOpContext)._right = this.expr(18);
						}
						break;
					case 8:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 484;
						if (!(this.precpred(this._ctx, 16))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 16)");
						}
						this.state = 485;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(((((_la - 23)) & ~0x1F) === 0 && ((1 << (_la - 23)) & 16777219) !== 0))) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 486;
						(localctx as ExprBinOpContext)._right = this.expr(17);
						}
						break;
					case 9:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 487;
						if (!(this.precpred(this._ctx, 15))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 15)");
						}
						this.state = 488;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===12 || _la===13 || _la===48 || _la===49)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 489;
						(localctx as ExprBinOpContext)._right = this.expr(16);
						}
						break;
					case 10:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 490;
						if (!(this.precpred(this._ctx, 11))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 11)");
						}
						this.state = 491;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(((((_la - 52)) & ~0x1F) === 0 && ((1 << (_la - 52)) & 15) !== 0))) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 492;
						(localctx as ExprBinOpContext)._right = this.expr(12);
						}
						break;
					case 11:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 493;
						if (!(this.precpred(this._ctx, 10))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 10)");
						}
						this.state = 494;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===56 || _la===57)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 495;
						(localctx as ExprBinOpContext)._right = this.expr(11);
						}
						break;
					case 12:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 496;
						if (!(this.precpred(this._ctx, 9))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 9)");
						}
						this.state = 497;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===58 || _la===59)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 498;
						(localctx as ExprBinOpContext)._right = this.expr(10);
						}
						break;
					case 13:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 499;
						if (!(this.precpred(this._ctx, 8))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 8)");
						}
						this.state = 500;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===60 || _la===61)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 501;
						(localctx as ExprBinOpContext)._right = this.expr(9);
						}
						break;
					case 14:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 502;
						if (!(this.precpred(this._ctx, 7))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 7)");
						}
						this.state = 503;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__61);
						this.state = 504;
						(localctx as ExprBinOpContext)._right = this.expr(7);
						}
						break;
					case 15:
						{
						localctx = new ExprPipeContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprPipeContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 505;
						if (!(this.precpred(this._ctx, 3))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 3)");
						}
						this.state = 506;
						this.match(EclangParser.T__65);
						this.state = 507;
						(localctx as ExprPipeContext)._right = this.expr(4);
						}
						break;
					case 16:
						{
						localctx = new ExprFunctionCallNoArgsContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprFunctionCallNoArgsContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 508;
						if (!(this.precpred(this._ctx, 46))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 46)");
						}
						this.state = 509;
						this.match(EclangParser.T__10);
						this.state = 510;
						this.match(EclangParser.NAME);
						this.state = 511;
						this.match(EclangParser.T__3);
						this.state = 512;
						this.match(EclangParser.T__5);
						}
						break;
					case 17:
						{
						localctx = new ExprFunctionCallWithArgsContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprFunctionCallWithArgsContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 513;
						if (!(this.precpred(this._ctx, 45))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 45)");
						}
						this.state = 514;
						this.match(EclangParser.T__10);
						this.state = 515;
						this.match(EclangParser.NAME);
						this.state = 516;
						this.match(EclangParser.T__3);
						this.state = 517;
						(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
						(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
						this.state = 522;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 518;
							this.match(EclangParser.T__4);
							this.state = 519;
							(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
							(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
							}
							}
							this.state = 524;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 525;
						this.match(EclangParser.T__5);
						}
						break;
					case 18:
						{
						localctx = new ExprFunctionCallWithArgsContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprFunctionCallWithArgsContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 527;
						if (!(this.precpred(this._ctx, 44))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 44)");
						}
						this.state = 528;
						this.match(EclangParser.T__10);
						this.state = 529;
						this.match(EclangParser.NAME);
						this.state = 530;
						this.match(EclangParser.T__3);
						this.state = 531;
						this.match(EclangParser.INDENT);
						this.state = 532;
						(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
						(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
						this.state = 538;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 533;
							this.match(EclangParser.T__4);
							this.state = 534;
							this.match(EclangParser.NL);
							this.state = 535;
							(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
							(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
							}
							}
							this.state = 540;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 541;
						this.match(EclangParser.NL);
						this.state = 542;
						this.match(EclangParser.DEDENT);
						this.state = 543;
						this.match(EclangParser.T__5);
						}
						break;
					case 19:
						{
						localctx = new ExprSafeCallContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSafeCallContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 545;
						if (!(this.precpred(this._ctx, 43))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 43)");
						}
						this.state = 546;
						this.match(EclangParser.T__20);
						this.state = 547;
						this.match(EclangParser.NAME);
						this.state = 548;
						this.match(EclangParser.T__3);
						this.state = 549;
						this.match(EclangParser.T__5);
						}
						break;
					case 20:
						{
						localctx = new ExprSafeCallContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSafeCallContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 550;
						if (!(this.precpred(this._ctx, 42))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 42)");
						}
						this.state = 551;
						this.match(EclangParser.T__20);
						this.state = 552;
						this.match(EclangParser.NAME);
						this.state = 553;
						this.match(EclangParser.T__3);
						this.state = 554;
						(localctx as ExprSafeCallContext)._argument = this.argument();
						(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
						this.state = 559;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 555;
							this.match(EclangParser.T__4);
							this.state = 556;
							(localctx as ExprSafeCallContext)._argument = this.argument();
							(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
							}
							}
							this.state = 561;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 562;
						this.match(EclangParser.T__5);
						}
						break;
					case 21:
						{
						localctx = new ExprSafeCallContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSafeCallContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 564;
						if (!(this.precpred(this._ctx, 41))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 41)");
						}
						this.state = 565;
						this.match(EclangParser.T__20);
						this.state = 566;
						this.match(EclangParser.NAME);
						this.state = 567;
						this.match(EclangParser.T__3);
						this.state = 568;
						this.match(EclangParser.INDENT);
						this.state = 569;
						(localctx as ExprSafeCallContext)._argument = this.argument();
						(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
						this.state = 575;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 570;
							this.match(EclangParser.T__4);
							this.state = 571;
							this.match(EclangParser.NL);
							this.state = 572;
							(localctx as ExprSafeCallContext)._argument = this.argument();
							(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
							}
							}
							this.state = 577;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 578;
						this.match(EclangParser.NL);
						this.state = 579;
						this.match(EclangParser.DEDENT);
						this.state = 580;
						this.match(EclangParser.T__5);
						}
						break;
					case 22:
						{
						localctx = new ExprAccessNameContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessNameContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 582;
						if (!(this.precpred(this._ctx, 40))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 40)");
						}
						this.state = 583;
						this.match(EclangParser.T__10);
						this.state = 584;
						this.match(EclangParser.NAME);
						}
						break;
					case 23:
						{
						localctx = new ExprAccessNameNullishCoalescingContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessNameNullishCoalescingContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 585;
						if (!(this.precpred(this._ctx, 39))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 39)");
						}
						this.state = 586;
						this.match(EclangParser.T__20);
						this.state = 587;
						this.match(EclangParser.NAME);
						}
						break;
					case 24:
						{
						localctx = new ExprAccessorContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessorContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 588;
						if (!(this.precpred(this._ctx, 38))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 38)");
						}
						this.state = 589;
						this.match(EclangParser.T__17);
						this.state = 590;
						(localctx as ExprAccessorContext)._index = this.expr(0);
						this.state = 591;
						this.match(EclangParser.T__19);
						}
						break;
					case 25:
						{
						localctx = new ExprAccessorContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessorContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 593;
						if (!(this.precpred(this._ctx, 37))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 37)");
						}
						this.state = 594;
						this.match(EclangParser.T__17);
						this.state = 595;
						this.match(EclangParser.INDENT);
						this.state = 596;
						(localctx as ExprAccessorContext)._index = this.expr(0);
						this.state = 597;
						this.match(EclangParser.NL);
						this.state = 598;
						this.match(EclangParser.DEDENT);
						this.state = 599;
						this.match(EclangParser.T__19);
						}
						break;
					case 26:
						{
						localctx = new ExprSliceOpenContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSliceOpenContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 601;
						if (!(this.precpred(this._ctx, 36))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 36)");
						}
						this.state = 602;
						this.match(EclangParser.T__17);
						this.state = 604;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						if ((((_la) & ~0x1F) === 0 && ((1 << _la) & 3791931294) !== 0) || ((((_la - 32)) & ~0x1F) === 0 && ((1 << (_la - 32)) & 2147483769) !== 0) || ((((_la - 98)) & ~0x1F) === 0 && ((1 << (_la - 98)) & 267911169) !== 0)) {
							{
							this.state = 603;
							(localctx as ExprSliceOpenContext)._from_ = this.expr(0);
							}
						}

						this.state = 606;
						this.match(EclangParser.T__21);
						this.state = 607;
						this.match(EclangParser.T__19);
						}
						break;
					case 27:
						{
						localctx = new ExprSliceOpenContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSliceOpenContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 608;
						if (!(this.precpred(this._ctx, 35))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 35)");
						}
						this.state = 609;
						this.match(EclangParser.T__17);
						this.state = 610;
						(localctx as ExprSliceOpenContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===23 || _la===24)) {
						    (localctx as ExprSliceOpenContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 611;
						(localctx as ExprSliceOpenContext)._to = this.expr(0);
						this.state = 612;
						this.match(EclangParser.T__19);
						}
						break;
					case 28:
						{
						localctx = new ExprCastNotNullContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 614;
						if (!(this.precpred(this._ctx, 34))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 34)");
						}
						this.state = 615;
						this.match(EclangParser.T__24);
						}
						break;
					case 29:
						{
						localctx = new ExprAsContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 616;
						if (!(this.precpred(this._ctx, 33))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 33)");
						}
						this.state = 617;
						this.match(EclangParser.T__25);
						this.state = 618;
						this.type_(0);
						}
						break;
					case 30:
						{
						localctx = new ExprAsUnsafeContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 619;
						if (!(this.precpred(this._ctx, 32))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 32)");
						}
						this.state = 620;
						this.match(EclangParser.T__25);
						this.state = 621;
						this.match(EclangParser.T__26);
						this.state = 622;
						this.type_(0);
						}
						break;
					case 31:
						{
						localctx = new ExprAsUnsafeContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 623;
						if (!(this.precpred(this._ctx, 31))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 31)");
						}
						this.state = 624;
						this.match(EclangParser.T__27);
						this.state = 625;
						this.type_(0);
						}
						break;
					case 32:
						{
						localctx = new ExprIsContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 626;
						if (!(this.precpred(this._ctx, 14))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 14)");
						}
						this.state = 627;
						this.match(EclangParser.T__49);
						this.state = 628;
						this.type_(0);
						}
						break;
					case 33:
						{
						localctx = new ExprIsNotContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 629;
						if (!(this.precpred(this._ctx, 13))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 13)");
						}
						this.state = 630;
						this.match(EclangParser.T__50);
						this.state = 631;
						this.type_(0);
						}
						break;
					case 34:
						{
						localctx = new ExprIsNotContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 632;
						if (!(this.precpred(this._ctx, 12))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 12)");
						}
						this.state = 633;
						this.match(EclangParser.T__49);
						this.state = 634;
						this.match(EclangParser.T__34);
						this.state = 635;
						this.type_(0);
						}
						break;
					case 35:
						{
						localctx = new ExprNullishJumpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprNullishJumpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 636;
						if (!(this.precpred(this._ctx, 6))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 6)");
						}
						this.state = 637;
						this.match(EclangParser.T__61);
						this.state = 638;
						this.jump();
						}
						break;
					}
					}
				}
				this.state = 643;
				this._errHandler.sync(this);
				_alt = this._interp.adaptivePredict(this._input, 52, this._ctx);
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.unrollRecursionContexts(_parentctx);
		}
		return localctx;
	}
	// @RuleVersion(0)
	public argument(): ArgumentContext {
		let localctx: ArgumentContext = new ArgumentContext(this, this._ctx, this.state);
		this.enterRule(localctx, 8, EclangParser.RULE_argument);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 646;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 53, this._ctx) ) {
			case 1:
				{
				this.state = 644;
				localctx._name = this.match(EclangParser.NAME);
				this.state = 645;
				this.match(EclangParser.T__14);
				}
				break;
			}
			this.state = 648;
			this.expr(0);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public jump(): JumpContext {
		let localctx: JumpContext = new JumpContext(this, this._ctx, this.state);
		this.enterRule(localctx, 10, EclangParser.RULE_jump);
		try {
			this.state = 664;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 68:
				localctx = new JumpReturnContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 650;
				this.match(EclangParser.T__67);
				this.state = 652;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 54, this._ctx) ) {
				case 1:
					{
					this.state = 651;
					(localctx as JumpReturnContext)._value = this.expr(0);
					}
					break;
				}
				}
				break;
			case 69:
				localctx = new JumpThrowContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 654;
				this.match(EclangParser.T__68);
				this.state = 655;
				(localctx as JumpThrowContext)._value = this.expr(0);
				}
				break;
			case 70:
				localctx = new JumpBreakContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 656;
				this.match(EclangParser.T__69);
				this.state = 658;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 55, this._ctx) ) {
				case 1:
					{
					this.state = 657;
					(localctx as JumpBreakContext)._label = this.match(EclangParser.NAME);
					}
					break;
				}
				}
				break;
			case 71:
				localctx = new JumpContinueContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 660;
				this.match(EclangParser.T__70);
				this.state = 662;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 56, this._ctx) ) {
				case 1:
					{
					this.state = 661;
					(localctx as JumpContinueContext)._label = this.match(EclangParser.NAME);
					}
					break;
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public tail(): TailContext {
		let localctx: TailContext = new TailContext(this, this._ctx, this.state);
		this.enterRule(localctx, 12, EclangParser.RULE_tail);
		try {
			this.state = 670;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 58, this._ctx) ) {
			case 1:
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 666;
				this.expr(0);
				this.state = 667;
				this.match(EclangParser.NL);
				}
				break;
			case 2:
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 669;
				this.whenExpr();
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public lambdaParameter(): LambdaParameterContext {
		let localctx: LambdaParameterContext = new LambdaParameterContext(this, this._ctx, this.state);
		this.enterRule(localctx, 14, EclangParser.RULE_lambdaParameter);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 672;
			this.match(EclangParser.NAME);
			this.state = 675;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===67) {
				{
				this.state = 673;
				this.match(EclangParser.T__66);
				this.state = 674;
				this.type_(0);
				}
			}

			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}

	public type_(): TypeContext;
	public type_(_p: number): TypeContext;
	// @RuleVersion(0)
	public type_(_p?: number): TypeContext {
		if (_p === undefined) {
			_p = 0;
		}

		let _parentctx: ParserRuleContext = this._ctx;
		let _parentState: number = this.state;
		let localctx: TypeContext = new TypeContext(this, this._ctx, _parentState);
		let _prevctx: TypeContext = localctx;
		let _startState: number = 16;
		this.enterRecursionRule(localctx, 16, EclangParser.RULE_type, _p);
		let _la: number;
		try {
			let _alt: number;
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 786;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 72, this._ctx) ) {
			case 1:
				{
				localctx = new TypeByNameParametersContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;

				this.state = 680;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 60, this._ctx) ) {
				case 1:
					{
					this.state = 678;
					(localctx as TypeByNameParametersContext)._qualifier = this.match(EclangParser.NAME);
					this.state = 679;
					this.match(EclangParser.T__10);
					}
					break;
				}
				this.state = 682;
				(localctx as TypeByNameParametersContext)._name = this.match(EclangParser.NAME);
				this.state = 683;
				this.match(EclangParser.T__11);
				this.state = 684;
				(localctx as TypeByNameParametersContext)._type_ = this.type_(0);
				(localctx as TypeByNameParametersContext)._args.push((localctx as TypeByNameParametersContext)._type_);
				this.state = 689;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 685;
					this.match(EclangParser.T__4);
					this.state = 686;
					(localctx as TypeByNameParametersContext)._type_ = this.type_(0);
					(localctx as TypeByNameParametersContext)._args.push((localctx as TypeByNameParametersContext)._type_);
					}
					}
					this.state = 691;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 692;
				this.match(EclangParser.T__12);
				}
				break;
			case 2:
				{
				localctx = new TypeByNameContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 694;
				this.match(EclangParser.NAME);
				this.state = 695;
				this.match(EclangParser.T__10);
				this.state = 696;
				this.match(EclangParser.NAME);
				}
				break;
			case 3:
				{
				localctx = new TypeByNameContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 697;
				this.match(EclangParser.NAME);
				}
				break;
			case 4:
				{
				localctx = new TypeAddressByNameContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 698;
				this.match(EclangParser.T__28);
				this.state = 699;
				this.type_(16);
				}
				break;
			case 5:
				{
				localctx = new TypeUnionAnonContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 700;
				this.match(EclangParser.T__72);
				this.state = 701;
				(localctx as TypeUnionAnonContext)._type_ = this.type_(0);
				(localctx as TypeUnionAnonContext)._types.push((localctx as TypeUnionAnonContext)._type_);
				this.state = 702;
				this.match(EclangParser.T__45);
				this.state = 703;
				(localctx as TypeUnionAnonContext)._type_ = this.type_(0);
				(localctx as TypeUnionAnonContext)._types.push((localctx as TypeUnionAnonContext)._type_);
				this.state = 708;
				this._errHandler.sync(this);
				_alt = this._interp.adaptivePredict(this._input, 62, this._ctx);
				while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
					if (_alt === 1) {
						{
						{
						this.state = 704;
						this.match(EclangParser.T__45);
						this.state = 705;
						(localctx as TypeUnionAnonContext)._type_ = this.type_(0);
						(localctx as TypeUnionAnonContext)._types.push((localctx as TypeUnionAnonContext)._type_);
						}
						}
					}
					this.state = 710;
					this._errHandler.sync(this);
					_alt = this._interp.adaptivePredict(this._input, 62, this._ctx);
				}
				}
				break;
			case 6:
				{
				localctx = new TypeNestContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 711;
				this.match(EclangParser.T__3);
				this.state = 712;
				this.type_(0);
				this.state = 713;
				this.match(EclangParser.T__5);
				}
				break;
			case 7:
				{
				localctx = new TypeAtomContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 715;
				this.match(EclangParser.ATOM);
				}
				break;
			case 8:
				{
				localctx = new TypeGenericContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 716;
				this.match(EclangParser.T__11);
				this.state = 717;
				this.match(EclangParser.NAME);
				this.state = 718;
				this.match(EclangParser.T__12);
				}
				break;
			case 9:
				{
				localctx = new TypeTypeOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 719;
				this.match(EclangParser.T__73);
				this.state = 720;
				this.expr(0);
				}
				break;
			case 10:
				{
				localctx = new TypeCallSignatureContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 721;
				this.match(EclangParser.T__9);
				this.state = 722;
				this.match(EclangParser.T__3);
				this.state = 723;
				this.match(EclangParser.T__5);
				this.state = 726;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 63, this._ctx) ) {
				case 1:
					{
					this.state = 724;
					_la = this._input.LA(1);
					if(!(_la===67 || _la===76)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 725;
					(localctx as TypeCallSignatureContext)._returnType = this.type_(0);
					}
					break;
				}
				}
				break;
			case 11:
				{
				localctx = new TypeCallSignatureContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 728;
				this.match(EclangParser.T__9);
				this.state = 729;
				this.match(EclangParser.T__3);
				this.state = 732;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 64, this._ctx) ) {
				case 1:
					{
					this.state = 730;
					this.match(EclangParser.NAME);
					this.state = 731;
					this.match(EclangParser.T__66);
					}
					break;
				}
				this.state = 734;
				(localctx as TypeCallSignatureContext)._type_ = this.type_(0);
				(localctx as TypeCallSignatureContext)._argTypes.push((localctx as TypeCallSignatureContext)._type_);
				this.state = 743;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 735;
					this.match(EclangParser.T__4);
					this.state = 738;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 65, this._ctx) ) {
					case 1:
						{
						this.state = 736;
						this.match(EclangParser.NAME);
						this.state = 737;
						this.match(EclangParser.T__66);
						}
						break;
					}
					this.state = 740;
					(localctx as TypeCallSignatureContext)._type_ = this.type_(0);
					(localctx as TypeCallSignatureContext)._argTypes.push((localctx as TypeCallSignatureContext)._type_);
					}
					}
					this.state = 745;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 746;
				this.match(EclangParser.T__5);
				this.state = 749;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 67, this._ctx) ) {
				case 1:
					{
					this.state = 747;
					_la = this._input.LA(1);
					if(!(_la===67 || _la===76)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 748;
					(localctx as TypeCallSignatureContext)._returnType = this.type_(0);
					}
					break;
				}
				}
				break;
			case 12:
				{
				localctx = new TypeCallSignatureContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 751;
				this.match(EclangParser.T__3);
				this.state = 752;
				this.match(EclangParser.T__5);
				this.state = 753;
				this.match(EclangParser.T__75);
				this.state = 754;
				(localctx as TypeCallSignatureContext)._returnType = this.type_(3);
				}
				break;
			case 13:
				{
				localctx = new TypeCallSignatureContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 755;
				this.match(EclangParser.T__3);
				this.state = 758;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 68, this._ctx) ) {
				case 1:
					{
					this.state = 756;
					this.match(EclangParser.NAME);
					this.state = 757;
					this.match(EclangParser.T__66);
					}
					break;
				}
				this.state = 760;
				(localctx as TypeCallSignatureContext)._type_ = this.type_(0);
				(localctx as TypeCallSignatureContext)._argTypes.push((localctx as TypeCallSignatureContext)._type_);
				this.state = 769;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 761;
					this.match(EclangParser.T__4);
					this.state = 764;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 69, this._ctx) ) {
					case 1:
						{
						this.state = 762;
						this.match(EclangParser.NAME);
						this.state = 763;
						this.match(EclangParser.T__66);
						}
						break;
					}
					this.state = 766;
					(localctx as TypeCallSignatureContext)._type_ = this.type_(0);
					(localctx as TypeCallSignatureContext)._argTypes.push((localctx as TypeCallSignatureContext)._type_);
					}
					}
					this.state = 771;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 772;
				this.match(EclangParser.T__5);
				this.state = 773;
				this.match(EclangParser.T__75);
				this.state = 774;
				(localctx as TypeCallSignatureContext)._returnType = this.type_(2);
				}
				break;
			case 14:
				{
				localctx = new TypeTupleContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 776;
				this.match(EclangParser.T__3);
				this.state = 777;
				(localctx as TypeTupleContext)._type_ = this.type_(0);
				(localctx as TypeTupleContext)._types.push((localctx as TypeTupleContext)._type_);
				this.state = 780;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 778;
					this.match(EclangParser.T__4);
					this.state = 779;
					(localctx as TypeTupleContext)._type_ = this.type_(0);
					(localctx as TypeTupleContext)._types.push((localctx as TypeTupleContext)._type_);
					}
					}
					this.state = 782;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===5);
				this.state = 784;
				this.match(EclangParser.T__5);
				}
				break;
			}
			this._ctx.stop = this._input.LT(-1);
			this.state = 818;
			this._errHandler.sync(this);
			_alt = this._interp.adaptivePredict(this._input, 76, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					if (this._parseListeners != null) {
						this.triggerExitRuleEvent();
					}
					_prevctx = localctx;
					{
					this.state = 816;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 75, this._ctx) ) {
					case 1:
						{
						localctx = new TypeThrowsContext(this, new TypeContext(this, _parentctx, _parentState));
						(localctx as TypeThrowsContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 788;
						if (!(this.precpred(this._ctx, 6))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 6)");
						}
						this.state = 789;
						this.match(EclangParser.T__74);
						this.state = 790;
						(localctx as TypeThrowsContext)._throwing = this.type_(7);
						}
						break;
					case 2:
						{
						localctx = new TypeNullableContext(this, new TypeContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 791;
						if (!(this.precpred(this._ctx, 15))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 15)");
						}
						this.state = 792;
						this.match(EclangParser.T__71);
						}
						break;
					case 3:
						{
						localctx = new TypeEnumContext(this, new TypeContext(this, _parentctx, _parentState));
						(localctx as TypeEnumContext)._types.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 793;
						if (!(this.precpred(this._ctx, 14))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 14)");
						}
						this.state = 794;
						this.match(EclangParser.T__43);
						this.state = 795;
						(localctx as TypeEnumContext)._type_ = this.type_(0);
						(localctx as TypeEnumContext)._types.push((localctx as TypeEnumContext)._type_);
						this.state = 800;
						this._errHandler.sync(this);
						_alt = this._interp.adaptivePredict(this._input, 73, this._ctx);
						while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
							if (_alt === 1) {
								{
								{
								this.state = 796;
								this.match(EclangParser.T__43);
								this.state = 797;
								(localctx as TypeEnumContext)._type_ = this.type_(0);
								(localctx as TypeEnumContext)._types.push((localctx as TypeEnumContext)._type_);
								}
								}
							}
							this.state = 802;
							this._errHandler.sync(this);
							_alt = this._interp.adaptivePredict(this._input, 73, this._ctx);
						}
						}
						break;
					case 4:
						{
						localctx = new TypeUnionContext(this, new TypeContext(this, _parentctx, _parentState));
						(localctx as TypeUnionContext)._types.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 803;
						if (!(this.precpred(this._ctx, 13))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 13)");
						}
						this.state = 804;
						this.match(EclangParser.T__45);
						this.state = 805;
						(localctx as TypeUnionContext)._type_ = this.type_(0);
						(localctx as TypeUnionContext)._types.push((localctx as TypeUnionContext)._type_);
						this.state = 810;
						this._errHandler.sync(this);
						_alt = this._interp.adaptivePredict(this._input, 74, this._ctx);
						while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
							if (_alt === 1) {
								{
								{
								this.state = 806;
								this.match(EclangParser.T__45);
								this.state = 807;
								(localctx as TypeUnionContext)._type_ = this.type_(0);
								(localctx as TypeUnionContext)._types.push((localctx as TypeUnionContext)._type_);
								}
								}
							}
							this.state = 812;
							this._errHandler.sync(this);
							_alt = this._interp.adaptivePredict(this._input, 74, this._ctx);
						}
						}
						break;
					case 5:
						{
						localctx = new TypeArrayContext(this, new TypeContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 813;
						if (!(this.precpred(this._ctx, 7))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 7)");
						}
						this.state = 814;
						this.match(EclangParser.T__17);
						this.state = 815;
						_la = this._input.LA(1);
						if(!(_la===19 || _la===20)) {
						this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						}
						break;
					}
					}
				}
				this.state = 820;
				this._errHandler.sync(this);
				_alt = this._interp.adaptivePredict(this._input, 76, this._ctx);
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.unrollRecursionContexts(_parentctx);
		}
		return localctx;
	}
	// @RuleVersion(0)
	public interface_(): InterfaceContext {
		let localctx: InterfaceContext = new InterfaceContext(this, this._ctx, this.state);
		this.enterRule(localctx, 18, EclangParser.RULE_interface);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 821;
			this.match(EclangParser.T__76);
			this.state = 822;
			localctx._name = this.match(EclangParser.NAME);
			this.state = 823;
			this.match(EclangParser.T__25);
			this.state = 824;
			this.match(EclangParser.INDENT);
			this.state = 830;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 825;
				localctx._NAME = this.match(EclangParser.NAME);
				localctx._names.push(localctx._NAME);
				this.state = 826;
				this.match(EclangParser.T__66);
				this.state = 827;
				localctx._type_ = this.type_(0);
				localctx._types.push(localctx._type_);
				this.state = 828;
				this.match(EclangParser.NL);
				}
				}
				this.state = 832;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while (_la===118);
			this.state = 834;
			this.match(EclangParser.DEDENT);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public parameter(): ParameterContext {
		let localctx: ParameterContext = new ParameterContext(this, this._ctx, this.state);
		this.enterRule(localctx, 20, EclangParser.RULE_parameter);
		try {
			this.state = 859;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 78, this._ctx) ) {
			case 1:
				localctx = new ParameterTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 836;
				this.match(EclangParser.NAME);
				this.state = 837;
				this.match(EclangParser.T__66);
				this.state = 838;
				this.type_(0);
				}
				break;
			case 2:
				localctx = new ParameterTypedDefaultContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 839;
				this.match(EclangParser.NAME);
				this.state = 840;
				this.match(EclangParser.T__66);
				this.state = 841;
				this.type_(0);
				this.state = 842;
				this.match(EclangParser.T__14);
				this.state = 843;
				this.expr(0);
				}
				break;
			case 3:
				localctx = new ParameterDefaultContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 845;
				this.match(EclangParser.NAME);
				this.state = 846;
				this.match(EclangParser.T__14);
				this.state = 847;
				this.expr(0);
				}
				break;
			case 4:
				localctx = new ParameterVarArgContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 848;
				this.match(EclangParser.T__16);
				this.state = 849;
				this.match(EclangParser.NAME);
				this.state = 850;
				this.match(EclangParser.T__66);
				this.state = 851;
				this.type_(0);
				}
				break;
			case 5:
				localctx = new ParameterVarArgCContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 852;
				this.match(EclangParser.T__16);
				}
				break;
			case 6:
				localctx = new ParameterVariableContext(this, localctx);
				this.enterOuterAlt(localctx, 6);
				{
				this.state = 853;
				this.match(EclangParser.T__77);
				this.state = 854;
				this.parameter();
				}
				break;
			case 7:
				localctx = new ParameterConstContext(this, localctx);
				this.enterOuterAlt(localctx, 7);
				{
				this.state = 855;
				this.match(EclangParser.T__78);
				this.state = 856;
				this.parameter();
				}
				break;
			case 8:
				localctx = new ParameterVolatileContext(this, localctx);
				this.enterOuterAlt(localctx, 8);
				{
				this.state = 857;
				this.match(EclangParser.T__79);
				this.state = 858;
				this.parameter();
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public statement(): StatementContext {
		let localctx: StatementContext = new StatementContext(this, this._ctx, this.state);
		this.enterRule(localctx, 22, EclangParser.RULE_statement);
		let _la: number;
		try {
			let _alt: number;
			this.state = 1247;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 114, this._ctx) ) {
			case 1:
				localctx = new StatementDefineVarContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 861;
				this.match(EclangParser.T__77);
				this.state = 862;
				this.match(EclangParser.NAME);
				this.state = 863;
				this.match(EclangParser.T__14);
				this.state = 864;
				this.tail();
				}
				break;
			case 2:
				localctx = new StatementDefineValContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 865;
				this.match(EclangParser.T__78);
				this.state = 866;
				this.match(EclangParser.NAME);
				this.state = 867;
				this.match(EclangParser.T__14);
				this.state = 868;
				this.tail();
				}
				break;
			case 3:
				localctx = new StatementDefineVolContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 869;
				this.match(EclangParser.T__79);
				this.state = 870;
				this.match(EclangParser.NAME);
				this.state = 871;
				this.match(EclangParser.T__14);
				this.state = 872;
				this.tail();
				}
				break;
			case 4:
				localctx = new StatementDefineConstContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 873;
				this.match(EclangParser.T__80);
				this.state = 874;
				this.match(EclangParser.NAME);
				this.state = 875;
				this.match(EclangParser.T__14);
				this.state = 876;
				this.expr(0);
				this.state = 877;
				this.match(EclangParser.NL);
				}
				break;
			case 5:
				localctx = new StatementDefineVarTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 879;
				this.match(EclangParser.T__77);
				this.state = 880;
				this.match(EclangParser.NAME);
				this.state = 881;
				this.match(EclangParser.T__66);
				this.state = 882;
				this.type_(0);
				this.state = 883;
				this.match(EclangParser.T__14);
				this.state = 884;
				this.tail();
				}
				break;
			case 6:
				localctx = new StatementDefineValTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 6);
				{
				this.state = 886;
				this.match(EclangParser.T__78);
				this.state = 887;
				this.match(EclangParser.NAME);
				this.state = 888;
				this.match(EclangParser.T__66);
				this.state = 889;
				this.type_(0);
				this.state = 890;
				this.match(EclangParser.T__14);
				this.state = 891;
				this.tail();
				}
				break;
			case 7:
				localctx = new StatementDefineVolTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 7);
				{
				this.state = 893;
				this.match(EclangParser.T__79);
				this.state = 894;
				this.match(EclangParser.NAME);
				this.state = 895;
				this.match(EclangParser.T__66);
				this.state = 896;
				this.type_(0);
				this.state = 897;
				this.match(EclangParser.T__14);
				this.state = 898;
				this.tail();
				}
				break;
			case 8:
				localctx = new StatementDefineConstTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 8);
				{
				this.state = 900;
				this.match(EclangParser.T__80);
				this.state = 901;
				this.match(EclangParser.NAME);
				this.state = 902;
				this.match(EclangParser.T__66);
				this.state = 903;
				this.type_(0);
				this.state = 904;
				this.match(EclangParser.T__14);
				this.state = 905;
				this.expr(0);
				this.state = 906;
				this.match(EclangParser.NL);
				}
				break;
			case 9:
				localctx = new StatementDeclareVarContext(this, localctx);
				this.enterOuterAlt(localctx, 9);
				{
				this.state = 908;
				this.match(EclangParser.T__77);
				this.state = 909;
				this.match(EclangParser.NAME);
				this.state = 910;
				this.match(EclangParser.T__66);
				this.state = 911;
				this.type_(0);
				this.state = 912;
				this.match(EclangParser.NL);
				}
				break;
			case 10:
				localctx = new StatementDestructureContext(this, localctx);
				this.enterOuterAlt(localctx, 10);
				{
				this.state = 914;
				(localctx as StatementDestructureContext)._kind = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===78 || _la===79)) {
				    (localctx as StatementDestructureContext)._kind = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 915;
				this.destructure();
				this.state = 918;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 916;
					this.match(EclangParser.T__66);
					this.state = 917;
					this.type_(0);
					}
				}

				this.state = 920;
				this.match(EclangParser.T__14);
				this.state = 921;
				this.tail();
				}
				break;
			case 11:
				localctx = new StatementAssignmentContext(this, localctx);
				this.enterOuterAlt(localctx, 11);
				{
				this.state = 923;
				(localctx as StatementAssignmentContext)._target = this.expr(0);
				this.state = 924;
				(localctx as StatementAssignmentContext)._op = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===15 || ((((_la - 82)) & ~0x1F) === 0 && ((1 << (_la - 82)) & 1023) !== 0))) {
				    (localctx as StatementAssignmentContext)._op = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 925;
				(localctx as StatementAssignmentContext)._value = this.tail();
				}
				break;
			case 12:
				localctx = new StatementBreakContext(this, localctx);
				this.enterOuterAlt(localctx, 12);
				{
				this.state = 927;
				this.match(EclangParser.T__69);
				this.state = 929;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 928;
					(localctx as StatementBreakContext)._label = this.match(EclangParser.NAME);
					}
				}

				this.state = 933;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 931;
					this.match(EclangParser.T__62);
					this.state = 932;
					(localctx as StatementBreakContext)._condition = this.expr(0);
					}
				}

				this.state = 935;
				this.match(EclangParser.NL);
				}
				break;
			case 13:
				localctx = new StatementContinueContext(this, localctx);
				this.enterOuterAlt(localctx, 13);
				{
				this.state = 936;
				this.match(EclangParser.T__70);
				this.state = 938;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 937;
					(localctx as StatementContinueContext)._label = this.match(EclangParser.NAME);
					}
				}

				this.state = 942;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 940;
					this.match(EclangParser.T__62);
					this.state = 941;
					(localctx as StatementContinueContext)._condition = this.expr(0);
					}
				}

				this.state = 944;
				this.match(EclangParser.NL);
				}
				break;
			case 14:
				localctx = new StatementPassContext(this, localctx);
				this.enterOuterAlt(localctx, 14);
				{
				this.state = 945;
				this.match(EclangParser.T__91);
				this.state = 948;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 946;
					this.match(EclangParser.T__62);
					this.state = 947;
					(localctx as StatementPassContext)._condition = this.expr(0);
					}
				}

				this.state = 950;
				this.match(EclangParser.NL);
				}
				break;
			case 15:
				localctx = new StatementYieldContext(this, localctx);
				this.enterOuterAlt(localctx, 15);
				{
				this.state = 951;
				this.match(EclangParser.T__92);
				this.state = 954;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 952;
					this.match(EclangParser.T__62);
					this.state = 953;
					(localctx as StatementYieldContext)._condition = this.expr(0);
					}
				}

				this.state = 956;
				this.match(EclangParser.NL);
				}
				break;
			case 16:
				localctx = new StatementThrowContext(this, localctx);
				this.enterOuterAlt(localctx, 16);
				{
				this.state = 957;
				this.match(EclangParser.T__68);
				this.state = 958;
				(localctx as StatementThrowContext)._throw_ = this.expr(0);
				this.state = 961;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 959;
					this.match(EclangParser.T__62);
					this.state = 960;
					(localctx as StatementThrowContext)._condition = this.expr(0);
					}
				}

				this.state = 963;
				this.match(EclangParser.NL);
				}
				break;
			case 17:
				localctx = new StatementAssertContext(this, localctx);
				this.enterOuterAlt(localctx, 17);
				{
				this.state = 965;
				this.match(EclangParser.T__93);
				this.state = 966;
				(localctx as StatementAssertContext)._condition = this.expr(0);
				this.state = 969;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===5) {
					{
					this.state = 967;
					this.match(EclangParser.T__4);
					this.state = 968;
					(localctx as StatementAssertContext)._message = this.match(EclangParser.STRING);
					}
				}

				this.state = 971;
				this.match(EclangParser.NL);
				}
				break;
			case 18:
				localctx = new StatementReturnContext(this, localctx);
				this.enterOuterAlt(localctx, 18);
				{
				this.state = 973;
				this.match(EclangParser.T__67);
				this.state = 974;
				(localctx as StatementReturnContext)._return_ = this.expr(0);
				this.state = 977;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 975;
					this.match(EclangParser.T__62);
					this.state = 976;
					(localctx as StatementReturnContext)._condition = this.expr(0);
					}
				}

				this.state = 979;
				this.match(EclangParser.NL);
				}
				break;
			case 19:
				localctx = new StatementReturnContext(this, localctx);
				this.enterOuterAlt(localctx, 19);
				{
				this.state = 981;
				this.match(EclangParser.T__67);
				this.state = 982;
				this.whenExpr();
				}
				break;
			case 20:
				localctx = new StatementTailIfContext(this, localctx);
				this.enterOuterAlt(localctx, 20);
				{
				this.state = 983;
				(localctx as StatementTailIfContext)._expression = this.expr(0);
				this.state = 984;
				this.match(EclangParser.T__62);
				this.state = 985;
				(localctx as StatementTailIfContext)._condition = this.expr(0);
				this.state = 986;
				this.match(EclangParser.NL);
				}
				break;
			case 21:
				localctx = new StatementTailWhileContext(this, localctx);
				this.enterOuterAlt(localctx, 21);
				{
				this.state = 988;
				(localctx as StatementTailWhileContext)._expression = this.expr(0);
				this.state = 989;
				this.match(EclangParser.T__94);
				this.state = 990;
				(localctx as StatementTailWhileContext)._condition = this.expr(0);
				this.state = 991;
				this.match(EclangParser.NL);
				}
				break;
			case 22:
				localctx = new StatementDeferContext(this, localctx);
				this.enterOuterAlt(localctx, 22);
				{
				this.state = 993;
				this.match(EclangParser.T__95);
				this.state = 994;
				this.statement();
				}
				break;
			case 23:
				localctx = new StatementDeferDoContext(this, localctx);
				this.enterOuterAlt(localctx, 23);
				{
				this.state = 995;
				this.match(EclangParser.T__95);
				this.state = 996;
				this.match(EclangParser.T__63);
				this.state = 997;
				(localctx as StatementDeferDoContext)._body = this.block();
				}
				break;
			case 24:
				localctx = new StatementErrDeferContext(this, localctx);
				this.enterOuterAlt(localctx, 24);
				{
				this.state = 998;
				this.match(EclangParser.T__96);
				this.state = 999;
				this.statement();
				}
				break;
			case 25:
				localctx = new StatementErrDeferDoContext(this, localctx);
				this.enterOuterAlt(localctx, 25);
				{
				this.state = 1000;
				this.match(EclangParser.T__96);
				this.state = 1001;
				this.match(EclangParser.T__63);
				this.state = 1002;
				(localctx as StatementErrDeferDoContext)._body = this.block();
				}
				break;
			case 26:
				localctx = new StatementWhenContext(this, localctx);
				this.enterOuterAlt(localctx, 26);
				{
				this.state = 1003;
				this.match(EclangParser.T__97);
				this.state = 1007;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===78 || _la===79) {
					{
					this.state = 1004;
					(localctx as StatementWhenContext)._kind = this._input.LT(1);
					_la = this._input.LA(1);
					if(!(_la===78 || _la===79)) {
					    (localctx as StatementWhenContext)._kind = this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 1005;
					this.match(EclangParser.NAME);
					this.state = 1006;
					this.match(EclangParser.T__14);
					}
				}

				this.state = 1009;
				(localctx as StatementWhenContext)._subject = this.expr(0);
				this.state = 1010;
				this.match(EclangParser.T__63);
				this.state = 1011;
				this.match(EclangParser.INDENT);
				this.state = 1013;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1012;
					(localctx as StatementWhenContext)._whenArm = this.whenArm();
					(localctx as StatementWhenContext)._arms.push((localctx as StatementWhenContext)._whenArm);
					}
					}
					this.state = 1015;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (((((_la - 1)) & ~0x1F) === 0 && ((1 << (_la - 1)) & 4056032207) !== 0) || ((((_la - 35)) & ~0x1F) === 0 && ((1 << (_la - 35)) & 1342210063) !== 0) || ((((_la - 98)) & ~0x1F) === 0 && ((1 << (_la - 98)) & 267911169) !== 0));
				this.state = 1017;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 27:
				localctx = new StatementLetIfContext(this, localctx);
				this.enterOuterAlt(localctx, 27);
				{
				this.state = 1019;
				this.match(EclangParser.T__62);
				this.state = 1020;
				(localctx as StatementLetIfContext)._kind = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===78 || _la===79)) {
				    (localctx as StatementLetIfContext)._kind = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1021;
				this.match(EclangParser.NAME);
				this.state = 1022;
				this.match(EclangParser.T__14);
				this.state = 1023;
				(localctx as StatementLetIfContext)._value = this.expr(0);
				this.state = 1024;
				this.match(EclangParser.T__63);
				this.state = 1025;
				(localctx as StatementLetIfContext)._body = this.block();
				this.state = 1031;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 92, this._ctx) ) {
				case 1:
					{
					this.state = 1026;
					this.match(EclangParser.T__64);
					this.state = 1028;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===64) {
						{
						this.state = 1027;
						this.match(EclangParser.T__63);
						}
					}

					this.state = 1030;
					(localctx as StatementLetIfContext)._elseBody = this.block();
					}
					break;
				}
				}
				break;
			case 28:
				localctx = new StatementIfContext(this, localctx);
				this.enterOuterAlt(localctx, 28);
				{
				this.state = 1033;
				this.match(EclangParser.T__62);
				this.state = 1034;
				(localctx as StatementIfContext)._expr = this.expr(0);
				(localctx as StatementIfContext)._conditions.push((localctx as StatementIfContext)._expr);
				this.state = 1035;
				this.match(EclangParser.T__63);
				this.state = 1036;
				(localctx as StatementIfContext)._block = this.block();
				(localctx as StatementIfContext)._bodies.push((localctx as StatementIfContext)._block);
				this.state = 1045;
				this._errHandler.sync(this);
				_alt = this._interp.adaptivePredict(this._input, 93, this._ctx);
				while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
					if (_alt === 1) {
						{
						{
						this.state = 1037;
						this.match(EclangParser.T__64);
						this.state = 1038;
						this.match(EclangParser.T__62);
						this.state = 1039;
						(localctx as StatementIfContext)._expr = this.expr(0);
						(localctx as StatementIfContext)._conditions.push((localctx as StatementIfContext)._expr);
						this.state = 1040;
						this.match(EclangParser.T__63);
						this.state = 1041;
						(localctx as StatementIfContext)._block = this.block();
						(localctx as StatementIfContext)._bodies.push((localctx as StatementIfContext)._block);
						}
						}
					}
					this.state = 1047;
					this._errHandler.sync(this);
					_alt = this._interp.adaptivePredict(this._input, 93, this._ctx);
				}
				this.state = 1053;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 95, this._ctx) ) {
				case 1:
					{
					this.state = 1048;
					this.match(EclangParser.T__64);
					this.state = 1050;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===64) {
						{
						this.state = 1049;
						this.match(EclangParser.T__63);
						}
					}

					this.state = 1052;
					(localctx as StatementIfContext)._elseBody = this.block();
					}
					break;
				}
				}
				break;
			case 29:
				localctx = new StatementLabeledContext(this, localctx);
				this.enterOuterAlt(localctx, 29);
				{
				this.state = 1055;
				(localctx as StatementLabeledContext)._label = this.match(EclangParser.NAME);
				this.state = 1056;
				this.match(EclangParser.T__66);
				this.state = 1057;
				this.statement();
				}
				break;
			case 30:
				localctx = new StatementWhileContext(this, localctx);
				this.enterOuterAlt(localctx, 30);
				{
				this.state = 1058;
				this.match(EclangParser.T__94);
				this.state = 1059;
				(localctx as StatementWhileContext)._condition = this.expr(0);
				this.state = 1060;
				this.match(EclangParser.T__63);
				this.state = 1061;
				(localctx as StatementWhileContext)._body = this.block();
				this.state = 1067;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 97, this._ctx) ) {
				case 1:
					{
					this.state = 1062;
					this.match(EclangParser.T__64);
					this.state = 1064;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===64) {
						{
						this.state = 1063;
						this.match(EclangParser.T__63);
						}
					}

					this.state = 1066;
					(localctx as StatementWhileContext)._elseBody = this.block();
					}
					break;
				}
				}
				break;
			case 31:
				localctx = new StatementForInRangeContext(this, localctx);
				this.enterOuterAlt(localctx, 31);
				{
				this.state = 1069;
				this.match(EclangParser.T__98);
				this.state = 1071;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===78 || _la===79) {
					{
					this.state = 1070;
					_la = this._input.LA(1);
					if(!(_la===78 || _la===79)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					}
				}

				this.state = 1073;
				this.match(EclangParser.NAME);
				this.state = 1074;
				this.match(EclangParser.T__32);
				this.state = 1075;
				(localctx as StatementForInRangeContext)._lower = this.expr(0);
				this.state = 1076;
				this.match(EclangParser.T__99);
				this.state = 1077;
				(localctx as StatementForInRangeContext)._upper = this.expr(0);
				this.state = 1078;
				this.match(EclangParser.T__63);
				this.state = 1079;
				(localctx as StatementForInRangeContext)._body = this.block();
				this.state = 1085;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 100, this._ctx) ) {
				case 1:
					{
					this.state = 1080;
					this.match(EclangParser.T__64);
					this.state = 1082;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===64) {
						{
						this.state = 1081;
						this.match(EclangParser.T__63);
						}
					}

					this.state = 1084;
					(localctx as StatementForInRangeContext)._elseBody = this.block();
					}
					break;
				}
				}
				break;
			case 32:
				localctx = new StatementForInContext(this, localctx);
				this.enterOuterAlt(localctx, 32);
				{
				this.state = 1087;
				this.match(EclangParser.T__98);
				this.state = 1089;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===78 || _la===79) {
					{
					this.state = 1088;
					_la = this._input.LA(1);
					if(!(_la===78 || _la===79)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					}
				}

				this.state = 1093;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 102, this._ctx) ) {
				case 1:
					{
					this.state = 1091;
					(localctx as StatementForInContext)._index = this.match(EclangParser.NAME);
					this.state = 1092;
					this.match(EclangParser.T__4);
					}
					break;
				}
				this.state = 1097;
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case 118:
					{
					this.state = 1095;
					(localctx as StatementForInContext)._name = this.match(EclangParser.NAME);
					}
					break;
				case 4:
					{
					this.state = 1096;
					(localctx as StatementForInContext)._parts = this.destructure();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				this.state = 1099;
				this.match(EclangParser.T__32);
				this.state = 1100;
				(localctx as StatementForInContext)._iterable = this.expr(0);
				this.state = 1103;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===101) {
					{
					this.state = 1101;
					this.match(EclangParser.T__100);
					this.state = 1102;
					(localctx as StatementForInContext)._filter = this.expr(0);
					}
				}

				this.state = 1105;
				this.match(EclangParser.T__63);
				this.state = 1106;
				(localctx as StatementForInContext)._body = this.block();
				this.state = 1112;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 106, this._ctx) ) {
				case 1:
					{
					this.state = 1107;
					this.match(EclangParser.T__64);
					this.state = 1109;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===64) {
						{
						this.state = 1108;
						this.match(EclangParser.T__63);
						}
					}

					this.state = 1111;
					(localctx as StatementForInContext)._elseBody = this.block();
					}
					break;
				}
				}
				break;
			case 33:
				localctx = new StatementTryContext(this, localctx);
				this.enterOuterAlt(localctx, 33);
				{
				this.state = 1114;
				this.match(EclangParser.T__30);
				this.state = 1115;
				(localctx as StatementTryContext)._left = this.expr(0);
				this.state = 1116;
				this.match(EclangParser.NL);
				}
				break;
			case 34:
				localctx = new StatementTryCatchContext(this, localctx);
				this.enterOuterAlt(localctx, 34);
				{
				this.state = 1118;
				this.match(EclangParser.T__30);
				this.state = 1119;
				(localctx as StatementTryCatchContext)._left = this.expr(0);
				this.state = 1120;
				this.match(EclangParser.T__101);
				this.state = 1121;
				this.match(EclangParser.NAME);
				this.state = 1122;
				this.match(EclangParser.T__63);
				this.state = 1123;
				(localctx as StatementTryCatchContext)._elseBody = this.block();
				}
				break;
			case 35:
				localctx = new StatementFunctionCallNoArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 35);
				{
				this.state = 1125;
				this.match(EclangParser.NAME);
				this.state = 1126;
				this.match(EclangParser.T__3);
				this.state = 1127;
				this.match(EclangParser.T__5);
				this.state = 1128;
				this.match(EclangParser.NL);
				}
				break;
			case 36:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 36);
				{
				this.state = 1129;
				this.match(EclangParser.NAME);
				this.state = 1130;
				this.match(EclangParser.T__3);
				this.state = 1131;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1136;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1132;
					this.match(EclangParser.T__4);
					this.state = 1133;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1138;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1139;
				this.match(EclangParser.T__5);
				this.state = 1140;
				this.match(EclangParser.NL);
				}
				break;
			case 37:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 37);
				{
				this.state = 1142;
				this.match(EclangParser.NAME);
				this.state = 1143;
				this.match(EclangParser.T__3);
				this.state = 1144;
				this.match(EclangParser.INDENT);
				this.state = 1145;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1151;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1146;
					this.match(EclangParser.T__4);
					this.state = 1147;
					this.match(EclangParser.NL);
					this.state = 1148;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1153;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1154;
				this.match(EclangParser.NL);
				this.state = 1155;
				this.match(EclangParser.DEDENT);
				this.state = 1156;
				this.match(EclangParser.T__5);
				this.state = 1157;
				this.match(EclangParser.NL);
				}
				break;
			case 38:
				localctx = new StatementFunctionCallNoArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 38);
				{
				this.state = 1159;
				(localctx as StatementFunctionCallNoArgsContext)._expr = this.expr(0);
				(localctx as StatementFunctionCallNoArgsContext)._args.push((localctx as StatementFunctionCallNoArgsContext)._expr);
				this.state = 1160;
				this.match(EclangParser.T__10);
				this.state = 1161;
				this.match(EclangParser.NAME);
				this.state = 1162;
				this.match(EclangParser.T__3);
				this.state = 1163;
				this.match(EclangParser.T__5);
				this.state = 1164;
				this.match(EclangParser.NL);
				}
				break;
			case 39:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 39);
				{
				this.state = 1166;
				(localctx as StatementFunctionCallWithArgsContext)._expr = this.expr(0);
				(localctx as StatementFunctionCallWithArgsContext)._args.push((localctx as StatementFunctionCallWithArgsContext)._expr);
				this.state = 1167;
				this.match(EclangParser.T__10);
				this.state = 1168;
				this.match(EclangParser.NAME);
				this.state = 1169;
				this.match(EclangParser.T__3);
				this.state = 1170;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1175;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1171;
					this.match(EclangParser.T__4);
					this.state = 1172;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1177;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1178;
				this.match(EclangParser.T__5);
				this.state = 1179;
				this.match(EclangParser.NL);
				}
				break;
			case 40:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 40);
				{
				this.state = 1181;
				(localctx as StatementFunctionCallWithArgsContext)._expr = this.expr(0);
				(localctx as StatementFunctionCallWithArgsContext)._args.push((localctx as StatementFunctionCallWithArgsContext)._expr);
				this.state = 1182;
				this.match(EclangParser.T__10);
				this.state = 1183;
				this.match(EclangParser.NAME);
				this.state = 1184;
				this.match(EclangParser.T__3);
				this.state = 1185;
				this.match(EclangParser.INDENT);
				this.state = 1186;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1192;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1187;
					this.match(EclangParser.T__4);
					this.state = 1188;
					this.match(EclangParser.NL);
					this.state = 1189;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1194;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1195;
				this.match(EclangParser.NL);
				this.state = 1196;
				this.match(EclangParser.DEDENT);
				this.state = 1197;
				this.match(EclangParser.T__5);
				this.state = 1198;
				this.match(EclangParser.NL);
				}
				break;
			case 41:
				localctx = new StatementSafeCallContext(this, localctx);
				this.enterOuterAlt(localctx, 41);
				{
				this.state = 1200;
				(localctx as StatementSafeCallContext)._expr = this.expr(0);
				(localctx as StatementSafeCallContext)._args.push((localctx as StatementSafeCallContext)._expr);
				this.state = 1201;
				this.match(EclangParser.T__20);
				this.state = 1202;
				this.match(EclangParser.NAME);
				this.state = 1203;
				this.match(EclangParser.T__3);
				this.state = 1204;
				this.match(EclangParser.T__5);
				this.state = 1205;
				this.match(EclangParser.NL);
				}
				break;
			case 42:
				localctx = new StatementSafeCallContext(this, localctx);
				this.enterOuterAlt(localctx, 42);
				{
				this.state = 1207;
				(localctx as StatementSafeCallContext)._expr = this.expr(0);
				(localctx as StatementSafeCallContext)._args.push((localctx as StatementSafeCallContext)._expr);
				this.state = 1208;
				this.match(EclangParser.T__20);
				this.state = 1209;
				this.match(EclangParser.NAME);
				this.state = 1210;
				this.match(EclangParser.T__3);
				this.state = 1211;
				(localctx as StatementSafeCallContext)._argument = this.argument();
				(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
				this.state = 1216;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1212;
					this.match(EclangParser.T__4);
					this.state = 1213;
					(localctx as StatementSafeCallContext)._argument = this.argument();
					(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
					}
					}
					this.state = 1218;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1219;
				this.match(EclangParser.T__5);
				this.state = 1220;
				this.match(EclangParser.NL);
				}
				break;
			case 43:
				localctx = new StatementSafeCallContext(this, localctx);
				this.enterOuterAlt(localctx, 43);
				{
				this.state = 1222;
				(localctx as StatementSafeCallContext)._expr = this.expr(0);
				(localctx as StatementSafeCallContext)._args.push((localctx as StatementSafeCallContext)._expr);
				this.state = 1223;
				this.match(EclangParser.T__20);
				this.state = 1224;
				this.match(EclangParser.NAME);
				this.state = 1225;
				this.match(EclangParser.T__3);
				this.state = 1226;
				this.match(EclangParser.INDENT);
				this.state = 1227;
				(localctx as StatementSafeCallContext)._argument = this.argument();
				(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
				this.state = 1233;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1228;
					this.match(EclangParser.T__4);
					this.state = 1229;
					this.match(EclangParser.NL);
					this.state = 1230;
					(localctx as StatementSafeCallContext)._argument = this.argument();
					(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
					}
					}
					this.state = 1235;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1236;
				this.match(EclangParser.NL);
				this.state = 1237;
				this.match(EclangParser.DEDENT);
				this.state = 1238;
				this.match(EclangParser.T__5);
				this.state = 1239;
				this.match(EclangParser.NL);
				}
				break;
			case 44:
				localctx = new StatementPoolContext(this, localctx);
				this.enterOuterAlt(localctx, 44);
				{
				this.state = 1241;
				(localctx as StatementPoolContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1243;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 1242;
					(localctx as StatementPoolContext)._name = this.match(EclangParser.NAME);
					}
				}

				this.state = 1245;
				this.match(EclangParser.T__63);
				this.state = 1246;
				(localctx as StatementPoolContext)._body = this.block();
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public destructure(): DestructureContext {
		let localctx: DestructureContext = new DestructureContext(this, this._ctx, this.state);
		this.enterRule(localctx, 24, EclangParser.RULE_destructure);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1249;
			this.match(EclangParser.T__3);
			this.state = 1250;
			localctx._destructurePart = this.destructurePart();
			localctx._parts.push(localctx._destructurePart);
			this.state = 1253;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 1251;
				this.match(EclangParser.T__4);
				this.state = 1252;
				localctx._destructurePart = this.destructurePart();
				localctx._parts.push(localctx._destructurePart);
				}
				}
				this.state = 1255;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while (_la===5);
			this.state = 1257;
			this.match(EclangParser.T__5);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public destructurePart(): DestructurePartContext {
		let localctx: DestructurePartContext = new DestructurePartContext(this, this._ctx, this.state);
		this.enterRule(localctx, 26, EclangParser.RULE_destructurePart);
		try {
			this.state = 1261;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 118:
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1259;
				this.match(EclangParser.NAME);
				}
				break;
			case 4:
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1260;
				this.destructure();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public whenPattern(): WhenPatternContext {
		let localctx: WhenPatternContext = new WhenPatternContext(this, this._ctx, this.state);
		this.enterRule(localctx, 28, EclangParser.RULE_whenPattern);
		let _la: number;
		try {
			this.state = 1285;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 120, this._ctx) ) {
			case 1:
				localctx = new WhenPatternIsContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1263;
				this.match(EclangParser.T__49);
				this.state = 1264;
				this.type_(0);
				this.state = 1267;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 1265;
					this.match(EclangParser.T__62);
					this.state = 1266;
					(localctx as WhenPatternIsContext)._guard = this.expr(0);
					}
				}

				}
				break;
			case 2:
				localctx = new WhenPatternElseContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1269;
				this.match(EclangParser.T__64);
				}
				break;
			case 3:
				localctx = new WhenPatternBindContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1270;
				this.match(EclangParser.NAME);
				this.state = 1271;
				this.match(EclangParser.T__62);
				this.state = 1272;
				(localctx as WhenPatternBindContext)._guard = this.expr(0);
				}
				break;
			case 4:
				localctx = new WhenPatternValuesContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 1273;
				(localctx as WhenPatternValuesContext)._whenValue = this.whenValue();
				(localctx as WhenPatternValuesContext)._values.push((localctx as WhenPatternValuesContext)._whenValue);
				this.state = 1278;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1274;
					this.match(EclangParser.T__4);
					this.state = 1275;
					(localctx as WhenPatternValuesContext)._whenValue = this.whenValue();
					(localctx as WhenPatternValuesContext)._values.push((localctx as WhenPatternValuesContext)._whenValue);
					}
					}
					this.state = 1280;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1283;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 1281;
					this.match(EclangParser.T__62);
					this.state = 1282;
					(localctx as WhenPatternValuesContext)._guard = this.expr(0);
					}
				}

				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public whenValue(): WhenValueContext {
		let localctx: WhenValueContext = new WhenValueContext(this, this._ctx, this.state);
		this.enterRule(localctx, 30, EclangParser.RULE_whenValue);
		let _la: number;
		try {
			this.state = 1293;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 121, this._ctx) ) {
			case 1:
				localctx = new WhenValuePlainContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1287;
				this.expr(0);
				}
				break;
			case 2:
				localctx = new WhenValueFromContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1288;
				(localctx as WhenValueFromContext)._from_ = this.expr(0);
				this.state = 1289;
				this.match(EclangParser.T__21);
				}
				break;
			case 3:
				localctx = new WhenValueUpToContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1291;
				(localctx as WhenValueUpToContext)._op = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===23 || _la===24)) {
				    (localctx as WhenValueUpToContext)._op = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1292;
				(localctx as WhenValueUpToContext)._to = this.expr(0);
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public whenArm(): WhenArmContext {
		let localctx: WhenArmContext = new WhenArmContext(this, this._ctx, this.state);
		this.enterRule(localctx, 32, EclangParser.RULE_whenArm);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1295;
			this.whenPattern();
			this.state = 1296;
			this.match(EclangParser.T__63);
			this.state = 1299;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 132:
				{
				this.state = 1297;
				localctx._body = this.block();
				}
				break;
			case 1:
			case 2:
			case 3:
			case 4:
			case 7:
			case 8:
			case 9:
			case 10:
			case 14:
			case 18:
			case 25:
			case 29:
			case 30:
			case 31:
			case 32:
			case 35:
			case 36:
			case 37:
			case 38:
			case 63:
			case 68:
			case 69:
			case 70:
			case 71:
			case 78:
			case 79:
			case 80:
			case 81:
			case 92:
			case 93:
			case 94:
			case 95:
			case 96:
			case 97:
			case 98:
			case 99:
			case 117:
			case 118:
			case 119:
			case 120:
			case 121:
			case 122:
			case 123:
			case 124:
			case 125:
				{
				this.state = 1298;
				localctx._line = this.statement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public whenExpr(): WhenExprContext {
		let localctx: WhenExprContext = new WhenExprContext(this, this._ctx, this.state);
		this.enterRule(localctx, 34, EclangParser.RULE_whenExpr);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1301;
			this.match(EclangParser.T__97);
			this.state = 1305;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===78 || _la===79) {
				{
				this.state = 1302;
				localctx._kind = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===78 || _la===79)) {
				    localctx._kind = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1303;
				this.match(EclangParser.NAME);
				this.state = 1304;
				this.match(EclangParser.T__14);
				}
			}

			this.state = 1307;
			localctx._subject = this.expr(0);
			this.state = 1308;
			this.match(EclangParser.T__63);
			this.state = 1309;
			this.match(EclangParser.INDENT);
			this.state = 1311;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 1310;
				localctx._whenValueArm = this.whenValueArm();
				localctx._arms.push(localctx._whenValueArm);
				}
				}
				this.state = 1313;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while (((((_la - 1)) & ~0x1F) === 0 && ((1 << (_la - 1)) & 4056032207) !== 0) || ((((_la - 35)) & ~0x1F) === 0 && ((1 << (_la - 35)) & 1342210063) !== 0) || ((((_la - 98)) & ~0x1F) === 0 && ((1 << (_la - 98)) & 267911169) !== 0));
			this.state = 1315;
			this.match(EclangParser.DEDENT);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public whenValueArm(): WhenValueArmContext {
		let localctx: WhenValueArmContext = new WhenValueArmContext(this, this._ctx, this.state);
		this.enterRule(localctx, 36, EclangParser.RULE_whenValueArm);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1317;
			this.whenPattern();
			this.state = 1318;
			this.match(EclangParser.T__63);
			this.state = 1322;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 125, this._ctx) ) {
			case 1:
				{
				this.state = 1319;
				localctx._value = this.tail();
				}
				break;
			case 2:
				{
				this.state = 1320;
				localctx._line = this.statement();
				}
				break;
			case 3:
				{
				this.state = 1321;
				localctx._body = this.block();
				}
				break;
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public func(): FuncContext {
		let localctx: FuncContext = new FuncContext(this, this._ctx, this.state);
		this.enterRule(localctx, 38, EclangParser.RULE_func);
		let _la: number;
		try {
			this.state = 1404;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 140, this._ctx) ) {
			case 1:
				localctx = new FunctionDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1324;
				this.match(EclangParser.T__102);
				this.state = 1325;
				this.match(EclangParser.T__9);
				this.state = 1326;
				(localctx as FunctionDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1329;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1327;
					this.match(EclangParser.T__103);
					this.state = 1328;
					(localctx as FunctionDeclareContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1331;
				this.match(EclangParser.T__25);
				this.state = 1332;
				this.match(EclangParser.T__3);
				this.state = 1334;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===17 || ((((_la - 78)) & ~0x1F) === 0 && ((1 << (_la - 78)) & 7) !== 0) || _la===118) {
					{
					this.state = 1333;
					(localctx as FunctionDeclareContext)._parameter = this.parameter();
					(localctx as FunctionDeclareContext)._params.push((localctx as FunctionDeclareContext)._parameter);
					}
				}

				this.state = 1340;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1336;
					this.match(EclangParser.T__4);
					this.state = 1337;
					(localctx as FunctionDeclareContext)._parameter = this.parameter();
					(localctx as FunctionDeclareContext)._params.push((localctx as FunctionDeclareContext)._parameter);
					}
					}
					this.state = 1342;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1343;
				this.match(EclangParser.T__5);
				this.state = 1346;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67 || _la===76) {
					{
					this.state = 1344;
					_la = this._input.LA(1);
					if(!(_la===67 || _la===76)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 1345;
					(localctx as FunctionDeclareContext)._returnType = this.type_(0);
					}
				}

				this.state = 1350;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===75) {
					{
					this.state = 1348;
					this.match(EclangParser.T__74);
					this.state = 1349;
					(localctx as FunctionDeclareContext)._throwing = this.type_(0);
					}
				}

				this.state = 1352;
				this.match(EclangParser.NL);
				}
				break;
			case 2:
				localctx = new FunctionBlockContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1353;
				this.match(EclangParser.T__9);
				this.state = 1354;
				(localctx as FunctionBlockContext)._name = this.match(EclangParser.NAME);
				this.state = 1355;
				this.match(EclangParser.T__3);
				this.state = 1357;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===17 || ((((_la - 78)) & ~0x1F) === 0 && ((1 << (_la - 78)) & 7) !== 0) || _la===118) {
					{
					this.state = 1356;
					(localctx as FunctionBlockContext)._parameter = this.parameter();
					(localctx as FunctionBlockContext)._params.push((localctx as FunctionBlockContext)._parameter);
					}
				}

				this.state = 1363;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1359;
					this.match(EclangParser.T__4);
					this.state = 1360;
					(localctx as FunctionBlockContext)._parameter = this.parameter();
					(localctx as FunctionBlockContext)._params.push((localctx as FunctionBlockContext)._parameter);
					}
					}
					this.state = 1365;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1366;
				this.match(EclangParser.T__5);
				this.state = 1369;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1367;
					this.match(EclangParser.T__66);
					this.state = 1368;
					(localctx as FunctionBlockContext)._returnType = this.type_(0);
					}
				}

				this.state = 1373;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===75) {
					{
					this.state = 1371;
					this.match(EclangParser.T__74);
					this.state = 1372;
					(localctx as FunctionBlockContext)._throwing = this.type_(0);
					}
				}

				this.state = 1375;
				this.match(EclangParser.T__63);
				this.state = 1376;
				(localctx as FunctionBlockContext)._body = this.block();
				}
				break;
			case 3:
				localctx = new FunctionInlineContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1378;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===105) {
					{
					this.state = 1377;
					this.match(EclangParser.T__104);
					}
				}

				this.state = 1380;
				this.match(EclangParser.T__9);
				this.state = 1381;
				(localctx as FunctionInlineContext)._name = this.match(EclangParser.NAME);
				this.state = 1382;
				this.match(EclangParser.T__3);
				this.state = 1384;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===17 || ((((_la - 78)) & ~0x1F) === 0 && ((1 << (_la - 78)) & 7) !== 0) || _la===118) {
					{
					this.state = 1383;
					(localctx as FunctionInlineContext)._parameter = this.parameter();
					(localctx as FunctionInlineContext)._params.push((localctx as FunctionInlineContext)._parameter);
					}
				}

				this.state = 1390;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1386;
					this.match(EclangParser.T__4);
					this.state = 1387;
					(localctx as FunctionInlineContext)._parameter = this.parameter();
					(localctx as FunctionInlineContext)._params.push((localctx as FunctionInlineContext)._parameter);
					}
					}
					this.state = 1392;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1393;
				this.match(EclangParser.T__5);
				this.state = 1396;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1394;
					this.match(EclangParser.T__66);
					this.state = 1395;
					(localctx as FunctionInlineContext)._returnType = this.type_(0);
					}
				}

				this.state = 1400;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===75) {
					{
					this.state = 1398;
					this.match(EclangParser.T__74);
					this.state = 1399;
					(localctx as FunctionInlineContext)._throwing = this.type_(0);
					}
				}

				this.state = 1402;
				this.match(EclangParser.T__14);
				this.state = 1403;
				(localctx as FunctionInlineContext)._expression = this.tail();
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public global(): GlobalContext {
		let localctx: GlobalContext = new GlobalContext(this, this._ctx, this.state);
		this.enterRule(localctx, 40, EclangParser.RULE_global);
		let _la: number;
		try {
			this.state = 1629;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 160, this._ctx) ) {
			case 1:
				localctx = new GlobalDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1406;
				this.match(EclangParser.T__77);
				this.state = 1407;
				this.match(EclangParser.NAME);
				this.state = 1408;
				this.match(EclangParser.T__14);
				this.state = 1409;
				this.expr(0);
				this.state = 1410;
				this.match(EclangParser.NL);
				}
				break;
			case 2:
				localctx = new GlobalDefineConstContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1412;
				this.match(EclangParser.T__78);
				this.state = 1413;
				this.match(EclangParser.NAME);
				this.state = 1414;
				this.match(EclangParser.T__14);
				this.state = 1415;
				this.expr(0);
				this.state = 1416;
				this.match(EclangParser.NL);
				}
				break;
			case 3:
				localctx = new GlobalDefineVolatileContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1418;
				this.match(EclangParser.T__79);
				this.state = 1419;
				this.match(EclangParser.NAME);
				this.state = 1420;
				this.match(EclangParser.T__14);
				this.state = 1421;
				this.expr(0);
				this.state = 1422;
				this.match(EclangParser.NL);
				}
				break;
			case 4:
				localctx = new GlobalDefineTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 1424;
				this.match(EclangParser.T__77);
				this.state = 1425;
				this.match(EclangParser.NAME);
				this.state = 1428;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1426;
					this.match(EclangParser.T__66);
					this.state = 1427;
					this.type_(0);
					}
				}

				this.state = 1430;
				this.match(EclangParser.T__14);
				this.state = 1431;
				this.expr(0);
				this.state = 1432;
				this.match(EclangParser.NL);
				}
				break;
			case 5:
				localctx = new GlobalDefineConstTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 1434;
				this.match(EclangParser.T__78);
				this.state = 1435;
				this.match(EclangParser.NAME);
				this.state = 1438;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1436;
					this.match(EclangParser.T__66);
					this.state = 1437;
					this.type_(0);
					}
				}

				this.state = 1440;
				this.match(EclangParser.T__14);
				this.state = 1441;
				this.expr(0);
				this.state = 1442;
				this.match(EclangParser.NL);
				}
				break;
			case 6:
				localctx = new GlobalDefineVolatileTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 6);
				{
				this.state = 1444;
				this.match(EclangParser.T__79);
				this.state = 1445;
				this.match(EclangParser.NAME);
				this.state = 1448;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1446;
					this.match(EclangParser.T__66);
					this.state = 1447;
					this.type_(0);
					}
				}

				this.state = 1450;
				this.match(EclangParser.T__14);
				this.state = 1451;
				this.expr(0);
				this.state = 1452;
				this.match(EclangParser.NL);
				}
				break;
			case 7:
				localctx = new GlobalConstantContext(this, localctx);
				this.enterOuterAlt(localctx, 7);
				{
				this.state = 1454;
				this.match(EclangParser.T__80);
				this.state = 1455;
				this.match(EclangParser.NAME);
				this.state = 1458;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1456;
					this.match(EclangParser.T__66);
					this.state = 1457;
					this.type_(0);
					}
				}

				this.state = 1460;
				this.match(EclangParser.T__14);
				this.state = 1461;
				this.expr(0);
				this.state = 1462;
				this.match(EclangParser.NL);
				}
				break;
			case 8:
				localctx = new GlobalDeclareDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 8);
				{
				this.state = 1464;
				this.match(EclangParser.T__102);
				this.state = 1465;
				this.match(EclangParser.T__77);
				this.state = 1466;
				(localctx as GlobalDeclareDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1469;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1467;
					this.match(EclangParser.T__103);
					this.state = 1468;
					(localctx as GlobalDeclareDefineContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1471;
				this.match(EclangParser.T__25);
				this.state = 1472;
				this.type_(0);
				this.state = 1473;
				this.match(EclangParser.NL);
				}
				break;
			case 9:
				localctx = new GlobalDeclareConstContext(this, localctx);
				this.enterOuterAlt(localctx, 9);
				{
				this.state = 1475;
				this.match(EclangParser.T__102);
				this.state = 1476;
				_la = this._input.LA(1);
				if(!(_la===79 || _la===81)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1477;
				(localctx as GlobalDeclareConstContext)._name = this.match(EclangParser.NAME);
				this.state = 1480;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1478;
					this.match(EclangParser.T__103);
					this.state = 1479;
					(localctx as GlobalDeclareConstContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1482;
				this.match(EclangParser.T__25);
				this.state = 1483;
				this.type_(0);
				this.state = 1484;
				this.match(EclangParser.NL);
				}
				break;
			case 10:
				localctx = new GlobalTypeDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 10);
				{
				this.state = 1486;
				this.match(EclangParser.T__102);
				this.state = 1487;
				this.match(EclangParser.T__105);
				this.state = 1488;
				this.match(EclangParser.NAME);
				this.state = 1489;
				this.match(EclangParser.T__25);
				this.state = 1490;
				this.type_(0);
				this.state = 1491;
				this.match(EclangParser.NL);
				}
				break;
			case 11:
				localctx = new GlobalTypeAliasContext(this, localctx);
				this.enterOuterAlt(localctx, 11);
				{
				this.state = 1493;
				this.match(EclangParser.T__105);
				this.state = 1494;
				(localctx as GlobalTypeAliasContext)._name = this.match(EclangParser.NAME);
				this.state = 1495;
				this.match(EclangParser.T__14);
				this.state = 1497;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 147, this._ctx) ) {
				case 1:
					{
					this.state = 1496;
					(localctx as GlobalTypeAliasContext)._modifier = this.match(EclangParser.NAME);
					}
					break;
				}
				this.state = 1499;
				this.type_(0);
				this.state = 1500;
				this.match(EclangParser.NL);
				}
				break;
			case 12:
				localctx = new GlobalEnumDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 12);
				{
				this.state = 1502;
				this.match(EclangParser.T__102);
				this.state = 1503;
				this.match(EclangParser.T__106);
				this.state = 1504;
				(localctx as GlobalEnumDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1505;
				this.match(EclangParser.T__25);
				this.state = 1506;
				this.match(EclangParser.INDENT);
				this.state = 1511;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1507;
					(localctx as GlobalEnumDeclareContext)._ATOM = this.match(EclangParser.ATOM);
					(localctx as GlobalEnumDeclareContext)._atoms.push((localctx as GlobalEnumDeclareContext)._ATOM);
					this.state = 1508;
					this.match(EclangParser.T__14);
					this.state = 1509;
					(localctx as GlobalEnumDeclareContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalEnumDeclareContext)._constants.push((localctx as GlobalEnumDeclareContext)._NAME);
					this.state = 1510;
					this.match(EclangParser.NL);
					}
					}
					this.state = 1513;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===119);
				this.state = 1515;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 13:
				localctx = new GlobalUnitDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 13);
				{
				this.state = 1516;
				this.match(EclangParser.T__107);
				this.state = 1517;
				this.match(EclangParser.NAME);
				this.state = 1518;
				this.match(EclangParser.T__3);
				this.state = 1519;
				this.match(EclangParser.NAME);
				this.state = 1520;
				this.match(EclangParser.T__66);
				this.state = 1521;
				this.type_(0);
				this.state = 1522;
				this.match(EclangParser.T__5);
				this.state = 1523;
				this.match(EclangParser.T__14);
				this.state = 1524;
				this.expr(0);
				this.state = 1525;
				this.match(EclangParser.NL);
				}
				break;
			case 14:
				localctx = new GlobalRecordDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 14);
				{
				this.state = 1527;
				_la = this._input.LA(1);
				if(!(_la===109 || _la===110)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1528;
				(localctx as GlobalRecordDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1539;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===12) {
					{
					this.state = 1529;
					this.match(EclangParser.T__11);
					this.state = 1530;
					(localctx as GlobalRecordDefineContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalRecordDefineContext)._typeParams.push((localctx as GlobalRecordDefineContext)._NAME);
					this.state = 1535;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===5) {
						{
						{
						this.state = 1531;
						this.match(EclangParser.T__4);
						this.state = 1532;
						(localctx as GlobalRecordDefineContext)._NAME = this.match(EclangParser.NAME);
						(localctx as GlobalRecordDefineContext)._typeParams.push((localctx as GlobalRecordDefineContext)._NAME);
						}
						}
						this.state = 1537;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					this.state = 1538;
					this.match(EclangParser.T__12);
					}
				}

				this.state = 1543;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===111) {
					{
					this.state = 1541;
					this.match(EclangParser.T__110);
					this.state = 1542;
					(localctx as GlobalRecordDefineContext)._extending = this.match(EclangParser.NAME);
					}
				}

				this.state = 1545;
				this.match(EclangParser.T__25);
				this.state = 1546;
				this.match(EclangParser.INDENT);
				this.state = 1548;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1547;
					(localctx as GlobalRecordDefineContext)._recordField = this.recordField();
					(localctx as GlobalRecordDefineContext)._fields.push((localctx as GlobalRecordDefineContext)._recordField);
					}
					}
					this.state = 1550;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===118);
				this.state = 1552;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 15:
				localctx = new GlobalRecordDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 15);
				{
				this.state = 1554;
				_la = this._input.LA(1);
				if(!(_la===109 || _la===110)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1555;
				(localctx as GlobalRecordDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1556;
				this.match(EclangParser.T__110);
				this.state = 1557;
				(localctx as GlobalRecordDefineContext)._extending = this.match(EclangParser.NAME);
				this.state = 1558;
				this.match(EclangParser.NL);
				}
				break;
			case 16:
				localctx = new GlobalRecordDeclareDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 16);
				{
				this.state = 1559;
				this.match(EclangParser.T__102);
				this.state = 1560;
				_la = this._input.LA(1);
				if(!(_la===109 || _la===110)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1561;
				(localctx as GlobalRecordDeclareDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1564;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1562;
					this.match(EclangParser.T__103);
					this.state = 1563;
					(localctx as GlobalRecordDeclareDefineContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1566;
				this.match(EclangParser.T__25);
				this.state = 1567;
				this.match(EclangParser.INDENT);
				this.state = 1573;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1568;
					(localctx as GlobalRecordDeclareDefineContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalRecordDeclareDefineContext)._names.push((localctx as GlobalRecordDeclareDefineContext)._NAME);
					this.state = 1569;
					this.match(EclangParser.T__66);
					this.state = 1570;
					(localctx as GlobalRecordDeclareDefineContext)._type_ = this.type_(0);
					(localctx as GlobalRecordDeclareDefineContext)._types.push((localctx as GlobalRecordDeclareDefineContext)._type_);
					this.state = 1571;
					this.match(EclangParser.NL);
					}
					}
					this.state = 1575;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===118);
				this.state = 1577;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 17:
				localctx = new GlobalRecordDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 17);
				{
				this.state = 1579;
				this.match(EclangParser.T__102);
				this.state = 1580;
				_la = this._input.LA(1);
				if(!(_la===109 || _la===110)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1581;
				(localctx as GlobalRecordDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1584;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1582;
					this.match(EclangParser.T__103);
					this.state = 1583;
					(localctx as GlobalRecordDeclareContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1586;
				this.match(EclangParser.NL);
				}
				break;
			case 18:
				localctx = new GlobalStructDeclareDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 18);
				{
				this.state = 1587;
				this.match(EclangParser.T__102);
				this.state = 1588;
				this.match(EclangParser.T__111);
				this.state = 1589;
				(localctx as GlobalStructDeclareDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1592;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1590;
					this.match(EclangParser.T__103);
					this.state = 1591;
					(localctx as GlobalStructDeclareDefineContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1594;
				this.match(EclangParser.T__25);
				this.state = 1595;
				this.match(EclangParser.INDENT);
				this.state = 1601;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1596;
					(localctx as GlobalStructDeclareDefineContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalStructDeclareDefineContext)._names.push((localctx as GlobalStructDeclareDefineContext)._NAME);
					this.state = 1597;
					this.match(EclangParser.T__66);
					this.state = 1598;
					(localctx as GlobalStructDeclareDefineContext)._type_ = this.type_(0);
					(localctx as GlobalStructDeclareDefineContext)._types.push((localctx as GlobalStructDeclareDefineContext)._type_);
					this.state = 1599;
					this.match(EclangParser.NL);
					}
					}
					this.state = 1603;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===118);
				this.state = 1605;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 19:
				localctx = new GlobalStructDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 19);
				{
				this.state = 1607;
				this.match(EclangParser.T__102);
				this.state = 1608;
				this.match(EclangParser.T__111);
				this.state = 1609;
				(localctx as GlobalStructDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1612;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1610;
					this.match(EclangParser.T__103);
					this.state = 1611;
					(localctx as GlobalStructDeclareContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1614;
				this.match(EclangParser.NL);
				}
				break;
			case 20:
				localctx = new GlobalInterfaceDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 20);
				{
				this.state = 1615;
				this.match(EclangParser.T__76);
				this.state = 1616;
				(localctx as GlobalInterfaceDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1617;
				this.match(EclangParser.T__25);
				this.state = 1618;
				this.match(EclangParser.INDENT);
				this.state = 1620;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1619;
					(localctx as GlobalInterfaceDefineContext)._interfaceMember = this.interfaceMember();
					(localctx as GlobalInterfaceDefineContext)._members.push((localctx as GlobalInterfaceDefineContext)._interfaceMember);
					}
					}
					this.state = 1622;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===10 || _la===118);
				this.state = 1624;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 21:
				localctx = new GlobalNamespaceContext(this, localctx);
				this.enterOuterAlt(localctx, 21);
				{
				this.state = 1626;
				this.match(EclangParser.T__112);
				this.state = 1627;
				(localctx as GlobalNamespaceContext)._name = this.match(EclangParser.STRING);
				this.state = 1628;
				this.match(EclangParser.NL);
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public interfaceMember(): InterfaceMemberContext {
		let localctx: InterfaceMemberContext = new InterfaceMemberContext(this, this._ctx, this.state);
		this.enterRule(localctx, 42, EclangParser.RULE_interfaceMember);
		let _la: number;
		try {
			this.state = 1655;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 118:
				localctx = new InterfaceFieldContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1631;
				(localctx as InterfaceFieldContext)._name = this.match(EclangParser.NAME);
				this.state = 1632;
				this.match(EclangParser.T__66);
				this.state = 1633;
				this.type_(0);
				this.state = 1634;
				this.match(EclangParser.NL);
				}
				break;
			case 10:
				localctx = new InterfaceMethodContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1636;
				this.match(EclangParser.T__9);
				this.state = 1637;
				(localctx as InterfaceMethodContext)._name = this.match(EclangParser.NAME);
				this.state = 1638;
				this.match(EclangParser.T__3);
				this.state = 1647;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x1F) === 0 && ((1 << _la) & 536876048) !== 0) || _la===73 || _la===74 || _la===118 || _la===119) {
					{
					this.state = 1639;
					(localctx as InterfaceMethodContext)._type_ = this.type_(0);
					(localctx as InterfaceMethodContext)._params.push((localctx as InterfaceMethodContext)._type_);
					this.state = 1644;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===5) {
						{
						{
						this.state = 1640;
						this.match(EclangParser.T__4);
						this.state = 1641;
						(localctx as InterfaceMethodContext)._type_ = this.type_(0);
						(localctx as InterfaceMethodContext)._params.push((localctx as InterfaceMethodContext)._type_);
						}
						}
						this.state = 1646;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					}
				}

				this.state = 1649;
				this.match(EclangParser.T__5);
				this.state = 1652;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1650;
					this.match(EclangParser.T__66);
					this.state = 1651;
					(localctx as InterfaceMethodContext)._returnType = this.type_(0);
					}
				}

				this.state = 1654;
				this.match(EclangParser.NL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public recordField(): RecordFieldContext {
		let localctx: RecordFieldContext = new RecordFieldContext(this, this._ctx, this.state);
		this.enterRule(localctx, 44, EclangParser.RULE_recordField);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1657;
			localctx._name = this.match(EclangParser.NAME);
			this.state = 1658;
			this.match(EclangParser.T__66);
			this.state = 1659;
			this.type_(0);
			this.state = 1662;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===15) {
				{
				this.state = 1660;
				this.match(EclangParser.T__14);
				this.state = 1661;
				localctx._value = this.expr(0);
				}
			}

			this.state = 1664;
			this.match(EclangParser.NL);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public use(): UseContext {
		let localctx: UseContext = new UseContext(this, this._ctx, this.state);
		this.enterRule(localctx, 46, EclangParser.RULE_use);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1666;
			this.match(EclangParser.T__113);
			this.state = 1667;
			localctx._from_ = this.match(EclangParser.STRING);
			this.state = 1668;
			this.match(EclangParser.NL);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public interpolation(): InterpolationContext {
		let localctx: InterpolationContext = new InterpolationContext(this, this._ctx, this.state);
		this.enterRule(localctx, 48, EclangParser.RULE_interpolation);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1670;
			this.expr(0);
			this.state = 1674;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la===116) {
				{
				{
				this.state = 1671;
				this.match(EclangParser.NL);
				}
				}
				this.state = 1676;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			this.state = 1677;
			this.match(EclangParser.EOF);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public privateDeclaration(): PrivateDeclarationContext {
		let localctx: PrivateDeclarationContext = new PrivateDeclarationContext(this, this._ctx, this.state);
		this.enterRule(localctx, 50, EclangParser.RULE_privateDeclaration);
		try {
			this.state = 1683;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 167, this._ctx) ) {
			case 1:
				localctx = new PrivateFunctionContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1679;
				this.match(EclangParser.T__114);
				this.state = 1680;
				this.func();
				}
				break;
			case 2:
				localctx = new PrivateGlobalContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1681;
				this.match(EclangParser.T__114);
				this.state = 1682;
				this.global();
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}
	// @RuleVersion(0)
	public program(): ProgramContext {
		let localctx: ProgramContext = new ProgramContext(this, this._ctx, this.state);
		this.enterRule(localctx, 52, EclangParser.RULE_program);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1692;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la===10 || ((((_la - 77)) & ~0x1F) === 0 && ((1 << (_la - 77)) & 3019898911) !== 0) || ((((_la - 109)) & ~0x1F) === 0 && ((1 << (_la - 109)) & 243) !== 0)) {
				{
				this.state = 1690;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 168, this._ctx) ) {
				case 1:
					{
					this.state = 1685;
					localctx._func = this.func();
					localctx._functions.push(localctx._func);
					}
					break;
				case 2:
					{
					this.state = 1686;
					localctx._global = this.global();
					localctx._globals.push(localctx._global);
					}
					break;
				case 3:
					{
					this.state = 1687;
					localctx._use = this.use();
					localctx._uses.push(localctx._use);
					}
					break;
				case 4:
					{
					this.state = 1688;
					localctx._privateDeclaration = this.privateDeclaration();
					localctx._privates.push(localctx._privateDeclaration);
					}
					break;
				case 5:
					{
					this.state = 1689;
					this.match(EclangParser.NL);
					}
					break;
				}
				}
				this.state = 1694;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			this.state = 1695;
			this.match(EclangParser.EOF);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return localctx;
	}

	public sempred(localctx: RuleContext, ruleIndex: number, predIndex: number): boolean {
		switch (ruleIndex) {
		case 3:
			return this.expr_sempred(localctx as ExprContext, predIndex);
		case 8:
			return this.type_sempred(localctx as TypeContext, predIndex);
		}
		return true;
	}
	private expr_sempred(localctx: ExprContext, predIndex: number): boolean {
		switch (predIndex) {
		case 0:
			return this.precpred(this._ctx, 24);
		case 1:
			return this.precpred(this._ctx, 22);
		case 2:
			return this.precpred(this._ctx, 21);
		case 3:
			return this.precpred(this._ctx, 20);
		case 4:
			return this.precpred(this._ctx, 19);
		case 5:
			return this.precpred(this._ctx, 18);
		case 6:
			return this.precpred(this._ctx, 17);
		case 7:
			return this.precpred(this._ctx, 16);
		case 8:
			return this.precpred(this._ctx, 15);
		case 9:
			return this.precpred(this._ctx, 11);
		case 10:
			return this.precpred(this._ctx, 10);
		case 11:
			return this.precpred(this._ctx, 9);
		case 12:
			return this.precpred(this._ctx, 8);
		case 13:
			return this.precpred(this._ctx, 7);
		case 14:
			return this.precpred(this._ctx, 3);
		case 15:
			return this.precpred(this._ctx, 46);
		case 16:
			return this.precpred(this._ctx, 45);
		case 17:
			return this.precpred(this._ctx, 44);
		case 18:
			return this.precpred(this._ctx, 43);
		case 19:
			return this.precpred(this._ctx, 42);
		case 20:
			return this.precpred(this._ctx, 41);
		case 21:
			return this.precpred(this._ctx, 40);
		case 22:
			return this.precpred(this._ctx, 39);
		case 23:
			return this.precpred(this._ctx, 38);
		case 24:
			return this.precpred(this._ctx, 37);
		case 25:
			return this.precpred(this._ctx, 36);
		case 26:
			return this.precpred(this._ctx, 35);
		case 27:
			return this.precpred(this._ctx, 34);
		case 28:
			return this.precpred(this._ctx, 33);
		case 29:
			return this.precpred(this._ctx, 32);
		case 30:
			return this.precpred(this._ctx, 31);
		case 31:
			return this.precpred(this._ctx, 14);
		case 32:
			return this.precpred(this._ctx, 13);
		case 33:
			return this.precpred(this._ctx, 12);
		case 34:
			return this.precpred(this._ctx, 6);
		}
		return true;
	}
	private type_sempred(localctx: TypeContext, predIndex: number): boolean {
		switch (predIndex) {
		case 35:
			return this.precpred(this._ctx, 6);
		case 36:
			return this.precpred(this._ctx, 15);
		case 37:
			return this.precpred(this._ctx, 14);
		case 38:
			return this.precpred(this._ctx, 13);
		case 39:
			return this.precpred(this._ctx, 7);
		}
		return true;
	}

	public static readonly _serializedATN: number[] = [4,1,133,1698,2,0,7,0,
	2,1,7,1,2,2,7,2,2,3,7,3,2,4,7,4,2,5,7,5,2,6,7,6,2,7,7,7,2,8,7,8,2,9,7,9,
	2,10,7,10,2,11,7,11,2,12,7,12,2,13,7,13,2,14,7,14,2,15,7,15,2,16,7,16,2,
	17,7,17,2,18,7,18,2,19,7,19,2,20,7,20,2,21,7,21,2,22,7,22,2,23,7,23,2,24,
	7,24,2,25,7,25,2,26,7,26,1,0,1,0,1,0,1,0,3,0,59,8,0,1,1,1,1,1,1,1,1,1,1,
	3,1,66,8,1,1,2,1,2,4,2,70,8,2,11,2,12,2,71,1,2,1,2,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,5,3,84,8,3,10,3,12,3,87,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	5,3,97,8,3,10,3,12,3,100,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,124,8,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,5,3,135,8,3,10,3,12,3,138,9,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,5,3,149,8,3,10,3,12,3,152,9,3,1,3,1,3,1,3,1,3,1,3,1,3,3,
	3,160,8,3,1,3,1,3,1,3,1,3,1,3,5,3,167,8,3,10,3,12,3,170,9,3,1,3,1,3,3,3,
	174,8,3,3,3,176,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,186,8,3,10,3,12,
	3,189,9,3,1,3,1,3,1,3,1,3,3,3,195,8,3,1,3,1,3,1,3,1,3,1,3,5,3,202,8,3,10,
	3,12,3,205,9,3,1,3,1,3,3,3,209,8,3,3,3,211,8,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,5,3,223,8,3,10,3,12,3,226,9,3,1,3,1,3,1,3,1,3,1,3,1,3,3,
	3,234,8,3,1,3,1,3,1,3,1,3,1,3,5,3,241,8,3,10,3,12,3,244,9,3,1,3,1,3,3,3,
	248,8,3,3,3,250,8,3,1,3,1,3,1,3,1,3,5,3,256,8,3,10,3,12,3,259,9,3,1,3,1,
	3,1,3,1,3,3,3,265,8,3,1,3,1,3,1,3,1,3,1,3,5,3,272,8,3,10,3,12,3,275,9,3,
	1,3,1,3,3,3,279,8,3,3,3,281,8,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,289,8,3,10,
	3,12,3,292,9,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,300,8,3,1,3,1,3,1,3,1,3,1,3,
	5,3,307,8,3,10,3,12,3,310,9,3,1,3,1,3,3,3,314,8,3,3,3,316,8,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,5,3,325,8,3,10,3,12,3,328,9,3,1,3,1,3,1,3,1,3,3,3,334,
	8,3,1,3,1,3,1,3,1,3,1,3,5,3,341,8,3,10,3,12,3,344,9,3,1,3,1,3,3,3,348,8,
	3,3,3,350,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,361,8,3,10,3,12,3,
	364,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,375,8,3,10,3,12,3,378,9,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,388,8,3,10,3,12,3,391,9,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,3,3,419,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,444,8,3,10,
	3,12,3,447,9,3,3,3,449,8,3,1,3,1,3,1,3,3,3,454,8,3,1,3,1,3,3,3,458,8,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,473,8,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,521,8,3,10,3,12,3,524,9,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,537,8,3,10,3,12,3,540,9,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,558,8,3,10,
	3,12,3,561,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,574,8,3,
	10,3,12,3,577,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,605,8,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,640,8,3,10,3,12,3,
	643,9,3,1,4,1,4,3,4,647,8,4,1,4,1,4,1,5,1,5,3,5,653,8,5,1,5,1,5,1,5,1,5,
	3,5,659,8,5,1,5,1,5,3,5,663,8,5,3,5,665,8,5,1,6,1,6,1,6,1,6,3,6,671,8,6,
	1,7,1,7,1,7,3,7,676,8,7,1,8,1,8,1,8,3,8,681,8,8,1,8,1,8,1,8,1,8,1,8,5,8,
	688,8,8,10,8,12,8,691,9,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,
	8,1,8,1,8,5,8,707,8,8,10,8,12,8,710,9,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,
	1,8,1,8,1,8,1,8,1,8,1,8,1,8,3,8,727,8,8,1,8,1,8,1,8,1,8,3,8,733,8,8,1,8,
	1,8,1,8,1,8,3,8,739,8,8,1,8,5,8,742,8,8,10,8,12,8,745,9,8,1,8,1,8,1,8,3,
	8,750,8,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,3,8,759,8,8,1,8,1,8,1,8,1,8,3,8,765,
	8,8,1,8,5,8,768,8,8,10,8,12,8,771,9,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,4,
	8,781,8,8,11,8,12,8,782,1,8,1,8,3,8,787,8,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,
	1,8,1,8,1,8,5,8,799,8,8,10,8,12,8,802,9,8,1,8,1,8,1,8,1,8,1,8,5,8,809,8,
	8,10,8,12,8,812,9,8,1,8,1,8,1,8,5,8,817,8,8,10,8,12,8,820,9,8,1,9,1,9,1,
	9,1,9,1,9,1,9,1,9,1,9,1,9,4,9,831,8,9,11,9,12,9,832,1,9,1,9,1,10,1,10,1,
	10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,
	1,10,1,10,1,10,1,10,1,10,1,10,3,10,860,8,10,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,3,11,919,8,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,3,11,930,8,11,1,11,1,11,3,11,934,8,11,1,11,
	1,11,1,11,3,11,939,8,11,1,11,1,11,3,11,943,8,11,1,11,1,11,1,11,1,11,3,11,
	949,8,11,1,11,1,11,1,11,1,11,3,11,955,8,11,1,11,1,11,1,11,1,11,1,11,3,11,
	962,8,11,1,11,1,11,1,11,1,11,1,11,1,11,3,11,970,8,11,1,11,1,11,1,11,1,11,
	1,11,1,11,3,11,978,8,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,1,11,1,11,3,11,1008,8,11,1,11,1,11,1,11,1,11,4,11,1014,8,11,11,
	11,12,11,1015,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,3,
	11,1029,8,11,1,11,3,11,1032,8,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,5,11,1044,8,11,10,11,12,11,1047,9,11,1,11,1,11,3,11,1051,8,11,
	1,11,3,11,1054,8,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,3,11,1065,
	8,11,1,11,3,11,1068,8,11,1,11,1,11,3,11,1072,8,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,3,11,1083,8,11,1,11,3,11,1086,8,11,1,11,1,11,3,11,
	1090,8,11,1,11,1,11,3,11,1094,8,11,1,11,1,11,3,11,1098,8,11,1,11,1,11,1,
	11,1,11,3,11,1104,8,11,1,11,1,11,1,11,1,11,3,11,1110,8,11,1,11,3,11,1113,
	8,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,5,11,1135,8,11,10,11,12,11,1138,9,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,5,11,1150,8,11,10,11,12,
	11,1153,9,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,5,11,1174,8,11,10,11,12,11,1177,9,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,5,11,1191,8,
	11,10,11,12,11,1194,9,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,5,11,1215,8,11,10,11,12,
	11,1218,9,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	5,11,1232,8,11,10,11,12,11,1235,9,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	3,11,1244,8,11,1,11,1,11,3,11,1248,8,11,1,12,1,12,1,12,1,12,4,12,1254,8,
	12,11,12,12,12,1255,1,12,1,12,1,13,1,13,3,13,1262,8,13,1,14,1,14,1,14,1,
	14,3,14,1268,8,14,1,14,1,14,1,14,1,14,1,14,1,14,1,14,5,14,1277,8,14,10,
	14,12,14,1280,9,14,1,14,1,14,3,14,1284,8,14,3,14,1286,8,14,1,15,1,15,1,
	15,1,15,1,15,1,15,3,15,1294,8,15,1,16,1,16,1,16,1,16,3,16,1300,8,16,1,17,
	1,17,1,17,1,17,3,17,1306,8,17,1,17,1,17,1,17,1,17,4,17,1312,8,17,11,17,
	12,17,1313,1,17,1,17,1,18,1,18,1,18,1,18,1,18,3,18,1323,8,18,1,19,1,19,
	1,19,1,19,1,19,3,19,1330,8,19,1,19,1,19,1,19,3,19,1335,8,19,1,19,1,19,5,
	19,1339,8,19,10,19,12,19,1342,9,19,1,19,1,19,1,19,3,19,1347,8,19,1,19,1,
	19,3,19,1351,8,19,1,19,1,19,1,19,1,19,1,19,3,19,1358,8,19,1,19,1,19,5,19,
	1362,8,19,10,19,12,19,1365,9,19,1,19,1,19,1,19,3,19,1370,8,19,1,19,1,19,
	3,19,1374,8,19,1,19,1,19,1,19,3,19,1379,8,19,1,19,1,19,1,19,1,19,3,19,1385,
	8,19,1,19,1,19,5,19,1389,8,19,10,19,12,19,1392,9,19,1,19,1,19,1,19,3,19,
	1397,8,19,1,19,1,19,3,19,1401,8,19,1,19,1,19,3,19,1405,8,19,1,20,1,20,1,
	20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,
	1,20,1,20,1,20,1,20,1,20,3,20,1429,8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,
	20,1,20,3,20,1439,8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1449,
	8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1459,8,20,1,20,1,20,1,
	20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1470,8,20,1,20,1,20,1,20,1,20,1,20,
	1,20,1,20,1,20,1,20,3,20,1481,8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,
	20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1498,8,20,1,20,1,20,1,20,1,20,
	1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,4,20,1512,8,20,11,20,12,20,1513,
	1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,
	20,1,20,1,20,1,20,5,20,1534,8,20,10,20,12,20,1537,9,20,1,20,3,20,1540,8,
	20,1,20,1,20,3,20,1544,8,20,1,20,1,20,1,20,4,20,1549,8,20,11,20,12,20,1550,
	1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1565,8,
	20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,4,20,1574,8,20,11,20,12,20,1575,1,
	20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1585,8,20,1,20,1,20,1,20,1,20,1,20,
	1,20,3,20,1593,8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,4,20,1602,8,20,11,
	20,12,20,1603,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1613,8,20,1,20,1,
	20,1,20,1,20,1,20,1,20,4,20,1621,8,20,11,20,12,20,1622,1,20,1,20,1,20,1,
	20,1,20,3,20,1630,8,20,1,21,1,21,1,21,1,21,1,21,1,21,1,21,1,21,1,21,1,21,
	1,21,5,21,1643,8,21,10,21,12,21,1646,9,21,3,21,1648,8,21,1,21,1,21,1,21,
	3,21,1653,8,21,1,21,3,21,1656,8,21,1,22,1,22,1,22,1,22,1,22,3,22,1663,8,
	22,1,22,1,22,1,23,1,23,1,23,1,23,1,24,1,24,5,24,1673,8,24,10,24,12,24,1676,
	9,24,1,24,1,24,1,25,1,25,1,25,1,25,3,25,1684,8,25,1,26,1,26,1,26,1,26,1,
	26,5,26,1691,8,26,10,26,12,26,1694,9,26,1,26,1,26,1,26,0,2,6,16,27,0,2,
	4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,
	0,16,1,0,19,20,2,0,25,25,35,38,1,0,39,41,2,0,38,38,42,42,2,0,23,24,47,47,
	2,0,12,13,48,49,1,0,52,55,1,0,56,57,1,0,58,59,1,0,60,61,1,0,23,24,2,0,67,
	67,76,76,1,0,78,79,2,0,15,15,82,91,2,0,79,79,81,81,1,0,109,110,2002,0,58,
	1,0,0,0,2,65,1,0,0,0,4,67,1,0,0,0,6,457,1,0,0,0,8,646,1,0,0,0,10,664,1,
	0,0,0,12,670,1,0,0,0,14,672,1,0,0,0,16,786,1,0,0,0,18,821,1,0,0,0,20,859,
	1,0,0,0,22,1247,1,0,0,0,24,1249,1,0,0,0,26,1261,1,0,0,0,28,1285,1,0,0,0,
	30,1293,1,0,0,0,32,1295,1,0,0,0,34,1301,1,0,0,0,36,1317,1,0,0,0,38,1404,
	1,0,0,0,40,1629,1,0,0,0,42,1655,1,0,0,0,44,1657,1,0,0,0,46,1666,1,0,0,0,
	48,1670,1,0,0,0,50,1683,1,0,0,0,52,1692,1,0,0,0,54,59,5,121,0,0,55,59,5,
	120,0,0,56,59,5,122,0,0,57,59,5,123,0,0,58,54,1,0,0,0,58,55,1,0,0,0,58,
	56,1,0,0,0,58,57,1,0,0,0,59,1,1,0,0,0,60,66,3,0,0,0,61,66,5,117,0,0,62,
	66,5,125,0,0,63,66,5,124,0,0,64,66,5,119,0,0,65,60,1,0,0,0,65,61,1,0,0,
	0,65,62,1,0,0,0,65,63,1,0,0,0,65,64,1,0,0,0,66,3,1,0,0,0,67,69,5,132,0,
	0,68,70,3,22,11,0,69,68,1,0,0,0,70,71,1,0,0,0,71,69,1,0,0,0,71,72,1,0,0,
	0,72,73,1,0,0,0,73,74,5,133,0,0,74,5,1,0,0,0,75,76,6,3,-1,0,76,458,5,1,
	0,0,77,458,5,2,0,0,78,458,5,3,0,0,79,80,5,4,0,0,80,85,3,6,3,0,81,82,5,5,
	0,0,82,84,3,6,3,0,83,81,1,0,0,0,84,87,1,0,0,0,85,83,1,0,0,0,85,86,1,0,0,
	0,86,88,1,0,0,0,87,85,1,0,0,0,88,89,5,6,0,0,89,458,1,0,0,0,90,91,5,4,0,
	0,91,92,5,132,0,0,92,98,3,6,3,0,93,94,5,5,0,0,94,95,5,116,0,0,95,97,3,6,
	3,0,96,93,1,0,0,0,97,100,1,0,0,0,98,96,1,0,0,0,98,99,1,0,0,0,99,101,1,0,
	0,0,100,98,1,0,0,0,101,102,5,116,0,0,102,103,5,133,0,0,103,104,5,6,0,0,
	104,458,1,0,0,0,105,106,3,2,1,0,106,107,5,118,0,0,107,458,1,0,0,0,108,458,
	3,2,1,0,109,110,5,7,0,0,110,458,3,16,8,0,111,112,5,8,0,0,112,458,3,16,8,
	0,113,114,5,9,0,0,114,115,5,4,0,0,115,116,3,16,8,0,116,117,5,5,0,0,117,
	118,5,118,0,0,118,119,5,6,0,0,119,458,1,0,0,0,120,123,5,10,0,0,121,122,
	5,118,0,0,122,124,5,11,0,0,123,121,1,0,0,0,123,124,1,0,0,0,124,125,1,0,
	0,0,125,458,5,118,0,0,126,127,5,118,0,0,127,128,5,4,0,0,128,458,5,6,0,0,
	129,130,5,118,0,0,130,131,5,4,0,0,131,136,3,8,4,0,132,133,5,5,0,0,133,135,
	3,8,4,0,134,132,1,0,0,0,135,138,1,0,0,0,136,134,1,0,0,0,136,137,1,0,0,0,
	137,139,1,0,0,0,138,136,1,0,0,0,139,140,5,6,0,0,140,458,1,0,0,0,141,142,
	5,118,0,0,142,143,5,4,0,0,143,144,5,132,0,0,144,150,3,8,4,0,145,146,5,5,
	0,0,146,147,5,116,0,0,147,149,3,8,4,0,148,145,1,0,0,0,149,152,1,0,0,0,150,
	148,1,0,0,0,150,151,1,0,0,0,151,153,1,0,0,0,152,150,1,0,0,0,153,154,5,116,
	0,0,154,155,5,133,0,0,155,156,5,6,0,0,156,458,1,0,0,0,157,158,5,118,0,0,
	158,160,5,11,0,0,159,157,1,0,0,0,159,160,1,0,0,0,160,161,1,0,0,0,161,173,
	5,118,0,0,162,163,5,12,0,0,163,168,3,16,8,0,164,165,5,5,0,0,165,167,3,16,
	8,0,166,164,1,0,0,0,167,170,1,0,0,0,168,166,1,0,0,0,168,169,1,0,0,0,169,
	171,1,0,0,0,170,168,1,0,0,0,171,172,5,13,0,0,172,174,1,0,0,0,173,162,1,
	0,0,0,173,174,1,0,0,0,174,176,1,0,0,0,175,159,1,0,0,0,175,176,1,0,0,0,176,
	177,1,0,0,0,177,178,5,14,0,0,178,179,5,118,0,0,179,180,5,15,0,0,180,187,
	3,6,3,0,181,182,5,5,0,0,182,183,5,118,0,0,183,184,5,15,0,0,184,186,3,6,
	3,0,185,181,1,0,0,0,186,189,1,0,0,0,187,185,1,0,0,0,187,188,1,0,0,0,188,
	190,1,0,0,0,189,187,1,0,0,0,190,191,5,16,0,0,191,458,1,0,0,0,192,193,5,
	118,0,0,193,195,5,11,0,0,194,192,1,0,0,0,194,195,1,0,0,0,195,196,1,0,0,
	0,196,208,5,118,0,0,197,198,5,12,0,0,198,203,3,16,8,0,199,200,5,5,0,0,200,
	202,3,16,8,0,201,199,1,0,0,0,202,205,1,0,0,0,203,201,1,0,0,0,203,204,1,
	0,0,0,204,206,1,0,0,0,205,203,1,0,0,0,206,207,5,13,0,0,207,209,1,0,0,0,
	208,197,1,0,0,0,208,209,1,0,0,0,209,211,1,0,0,0,210,194,1,0,0,0,210,211,
	1,0,0,0,211,212,1,0,0,0,212,213,5,14,0,0,213,214,5,132,0,0,214,215,5,118,
	0,0,215,216,5,15,0,0,216,224,3,6,3,0,217,218,5,5,0,0,218,219,5,116,0,0,
	219,220,5,118,0,0,220,221,5,15,0,0,221,223,3,6,3,0,222,217,1,0,0,0,223,
	226,1,0,0,0,224,222,1,0,0,0,224,225,1,0,0,0,225,227,1,0,0,0,226,224,1,0,
	0,0,227,228,5,116,0,0,228,229,5,133,0,0,229,230,5,16,0,0,230,458,1,0,0,
	0,231,232,5,118,0,0,232,234,5,11,0,0,233,231,1,0,0,0,233,234,1,0,0,0,234,
	235,1,0,0,0,235,247,5,118,0,0,236,237,5,12,0,0,237,242,3,16,8,0,238,239,
	5,5,0,0,239,241,3,16,8,0,240,238,1,0,0,0,241,244,1,0,0,0,242,240,1,0,0,
	0,242,243,1,0,0,0,243,245,1,0,0,0,244,242,1,0,0,0,245,246,5,13,0,0,246,
	248,1,0,0,0,247,236,1,0,0,0,247,248,1,0,0,0,248,250,1,0,0,0,249,233,1,0,
	0,0,249,250,1,0,0,0,250,251,1,0,0,0,251,252,5,14,0,0,252,257,3,6,3,0,253,
	254,5,5,0,0,254,256,3,6,3,0,255,253,1,0,0,0,256,259,1,0,0,0,257,255,1,0,
	0,0,257,258,1,0,0,0,258,260,1,0,0,0,259,257,1,0,0,0,260,261,5,16,0,0,261,
	458,1,0,0,0,262,263,5,118,0,0,263,265,5,11,0,0,264,262,1,0,0,0,264,265,
	1,0,0,0,265,266,1,0,0,0,266,278,5,118,0,0,267,268,5,12,0,0,268,273,3,16,
	8,0,269,270,5,5,0,0,270,272,3,16,8,0,271,269,1,0,0,0,272,275,1,0,0,0,273,
	271,1,0,0,0,273,274,1,0,0,0,274,276,1,0,0,0,275,273,1,0,0,0,276,277,5,13,
	0,0,277,279,1,0,0,0,278,267,1,0,0,0,278,279,1,0,0,0,279,281,1,0,0,0,280,
	264,1,0,0,0,280,281,1,0,0,0,281,282,1,0,0,0,282,283,5,14,0,0,283,284,5,
	132,0,0,284,290,3,6,3,0,285,286,5,5,0,0,286,287,5,116,0,0,287,289,3,6,3,
	0,288,285,1,0,0,0,289,292,1,0,0,0,290,288,1,0,0,0,290,291,1,0,0,0,291,293,
	1,0,0,0,292,290,1,0,0,0,293,294,5,116,0,0,294,295,5,133,0,0,295,296,5,16,
	0,0,296,458,1,0,0,0,297,298,5,118,0,0,298,300,5,11,0,0,299,297,1,0,0,0,
	299,300,1,0,0,0,300,301,1,0,0,0,301,313,5,118,0,0,302,303,5,12,0,0,303,
	308,3,16,8,0,304,305,5,5,0,0,305,307,3,16,8,0,306,304,1,0,0,0,307,310,1,
	0,0,0,308,306,1,0,0,0,308,309,1,0,0,0,309,311,1,0,0,0,310,308,1,0,0,0,311,
	312,5,13,0,0,312,314,1,0,0,0,313,302,1,0,0,0,313,314,1,0,0,0,314,316,1,
	0,0,0,315,299,1,0,0,0,315,316,1,0,0,0,316,317,1,0,0,0,317,318,5,14,0,0,
	318,319,5,17,0,0,319,326,3,6,3,0,320,321,5,5,0,0,321,322,5,118,0,0,322,
	323,5,15,0,0,323,325,3,6,3,0,324,320,1,0,0,0,325,328,1,0,0,0,326,324,1,
	0,0,0,326,327,1,0,0,0,327,329,1,0,0,0,328,326,1,0,0,0,329,330,5,16,0,0,
	330,458,1,0,0,0,331,332,5,118,0,0,332,334,5,11,0,0,333,331,1,0,0,0,333,
	334,1,0,0,0,334,335,1,0,0,0,335,347,5,118,0,0,336,337,5,12,0,0,337,342,
	3,16,8,0,338,339,5,5,0,0,339,341,3,16,8,0,340,338,1,0,0,0,341,344,1,0,0,
	0,342,340,1,0,0,0,342,343,1,0,0,0,343,345,1,0,0,0,344,342,1,0,0,0,345,346,
	5,13,0,0,346,348,1,0,0,0,347,336,1,0,0,0,347,348,1,0,0,0,348,350,1,0,0,
	0,349,333,1,0,0,0,349,350,1,0,0,0,350,351,1,0,0,0,351,352,5,14,0,0,352,
	353,5,132,0,0,353,354,5,17,0,0,354,362,3,6,3,0,355,356,5,5,0,0,356,357,
	5,116,0,0,357,358,5,118,0,0,358,359,5,15,0,0,359,361,3,6,3,0,360,355,1,
	0,0,0,361,364,1,0,0,0,362,360,1,0,0,0,362,363,1,0,0,0,363,365,1,0,0,0,364,
	362,1,0,0,0,365,366,5,116,0,0,366,367,5,133,0,0,367,368,5,16,0,0,368,458,
	1,0,0,0,369,458,5,118,0,0,370,371,5,18,0,0,371,376,3,6,3,0,372,373,5,5,
	0,0,373,375,3,6,3,0,374,372,1,0,0,0,375,378,1,0,0,0,376,374,1,0,0,0,376,
	377,1,0,0,0,377,379,1,0,0,0,378,376,1,0,0,0,379,380,7,0,0,0,380,458,1,0,
	0,0,381,382,5,18,0,0,382,383,5,132,0,0,383,389,3,6,3,0,384,385,5,5,0,0,
	385,386,5,116,0,0,386,388,3,6,3,0,387,384,1,0,0,0,388,391,1,0,0,0,389,387,
	1,0,0,0,389,390,1,0,0,0,390,392,1,0,0,0,391,389,1,0,0,0,392,393,5,116,0,
	0,393,394,5,133,0,0,394,395,7,0,0,0,395,458,1,0,0,0,396,397,5,29,0,0,397,
	458,3,6,3,30,398,399,5,30,0,0,399,458,3,6,3,29,400,401,5,31,0,0,401,458,
	3,6,3,28,402,403,5,32,0,0,403,404,3,6,3,0,404,405,5,33,0,0,405,406,3,6,
	3,27,406,458,1,0,0,0,407,408,5,32,0,0,408,458,3,6,3,26,409,410,5,32,0,0,
	410,411,5,4,0,0,411,412,3,16,8,0,412,413,5,6,0,0,413,414,5,18,0,0,414,415,
	3,6,3,0,415,418,5,20,0,0,416,417,5,33,0,0,417,419,3,6,3,0,418,416,1,0,0,
	0,418,419,1,0,0,0,419,458,1,0,0,0,420,421,7,1,0,0,421,458,3,6,3,23,422,
	423,5,63,0,0,423,424,3,6,3,0,424,425,5,64,0,0,425,426,3,6,3,0,426,427,5,
	65,0,0,427,428,3,6,3,5,428,458,1,0,0,0,429,430,5,63,0,0,430,431,5,4,0,0,
	431,432,3,6,3,0,432,433,5,6,0,0,433,434,3,6,3,0,434,435,5,65,0,0,435,436,
	3,6,3,4,436,458,1,0,0,0,437,458,3,34,17,0,438,439,5,10,0,0,439,448,5,4,
	0,0,440,445,3,14,7,0,441,442,5,5,0,0,442,444,3,14,7,0,443,441,1,0,0,0,444,
	447,1,0,0,0,445,443,1,0,0,0,445,446,1,0,0,0,446,449,1,0,0,0,447,445,1,0,
	0,0,448,440,1,0,0,0,448,449,1,0,0,0,449,450,1,0,0,0,450,453,5,6,0,0,451,
	452,5,67,0,0,452,454,3,16,8,0,453,451,1,0,0,0,453,454,1,0,0,0,454,455,1,
	0,0,0,455,456,5,15,0,0,456,458,3,6,3,1,457,75,1,0,0,0,457,77,1,0,0,0,457,
	78,1,0,0,0,457,79,1,0,0,0,457,90,1,0,0,0,457,105,1,0,0,0,457,108,1,0,0,
	0,457,109,1,0,0,0,457,111,1,0,0,0,457,113,1,0,0,0,457,120,1,0,0,0,457,126,
	1,0,0,0,457,129,1,0,0,0,457,141,1,0,0,0,457,175,1,0,0,0,457,210,1,0,0,0,
	457,249,1,0,0,0,457,280,1,0,0,0,457,315,1,0,0,0,457,349,1,0,0,0,457,369,
	1,0,0,0,457,370,1,0,0,0,457,381,1,0,0,0,457,396,1,0,0,0,457,398,1,0,0,0,
	457,400,1,0,0,0,457,402,1,0,0,0,457,407,1,0,0,0,457,409,1,0,0,0,457,420,
	1,0,0,0,457,422,1,0,0,0,457,429,1,0,0,0,457,437,1,0,0,0,457,438,1,0,0,0,
	458,641,1,0,0,0,459,460,10,24,0,0,460,461,5,34,0,0,461,640,3,6,3,24,462,
	463,10,22,0,0,463,464,7,2,0,0,464,640,3,6,3,23,465,466,10,21,0,0,466,467,
	7,3,0,0,467,640,3,6,3,22,468,472,10,20,0,0,469,473,5,43,0,0,470,471,5,13,
	0,0,471,473,5,13,0,0,472,469,1,0,0,0,472,470,1,0,0,0,473,474,1,0,0,0,474,
	640,3,6,3,21,475,476,10,19,0,0,476,477,5,44,0,0,477,640,3,6,3,20,478,479,
	10,18,0,0,479,480,5,45,0,0,480,640,3,6,3,19,481,482,10,17,0,0,482,483,5,
	46,0,0,483,640,3,6,3,18,484,485,10,16,0,0,485,486,7,4,0,0,486,640,3,6,3,
	17,487,488,10,15,0,0,488,489,7,5,0,0,489,640,3,6,3,16,490,491,10,11,0,0,
	491,492,7,6,0,0,492,640,3,6,3,12,493,494,10,10,0,0,494,495,7,7,0,0,495,
	640,3,6,3,11,496,497,10,9,0,0,497,498,7,8,0,0,498,640,3,6,3,10,499,500,
	10,8,0,0,500,501,7,9,0,0,501,640,3,6,3,9,502,503,10,7,0,0,503,504,5,62,
	0,0,504,640,3,6,3,7,505,506,10,3,0,0,506,507,5,66,0,0,507,640,3,6,3,4,508,
	509,10,46,0,0,509,510,5,11,0,0,510,511,5,118,0,0,511,512,5,4,0,0,512,640,
	5,6,0,0,513,514,10,45,0,0,514,515,5,11,0,0,515,516,5,118,0,0,516,517,5,
	4,0,0,517,522,3,8,4,0,518,519,5,5,0,0,519,521,3,8,4,0,520,518,1,0,0,0,521,
	524,1,0,0,0,522,520,1,0,0,0,522,523,1,0,0,0,523,525,1,0,0,0,524,522,1,0,
	0,0,525,526,5,6,0,0,526,640,1,0,0,0,527,528,10,44,0,0,528,529,5,11,0,0,
	529,530,5,118,0,0,530,531,5,4,0,0,531,532,5,132,0,0,532,538,3,8,4,0,533,
	534,5,5,0,0,534,535,5,116,0,0,535,537,3,8,4,0,536,533,1,0,0,0,537,540,1,
	0,0,0,538,536,1,0,0,0,538,539,1,0,0,0,539,541,1,0,0,0,540,538,1,0,0,0,541,
	542,5,116,0,0,542,543,5,133,0,0,543,544,5,6,0,0,544,640,1,0,0,0,545,546,
	10,43,0,0,546,547,5,21,0,0,547,548,5,118,0,0,548,549,5,4,0,0,549,640,5,
	6,0,0,550,551,10,42,0,0,551,552,5,21,0,0,552,553,5,118,0,0,553,554,5,4,
	0,0,554,559,3,8,4,0,555,556,5,5,0,0,556,558,3,8,4,0,557,555,1,0,0,0,558,
	561,1,0,0,0,559,557,1,0,0,0,559,560,1,0,0,0,560,562,1,0,0,0,561,559,1,0,
	0,0,562,563,5,6,0,0,563,640,1,0,0,0,564,565,10,41,0,0,565,566,5,21,0,0,
	566,567,5,118,0,0,567,568,5,4,0,0,568,569,5,132,0,0,569,575,3,8,4,0,570,
	571,5,5,0,0,571,572,5,116,0,0,572,574,3,8,4,0,573,570,1,0,0,0,574,577,1,
	0,0,0,575,573,1,0,0,0,575,576,1,0,0,0,576,578,1,0,0,0,577,575,1,0,0,0,578,
	579,5,116,0,0,579,580,5,133,0,0,580,581,5,6,0,0,581,640,1,0,0,0,582,583,
	10,40,0,0,583,584,5,11,0,0,584,640,5,118,0,0,585,586,10,39,0,0,586,587,
	5,21,0,0,587,640,5,118,0,0,588,589,10,38,0,0,589,590,5,18,0,0,590,591,3,
	6,3,0,591,592,5,20,0,0,592,640,1,0,0,0,593,594,10,37,0,0,594,595,5,18,0,
	0,595,596,5,132,0,0,596,597,3,6,3,0,597,598,5,116,0,0,598,599,5,133,0,0,
	599,600,5,20,0,0,600,640,1,0,0,0,601,602,10,36,0,0,602,604,5,18,0,0,603,
	605,3,6,3,0,604,603,1,0,0,0,604,605,1,0,0,0,605,606,1,0,0,0,606,607,5,22,
	0,0,607,640,5,20,0,0,608,609,10,35,0,0,609,610,5,18,0,0,610,611,7,10,0,
	0,611,612,3,6,3,0,612,613,5,20,0,0,613,640,1,0,0,0,614,615,10,34,0,0,615,
	640,5,25,0,0,616,617,10,33,0,0,617,618,5,26,0,0,618,640,3,16,8,0,619,620,
	10,32,0,0,620,621,5,26,0,0,621,622,5,27,0,0,622,640,3,16,8,0,623,624,10,
	31,0,0,624,625,5,28,0,0,625,640,3,16,8,0,626,627,10,14,0,0,627,628,5,50,
	0,0,628,640,3,16,8,0,629,630,10,13,0,0,630,631,5,51,0,0,631,640,3,16,8,
	0,632,633,10,12,0,0,633,634,5,50,0,0,634,635,5,35,0,0,635,640,3,16,8,0,
	636,637,10,6,0,0,637,638,5,62,0,0,638,640,3,10,5,0,639,459,1,0,0,0,639,
	462,1,0,0,0,639,465,1,0,0,0,639,468,1,0,0,0,639,475,1,0,0,0,639,478,1,0,
	0,0,639,481,1,0,0,0,639,484,1,0,0,0,639,487,1,0,0,0,639,490,1,0,0,0,639,
	493,1,0,0,0,639,496,1,0,0,0,639,499,1,0,0,0,639,502,1,0,0,0,639,505,1,0,
	0,0,639,508,1,0,0,0,639,513,1,0,0,0,639,527,1,0,0,0,639,545,1,0,0,0,639,
	550,1,0,0,0,639,564,1,0,0,0,639,582,1,0,0,0,639,585,1,0,0,0,639,588,1,0,
	0,0,639,593,1,0,0,0,639,601,1,0,0,0,639,608,1,0,0,0,639,614,1,0,0,0,639,
	616,1,0,0,0,639,619,1,0,0,0,639,623,1,0,0,0,639,626,1,0,0,0,639,629,1,0,
	0,0,639,632,1,0,0,0,639,636,1,0,0,0,640,643,1,0,0,0,641,639,1,0,0,0,641,
	642,1,0,0,0,642,7,1,0,0,0,643,641,1,0,0,0,644,645,5,118,0,0,645,647,5,15,
	0,0,646,644,1,0,0,0,646,647,1,0,0,0,647,648,1,0,0,0,648,649,3,6,3,0,649,
	9,1,0,0,0,650,652,5,68,0,0,651,653,3,6,3,0,652,651,1,0,0,0,652,653,1,0,
	0,0,653,665,1,0,0,0,654,655,5,69,0,0,655,665,3,6,3,0,656,658,5,70,0,0,657,
	659,5,118,0,0,658,657,1,0,0,0,658,659,1,0,0,0,659,665,1,0,0,0,660,662,5,
	71,0,0,661,663,5,118,0,0,662,661,1,0,0,0,662,663,1,0,0,0,663,665,1,0,0,
	0,664,650,1,0,0,0,664,654,1,0,0,0,664,656,1,0,0,0,664,660,1,0,0,0,665,11,
	1,0,0,0,666,667,3,6,3,0,667,668,5,116,0,0,668,671,1,0,0,0,669,671,3,34,
	17,0,670,666,1,0,0,0,670,669,1,0,0,0,671,13,1,0,0,0,672,675,5,118,0,0,673,
	674,5,67,0,0,674,676,3,16,8,0,675,673,1,0,0,0,675,676,1,0,0,0,676,15,1,
	0,0,0,677,680,6,8,-1,0,678,679,5,118,0,0,679,681,5,11,0,0,680,678,1,0,0,
	0,680,681,1,0,0,0,681,682,1,0,0,0,682,683,5,118,0,0,683,684,5,12,0,0,684,
	689,3,16,8,0,685,686,5,5,0,0,686,688,3,16,8,0,687,685,1,0,0,0,688,691,1,
	0,0,0,689,687,1,0,0,0,689,690,1,0,0,0,690,692,1,0,0,0,691,689,1,0,0,0,692,
	693,5,13,0,0,693,787,1,0,0,0,694,695,5,118,0,0,695,696,5,11,0,0,696,787,
	5,118,0,0,697,787,5,118,0,0,698,699,5,29,0,0,699,787,3,16,8,16,700,701,
	5,73,0,0,701,702,3,16,8,0,702,703,5,46,0,0,703,708,3,16,8,0,704,705,5,46,
	0,0,705,707,3,16,8,0,706,704,1,0,0,0,707,710,1,0,0,0,708,706,1,0,0,0,708,
	709,1,0,0,0,709,787,1,0,0,0,710,708,1,0,0,0,711,712,5,4,0,0,712,713,3,16,
	8,0,713,714,5,6,0,0,714,787,1,0,0,0,715,787,5,119,0,0,716,717,5,12,0,0,
	717,718,5,118,0,0,718,787,5,13,0,0,719,720,5,74,0,0,720,787,3,6,3,0,721,
	722,5,10,0,0,722,723,5,4,0,0,723,726,5,6,0,0,724,725,7,11,0,0,725,727,3,
	16,8,0,726,724,1,0,0,0,726,727,1,0,0,0,727,787,1,0,0,0,728,729,5,10,0,0,
	729,732,5,4,0,0,730,731,5,118,0,0,731,733,5,67,0,0,732,730,1,0,0,0,732,
	733,1,0,0,0,733,734,1,0,0,0,734,743,3,16,8,0,735,738,5,5,0,0,736,737,5,
	118,0,0,737,739,5,67,0,0,738,736,1,0,0,0,738,739,1,0,0,0,739,740,1,0,0,
	0,740,742,3,16,8,0,741,735,1,0,0,0,742,745,1,0,0,0,743,741,1,0,0,0,743,
	744,1,0,0,0,744,746,1,0,0,0,745,743,1,0,0,0,746,749,5,6,0,0,747,748,7,11,
	0,0,748,750,3,16,8,0,749,747,1,0,0,0,749,750,1,0,0,0,750,787,1,0,0,0,751,
	752,5,4,0,0,752,753,5,6,0,0,753,754,5,76,0,0,754,787,3,16,8,3,755,758,5,
	4,0,0,756,757,5,118,0,0,757,759,5,67,0,0,758,756,1,0,0,0,758,759,1,0,0,
	0,759,760,1,0,0,0,760,769,3,16,8,0,761,764,5,5,0,0,762,763,5,118,0,0,763,
	765,5,67,0,0,764,762,1,0,0,0,764,765,1,0,0,0,765,766,1,0,0,0,766,768,3,
	16,8,0,767,761,1,0,0,0,768,771,1,0,0,0,769,767,1,0,0,0,769,770,1,0,0,0,
	770,772,1,0,0,0,771,769,1,0,0,0,772,773,5,6,0,0,773,774,5,76,0,0,774,775,
	3,16,8,2,775,787,1,0,0,0,776,777,5,4,0,0,777,780,3,16,8,0,778,779,5,5,0,
	0,779,781,3,16,8,0,780,778,1,0,0,0,781,782,1,0,0,0,782,780,1,0,0,0,782,
	783,1,0,0,0,783,784,1,0,0,0,784,785,5,6,0,0,785,787,1,0,0,0,786,677,1,0,
	0,0,786,694,1,0,0,0,786,697,1,0,0,0,786,698,1,0,0,0,786,700,1,0,0,0,786,
	711,1,0,0,0,786,715,1,0,0,0,786,716,1,0,0,0,786,719,1,0,0,0,786,721,1,0,
	0,0,786,728,1,0,0,0,786,751,1,0,0,0,786,755,1,0,0,0,786,776,1,0,0,0,787,
	818,1,0,0,0,788,789,10,6,0,0,789,790,5,75,0,0,790,817,3,16,8,7,791,792,
	10,15,0,0,792,817,5,72,0,0,793,794,10,14,0,0,794,795,5,44,0,0,795,800,3,
	16,8,0,796,797,5,44,0,0,797,799,3,16,8,0,798,796,1,0,0,0,799,802,1,0,0,
	0,800,798,1,0,0,0,800,801,1,0,0,0,801,817,1,0,0,0,802,800,1,0,0,0,803,804,
	10,13,0,0,804,805,5,46,0,0,805,810,3,16,8,0,806,807,5,46,0,0,807,809,3,
	16,8,0,808,806,1,0,0,0,809,812,1,0,0,0,810,808,1,0,0,0,810,811,1,0,0,0,
	811,817,1,0,0,0,812,810,1,0,0,0,813,814,10,7,0,0,814,815,5,18,0,0,815,817,
	7,0,0,0,816,788,1,0,0,0,816,791,1,0,0,0,816,793,1,0,0,0,816,803,1,0,0,0,
	816,813,1,0,0,0,817,820,1,0,0,0,818,816,1,0,0,0,818,819,1,0,0,0,819,17,
	1,0,0,0,820,818,1,0,0,0,821,822,5,77,0,0,822,823,5,118,0,0,823,824,5,26,
	0,0,824,830,5,132,0,0,825,826,5,118,0,0,826,827,5,67,0,0,827,828,3,16,8,
	0,828,829,5,116,0,0,829,831,1,0,0,0,830,825,1,0,0,0,831,832,1,0,0,0,832,
	830,1,0,0,0,832,833,1,0,0,0,833,834,1,0,0,0,834,835,5,133,0,0,835,19,1,
	0,0,0,836,837,5,118,0,0,837,838,5,67,0,0,838,860,3,16,8,0,839,840,5,118,
	0,0,840,841,5,67,0,0,841,842,3,16,8,0,842,843,5,15,0,0,843,844,3,6,3,0,
	844,860,1,0,0,0,845,846,5,118,0,0,846,847,5,15,0,0,847,860,3,6,3,0,848,
	849,5,17,0,0,849,850,5,118,0,0,850,851,5,67,0,0,851,860,3,16,8,0,852,860,
	5,17,0,0,853,854,5,78,0,0,854,860,3,20,10,0,855,856,5,79,0,0,856,860,3,
	20,10,0,857,858,5,80,0,0,858,860,3,20,10,0,859,836,1,0,0,0,859,839,1,0,
	0,0,859,845,1,0,0,0,859,848,1,0,0,0,859,852,1,0,0,0,859,853,1,0,0,0,859,
	855,1,0,0,0,859,857,1,0,0,0,860,21,1,0,0,0,861,862,5,78,0,0,862,863,5,118,
	0,0,863,864,5,15,0,0,864,1248,3,12,6,0,865,866,5,79,0,0,866,867,5,118,0,
	0,867,868,5,15,0,0,868,1248,3,12,6,0,869,870,5,80,0,0,870,871,5,118,0,0,
	871,872,5,15,0,0,872,1248,3,12,6,0,873,874,5,81,0,0,874,875,5,118,0,0,875,
	876,5,15,0,0,876,877,3,6,3,0,877,878,5,116,0,0,878,1248,1,0,0,0,879,880,
	5,78,0,0,880,881,5,118,0,0,881,882,5,67,0,0,882,883,3,16,8,0,883,884,5,
	15,0,0,884,885,3,12,6,0,885,1248,1,0,0,0,886,887,5,79,0,0,887,888,5,118,
	0,0,888,889,5,67,0,0,889,890,3,16,8,0,890,891,5,15,0,0,891,892,3,12,6,0,
	892,1248,1,0,0,0,893,894,5,80,0,0,894,895,5,118,0,0,895,896,5,67,0,0,896,
	897,3,16,8,0,897,898,5,15,0,0,898,899,3,12,6,0,899,1248,1,0,0,0,900,901,
	5,81,0,0,901,902,5,118,0,0,902,903,5,67,0,0,903,904,3,16,8,0,904,905,5,
	15,0,0,905,906,3,6,3,0,906,907,5,116,0,0,907,1248,1,0,0,0,908,909,5,78,
	0,0,909,910,5,118,0,0,910,911,5,67,0,0,911,912,3,16,8,0,912,913,5,116,0,
	0,913,1248,1,0,0,0,914,915,7,12,0,0,915,918,3,24,12,0,916,917,5,67,0,0,
	917,919,3,16,8,0,918,916,1,0,0,0,918,919,1,0,0,0,919,920,1,0,0,0,920,921,
	5,15,0,0,921,922,3,12,6,0,922,1248,1,0,0,0,923,924,3,6,3,0,924,925,7,13,
	0,0,925,926,3,12,6,0,926,1248,1,0,0,0,927,929,5,70,0,0,928,930,5,118,0,
	0,929,928,1,0,0,0,929,930,1,0,0,0,930,933,1,0,0,0,931,932,5,63,0,0,932,
	934,3,6,3,0,933,931,1,0,0,0,933,934,1,0,0,0,934,935,1,0,0,0,935,1248,5,
	116,0,0,936,938,5,71,0,0,937,939,5,118,0,0,938,937,1,0,0,0,938,939,1,0,
	0,0,939,942,1,0,0,0,940,941,5,63,0,0,941,943,3,6,3,0,942,940,1,0,0,0,942,
	943,1,0,0,0,943,944,1,0,0,0,944,1248,5,116,0,0,945,948,5,92,0,0,946,947,
	5,63,0,0,947,949,3,6,3,0,948,946,1,0,0,0,948,949,1,0,0,0,949,950,1,0,0,
	0,950,1248,5,116,0,0,951,954,5,93,0,0,952,953,5,63,0,0,953,955,3,6,3,0,
	954,952,1,0,0,0,954,955,1,0,0,0,955,956,1,0,0,0,956,1248,5,116,0,0,957,
	958,5,69,0,0,958,961,3,6,3,0,959,960,5,63,0,0,960,962,3,6,3,0,961,959,1,
	0,0,0,961,962,1,0,0,0,962,963,1,0,0,0,963,964,5,116,0,0,964,1248,1,0,0,
	0,965,966,5,94,0,0,966,969,3,6,3,0,967,968,5,5,0,0,968,970,5,125,0,0,969,
	967,1,0,0,0,969,970,1,0,0,0,970,971,1,0,0,0,971,972,5,116,0,0,972,1248,
	1,0,0,0,973,974,5,68,0,0,974,977,3,6,3,0,975,976,5,63,0,0,976,978,3,6,3,
	0,977,975,1,0,0,0,977,978,1,0,0,0,978,979,1,0,0,0,979,980,5,116,0,0,980,
	1248,1,0,0,0,981,982,5,68,0,0,982,1248,3,34,17,0,983,984,3,6,3,0,984,985,
	5,63,0,0,985,986,3,6,3,0,986,987,5,116,0,0,987,1248,1,0,0,0,988,989,3,6,
	3,0,989,990,5,95,0,0,990,991,3,6,3,0,991,992,5,116,0,0,992,1248,1,0,0,0,
	993,994,5,96,0,0,994,1248,3,22,11,0,995,996,5,96,0,0,996,997,5,64,0,0,997,
	1248,3,4,2,0,998,999,5,97,0,0,999,1248,3,22,11,0,1000,1001,5,97,0,0,1001,
	1002,5,64,0,0,1002,1248,3,4,2,0,1003,1007,5,98,0,0,1004,1005,7,12,0,0,1005,
	1006,5,118,0,0,1006,1008,5,15,0,0,1007,1004,1,0,0,0,1007,1008,1,0,0,0,1008,
	1009,1,0,0,0,1009,1010,3,6,3,0,1010,1011,5,64,0,0,1011,1013,5,132,0,0,1012,
	1014,3,32,16,0,1013,1012,1,0,0,0,1014,1015,1,0,0,0,1015,1013,1,0,0,0,1015,
	1016,1,0,0,0,1016,1017,1,0,0,0,1017,1018,5,133,0,0,1018,1248,1,0,0,0,1019,
	1020,5,63,0,0,1020,1021,7,12,0,0,1021,1022,5,118,0,0,1022,1023,5,15,0,0,
	1023,1024,3,6,3,0,1024,1025,5,64,0,0,1025,1031,3,4,2,0,1026,1028,5,65,0,
	0,1027,1029,5,64,0,0,1028,1027,1,0,0,0,1028,1029,1,0,0,0,1029,1030,1,0,
	0,0,1030,1032,3,4,2,0,1031,1026,1,0,0,0,1031,1032,1,0,0,0,1032,1248,1,0,
	0,0,1033,1034,5,63,0,0,1034,1035,3,6,3,0,1035,1036,5,64,0,0,1036,1045,3,
	4,2,0,1037,1038,5,65,0,0,1038,1039,5,63,0,0,1039,1040,3,6,3,0,1040,1041,
	5,64,0,0,1041,1042,3,4,2,0,1042,1044,1,0,0,0,1043,1037,1,0,0,0,1044,1047,
	1,0,0,0,1045,1043,1,0,0,0,1045,1046,1,0,0,0,1046,1053,1,0,0,0,1047,1045,
	1,0,0,0,1048,1050,5,65,0,0,1049,1051,5,64,0,0,1050,1049,1,0,0,0,1050,1051,
	1,0,0,0,1051,1052,1,0,0,0,1052,1054,3,4,2,0,1053,1048,1,0,0,0,1053,1054,
	1,0,0,0,1054,1248,1,0,0,0,1055,1056,5,118,0,0,1056,1057,5,67,0,0,1057,1248,
	3,22,11,0,1058,1059,5,95,0,0,1059,1060,3,6,3,0,1060,1061,5,64,0,0,1061,
	1067,3,4,2,0,1062,1064,5,65,0,0,1063,1065,5,64,0,0,1064,1063,1,0,0,0,1064,
	1065,1,0,0,0,1065,1066,1,0,0,0,1066,1068,3,4,2,0,1067,1062,1,0,0,0,1067,
	1068,1,0,0,0,1068,1248,1,0,0,0,1069,1071,5,99,0,0,1070,1072,7,12,0,0,1071,
	1070,1,0,0,0,1071,1072,1,0,0,0,1072,1073,1,0,0,0,1073,1074,5,118,0,0,1074,
	1075,5,33,0,0,1075,1076,3,6,3,0,1076,1077,5,100,0,0,1077,1078,3,6,3,0,1078,
	1079,5,64,0,0,1079,1085,3,4,2,0,1080,1082,5,65,0,0,1081,1083,5,64,0,0,1082,
	1081,1,0,0,0,1082,1083,1,0,0,0,1083,1084,1,0,0,0,1084,1086,3,4,2,0,1085,
	1080,1,0,0,0,1085,1086,1,0,0,0,1086,1248,1,0,0,0,1087,1089,5,99,0,0,1088,
	1090,7,12,0,0,1089,1088,1,0,0,0,1089,1090,1,0,0,0,1090,1093,1,0,0,0,1091,
	1092,5,118,0,0,1092,1094,5,5,0,0,1093,1091,1,0,0,0,1093,1094,1,0,0,0,1094,
	1097,1,0,0,0,1095,1098,5,118,0,0,1096,1098,3,24,12,0,1097,1095,1,0,0,0,
	1097,1096,1,0,0,0,1098,1099,1,0,0,0,1099,1100,5,33,0,0,1100,1103,3,6,3,
	0,1101,1102,5,101,0,0,1102,1104,3,6,3,0,1103,1101,1,0,0,0,1103,1104,1,0,
	0,0,1104,1105,1,0,0,0,1105,1106,5,64,0,0,1106,1112,3,4,2,0,1107,1109,5,
	65,0,0,1108,1110,5,64,0,0,1109,1108,1,0,0,0,1109,1110,1,0,0,0,1110,1111,
	1,0,0,0,1111,1113,3,4,2,0,1112,1107,1,0,0,0,1112,1113,1,0,0,0,1113,1248,
	1,0,0,0,1114,1115,5,31,0,0,1115,1116,3,6,3,0,1116,1117,5,116,0,0,1117,1248,
	1,0,0,0,1118,1119,5,31,0,0,1119,1120,3,6,3,0,1120,1121,5,102,0,0,1121,1122,
	5,118,0,0,1122,1123,5,64,0,0,1123,1124,3,4,2,0,1124,1248,1,0,0,0,1125,1126,
	5,118,0,0,1126,1127,5,4,0,0,1127,1128,5,6,0,0,1128,1248,5,116,0,0,1129,
	1130,5,118,0,0,1130,1131,5,4,0,0,1131,1136,3,8,4,0,1132,1133,5,5,0,0,1133,
	1135,3,8,4,0,1134,1132,1,0,0,0,1135,1138,1,0,0,0,1136,1134,1,0,0,0,1136,
	1137,1,0,0,0,1137,1139,1,0,0,0,1138,1136,1,0,0,0,1139,1140,5,6,0,0,1140,
	1141,5,116,0,0,1141,1248,1,0,0,0,1142,1143,5,118,0,0,1143,1144,5,4,0,0,
	1144,1145,5,132,0,0,1145,1151,3,8,4,0,1146,1147,5,5,0,0,1147,1148,5,116,
	0,0,1148,1150,3,8,4,0,1149,1146,1,0,0,0,1150,1153,1,0,0,0,1151,1149,1,0,
	0,0,1151,1152,1,0,0,0,1152,1154,1,0,0,0,1153,1151,1,0,0,0,1154,1155,5,116,
	0,0,1155,1156,5,133,0,0,1156,1157,5,6,0,0,1157,1158,5,116,0,0,1158,1248,
	1,0,0,0,1159,1160,3,6,3,0,1160,1161,5,11,0,0,1161,1162,5,118,0,0,1162,1163,
	5,4,0,0,1163,1164,5,6,0,0,1164,1165,5,116,0,0,1165,1248,1,0,0,0,1166,1167,
	3,6,3,0,1167,1168,5,11,0,0,1168,1169,5,118,0,0,1169,1170,5,4,0,0,1170,1175,
	3,8,4,0,1171,1172,5,5,0,0,1172,1174,3,8,4,0,1173,1171,1,0,0,0,1174,1177,
	1,0,0,0,1175,1173,1,0,0,0,1175,1176,1,0,0,0,1176,1178,1,0,0,0,1177,1175,
	1,0,0,0,1178,1179,5,6,0,0,1179,1180,5,116,0,0,1180,1248,1,0,0,0,1181,1182,
	3,6,3,0,1182,1183,5,11,0,0,1183,1184,5,118,0,0,1184,1185,5,4,0,0,1185,1186,
	5,132,0,0,1186,1192,3,8,4,0,1187,1188,5,5,0,0,1188,1189,5,116,0,0,1189,
	1191,3,8,4,0,1190,1187,1,0,0,0,1191,1194,1,0,0,0,1192,1190,1,0,0,0,1192,
	1193,1,0,0,0,1193,1195,1,0,0,0,1194,1192,1,0,0,0,1195,1196,5,116,0,0,1196,
	1197,5,133,0,0,1197,1198,5,6,0,0,1198,1199,5,116,0,0,1199,1248,1,0,0,0,
	1200,1201,3,6,3,0,1201,1202,5,21,0,0,1202,1203,5,118,0,0,1203,1204,5,4,
	0,0,1204,1205,5,6,0,0,1205,1206,5,116,0,0,1206,1248,1,0,0,0,1207,1208,3,
	6,3,0,1208,1209,5,21,0,0,1209,1210,5,118,0,0,1210,1211,5,4,0,0,1211,1216,
	3,8,4,0,1212,1213,5,5,0,0,1213,1215,3,8,4,0,1214,1212,1,0,0,0,1215,1218,
	1,0,0,0,1216,1214,1,0,0,0,1216,1217,1,0,0,0,1217,1219,1,0,0,0,1218,1216,
	1,0,0,0,1219,1220,5,6,0,0,1220,1221,5,116,0,0,1221,1248,1,0,0,0,1222,1223,
	3,6,3,0,1223,1224,5,21,0,0,1224,1225,5,118,0,0,1225,1226,5,4,0,0,1226,1227,
	5,132,0,0,1227,1233,3,8,4,0,1228,1229,5,5,0,0,1229,1230,5,116,0,0,1230,
	1232,3,8,4,0,1231,1228,1,0,0,0,1232,1235,1,0,0,0,1233,1231,1,0,0,0,1233,
	1234,1,0,0,0,1234,1236,1,0,0,0,1235,1233,1,0,0,0,1236,1237,5,116,0,0,1237,
	1238,5,133,0,0,1238,1239,5,6,0,0,1239,1240,5,116,0,0,1240,1248,1,0,0,0,
	1241,1243,5,118,0,0,1242,1244,5,118,0,0,1243,1242,1,0,0,0,1243,1244,1,0,
	0,0,1244,1245,1,0,0,0,1245,1246,5,64,0,0,1246,1248,3,4,2,0,1247,861,1,0,
	0,0,1247,865,1,0,0,0,1247,869,1,0,0,0,1247,873,1,0,0,0,1247,879,1,0,0,0,
	1247,886,1,0,0,0,1247,893,1,0,0,0,1247,900,1,0,0,0,1247,908,1,0,0,0,1247,
	914,1,0,0,0,1247,923,1,0,0,0,1247,927,1,0,0,0,1247,936,1,0,0,0,1247,945,
	1,0,0,0,1247,951,1,0,0,0,1247,957,1,0,0,0,1247,965,1,0,0,0,1247,973,1,0,
	0,0,1247,981,1,0,0,0,1247,983,1,0,0,0,1247,988,1,0,0,0,1247,993,1,0,0,0,
	1247,995,1,0,0,0,1247,998,1,0,0,0,1247,1000,1,0,0,0,1247,1003,1,0,0,0,1247,
	1019,1,0,0,0,1247,1033,1,0,0,0,1247,1055,1,0,0,0,1247,1058,1,0,0,0,1247,
	1069,1,0,0,0,1247,1087,1,0,0,0,1247,1114,1,0,0,0,1247,1118,1,0,0,0,1247,
	1125,1,0,0,0,1247,1129,1,0,0,0,1247,1142,1,0,0,0,1247,1159,1,0,0,0,1247,
	1166,1,0,0,0,1247,1181,1,0,0,0,1247,1200,1,0,0,0,1247,1207,1,0,0,0,1247,
	1222,1,0,0,0,1247,1241,1,0,0,0,1248,23,1,0,0,0,1249,1250,5,4,0,0,1250,1253,
	3,26,13,0,1251,1252,5,5,0,0,1252,1254,3,26,13,0,1253,1251,1,0,0,0,1254,
	1255,1,0,0,0,1255,1253,1,0,0,0,1255,1256,1,0,0,0,1256,1257,1,0,0,0,1257,
	1258,5,6,0,0,1258,25,1,0,0,0,1259,1262,5,118,0,0,1260,1262,3,24,12,0,1261,
	1259,1,0,0,0,1261,1260,1,0,0,0,1262,27,1,0,0,0,1263,1264,5,50,0,0,1264,
	1267,3,16,8,0,1265,1266,5,63,0,0,1266,1268,3,6,3,0,1267,1265,1,0,0,0,1267,
	1268,1,0,0,0,1268,1286,1,0,0,0,1269,1286,5,65,0,0,1270,1271,5,118,0,0,1271,
	1272,5,63,0,0,1272,1286,3,6,3,0,1273,1278,3,30,15,0,1274,1275,5,5,0,0,1275,
	1277,3,30,15,0,1276,1274,1,0,0,0,1277,1280,1,0,0,0,1278,1276,1,0,0,0,1278,
	1279,1,0,0,0,1279,1283,1,0,0,0,1280,1278,1,0,0,0,1281,1282,5,63,0,0,1282,
	1284,3,6,3,0,1283,1281,1,0,0,0,1283,1284,1,0,0,0,1284,1286,1,0,0,0,1285,
	1263,1,0,0,0,1285,1269,1,0,0,0,1285,1270,1,0,0,0,1285,1273,1,0,0,0,1286,
	29,1,0,0,0,1287,1294,3,6,3,0,1288,1289,3,6,3,0,1289,1290,5,22,0,0,1290,
	1294,1,0,0,0,1291,1292,7,10,0,0,1292,1294,3,6,3,0,1293,1287,1,0,0,0,1293,
	1288,1,0,0,0,1293,1291,1,0,0,0,1294,31,1,0,0,0,1295,1296,3,28,14,0,1296,
	1299,5,64,0,0,1297,1300,3,4,2,0,1298,1300,3,22,11,0,1299,1297,1,0,0,0,1299,
	1298,1,0,0,0,1300,33,1,0,0,0,1301,1305,5,98,0,0,1302,1303,7,12,0,0,1303,
	1304,5,118,0,0,1304,1306,5,15,0,0,1305,1302,1,0,0,0,1305,1306,1,0,0,0,1306,
	1307,1,0,0,0,1307,1308,3,6,3,0,1308,1309,5,64,0,0,1309,1311,5,132,0,0,1310,
	1312,3,36,18,0,1311,1310,1,0,0,0,1312,1313,1,0,0,0,1313,1311,1,0,0,0,1313,
	1314,1,0,0,0,1314,1315,1,0,0,0,1315,1316,5,133,0,0,1316,35,1,0,0,0,1317,
	1318,3,28,14,0,1318,1322,5,64,0,0,1319,1323,3,12,6,0,1320,1323,3,22,11,
	0,1321,1323,3,4,2,0,1322,1319,1,0,0,0,1322,1320,1,0,0,0,1322,1321,1,0,0,
	0,1323,37,1,0,0,0,1324,1325,5,103,0,0,1325,1326,5,10,0,0,1326,1329,5,118,
	0,0,1327,1328,5,104,0,0,1328,1330,5,125,0,0,1329,1327,1,0,0,0,1329,1330,
	1,0,0,0,1330,1331,1,0,0,0,1331,1332,5,26,0,0,1332,1334,5,4,0,0,1333,1335,
	3,20,10,0,1334,1333,1,0,0,0,1334,1335,1,0,0,0,1335,1340,1,0,0,0,1336,1337,
	5,5,0,0,1337,1339,3,20,10,0,1338,1336,1,0,0,0,1339,1342,1,0,0,0,1340,1338,
	1,0,0,0,1340,1341,1,0,0,0,1341,1343,1,0,0,0,1342,1340,1,0,0,0,1343,1346,
	5,6,0,0,1344,1345,7,11,0,0,1345,1347,3,16,8,0,1346,1344,1,0,0,0,1346,1347,
	1,0,0,0,1347,1350,1,0,0,0,1348,1349,5,75,0,0,1349,1351,3,16,8,0,1350,1348,
	1,0,0,0,1350,1351,1,0,0,0,1351,1352,1,0,0,0,1352,1405,5,116,0,0,1353,1354,
	5,10,0,0,1354,1355,5,118,0,0,1355,1357,5,4,0,0,1356,1358,3,20,10,0,1357,
	1356,1,0,0,0,1357,1358,1,0,0,0,1358,1363,1,0,0,0,1359,1360,5,5,0,0,1360,
	1362,3,20,10,0,1361,1359,1,0,0,0,1362,1365,1,0,0,0,1363,1361,1,0,0,0,1363,
	1364,1,0,0,0,1364,1366,1,0,0,0,1365,1363,1,0,0,0,1366,1369,5,6,0,0,1367,
	1368,5,67,0,0,1368,1370,3,16,8,0,1369,1367,1,0,0,0,1369,1370,1,0,0,0,1370,
	1373,1,0,0,0,1371,1372,5,75,0,0,1372,1374,3,16,8,0,1373,1371,1,0,0,0,1373,
	1374,1,0,0,0,1374,1375,1,0,0,0,1375,1376,5,64,0,0,1376,1405,3,4,2,0,1377,
	1379,5,105,0,0,1378,1377,1,0,0,0,1378,1379,1,0,0,0,1379,1380,1,0,0,0,1380,
	1381,5,10,0,0,1381,1382,5,118,0,0,1382,1384,5,4,0,0,1383,1385,3,20,10,0,
	1384,1383,1,0,0,0,1384,1385,1,0,0,0,1385,1390,1,0,0,0,1386,1387,5,5,0,0,
	1387,1389,3,20,10,0,1388,1386,1,0,0,0,1389,1392,1,0,0,0,1390,1388,1,0,0,
	0,1390,1391,1,0,0,0,1391,1393,1,0,0,0,1392,1390,1,0,0,0,1393,1396,5,6,0,
	0,1394,1395,5,67,0,0,1395,1397,3,16,8,0,1396,1394,1,0,0,0,1396,1397,1,0,
	0,0,1397,1400,1,0,0,0,1398,1399,5,75,0,0,1399,1401,3,16,8,0,1400,1398,1,
	0,0,0,1400,1401,1,0,0,0,1401,1402,1,0,0,0,1402,1403,5,15,0,0,1403,1405,
	3,12,6,0,1404,1324,1,0,0,0,1404,1353,1,0,0,0,1404,1378,1,0,0,0,1405,39,
	1,0,0,0,1406,1407,5,78,0,0,1407,1408,5,118,0,0,1408,1409,5,15,0,0,1409,
	1410,3,6,3,0,1410,1411,5,116,0,0,1411,1630,1,0,0,0,1412,1413,5,79,0,0,1413,
	1414,5,118,0,0,1414,1415,5,15,0,0,1415,1416,3,6,3,0,1416,1417,5,116,0,0,
	1417,1630,1,0,0,0,1418,1419,5,80,0,0,1419,1420,5,118,0,0,1420,1421,5,15,
	0,0,1421,1422,3,6,3,0,1422,1423,5,116,0,0,1423,1630,1,0,0,0,1424,1425,5,
	78,0,0,1425,1428,5,118,0,0,1426,1427,5,67,0,0,1427,1429,3,16,8,0,1428,1426,
	1,0,0,0,1428,1429,1,0,0,0,1429,1430,1,0,0,0,1430,1431,5,15,0,0,1431,1432,
	3,6,3,0,1432,1433,5,116,0,0,1433,1630,1,0,0,0,1434,1435,5,79,0,0,1435,1438,
	5,118,0,0,1436,1437,5,67,0,0,1437,1439,3,16,8,0,1438,1436,1,0,0,0,1438,
	1439,1,0,0,0,1439,1440,1,0,0,0,1440,1441,5,15,0,0,1441,1442,3,6,3,0,1442,
	1443,5,116,0,0,1443,1630,1,0,0,0,1444,1445,5,80,0,0,1445,1448,5,118,0,0,
	1446,1447,5,67,0,0,1447,1449,3,16,8,0,1448,1446,1,0,0,0,1448,1449,1,0,0,
	0,1449,1450,1,0,0,0,1450,1451,5,15,0,0,1451,1452,3,6,3,0,1452,1453,5,116,
	0,0,1453,1630,1,0,0,0,1454,1455,5,81,0,0,1455,1458,5,118,0,0,1456,1457,
	5,67,0,0,1457,1459,3,16,8,0,1458,1456,1,0,0,0,1458,1459,1,0,0,0,1459,1460,
	1,0,0,0,1460,1461,5,15,0,0,1461,1462,3,6,3,0,1462,1463,5,116,0,0,1463,1630,
	1,0,0,0,1464,1465,5,103,0,0,1465,1466,5,78,0,0,1466,1469,5,118,0,0,1467,
	1468,5,104,0,0,1468,1470,5,125,0,0,1469,1467,1,0,0,0,1469,1470,1,0,0,0,
	1470,1471,1,0,0,0,1471,1472,5,26,0,0,1472,1473,3,16,8,0,1473,1474,5,116,
	0,0,1474,1630,1,0,0,0,1475,1476,5,103,0,0,1476,1477,7,14,0,0,1477,1480,
	5,118,0,0,1478,1479,5,104,0,0,1479,1481,5,125,0,0,1480,1478,1,0,0,0,1480,
	1481,1,0,0,0,1481,1482,1,0,0,0,1482,1483,5,26,0,0,1483,1484,3,16,8,0,1484,
	1485,5,116,0,0,1485,1630,1,0,0,0,1486,1487,5,103,0,0,1487,1488,5,106,0,
	0,1488,1489,5,118,0,0,1489,1490,5,26,0,0,1490,1491,3,16,8,0,1491,1492,5,
	116,0,0,1492,1630,1,0,0,0,1493,1494,5,106,0,0,1494,1495,5,118,0,0,1495,
	1497,5,15,0,0,1496,1498,5,118,0,0,1497,1496,1,0,0,0,1497,1498,1,0,0,0,1498,
	1499,1,0,0,0,1499,1500,3,16,8,0,1500,1501,5,116,0,0,1501,1630,1,0,0,0,1502,
	1503,5,103,0,0,1503,1504,5,107,0,0,1504,1505,5,118,0,0,1505,1506,5,26,0,
	0,1506,1511,5,132,0,0,1507,1508,5,119,0,0,1508,1509,5,15,0,0,1509,1510,
	5,118,0,0,1510,1512,5,116,0,0,1511,1507,1,0,0,0,1512,1513,1,0,0,0,1513,
	1511,1,0,0,0,1513,1514,1,0,0,0,1514,1515,1,0,0,0,1515,1630,5,133,0,0,1516,
	1517,5,108,0,0,1517,1518,5,118,0,0,1518,1519,5,4,0,0,1519,1520,5,118,0,
	0,1520,1521,5,67,0,0,1521,1522,3,16,8,0,1522,1523,5,6,0,0,1523,1524,5,15,
	0,0,1524,1525,3,6,3,0,1525,1526,5,116,0,0,1526,1630,1,0,0,0,1527,1528,7,
	15,0,0,1528,1539,5,118,0,0,1529,1530,5,12,0,0,1530,1535,5,118,0,0,1531,
	1532,5,5,0,0,1532,1534,5,118,0,0,1533,1531,1,0,0,0,1534,1537,1,0,0,0,1535,
	1533,1,0,0,0,1535,1536,1,0,0,0,1536,1538,1,0,0,0,1537,1535,1,0,0,0,1538,
	1540,5,13,0,0,1539,1529,1,0,0,0,1539,1540,1,0,0,0,1540,1543,1,0,0,0,1541,
	1542,5,111,0,0,1542,1544,5,118,0,0,1543,1541,1,0,0,0,1543,1544,1,0,0,0,
	1544,1545,1,0,0,0,1545,1546,5,26,0,0,1546,1548,5,132,0,0,1547,1549,3,44,
	22,0,1548,1547,1,0,0,0,1549,1550,1,0,0,0,1550,1548,1,0,0,0,1550,1551,1,
	0,0,0,1551,1552,1,0,0,0,1552,1553,5,133,0,0,1553,1630,1,0,0,0,1554,1555,
	7,15,0,0,1555,1556,5,118,0,0,1556,1557,5,111,0,0,1557,1558,5,118,0,0,1558,
	1630,5,116,0,0,1559,1560,5,103,0,0,1560,1561,7,15,0,0,1561,1564,5,118,0,
	0,1562,1563,5,104,0,0,1563,1565,5,125,0,0,1564,1562,1,0,0,0,1564,1565,1,
	0,0,0,1565,1566,1,0,0,0,1566,1567,5,26,0,0,1567,1573,5,132,0,0,1568,1569,
	5,118,0,0,1569,1570,5,67,0,0,1570,1571,3,16,8,0,1571,1572,5,116,0,0,1572,
	1574,1,0,0,0,1573,1568,1,0,0,0,1574,1575,1,0,0,0,1575,1573,1,0,0,0,1575,
	1576,1,0,0,0,1576,1577,1,0,0,0,1577,1578,5,133,0,0,1578,1630,1,0,0,0,1579,
	1580,5,103,0,0,1580,1581,7,15,0,0,1581,1584,5,118,0,0,1582,1583,5,104,0,
	0,1583,1585,5,125,0,0,1584,1582,1,0,0,0,1584,1585,1,0,0,0,1585,1586,1,0,
	0,0,1586,1630,5,116,0,0,1587,1588,5,103,0,0,1588,1589,5,112,0,0,1589,1592,
	5,118,0,0,1590,1591,5,104,0,0,1591,1593,5,125,0,0,1592,1590,1,0,0,0,1592,
	1593,1,0,0,0,1593,1594,1,0,0,0,1594,1595,5,26,0,0,1595,1601,5,132,0,0,1596,
	1597,5,118,0,0,1597,1598,5,67,0,0,1598,1599,3,16,8,0,1599,1600,5,116,0,
	0,1600,1602,1,0,0,0,1601,1596,1,0,0,0,1602,1603,1,0,0,0,1603,1601,1,0,0,
	0,1603,1604,1,0,0,0,1604,1605,1,0,0,0,1605,1606,5,133,0,0,1606,1630,1,0,
	0,0,1607,1608,5,103,0,0,1608,1609,5,112,0,0,1609,1612,5,118,0,0,1610,1611,
	5,104,0,0,1611,1613,5,125,0,0,1612,1610,1,0,0,0,1612,1613,1,0,0,0,1613,
	1614,1,0,0,0,1614,1630,5,116,0,0,1615,1616,5,77,0,0,1616,1617,5,118,0,0,
	1617,1618,5,26,0,0,1618,1620,5,132,0,0,1619,1621,3,42,21,0,1620,1619,1,
	0,0,0,1621,1622,1,0,0,0,1622,1620,1,0,0,0,1622,1623,1,0,0,0,1623,1624,1,
	0,0,0,1624,1625,5,133,0,0,1625,1630,1,0,0,0,1626,1627,5,113,0,0,1627,1628,
	5,125,0,0,1628,1630,5,116,0,0,1629,1406,1,0,0,0,1629,1412,1,0,0,0,1629,
	1418,1,0,0,0,1629,1424,1,0,0,0,1629,1434,1,0,0,0,1629,1444,1,0,0,0,1629,
	1454,1,0,0,0,1629,1464,1,0,0,0,1629,1475,1,0,0,0,1629,1486,1,0,0,0,1629,
	1493,1,0,0,0,1629,1502,1,0,0,0,1629,1516,1,0,0,0,1629,1527,1,0,0,0,1629,
	1554,1,0,0,0,1629,1559,1,0,0,0,1629,1579,1,0,0,0,1629,1587,1,0,0,0,1629,
	1607,1,0,0,0,1629,1615,1,0,0,0,1629,1626,1,0,0,0,1630,41,1,0,0,0,1631,1632,
	5,118,0,0,1632,1633,5,67,0,0,1633,1634,3,16,8,0,1634,1635,5,116,0,0,1635,
	1656,1,0,0,0,1636,1637,5,10,0,0,1637,1638,5,118,0,0,1638,1647,5,4,0,0,1639,
	1644,3,16,8,0,1640,1641,5,5,0,0,1641,1643,3,16,8,0,1642,1640,1,0,0,0,1643,
	1646,1,0,0,0,1644,1642,1,0,0,0,1644,1645,1,0,0,0,1645,1648,1,0,0,0,1646,
	1644,1,0,0,0,1647,1639,1,0,0,0,1647,1648,1,0,0,0,1648,1649,1,0,0,0,1649,
	1652,5,6,0,0,1650,1651,5,67,0,0,1651,1653,3,16,8,0,1652,1650,1,0,0,0,1652,
	1653,1,0,0,0,1653,1654,1,0,0,0,1654,1656,5,116,0,0,1655,1631,1,0,0,0,1655,
	1636,1,0,0,0,1656,43,1,0,0,0,1657,1658,5,118,0,0,1658,1659,5,67,0,0,1659,
	1662,3,16,8,0,1660,1661,5,15,0,0,1661,1663,3,6,3,0,1662,1660,1,0,0,0,1662,
	1663,1,0,0,0,1663,1664,1,0,0,0,1664,1665,5,116,0,0,1665,45,1,0,0,0,1666,
	1667,5,114,0,0,1667,1668,5,125,0,0,1668,1669,5,116,0,0,1669,47,1,0,0,0,
	1670,1674,3,6,3,0,1671,1673,5,116,0,0,1672,1671,1,0,0,0,1673,1676,1,0,0,
	0,1674,1672,1,0,0,0,1674,1675,1,0,0,0,1675,1677,1,0,0,0,1676,1674,1,0,0,
	0,1677,1678,5,0,0,1,1678,49,1,0,0,0,1679,1680,5,115,0,0,1680,1684,3,38,
	19,0,1681,1682,5,115,0,0,1682,1684,3,40,20,0,1683,1679,1,0,0,0,1683,1681,
	1,0,0,0,1684,51,1,0,0,0,1685,1691,3,38,19,0,1686,1691,3,40,20,0,1687,1691,
	3,46,23,0,1688,1691,3,50,25,0,1689,1691,5,116,0,0,1690,1685,1,0,0,0,1690,
	1686,1,0,0,0,1690,1687,1,0,0,0,1690,1688,1,0,0,0,1690,1689,1,0,0,0,1691,
	1694,1,0,0,0,1692,1690,1,0,0,0,1692,1693,1,0,0,0,1693,1695,1,0,0,0,1694,
	1692,1,0,0,0,1695,1696,5,0,0,1,1696,53,1,0,0,0,170,58,65,71,85,98,123,136,
	150,159,168,173,175,187,194,203,208,210,224,233,242,247,249,257,264,273,
	278,280,290,299,308,313,315,326,333,342,347,349,362,376,389,418,445,448,
	453,457,472,522,538,559,575,604,639,641,646,652,658,662,664,670,675,680,
	689,708,726,732,738,743,749,758,764,769,782,786,800,810,816,818,832,859,
	918,929,933,938,942,948,954,961,969,977,1007,1015,1028,1031,1045,1050,1053,
	1064,1067,1071,1082,1085,1089,1093,1097,1103,1109,1112,1136,1151,1175,1192,
	1216,1233,1243,1247,1255,1261,1267,1278,1283,1285,1293,1299,1305,1313,1322,
	1329,1334,1340,1346,1350,1357,1363,1369,1373,1378,1384,1390,1396,1400,1404,
	1428,1438,1448,1458,1469,1480,1497,1513,1535,1539,1543,1550,1564,1575,1584,
	1592,1603,1612,1622,1629,1644,1647,1652,1655,1662,1674,1683,1690,1692];

	private static __ATN: ATN;
	public static get _ATN(): ATN {
		if (!EclangParser.__ATN) {
			EclangParser.__ATN = new ATNDeserializer().deserialize(EclangParser._serializedATN);
		}

		return EclangParser.__ATN;
	}


	static DecisionsToDFA = EclangParser._ATN.decisionToState.map( (ds: DecisionState, index: number) => new DFA(ds, index) );

}

export class NumberContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_number;
	}
	public copyFrom(ctx: NumberContext): void {
		super.copyFrom(ctx);
	}
}
export class NumberFloatingContext extends NumberContext {
	constructor(parser: EclangParser, ctx: NumberContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public FLOAT(): TerminalNode {
		return this.getToken(EclangParser.FLOAT, 0);
	}
}
export class NumberHexContext extends NumberContext {
	constructor(parser: EclangParser, ctx: NumberContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public HEX(): TerminalNode {
		return this.getToken(EclangParser.HEX, 0);
	}
}
export class NumberBinContext extends NumberContext {
	constructor(parser: EclangParser, ctx: NumberContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public BIN(): TerminalNode {
		return this.getToken(EclangParser.BIN, 0);
	}
}
export class NumberIntegerContext extends NumberContext {
	constructor(parser: EclangParser, ctx: NumberContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public INT(): TerminalNode {
		return this.getToken(EclangParser.INT, 0);
	}
}


export class PrimitiveContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_primitive;
	}
	public copyFrom(ctx: PrimitiveContext): void {
		super.copyFrom(ctx);
	}
}
export class PrimitiveBoolContext extends PrimitiveContext {
	constructor(parser: EclangParser, ctx: PrimitiveContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public BOOL(): TerminalNode {
		return this.getToken(EclangParser.BOOL, 0);
	}
}
export class PrimitiveNumberContext extends PrimitiveContext {
	constructor(parser: EclangParser, ctx: PrimitiveContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public number_(): NumberContext {
		return this.getTypedRuleContext(NumberContext, 0) as NumberContext;
	}
}
export class PrimitiveCharContext extends PrimitiveContext {
	constructor(parser: EclangParser, ctx: PrimitiveContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public CHAR(): TerminalNode {
		return this.getToken(EclangParser.CHAR, 0);
	}
}
export class PrimitiveStringContext extends PrimitiveContext {
	constructor(parser: EclangParser, ctx: PrimitiveContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
}
export class PrimitiveAtomContext extends PrimitiveContext {
	constructor(parser: EclangParser, ctx: PrimitiveContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public ATOM(): TerminalNode {
		return this.getToken(EclangParser.ATOM, 0);
	}
}


export class BlockContext extends ParserRuleContext {
	public _statement!: StatementContext;
	public _statements: StatementContext[] = [];
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public statement_list(): StatementContext[] {
		return this.getTypedRuleContexts(StatementContext) as StatementContext[];
	}
	public statement(i: number): StatementContext {
		return this.getTypedRuleContext(StatementContext, i) as StatementContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_block;
	}
}


export class ExprContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_expr;
	}
	public copyFrom(ctx: ExprContext): void {
		super.copyFrom(ctx);
	}
}
export class ExprSafeCallContext extends ExprContext {
	public _args: ExprContext[] = [];
	public _argument!: ArgumentContext;
	public _rest: ArgumentContext[] = [];
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public argument_list(): ArgumentContext[] {
		return this.getTypedRuleContexts(ArgumentContext) as ArgumentContext[];
	}
	public argument(i: number): ArgumentContext {
		return this.getTypedRuleContext(ArgumentContext, i) as ArgumentContext;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
}
export class ExprAsUnsafeContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class ExprCreateArrayContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
}
export class ExprAccessNameNullishCoalescingContext extends ExprContext {
	public _accesssed!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class ExprCastNotNullContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class ExprFunctionCallWithArgsContext extends ExprContext {
	public _args: ExprContext[] = [];
	public _argument!: ArgumentContext;
	public _rest: ArgumentContext[] = [];
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public argument_list(): ArgumentContext[] {
		return this.getTypedRuleContexts(ArgumentContext) as ArgumentContext[];
	}
	public argument(i: number): ArgumentContext {
		return this.getTypedRuleContext(ArgumentContext, i) as ArgumentContext;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class ExprHereContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
}
export class ExprCreateRecordContext extends ExprContext {
	public _qualifier!: Token;
	public _recordType!: Token;
	public _type_!: TypeContext;
	public _typeArgs: TypeContext[] = [];
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
}
export class ExprNamedContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
}
export class ExprInlineIfOldContext extends ExprContext {
	public _condition!: ExprContext;
	public _body!: ExprContext;
	public _elseBody!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
}
export class ExprLambdaContext extends ExprContext {
	public _lambdaParameter!: LambdaParameterContext;
	public _params: LambdaParameterContext[] = [];
	public _returnType!: TypeContext;
	public _body!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public lambdaParameter_list(): LambdaParameterContext[] {
		return this.getTypedRuleContexts(LambdaParameterContext) as LambdaParameterContext[];
	}
	public lambdaParameter(i: number): LambdaParameterContext {
		return this.getTypedRuleContext(LambdaParameterContext, i) as LambdaParameterContext;
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class ExprPipeContext extends ExprContext {
	public _left!: ExprContext;
	public _right!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
}
export class ExprNullContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
}
export class ExprAlignOfContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class ExprNewContext extends ExprContext {
	public _value!: ExprContext;
	public _pool!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
}
export class ExprAccessorContext extends ExprContext {
	public _accesssed!: ExprContext;
	public _index!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
}
export class ExprIsContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class ExprFunctionCallNoArgsContext extends ExprContext {
	public _args: ExprContext[] = [];
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class ExprAccessNameContext extends ExprContext {
	public _accesssed!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class ExprIsNotContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class ExprWhenContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public whenExpr(): WhenExprContext {
		return this.getTypedRuleContext(WhenExprContext, 0) as WhenExprContext;
	}
}
export class ExprNestContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
}
export class ExprInlineIfContext extends ExprContext {
	public _condition!: ExprContext;
	public _body!: ExprContext;
	public _elseBody!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
}
export class ExprItContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
}
export class ExprNullishJumpContext extends ExprContext {
	public _left!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public jump(): JumpContext {
		return this.getTypedRuleContext(JumpContext, 0) as JumpContext;
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class ExprAsContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class ExprOffsetOfContext extends ExprContext {
	public _field!: Token;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
}
export class ExprBinOpContext extends ExprContext {
	public _left!: ExprContext;
	public _op!: Token;
	public _right!: ExprContext;
	public _shr!: Token;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
}
export class ExprPrimitiveUnitContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public primitive(): PrimitiveContext {
		return this.getTypedRuleContext(PrimitiveContext, 0) as PrimitiveContext;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
}
export class ExprSizeOfContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class ExprUpdateRecordContext extends ExprContext {
	public _qualifier!: Token;
	public _recordType!: Token;
	public _type_!: TypeContext;
	public _typeArgs: TypeContext[] = [];
	public _base!: ExprContext;
	public _NAME!: Token;
	public _names: Token[] = [];
	public _expr!: ExprContext;
	public _values: ExprContext[] = [];
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
}
export class ExprAddressOfContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class ExprNewArrayOfTypeContext extends ExprContext {
	public _elementType!: TypeContext;
	public _count!: ExprContext;
	public _pool!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
}
export class ExprPrimitiveContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public primitive(): PrimitiveContext {
		return this.getTypedRuleContext(PrimitiveContext, 0) as PrimitiveContext;
	}
}
export class ExprTryContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class ExprSliceOpenContext extends ExprContext {
	public _accesssed!: ExprContext;
	public _from_!: ExprContext;
	public _op!: Token;
	public _to!: ExprContext;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
}
export class ExprFunctionReferenceContext extends ExprContext {
	public _qualifier!: Token;
	public _name!: Token;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
}
export class ExprCreateRecordNamedContext extends ExprContext {
	public _qualifier!: Token;
	public _recordType!: Token;
	public _type_!: TypeContext;
	public _typeArgs: TypeContext[] = [];
	public _NAME!: Token;
	public _names: Token[] = [];
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
}
export class ExprUnOpContext extends ExprContext {
	public _op!: Token;
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}


export class ArgumentContext extends ParserRuleContext {
	public _name!: Token;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_argument;
	}
}


export class JumpContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_jump;
	}
	public copyFrom(ctx: JumpContext): void {
		super.copyFrom(ctx);
	}
}
export class JumpContinueContext extends JumpContext {
	public _label!: Token;
	constructor(parser: EclangParser, ctx: JumpContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
}
export class JumpReturnContext extends JumpContext {
	public _value!: ExprContext;
	constructor(parser: EclangParser, ctx: JumpContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class JumpBreakContext extends JumpContext {
	public _label!: Token;
	constructor(parser: EclangParser, ctx: JumpContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
}
export class JumpThrowContext extends JumpContext {
	public _value!: ExprContext;
	constructor(parser: EclangParser, ctx: JumpContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}


export class TailContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public whenExpr(): WhenExprContext {
		return this.getTypedRuleContext(WhenExprContext, 0) as WhenExprContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_tail;
	}
}


export class LambdaParameterContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_lambdaParameter;
	}
}


export class TypeContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_type;
	}
	public copyFrom(ctx: TypeContext): void {
		super.copyFrom(ctx);
	}
}
export class TypeAddressByNameContext extends TypeContext {
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class TypeByNameParametersContext extends TypeContext {
	public _qualifier!: Token;
	public _name!: Token;
	public _type_!: TypeContext;
	public _args: TypeContext[] = [];
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}
export class TypeNullableContext extends TypeContext {
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class TypeTypeOfContext extends TypeContext {
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class TypeCallSignatureContext extends TypeContext {
	public _returnType!: TypeContext;
	public _type_!: TypeContext;
	public _argTypes: TypeContext[] = [];
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
}
export class TypeAtomContext extends TypeContext {
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public ATOM(): TerminalNode {
		return this.getToken(EclangParser.ATOM, 0);
	}
}
export class TypeGenericContext extends TypeContext {
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
}
export class TypeUnionContext extends TypeContext {
	public _types: TypeContext[] = [];
	public _type_!: TypeContext;
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}
export class TypeEnumContext extends TypeContext {
	public _types: TypeContext[] = [];
	public _type_!: TypeContext;
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}
export class TypeArrayContext extends TypeContext {
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class TypeTupleContext extends TypeContext {
	public _type_!: TypeContext;
	public _types: TypeContext[] = [];
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}
export class TypeThrowsContext extends TypeContext {
	public _left!: TypeContext;
	public _throwing!: TypeContext;
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}
export class TypeByNameContext extends TypeContext {
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
}
export class TypeUnionAnonContext extends TypeContext {
	public _type_!: TypeContext;
	public _types: TypeContext[] = [];
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}
export class TypeNestContext extends TypeContext {
	constructor(parser: EclangParser, ctx: TypeContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}


export class InterfaceContext extends ParserRuleContext {
	public _name!: Token;
	public _NAME!: Token;
	public _names: Token[] = [];
	public _type_!: TypeContext;
	public _types: TypeContext[] = [];
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_interface;
	}
}


export class ParameterContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_parameter;
	}
	public copyFrom(ctx: ParameterContext): void {
		super.copyFrom(ctx);
	}
}
export class ParameterTypedContext extends ParameterContext {
	constructor(parser: EclangParser, ctx: ParameterContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class ParameterVarArgCContext extends ParameterContext {
	constructor(parser: EclangParser, ctx: ParameterContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
}
export class ParameterConstContext extends ParameterContext {
	constructor(parser: EclangParser, ctx: ParameterContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public parameter(): ParameterContext {
		return this.getTypedRuleContext(ParameterContext, 0) as ParameterContext;
	}
}
export class ParameterDefaultContext extends ParameterContext {
	constructor(parser: EclangParser, ctx: ParameterContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class ParameterVariableContext extends ParameterContext {
	constructor(parser: EclangParser, ctx: ParameterContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public parameter(): ParameterContext {
		return this.getTypedRuleContext(ParameterContext, 0) as ParameterContext;
	}
}
export class ParameterVarArgContext extends ParameterContext {
	constructor(parser: EclangParser, ctx: ParameterContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class ParameterTypedDefaultContext extends ParameterContext {
	constructor(parser: EclangParser, ctx: ParameterContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class ParameterVolatileContext extends ParameterContext {
	constructor(parser: EclangParser, ctx: ParameterContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public parameter(): ParameterContext {
		return this.getTypedRuleContext(ParameterContext, 0) as ParameterContext;
	}
}


export class StatementContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_statement;
	}
	public copyFrom(ctx: StatementContext): void {
		super.copyFrom(ctx);
	}
}
export class StatementContinueContext extends StatementContext {
	public _label!: Token;
	public _condition!: ExprContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class StatementReturnContext extends StatementContext {
	public _return_!: ExprContext;
	public _condition!: ExprContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
	public whenExpr(): WhenExprContext {
		return this.getTypedRuleContext(WhenExprContext, 0) as WhenExprContext;
	}
}
export class StatementYieldContext extends StatementContext {
	public _condition!: ExprContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class StatementLetIfContext extends StatementContext {
	public _kind!: Token;
	public _value!: ExprContext;
	public _body!: BlockContext;
	public _elseBody!: BlockContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public block_list(): BlockContext[] {
		return this.getTypedRuleContexts(BlockContext) as BlockContext[];
	}
	public block(i: number): BlockContext {
		return this.getTypedRuleContext(BlockContext, i) as BlockContext;
	}
}
export class StatementBreakContext extends StatementContext {
	public _label!: Token;
	public _condition!: ExprContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class StatementTailWhileContext extends StatementContext {
	public _expression!: ExprContext;
	public _condition!: ExprContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
}
export class StatementErrDeferContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public statement(): StatementContext {
		return this.getTypedRuleContext(StatementContext, 0) as StatementContext;
	}
}
export class StatementLabeledContext extends StatementContext {
	public _label!: Token;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public statement(): StatementContext {
		return this.getTypedRuleContext(StatementContext, 0) as StatementContext;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
}
export class StatementForInRangeContext extends StatementContext {
	public _lower!: ExprContext;
	public _upper!: ExprContext;
	public _body!: BlockContext;
	public _elseBody!: BlockContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
	public block_list(): BlockContext[] {
		return this.getTypedRuleContexts(BlockContext) as BlockContext[];
	}
	public block(i: number): BlockContext {
		return this.getTypedRuleContext(BlockContext, i) as BlockContext;
	}
}
export class StatementForInContext extends StatementContext {
	public _index!: Token;
	public _name!: Token;
	public _parts!: DestructureContext;
	public _iterable!: ExprContext;
	public _filter!: ExprContext;
	public _body!: BlockContext;
	public _elseBody!: BlockContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
	public block_list(): BlockContext[] {
		return this.getTypedRuleContexts(BlockContext) as BlockContext[];
	}
	public block(i: number): BlockContext {
		return this.getTypedRuleContext(BlockContext, i) as BlockContext;
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public destructure(): DestructureContext {
		return this.getTypedRuleContext(DestructureContext, 0) as DestructureContext;
	}
}
export class StatementDefineValTypedContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
}
export class StatementDefineConstContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
}
export class StatementPoolContext extends StatementContext {
	public _keyword!: Token;
	public _name!: Token;
	public _body!: BlockContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public block(): BlockContext {
		return this.getTypedRuleContext(BlockContext, 0) as BlockContext;
	}
}
export class StatementDeclareVarContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
}
export class StatementPassContext extends StatementContext {
	public _condition!: ExprContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class StatementErrDeferDoContext extends StatementContext {
	public _body!: BlockContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public block(): BlockContext {
		return this.getTypedRuleContext(BlockContext, 0) as BlockContext;
	}
}
export class StatementFunctionCallWithArgsContext extends StatementContext {
	public _argument!: ArgumentContext;
	public _rest: ArgumentContext[] = [];
	public _expr!: ExprContext;
	public _args: ExprContext[] = [];
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public argument_list(): ArgumentContext[] {
		return this.getTypedRuleContexts(ArgumentContext) as ArgumentContext[];
	}
	public argument(i: number): ArgumentContext {
		return this.getTypedRuleContext(ArgumentContext, i) as ArgumentContext;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class StatementDefineVolContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
}
export class StatementDestructureContext extends StatementContext {
	public _kind!: Token;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public destructure(): DestructureContext {
		return this.getTypedRuleContext(DestructureContext, 0) as DestructureContext;
	}
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class StatementAssignmentContext extends StatementContext {
	public _target!: ExprContext;
	public _op!: Token;
	public _value!: TailContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
}
export class StatementThrowContext extends StatementContext {
	public _throw_!: ExprContext;
	public _condition!: ExprContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
}
export class StatementTryCatchContext extends StatementContext {
	public _left!: ExprContext;
	public _elseBody!: BlockContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public block(): BlockContext {
		return this.getTypedRuleContext(BlockContext, 0) as BlockContext;
	}
}
export class StatementDeferDoContext extends StatementContext {
	public _body!: BlockContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public block(): BlockContext {
		return this.getTypedRuleContext(BlockContext, 0) as BlockContext;
	}
}
export class StatementAssertContext extends StatementContext {
	public _condition!: ExprContext;
	public _message!: Token;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
}
export class StatementDeferContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public statement(): StatementContext {
		return this.getTypedRuleContext(StatementContext, 0) as StatementContext;
	}
}
export class StatementDefineVarTypedContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
}
export class StatementFunctionCallNoArgsContext extends StatementContext {
	public _expr!: ExprContext;
	public _args: ExprContext[] = [];
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class StatementTryContext extends StatementContext {
	public _left!: ExprContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class StatementDefineConstTypedContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
}
export class StatementDefineVarContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
}
export class StatementWhenContext extends StatementContext {
	public _kind!: Token;
	public _subject!: ExprContext;
	public _whenArm!: WhenArmContext;
	public _arms: WhenArmContext[] = [];
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public whenArm_list(): WhenArmContext[] {
		return this.getTypedRuleContexts(WhenArmContext) as WhenArmContext[];
	}
	public whenArm(i: number): WhenArmContext {
		return this.getTypedRuleContext(WhenArmContext, i) as WhenArmContext;
	}
}
export class StatementDefineVolTypedContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
}
export class StatementTailIfContext extends StatementContext {
	public _expression!: ExprContext;
	public _condition!: ExprContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
}
export class StatementWhileContext extends StatementContext {
	public _condition!: ExprContext;
	public _body!: BlockContext;
	public _elseBody!: BlockContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public block_list(): BlockContext[] {
		return this.getTypedRuleContexts(BlockContext) as BlockContext[];
	}
	public block(i: number): BlockContext {
		return this.getTypedRuleContext(BlockContext, i) as BlockContext;
	}
}
export class StatementIfContext extends StatementContext {
	public _expr!: ExprContext;
	public _conditions: ExprContext[] = [];
	public _block!: BlockContext;
	public _bodies: BlockContext[] = [];
	public _elseBody!: BlockContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
	public block_list(): BlockContext[] {
		return this.getTypedRuleContexts(BlockContext) as BlockContext[];
	}
	public block(i: number): BlockContext {
		return this.getTypedRuleContext(BlockContext, i) as BlockContext;
	}
}
export class StatementDefineValContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
}
export class StatementSafeCallContext extends StatementContext {
	public _expr!: ExprContext;
	public _args: ExprContext[] = [];
	public _argument!: ArgumentContext;
	public _rest: ArgumentContext[] = [];
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public argument_list(): ArgumentContext[] {
		return this.getTypedRuleContexts(ArgumentContext) as ArgumentContext[];
	}
	public argument(i: number): ArgumentContext {
		return this.getTypedRuleContext(ArgumentContext, i) as ArgumentContext;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
}


export class DestructureContext extends ParserRuleContext {
	public _destructurePart!: DestructurePartContext;
	public _parts: DestructurePartContext[] = [];
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public destructurePart_list(): DestructurePartContext[] {
		return this.getTypedRuleContexts(DestructurePartContext) as DestructurePartContext[];
	}
	public destructurePart(i: number): DestructurePartContext {
		return this.getTypedRuleContext(DestructurePartContext, i) as DestructurePartContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_destructure;
	}
}


export class DestructurePartContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public destructure(): DestructureContext {
		return this.getTypedRuleContext(DestructureContext, 0) as DestructureContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_destructurePart;
	}
}


export class WhenPatternContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_whenPattern;
	}
	public copyFrom(ctx: WhenPatternContext): void {
		super.copyFrom(ctx);
	}
}
export class WhenPatternBindContext extends WhenPatternContext {
	public _guard!: ExprContext;
	constructor(parser: EclangParser, ctx: WhenPatternContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class WhenPatternValuesContext extends WhenPatternContext {
	public _whenValue!: WhenValueContext;
	public _values: WhenValueContext[] = [];
	public _guard!: ExprContext;
	constructor(parser: EclangParser, ctx: WhenPatternContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public whenValue_list(): WhenValueContext[] {
		return this.getTypedRuleContexts(WhenValueContext) as WhenValueContext[];
	}
	public whenValue(i: number): WhenValueContext {
		return this.getTypedRuleContext(WhenValueContext, i) as WhenValueContext;
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class WhenPatternIsContext extends WhenPatternContext {
	public _guard!: ExprContext;
	constructor(parser: EclangParser, ctx: WhenPatternContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class WhenPatternElseContext extends WhenPatternContext {
	constructor(parser: EclangParser, ctx: WhenPatternContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
}


export class WhenValueContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_whenValue;
	}
	public copyFrom(ctx: WhenValueContext): void {
		super.copyFrom(ctx);
	}
}
export class WhenValuePlainContext extends WhenValueContext {
	constructor(parser: EclangParser, ctx: WhenValueContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class WhenValueFromContext extends WhenValueContext {
	public _from_!: ExprContext;
	constructor(parser: EclangParser, ctx: WhenValueContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}
export class WhenValueUpToContext extends WhenValueContext {
	public _op!: Token;
	public _to!: ExprContext;
	constructor(parser: EclangParser, ctx: WhenValueContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
}


export class WhenArmContext extends ParserRuleContext {
	public _body!: BlockContext;
	public _line!: StatementContext;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public whenPattern(): WhenPatternContext {
		return this.getTypedRuleContext(WhenPatternContext, 0) as WhenPatternContext;
	}
	public block(): BlockContext {
		return this.getTypedRuleContext(BlockContext, 0) as BlockContext;
	}
	public statement(): StatementContext {
		return this.getTypedRuleContext(StatementContext, 0) as StatementContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_whenArm;
	}
}


export class WhenExprContext extends ParserRuleContext {
	public _kind!: Token;
	public _subject!: ExprContext;
	public _whenValueArm!: WhenValueArmContext;
	public _arms: WhenValueArmContext[] = [];
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public whenValueArm_list(): WhenValueArmContext[] {
		return this.getTypedRuleContexts(WhenValueArmContext) as WhenValueArmContext[];
	}
	public whenValueArm(i: number): WhenValueArmContext {
		return this.getTypedRuleContext(WhenValueArmContext, i) as WhenValueArmContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_whenExpr;
	}
}


export class WhenValueArmContext extends ParserRuleContext {
	public _value!: TailContext;
	public _line!: StatementContext;
	public _body!: BlockContext;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public whenPattern(): WhenPatternContext {
		return this.getTypedRuleContext(WhenPatternContext, 0) as WhenPatternContext;
	}
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
	public statement(): StatementContext {
		return this.getTypedRuleContext(StatementContext, 0) as StatementContext;
	}
	public block(): BlockContext {
		return this.getTypedRuleContext(BlockContext, 0) as BlockContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_whenValueArm;
	}
}


export class FuncContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_func;
	}
	public copyFrom(ctx: FuncContext): void {
		super.copyFrom(ctx);
	}
}
export class FunctionInlineContext extends FuncContext {
	public _name!: Token;
	public _parameter!: ParameterContext;
	public _params: ParameterContext[] = [];
	public _returnType!: TypeContext;
	public _throwing!: TypeContext;
	public _expression!: TailContext;
	constructor(parser: EclangParser, ctx: FuncContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
	public parameter_list(): ParameterContext[] {
		return this.getTypedRuleContexts(ParameterContext) as ParameterContext[];
	}
	public parameter(i: number): ParameterContext {
		return this.getTypedRuleContext(ParameterContext, i) as ParameterContext;
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}
export class FunctionBlockContext extends FuncContext {
	public _name!: Token;
	public _parameter!: ParameterContext;
	public _params: ParameterContext[] = [];
	public _returnType!: TypeContext;
	public _throwing!: TypeContext;
	public _body!: BlockContext;
	constructor(parser: EclangParser, ctx: FuncContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public block(): BlockContext {
		return this.getTypedRuleContext(BlockContext, 0) as BlockContext;
	}
	public parameter_list(): ParameterContext[] {
		return this.getTypedRuleContexts(ParameterContext) as ParameterContext[];
	}
	public parameter(i: number): ParameterContext {
		return this.getTypedRuleContext(ParameterContext, i) as ParameterContext;
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}
export class FunctionDeclareContext extends FuncContext {
	public _name!: Token;
	public _externalName!: Token;
	public _parameter!: ParameterContext;
	public _params: ParameterContext[] = [];
	public _returnType!: TypeContext;
	public _throwing!: TypeContext;
	constructor(parser: EclangParser, ctx: FuncContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
	public parameter_list(): ParameterContext[] {
		return this.getTypedRuleContexts(ParameterContext) as ParameterContext[];
	}
	public parameter(i: number): ParameterContext {
		return this.getTypedRuleContext(ParameterContext, i) as ParameterContext;
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}


export class GlobalContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_global;
	}
	public copyFrom(ctx: GlobalContext): void {
		super.copyFrom(ctx);
	}
}
export class GlobalDefineConstTypedContext extends GlobalContext {
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class GlobalUnitDeclareContext extends GlobalContext {
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
}
export class GlobalStructDeclareContext extends GlobalContext {
	public _name!: Token;
	public _externalName!: Token;
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
}
export class GlobalDefineConstContext extends GlobalContext {
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
}
export class GlobalRecordDeclareDefineContext extends GlobalContext {
	public _name!: Token;
	public _externalName!: Token;
	public _NAME!: Token;
	public _names: Token[] = [];
	public _type_!: TypeContext;
	public _types: TypeContext[] = [];
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}
export class GlobalStructDeclareDefineContext extends GlobalContext {
	public _name!: Token;
	public _externalName!: Token;
	public _NAME!: Token;
	public _names: Token[] = [];
	public _type_!: TypeContext;
	public _types: TypeContext[] = [];
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}
export class GlobalDefineContext extends GlobalContext {
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
}
export class GlobalEnumDeclareContext extends GlobalContext {
	public _name!: Token;
	public _ATOM!: Token;
	public _atoms: Token[] = [];
	public _NAME!: Token;
	public _constants: Token[] = [];
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public ATOM_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.ATOM);
	}
	public ATOM(i: number): TerminalNode {
		return this.getToken(EclangParser.ATOM, i);
	}
}
export class GlobalTypeDefineContext extends GlobalContext {
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
}
export class GlobalDeclareDefineContext extends GlobalContext {
	public _name!: Token;
	public _externalName!: Token;
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
}
export class GlobalConstantContext extends GlobalContext {
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class GlobalRecordDeclareContext extends GlobalContext {
	public _name!: Token;
	public _externalName!: Token;
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
}
export class GlobalInterfaceDefineContext extends GlobalContext {
	public _name!: Token;
	public _interfaceMember!: InterfaceMemberContext;
	public _members: InterfaceMemberContext[] = [];
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public interfaceMember_list(): InterfaceMemberContext[] {
		return this.getTypedRuleContexts(InterfaceMemberContext) as InterfaceMemberContext[];
	}
	public interfaceMember(i: number): InterfaceMemberContext {
		return this.getTypedRuleContext(InterfaceMemberContext, i) as InterfaceMemberContext;
	}
}
export class GlobalDeclareConstContext extends GlobalContext {
	public _name!: Token;
	public _externalName!: Token;
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
}
export class GlobalRecordDefineContext extends GlobalContext {
	public _name!: Token;
	public _NAME!: Token;
	public _typeParams: Token[] = [];
	public _extending!: Token;
	public _recordField!: RecordFieldContext;
	public _fields: RecordFieldContext[] = [];
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public INDENT(): TerminalNode {
		return this.getToken(EclangParser.INDENT, 0);
	}
	public DEDENT(): TerminalNode {
		return this.getToken(EclangParser.DEDENT, 0);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public recordField_list(): RecordFieldContext[] {
		return this.getTypedRuleContexts(RecordFieldContext) as RecordFieldContext[];
	}
	public recordField(i: number): RecordFieldContext {
		return this.getTypedRuleContext(RecordFieldContext, i) as RecordFieldContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
}
export class GlobalDefineVolatileContext extends GlobalContext {
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
}
export class GlobalDefineTypedContext extends GlobalContext {
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}
export class GlobalTypeAliasContext extends GlobalContext {
	public _name!: Token;
	public _modifier!: Token;
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
}
export class GlobalNamespaceContext extends GlobalContext {
	public _name!: Token;
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
}
export class GlobalDefineVolatileTypedContext extends GlobalContext {
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
}


export class InterfaceMemberContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_interfaceMember;
	}
	public copyFrom(ctx: InterfaceMemberContext): void {
		super.copyFrom(ctx);
	}
}
export class InterfaceFieldContext extends InterfaceMemberContext {
	public _name!: Token;
	constructor(parser: EclangParser, ctx: InterfaceMemberContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
}
export class InterfaceMethodContext extends InterfaceMemberContext {
	public _name!: Token;
	public _type_!: TypeContext;
	public _params: TypeContext[] = [];
	public _returnType!: TypeContext;
	constructor(parser: EclangParser, ctx: InterfaceMemberContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
}


export class RecordFieldContext extends ParserRuleContext {
	public _name!: Token;
	public _value!: ExprContext;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public type_(): TypeContext {
		return this.getTypedRuleContext(TypeContext, 0) as TypeContext;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_recordField;
	}
}


export class UseContext extends ParserRuleContext {
	public _from_!: Token;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_use;
	}
}


export class InterpolationContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public EOF(): TerminalNode {
		return this.getToken(EclangParser.EOF, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_interpolation;
	}
}


export class PrivateDeclarationContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_privateDeclaration;
	}
	public copyFrom(ctx: PrivateDeclarationContext): void {
		super.copyFrom(ctx);
	}
}
export class PrivateFunctionContext extends PrivateDeclarationContext {
	constructor(parser: EclangParser, ctx: PrivateDeclarationContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public func(): FuncContext {
		return this.getTypedRuleContext(FuncContext, 0) as FuncContext;
	}
}
export class PrivateGlobalContext extends PrivateDeclarationContext {
	constructor(parser: EclangParser, ctx: PrivateDeclarationContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public global(): GlobalContext {
		return this.getTypedRuleContext(GlobalContext, 0) as GlobalContext;
	}
}


export class ProgramContext extends ParserRuleContext {
	public _func!: FuncContext;
	public _functions: FuncContext[] = [];
	public _global!: GlobalContext;
	public _globals: GlobalContext[] = [];
	public _use!: UseContext;
	public _uses: UseContext[] = [];
	public _privateDeclaration!: PrivateDeclarationContext;
	public _privates: PrivateDeclarationContext[] = [];
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public EOF(): TerminalNode {
		return this.getToken(EclangParser.EOF, 0);
	}
	public NL_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NL);
	}
	public NL(i: number): TerminalNode {
		return this.getToken(EclangParser.NL, i);
	}
	public func_list(): FuncContext[] {
		return this.getTypedRuleContexts(FuncContext) as FuncContext[];
	}
	public func(i: number): FuncContext {
		return this.getTypedRuleContext(FuncContext, i) as FuncContext;
	}
	public global_list(): GlobalContext[] {
		return this.getTypedRuleContexts(GlobalContext) as GlobalContext[];
	}
	public global(i: number): GlobalContext {
		return this.getTypedRuleContext(GlobalContext, i) as GlobalContext;
	}
	public use_list(): UseContext[] {
		return this.getTypedRuleContexts(UseContext) as UseContext[];
	}
	public use(i: number): UseContext {
		return this.getTypedRuleContext(UseContext, i) as UseContext;
	}
	public privateDeclaration_list(): PrivateDeclarationContext[] {
		return this.getTypedRuleContexts(PrivateDeclarationContext) as PrivateDeclarationContext[];
	}
	public privateDeclaration(i: number): PrivateDeclarationContext {
		return this.getTypedRuleContext(PrivateDeclarationContext, i) as PrivateDeclarationContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_program;
	}
}
