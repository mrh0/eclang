grammar Eclang;

tokens { INDENT, DEDENT }

@lexer::header {
  import com.yuvalshavit.antlr4.DenterHelper;
}

@lexer::members {
  private final DenterHelper denter = new DenterHelper(NL,
                                                       EclangParser.INDENT,
                                                       EclangParser.DEDENT)
  {
    @Override
    public Token pullToken() {
      Token t = EclangLexer.super.nextToken();
      while (t.getType() == ECDOC) {
        if (pendingDoc != null) strayEcdocs.add(pendingDoc);
        pendingDoc = t;
        t = EclangLexer.super.nextToken();
      }
      if (pendingDoc != null && t.getType() != NL) {
        if (t.getType() == EOF) strayEcdocs.add(pendingDoc);
        else ecdocs.put(t.getStartIndex(), pendingDoc);
        pendingDoc = null;
      }
      return t;
    }
  };

  // Ecdocs never reach the parser (or the indentation tracking, so they can go anywhere). Each one is kept
  // here, by the start offset of the token after it; doc/DocExtractor attaches them to declarations.
  public final java.util.Map<Integer, Token> ecdocs = new java.util.HashMap<>();
  // Ecdocs followed by another ecdoc or by the end of the file: they document nothing.
  public final java.util.List<Token> strayEcdocs = new java.util.ArrayList<>();
  private Token pendingDoc;

  @Override
  public Token nextToken() {
    return denter.nextToken();
  }
}

NL: ('\r'? '\n' ' '*);

BOOL: 'true' | 'false';
NAME: [_a-zA-Z][_a-zA-Z0-9]*;
ATOM: ':'[a-zA-Z0-9][_a-zA-Z0-9]*;

FLOAT: [0-9][_0-9]* '.' [0-9][_0-9]* ([eE] [+-]? [0-9]+)?;
INT: '0'|[1-9][_0-9]*;
HEX: '0x'[0-9a-fA-F]*;
BIN: '0b'[0-1]*;

CHAR: '\''.'\'' | '\'\\'('n'|'r'|'t'|'\\'|'\''|'"'|'0')'\'';

STRING: '"' ('\\' ~[\r\n] | ~["\\\r\n])* ('"c' | '"');
EMBEDED: [_a-zA-Z][_a-zA-Z0-9]*'`'.*?'`';

WHITESPACE: [ \t]+ -> skip;
EMPTYLINE: NL -> skip;
COMMENT: '//' ~[\r\n]* -> skip;
// `/** ... */` documents the declaration after it (see the lexer members above). Before BLOCKCOMMENT, which matches it too.
ECDOC: '/**' .*? '*/' -> channel(HIDDEN);
BLOCKCOMMENT: '/*' .*? '*/' -> skip;

number:
      INT           #numberInteger
    | FLOAT         #numberFloating
    | HEX           #numberHex
    | BIN           #numberBin
    ;

primitive:
      number        #primitiveNumber
    | BOOL          #primitiveBool
    | STRING        #primitiveString
    | CHAR          #primitiveChar
    | ATOM          #primitiveAtom
    ;

block:
      INDENT (statements+=statement)+ DEDENT
    ;

// Expressions. In a left-recursive ANTLR rule, alternatives listed first bind tightest: primaries, then postfix
// operators (`.x`, `[i]`, calls, `!`, `as`), then `@`, `addrof`, `try` and `new`, then `**`, then the other prefix operators
// (so `-2 ** 2` is `-(2 ** 2)`, as in maths), then binary operators from `*` down to `??`, and last the inline `if`,
// `|>` and lambdas, whose last part reaches as far right as it can.
expr:
      'here'                                                        #exprHere
    | 'null'                                                        #exprNull
    | 'it'                                                          #exprIt // For shorthand functions, switch and pipe op
    | '(' expr (',' expr)* ')'                                      #exprNest // A tuple with more than one
    | '(' INDENT expr (',' NL expr)* NL DEDENT ')'                  #exprNest
    | primitive NAME                                                #exprPrimitiveUnit
    | primitive                                                     #exprPrimitive
    | 'sizeof' type                                                 #exprSizeOf
    | 'alignof' type                                                #exprAlignOf
    | 'offsetof' '(' type ',' field=NAME ')'                        #exprOffsetOf
    | 'fn' (qualifier=NAME '.')? name=NAME                          #exprFunctionReference

    | NAME '(' ')'                                                  #exprFunctionCallNoArgs
    | NAME '(' rest+=argument (',' rest+=argument)* ')'                     #exprFunctionCallWithArgs
    | NAME '(' INDENT rest+=argument (',' NL rest+=argument)* NL DEDENT ')' #exprFunctionCallWithArgs

    | ((qualifier=NAME '.')? recordType=NAME ('<' typeArgs+=type (',' typeArgs+=type)* '>')?)? '{' names+=NAME '=' expr (',' names+=NAME '=' expr)* '}'                         #exprCreateRecordNamed
    | ((qualifier=NAME '.')? recordType=NAME ('<' typeArgs+=type (',' typeArgs+=type)* '>')?)? '{' INDENT names+=NAME '=' expr (',' NL names+=NAME '=' expr)* NL DEDENT '}'     #exprCreateRecordNamed
    // `Options {}`: every field has its default. It needs the type: nothing else says what the fields are.
    | (qualifier=NAME '.')? recordType=NAME ('<' typeArgs+=type (',' typeArgs+=type)* '>')? '{' '}'                                                                             #exprCreateRecordNamed
    | ((qualifier=NAME '.')? recordType=NAME ('<' typeArgs+=type (',' typeArgs+=type)* '>')?)? '{' expr (',' expr)* '}'                                                         #exprCreateRecord
    | ((qualifier=NAME '.')? recordType=NAME ('<' typeArgs+=type (',' typeArgs+=type)* '>')?)? '{' INDENT expr (',' NL expr)* NL DEDENT '}'                                     #exprCreateRecord

    | ((qualifier=NAME '.')? recordType=NAME ('<' typeArgs+=type (',' typeArgs+=type)* '>')?)? '{' '...' base=expr (',' names+=NAME '=' values+=expr)* '}'                       #exprUpdateRecord
    | ((qualifier=NAME '.')? recordType=NAME ('<' typeArgs+=type (',' typeArgs+=type)* '>')?)? '{' INDENT '...' base=expr (',' NL names+=NAME '=' values+=expr)* NL DEDENT '}'   #exprUpdateRecord

    // After the record literals: `Box<Int> { 1 }` isn't the comparison `Box < Int > { 1 }`.
    | NAME                                                          #exprNamed

    | '[' expr (',' expr)* (']c' | ']')                                                                 #exprCreateArray
    | '[' INDENT expr (',' NL expr)* NL DEDENT (']c' | ']')                                             #exprCreateArray

    // Postfix
    | args+=expr '.' NAME '(' ')'                                   #exprFunctionCallNoArgs
    | args+=expr '.' NAME '(' rest+=argument (',' rest+=argument)* ')'      #exprFunctionCallWithArgs
    | args+=expr '.' NAME '(' INDENT rest+=argument (',' NL rest+=argument)* NL DEDENT ')'                      #exprFunctionCallWithArgs
    | args+=expr '?.' NAME '(' ')'                                  #exprSafeCall
    | args+=expr '?.' NAME '(' rest+=argument (',' rest+=argument)* ')'     #exprSafeCall
    | args+=expr '?.' NAME '(' INDENT rest+=argument (',' NL rest+=argument)* NL DEDENT ')'             #exprSafeCall
    | accesssed=expr '.' NAME                                       #exprAccessName
    | accesssed=expr '?.' NAME                                      #exprAccessNameNullishCoalescing
    | accesssed=expr '[' index=expr ']'                             #exprAccessor
    | accesssed=expr '[' INDENT index=expr NL DEDENT ']'            #exprAccessor
    | accesssed=expr '[' from=expr? '..' ']'                        #exprSliceOpen // `s[1..]`, `s[..]`
    | accesssed=expr '[' op=('..<' | '..=') to=expr ']'             #exprSliceOpen // `s[..<3]`
    | expr '!'                                                      #exprCastNotNull
    | expr 'as' type                                                #exprAs
    | expr 'as' 'unsafe' type                                       #exprAsUnsafe
    | expr '!as' type                                               #exprAsUnsafe

    // Prefix, binding tighter than `**`
    | op='@' expr                                                   #exprUnOp
    | 'addrof' expr                                                 #exprAddressOf
    | 'try' expr                                                    #exprTry
    | 'new' value=expr 'in' pool=expr                               #exprNew // A copy of the value (or an empty T, or T[n]) in a pool
    | 'new' value=expr                                              #exprNew
    | 'new' '(' elementType=type ')' '[' count=expr ']' ('in' pool=expr)?    #exprNewArrayOfType // `new (@T?)[n]`: any element type

    | <assoc=right> left=expr op='**' right=expr                    #exprBinOp

    // Prefix, binding looser than `**`
    | op=('not' | '!' | '!!' | '~' | '-') expr                      #exprUnOp

    // Binary, tightest first
    | left=expr op=('*' | '/' | '%') right=expr                     #exprBinOp
    | left=expr op=('+' | '-') right=expr                           #exprBinOp
    // `>>` is two `>` tokens, so that `List<List<Int>>` closes two type argument lists; Visitor checks they touch.
    | left=expr (op='<<' | op='>' shr='>') right=expr                #exprBinOp
    | left=expr op='&' right=expr                                   #exprBinOp
    | left=expr op='^' right=expr                                   #exprBinOp
    | left=expr op='|' right=expr                                   #exprBinOp
    | left=expr op=('..<' | '..=' | '..>') right=expr               #exprBinOp
    | left=expr op=('<' | '>' | '<=' | '>=') right=expr             #exprBinOp
    | expr 'is' type                                                #exprIs
    | expr '!is' type                                               #exprIsNot
    | expr 'is' 'not' type                                          #exprIsNot
    | left=expr op=('==' | '!=' | '===' | '!==') right=expr         #exprBinOp
    | left=expr op=('&&' | 'and') right=expr                        #exprBinOp
    | left=expr op=('^^' | 'xor') right=expr                        #exprBinOp
    | left=expr op=('||' | 'or') right=expr                         #exprBinOp
    | <assoc=right> left=expr op='??' right=expr                    #exprBinOp
    | left=expr '??' jump                                           #exprNullishJump // After `??`: `a ?? b ?? ret 0` is `(a ?? b) ?? ret 0`

    | 'if' condition=expr 'do' body=expr 'else' elseBody=expr      #exprInlineIf
    | 'if' '(' condition=expr ')' body=expr 'else' elseBody=expr    #exprInlineIfOld // Rejected with a hint to the form above
    | left=expr '|>' right=expr                                     #exprPipe
    | whenExpr                                                      #exprWhen
    | 'fn' '(' (params+=lambdaParameter (',' params+=lambdaParameter)*)? ')' (':' returnType=type)? '=' body=expr    #exprLambda
    ;

// A call argument, with its parameter's name or in order.
argument:
      (name=NAME '=')? expr
    ;

// What leaves on the right of `??`: `x ?? ret 0`.
jump:
      'ret' value=expr?             #jumpReturn
    | 'throw' value=expr            #jumpThrow
    | 'break' label=NAME?           #jumpBreak
    | 'continue' label=NAME?        #jumpContinue
    ;

// A value that ends a statement. A `when` ends with a DEDENT, after which the denter gives no NL.
tail:
      expr NL
    | whenExpr
    ;

lambdaParameter:
      NAME (':' type)?
    ;

type:
      (qualifier=NAME '.')? name=NAME '<' args+=type (',' args+=type)* '>'      #typeByNameParameters // List<Int>
    | NAME '.' NAME                                                             #typeByName // module.Type
    | NAME                                                                      #typeByName
    | '@' type                                                                  #typeAddressByName
    | type '?'                                                                  #typeNullable
    | types+=type '&' types+=type ('&' types+=type)*                            #typeEnum
    | types+=type '|' types+=type ('|' types+=type)*                            #typeUnion
    | 'anon' types+=type '|' types+=type ('|' types+=type)*                     #typeUnionAnon // Anonymus union
    | '(' type ')'                                                              #typeNest
    | ATOM                                                                      #typeAtom
    | '<' NAME '>'                                                              #typeGeneric
    | 'typeof' expr                                                             #typeTypeOf
    | type '[' (']c' | ']')                                                     #typeArray
    | type '[' size=expr ']'                                                    #typeFixedArray // Int[4]: the elements are part of the value, like a C array in a struct
    | left=type 'throws' throwing=type                                          #typeThrows
    //| ('vol' | 'volatile') type                                                 #typeVolatile
    | 'fn' '(' ')' (('=>' | ':') returnType=type)?                              #typeCallSignature
    | 'fn' '(' (NAME ':')? argTypes+=type (',' (NAME ':')? argTypes+=type)* ')' (('=>' | ':') returnType=type)?    #typeCallSignature
    | '(' ')' '=>' returnType=type                                              #typeCallSignature
    | '(' (NAME ':')? argTypes+=type (',' (NAME ':')? argTypes+=type)* ')' '=>' returnType=type    #typeCallSignature
    // After the function types: `(A, B) => R` isn't the tuple `(A, B)`.
    | '(' types+=type (',' types+=type)+ ')'                                    #typeTuple
    //| 'const' type                                                          #typeConst // When argument must be known at compile time
    ;

interface:
      'inter' name=NAME 'as' INDENT (names+=NAME ':' types+=type NL)+ DEDENT
    ;

parameter:
      NAME ':' type                         #parameterTyped
    | NAME ':' type '=' expr                #parameterTypedDefault
    | NAME '=' expr                         #parameterDefault
    | '...' NAME ':' type                   #parameterVarArg
    | '...'                                 #parameterVarArgC
    | 'var' parameter                       #parameterVariable
    | 'val' parameter                       #parameterConst
    | 'vol' parameter                       #parameterVolatile
    ;

statement:
      'var' NAME '=' tail                                                               #statementDefineVar
    | 'val' NAME '=' tail                                                               #statementDefineVal
    | 'vol' NAME '=' tail                                                               #statementDefineVol
    | 'const' NAME '=' expr NL                                                          #statementDefineConst
    | 'var' NAME ':' type '=' tail                                                      #statementDefineVarTyped
    | 'val' NAME ':' type '=' tail                                                      #statementDefineValTyped
    | 'vol' NAME ':' type '=' tail                                                      #statementDefineVolTyped
    | 'const' NAME ':' type '=' expr NL                                                 #statementDefineConstTyped

    | 'var' NAME ':' type NL                                                            #statementDeclareVar

    | kind=('val' | 'var') destructure (':' type)? '=' tail                             #statementDestructure

    | target=expr op=('=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=' | '<<=' | '>>=') value=tail        #statementAssignment

    | 'break' label=NAME? ('if' condition=expr)? NL                                          #statementBreak
    | 'continue' label=NAME? ('if' condition=expr)? NL                                       #statementContinue
    | 'pass' ('if' condition=expr)? NL                                                       #statementPass
    | 'yield' ('if' condition=expr)? NL                                                      #statementYield
    | 'throw' throw=expr ('if' condition=expr)? NL                                           #statementThrow
    | 'assert' condition=expr (',' message=STRING)? NL                                       #statementAssert
    | 'ret' return=expr? ('if' condition=expr)? NL                                           #statementReturn
    | 'ret' whenExpr                                                                         #statementReturn
    | expression=expr 'if' condition=expr NL                                                 #statementTailIf
    // What to do when a value is missing or a call failed, with no value to keep: `f() ?? panic("x")`, `x ?? ret`.
    | left=expr '??' (jump | right=expr) NL                                                  #statementNullish

    | expression=expr 'while' condition=expr NL                                                 #statementTailWhile
    //| expression=expr 'for' condition=expr NL                                                 #statementTailFor

    | 'defer' statement                                                                 #statementDefer
    | 'defer' 'do' body=block                                                           #statementDeferDo
    | 'errdefer' statement                                                              #statementErrDefer
    | 'errdefer' 'do' body=block                                                        #statementErrDeferDo

    | 'when' (kind=('val' | 'var') NAME '=')? subject=expr 'do' INDENT (arms+=whenArm)+ DEDENT                                       #statementWhen

    | 'if' kind=('val' | 'var') NAME '=' value=expr 'do' body=block ('else' 'do'? elseBody=block | 'else' next=statement)?             #statementLetIf // `next`: `else if ...`, see Visitor

    | 'if' conditions+=expr 'do' bodies+=block ('else' 'if' conditions+=expr 'do' bodies+=block)* ('else' 'do'? elseBody=block | 'else' next=statement)?      	#statementIf // `next`: `else if val ...`
    | label=NAME ':' statement                                                                                                        #statementLabeled // `outer: for ...`
    | 'while' condition=expr 'do' body=block ('else' 'do'? elseBody=block)?                                                           	#statementWhile

    | 'for' ('var' | 'val')? NAME 'in' lower=expr 'to' upper=expr 'do' body=block ('else' 'do'? elseBody=block)?                        #statementForInRange // Rejected with a hint to use a range
    | 'for' ('var' | 'val')? (index=NAME ',')? (name=NAME | parts=destructure) 'in' iterable=expr ('where' filter=expr)? 'do' body=block ('else' 'do'? elseBody=block)?    #statementForIn

    // Before the calls: `try m.f(x)` is a `try` of the call, not a call of `f` on `try m`.
    | 'try' left=expr NL                                                                        #statementTry
    | 'try' left=expr 'catch' NAME 'do' elseBody=block                                          #statementTryCatch

    | NAME '(' ')' NL                                                   #statementFunctionCallNoArgs
    | NAME '(' rest+=argument (',' rest+=argument)* ')' NL                      #statementFunctionCallWithArgs
    | NAME '(' INDENT rest+=argument (',' NL rest+=argument)* NL DEDENT ')' NL  #statementFunctionCallWithArgs

    | args+=expr '.' NAME '(' ')' NL                                    #statementFunctionCallNoArgs
    | args+=expr '.' NAME '(' rest+=argument (',' rest+=argument)* ')' NL       #statementFunctionCallWithArgs
    | args+=expr '.' NAME '(' INDENT rest+=argument (',' NL rest+=argument)* NL DEDENT ')' NL       #statementFunctionCallWithArgs
    | args+=expr '?.' NAME '(' ')' NL                                   #statementSafeCall
    | args+=expr '?.' NAME '(' rest+=argument (',' rest+=argument)* ')' NL      #statementSafeCall
    | args+=expr '?.' NAME '(' INDENT rest+=argument (',' NL rest+=argument)* NL DEDENT ')' NL      #statementSafeCall

    | keyword=NAME (name=NAME)? 'do' body=block                                                 #statementPool // `pool do` / `pool name do`; `pool` stays usable as a name
    // | 'try' INDENT left=expr NL DEDENT 'catch' NAME 'do' elseBody=block                          #statementTryCatch
    ;

// What `val (a, (b, _)) = ...` and `for (a, b) in ...` name: the parts of a tuple. `_` skips a part.
destructure:
      '(' parts+=destructurePart (',' parts+=destructurePart)+ ')'
    ;

destructurePart:
      NAME
    | destructure
    ;

// What a `when` arm matches: `is T`, values and ranges (equal to one of them), or `else`. `if` adds a condition; `n if c` names
// the value being matched.
whenPattern:
      'is' type ('if' guard=expr)?                                      #whenPatternIs
    | 'else'                                                            #whenPatternElse
    | NAME 'if' guard=expr                                              #whenPatternBind
    | values+=whenValue (',' values+=whenValue)* ('if' guard=expr)?     #whenPatternValues
    ;

// `a` or `a..<b` (an ordinary expression), or a range open at one end.
whenValue:
      expr                          #whenValuePlain
    | from=expr '..'                #whenValueFrom
    | op=('..<' | '..=') to=expr    #whenValueUpTo
    ;

// One arm of `when x do`: a pattern, `do`, then a block or one statement.
whenArm:
      whenPattern 'do' (body=block | line=statement)
    ;

// `when` as a value. Each arm gives a value, or leaves (`ret`, `throw`, ...).
whenExpr:
      'when' (kind=('val' | 'var') NAME '=')? subject=expr 'do' INDENT (arms+=whenValueArm)+ DEDENT
    ;

whenValueArm:
      whenPattern 'do' (value=tail | line=statement | body=block)
    ;

func:
      'declare' 'fn' name=NAME ('extern' externalName=STRING)? 'as' '(' params+=parameter? (',' params+=parameter)* ')' ((':' | '=>') returnType=type)? ('throws' throwing=type)? NL       #functionDeclare
    | 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)? ('throws' throwing=type)? 'do' body=block	            #functionBlock
    | 'inline'? 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)? ('throws' throwing=type)? '=' expression=tail               #functionInline
    ;

global:
      'var' NAME '=' expr NL                                                                                                        #globalDefine
    | 'val' NAME '=' expr NL                                                                                                        #globalDefineConst
    | 'vol' NAME '=' expr NL                                                                                                        #globalDefineVolatile
    | 'var' NAME (':' type)? '=' expr NL                                                                                            #globalDefineTyped
    | 'val' NAME (':' type)? '=' expr NL                                                                                            #globalDefineConstTyped
    | 'vol' NAME (':' type)? '=' expr NL                                                                                            #globalDefineVolatileTyped

    | 'const' NAME (':' type)? '=' expr NL                                                                                          #globalConstant

    | 'declare' 'var' name=NAME ('extern' externalName=STRING)? 'as' type  NL                                                       #globalDeclareDefine
    | 'declare' ('val' | 'const') name=NAME ('extern' externalName=STRING)? 'as' type  NL                                           #globalDeclareConst
    | 'declare' 'type' NAME 'as' type NL                                                                                            #globalTypeDefine
    | 'type' name=NAME '=' modifier=NAME? type NL                                                                                   #globalTypeAlias // `distinct` isn't a keyword, like `pool`
    | 'declare' 'enum' name=NAME 'as' INDENT (atoms+=ATOM '=' constants+=NAME NL)+ DEDENT                                          #globalEnumDeclare

    | 'unit' NAME '(' NAME ':' type ')' '=' expr NL                                                                                 #globalUnitDeclare // Number sufix

    | (modifiers+=recordModifier)* ('rec' | 'record') name=NAME ('<' typeParams+=NAME (',' typeParams+=NAME)* '>')? ('extends' (qualifier=NAME '.')? extending=NAME)? 'as' INDENT (fields+=recordField)+ DEDENT    #globalRecordDefine
    | ('rec' | 'record') name=NAME 'extends' (qualifier=NAME '.')? extending=NAME NL                                                              #globalRecordDefine
    // `union Name as`: the fields share their memory. `union` isn't a keyword (`union(a, b)` is a function of Set), so Visitor checks the word.
    | (modifiers+=recordModifier)* keyword=NAME name=NAME 'as' INDENT (fields+=recordField)+ DEDENT                                              #globalUnionDefine
    | 'declare' ('rec' | 'record') name=NAME ('extern' externalName=STRING)? 'as' INDENT (names+=NAME ':' types+=type widths+=fieldWidth NL)+  DEDENT               #globalRecordDeclareDefine
    | 'declare' ('rec' | 'record') name=NAME ('extern' externalName=STRING)? NL                                                                  #globalRecordDeclare

    | 'declare' 'struct' name=NAME ('extern' externalName=STRING)? 'as' INDENT (names+=NAME ':' types+=type widths+=fieldWidth NL)+  DEDENT            #globalStructDeclareDefine
    | 'declare' 'struct' name=NAME ('extern' externalName=STRING)? NL                                                               #globalStructDeclare

    // `declare union x`: C's `union x`. `declare union rec x_t`: a typedef of a union, as `declare rec` is of a struct.
    | 'declare' keyword=NAME typedef=('rec' | 'record')? name=NAME ('extern' externalName=STRING)? 'as' INDENT (names+=NAME ':' types+=type widths+=fieldWidth NL)+  DEDENT    #globalUnionDeclareDefine
    | 'declare' keyword=NAME typedef=('rec' | 'record')? name=NAME ('extern' externalName=STRING)? NL                                                                         #globalUnionDeclare

    | 'inter' name=NAME 'as' INDENT (members+=interfaceMember)+ DEDENT                                                             #globalInterfaceDefine

    | 'module' name=STRING NL                                                                                                           #globalNamespace
    ;

// What an interface asks of a record: a field `name: Type`, or a function `fn name(Types): R` callable on it (`x.name(...)`).
interfaceMember:
      name=NAME ':' type NL                                                                          #interfaceField
    | 'fn' name=NAME '(' (params+=type (',' params+=type)*)? ')' (':' returnType=type)? ('throws' throwing=type)? NL    #interfaceMethod
    ;

// A field of a record: `name: Type`, or `name: Type = default`, the value a record literal gets when it leaves the field out.
recordField:
      name=NAME ':' type fieldWidth ('=' value=expr)? NL
    ;

// `: 3` after a field's type: a C bit-field of 3 bits. Written without the space (`UInt:3`) the lexer sees an atom.
fieldWidth:
      (':' width=expr | atomWidth=ATOM)?
    ;

// `packed` or `align(16)` before `rec` or `union`. Not keywords, like `pool`, so Visitor checks the word.
recordModifier:
      name=NAME ('(' size=expr ')')?
    ;

use:
      'use' from=STRING NL
    ;

// The expression inside `${...}` in a string literal, parsed on its own (see Compiler.parseInterpolation).
interpolation:
      expr NL* EOF
    ;

// `export fn f()`: C code can call f by its name; `export extern "c_name" fn f()` gives it another. `export` isn't a keyword either.
exportDeclaration:
      keyword=NAME ('extern' externalName=STRING)? func
    ;

// `priv` before a declaration: only code in the same module can use it.
privateDeclaration:
      'priv' func         #privateFunction
    | 'priv' global       #privateGlobal
    ;

program:
      (functions+=func | globals+=global | uses+=use | privates+=privateDeclaration | exports+=exportDeclaration | NL)* EOF
    ;