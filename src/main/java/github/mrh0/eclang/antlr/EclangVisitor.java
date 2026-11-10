// Generated from src/main/antlr4/Eclang.g4 by ANTLR 4.13.1
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
	 * Visit a parse tree produced by the {@code numberInteger}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberInteger(EclangParser.NumberIntegerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberFloating}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberFloating(EclangParser.NumberFloatingContext ctx);
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
	 * Visit a parse tree produced by the {@code exprSafeCall}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSafeCall(EclangParser.ExprSafeCallContext ctx);
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
	 * Visit a parse tree produced by the {@code exprAccessNameNullishCoalescing}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAccessNameNullishCoalescing(EclangParser.ExprAccessNameNullishCoalescingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprCastNotNull}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCastNotNull(EclangParser.ExprCastNotNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprFunctionCallWithArgs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprFunctionCallWithArgs(EclangParser.ExprFunctionCallWithArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprCreateRecord}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCreateRecord(EclangParser.ExprCreateRecordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprNamed}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNamed(EclangParser.ExprNamedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprInlineIfOld}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprInlineIfOld(EclangParser.ExprInlineIfOldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprLambda}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprLambda(EclangParser.ExprLambdaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprPipe}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprPipe(EclangParser.ExprPipeContext ctx);
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
	 * Visit a parse tree produced by the {@code exprNew}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNew(EclangParser.ExprNewContext ctx);
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
	 * Visit a parse tree produced by the {@code exprUnsafe}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprUnsafe(EclangParser.ExprUnsafeContext ctx);
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
	 * Visit a parse tree produced by the {@code exprWhen}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprWhen(EclangParser.ExprWhenContext ctx);
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
	 * Visit a parse tree produced by the {@code exprNullishJump}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNullishJump(EclangParser.ExprNullishJumpContext ctx);
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
	 * Visit a parse tree produced by the {@code exprPrimitiveUnit}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprPrimitiveUnit(EclangParser.ExprPrimitiveUnitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprSizeOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSizeOf(EclangParser.ExprSizeOfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprUpdateRecord}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprUpdateRecord(EclangParser.ExprUpdateRecordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprAddressOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAddressOf(EclangParser.ExprAddressOfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprNewArrayOfType}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNewArrayOfType(EclangParser.ExprNewArrayOfTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprPrimitive}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprPrimitive(EclangParser.ExprPrimitiveContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprTry}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprTry(EclangParser.ExprTryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprSliceOpen}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSliceOpen(EclangParser.ExprSliceOpenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprFunctionReference}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprFunctionReference(EclangParser.ExprFunctionReferenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprCreateRecordNamed}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCreateRecordNamed(EclangParser.ExprCreateRecordNamedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprUnOp}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprUnOp(EclangParser.ExprUnOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(EclangParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jumpReturn}
	 * labeled alternative in {@link EclangParser#jump}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpReturn(EclangParser.JumpReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jumpThrow}
	 * labeled alternative in {@link EclangParser#jump}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpThrow(EclangParser.JumpThrowContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jumpBreak}
	 * labeled alternative in {@link EclangParser#jump}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpBreak(EclangParser.JumpBreakContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jumpContinue}
	 * labeled alternative in {@link EclangParser#jump}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpContinue(EclangParser.JumpContinueContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#tail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTail(EclangParser.TailContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#tryCatch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatch(EclangParser.TryCatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#lambdaParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaParameter(EclangParser.LambdaParameterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeAddressByName}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAddressByName(EclangParser.TypeAddressByNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeByNameParameters}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeByNameParameters(EclangParser.TypeByNameParametersContext ctx);
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
	 * Visit a parse tree produced by the {@code typeFixedArray}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeFixedArray(EclangParser.TypeFixedArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeCallSignature}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCallSignature(EclangParser.TypeCallSignatureContext ctx);
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
	 * Visit a parse tree produced by the {@code typeUnion}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeUnion(EclangParser.TypeUnionContext ctx);
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
	 * Visit a parse tree produced by the {@code typeTuple}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeTuple(EclangParser.TypeTupleContext ctx);
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
	 * Visit a parse tree produced by the {@code parameterVarArg}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterVarArg(EclangParser.ParameterVarArgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parameterVarArgC}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterVarArgC(EclangParser.ParameterVarArgCContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parameterMut}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterMut(EclangParser.ParameterMutContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parameterKeyword}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterKeyword(EclangParser.ParameterKeywordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parameterVariable}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterVariable(EclangParser.ParameterVariableContext ctx);
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
	 * Visit a parse tree produced by the {@code statementDeclareVar}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDeclareVar(EclangParser.StatementDeclareVarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDestructure}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDestructure(EclangParser.StatementDestructureContext ctx);
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
	 * Visit a parse tree produced by the {@code statementConstAssert}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementConstAssert(EclangParser.StatementConstAssertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementReturn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementReturn(EclangParser.StatementReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementTailIf}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementTailIf(EclangParser.StatementTailIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementNullish}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementNullish(EclangParser.StatementNullishContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementTailWhile}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementTailWhile(EclangParser.StatementTailWhileContext ctx);
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
	 * Visit a parse tree produced by the {@code statementErrDefer}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementErrDefer(EclangParser.StatementErrDeferContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementErrDeferDo}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementErrDeferDo(EclangParser.StatementErrDeferDoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementWhen}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementWhen(EclangParser.StatementWhenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementLetIf}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementLetIf(EclangParser.StatementLetIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementIf}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementIf(EclangParser.StatementIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementLabeled}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementLabeled(EclangParser.StatementLabeledContext ctx);
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
	 * Visit a parse tree produced by the {@code statementSafeCall}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementSafeCall(EclangParser.StatementSafeCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementUnsafe}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementUnsafe(EclangParser.StatementUnsafeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementUnsafeLine}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementUnsafeLine(EclangParser.StatementUnsafeLineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDestroy}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDestroy(EclangParser.StatementDestroyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementPool}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementPool(EclangParser.StatementPoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementPrefixed}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementPrefixed(EclangParser.StatementPrefixedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constAssertRelation}
	 * labeled alternative in {@link EclangParser#constAssert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstAssertRelation(EclangParser.ConstAssertRelationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constAssertIn}
	 * labeled alternative in {@link EclangParser#constAssert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstAssertIn(EclangParser.ConstAssertInContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constAssertCondition}
	 * labeled alternative in {@link EclangParser#constAssert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstAssertCondition(EclangParser.ConstAssertConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#contract}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContract(EclangParser.ContractContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#destructure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDestructure(EclangParser.DestructureContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#destructurePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDestructurePart(EclangParser.DestructurePartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whenPatternIs}
	 * labeled alternative in {@link EclangParser#whenPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenPatternIs(EclangParser.WhenPatternIsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whenPatternElse}
	 * labeled alternative in {@link EclangParser#whenPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenPatternElse(EclangParser.WhenPatternElseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whenPatternBind}
	 * labeled alternative in {@link EclangParser#whenPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenPatternBind(EclangParser.WhenPatternBindContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whenPatternValues}
	 * labeled alternative in {@link EclangParser#whenPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenPatternValues(EclangParser.WhenPatternValuesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whenValuePlain}
	 * labeled alternative in {@link EclangParser#whenValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenValuePlain(EclangParser.WhenValuePlainContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whenValueFrom}
	 * labeled alternative in {@link EclangParser#whenValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenValueFrom(EclangParser.WhenValueFromContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whenValueUpTo}
	 * labeled alternative in {@link EclangParser#whenValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenValueUpTo(EclangParser.WhenValueUpToContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#whenArm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenArm(EclangParser.WhenArmContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#whenExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenExpr(EclangParser.WhenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#whenValueArm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenValueArm(EclangParser.WhenValueArmContext ctx);
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
	 * Visit a parse tree produced by the {@code globalConstant}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalConstant(EclangParser.GlobalConstantContext ctx);
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
	 * Visit a parse tree produced by the {@code globalTypeAlias}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalTypeAlias(EclangParser.GlobalTypeAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalEnumDeclare}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalEnumDeclare(EclangParser.GlobalEnumDeclareContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalRecordDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalRecordDefine(EclangParser.GlobalRecordDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalUnionDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalUnionDefine(EclangParser.GlobalUnionDefineContext ctx);
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
	 * Visit a parse tree produced by the {@code globalUnionDeclareDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalUnionDeclareDefine(EclangParser.GlobalUnionDeclareDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalUnionDeclare}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalUnionDeclare(EclangParser.GlobalUnionDeclareContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalInterfaceDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalInterfaceDefine(EclangParser.GlobalInterfaceDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalNamespace}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalNamespace(EclangParser.GlobalNamespaceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalConstAssert}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalConstAssert(EclangParser.GlobalConstAssertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code interfaceField}
	 * labeled alternative in {@link EclangParser#interfaceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceField(EclangParser.InterfaceFieldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code interfaceMethod}
	 * labeled alternative in {@link EclangParser#interfaceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMethod(EclangParser.InterfaceMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#recordField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordField(EclangParser.RecordFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#fieldWidth}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldWidth(EclangParser.FieldWidthContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#fieldExtern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldExtern(EclangParser.FieldExternContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#recordModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordModifier(EclangParser.RecordModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#typeParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeParameter(EclangParser.TypeParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#use}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse(EclangParser.UseContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#interpolation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterpolation(EclangParser.InterpolationContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#exportDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExportDeclaration(EclangParser.ExportDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code privateFunction}
	 * labeled alternative in {@link EclangParser#privateDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivateFunction(EclangParser.PrivateFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code privateGlobal}
	 * labeled alternative in {@link EclangParser#privateDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivateGlobal(EclangParser.PrivateGlobalContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(EclangParser.ProgramContext ctx);
}