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
FLOAT: '0f'|[1-9][0-9]*('.'[0-9]*)?'f'?;
DOUBLE: '0d'|[1-9][0-9]*('.'[0-9]*)?'d'?;
HEX: '0x'[0-9a-fA-F]*;
BIN: '0b'[0-1]*;

CHAR: '\''.'\'' | '\'\\'('n'|'r'|'t'|'\\'|'\''|'"'|'0')'\'';

STRING: '"' .*? '"';
EMBEDED: [_a-zA-Z][_a-zA-Z0-9]*'`'.*?'`';

WHITESPACE: [ \t]+ -> skip;
EMPTYLINE: NL -> skip;
COMMENT: '//' ~[\r\n]* -> skip;
BLOCKCOMMENT: '/*' .*? '*/' -> skip;

number:
      INT           #numberInt
    | FLOAT         #numberFloat
    | DOUBLE        #numberDouble
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
    'not' | '!' | '!!' | '~' | '-'
    ;

binOp:
      '+' | '-' | '*' | '/' | '%' | '**'
    | '<' | '>' | '<=' | '>=' | '==' | '!='
    |'&&' | 'and' | '||' | 'or' | '^^' | 'xor'
    | '===' | '!==' | '??'
    | '<<' | '>>' | '&' | '|' | '^'
    ;

expr:
      'here'                                                        #exprHere
    | left=expr binOp right=expr                                    #exprBinOp
    | unOp expr                                                     #exprUnOp
    | '(' INDENT expr NL DEDENT ')'                                 #exprNest
    | primitive                                                     #exprPrimitive
    | NAME                                                          #exprNamed
    | 'if' '(' condition=expr ')' body=expr 'else' elseBody=expr    #exprInlineIf
    | expr 'is' NAME                                                #exprIs
    | expr '!is' NAME                                               #exprIsNot
    | expr 'as' type                                                #exprAs
    | expr 'as' 'unsafe' type                                       #exprAs

    | expr '.' NAME                                                 #exprAccessName
    | expr '[' expr ']'                                             #exprAccessor
    | expr '[' INDENT expr NL DEDENT ']'                            #exprAccessor

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

    | '(' expr ')'                                                                                      #exprNest
    | '(' INDENT expr NL DEDENT ')'                                                                     #exprNest
//    | 'case' '('? match=expr ')'? 'when' lefts+=primitive '->' rights+=expr ('|' lefts+=primitive '->' rights+=expr)* #exprMatch
    ;

type:
      NAME                                                                  #typeByName
    | type '?'                                                              #typeNullable
    | types+=type '|' types+=type ('|' types+=type)*                        #typeEnum
    | types+=type '&' types+=type ('&' types+=type)*                        #typeUnion
    | '(' type ')'                                                          #typeNest
    | ATOM                                                                  #typeAtom
    ;

record:
      'rec' name=NAME 'as' INDENT (names+=NAME ':' types+=type NL)+ DEDENT
    | 'declare' 'rec' name=NAME 'as' INDENT (names+=NAME ':' types+=type NL)+ DEDENT
    ;

interface:
      'inter'
    ;

parameter:
      NAME ':' type                         #parameterTyped
    | NAME ':' type '=' expr                #parameterTypedDefault
    | NAME '=' expr                         #parameterDefault
    ;

statement:
      'var' NAME '=' expr NL                                                            #statementDefine
    | 'val' NAME '=' expr NL                                                            #statementDefineConst
    | 'var' NAME (':' type)? '=' expr NL                                                #statementDefineTyped
    | 'val' NAME (':' type)? '=' expr NL                                                #statementDefineConstTyped
    | NAME '=' expr NL                                                                  #statementAssignment

    | 'break' NL                                                                        #statementBreak
    | 'continue' NL                                                                     #statementContinue
    | 'pass' NL                                                                         #statementPass

    | 'defer' statement                                                                 #statementDefer

    | 'if' '('? conditions+=expr ')'? 'do' bodies+=block ('else' 'if' '('? conditions+=expr ')'? 'do' bodies+=block)* ('else' elseBody=block)?      #statementIf
    | 'while' '('? condition=expr ')'? 'do' body=block ('else' elseBody=block)?                                                                     #statementWhile
    | 'for' '('? NAME 'in' expr ('where' expr)? ')'? 'do' body=block ('else' elseBody=block)?                                                       #statementForIn

    | NAME '(' ')' NL                                                   #statementFunctionCallNoArgs
    | NAME '('? args+=expr ')'? NL                                      #statementFunctionCallWithArgs
    | NAME '(' INDENT args+=expr NL DEDENT ')' NL                       #statementFunctionCallWithArgs
    | NAME '(' args+=expr (',' args+=expr)* ')' NL                      #statementFunctionCallWithArgs
    | NAME '(' INDENT args+=expr (',' NL args+=expr)* NL DEDENT ')' NL  #statementFunctionCallWithArgs

    | args+=expr '.' NAME '(' ')' NL                                    #statementFunctionCallNoArgs
    | args+=expr '.' NAME '(' args+=expr ')' NL                         #statementFunctionCallNoArgs
    | args+=expr '.' NAME '(' INDENT args+=expr NL DEDENT ')' NL        #statementFunctionCallNoArgs
    | args+=expr '.' NAME '(' args+=expr (',' args+=expr)* ')' NL       #statementFunctionCallWithArgs
    | args+=expr '.' NAME '(' INDENT args+=expr (',' NL args+=expr)* NL DEDENT ')' NL       #statementFunctionCallWithArgs

    | 'ret' expr NL                                                     #statementReturn
    | 'pool' NAME ('from' NAME)? 'in' body=block                        #statementPoolLocal
    ;

func:
      'declare' 'fn' (externalName=STRING 'as')? name=NAME '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)? NL    #functionDeclare
    | 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)? 'do' body=block                             #functionBlock
    | 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)? '=' expression=expr NL                      #functionInline
    ;

global:
      'var' NAME '=' expr NL                                                                            #globalDefine
    | 'val' NAME '=' expr NL                                                                            #globalDefineConst
    | 'var' NAME (':' type)? '=' expr NL                                                                #globalDefineTyped
    | 'val' NAME (':' type)? '=' expr NL                                                                #globalDefineConstTyped
    | 'declare' 'var' (externalName=STRING 'as')? name=NAME ':' type '=' expr NL                        #globalDeclareDefine
    | 'declare' 'val' (externalName=STRING 'as')? name=NAME ':' type '=' expr NL                        #globalDeclareDefineConst
    | 'type' NAME '=' type NL                                                                           #globalTypeDefine
    ;

use:
      'use' from=STRING NL
    ;

program:
      (functions+=func | records+=record | globals+=global | uses+=use)* EOF
    ;