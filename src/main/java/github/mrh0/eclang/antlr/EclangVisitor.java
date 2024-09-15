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
	 * Visit a parse tree produced by the {@code exprNest}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNest(EclangParser.ExprNestContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprAccessNameNullishCoalescing}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAccessNameNullishCoalescing(EclangParser.ExprAccessNameNullishCoalescingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprInlineIf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprInlineIf(EclangParser.ExprInlineIfContext ctx);
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
	 * Visit a parse tree produced by the {@code exprPrimitive}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprPrimitive(EclangParser.ExprPrimitiveContext ctx);
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
	 * Visit a parse tree produced by the {@code typeEnum}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeEnum(EclangParser.TypeEnumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeNullable}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNullable(EclangParser.TypeNullableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeByName}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeByName(EclangParser.TypeByNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeAtom}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAtom(EclangParser.TypeAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeNest}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNest(EclangParser.TypeNestContext ctx);
	/**
	 * Visit a parse tree produced by {@link EclangParser#record}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecord(EclangParser.RecordContext ctx);
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
	 * Visit a parse tree produced by the {@code statementDefine}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefine(EclangParser.StatementDefineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineConst}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineConst(EclangParser.StatementDefineConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementDefineTyped}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefineTyped(EclangParser.StatementDefineTypedContext ctx);
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
	 * Visit a parse tree produced by the {@code statementDefer}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementDefer(EclangParser.StatementDeferContext ctx);
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
	 * Visit a parse tree produced by the {@code statementReturn}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementReturn(EclangParser.StatementReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statementPoolLocal}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementPoolLocal(EclangParser.StatementPoolLocalContext ctx);
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
	 * Visit a parse tree produced by the {@code globalDeclareDefineConst}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDeclareDefineConst(EclangParser.GlobalDeclareDefineConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalTypeDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalTypeDefine(EclangParser.GlobalTypeDefineContext ctx);
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