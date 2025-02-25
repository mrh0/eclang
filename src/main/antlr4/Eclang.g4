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

INT: '0'|[1-9][_0-9]*;
FLOAT: '.0'|[1-9][0-9]*('.'[0-9]*)?;
HEX: '0x'[0-9a-fA-F]*;
BIN: '0b'[0-1]*;

CHAR: '\''.'\'' | '\'\\'('n'|'r'|'t'|'\\'|'\''|'"'|'0')'\'';

STRING: '"' .*? ('"c' | '"');
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

unOp:
    'not' | '!' | '!!' | '~' | '-' | '@'
    ;

binOp:
      '+' | '-' | '*' | '/' | '%' | '**'
    | '<' | '>' | '<=' | '>=' | '==' | '!='
    |'&&' | 'and' | '||' | 'or' | '^^' | 'xor'
    | '===' | '!==' | '??'
    | '<<' | '>>' | '&' | '|' | '^'
    | '..<' | '..=' | '..>' // Iterators
    ;

expr:
      'here'                                                        #exprHere
    | 'Null'                                                        #exprNull
    | left=expr binOp right=expr                                    #exprBinOp
    | unOp expr                                                     #exprUnOp
    | 'sizeof' type                                                 #exprSizeOf
    | 'alignof' type                                                #exprAlignOf
    | 'offsetof' type                                               #exprOffsetOf
    | 'addrof' expr                                                 #exprAddressOf
    | '(' expr ')'                                                  #exprNest
    | '(' INDENT expr NL DEDENT ')'                                 #exprNest
    | primitive NAME                                                #exprPrimitiveUnit
    | primitive                                                     #exprPrimitive
    | NAME                                                          #exprNamed
    | 'if' '(' condition=expr ')' body=expr 'else' elseBody=expr    #exprInlineIf
    | expr 'is' NAME                                                #exprIs
    | expr '!is' NAME                                               #exprIsNot
    | expr 'as' type                                                #exprAs
    | expr 'as' '(' type ')'                                        #exprAs
    | expr 'as' 'unsafe' type                                       #exprAsUnsafe
    | expr 'as' 'unsafe' '(' type ')'                               #exprAsUnsafe
    | expr '!'                                                      #exprCastNotNull

    | accesssed=expr '.' NAME                                                 #exprAccessName
    | accesssed=expr '?.' NAME                                                #exprAccessNameNullishCoalescing
    | accesssed=expr '[' index=expr ']'                                             #exprAccessor
    | accesssed=expr '[' INDENT index=expr NL DEDENT ']'                            #exprAccessor

    | NAME '(' ')'                                                  #exprFunctionCallNoArgs
    | NAME '(' args+=expr (',' args+=expr)* ')'                     #exprFunctionCallWithArgs
    | NAME '(' INDENT args+=expr (',' NL args+=expr)* NL DEDENT ')' #exprFunctionCallWithArgs
    | args+=expr '.' NAME '(' ')'                                   #exprFunctionCallNoArgs
    | args+=expr '.' NAME '(' args+=expr (',' args+=expr)* ')'      #exprFunctionCallWithArgs
    | args+=expr '.' NAME '(' INDENT args+=expr (',' NL args+=expr)* NL DEDENT ')'                      #exprFunctionCallWithArgs

    | recordType=NAME? '{' names+=NAME '=' expr (',' names+=NAME '=' expr)* '}'                         #exprCreateRecordNamed
    | recordType=NAME? '{' INDENT names+=NAME '=' expr (',' NL names+=NAME '=' expr)* NL DEDENT '}'     #exprCreateRecordNamed
    | recordType=NAME? '{' expr (',' expr)* '}'                                                         #exprCreateRecord
    | recordType=NAME? '{' INDENT expr (',' NL expr)* NL DEDENT '}'                                     #exprCreateRecord

    | '[' expr (',' expr)* (']c' | ']')                                                                 #exprCreateArray
    | '[' INDENT expr (',' NL expr)* NL DEDENT (']c' | ']')                                             #exprCreateArray

    | '(' expr ')'                                                                                      #exprNest
    | '(' INDENT expr NL DEDENT ')'                                                                     #exprNest
    //| 'try' left=expr 'else' right=expr                                                                 #exprTry
    //| 'try' INDENT left=expr NL DEDENT 'else' right=expr                                                #exprTry
    //| 'try' left=expr 'else' INDENT right=expr NL DEDENT                                                #exprTry
    //| 'try' INDENT left=expr NL DEDENT 'else' INDENT right=expr NL DEDENT                               #exprTry

    // 'case' '('? match=expr ')'? 'when' lefts+=primitive '->' rights+=expr ('|' lefts+=primitive '->' rights+=expr)* #exprMatch
    ;

type:
      NAME                                                                  #typeByName
    | '@' type                                                              #typeAddressByName
    | type '?'                                                              #typeNullable
    | types+=type '&' types+=type ('&' types+=type)*                        #typeEnum
    | types+=type '|' types+=type ('|' types+=type)*                        #typeUnion
    | 'anon' types+=type '|' types+=type ('|' types+=type)*                 #typeUnionAnon // Anonymus union
    | '(' type ')'                                                          #typeNest
    | ATOM                                                                  #typeAtom
    | '<' NAME '>'                                                          #typeGeneric
    | 'typeof' expr                                                         #typeTypeOf
    | type '[' (']c' | ']')                                                 #typeArray
    | left=type 'throws' throwing=type                                      #typeThrows
    | 'volatile' type                                                       #typeVolatile
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
    | 'const' parameter                     #parameterConst
    ;

statement:
      'var' NAME '=' expr NL                                                            #statementDefineVar
    | 'val' NAME '=' expr NL                                                            #statementDefineVal
    | 'const' NAME '=' expr NL                                                          #statementDefineConst
    | 'var' NAME ':' type '=' expr NL                                                   #statementDefineVarTyped
    | 'val' NAME ':' type '=' expr NL                                                   #statementDefineValTyped
    | 'const' NAME ':' type '=' expr NL                                                 #statementDefineConstTyped

    | NAME '=' expr NL                                                                  #statementAssignment

    | 'break' ('if' condition=expr)? NL                                                      #statementBreak
    | 'continue' ('if' condition=expr)? NL                                                   #statementContinue
    | 'pass' ('if' condition=expr)? NL                                                       #statementPass
    | 'yield' ('if' condition=expr)? NL                                                      #statementYield
    | 'throw' throw=expr ('if' condition=expr)? NL                                           #statementThrow
    | 'assert' STRING ('if' condition=expr)? NL                                              #statementAssert
    | 'ret' return=expr ('if' condition=expr)? NL                                            #statementReturn
    | expression=expr 'if' condition=expr NL                                                 #statementTailIf

    | 'defer' statement                                                                 #statementDefer
    | 'defer' 'do' body=block                                                           #statementDeferDo

    | 'if' ('val' | 'var') NAME '=' expr 'do' body=block                                #statementLetIf

    | 'if' conditions+=expr 'do' bodies+=block ('else' 'if' conditions+=expr 'do' bodies+=block)* ('else' 'do'? elseBody=block)?      	#statementIf
    | 'while' condition=expr 'do' body=block ('else' 'do'? elseBody=block)?                                                           	#statementWhile

    | 'for' ('var' | 'val')? NAME 'in' lower=expr 'to' upper=expr 'do' body=block ('else' 'do'? elseBody=block)?                        #statementForInRange
    | 'for' ('var' | 'val')? NAME 'in' iterable=expr ('where' expr)? 'do' body=block ('else' 'do'? elseBody=block)?                              #statementForIn

    | NAME '(' ')' NL                                                   #statementFunctionCallNoArgs
    | NAME '(' args+=expr (',' args+=expr)* ')' NL                      #statementFunctionCallWithArgs
    | NAME '(' INDENT args+=expr (',' NL args+=expr)* NL DEDENT ')' NL  #statementFunctionCallWithArgs

    | args+=expr '.' NAME '(' ')' NL                                    #statementFunctionCallNoArgs
    | args+=expr '.' NAME '(' args+=expr (',' args+=expr)* ')' NL       #statementFunctionCallWithArgs
    | args+=expr '.' NAME '(' INDENT args+=expr (',' NL args+=expr)* NL DEDENT ')' NL       #statementFunctionCallWithArgs

    | 'try' left=expr                                                                           #statementTry
    | 'try' left=expr 'catch' NAME 'do' elseBody=block                                          #statementTryCatch
    // | 'try' INDENT left=expr NL DEDENT 'catch' NAME 'do' elseBody=block                          #statementTryCatch
    ;

func:
      'declare' 'fn' name=NAME ('extern' externalName=STRING)? 'as' '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)?  NL                    #functionDeclare
    | 'pub'? 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)? 'do' body=block		            #functionBlock
    | 'pub'? 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)? '=' expression=expr NL            #functionInline
    ;

global:
      'var' NAME '=' expr NL                                                                                                        #globalDefine
    | 'val' NAME '=' expr NL                                                                                                        #globalDefineConst
    | 'var' NAME (':' type)? '=' expr NL                                                                                            #globalDefineTyped
    | 'val' NAME (':' type)? '=' expr NL                                                                                            #globalDefineConstTyped

    | 'declare' 'var' name=NAME ('extern' externalName=STRING)? 'as' type  NL                                                       #globalDeclareDefine
    | 'declare' ('val' | 'const') name=NAME ('extern' externalName=STRING)? 'as' type  NL                                           #globalDeclareConst
    | 'declare' 'type' NAME 'as' type NL                                                                                            #globalTypeDefine

    | 'unit' NAME '(' NAME ':' type ')' '=' expr NL                                                                                 #globalUnitDeclare // Number sufix

    | 'rec' name=NAME ('extends' extending=NAME)? 'as' INDENT (names+=NAME ':' types+=type NL)+ DEDENT                                                          #globalRecordDefine
    | 'declare' 'rec' name=NAME ('extern' externalName=STRING)? 'as' INDENT (names+=NAME ':' types+=type NL)+  DEDENT               #globalRecordDeclareDefine
    | 'declare' 'rec' name=NAME ('extern' externalName=STRING)? NL                                                                  #globalRecordDeclare

    | 'declare' 'struct' name=NAME ('extern' externalName=STRING)? 'as' INDENT (names+=NAME ':' types+=type NL)+  DEDENT            #globalStructDeclareDefine
    | 'declare' 'struct' name=NAME ('extern' externalName=STRING)? NL                                                               #globalStructDeclare

    | 'module' NAME NL                                                                                                           #globalNamespace
    ;

use:
      'use' from=STRING NL
    ;

program:
      (functions+=func | globals+=global | uses+=use)* EOF
    ;