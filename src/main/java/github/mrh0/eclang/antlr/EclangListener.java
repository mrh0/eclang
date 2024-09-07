// Generated from W:/Development/GitHub/eclang/src/main/antlr4/Eclang.g4 by ANTLR 4.13.1
package github.mrh0.eclang.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link EclangParser}.
 */
public interface EclangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code numberInt}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumberInt(EclangParser.NumberIntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberInt}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumberInt(EclangParser.NumberIntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberFloat}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumberFloat(EclangParser.NumberFloatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberFloat}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumberFloat(EclangParser.NumberFloatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberHex}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumberHex(EclangParser.NumberHexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberHex}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumberHex(EclangParser.NumberHexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberBin}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumberBin(EclangParser.NumberBinContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberBin}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumberBin(EclangParser.NumberBinContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primitiveNumber}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveNumber(EclangParser.PrimitiveNumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primitiveNumber}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveNumber(EclangParser.PrimitiveNumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primitiveBool}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveBool(EclangParser.PrimitiveBoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primitiveBool}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveBool(EclangParser.PrimitiveBoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primitiveString}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveString(EclangParser.PrimitiveStringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primitiveString}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveString(EclangParser.PrimitiveStringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primitiveChar}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveChar(EclangParser.PrimitiveCharContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primitiveChar}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveChar(EclangParser.PrimitiveCharContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primitiveAtom}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveAtom(EclangParser.PrimitiveAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primitiveAtom}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveAtom(EclangParser.PrimitiveAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link EclangParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(EclangParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link EclangParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(EclangParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link EclangParser#unOp}.
	 * @param ctx the parse tree
	 */
	void enterUnOp(EclangParser.UnOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link EclangParser#unOp}.
	 * @param ctx the parse tree
	 */
	void exitUnOp(EclangParser.UnOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link EclangParser#binOp}.
	 * @param ctx the parse tree
	 */
	void enterBinOp(EclangParser.BinOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link EclangParser#binOp}.
	 * @param ctx the parse tree
	 */
	void exitBinOp(EclangParser.BinOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprNest}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprNest(EclangParser.ExprNestContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprNest}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprNest(EclangParser.ExprNestContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprInlineIf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprInlineIf(EclangParser.ExprInlineIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprInlineIf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprInlineIf(EclangParser.ExprInlineIfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprFunctionCallWithArgs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprFunctionCallWithArgs(EclangParser.ExprFunctionCallWithArgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprFunctionCallWithArgs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprFunctionCallWithArgs(EclangParser.ExprFunctionCallWithArgsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprHere}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprHere(EclangParser.ExprHereContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprHere}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprHere(EclangParser.ExprHereContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprNamed}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprNamed(EclangParser.ExprNamedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprNamed}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprNamed(EclangParser.ExprNamedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprAs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprAs(EclangParser.ExprAsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprAs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprAs(EclangParser.ExprAsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprBinOp}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprBinOp(EclangParser.ExprBinOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprBinOp}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprBinOp(EclangParser.ExprBinOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprAccessor}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprAccessor(EclangParser.ExprAccessorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprAccessor}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprAccessor(EclangParser.ExprAccessorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprIs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprIs(EclangParser.ExprIsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprIs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprIs(EclangParser.ExprIsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprTuple}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprTuple(EclangParser.ExprTupleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprTuple}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprTuple(EclangParser.ExprTupleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprPrimitive}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprPrimitive(EclangParser.ExprPrimitiveContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprPrimitive}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprPrimitive(EclangParser.ExprPrimitiveContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprFunctionCallNoArgs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprFunctionCallNoArgs(EclangParser.ExprFunctionCallNoArgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprFunctionCallNoArgs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprFunctionCallNoArgs(EclangParser.ExprFunctionCallNoArgsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprAccessName}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprAccessName(EclangParser.ExprAccessNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprAccessName}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprAccessName(EclangParser.ExprAccessNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprIsNot}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprIsNot(EclangParser.ExprIsNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprIsNot}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprIsNot(EclangParser.ExprIsNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprUnOp}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprUnOp(EclangParser.ExprUnOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprUnOp}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprUnOp(EclangParser.ExprUnOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeUnion}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeUnion(EclangParser.TypeUnionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeUnion}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeUnion(EclangParser.TypeUnionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeEnum}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeEnum(EclangParser.TypeEnumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeEnum}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeEnum(EclangParser.TypeEnumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeNullable}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeNullable(EclangParser.TypeNullableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeNullable}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeNullable(EclangParser.TypeNullableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeByName}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeByName(EclangParser.TypeByNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeByName}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeByName(EclangParser.TypeByNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeCallSignature}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeCallSignature(EclangParser.TypeCallSignatureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeCallSignature}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeCallSignature(EclangParser.TypeCallSignatureContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeAtom}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeAtom(EclangParser.TypeAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeAtom}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeAtom(EclangParser.TypeAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeNest}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeNest(EclangParser.TypeNestContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeNest}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeNest(EclangParser.TypeNestContext ctx);
	/**
	 * Enter a parse tree produced by {@link EclangParser#record}.
	 * @param ctx the parse tree
	 */
	void enterRecord(EclangParser.RecordContext ctx);
	/**
	 * Exit a parse tree produced by {@link EclangParser#record}.
	 * @param ctx the parse tree
	 */
	void exitRecord(EclangParser.RecordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parameterTyped}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameterTyped(EclangParser.ParameterTypedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parameterTyped}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameterTyped(EclangParser.ParameterTypedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parameterTypedDefault}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameterTypedDefault(EclangParser.ParameterTypedDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parameterTypedDefault}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameterTypedDefault(EclangParser.ParameterTypedDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parameterDefault}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameterDefault(EclangParser.ParameterDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parameterDefault}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameterDefault(EclangParser.ParameterDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link EclangParser#orderExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrderExpression(EclangParser.OrderExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EclangParser#orderExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrderExpression(EclangParser.OrderExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefine}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefine(EclangParser.StatementDefineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefine}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefine(EclangParser.StatementDefineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefineConst}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefineConst(EclangParser.StatementDefineConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefineConst}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefineConst(EclangParser.StatementDefineConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefineRecord}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefineRecord(EclangParser.StatementDefineRecordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefineRecord}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefineRecord(EclangParser.StatementDefineRecordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefineRecordConst}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefineRecordConst(EclangParser.StatementDefineRecordConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefineRecordConst}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefineRecordConst(EclangParser.StatementDefineRecordConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementAssignment}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementAssignment(EclangParser.StatementAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementAssignment}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementAssignment(EclangParser.StatementAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementBreak}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementBreak(EclangParser.StatementBreakContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementBreak}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementBreak(EclangParser.StatementBreakContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementContinue}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementContinue(EclangParser.StatementContinueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementContinue}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementContinue(EclangParser.StatementContinueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefer}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefer(EclangParser.StatementDeferContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefer}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefer(EclangParser.StatementDeferContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementIf}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementIf(EclangParser.StatementIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementIf}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementIf(EclangParser.StatementIfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementWhile}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementWhile(EclangParser.StatementWhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementWhile}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementWhile(EclangParser.StatementWhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementForIn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementForIn(EclangParser.StatementForInContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementForIn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementForIn(EclangParser.StatementForInContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementFunctionCallNoArgs}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementFunctionCallNoArgs(EclangParser.StatementFunctionCallNoArgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementFunctionCallNoArgs}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementFunctionCallNoArgs(EclangParser.StatementFunctionCallNoArgsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementFunctionCallWithArgs}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementFunctionCallWithArgs(EclangParser.StatementFunctionCallWithArgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementFunctionCallWithArgs}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementFunctionCallWithArgs(EclangParser.StatementFunctionCallWithArgsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementReturn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementReturn(EclangParser.StatementReturnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementReturn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementReturn(EclangParser.StatementReturnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementPoolLocal}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementPoolLocal(EclangParser.StatementPoolLocalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementPoolLocal}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementPoolLocal(EclangParser.StatementPoolLocalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code deferedStatement}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDeferedStatement(EclangParser.DeferedStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code deferedStatement}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDeferedStatement(EclangParser.DeferedStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code useModule}
	 * labeled alternative in {@link EclangParser#use}.
	 * @param ctx the parse tree
	 */
	void enterUseModule(EclangParser.UseModuleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code useModule}
	 * labeled alternative in {@link EclangParser#use}.
	 * @param ctx the parse tree
	 */
	void exitUseModule(EclangParser.UseModuleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code useAllFromModule}
	 * labeled alternative in {@link EclangParser#use}.
	 * @param ctx the parse tree
	 */
	void enterUseAllFromModule(EclangParser.UseAllFromModuleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code useAllFromModule}
	 * labeled alternative in {@link EclangParser#use}.
	 * @param ctx the parse tree
	 */
	void exitUseAllFromModule(EclangParser.UseAllFromModuleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code useFromModule}
	 * labeled alternative in {@link EclangParser#use}.
	 * @param ctx the parse tree
	 */
	void enterUseFromModule(EclangParser.UseFromModuleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code useFromModule}
	 * labeled alternative in {@link EclangParser#use}.
	 * @param ctx the parse tree
	 */
	void exitUseFromModule(EclangParser.UseFromModuleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionExternal}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFunctionExternal(EclangParser.FunctionExternalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionExternal}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFunctionExternal(EclangParser.FunctionExternalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionBlock}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBlock(EclangParser.FunctionBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionBlock}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBlock(EclangParser.FunctionBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionInline}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFunctionInline(EclangParser.FunctionInlineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionInline}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFunctionInline(EclangParser.FunctionInlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link EclangParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(EclangParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link EclangParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(EclangParser.ProgramContext ctx);
}