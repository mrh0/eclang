// Generated from W:/Development/GitHub/eclang/src/main/antlr4\Eclang.g4 by ANTLR 4.10.1
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
	 * Visit a parse tree produced by the {@code numberInt}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberInt(EclangParser.NumberIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberFloat}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberFloat(EclangParser.NumberFloatContext ctx);
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
	 * Visit a parse tree produced by the {@code lambdaNoArgs}
	 * labeled alternative in {@link EclangParser#lambda}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaNoArgs(EclangParser.LambdaNoArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lambdaArgs}
	 * labeled alternative in {@link EclangParser#lambda}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaArgs(EclangParser.LambdaArgsContext ctx);
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
	 * Visit a parse tree produced by the {@code exprLambda}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprLambda(EclangParser.ExprLambdaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprAs}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAs(EclangParser.ExprAsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprBinOp}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprBinOp(EclangParser.ExprBinOpContext ctx);
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
	 * Visit a parse tree produced by the {@code exprTuple}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprTuple(EclangParser.ExprTupleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprCallFunction}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCallFunction(EclangParser.ExprCallFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprPrimitive}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprPrimitive(EclangParser.ExprPrimitiveContext ctx);
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
	 * Visit a parse tree produced by the {@code exprUnOp}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprUnOp(EclangParser.ExprUnOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeUnion}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeUnion(EclangParser.TypeUnionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeTuple}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeTuple(EclangParser.TypeTupleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeByName}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeByName(EclangParser.TypeByNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeCallSignature}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCallSignature(EclangParser.TypeCallSignatureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeNest}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNest(EclangParser.TypeNestContext ctx);
	/**
	 * Visit a parse tree produced by the {@code argumentTyped}
	 * labeled alternative in {@link EclangParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentTyped(EclangParser.ArgumentTypedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code argumentPrimitive}
	 * labeled alternative in {@link EclangParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentPrimitive(EclangParser.ArgumentPrimitiveContext ctx);
	/**
	 * Visit a parse tree produced by the {@code argumentWildcard}
	 * labeled alternative in {@link EclangParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentWildcard(EclangParser.ArgumentWildcardContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#orderExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderExpression(EclangParser.OrderExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCallWithArgs}
	 * labeled alternative in {@link EclangParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallWithArgs(EclangParser.FunctionCallWithArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCallNoArgs}
	 * labeled alternative in {@link EclangParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallNoArgs(EclangParser.FunctionCallNoArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefine}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefine(EclangParser.StatementDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineDestructureTuple}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineDestructureTuple(EclangParser.StatementDefineDestructureTupleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineConst}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineConst(EclangParser.StatementDefineConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineConstDestructureTuple}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineConstDestructureTuple(EclangParser.StatementDefineConstDestructureTupleContext ctx);
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
	 * Visit a parse tree produced by the {@code statementForIn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementForIn(EclangParser.StatementForInContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementCallFunction}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementCallFunction(EclangParser.StatementCallFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementCallFunctionReturn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementCallFunctionReturn(EclangParser.StatementCallFunctionReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementReturn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementReturn(EclangParser.StatementReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code useModule}
	 * labeled alternative in {@link EclangParser#use}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseModule(EclangParser.UseModuleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code useAllFromModule}
	 * labeled alternative in {@link EclangParser#use}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseAllFromModule(EclangParser.UseAllFromModuleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code useFromModule}
	 * labeled alternative in {@link EclangParser#use}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseFromModule(EclangParser.UseFromModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#funcPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncPrefix(EclangParser.FuncPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(EclangParser.FuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(EclangParser.ProgramContext ctx);
}