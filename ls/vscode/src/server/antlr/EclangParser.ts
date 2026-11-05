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
	public static readonly RULE_fieldWidth = 23;
	public static readonly RULE_recordModifier = 24;
	public static readonly RULE_use = 25;
	public static readonly RULE_interpolation = 26;
	public static readonly RULE_exportDeclaration = 27;
	public static readonly RULE_privateDeclaration = 28;
	public static readonly RULE_program = 29;
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
		"global", "interfaceMember", "recordField", "fieldWidth", "recordModifier", 
		"use", "interpolation", "exportDeclaration", "privateDeclaration", "program",
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
			this.state = 64;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 121:
				localctx = new NumberIntegerContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 60;
				this.match(EclangParser.INT);
				}
				break;
			case 120:
				localctx = new NumberFloatingContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 61;
				this.match(EclangParser.FLOAT);
				}
				break;
			case 122:
				localctx = new NumberHexContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 62;
				this.match(EclangParser.HEX);
				}
				break;
			case 123:
				localctx = new NumberBinContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 63;
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
			this.state = 71;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 120:
			case 121:
			case 122:
			case 123:
				localctx = new PrimitiveNumberContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 66;
				this.number_();
				}
				break;
			case 117:
				localctx = new PrimitiveBoolContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 67;
				this.match(EclangParser.BOOL);
				}
				break;
			case 125:
				localctx = new PrimitiveStringContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 68;
				this.match(EclangParser.STRING);
				}
				break;
			case 124:
				localctx = new PrimitiveCharContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 69;
				this.match(EclangParser.CHAR);
				}
				break;
			case 119:
				localctx = new PrimitiveAtomContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 70;
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
			this.state = 73;
			this.match(EclangParser.INDENT);
			this.state = 75;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 74;
				localctx._statement = this.statement();
				localctx._statements.push(localctx._statement);
				}
				}
				this.state = 77;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while ((((_la) & ~0x1F) === 0 && ((1 << _la) & 3791931294) !== 0) || ((((_la - 32)) & ~0x1F) === 0 && ((1 << (_la - 32)) & 2147483769) !== 0) || ((((_la - 68)) & ~0x1F) === 0 && ((1 << (_la - 68)) & 4278205455) !== 0) || ((((_la - 117)) & ~0x1F) === 0 && ((1 << (_la - 117)) & 511) !== 0));
			this.state = 79;
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
			this.state = 483;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 47, this._ctx) ) {
			case 1:
				{
				localctx = new ExprHereContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;

				this.state = 82;
				this.match(EclangParser.T__0);
				}
				break;
			case 2:
				{
				localctx = new ExprNullContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 83;
				this.match(EclangParser.T__1);
				}
				break;
			case 3:
				{
				localctx = new ExprItContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 84;
				this.match(EclangParser.T__2);
				}
				break;
			case 4:
				{
				localctx = new ExprNestContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 85;
				this.match(EclangParser.T__3);
				this.state = 86;
				this.expr(0);
				this.state = 91;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 87;
					this.match(EclangParser.T__4);
					this.state = 88;
					this.expr(0);
					}
					}
					this.state = 93;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 94;
				this.match(EclangParser.T__5);
				}
				break;
			case 5:
				{
				localctx = new ExprNestContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 96;
				this.match(EclangParser.T__3);
				this.state = 97;
				this.match(EclangParser.INDENT);
				this.state = 98;
				this.expr(0);
				this.state = 104;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 99;
					this.match(EclangParser.T__4);
					this.state = 100;
					this.match(EclangParser.NL);
					this.state = 101;
					this.expr(0);
					}
					}
					this.state = 106;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 107;
				this.match(EclangParser.NL);
				this.state = 108;
				this.match(EclangParser.DEDENT);
				this.state = 109;
				this.match(EclangParser.T__5);
				}
				break;
			case 6:
				{
				localctx = new ExprPrimitiveUnitContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 111;
				this.primitive();
				this.state = 112;
				this.match(EclangParser.NAME);
				}
				break;
			case 7:
				{
				localctx = new ExprPrimitiveContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 114;
				this.primitive();
				}
				break;
			case 8:
				{
				localctx = new ExprSizeOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 115;
				this.match(EclangParser.T__6);
				this.state = 116;
				this.type_(0);
				}
				break;
			case 9:
				{
				localctx = new ExprAlignOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 117;
				this.match(EclangParser.T__7);
				this.state = 118;
				this.type_(0);
				}
				break;
			case 10:
				{
				localctx = new ExprOffsetOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 119;
				this.match(EclangParser.T__8);
				this.state = 120;
				this.match(EclangParser.T__3);
				this.state = 121;
				this.type_(0);
				this.state = 122;
				this.match(EclangParser.T__4);
				this.state = 123;
				(localctx as ExprOffsetOfContext)._field = this.match(EclangParser.NAME);
				this.state = 124;
				this.match(EclangParser.T__5);
				}
				break;
			case 11:
				{
				localctx = new ExprFunctionReferenceContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 126;
				this.match(EclangParser.T__9);
				this.state = 129;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 5, this._ctx) ) {
				case 1:
					{
					this.state = 127;
					(localctx as ExprFunctionReferenceContext)._qualifier = this.match(EclangParser.NAME);
					this.state = 128;
					this.match(EclangParser.T__10);
					}
					break;
				}
				this.state = 131;
				(localctx as ExprFunctionReferenceContext)._name = this.match(EclangParser.NAME);
				}
				break;
			case 12:
				{
				localctx = new ExprFunctionCallNoArgsContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 132;
				this.match(EclangParser.NAME);
				this.state = 133;
				this.match(EclangParser.T__3);
				this.state = 134;
				this.match(EclangParser.T__5);
				}
				break;
			case 13:
				{
				localctx = new ExprFunctionCallWithArgsContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 135;
				this.match(EclangParser.NAME);
				this.state = 136;
				this.match(EclangParser.T__3);
				this.state = 137;
				(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
				this.state = 142;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 138;
					this.match(EclangParser.T__4);
					this.state = 139;
					(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 144;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 145;
				this.match(EclangParser.T__5);
				}
				break;
			case 14:
				{
				localctx = new ExprFunctionCallWithArgsContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 147;
				this.match(EclangParser.NAME);
				this.state = 148;
				this.match(EclangParser.T__3);
				this.state = 149;
				this.match(EclangParser.INDENT);
				this.state = 150;
				(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
				this.state = 156;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 151;
					this.match(EclangParser.T__4);
					this.state = 152;
					this.match(EclangParser.NL);
					this.state = 153;
					(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 158;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 159;
				this.match(EclangParser.NL);
				this.state = 160;
				this.match(EclangParser.DEDENT);
				this.state = 161;
				this.match(EclangParser.T__5);
				}
				break;
			case 15:
				{
				localctx = new ExprCreateRecordNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 181;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 165;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 8, this._ctx) ) {
					case 1:
						{
						this.state = 163;
						(localctx as ExprCreateRecordNamedContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 164;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 167;
					(localctx as ExprCreateRecordNamedContext)._recordType = this.match(EclangParser.NAME);
					this.state = 179;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 168;
						this.match(EclangParser.T__11);
						this.state = 169;
						(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
						this.state = 174;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 170;
							this.match(EclangParser.T__4);
							this.state = 171;
							(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
							}
							}
							this.state = 176;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 177;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 183;
				this.match(EclangParser.T__13);
				this.state = 184;
				(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
				(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
				this.state = 185;
				this.match(EclangParser.T__14);
				this.state = 186;
				this.expr(0);
				this.state = 193;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 187;
					this.match(EclangParser.T__4);
					this.state = 188;
					(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
					this.state = 189;
					this.match(EclangParser.T__14);
					this.state = 190;
					this.expr(0);
					}
					}
					this.state = 195;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 196;
				this.match(EclangParser.T__15);
				}
				break;
			case 16:
				{
				localctx = new ExprCreateRecordNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 216;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 200;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 13, this._ctx) ) {
					case 1:
						{
						this.state = 198;
						(localctx as ExprCreateRecordNamedContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 199;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 202;
					(localctx as ExprCreateRecordNamedContext)._recordType = this.match(EclangParser.NAME);
					this.state = 214;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 203;
						this.match(EclangParser.T__11);
						this.state = 204;
						(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
						this.state = 209;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 205;
							this.match(EclangParser.T__4);
							this.state = 206;
							(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
							}
							}
							this.state = 211;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 212;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 218;
				this.match(EclangParser.T__13);
				this.state = 219;
				this.match(EclangParser.INDENT);
				this.state = 220;
				(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
				(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
				this.state = 221;
				this.match(EclangParser.T__14);
				this.state = 222;
				this.expr(0);
				this.state = 230;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 223;
					this.match(EclangParser.T__4);
					this.state = 224;
					this.match(EclangParser.NL);
					this.state = 225;
					(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
					this.state = 226;
					this.match(EclangParser.T__14);
					this.state = 227;
					this.expr(0);
					}
					}
					this.state = 232;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 233;
				this.match(EclangParser.NL);
				this.state = 234;
				this.match(EclangParser.DEDENT);
				this.state = 235;
				this.match(EclangParser.T__15);
				}
				break;
			case 17:
				{
				localctx = new ExprCreateRecordNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 239;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 18, this._ctx) ) {
				case 1:
					{
					this.state = 237;
					(localctx as ExprCreateRecordNamedContext)._qualifier = this.match(EclangParser.NAME);
					this.state = 238;
					this.match(EclangParser.T__10);
					}
					break;
				}
				this.state = 241;
				(localctx as ExprCreateRecordNamedContext)._recordType = this.match(EclangParser.NAME);
				this.state = 253;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===12) {
					{
					this.state = 242;
					this.match(EclangParser.T__11);
					this.state = 243;
					(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
					(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
					this.state = 248;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===5) {
						{
						{
						this.state = 244;
						this.match(EclangParser.T__4);
						this.state = 245;
						(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
						}
						}
						this.state = 250;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					this.state = 251;
					this.match(EclangParser.T__12);
					}
				}

				this.state = 255;
				this.match(EclangParser.T__13);
				this.state = 256;
				this.match(EclangParser.T__15);
				}
				break;
			case 18:
				{
				localctx = new ExprCreateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 275;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 259;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 21, this._ctx) ) {
					case 1:
						{
						this.state = 257;
						(localctx as ExprCreateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 258;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 261;
					(localctx as ExprCreateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 273;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 262;
						this.match(EclangParser.T__11);
						this.state = 263;
						(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
						this.state = 268;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 264;
							this.match(EclangParser.T__4);
							this.state = 265;
							(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
							}
							}
							this.state = 270;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 271;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 277;
				this.match(EclangParser.T__13);
				this.state = 278;
				this.expr(0);
				this.state = 283;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 279;
					this.match(EclangParser.T__4);
					this.state = 280;
					this.expr(0);
					}
					}
					this.state = 285;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 286;
				this.match(EclangParser.T__15);
				}
				break;
			case 19:
				{
				localctx = new ExprCreateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 306;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 290;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 26, this._ctx) ) {
					case 1:
						{
						this.state = 288;
						(localctx as ExprCreateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 289;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 292;
					(localctx as ExprCreateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 304;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 293;
						this.match(EclangParser.T__11);
						this.state = 294;
						(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
						this.state = 299;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 295;
							this.match(EclangParser.T__4);
							this.state = 296;
							(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
							}
							}
							this.state = 301;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 302;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 308;
				this.match(EclangParser.T__13);
				this.state = 309;
				this.match(EclangParser.INDENT);
				this.state = 310;
				this.expr(0);
				this.state = 316;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 311;
					this.match(EclangParser.T__4);
					this.state = 312;
					this.match(EclangParser.NL);
					this.state = 313;
					this.expr(0);
					}
					}
					this.state = 318;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 319;
				this.match(EclangParser.NL);
				this.state = 320;
				this.match(EclangParser.DEDENT);
				this.state = 321;
				this.match(EclangParser.T__15);
				}
				break;
			case 20:
				{
				localctx = new ExprUpdateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 341;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 325;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 31, this._ctx) ) {
					case 1:
						{
						this.state = 323;
						(localctx as ExprUpdateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 324;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 327;
					(localctx as ExprUpdateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 339;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 328;
						this.match(EclangParser.T__11);
						this.state = 329;
						(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
						(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
						this.state = 334;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 330;
							this.match(EclangParser.T__4);
							this.state = 331;
							(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
							(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
							}
							}
							this.state = 336;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 337;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 343;
				this.match(EclangParser.T__13);
				this.state = 344;
				this.match(EclangParser.T__16);
				this.state = 345;
				(localctx as ExprUpdateRecordContext)._base = this.expr(0);
				this.state = 352;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 346;
					this.match(EclangParser.T__4);
					this.state = 347;
					(localctx as ExprUpdateRecordContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprUpdateRecordContext)._names.push((localctx as ExprUpdateRecordContext)._NAME);
					this.state = 348;
					this.match(EclangParser.T__14);
					this.state = 349;
					(localctx as ExprUpdateRecordContext)._expr = this.expr(0);
					(localctx as ExprUpdateRecordContext)._values.push((localctx as ExprUpdateRecordContext)._expr);
					}
					}
					this.state = 354;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 355;
				this.match(EclangParser.T__15);
				}
				break;
			case 21:
				{
				localctx = new ExprUpdateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 375;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 359;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 36, this._ctx) ) {
					case 1:
						{
						this.state = 357;
						(localctx as ExprUpdateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 358;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 361;
					(localctx as ExprUpdateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 373;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===12) {
						{
						this.state = 362;
						this.match(EclangParser.T__11);
						this.state = 363;
						(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
						(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
						this.state = 368;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 364;
							this.match(EclangParser.T__4);
							this.state = 365;
							(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
							(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
							}
							}
							this.state = 370;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 371;
						this.match(EclangParser.T__12);
						}
					}

					}
				}

				this.state = 377;
				this.match(EclangParser.T__13);
				this.state = 378;
				this.match(EclangParser.INDENT);
				this.state = 379;
				this.match(EclangParser.T__16);
				this.state = 380;
				(localctx as ExprUpdateRecordContext)._base = this.expr(0);
				this.state = 388;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 381;
					this.match(EclangParser.T__4);
					this.state = 382;
					this.match(EclangParser.NL);
					this.state = 383;
					(localctx as ExprUpdateRecordContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprUpdateRecordContext)._names.push((localctx as ExprUpdateRecordContext)._NAME);
					this.state = 384;
					this.match(EclangParser.T__14);
					this.state = 385;
					(localctx as ExprUpdateRecordContext)._expr = this.expr(0);
					(localctx as ExprUpdateRecordContext)._values.push((localctx as ExprUpdateRecordContext)._expr);
					}
					}
					this.state = 390;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 391;
				this.match(EclangParser.NL);
				this.state = 392;
				this.match(EclangParser.DEDENT);
				this.state = 393;
				this.match(EclangParser.T__15);
				}
				break;
			case 22:
				{
				localctx = new ExprNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 395;
				this.match(EclangParser.NAME);
				}
				break;
			case 23:
				{
				localctx = new ExprCreateArrayContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 396;
				this.match(EclangParser.T__17);
				this.state = 397;
				this.expr(0);
				this.state = 402;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 398;
					this.match(EclangParser.T__4);
					this.state = 399;
					this.expr(0);
					}
					}
					this.state = 404;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 405;
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
				localctx = new ExprCreateArrayContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 407;
				this.match(EclangParser.T__17);
				this.state = 408;
				this.match(EclangParser.INDENT);
				this.state = 409;
				this.expr(0);
				this.state = 415;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 410;
					this.match(EclangParser.T__4);
					this.state = 411;
					this.match(EclangParser.NL);
					this.state = 412;
					this.expr(0);
					}
					}
					this.state = 417;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 418;
				this.match(EclangParser.NL);
				this.state = 419;
				this.match(EclangParser.DEDENT);
				this.state = 420;
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
			case 25:
				{
				localctx = new ExprUnOpContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 422;
				(localctx as ExprUnOpContext)._op = this.match(EclangParser.T__28);
				this.state = 423;
				this.expr(30);
				}
				break;
			case 26:
				{
				localctx = new ExprAddressOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 424;
				this.match(EclangParser.T__29);
				this.state = 425;
				this.expr(29);
				}
				break;
			case 27:
				{
				localctx = new ExprTryContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 426;
				this.match(EclangParser.T__30);
				this.state = 427;
				this.expr(28);
				}
				break;
			case 28:
				{
				localctx = new ExprNewContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 428;
				this.match(EclangParser.T__31);
				this.state = 429;
				(localctx as ExprNewContext)._value = this.expr(0);
				this.state = 430;
				this.match(EclangParser.T__32);
				this.state = 431;
				(localctx as ExprNewContext)._pool = this.expr(27);
				}
				break;
			case 29:
				{
				localctx = new ExprNewContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 433;
				this.match(EclangParser.T__31);
				this.state = 434;
				(localctx as ExprNewContext)._value = this.expr(26);
				}
				break;
			case 30:
				{
				localctx = new ExprNewArrayOfTypeContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 435;
				this.match(EclangParser.T__31);
				this.state = 436;
				this.match(EclangParser.T__3);
				this.state = 437;
				(localctx as ExprNewArrayOfTypeContext)._elementType = this.type_(0);
				this.state = 438;
				this.match(EclangParser.T__5);
				this.state = 439;
				this.match(EclangParser.T__17);
				this.state = 440;
				(localctx as ExprNewArrayOfTypeContext)._count = this.expr(0);
				this.state = 441;
				this.match(EclangParser.T__19);
				this.state = 444;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 43, this._ctx) ) {
				case 1:
					{
					this.state = 442;
					this.match(EclangParser.T__32);
					this.state = 443;
					(localctx as ExprNewArrayOfTypeContext)._pool = this.expr(0);
					}
					break;
				}
				}
				break;
			case 31:
				{
				localctx = new ExprUnOpContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 446;
				(localctx as ExprUnOpContext)._op = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(((((_la - 25)) & ~0x1F) === 0 && ((1 << (_la - 25)) & 15361) !== 0))) {
				    (localctx as ExprUnOpContext)._op = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 447;
				this.expr(23);
				}
				break;
			case 32:
				{
				localctx = new ExprInlineIfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 448;
				this.match(EclangParser.T__62);
				this.state = 449;
				(localctx as ExprInlineIfContext)._condition = this.expr(0);
				this.state = 450;
				this.match(EclangParser.T__63);
				this.state = 451;
				(localctx as ExprInlineIfContext)._body = this.expr(0);
				this.state = 452;
				this.match(EclangParser.T__64);
				this.state = 453;
				(localctx as ExprInlineIfContext)._elseBody = this.expr(5);
				}
				break;
			case 33:
				{
				localctx = new ExprInlineIfOldContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 455;
				this.match(EclangParser.T__62);
				this.state = 456;
				this.match(EclangParser.T__3);
				this.state = 457;
				(localctx as ExprInlineIfOldContext)._condition = this.expr(0);
				this.state = 458;
				this.match(EclangParser.T__5);
				this.state = 459;
				(localctx as ExprInlineIfOldContext)._body = this.expr(0);
				this.state = 460;
				this.match(EclangParser.T__64);
				this.state = 461;
				(localctx as ExprInlineIfOldContext)._elseBody = this.expr(4);
				}
				break;
			case 34:
				{
				localctx = new ExprWhenContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 463;
				this.whenExpr();
				}
				break;
			case 35:
				{
				localctx = new ExprLambdaContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 464;
				this.match(EclangParser.T__9);
				this.state = 465;
				this.match(EclangParser.T__3);
				this.state = 474;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 466;
					(localctx as ExprLambdaContext)._lambdaParameter = this.lambdaParameter();
					(localctx as ExprLambdaContext)._params.push((localctx as ExprLambdaContext)._lambdaParameter);
					this.state = 471;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===5) {
						{
						{
						this.state = 467;
						this.match(EclangParser.T__4);
						this.state = 468;
						(localctx as ExprLambdaContext)._lambdaParameter = this.lambdaParameter();
						(localctx as ExprLambdaContext)._params.push((localctx as ExprLambdaContext)._lambdaParameter);
						}
						}
						this.state = 473;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					}
				}

				this.state = 476;
				this.match(EclangParser.T__5);
				this.state = 479;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 477;
					this.match(EclangParser.T__66);
					this.state = 478;
					(localctx as ExprLambdaContext)._returnType = this.type_(0);
					}
				}

				this.state = 481;
				this.match(EclangParser.T__14);
				this.state = 482;
				(localctx as ExprLambdaContext)._body = this.expr(1);
				}
				break;
			}
			this._ctx.stop = this._input.LT(-1);
			this.state = 667;
			this._errHandler.sync(this);
			_alt = this._interp.adaptivePredict(this._input, 55, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					if (this._parseListeners != null) {
						this.triggerExitRuleEvent();
					}
					_prevctx = localctx;
					{
					this.state = 665;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 54, this._ctx) ) {
					case 1:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 485;
						if (!(this.precpred(this._ctx, 24))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 24)");
						}
						this.state = 486;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__33);
						this.state = 487;
						(localctx as ExprBinOpContext)._right = this.expr(24);
						}
						break;
					case 2:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 488;
						if (!(this.precpred(this._ctx, 22))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 22)");
						}
						this.state = 489;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(((((_la - 39)) & ~0x1F) === 0 && ((1 << (_la - 39)) & 7) !== 0))) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 490;
						(localctx as ExprBinOpContext)._right = this.expr(23);
						}
						break;
					case 3:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 491;
						if (!(this.precpred(this._ctx, 21))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 21)");
						}
						this.state = 492;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===38 || _la===42)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 493;
						(localctx as ExprBinOpContext)._right = this.expr(22);
						}
						break;
					case 4:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 494;
						if (!(this.precpred(this._ctx, 20))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 20)");
						}
						this.state = 498;
						this._errHandler.sync(this);
						switch (this._input.LA(1)) {
						case 43:
							{
							this.state = 495;
							(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__42);
							}
							break;
						case 13:
							{
							this.state = 496;
							(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__12);
							this.state = 497;
							(localctx as ExprBinOpContext)._shr = this.match(EclangParser.T__12);
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						this.state = 500;
						(localctx as ExprBinOpContext)._right = this.expr(21);
						}
						break;
					case 5:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 501;
						if (!(this.precpred(this._ctx, 19))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 19)");
						}
						this.state = 502;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__43);
						this.state = 503;
						(localctx as ExprBinOpContext)._right = this.expr(20);
						}
						break;
					case 6:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 504;
						if (!(this.precpred(this._ctx, 18))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 18)");
						}
						this.state = 505;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__44);
						this.state = 506;
						(localctx as ExprBinOpContext)._right = this.expr(19);
						}
						break;
					case 7:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 507;
						if (!(this.precpred(this._ctx, 17))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 17)");
						}
						this.state = 508;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__45);
						this.state = 509;
						(localctx as ExprBinOpContext)._right = this.expr(18);
						}
						break;
					case 8:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 510;
						if (!(this.precpred(this._ctx, 16))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 16)");
						}
						this.state = 511;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(((((_la - 23)) & ~0x1F) === 0 && ((1 << (_la - 23)) & 16777219) !== 0))) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 512;
						(localctx as ExprBinOpContext)._right = this.expr(17);
						}
						break;
					case 9:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 513;
						if (!(this.precpred(this._ctx, 15))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 15)");
						}
						this.state = 514;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===12 || _la===13 || _la===48 || _la===49)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 515;
						(localctx as ExprBinOpContext)._right = this.expr(16);
						}
						break;
					case 10:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 516;
						if (!(this.precpred(this._ctx, 11))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 11)");
						}
						this.state = 517;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(((((_la - 52)) & ~0x1F) === 0 && ((1 << (_la - 52)) & 15) !== 0))) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 518;
						(localctx as ExprBinOpContext)._right = this.expr(12);
						}
						break;
					case 11:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 519;
						if (!(this.precpred(this._ctx, 10))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 10)");
						}
						this.state = 520;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===56 || _la===57)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 521;
						(localctx as ExprBinOpContext)._right = this.expr(11);
						}
						break;
					case 12:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 522;
						if (!(this.precpred(this._ctx, 9))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 9)");
						}
						this.state = 523;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===58 || _la===59)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 524;
						(localctx as ExprBinOpContext)._right = this.expr(10);
						}
						break;
					case 13:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 525;
						if (!(this.precpred(this._ctx, 8))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 8)");
						}
						this.state = 526;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===60 || _la===61)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 527;
						(localctx as ExprBinOpContext)._right = this.expr(9);
						}
						break;
					case 14:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 528;
						if (!(this.precpred(this._ctx, 7))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 7)");
						}
						this.state = 529;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__61);
						this.state = 530;
						(localctx as ExprBinOpContext)._right = this.expr(7);
						}
						break;
					case 15:
						{
						localctx = new ExprPipeContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprPipeContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 531;
						if (!(this.precpred(this._ctx, 3))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 3)");
						}
						this.state = 532;
						this.match(EclangParser.T__65);
						this.state = 533;
						(localctx as ExprPipeContext)._right = this.expr(4);
						}
						break;
					case 16:
						{
						localctx = new ExprFunctionCallNoArgsContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprFunctionCallNoArgsContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 534;
						if (!(this.precpred(this._ctx, 46))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 46)");
						}
						this.state = 535;
						this.match(EclangParser.T__10);
						this.state = 536;
						this.match(EclangParser.NAME);
						this.state = 537;
						this.match(EclangParser.T__3);
						this.state = 538;
						this.match(EclangParser.T__5);
						}
						break;
					case 17:
						{
						localctx = new ExprFunctionCallWithArgsContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprFunctionCallWithArgsContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 539;
						if (!(this.precpred(this._ctx, 45))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 45)");
						}
						this.state = 540;
						this.match(EclangParser.T__10);
						this.state = 541;
						this.match(EclangParser.NAME);
						this.state = 542;
						this.match(EclangParser.T__3);
						this.state = 543;
						(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
						(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
						this.state = 548;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 544;
							this.match(EclangParser.T__4);
							this.state = 545;
							(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
							(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
							}
							}
							this.state = 550;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 551;
						this.match(EclangParser.T__5);
						}
						break;
					case 18:
						{
						localctx = new ExprFunctionCallWithArgsContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprFunctionCallWithArgsContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 553;
						if (!(this.precpred(this._ctx, 44))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 44)");
						}
						this.state = 554;
						this.match(EclangParser.T__10);
						this.state = 555;
						this.match(EclangParser.NAME);
						this.state = 556;
						this.match(EclangParser.T__3);
						this.state = 557;
						this.match(EclangParser.INDENT);
						this.state = 558;
						(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
						(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
						this.state = 564;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 559;
							this.match(EclangParser.T__4);
							this.state = 560;
							this.match(EclangParser.NL);
							this.state = 561;
							(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
							(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
							}
							}
							this.state = 566;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 567;
						this.match(EclangParser.NL);
						this.state = 568;
						this.match(EclangParser.DEDENT);
						this.state = 569;
						this.match(EclangParser.T__5);
						}
						break;
					case 19:
						{
						localctx = new ExprSafeCallContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSafeCallContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 571;
						if (!(this.precpred(this._ctx, 43))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 43)");
						}
						this.state = 572;
						this.match(EclangParser.T__20);
						this.state = 573;
						this.match(EclangParser.NAME);
						this.state = 574;
						this.match(EclangParser.T__3);
						this.state = 575;
						this.match(EclangParser.T__5);
						}
						break;
					case 20:
						{
						localctx = new ExprSafeCallContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSafeCallContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 576;
						if (!(this.precpred(this._ctx, 42))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 42)");
						}
						this.state = 577;
						this.match(EclangParser.T__20);
						this.state = 578;
						this.match(EclangParser.NAME);
						this.state = 579;
						this.match(EclangParser.T__3);
						this.state = 580;
						(localctx as ExprSafeCallContext)._argument = this.argument();
						(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
						this.state = 585;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 581;
							this.match(EclangParser.T__4);
							this.state = 582;
							(localctx as ExprSafeCallContext)._argument = this.argument();
							(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
							}
							}
							this.state = 587;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 588;
						this.match(EclangParser.T__5);
						}
						break;
					case 21:
						{
						localctx = new ExprSafeCallContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSafeCallContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 590;
						if (!(this.precpred(this._ctx, 41))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 41)");
						}
						this.state = 591;
						this.match(EclangParser.T__20);
						this.state = 592;
						this.match(EclangParser.NAME);
						this.state = 593;
						this.match(EclangParser.T__3);
						this.state = 594;
						this.match(EclangParser.INDENT);
						this.state = 595;
						(localctx as ExprSafeCallContext)._argument = this.argument();
						(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
						this.state = 601;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===5) {
							{
							{
							this.state = 596;
							this.match(EclangParser.T__4);
							this.state = 597;
							this.match(EclangParser.NL);
							this.state = 598;
							(localctx as ExprSafeCallContext)._argument = this.argument();
							(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
							}
							}
							this.state = 603;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 604;
						this.match(EclangParser.NL);
						this.state = 605;
						this.match(EclangParser.DEDENT);
						this.state = 606;
						this.match(EclangParser.T__5);
						}
						break;
					case 22:
						{
						localctx = new ExprAccessNameContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessNameContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 608;
						if (!(this.precpred(this._ctx, 40))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 40)");
						}
						this.state = 609;
						this.match(EclangParser.T__10);
						this.state = 610;
						this.match(EclangParser.NAME);
						}
						break;
					case 23:
						{
						localctx = new ExprAccessNameNullishCoalescingContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessNameNullishCoalescingContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 611;
						if (!(this.precpred(this._ctx, 39))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 39)");
						}
						this.state = 612;
						this.match(EclangParser.T__20);
						this.state = 613;
						this.match(EclangParser.NAME);
						}
						break;
					case 24:
						{
						localctx = new ExprAccessorContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessorContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 614;
						if (!(this.precpred(this._ctx, 38))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 38)");
						}
						this.state = 615;
						this.match(EclangParser.T__17);
						this.state = 616;
						(localctx as ExprAccessorContext)._index = this.expr(0);
						this.state = 617;
						this.match(EclangParser.T__19);
						}
						break;
					case 25:
						{
						localctx = new ExprAccessorContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessorContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 619;
						if (!(this.precpred(this._ctx, 37))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 37)");
						}
						this.state = 620;
						this.match(EclangParser.T__17);
						this.state = 621;
						this.match(EclangParser.INDENT);
						this.state = 622;
						(localctx as ExprAccessorContext)._index = this.expr(0);
						this.state = 623;
						this.match(EclangParser.NL);
						this.state = 624;
						this.match(EclangParser.DEDENT);
						this.state = 625;
						this.match(EclangParser.T__19);
						}
						break;
					case 26:
						{
						localctx = new ExprSliceOpenContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSliceOpenContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 627;
						if (!(this.precpred(this._ctx, 36))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 36)");
						}
						this.state = 628;
						this.match(EclangParser.T__17);
						this.state = 630;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						if ((((_la) & ~0x1F) === 0 && ((1 << _la) & 3791931294) !== 0) || ((((_la - 32)) & ~0x1F) === 0 && ((1 << (_la - 32)) & 2147483769) !== 0) || ((((_la - 98)) & ~0x1F) === 0 && ((1 << (_la - 98)) & 267911169) !== 0)) {
							{
							this.state = 629;
							(localctx as ExprSliceOpenContext)._from_ = this.expr(0);
							}
						}

						this.state = 632;
						this.match(EclangParser.T__21);
						this.state = 633;
						this.match(EclangParser.T__19);
						}
						break;
					case 27:
						{
						localctx = new ExprSliceOpenContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSliceOpenContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 634;
						if (!(this.precpred(this._ctx, 35))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 35)");
						}
						this.state = 635;
						this.match(EclangParser.T__17);
						this.state = 636;
						(localctx as ExprSliceOpenContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===23 || _la===24)) {
						    (localctx as ExprSliceOpenContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 637;
						(localctx as ExprSliceOpenContext)._to = this.expr(0);
						this.state = 638;
						this.match(EclangParser.T__19);
						}
						break;
					case 28:
						{
						localctx = new ExprCastNotNullContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 640;
						if (!(this.precpred(this._ctx, 34))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 34)");
						}
						this.state = 641;
						this.match(EclangParser.T__24);
						}
						break;
					case 29:
						{
						localctx = new ExprAsContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 642;
						if (!(this.precpred(this._ctx, 33))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 33)");
						}
						this.state = 643;
						this.match(EclangParser.T__25);
						this.state = 644;
						this.type_(0);
						}
						break;
					case 30:
						{
						localctx = new ExprAsUnsafeContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 645;
						if (!(this.precpred(this._ctx, 32))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 32)");
						}
						this.state = 646;
						this.match(EclangParser.T__25);
						this.state = 647;
						this.match(EclangParser.T__26);
						this.state = 648;
						this.type_(0);
						}
						break;
					case 31:
						{
						localctx = new ExprAsUnsafeContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 649;
						if (!(this.precpred(this._ctx, 31))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 31)");
						}
						this.state = 650;
						this.match(EclangParser.T__27);
						this.state = 651;
						this.type_(0);
						}
						break;
					case 32:
						{
						localctx = new ExprIsContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 652;
						if (!(this.precpred(this._ctx, 14))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 14)");
						}
						this.state = 653;
						this.match(EclangParser.T__49);
						this.state = 654;
						this.type_(0);
						}
						break;
					case 33:
						{
						localctx = new ExprIsNotContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 655;
						if (!(this.precpred(this._ctx, 13))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 13)");
						}
						this.state = 656;
						this.match(EclangParser.T__50);
						this.state = 657;
						this.type_(0);
						}
						break;
					case 34:
						{
						localctx = new ExprIsNotContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 658;
						if (!(this.precpred(this._ctx, 12))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 12)");
						}
						this.state = 659;
						this.match(EclangParser.T__49);
						this.state = 660;
						this.match(EclangParser.T__34);
						this.state = 661;
						this.type_(0);
						}
						break;
					case 35:
						{
						localctx = new ExprNullishJumpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprNullishJumpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 662;
						if (!(this.precpred(this._ctx, 6))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 6)");
						}
						this.state = 663;
						this.match(EclangParser.T__61);
						this.state = 664;
						this.jump();
						}
						break;
					}
					}
				}
				this.state = 669;
				this._errHandler.sync(this);
				_alt = this._interp.adaptivePredict(this._input, 55, this._ctx);
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
			this.state = 672;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 56, this._ctx) ) {
			case 1:
				{
				this.state = 670;
				localctx._name = this.match(EclangParser.NAME);
				this.state = 671;
				this.match(EclangParser.T__14);
				}
				break;
			}
			this.state = 674;
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
			this.state = 690;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 68:
				localctx = new JumpReturnContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 676;
				this.match(EclangParser.T__67);
				this.state = 678;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 57, this._ctx) ) {
				case 1:
					{
					this.state = 677;
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
				this.state = 680;
				this.match(EclangParser.T__68);
				this.state = 681;
				(localctx as JumpThrowContext)._value = this.expr(0);
				}
				break;
			case 70:
				localctx = new JumpBreakContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 682;
				this.match(EclangParser.T__69);
				this.state = 684;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 58, this._ctx) ) {
				case 1:
					{
					this.state = 683;
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
				this.state = 686;
				this.match(EclangParser.T__70);
				this.state = 688;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 59, this._ctx) ) {
				case 1:
					{
					this.state = 687;
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
			this.state = 696;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 61, this._ctx) ) {
			case 1:
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 692;
				this.expr(0);
				this.state = 693;
				this.match(EclangParser.NL);
				}
				break;
			case 2:
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 695;
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
			this.state = 698;
			this.match(EclangParser.NAME);
			this.state = 701;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===67) {
				{
				this.state = 699;
				this.match(EclangParser.T__66);
				this.state = 700;
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
			this.state = 812;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 75, this._ctx) ) {
			case 1:
				{
				localctx = new TypeByNameParametersContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;

				this.state = 706;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 63, this._ctx) ) {
				case 1:
					{
					this.state = 704;
					(localctx as TypeByNameParametersContext)._qualifier = this.match(EclangParser.NAME);
					this.state = 705;
					this.match(EclangParser.T__10);
					}
					break;
				}
				this.state = 708;
				(localctx as TypeByNameParametersContext)._name = this.match(EclangParser.NAME);
				this.state = 709;
				this.match(EclangParser.T__11);
				this.state = 710;
				(localctx as TypeByNameParametersContext)._type_ = this.type_(0);
				(localctx as TypeByNameParametersContext)._args.push((localctx as TypeByNameParametersContext)._type_);
				this.state = 715;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 711;
					this.match(EclangParser.T__4);
					this.state = 712;
					(localctx as TypeByNameParametersContext)._type_ = this.type_(0);
					(localctx as TypeByNameParametersContext)._args.push((localctx as TypeByNameParametersContext)._type_);
					}
					}
					this.state = 717;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 718;
				this.match(EclangParser.T__12);
				}
				break;
			case 2:
				{
				localctx = new TypeByNameContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 720;
				this.match(EclangParser.NAME);
				this.state = 721;
				this.match(EclangParser.T__10);
				this.state = 722;
				this.match(EclangParser.NAME);
				}
				break;
			case 3:
				{
				localctx = new TypeByNameContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 723;
				this.match(EclangParser.NAME);
				}
				break;
			case 4:
				{
				localctx = new TypeAddressByNameContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 724;
				this.match(EclangParser.T__28);
				this.state = 725;
				this.type_(17);
				}
				break;
			case 5:
				{
				localctx = new TypeUnionAnonContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 726;
				this.match(EclangParser.T__72);
				this.state = 727;
				(localctx as TypeUnionAnonContext)._type_ = this.type_(0);
				(localctx as TypeUnionAnonContext)._types.push((localctx as TypeUnionAnonContext)._type_);
				this.state = 728;
				this.match(EclangParser.T__45);
				this.state = 729;
				(localctx as TypeUnionAnonContext)._type_ = this.type_(0);
				(localctx as TypeUnionAnonContext)._types.push((localctx as TypeUnionAnonContext)._type_);
				this.state = 734;
				this._errHandler.sync(this);
				_alt = this._interp.adaptivePredict(this._input, 65, this._ctx);
				while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
					if (_alt === 1) {
						{
						{
						this.state = 730;
						this.match(EclangParser.T__45);
						this.state = 731;
						(localctx as TypeUnionAnonContext)._type_ = this.type_(0);
						(localctx as TypeUnionAnonContext)._types.push((localctx as TypeUnionAnonContext)._type_);
						}
						}
					}
					this.state = 736;
					this._errHandler.sync(this);
					_alt = this._interp.adaptivePredict(this._input, 65, this._ctx);
				}
				}
				break;
			case 6:
				{
				localctx = new TypeNestContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 737;
				this.match(EclangParser.T__3);
				this.state = 738;
				this.type_(0);
				this.state = 739;
				this.match(EclangParser.T__5);
				}
				break;
			case 7:
				{
				localctx = new TypeAtomContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 741;
				this.match(EclangParser.ATOM);
				}
				break;
			case 8:
				{
				localctx = new TypeGenericContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 742;
				this.match(EclangParser.T__11);
				this.state = 743;
				this.match(EclangParser.NAME);
				this.state = 744;
				this.match(EclangParser.T__12);
				}
				break;
			case 9:
				{
				localctx = new TypeTypeOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 745;
				this.match(EclangParser.T__73);
				this.state = 746;
				this.expr(0);
				}
				break;
			case 10:
				{
				localctx = new TypeCallSignatureContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 747;
				this.match(EclangParser.T__9);
				this.state = 748;
				this.match(EclangParser.T__3);
				this.state = 749;
				this.match(EclangParser.T__5);
				this.state = 752;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 66, this._ctx) ) {
				case 1:
					{
					this.state = 750;
					_la = this._input.LA(1);
					if(!(_la===67 || _la===76)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 751;
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
				this.state = 754;
				this.match(EclangParser.T__9);
				this.state = 755;
				this.match(EclangParser.T__3);
				this.state = 758;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 67, this._ctx) ) {
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
					switch ( this._interp.adaptivePredict(this._input, 68, this._ctx) ) {
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
				this.state = 775;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 70, this._ctx) ) {
				case 1:
					{
					this.state = 773;
					_la = this._input.LA(1);
					if(!(_la===67 || _la===76)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 774;
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
				this.state = 777;
				this.match(EclangParser.T__3);
				this.state = 778;
				this.match(EclangParser.T__5);
				this.state = 779;
				this.match(EclangParser.T__75);
				this.state = 780;
				(localctx as TypeCallSignatureContext)._returnType = this.type_(3);
				}
				break;
			case 13:
				{
				localctx = new TypeCallSignatureContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 781;
				this.match(EclangParser.T__3);
				this.state = 784;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 71, this._ctx) ) {
				case 1:
					{
					this.state = 782;
					this.match(EclangParser.NAME);
					this.state = 783;
					this.match(EclangParser.T__66);
					}
					break;
				}
				this.state = 786;
				(localctx as TypeCallSignatureContext)._type_ = this.type_(0);
				(localctx as TypeCallSignatureContext)._argTypes.push((localctx as TypeCallSignatureContext)._type_);
				this.state = 795;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 787;
					this.match(EclangParser.T__4);
					this.state = 790;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 72, this._ctx) ) {
					case 1:
						{
						this.state = 788;
						this.match(EclangParser.NAME);
						this.state = 789;
						this.match(EclangParser.T__66);
						}
						break;
					}
					this.state = 792;
					(localctx as TypeCallSignatureContext)._type_ = this.type_(0);
					(localctx as TypeCallSignatureContext)._argTypes.push((localctx as TypeCallSignatureContext)._type_);
					}
					}
					this.state = 797;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 798;
				this.match(EclangParser.T__5);
				this.state = 799;
				this.match(EclangParser.T__75);
				this.state = 800;
				(localctx as TypeCallSignatureContext)._returnType = this.type_(2);
				}
				break;
			case 14:
				{
				localctx = new TypeTupleContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 802;
				this.match(EclangParser.T__3);
				this.state = 803;
				(localctx as TypeTupleContext)._type_ = this.type_(0);
				(localctx as TypeTupleContext)._types.push((localctx as TypeTupleContext)._type_);
				this.state = 806;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 804;
					this.match(EclangParser.T__4);
					this.state = 805;
					(localctx as TypeTupleContext)._type_ = this.type_(0);
					(localctx as TypeTupleContext)._types.push((localctx as TypeTupleContext)._type_);
					}
					}
					this.state = 808;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===5);
				this.state = 810;
				this.match(EclangParser.T__5);
				}
				break;
			}
			this._ctx.stop = this._input.LT(-1);
			this.state = 849;
			this._errHandler.sync(this);
			_alt = this._interp.adaptivePredict(this._input, 79, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					if (this._parseListeners != null) {
						this.triggerExitRuleEvent();
					}
					_prevctx = localctx;
					{
					this.state = 847;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 78, this._ctx) ) {
					case 1:
						{
						localctx = new TypeThrowsContext(this, new TypeContext(this, _parentctx, _parentState));
						(localctx as TypeThrowsContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 814;
						if (!(this.precpred(this._ctx, 6))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 6)");
						}
						this.state = 815;
						this.match(EclangParser.T__74);
						this.state = 816;
						(localctx as TypeThrowsContext)._throwing = this.type_(7);
						}
						break;
					case 2:
						{
						localctx = new TypeNullableContext(this, new TypeContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 817;
						if (!(this.precpred(this._ctx, 16))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 16)");
						}
						this.state = 818;
						this.match(EclangParser.T__71);
						}
						break;
					case 3:
						{
						localctx = new TypeEnumContext(this, new TypeContext(this, _parentctx, _parentState));
						(localctx as TypeEnumContext)._types.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 819;
						if (!(this.precpred(this._ctx, 15))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 15)");
						}
						this.state = 820;
						this.match(EclangParser.T__43);
						this.state = 821;
						(localctx as TypeEnumContext)._type_ = this.type_(0);
						(localctx as TypeEnumContext)._types.push((localctx as TypeEnumContext)._type_);
						this.state = 826;
						this._errHandler.sync(this);
						_alt = this._interp.adaptivePredict(this._input, 76, this._ctx);
						while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
							if (_alt === 1) {
								{
								{
								this.state = 822;
								this.match(EclangParser.T__43);
								this.state = 823;
								(localctx as TypeEnumContext)._type_ = this.type_(0);
								(localctx as TypeEnumContext)._types.push((localctx as TypeEnumContext)._type_);
								}
								}
							}
							this.state = 828;
							this._errHandler.sync(this);
							_alt = this._interp.adaptivePredict(this._input, 76, this._ctx);
						}
						}
						break;
					case 4:
						{
						localctx = new TypeUnionContext(this, new TypeContext(this, _parentctx, _parentState));
						(localctx as TypeUnionContext)._types.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 829;
						if (!(this.precpred(this._ctx, 14))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 14)");
						}
						this.state = 830;
						this.match(EclangParser.T__45);
						this.state = 831;
						(localctx as TypeUnionContext)._type_ = this.type_(0);
						(localctx as TypeUnionContext)._types.push((localctx as TypeUnionContext)._type_);
						this.state = 836;
						this._errHandler.sync(this);
						_alt = this._interp.adaptivePredict(this._input, 77, this._ctx);
						while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
							if (_alt === 1) {
								{
								{
								this.state = 832;
								this.match(EclangParser.T__45);
								this.state = 833;
								(localctx as TypeUnionContext)._type_ = this.type_(0);
								(localctx as TypeUnionContext)._types.push((localctx as TypeUnionContext)._type_);
								}
								}
							}
							this.state = 838;
							this._errHandler.sync(this);
							_alt = this._interp.adaptivePredict(this._input, 77, this._ctx);
						}
						}
						break;
					case 5:
						{
						localctx = new TypeArrayContext(this, new TypeContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 839;
						if (!(this.precpred(this._ctx, 8))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 8)");
						}
						this.state = 840;
						this.match(EclangParser.T__17);
						this.state = 841;
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
					case 6:
						{
						localctx = new TypeFixedArrayContext(this, new TypeContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 842;
						if (!(this.precpred(this._ctx, 7))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 7)");
						}
						this.state = 843;
						this.match(EclangParser.T__17);
						this.state = 844;
						(localctx as TypeFixedArrayContext)._size = this.expr(0);
						this.state = 845;
						this.match(EclangParser.T__19);
						}
						break;
					}
					}
				}
				this.state = 851;
				this._errHandler.sync(this);
				_alt = this._interp.adaptivePredict(this._input, 79, this._ctx);
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
			this.state = 852;
			this.match(EclangParser.T__76);
			this.state = 853;
			localctx._name = this.match(EclangParser.NAME);
			this.state = 854;
			this.match(EclangParser.T__25);
			this.state = 855;
			this.match(EclangParser.INDENT);
			this.state = 861;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 856;
				localctx._NAME = this.match(EclangParser.NAME);
				localctx._names.push(localctx._NAME);
				this.state = 857;
				this.match(EclangParser.T__66);
				this.state = 858;
				localctx._type_ = this.type_(0);
				localctx._types.push(localctx._type_);
				this.state = 859;
				this.match(EclangParser.NL);
				}
				}
				this.state = 863;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while (_la===118);
			this.state = 865;
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
			this.state = 890;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 81, this._ctx) ) {
			case 1:
				localctx = new ParameterTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 867;
				this.match(EclangParser.NAME);
				this.state = 868;
				this.match(EclangParser.T__66);
				this.state = 869;
				this.type_(0);
				}
				break;
			case 2:
				localctx = new ParameterTypedDefaultContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 870;
				this.match(EclangParser.NAME);
				this.state = 871;
				this.match(EclangParser.T__66);
				this.state = 872;
				this.type_(0);
				this.state = 873;
				this.match(EclangParser.T__14);
				this.state = 874;
				this.expr(0);
				}
				break;
			case 3:
				localctx = new ParameterDefaultContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 876;
				this.match(EclangParser.NAME);
				this.state = 877;
				this.match(EclangParser.T__14);
				this.state = 878;
				this.expr(0);
				}
				break;
			case 4:
				localctx = new ParameterVarArgContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 879;
				this.match(EclangParser.T__16);
				this.state = 880;
				this.match(EclangParser.NAME);
				this.state = 881;
				this.match(EclangParser.T__66);
				this.state = 882;
				this.type_(0);
				}
				break;
			case 5:
				localctx = new ParameterVarArgCContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 883;
				this.match(EclangParser.T__16);
				}
				break;
			case 6:
				localctx = new ParameterVariableContext(this, localctx);
				this.enterOuterAlt(localctx, 6);
				{
				this.state = 884;
				this.match(EclangParser.T__77);
				this.state = 885;
				this.parameter();
				}
				break;
			case 7:
				localctx = new ParameterConstContext(this, localctx);
				this.enterOuterAlt(localctx, 7);
				{
				this.state = 886;
				this.match(EclangParser.T__78);
				this.state = 887;
				this.parameter();
				}
				break;
			case 8:
				localctx = new ParameterVolatileContext(this, localctx);
				this.enterOuterAlt(localctx, 8);
				{
				this.state = 888;
				this.match(EclangParser.T__79);
				this.state = 889;
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
			this.state = 1283;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 118, this._ctx) ) {
			case 1:
				localctx = new StatementDefineVarContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 892;
				this.match(EclangParser.T__77);
				this.state = 893;
				this.match(EclangParser.NAME);
				this.state = 894;
				this.match(EclangParser.T__14);
				this.state = 895;
				this.tail();
				}
				break;
			case 2:
				localctx = new StatementDefineValContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 896;
				this.match(EclangParser.T__78);
				this.state = 897;
				this.match(EclangParser.NAME);
				this.state = 898;
				this.match(EclangParser.T__14);
				this.state = 899;
				this.tail();
				}
				break;
			case 3:
				localctx = new StatementDefineVolContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 900;
				this.match(EclangParser.T__79);
				this.state = 901;
				this.match(EclangParser.NAME);
				this.state = 902;
				this.match(EclangParser.T__14);
				this.state = 903;
				this.tail();
				}
				break;
			case 4:
				localctx = new StatementDefineConstContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 904;
				this.match(EclangParser.T__80);
				this.state = 905;
				this.match(EclangParser.NAME);
				this.state = 906;
				this.match(EclangParser.T__14);
				this.state = 907;
				this.expr(0);
				this.state = 908;
				this.match(EclangParser.NL);
				}
				break;
			case 5:
				localctx = new StatementDefineVarTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 910;
				this.match(EclangParser.T__77);
				this.state = 911;
				this.match(EclangParser.NAME);
				this.state = 912;
				this.match(EclangParser.T__66);
				this.state = 913;
				this.type_(0);
				this.state = 914;
				this.match(EclangParser.T__14);
				this.state = 915;
				this.tail();
				}
				break;
			case 6:
				localctx = new StatementDefineValTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 6);
				{
				this.state = 917;
				this.match(EclangParser.T__78);
				this.state = 918;
				this.match(EclangParser.NAME);
				this.state = 919;
				this.match(EclangParser.T__66);
				this.state = 920;
				this.type_(0);
				this.state = 921;
				this.match(EclangParser.T__14);
				this.state = 922;
				this.tail();
				}
				break;
			case 7:
				localctx = new StatementDefineVolTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 7);
				{
				this.state = 924;
				this.match(EclangParser.T__79);
				this.state = 925;
				this.match(EclangParser.NAME);
				this.state = 926;
				this.match(EclangParser.T__66);
				this.state = 927;
				this.type_(0);
				this.state = 928;
				this.match(EclangParser.T__14);
				this.state = 929;
				this.tail();
				}
				break;
			case 8:
				localctx = new StatementDefineConstTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 8);
				{
				this.state = 931;
				this.match(EclangParser.T__80);
				this.state = 932;
				this.match(EclangParser.NAME);
				this.state = 933;
				this.match(EclangParser.T__66);
				this.state = 934;
				this.type_(0);
				this.state = 935;
				this.match(EclangParser.T__14);
				this.state = 936;
				this.expr(0);
				this.state = 937;
				this.match(EclangParser.NL);
				}
				break;
			case 9:
				localctx = new StatementDeclareVarContext(this, localctx);
				this.enterOuterAlt(localctx, 9);
				{
				this.state = 939;
				this.match(EclangParser.T__77);
				this.state = 940;
				this.match(EclangParser.NAME);
				this.state = 941;
				this.match(EclangParser.T__66);
				this.state = 942;
				this.type_(0);
				this.state = 943;
				this.match(EclangParser.NL);
				}
				break;
			case 10:
				localctx = new StatementDestructureContext(this, localctx);
				this.enterOuterAlt(localctx, 10);
				{
				this.state = 945;
				(localctx as StatementDestructureContext)._kind = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===78 || _la===79)) {
				    (localctx as StatementDestructureContext)._kind = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 946;
				this.destructure();
				this.state = 949;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 947;
					this.match(EclangParser.T__66);
					this.state = 948;
					this.type_(0);
					}
				}

				this.state = 951;
				this.match(EclangParser.T__14);
				this.state = 952;
				this.tail();
				}
				break;
			case 11:
				localctx = new StatementAssignmentContext(this, localctx);
				this.enterOuterAlt(localctx, 11);
				{
				this.state = 954;
				(localctx as StatementAssignmentContext)._target = this.expr(0);
				this.state = 955;
				(localctx as StatementAssignmentContext)._op = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===15 || ((((_la - 82)) & ~0x1F) === 0 && ((1 << (_la - 82)) & 1023) !== 0))) {
				    (localctx as StatementAssignmentContext)._op = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 956;
				(localctx as StatementAssignmentContext)._value = this.tail();
				}
				break;
			case 12:
				localctx = new StatementBreakContext(this, localctx);
				this.enterOuterAlt(localctx, 12);
				{
				this.state = 958;
				this.match(EclangParser.T__69);
				this.state = 960;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 959;
					(localctx as StatementBreakContext)._label = this.match(EclangParser.NAME);
					}
				}

				this.state = 964;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 962;
					this.match(EclangParser.T__62);
					this.state = 963;
					(localctx as StatementBreakContext)._condition = this.expr(0);
					}
				}

				this.state = 966;
				this.match(EclangParser.NL);
				}
				break;
			case 13:
				localctx = new StatementContinueContext(this, localctx);
				this.enterOuterAlt(localctx, 13);
				{
				this.state = 967;
				this.match(EclangParser.T__70);
				this.state = 969;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 968;
					(localctx as StatementContinueContext)._label = this.match(EclangParser.NAME);
					}
				}

				this.state = 973;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 971;
					this.match(EclangParser.T__62);
					this.state = 972;
					(localctx as StatementContinueContext)._condition = this.expr(0);
					}
				}

				this.state = 975;
				this.match(EclangParser.NL);
				}
				break;
			case 14:
				localctx = new StatementPassContext(this, localctx);
				this.enterOuterAlt(localctx, 14);
				{
				this.state = 976;
				this.match(EclangParser.T__91);
				this.state = 979;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 977;
					this.match(EclangParser.T__62);
					this.state = 978;
					(localctx as StatementPassContext)._condition = this.expr(0);
					}
				}

				this.state = 981;
				this.match(EclangParser.NL);
				}
				break;
			case 15:
				localctx = new StatementYieldContext(this, localctx);
				this.enterOuterAlt(localctx, 15);
				{
				this.state = 982;
				this.match(EclangParser.T__92);
				this.state = 985;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 983;
					this.match(EclangParser.T__62);
					this.state = 984;
					(localctx as StatementYieldContext)._condition = this.expr(0);
					}
				}

				this.state = 987;
				this.match(EclangParser.NL);
				}
				break;
			case 16:
				localctx = new StatementThrowContext(this, localctx);
				this.enterOuterAlt(localctx, 16);
				{
				this.state = 988;
				this.match(EclangParser.T__68);
				this.state = 989;
				(localctx as StatementThrowContext)._throw_ = this.expr(0);
				this.state = 992;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 990;
					this.match(EclangParser.T__62);
					this.state = 991;
					(localctx as StatementThrowContext)._condition = this.expr(0);
					}
				}

				this.state = 994;
				this.match(EclangParser.NL);
				}
				break;
			case 17:
				localctx = new StatementAssertContext(this, localctx);
				this.enterOuterAlt(localctx, 17);
				{
				this.state = 996;
				this.match(EclangParser.T__93);
				this.state = 997;
				(localctx as StatementAssertContext)._condition = this.expr(0);
				this.state = 1000;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===5) {
					{
					this.state = 998;
					this.match(EclangParser.T__4);
					this.state = 999;
					(localctx as StatementAssertContext)._message = this.match(EclangParser.STRING);
					}
				}

				this.state = 1002;
				this.match(EclangParser.NL);
				}
				break;
			case 18:
				localctx = new StatementReturnContext(this, localctx);
				this.enterOuterAlt(localctx, 18);
				{
				this.state = 1004;
				this.match(EclangParser.T__67);
				this.state = 1006;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 91, this._ctx) ) {
				case 1:
					{
					this.state = 1005;
					(localctx as StatementReturnContext)._return_ = this.expr(0);
					}
					break;
				}
				this.state = 1010;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 1008;
					this.match(EclangParser.T__62);
					this.state = 1009;
					(localctx as StatementReturnContext)._condition = this.expr(0);
					}
				}

				this.state = 1012;
				this.match(EclangParser.NL);
				}
				break;
			case 19:
				localctx = new StatementReturnContext(this, localctx);
				this.enterOuterAlt(localctx, 19);
				{
				this.state = 1013;
				this.match(EclangParser.T__67);
				this.state = 1014;
				this.whenExpr();
				}
				break;
			case 20:
				localctx = new StatementTailIfContext(this, localctx);
				this.enterOuterAlt(localctx, 20);
				{
				this.state = 1015;
				(localctx as StatementTailIfContext)._expression = this.expr(0);
				this.state = 1016;
				this.match(EclangParser.T__62);
				this.state = 1017;
				(localctx as StatementTailIfContext)._condition = this.expr(0);
				this.state = 1018;
				this.match(EclangParser.NL);
				}
				break;
			case 21:
				localctx = new StatementTailWhileContext(this, localctx);
				this.enterOuterAlt(localctx, 21);
				{
				this.state = 1020;
				(localctx as StatementTailWhileContext)._expression = this.expr(0);
				this.state = 1021;
				this.match(EclangParser.T__94);
				this.state = 1022;
				(localctx as StatementTailWhileContext)._condition = this.expr(0);
				this.state = 1023;
				this.match(EclangParser.NL);
				}
				break;
			case 22:
				localctx = new StatementDeferContext(this, localctx);
				this.enterOuterAlt(localctx, 22);
				{
				this.state = 1025;
				this.match(EclangParser.T__95);
				this.state = 1026;
				this.statement();
				}
				break;
			case 23:
				localctx = new StatementDeferDoContext(this, localctx);
				this.enterOuterAlt(localctx, 23);
				{
				this.state = 1027;
				this.match(EclangParser.T__95);
				this.state = 1028;
				this.match(EclangParser.T__63);
				this.state = 1029;
				(localctx as StatementDeferDoContext)._body = this.block();
				}
				break;
			case 24:
				localctx = new StatementErrDeferContext(this, localctx);
				this.enterOuterAlt(localctx, 24);
				{
				this.state = 1030;
				this.match(EclangParser.T__96);
				this.state = 1031;
				this.statement();
				}
				break;
			case 25:
				localctx = new StatementErrDeferDoContext(this, localctx);
				this.enterOuterAlt(localctx, 25);
				{
				this.state = 1032;
				this.match(EclangParser.T__96);
				this.state = 1033;
				this.match(EclangParser.T__63);
				this.state = 1034;
				(localctx as StatementErrDeferDoContext)._body = this.block();
				}
				break;
			case 26:
				localctx = new StatementWhenContext(this, localctx);
				this.enterOuterAlt(localctx, 26);
				{
				this.state = 1035;
				this.match(EclangParser.T__97);
				this.state = 1039;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===78 || _la===79) {
					{
					this.state = 1036;
					(localctx as StatementWhenContext)._kind = this._input.LT(1);
					_la = this._input.LA(1);
					if(!(_la===78 || _la===79)) {
					    (localctx as StatementWhenContext)._kind = this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 1037;
					this.match(EclangParser.NAME);
					this.state = 1038;
					this.match(EclangParser.T__14);
					}
				}

				this.state = 1041;
				(localctx as StatementWhenContext)._subject = this.expr(0);
				this.state = 1042;
				this.match(EclangParser.T__63);
				this.state = 1043;
				this.match(EclangParser.INDENT);
				this.state = 1045;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1044;
					(localctx as StatementWhenContext)._whenArm = this.whenArm();
					(localctx as StatementWhenContext)._arms.push((localctx as StatementWhenContext)._whenArm);
					}
					}
					this.state = 1047;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (((((_la - 1)) & ~0x1F) === 0 && ((1 << (_la - 1)) & 4056032207) !== 0) || ((((_la - 35)) & ~0x1F) === 0 && ((1 << (_la - 35)) & 1342210063) !== 0) || ((((_la - 98)) & ~0x1F) === 0 && ((1 << (_la - 98)) & 267911169) !== 0));
				this.state = 1049;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 27:
				localctx = new StatementLetIfContext(this, localctx);
				this.enterOuterAlt(localctx, 27);
				{
				this.state = 1051;
				this.match(EclangParser.T__62);
				this.state = 1052;
				(localctx as StatementLetIfContext)._kind = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===78 || _la===79)) {
				    (localctx as StatementLetIfContext)._kind = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1053;
				this.match(EclangParser.NAME);
				this.state = 1054;
				this.match(EclangParser.T__14);
				this.state = 1055;
				(localctx as StatementLetIfContext)._value = this.expr(0);
				this.state = 1056;
				this.match(EclangParser.T__63);
				this.state = 1057;
				(localctx as StatementLetIfContext)._body = this.block();
				this.state = 1065;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 96, this._ctx) ) {
				case 1:
					{
					this.state = 1058;
					this.match(EclangParser.T__64);
					this.state = 1060;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===64) {
						{
						this.state = 1059;
						this.match(EclangParser.T__63);
						}
					}

					this.state = 1062;
					(localctx as StatementLetIfContext)._elseBody = this.block();
					}
					break;
				case 2:
					{
					this.state = 1063;
					this.match(EclangParser.T__64);
					this.state = 1064;
					(localctx as StatementLetIfContext)._next = this.statement();
					}
					break;
				}
				}
				break;
			case 28:
				localctx = new StatementIfContext(this, localctx);
				this.enterOuterAlt(localctx, 28);
				{
				this.state = 1067;
				this.match(EclangParser.T__62);
				this.state = 1068;
				(localctx as StatementIfContext)._expr = this.expr(0);
				(localctx as StatementIfContext)._conditions.push((localctx as StatementIfContext)._expr);
				this.state = 1069;
				this.match(EclangParser.T__63);
				this.state = 1070;
				(localctx as StatementIfContext)._block = this.block();
				(localctx as StatementIfContext)._bodies.push((localctx as StatementIfContext)._block);
				this.state = 1079;
				this._errHandler.sync(this);
				_alt = this._interp.adaptivePredict(this._input, 97, this._ctx);
				while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
					if (_alt === 1) {
						{
						{
						this.state = 1071;
						this.match(EclangParser.T__64);
						this.state = 1072;
						this.match(EclangParser.T__62);
						this.state = 1073;
						(localctx as StatementIfContext)._expr = this.expr(0);
						(localctx as StatementIfContext)._conditions.push((localctx as StatementIfContext)._expr);
						this.state = 1074;
						this.match(EclangParser.T__63);
						this.state = 1075;
						(localctx as StatementIfContext)._block = this.block();
						(localctx as StatementIfContext)._bodies.push((localctx as StatementIfContext)._block);
						}
						}
					}
					this.state = 1081;
					this._errHandler.sync(this);
					_alt = this._interp.adaptivePredict(this._input, 97, this._ctx);
				}
				this.state = 1089;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 99, this._ctx) ) {
				case 1:
					{
					this.state = 1082;
					this.match(EclangParser.T__64);
					this.state = 1084;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===64) {
						{
						this.state = 1083;
						this.match(EclangParser.T__63);
						}
					}

					this.state = 1086;
					(localctx as StatementIfContext)._elseBody = this.block();
					}
					break;
				case 2:
					{
					this.state = 1087;
					this.match(EclangParser.T__64);
					this.state = 1088;
					(localctx as StatementIfContext)._next = this.statement();
					}
					break;
				}
				}
				break;
			case 29:
				localctx = new StatementLabeledContext(this, localctx);
				this.enterOuterAlt(localctx, 29);
				{
				this.state = 1091;
				(localctx as StatementLabeledContext)._label = this.match(EclangParser.NAME);
				this.state = 1092;
				this.match(EclangParser.T__66);
				this.state = 1093;
				this.statement();
				}
				break;
			case 30:
				localctx = new StatementWhileContext(this, localctx);
				this.enterOuterAlt(localctx, 30);
				{
				this.state = 1094;
				this.match(EclangParser.T__94);
				this.state = 1095;
				(localctx as StatementWhileContext)._condition = this.expr(0);
				this.state = 1096;
				this.match(EclangParser.T__63);
				this.state = 1097;
				(localctx as StatementWhileContext)._body = this.block();
				this.state = 1103;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 101, this._ctx) ) {
				case 1:
					{
					this.state = 1098;
					this.match(EclangParser.T__64);
					this.state = 1100;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===64) {
						{
						this.state = 1099;
						this.match(EclangParser.T__63);
						}
					}

					this.state = 1102;
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
				this.state = 1105;
				this.match(EclangParser.T__98);
				this.state = 1107;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===78 || _la===79) {
					{
					this.state = 1106;
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

				this.state = 1109;
				this.match(EclangParser.NAME);
				this.state = 1110;
				this.match(EclangParser.T__32);
				this.state = 1111;
				(localctx as StatementForInRangeContext)._lower = this.expr(0);
				this.state = 1112;
				this.match(EclangParser.T__99);
				this.state = 1113;
				(localctx as StatementForInRangeContext)._upper = this.expr(0);
				this.state = 1114;
				this.match(EclangParser.T__63);
				this.state = 1115;
				(localctx as StatementForInRangeContext)._body = this.block();
				this.state = 1121;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 104, this._ctx) ) {
				case 1:
					{
					this.state = 1116;
					this.match(EclangParser.T__64);
					this.state = 1118;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===64) {
						{
						this.state = 1117;
						this.match(EclangParser.T__63);
						}
					}

					this.state = 1120;
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
				this.state = 1123;
				this.match(EclangParser.T__98);
				this.state = 1125;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===78 || _la===79) {
					{
					this.state = 1124;
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

				this.state = 1129;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 106, this._ctx) ) {
				case 1:
					{
					this.state = 1127;
					(localctx as StatementForInContext)._index = this.match(EclangParser.NAME);
					this.state = 1128;
					this.match(EclangParser.T__4);
					}
					break;
				}
				this.state = 1133;
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case 118:
					{
					this.state = 1131;
					(localctx as StatementForInContext)._name = this.match(EclangParser.NAME);
					}
					break;
				case 4:
					{
					this.state = 1132;
					(localctx as StatementForInContext)._parts = this.destructure();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				this.state = 1135;
				this.match(EclangParser.T__32);
				this.state = 1136;
				(localctx as StatementForInContext)._iterable = this.expr(0);
				this.state = 1139;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===101) {
					{
					this.state = 1137;
					this.match(EclangParser.T__100);
					this.state = 1138;
					(localctx as StatementForInContext)._filter = this.expr(0);
					}
				}

				this.state = 1141;
				this.match(EclangParser.T__63);
				this.state = 1142;
				(localctx as StatementForInContext)._body = this.block();
				this.state = 1148;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 110, this._ctx) ) {
				case 1:
					{
					this.state = 1143;
					this.match(EclangParser.T__64);
					this.state = 1145;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===64) {
						{
						this.state = 1144;
						this.match(EclangParser.T__63);
						}
					}

					this.state = 1147;
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
				this.state = 1150;
				this.match(EclangParser.T__30);
				this.state = 1151;
				(localctx as StatementTryContext)._left = this.expr(0);
				this.state = 1152;
				this.match(EclangParser.NL);
				}
				break;
			case 34:
				localctx = new StatementTryCatchContext(this, localctx);
				this.enterOuterAlt(localctx, 34);
				{
				this.state = 1154;
				this.match(EclangParser.T__30);
				this.state = 1155;
				(localctx as StatementTryCatchContext)._left = this.expr(0);
				this.state = 1156;
				this.match(EclangParser.T__101);
				this.state = 1157;
				this.match(EclangParser.NAME);
				this.state = 1158;
				this.match(EclangParser.T__63);
				this.state = 1159;
				(localctx as StatementTryCatchContext)._elseBody = this.block();
				}
				break;
			case 35:
				localctx = new StatementFunctionCallNoArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 35);
				{
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
			case 36:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 36);
				{
				this.state = 1165;
				this.match(EclangParser.NAME);
				this.state = 1166;
				this.match(EclangParser.T__3);
				this.state = 1167;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1172;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1168;
					this.match(EclangParser.T__4);
					this.state = 1169;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1174;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1175;
				this.match(EclangParser.T__5);
				this.state = 1176;
				this.match(EclangParser.NL);
				}
				break;
			case 37:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 37);
				{
				this.state = 1178;
				this.match(EclangParser.NAME);
				this.state = 1179;
				this.match(EclangParser.T__3);
				this.state = 1180;
				this.match(EclangParser.INDENT);
				this.state = 1181;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1187;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1182;
					this.match(EclangParser.T__4);
					this.state = 1183;
					this.match(EclangParser.NL);
					this.state = 1184;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1189;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1190;
				this.match(EclangParser.NL);
				this.state = 1191;
				this.match(EclangParser.DEDENT);
				this.state = 1192;
				this.match(EclangParser.T__5);
				this.state = 1193;
				this.match(EclangParser.NL);
				}
				break;
			case 38:
				localctx = new StatementFunctionCallNoArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 38);
				{
				this.state = 1195;
				(localctx as StatementFunctionCallNoArgsContext)._expr = this.expr(0);
				(localctx as StatementFunctionCallNoArgsContext)._args.push((localctx as StatementFunctionCallNoArgsContext)._expr);
				this.state = 1196;
				this.match(EclangParser.T__10);
				this.state = 1197;
				this.match(EclangParser.NAME);
				this.state = 1198;
				this.match(EclangParser.T__3);
				this.state = 1199;
				this.match(EclangParser.T__5);
				this.state = 1200;
				this.match(EclangParser.NL);
				}
				break;
			case 39:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 39);
				{
				this.state = 1202;
				(localctx as StatementFunctionCallWithArgsContext)._expr = this.expr(0);
				(localctx as StatementFunctionCallWithArgsContext)._args.push((localctx as StatementFunctionCallWithArgsContext)._expr);
				this.state = 1203;
				this.match(EclangParser.T__10);
				this.state = 1204;
				this.match(EclangParser.NAME);
				this.state = 1205;
				this.match(EclangParser.T__3);
				this.state = 1206;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1211;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1207;
					this.match(EclangParser.T__4);
					this.state = 1208;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1213;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1214;
				this.match(EclangParser.T__5);
				this.state = 1215;
				this.match(EclangParser.NL);
				}
				break;
			case 40:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 40);
				{
				this.state = 1217;
				(localctx as StatementFunctionCallWithArgsContext)._expr = this.expr(0);
				(localctx as StatementFunctionCallWithArgsContext)._args.push((localctx as StatementFunctionCallWithArgsContext)._expr);
				this.state = 1218;
				this.match(EclangParser.T__10);
				this.state = 1219;
				this.match(EclangParser.NAME);
				this.state = 1220;
				this.match(EclangParser.T__3);
				this.state = 1221;
				this.match(EclangParser.INDENT);
				this.state = 1222;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1228;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1223;
					this.match(EclangParser.T__4);
					this.state = 1224;
					this.match(EclangParser.NL);
					this.state = 1225;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1230;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1231;
				this.match(EclangParser.NL);
				this.state = 1232;
				this.match(EclangParser.DEDENT);
				this.state = 1233;
				this.match(EclangParser.T__5);
				this.state = 1234;
				this.match(EclangParser.NL);
				}
				break;
			case 41:
				localctx = new StatementSafeCallContext(this, localctx);
				this.enterOuterAlt(localctx, 41);
				{
				this.state = 1236;
				(localctx as StatementSafeCallContext)._expr = this.expr(0);
				(localctx as StatementSafeCallContext)._args.push((localctx as StatementSafeCallContext)._expr);
				this.state = 1237;
				this.match(EclangParser.T__20);
				this.state = 1238;
				this.match(EclangParser.NAME);
				this.state = 1239;
				this.match(EclangParser.T__3);
				this.state = 1240;
				this.match(EclangParser.T__5);
				this.state = 1241;
				this.match(EclangParser.NL);
				}
				break;
			case 42:
				localctx = new StatementSafeCallContext(this, localctx);
				this.enterOuterAlt(localctx, 42);
				{
				this.state = 1243;
				(localctx as StatementSafeCallContext)._expr = this.expr(0);
				(localctx as StatementSafeCallContext)._args.push((localctx as StatementSafeCallContext)._expr);
				this.state = 1244;
				this.match(EclangParser.T__20);
				this.state = 1245;
				this.match(EclangParser.NAME);
				this.state = 1246;
				this.match(EclangParser.T__3);
				this.state = 1247;
				(localctx as StatementSafeCallContext)._argument = this.argument();
				(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
				this.state = 1252;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1248;
					this.match(EclangParser.T__4);
					this.state = 1249;
					(localctx as StatementSafeCallContext)._argument = this.argument();
					(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
					}
					}
					this.state = 1254;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1255;
				this.match(EclangParser.T__5);
				this.state = 1256;
				this.match(EclangParser.NL);
				}
				break;
			case 43:
				localctx = new StatementSafeCallContext(this, localctx);
				this.enterOuterAlt(localctx, 43);
				{
				this.state = 1258;
				(localctx as StatementSafeCallContext)._expr = this.expr(0);
				(localctx as StatementSafeCallContext)._args.push((localctx as StatementSafeCallContext)._expr);
				this.state = 1259;
				this.match(EclangParser.T__20);
				this.state = 1260;
				this.match(EclangParser.NAME);
				this.state = 1261;
				this.match(EclangParser.T__3);
				this.state = 1262;
				this.match(EclangParser.INDENT);
				this.state = 1263;
				(localctx as StatementSafeCallContext)._argument = this.argument();
				(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
				this.state = 1269;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1264;
					this.match(EclangParser.T__4);
					this.state = 1265;
					this.match(EclangParser.NL);
					this.state = 1266;
					(localctx as StatementSafeCallContext)._argument = this.argument();
					(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
					}
					}
					this.state = 1271;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1272;
				this.match(EclangParser.NL);
				this.state = 1273;
				this.match(EclangParser.DEDENT);
				this.state = 1274;
				this.match(EclangParser.T__5);
				this.state = 1275;
				this.match(EclangParser.NL);
				}
				break;
			case 44:
				localctx = new StatementPoolContext(this, localctx);
				this.enterOuterAlt(localctx, 44);
				{
				this.state = 1277;
				(localctx as StatementPoolContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1279;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===118) {
					{
					this.state = 1278;
					(localctx as StatementPoolContext)._name = this.match(EclangParser.NAME);
					}
				}

				this.state = 1281;
				this.match(EclangParser.T__63);
				this.state = 1282;
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
			this.state = 1285;
			this.match(EclangParser.T__3);
			this.state = 1286;
			localctx._destructurePart = this.destructurePart();
			localctx._parts.push(localctx._destructurePart);
			this.state = 1289;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 1287;
				this.match(EclangParser.T__4);
				this.state = 1288;
				localctx._destructurePart = this.destructurePart();
				localctx._parts.push(localctx._destructurePart);
				}
				}
				this.state = 1291;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while (_la===5);
			this.state = 1293;
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
			this.state = 1297;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 118:
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1295;
				this.match(EclangParser.NAME);
				}
				break;
			case 4:
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1296;
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
			this.state = 1321;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 124, this._ctx) ) {
			case 1:
				localctx = new WhenPatternIsContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1299;
				this.match(EclangParser.T__49);
				this.state = 1300;
				this.type_(0);
				this.state = 1303;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 1301;
					this.match(EclangParser.T__62);
					this.state = 1302;
					(localctx as WhenPatternIsContext)._guard = this.expr(0);
					}
				}

				}
				break;
			case 2:
				localctx = new WhenPatternElseContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1305;
				this.match(EclangParser.T__64);
				}
				break;
			case 3:
				localctx = new WhenPatternBindContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1306;
				this.match(EclangParser.NAME);
				this.state = 1307;
				this.match(EclangParser.T__62);
				this.state = 1308;
				(localctx as WhenPatternBindContext)._guard = this.expr(0);
				}
				break;
			case 4:
				localctx = new WhenPatternValuesContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 1309;
				(localctx as WhenPatternValuesContext)._whenValue = this.whenValue();
				(localctx as WhenPatternValuesContext)._values.push((localctx as WhenPatternValuesContext)._whenValue);
				this.state = 1314;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1310;
					this.match(EclangParser.T__4);
					this.state = 1311;
					(localctx as WhenPatternValuesContext)._whenValue = this.whenValue();
					(localctx as WhenPatternValuesContext)._values.push((localctx as WhenPatternValuesContext)._whenValue);
					}
					}
					this.state = 1316;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1319;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===63) {
					{
					this.state = 1317;
					this.match(EclangParser.T__62);
					this.state = 1318;
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
			this.state = 1329;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 125, this._ctx) ) {
			case 1:
				localctx = new WhenValuePlainContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1323;
				this.expr(0);
				}
				break;
			case 2:
				localctx = new WhenValueFromContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1324;
				(localctx as WhenValueFromContext)._from_ = this.expr(0);
				this.state = 1325;
				this.match(EclangParser.T__21);
				}
				break;
			case 3:
				localctx = new WhenValueUpToContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1327;
				(localctx as WhenValueUpToContext)._op = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===23 || _la===24)) {
				    (localctx as WhenValueUpToContext)._op = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1328;
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
			this.state = 1331;
			this.whenPattern();
			this.state = 1332;
			this.match(EclangParser.T__63);
			this.state = 1335;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 132:
				{
				this.state = 1333;
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
				this.state = 1334;
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
			this.state = 1337;
			this.match(EclangParser.T__97);
			this.state = 1341;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===78 || _la===79) {
				{
				this.state = 1338;
				localctx._kind = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===78 || _la===79)) {
				    localctx._kind = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1339;
				this.match(EclangParser.NAME);
				this.state = 1340;
				this.match(EclangParser.T__14);
				}
			}

			this.state = 1343;
			localctx._subject = this.expr(0);
			this.state = 1344;
			this.match(EclangParser.T__63);
			this.state = 1345;
			this.match(EclangParser.INDENT);
			this.state = 1347;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 1346;
				localctx._whenValueArm = this.whenValueArm();
				localctx._arms.push(localctx._whenValueArm);
				}
				}
				this.state = 1349;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while (((((_la - 1)) & ~0x1F) === 0 && ((1 << (_la - 1)) & 4056032207) !== 0) || ((((_la - 35)) & ~0x1F) === 0 && ((1 << (_la - 35)) & 1342210063) !== 0) || ((((_la - 98)) & ~0x1F) === 0 && ((1 << (_la - 98)) & 267911169) !== 0));
			this.state = 1351;
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
			this.state = 1353;
			this.whenPattern();
			this.state = 1354;
			this.match(EclangParser.T__63);
			this.state = 1358;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 129, this._ctx) ) {
			case 1:
				{
				this.state = 1355;
				localctx._value = this.tail();
				}
				break;
			case 2:
				{
				this.state = 1356;
				localctx._line = this.statement();
				}
				break;
			case 3:
				{
				this.state = 1357;
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
			this.state = 1440;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 144, this._ctx) ) {
			case 1:
				localctx = new FunctionDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1360;
				this.match(EclangParser.T__102);
				this.state = 1361;
				this.match(EclangParser.T__9);
				this.state = 1362;
				(localctx as FunctionDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1365;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1363;
					this.match(EclangParser.T__103);
					this.state = 1364;
					(localctx as FunctionDeclareContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1367;
				this.match(EclangParser.T__25);
				this.state = 1368;
				this.match(EclangParser.T__3);
				this.state = 1370;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===17 || ((((_la - 78)) & ~0x1F) === 0 && ((1 << (_la - 78)) & 7) !== 0) || _la===118) {
					{
					this.state = 1369;
					(localctx as FunctionDeclareContext)._parameter = this.parameter();
					(localctx as FunctionDeclareContext)._params.push((localctx as FunctionDeclareContext)._parameter);
					}
				}

				this.state = 1376;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1372;
					this.match(EclangParser.T__4);
					this.state = 1373;
					(localctx as FunctionDeclareContext)._parameter = this.parameter();
					(localctx as FunctionDeclareContext)._params.push((localctx as FunctionDeclareContext)._parameter);
					}
					}
					this.state = 1378;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1379;
				this.match(EclangParser.T__5);
				this.state = 1382;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67 || _la===76) {
					{
					this.state = 1380;
					_la = this._input.LA(1);
					if(!(_la===67 || _la===76)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 1381;
					(localctx as FunctionDeclareContext)._returnType = this.type_(0);
					}
				}

				this.state = 1386;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===75) {
					{
					this.state = 1384;
					this.match(EclangParser.T__74);
					this.state = 1385;
					(localctx as FunctionDeclareContext)._throwing = this.type_(0);
					}
				}

				this.state = 1388;
				this.match(EclangParser.NL);
				}
				break;
			case 2:
				localctx = new FunctionBlockContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1389;
				this.match(EclangParser.T__9);
				this.state = 1390;
				(localctx as FunctionBlockContext)._name = this.match(EclangParser.NAME);
				this.state = 1391;
				this.match(EclangParser.T__3);
				this.state = 1393;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===17 || ((((_la - 78)) & ~0x1F) === 0 && ((1 << (_la - 78)) & 7) !== 0) || _la===118) {
					{
					this.state = 1392;
					(localctx as FunctionBlockContext)._parameter = this.parameter();
					(localctx as FunctionBlockContext)._params.push((localctx as FunctionBlockContext)._parameter);
					}
				}

				this.state = 1399;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1395;
					this.match(EclangParser.T__4);
					this.state = 1396;
					(localctx as FunctionBlockContext)._parameter = this.parameter();
					(localctx as FunctionBlockContext)._params.push((localctx as FunctionBlockContext)._parameter);
					}
					}
					this.state = 1401;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1402;
				this.match(EclangParser.T__5);
				this.state = 1405;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1403;
					this.match(EclangParser.T__66);
					this.state = 1404;
					(localctx as FunctionBlockContext)._returnType = this.type_(0);
					}
				}

				this.state = 1409;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===75) {
					{
					this.state = 1407;
					this.match(EclangParser.T__74);
					this.state = 1408;
					(localctx as FunctionBlockContext)._throwing = this.type_(0);
					}
				}

				this.state = 1411;
				this.match(EclangParser.T__63);
				this.state = 1412;
				(localctx as FunctionBlockContext)._body = this.block();
				}
				break;
			case 3:
				localctx = new FunctionInlineContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1414;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===105) {
					{
					this.state = 1413;
					this.match(EclangParser.T__104);
					}
				}

				this.state = 1416;
				this.match(EclangParser.T__9);
				this.state = 1417;
				(localctx as FunctionInlineContext)._name = this.match(EclangParser.NAME);
				this.state = 1418;
				this.match(EclangParser.T__3);
				this.state = 1420;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===17 || ((((_la - 78)) & ~0x1F) === 0 && ((1 << (_la - 78)) & 7) !== 0) || _la===118) {
					{
					this.state = 1419;
					(localctx as FunctionInlineContext)._parameter = this.parameter();
					(localctx as FunctionInlineContext)._params.push((localctx as FunctionInlineContext)._parameter);
					}
				}

				this.state = 1426;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===5) {
					{
					{
					this.state = 1422;
					this.match(EclangParser.T__4);
					this.state = 1423;
					(localctx as FunctionInlineContext)._parameter = this.parameter();
					(localctx as FunctionInlineContext)._params.push((localctx as FunctionInlineContext)._parameter);
					}
					}
					this.state = 1428;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1429;
				this.match(EclangParser.T__5);
				this.state = 1432;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1430;
					this.match(EclangParser.T__66);
					this.state = 1431;
					(localctx as FunctionInlineContext)._returnType = this.type_(0);
					}
				}

				this.state = 1436;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===75) {
					{
					this.state = 1434;
					this.match(EclangParser.T__74);
					this.state = 1435;
					(localctx as FunctionInlineContext)._throwing = this.type_(0);
					}
				}

				this.state = 1438;
				this.match(EclangParser.T__14);
				this.state = 1439;
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
			let _alt: number;
			this.state = 1733;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 174, this._ctx) ) {
			case 1:
				localctx = new GlobalDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1442;
				this.match(EclangParser.T__77);
				this.state = 1443;
				this.match(EclangParser.NAME);
				this.state = 1444;
				this.match(EclangParser.T__14);
				this.state = 1445;
				this.expr(0);
				this.state = 1446;
				this.match(EclangParser.NL);
				}
				break;
			case 2:
				localctx = new GlobalDefineConstContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1448;
				this.match(EclangParser.T__78);
				this.state = 1449;
				this.match(EclangParser.NAME);
				this.state = 1450;
				this.match(EclangParser.T__14);
				this.state = 1451;
				this.expr(0);
				this.state = 1452;
				this.match(EclangParser.NL);
				}
				break;
			case 3:
				localctx = new GlobalDefineVolatileContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1454;
				this.match(EclangParser.T__79);
				this.state = 1455;
				this.match(EclangParser.NAME);
				this.state = 1456;
				this.match(EclangParser.T__14);
				this.state = 1457;
				this.expr(0);
				this.state = 1458;
				this.match(EclangParser.NL);
				}
				break;
			case 4:
				localctx = new GlobalDefineTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 1460;
				this.match(EclangParser.T__77);
				this.state = 1461;
				this.match(EclangParser.NAME);
				this.state = 1464;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1462;
					this.match(EclangParser.T__66);
					this.state = 1463;
					this.type_(0);
					}
				}

				this.state = 1466;
				this.match(EclangParser.T__14);
				this.state = 1467;
				this.expr(0);
				this.state = 1468;
				this.match(EclangParser.NL);
				}
				break;
			case 5:
				localctx = new GlobalDefineConstTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 1470;
				this.match(EclangParser.T__78);
				this.state = 1471;
				this.match(EclangParser.NAME);
				this.state = 1474;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1472;
					this.match(EclangParser.T__66);
					this.state = 1473;
					this.type_(0);
					}
				}

				this.state = 1476;
				this.match(EclangParser.T__14);
				this.state = 1477;
				this.expr(0);
				this.state = 1478;
				this.match(EclangParser.NL);
				}
				break;
			case 6:
				localctx = new GlobalDefineVolatileTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 6);
				{
				this.state = 1480;
				this.match(EclangParser.T__79);
				this.state = 1481;
				this.match(EclangParser.NAME);
				this.state = 1484;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1482;
					this.match(EclangParser.T__66);
					this.state = 1483;
					this.type_(0);
					}
				}

				this.state = 1486;
				this.match(EclangParser.T__14);
				this.state = 1487;
				this.expr(0);
				this.state = 1488;
				this.match(EclangParser.NL);
				}
				break;
			case 7:
				localctx = new GlobalConstantContext(this, localctx);
				this.enterOuterAlt(localctx, 7);
				{
				this.state = 1490;
				this.match(EclangParser.T__80);
				this.state = 1491;
				this.match(EclangParser.NAME);
				this.state = 1494;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1492;
					this.match(EclangParser.T__66);
					this.state = 1493;
					this.type_(0);
					}
				}

				this.state = 1496;
				this.match(EclangParser.T__14);
				this.state = 1497;
				this.expr(0);
				this.state = 1498;
				this.match(EclangParser.NL);
				}
				break;
			case 8:
				localctx = new GlobalDeclareDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 8);
				{
				this.state = 1500;
				this.match(EclangParser.T__102);
				this.state = 1501;
				this.match(EclangParser.T__77);
				this.state = 1502;
				(localctx as GlobalDeclareDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1505;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1503;
					this.match(EclangParser.T__103);
					this.state = 1504;
					(localctx as GlobalDeclareDefineContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1507;
				this.match(EclangParser.T__25);
				this.state = 1508;
				this.type_(0);
				this.state = 1509;
				this.match(EclangParser.NL);
				}
				break;
			case 9:
				localctx = new GlobalDeclareConstContext(this, localctx);
				this.enterOuterAlt(localctx, 9);
				{
				this.state = 1511;
				this.match(EclangParser.T__102);
				this.state = 1512;
				_la = this._input.LA(1);
				if(!(_la===79 || _la===81)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1513;
				(localctx as GlobalDeclareConstContext)._name = this.match(EclangParser.NAME);
				this.state = 1516;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1514;
					this.match(EclangParser.T__103);
					this.state = 1515;
					(localctx as GlobalDeclareConstContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1518;
				this.match(EclangParser.T__25);
				this.state = 1519;
				this.type_(0);
				this.state = 1520;
				this.match(EclangParser.NL);
				}
				break;
			case 10:
				localctx = new GlobalTypeDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 10);
				{
				this.state = 1522;
				this.match(EclangParser.T__102);
				this.state = 1523;
				this.match(EclangParser.T__105);
				this.state = 1524;
				this.match(EclangParser.NAME);
				this.state = 1525;
				this.match(EclangParser.T__25);
				this.state = 1526;
				this.type_(0);
				this.state = 1527;
				this.match(EclangParser.NL);
				}
				break;
			case 11:
				localctx = new GlobalTypeAliasContext(this, localctx);
				this.enterOuterAlt(localctx, 11);
				{
				this.state = 1529;
				this.match(EclangParser.T__105);
				this.state = 1530;
				(localctx as GlobalTypeAliasContext)._name = this.match(EclangParser.NAME);
				this.state = 1531;
				this.match(EclangParser.T__14);
				this.state = 1533;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 151, this._ctx) ) {
				case 1:
					{
					this.state = 1532;
					(localctx as GlobalTypeAliasContext)._modifier = this.match(EclangParser.NAME);
					}
					break;
				}
				this.state = 1535;
				this.type_(0);
				this.state = 1536;
				this.match(EclangParser.NL);
				}
				break;
			case 12:
				localctx = new GlobalEnumDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 12);
				{
				this.state = 1538;
				this.match(EclangParser.T__102);
				this.state = 1539;
				this.match(EclangParser.T__106);
				this.state = 1540;
				(localctx as GlobalEnumDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1541;
				this.match(EclangParser.T__25);
				this.state = 1542;
				this.match(EclangParser.INDENT);
				this.state = 1547;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1543;
					(localctx as GlobalEnumDeclareContext)._ATOM = this.match(EclangParser.ATOM);
					(localctx as GlobalEnumDeclareContext)._atoms.push((localctx as GlobalEnumDeclareContext)._ATOM);
					this.state = 1544;
					this.match(EclangParser.T__14);
					this.state = 1545;
					(localctx as GlobalEnumDeclareContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalEnumDeclareContext)._constants.push((localctx as GlobalEnumDeclareContext)._NAME);
					this.state = 1546;
					this.match(EclangParser.NL);
					}
					}
					this.state = 1549;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===119);
				this.state = 1551;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 13:
				localctx = new GlobalUnitDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 13);
				{
				this.state = 1552;
				this.match(EclangParser.T__107);
				this.state = 1553;
				this.match(EclangParser.NAME);
				this.state = 1554;
				this.match(EclangParser.T__3);
				this.state = 1555;
				this.match(EclangParser.NAME);
				this.state = 1556;
				this.match(EclangParser.T__66);
				this.state = 1557;
				this.type_(0);
				this.state = 1558;
				this.match(EclangParser.T__5);
				this.state = 1559;
				this.match(EclangParser.T__14);
				this.state = 1560;
				this.expr(0);
				this.state = 1561;
				this.match(EclangParser.NL);
				}
				break;
			case 14:
				localctx = new GlobalRecordDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 14);
				{
				this.state = 1566;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===118) {
					{
					{
					this.state = 1563;
					(localctx as GlobalRecordDefineContext)._recordModifier = this.recordModifier();
					(localctx as GlobalRecordDefineContext)._modifiers.push((localctx as GlobalRecordDefineContext)._recordModifier);
					}
					}
					this.state = 1568;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1569;
				_la = this._input.LA(1);
				if(!(_la===109 || _la===110)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1570;
				(localctx as GlobalRecordDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1581;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===12) {
					{
					this.state = 1571;
					this.match(EclangParser.T__11);
					this.state = 1572;
					(localctx as GlobalRecordDefineContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalRecordDefineContext)._typeParams.push((localctx as GlobalRecordDefineContext)._NAME);
					this.state = 1577;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===5) {
						{
						{
						this.state = 1573;
						this.match(EclangParser.T__4);
						this.state = 1574;
						(localctx as GlobalRecordDefineContext)._NAME = this.match(EclangParser.NAME);
						(localctx as GlobalRecordDefineContext)._typeParams.push((localctx as GlobalRecordDefineContext)._NAME);
						}
						}
						this.state = 1579;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					this.state = 1580;
					this.match(EclangParser.T__12);
					}
				}

				this.state = 1589;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===111) {
					{
					this.state = 1583;
					this.match(EclangParser.T__110);
					this.state = 1586;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 156, this._ctx) ) {
					case 1:
						{
						this.state = 1584;
						(localctx as GlobalRecordDefineContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 1585;
						this.match(EclangParser.T__10);
						}
						break;
					}
					this.state = 1588;
					(localctx as GlobalRecordDefineContext)._extending = this.match(EclangParser.NAME);
					}
				}

				this.state = 1591;
				this.match(EclangParser.T__25);
				this.state = 1592;
				this.match(EclangParser.INDENT);
				this.state = 1594;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1593;
					(localctx as GlobalRecordDefineContext)._recordField = this.recordField();
					(localctx as GlobalRecordDefineContext)._fields.push((localctx as GlobalRecordDefineContext)._recordField);
					}
					}
					this.state = 1596;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===118);
				this.state = 1598;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 15:
				localctx = new GlobalRecordDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 15);
				{
				this.state = 1600;
				_la = this._input.LA(1);
				if(!(_la===109 || _la===110)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1601;
				(localctx as GlobalRecordDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1602;
				this.match(EclangParser.T__110);
				this.state = 1605;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 159, this._ctx) ) {
				case 1:
					{
					this.state = 1603;
					(localctx as GlobalRecordDefineContext)._qualifier = this.match(EclangParser.NAME);
					this.state = 1604;
					this.match(EclangParser.T__10);
					}
					break;
				}
				this.state = 1607;
				(localctx as GlobalRecordDefineContext)._extending = this.match(EclangParser.NAME);
				this.state = 1608;
				this.match(EclangParser.NL);
				}
				break;
			case 16:
				localctx = new GlobalUnionDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 16);
				{
				this.state = 1612;
				this._errHandler.sync(this);
				_alt = this._interp.adaptivePredict(this._input, 160, this._ctx);
				while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
					if (_alt === 1) {
						{
						{
						this.state = 1609;
						(localctx as GlobalUnionDefineContext)._recordModifier = this.recordModifier();
						(localctx as GlobalUnionDefineContext)._modifiers.push((localctx as GlobalUnionDefineContext)._recordModifier);
						}
						}
					}
					this.state = 1614;
					this._errHandler.sync(this);
					_alt = this._interp.adaptivePredict(this._input, 160, this._ctx);
				}
				this.state = 1615;
				(localctx as GlobalUnionDefineContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1616;
				(localctx as GlobalUnionDefineContext)._name = this.match(EclangParser.NAME);
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
					(localctx as GlobalUnionDefineContext)._recordField = this.recordField();
					(localctx as GlobalUnionDefineContext)._fields.push((localctx as GlobalUnionDefineContext)._recordField);
					}
					}
					this.state = 1622;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===118);
				this.state = 1624;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 17:
				localctx = new GlobalRecordDeclareDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 17);
				{
				this.state = 1626;
				this.match(EclangParser.T__102);
				this.state = 1627;
				_la = this._input.LA(1);
				if(!(_la===109 || _la===110)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1628;
				(localctx as GlobalRecordDeclareDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1631;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1629;
					this.match(EclangParser.T__103);
					this.state = 1630;
					(localctx as GlobalRecordDeclareDefineContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1633;
				this.match(EclangParser.T__25);
				this.state = 1634;
				this.match(EclangParser.INDENT);
				this.state = 1641;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1635;
					(localctx as GlobalRecordDeclareDefineContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalRecordDeclareDefineContext)._names.push((localctx as GlobalRecordDeclareDefineContext)._NAME);
					this.state = 1636;
					this.match(EclangParser.T__66);
					this.state = 1637;
					(localctx as GlobalRecordDeclareDefineContext)._type_ = this.type_(0);
					(localctx as GlobalRecordDeclareDefineContext)._types.push((localctx as GlobalRecordDeclareDefineContext)._type_);
					this.state = 1638;
					(localctx as GlobalRecordDeclareDefineContext)._fieldWidth = this.fieldWidth();
					(localctx as GlobalRecordDeclareDefineContext)._widths.push((localctx as GlobalRecordDeclareDefineContext)._fieldWidth);
					this.state = 1639;
					this.match(EclangParser.NL);
					}
					}
					this.state = 1643;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===118);
				this.state = 1645;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 18:
				localctx = new GlobalRecordDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 18);
				{
				this.state = 1647;
				this.match(EclangParser.T__102);
				this.state = 1648;
				_la = this._input.LA(1);
				if(!(_la===109 || _la===110)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1649;
				(localctx as GlobalRecordDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1652;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1650;
					this.match(EclangParser.T__103);
					this.state = 1651;
					(localctx as GlobalRecordDeclareContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1654;
				this.match(EclangParser.NL);
				}
				break;
			case 19:
				localctx = new GlobalStructDeclareDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 19);
				{
				this.state = 1655;
				this.match(EclangParser.T__102);
				this.state = 1656;
				this.match(EclangParser.T__111);
				this.state = 1657;
				(localctx as GlobalStructDeclareDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1660;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1658;
					this.match(EclangParser.T__103);
					this.state = 1659;
					(localctx as GlobalStructDeclareDefineContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1662;
				this.match(EclangParser.T__25);
				this.state = 1663;
				this.match(EclangParser.INDENT);
				this.state = 1670;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1664;
					(localctx as GlobalStructDeclareDefineContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalStructDeclareDefineContext)._names.push((localctx as GlobalStructDeclareDefineContext)._NAME);
					this.state = 1665;
					this.match(EclangParser.T__66);
					this.state = 1666;
					(localctx as GlobalStructDeclareDefineContext)._type_ = this.type_(0);
					(localctx as GlobalStructDeclareDefineContext)._types.push((localctx as GlobalStructDeclareDefineContext)._type_);
					this.state = 1667;
					(localctx as GlobalStructDeclareDefineContext)._fieldWidth = this.fieldWidth();
					(localctx as GlobalStructDeclareDefineContext)._widths.push((localctx as GlobalStructDeclareDefineContext)._fieldWidth);
					this.state = 1668;
					this.match(EclangParser.NL);
					}
					}
					this.state = 1672;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===118);
				this.state = 1674;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 20:
				localctx = new GlobalStructDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 20);
				{
				this.state = 1676;
				this.match(EclangParser.T__102);
				this.state = 1677;
				this.match(EclangParser.T__111);
				this.state = 1678;
				(localctx as GlobalStructDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1681;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1679;
					this.match(EclangParser.T__103);
					this.state = 1680;
					(localctx as GlobalStructDeclareContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1683;
				this.match(EclangParser.NL);
				}
				break;
			case 21:
				localctx = new GlobalUnionDeclareDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 21);
				{
				this.state = 1684;
				this.match(EclangParser.T__102);
				this.state = 1685;
				(localctx as GlobalUnionDeclareDefineContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1687;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===109 || _la===110) {
					{
					this.state = 1686;
					(localctx as GlobalUnionDeclareDefineContext)._typedef = this._input.LT(1);
					_la = this._input.LA(1);
					if(!(_la===109 || _la===110)) {
					    (localctx as GlobalUnionDeclareDefineContext)._typedef = this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					}
				}

				this.state = 1689;
				(localctx as GlobalUnionDeclareDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1692;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1690;
					this.match(EclangParser.T__103);
					this.state = 1691;
					(localctx as GlobalUnionDeclareDefineContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1694;
				this.match(EclangParser.T__25);
				this.state = 1695;
				this.match(EclangParser.INDENT);
				this.state = 1702;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1696;
					(localctx as GlobalUnionDeclareDefineContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalUnionDeclareDefineContext)._names.push((localctx as GlobalUnionDeclareDefineContext)._NAME);
					this.state = 1697;
					this.match(EclangParser.T__66);
					this.state = 1698;
					(localctx as GlobalUnionDeclareDefineContext)._type_ = this.type_(0);
					(localctx as GlobalUnionDeclareDefineContext)._types.push((localctx as GlobalUnionDeclareDefineContext)._type_);
					this.state = 1699;
					(localctx as GlobalUnionDeclareDefineContext)._fieldWidth = this.fieldWidth();
					(localctx as GlobalUnionDeclareDefineContext)._widths.push((localctx as GlobalUnionDeclareDefineContext)._fieldWidth);
					this.state = 1700;
					this.match(EclangParser.NL);
					}
					}
					this.state = 1704;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===118);
				this.state = 1706;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 22:
				localctx = new GlobalUnionDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 22);
				{
				this.state = 1708;
				this.match(EclangParser.T__102);
				this.state = 1709;
				(localctx as GlobalUnionDeclareContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1711;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===109 || _la===110) {
					{
					this.state = 1710;
					(localctx as GlobalUnionDeclareContext)._typedef = this._input.LT(1);
					_la = this._input.LA(1);
					if(!(_la===109 || _la===110)) {
					    (localctx as GlobalUnionDeclareContext)._typedef = this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					}
				}

				this.state = 1713;
				(localctx as GlobalUnionDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1716;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1714;
					this.match(EclangParser.T__103);
					this.state = 1715;
					(localctx as GlobalUnionDeclareContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1718;
				this.match(EclangParser.NL);
				}
				break;
			case 23:
				localctx = new GlobalInterfaceDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 23);
				{
				this.state = 1719;
				this.match(EclangParser.T__76);
				this.state = 1720;
				(localctx as GlobalInterfaceDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1721;
				this.match(EclangParser.T__25);
				this.state = 1722;
				this.match(EclangParser.INDENT);
				this.state = 1724;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1723;
					(localctx as GlobalInterfaceDefineContext)._interfaceMember = this.interfaceMember();
					(localctx as GlobalInterfaceDefineContext)._members.push((localctx as GlobalInterfaceDefineContext)._interfaceMember);
					}
					}
					this.state = 1726;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===10 || _la===118);
				this.state = 1728;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 24:
				localctx = new GlobalNamespaceContext(this, localctx);
				this.enterOuterAlt(localctx, 24);
				{
				this.state = 1730;
				this.match(EclangParser.T__112);
				this.state = 1731;
				(localctx as GlobalNamespaceContext)._name = this.match(EclangParser.STRING);
				this.state = 1732;
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
			this.state = 1763;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 118:
				localctx = new InterfaceFieldContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1735;
				(localctx as InterfaceFieldContext)._name = this.match(EclangParser.NAME);
				this.state = 1736;
				this.match(EclangParser.T__66);
				this.state = 1737;
				this.type_(0);
				this.state = 1738;
				this.match(EclangParser.NL);
				}
				break;
			case 10:
				localctx = new InterfaceMethodContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1740;
				this.match(EclangParser.T__9);
				this.state = 1741;
				(localctx as InterfaceMethodContext)._name = this.match(EclangParser.NAME);
				this.state = 1742;
				this.match(EclangParser.T__3);
				this.state = 1751;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x1F) === 0 && ((1 << _la) & 536876048) !== 0) || _la===73 || _la===74 || _la===118 || _la===119) {
					{
					this.state = 1743;
					(localctx as InterfaceMethodContext)._type_ = this.type_(0);
					(localctx as InterfaceMethodContext)._params.push((localctx as InterfaceMethodContext)._type_);
					this.state = 1748;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===5) {
						{
						{
						this.state = 1744;
						this.match(EclangParser.T__4);
						this.state = 1745;
						(localctx as InterfaceMethodContext)._type_ = this.type_(0);
						(localctx as InterfaceMethodContext)._params.push((localctx as InterfaceMethodContext)._type_);
						}
						}
						this.state = 1750;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					}
				}

				this.state = 1753;
				this.match(EclangParser.T__5);
				this.state = 1756;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===67) {
					{
					this.state = 1754;
					this.match(EclangParser.T__66);
					this.state = 1755;
					(localctx as InterfaceMethodContext)._returnType = this.type_(0);
					}
				}

				this.state = 1760;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===75) {
					{
					this.state = 1758;
					this.match(EclangParser.T__74);
					this.state = 1759;
					(localctx as InterfaceMethodContext)._throwing = this.type_(0);
					}
				}

				this.state = 1762;
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
			this.state = 1765;
			localctx._name = this.match(EclangParser.NAME);
			this.state = 1766;
			this.match(EclangParser.T__66);
			this.state = 1767;
			this.type_(0);
			this.state = 1768;
			this.fieldWidth();
			this.state = 1771;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===15) {
				{
				this.state = 1769;
				this.match(EclangParser.T__14);
				this.state = 1770;
				localctx._value = this.expr(0);
				}
			}

			this.state = 1773;
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
	public fieldWidth(): FieldWidthContext {
		let localctx: FieldWidthContext = new FieldWidthContext(this, this._ctx, this.state);
		this.enterRule(localctx, 46, EclangParser.RULE_fieldWidth);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1778;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 67:
				{
				this.state = 1775;
				this.match(EclangParser.T__66);
				this.state = 1776;
				localctx._width = this.expr(0);
				}
				break;
			case 119:
				{
				this.state = 1777;
				localctx._atomWidth = this.match(EclangParser.ATOM);
				}
				break;
			case 15:
			case 116:
				break;
			default:
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
	public recordModifier(): RecordModifierContext {
		let localctx: RecordModifierContext = new RecordModifierContext(this, this._ctx, this.state);
		this.enterRule(localctx, 48, EclangParser.RULE_recordModifier);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1780;
			localctx._name = this.match(EclangParser.NAME);
			this.state = 1785;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===4) {
				{
				this.state = 1781;
				this.match(EclangParser.T__3);
				this.state = 1782;
				localctx._size = this.expr(0);
				this.state = 1783;
				this.match(EclangParser.T__5);
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
	// @RuleVersion(0)
	public use(): UseContext {
		let localctx: UseContext = new UseContext(this, this._ctx, this.state);
		this.enterRule(localctx, 50, EclangParser.RULE_use);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1787;
			this.match(EclangParser.T__113);
			this.state = 1788;
			localctx._from_ = this.match(EclangParser.STRING);
			this.state = 1789;
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
		this.enterRule(localctx, 52, EclangParser.RULE_interpolation);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1791;
			this.expr(0);
			this.state = 1795;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la===116) {
				{
				{
				this.state = 1792;
				this.match(EclangParser.NL);
				}
				}
				this.state = 1797;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			this.state = 1798;
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
	public exportDeclaration(): ExportDeclarationContext {
		let localctx: ExportDeclarationContext = new ExportDeclarationContext(this, this._ctx, this.state);
		this.enterRule(localctx, 54, EclangParser.RULE_exportDeclaration);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1800;
			localctx._keyword = this.match(EclangParser.NAME);
			this.state = 1803;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===104) {
				{
				this.state = 1801;
				this.match(EclangParser.T__103);
				this.state = 1802;
				localctx._externalName = this.match(EclangParser.STRING);
				}
			}

			this.state = 1805;
			this.func();
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
		this.enterRule(localctx, 56, EclangParser.RULE_privateDeclaration);
		try {
			this.state = 1811;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 185, this._ctx) ) {
			case 1:
				localctx = new PrivateFunctionContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1807;
				this.match(EclangParser.T__114);
				this.state = 1808;
				this.func();
				}
				break;
			case 2:
				localctx = new PrivateGlobalContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1809;
				this.match(EclangParser.T__114);
				this.state = 1810;
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
		this.enterRule(localctx, 58, EclangParser.RULE_program);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1821;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la===10 || ((((_la - 77)) & ~0x1F) === 0 && ((1 << (_la - 77)) & 3019898911) !== 0) || ((((_la - 109)) & ~0x1F) === 0 && ((1 << (_la - 109)) & 755) !== 0)) {
				{
				this.state = 1819;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 186, this._ctx) ) {
				case 1:
					{
					this.state = 1813;
					localctx._func = this.func();
					localctx._functions.push(localctx._func);
					}
					break;
				case 2:
					{
					this.state = 1814;
					localctx._global = this.global();
					localctx._globals.push(localctx._global);
					}
					break;
				case 3:
					{
					this.state = 1815;
					localctx._use = this.use();
					localctx._uses.push(localctx._use);
					}
					break;
				case 4:
					{
					this.state = 1816;
					localctx._privateDeclaration = this.privateDeclaration();
					localctx._privates.push(localctx._privateDeclaration);
					}
					break;
				case 5:
					{
					this.state = 1817;
					localctx._exportDeclaration = this.exportDeclaration();
					localctx._exports.push(localctx._exportDeclaration);
					}
					break;
				case 6:
					{
					this.state = 1818;
					this.match(EclangParser.NL);
					}
					break;
				}
				}
				this.state = 1823;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			this.state = 1824;
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
			return this.precpred(this._ctx, 16);
		case 37:
			return this.precpred(this._ctx, 15);
		case 38:
			return this.precpred(this._ctx, 14);
		case 39:
			return this.precpred(this._ctx, 8);
		case 40:
			return this.precpred(this._ctx, 7);
		}
		return true;
	}

	public static readonly _serializedATN: number[] = [4,1,133,1827,2,0,7,0,
	2,1,7,1,2,2,7,2,2,3,7,3,2,4,7,4,2,5,7,5,2,6,7,6,2,7,7,7,2,8,7,8,2,9,7,9,
	2,10,7,10,2,11,7,11,2,12,7,12,2,13,7,13,2,14,7,14,2,15,7,15,2,16,7,16,2,
	17,7,17,2,18,7,18,2,19,7,19,2,20,7,20,2,21,7,21,2,22,7,22,2,23,7,23,2,24,
	7,24,2,25,7,25,2,26,7,26,2,27,7,27,2,28,7,28,2,29,7,29,1,0,1,0,1,0,1,0,
	3,0,65,8,0,1,1,1,1,1,1,1,1,1,1,3,1,72,8,1,1,2,1,2,4,2,76,8,2,11,2,12,2,
	77,1,2,1,2,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,90,8,3,10,3,12,3,93,9,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,103,8,3,10,3,12,3,106,9,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,3,3,130,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,141,8,3,10,3,
	12,3,144,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,155,8,3,10,3,12,3,
	158,9,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,166,8,3,1,3,1,3,1,3,1,3,1,3,5,3,173,
	8,3,10,3,12,3,176,9,3,1,3,1,3,3,3,180,8,3,3,3,182,8,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,5,3,192,8,3,10,3,12,3,195,9,3,1,3,1,3,1,3,1,3,3,3,201,8,3,
	1,3,1,3,1,3,1,3,1,3,5,3,208,8,3,10,3,12,3,211,9,3,1,3,1,3,3,3,215,8,3,3,
	3,217,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,229,8,3,10,3,12,3,
	232,9,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,240,8,3,1,3,1,3,1,3,1,3,1,3,5,3,247,
	8,3,10,3,12,3,250,9,3,1,3,1,3,3,3,254,8,3,1,3,1,3,1,3,1,3,3,3,260,8,3,1,
	3,1,3,1,3,1,3,1,3,5,3,267,8,3,10,3,12,3,270,9,3,1,3,1,3,3,3,274,8,3,3,3,
	276,8,3,1,3,1,3,1,3,1,3,5,3,282,8,3,10,3,12,3,285,9,3,1,3,1,3,1,3,1,3,3,
	3,291,8,3,1,3,1,3,1,3,1,3,1,3,5,3,298,8,3,10,3,12,3,301,9,3,1,3,1,3,3,3,
	305,8,3,3,3,307,8,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,315,8,3,10,3,12,3,318,9,
	3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,326,8,3,1,3,1,3,1,3,1,3,1,3,5,3,333,8,3,10,
	3,12,3,336,9,3,1,3,1,3,3,3,340,8,3,3,3,342,8,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,5,3,351,8,3,10,3,12,3,354,9,3,1,3,1,3,1,3,1,3,3,3,360,8,3,1,3,1,3,1,
	3,1,3,1,3,5,3,367,8,3,10,3,12,3,370,9,3,1,3,1,3,3,3,374,8,3,3,3,376,8,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,387,8,3,10,3,12,3,390,9,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,401,8,3,10,3,12,3,404,9,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,5,3,414,8,3,10,3,12,3,417,9,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,3,3,445,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,470,8,3,10,3,12,3,473,9,3,
	3,3,475,8,3,1,3,1,3,1,3,3,3,480,8,3,1,3,1,3,3,3,484,8,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,499,8,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,5,3,547,8,3,10,3,12,3,550,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,5,3,563,8,3,10,3,12,3,566,9,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,584,8,3,10,3,12,3,587,9,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,600,8,3,10,3,12,3,603,9,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,631,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,666,8,3,10,3,12,3,669,9,3,1,4,1,4,3,
	4,673,8,4,1,4,1,4,1,5,1,5,3,5,679,8,5,1,5,1,5,1,5,1,5,3,5,685,8,5,1,5,1,
	5,3,5,689,8,5,3,5,691,8,5,1,6,1,6,1,6,1,6,3,6,697,8,6,1,7,1,7,1,7,3,7,702,
	8,7,1,8,1,8,1,8,3,8,707,8,8,1,8,1,8,1,8,1,8,1,8,5,8,714,8,8,10,8,12,8,717,
	9,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,5,8,733,8,8,
	10,8,12,8,736,9,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,
	8,1,8,3,8,753,8,8,1,8,1,8,1,8,1,8,3,8,759,8,8,1,8,1,8,1,8,1,8,3,8,765,8,
	8,1,8,5,8,768,8,8,10,8,12,8,771,9,8,1,8,1,8,1,8,3,8,776,8,8,1,8,1,8,1,8,
	1,8,1,8,1,8,1,8,3,8,785,8,8,1,8,1,8,1,8,1,8,3,8,791,8,8,1,8,5,8,794,8,8,
	10,8,12,8,797,9,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,4,8,807,8,8,11,8,12,8,
	808,1,8,1,8,3,8,813,8,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,5,8,825,
	8,8,10,8,12,8,828,9,8,1,8,1,8,1,8,1,8,1,8,5,8,835,8,8,10,8,12,8,838,9,8,
	1,8,1,8,1,8,1,8,1,8,1,8,1,8,1,8,5,8,848,8,8,10,8,12,8,851,9,8,1,9,1,9,1,
	9,1,9,1,9,1,9,1,9,1,9,1,9,4,9,862,8,9,11,9,12,9,863,1,9,1,9,1,10,1,10,1,
	10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,1,10,
	1,10,1,10,1,10,1,10,1,10,1,10,3,10,891,8,10,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,3,11,950,8,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,3,11,961,8,11,1,11,1,11,3,11,965,8,11,1,11,
	1,11,1,11,3,11,970,8,11,1,11,1,11,3,11,974,8,11,1,11,1,11,1,11,1,11,3,11,
	980,8,11,1,11,1,11,1,11,1,11,3,11,986,8,11,1,11,1,11,1,11,1,11,1,11,3,11,
	993,8,11,1,11,1,11,1,11,1,11,1,11,1,11,3,11,1001,8,11,1,11,1,11,1,11,1,
	11,3,11,1007,8,11,1,11,1,11,3,11,1011,8,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,3,11,1040,8,11,1,11,1,11,1,11,1,11,4,11,
	1046,8,11,11,11,12,11,1047,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,3,11,1061,8,11,1,11,1,11,1,11,3,11,1066,8,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,5,11,1078,8,11,10,11,12,11,1081,9,11,1,
	11,1,11,3,11,1085,8,11,1,11,1,11,1,11,3,11,1090,8,11,1,11,1,11,1,11,1,11,
	1,11,1,11,1,11,1,11,1,11,3,11,1101,8,11,1,11,3,11,1104,8,11,1,11,1,11,3,
	11,1108,8,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,3,11,1119,8,11,
	1,11,3,11,1122,8,11,1,11,1,11,3,11,1126,8,11,1,11,1,11,3,11,1130,8,11,1,
	11,1,11,3,11,1134,8,11,1,11,1,11,1,11,1,11,3,11,1140,8,11,1,11,1,11,1,11,
	1,11,3,11,1146,8,11,1,11,3,11,1149,8,11,1,11,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,5,11,
	1171,8,11,10,11,12,11,1174,9,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,5,11,1186,8,11,10,11,12,11,1189,9,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,5,
	11,1210,8,11,10,11,12,11,1213,9,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,5,11,1227,8,11,10,11,12,11,1230,9,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,5,11,1251,8,11,10,11,12,11,1254,9,11,1,11,1,11,1,11,1,11,1,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,5,11,1268,8,11,10,11,12,11,1271,9,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,3,11,1280,8,11,1,11,1,11,3,11,1284,8,
	11,1,12,1,12,1,12,1,12,4,12,1290,8,12,11,12,12,12,1291,1,12,1,12,1,13,1,
	13,3,13,1298,8,13,1,14,1,14,1,14,1,14,3,14,1304,8,14,1,14,1,14,1,14,1,14,
	1,14,1,14,1,14,5,14,1313,8,14,10,14,12,14,1316,9,14,1,14,1,14,3,14,1320,
	8,14,3,14,1322,8,14,1,15,1,15,1,15,1,15,1,15,1,15,3,15,1330,8,15,1,16,1,
	16,1,16,1,16,3,16,1336,8,16,1,17,1,17,1,17,1,17,3,17,1342,8,17,1,17,1,17,
	1,17,1,17,4,17,1348,8,17,11,17,12,17,1349,1,17,1,17,1,18,1,18,1,18,1,18,
	1,18,3,18,1359,8,18,1,19,1,19,1,19,1,19,1,19,3,19,1366,8,19,1,19,1,19,1,
	19,3,19,1371,8,19,1,19,1,19,5,19,1375,8,19,10,19,12,19,1378,9,19,1,19,1,
	19,1,19,3,19,1383,8,19,1,19,1,19,3,19,1387,8,19,1,19,1,19,1,19,1,19,1,19,
	3,19,1394,8,19,1,19,1,19,5,19,1398,8,19,10,19,12,19,1401,9,19,1,19,1,19,
	1,19,3,19,1406,8,19,1,19,1,19,3,19,1410,8,19,1,19,1,19,1,19,3,19,1415,8,
	19,1,19,1,19,1,19,1,19,3,19,1421,8,19,1,19,1,19,5,19,1425,8,19,10,19,12,
	19,1428,9,19,1,19,1,19,1,19,3,19,1433,8,19,1,19,1,19,3,19,1437,8,19,1,19,
	1,19,3,19,1441,8,19,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,
	20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1465,8,20,
	1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1475,8,20,1,20,1,20,1,20,1,
	20,1,20,1,20,1,20,1,20,3,20,1485,8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,
	1,20,3,20,1495,8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1506,
	8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1517,8,20,1,20,1,
	20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,
	1534,8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,4,
	20,1548,8,20,11,20,12,20,1549,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,
	20,1,20,1,20,1,20,1,20,5,20,1565,8,20,10,20,12,20,1568,9,20,1,20,1,20,1,
	20,1,20,1,20,1,20,5,20,1576,8,20,10,20,12,20,1579,9,20,1,20,3,20,1582,8,
	20,1,20,1,20,1,20,3,20,1587,8,20,1,20,3,20,1590,8,20,1,20,1,20,1,20,4,20,
	1595,8,20,11,20,12,20,1596,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1606,
	8,20,1,20,1,20,1,20,5,20,1611,8,20,10,20,12,20,1614,9,20,1,20,1,20,1,20,
	1,20,1,20,4,20,1621,8,20,11,20,12,20,1622,1,20,1,20,1,20,1,20,1,20,1,20,
	1,20,3,20,1632,8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,4,20,1642,8,
	20,11,20,12,20,1643,1,20,1,20,1,20,1,20,1,20,1,20,1,20,3,20,1653,8,20,1,
	20,1,20,1,20,1,20,1,20,1,20,3,20,1661,8,20,1,20,1,20,1,20,1,20,1,20,1,20,
	1,20,1,20,4,20,1671,8,20,11,20,12,20,1672,1,20,1,20,1,20,1,20,1,20,1,20,
	1,20,3,20,1682,8,20,1,20,1,20,1,20,1,20,3,20,1688,8,20,1,20,1,20,1,20,3,
	20,1693,8,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,1,20,4,20,1703,8,20,11,
	20,12,20,1704,1,20,1,20,1,20,1,20,1,20,3,20,1712,8,20,1,20,1,20,1,20,3,
	20,1717,8,20,1,20,1,20,1,20,1,20,1,20,1,20,4,20,1725,8,20,11,20,12,20,1726,
	1,20,1,20,1,20,1,20,1,20,3,20,1734,8,20,1,21,1,21,1,21,1,21,1,21,1,21,1,
	21,1,21,1,21,1,21,1,21,5,21,1747,8,21,10,21,12,21,1750,9,21,3,21,1752,8,
	21,1,21,1,21,1,21,3,21,1757,8,21,1,21,1,21,3,21,1761,8,21,1,21,3,21,1764,
	8,21,1,22,1,22,1,22,1,22,1,22,1,22,3,22,1772,8,22,1,22,1,22,1,23,1,23,1,
	23,3,23,1779,8,23,1,24,1,24,1,24,1,24,1,24,3,24,1786,8,24,1,25,1,25,1,25,
	1,25,1,26,1,26,5,26,1794,8,26,10,26,12,26,1797,9,26,1,26,1,26,1,27,1,27,
	1,27,3,27,1804,8,27,1,27,1,27,1,28,1,28,1,28,1,28,3,28,1812,8,28,1,29,1,
	29,1,29,1,29,1,29,1,29,5,29,1820,8,29,10,29,12,29,1823,9,29,1,29,1,29,1,
	29,0,2,6,16,30,0,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,
	42,44,46,48,50,52,54,56,58,0,16,1,0,19,20,2,0,25,25,35,38,1,0,39,41,2,0,
	38,38,42,42,2,0,23,24,47,47,2,0,12,13,48,49,1,0,52,55,1,0,56,57,1,0,58,
	59,1,0,60,61,1,0,23,24,2,0,67,67,76,76,1,0,78,79,2,0,15,15,82,91,2,0,79,
	79,81,81,1,0,109,110,2155,0,64,1,0,0,0,2,71,1,0,0,0,4,73,1,0,0,0,6,483,
	1,0,0,0,8,672,1,0,0,0,10,690,1,0,0,0,12,696,1,0,0,0,14,698,1,0,0,0,16,812,
	1,0,0,0,18,852,1,0,0,0,20,890,1,0,0,0,22,1283,1,0,0,0,24,1285,1,0,0,0,26,
	1297,1,0,0,0,28,1321,1,0,0,0,30,1329,1,0,0,0,32,1331,1,0,0,0,34,1337,1,
	0,0,0,36,1353,1,0,0,0,38,1440,1,0,0,0,40,1733,1,0,0,0,42,1763,1,0,0,0,44,
	1765,1,0,0,0,46,1778,1,0,0,0,48,1780,1,0,0,0,50,1787,1,0,0,0,52,1791,1,
	0,0,0,54,1800,1,0,0,0,56,1811,1,0,0,0,58,1821,1,0,0,0,60,65,5,121,0,0,61,
	65,5,120,0,0,62,65,5,122,0,0,63,65,5,123,0,0,64,60,1,0,0,0,64,61,1,0,0,
	0,64,62,1,0,0,0,64,63,1,0,0,0,65,1,1,0,0,0,66,72,3,0,0,0,67,72,5,117,0,
	0,68,72,5,125,0,0,69,72,5,124,0,0,70,72,5,119,0,0,71,66,1,0,0,0,71,67,1,
	0,0,0,71,68,1,0,0,0,71,69,1,0,0,0,71,70,1,0,0,0,72,3,1,0,0,0,73,75,5,132,
	0,0,74,76,3,22,11,0,75,74,1,0,0,0,76,77,1,0,0,0,77,75,1,0,0,0,77,78,1,0,
	0,0,78,79,1,0,0,0,79,80,5,133,0,0,80,5,1,0,0,0,81,82,6,3,-1,0,82,484,5,
	1,0,0,83,484,5,2,0,0,84,484,5,3,0,0,85,86,5,4,0,0,86,91,3,6,3,0,87,88,5,
	5,0,0,88,90,3,6,3,0,89,87,1,0,0,0,90,93,1,0,0,0,91,89,1,0,0,0,91,92,1,0,
	0,0,92,94,1,0,0,0,93,91,1,0,0,0,94,95,5,6,0,0,95,484,1,0,0,0,96,97,5,4,
	0,0,97,98,5,132,0,0,98,104,3,6,3,0,99,100,5,5,0,0,100,101,5,116,0,0,101,
	103,3,6,3,0,102,99,1,0,0,0,103,106,1,0,0,0,104,102,1,0,0,0,104,105,1,0,
	0,0,105,107,1,0,0,0,106,104,1,0,0,0,107,108,5,116,0,0,108,109,5,133,0,0,
	109,110,5,6,0,0,110,484,1,0,0,0,111,112,3,2,1,0,112,113,5,118,0,0,113,484,
	1,0,0,0,114,484,3,2,1,0,115,116,5,7,0,0,116,484,3,16,8,0,117,118,5,8,0,
	0,118,484,3,16,8,0,119,120,5,9,0,0,120,121,5,4,0,0,121,122,3,16,8,0,122,
	123,5,5,0,0,123,124,5,118,0,0,124,125,5,6,0,0,125,484,1,0,0,0,126,129,5,
	10,0,0,127,128,5,118,0,0,128,130,5,11,0,0,129,127,1,0,0,0,129,130,1,0,0,
	0,130,131,1,0,0,0,131,484,5,118,0,0,132,133,5,118,0,0,133,134,5,4,0,0,134,
	484,5,6,0,0,135,136,5,118,0,0,136,137,5,4,0,0,137,142,3,8,4,0,138,139,5,
	5,0,0,139,141,3,8,4,0,140,138,1,0,0,0,141,144,1,0,0,0,142,140,1,0,0,0,142,
	143,1,0,0,0,143,145,1,0,0,0,144,142,1,0,0,0,145,146,5,6,0,0,146,484,1,0,
	0,0,147,148,5,118,0,0,148,149,5,4,0,0,149,150,5,132,0,0,150,156,3,8,4,0,
	151,152,5,5,0,0,152,153,5,116,0,0,153,155,3,8,4,0,154,151,1,0,0,0,155,158,
	1,0,0,0,156,154,1,0,0,0,156,157,1,0,0,0,157,159,1,0,0,0,158,156,1,0,0,0,
	159,160,5,116,0,0,160,161,5,133,0,0,161,162,5,6,0,0,162,484,1,0,0,0,163,
	164,5,118,0,0,164,166,5,11,0,0,165,163,1,0,0,0,165,166,1,0,0,0,166,167,
	1,0,0,0,167,179,5,118,0,0,168,169,5,12,0,0,169,174,3,16,8,0,170,171,5,5,
	0,0,171,173,3,16,8,0,172,170,1,0,0,0,173,176,1,0,0,0,174,172,1,0,0,0,174,
	175,1,0,0,0,175,177,1,0,0,0,176,174,1,0,0,0,177,178,5,13,0,0,178,180,1,
	0,0,0,179,168,1,0,0,0,179,180,1,0,0,0,180,182,1,0,0,0,181,165,1,0,0,0,181,
	182,1,0,0,0,182,183,1,0,0,0,183,184,5,14,0,0,184,185,5,118,0,0,185,186,
	5,15,0,0,186,193,3,6,3,0,187,188,5,5,0,0,188,189,5,118,0,0,189,190,5,15,
	0,0,190,192,3,6,3,0,191,187,1,0,0,0,192,195,1,0,0,0,193,191,1,0,0,0,193,
	194,1,0,0,0,194,196,1,0,0,0,195,193,1,0,0,0,196,197,5,16,0,0,197,484,1,
	0,0,0,198,199,5,118,0,0,199,201,5,11,0,0,200,198,1,0,0,0,200,201,1,0,0,
	0,201,202,1,0,0,0,202,214,5,118,0,0,203,204,5,12,0,0,204,209,3,16,8,0,205,
	206,5,5,0,0,206,208,3,16,8,0,207,205,1,0,0,0,208,211,1,0,0,0,209,207,1,
	0,0,0,209,210,1,0,0,0,210,212,1,0,0,0,211,209,1,0,0,0,212,213,5,13,0,0,
	213,215,1,0,0,0,214,203,1,0,0,0,214,215,1,0,0,0,215,217,1,0,0,0,216,200,
	1,0,0,0,216,217,1,0,0,0,217,218,1,0,0,0,218,219,5,14,0,0,219,220,5,132,
	0,0,220,221,5,118,0,0,221,222,5,15,0,0,222,230,3,6,3,0,223,224,5,5,0,0,
	224,225,5,116,0,0,225,226,5,118,0,0,226,227,5,15,0,0,227,229,3,6,3,0,228,
	223,1,0,0,0,229,232,1,0,0,0,230,228,1,0,0,0,230,231,1,0,0,0,231,233,1,0,
	0,0,232,230,1,0,0,0,233,234,5,116,0,0,234,235,5,133,0,0,235,236,5,16,0,
	0,236,484,1,0,0,0,237,238,5,118,0,0,238,240,5,11,0,0,239,237,1,0,0,0,239,
	240,1,0,0,0,240,241,1,0,0,0,241,253,5,118,0,0,242,243,5,12,0,0,243,248,
	3,16,8,0,244,245,5,5,0,0,245,247,3,16,8,0,246,244,1,0,0,0,247,250,1,0,0,
	0,248,246,1,0,0,0,248,249,1,0,0,0,249,251,1,0,0,0,250,248,1,0,0,0,251,252,
	5,13,0,0,252,254,1,0,0,0,253,242,1,0,0,0,253,254,1,0,0,0,254,255,1,0,0,
	0,255,256,5,14,0,0,256,484,5,16,0,0,257,258,5,118,0,0,258,260,5,11,0,0,
	259,257,1,0,0,0,259,260,1,0,0,0,260,261,1,0,0,0,261,273,5,118,0,0,262,263,
	5,12,0,0,263,268,3,16,8,0,264,265,5,5,0,0,265,267,3,16,8,0,266,264,1,0,
	0,0,267,270,1,0,0,0,268,266,1,0,0,0,268,269,1,0,0,0,269,271,1,0,0,0,270,
	268,1,0,0,0,271,272,5,13,0,0,272,274,1,0,0,0,273,262,1,0,0,0,273,274,1,
	0,0,0,274,276,1,0,0,0,275,259,1,0,0,0,275,276,1,0,0,0,276,277,1,0,0,0,277,
	278,5,14,0,0,278,283,3,6,3,0,279,280,5,5,0,0,280,282,3,6,3,0,281,279,1,
	0,0,0,282,285,1,0,0,0,283,281,1,0,0,0,283,284,1,0,0,0,284,286,1,0,0,0,285,
	283,1,0,0,0,286,287,5,16,0,0,287,484,1,0,0,0,288,289,5,118,0,0,289,291,
	5,11,0,0,290,288,1,0,0,0,290,291,1,0,0,0,291,292,1,0,0,0,292,304,5,118,
	0,0,293,294,5,12,0,0,294,299,3,16,8,0,295,296,5,5,0,0,296,298,3,16,8,0,
	297,295,1,0,0,0,298,301,1,0,0,0,299,297,1,0,0,0,299,300,1,0,0,0,300,302,
	1,0,0,0,301,299,1,0,0,0,302,303,5,13,0,0,303,305,1,0,0,0,304,293,1,0,0,
	0,304,305,1,0,0,0,305,307,1,0,0,0,306,290,1,0,0,0,306,307,1,0,0,0,307,308,
	1,0,0,0,308,309,5,14,0,0,309,310,5,132,0,0,310,316,3,6,3,0,311,312,5,5,
	0,0,312,313,5,116,0,0,313,315,3,6,3,0,314,311,1,0,0,0,315,318,1,0,0,0,316,
	314,1,0,0,0,316,317,1,0,0,0,317,319,1,0,0,0,318,316,1,0,0,0,319,320,5,116,
	0,0,320,321,5,133,0,0,321,322,5,16,0,0,322,484,1,0,0,0,323,324,5,118,0,
	0,324,326,5,11,0,0,325,323,1,0,0,0,325,326,1,0,0,0,326,327,1,0,0,0,327,
	339,5,118,0,0,328,329,5,12,0,0,329,334,3,16,8,0,330,331,5,5,0,0,331,333,
	3,16,8,0,332,330,1,0,0,0,333,336,1,0,0,0,334,332,1,0,0,0,334,335,1,0,0,
	0,335,337,1,0,0,0,336,334,1,0,0,0,337,338,5,13,0,0,338,340,1,0,0,0,339,
	328,1,0,0,0,339,340,1,0,0,0,340,342,1,0,0,0,341,325,1,0,0,0,341,342,1,0,
	0,0,342,343,1,0,0,0,343,344,5,14,0,0,344,345,5,17,0,0,345,352,3,6,3,0,346,
	347,5,5,0,0,347,348,5,118,0,0,348,349,5,15,0,0,349,351,3,6,3,0,350,346,
	1,0,0,0,351,354,1,0,0,0,352,350,1,0,0,0,352,353,1,0,0,0,353,355,1,0,0,0,
	354,352,1,0,0,0,355,356,5,16,0,0,356,484,1,0,0,0,357,358,5,118,0,0,358,
	360,5,11,0,0,359,357,1,0,0,0,359,360,1,0,0,0,360,361,1,0,0,0,361,373,5,
	118,0,0,362,363,5,12,0,0,363,368,3,16,8,0,364,365,5,5,0,0,365,367,3,16,
	8,0,366,364,1,0,0,0,367,370,1,0,0,0,368,366,1,0,0,0,368,369,1,0,0,0,369,
	371,1,0,0,0,370,368,1,0,0,0,371,372,5,13,0,0,372,374,1,0,0,0,373,362,1,
	0,0,0,373,374,1,0,0,0,374,376,1,0,0,0,375,359,1,0,0,0,375,376,1,0,0,0,376,
	377,1,0,0,0,377,378,5,14,0,0,378,379,5,132,0,0,379,380,5,17,0,0,380,388,
	3,6,3,0,381,382,5,5,0,0,382,383,5,116,0,0,383,384,5,118,0,0,384,385,5,15,
	0,0,385,387,3,6,3,0,386,381,1,0,0,0,387,390,1,0,0,0,388,386,1,0,0,0,388,
	389,1,0,0,0,389,391,1,0,0,0,390,388,1,0,0,0,391,392,5,116,0,0,392,393,5,
	133,0,0,393,394,5,16,0,0,394,484,1,0,0,0,395,484,5,118,0,0,396,397,5,18,
	0,0,397,402,3,6,3,0,398,399,5,5,0,0,399,401,3,6,3,0,400,398,1,0,0,0,401,
	404,1,0,0,0,402,400,1,0,0,0,402,403,1,0,0,0,403,405,1,0,0,0,404,402,1,0,
	0,0,405,406,7,0,0,0,406,484,1,0,0,0,407,408,5,18,0,0,408,409,5,132,0,0,
	409,415,3,6,3,0,410,411,5,5,0,0,411,412,5,116,0,0,412,414,3,6,3,0,413,410,
	1,0,0,0,414,417,1,0,0,0,415,413,1,0,0,0,415,416,1,0,0,0,416,418,1,0,0,0,
	417,415,1,0,0,0,418,419,5,116,0,0,419,420,5,133,0,0,420,421,7,0,0,0,421,
	484,1,0,0,0,422,423,5,29,0,0,423,484,3,6,3,30,424,425,5,30,0,0,425,484,
	3,6,3,29,426,427,5,31,0,0,427,484,3,6,3,28,428,429,5,32,0,0,429,430,3,6,
	3,0,430,431,5,33,0,0,431,432,3,6,3,27,432,484,1,0,0,0,433,434,5,32,0,0,
	434,484,3,6,3,26,435,436,5,32,0,0,436,437,5,4,0,0,437,438,3,16,8,0,438,
	439,5,6,0,0,439,440,5,18,0,0,440,441,3,6,3,0,441,444,5,20,0,0,442,443,5,
	33,0,0,443,445,3,6,3,0,444,442,1,0,0,0,444,445,1,0,0,0,445,484,1,0,0,0,
	446,447,7,1,0,0,447,484,3,6,3,23,448,449,5,63,0,0,449,450,3,6,3,0,450,451,
	5,64,0,0,451,452,3,6,3,0,452,453,5,65,0,0,453,454,3,6,3,5,454,484,1,0,0,
	0,455,456,5,63,0,0,456,457,5,4,0,0,457,458,3,6,3,0,458,459,5,6,0,0,459,
	460,3,6,3,0,460,461,5,65,0,0,461,462,3,6,3,4,462,484,1,0,0,0,463,484,3,
	34,17,0,464,465,5,10,0,0,465,474,5,4,0,0,466,471,3,14,7,0,467,468,5,5,0,
	0,468,470,3,14,7,0,469,467,1,0,0,0,470,473,1,0,0,0,471,469,1,0,0,0,471,
	472,1,0,0,0,472,475,1,0,0,0,473,471,1,0,0,0,474,466,1,0,0,0,474,475,1,0,
	0,0,475,476,1,0,0,0,476,479,5,6,0,0,477,478,5,67,0,0,478,480,3,16,8,0,479,
	477,1,0,0,0,479,480,1,0,0,0,480,481,1,0,0,0,481,482,5,15,0,0,482,484,3,
	6,3,1,483,81,1,0,0,0,483,83,1,0,0,0,483,84,1,0,0,0,483,85,1,0,0,0,483,96,
	1,0,0,0,483,111,1,0,0,0,483,114,1,0,0,0,483,115,1,0,0,0,483,117,1,0,0,0,
	483,119,1,0,0,0,483,126,1,0,0,0,483,132,1,0,0,0,483,135,1,0,0,0,483,147,
	1,0,0,0,483,181,1,0,0,0,483,216,1,0,0,0,483,239,1,0,0,0,483,275,1,0,0,0,
	483,306,1,0,0,0,483,341,1,0,0,0,483,375,1,0,0,0,483,395,1,0,0,0,483,396,
	1,0,0,0,483,407,1,0,0,0,483,422,1,0,0,0,483,424,1,0,0,0,483,426,1,0,0,0,
	483,428,1,0,0,0,483,433,1,0,0,0,483,435,1,0,0,0,483,446,1,0,0,0,483,448,
	1,0,0,0,483,455,1,0,0,0,483,463,1,0,0,0,483,464,1,0,0,0,484,667,1,0,0,0,
	485,486,10,24,0,0,486,487,5,34,0,0,487,666,3,6,3,24,488,489,10,22,0,0,489,
	490,7,2,0,0,490,666,3,6,3,23,491,492,10,21,0,0,492,493,7,3,0,0,493,666,
	3,6,3,22,494,498,10,20,0,0,495,499,5,43,0,0,496,497,5,13,0,0,497,499,5,
	13,0,0,498,495,1,0,0,0,498,496,1,0,0,0,499,500,1,0,0,0,500,666,3,6,3,21,
	501,502,10,19,0,0,502,503,5,44,0,0,503,666,3,6,3,20,504,505,10,18,0,0,505,
	506,5,45,0,0,506,666,3,6,3,19,507,508,10,17,0,0,508,509,5,46,0,0,509,666,
	3,6,3,18,510,511,10,16,0,0,511,512,7,4,0,0,512,666,3,6,3,17,513,514,10,
	15,0,0,514,515,7,5,0,0,515,666,3,6,3,16,516,517,10,11,0,0,517,518,7,6,0,
	0,518,666,3,6,3,12,519,520,10,10,0,0,520,521,7,7,0,0,521,666,3,6,3,11,522,
	523,10,9,0,0,523,524,7,8,0,0,524,666,3,6,3,10,525,526,10,8,0,0,526,527,
	7,9,0,0,527,666,3,6,3,9,528,529,10,7,0,0,529,530,5,62,0,0,530,666,3,6,3,
	7,531,532,10,3,0,0,532,533,5,66,0,0,533,666,3,6,3,4,534,535,10,46,0,0,535,
	536,5,11,0,0,536,537,5,118,0,0,537,538,5,4,0,0,538,666,5,6,0,0,539,540,
	10,45,0,0,540,541,5,11,0,0,541,542,5,118,0,0,542,543,5,4,0,0,543,548,3,
	8,4,0,544,545,5,5,0,0,545,547,3,8,4,0,546,544,1,0,0,0,547,550,1,0,0,0,548,
	546,1,0,0,0,548,549,1,0,0,0,549,551,1,0,0,0,550,548,1,0,0,0,551,552,5,6,
	0,0,552,666,1,0,0,0,553,554,10,44,0,0,554,555,5,11,0,0,555,556,5,118,0,
	0,556,557,5,4,0,0,557,558,5,132,0,0,558,564,3,8,4,0,559,560,5,5,0,0,560,
	561,5,116,0,0,561,563,3,8,4,0,562,559,1,0,0,0,563,566,1,0,0,0,564,562,1,
	0,0,0,564,565,1,0,0,0,565,567,1,0,0,0,566,564,1,0,0,0,567,568,5,116,0,0,
	568,569,5,133,0,0,569,570,5,6,0,0,570,666,1,0,0,0,571,572,10,43,0,0,572,
	573,5,21,0,0,573,574,5,118,0,0,574,575,5,4,0,0,575,666,5,6,0,0,576,577,
	10,42,0,0,577,578,5,21,0,0,578,579,5,118,0,0,579,580,5,4,0,0,580,585,3,
	8,4,0,581,582,5,5,0,0,582,584,3,8,4,0,583,581,1,0,0,0,584,587,1,0,0,0,585,
	583,1,0,0,0,585,586,1,0,0,0,586,588,1,0,0,0,587,585,1,0,0,0,588,589,5,6,
	0,0,589,666,1,0,0,0,590,591,10,41,0,0,591,592,5,21,0,0,592,593,5,118,0,
	0,593,594,5,4,0,0,594,595,5,132,0,0,595,601,3,8,4,0,596,597,5,5,0,0,597,
	598,5,116,0,0,598,600,3,8,4,0,599,596,1,0,0,0,600,603,1,0,0,0,601,599,1,
	0,0,0,601,602,1,0,0,0,602,604,1,0,0,0,603,601,1,0,0,0,604,605,5,116,0,0,
	605,606,5,133,0,0,606,607,5,6,0,0,607,666,1,0,0,0,608,609,10,40,0,0,609,
	610,5,11,0,0,610,666,5,118,0,0,611,612,10,39,0,0,612,613,5,21,0,0,613,666,
	5,118,0,0,614,615,10,38,0,0,615,616,5,18,0,0,616,617,3,6,3,0,617,618,5,
	20,0,0,618,666,1,0,0,0,619,620,10,37,0,0,620,621,5,18,0,0,621,622,5,132,
	0,0,622,623,3,6,3,0,623,624,5,116,0,0,624,625,5,133,0,0,625,626,5,20,0,
	0,626,666,1,0,0,0,627,628,10,36,0,0,628,630,5,18,0,0,629,631,3,6,3,0,630,
	629,1,0,0,0,630,631,1,0,0,0,631,632,1,0,0,0,632,633,5,22,0,0,633,666,5,
	20,0,0,634,635,10,35,0,0,635,636,5,18,0,0,636,637,7,10,0,0,637,638,3,6,
	3,0,638,639,5,20,0,0,639,666,1,0,0,0,640,641,10,34,0,0,641,666,5,25,0,0,
	642,643,10,33,0,0,643,644,5,26,0,0,644,666,3,16,8,0,645,646,10,32,0,0,646,
	647,5,26,0,0,647,648,5,27,0,0,648,666,3,16,8,0,649,650,10,31,0,0,650,651,
	5,28,0,0,651,666,3,16,8,0,652,653,10,14,0,0,653,654,5,50,0,0,654,666,3,
	16,8,0,655,656,10,13,0,0,656,657,5,51,0,0,657,666,3,16,8,0,658,659,10,12,
	0,0,659,660,5,50,0,0,660,661,5,35,0,0,661,666,3,16,8,0,662,663,10,6,0,0,
	663,664,5,62,0,0,664,666,3,10,5,0,665,485,1,0,0,0,665,488,1,0,0,0,665,491,
	1,0,0,0,665,494,1,0,0,0,665,501,1,0,0,0,665,504,1,0,0,0,665,507,1,0,0,0,
	665,510,1,0,0,0,665,513,1,0,0,0,665,516,1,0,0,0,665,519,1,0,0,0,665,522,
	1,0,0,0,665,525,1,0,0,0,665,528,1,0,0,0,665,531,1,0,0,0,665,534,1,0,0,0,
	665,539,1,0,0,0,665,553,1,0,0,0,665,571,1,0,0,0,665,576,1,0,0,0,665,590,
	1,0,0,0,665,608,1,0,0,0,665,611,1,0,0,0,665,614,1,0,0,0,665,619,1,0,0,0,
	665,627,1,0,0,0,665,634,1,0,0,0,665,640,1,0,0,0,665,642,1,0,0,0,665,645,
	1,0,0,0,665,649,1,0,0,0,665,652,1,0,0,0,665,655,1,0,0,0,665,658,1,0,0,0,
	665,662,1,0,0,0,666,669,1,0,0,0,667,665,1,0,0,0,667,668,1,0,0,0,668,7,1,
	0,0,0,669,667,1,0,0,0,670,671,5,118,0,0,671,673,5,15,0,0,672,670,1,0,0,
	0,672,673,1,0,0,0,673,674,1,0,0,0,674,675,3,6,3,0,675,9,1,0,0,0,676,678,
	5,68,0,0,677,679,3,6,3,0,678,677,1,0,0,0,678,679,1,0,0,0,679,691,1,0,0,
	0,680,681,5,69,0,0,681,691,3,6,3,0,682,684,5,70,0,0,683,685,5,118,0,0,684,
	683,1,0,0,0,684,685,1,0,0,0,685,691,1,0,0,0,686,688,5,71,0,0,687,689,5,
	118,0,0,688,687,1,0,0,0,688,689,1,0,0,0,689,691,1,0,0,0,690,676,1,0,0,0,
	690,680,1,0,0,0,690,682,1,0,0,0,690,686,1,0,0,0,691,11,1,0,0,0,692,693,
	3,6,3,0,693,694,5,116,0,0,694,697,1,0,0,0,695,697,3,34,17,0,696,692,1,0,
	0,0,696,695,1,0,0,0,697,13,1,0,0,0,698,701,5,118,0,0,699,700,5,67,0,0,700,
	702,3,16,8,0,701,699,1,0,0,0,701,702,1,0,0,0,702,15,1,0,0,0,703,706,6,8,
	-1,0,704,705,5,118,0,0,705,707,5,11,0,0,706,704,1,0,0,0,706,707,1,0,0,0,
	707,708,1,0,0,0,708,709,5,118,0,0,709,710,5,12,0,0,710,715,3,16,8,0,711,
	712,5,5,0,0,712,714,3,16,8,0,713,711,1,0,0,0,714,717,1,0,0,0,715,713,1,
	0,0,0,715,716,1,0,0,0,716,718,1,0,0,0,717,715,1,0,0,0,718,719,5,13,0,0,
	719,813,1,0,0,0,720,721,5,118,0,0,721,722,5,11,0,0,722,813,5,118,0,0,723,
	813,5,118,0,0,724,725,5,29,0,0,725,813,3,16,8,17,726,727,5,73,0,0,727,728,
	3,16,8,0,728,729,5,46,0,0,729,734,3,16,8,0,730,731,5,46,0,0,731,733,3,16,
	8,0,732,730,1,0,0,0,733,736,1,0,0,0,734,732,1,0,0,0,734,735,1,0,0,0,735,
	813,1,0,0,0,736,734,1,0,0,0,737,738,5,4,0,0,738,739,3,16,8,0,739,740,5,
	6,0,0,740,813,1,0,0,0,741,813,5,119,0,0,742,743,5,12,0,0,743,744,5,118,
	0,0,744,813,5,13,0,0,745,746,5,74,0,0,746,813,3,6,3,0,747,748,5,10,0,0,
	748,749,5,4,0,0,749,752,5,6,0,0,750,751,7,11,0,0,751,753,3,16,8,0,752,750,
	1,0,0,0,752,753,1,0,0,0,753,813,1,0,0,0,754,755,5,10,0,0,755,758,5,4,0,
	0,756,757,5,118,0,0,757,759,5,67,0,0,758,756,1,0,0,0,758,759,1,0,0,0,759,
	760,1,0,0,0,760,769,3,16,8,0,761,764,5,5,0,0,762,763,5,118,0,0,763,765,
	5,67,0,0,764,762,1,0,0,0,764,765,1,0,0,0,765,766,1,0,0,0,766,768,3,16,8,
	0,767,761,1,0,0,0,768,771,1,0,0,0,769,767,1,0,0,0,769,770,1,0,0,0,770,772,
	1,0,0,0,771,769,1,0,0,0,772,775,5,6,0,0,773,774,7,11,0,0,774,776,3,16,8,
	0,775,773,1,0,0,0,775,776,1,0,0,0,776,813,1,0,0,0,777,778,5,4,0,0,778,779,
	5,6,0,0,779,780,5,76,0,0,780,813,3,16,8,3,781,784,5,4,0,0,782,783,5,118,
	0,0,783,785,5,67,0,0,784,782,1,0,0,0,784,785,1,0,0,0,785,786,1,0,0,0,786,
	795,3,16,8,0,787,790,5,5,0,0,788,789,5,118,0,0,789,791,5,67,0,0,790,788,
	1,0,0,0,790,791,1,0,0,0,791,792,1,0,0,0,792,794,3,16,8,0,793,787,1,0,0,
	0,794,797,1,0,0,0,795,793,1,0,0,0,795,796,1,0,0,0,796,798,1,0,0,0,797,795,
	1,0,0,0,798,799,5,6,0,0,799,800,5,76,0,0,800,801,3,16,8,2,801,813,1,0,0,
	0,802,803,5,4,0,0,803,806,3,16,8,0,804,805,5,5,0,0,805,807,3,16,8,0,806,
	804,1,0,0,0,807,808,1,0,0,0,808,806,1,0,0,0,808,809,1,0,0,0,809,810,1,0,
	0,0,810,811,5,6,0,0,811,813,1,0,0,0,812,703,1,0,0,0,812,720,1,0,0,0,812,
	723,1,0,0,0,812,724,1,0,0,0,812,726,1,0,0,0,812,737,1,0,0,0,812,741,1,0,
	0,0,812,742,1,0,0,0,812,745,1,0,0,0,812,747,1,0,0,0,812,754,1,0,0,0,812,
	777,1,0,0,0,812,781,1,0,0,0,812,802,1,0,0,0,813,849,1,0,0,0,814,815,10,
	6,0,0,815,816,5,75,0,0,816,848,3,16,8,7,817,818,10,16,0,0,818,848,5,72,
	0,0,819,820,10,15,0,0,820,821,5,44,0,0,821,826,3,16,8,0,822,823,5,44,0,
	0,823,825,3,16,8,0,824,822,1,0,0,0,825,828,1,0,0,0,826,824,1,0,0,0,826,
	827,1,0,0,0,827,848,1,0,0,0,828,826,1,0,0,0,829,830,10,14,0,0,830,831,5,
	46,0,0,831,836,3,16,8,0,832,833,5,46,0,0,833,835,3,16,8,0,834,832,1,0,0,
	0,835,838,1,0,0,0,836,834,1,0,0,0,836,837,1,0,0,0,837,848,1,0,0,0,838,836,
	1,0,0,0,839,840,10,8,0,0,840,841,5,18,0,0,841,848,7,0,0,0,842,843,10,7,
	0,0,843,844,5,18,0,0,844,845,3,6,3,0,845,846,5,20,0,0,846,848,1,0,0,0,847,
	814,1,0,0,0,847,817,1,0,0,0,847,819,1,0,0,0,847,829,1,0,0,0,847,839,1,0,
	0,0,847,842,1,0,0,0,848,851,1,0,0,0,849,847,1,0,0,0,849,850,1,0,0,0,850,
	17,1,0,0,0,851,849,1,0,0,0,852,853,5,77,0,0,853,854,5,118,0,0,854,855,5,
	26,0,0,855,861,5,132,0,0,856,857,5,118,0,0,857,858,5,67,0,0,858,859,3,16,
	8,0,859,860,5,116,0,0,860,862,1,0,0,0,861,856,1,0,0,0,862,863,1,0,0,0,863,
	861,1,0,0,0,863,864,1,0,0,0,864,865,1,0,0,0,865,866,5,133,0,0,866,19,1,
	0,0,0,867,868,5,118,0,0,868,869,5,67,0,0,869,891,3,16,8,0,870,871,5,118,
	0,0,871,872,5,67,0,0,872,873,3,16,8,0,873,874,5,15,0,0,874,875,3,6,3,0,
	875,891,1,0,0,0,876,877,5,118,0,0,877,878,5,15,0,0,878,891,3,6,3,0,879,
	880,5,17,0,0,880,881,5,118,0,0,881,882,5,67,0,0,882,891,3,16,8,0,883,891,
	5,17,0,0,884,885,5,78,0,0,885,891,3,20,10,0,886,887,5,79,0,0,887,891,3,
	20,10,0,888,889,5,80,0,0,889,891,3,20,10,0,890,867,1,0,0,0,890,870,1,0,
	0,0,890,876,1,0,0,0,890,879,1,0,0,0,890,883,1,0,0,0,890,884,1,0,0,0,890,
	886,1,0,0,0,890,888,1,0,0,0,891,21,1,0,0,0,892,893,5,78,0,0,893,894,5,118,
	0,0,894,895,5,15,0,0,895,1284,3,12,6,0,896,897,5,79,0,0,897,898,5,118,0,
	0,898,899,5,15,0,0,899,1284,3,12,6,0,900,901,5,80,0,0,901,902,5,118,0,0,
	902,903,5,15,0,0,903,1284,3,12,6,0,904,905,5,81,0,0,905,906,5,118,0,0,906,
	907,5,15,0,0,907,908,3,6,3,0,908,909,5,116,0,0,909,1284,1,0,0,0,910,911,
	5,78,0,0,911,912,5,118,0,0,912,913,5,67,0,0,913,914,3,16,8,0,914,915,5,
	15,0,0,915,916,3,12,6,0,916,1284,1,0,0,0,917,918,5,79,0,0,918,919,5,118,
	0,0,919,920,5,67,0,0,920,921,3,16,8,0,921,922,5,15,0,0,922,923,3,12,6,0,
	923,1284,1,0,0,0,924,925,5,80,0,0,925,926,5,118,0,0,926,927,5,67,0,0,927,
	928,3,16,8,0,928,929,5,15,0,0,929,930,3,12,6,0,930,1284,1,0,0,0,931,932,
	5,81,0,0,932,933,5,118,0,0,933,934,5,67,0,0,934,935,3,16,8,0,935,936,5,
	15,0,0,936,937,3,6,3,0,937,938,5,116,0,0,938,1284,1,0,0,0,939,940,5,78,
	0,0,940,941,5,118,0,0,941,942,5,67,0,0,942,943,3,16,8,0,943,944,5,116,0,
	0,944,1284,1,0,0,0,945,946,7,12,0,0,946,949,3,24,12,0,947,948,5,67,0,0,
	948,950,3,16,8,0,949,947,1,0,0,0,949,950,1,0,0,0,950,951,1,0,0,0,951,952,
	5,15,0,0,952,953,3,12,6,0,953,1284,1,0,0,0,954,955,3,6,3,0,955,956,7,13,
	0,0,956,957,3,12,6,0,957,1284,1,0,0,0,958,960,5,70,0,0,959,961,5,118,0,
	0,960,959,1,0,0,0,960,961,1,0,0,0,961,964,1,0,0,0,962,963,5,63,0,0,963,
	965,3,6,3,0,964,962,1,0,0,0,964,965,1,0,0,0,965,966,1,0,0,0,966,1284,5,
	116,0,0,967,969,5,71,0,0,968,970,5,118,0,0,969,968,1,0,0,0,969,970,1,0,
	0,0,970,973,1,0,0,0,971,972,5,63,0,0,972,974,3,6,3,0,973,971,1,0,0,0,973,
	974,1,0,0,0,974,975,1,0,0,0,975,1284,5,116,0,0,976,979,5,92,0,0,977,978,
	5,63,0,0,978,980,3,6,3,0,979,977,1,0,0,0,979,980,1,0,0,0,980,981,1,0,0,
	0,981,1284,5,116,0,0,982,985,5,93,0,0,983,984,5,63,0,0,984,986,3,6,3,0,
	985,983,1,0,0,0,985,986,1,0,0,0,986,987,1,0,0,0,987,1284,5,116,0,0,988,
	989,5,69,0,0,989,992,3,6,3,0,990,991,5,63,0,0,991,993,3,6,3,0,992,990,1,
	0,0,0,992,993,1,0,0,0,993,994,1,0,0,0,994,995,5,116,0,0,995,1284,1,0,0,
	0,996,997,5,94,0,0,997,1000,3,6,3,0,998,999,5,5,0,0,999,1001,5,125,0,0,
	1000,998,1,0,0,0,1000,1001,1,0,0,0,1001,1002,1,0,0,0,1002,1003,5,116,0,
	0,1003,1284,1,0,0,0,1004,1006,5,68,0,0,1005,1007,3,6,3,0,1006,1005,1,0,
	0,0,1006,1007,1,0,0,0,1007,1010,1,0,0,0,1008,1009,5,63,0,0,1009,1011,3,
	6,3,0,1010,1008,1,0,0,0,1010,1011,1,0,0,0,1011,1012,1,0,0,0,1012,1284,5,
	116,0,0,1013,1014,5,68,0,0,1014,1284,3,34,17,0,1015,1016,3,6,3,0,1016,1017,
	5,63,0,0,1017,1018,3,6,3,0,1018,1019,5,116,0,0,1019,1284,1,0,0,0,1020,1021,
	3,6,3,0,1021,1022,5,95,0,0,1022,1023,3,6,3,0,1023,1024,5,116,0,0,1024,1284,
	1,0,0,0,1025,1026,5,96,0,0,1026,1284,3,22,11,0,1027,1028,5,96,0,0,1028,
	1029,5,64,0,0,1029,1284,3,4,2,0,1030,1031,5,97,0,0,1031,1284,3,22,11,0,
	1032,1033,5,97,0,0,1033,1034,5,64,0,0,1034,1284,3,4,2,0,1035,1039,5,98,
	0,0,1036,1037,7,12,0,0,1037,1038,5,118,0,0,1038,1040,5,15,0,0,1039,1036,
	1,0,0,0,1039,1040,1,0,0,0,1040,1041,1,0,0,0,1041,1042,3,6,3,0,1042,1043,
	5,64,0,0,1043,1045,5,132,0,0,1044,1046,3,32,16,0,1045,1044,1,0,0,0,1046,
	1047,1,0,0,0,1047,1045,1,0,0,0,1047,1048,1,0,0,0,1048,1049,1,0,0,0,1049,
	1050,5,133,0,0,1050,1284,1,0,0,0,1051,1052,5,63,0,0,1052,1053,7,12,0,0,
	1053,1054,5,118,0,0,1054,1055,5,15,0,0,1055,1056,3,6,3,0,1056,1057,5,64,
	0,0,1057,1065,3,4,2,0,1058,1060,5,65,0,0,1059,1061,5,64,0,0,1060,1059,1,
	0,0,0,1060,1061,1,0,0,0,1061,1062,1,0,0,0,1062,1066,3,4,2,0,1063,1064,5,
	65,0,0,1064,1066,3,22,11,0,1065,1058,1,0,0,0,1065,1063,1,0,0,0,1065,1066,
	1,0,0,0,1066,1284,1,0,0,0,1067,1068,5,63,0,0,1068,1069,3,6,3,0,1069,1070,
	5,64,0,0,1070,1079,3,4,2,0,1071,1072,5,65,0,0,1072,1073,5,63,0,0,1073,1074,
	3,6,3,0,1074,1075,5,64,0,0,1075,1076,3,4,2,0,1076,1078,1,0,0,0,1077,1071,
	1,0,0,0,1078,1081,1,0,0,0,1079,1077,1,0,0,0,1079,1080,1,0,0,0,1080,1089,
	1,0,0,0,1081,1079,1,0,0,0,1082,1084,5,65,0,0,1083,1085,5,64,0,0,1084,1083,
	1,0,0,0,1084,1085,1,0,0,0,1085,1086,1,0,0,0,1086,1090,3,4,2,0,1087,1088,
	5,65,0,0,1088,1090,3,22,11,0,1089,1082,1,0,0,0,1089,1087,1,0,0,0,1089,1090,
	1,0,0,0,1090,1284,1,0,0,0,1091,1092,5,118,0,0,1092,1093,5,67,0,0,1093,1284,
	3,22,11,0,1094,1095,5,95,0,0,1095,1096,3,6,3,0,1096,1097,5,64,0,0,1097,
	1103,3,4,2,0,1098,1100,5,65,0,0,1099,1101,5,64,0,0,1100,1099,1,0,0,0,1100,
	1101,1,0,0,0,1101,1102,1,0,0,0,1102,1104,3,4,2,0,1103,1098,1,0,0,0,1103,
	1104,1,0,0,0,1104,1284,1,0,0,0,1105,1107,5,99,0,0,1106,1108,7,12,0,0,1107,
	1106,1,0,0,0,1107,1108,1,0,0,0,1108,1109,1,0,0,0,1109,1110,5,118,0,0,1110,
	1111,5,33,0,0,1111,1112,3,6,3,0,1112,1113,5,100,0,0,1113,1114,3,6,3,0,1114,
	1115,5,64,0,0,1115,1121,3,4,2,0,1116,1118,5,65,0,0,1117,1119,5,64,0,0,1118,
	1117,1,0,0,0,1118,1119,1,0,0,0,1119,1120,1,0,0,0,1120,1122,3,4,2,0,1121,
	1116,1,0,0,0,1121,1122,1,0,0,0,1122,1284,1,0,0,0,1123,1125,5,99,0,0,1124,
	1126,7,12,0,0,1125,1124,1,0,0,0,1125,1126,1,0,0,0,1126,1129,1,0,0,0,1127,
	1128,5,118,0,0,1128,1130,5,5,0,0,1129,1127,1,0,0,0,1129,1130,1,0,0,0,1130,
	1133,1,0,0,0,1131,1134,5,118,0,0,1132,1134,3,24,12,0,1133,1131,1,0,0,0,
	1133,1132,1,0,0,0,1134,1135,1,0,0,0,1135,1136,5,33,0,0,1136,1139,3,6,3,
	0,1137,1138,5,101,0,0,1138,1140,3,6,3,0,1139,1137,1,0,0,0,1139,1140,1,0,
	0,0,1140,1141,1,0,0,0,1141,1142,5,64,0,0,1142,1148,3,4,2,0,1143,1145,5,
	65,0,0,1144,1146,5,64,0,0,1145,1144,1,0,0,0,1145,1146,1,0,0,0,1146,1147,
	1,0,0,0,1147,1149,3,4,2,0,1148,1143,1,0,0,0,1148,1149,1,0,0,0,1149,1284,
	1,0,0,0,1150,1151,5,31,0,0,1151,1152,3,6,3,0,1152,1153,5,116,0,0,1153,1284,
	1,0,0,0,1154,1155,5,31,0,0,1155,1156,3,6,3,0,1156,1157,5,102,0,0,1157,1158,
	5,118,0,0,1158,1159,5,64,0,0,1159,1160,3,4,2,0,1160,1284,1,0,0,0,1161,1162,
	5,118,0,0,1162,1163,5,4,0,0,1163,1164,5,6,0,0,1164,1284,5,116,0,0,1165,
	1166,5,118,0,0,1166,1167,5,4,0,0,1167,1172,3,8,4,0,1168,1169,5,5,0,0,1169,
	1171,3,8,4,0,1170,1168,1,0,0,0,1171,1174,1,0,0,0,1172,1170,1,0,0,0,1172,
	1173,1,0,0,0,1173,1175,1,0,0,0,1174,1172,1,0,0,0,1175,1176,5,6,0,0,1176,
	1177,5,116,0,0,1177,1284,1,0,0,0,1178,1179,5,118,0,0,1179,1180,5,4,0,0,
	1180,1181,5,132,0,0,1181,1187,3,8,4,0,1182,1183,5,5,0,0,1183,1184,5,116,
	0,0,1184,1186,3,8,4,0,1185,1182,1,0,0,0,1186,1189,1,0,0,0,1187,1185,1,0,
	0,0,1187,1188,1,0,0,0,1188,1190,1,0,0,0,1189,1187,1,0,0,0,1190,1191,5,116,
	0,0,1191,1192,5,133,0,0,1192,1193,5,6,0,0,1193,1194,5,116,0,0,1194,1284,
	1,0,0,0,1195,1196,3,6,3,0,1196,1197,5,11,0,0,1197,1198,5,118,0,0,1198,1199,
	5,4,0,0,1199,1200,5,6,0,0,1200,1201,5,116,0,0,1201,1284,1,0,0,0,1202,1203,
	3,6,3,0,1203,1204,5,11,0,0,1204,1205,5,118,0,0,1205,1206,5,4,0,0,1206,1211,
	3,8,4,0,1207,1208,5,5,0,0,1208,1210,3,8,4,0,1209,1207,1,0,0,0,1210,1213,
	1,0,0,0,1211,1209,1,0,0,0,1211,1212,1,0,0,0,1212,1214,1,0,0,0,1213,1211,
	1,0,0,0,1214,1215,5,6,0,0,1215,1216,5,116,0,0,1216,1284,1,0,0,0,1217,1218,
	3,6,3,0,1218,1219,5,11,0,0,1219,1220,5,118,0,0,1220,1221,5,4,0,0,1221,1222,
	5,132,0,0,1222,1228,3,8,4,0,1223,1224,5,5,0,0,1224,1225,5,116,0,0,1225,
	1227,3,8,4,0,1226,1223,1,0,0,0,1227,1230,1,0,0,0,1228,1226,1,0,0,0,1228,
	1229,1,0,0,0,1229,1231,1,0,0,0,1230,1228,1,0,0,0,1231,1232,5,116,0,0,1232,
	1233,5,133,0,0,1233,1234,5,6,0,0,1234,1235,5,116,0,0,1235,1284,1,0,0,0,
	1236,1237,3,6,3,0,1237,1238,5,21,0,0,1238,1239,5,118,0,0,1239,1240,5,4,
	0,0,1240,1241,5,6,0,0,1241,1242,5,116,0,0,1242,1284,1,0,0,0,1243,1244,3,
	6,3,0,1244,1245,5,21,0,0,1245,1246,5,118,0,0,1246,1247,5,4,0,0,1247,1252,
	3,8,4,0,1248,1249,5,5,0,0,1249,1251,3,8,4,0,1250,1248,1,0,0,0,1251,1254,
	1,0,0,0,1252,1250,1,0,0,0,1252,1253,1,0,0,0,1253,1255,1,0,0,0,1254,1252,
	1,0,0,0,1255,1256,5,6,0,0,1256,1257,5,116,0,0,1257,1284,1,0,0,0,1258,1259,
	3,6,3,0,1259,1260,5,21,0,0,1260,1261,5,118,0,0,1261,1262,5,4,0,0,1262,1263,
	5,132,0,0,1263,1269,3,8,4,0,1264,1265,5,5,0,0,1265,1266,5,116,0,0,1266,
	1268,3,8,4,0,1267,1264,1,0,0,0,1268,1271,1,0,0,0,1269,1267,1,0,0,0,1269,
	1270,1,0,0,0,1270,1272,1,0,0,0,1271,1269,1,0,0,0,1272,1273,5,116,0,0,1273,
	1274,5,133,0,0,1274,1275,5,6,0,0,1275,1276,5,116,0,0,1276,1284,1,0,0,0,
	1277,1279,5,118,0,0,1278,1280,5,118,0,0,1279,1278,1,0,0,0,1279,1280,1,0,
	0,0,1280,1281,1,0,0,0,1281,1282,5,64,0,0,1282,1284,3,4,2,0,1283,892,1,0,
	0,0,1283,896,1,0,0,0,1283,900,1,0,0,0,1283,904,1,0,0,0,1283,910,1,0,0,0,
	1283,917,1,0,0,0,1283,924,1,0,0,0,1283,931,1,0,0,0,1283,939,1,0,0,0,1283,
	945,1,0,0,0,1283,954,1,0,0,0,1283,958,1,0,0,0,1283,967,1,0,0,0,1283,976,
	1,0,0,0,1283,982,1,0,0,0,1283,988,1,0,0,0,1283,996,1,0,0,0,1283,1004,1,
	0,0,0,1283,1013,1,0,0,0,1283,1015,1,0,0,0,1283,1020,1,0,0,0,1283,1025,1,
	0,0,0,1283,1027,1,0,0,0,1283,1030,1,0,0,0,1283,1032,1,0,0,0,1283,1035,1,
	0,0,0,1283,1051,1,0,0,0,1283,1067,1,0,0,0,1283,1091,1,0,0,0,1283,1094,1,
	0,0,0,1283,1105,1,0,0,0,1283,1123,1,0,0,0,1283,1150,1,0,0,0,1283,1154,1,
	0,0,0,1283,1161,1,0,0,0,1283,1165,1,0,0,0,1283,1178,1,0,0,0,1283,1195,1,
	0,0,0,1283,1202,1,0,0,0,1283,1217,1,0,0,0,1283,1236,1,0,0,0,1283,1243,1,
	0,0,0,1283,1258,1,0,0,0,1283,1277,1,0,0,0,1284,23,1,0,0,0,1285,1286,5,4,
	0,0,1286,1289,3,26,13,0,1287,1288,5,5,0,0,1288,1290,3,26,13,0,1289,1287,
	1,0,0,0,1290,1291,1,0,0,0,1291,1289,1,0,0,0,1291,1292,1,0,0,0,1292,1293,
	1,0,0,0,1293,1294,5,6,0,0,1294,25,1,0,0,0,1295,1298,5,118,0,0,1296,1298,
	3,24,12,0,1297,1295,1,0,0,0,1297,1296,1,0,0,0,1298,27,1,0,0,0,1299,1300,
	5,50,0,0,1300,1303,3,16,8,0,1301,1302,5,63,0,0,1302,1304,3,6,3,0,1303,1301,
	1,0,0,0,1303,1304,1,0,0,0,1304,1322,1,0,0,0,1305,1322,5,65,0,0,1306,1307,
	5,118,0,0,1307,1308,5,63,0,0,1308,1322,3,6,3,0,1309,1314,3,30,15,0,1310,
	1311,5,5,0,0,1311,1313,3,30,15,0,1312,1310,1,0,0,0,1313,1316,1,0,0,0,1314,
	1312,1,0,0,0,1314,1315,1,0,0,0,1315,1319,1,0,0,0,1316,1314,1,0,0,0,1317,
	1318,5,63,0,0,1318,1320,3,6,3,0,1319,1317,1,0,0,0,1319,1320,1,0,0,0,1320,
	1322,1,0,0,0,1321,1299,1,0,0,0,1321,1305,1,0,0,0,1321,1306,1,0,0,0,1321,
	1309,1,0,0,0,1322,29,1,0,0,0,1323,1330,3,6,3,0,1324,1325,3,6,3,0,1325,1326,
	5,22,0,0,1326,1330,1,0,0,0,1327,1328,7,10,0,0,1328,1330,3,6,3,0,1329,1323,
	1,0,0,0,1329,1324,1,0,0,0,1329,1327,1,0,0,0,1330,31,1,0,0,0,1331,1332,3,
	28,14,0,1332,1335,5,64,0,0,1333,1336,3,4,2,0,1334,1336,3,22,11,0,1335,1333,
	1,0,0,0,1335,1334,1,0,0,0,1336,33,1,0,0,0,1337,1341,5,98,0,0,1338,1339,
	7,12,0,0,1339,1340,5,118,0,0,1340,1342,5,15,0,0,1341,1338,1,0,0,0,1341,
	1342,1,0,0,0,1342,1343,1,0,0,0,1343,1344,3,6,3,0,1344,1345,5,64,0,0,1345,
	1347,5,132,0,0,1346,1348,3,36,18,0,1347,1346,1,0,0,0,1348,1349,1,0,0,0,
	1349,1347,1,0,0,0,1349,1350,1,0,0,0,1350,1351,1,0,0,0,1351,1352,5,133,0,
	0,1352,35,1,0,0,0,1353,1354,3,28,14,0,1354,1358,5,64,0,0,1355,1359,3,12,
	6,0,1356,1359,3,22,11,0,1357,1359,3,4,2,0,1358,1355,1,0,0,0,1358,1356,1,
	0,0,0,1358,1357,1,0,0,0,1359,37,1,0,0,0,1360,1361,5,103,0,0,1361,1362,5,
	10,0,0,1362,1365,5,118,0,0,1363,1364,5,104,0,0,1364,1366,5,125,0,0,1365,
	1363,1,0,0,0,1365,1366,1,0,0,0,1366,1367,1,0,0,0,1367,1368,5,26,0,0,1368,
	1370,5,4,0,0,1369,1371,3,20,10,0,1370,1369,1,0,0,0,1370,1371,1,0,0,0,1371,
	1376,1,0,0,0,1372,1373,5,5,0,0,1373,1375,3,20,10,0,1374,1372,1,0,0,0,1375,
	1378,1,0,0,0,1376,1374,1,0,0,0,1376,1377,1,0,0,0,1377,1379,1,0,0,0,1378,
	1376,1,0,0,0,1379,1382,5,6,0,0,1380,1381,7,11,0,0,1381,1383,3,16,8,0,1382,
	1380,1,0,0,0,1382,1383,1,0,0,0,1383,1386,1,0,0,0,1384,1385,5,75,0,0,1385,
	1387,3,16,8,0,1386,1384,1,0,0,0,1386,1387,1,0,0,0,1387,1388,1,0,0,0,1388,
	1441,5,116,0,0,1389,1390,5,10,0,0,1390,1391,5,118,0,0,1391,1393,5,4,0,0,
	1392,1394,3,20,10,0,1393,1392,1,0,0,0,1393,1394,1,0,0,0,1394,1399,1,0,0,
	0,1395,1396,5,5,0,0,1396,1398,3,20,10,0,1397,1395,1,0,0,0,1398,1401,1,0,
	0,0,1399,1397,1,0,0,0,1399,1400,1,0,0,0,1400,1402,1,0,0,0,1401,1399,1,0,
	0,0,1402,1405,5,6,0,0,1403,1404,5,67,0,0,1404,1406,3,16,8,0,1405,1403,1,
	0,0,0,1405,1406,1,0,0,0,1406,1409,1,0,0,0,1407,1408,5,75,0,0,1408,1410,
	3,16,8,0,1409,1407,1,0,0,0,1409,1410,1,0,0,0,1410,1411,1,0,0,0,1411,1412,
	5,64,0,0,1412,1441,3,4,2,0,1413,1415,5,105,0,0,1414,1413,1,0,0,0,1414,1415,
	1,0,0,0,1415,1416,1,0,0,0,1416,1417,5,10,0,0,1417,1418,5,118,0,0,1418,1420,
	5,4,0,0,1419,1421,3,20,10,0,1420,1419,1,0,0,0,1420,1421,1,0,0,0,1421,1426,
	1,0,0,0,1422,1423,5,5,0,0,1423,1425,3,20,10,0,1424,1422,1,0,0,0,1425,1428,
	1,0,0,0,1426,1424,1,0,0,0,1426,1427,1,0,0,0,1427,1429,1,0,0,0,1428,1426,
	1,0,0,0,1429,1432,5,6,0,0,1430,1431,5,67,0,0,1431,1433,3,16,8,0,1432,1430,
	1,0,0,0,1432,1433,1,0,0,0,1433,1436,1,0,0,0,1434,1435,5,75,0,0,1435,1437,
	3,16,8,0,1436,1434,1,0,0,0,1436,1437,1,0,0,0,1437,1438,1,0,0,0,1438,1439,
	5,15,0,0,1439,1441,3,12,6,0,1440,1360,1,0,0,0,1440,1389,1,0,0,0,1440,1414,
	1,0,0,0,1441,39,1,0,0,0,1442,1443,5,78,0,0,1443,1444,5,118,0,0,1444,1445,
	5,15,0,0,1445,1446,3,6,3,0,1446,1447,5,116,0,0,1447,1734,1,0,0,0,1448,1449,
	5,79,0,0,1449,1450,5,118,0,0,1450,1451,5,15,0,0,1451,1452,3,6,3,0,1452,
	1453,5,116,0,0,1453,1734,1,0,0,0,1454,1455,5,80,0,0,1455,1456,5,118,0,0,
	1456,1457,5,15,0,0,1457,1458,3,6,3,0,1458,1459,5,116,0,0,1459,1734,1,0,
	0,0,1460,1461,5,78,0,0,1461,1464,5,118,0,0,1462,1463,5,67,0,0,1463,1465,
	3,16,8,0,1464,1462,1,0,0,0,1464,1465,1,0,0,0,1465,1466,1,0,0,0,1466,1467,
	5,15,0,0,1467,1468,3,6,3,0,1468,1469,5,116,0,0,1469,1734,1,0,0,0,1470,1471,
	5,79,0,0,1471,1474,5,118,0,0,1472,1473,5,67,0,0,1473,1475,3,16,8,0,1474,
	1472,1,0,0,0,1474,1475,1,0,0,0,1475,1476,1,0,0,0,1476,1477,5,15,0,0,1477,
	1478,3,6,3,0,1478,1479,5,116,0,0,1479,1734,1,0,0,0,1480,1481,5,80,0,0,1481,
	1484,5,118,0,0,1482,1483,5,67,0,0,1483,1485,3,16,8,0,1484,1482,1,0,0,0,
	1484,1485,1,0,0,0,1485,1486,1,0,0,0,1486,1487,5,15,0,0,1487,1488,3,6,3,
	0,1488,1489,5,116,0,0,1489,1734,1,0,0,0,1490,1491,5,81,0,0,1491,1494,5,
	118,0,0,1492,1493,5,67,0,0,1493,1495,3,16,8,0,1494,1492,1,0,0,0,1494,1495,
	1,0,0,0,1495,1496,1,0,0,0,1496,1497,5,15,0,0,1497,1498,3,6,3,0,1498,1499,
	5,116,0,0,1499,1734,1,0,0,0,1500,1501,5,103,0,0,1501,1502,5,78,0,0,1502,
	1505,5,118,0,0,1503,1504,5,104,0,0,1504,1506,5,125,0,0,1505,1503,1,0,0,
	0,1505,1506,1,0,0,0,1506,1507,1,0,0,0,1507,1508,5,26,0,0,1508,1509,3,16,
	8,0,1509,1510,5,116,0,0,1510,1734,1,0,0,0,1511,1512,5,103,0,0,1512,1513,
	7,14,0,0,1513,1516,5,118,0,0,1514,1515,5,104,0,0,1515,1517,5,125,0,0,1516,
	1514,1,0,0,0,1516,1517,1,0,0,0,1517,1518,1,0,0,0,1518,1519,5,26,0,0,1519,
	1520,3,16,8,0,1520,1521,5,116,0,0,1521,1734,1,0,0,0,1522,1523,5,103,0,0,
	1523,1524,5,106,0,0,1524,1525,5,118,0,0,1525,1526,5,26,0,0,1526,1527,3,
	16,8,0,1527,1528,5,116,0,0,1528,1734,1,0,0,0,1529,1530,5,106,0,0,1530,1531,
	5,118,0,0,1531,1533,5,15,0,0,1532,1534,5,118,0,0,1533,1532,1,0,0,0,1533,
	1534,1,0,0,0,1534,1535,1,0,0,0,1535,1536,3,16,8,0,1536,1537,5,116,0,0,1537,
	1734,1,0,0,0,1538,1539,5,103,0,0,1539,1540,5,107,0,0,1540,1541,5,118,0,
	0,1541,1542,5,26,0,0,1542,1547,5,132,0,0,1543,1544,5,119,0,0,1544,1545,
	5,15,0,0,1545,1546,5,118,0,0,1546,1548,5,116,0,0,1547,1543,1,0,0,0,1548,
	1549,1,0,0,0,1549,1547,1,0,0,0,1549,1550,1,0,0,0,1550,1551,1,0,0,0,1551,
	1734,5,133,0,0,1552,1553,5,108,0,0,1553,1554,5,118,0,0,1554,1555,5,4,0,
	0,1555,1556,5,118,0,0,1556,1557,5,67,0,0,1557,1558,3,16,8,0,1558,1559,5,
	6,0,0,1559,1560,5,15,0,0,1560,1561,3,6,3,0,1561,1562,5,116,0,0,1562,1734,
	1,0,0,0,1563,1565,3,48,24,0,1564,1563,1,0,0,0,1565,1568,1,0,0,0,1566,1564,
	1,0,0,0,1566,1567,1,0,0,0,1567,1569,1,0,0,0,1568,1566,1,0,0,0,1569,1570,
	7,15,0,0,1570,1581,5,118,0,0,1571,1572,5,12,0,0,1572,1577,5,118,0,0,1573,
	1574,5,5,0,0,1574,1576,5,118,0,0,1575,1573,1,0,0,0,1576,1579,1,0,0,0,1577,
	1575,1,0,0,0,1577,1578,1,0,0,0,1578,1580,1,0,0,0,1579,1577,1,0,0,0,1580,
	1582,5,13,0,0,1581,1571,1,0,0,0,1581,1582,1,0,0,0,1582,1589,1,0,0,0,1583,
	1586,5,111,0,0,1584,1585,5,118,0,0,1585,1587,5,11,0,0,1586,1584,1,0,0,0,
	1586,1587,1,0,0,0,1587,1588,1,0,0,0,1588,1590,5,118,0,0,1589,1583,1,0,0,
	0,1589,1590,1,0,0,0,1590,1591,1,0,0,0,1591,1592,5,26,0,0,1592,1594,5,132,
	0,0,1593,1595,3,44,22,0,1594,1593,1,0,0,0,1595,1596,1,0,0,0,1596,1594,1,
	0,0,0,1596,1597,1,0,0,0,1597,1598,1,0,0,0,1598,1599,5,133,0,0,1599,1734,
	1,0,0,0,1600,1601,7,15,0,0,1601,1602,5,118,0,0,1602,1605,5,111,0,0,1603,
	1604,5,118,0,0,1604,1606,5,11,0,0,1605,1603,1,0,0,0,1605,1606,1,0,0,0,1606,
	1607,1,0,0,0,1607,1608,5,118,0,0,1608,1734,5,116,0,0,1609,1611,3,48,24,
	0,1610,1609,1,0,0,0,1611,1614,1,0,0,0,1612,1610,1,0,0,0,1612,1613,1,0,0,
	0,1613,1615,1,0,0,0,1614,1612,1,0,0,0,1615,1616,5,118,0,0,1616,1617,5,118,
	0,0,1617,1618,5,26,0,0,1618,1620,5,132,0,0,1619,1621,3,44,22,0,1620,1619,
	1,0,0,0,1621,1622,1,0,0,0,1622,1620,1,0,0,0,1622,1623,1,0,0,0,1623,1624,
	1,0,0,0,1624,1625,5,133,0,0,1625,1734,1,0,0,0,1626,1627,5,103,0,0,1627,
	1628,7,15,0,0,1628,1631,5,118,0,0,1629,1630,5,104,0,0,1630,1632,5,125,0,
	0,1631,1629,1,0,0,0,1631,1632,1,0,0,0,1632,1633,1,0,0,0,1633,1634,5,26,
	0,0,1634,1641,5,132,0,0,1635,1636,5,118,0,0,1636,1637,5,67,0,0,1637,1638,
	3,16,8,0,1638,1639,3,46,23,0,1639,1640,5,116,0,0,1640,1642,1,0,0,0,1641,
	1635,1,0,0,0,1642,1643,1,0,0,0,1643,1641,1,0,0,0,1643,1644,1,0,0,0,1644,
	1645,1,0,0,0,1645,1646,5,133,0,0,1646,1734,1,0,0,0,1647,1648,5,103,0,0,
	1648,1649,7,15,0,0,1649,1652,5,118,0,0,1650,1651,5,104,0,0,1651,1653,5,
	125,0,0,1652,1650,1,0,0,0,1652,1653,1,0,0,0,1653,1654,1,0,0,0,1654,1734,
	5,116,0,0,1655,1656,5,103,0,0,1656,1657,5,112,0,0,1657,1660,5,118,0,0,1658,
	1659,5,104,0,0,1659,1661,5,125,0,0,1660,1658,1,0,0,0,1660,1661,1,0,0,0,
	1661,1662,1,0,0,0,1662,1663,5,26,0,0,1663,1670,5,132,0,0,1664,1665,5,118,
	0,0,1665,1666,5,67,0,0,1666,1667,3,16,8,0,1667,1668,3,46,23,0,1668,1669,
	5,116,0,0,1669,1671,1,0,0,0,1670,1664,1,0,0,0,1671,1672,1,0,0,0,1672,1670,
	1,0,0,0,1672,1673,1,0,0,0,1673,1674,1,0,0,0,1674,1675,5,133,0,0,1675,1734,
	1,0,0,0,1676,1677,5,103,0,0,1677,1678,5,112,0,0,1678,1681,5,118,0,0,1679,
	1680,5,104,0,0,1680,1682,5,125,0,0,1681,1679,1,0,0,0,1681,1682,1,0,0,0,
	1682,1683,1,0,0,0,1683,1734,5,116,0,0,1684,1685,5,103,0,0,1685,1687,5,118,
	0,0,1686,1688,7,15,0,0,1687,1686,1,0,0,0,1687,1688,1,0,0,0,1688,1689,1,
	0,0,0,1689,1692,5,118,0,0,1690,1691,5,104,0,0,1691,1693,5,125,0,0,1692,
	1690,1,0,0,0,1692,1693,1,0,0,0,1693,1694,1,0,0,0,1694,1695,5,26,0,0,1695,
	1702,5,132,0,0,1696,1697,5,118,0,0,1697,1698,5,67,0,0,1698,1699,3,16,8,
	0,1699,1700,3,46,23,0,1700,1701,5,116,0,0,1701,1703,1,0,0,0,1702,1696,1,
	0,0,0,1703,1704,1,0,0,0,1704,1702,1,0,0,0,1704,1705,1,0,0,0,1705,1706,1,
	0,0,0,1706,1707,5,133,0,0,1707,1734,1,0,0,0,1708,1709,5,103,0,0,1709,1711,
	5,118,0,0,1710,1712,7,15,0,0,1711,1710,1,0,0,0,1711,1712,1,0,0,0,1712,1713,
	1,0,0,0,1713,1716,5,118,0,0,1714,1715,5,104,0,0,1715,1717,5,125,0,0,1716,
	1714,1,0,0,0,1716,1717,1,0,0,0,1717,1718,1,0,0,0,1718,1734,5,116,0,0,1719,
	1720,5,77,0,0,1720,1721,5,118,0,0,1721,1722,5,26,0,0,1722,1724,5,132,0,
	0,1723,1725,3,42,21,0,1724,1723,1,0,0,0,1725,1726,1,0,0,0,1726,1724,1,0,
	0,0,1726,1727,1,0,0,0,1727,1728,1,0,0,0,1728,1729,5,133,0,0,1729,1734,1,
	0,0,0,1730,1731,5,113,0,0,1731,1732,5,125,0,0,1732,1734,5,116,0,0,1733,
	1442,1,0,0,0,1733,1448,1,0,0,0,1733,1454,1,0,0,0,1733,1460,1,0,0,0,1733,
	1470,1,0,0,0,1733,1480,1,0,0,0,1733,1490,1,0,0,0,1733,1500,1,0,0,0,1733,
	1511,1,0,0,0,1733,1522,1,0,0,0,1733,1529,1,0,0,0,1733,1538,1,0,0,0,1733,
	1552,1,0,0,0,1733,1566,1,0,0,0,1733,1600,1,0,0,0,1733,1612,1,0,0,0,1733,
	1626,1,0,0,0,1733,1647,1,0,0,0,1733,1655,1,0,0,0,1733,1676,1,0,0,0,1733,
	1684,1,0,0,0,1733,1708,1,0,0,0,1733,1719,1,0,0,0,1733,1730,1,0,0,0,1734,
	41,1,0,0,0,1735,1736,5,118,0,0,1736,1737,5,67,0,0,1737,1738,3,16,8,0,1738,
	1739,5,116,0,0,1739,1764,1,0,0,0,1740,1741,5,10,0,0,1741,1742,5,118,0,0,
	1742,1751,5,4,0,0,1743,1748,3,16,8,0,1744,1745,5,5,0,0,1745,1747,3,16,8,
	0,1746,1744,1,0,0,0,1747,1750,1,0,0,0,1748,1746,1,0,0,0,1748,1749,1,0,0,
	0,1749,1752,1,0,0,0,1750,1748,1,0,0,0,1751,1743,1,0,0,0,1751,1752,1,0,0,
	0,1752,1753,1,0,0,0,1753,1756,5,6,0,0,1754,1755,5,67,0,0,1755,1757,3,16,
	8,0,1756,1754,1,0,0,0,1756,1757,1,0,0,0,1757,1760,1,0,0,0,1758,1759,5,75,
	0,0,1759,1761,3,16,8,0,1760,1758,1,0,0,0,1760,1761,1,0,0,0,1761,1762,1,
	0,0,0,1762,1764,5,116,0,0,1763,1735,1,0,0,0,1763,1740,1,0,0,0,1764,43,1,
	0,0,0,1765,1766,5,118,0,0,1766,1767,5,67,0,0,1767,1768,3,16,8,0,1768,1771,
	3,46,23,0,1769,1770,5,15,0,0,1770,1772,3,6,3,0,1771,1769,1,0,0,0,1771,1772,
	1,0,0,0,1772,1773,1,0,0,0,1773,1774,5,116,0,0,1774,45,1,0,0,0,1775,1776,
	5,67,0,0,1776,1779,3,6,3,0,1777,1779,5,119,0,0,1778,1775,1,0,0,0,1778,1777,
	1,0,0,0,1778,1779,1,0,0,0,1779,47,1,0,0,0,1780,1785,5,118,0,0,1781,1782,
	5,4,0,0,1782,1783,3,6,3,0,1783,1784,5,6,0,0,1784,1786,1,0,0,0,1785,1781,
	1,0,0,0,1785,1786,1,0,0,0,1786,49,1,0,0,0,1787,1788,5,114,0,0,1788,1789,
	5,125,0,0,1789,1790,5,116,0,0,1790,51,1,0,0,0,1791,1795,3,6,3,0,1792,1794,
	5,116,0,0,1793,1792,1,0,0,0,1794,1797,1,0,0,0,1795,1793,1,0,0,0,1795,1796,
	1,0,0,0,1796,1798,1,0,0,0,1797,1795,1,0,0,0,1798,1799,5,0,0,1,1799,53,1,
	0,0,0,1800,1803,5,118,0,0,1801,1802,5,104,0,0,1802,1804,5,125,0,0,1803,
	1801,1,0,0,0,1803,1804,1,0,0,0,1804,1805,1,0,0,0,1805,1806,3,38,19,0,1806,
	55,1,0,0,0,1807,1808,5,115,0,0,1808,1812,3,38,19,0,1809,1810,5,115,0,0,
	1810,1812,3,40,20,0,1811,1807,1,0,0,0,1811,1809,1,0,0,0,1812,57,1,0,0,0,
	1813,1820,3,38,19,0,1814,1820,3,40,20,0,1815,1820,3,50,25,0,1816,1820,3,
	56,28,0,1817,1820,3,54,27,0,1818,1820,5,116,0,0,1819,1813,1,0,0,0,1819,
	1814,1,0,0,0,1819,1815,1,0,0,0,1819,1816,1,0,0,0,1819,1817,1,0,0,0,1819,
	1818,1,0,0,0,1820,1823,1,0,0,0,1821,1819,1,0,0,0,1821,1822,1,0,0,0,1822,
	1824,1,0,0,0,1823,1821,1,0,0,0,1824,1825,5,0,0,1,1825,59,1,0,0,0,188,64,
	71,77,91,104,129,142,156,165,174,179,181,193,200,209,214,216,230,239,248,
	253,259,268,273,275,283,290,299,304,306,316,325,334,339,341,352,359,368,
	373,375,388,402,415,444,471,474,479,483,498,548,564,585,601,630,665,667,
	672,678,684,688,690,696,701,706,715,734,752,758,764,769,775,784,790,795,
	808,812,826,836,847,849,863,890,949,960,964,969,973,979,985,992,1000,1006,
	1010,1039,1047,1060,1065,1079,1084,1089,1100,1103,1107,1118,1121,1125,1129,
	1133,1139,1145,1148,1172,1187,1211,1228,1252,1269,1279,1283,1291,1297,1303,
	1314,1319,1321,1329,1335,1341,1349,1358,1365,1370,1376,1382,1386,1393,1399,
	1405,1409,1414,1420,1426,1432,1436,1440,1464,1474,1484,1494,1505,1516,1533,
	1549,1566,1577,1581,1586,1589,1596,1605,1612,1622,1631,1643,1652,1660,1672,
	1681,1687,1692,1704,1711,1716,1726,1733,1748,1751,1756,1760,1763,1771,1778,
	1785,1795,1803,1811,1819,1821];

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
export class TypeFixedArrayContext extends TypeContext {
	public _size!: ExprContext;
	constructor(parser: EclangParser, ctx: TypeContext) {
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
	public _next!: StatementContext;
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
	public statement(): StatementContext {
		return this.getTypedRuleContext(StatementContext, 0) as StatementContext;
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
	public _next!: StatementContext;
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
	public statement(): StatementContext {
		return this.getTypedRuleContext(StatementContext, 0) as StatementContext;
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
export class GlobalUnionDeclareContext extends GlobalContext {
	public _keyword!: Token;
	public _typedef!: Token;
	public _name!: Token;
	public _externalName!: Token;
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
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
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
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
	public _fieldWidth!: FieldWidthContext;
	public _widths: FieldWidthContext[] = [];
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
	public fieldWidth_list(): FieldWidthContext[] {
		return this.getTypedRuleContexts(FieldWidthContext) as FieldWidthContext[];
	}
	public fieldWidth(i: number): FieldWidthContext {
		return this.getTypedRuleContext(FieldWidthContext, i) as FieldWidthContext;
	}
}
export class GlobalStructDeclareDefineContext extends GlobalContext {
	public _name!: Token;
	public _externalName!: Token;
	public _NAME!: Token;
	public _names: Token[] = [];
	public _type_!: TypeContext;
	public _types: TypeContext[] = [];
	public _fieldWidth!: FieldWidthContext;
	public _widths: FieldWidthContext[] = [];
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
	public fieldWidth_list(): FieldWidthContext[] {
		return this.getTypedRuleContexts(FieldWidthContext) as FieldWidthContext[];
	}
	public fieldWidth(i: number): FieldWidthContext {
		return this.getTypedRuleContext(FieldWidthContext, i) as FieldWidthContext;
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
export class GlobalUnionDeclareDefineContext extends GlobalContext {
	public _keyword!: Token;
	public _typedef!: Token;
	public _name!: Token;
	public _externalName!: Token;
	public _NAME!: Token;
	public _names: Token[] = [];
	public _type_!: TypeContext;
	public _types: TypeContext[] = [];
	public _fieldWidth!: FieldWidthContext;
	public _widths: FieldWidthContext[] = [];
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
	public fieldWidth_list(): FieldWidthContext[] {
		return this.getTypedRuleContexts(FieldWidthContext) as FieldWidthContext[];
	}
	public fieldWidth(i: number): FieldWidthContext {
		return this.getTypedRuleContext(FieldWidthContext, i) as FieldWidthContext;
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
export class GlobalUnionDefineContext extends GlobalContext {
	public _recordModifier!: RecordModifierContext;
	public _modifiers: RecordModifierContext[] = [];
	public _keyword!: Token;
	public _name!: Token;
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
	public recordModifier_list(): RecordModifierContext[] {
		return this.getTypedRuleContexts(RecordModifierContext) as RecordModifierContext[];
	}
	public recordModifier(i: number): RecordModifierContext {
		return this.getTypedRuleContext(RecordModifierContext, i) as RecordModifierContext;
	}
	public recordField_list(): RecordFieldContext[] {
		return this.getTypedRuleContexts(RecordFieldContext) as RecordFieldContext[];
	}
	public recordField(i: number): RecordFieldContext {
		return this.getTypedRuleContext(RecordFieldContext, i) as RecordFieldContext;
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
	public _recordModifier!: RecordModifierContext;
	public _modifiers: RecordModifierContext[] = [];
	public _name!: Token;
	public _NAME!: Token;
	public _typeParams: Token[] = [];
	public _qualifier!: Token;
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
	public recordModifier_list(): RecordModifierContext[] {
		return this.getTypedRuleContexts(RecordModifierContext) as RecordModifierContext[];
	}
	public recordModifier(i: number): RecordModifierContext {
		return this.getTypedRuleContext(RecordModifierContext, i) as RecordModifierContext;
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
	public _throwing!: TypeContext;
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
	public fieldWidth(): FieldWidthContext {
		return this.getTypedRuleContext(FieldWidthContext, 0) as FieldWidthContext;
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


export class FieldWidthContext extends ParserRuleContext {
	public _width!: ExprContext;
	public _atomWidth!: Token;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
	public ATOM(): TerminalNode {
		return this.getToken(EclangParser.ATOM, 0);
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_fieldWidth;
	}
}


export class RecordModifierContext extends ParserRuleContext {
	public _name!: Token;
	public _size!: ExprContext;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public expr(): ExprContext {
		return this.getTypedRuleContext(ExprContext, 0) as ExprContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_recordModifier;
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


export class ExportDeclarationContext extends ParserRuleContext {
	public _keyword!: Token;
	public _externalName!: Token;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public func(): FuncContext {
		return this.getTypedRuleContext(FuncContext, 0) as FuncContext;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_exportDeclaration;
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
	public _exportDeclaration!: ExportDeclarationContext;
	public _exports: ExportDeclarationContext[] = [];
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
	public exportDeclaration_list(): ExportDeclarationContext[] {
		return this.getTypedRuleContexts(ExportDeclarationContext) as ExportDeclarationContext[];
	}
	public exportDeclaration(i: number): ExportDeclarationContext {
		return this.getTypedRuleContext(ExportDeclarationContext, i) as ExportDeclarationContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_program;
	}
}
