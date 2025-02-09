// Generated from W:/Development/GitHub/eclang/src/main/antlr4/Eclang.g4 by ANTLR 4.13.1
package github.mrh0.eclang.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link EclangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface EclangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code numberLong}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberLong(EclangParser.NumberLongContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberChar}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberChar(EclangParser.NumberCharContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberShort}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberShort(EclangParser.NumberShortContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberInt}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberInt(EclangParser.NumberIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberUInt}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberUInt(EclangParser.NumberUIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberUChar}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberUChar(EclangParser.NumberUCharContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberUShort}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberUShort(EclangParser.NumberUShortContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberULong}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberULong(EclangParser.NumberULongContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberFloat}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberFloat(EclangParser.NumberFloatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberDouble}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberDouble(EclangParser.NumberDoubleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberHex}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberHex(EclangParser.NumberHexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberBin}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberBin(EclangParser.NumberBinContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primitiveNumber}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveNumber(EclangParser.PrimitiveNumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primitiveBool}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveBool(EclangParser.PrimitiveBoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primitiveString}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveString(EclangParser.PrimitiveStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primitiveChar}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveChar(EclangParser.PrimitiveCharContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primitiveAtom}
	 * labeled alternative in {@link EclangParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveAtom(EclangParser.PrimitiveAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(EclangParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#unOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnOp(EclangParser.UnOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#binOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinOp(EclangParser.BinOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprAsUnsafe}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAsUnsafe(EclangParser.ExprAsUnsafeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprCreateArray}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCreateArray(EclangParser.ExprCreateArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprCastNotNull}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCastNotNull(EclangParser.ExprCastNotNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprAccessNameNullishCoalescing}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAccessNameNullishCoalescing(EclangParser.ExprAccessNameNullishCoalescingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprFunctionCallWithArgs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprFunctionCallWithArgs(EclangParser.ExprFunctionCallWithArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprHere}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprHere(EclangParser.ExprHereContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprNamed}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNamed(EclangParser.ExprNamedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprCreateRecord}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCreateRecord(EclangParser.ExprCreateRecordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprNull}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNull(EclangParser.ExprNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprAlignOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAlignOf(EclangParser.ExprAlignOfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprAccessor}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAccessor(EclangParser.ExprAccessorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprIs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprIs(EclangParser.ExprIsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprFunctionCallNoArgs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprFunctionCallNoArgs(EclangParser.ExprFunctionCallNoArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprAccessName}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAccessName(EclangParser.ExprAccessNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprIsNot}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprIsNot(EclangParser.ExprIsNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprNest}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNest(EclangParser.ExprNestContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprInlineIf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprInlineIf(EclangParser.ExprInlineIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprAs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAs(EclangParser.ExprAsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprOffsetOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprOffsetOf(EclangParser.ExprOffsetOfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprBinOp}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprBinOp(EclangParser.ExprBinOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprSizeOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSizeOf(EclangParser.ExprSizeOfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprAddressOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAddressOf(EclangParser.ExprAddressOfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprPrimitive}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprPrimitive(EclangParser.ExprPrimitiveContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprUnOp}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprUnOp(EclangParser.ExprUnOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprCreateRecordNamed}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCreateRecordNamed(EclangParser.ExprCreateRecordNamedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeUnion}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeUnion(EclangParser.TypeUnionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeAddressByName}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAddressByName(EclangParser.TypeAddressByNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeEnum}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeEnum(EclangParser.TypeEnumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeArray}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArray(EclangParser.TypeArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeNullable}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNullable(EclangParser.TypeNullableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeTypeOf}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeTypeOf(EclangParser.TypeTypeOfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeThrows}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeThrows(EclangParser.TypeThrowsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeByName}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeByName(EclangParser.TypeByNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeUnionAnon}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeUnionAnon(EclangParser.TypeUnionAnonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeAtom}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAtom(EclangParser.TypeAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeGeneric}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeGeneric(EclangParser.TypeGenericContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeNest}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNest(EclangParser.TypeNestContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#interface}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterface(EclangParser.InterfaceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parameterTyped}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterTyped(EclangParser.ParameterTypedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parameterTypedDefault}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterTypedDefault(EclangParser.ParameterTypedDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parameterDefault}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDefault(EclangParser.ParameterDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parameterRest}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterRest(EclangParser.ParameterRestContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parameterConst}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterConst(EclangParser.ParameterConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineVar}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineVar(EclangParser.StatementDefineVarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineVal}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineVal(EclangParser.StatementDefineValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineConst}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineConst(EclangParser.StatementDefineConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineVarTyped}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineVarTyped(EclangParser.StatementDefineVarTypedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineValTyped}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineValTyped(EclangParser.StatementDefineValTypedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineConstTyped}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineConstTyped(EclangParser.StatementDefineConstTypedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementAssignment}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementAssignment(EclangParser.StatementAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementBreak}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementBreak(EclangParser.StatementBreakContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementContinue}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementContinue(EclangParser.StatementContinueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementPass}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementPass(EclangParser.StatementPassContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementYield}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementYield(EclangParser.StatementYieldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementThrow}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementThrow(EclangParser.StatementThrowContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementAssert}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementAssert(EclangParser.StatementAssertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementReturn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementReturn(EclangParser.StatementReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementWhen}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementWhen(EclangParser.StatementWhenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefer}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefer(EclangParser.StatementDeferContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDeferDo}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDeferDo(EclangParser.StatementDeferDoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementIf}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementIf(EclangParser.StatementIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementWhile}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementWhile(EclangParser.StatementWhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementForInRange}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementForInRange(EclangParser.StatementForInRangeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementForIn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementForIn(EclangParser.StatementForInContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementFunctionCallNoArgs}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementFunctionCallNoArgs(EclangParser.StatementFunctionCallNoArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementFunctionCallWithArgs}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementFunctionCallWithArgs(EclangParser.StatementFunctionCallWithArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementTry}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementTry(EclangParser.StatementTryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementTryCatch}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementTryCatch(EclangParser.StatementTryCatchContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionDeclare}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclare(EclangParser.FunctionDeclareContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionBlock}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBlock(EclangParser.FunctionBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionInline}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionInline(EclangParser.FunctionInlineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDefine(EclangParser.GlobalDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalDefineConst}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDefineConst(EclangParser.GlobalDefineConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalDefineTyped}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDefineTyped(EclangParser.GlobalDefineTypedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalDefineConstTyped}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDefineConstTyped(EclangParser.GlobalDefineConstTypedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalDeclareDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDeclareDefine(EclangParser.GlobalDeclareDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalDeclareConst}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDeclareConst(EclangParser.GlobalDeclareConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalTypeDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalTypeDefine(EclangParser.GlobalTypeDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalRecordDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalRecordDefine(EclangParser.GlobalRecordDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalRecordDeclareDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalRecordDeclareDefine(EclangParser.GlobalRecordDeclareDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalRecordDeclare}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalRecordDeclare(EclangParser.GlobalRecordDeclareContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalStructDeclareDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalStructDeclareDefine(EclangParser.GlobalStructDeclareDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalStructDeclare}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalStructDeclare(EclangParser.GlobalStructDeclareContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#use}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse(EclangParser.UseContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(EclangParser.ProgramContext ctx);
}