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
      return EclangLexer.super.nextToken();
    }
  };

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
    | '(' expr ')'                                                  #exprNest
    | '(' INDENT expr NL DEDENT ')'                                 #exprNest
    | primitive NAME                                                #exprPrimitiveUnit
    | primitive                                                     #exprPrimitive
    | NAME                                                          #exprNamed
    | 'sizeof' type                                                 #exprSizeOf
    | 'alignof' type                                                #exprAlignOf
    | 'offsetof' '(' type ',' field=NAME ')'                        #exprOffsetOf
    | 'fn' (qualifier=NAME '.')? name=NAME                          #exprFunctionReference

    | NAME '(' ')'                                                  #exprFunctionCallNoArgs
    | NAME '(' args+=expr (',' args+=expr)* ')'                     #exprFunctionCallWithArgs
    | NAME '(' INDENT args+=expr (',' NL args+=expr)* NL DEDENT ')' #exprFunctionCallWithArgs

    | ((qualifier=NAME '.')? recordType=NAME)? '{' names+=NAME '=' expr (',' names+=NAME '=' expr)* '}'                         #exprCreateRecordNamed
    | ((qualifier=NAME '.')? recordType=NAME)? '{' INDENT names+=NAME '=' expr (',' NL names+=NAME '=' expr)* NL DEDENT '}'     #exprCreateRecordNamed
    | ((qualifier=NAME '.')? recordType=NAME)? '{' expr (',' expr)* '}'                                                         #exprCreateRecord
    | ((qualifier=NAME '.')? recordType=NAME)? '{' INDENT expr (',' NL expr)* NL DEDENT '}'                                     #exprCreateRecord

    | '[' expr (',' expr)* (']c' | ']')                                                                 #exprCreateArray
    | '[' INDENT expr (',' NL expr)* NL DEDENT (']c' | ']')                                             #exprCreateArray

    // Postfix
    | args+=expr '.' NAME '(' ')'                                   #exprFunctionCallNoArgs
    | args+=expr '.' NAME '(' args+=expr (',' args+=expr)* ')'      #exprFunctionCallWithArgs
    | args+=expr '.' NAME '(' INDENT args+=expr (',' NL args+=expr)* NL DEDENT ')'                      #exprFunctionCallWithArgs
    | accesssed=expr '.' NAME                                       #exprAccessName
    | accesssed=expr '?.' NAME                                      #exprAccessNameNullishCoalescing
    | accesssed=expr '[' index=expr ']'                             #exprAccessor
    | accesssed=expr '[' INDENT index=expr NL DEDENT ']'            #exprAccessor
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

    | <assoc=right> left=expr op='**' right=expr                    #exprBinOp

    // Prefix, binding looser than `**`
    | op=('not' | '!' | '!!' | '~' | '-') expr                      #exprUnOp

    // Binary, tightest first
    | left=expr op=('*' | '/' | '%') right=expr                     #exprBinOp
    | left=expr op=('+' | '-') right=expr                           #exprBinOp
    | left=expr op=('<<' | '>>') right=expr                         #exprBinOp
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

    | 'if' condition=expr 'do' body=expr 'else' elseBody=expr      #exprInlineIf
    | 'if' '(' condition=expr ')' body=expr 'else' elseBody=expr    #exprInlineIfOld // Rejected with a hint to the form above
    | left=expr '|>' right=expr                                     #exprPipe
    | 'fn' '(' (params+=lambdaParameter (',' params+=lambdaParameter)*)? ')' (':' returnType=type)? '=' body=expr    #exprLambda
    ;

lambdaParameter:
      NAME (':' type)?
    ;

type:
      NAME '<' types+=NAME+ '>'                                                 #typeByNameParameters
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
    | left=type 'throws' throwing=type                                          #typeThrows
    //| ('vol' | 'volatile') type                                                 #typeVolatile
    | 'fn' '(' ')' (('=>' | ':') returnType=type)?                              #typeCallSignature
    | 'fn' '(' (NAME ':')? argTypes+=type (',' (NAME ':')? argTypes+=type)* ')' (('=>' | ':') returnType=type)?    #typeCallSignature
    | '(' ')' '=>' returnType=type                                              #typeCallSignature
    | '(' (NAME ':')? argTypes+=type (',' (NAME ':')? argTypes+=type)* ')' '=>' returnType=type    #typeCallSignature
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
      'var' NAME '=' expr NL                                                            #statementDefineVar
    | 'val' NAME '=' expr NL                                                            #statementDefineVal
    | 'vol' NAME '=' expr NL                                                            #statementDefineVol
    | 'const' NAME '=' expr NL                                                          #statementDefineConst
    | 'var' NAME ':' type '=' expr NL                                                   #statementDefineVarTyped
    | 'val' NAME ':' type '=' expr NL                                                   #statementDefineValTyped
    | 'vol' NAME ':' type '=' expr NL                                                   #statementDefineVolTyped
    | 'const' NAME ':' type '=' expr NL                                                 #statementDefineConstTyped

    | 'var' NAME ':' type NL                                                            #statementDeclareVar

    | target=expr op=('=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=' | '<<=' | '>>=') value=expr NL     #statementAssignment

    | 'break' ('if' condition=expr)? NL                                                      #statementBreak
    | 'continue' ('if' condition=expr)? NL                                                   #statementContinue
    | 'pass' ('if' condition=expr)? NL                                                       #statementPass
    | 'yield' ('if' condition=expr)? NL                                                      #statementYield
    | 'throw' throw=expr ('if' condition=expr)? NL                                           #statementThrow
    | 'assert' condition=expr (',' message=STRING)? NL                                       #statementAssert
    | 'ret' return=expr ('if' condition=expr)? NL                                            #statementReturn
    | expression=expr 'if' condition=expr NL                                                 #statementTailIf

    | expression=expr 'while' condition=expr NL                                                 #statementTailWhile
    //| expression=expr 'for' condition=expr NL                                                 #statementTailFor

    | 'defer' statement                                                                 #statementDefer
    | 'defer' 'do' body=block                                                           #statementDeferDo

    | 'if' kind=('val' | 'var') NAME '=' value=expr 'do' body=block ('else' 'do'? elseBody=block)?                                    #statementLetIf

    | 'if' conditions+=expr 'do' bodies+=block ('else' 'if' conditions+=expr 'do' bodies+=block)* ('else' 'do'? elseBody=block)?      	#statementIf
    | 'while' condition=expr 'do' body=block ('else' 'do'? elseBody=block)?                                                           	#statementWhile

    | 'for' ('var' | 'val')? NAME 'in' lower=expr 'to' upper=expr 'do' body=block ('else' 'do'? elseBody=block)?                        #statementForInRange // Rejected with a hint to use a range
    | 'for' ('var' | 'val')? (index=NAME ',')? name=NAME 'in' iterable=expr ('where' filter=expr)? 'do' body=block ('else' 'do'? elseBody=block)?    #statementForIn

    | NAME '(' ')' NL                                                   #statementFunctionCallNoArgs
    | NAME '(' args+=expr (',' args+=expr)* ')' NL                      #statementFunctionCallWithArgs
    | NAME '(' INDENT args+=expr (',' NL args+=expr)* NL DEDENT ')' NL  #statementFunctionCallWithArgs

    | args+=expr '.' NAME '(' ')' NL                                    #statementFunctionCallNoArgs
    | args+=expr '.' NAME '(' args+=expr (',' args+=expr)* ')' NL       #statementFunctionCallWithArgs
    | args+=expr '.' NAME '(' INDENT args+=expr (',' NL args+=expr)* NL DEDENT ')' NL       #statementFunctionCallWithArgs

    | keyword=NAME (name=NAME)? 'do' body=block                                                 #statementPool // `pool do` / `pool name do`; `pool` stays usable as a name
    | 'try' left=expr NL                                                                        #statementTry
    | 'try' left=expr 'catch' NAME 'do' elseBody=block                                          #statementTryCatch
    // | 'try' INDENT left=expr NL DEDENT 'catch' NAME 'do' elseBody=block                          #statementTryCatch
    ;

func:
      'declare' 'fn' name=NAME ('extern' externalName=STRING)? 'as' '(' params+=parameter? (',' params+=parameter)* ')' ((':' | '=>') returnType=type)? ('throws' throwing=type)? NL       #functionDeclare
    | 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)? ('throws' throwing=type)? 'do' body=block	            #functionBlock
    | 'inline'? 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)? ('throws' throwing=type)? '=' expression=expr NL            #functionInline
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
    | 'declare' 'enum' name=NAME 'as' INDENT (atoms+=ATOM '=' constants+=NAME NL)+ DEDENT                                          #globalEnumDeclare

    | 'unit' NAME '(' NAME ':' type ')' '=' expr NL                                                                                 #globalUnitDeclare // Number sufix

    | ('rec' | 'record') name=NAME ('extends' extending=NAME)? 'as' INDENT (names+=NAME ':' types+=type NL)+ DEDENT                              #globalRecordDefine
    | 'declare' ('rec' | 'record') name=NAME ('extern' externalName=STRING)? 'as' INDENT (names+=NAME ':' types+=type NL)+  DEDENT               #globalRecordDeclareDefine
    | 'declare' ('rec' | 'record') name=NAME ('extern' externalName=STRING)? NL                                                                  #globalRecordDeclare

    | 'declare' 'struct' name=NAME ('extern' externalName=STRING)? 'as' INDENT (names+=NAME ':' types+=type NL)+  DEDENT            #globalStructDeclareDefine
    | 'declare' 'struct' name=NAME ('extern' externalName=STRING)? NL                                                               #globalStructDeclare

    | 'inter' name=NAME 'as' INDENT (names+=NAME ':' types+=type NL)+ DEDENT                                                       #globalInterfaceDefine

    | 'module' name=STRING NL                                                                                                           #globalNamespace
    ;

use:
      'use' from=STRING NL
    ;

// The expression inside `${...}` in a string literal, parsed on its own (see Compiler.parseInterpolation).
interpolation:
      expr NL* EOF
    ;

program:
      (functions+=func | globals+=global | uses+=use | NL)* EOF
    ;