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
	public static readonly NL = 108;
	public static readonly BOOL = 109;
	public static readonly NAME = 110;
	public static readonly ATOM = 111;
	public static readonly FLOAT = 112;
	public static readonly INT = 113;
	public static readonly HEX = 114;
	public static readonly BIN = 115;
	public static readonly CHAR = 116;
	public static readonly STRING = 117;
	public static readonly EMBEDED = 118;
	public static readonly WHITESPACE = 119;
	public static readonly EMPTYLINE = 120;
	public static readonly COMMENT = 121;
	public static readonly ECDOC = 122;
	public static readonly BLOCKCOMMENT = 123;
	public static readonly INDENT = 124;
	public static readonly DEDENT = 125;
	public static readonly EOF = Token.EOF;
	public static readonly RULE_number = 0;
	public static readonly RULE_primitive = 1;
	public static readonly RULE_block = 2;
	public static readonly RULE_expr = 3;
	public static readonly RULE_argument = 4;
	public static readonly RULE_jump = 5;
	public static readonly RULE_tail = 6;
	public static readonly RULE_tryCatch = 7;
	public static readonly RULE_lambdaParameter = 8;
	public static readonly RULE_type = 9;
	public static readonly RULE_interface = 10;
	public static readonly RULE_parameter = 11;
	public static readonly RULE_statement = 12;
	public static readonly RULE_constAssert = 13;
	public static readonly RULE_contract = 14;
	public static readonly RULE_destructure = 15;
	public static readonly RULE_destructurePart = 16;
	public static readonly RULE_whenPattern = 17;
	public static readonly RULE_whenValue = 18;
	public static readonly RULE_whenArm = 19;
	public static readonly RULE_whenExpr = 20;
	public static readonly RULE_whenValueArm = 21;
	public static readonly RULE_func = 22;
	public static readonly RULE_global = 23;
	public static readonly RULE_interfaceMember = 24;
	public static readonly RULE_recordField = 25;
	public static readonly RULE_fieldWidth = 26;
	public static readonly RULE_fieldExtern = 27;
	public static readonly RULE_recordModifier = 28;
	public static readonly RULE_typeParameter = 29;
	public static readonly RULE_use = 30;
	public static readonly RULE_interpolation = 31;
	public static readonly RULE_exportDeclaration = 32;
	public static readonly RULE_privateDeclaration = 33;
	public static readonly RULE_program = 34;
	public static readonly literalNames: (string | null)[] = [ null, "'null'", 
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
                                                            "':'", "'mut'", 
                                                            "'ret'", "'throw'", 
                                                            "'break'", "'continue'", 
                                                            "'catch'", "'?'", 
                                                            "'typeof'", 
                                                            "'throws'", 
                                                            "'=>'", "'inter'", 
                                                            "'var'", "'val'", 
                                                            "'const'", "'+='", 
                                                            "'-='", "'*='", 
                                                            "'/='", "'%='", 
                                                            "'&='", "'|='", 
                                                            "'^='", "'<<='", 
                                                            "'>>='", "'pass'", 
                                                            "'yield'", "'assert'", 
                                                            "'while'", "'defer'", 
                                                            "'errdefer'", 
                                                            "'when'", "'for'", 
                                                            "'to'", "'destroy'", 
                                                            "'declare'", 
                                                            "'inline'", 
                                                            "'type'", "'enum'", 
                                                            "'rec'", "'extends'", 
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
		"number", "primitive", "block", "expr", "argument", "jump", "tail", "tryCatch", 
		"lambdaParameter", "type", "interface", "parameter", "statement", "constAssert", 
		"contract", "destructure", "destructurePart", "whenPattern", "whenValue", 
		"whenArm", "whenExpr", "whenValueArm", "func", "global", "interfaceMember", 
		"recordField", "fieldWidth", "fieldExtern", "recordModifier", "typeParameter", 
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
			this.state = 74;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 113:
				localctx = new NumberIntegerContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 70;
				this.match(EclangParser.INT);
				}
				break;
			case 112:
				localctx = new NumberFloatingContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 71;
				this.match(EclangParser.FLOAT);
				}
				break;
			case 114:
				localctx = new NumberHexContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 72;
				this.match(EclangParser.HEX);
				}
				break;
			case 115:
				localctx = new NumberBinContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 73;
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
			this.state = 81;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 112:
			case 113:
			case 114:
			case 115:
				localctx = new PrimitiveNumberContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 76;
				this.number_();
				}
				break;
			case 109:
				localctx = new PrimitiveBoolContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 77;
				this.match(EclangParser.BOOL);
				}
				break;
			case 117:
				localctx = new PrimitiveStringContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 78;
				this.match(EclangParser.STRING);
				}
				break;
			case 116:
				localctx = new PrimitiveCharContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 79;
				this.match(EclangParser.CHAR);
				}
				break;
			case 111:
				localctx = new PrimitiveAtomContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 80;
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
			this.state = 83;
			this.match(EclangParser.INDENT);
			this.state = 85;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 84;
				localctx._statement = this.statement();
				localctx._statements.push(localctx._statement);
				}
				}
				this.state = 87;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while ((((_la) & ~0x1F) === 0 && ((1 << _la) & 2055279078) !== 0) || ((((_la - 33)) & ~0x1F) === 0 && ((1 << (_la - 33)) & 268435471) !== 0) || ((((_la - 67)) & ~0x1F) === 0 && ((1 << (_la - 67)) & 2139102223) !== 0) || ((((_la - 99)) & ~0x1F) === 0 && ((1 << (_la - 99)) & 523265) !== 0));
			this.state = 89;
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
			this.state = 493;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 47, this._ctx) ) {
			case 1:
				{
				localctx = new ExprNullContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;

				this.state = 92;
				this.match(EclangParser.T__0);
				}
				break;
			case 2:
				{
				localctx = new ExprNestContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 93;
				this.match(EclangParser.T__1);
				this.state = 94;
				this.expr(0);
				this.state = 99;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 95;
					this.match(EclangParser.T__2);
					this.state = 96;
					this.expr(0);
					}
					}
					this.state = 101;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 102;
				this.match(EclangParser.T__3);
				}
				break;
			case 3:
				{
				localctx = new ExprNestContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 104;
				this.match(EclangParser.T__1);
				this.state = 105;
				this.match(EclangParser.INDENT);
				this.state = 106;
				this.expr(0);
				this.state = 112;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 107;
					this.match(EclangParser.T__2);
					this.state = 108;
					this.match(EclangParser.NL);
					this.state = 109;
					this.expr(0);
					}
					}
					this.state = 114;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 115;
				this.match(EclangParser.NL);
				this.state = 116;
				this.match(EclangParser.DEDENT);
				this.state = 117;
				this.match(EclangParser.T__3);
				}
				break;
			case 4:
				{
				localctx = new ExprPrimitiveContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 119;
				this.primitive();
				}
				break;
			case 5:
				{
				localctx = new ExprPrimitiveUnitContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 120;
				this.primitive();
				this.state = 121;
				this.match(EclangParser.NAME);
				}
				break;
			case 6:
				{
				localctx = new ExprSizeOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 123;
				this.match(EclangParser.T__4);
				this.state = 124;
				this.type_(0);
				}
				break;
			case 7:
				{
				localctx = new ExprAlignOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 125;
				this.match(EclangParser.T__5);
				this.state = 126;
				this.type_(0);
				}
				break;
			case 8:
				{
				localctx = new ExprOffsetOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 127;
				this.match(EclangParser.T__6);
				this.state = 128;
				this.match(EclangParser.T__1);
				this.state = 129;
				this.type_(0);
				this.state = 130;
				this.match(EclangParser.T__2);
				this.state = 131;
				(localctx as ExprOffsetOfContext)._field = this.match(EclangParser.NAME);
				this.state = 132;
				this.match(EclangParser.T__3);
				}
				break;
			case 9:
				{
				localctx = new ExprFunctionReferenceContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 134;
				this.match(EclangParser.T__7);
				this.state = 137;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 5, this._ctx) ) {
				case 1:
					{
					this.state = 135;
					(localctx as ExprFunctionReferenceContext)._qualifier = this.match(EclangParser.NAME);
					this.state = 136;
					this.match(EclangParser.T__8);
					}
					break;
				}
				this.state = 139;
				(localctx as ExprFunctionReferenceContext)._name = this.match(EclangParser.NAME);
				}
				break;
			case 10:
				{
				localctx = new ExprFunctionCallNoArgsContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 140;
				this.match(EclangParser.NAME);
				this.state = 141;
				this.match(EclangParser.T__1);
				this.state = 142;
				this.match(EclangParser.T__3);
				}
				break;
			case 11:
				{
				localctx = new ExprFunctionCallWithArgsContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 143;
				this.match(EclangParser.NAME);
				this.state = 144;
				this.match(EclangParser.T__1);
				this.state = 145;
				(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
				this.state = 150;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 146;
					this.match(EclangParser.T__2);
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
				this.match(EclangParser.T__3);
				}
				break;
			case 12:
				{
				localctx = new ExprFunctionCallWithArgsContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 155;
				this.match(EclangParser.NAME);
				this.state = 156;
				this.match(EclangParser.T__1);
				this.state = 157;
				this.match(EclangParser.INDENT);
				this.state = 158;
				(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
				this.state = 164;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 159;
					this.match(EclangParser.T__2);
					this.state = 160;
					this.match(EclangParser.NL);
					this.state = 161;
					(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 166;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 167;
				this.match(EclangParser.NL);
				this.state = 168;
				this.match(EclangParser.DEDENT);
				this.state = 169;
				this.match(EclangParser.T__3);
				}
				break;
			case 13:
				{
				localctx = new ExprCreateRecordNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 189;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 173;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 8, this._ctx) ) {
					case 1:
						{
						this.state = 171;
						(localctx as ExprCreateRecordNamedContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 172;
						this.match(EclangParser.T__8);
						}
						break;
					}
					this.state = 175;
					(localctx as ExprCreateRecordNamedContext)._recordType = this.match(EclangParser.NAME);
					this.state = 187;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===10) {
						{
						this.state = 176;
						this.match(EclangParser.T__9);
						this.state = 177;
						(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
						this.state = 182;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===3) {
							{
							{
							this.state = 178;
							this.match(EclangParser.T__2);
							this.state = 179;
							(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
							}
							}
							this.state = 184;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 185;
						this.match(EclangParser.T__10);
						}
					}

					}
				}

				this.state = 191;
				this.match(EclangParser.T__11);
				this.state = 192;
				(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
				(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
				this.state = 193;
				this.match(EclangParser.T__12);
				this.state = 194;
				this.expr(0);
				this.state = 201;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 195;
					this.match(EclangParser.T__2);
					this.state = 196;
					(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
					this.state = 197;
					this.match(EclangParser.T__12);
					this.state = 198;
					this.expr(0);
					}
					}
					this.state = 203;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 204;
				this.match(EclangParser.T__13);
				}
				break;
			case 14:
				{
				localctx = new ExprCreateRecordNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 224;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 208;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 13, this._ctx) ) {
					case 1:
						{
						this.state = 206;
						(localctx as ExprCreateRecordNamedContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 207;
						this.match(EclangParser.T__8);
						}
						break;
					}
					this.state = 210;
					(localctx as ExprCreateRecordNamedContext)._recordType = this.match(EclangParser.NAME);
					this.state = 222;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===10) {
						{
						this.state = 211;
						this.match(EclangParser.T__9);
						this.state = 212;
						(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
						this.state = 217;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===3) {
							{
							{
							this.state = 213;
							this.match(EclangParser.T__2);
							this.state = 214;
							(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
							}
							}
							this.state = 219;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 220;
						this.match(EclangParser.T__10);
						}
					}

					}
				}

				this.state = 226;
				this.match(EclangParser.T__11);
				this.state = 227;
				this.match(EclangParser.INDENT);
				this.state = 228;
				(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
				(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
				this.state = 229;
				this.match(EclangParser.T__12);
				this.state = 230;
				this.expr(0);
				this.state = 238;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 231;
					this.match(EclangParser.T__2);
					this.state = 232;
					this.match(EclangParser.NL);
					this.state = 233;
					(localctx as ExprCreateRecordNamedContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprCreateRecordNamedContext)._names.push((localctx as ExprCreateRecordNamedContext)._NAME);
					this.state = 234;
					this.match(EclangParser.T__12);
					this.state = 235;
					this.expr(0);
					}
					}
					this.state = 240;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 241;
				this.match(EclangParser.NL);
				this.state = 242;
				this.match(EclangParser.DEDENT);
				this.state = 243;
				this.match(EclangParser.T__13);
				}
				break;
			case 15:
				{
				localctx = new ExprCreateRecordNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 247;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 18, this._ctx) ) {
				case 1:
					{
					this.state = 245;
					(localctx as ExprCreateRecordNamedContext)._qualifier = this.match(EclangParser.NAME);
					this.state = 246;
					this.match(EclangParser.T__8);
					}
					break;
				}
				this.state = 249;
				(localctx as ExprCreateRecordNamedContext)._recordType = this.match(EclangParser.NAME);
				this.state = 261;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===10) {
					{
					this.state = 250;
					this.match(EclangParser.T__9);
					this.state = 251;
					(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
					(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
					this.state = 256;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===3) {
						{
						{
						this.state = 252;
						this.match(EclangParser.T__2);
						this.state = 253;
						(localctx as ExprCreateRecordNamedContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordNamedContext)._typeArgs.push((localctx as ExprCreateRecordNamedContext)._type_);
						}
						}
						this.state = 258;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					this.state = 259;
					this.match(EclangParser.T__10);
					}
				}

				this.state = 263;
				this.match(EclangParser.T__11);
				this.state = 264;
				this.match(EclangParser.T__13);
				}
				break;
			case 16:
				{
				localctx = new ExprCreateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 283;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 267;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 21, this._ctx) ) {
					case 1:
						{
						this.state = 265;
						(localctx as ExprCreateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 266;
						this.match(EclangParser.T__8);
						}
						break;
					}
					this.state = 269;
					(localctx as ExprCreateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 281;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===10) {
						{
						this.state = 270;
						this.match(EclangParser.T__9);
						this.state = 271;
						(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
						this.state = 276;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===3) {
							{
							{
							this.state = 272;
							this.match(EclangParser.T__2);
							this.state = 273;
							(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
							}
							}
							this.state = 278;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 279;
						this.match(EclangParser.T__10);
						}
					}

					}
				}

				this.state = 285;
				this.match(EclangParser.T__11);
				this.state = 286;
				this.expr(0);
				this.state = 291;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 287;
					this.match(EclangParser.T__2);
					this.state = 288;
					this.expr(0);
					}
					}
					this.state = 293;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 294;
				this.match(EclangParser.T__13);
				}
				break;
			case 17:
				{
				localctx = new ExprCreateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 314;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 298;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 26, this._ctx) ) {
					case 1:
						{
						this.state = 296;
						(localctx as ExprCreateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 297;
						this.match(EclangParser.T__8);
						}
						break;
					}
					this.state = 300;
					(localctx as ExprCreateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 312;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===10) {
						{
						this.state = 301;
						this.match(EclangParser.T__9);
						this.state = 302;
						(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
						(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
						this.state = 307;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===3) {
							{
							{
							this.state = 303;
							this.match(EclangParser.T__2);
							this.state = 304;
							(localctx as ExprCreateRecordContext)._type_ = this.type_(0);
							(localctx as ExprCreateRecordContext)._typeArgs.push((localctx as ExprCreateRecordContext)._type_);
							}
							}
							this.state = 309;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 310;
						this.match(EclangParser.T__10);
						}
					}

					}
				}

				this.state = 316;
				this.match(EclangParser.T__11);
				this.state = 317;
				this.match(EclangParser.INDENT);
				this.state = 318;
				this.expr(0);
				this.state = 324;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 319;
					this.match(EclangParser.T__2);
					this.state = 320;
					this.match(EclangParser.NL);
					this.state = 321;
					this.expr(0);
					}
					}
					this.state = 326;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 327;
				this.match(EclangParser.NL);
				this.state = 328;
				this.match(EclangParser.DEDENT);
				this.state = 329;
				this.match(EclangParser.T__13);
				}
				break;
			case 18:
				{
				localctx = new ExprUpdateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 349;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 333;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 31, this._ctx) ) {
					case 1:
						{
						this.state = 331;
						(localctx as ExprUpdateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 332;
						this.match(EclangParser.T__8);
						}
						break;
					}
					this.state = 335;
					(localctx as ExprUpdateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 347;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===10) {
						{
						this.state = 336;
						this.match(EclangParser.T__9);
						this.state = 337;
						(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
						(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
						this.state = 342;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===3) {
							{
							{
							this.state = 338;
							this.match(EclangParser.T__2);
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
						this.match(EclangParser.T__10);
						}
					}

					}
				}

				this.state = 351;
				this.match(EclangParser.T__11);
				this.state = 352;
				this.match(EclangParser.T__14);
				this.state = 353;
				(localctx as ExprUpdateRecordContext)._base = this.expr(0);
				this.state = 360;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 354;
					this.match(EclangParser.T__2);
					this.state = 355;
					(localctx as ExprUpdateRecordContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprUpdateRecordContext)._names.push((localctx as ExprUpdateRecordContext)._NAME);
					this.state = 356;
					this.match(EclangParser.T__12);
					this.state = 357;
					(localctx as ExprUpdateRecordContext)._expr = this.expr(0);
					(localctx as ExprUpdateRecordContext)._values.push((localctx as ExprUpdateRecordContext)._expr);
					}
					}
					this.state = 362;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 363;
				this.match(EclangParser.T__13);
				}
				break;
			case 19:
				{
				localctx = new ExprUpdateRecordContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 383;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 367;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 36, this._ctx) ) {
					case 1:
						{
						this.state = 365;
						(localctx as ExprUpdateRecordContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 366;
						this.match(EclangParser.T__8);
						}
						break;
					}
					this.state = 369;
					(localctx as ExprUpdateRecordContext)._recordType = this.match(EclangParser.NAME);
					this.state = 381;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===10) {
						{
						this.state = 370;
						this.match(EclangParser.T__9);
						this.state = 371;
						(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
						(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
						this.state = 376;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===3) {
							{
							{
							this.state = 372;
							this.match(EclangParser.T__2);
							this.state = 373;
							(localctx as ExprUpdateRecordContext)._type_ = this.type_(0);
							(localctx as ExprUpdateRecordContext)._typeArgs.push((localctx as ExprUpdateRecordContext)._type_);
							}
							}
							this.state = 378;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 379;
						this.match(EclangParser.T__10);
						}
					}

					}
				}

				this.state = 385;
				this.match(EclangParser.T__11);
				this.state = 386;
				this.match(EclangParser.INDENT);
				this.state = 387;
				this.match(EclangParser.T__14);
				this.state = 388;
				(localctx as ExprUpdateRecordContext)._base = this.expr(0);
				this.state = 396;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 389;
					this.match(EclangParser.T__2);
					this.state = 390;
					this.match(EclangParser.NL);
					this.state = 391;
					(localctx as ExprUpdateRecordContext)._NAME = this.match(EclangParser.NAME);
					(localctx as ExprUpdateRecordContext)._names.push((localctx as ExprUpdateRecordContext)._NAME);
					this.state = 392;
					this.match(EclangParser.T__12);
					this.state = 393;
					(localctx as ExprUpdateRecordContext)._expr = this.expr(0);
					(localctx as ExprUpdateRecordContext)._values.push((localctx as ExprUpdateRecordContext)._expr);
					}
					}
					this.state = 398;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 399;
				this.match(EclangParser.NL);
				this.state = 400;
				this.match(EclangParser.DEDENT);
				this.state = 401;
				this.match(EclangParser.T__13);
				}
				break;
			case 20:
				{
				localctx = new ExprNamedContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 403;
				this.match(EclangParser.NAME);
				}
				break;
			case 21:
				{
				localctx = new ExprCreateArrayContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 404;
				this.match(EclangParser.T__15);
				this.state = 405;
				this.expr(0);
				this.state = 410;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 406;
					this.match(EclangParser.T__2);
					this.state = 407;
					this.expr(0);
					}
					}
					this.state = 412;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 413;
				_la = this._input.LA(1);
				if(!(_la===17 || _la===18)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				}
				break;
			case 22:
				{
				localctx = new ExprCreateArrayContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 415;
				this.match(EclangParser.T__15);
				this.state = 416;
				this.match(EclangParser.INDENT);
				this.state = 417;
				this.expr(0);
				this.state = 423;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 418;
					this.match(EclangParser.T__2);
					this.state = 419;
					this.match(EclangParser.NL);
					this.state = 420;
					this.expr(0);
					}
					}
					this.state = 425;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 426;
				this.match(EclangParser.NL);
				this.state = 427;
				this.match(EclangParser.DEDENT);
				this.state = 428;
				_la = this._input.LA(1);
				if(!(_la===17 || _la===18)) {
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
				localctx = new ExprUnOpContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 430;
				(localctx as ExprUnOpContext)._op = this.match(EclangParser.T__26);
				this.state = 431;
				this.expr(31);
				}
				break;
			case 24:
				{
				localctx = new ExprAddressOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 432;
				this.match(EclangParser.T__27);
				this.state = 433;
				this.expr(30);
				}
				break;
			case 25:
				{
				localctx = new ExprTryContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 434;
				this.match(EclangParser.T__28);
				this.state = 435;
				this.expr(29);
				}
				break;
			case 26:
				{
				localctx = new ExprUnsafeContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 436;
				this.match(EclangParser.T__24);
				this.state = 437;
				this.expr(28);
				}
				break;
			case 27:
				{
				localctx = new ExprNewContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 438;
				this.match(EclangParser.T__29);
				this.state = 439;
				(localctx as ExprNewContext)._value = this.expr(0);
				this.state = 440;
				this.match(EclangParser.T__30);
				this.state = 441;
				(localctx as ExprNewContext)._pool = this.expr(27);
				}
				break;
			case 28:
				{
				localctx = new ExprNewContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 443;
				this.match(EclangParser.T__29);
				this.state = 444;
				(localctx as ExprNewContext)._value = this.expr(26);
				}
				break;
			case 29:
				{
				localctx = new ExprNewArrayOfTypeContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 445;
				this.match(EclangParser.T__29);
				this.state = 446;
				this.match(EclangParser.T__1);
				this.state = 447;
				(localctx as ExprNewArrayOfTypeContext)._elementType = this.type_(0);
				this.state = 448;
				this.match(EclangParser.T__3);
				this.state = 449;
				this.match(EclangParser.T__15);
				this.state = 450;
				(localctx as ExprNewArrayOfTypeContext)._count = this.expr(0);
				this.state = 451;
				this.match(EclangParser.T__17);
				this.state = 454;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 43, this._ctx) ) {
				case 1:
					{
					this.state = 452;
					this.match(EclangParser.T__30);
					this.state = 453;
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
				this.state = 456;
				(localctx as ExprUnOpContext)._op = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(((((_la - 23)) & ~0x1F) === 0 && ((1 << (_la - 23)) & 15361) !== 0))) {
				    (localctx as ExprUnOpContext)._op = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 457;
				this.expr(23);
				}
				break;
			case 31:
				{
				localctx = new ExprInlineIfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 458;
				this.match(EclangParser.T__60);
				this.state = 459;
				(localctx as ExprInlineIfContext)._condition = this.expr(0);
				this.state = 460;
				this.match(EclangParser.T__61);
				this.state = 461;
				(localctx as ExprInlineIfContext)._body = this.expr(0);
				this.state = 462;
				this.match(EclangParser.T__62);
				this.state = 463;
				(localctx as ExprInlineIfContext)._elseBody = this.expr(5);
				}
				break;
			case 32:
				{
				localctx = new ExprInlineIfOldContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 465;
				this.match(EclangParser.T__60);
				this.state = 466;
				this.match(EclangParser.T__1);
				this.state = 467;
				(localctx as ExprInlineIfOldContext)._condition = this.expr(0);
				this.state = 468;
				this.match(EclangParser.T__3);
				this.state = 469;
				(localctx as ExprInlineIfOldContext)._body = this.expr(0);
				this.state = 470;
				this.match(EclangParser.T__62);
				this.state = 471;
				(localctx as ExprInlineIfOldContext)._elseBody = this.expr(4);
				}
				break;
			case 33:
				{
				localctx = new ExprWhenContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 473;
				this.whenExpr();
				}
				break;
			case 34:
				{
				localctx = new ExprLambdaContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 474;
				this.match(EclangParser.T__7);
				this.state = 475;
				this.match(EclangParser.T__1);
				this.state = 484;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 476;
					(localctx as ExprLambdaContext)._lambdaParameter = this.lambdaParameter();
					(localctx as ExprLambdaContext)._params.push((localctx as ExprLambdaContext)._lambdaParameter);
					this.state = 481;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===3) {
						{
						{
						this.state = 477;
						this.match(EclangParser.T__2);
						this.state = 478;
						(localctx as ExprLambdaContext)._lambdaParameter = this.lambdaParameter();
						(localctx as ExprLambdaContext)._params.push((localctx as ExprLambdaContext)._lambdaParameter);
						}
						}
						this.state = 483;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					}
				}

				this.state = 486;
				this.match(EclangParser.T__3);
				this.state = 489;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===65) {
					{
					this.state = 487;
					this.match(EclangParser.T__64);
					this.state = 488;
					(localctx as ExprLambdaContext)._returnType = this.type_(0);
					}
				}

				this.state = 491;
				this.match(EclangParser.T__12);
				this.state = 492;
				(localctx as ExprLambdaContext)._body = this.expr(1);
				}
				break;
			}
			this._ctx.stop = this._input.LT(-1);
			this.state = 677;
			this._errHandler.sync(this);
			_alt = this._interp.adaptivePredict(this._input, 55, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					if (this._parseListeners != null) {
						this.triggerExitRuleEvent();
					}
					_prevctx = localctx;
					{
					this.state = 675;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 54, this._ctx) ) {
					case 1:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 495;
						if (!(this.precpred(this._ctx, 24))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 24)");
						}
						this.state = 496;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__31);
						this.state = 497;
						(localctx as ExprBinOpContext)._right = this.expr(24);
						}
						break;
					case 2:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 498;
						if (!(this.precpred(this._ctx, 22))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 22)");
						}
						this.state = 499;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(((((_la - 37)) & ~0x1F) === 0 && ((1 << (_la - 37)) & 7) !== 0))) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 500;
						(localctx as ExprBinOpContext)._right = this.expr(23);
						}
						break;
					case 3:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 501;
						if (!(this.precpred(this._ctx, 21))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 21)");
						}
						this.state = 502;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===36 || _la===40)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 503;
						(localctx as ExprBinOpContext)._right = this.expr(22);
						}
						break;
					case 4:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 504;
						if (!(this.precpred(this._ctx, 20))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 20)");
						}
						this.state = 508;
						this._errHandler.sync(this);
						switch (this._input.LA(1)) {
						case 41:
							{
							this.state = 505;
							(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__40);
							}
							break;
						case 11:
							{
							this.state = 506;
							(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__10);
							this.state = 507;
							(localctx as ExprBinOpContext)._shr = this.match(EclangParser.T__10);
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						this.state = 510;
						(localctx as ExprBinOpContext)._right = this.expr(21);
						}
						break;
					case 5:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 511;
						if (!(this.precpred(this._ctx, 19))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 19)");
						}
						this.state = 512;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__41);
						this.state = 513;
						(localctx as ExprBinOpContext)._right = this.expr(20);
						}
						break;
					case 6:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 514;
						if (!(this.precpred(this._ctx, 18))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 18)");
						}
						this.state = 515;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__42);
						this.state = 516;
						(localctx as ExprBinOpContext)._right = this.expr(19);
						}
						break;
					case 7:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 517;
						if (!(this.precpred(this._ctx, 17))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 17)");
						}
						this.state = 518;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__43);
						this.state = 519;
						(localctx as ExprBinOpContext)._right = this.expr(18);
						}
						break;
					case 8:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 520;
						if (!(this.precpred(this._ctx, 16))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 16)");
						}
						this.state = 521;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(((((_la - 21)) & ~0x1F) === 0 && ((1 << (_la - 21)) & 16777219) !== 0))) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 522;
						(localctx as ExprBinOpContext)._right = this.expr(17);
						}
						break;
					case 9:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 523;
						if (!(this.precpred(this._ctx, 15))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 15)");
						}
						this.state = 524;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===10 || _la===11 || _la===46 || _la===47)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 525;
						(localctx as ExprBinOpContext)._right = this.expr(16);
						}
						break;
					case 10:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 526;
						if (!(this.precpred(this._ctx, 11))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 11)");
						}
						this.state = 527;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(((((_la - 50)) & ~0x1F) === 0 && ((1 << (_la - 50)) & 15) !== 0))) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 528;
						(localctx as ExprBinOpContext)._right = this.expr(12);
						}
						break;
					case 11:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 529;
						if (!(this.precpred(this._ctx, 10))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 10)");
						}
						this.state = 530;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===54 || _la===55)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 531;
						(localctx as ExprBinOpContext)._right = this.expr(11);
						}
						break;
					case 12:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 532;
						if (!(this.precpred(this._ctx, 9))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 9)");
						}
						this.state = 533;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===56 || _la===57)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 534;
						(localctx as ExprBinOpContext)._right = this.expr(10);
						}
						break;
					case 13:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 535;
						if (!(this.precpred(this._ctx, 8))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 8)");
						}
						this.state = 536;
						(localctx as ExprBinOpContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===58 || _la===59)) {
						    (localctx as ExprBinOpContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 537;
						(localctx as ExprBinOpContext)._right = this.expr(9);
						}
						break;
					case 14:
						{
						localctx = new ExprBinOpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprBinOpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 538;
						if (!(this.precpred(this._ctx, 7))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 7)");
						}
						this.state = 539;
						(localctx as ExprBinOpContext)._op = this.match(EclangParser.T__59);
						this.state = 540;
						(localctx as ExprBinOpContext)._right = this.expr(7);
						}
						break;
					case 15:
						{
						localctx = new ExprPipeContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprPipeContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 541;
						if (!(this.precpred(this._ctx, 3))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 3)");
						}
						this.state = 542;
						this.match(EclangParser.T__63);
						this.state = 543;
						(localctx as ExprPipeContext)._right = this.expr(4);
						}
						break;
					case 16:
						{
						localctx = new ExprFunctionCallNoArgsContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprFunctionCallNoArgsContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 544;
						if (!(this.precpred(this._ctx, 47))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 47)");
						}
						this.state = 545;
						this.match(EclangParser.T__8);
						this.state = 546;
						this.match(EclangParser.NAME);
						this.state = 547;
						this.match(EclangParser.T__1);
						this.state = 548;
						this.match(EclangParser.T__3);
						}
						break;
					case 17:
						{
						localctx = new ExprFunctionCallWithArgsContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprFunctionCallWithArgsContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 549;
						if (!(this.precpred(this._ctx, 46))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 46)");
						}
						this.state = 550;
						this.match(EclangParser.T__8);
						this.state = 551;
						this.match(EclangParser.NAME);
						this.state = 552;
						this.match(EclangParser.T__1);
						this.state = 553;
						(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
						(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
						this.state = 558;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===3) {
							{
							{
							this.state = 554;
							this.match(EclangParser.T__2);
							this.state = 555;
							(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
							(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
							}
							}
							this.state = 560;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 561;
						this.match(EclangParser.T__3);
						}
						break;
					case 18:
						{
						localctx = new ExprFunctionCallWithArgsContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprFunctionCallWithArgsContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 563;
						if (!(this.precpred(this._ctx, 45))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 45)");
						}
						this.state = 564;
						this.match(EclangParser.T__8);
						this.state = 565;
						this.match(EclangParser.NAME);
						this.state = 566;
						this.match(EclangParser.T__1);
						this.state = 567;
						this.match(EclangParser.INDENT);
						this.state = 568;
						(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
						(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
						this.state = 574;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===3) {
							{
							{
							this.state = 569;
							this.match(EclangParser.T__2);
							this.state = 570;
							this.match(EclangParser.NL);
							this.state = 571;
							(localctx as ExprFunctionCallWithArgsContext)._argument = this.argument();
							(localctx as ExprFunctionCallWithArgsContext)._rest.push((localctx as ExprFunctionCallWithArgsContext)._argument);
							}
							}
							this.state = 576;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 577;
						this.match(EclangParser.NL);
						this.state = 578;
						this.match(EclangParser.DEDENT);
						this.state = 579;
						this.match(EclangParser.T__3);
						}
						break;
					case 19:
						{
						localctx = new ExprSafeCallContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSafeCallContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 581;
						if (!(this.precpred(this._ctx, 44))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 44)");
						}
						this.state = 582;
						this.match(EclangParser.T__18);
						this.state = 583;
						this.match(EclangParser.NAME);
						this.state = 584;
						this.match(EclangParser.T__1);
						this.state = 585;
						this.match(EclangParser.T__3);
						}
						break;
					case 20:
						{
						localctx = new ExprSafeCallContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSafeCallContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 586;
						if (!(this.precpred(this._ctx, 43))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 43)");
						}
						this.state = 587;
						this.match(EclangParser.T__18);
						this.state = 588;
						this.match(EclangParser.NAME);
						this.state = 589;
						this.match(EclangParser.T__1);
						this.state = 590;
						(localctx as ExprSafeCallContext)._argument = this.argument();
						(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
						this.state = 595;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===3) {
							{
							{
							this.state = 591;
							this.match(EclangParser.T__2);
							this.state = 592;
							(localctx as ExprSafeCallContext)._argument = this.argument();
							(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
							}
							}
							this.state = 597;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 598;
						this.match(EclangParser.T__3);
						}
						break;
					case 21:
						{
						localctx = new ExprSafeCallContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSafeCallContext)._args.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 600;
						if (!(this.precpred(this._ctx, 42))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 42)");
						}
						this.state = 601;
						this.match(EclangParser.T__18);
						this.state = 602;
						this.match(EclangParser.NAME);
						this.state = 603;
						this.match(EclangParser.T__1);
						this.state = 604;
						this.match(EclangParser.INDENT);
						this.state = 605;
						(localctx as ExprSafeCallContext)._argument = this.argument();
						(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
						this.state = 611;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						while (_la===3) {
							{
							{
							this.state = 606;
							this.match(EclangParser.T__2);
							this.state = 607;
							this.match(EclangParser.NL);
							this.state = 608;
							(localctx as ExprSafeCallContext)._argument = this.argument();
							(localctx as ExprSafeCallContext)._rest.push((localctx as ExprSafeCallContext)._argument);
							}
							}
							this.state = 613;
							this._errHandler.sync(this);
							_la = this._input.LA(1);
						}
						this.state = 614;
						this.match(EclangParser.NL);
						this.state = 615;
						this.match(EclangParser.DEDENT);
						this.state = 616;
						this.match(EclangParser.T__3);
						}
						break;
					case 22:
						{
						localctx = new ExprAccessNameContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessNameContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 618;
						if (!(this.precpred(this._ctx, 41))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 41)");
						}
						this.state = 619;
						this.match(EclangParser.T__8);
						this.state = 620;
						this.match(EclangParser.NAME);
						}
						break;
					case 23:
						{
						localctx = new ExprAccessNameNullishCoalescingContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessNameNullishCoalescingContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 621;
						if (!(this.precpred(this._ctx, 40))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 40)");
						}
						this.state = 622;
						this.match(EclangParser.T__18);
						this.state = 623;
						this.match(EclangParser.NAME);
						}
						break;
					case 24:
						{
						localctx = new ExprAccessorContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessorContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 624;
						if (!(this.precpred(this._ctx, 39))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 39)");
						}
						this.state = 625;
						this.match(EclangParser.T__15);
						this.state = 626;
						(localctx as ExprAccessorContext)._index = this.expr(0);
						this.state = 627;
						this.match(EclangParser.T__17);
						}
						break;
					case 25:
						{
						localctx = new ExprAccessorContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprAccessorContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 629;
						if (!(this.precpred(this._ctx, 38))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 38)");
						}
						this.state = 630;
						this.match(EclangParser.T__15);
						this.state = 631;
						this.match(EclangParser.INDENT);
						this.state = 632;
						(localctx as ExprAccessorContext)._index = this.expr(0);
						this.state = 633;
						this.match(EclangParser.NL);
						this.state = 634;
						this.match(EclangParser.DEDENT);
						this.state = 635;
						this.match(EclangParser.T__17);
						}
						break;
					case 26:
						{
						localctx = new ExprSliceOpenContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSliceOpenContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 637;
						if (!(this.precpred(this._ctx, 37))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 37)");
						}
						this.state = 638;
						this.match(EclangParser.T__15);
						this.state = 640;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
						if ((((_la) & ~0x1F) === 0 && ((1 << _la) & 2055279078) !== 0) || ((((_la - 33)) & ~0x1F) === 0 && ((1 << (_la - 33)) & 268435471) !== 0) || ((((_la - 96)) & ~0x1F) === 0 && ((1 << (_la - 96)) & 4186113) !== 0)) {
							{
							this.state = 639;
							(localctx as ExprSliceOpenContext)._from_ = this.expr(0);
							}
						}

						this.state = 642;
						this.match(EclangParser.T__19);
						this.state = 643;
						this.match(EclangParser.T__17);
						}
						break;
					case 27:
						{
						localctx = new ExprSliceOpenContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprSliceOpenContext)._accesssed = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 644;
						if (!(this.precpred(this._ctx, 36))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 36)");
						}
						this.state = 645;
						this.match(EclangParser.T__15);
						this.state = 646;
						(localctx as ExprSliceOpenContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if(!(_la===21 || _la===22)) {
						    (localctx as ExprSliceOpenContext)._op = this._errHandler.recoverInline(this);
						}
						else {
							this._errHandler.reportMatch(this);
						    this.consume();
						}
						this.state = 647;
						(localctx as ExprSliceOpenContext)._to = this.expr(0);
						this.state = 648;
						this.match(EclangParser.T__17);
						}
						break;
					case 28:
						{
						localctx = new ExprCastNotNullContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 650;
						if (!(this.precpred(this._ctx, 35))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 35)");
						}
						this.state = 651;
						this.match(EclangParser.T__22);
						}
						break;
					case 29:
						{
						localctx = new ExprAsContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 652;
						if (!(this.precpred(this._ctx, 34))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 34)");
						}
						this.state = 653;
						this.match(EclangParser.T__23);
						this.state = 654;
						this.type_(0);
						}
						break;
					case 30:
						{
						localctx = new ExprAsUnsafeContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 655;
						if (!(this.precpred(this._ctx, 33))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 33)");
						}
						this.state = 656;
						this.match(EclangParser.T__23);
						this.state = 657;
						this.match(EclangParser.T__24);
						this.state = 658;
						this.type_(0);
						}
						break;
					case 31:
						{
						localctx = new ExprAsUnsafeContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 659;
						if (!(this.precpred(this._ctx, 32))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 32)");
						}
						this.state = 660;
						this.match(EclangParser.T__25);
						this.state = 661;
						this.type_(0);
						}
						break;
					case 32:
						{
						localctx = new ExprIsContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 662;
						if (!(this.precpred(this._ctx, 14))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 14)");
						}
						this.state = 663;
						this.match(EclangParser.T__47);
						this.state = 664;
						this.type_(0);
						}
						break;
					case 33:
						{
						localctx = new ExprIsNotContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 665;
						if (!(this.precpred(this._ctx, 13))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 13)");
						}
						this.state = 666;
						this.match(EclangParser.T__48);
						this.state = 667;
						this.type_(0);
						}
						break;
					case 34:
						{
						localctx = new ExprIsNotContext(this, new ExprContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 668;
						if (!(this.precpred(this._ctx, 12))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 12)");
						}
						this.state = 669;
						this.match(EclangParser.T__47);
						this.state = 670;
						this.match(EclangParser.T__32);
						this.state = 671;
						this.type_(0);
						}
						break;
					case 35:
						{
						localctx = new ExprNullishJumpContext(this, new ExprContext(this, _parentctx, _parentState));
						(localctx as ExprNullishJumpContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_expr);
						this.state = 672;
						if (!(this.precpred(this._ctx, 6))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 6)");
						}
						this.state = 673;
						this.match(EclangParser.T__59);
						this.state = 674;
						this.jump();
						}
						break;
					}
					}
				}
				this.state = 679;
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
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 682;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 56, this._ctx) ) {
			case 1:
				{
				this.state = 680;
				localctx._name = this.match(EclangParser.NAME);
				this.state = 681;
				this.match(EclangParser.T__12);
				}
				break;
			}
			this.state = 685;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===66) {
				{
				this.state = 684;
				localctx._mut = this.match(EclangParser.T__65);
				}
			}

			this.state = 687;
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
			this.state = 703;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 67:
				localctx = new JumpReturnContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 689;
				this.match(EclangParser.T__66);
				this.state = 691;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 58, this._ctx) ) {
				case 1:
					{
					this.state = 690;
					(localctx as JumpReturnContext)._value = this.expr(0);
					}
					break;
				}
				}
				break;
			case 68:
				localctx = new JumpThrowContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 693;
				this.match(EclangParser.T__67);
				this.state = 694;
				(localctx as JumpThrowContext)._value = this.expr(0);
				}
				break;
			case 69:
				localctx = new JumpBreakContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 695;
				this.match(EclangParser.T__68);
				this.state = 697;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 59, this._ctx) ) {
				case 1:
					{
					this.state = 696;
					(localctx as JumpBreakContext)._label = this.match(EclangParser.NAME);
					}
					break;
				}
				}
				break;
			case 70:
				localctx = new JumpContinueContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 699;
				this.match(EclangParser.T__69);
				this.state = 701;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 60, this._ctx) ) {
				case 1:
					{
					this.state = 700;
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
			this.state = 710;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 62, this._ctx) ) {
			case 1:
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 705;
				this.expr(0);
				this.state = 706;
				this.match(EclangParser.NL);
				}
				break;
			case 2:
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 708;
				this.whenExpr();
				}
				break;
			case 3:
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 709;
				this.tryCatch();
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
	public tryCatch(): TryCatchContext {
		let localctx: TryCatchContext = new TryCatchContext(this, this._ctx, this.state);
		this.enterRule(localctx, 14, EclangParser.RULE_tryCatch);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 712;
			this.match(EclangParser.T__28);
			this.state = 713;
			localctx._left = this.expr(0);
			this.state = 714;
			this.match(EclangParser.T__70);
			this.state = 715;
			this.match(EclangParser.NAME);
			this.state = 716;
			this.match(EclangParser.T__61);
			this.state = 719;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 1:
			case 2:
			case 5:
			case 6:
			case 7:
			case 8:
			case 12:
			case 16:
			case 23:
			case 25:
			case 27:
			case 28:
			case 29:
			case 30:
			case 33:
			case 34:
			case 35:
			case 36:
			case 61:
			case 96:
			case 109:
			case 110:
			case 111:
			case 112:
			case 113:
			case 114:
			case 115:
			case 116:
			case 117:
				{
				this.state = 717;
				localctx._value = this.tail();
				}
				break;
			case 124:
				{
				this.state = 718;
				localctx._body = this.block();
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
	public lambdaParameter(): LambdaParameterContext {
		let localctx: LambdaParameterContext = new LambdaParameterContext(this, this._ctx, this.state);
		this.enterRule(localctx, 16, EclangParser.RULE_lambdaParameter);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 721;
			this.match(EclangParser.NAME);
			this.state = 724;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===65) {
				{
				this.state = 722;
				this.match(EclangParser.T__64);
				this.state = 723;
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
		let _startState: number = 18;
		this.enterRecursionRule(localctx, 18, EclangParser.RULE_type, _p);
		let _la: number;
		try {
			let _alt: number;
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 827;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 77, this._ctx) ) {
			case 1:
				{
				localctx = new TypeByNameParametersContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;

				this.state = 729;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 65, this._ctx) ) {
				case 1:
					{
					this.state = 727;
					(localctx as TypeByNameParametersContext)._qualifier = this.match(EclangParser.NAME);
					this.state = 728;
					this.match(EclangParser.T__8);
					}
					break;
				}
				this.state = 731;
				(localctx as TypeByNameParametersContext)._name = this.match(EclangParser.NAME);
				this.state = 732;
				this.match(EclangParser.T__9);
				this.state = 733;
				(localctx as TypeByNameParametersContext)._type_ = this.type_(0);
				(localctx as TypeByNameParametersContext)._args.push((localctx as TypeByNameParametersContext)._type_);
				this.state = 738;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 734;
					this.match(EclangParser.T__2);
					this.state = 735;
					(localctx as TypeByNameParametersContext)._type_ = this.type_(0);
					(localctx as TypeByNameParametersContext)._args.push((localctx as TypeByNameParametersContext)._type_);
					}
					}
					this.state = 740;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 741;
				this.match(EclangParser.T__10);
				}
				break;
			case 2:
				{
				localctx = new TypeByNameContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 743;
				this.match(EclangParser.NAME);
				this.state = 744;
				this.match(EclangParser.T__8);
				this.state = 745;
				this.match(EclangParser.NAME);
				}
				break;
			case 3:
				{
				localctx = new TypeByNameContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 746;
				this.match(EclangParser.NAME);
				}
				break;
			case 4:
				{
				localctx = new TypeAddressByNameContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 747;
				this.match(EclangParser.T__26);
				this.state = 748;
				this.type_(16);
				}
				break;
			case 5:
				{
				localctx = new TypeNestContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 749;
				this.match(EclangParser.T__1);
				this.state = 750;
				this.type_(0);
				this.state = 751;
				this.match(EclangParser.T__3);
				}
				break;
			case 6:
				{
				localctx = new TypeAtomContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 753;
				this.match(EclangParser.ATOM);
				}
				break;
			case 7:
				{
				localctx = new TypeGenericContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 754;
				this.match(EclangParser.T__9);
				this.state = 756;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===66) {
					{
					this.state = 755;
					(localctx as TypeGenericContext)._mut = this.match(EclangParser.T__65);
					}
				}

				this.state = 758;
				this.match(EclangParser.NAME);
				this.state = 759;
				this.match(EclangParser.T__10);
				}
				break;
			case 8:
				{
				localctx = new TypeTypeOfContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 760;
				this.match(EclangParser.T__72);
				this.state = 761;
				this.expr(0);
				}
				break;
			case 9:
				{
				localctx = new TypeCallSignatureContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 762;
				this.match(EclangParser.T__7);
				this.state = 763;
				this.match(EclangParser.T__1);
				this.state = 764;
				this.match(EclangParser.T__3);
				this.state = 767;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 68, this._ctx) ) {
				case 1:
					{
					this.state = 765;
					_la = this._input.LA(1);
					if(!(_la===65 || _la===75)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 766;
					(localctx as TypeCallSignatureContext)._returnType = this.type_(0);
					}
					break;
				}
				}
				break;
			case 10:
				{
				localctx = new TypeCallSignatureContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 769;
				this.match(EclangParser.T__7);
				this.state = 770;
				this.match(EclangParser.T__1);
				this.state = 773;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 69, this._ctx) ) {
				case 1:
					{
					this.state = 771;
					this.match(EclangParser.NAME);
					this.state = 772;
					this.match(EclangParser.T__64);
					}
					break;
				}
				this.state = 775;
				(localctx as TypeCallSignatureContext)._type_ = this.type_(0);
				(localctx as TypeCallSignatureContext)._argTypes.push((localctx as TypeCallSignatureContext)._type_);
				this.state = 784;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 776;
					this.match(EclangParser.T__2);
					this.state = 779;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 70, this._ctx) ) {
					case 1:
						{
						this.state = 777;
						this.match(EclangParser.NAME);
						this.state = 778;
						this.match(EclangParser.T__64);
						}
						break;
					}
					this.state = 781;
					(localctx as TypeCallSignatureContext)._type_ = this.type_(0);
					(localctx as TypeCallSignatureContext)._argTypes.push((localctx as TypeCallSignatureContext)._type_);
					}
					}
					this.state = 786;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 787;
				this.match(EclangParser.T__3);
				this.state = 790;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 72, this._ctx) ) {
				case 1:
					{
					this.state = 788;
					_la = this._input.LA(1);
					if(!(_la===65 || _la===75)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 789;
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
				this.state = 792;
				this.match(EclangParser.T__1);
				this.state = 793;
				this.match(EclangParser.T__3);
				this.state = 794;
				this.match(EclangParser.T__74);
				this.state = 795;
				(localctx as TypeCallSignatureContext)._returnType = this.type_(3);
				}
				break;
			case 12:
				{
				localctx = new TypeCallSignatureContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 796;
				this.match(EclangParser.T__1);
				this.state = 799;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 73, this._ctx) ) {
				case 1:
					{
					this.state = 797;
					this.match(EclangParser.NAME);
					this.state = 798;
					this.match(EclangParser.T__64);
					}
					break;
				}
				this.state = 801;
				(localctx as TypeCallSignatureContext)._type_ = this.type_(0);
				(localctx as TypeCallSignatureContext)._argTypes.push((localctx as TypeCallSignatureContext)._type_);
				this.state = 810;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 802;
					this.match(EclangParser.T__2);
					this.state = 805;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 74, this._ctx) ) {
					case 1:
						{
						this.state = 803;
						this.match(EclangParser.NAME);
						this.state = 804;
						this.match(EclangParser.T__64);
						}
						break;
					}
					this.state = 807;
					(localctx as TypeCallSignatureContext)._type_ = this.type_(0);
					(localctx as TypeCallSignatureContext)._argTypes.push((localctx as TypeCallSignatureContext)._type_);
					}
					}
					this.state = 812;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 813;
				this.match(EclangParser.T__3);
				this.state = 814;
				this.match(EclangParser.T__74);
				this.state = 815;
				(localctx as TypeCallSignatureContext)._returnType = this.type_(2);
				}
				break;
			case 13:
				{
				localctx = new TypeTupleContext(this, localctx);
				this._ctx = localctx;
				_prevctx = localctx;
				this.state = 817;
				this.match(EclangParser.T__1);
				this.state = 818;
				(localctx as TypeTupleContext)._type_ = this.type_(0);
				(localctx as TypeTupleContext)._types.push((localctx as TypeTupleContext)._type_);
				this.state = 821;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 819;
					this.match(EclangParser.T__2);
					this.state = 820;
					(localctx as TypeTupleContext)._type_ = this.type_(0);
					(localctx as TypeTupleContext)._types.push((localctx as TypeTupleContext)._type_);
					}
					}
					this.state = 823;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===3);
				this.state = 825;
				this.match(EclangParser.T__3);
				}
				break;
			}
			this._ctx.stop = this._input.LT(-1);
			this.state = 850;
			this._errHandler.sync(this);
			_alt = this._interp.adaptivePredict(this._input, 79, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					if (this._parseListeners != null) {
						this.triggerExitRuleEvent();
					}
					_prevctx = localctx;
					{
					this.state = 848;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 78, this._ctx) ) {
					case 1:
						{
						localctx = new TypeEnumContext(this, new TypeContext(this, _parentctx, _parentState));
						(localctx as TypeEnumContext)._types.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 829;
						if (!(this.precpred(this._ctx, 12))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 12)");
						}
						this.state = 830;
						this.match(EclangParser.T__41);
						this.state = 831;
						(localctx as TypeEnumContext)._type_ = this.type_(13);
						(localctx as TypeEnumContext)._types.push((localctx as TypeEnumContext)._type_);
						}
						break;
					case 2:
						{
						localctx = new TypeUnionContext(this, new TypeContext(this, _parentctx, _parentState));
						(localctx as TypeUnionContext)._types.push(_prevctx);
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 832;
						if (!(this.precpred(this._ctx, 11))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 11)");
						}
						this.state = 833;
						this.match(EclangParser.T__43);
						this.state = 834;
						(localctx as TypeUnionContext)._type_ = this.type_(12);
						(localctx as TypeUnionContext)._types.push((localctx as TypeUnionContext)._type_);
						}
						break;
					case 3:
						{
						localctx = new TypeThrowsContext(this, new TypeContext(this, _parentctx, _parentState));
						(localctx as TypeThrowsContext)._left = _prevctx;
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 835;
						if (!(this.precpred(this._ctx, 6))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 6)");
						}
						this.state = 836;
						this.match(EclangParser.T__73);
						this.state = 837;
						(localctx as TypeThrowsContext)._throwing = this.type_(7);
						}
						break;
					case 4:
						{
						localctx = new TypeNullableContext(this, new TypeContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 838;
						if (!(this.precpred(this._ctx, 15))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 15)");
						}
						this.state = 839;
						this.match(EclangParser.T__71);
						}
						break;
					case 5:
						{
						localctx = new TypeArrayContext(this, new TypeContext(this, _parentctx, _parentState));
						this.pushNewRecursionContext(localctx, _startState, EclangParser.RULE_type);
						this.state = 840;
						if (!(this.precpred(this._ctx, 14))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 14)");
						}
						this.state = 841;
						this.match(EclangParser.T__15);
						this.state = 842;
						_la = this._input.LA(1);
						if(!(_la===17 || _la===18)) {
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
						this.state = 843;
						if (!(this.precpred(this._ctx, 13))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 13)");
						}
						this.state = 844;
						this.match(EclangParser.T__15);
						this.state = 845;
						(localctx as TypeFixedArrayContext)._size = this.expr(0);
						this.state = 846;
						this.match(EclangParser.T__17);
						}
						break;
					}
					}
				}
				this.state = 852;
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
		this.enterRule(localctx, 20, EclangParser.RULE_interface);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 853;
			this.match(EclangParser.T__75);
			this.state = 854;
			localctx._name = this.match(EclangParser.NAME);
			this.state = 855;
			this.match(EclangParser.T__23);
			this.state = 856;
			this.match(EclangParser.INDENT);
			this.state = 862;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 857;
				localctx._NAME = this.match(EclangParser.NAME);
				localctx._names.push(localctx._NAME);
				this.state = 858;
				this.match(EclangParser.T__64);
				this.state = 859;
				localctx._type_ = this.type_(0);
				localctx._types.push(localctx._type_);
				this.state = 860;
				this.match(EclangParser.NL);
				}
				}
				this.state = 864;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while (_la===110);
			this.state = 866;
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
		this.enterRule(localctx, 22, EclangParser.RULE_parameter);
		try {
			this.state = 893;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 81, this._ctx) ) {
			case 1:
				localctx = new ParameterTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 868;
				this.match(EclangParser.NAME);
				this.state = 869;
				this.match(EclangParser.T__64);
				this.state = 870;
				this.type_(0);
				}
				break;
			case 2:
				localctx = new ParameterTypedDefaultContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 871;
				this.match(EclangParser.NAME);
				this.state = 872;
				this.match(EclangParser.T__64);
				this.state = 873;
				this.type_(0);
				this.state = 874;
				this.match(EclangParser.T__12);
				this.state = 875;
				this.expr(0);
				}
				break;
			case 3:
				localctx = new ParameterDefaultContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 877;
				this.match(EclangParser.NAME);
				this.state = 878;
				this.match(EclangParser.T__12);
				this.state = 879;
				this.expr(0);
				}
				break;
			case 4:
				localctx = new ParameterVarArgContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 880;
				this.match(EclangParser.T__14);
				this.state = 881;
				this.match(EclangParser.NAME);
				this.state = 882;
				this.match(EclangParser.T__64);
				this.state = 883;
				this.type_(0);
				}
				break;
			case 5:
				localctx = new ParameterVarArgCContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 884;
				this.match(EclangParser.T__14);
				}
				break;
			case 6:
				localctx = new ParameterMutContext(this, localctx);
				this.enterOuterAlt(localctx, 6);
				{
				this.state = 885;
				this.match(EclangParser.T__65);
				this.state = 886;
				this.parameter();
				}
				break;
			case 7:
				localctx = new ParameterKeywordContext(this, localctx);
				this.enterOuterAlt(localctx, 7);
				{
				this.state = 887;
				(localctx as ParameterKeywordContext)._keyword = this.match(EclangParser.NAME);
				this.state = 888;
				this.parameter();
				}
				break;
			case 8:
				localctx = new ParameterVariableContext(this, localctx);
				this.enterOuterAlt(localctx, 8);
				{
				this.state = 889;
				this.match(EclangParser.T__76);
				this.state = 890;
				this.parameter();
				}
				break;
			case 9:
				localctx = new ParameterConstContext(this, localctx);
				this.enterOuterAlt(localctx, 9);
				{
				this.state = 891;
				this.match(EclangParser.T__77);
				this.state = 892;
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
		this.enterRule(localctx, 24, EclangParser.RULE_statement);
		let _la: number;
		try {
			this.state = 1284;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 118, this._ctx) ) {
			case 1:
				localctx = new StatementDefineVarContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 895;
				this.match(EclangParser.T__76);
				this.state = 896;
				this.match(EclangParser.NAME);
				this.state = 897;
				this.match(EclangParser.T__12);
				this.state = 898;
				this.tail();
				}
				break;
			case 2:
				localctx = new StatementDefineValContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 899;
				this.match(EclangParser.T__77);
				this.state = 900;
				this.match(EclangParser.NAME);
				this.state = 901;
				this.match(EclangParser.T__12);
				this.state = 902;
				this.tail();
				}
				break;
			case 3:
				localctx = new StatementDefineConstContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 903;
				this.match(EclangParser.T__78);
				this.state = 904;
				this.match(EclangParser.NAME);
				this.state = 905;
				this.match(EclangParser.T__12);
				this.state = 906;
				this.expr(0);
				this.state = 907;
				this.match(EclangParser.NL);
				}
				break;
			case 4:
				localctx = new StatementDefineVarTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 909;
				this.match(EclangParser.T__76);
				this.state = 910;
				this.match(EclangParser.NAME);
				this.state = 911;
				this.match(EclangParser.T__64);
				this.state = 912;
				this.type_(0);
				this.state = 913;
				this.match(EclangParser.T__12);
				this.state = 914;
				this.tail();
				}
				break;
			case 5:
				localctx = new StatementDefineValTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 916;
				this.match(EclangParser.T__77);
				this.state = 917;
				this.match(EclangParser.NAME);
				this.state = 918;
				this.match(EclangParser.T__64);
				this.state = 919;
				this.type_(0);
				this.state = 920;
				this.match(EclangParser.T__12);
				this.state = 921;
				this.tail();
				}
				break;
			case 6:
				localctx = new StatementDefineConstTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 6);
				{
				this.state = 923;
				this.match(EclangParser.T__78);
				this.state = 924;
				this.match(EclangParser.NAME);
				this.state = 925;
				this.match(EclangParser.T__64);
				this.state = 926;
				this.type_(0);
				this.state = 927;
				this.match(EclangParser.T__12);
				this.state = 928;
				this.expr(0);
				this.state = 929;
				this.match(EclangParser.NL);
				}
				break;
			case 7:
				localctx = new StatementDeclareVarContext(this, localctx);
				this.enterOuterAlt(localctx, 7);
				{
				this.state = 931;
				this.match(EclangParser.T__76);
				this.state = 932;
				this.match(EclangParser.NAME);
				this.state = 933;
				this.match(EclangParser.T__64);
				this.state = 934;
				this.type_(0);
				this.state = 935;
				this.match(EclangParser.NL);
				}
				break;
			case 8:
				localctx = new StatementDestructureContext(this, localctx);
				this.enterOuterAlt(localctx, 8);
				{
				this.state = 937;
				(localctx as StatementDestructureContext)._kind = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===77 || _la===78)) {
				    (localctx as StatementDestructureContext)._kind = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 938;
				this.destructure();
				this.state = 941;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===65) {
					{
					this.state = 939;
					this.match(EclangParser.T__64);
					this.state = 940;
					this.type_(0);
					}
				}

				this.state = 943;
				this.match(EclangParser.T__12);
				this.state = 944;
				this.tail();
				}
				break;
			case 9:
				localctx = new StatementAssignmentContext(this, localctx);
				this.enterOuterAlt(localctx, 9);
				{
				this.state = 946;
				(localctx as StatementAssignmentContext)._target = this.expr(0);
				this.state = 947;
				(localctx as StatementAssignmentContext)._op = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===13 || ((((_la - 80)) & ~0x1F) === 0 && ((1 << (_la - 80)) & 1023) !== 0))) {
				    (localctx as StatementAssignmentContext)._op = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 948;
				(localctx as StatementAssignmentContext)._value = this.tail();
				}
				break;
			case 10:
				localctx = new StatementBreakContext(this, localctx);
				this.enterOuterAlt(localctx, 10);
				{
				this.state = 950;
				this.match(EclangParser.T__68);
				this.state = 952;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 951;
					(localctx as StatementBreakContext)._label = this.match(EclangParser.NAME);
					}
				}

				this.state = 956;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===61) {
					{
					this.state = 954;
					this.match(EclangParser.T__60);
					this.state = 955;
					(localctx as StatementBreakContext)._condition = this.expr(0);
					}
				}

				this.state = 958;
				this.match(EclangParser.NL);
				}
				break;
			case 11:
				localctx = new StatementContinueContext(this, localctx);
				this.enterOuterAlt(localctx, 11);
				{
				this.state = 959;
				this.match(EclangParser.T__69);
				this.state = 961;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 960;
					(localctx as StatementContinueContext)._label = this.match(EclangParser.NAME);
					}
				}

				this.state = 965;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===61) {
					{
					this.state = 963;
					this.match(EclangParser.T__60);
					this.state = 964;
					(localctx as StatementContinueContext)._condition = this.expr(0);
					}
				}

				this.state = 967;
				this.match(EclangParser.NL);
				}
				break;
			case 12:
				localctx = new StatementPassContext(this, localctx);
				this.enterOuterAlt(localctx, 12);
				{
				this.state = 968;
				this.match(EclangParser.T__89);
				this.state = 971;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===61) {
					{
					this.state = 969;
					this.match(EclangParser.T__60);
					this.state = 970;
					(localctx as StatementPassContext)._condition = this.expr(0);
					}
				}

				this.state = 973;
				this.match(EclangParser.NL);
				}
				break;
			case 13:
				localctx = new StatementYieldContext(this, localctx);
				this.enterOuterAlt(localctx, 13);
				{
				this.state = 974;
				this.match(EclangParser.T__90);
				this.state = 977;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===61) {
					{
					this.state = 975;
					this.match(EclangParser.T__60);
					this.state = 976;
					(localctx as StatementYieldContext)._condition = this.expr(0);
					}
				}

				this.state = 979;
				this.match(EclangParser.NL);
				}
				break;
			case 14:
				localctx = new StatementThrowContext(this, localctx);
				this.enterOuterAlt(localctx, 14);
				{
				this.state = 980;
				this.match(EclangParser.T__67);
				this.state = 981;
				(localctx as StatementThrowContext)._throw_ = this.expr(0);
				this.state = 984;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===61) {
					{
					this.state = 982;
					this.match(EclangParser.T__60);
					this.state = 983;
					(localctx as StatementThrowContext)._condition = this.expr(0);
					}
				}

				this.state = 986;
				this.match(EclangParser.NL);
				}
				break;
			case 15:
				localctx = new StatementAssertContext(this, localctx);
				this.enterOuterAlt(localctx, 15);
				{
				this.state = 988;
				this.match(EclangParser.T__91);
				this.state = 989;
				(localctx as StatementAssertContext)._condition = this.expr(0);
				this.state = 992;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===3) {
					{
					this.state = 990;
					this.match(EclangParser.T__2);
					this.state = 991;
					(localctx as StatementAssertContext)._message = this.expr(0);
					}
				}

				this.state = 994;
				this.match(EclangParser.NL);
				}
				break;
			case 16:
				localctx = new StatementConstAssertContext(this, localctx);
				this.enterOuterAlt(localctx, 16);
				{
				this.state = 996;
				this.constAssert();
				}
				break;
			case 17:
				localctx = new StatementReturnContext(this, localctx);
				this.enterOuterAlt(localctx, 17);
				{
				this.state = 997;
				this.match(EclangParser.T__66);
				this.state = 999;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 91, this._ctx) ) {
				case 1:
					{
					this.state = 998;
					(localctx as StatementReturnContext)._return_ = this.expr(0);
					}
					break;
				}
				this.state = 1003;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===61) {
					{
					this.state = 1001;
					this.match(EclangParser.T__60);
					this.state = 1002;
					(localctx as StatementReturnContext)._condition = this.expr(0);
					}
				}

				this.state = 1005;
				this.match(EclangParser.NL);
				}
				break;
			case 18:
				localctx = new StatementReturnContext(this, localctx);
				this.enterOuterAlt(localctx, 18);
				{
				this.state = 1006;
				this.match(EclangParser.T__66);
				this.state = 1007;
				this.whenExpr();
				}
				break;
			case 19:
				localctx = new StatementTailIfContext(this, localctx);
				this.enterOuterAlt(localctx, 19);
				{
				this.state = 1008;
				(localctx as StatementTailIfContext)._expression = this.expr(0);
				this.state = 1009;
				this.match(EclangParser.T__60);
				this.state = 1010;
				(localctx as StatementTailIfContext)._condition = this.expr(0);
				this.state = 1011;
				this.match(EclangParser.NL);
				}
				break;
			case 20:
				localctx = new StatementNullishContext(this, localctx);
				this.enterOuterAlt(localctx, 20);
				{
				this.state = 1013;
				(localctx as StatementNullishContext)._left = this.expr(0);
				this.state = 1014;
				this.match(EclangParser.T__59);
				this.state = 1017;
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case 67:
				case 68:
				case 69:
				case 70:
					{
					this.state = 1015;
					this.jump();
					}
					break;
				case 1:
				case 2:
				case 5:
				case 6:
				case 7:
				case 8:
				case 12:
				case 16:
				case 23:
				case 25:
				case 27:
				case 28:
				case 29:
				case 30:
				case 33:
				case 34:
				case 35:
				case 36:
				case 61:
				case 96:
				case 109:
				case 110:
				case 111:
				case 112:
				case 113:
				case 114:
				case 115:
				case 116:
				case 117:
					{
					this.state = 1016;
					(localctx as StatementNullishContext)._right = this.expr(0);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				this.state = 1019;
				this.match(EclangParser.NL);
				}
				break;
			case 21:
				localctx = new StatementTailWhileContext(this, localctx);
				this.enterOuterAlt(localctx, 21);
				{
				this.state = 1021;
				(localctx as StatementTailWhileContext)._expression = this.expr(0);
				this.state = 1022;
				this.match(EclangParser.T__92);
				this.state = 1023;
				(localctx as StatementTailWhileContext)._condition = this.expr(0);
				this.state = 1024;
				this.match(EclangParser.NL);
				}
				break;
			case 22:
				localctx = new StatementDeferContext(this, localctx);
				this.enterOuterAlt(localctx, 22);
				{
				this.state = 1026;
				this.match(EclangParser.T__93);
				this.state = 1027;
				this.statement();
				}
				break;
			case 23:
				localctx = new StatementDeferDoContext(this, localctx);
				this.enterOuterAlt(localctx, 23);
				{
				this.state = 1028;
				this.match(EclangParser.T__93);
				this.state = 1029;
				this.match(EclangParser.T__61);
				this.state = 1030;
				(localctx as StatementDeferDoContext)._body = this.block();
				}
				break;
			case 24:
				localctx = new StatementErrDeferContext(this, localctx);
				this.enterOuterAlt(localctx, 24);
				{
				this.state = 1031;
				this.match(EclangParser.T__94);
				this.state = 1032;
				this.statement();
				}
				break;
			case 25:
				localctx = new StatementErrDeferDoContext(this, localctx);
				this.enterOuterAlt(localctx, 25);
				{
				this.state = 1033;
				this.match(EclangParser.T__94);
				this.state = 1034;
				this.match(EclangParser.T__61);
				this.state = 1035;
				(localctx as StatementErrDeferDoContext)._body = this.block();
				}
				break;
			case 26:
				localctx = new StatementWhenContext(this, localctx);
				this.enterOuterAlt(localctx, 26);
				{
				this.state = 1036;
				this.match(EclangParser.T__95);
				this.state = 1040;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===77 || _la===78) {
					{
					this.state = 1037;
					(localctx as StatementWhenContext)._kind = this._input.LT(1);
					_la = this._input.LA(1);
					if(!(_la===77 || _la===78)) {
					    (localctx as StatementWhenContext)._kind = this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 1038;
					this.match(EclangParser.NAME);
					this.state = 1039;
					this.match(EclangParser.T__12);
					}
				}

				this.state = 1042;
				(localctx as StatementWhenContext)._subject = this.expr(0);
				this.state = 1043;
				this.match(EclangParser.T__61);
				this.state = 1044;
				this.match(EclangParser.INDENT);
				this.state = 1046;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1045;
					(localctx as StatementWhenContext)._whenArm = this.whenArm();
					(localctx as StatementWhenContext)._arms.push((localctx as StatementWhenContext)._whenArm);
					}
					}
					this.state = 1048;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while ((((_la) & ~0x1F) === 0 && ((1 << _la) & 2061570534) !== 0) || ((((_la - 33)) & ~0x1F) === 0 && ((1 << (_la - 33)) & 1342210063) !== 0) || ((((_la - 96)) & ~0x1F) === 0 && ((1 << (_la - 96)) & 4186113) !== 0));
				this.state = 1050;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 27:
				localctx = new StatementLetIfContext(this, localctx);
				this.enterOuterAlt(localctx, 27);
				{
				this.state = 1052;
				this.match(EclangParser.T__60);
				this.state = 1053;
				(localctx as StatementLetIfContext)._kind = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===77 || _la===78)) {
				    (localctx as StatementLetIfContext)._kind = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1054;
				this.match(EclangParser.NAME);
				this.state = 1055;
				this.match(EclangParser.T__12);
				this.state = 1056;
				(localctx as StatementLetIfContext)._value = this.expr(0);
				this.state = 1057;
				this.match(EclangParser.T__61);
				this.state = 1058;
				(localctx as StatementLetIfContext)._body = this.block();
				this.state = 1066;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 97, this._ctx) ) {
				case 1:
					{
					this.state = 1059;
					this.match(EclangParser.T__62);
					this.state = 1061;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===62) {
						{
						this.state = 1060;
						this.match(EclangParser.T__61);
						}
					}

					this.state = 1063;
					(localctx as StatementLetIfContext)._elseBody = this.block();
					}
					break;
				case 2:
					{
					this.state = 1064;
					this.match(EclangParser.T__62);
					this.state = 1065;
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
				this.state = 1068;
				this.match(EclangParser.T__60);
				this.state = 1069;
				(localctx as StatementIfContext)._condition = this.expr(0);
				this.state = 1070;
				this.match(EclangParser.T__61);
				this.state = 1071;
				(localctx as StatementIfContext)._body = this.block();
				this.state = 1079;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 99, this._ctx) ) {
				case 1:
					{
					this.state = 1072;
					this.match(EclangParser.T__62);
					this.state = 1074;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===62) {
						{
						this.state = 1073;
						this.match(EclangParser.T__61);
						}
					}

					this.state = 1076;
					(localctx as StatementIfContext)._elseBody = this.block();
					}
					break;
				case 2:
					{
					this.state = 1077;
					this.match(EclangParser.T__62);
					this.state = 1078;
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
				this.state = 1081;
				(localctx as StatementLabeledContext)._label = this.match(EclangParser.NAME);
				this.state = 1082;
				this.match(EclangParser.T__64);
				this.state = 1083;
				this.statement();
				}
				break;
			case 30:
				localctx = new StatementWhileContext(this, localctx);
				this.enterOuterAlt(localctx, 30);
				{
				this.state = 1084;
				this.match(EclangParser.T__92);
				this.state = 1085;
				(localctx as StatementWhileContext)._condition = this.expr(0);
				this.state = 1086;
				this.match(EclangParser.T__61);
				this.state = 1087;
				(localctx as StatementWhileContext)._body = this.block();
				this.state = 1093;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 101, this._ctx) ) {
				case 1:
					{
					this.state = 1088;
					this.match(EclangParser.T__62);
					this.state = 1090;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===62) {
						{
						this.state = 1089;
						this.match(EclangParser.T__61);
						}
					}

					this.state = 1092;
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
				this.state = 1095;
				this.match(EclangParser.T__96);
				this.state = 1097;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===77 || _la===78) {
					{
					this.state = 1096;
					_la = this._input.LA(1);
					if(!(_la===77 || _la===78)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					}
				}

				this.state = 1099;
				this.match(EclangParser.NAME);
				this.state = 1100;
				this.match(EclangParser.T__30);
				this.state = 1101;
				(localctx as StatementForInRangeContext)._lower = this.expr(0);
				this.state = 1102;
				this.match(EclangParser.T__97);
				this.state = 1103;
				(localctx as StatementForInRangeContext)._upper = this.expr(0);
				this.state = 1104;
				this.match(EclangParser.T__61);
				this.state = 1105;
				(localctx as StatementForInRangeContext)._body = this.block();
				this.state = 1111;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 104, this._ctx) ) {
				case 1:
					{
					this.state = 1106;
					this.match(EclangParser.T__62);
					this.state = 1108;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===62) {
						{
						this.state = 1107;
						this.match(EclangParser.T__61);
						}
					}

					this.state = 1110;
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
				this.state = 1113;
				this.match(EclangParser.T__96);
				this.state = 1115;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===77 || _la===78) {
					{
					this.state = 1114;
					_la = this._input.LA(1);
					if(!(_la===77 || _la===78)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					}
				}

				this.state = 1119;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 106, this._ctx) ) {
				case 1:
					{
					this.state = 1117;
					(localctx as StatementForInContext)._index = this.match(EclangParser.NAME);
					this.state = 1118;
					this.match(EclangParser.T__2);
					}
					break;
				}
				this.state = 1123;
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case 110:
					{
					this.state = 1121;
					(localctx as StatementForInContext)._name = this.match(EclangParser.NAME);
					}
					break;
				case 2:
					{
					this.state = 1122;
					(localctx as StatementForInContext)._parts = this.destructure();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				this.state = 1125;
				this.match(EclangParser.T__30);
				this.state = 1126;
				(localctx as StatementForInContext)._iterable = this.expr(0);
				this.state = 1129;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 1127;
					(localctx as StatementForInContext)._where = this.match(EclangParser.NAME);
					this.state = 1128;
					(localctx as StatementForInContext)._filter = this.expr(0);
					}
				}

				this.state = 1131;
				this.match(EclangParser.T__61);
				this.state = 1132;
				(localctx as StatementForInContext)._body = this.block();
				this.state = 1138;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 110, this._ctx) ) {
				case 1:
					{
					this.state = 1133;
					this.match(EclangParser.T__62);
					this.state = 1135;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					if (_la===62) {
						{
						this.state = 1134;
						this.match(EclangParser.T__61);
						}
					}

					this.state = 1137;
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
				this.state = 1140;
				this.match(EclangParser.T__28);
				this.state = 1141;
				(localctx as StatementTryContext)._left = this.expr(0);
				this.state = 1142;
				this.match(EclangParser.NL);
				}
				break;
			case 34:
				localctx = new StatementTryCatchContext(this, localctx);
				this.enterOuterAlt(localctx, 34);
				{
				this.state = 1144;
				this.match(EclangParser.T__28);
				this.state = 1145;
				(localctx as StatementTryCatchContext)._left = this.expr(0);
				this.state = 1146;
				this.match(EclangParser.T__70);
				this.state = 1147;
				this.match(EclangParser.NAME);
				this.state = 1148;
				this.match(EclangParser.T__61);
				this.state = 1149;
				(localctx as StatementTryCatchContext)._elseBody = this.block();
				}
				break;
			case 35:
				localctx = new StatementFunctionCallNoArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 35);
				{
				this.state = 1151;
				this.match(EclangParser.NAME);
				this.state = 1152;
				this.match(EclangParser.T__1);
				this.state = 1153;
				this.match(EclangParser.T__3);
				this.state = 1154;
				this.match(EclangParser.NL);
				}
				break;
			case 36:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 36);
				{
				this.state = 1155;
				this.match(EclangParser.NAME);
				this.state = 1156;
				this.match(EclangParser.T__1);
				this.state = 1157;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1162;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 1158;
					this.match(EclangParser.T__2);
					this.state = 1159;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1164;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1165;
				this.match(EclangParser.T__3);
				this.state = 1166;
				this.match(EclangParser.NL);
				}
				break;
			case 37:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 37);
				{
				this.state = 1168;
				this.match(EclangParser.NAME);
				this.state = 1169;
				this.match(EclangParser.T__1);
				this.state = 1170;
				this.match(EclangParser.INDENT);
				this.state = 1171;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1177;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 1172;
					this.match(EclangParser.T__2);
					this.state = 1173;
					this.match(EclangParser.NL);
					this.state = 1174;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1179;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1180;
				this.match(EclangParser.NL);
				this.state = 1181;
				this.match(EclangParser.DEDENT);
				this.state = 1182;
				this.match(EclangParser.T__3);
				this.state = 1183;
				this.match(EclangParser.NL);
				}
				break;
			case 38:
				localctx = new StatementFunctionCallNoArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 38);
				{
				this.state = 1185;
				(localctx as StatementFunctionCallNoArgsContext)._expr = this.expr(0);
				(localctx as StatementFunctionCallNoArgsContext)._args.push((localctx as StatementFunctionCallNoArgsContext)._expr);
				this.state = 1186;
				this.match(EclangParser.T__8);
				this.state = 1187;
				this.match(EclangParser.NAME);
				this.state = 1188;
				this.match(EclangParser.T__1);
				this.state = 1189;
				this.match(EclangParser.T__3);
				this.state = 1190;
				this.match(EclangParser.NL);
				}
				break;
			case 39:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 39);
				{
				this.state = 1192;
				(localctx as StatementFunctionCallWithArgsContext)._expr = this.expr(0);
				(localctx as StatementFunctionCallWithArgsContext)._args.push((localctx as StatementFunctionCallWithArgsContext)._expr);
				this.state = 1193;
				this.match(EclangParser.T__8);
				this.state = 1194;
				this.match(EclangParser.NAME);
				this.state = 1195;
				this.match(EclangParser.T__1);
				this.state = 1196;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1201;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 1197;
					this.match(EclangParser.T__2);
					this.state = 1198;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1203;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1204;
				this.match(EclangParser.T__3);
				this.state = 1205;
				this.match(EclangParser.NL);
				}
				break;
			case 40:
				localctx = new StatementFunctionCallWithArgsContext(this, localctx);
				this.enterOuterAlt(localctx, 40);
				{
				this.state = 1207;
				(localctx as StatementFunctionCallWithArgsContext)._expr = this.expr(0);
				(localctx as StatementFunctionCallWithArgsContext)._args.push((localctx as StatementFunctionCallWithArgsContext)._expr);
				this.state = 1208;
				this.match(EclangParser.T__8);
				this.state = 1209;
				this.match(EclangParser.NAME);
				this.state = 1210;
				this.match(EclangParser.T__1);
				this.state = 1211;
				this.match(EclangParser.INDENT);
				this.state = 1212;
				(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
				(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
				this.state = 1218;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 1213;
					this.match(EclangParser.T__2);
					this.state = 1214;
					this.match(EclangParser.NL);
					this.state = 1215;
					(localctx as StatementFunctionCallWithArgsContext)._argument = this.argument();
					(localctx as StatementFunctionCallWithArgsContext)._rest.push((localctx as StatementFunctionCallWithArgsContext)._argument);
					}
					}
					this.state = 1220;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1221;
				this.match(EclangParser.NL);
				this.state = 1222;
				this.match(EclangParser.DEDENT);
				this.state = 1223;
				this.match(EclangParser.T__3);
				this.state = 1224;
				this.match(EclangParser.NL);
				}
				break;
			case 41:
				localctx = new StatementSafeCallContext(this, localctx);
				this.enterOuterAlt(localctx, 41);
				{
				this.state = 1226;
				(localctx as StatementSafeCallContext)._expr = this.expr(0);
				(localctx as StatementSafeCallContext)._args.push((localctx as StatementSafeCallContext)._expr);
				this.state = 1227;
				this.match(EclangParser.T__18);
				this.state = 1228;
				this.match(EclangParser.NAME);
				this.state = 1229;
				this.match(EclangParser.T__1);
				this.state = 1230;
				this.match(EclangParser.T__3);
				this.state = 1231;
				this.match(EclangParser.NL);
				}
				break;
			case 42:
				localctx = new StatementSafeCallContext(this, localctx);
				this.enterOuterAlt(localctx, 42);
				{
				this.state = 1233;
				(localctx as StatementSafeCallContext)._expr = this.expr(0);
				(localctx as StatementSafeCallContext)._args.push((localctx as StatementSafeCallContext)._expr);
				this.state = 1234;
				this.match(EclangParser.T__18);
				this.state = 1235;
				this.match(EclangParser.NAME);
				this.state = 1236;
				this.match(EclangParser.T__1);
				this.state = 1237;
				(localctx as StatementSafeCallContext)._argument = this.argument();
				(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
				this.state = 1242;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 1238;
					this.match(EclangParser.T__2);
					this.state = 1239;
					(localctx as StatementSafeCallContext)._argument = this.argument();
					(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
					}
					}
					this.state = 1244;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1245;
				this.match(EclangParser.T__3);
				this.state = 1246;
				this.match(EclangParser.NL);
				}
				break;
			case 43:
				localctx = new StatementSafeCallContext(this, localctx);
				this.enterOuterAlt(localctx, 43);
				{
				this.state = 1248;
				(localctx as StatementSafeCallContext)._expr = this.expr(0);
				(localctx as StatementSafeCallContext)._args.push((localctx as StatementSafeCallContext)._expr);
				this.state = 1249;
				this.match(EclangParser.T__18);
				this.state = 1250;
				this.match(EclangParser.NAME);
				this.state = 1251;
				this.match(EclangParser.T__1);
				this.state = 1252;
				this.match(EclangParser.INDENT);
				this.state = 1253;
				(localctx as StatementSafeCallContext)._argument = this.argument();
				(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
				this.state = 1259;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 1254;
					this.match(EclangParser.T__2);
					this.state = 1255;
					this.match(EclangParser.NL);
					this.state = 1256;
					(localctx as StatementSafeCallContext)._argument = this.argument();
					(localctx as StatementSafeCallContext)._rest.push((localctx as StatementSafeCallContext)._argument);
					}
					}
					this.state = 1261;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1262;
				this.match(EclangParser.NL);
				this.state = 1263;
				this.match(EclangParser.DEDENT);
				this.state = 1264;
				this.match(EclangParser.T__3);
				this.state = 1265;
				this.match(EclangParser.NL);
				}
				break;
			case 44:
				localctx = new StatementUnsafeContext(this, localctx);
				this.enterOuterAlt(localctx, 44);
				{
				this.state = 1267;
				this.match(EclangParser.T__24);
				this.state = 1268;
				this.match(EclangParser.T__61);
				this.state = 1269;
				(localctx as StatementUnsafeContext)._body = this.block();
				}
				break;
			case 45:
				localctx = new StatementUnsafeLineContext(this, localctx);
				this.enterOuterAlt(localctx, 45);
				{
				this.state = 1270;
				this.match(EclangParser.T__24);
				this.state = 1271;
				this.statement();
				}
				break;
			case 46:
				localctx = new StatementDestroyContext(this, localctx);
				this.enterOuterAlt(localctx, 46);
				{
				this.state = 1272;
				this.match(EclangParser.T__98);
				this.state = 1273;
				(localctx as StatementDestroyContext)._target = this.expr(0);
				this.state = 1274;
				this.match(EclangParser.NL);
				}
				break;
			case 47:
				localctx = new StatementPoolContext(this, localctx);
				this.enterOuterAlt(localctx, 47);
				{
				this.state = 1276;
				(localctx as StatementPoolContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1278;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 1277;
					(localctx as StatementPoolContext)._name = this.match(EclangParser.NAME);
					}
				}

				this.state = 1280;
				this.match(EclangParser.T__61);
				this.state = 1281;
				(localctx as StatementPoolContext)._body = this.block();
				}
				break;
			case 48:
				localctx = new StatementPrefixedContext(this, localctx);
				this.enterOuterAlt(localctx, 48);
				{
				this.state = 1282;
				(localctx as StatementPrefixedContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1283;
				this.statement();
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
	public constAssert(): ConstAssertContext {
		let localctx: ConstAssertContext = new ConstAssertContext(this, this._ctx, this.state);
		this.enterRule(localctx, 26, EclangParser.RULE_constAssert);
		let _la: number;
		try {
			this.state = 1317;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 122, this._ctx) ) {
			case 1:
				localctx = new ConstAssertRelationContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1286;
				this.match(EclangParser.T__78);
				this.state = 1287;
				this.match(EclangParser.T__91);
				this.state = 1288;
				(localctx as ConstAssertRelationContext)._left = this.type_(0);
				this.state = 1289;
				(localctx as ConstAssertRelationContext)._relation = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===48 || _la===110)) {
				    (localctx as ConstAssertRelationContext)._relation = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1290;
				(localctx as ConstAssertRelationContext)._right = this.type_(0);
				this.state = 1293;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===3) {
					{
					this.state = 1291;
					this.match(EclangParser.T__2);
					this.state = 1292;
					(localctx as ConstAssertRelationContext)._message = this.match(EclangParser.STRING);
					}
				}

				this.state = 1295;
				this.match(EclangParser.NL);
				}
				break;
			case 2:
				localctx = new ConstAssertInContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1297;
				this.match(EclangParser.T__78);
				this.state = 1298;
				this.match(EclangParser.T__91);
				this.state = 1299;
				(localctx as ConstAssertInContext)._value = this.expr(0);
				this.state = 1300;
				this.match(EclangParser.T__30);
				this.state = 1301;
				(localctx as ConstAssertInContext)._pool = this.expr(0);
				this.state = 1304;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===3) {
					{
					this.state = 1302;
					this.match(EclangParser.T__2);
					this.state = 1303;
					(localctx as ConstAssertInContext)._message = this.match(EclangParser.STRING);
					}
				}

				this.state = 1306;
				this.match(EclangParser.NL);
				}
				break;
			case 3:
				localctx = new ConstAssertConditionContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1308;
				this.match(EclangParser.T__78);
				this.state = 1309;
				this.match(EclangParser.T__91);
				this.state = 1310;
				(localctx as ConstAssertConditionContext)._condition = this.expr(0);
				this.state = 1313;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===3) {
					{
					this.state = 1311;
					this.match(EclangParser.T__2);
					this.state = 1312;
					(localctx as ConstAssertConditionContext)._message = this.match(EclangParser.STRING);
					}
				}

				this.state = 1315;
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
	public contract(): ContractContext {
		let localctx: ContractContext = new ContractContext(this, this._ctx, this.state);
		this.enterRule(localctx, 28, EclangParser.RULE_contract);
		let _la: number;
		try {
			this.state = 1332;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 125, this._ctx) ) {
			case 1:
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1319;
				localctx._kind = this.match(EclangParser.NAME);
				this.state = 1320;
				localctx._condition = this.expr(0);
				this.state = 1323;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===3) {
					{
					this.state = 1321;
					this.match(EclangParser.T__2);
					this.state = 1322;
					localctx._message = this.expr(0);
					}
				}

				}
				break;
			case 2:
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1325;
				localctx._debug = this.match(EclangParser.NAME);
				this.state = 1326;
				localctx._kind = this.match(EclangParser.NAME);
				this.state = 1327;
				localctx._condition = this.expr(0);
				this.state = 1330;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===3) {
					{
					this.state = 1328;
					this.match(EclangParser.T__2);
					this.state = 1329;
					localctx._message = this.expr(0);
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
	public destructure(): DestructureContext {
		let localctx: DestructureContext = new DestructureContext(this, this._ctx, this.state);
		this.enterRule(localctx, 30, EclangParser.RULE_destructure);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1334;
			this.match(EclangParser.T__1);
			this.state = 1335;
			localctx._destructurePart = this.destructurePart();
			localctx._parts.push(localctx._destructurePart);
			this.state = 1338;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 1336;
				this.match(EclangParser.T__2);
				this.state = 1337;
				localctx._destructurePart = this.destructurePart();
				localctx._parts.push(localctx._destructurePart);
				}
				}
				this.state = 1340;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while (_la===3);
			this.state = 1342;
			this.match(EclangParser.T__3);
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
		this.enterRule(localctx, 32, EclangParser.RULE_destructurePart);
		try {
			this.state = 1346;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 110:
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1344;
				this.match(EclangParser.NAME);
				}
				break;
			case 2:
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1345;
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
		this.enterRule(localctx, 34, EclangParser.RULE_whenPattern);
		let _la: number;
		try {
			this.state = 1370;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 131, this._ctx) ) {
			case 1:
				localctx = new WhenPatternIsContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1348;
				this.match(EclangParser.T__47);
				this.state = 1349;
				this.type_(0);
				this.state = 1352;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===61) {
					{
					this.state = 1350;
					this.match(EclangParser.T__60);
					this.state = 1351;
					(localctx as WhenPatternIsContext)._guard = this.expr(0);
					}
				}

				}
				break;
			case 2:
				localctx = new WhenPatternElseContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1354;
				this.match(EclangParser.T__62);
				}
				break;
			case 3:
				localctx = new WhenPatternBindContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1355;
				this.match(EclangParser.NAME);
				this.state = 1356;
				this.match(EclangParser.T__60);
				this.state = 1357;
				(localctx as WhenPatternBindContext)._guard = this.expr(0);
				}
				break;
			case 4:
				localctx = new WhenPatternValuesContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 1358;
				(localctx as WhenPatternValuesContext)._whenValue = this.whenValue();
				(localctx as WhenPatternValuesContext)._values.push((localctx as WhenPatternValuesContext)._whenValue);
				this.state = 1363;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 1359;
					this.match(EclangParser.T__2);
					this.state = 1360;
					(localctx as WhenPatternValuesContext)._whenValue = this.whenValue();
					(localctx as WhenPatternValuesContext)._values.push((localctx as WhenPatternValuesContext)._whenValue);
					}
					}
					this.state = 1365;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1368;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===61) {
					{
					this.state = 1366;
					this.match(EclangParser.T__60);
					this.state = 1367;
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
		this.enterRule(localctx, 36, EclangParser.RULE_whenValue);
		let _la: number;
		try {
			this.state = 1378;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 132, this._ctx) ) {
			case 1:
				localctx = new WhenValuePlainContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1372;
				this.expr(0);
				}
				break;
			case 2:
				localctx = new WhenValueFromContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1373;
				(localctx as WhenValueFromContext)._from_ = this.expr(0);
				this.state = 1374;
				this.match(EclangParser.T__19);
				}
				break;
			case 3:
				localctx = new WhenValueUpToContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1376;
				(localctx as WhenValueUpToContext)._op = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===21 || _la===22)) {
				    (localctx as WhenValueUpToContext)._op = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1377;
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
		this.enterRule(localctx, 38, EclangParser.RULE_whenArm);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1380;
			this.whenPattern();
			this.state = 1381;
			this.match(EclangParser.T__61);
			this.state = 1384;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 124:
				{
				this.state = 1382;
				localctx._body = this.block();
				}
				break;
			case 1:
			case 2:
			case 5:
			case 6:
			case 7:
			case 8:
			case 12:
			case 16:
			case 23:
			case 25:
			case 27:
			case 28:
			case 29:
			case 30:
			case 33:
			case 34:
			case 35:
			case 36:
			case 61:
			case 67:
			case 68:
			case 69:
			case 70:
			case 77:
			case 78:
			case 79:
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 95:
			case 96:
			case 97:
			case 99:
			case 109:
			case 110:
			case 111:
			case 112:
			case 113:
			case 114:
			case 115:
			case 116:
			case 117:
				{
				this.state = 1383;
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
		this.enterRule(localctx, 40, EclangParser.RULE_whenExpr);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1386;
			this.match(EclangParser.T__95);
			this.state = 1390;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===77 || _la===78) {
				{
				this.state = 1387;
				localctx._kind = this._input.LT(1);
				_la = this._input.LA(1);
				if(!(_la===77 || _la===78)) {
				    localctx._kind = this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1388;
				this.match(EclangParser.NAME);
				this.state = 1389;
				this.match(EclangParser.T__12);
				}
			}

			this.state = 1392;
			localctx._subject = this.expr(0);
			this.state = 1393;
			this.match(EclangParser.T__61);
			this.state = 1394;
			this.match(EclangParser.INDENT);
			this.state = 1396;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				{
				this.state = 1395;
				localctx._whenValueArm = this.whenValueArm();
				localctx._arms.push(localctx._whenValueArm);
				}
				}
				this.state = 1398;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while ((((_la) & ~0x1F) === 0 && ((1 << _la) & 2061570534) !== 0) || ((((_la - 33)) & ~0x1F) === 0 && ((1 << (_la - 33)) & 1342210063) !== 0) || ((((_la - 96)) & ~0x1F) === 0 && ((1 << (_la - 96)) & 4186113) !== 0));
			this.state = 1400;
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
		this.enterRule(localctx, 42, EclangParser.RULE_whenValueArm);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1402;
			this.whenPattern();
			this.state = 1403;
			this.match(EclangParser.T__61);
			this.state = 1407;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 136, this._ctx) ) {
			case 1:
				{
				this.state = 1404;
				localctx._value = this.tail();
				}
				break;
			case 2:
				{
				this.state = 1405;
				localctx._line = this.statement();
				}
				break;
			case 3:
				{
				this.state = 1406;
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
		this.enterRule(localctx, 44, EclangParser.RULE_func);
		let _la: number;
		try {
			this.state = 1501;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 153, this._ctx) ) {
			case 1:
				localctx = new FunctionDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1409;
				this.match(EclangParser.T__99);
				this.state = 1410;
				this.match(EclangParser.T__7);
				this.state = 1411;
				(localctx as FunctionDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1414;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 1412;
					(localctx as FunctionDeclareContext)._ext = this.match(EclangParser.NAME);
					this.state = 1413;
					(localctx as FunctionDeclareContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1416;
				this.match(EclangParser.T__23);
				this.state = 1417;
				this.match(EclangParser.T__1);
				this.state = 1419;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===15 || ((((_la - 66)) & ~0x1F) === 0 && ((1 << (_la - 66)) & 6145) !== 0) || _la===110) {
					{
					this.state = 1418;
					(localctx as FunctionDeclareContext)._parameter = this.parameter();
					(localctx as FunctionDeclareContext)._params.push((localctx as FunctionDeclareContext)._parameter);
					}
				}

				this.state = 1425;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 1421;
					this.match(EclangParser.T__2);
					this.state = 1422;
					(localctx as FunctionDeclareContext)._parameter = this.parameter();
					(localctx as FunctionDeclareContext)._params.push((localctx as FunctionDeclareContext)._parameter);
					}
					}
					this.state = 1427;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1428;
				this.match(EclangParser.T__3);
				this.state = 1431;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===65 || _la===75) {
					{
					this.state = 1429;
					_la = this._input.LA(1);
					if(!(_la===65 || _la===75)) {
					this._errHandler.recoverInline(this);
					}
					else {
						this._errHandler.reportMatch(this);
					    this.consume();
					}
					this.state = 1430;
					(localctx as FunctionDeclareContext)._returnType = this.type_(0);
					}
				}

				this.state = 1435;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===74) {
					{
					this.state = 1433;
					this.match(EclangParser.T__73);
					this.state = 1434;
					(localctx as FunctionDeclareContext)._throwing = this.type_(0);
					}
				}

				this.state = 1437;
				this.match(EclangParser.NL);
				}
				break;
			case 2:
				localctx = new FunctionBlockContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1438;
				this.match(EclangParser.T__7);
				this.state = 1439;
				(localctx as FunctionBlockContext)._name = this.match(EclangParser.NAME);
				this.state = 1440;
				this.match(EclangParser.T__1);
				this.state = 1442;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===15 || ((((_la - 66)) & ~0x1F) === 0 && ((1 << (_la - 66)) & 6145) !== 0) || _la===110) {
					{
					this.state = 1441;
					(localctx as FunctionBlockContext)._parameter = this.parameter();
					(localctx as FunctionBlockContext)._params.push((localctx as FunctionBlockContext)._parameter);
					}
				}

				this.state = 1448;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 1444;
					this.match(EclangParser.T__2);
					this.state = 1445;
					(localctx as FunctionBlockContext)._parameter = this.parameter();
					(localctx as FunctionBlockContext)._params.push((localctx as FunctionBlockContext)._parameter);
					}
					}
					this.state = 1450;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1451;
				this.match(EclangParser.T__3);
				this.state = 1454;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===65) {
					{
					this.state = 1452;
					this.match(EclangParser.T__64);
					this.state = 1453;
					(localctx as FunctionBlockContext)._returnType = this.type_(0);
					}
				}

				this.state = 1458;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===74) {
					{
					this.state = 1456;
					this.match(EclangParser.T__73);
					this.state = 1457;
					(localctx as FunctionBlockContext)._throwing = this.type_(0);
					}
				}

				this.state = 1463;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===110) {
					{
					{
					this.state = 1460;
					(localctx as FunctionBlockContext)._contract = this.contract();
					(localctx as FunctionBlockContext)._contracts.push((localctx as FunctionBlockContext)._contract);
					}
					}
					this.state = 1465;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1466;
				this.match(EclangParser.T__61);
				this.state = 1467;
				(localctx as FunctionBlockContext)._body = this.block();
				}
				break;
			case 3:
				localctx = new FunctionInlineContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1469;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===101) {
					{
					this.state = 1468;
					this.match(EclangParser.T__100);
					}
				}

				this.state = 1471;
				this.match(EclangParser.T__7);
				this.state = 1472;
				(localctx as FunctionInlineContext)._name = this.match(EclangParser.NAME);
				this.state = 1473;
				this.match(EclangParser.T__1);
				this.state = 1475;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===15 || ((((_la - 66)) & ~0x1F) === 0 && ((1 << (_la - 66)) & 6145) !== 0) || _la===110) {
					{
					this.state = 1474;
					(localctx as FunctionInlineContext)._parameter = this.parameter();
					(localctx as FunctionInlineContext)._params.push((localctx as FunctionInlineContext)._parameter);
					}
				}

				this.state = 1481;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===3) {
					{
					{
					this.state = 1477;
					this.match(EclangParser.T__2);
					this.state = 1478;
					(localctx as FunctionInlineContext)._parameter = this.parameter();
					(localctx as FunctionInlineContext)._params.push((localctx as FunctionInlineContext)._parameter);
					}
					}
					this.state = 1483;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1484;
				this.match(EclangParser.T__3);
				this.state = 1487;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===65) {
					{
					this.state = 1485;
					this.match(EclangParser.T__64);
					this.state = 1486;
					(localctx as FunctionInlineContext)._returnType = this.type_(0);
					}
				}

				this.state = 1491;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===74) {
					{
					this.state = 1489;
					this.match(EclangParser.T__73);
					this.state = 1490;
					(localctx as FunctionInlineContext)._throwing = this.type_(0);
					}
				}

				this.state = 1496;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===110) {
					{
					{
					this.state = 1493;
					(localctx as FunctionInlineContext)._contract = this.contract();
					(localctx as FunctionInlineContext)._contracts.push((localctx as FunctionInlineContext)._contract);
					}
					}
					this.state = 1498;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1499;
				this.match(EclangParser.T__12);
				this.state = 1500;
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
		this.enterRule(localctx, 46, EclangParser.RULE_global);
		let _la: number;
		try {
			let _alt: number;
			this.state = 1760;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 184, this._ctx) ) {
			case 1:
				localctx = new GlobalDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1503;
				this.match(EclangParser.T__76);
				this.state = 1504;
				this.match(EclangParser.NAME);
				this.state = 1505;
				this.match(EclangParser.T__12);
				this.state = 1506;
				this.expr(0);
				this.state = 1507;
				this.match(EclangParser.NL);
				}
				break;
			case 2:
				localctx = new GlobalDefineConstContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1509;
				this.match(EclangParser.T__77);
				this.state = 1510;
				this.match(EclangParser.NAME);
				this.state = 1511;
				this.match(EclangParser.T__12);
				this.state = 1512;
				this.expr(0);
				this.state = 1513;
				this.match(EclangParser.NL);
				}
				break;
			case 3:
				localctx = new GlobalDefineTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 3);
				{
				this.state = 1515;
				this.match(EclangParser.T__76);
				this.state = 1516;
				this.match(EclangParser.NAME);
				this.state = 1519;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===65) {
					{
					this.state = 1517;
					this.match(EclangParser.T__64);
					this.state = 1518;
					this.type_(0);
					}
				}

				this.state = 1521;
				this.match(EclangParser.T__12);
				this.state = 1522;
				this.expr(0);
				this.state = 1523;
				this.match(EclangParser.NL);
				}
				break;
			case 4:
				localctx = new GlobalDefineConstTypedContext(this, localctx);
				this.enterOuterAlt(localctx, 4);
				{
				this.state = 1525;
				this.match(EclangParser.T__77);
				this.state = 1526;
				this.match(EclangParser.NAME);
				this.state = 1529;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===65) {
					{
					this.state = 1527;
					this.match(EclangParser.T__64);
					this.state = 1528;
					this.type_(0);
					}
				}

				this.state = 1531;
				this.match(EclangParser.T__12);
				this.state = 1532;
				this.expr(0);
				this.state = 1533;
				this.match(EclangParser.NL);
				}
				break;
			case 5:
				localctx = new GlobalConstantContext(this, localctx);
				this.enterOuterAlt(localctx, 5);
				{
				this.state = 1535;
				this.match(EclangParser.T__78);
				this.state = 1536;
				this.match(EclangParser.NAME);
				this.state = 1539;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===65) {
					{
					this.state = 1537;
					this.match(EclangParser.T__64);
					this.state = 1538;
					this.type_(0);
					}
				}

				this.state = 1541;
				this.match(EclangParser.T__12);
				this.state = 1542;
				this.expr(0);
				this.state = 1543;
				this.match(EclangParser.NL);
				}
				break;
			case 6:
				localctx = new GlobalDeclareDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 6);
				{
				this.state = 1545;
				this.match(EclangParser.T__99);
				this.state = 1546;
				this.match(EclangParser.T__76);
				this.state = 1547;
				(localctx as GlobalDeclareDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1550;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 1548;
					(localctx as GlobalDeclareDefineContext)._ext = this.match(EclangParser.NAME);
					this.state = 1549;
					(localctx as GlobalDeclareDefineContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1552;
				this.match(EclangParser.T__23);
				this.state = 1553;
				this.type_(0);
				this.state = 1554;
				this.match(EclangParser.NL);
				}
				break;
			case 7:
				localctx = new GlobalDeclareConstContext(this, localctx);
				this.enterOuterAlt(localctx, 7);
				{
				this.state = 1556;
				this.match(EclangParser.T__99);
				this.state = 1557;
				_la = this._input.LA(1);
				if(!(_la===78 || _la===79)) {
				this._errHandler.recoverInline(this);
				}
				else {
					this._errHandler.reportMatch(this);
				    this.consume();
				}
				this.state = 1558;
				(localctx as GlobalDeclareConstContext)._name = this.match(EclangParser.NAME);
				this.state = 1561;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 1559;
					(localctx as GlobalDeclareConstContext)._ext = this.match(EclangParser.NAME);
					this.state = 1560;
					(localctx as GlobalDeclareConstContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1563;
				this.match(EclangParser.T__23);
				this.state = 1564;
				this.type_(0);
				this.state = 1565;
				this.match(EclangParser.NL);
				}
				break;
			case 8:
				localctx = new GlobalTypeDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 8);
				{
				this.state = 1567;
				this.match(EclangParser.T__99);
				this.state = 1568;
				this.match(EclangParser.T__101);
				this.state = 1569;
				this.match(EclangParser.NAME);
				this.state = 1570;
				this.match(EclangParser.T__23);
				this.state = 1571;
				this.type_(0);
				this.state = 1572;
				this.match(EclangParser.NL);
				}
				break;
			case 9:
				localctx = new GlobalTypeAliasContext(this, localctx);
				this.enterOuterAlt(localctx, 9);
				{
				this.state = 1574;
				this.match(EclangParser.T__101);
				this.state = 1575;
				(localctx as GlobalTypeAliasContext)._name = this.match(EclangParser.NAME);
				this.state = 1576;
				this.match(EclangParser.T__12);
				this.state = 1578;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 159, this._ctx) ) {
				case 1:
					{
					this.state = 1577;
					(localctx as GlobalTypeAliasContext)._modifier = this.match(EclangParser.NAME);
					}
					break;
				}
				this.state = 1580;
				this.type_(0);
				this.state = 1581;
				this.match(EclangParser.NL);
				}
				break;
			case 10:
				localctx = new GlobalEnumDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 10);
				{
				this.state = 1583;
				this.match(EclangParser.T__99);
				this.state = 1584;
				this.match(EclangParser.T__102);
				this.state = 1585;
				(localctx as GlobalEnumDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1586;
				this.match(EclangParser.T__23);
				this.state = 1587;
				this.match(EclangParser.INDENT);
				this.state = 1592;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1588;
					(localctx as GlobalEnumDeclareContext)._ATOM = this.match(EclangParser.ATOM);
					(localctx as GlobalEnumDeclareContext)._atoms.push((localctx as GlobalEnumDeclareContext)._ATOM);
					this.state = 1589;
					this.match(EclangParser.T__12);
					this.state = 1590;
					(localctx as GlobalEnumDeclareContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalEnumDeclareContext)._constants.push((localctx as GlobalEnumDeclareContext)._NAME);
					this.state = 1591;
					this.match(EclangParser.NL);
					}
					}
					this.state = 1594;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===111);
				this.state = 1596;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 11:
				localctx = new GlobalRecordDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 11);
				{
				this.state = 1600;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===66 || _la===110) {
					{
					{
					this.state = 1597;
					(localctx as GlobalRecordDefineContext)._recordModifier = this.recordModifier();
					(localctx as GlobalRecordDefineContext)._modifiers.push((localctx as GlobalRecordDefineContext)._recordModifier);
					}
					}
					this.state = 1602;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1603;
				this.match(EclangParser.T__103);
				this.state = 1604;
				(localctx as GlobalRecordDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1616;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===10) {
					{
					this.state = 1605;
					this.match(EclangParser.T__9);
					this.state = 1606;
					(localctx as GlobalRecordDefineContext)._typeParameter = this.typeParameter();
					(localctx as GlobalRecordDefineContext)._typeParams.push((localctx as GlobalRecordDefineContext)._typeParameter);
					this.state = 1611;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===3) {
						{
						{
						this.state = 1607;
						this.match(EclangParser.T__2);
						this.state = 1608;
						(localctx as GlobalRecordDefineContext)._typeParameter = this.typeParameter();
						(localctx as GlobalRecordDefineContext)._typeParams.push((localctx as GlobalRecordDefineContext)._typeParameter);
						}
						}
						this.state = 1613;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					this.state = 1614;
					this.match(EclangParser.T__10);
					}
				}

				this.state = 1624;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===105) {
					{
					this.state = 1618;
					this.match(EclangParser.T__104);
					this.state = 1621;
					this._errHandler.sync(this);
					switch ( this._interp.adaptivePredict(this._input, 164, this._ctx) ) {
					case 1:
						{
						this.state = 1619;
						(localctx as GlobalRecordDefineContext)._qualifier = this.match(EclangParser.NAME);
						this.state = 1620;
						this.match(EclangParser.T__8);
						}
						break;
					}
					this.state = 1623;
					(localctx as GlobalRecordDefineContext)._extending = this.match(EclangParser.NAME);
					}
				}

				this.state = 1626;
				this.match(EclangParser.T__23);
				this.state = 1627;
				this.match(EclangParser.INDENT);
				this.state = 1629;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1628;
					(localctx as GlobalRecordDefineContext)._recordField = this.recordField();
					(localctx as GlobalRecordDefineContext)._fields.push((localctx as GlobalRecordDefineContext)._recordField);
					}
					}
					this.state = 1631;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===110);
				this.state = 1633;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 12:
				localctx = new GlobalRecordDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 12);
				{
				this.state = 1638;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la===66 || _la===110) {
					{
					{
					this.state = 1635;
					(localctx as GlobalRecordDefineContext)._recordModifier = this.recordModifier();
					(localctx as GlobalRecordDefineContext)._modifiers.push((localctx as GlobalRecordDefineContext)._recordModifier);
					}
					}
					this.state = 1640;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 1641;
				this.match(EclangParser.T__103);
				this.state = 1642;
				(localctx as GlobalRecordDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1643;
				this.match(EclangParser.T__104);
				this.state = 1646;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 168, this._ctx) ) {
				case 1:
					{
					this.state = 1644;
					(localctx as GlobalRecordDefineContext)._qualifier = this.match(EclangParser.NAME);
					this.state = 1645;
					this.match(EclangParser.T__8);
					}
					break;
				}
				this.state = 1648;
				(localctx as GlobalRecordDefineContext)._extending = this.match(EclangParser.NAME);
				this.state = 1649;
				this.match(EclangParser.NL);
				}
				break;
			case 13:
				localctx = new GlobalUnionDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 13);
				{
				this.state = 1653;
				this._errHandler.sync(this);
				_alt = this._interp.adaptivePredict(this._input, 169, this._ctx);
				while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
					if (_alt === 1) {
						{
						{
						this.state = 1650;
						(localctx as GlobalUnionDefineContext)._recordModifier = this.recordModifier();
						(localctx as GlobalUnionDefineContext)._modifiers.push((localctx as GlobalUnionDefineContext)._recordModifier);
						}
						}
					}
					this.state = 1655;
					this._errHandler.sync(this);
					_alt = this._interp.adaptivePredict(this._input, 169, this._ctx);
				}
				this.state = 1656;
				(localctx as GlobalUnionDefineContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1657;
				(localctx as GlobalUnionDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1658;
				this.match(EclangParser.T__23);
				this.state = 1659;
				this.match(EclangParser.INDENT);
				this.state = 1661;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1660;
					(localctx as GlobalUnionDefineContext)._recordField = this.recordField();
					(localctx as GlobalUnionDefineContext)._fields.push((localctx as GlobalUnionDefineContext)._recordField);
					}
					}
					this.state = 1663;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===110);
				this.state = 1665;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 14:
				localctx = new GlobalRecordDeclareDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 14);
				{
				this.state = 1667;
				this.match(EclangParser.T__99);
				this.state = 1669;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===66) {
					{
					this.state = 1668;
					(localctx as GlobalRecordDeclareDefineContext)._mut = this.match(EclangParser.T__65);
					}
				}

				this.state = 1671;
				this.match(EclangParser.T__103);
				this.state = 1672;
				(localctx as GlobalRecordDeclareDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1675;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 1673;
					(localctx as GlobalRecordDeclareDefineContext)._ext = this.match(EclangParser.NAME);
					this.state = 1674;
					(localctx as GlobalRecordDeclareDefineContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1677;
				this.match(EclangParser.T__23);
				this.state = 1678;
				this.match(EclangParser.INDENT);
				this.state = 1686;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1679;
					(localctx as GlobalRecordDeclareDefineContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalRecordDeclareDefineContext)._names.push((localctx as GlobalRecordDeclareDefineContext)._NAME);
					this.state = 1680;
					(localctx as GlobalRecordDeclareDefineContext)._fieldExtern = this.fieldExtern();
					(localctx as GlobalRecordDeclareDefineContext)._externs.push((localctx as GlobalRecordDeclareDefineContext)._fieldExtern);
					this.state = 1681;
					this.match(EclangParser.T__64);
					this.state = 1682;
					(localctx as GlobalRecordDeclareDefineContext)._type_ = this.type_(0);
					(localctx as GlobalRecordDeclareDefineContext)._types.push((localctx as GlobalRecordDeclareDefineContext)._type_);
					this.state = 1683;
					(localctx as GlobalRecordDeclareDefineContext)._fieldWidth = this.fieldWidth();
					(localctx as GlobalRecordDeclareDefineContext)._widths.push((localctx as GlobalRecordDeclareDefineContext)._fieldWidth);
					this.state = 1684;
					this.match(EclangParser.NL);
					}
					}
					this.state = 1688;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===110);
				this.state = 1690;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 15:
				localctx = new GlobalRecordDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 15);
				{
				this.state = 1692;
				this.match(EclangParser.T__99);
				this.state = 1694;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===66) {
					{
					this.state = 1693;
					(localctx as GlobalRecordDeclareContext)._mut = this.match(EclangParser.T__65);
					}
				}

				this.state = 1696;
				this.match(EclangParser.T__103);
				this.state = 1697;
				(localctx as GlobalRecordDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1700;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 1698;
					(localctx as GlobalRecordDeclareContext)._ext = this.match(EclangParser.NAME);
					this.state = 1699;
					(localctx as GlobalRecordDeclareContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1702;
				this.match(EclangParser.NL);
				}
				break;
			case 16:
				localctx = new GlobalUnionDeclareDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 16);
				{
				this.state = 1703;
				this.match(EclangParser.T__99);
				this.state = 1705;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===66) {
					{
					this.state = 1704;
					(localctx as GlobalUnionDeclareDefineContext)._mut = this.match(EclangParser.T__65);
					}
				}

				this.state = 1707;
				(localctx as GlobalUnionDeclareDefineContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1709;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1708;
					(localctx as GlobalUnionDeclareDefineContext)._typedef = this.match(EclangParser.T__103);
					}
				}

				this.state = 1711;
				(localctx as GlobalUnionDeclareDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1714;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 1712;
					(localctx as GlobalUnionDeclareDefineContext)._ext = this.match(EclangParser.NAME);
					this.state = 1713;
					(localctx as GlobalUnionDeclareDefineContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1716;
				this.match(EclangParser.T__23);
				this.state = 1717;
				this.match(EclangParser.INDENT);
				this.state = 1725;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1718;
					(localctx as GlobalUnionDeclareDefineContext)._NAME = this.match(EclangParser.NAME);
					(localctx as GlobalUnionDeclareDefineContext)._names.push((localctx as GlobalUnionDeclareDefineContext)._NAME);
					this.state = 1719;
					(localctx as GlobalUnionDeclareDefineContext)._fieldExtern = this.fieldExtern();
					(localctx as GlobalUnionDeclareDefineContext)._externs.push((localctx as GlobalUnionDeclareDefineContext)._fieldExtern);
					this.state = 1720;
					this.match(EclangParser.T__64);
					this.state = 1721;
					(localctx as GlobalUnionDeclareDefineContext)._type_ = this.type_(0);
					(localctx as GlobalUnionDeclareDefineContext)._types.push((localctx as GlobalUnionDeclareDefineContext)._type_);
					this.state = 1722;
					(localctx as GlobalUnionDeclareDefineContext)._fieldWidth = this.fieldWidth();
					(localctx as GlobalUnionDeclareDefineContext)._widths.push((localctx as GlobalUnionDeclareDefineContext)._fieldWidth);
					this.state = 1723;
					this.match(EclangParser.NL);
					}
					}
					this.state = 1727;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===110);
				this.state = 1729;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 17:
				localctx = new GlobalUnionDeclareContext(this, localctx);
				this.enterOuterAlt(localctx, 17);
				{
				this.state = 1731;
				this.match(EclangParser.T__99);
				this.state = 1733;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===66) {
					{
					this.state = 1732;
					(localctx as GlobalUnionDeclareContext)._mut = this.match(EclangParser.T__65);
					}
				}

				this.state = 1735;
				(localctx as GlobalUnionDeclareContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1737;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===104) {
					{
					this.state = 1736;
					(localctx as GlobalUnionDeclareContext)._typedef = this.match(EclangParser.T__103);
					}
				}

				this.state = 1739;
				(localctx as GlobalUnionDeclareContext)._name = this.match(EclangParser.NAME);
				this.state = 1742;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===110) {
					{
					this.state = 1740;
					(localctx as GlobalUnionDeclareContext)._ext = this.match(EclangParser.NAME);
					this.state = 1741;
					(localctx as GlobalUnionDeclareContext)._externalName = this.match(EclangParser.STRING);
					}
				}

				this.state = 1744;
				this.match(EclangParser.NL);
				}
				break;
			case 18:
				localctx = new GlobalInterfaceDefineContext(this, localctx);
				this.enterOuterAlt(localctx, 18);
				{
				this.state = 1745;
				this.match(EclangParser.T__75);
				this.state = 1746;
				(localctx as GlobalInterfaceDefineContext)._name = this.match(EclangParser.NAME);
				this.state = 1747;
				this.match(EclangParser.T__23);
				this.state = 1748;
				this.match(EclangParser.INDENT);
				this.state = 1750;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				do {
					{
					{
					this.state = 1749;
					(localctx as GlobalInterfaceDefineContext)._interfaceMember = this.interfaceMember();
					(localctx as GlobalInterfaceDefineContext)._members.push((localctx as GlobalInterfaceDefineContext)._interfaceMember);
					}
					}
					this.state = 1752;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				} while (_la===8 || _la===110);
				this.state = 1754;
				this.match(EclangParser.DEDENT);
				}
				break;
			case 19:
				localctx = new GlobalNamespaceContext(this, localctx);
				this.enterOuterAlt(localctx, 19);
				{
				this.state = 1756;
				(localctx as GlobalNamespaceContext)._keyword = this.match(EclangParser.NAME);
				this.state = 1757;
				(localctx as GlobalNamespaceContext)._name = this.match(EclangParser.STRING);
				this.state = 1758;
				this.match(EclangParser.NL);
				}
				break;
			case 20:
				localctx = new GlobalConstAssertContext(this, localctx);
				this.enterOuterAlt(localctx, 20);
				{
				this.state = 1759;
				this.constAssert();
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
		this.enterRule(localctx, 48, EclangParser.RULE_interfaceMember);
		let _la: number;
		try {
			this.state = 1790;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 110:
				localctx = new InterfaceFieldContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1762;
				(localctx as InterfaceFieldContext)._name = this.match(EclangParser.NAME);
				this.state = 1763;
				this.match(EclangParser.T__64);
				this.state = 1764;
				this.type_(0);
				this.state = 1765;
				this.match(EclangParser.NL);
				}
				break;
			case 8:
				localctx = new InterfaceMethodContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1767;
				this.match(EclangParser.T__7);
				this.state = 1768;
				(localctx as InterfaceMethodContext)._name = this.match(EclangParser.NAME);
				this.state = 1769;
				this.match(EclangParser.T__1);
				this.state = 1778;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x1F) === 0 && ((1 << _la) & 134219012) !== 0) || _la===73 || _la===110 || _la===111) {
					{
					this.state = 1770;
					(localctx as InterfaceMethodContext)._type_ = this.type_(0);
					(localctx as InterfaceMethodContext)._params.push((localctx as InterfaceMethodContext)._type_);
					this.state = 1775;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
					while (_la===3) {
						{
						{
						this.state = 1771;
						this.match(EclangParser.T__2);
						this.state = 1772;
						(localctx as InterfaceMethodContext)._type_ = this.type_(0);
						(localctx as InterfaceMethodContext)._params.push((localctx as InterfaceMethodContext)._type_);
						}
						}
						this.state = 1777;
						this._errHandler.sync(this);
						_la = this._input.LA(1);
					}
					}
				}

				this.state = 1780;
				this.match(EclangParser.T__3);
				this.state = 1783;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===65) {
					{
					this.state = 1781;
					this.match(EclangParser.T__64);
					this.state = 1782;
					(localctx as InterfaceMethodContext)._returnType = this.type_(0);
					}
				}

				this.state = 1787;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===74) {
					{
					this.state = 1785;
					this.match(EclangParser.T__73);
					this.state = 1786;
					(localctx as InterfaceMethodContext)._throwing = this.type_(0);
					}
				}

				this.state = 1789;
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
		this.enterRule(localctx, 50, EclangParser.RULE_recordField);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1792;
			localctx._name = this.match(EclangParser.NAME);
			this.state = 1793;
			this.match(EclangParser.T__64);
			this.state = 1794;
			this.type_(0);
			this.state = 1795;
			this.fieldWidth();
			this.state = 1798;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===13) {
				{
				this.state = 1796;
				this.match(EclangParser.T__12);
				this.state = 1797;
				localctx._value = this.expr(0);
				}
			}

			this.state = 1800;
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
		this.enterRule(localctx, 52, EclangParser.RULE_fieldWidth);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1805;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 65:
				{
				this.state = 1802;
				this.match(EclangParser.T__64);
				this.state = 1803;
				localctx._width = this.expr(0);
				}
				break;
			case 111:
				{
				this.state = 1804;
				localctx._atomWidth = this.match(EclangParser.ATOM);
				}
				break;
			case 13:
			case 108:
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
	public fieldExtern(): FieldExternContext {
		let localctx: FieldExternContext = new FieldExternContext(this, this._ctx, this.state);
		this.enterRule(localctx, 54, EclangParser.RULE_fieldExtern);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1809;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===110) {
				{
				this.state = 1807;
				localctx._ext = this.match(EclangParser.NAME);
				this.state = 1808;
				localctx._cName = this.match(EclangParser.STRING);
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
	public recordModifier(): RecordModifierContext {
		let localctx: RecordModifierContext = new RecordModifierContext(this, this._ctx, this.state);
		this.enterRule(localctx, 56, EclangParser.RULE_recordModifier);
		let _la: number;
		try {
			this.state = 1819;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case 110:
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1811;
				localctx._name = this.match(EclangParser.NAME);
				this.state = 1816;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la===2) {
					{
					this.state = 1812;
					this.match(EclangParser.T__1);
					this.state = 1813;
					localctx._size = this.expr(0);
					this.state = 1814;
					this.match(EclangParser.T__3);
					}
				}

				}
				break;
			case 66:
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1818;
				localctx._mut = this.match(EclangParser.T__65);
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
	public typeParameter(): TypeParameterContext {
		let localctx: TypeParameterContext = new TypeParameterContext(this, this._ctx, this.state);
		this.enterRule(localctx, 58, EclangParser.RULE_typeParameter);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1822;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===66) {
				{
				this.state = 1821;
				localctx._mut = this.match(EclangParser.T__65);
				}
			}

			this.state = 1824;
			localctx._name = this.match(EclangParser.NAME);
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
		this.enterRule(localctx, 60, EclangParser.RULE_use);
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1826;
			this.match(EclangParser.T__105);
			this.state = 1827;
			localctx._from_ = this.match(EclangParser.STRING);
			this.state = 1828;
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
		this.enterRule(localctx, 62, EclangParser.RULE_interpolation);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1830;
			this.expr(0);
			this.state = 1834;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la===108) {
				{
				{
				this.state = 1831;
				this.match(EclangParser.NL);
				}
				}
				this.state = 1836;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			this.state = 1837;
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
		this.enterRule(localctx, 64, EclangParser.RULE_exportDeclaration);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1839;
			localctx._keyword = this.match(EclangParser.NAME);
			this.state = 1842;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la===110) {
				{
				this.state = 1840;
				localctx._ext = this.match(EclangParser.NAME);
				this.state = 1841;
				localctx._externalName = this.match(EclangParser.STRING);
				}
			}

			this.state = 1844;
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
		this.enterRule(localctx, 66, EclangParser.RULE_privateDeclaration);
		try {
			this.state = 1850;
			this._errHandler.sync(this);
			switch ( this._interp.adaptivePredict(this._input, 198, this._ctx) ) {
			case 1:
				localctx = new PrivateFunctionContext(this, localctx);
				this.enterOuterAlt(localctx, 1);
				{
				this.state = 1846;
				this.match(EclangParser.T__106);
				this.state = 1847;
				this.func();
				}
				break;
			case 2:
				localctx = new PrivateGlobalContext(this, localctx);
				this.enterOuterAlt(localctx, 2);
				{
				this.state = 1848;
				this.match(EclangParser.T__106);
				this.state = 1849;
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
		this.enterRule(localctx, 68, EclangParser.RULE_program);
		let _la: number;
		try {
			this.enterOuterAlt(localctx, 1);
			{
			this.state = 1860;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la===8 || ((((_la - 66)) & ~0x1F) === 0 && ((1 << (_la - 66)) & 15361) !== 0) || ((((_la - 100)) & ~0x1F) === 0 && ((1 << (_la - 100)) & 1495) !== 0)) {
				{
				this.state = 1858;
				this._errHandler.sync(this);
				switch ( this._interp.adaptivePredict(this._input, 199, this._ctx) ) {
				case 1:
					{
					this.state = 1852;
					localctx._func = this.func();
					localctx._functions.push(localctx._func);
					}
					break;
				case 2:
					{
					this.state = 1853;
					localctx._global = this.global();
					localctx._globals.push(localctx._global);
					}
					break;
				case 3:
					{
					this.state = 1854;
					localctx._use = this.use();
					localctx._uses.push(localctx._use);
					}
					break;
				case 4:
					{
					this.state = 1855;
					localctx._privateDeclaration = this.privateDeclaration();
					localctx._privates.push(localctx._privateDeclaration);
					}
					break;
				case 5:
					{
					this.state = 1856;
					localctx._exportDeclaration = this.exportDeclaration();
					localctx._exports.push(localctx._exportDeclaration);
					}
					break;
				case 6:
					{
					this.state = 1857;
					this.match(EclangParser.NL);
					}
					break;
				}
				}
				this.state = 1862;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			this.state = 1863;
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
		case 9:
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
			return this.precpred(this._ctx, 47);
		case 16:
			return this.precpred(this._ctx, 46);
		case 17:
			return this.precpred(this._ctx, 45);
		case 18:
			return this.precpred(this._ctx, 44);
		case 19:
			return this.precpred(this._ctx, 43);
		case 20:
			return this.precpred(this._ctx, 42);
		case 21:
			return this.precpred(this._ctx, 41);
		case 22:
			return this.precpred(this._ctx, 40);
		case 23:
			return this.precpred(this._ctx, 39);
		case 24:
			return this.precpred(this._ctx, 38);
		case 25:
			return this.precpred(this._ctx, 37);
		case 26:
			return this.precpred(this._ctx, 36);
		case 27:
			return this.precpred(this._ctx, 35);
		case 28:
			return this.precpred(this._ctx, 34);
		case 29:
			return this.precpred(this._ctx, 33);
		case 30:
			return this.precpred(this._ctx, 32);
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
			return this.precpred(this._ctx, 12);
		case 36:
			return this.precpred(this._ctx, 11);
		case 37:
			return this.precpred(this._ctx, 6);
		case 38:
			return this.precpred(this._ctx, 15);
		case 39:
			return this.precpred(this._ctx, 14);
		case 40:
			return this.precpred(this._ctx, 13);
		}
		return true;
	}

	public static readonly _serializedATN: number[] = [4,1,125,1866,2,0,7,0,
	2,1,7,1,2,2,7,2,2,3,7,3,2,4,7,4,2,5,7,5,2,6,7,6,2,7,7,7,2,8,7,8,2,9,7,9,
	2,10,7,10,2,11,7,11,2,12,7,12,2,13,7,13,2,14,7,14,2,15,7,15,2,16,7,16,2,
	17,7,17,2,18,7,18,2,19,7,19,2,20,7,20,2,21,7,21,2,22,7,22,2,23,7,23,2,24,
	7,24,2,25,7,25,2,26,7,26,2,27,7,27,2,28,7,28,2,29,7,29,2,30,7,30,2,31,7,
	31,2,32,7,32,2,33,7,33,2,34,7,34,1,0,1,0,1,0,1,0,3,0,75,8,0,1,1,1,1,1,1,
	1,1,1,1,3,1,82,8,1,1,2,1,2,4,2,86,8,2,11,2,12,2,87,1,2,1,2,1,3,1,3,1,3,
	1,3,1,3,1,3,5,3,98,8,3,10,3,12,3,101,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,5,3,111,8,3,10,3,12,3,114,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,138,8,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,5,3,149,8,3,10,3,12,3,152,9,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,5,3,163,8,3,10,3,12,3,166,9,3,1,3,1,3,1,3,1,3,1,3,1,3,
	3,3,174,8,3,1,3,1,3,1,3,1,3,1,3,5,3,181,8,3,10,3,12,3,184,9,3,1,3,1,3,3,
	3,188,8,3,3,3,190,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,200,8,3,10,3,
	12,3,203,9,3,1,3,1,3,1,3,1,3,3,3,209,8,3,1,3,1,3,1,3,1,3,1,3,5,3,216,8,
	3,10,3,12,3,219,9,3,1,3,1,3,3,3,223,8,3,3,3,225,8,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,5,3,237,8,3,10,3,12,3,240,9,3,1,3,1,3,1,3,1,3,1,3,1,
	3,3,3,248,8,3,1,3,1,3,1,3,1,3,1,3,5,3,255,8,3,10,3,12,3,258,9,3,1,3,1,3,
	3,3,262,8,3,1,3,1,3,1,3,1,3,3,3,268,8,3,1,3,1,3,1,3,1,3,1,3,5,3,275,8,3,
	10,3,12,3,278,9,3,1,3,1,3,3,3,282,8,3,3,3,284,8,3,1,3,1,3,1,3,1,3,5,3,290,
	8,3,10,3,12,3,293,9,3,1,3,1,3,1,3,1,3,3,3,299,8,3,1,3,1,3,1,3,1,3,1,3,5,
	3,306,8,3,10,3,12,3,309,9,3,1,3,1,3,3,3,313,8,3,3,3,315,8,3,1,3,1,3,1,3,
	1,3,1,3,1,3,5,3,323,8,3,10,3,12,3,326,9,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,334,
	8,3,1,3,1,3,1,3,1,3,1,3,5,3,341,8,3,10,3,12,3,344,9,3,1,3,1,3,3,3,348,8,
	3,3,3,350,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,359,8,3,10,3,12,3,362,9,3,
	1,3,1,3,1,3,1,3,3,3,368,8,3,1,3,1,3,1,3,1,3,1,3,5,3,375,8,3,10,3,12,3,378,
	9,3,1,3,1,3,3,3,382,8,3,3,3,384,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	5,3,395,8,3,10,3,12,3,398,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,409,
	8,3,10,3,12,3,412,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,422,8,3,10,3,
	12,3,425,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,3,3,455,8,3,1,3,1,
	3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,5,3,480,8,3,10,3,12,3,483,9,3,3,3,485,8,3,1,3,1,3,1,3,3,3,
	490,8,3,1,3,1,3,3,3,494,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,3,3,509,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,557,8,3,
	10,3,12,3,560,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,5,3,573,8,
	3,10,3,12,3,576,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,5,3,594,8,3,10,3,12,3,597,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,
	3,1,3,1,3,1,3,5,3,610,8,3,10,3,12,3,613,9,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,3,3,641,8,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,
	1,3,5,3,676,8,3,10,3,12,3,679,9,3,1,4,1,4,3,4,683,8,4,1,4,3,4,686,8,4,1,
	4,1,4,1,5,1,5,3,5,692,8,5,1,5,1,5,1,5,1,5,3,5,698,8,5,1,5,1,5,3,5,702,8,
	5,3,5,704,8,5,1,6,1,6,1,6,1,6,1,6,3,6,711,8,6,1,7,1,7,1,7,1,7,1,7,1,7,1,
	7,3,7,720,8,7,1,8,1,8,1,8,3,8,725,8,8,1,9,1,9,1,9,3,9,730,8,9,1,9,1,9,1,
	9,1,9,1,9,5,9,737,8,9,10,9,12,9,740,9,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,
	1,9,1,9,1,9,1,9,1,9,1,9,1,9,3,9,757,8,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,
	1,9,3,9,768,8,9,1,9,1,9,1,9,1,9,3,9,774,8,9,1,9,1,9,1,9,1,9,3,9,780,8,9,
	1,9,5,9,783,8,9,10,9,12,9,786,9,9,1,9,1,9,1,9,3,9,791,8,9,1,9,1,9,1,9,1,
	9,1,9,1,9,1,9,3,9,800,8,9,1,9,1,9,1,9,1,9,3,9,806,8,9,1,9,5,9,809,8,9,10,
	9,12,9,812,9,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,4,9,822,8,9,11,9,12,9,823,
	1,9,1,9,3,9,828,8,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,1,9,
	1,9,1,9,1,9,1,9,1,9,1,9,5,9,849,8,9,10,9,12,9,852,9,9,1,10,1,10,1,10,1,
	10,1,10,1,10,1,10,1,10,1,10,4,10,863,8,10,11,10,12,10,864,1,10,1,10,1,11,
	1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,
	11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,1,11,3,11,894,8,11,1,12,1,12,
	1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,
	12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,
	1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,
	12,3,12,942,8,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,3,12,953,
	8,12,1,12,1,12,3,12,957,8,12,1,12,1,12,1,12,3,12,962,8,12,1,12,1,12,3,12,
	966,8,12,1,12,1,12,1,12,1,12,3,12,972,8,12,1,12,1,12,1,12,1,12,3,12,978,
	8,12,1,12,1,12,1,12,1,12,1,12,3,12,985,8,12,1,12,1,12,1,12,1,12,1,12,1,
	12,3,12,993,8,12,1,12,1,12,1,12,1,12,1,12,3,12,1000,8,12,1,12,1,12,3,12,
	1004,8,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,3,
	12,1018,8,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,
	1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,3,12,1041,8,12,1,12,1,12,1,
	12,1,12,4,12,1047,8,12,11,12,12,12,1048,1,12,1,12,1,12,1,12,1,12,1,12,1,
	12,1,12,1,12,1,12,1,12,3,12,1062,8,12,1,12,1,12,1,12,3,12,1067,8,12,1,12,
	1,12,1,12,1,12,1,12,1,12,3,12,1075,8,12,1,12,1,12,1,12,3,12,1080,8,12,1,
	12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,3,12,1091,8,12,1,12,3,12,1094,
	8,12,1,12,1,12,3,12,1098,8,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,
	12,3,12,1109,8,12,1,12,3,12,1112,8,12,1,12,1,12,3,12,1116,8,12,1,12,1,12,
	3,12,1120,8,12,1,12,1,12,3,12,1124,8,12,1,12,1,12,1,12,1,12,3,12,1130,8,
	12,1,12,1,12,1,12,1,12,3,12,1136,8,12,1,12,3,12,1139,8,12,1,12,1,12,1,12,
	1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,
	12,1,12,1,12,5,12,1161,8,12,10,12,12,12,1164,9,12,1,12,1,12,1,12,1,12,1,
	12,1,12,1,12,1,12,1,12,1,12,5,12,1176,8,12,10,12,12,12,1179,9,12,1,12,1,
	12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,
	1,12,1,12,1,12,5,12,1200,8,12,10,12,12,12,1203,9,12,1,12,1,12,1,12,1,12,
	1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,5,12,1217,8,12,10,12,12,12,1220,
	9,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,
	12,1,12,1,12,1,12,1,12,1,12,5,12,1241,8,12,10,12,12,12,1244,9,12,1,12,1,
	12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,5,12,1258,8,12,10,
	12,12,12,1261,9,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,12,1,
	12,1,12,1,12,1,12,1,12,1,12,3,12,1279,8,12,1,12,1,12,1,12,1,12,3,12,1285,
	8,12,1,13,1,13,1,13,1,13,1,13,1,13,1,13,3,13,1294,8,13,1,13,1,13,1,13,1,
	13,1,13,1,13,1,13,1,13,1,13,3,13,1305,8,13,1,13,1,13,1,13,1,13,1,13,1,13,
	1,13,3,13,1314,8,13,1,13,1,13,3,13,1318,8,13,1,14,1,14,1,14,1,14,3,14,1324,
	8,14,1,14,1,14,1,14,1,14,1,14,3,14,1331,8,14,3,14,1333,8,14,1,15,1,15,1,
	15,1,15,4,15,1339,8,15,11,15,12,15,1340,1,15,1,15,1,16,1,16,3,16,1347,8,
	16,1,17,1,17,1,17,1,17,3,17,1353,8,17,1,17,1,17,1,17,1,17,1,17,1,17,1,17,
	5,17,1362,8,17,10,17,12,17,1365,9,17,1,17,1,17,3,17,1369,8,17,3,17,1371,
	8,17,1,18,1,18,1,18,1,18,1,18,1,18,3,18,1379,8,18,1,19,1,19,1,19,1,19,3,
	19,1385,8,19,1,20,1,20,1,20,1,20,3,20,1391,8,20,1,20,1,20,1,20,1,20,4,20,
	1397,8,20,11,20,12,20,1398,1,20,1,20,1,21,1,21,1,21,1,21,1,21,3,21,1408,
	8,21,1,22,1,22,1,22,1,22,1,22,3,22,1415,8,22,1,22,1,22,1,22,3,22,1420,8,
	22,1,22,1,22,5,22,1424,8,22,10,22,12,22,1427,9,22,1,22,1,22,1,22,3,22,1432,
	8,22,1,22,1,22,3,22,1436,8,22,1,22,1,22,1,22,1,22,1,22,3,22,1443,8,22,1,
	22,1,22,5,22,1447,8,22,10,22,12,22,1450,9,22,1,22,1,22,1,22,3,22,1455,8,
	22,1,22,1,22,3,22,1459,8,22,1,22,5,22,1462,8,22,10,22,12,22,1465,9,22,1,
	22,1,22,1,22,3,22,1470,8,22,1,22,1,22,1,22,1,22,3,22,1476,8,22,1,22,1,22,
	5,22,1480,8,22,10,22,12,22,1483,9,22,1,22,1,22,1,22,3,22,1488,8,22,1,22,
	1,22,3,22,1492,8,22,1,22,5,22,1495,8,22,10,22,12,22,1498,9,22,1,22,1,22,
	3,22,1502,8,22,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,
	23,1,23,1,23,1,23,1,23,3,23,1520,8,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,
	1,23,3,23,1530,8,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,3,23,1540,8,
	23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,3,23,1551,8,23,1,23,1,23,
	1,23,1,23,1,23,1,23,1,23,1,23,1,23,3,23,1562,8,23,1,23,1,23,1,23,1,23,1,
	23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,3,23,1579,8,23,1,23,
	1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,4,23,1593,8,23,11,
	23,12,23,1594,1,23,1,23,5,23,1599,8,23,10,23,12,23,1602,9,23,1,23,1,23,
	1,23,1,23,1,23,1,23,5,23,1610,8,23,10,23,12,23,1613,9,23,1,23,1,23,3,23,
	1617,8,23,1,23,1,23,1,23,3,23,1622,8,23,1,23,3,23,1625,8,23,1,23,1,23,1,
	23,4,23,1630,8,23,11,23,12,23,1631,1,23,1,23,1,23,5,23,1637,8,23,10,23,
	12,23,1640,9,23,1,23,1,23,1,23,1,23,1,23,3,23,1647,8,23,1,23,1,23,1,23,
	5,23,1652,8,23,10,23,12,23,1655,9,23,1,23,1,23,1,23,1,23,1,23,4,23,1662,
	8,23,11,23,12,23,1663,1,23,1,23,1,23,1,23,3,23,1670,8,23,1,23,1,23,1,23,
	1,23,3,23,1676,8,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,4,23,1687,
	8,23,11,23,12,23,1688,1,23,1,23,1,23,1,23,3,23,1695,8,23,1,23,1,23,1,23,
	1,23,3,23,1701,8,23,1,23,1,23,1,23,3,23,1706,8,23,1,23,1,23,3,23,1710,8,
	23,1,23,1,23,1,23,3,23,1715,8,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,1,23,
	1,23,4,23,1726,8,23,11,23,12,23,1727,1,23,1,23,1,23,1,23,3,23,1734,8,23,
	1,23,1,23,3,23,1738,8,23,1,23,1,23,1,23,3,23,1743,8,23,1,23,1,23,1,23,1,
	23,1,23,1,23,4,23,1751,8,23,11,23,12,23,1752,1,23,1,23,1,23,1,23,1,23,1,
	23,3,23,1761,8,23,1,24,1,24,1,24,1,24,1,24,1,24,1,24,1,24,1,24,1,24,1,24,
	5,24,1774,8,24,10,24,12,24,1777,9,24,3,24,1779,8,24,1,24,1,24,1,24,3,24,
	1784,8,24,1,24,1,24,3,24,1788,8,24,1,24,3,24,1791,8,24,1,25,1,25,1,25,1,
	25,1,25,1,25,3,25,1799,8,25,1,25,1,25,1,26,1,26,1,26,3,26,1806,8,26,1,27,
	1,27,3,27,1810,8,27,1,28,1,28,1,28,1,28,1,28,3,28,1817,8,28,1,28,3,28,1820,
	8,28,1,29,3,29,1823,8,29,1,29,1,29,1,30,1,30,1,30,1,30,1,31,1,31,5,31,1833,
	8,31,10,31,12,31,1836,9,31,1,31,1,31,1,32,1,32,1,32,3,32,1843,8,32,1,32,
	1,32,1,33,1,33,1,33,1,33,3,33,1851,8,33,1,34,1,34,1,34,1,34,1,34,1,34,5,
	34,1859,8,34,10,34,12,34,1862,9,34,1,34,1,34,1,34,0,2,6,18,35,0,2,4,6,8,
	10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56,
	58,60,62,64,66,68,0,16,1,0,17,18,2,0,23,23,33,36,1,0,37,39,2,0,36,36,40,
	40,2,0,21,22,45,45,2,0,10,11,46,47,1,0,50,53,1,0,54,55,1,0,56,57,1,0,58,
	59,1,0,21,22,2,0,65,65,75,75,1,0,77,78,2,0,13,13,80,89,2,0,48,48,110,110,
	1,0,78,79,2203,0,74,1,0,0,0,2,81,1,0,0,0,4,83,1,0,0,0,6,493,1,0,0,0,8,682,
	1,0,0,0,10,703,1,0,0,0,12,710,1,0,0,0,14,712,1,0,0,0,16,721,1,0,0,0,18,
	827,1,0,0,0,20,853,1,0,0,0,22,893,1,0,0,0,24,1284,1,0,0,0,26,1317,1,0,0,
	0,28,1332,1,0,0,0,30,1334,1,0,0,0,32,1346,1,0,0,0,34,1370,1,0,0,0,36,1378,
	1,0,0,0,38,1380,1,0,0,0,40,1386,1,0,0,0,42,1402,1,0,0,0,44,1501,1,0,0,0,
	46,1760,1,0,0,0,48,1790,1,0,0,0,50,1792,1,0,0,0,52,1805,1,0,0,0,54,1809,
	1,0,0,0,56,1819,1,0,0,0,58,1822,1,0,0,0,60,1826,1,0,0,0,62,1830,1,0,0,0,
	64,1839,1,0,0,0,66,1850,1,0,0,0,68,1860,1,0,0,0,70,75,5,113,0,0,71,75,5,
	112,0,0,72,75,5,114,0,0,73,75,5,115,0,0,74,70,1,0,0,0,74,71,1,0,0,0,74,
	72,1,0,0,0,74,73,1,0,0,0,75,1,1,0,0,0,76,82,3,0,0,0,77,82,5,109,0,0,78,
	82,5,117,0,0,79,82,5,116,0,0,80,82,5,111,0,0,81,76,1,0,0,0,81,77,1,0,0,
	0,81,78,1,0,0,0,81,79,1,0,0,0,81,80,1,0,0,0,82,3,1,0,0,0,83,85,5,124,0,
	0,84,86,3,24,12,0,85,84,1,0,0,0,86,87,1,0,0,0,87,85,1,0,0,0,87,88,1,0,0,
	0,88,89,1,0,0,0,89,90,5,125,0,0,90,5,1,0,0,0,91,92,6,3,-1,0,92,494,5,1,
	0,0,93,94,5,2,0,0,94,99,3,6,3,0,95,96,5,3,0,0,96,98,3,6,3,0,97,95,1,0,0,
	0,98,101,1,0,0,0,99,97,1,0,0,0,99,100,1,0,0,0,100,102,1,0,0,0,101,99,1,
	0,0,0,102,103,5,4,0,0,103,494,1,0,0,0,104,105,5,2,0,0,105,106,5,124,0,0,
	106,112,3,6,3,0,107,108,5,3,0,0,108,109,5,108,0,0,109,111,3,6,3,0,110,107,
	1,0,0,0,111,114,1,0,0,0,112,110,1,0,0,0,112,113,1,0,0,0,113,115,1,0,0,0,
	114,112,1,0,0,0,115,116,5,108,0,0,116,117,5,125,0,0,117,118,5,4,0,0,118,
	494,1,0,0,0,119,494,3,2,1,0,120,121,3,2,1,0,121,122,5,110,0,0,122,494,1,
	0,0,0,123,124,5,5,0,0,124,494,3,18,9,0,125,126,5,6,0,0,126,494,3,18,9,0,
	127,128,5,7,0,0,128,129,5,2,0,0,129,130,3,18,9,0,130,131,5,3,0,0,131,132,
	5,110,0,0,132,133,5,4,0,0,133,494,1,0,0,0,134,137,5,8,0,0,135,136,5,110,
	0,0,136,138,5,9,0,0,137,135,1,0,0,0,137,138,1,0,0,0,138,139,1,0,0,0,139,
	494,5,110,0,0,140,141,5,110,0,0,141,142,5,2,0,0,142,494,5,4,0,0,143,144,
	5,110,0,0,144,145,5,2,0,0,145,150,3,8,4,0,146,147,5,3,0,0,147,149,3,8,4,
	0,148,146,1,0,0,0,149,152,1,0,0,0,150,148,1,0,0,0,150,151,1,0,0,0,151,153,
	1,0,0,0,152,150,1,0,0,0,153,154,5,4,0,0,154,494,1,0,0,0,155,156,5,110,0,
	0,156,157,5,2,0,0,157,158,5,124,0,0,158,164,3,8,4,0,159,160,5,3,0,0,160,
	161,5,108,0,0,161,163,3,8,4,0,162,159,1,0,0,0,163,166,1,0,0,0,164,162,1,
	0,0,0,164,165,1,0,0,0,165,167,1,0,0,0,166,164,1,0,0,0,167,168,5,108,0,0,
	168,169,5,125,0,0,169,170,5,4,0,0,170,494,1,0,0,0,171,172,5,110,0,0,172,
	174,5,9,0,0,173,171,1,0,0,0,173,174,1,0,0,0,174,175,1,0,0,0,175,187,5,110,
	0,0,176,177,5,10,0,0,177,182,3,18,9,0,178,179,5,3,0,0,179,181,3,18,9,0,
	180,178,1,0,0,0,181,184,1,0,0,0,182,180,1,0,0,0,182,183,1,0,0,0,183,185,
	1,0,0,0,184,182,1,0,0,0,185,186,5,11,0,0,186,188,1,0,0,0,187,176,1,0,0,
	0,187,188,1,0,0,0,188,190,1,0,0,0,189,173,1,0,0,0,189,190,1,0,0,0,190,191,
	1,0,0,0,191,192,5,12,0,0,192,193,5,110,0,0,193,194,5,13,0,0,194,201,3,6,
	3,0,195,196,5,3,0,0,196,197,5,110,0,0,197,198,5,13,0,0,198,200,3,6,3,0,
	199,195,1,0,0,0,200,203,1,0,0,0,201,199,1,0,0,0,201,202,1,0,0,0,202,204,
	1,0,0,0,203,201,1,0,0,0,204,205,5,14,0,0,205,494,1,0,0,0,206,207,5,110,
	0,0,207,209,5,9,0,0,208,206,1,0,0,0,208,209,1,0,0,0,209,210,1,0,0,0,210,
	222,5,110,0,0,211,212,5,10,0,0,212,217,3,18,9,0,213,214,5,3,0,0,214,216,
	3,18,9,0,215,213,1,0,0,0,216,219,1,0,0,0,217,215,1,0,0,0,217,218,1,0,0,
	0,218,220,1,0,0,0,219,217,1,0,0,0,220,221,5,11,0,0,221,223,1,0,0,0,222,
	211,1,0,0,0,222,223,1,0,0,0,223,225,1,0,0,0,224,208,1,0,0,0,224,225,1,0,
	0,0,225,226,1,0,0,0,226,227,5,12,0,0,227,228,5,124,0,0,228,229,5,110,0,
	0,229,230,5,13,0,0,230,238,3,6,3,0,231,232,5,3,0,0,232,233,5,108,0,0,233,
	234,5,110,0,0,234,235,5,13,0,0,235,237,3,6,3,0,236,231,1,0,0,0,237,240,
	1,0,0,0,238,236,1,0,0,0,238,239,1,0,0,0,239,241,1,0,0,0,240,238,1,0,0,0,
	241,242,5,108,0,0,242,243,5,125,0,0,243,244,5,14,0,0,244,494,1,0,0,0,245,
	246,5,110,0,0,246,248,5,9,0,0,247,245,1,0,0,0,247,248,1,0,0,0,248,249,1,
	0,0,0,249,261,5,110,0,0,250,251,5,10,0,0,251,256,3,18,9,0,252,253,5,3,0,
	0,253,255,3,18,9,0,254,252,1,0,0,0,255,258,1,0,0,0,256,254,1,0,0,0,256,
	257,1,0,0,0,257,259,1,0,0,0,258,256,1,0,0,0,259,260,5,11,0,0,260,262,1,
	0,0,0,261,250,1,0,0,0,261,262,1,0,0,0,262,263,1,0,0,0,263,264,5,12,0,0,
	264,494,5,14,0,0,265,266,5,110,0,0,266,268,5,9,0,0,267,265,1,0,0,0,267,
	268,1,0,0,0,268,269,1,0,0,0,269,281,5,110,0,0,270,271,5,10,0,0,271,276,
	3,18,9,0,272,273,5,3,0,0,273,275,3,18,9,0,274,272,1,0,0,0,275,278,1,0,0,
	0,276,274,1,0,0,0,276,277,1,0,0,0,277,279,1,0,0,0,278,276,1,0,0,0,279,280,
	5,11,0,0,280,282,1,0,0,0,281,270,1,0,0,0,281,282,1,0,0,0,282,284,1,0,0,
	0,283,267,1,0,0,0,283,284,1,0,0,0,284,285,1,0,0,0,285,286,5,12,0,0,286,
	291,3,6,3,0,287,288,5,3,0,0,288,290,3,6,3,0,289,287,1,0,0,0,290,293,1,0,
	0,0,291,289,1,0,0,0,291,292,1,0,0,0,292,294,1,0,0,0,293,291,1,0,0,0,294,
	295,5,14,0,0,295,494,1,0,0,0,296,297,5,110,0,0,297,299,5,9,0,0,298,296,
	1,0,0,0,298,299,1,0,0,0,299,300,1,0,0,0,300,312,5,110,0,0,301,302,5,10,
	0,0,302,307,3,18,9,0,303,304,5,3,0,0,304,306,3,18,9,0,305,303,1,0,0,0,306,
	309,1,0,0,0,307,305,1,0,0,0,307,308,1,0,0,0,308,310,1,0,0,0,309,307,1,0,
	0,0,310,311,5,11,0,0,311,313,1,0,0,0,312,301,1,0,0,0,312,313,1,0,0,0,313,
	315,1,0,0,0,314,298,1,0,0,0,314,315,1,0,0,0,315,316,1,0,0,0,316,317,5,12,
	0,0,317,318,5,124,0,0,318,324,3,6,3,0,319,320,5,3,0,0,320,321,5,108,0,0,
	321,323,3,6,3,0,322,319,1,0,0,0,323,326,1,0,0,0,324,322,1,0,0,0,324,325,
	1,0,0,0,325,327,1,0,0,0,326,324,1,0,0,0,327,328,5,108,0,0,328,329,5,125,
	0,0,329,330,5,14,0,0,330,494,1,0,0,0,331,332,5,110,0,0,332,334,5,9,0,0,
	333,331,1,0,0,0,333,334,1,0,0,0,334,335,1,0,0,0,335,347,5,110,0,0,336,337,
	5,10,0,0,337,342,3,18,9,0,338,339,5,3,0,0,339,341,3,18,9,0,340,338,1,0,
	0,0,341,344,1,0,0,0,342,340,1,0,0,0,342,343,1,0,0,0,343,345,1,0,0,0,344,
	342,1,0,0,0,345,346,5,11,0,0,346,348,1,0,0,0,347,336,1,0,0,0,347,348,1,
	0,0,0,348,350,1,0,0,0,349,333,1,0,0,0,349,350,1,0,0,0,350,351,1,0,0,0,351,
	352,5,12,0,0,352,353,5,15,0,0,353,360,3,6,3,0,354,355,5,3,0,0,355,356,5,
	110,0,0,356,357,5,13,0,0,357,359,3,6,3,0,358,354,1,0,0,0,359,362,1,0,0,
	0,360,358,1,0,0,0,360,361,1,0,0,0,361,363,1,0,0,0,362,360,1,0,0,0,363,364,
	5,14,0,0,364,494,1,0,0,0,365,366,5,110,0,0,366,368,5,9,0,0,367,365,1,0,
	0,0,367,368,1,0,0,0,368,369,1,0,0,0,369,381,5,110,0,0,370,371,5,10,0,0,
	371,376,3,18,9,0,372,373,5,3,0,0,373,375,3,18,9,0,374,372,1,0,0,0,375,378,
	1,0,0,0,376,374,1,0,0,0,376,377,1,0,0,0,377,379,1,0,0,0,378,376,1,0,0,0,
	379,380,5,11,0,0,380,382,1,0,0,0,381,370,1,0,0,0,381,382,1,0,0,0,382,384,
	1,0,0,0,383,367,1,0,0,0,383,384,1,0,0,0,384,385,1,0,0,0,385,386,5,12,0,
	0,386,387,5,124,0,0,387,388,5,15,0,0,388,396,3,6,3,0,389,390,5,3,0,0,390,
	391,5,108,0,0,391,392,5,110,0,0,392,393,5,13,0,0,393,395,3,6,3,0,394,389,
	1,0,0,0,395,398,1,0,0,0,396,394,1,0,0,0,396,397,1,0,0,0,397,399,1,0,0,0,
	398,396,1,0,0,0,399,400,5,108,0,0,400,401,5,125,0,0,401,402,5,14,0,0,402,
	494,1,0,0,0,403,494,5,110,0,0,404,405,5,16,0,0,405,410,3,6,3,0,406,407,
	5,3,0,0,407,409,3,6,3,0,408,406,1,0,0,0,409,412,1,0,0,0,410,408,1,0,0,0,
	410,411,1,0,0,0,411,413,1,0,0,0,412,410,1,0,0,0,413,414,7,0,0,0,414,494,
	1,0,0,0,415,416,5,16,0,0,416,417,5,124,0,0,417,423,3,6,3,0,418,419,5,3,
	0,0,419,420,5,108,0,0,420,422,3,6,3,0,421,418,1,0,0,0,422,425,1,0,0,0,423,
	421,1,0,0,0,423,424,1,0,0,0,424,426,1,0,0,0,425,423,1,0,0,0,426,427,5,108,
	0,0,427,428,5,125,0,0,428,429,7,0,0,0,429,494,1,0,0,0,430,431,5,27,0,0,
	431,494,3,6,3,31,432,433,5,28,0,0,433,494,3,6,3,30,434,435,5,29,0,0,435,
	494,3,6,3,29,436,437,5,25,0,0,437,494,3,6,3,28,438,439,5,30,0,0,439,440,
	3,6,3,0,440,441,5,31,0,0,441,442,3,6,3,27,442,494,1,0,0,0,443,444,5,30,
	0,0,444,494,3,6,3,26,445,446,5,30,0,0,446,447,5,2,0,0,447,448,3,18,9,0,
	448,449,5,4,0,0,449,450,5,16,0,0,450,451,3,6,3,0,451,454,5,18,0,0,452,453,
	5,31,0,0,453,455,3,6,3,0,454,452,1,0,0,0,454,455,1,0,0,0,455,494,1,0,0,
	0,456,457,7,1,0,0,457,494,3,6,3,23,458,459,5,61,0,0,459,460,3,6,3,0,460,
	461,5,62,0,0,461,462,3,6,3,0,462,463,5,63,0,0,463,464,3,6,3,5,464,494,1,
	0,0,0,465,466,5,61,0,0,466,467,5,2,0,0,467,468,3,6,3,0,468,469,5,4,0,0,
	469,470,3,6,3,0,470,471,5,63,0,0,471,472,3,6,3,4,472,494,1,0,0,0,473,494,
	3,40,20,0,474,475,5,8,0,0,475,484,5,2,0,0,476,481,3,16,8,0,477,478,5,3,
	0,0,478,480,3,16,8,0,479,477,1,0,0,0,480,483,1,0,0,0,481,479,1,0,0,0,481,
	482,1,0,0,0,482,485,1,0,0,0,483,481,1,0,0,0,484,476,1,0,0,0,484,485,1,0,
	0,0,485,486,1,0,0,0,486,489,5,4,0,0,487,488,5,65,0,0,488,490,3,18,9,0,489,
	487,1,0,0,0,489,490,1,0,0,0,490,491,1,0,0,0,491,492,5,13,0,0,492,494,3,
	6,3,1,493,91,1,0,0,0,493,93,1,0,0,0,493,104,1,0,0,0,493,119,1,0,0,0,493,
	120,1,0,0,0,493,123,1,0,0,0,493,125,1,0,0,0,493,127,1,0,0,0,493,134,1,0,
	0,0,493,140,1,0,0,0,493,143,1,0,0,0,493,155,1,0,0,0,493,189,1,0,0,0,493,
	224,1,0,0,0,493,247,1,0,0,0,493,283,1,0,0,0,493,314,1,0,0,0,493,349,1,0,
	0,0,493,383,1,0,0,0,493,403,1,0,0,0,493,404,1,0,0,0,493,415,1,0,0,0,493,
	430,1,0,0,0,493,432,1,0,0,0,493,434,1,0,0,0,493,436,1,0,0,0,493,438,1,0,
	0,0,493,443,1,0,0,0,493,445,1,0,0,0,493,456,1,0,0,0,493,458,1,0,0,0,493,
	465,1,0,0,0,493,473,1,0,0,0,493,474,1,0,0,0,494,677,1,0,0,0,495,496,10,
	24,0,0,496,497,5,32,0,0,497,676,3,6,3,24,498,499,10,22,0,0,499,500,7,2,
	0,0,500,676,3,6,3,23,501,502,10,21,0,0,502,503,7,3,0,0,503,676,3,6,3,22,
	504,508,10,20,0,0,505,509,5,41,0,0,506,507,5,11,0,0,507,509,5,11,0,0,508,
	505,1,0,0,0,508,506,1,0,0,0,509,510,1,0,0,0,510,676,3,6,3,21,511,512,10,
	19,0,0,512,513,5,42,0,0,513,676,3,6,3,20,514,515,10,18,0,0,515,516,5,43,
	0,0,516,676,3,6,3,19,517,518,10,17,0,0,518,519,5,44,0,0,519,676,3,6,3,18,
	520,521,10,16,0,0,521,522,7,4,0,0,522,676,3,6,3,17,523,524,10,15,0,0,524,
	525,7,5,0,0,525,676,3,6,3,16,526,527,10,11,0,0,527,528,7,6,0,0,528,676,
	3,6,3,12,529,530,10,10,0,0,530,531,7,7,0,0,531,676,3,6,3,11,532,533,10,
	9,0,0,533,534,7,8,0,0,534,676,3,6,3,10,535,536,10,8,0,0,536,537,7,9,0,0,
	537,676,3,6,3,9,538,539,10,7,0,0,539,540,5,60,0,0,540,676,3,6,3,7,541,542,
	10,3,0,0,542,543,5,64,0,0,543,676,3,6,3,4,544,545,10,47,0,0,545,546,5,9,
	0,0,546,547,5,110,0,0,547,548,5,2,0,0,548,676,5,4,0,0,549,550,10,46,0,0,
	550,551,5,9,0,0,551,552,5,110,0,0,552,553,5,2,0,0,553,558,3,8,4,0,554,555,
	5,3,0,0,555,557,3,8,4,0,556,554,1,0,0,0,557,560,1,0,0,0,558,556,1,0,0,0,
	558,559,1,0,0,0,559,561,1,0,0,0,560,558,1,0,0,0,561,562,5,4,0,0,562,676,
	1,0,0,0,563,564,10,45,0,0,564,565,5,9,0,0,565,566,5,110,0,0,566,567,5,2,
	0,0,567,568,5,124,0,0,568,574,3,8,4,0,569,570,5,3,0,0,570,571,5,108,0,0,
	571,573,3,8,4,0,572,569,1,0,0,0,573,576,1,0,0,0,574,572,1,0,0,0,574,575,
	1,0,0,0,575,577,1,0,0,0,576,574,1,0,0,0,577,578,5,108,0,0,578,579,5,125,
	0,0,579,580,5,4,0,0,580,676,1,0,0,0,581,582,10,44,0,0,582,583,5,19,0,0,
	583,584,5,110,0,0,584,585,5,2,0,0,585,676,5,4,0,0,586,587,10,43,0,0,587,
	588,5,19,0,0,588,589,5,110,0,0,589,590,5,2,0,0,590,595,3,8,4,0,591,592,
	5,3,0,0,592,594,3,8,4,0,593,591,1,0,0,0,594,597,1,0,0,0,595,593,1,0,0,0,
	595,596,1,0,0,0,596,598,1,0,0,0,597,595,1,0,0,0,598,599,5,4,0,0,599,676,
	1,0,0,0,600,601,10,42,0,0,601,602,5,19,0,0,602,603,5,110,0,0,603,604,5,
	2,0,0,604,605,5,124,0,0,605,611,3,8,4,0,606,607,5,3,0,0,607,608,5,108,0,
	0,608,610,3,8,4,0,609,606,1,0,0,0,610,613,1,0,0,0,611,609,1,0,0,0,611,612,
	1,0,0,0,612,614,1,0,0,0,613,611,1,0,0,0,614,615,5,108,0,0,615,616,5,125,
	0,0,616,617,5,4,0,0,617,676,1,0,0,0,618,619,10,41,0,0,619,620,5,9,0,0,620,
	676,5,110,0,0,621,622,10,40,0,0,622,623,5,19,0,0,623,676,5,110,0,0,624,
	625,10,39,0,0,625,626,5,16,0,0,626,627,3,6,3,0,627,628,5,18,0,0,628,676,
	1,0,0,0,629,630,10,38,0,0,630,631,5,16,0,0,631,632,5,124,0,0,632,633,3,
	6,3,0,633,634,5,108,0,0,634,635,5,125,0,0,635,636,5,18,0,0,636,676,1,0,
	0,0,637,638,10,37,0,0,638,640,5,16,0,0,639,641,3,6,3,0,640,639,1,0,0,0,
	640,641,1,0,0,0,641,642,1,0,0,0,642,643,5,20,0,0,643,676,5,18,0,0,644,645,
	10,36,0,0,645,646,5,16,0,0,646,647,7,10,0,0,647,648,3,6,3,0,648,649,5,18,
	0,0,649,676,1,0,0,0,650,651,10,35,0,0,651,676,5,23,0,0,652,653,10,34,0,
	0,653,654,5,24,0,0,654,676,3,18,9,0,655,656,10,33,0,0,656,657,5,24,0,0,
	657,658,5,25,0,0,658,676,3,18,9,0,659,660,10,32,0,0,660,661,5,26,0,0,661,
	676,3,18,9,0,662,663,10,14,0,0,663,664,5,48,0,0,664,676,3,18,9,0,665,666,
	10,13,0,0,666,667,5,49,0,0,667,676,3,18,9,0,668,669,10,12,0,0,669,670,5,
	48,0,0,670,671,5,33,0,0,671,676,3,18,9,0,672,673,10,6,0,0,673,674,5,60,
	0,0,674,676,3,10,5,0,675,495,1,0,0,0,675,498,1,0,0,0,675,501,1,0,0,0,675,
	504,1,0,0,0,675,511,1,0,0,0,675,514,1,0,0,0,675,517,1,0,0,0,675,520,1,0,
	0,0,675,523,1,0,0,0,675,526,1,0,0,0,675,529,1,0,0,0,675,532,1,0,0,0,675,
	535,1,0,0,0,675,538,1,0,0,0,675,541,1,0,0,0,675,544,1,0,0,0,675,549,1,0,
	0,0,675,563,1,0,0,0,675,581,1,0,0,0,675,586,1,0,0,0,675,600,1,0,0,0,675,
	618,1,0,0,0,675,621,1,0,0,0,675,624,1,0,0,0,675,629,1,0,0,0,675,637,1,0,
	0,0,675,644,1,0,0,0,675,650,1,0,0,0,675,652,1,0,0,0,675,655,1,0,0,0,675,
	659,1,0,0,0,675,662,1,0,0,0,675,665,1,0,0,0,675,668,1,0,0,0,675,672,1,0,
	0,0,676,679,1,0,0,0,677,675,1,0,0,0,677,678,1,0,0,0,678,7,1,0,0,0,679,677,
	1,0,0,0,680,681,5,110,0,0,681,683,5,13,0,0,682,680,1,0,0,0,682,683,1,0,
	0,0,683,685,1,0,0,0,684,686,5,66,0,0,685,684,1,0,0,0,685,686,1,0,0,0,686,
	687,1,0,0,0,687,688,3,6,3,0,688,9,1,0,0,0,689,691,5,67,0,0,690,692,3,6,
	3,0,691,690,1,0,0,0,691,692,1,0,0,0,692,704,1,0,0,0,693,694,5,68,0,0,694,
	704,3,6,3,0,695,697,5,69,0,0,696,698,5,110,0,0,697,696,1,0,0,0,697,698,
	1,0,0,0,698,704,1,0,0,0,699,701,5,70,0,0,700,702,5,110,0,0,701,700,1,0,
	0,0,701,702,1,0,0,0,702,704,1,0,0,0,703,689,1,0,0,0,703,693,1,0,0,0,703,
	695,1,0,0,0,703,699,1,0,0,0,704,11,1,0,0,0,705,706,3,6,3,0,706,707,5,108,
	0,0,707,711,1,0,0,0,708,711,3,40,20,0,709,711,3,14,7,0,710,705,1,0,0,0,
	710,708,1,0,0,0,710,709,1,0,0,0,711,13,1,0,0,0,712,713,5,29,0,0,713,714,
	3,6,3,0,714,715,5,71,0,0,715,716,5,110,0,0,716,719,5,62,0,0,717,720,3,12,
	6,0,718,720,3,4,2,0,719,717,1,0,0,0,719,718,1,0,0,0,720,15,1,0,0,0,721,
	724,5,110,0,0,722,723,5,65,0,0,723,725,3,18,9,0,724,722,1,0,0,0,724,725,
	1,0,0,0,725,17,1,0,0,0,726,729,6,9,-1,0,727,728,5,110,0,0,728,730,5,9,0,
	0,729,727,1,0,0,0,729,730,1,0,0,0,730,731,1,0,0,0,731,732,5,110,0,0,732,
	733,5,10,0,0,733,738,3,18,9,0,734,735,5,3,0,0,735,737,3,18,9,0,736,734,
	1,0,0,0,737,740,1,0,0,0,738,736,1,0,0,0,738,739,1,0,0,0,739,741,1,0,0,0,
	740,738,1,0,0,0,741,742,5,11,0,0,742,828,1,0,0,0,743,744,5,110,0,0,744,
	745,5,9,0,0,745,828,5,110,0,0,746,828,5,110,0,0,747,748,5,27,0,0,748,828,
	3,18,9,16,749,750,5,2,0,0,750,751,3,18,9,0,751,752,5,4,0,0,752,828,1,0,
	0,0,753,828,5,111,0,0,754,756,5,10,0,0,755,757,5,66,0,0,756,755,1,0,0,0,
	756,757,1,0,0,0,757,758,1,0,0,0,758,759,5,110,0,0,759,828,5,11,0,0,760,
	761,5,73,0,0,761,828,3,6,3,0,762,763,5,8,0,0,763,764,5,2,0,0,764,767,5,
	4,0,0,765,766,7,11,0,0,766,768,3,18,9,0,767,765,1,0,0,0,767,768,1,0,0,0,
	768,828,1,0,0,0,769,770,5,8,0,0,770,773,5,2,0,0,771,772,5,110,0,0,772,774,
	5,65,0,0,773,771,1,0,0,0,773,774,1,0,0,0,774,775,1,0,0,0,775,784,3,18,9,
	0,776,779,5,3,0,0,777,778,5,110,0,0,778,780,5,65,0,0,779,777,1,0,0,0,779,
	780,1,0,0,0,780,781,1,0,0,0,781,783,3,18,9,0,782,776,1,0,0,0,783,786,1,
	0,0,0,784,782,1,0,0,0,784,785,1,0,0,0,785,787,1,0,0,0,786,784,1,0,0,0,787,
	790,5,4,0,0,788,789,7,11,0,0,789,791,3,18,9,0,790,788,1,0,0,0,790,791,1,
	0,0,0,791,828,1,0,0,0,792,793,5,2,0,0,793,794,5,4,0,0,794,795,5,75,0,0,
	795,828,3,18,9,3,796,799,5,2,0,0,797,798,5,110,0,0,798,800,5,65,0,0,799,
	797,1,0,0,0,799,800,1,0,0,0,800,801,1,0,0,0,801,810,3,18,9,0,802,805,5,
	3,0,0,803,804,5,110,0,0,804,806,5,65,0,0,805,803,1,0,0,0,805,806,1,0,0,
	0,806,807,1,0,0,0,807,809,3,18,9,0,808,802,1,0,0,0,809,812,1,0,0,0,810,
	808,1,0,0,0,810,811,1,0,0,0,811,813,1,0,0,0,812,810,1,0,0,0,813,814,5,4,
	0,0,814,815,5,75,0,0,815,816,3,18,9,2,816,828,1,0,0,0,817,818,5,2,0,0,818,
	821,3,18,9,0,819,820,5,3,0,0,820,822,3,18,9,0,821,819,1,0,0,0,822,823,1,
	0,0,0,823,821,1,0,0,0,823,824,1,0,0,0,824,825,1,0,0,0,825,826,5,4,0,0,826,
	828,1,0,0,0,827,726,1,0,0,0,827,743,1,0,0,0,827,746,1,0,0,0,827,747,1,0,
	0,0,827,749,1,0,0,0,827,753,1,0,0,0,827,754,1,0,0,0,827,760,1,0,0,0,827,
	762,1,0,0,0,827,769,1,0,0,0,827,792,1,0,0,0,827,796,1,0,0,0,827,817,1,0,
	0,0,828,850,1,0,0,0,829,830,10,12,0,0,830,831,5,42,0,0,831,849,3,18,9,13,
	832,833,10,11,0,0,833,834,5,44,0,0,834,849,3,18,9,12,835,836,10,6,0,0,836,
	837,5,74,0,0,837,849,3,18,9,7,838,839,10,15,0,0,839,849,5,72,0,0,840,841,
	10,14,0,0,841,842,5,16,0,0,842,849,7,0,0,0,843,844,10,13,0,0,844,845,5,
	16,0,0,845,846,3,6,3,0,846,847,5,18,0,0,847,849,1,0,0,0,848,829,1,0,0,0,
	848,832,1,0,0,0,848,835,1,0,0,0,848,838,1,0,0,0,848,840,1,0,0,0,848,843,
	1,0,0,0,849,852,1,0,0,0,850,848,1,0,0,0,850,851,1,0,0,0,851,19,1,0,0,0,
	852,850,1,0,0,0,853,854,5,76,0,0,854,855,5,110,0,0,855,856,5,24,0,0,856,
	862,5,124,0,0,857,858,5,110,0,0,858,859,5,65,0,0,859,860,3,18,9,0,860,861,
	5,108,0,0,861,863,1,0,0,0,862,857,1,0,0,0,863,864,1,0,0,0,864,862,1,0,0,
	0,864,865,1,0,0,0,865,866,1,0,0,0,866,867,5,125,0,0,867,21,1,0,0,0,868,
	869,5,110,0,0,869,870,5,65,0,0,870,894,3,18,9,0,871,872,5,110,0,0,872,873,
	5,65,0,0,873,874,3,18,9,0,874,875,5,13,0,0,875,876,3,6,3,0,876,894,1,0,
	0,0,877,878,5,110,0,0,878,879,5,13,0,0,879,894,3,6,3,0,880,881,5,15,0,0,
	881,882,5,110,0,0,882,883,5,65,0,0,883,894,3,18,9,0,884,894,5,15,0,0,885,
	886,5,66,0,0,886,894,3,22,11,0,887,888,5,110,0,0,888,894,3,22,11,0,889,
	890,5,77,0,0,890,894,3,22,11,0,891,892,5,78,0,0,892,894,3,22,11,0,893,868,
	1,0,0,0,893,871,1,0,0,0,893,877,1,0,0,0,893,880,1,0,0,0,893,884,1,0,0,0,
	893,885,1,0,0,0,893,887,1,0,0,0,893,889,1,0,0,0,893,891,1,0,0,0,894,23,
	1,0,0,0,895,896,5,77,0,0,896,897,5,110,0,0,897,898,5,13,0,0,898,1285,3,
	12,6,0,899,900,5,78,0,0,900,901,5,110,0,0,901,902,5,13,0,0,902,1285,3,12,
	6,0,903,904,5,79,0,0,904,905,5,110,0,0,905,906,5,13,0,0,906,907,3,6,3,0,
	907,908,5,108,0,0,908,1285,1,0,0,0,909,910,5,77,0,0,910,911,5,110,0,0,911,
	912,5,65,0,0,912,913,3,18,9,0,913,914,5,13,0,0,914,915,3,12,6,0,915,1285,
	1,0,0,0,916,917,5,78,0,0,917,918,5,110,0,0,918,919,5,65,0,0,919,920,3,18,
	9,0,920,921,5,13,0,0,921,922,3,12,6,0,922,1285,1,0,0,0,923,924,5,79,0,0,
	924,925,5,110,0,0,925,926,5,65,0,0,926,927,3,18,9,0,927,928,5,13,0,0,928,
	929,3,6,3,0,929,930,5,108,0,0,930,1285,1,0,0,0,931,932,5,77,0,0,932,933,
	5,110,0,0,933,934,5,65,0,0,934,935,3,18,9,0,935,936,5,108,0,0,936,1285,
	1,0,0,0,937,938,7,12,0,0,938,941,3,30,15,0,939,940,5,65,0,0,940,942,3,18,
	9,0,941,939,1,0,0,0,941,942,1,0,0,0,942,943,1,0,0,0,943,944,5,13,0,0,944,
	945,3,12,6,0,945,1285,1,0,0,0,946,947,3,6,3,0,947,948,7,13,0,0,948,949,
	3,12,6,0,949,1285,1,0,0,0,950,952,5,69,0,0,951,953,5,110,0,0,952,951,1,
	0,0,0,952,953,1,0,0,0,953,956,1,0,0,0,954,955,5,61,0,0,955,957,3,6,3,0,
	956,954,1,0,0,0,956,957,1,0,0,0,957,958,1,0,0,0,958,1285,5,108,0,0,959,
	961,5,70,0,0,960,962,5,110,0,0,961,960,1,0,0,0,961,962,1,0,0,0,962,965,
	1,0,0,0,963,964,5,61,0,0,964,966,3,6,3,0,965,963,1,0,0,0,965,966,1,0,0,
	0,966,967,1,0,0,0,967,1285,5,108,0,0,968,971,5,90,0,0,969,970,5,61,0,0,
	970,972,3,6,3,0,971,969,1,0,0,0,971,972,1,0,0,0,972,973,1,0,0,0,973,1285,
	5,108,0,0,974,977,5,91,0,0,975,976,5,61,0,0,976,978,3,6,3,0,977,975,1,0,
	0,0,977,978,1,0,0,0,978,979,1,0,0,0,979,1285,5,108,0,0,980,981,5,68,0,0,
	981,984,3,6,3,0,982,983,5,61,0,0,983,985,3,6,3,0,984,982,1,0,0,0,984,985,
	1,0,0,0,985,986,1,0,0,0,986,987,5,108,0,0,987,1285,1,0,0,0,988,989,5,92,
	0,0,989,992,3,6,3,0,990,991,5,3,0,0,991,993,3,6,3,0,992,990,1,0,0,0,992,
	993,1,0,0,0,993,994,1,0,0,0,994,995,5,108,0,0,995,1285,1,0,0,0,996,1285,
	3,26,13,0,997,999,5,67,0,0,998,1000,3,6,3,0,999,998,1,0,0,0,999,1000,1,
	0,0,0,1000,1003,1,0,0,0,1001,1002,5,61,0,0,1002,1004,3,6,3,0,1003,1001,
	1,0,0,0,1003,1004,1,0,0,0,1004,1005,1,0,0,0,1005,1285,5,108,0,0,1006,1007,
	5,67,0,0,1007,1285,3,40,20,0,1008,1009,3,6,3,0,1009,1010,5,61,0,0,1010,
	1011,3,6,3,0,1011,1012,5,108,0,0,1012,1285,1,0,0,0,1013,1014,3,6,3,0,1014,
	1017,5,60,0,0,1015,1018,3,10,5,0,1016,1018,3,6,3,0,1017,1015,1,0,0,0,1017,
	1016,1,0,0,0,1018,1019,1,0,0,0,1019,1020,5,108,0,0,1020,1285,1,0,0,0,1021,
	1022,3,6,3,0,1022,1023,5,93,0,0,1023,1024,3,6,3,0,1024,1025,5,108,0,0,1025,
	1285,1,0,0,0,1026,1027,5,94,0,0,1027,1285,3,24,12,0,1028,1029,5,94,0,0,
	1029,1030,5,62,0,0,1030,1285,3,4,2,0,1031,1032,5,95,0,0,1032,1285,3,24,
	12,0,1033,1034,5,95,0,0,1034,1035,5,62,0,0,1035,1285,3,4,2,0,1036,1040,
	5,96,0,0,1037,1038,7,12,0,0,1038,1039,5,110,0,0,1039,1041,5,13,0,0,1040,
	1037,1,0,0,0,1040,1041,1,0,0,0,1041,1042,1,0,0,0,1042,1043,3,6,3,0,1043,
	1044,5,62,0,0,1044,1046,5,124,0,0,1045,1047,3,38,19,0,1046,1045,1,0,0,0,
	1047,1048,1,0,0,0,1048,1046,1,0,0,0,1048,1049,1,0,0,0,1049,1050,1,0,0,0,
	1050,1051,5,125,0,0,1051,1285,1,0,0,0,1052,1053,5,61,0,0,1053,1054,7,12,
	0,0,1054,1055,5,110,0,0,1055,1056,5,13,0,0,1056,1057,3,6,3,0,1057,1058,
	5,62,0,0,1058,1066,3,4,2,0,1059,1061,5,63,0,0,1060,1062,5,62,0,0,1061,1060,
	1,0,0,0,1061,1062,1,0,0,0,1062,1063,1,0,0,0,1063,1067,3,4,2,0,1064,1065,
	5,63,0,0,1065,1067,3,24,12,0,1066,1059,1,0,0,0,1066,1064,1,0,0,0,1066,1067,
	1,0,0,0,1067,1285,1,0,0,0,1068,1069,5,61,0,0,1069,1070,3,6,3,0,1070,1071,
	5,62,0,0,1071,1079,3,4,2,0,1072,1074,5,63,0,0,1073,1075,5,62,0,0,1074,1073,
	1,0,0,0,1074,1075,1,0,0,0,1075,1076,1,0,0,0,1076,1080,3,4,2,0,1077,1078,
	5,63,0,0,1078,1080,3,24,12,0,1079,1072,1,0,0,0,1079,1077,1,0,0,0,1079,1080,
	1,0,0,0,1080,1285,1,0,0,0,1081,1082,5,110,0,0,1082,1083,5,65,0,0,1083,1285,
	3,24,12,0,1084,1085,5,93,0,0,1085,1086,3,6,3,0,1086,1087,5,62,0,0,1087,
	1093,3,4,2,0,1088,1090,5,63,0,0,1089,1091,5,62,0,0,1090,1089,1,0,0,0,1090,
	1091,1,0,0,0,1091,1092,1,0,0,0,1092,1094,3,4,2,0,1093,1088,1,0,0,0,1093,
	1094,1,0,0,0,1094,1285,1,0,0,0,1095,1097,5,97,0,0,1096,1098,7,12,0,0,1097,
	1096,1,0,0,0,1097,1098,1,0,0,0,1098,1099,1,0,0,0,1099,1100,5,110,0,0,1100,
	1101,5,31,0,0,1101,1102,3,6,3,0,1102,1103,5,98,0,0,1103,1104,3,6,3,0,1104,
	1105,5,62,0,0,1105,1111,3,4,2,0,1106,1108,5,63,0,0,1107,1109,5,62,0,0,1108,
	1107,1,0,0,0,1108,1109,1,0,0,0,1109,1110,1,0,0,0,1110,1112,3,4,2,0,1111,
	1106,1,0,0,0,1111,1112,1,0,0,0,1112,1285,1,0,0,0,1113,1115,5,97,0,0,1114,
	1116,7,12,0,0,1115,1114,1,0,0,0,1115,1116,1,0,0,0,1116,1119,1,0,0,0,1117,
	1118,5,110,0,0,1118,1120,5,3,0,0,1119,1117,1,0,0,0,1119,1120,1,0,0,0,1120,
	1123,1,0,0,0,1121,1124,5,110,0,0,1122,1124,3,30,15,0,1123,1121,1,0,0,0,
	1123,1122,1,0,0,0,1124,1125,1,0,0,0,1125,1126,5,31,0,0,1126,1129,3,6,3,
	0,1127,1128,5,110,0,0,1128,1130,3,6,3,0,1129,1127,1,0,0,0,1129,1130,1,0,
	0,0,1130,1131,1,0,0,0,1131,1132,5,62,0,0,1132,1138,3,4,2,0,1133,1135,5,
	63,0,0,1134,1136,5,62,0,0,1135,1134,1,0,0,0,1135,1136,1,0,0,0,1136,1137,
	1,0,0,0,1137,1139,3,4,2,0,1138,1133,1,0,0,0,1138,1139,1,0,0,0,1139,1285,
	1,0,0,0,1140,1141,5,29,0,0,1141,1142,3,6,3,0,1142,1143,5,108,0,0,1143,1285,
	1,0,0,0,1144,1145,5,29,0,0,1145,1146,3,6,3,0,1146,1147,5,71,0,0,1147,1148,
	5,110,0,0,1148,1149,5,62,0,0,1149,1150,3,4,2,0,1150,1285,1,0,0,0,1151,1152,
	5,110,0,0,1152,1153,5,2,0,0,1153,1154,5,4,0,0,1154,1285,5,108,0,0,1155,
	1156,5,110,0,0,1156,1157,5,2,0,0,1157,1162,3,8,4,0,1158,1159,5,3,0,0,1159,
	1161,3,8,4,0,1160,1158,1,0,0,0,1161,1164,1,0,0,0,1162,1160,1,0,0,0,1162,
	1163,1,0,0,0,1163,1165,1,0,0,0,1164,1162,1,0,0,0,1165,1166,5,4,0,0,1166,
	1167,5,108,0,0,1167,1285,1,0,0,0,1168,1169,5,110,0,0,1169,1170,5,2,0,0,
	1170,1171,5,124,0,0,1171,1177,3,8,4,0,1172,1173,5,3,0,0,1173,1174,5,108,
	0,0,1174,1176,3,8,4,0,1175,1172,1,0,0,0,1176,1179,1,0,0,0,1177,1175,1,0,
	0,0,1177,1178,1,0,0,0,1178,1180,1,0,0,0,1179,1177,1,0,0,0,1180,1181,5,108,
	0,0,1181,1182,5,125,0,0,1182,1183,5,4,0,0,1183,1184,5,108,0,0,1184,1285,
	1,0,0,0,1185,1186,3,6,3,0,1186,1187,5,9,0,0,1187,1188,5,110,0,0,1188,1189,
	5,2,0,0,1189,1190,5,4,0,0,1190,1191,5,108,0,0,1191,1285,1,0,0,0,1192,1193,
	3,6,3,0,1193,1194,5,9,0,0,1194,1195,5,110,0,0,1195,1196,5,2,0,0,1196,1201,
	3,8,4,0,1197,1198,5,3,0,0,1198,1200,3,8,4,0,1199,1197,1,0,0,0,1200,1203,
	1,0,0,0,1201,1199,1,0,0,0,1201,1202,1,0,0,0,1202,1204,1,0,0,0,1203,1201,
	1,0,0,0,1204,1205,5,4,0,0,1205,1206,5,108,0,0,1206,1285,1,0,0,0,1207,1208,
	3,6,3,0,1208,1209,5,9,0,0,1209,1210,5,110,0,0,1210,1211,5,2,0,0,1211,1212,
	5,124,0,0,1212,1218,3,8,4,0,1213,1214,5,3,0,0,1214,1215,5,108,0,0,1215,
	1217,3,8,4,0,1216,1213,1,0,0,0,1217,1220,1,0,0,0,1218,1216,1,0,0,0,1218,
	1219,1,0,0,0,1219,1221,1,0,0,0,1220,1218,1,0,0,0,1221,1222,5,108,0,0,1222,
	1223,5,125,0,0,1223,1224,5,4,0,0,1224,1225,5,108,0,0,1225,1285,1,0,0,0,
	1226,1227,3,6,3,0,1227,1228,5,19,0,0,1228,1229,5,110,0,0,1229,1230,5,2,
	0,0,1230,1231,5,4,0,0,1231,1232,5,108,0,0,1232,1285,1,0,0,0,1233,1234,3,
	6,3,0,1234,1235,5,19,0,0,1235,1236,5,110,0,0,1236,1237,5,2,0,0,1237,1242,
	3,8,4,0,1238,1239,5,3,0,0,1239,1241,3,8,4,0,1240,1238,1,0,0,0,1241,1244,
	1,0,0,0,1242,1240,1,0,0,0,1242,1243,1,0,0,0,1243,1245,1,0,0,0,1244,1242,
	1,0,0,0,1245,1246,5,4,0,0,1246,1247,5,108,0,0,1247,1285,1,0,0,0,1248,1249,
	3,6,3,0,1249,1250,5,19,0,0,1250,1251,5,110,0,0,1251,1252,5,2,0,0,1252,1253,
	5,124,0,0,1253,1259,3,8,4,0,1254,1255,5,3,0,0,1255,1256,5,108,0,0,1256,
	1258,3,8,4,0,1257,1254,1,0,0,0,1258,1261,1,0,0,0,1259,1257,1,0,0,0,1259,
	1260,1,0,0,0,1260,1262,1,0,0,0,1261,1259,1,0,0,0,1262,1263,5,108,0,0,1263,
	1264,5,125,0,0,1264,1265,5,4,0,0,1265,1266,5,108,0,0,1266,1285,1,0,0,0,
	1267,1268,5,25,0,0,1268,1269,5,62,0,0,1269,1285,3,4,2,0,1270,1271,5,25,
	0,0,1271,1285,3,24,12,0,1272,1273,5,99,0,0,1273,1274,3,6,3,0,1274,1275,
	5,108,0,0,1275,1285,1,0,0,0,1276,1278,5,110,0,0,1277,1279,5,110,0,0,1278,
	1277,1,0,0,0,1278,1279,1,0,0,0,1279,1280,1,0,0,0,1280,1281,5,62,0,0,1281,
	1285,3,4,2,0,1282,1283,5,110,0,0,1283,1285,3,24,12,0,1284,895,1,0,0,0,1284,
	899,1,0,0,0,1284,903,1,0,0,0,1284,909,1,0,0,0,1284,916,1,0,0,0,1284,923,
	1,0,0,0,1284,931,1,0,0,0,1284,937,1,0,0,0,1284,946,1,0,0,0,1284,950,1,0,
	0,0,1284,959,1,0,0,0,1284,968,1,0,0,0,1284,974,1,0,0,0,1284,980,1,0,0,0,
	1284,988,1,0,0,0,1284,996,1,0,0,0,1284,997,1,0,0,0,1284,1006,1,0,0,0,1284,
	1008,1,0,0,0,1284,1013,1,0,0,0,1284,1021,1,0,0,0,1284,1026,1,0,0,0,1284,
	1028,1,0,0,0,1284,1031,1,0,0,0,1284,1033,1,0,0,0,1284,1036,1,0,0,0,1284,
	1052,1,0,0,0,1284,1068,1,0,0,0,1284,1081,1,0,0,0,1284,1084,1,0,0,0,1284,
	1095,1,0,0,0,1284,1113,1,0,0,0,1284,1140,1,0,0,0,1284,1144,1,0,0,0,1284,
	1151,1,0,0,0,1284,1155,1,0,0,0,1284,1168,1,0,0,0,1284,1185,1,0,0,0,1284,
	1192,1,0,0,0,1284,1207,1,0,0,0,1284,1226,1,0,0,0,1284,1233,1,0,0,0,1284,
	1248,1,0,0,0,1284,1267,1,0,0,0,1284,1270,1,0,0,0,1284,1272,1,0,0,0,1284,
	1276,1,0,0,0,1284,1282,1,0,0,0,1285,25,1,0,0,0,1286,1287,5,79,0,0,1287,
	1288,5,92,0,0,1288,1289,3,18,9,0,1289,1290,7,14,0,0,1290,1293,3,18,9,0,
	1291,1292,5,3,0,0,1292,1294,5,117,0,0,1293,1291,1,0,0,0,1293,1294,1,0,0,
	0,1294,1295,1,0,0,0,1295,1296,5,108,0,0,1296,1318,1,0,0,0,1297,1298,5,79,
	0,0,1298,1299,5,92,0,0,1299,1300,3,6,3,0,1300,1301,5,31,0,0,1301,1304,3,
	6,3,0,1302,1303,5,3,0,0,1303,1305,5,117,0,0,1304,1302,1,0,0,0,1304,1305,
	1,0,0,0,1305,1306,1,0,0,0,1306,1307,5,108,0,0,1307,1318,1,0,0,0,1308,1309,
	5,79,0,0,1309,1310,5,92,0,0,1310,1313,3,6,3,0,1311,1312,5,3,0,0,1312,1314,
	5,117,0,0,1313,1311,1,0,0,0,1313,1314,1,0,0,0,1314,1315,1,0,0,0,1315,1316,
	5,108,0,0,1316,1318,1,0,0,0,1317,1286,1,0,0,0,1317,1297,1,0,0,0,1317,1308,
	1,0,0,0,1318,27,1,0,0,0,1319,1320,5,110,0,0,1320,1323,3,6,3,0,1321,1322,
	5,3,0,0,1322,1324,3,6,3,0,1323,1321,1,0,0,0,1323,1324,1,0,0,0,1324,1333,
	1,0,0,0,1325,1326,5,110,0,0,1326,1327,5,110,0,0,1327,1330,3,6,3,0,1328,
	1329,5,3,0,0,1329,1331,3,6,3,0,1330,1328,1,0,0,0,1330,1331,1,0,0,0,1331,
	1333,1,0,0,0,1332,1319,1,0,0,0,1332,1325,1,0,0,0,1333,29,1,0,0,0,1334,1335,
	5,2,0,0,1335,1338,3,32,16,0,1336,1337,5,3,0,0,1337,1339,3,32,16,0,1338,
	1336,1,0,0,0,1339,1340,1,0,0,0,1340,1338,1,0,0,0,1340,1341,1,0,0,0,1341,
	1342,1,0,0,0,1342,1343,5,4,0,0,1343,31,1,0,0,0,1344,1347,5,110,0,0,1345,
	1347,3,30,15,0,1346,1344,1,0,0,0,1346,1345,1,0,0,0,1347,33,1,0,0,0,1348,
	1349,5,48,0,0,1349,1352,3,18,9,0,1350,1351,5,61,0,0,1351,1353,3,6,3,0,1352,
	1350,1,0,0,0,1352,1353,1,0,0,0,1353,1371,1,0,0,0,1354,1371,5,63,0,0,1355,
	1356,5,110,0,0,1356,1357,5,61,0,0,1357,1371,3,6,3,0,1358,1363,3,36,18,0,
	1359,1360,5,3,0,0,1360,1362,3,36,18,0,1361,1359,1,0,0,0,1362,1365,1,0,0,
	0,1363,1361,1,0,0,0,1363,1364,1,0,0,0,1364,1368,1,0,0,0,1365,1363,1,0,0,
	0,1366,1367,5,61,0,0,1367,1369,3,6,3,0,1368,1366,1,0,0,0,1368,1369,1,0,
	0,0,1369,1371,1,0,0,0,1370,1348,1,0,0,0,1370,1354,1,0,0,0,1370,1355,1,0,
	0,0,1370,1358,1,0,0,0,1371,35,1,0,0,0,1372,1379,3,6,3,0,1373,1374,3,6,3,
	0,1374,1375,5,20,0,0,1375,1379,1,0,0,0,1376,1377,7,10,0,0,1377,1379,3,6,
	3,0,1378,1372,1,0,0,0,1378,1373,1,0,0,0,1378,1376,1,0,0,0,1379,37,1,0,0,
	0,1380,1381,3,34,17,0,1381,1384,5,62,0,0,1382,1385,3,4,2,0,1383,1385,3,
	24,12,0,1384,1382,1,0,0,0,1384,1383,1,0,0,0,1385,39,1,0,0,0,1386,1390,5,
	96,0,0,1387,1388,7,12,0,0,1388,1389,5,110,0,0,1389,1391,5,13,0,0,1390,1387,
	1,0,0,0,1390,1391,1,0,0,0,1391,1392,1,0,0,0,1392,1393,3,6,3,0,1393,1394,
	5,62,0,0,1394,1396,5,124,0,0,1395,1397,3,42,21,0,1396,1395,1,0,0,0,1397,
	1398,1,0,0,0,1398,1396,1,0,0,0,1398,1399,1,0,0,0,1399,1400,1,0,0,0,1400,
	1401,5,125,0,0,1401,41,1,0,0,0,1402,1403,3,34,17,0,1403,1407,5,62,0,0,1404,
	1408,3,12,6,0,1405,1408,3,24,12,0,1406,1408,3,4,2,0,1407,1404,1,0,0,0,1407,
	1405,1,0,0,0,1407,1406,1,0,0,0,1408,43,1,0,0,0,1409,1410,5,100,0,0,1410,
	1411,5,8,0,0,1411,1414,5,110,0,0,1412,1413,5,110,0,0,1413,1415,5,117,0,
	0,1414,1412,1,0,0,0,1414,1415,1,0,0,0,1415,1416,1,0,0,0,1416,1417,5,24,
	0,0,1417,1419,5,2,0,0,1418,1420,3,22,11,0,1419,1418,1,0,0,0,1419,1420,1,
	0,0,0,1420,1425,1,0,0,0,1421,1422,5,3,0,0,1422,1424,3,22,11,0,1423,1421,
	1,0,0,0,1424,1427,1,0,0,0,1425,1423,1,0,0,0,1425,1426,1,0,0,0,1426,1428,
	1,0,0,0,1427,1425,1,0,0,0,1428,1431,5,4,0,0,1429,1430,7,11,0,0,1430,1432,
	3,18,9,0,1431,1429,1,0,0,0,1431,1432,1,0,0,0,1432,1435,1,0,0,0,1433,1434,
	5,74,0,0,1434,1436,3,18,9,0,1435,1433,1,0,0,0,1435,1436,1,0,0,0,1436,1437,
	1,0,0,0,1437,1502,5,108,0,0,1438,1439,5,8,0,0,1439,1440,5,110,0,0,1440,
	1442,5,2,0,0,1441,1443,3,22,11,0,1442,1441,1,0,0,0,1442,1443,1,0,0,0,1443,
	1448,1,0,0,0,1444,1445,5,3,0,0,1445,1447,3,22,11,0,1446,1444,1,0,0,0,1447,
	1450,1,0,0,0,1448,1446,1,0,0,0,1448,1449,1,0,0,0,1449,1451,1,0,0,0,1450,
	1448,1,0,0,0,1451,1454,5,4,0,0,1452,1453,5,65,0,0,1453,1455,3,18,9,0,1454,
	1452,1,0,0,0,1454,1455,1,0,0,0,1455,1458,1,0,0,0,1456,1457,5,74,0,0,1457,
	1459,3,18,9,0,1458,1456,1,0,0,0,1458,1459,1,0,0,0,1459,1463,1,0,0,0,1460,
	1462,3,28,14,0,1461,1460,1,0,0,0,1462,1465,1,0,0,0,1463,1461,1,0,0,0,1463,
	1464,1,0,0,0,1464,1466,1,0,0,0,1465,1463,1,0,0,0,1466,1467,5,62,0,0,1467,
	1502,3,4,2,0,1468,1470,5,101,0,0,1469,1468,1,0,0,0,1469,1470,1,0,0,0,1470,
	1471,1,0,0,0,1471,1472,5,8,0,0,1472,1473,5,110,0,0,1473,1475,5,2,0,0,1474,
	1476,3,22,11,0,1475,1474,1,0,0,0,1475,1476,1,0,0,0,1476,1481,1,0,0,0,1477,
	1478,5,3,0,0,1478,1480,3,22,11,0,1479,1477,1,0,0,0,1480,1483,1,0,0,0,1481,
	1479,1,0,0,0,1481,1482,1,0,0,0,1482,1484,1,0,0,0,1483,1481,1,0,0,0,1484,
	1487,5,4,0,0,1485,1486,5,65,0,0,1486,1488,3,18,9,0,1487,1485,1,0,0,0,1487,
	1488,1,0,0,0,1488,1491,1,0,0,0,1489,1490,5,74,0,0,1490,1492,3,18,9,0,1491,
	1489,1,0,0,0,1491,1492,1,0,0,0,1492,1496,1,0,0,0,1493,1495,3,28,14,0,1494,
	1493,1,0,0,0,1495,1498,1,0,0,0,1496,1494,1,0,0,0,1496,1497,1,0,0,0,1497,
	1499,1,0,0,0,1498,1496,1,0,0,0,1499,1500,5,13,0,0,1500,1502,3,12,6,0,1501,
	1409,1,0,0,0,1501,1438,1,0,0,0,1501,1469,1,0,0,0,1502,45,1,0,0,0,1503,1504,
	5,77,0,0,1504,1505,5,110,0,0,1505,1506,5,13,0,0,1506,1507,3,6,3,0,1507,
	1508,5,108,0,0,1508,1761,1,0,0,0,1509,1510,5,78,0,0,1510,1511,5,110,0,0,
	1511,1512,5,13,0,0,1512,1513,3,6,3,0,1513,1514,5,108,0,0,1514,1761,1,0,
	0,0,1515,1516,5,77,0,0,1516,1519,5,110,0,0,1517,1518,5,65,0,0,1518,1520,
	3,18,9,0,1519,1517,1,0,0,0,1519,1520,1,0,0,0,1520,1521,1,0,0,0,1521,1522,
	5,13,0,0,1522,1523,3,6,3,0,1523,1524,5,108,0,0,1524,1761,1,0,0,0,1525,1526,
	5,78,0,0,1526,1529,5,110,0,0,1527,1528,5,65,0,0,1528,1530,3,18,9,0,1529,
	1527,1,0,0,0,1529,1530,1,0,0,0,1530,1531,1,0,0,0,1531,1532,5,13,0,0,1532,
	1533,3,6,3,0,1533,1534,5,108,0,0,1534,1761,1,0,0,0,1535,1536,5,79,0,0,1536,
	1539,5,110,0,0,1537,1538,5,65,0,0,1538,1540,3,18,9,0,1539,1537,1,0,0,0,
	1539,1540,1,0,0,0,1540,1541,1,0,0,0,1541,1542,5,13,0,0,1542,1543,3,6,3,
	0,1543,1544,5,108,0,0,1544,1761,1,0,0,0,1545,1546,5,100,0,0,1546,1547,5,
	77,0,0,1547,1550,5,110,0,0,1548,1549,5,110,0,0,1549,1551,5,117,0,0,1550,
	1548,1,0,0,0,1550,1551,1,0,0,0,1551,1552,1,0,0,0,1552,1553,5,24,0,0,1553,
	1554,3,18,9,0,1554,1555,5,108,0,0,1555,1761,1,0,0,0,1556,1557,5,100,0,0,
	1557,1558,7,15,0,0,1558,1561,5,110,0,0,1559,1560,5,110,0,0,1560,1562,5,
	117,0,0,1561,1559,1,0,0,0,1561,1562,1,0,0,0,1562,1563,1,0,0,0,1563,1564,
	5,24,0,0,1564,1565,3,18,9,0,1565,1566,5,108,0,0,1566,1761,1,0,0,0,1567,
	1568,5,100,0,0,1568,1569,5,102,0,0,1569,1570,5,110,0,0,1570,1571,5,24,0,
	0,1571,1572,3,18,9,0,1572,1573,5,108,0,0,1573,1761,1,0,0,0,1574,1575,5,
	102,0,0,1575,1576,5,110,0,0,1576,1578,5,13,0,0,1577,1579,5,110,0,0,1578,
	1577,1,0,0,0,1578,1579,1,0,0,0,1579,1580,1,0,0,0,1580,1581,3,18,9,0,1581,
	1582,5,108,0,0,1582,1761,1,0,0,0,1583,1584,5,100,0,0,1584,1585,5,103,0,
	0,1585,1586,5,110,0,0,1586,1587,5,24,0,0,1587,1592,5,124,0,0,1588,1589,
	5,111,0,0,1589,1590,5,13,0,0,1590,1591,5,110,0,0,1591,1593,5,108,0,0,1592,
	1588,1,0,0,0,1593,1594,1,0,0,0,1594,1592,1,0,0,0,1594,1595,1,0,0,0,1595,
	1596,1,0,0,0,1596,1761,5,125,0,0,1597,1599,3,56,28,0,1598,1597,1,0,0,0,
	1599,1602,1,0,0,0,1600,1598,1,0,0,0,1600,1601,1,0,0,0,1601,1603,1,0,0,0,
	1602,1600,1,0,0,0,1603,1604,5,104,0,0,1604,1616,5,110,0,0,1605,1606,5,10,
	0,0,1606,1611,3,58,29,0,1607,1608,5,3,0,0,1608,1610,3,58,29,0,1609,1607,
	1,0,0,0,1610,1613,1,0,0,0,1611,1609,1,0,0,0,1611,1612,1,0,0,0,1612,1614,
	1,0,0,0,1613,1611,1,0,0,0,1614,1615,5,11,0,0,1615,1617,1,0,0,0,1616,1605,
	1,0,0,0,1616,1617,1,0,0,0,1617,1624,1,0,0,0,1618,1621,5,105,0,0,1619,1620,
	5,110,0,0,1620,1622,5,9,0,0,1621,1619,1,0,0,0,1621,1622,1,0,0,0,1622,1623,
	1,0,0,0,1623,1625,5,110,0,0,1624,1618,1,0,0,0,1624,1625,1,0,0,0,1625,1626,
	1,0,0,0,1626,1627,5,24,0,0,1627,1629,5,124,0,0,1628,1630,3,50,25,0,1629,
	1628,1,0,0,0,1630,1631,1,0,0,0,1631,1629,1,0,0,0,1631,1632,1,0,0,0,1632,
	1633,1,0,0,0,1633,1634,5,125,0,0,1634,1761,1,0,0,0,1635,1637,3,56,28,0,
	1636,1635,1,0,0,0,1637,1640,1,0,0,0,1638,1636,1,0,0,0,1638,1639,1,0,0,0,
	1639,1641,1,0,0,0,1640,1638,1,0,0,0,1641,1642,5,104,0,0,1642,1643,5,110,
	0,0,1643,1646,5,105,0,0,1644,1645,5,110,0,0,1645,1647,5,9,0,0,1646,1644,
	1,0,0,0,1646,1647,1,0,0,0,1647,1648,1,0,0,0,1648,1649,5,110,0,0,1649,1761,
	5,108,0,0,1650,1652,3,56,28,0,1651,1650,1,0,0,0,1652,1655,1,0,0,0,1653,
	1651,1,0,0,0,1653,1654,1,0,0,0,1654,1656,1,0,0,0,1655,1653,1,0,0,0,1656,
	1657,5,110,0,0,1657,1658,5,110,0,0,1658,1659,5,24,0,0,1659,1661,5,124,0,
	0,1660,1662,3,50,25,0,1661,1660,1,0,0,0,1662,1663,1,0,0,0,1663,1661,1,0,
	0,0,1663,1664,1,0,0,0,1664,1665,1,0,0,0,1665,1666,5,125,0,0,1666,1761,1,
	0,0,0,1667,1669,5,100,0,0,1668,1670,5,66,0,0,1669,1668,1,0,0,0,1669,1670,
	1,0,0,0,1670,1671,1,0,0,0,1671,1672,5,104,0,0,1672,1675,5,110,0,0,1673,
	1674,5,110,0,0,1674,1676,5,117,0,0,1675,1673,1,0,0,0,1675,1676,1,0,0,0,
	1676,1677,1,0,0,0,1677,1678,5,24,0,0,1678,1686,5,124,0,0,1679,1680,5,110,
	0,0,1680,1681,3,54,27,0,1681,1682,5,65,0,0,1682,1683,3,18,9,0,1683,1684,
	3,52,26,0,1684,1685,5,108,0,0,1685,1687,1,0,0,0,1686,1679,1,0,0,0,1687,
	1688,1,0,0,0,1688,1686,1,0,0,0,1688,1689,1,0,0,0,1689,1690,1,0,0,0,1690,
	1691,5,125,0,0,1691,1761,1,0,0,0,1692,1694,5,100,0,0,1693,1695,5,66,0,0,
	1694,1693,1,0,0,0,1694,1695,1,0,0,0,1695,1696,1,0,0,0,1696,1697,5,104,0,
	0,1697,1700,5,110,0,0,1698,1699,5,110,0,0,1699,1701,5,117,0,0,1700,1698,
	1,0,0,0,1700,1701,1,0,0,0,1701,1702,1,0,0,0,1702,1761,5,108,0,0,1703,1705,
	5,100,0,0,1704,1706,5,66,0,0,1705,1704,1,0,0,0,1705,1706,1,0,0,0,1706,1707,
	1,0,0,0,1707,1709,5,110,0,0,1708,1710,5,104,0,0,1709,1708,1,0,0,0,1709,
	1710,1,0,0,0,1710,1711,1,0,0,0,1711,1714,5,110,0,0,1712,1713,5,110,0,0,
	1713,1715,5,117,0,0,1714,1712,1,0,0,0,1714,1715,1,0,0,0,1715,1716,1,0,0,
	0,1716,1717,5,24,0,0,1717,1725,5,124,0,0,1718,1719,5,110,0,0,1719,1720,
	3,54,27,0,1720,1721,5,65,0,0,1721,1722,3,18,9,0,1722,1723,3,52,26,0,1723,
	1724,5,108,0,0,1724,1726,1,0,0,0,1725,1718,1,0,0,0,1726,1727,1,0,0,0,1727,
	1725,1,0,0,0,1727,1728,1,0,0,0,1728,1729,1,0,0,0,1729,1730,5,125,0,0,1730,
	1761,1,0,0,0,1731,1733,5,100,0,0,1732,1734,5,66,0,0,1733,1732,1,0,0,0,1733,
	1734,1,0,0,0,1734,1735,1,0,0,0,1735,1737,5,110,0,0,1736,1738,5,104,0,0,
	1737,1736,1,0,0,0,1737,1738,1,0,0,0,1738,1739,1,0,0,0,1739,1742,5,110,0,
	0,1740,1741,5,110,0,0,1741,1743,5,117,0,0,1742,1740,1,0,0,0,1742,1743,1,
	0,0,0,1743,1744,1,0,0,0,1744,1761,5,108,0,0,1745,1746,5,76,0,0,1746,1747,
	5,110,0,0,1747,1748,5,24,0,0,1748,1750,5,124,0,0,1749,1751,3,48,24,0,1750,
	1749,1,0,0,0,1751,1752,1,0,0,0,1752,1750,1,0,0,0,1752,1753,1,0,0,0,1753,
	1754,1,0,0,0,1754,1755,5,125,0,0,1755,1761,1,0,0,0,1756,1757,5,110,0,0,
	1757,1758,5,117,0,0,1758,1761,5,108,0,0,1759,1761,3,26,13,0,1760,1503,1,
	0,0,0,1760,1509,1,0,0,0,1760,1515,1,0,0,0,1760,1525,1,0,0,0,1760,1535,1,
	0,0,0,1760,1545,1,0,0,0,1760,1556,1,0,0,0,1760,1567,1,0,0,0,1760,1574,1,
	0,0,0,1760,1583,1,0,0,0,1760,1600,1,0,0,0,1760,1638,1,0,0,0,1760,1653,1,
	0,0,0,1760,1667,1,0,0,0,1760,1692,1,0,0,0,1760,1703,1,0,0,0,1760,1731,1,
	0,0,0,1760,1745,1,0,0,0,1760,1756,1,0,0,0,1760,1759,1,0,0,0,1761,47,1,0,
	0,0,1762,1763,5,110,0,0,1763,1764,5,65,0,0,1764,1765,3,18,9,0,1765,1766,
	5,108,0,0,1766,1791,1,0,0,0,1767,1768,5,8,0,0,1768,1769,5,110,0,0,1769,
	1778,5,2,0,0,1770,1775,3,18,9,0,1771,1772,5,3,0,0,1772,1774,3,18,9,0,1773,
	1771,1,0,0,0,1774,1777,1,0,0,0,1775,1773,1,0,0,0,1775,1776,1,0,0,0,1776,
	1779,1,0,0,0,1777,1775,1,0,0,0,1778,1770,1,0,0,0,1778,1779,1,0,0,0,1779,
	1780,1,0,0,0,1780,1783,5,4,0,0,1781,1782,5,65,0,0,1782,1784,3,18,9,0,1783,
	1781,1,0,0,0,1783,1784,1,0,0,0,1784,1787,1,0,0,0,1785,1786,5,74,0,0,1786,
	1788,3,18,9,0,1787,1785,1,0,0,0,1787,1788,1,0,0,0,1788,1789,1,0,0,0,1789,
	1791,5,108,0,0,1790,1762,1,0,0,0,1790,1767,1,0,0,0,1791,49,1,0,0,0,1792,
	1793,5,110,0,0,1793,1794,5,65,0,0,1794,1795,3,18,9,0,1795,1798,3,52,26,
	0,1796,1797,5,13,0,0,1797,1799,3,6,3,0,1798,1796,1,0,0,0,1798,1799,1,0,
	0,0,1799,1800,1,0,0,0,1800,1801,5,108,0,0,1801,51,1,0,0,0,1802,1803,5,65,
	0,0,1803,1806,3,6,3,0,1804,1806,5,111,0,0,1805,1802,1,0,0,0,1805,1804,1,
	0,0,0,1805,1806,1,0,0,0,1806,53,1,0,0,0,1807,1808,5,110,0,0,1808,1810,5,
	117,0,0,1809,1807,1,0,0,0,1809,1810,1,0,0,0,1810,55,1,0,0,0,1811,1816,5,
	110,0,0,1812,1813,5,2,0,0,1813,1814,3,6,3,0,1814,1815,5,4,0,0,1815,1817,
	1,0,0,0,1816,1812,1,0,0,0,1816,1817,1,0,0,0,1817,1820,1,0,0,0,1818,1820,
	5,66,0,0,1819,1811,1,0,0,0,1819,1818,1,0,0,0,1820,57,1,0,0,0,1821,1823,
	5,66,0,0,1822,1821,1,0,0,0,1822,1823,1,0,0,0,1823,1824,1,0,0,0,1824,1825,
	5,110,0,0,1825,59,1,0,0,0,1826,1827,5,106,0,0,1827,1828,5,117,0,0,1828,
	1829,5,108,0,0,1829,61,1,0,0,0,1830,1834,3,6,3,0,1831,1833,5,108,0,0,1832,
	1831,1,0,0,0,1833,1836,1,0,0,0,1834,1832,1,0,0,0,1834,1835,1,0,0,0,1835,
	1837,1,0,0,0,1836,1834,1,0,0,0,1837,1838,5,0,0,1,1838,63,1,0,0,0,1839,1842,
	5,110,0,0,1840,1841,5,110,0,0,1841,1843,5,117,0,0,1842,1840,1,0,0,0,1842,
	1843,1,0,0,0,1843,1844,1,0,0,0,1844,1845,3,44,22,0,1845,65,1,0,0,0,1846,
	1847,5,107,0,0,1847,1851,3,44,22,0,1848,1849,5,107,0,0,1849,1851,3,46,23,
	0,1850,1846,1,0,0,0,1850,1848,1,0,0,0,1851,67,1,0,0,0,1852,1859,3,44,22,
	0,1853,1859,3,46,23,0,1854,1859,3,60,30,0,1855,1859,3,66,33,0,1856,1859,
	3,64,32,0,1857,1859,5,108,0,0,1858,1852,1,0,0,0,1858,1853,1,0,0,0,1858,
	1854,1,0,0,0,1858,1855,1,0,0,0,1858,1856,1,0,0,0,1858,1857,1,0,0,0,1859,
	1862,1,0,0,0,1860,1858,1,0,0,0,1860,1861,1,0,0,0,1861,1863,1,0,0,0,1862,
	1860,1,0,0,0,1863,1864,5,0,0,1,1864,69,1,0,0,0,201,74,81,87,99,112,137,
	150,164,173,182,187,189,201,208,217,222,224,238,247,256,261,267,276,281,
	283,291,298,307,312,314,324,333,342,347,349,360,367,376,381,383,396,410,
	423,454,481,484,489,493,508,558,574,595,611,640,675,677,682,685,691,697,
	701,703,710,719,724,729,738,756,767,773,779,784,790,799,805,810,823,827,
	848,850,864,893,941,952,956,961,965,971,977,984,992,999,1003,1017,1040,
	1048,1061,1066,1074,1079,1090,1093,1097,1108,1111,1115,1119,1123,1129,1135,
	1138,1162,1177,1201,1218,1242,1259,1278,1284,1293,1304,1313,1317,1323,1330,
	1332,1340,1346,1352,1363,1368,1370,1378,1384,1390,1398,1407,1414,1419,1425,
	1431,1435,1442,1448,1454,1458,1463,1469,1475,1481,1487,1491,1496,1501,1519,
	1529,1539,1550,1561,1578,1594,1600,1611,1616,1621,1624,1631,1638,1646,1653,
	1663,1669,1675,1688,1694,1700,1705,1709,1714,1727,1733,1737,1742,1752,1760,
	1775,1778,1783,1787,1790,1798,1805,1809,1816,1819,1822,1834,1842,1850,1858,
	1860];

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
export class ExprUnsafeContext extends ExprContext {
	constructor(parser: EclangParser, ctx: ExprContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
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
	public _mut!: Token;
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
	public tryCatch(): TryCatchContext {
		return this.getTypedRuleContext(TryCatchContext, 0) as TryCatchContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_tail;
	}
}


export class TryCatchContext extends ParserRuleContext {
	public _left!: ExprContext;
	public _value!: TailContext;
	public _body!: BlockContext;
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
	public tail(): TailContext {
		return this.getTypedRuleContext(TailContext, 0) as TailContext;
	}
	public block(): BlockContext {
		return this.getTypedRuleContext(BlockContext, 0) as BlockContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_tryCatch;
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
	public _mut!: Token;
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
export class ParameterKeywordContext extends ParameterContext {
	public _keyword!: Token;
	constructor(parser: EclangParser, ctx: ParameterContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public parameter(): ParameterContext {
		return this.getTypedRuleContext(ParameterContext, 0) as ParameterContext;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
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
export class ParameterMutContext extends ParameterContext {
	constructor(parser: EclangParser, ctx: ParameterContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public parameter(): ParameterContext {
		return this.getTypedRuleContext(ParameterContext, 0) as ParameterContext;
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
export class StatementNullishContext extends StatementContext {
	public _left!: ExprContext;
	public _right!: ExprContext;
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
	public jump(): JumpContext {
		return this.getTypedRuleContext(JumpContext, 0) as JumpContext;
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
export class StatementDestroyContext extends StatementContext {
	public _target!: ExprContext;
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
export class StatementForInContext extends StatementContext {
	public _index!: Token;
	public _name!: Token;
	public _parts!: DestructureContext;
	public _iterable!: ExprContext;
	public _where!: Token;
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
export class StatementUnsafeContext extends StatementContext {
	public _body!: BlockContext;
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public block(): BlockContext {
		return this.getTypedRuleContext(BlockContext, 0) as BlockContext;
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
	public _message!: ExprContext;
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
export class StatementConstAssertContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public constAssert(): ConstAssertContext {
		return this.getTypedRuleContext(ConstAssertContext, 0) as ConstAssertContext;
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
export class StatementPrefixedContext extends StatementContext {
	public _keyword!: Token;
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
export class StatementUnsafeLineContext extends StatementContext {
	constructor(parser: EclangParser, ctx: StatementContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public statement(): StatementContext {
		return this.getTypedRuleContext(StatementContext, 0) as StatementContext;
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
	public _condition!: ExprContext;
	public _body!: BlockContext;
	public _elseBody!: BlockContext;
	public _next!: StatementContext;
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


export class ConstAssertContext extends ParserRuleContext {
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_constAssert;
	}
	public copyFrom(ctx: ConstAssertContext): void {
		super.copyFrom(ctx);
	}
}
export class ConstAssertConditionContext extends ConstAssertContext {
	public _condition!: ExprContext;
	public _message!: Token;
	constructor(parser: EclangParser, ctx: ConstAssertContext) {
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
export class ConstAssertRelationContext extends ConstAssertContext {
	public _left!: TypeContext;
	public _relation!: Token;
	public _right!: TypeContext;
	public _message!: Token;
	constructor(parser: EclangParser, ctx: ConstAssertContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public NL(): TerminalNode {
		return this.getToken(EclangParser.NL, 0);
	}
	public type__list(): TypeContext[] {
		return this.getTypedRuleContexts(TypeContext) as TypeContext[];
	}
	public type_(i: number): TypeContext {
		return this.getTypedRuleContext(TypeContext, i) as TypeContext;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
}
export class ConstAssertInContext extends ConstAssertContext {
	public _value!: ExprContext;
	public _pool!: ExprContext;
	public _message!: Token;
	constructor(parser: EclangParser, ctx: ConstAssertContext) {
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
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
}


export class ContractContext extends ParserRuleContext {
	public _kind!: Token;
	public _condition!: ExprContext;
	public _message!: ExprContext;
	public _debug!: Token;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
	}
	public expr_list(): ExprContext[] {
		return this.getTypedRuleContexts(ExprContext) as ExprContext[];
	}
	public expr(i: number): ExprContext {
		return this.getTypedRuleContext(ExprContext, i) as ExprContext;
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_contract;
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
	public _contract!: ContractContext;
	public _contracts: ContractContext[] = [];
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
	public contract_list(): ContractContext[] {
		return this.getTypedRuleContexts(ContractContext) as ContractContext[];
	}
	public contract(i: number): ContractContext {
		return this.getTypedRuleContext(ContractContext, i) as ContractContext;
	}
}
export class FunctionBlockContext extends FuncContext {
	public _name!: Token;
	public _parameter!: ParameterContext;
	public _params: ParameterContext[] = [];
	public _returnType!: TypeContext;
	public _throwing!: TypeContext;
	public _contract!: ContractContext;
	public _contracts: ContractContext[] = [];
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
	public contract_list(): ContractContext[] {
		return this.getTypedRuleContexts(ContractContext) as ContractContext[];
	}
	public contract(i: number): ContractContext {
		return this.getTypedRuleContext(ContractContext, i) as ContractContext;
	}
}
export class FunctionDeclareContext extends FuncContext {
	public _name!: Token;
	public _ext!: Token;
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
	public NAME_list(): TerminalNode[] {
	    	return this.getTokens(EclangParser.NAME);
	}
	public NAME(i: number): TerminalNode {
		return this.getToken(EclangParser.NAME, i);
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
	public _mut!: Token;
	public _keyword!: Token;
	public _typedef!: Token;
	public _name!: Token;
	public _ext!: Token;
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
	public _mut!: Token;
	public _name!: Token;
	public _ext!: Token;
	public _externalName!: Token;
	public _NAME!: Token;
	public _names: Token[] = [];
	public _fieldExtern!: FieldExternContext;
	public _externs: FieldExternContext[] = [];
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
	public fieldExtern_list(): FieldExternContext[] {
		return this.getTypedRuleContexts(FieldExternContext) as FieldExternContext[];
	}
	public fieldExtern(i: number): FieldExternContext {
		return this.getTypedRuleContext(FieldExternContext, i) as FieldExternContext;
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
	public _mut!: Token;
	public _keyword!: Token;
	public _typedef!: Token;
	public _name!: Token;
	public _ext!: Token;
	public _externalName!: Token;
	public _NAME!: Token;
	public _names: Token[] = [];
	public _fieldExtern!: FieldExternContext;
	public _externs: FieldExternContext[] = [];
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
	public fieldExtern_list(): FieldExternContext[] {
		return this.getTypedRuleContexts(FieldExternContext) as FieldExternContext[];
	}
	public fieldExtern(i: number): FieldExternContext {
		return this.getTypedRuleContext(FieldExternContext, i) as FieldExternContext;
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
export class GlobalConstAssertContext extends GlobalContext {
	constructor(parser: EclangParser, ctx: GlobalContext) {
		super(parser, ctx.parentCtx, ctx.invokingState);
		super.copyFrom(ctx);
	}
	public constAssert(): ConstAssertContext {
		return this.getTypedRuleContext(ConstAssertContext, 0) as ConstAssertContext;
	}
}
export class GlobalDeclareDefineContext extends GlobalContext {
	public _name!: Token;
	public _ext!: Token;
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
	public _mut!: Token;
	public _name!: Token;
	public _ext!: Token;
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
	public _ext!: Token;
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
export class GlobalRecordDefineContext extends GlobalContext {
	public _recordModifier!: RecordModifierContext;
	public _modifiers: RecordModifierContext[] = [];
	public _name!: Token;
	public _typeParameter!: TypeParameterContext;
	public _typeParams: TypeParameterContext[] = [];
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
	public typeParameter_list(): TypeParameterContext[] {
		return this.getTypedRuleContexts(TypeParameterContext) as TypeParameterContext[];
	}
	public typeParameter(i: number): TypeParameterContext {
		return this.getTypedRuleContext(TypeParameterContext, i) as TypeParameterContext;
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
	public _keyword!: Token;
	public _name!: Token;
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


export class FieldExternContext extends ParserRuleContext {
	public _ext!: Token;
	public _cName!: Token;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
	public STRING(): TerminalNode {
		return this.getToken(EclangParser.STRING, 0);
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_fieldExtern;
	}
}


export class RecordModifierContext extends ParserRuleContext {
	public _name!: Token;
	public _size!: ExprContext;
	public _mut!: Token;
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


export class TypeParameterContext extends ParserRuleContext {
	public _mut!: Token;
	public _name!: Token;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public NAME(): TerminalNode {
		return this.getToken(EclangParser.NAME, 0);
	}
    public get ruleIndex(): number {
    	return EclangParser.RULE_typeParameter;
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
	public _ext!: Token;
	public _externalName!: Token;
	constructor(parser?: EclangParser, parent?: ParserRuleContext, invokingState?: number) {
		super(parent, invokingState);
    	this.parser = parser;
	}
	public func(): FuncContext {
		return this.getTypedRuleContext(FuncContext, 0) as FuncContext;
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
