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
      '+' | '-' | '*' | '/' | '%'
    | '<' | '>' | '<=' | '>=' | '==' | '!='
    |'&&' | 'and' | '||' | 'or'
    | '<<' | '>>'
    ;

lambda:
    '(' ')' '=>' expr                                               #lambdaNoArgs
    | '('? params+=parameter ')'? '=>' expr                            #lambdaArgs
    | '(' params+=parameter (',' params+=parameter)+ ')' '=>' expr        #lambdaArgs
    ;

functionCall:
      NAME '(' ')'                                          #functionCallNoArgs
    | NAME '('? args+=expr ')'?                             #functionCallWithArgs
    | NAME '(' args+=expr (',' args+=expr)* ')'             #functionCallWithArgs
    ;

expr:
      'here'                                                        #exprHere
    | left=expr binOp right=expr                                    #exprBinOp
    | unOp expr                                                     #exprUnOp
    | '(' expr ')'                                                  #exprNest
    | primitive                                                     #exprPrimitive
    | NAME                                                          #exprNamed
    | 'if' '(' condition=expr ')' body=expr 'else' elseBody=expr    #exprInlineIf
    | expr 'is' NAME                                                #exprIs
    | expr '!is' NAME                                               #exprIsNot
    | expr 'as' NAME                                                #exprAs
    | NAME '(' args+=expr? (',' args+=expr)* ')'                    #exprCallFunction
    | values+=expr '&' values+=expr ('&' values+=expr)*             #exprTuple
    | expr '.' NAME                                                 #exprAccessName
    | expr '[' expr ']'                                             #exprAccessor
    | lambda                                                        #exprLambda
    | functionCall                                                  #exprFunctionCall
    ;

type:
      NAME                                                          #typeByName
    | types+=type '|' types+=type ('|' types+=type)*                #typeUnion
    | types+=type '&' types+=type ('&' types+=type)*                #typeTuple
    | '(' type (',' type)* ')' '=>' type                            #typeCallSignature
    | '(' type ')'                                                  #typeNest
    ;

record:
      'rec' name=NAME 'as' INDENT (names+=NAME ':' types+=type NL)+ DEDENT
    ;

parameter:
      NAME ':' type                         #parameterTyped
    | NAME ':' type '=' expr                #parameterTypedDefault
    | NAME '=' expr                         #parameterDefault
    ;

orderExpression:
      'orderby' expr
    | 'orderasc'
    | 'orderdesc'
    ;

statement:
      'var' NAME (':' type)? '=' expr NL                    #statementDefine
    | 'var' NAME (':' type)? '=' functionCall NL            #statementDefine
    | 'var' NAME ',' NAME (',' NAME)* '=' functionCall NL   #statementDefineDestructureTuple
    | 'val' NAME (':' type)? '=' expr NL                    #statementDefineConst
    | 'val' NAME (':' type)? '=' functionCall NL            #statementDefineConst
    | 'val' NAME ',' NAME (',' NAME)* '=' functionCall NL   #statementDefineConstDestructureTuple
    | NAME '=' expr NL                                      #statementAssignment

    | 'break' NL                                            #statementBreak
    | 'continue' NL                                         #statementContinue

    | 'if' '('? conditions+=expr ')'? 'do' bodies+=block ('eif' '('? conditions+=expr ')'? 'do' bodies+=block)* ('else' elseBody=block)?    #statementIf
    | 'while' '('? condition=expr ')'? 'do' body=block ('else' elseBody=block)?                                                             #statementWhile
    | 'for' '('? NAME 'in' expr ('where' expr)? orderExpression? ')'? 'do' body=block ('else' elseBody=block)?                              #statementForIn

    | functionCall NL                                       #statementCallFunction

    | 'ret' functionCall NL                                 #statementCallFunctionReturn
    | 'ret' expr NL                                         #statementReturn
    ;

use:
      'use' from=STRING ('as' as=NAME)? NL                              #useModule
    | 'use' '*' 'from' from=STRING NL                                   #useAllFromModule
    | 'use' exports+=NAME (',' exports+=NAME)* 'from' from=STRING NL    #useFromModule
    ;

funcPrefix:
      'start'
    ;

func:
      'external' 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' ':' returnType=type 'as' externalName=STRING NL   #functionExternal
    | funcPrefix? 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' ':' returnType=type 'do' body=block              #functionBlock
    | funcPrefix? 'fn' name=NAME '(' params+=parameter? (',' params+=parameter)* ')' (':' returnType=type)? '=' expression=expr NL    #functionExpr
    ;

program:
      use*
      (functions+=func | records+=record)+
      EOF
    ;