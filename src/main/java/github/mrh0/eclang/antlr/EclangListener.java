// Generated from W:/Development/GitHub/eclang/src/main/antlr4/Eclang.g4 by ANTLR 4.13.1
package github.mrh0.eclang.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link EclangParser}.
 */
public interface EclangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code numberInteger}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumberInteger(EclangParser.NumberIntegerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberInteger}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumberInteger(EclangParser.NumberIntegerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberFloating}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumberFloating(EclangParser.NumberFloatingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberFloating}
	 * labeled alternative in {@link EclangParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumberFloating(EclangParser.NumberFloatingContext ctx);
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
	 * Enter a parse tree produced by the {@code exprAsUnsafe}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprAsUnsafe(EclangParser.ExprAsUnsafeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprAsUnsafe}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprAsUnsafe(EclangParser.ExprAsUnsafeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprCreateArray}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprCreateArray(EclangParser.ExprCreateArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprCreateArray}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprCreateArray(EclangParser.ExprCreateArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprCastNotNull}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprCastNotNull(EclangParser.ExprCastNotNullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprCastNotNull}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprCastNotNull(EclangParser.ExprCastNotNullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprAccessNameNullishCoalescing}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprAccessNameNullishCoalescing(EclangParser.ExprAccessNameNullishCoalescingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprAccessNameNullishCoalescing}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprAccessNameNullishCoalescing(EclangParser.ExprAccessNameNullishCoalescingContext ctx);
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
	 * Enter a parse tree produced by the {@code exprCreateRecord}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprCreateRecord(EclangParser.ExprCreateRecordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprCreateRecord}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprCreateRecord(EclangParser.ExprCreateRecordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprPipe}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprPipe(EclangParser.ExprPipeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprPipe}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprPipe(EclangParser.ExprPipeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprNull}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprNull(EclangParser.ExprNullContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprNull}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprNull(EclangParser.ExprNullContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprAlignOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprAlignOf(EclangParser.ExprAlignOfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprAlignOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprAlignOf(EclangParser.ExprAlignOfContext ctx);
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
	 * Enter a parse tree produced by the {@code exprIt}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprIt(EclangParser.ExprItContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprIt}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprIt(EclangParser.ExprItContext ctx);
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
	 * Enter a parse tree produced by the {@code exprOffsetOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprOffsetOf(EclangParser.ExprOffsetOfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprOffsetOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprOffsetOf(EclangParser.ExprOffsetOfContext ctx);
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
	 * Enter a parse tree produced by the {@code exprPrimitiveUnit}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprPrimitiveUnit(EclangParser.ExprPrimitiveUnitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprPrimitiveUnit}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprPrimitiveUnit(EclangParser.ExprPrimitiveUnitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprSizeOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprSizeOf(EclangParser.ExprSizeOfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprSizeOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprSizeOf(EclangParser.ExprSizeOfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprAddressOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprAddressOf(EclangParser.ExprAddressOfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprAddressOf}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprAddressOf(EclangParser.ExprAddressOfContext ctx);
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
	 * Enter a parse tree produced by the {@code exprCreateRecordNamed}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprCreateRecordNamed(EclangParser.ExprCreateRecordNamedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprCreateRecordNamed}
	 * labeled alternative in {@link EclangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprCreateRecordNamed(EclangParser.ExprCreateRecordNamedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeAddressByName}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeAddressByName(EclangParser.TypeAddressByNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeAddressByName}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeAddressByName(EclangParser.TypeAddressByNameContext ctx);
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
	 * Enter a parse tree produced by the {@code typeTypeOf}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeTypeOf(EclangParser.TypeTypeOfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeTypeOf}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeTypeOf(EclangParser.TypeTypeOfContext ctx);
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
	 * Enter a parse tree produced by the {@code typeGeneric}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeGeneric(EclangParser.TypeGenericContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeGeneric}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeGeneric(EclangParser.TypeGenericContext ctx);
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
	 * Enter a parse tree produced by the {@code typeArray}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeArray(EclangParser.TypeArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeArray}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeArray(EclangParser.TypeArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeThrows}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeThrows(EclangParser.TypeThrowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeThrows}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeThrows(EclangParser.TypeThrowsContext ctx);
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
	 * Enter a parse tree produced by the {@code typeUnionAnon}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeUnionAnon(EclangParser.TypeUnionAnonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeUnionAnon}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeUnionAnon(EclangParser.TypeUnionAnonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeVolatile}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeVolatile(EclangParser.TypeVolatileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeVolatile}
	 * labeled alternative in {@link EclangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeVolatile(EclangParser.TypeVolatileContext ctx);
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
	 * Enter a parse tree produced by {@link EclangParser#interface}.
	 * @param ctx the parse tree
	 */
	void enterInterface(EclangParser.InterfaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link EclangParser#interface}.
	 * @param ctx the parse tree
	 */
	void exitInterface(EclangParser.InterfaceContext ctx);
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
	 * Enter a parse tree produced by the {@code parameterVarArg}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameterVarArg(EclangParser.ParameterVarArgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parameterVarArg}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameterVarArg(EclangParser.ParameterVarArgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parameterVarArgC}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameterVarArgC(EclangParser.ParameterVarArgCContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parameterVarArgC}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameterVarArgC(EclangParser.ParameterVarArgCContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parameterConst}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameterConst(EclangParser.ParameterConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parameterConst}
	 * labeled alternative in {@link EclangParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameterConst(EclangParser.ParameterConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefineVar}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefineVar(EclangParser.StatementDefineVarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefineVar}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefineVar(EclangParser.StatementDefineVarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefineVal}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefineVal(EclangParser.StatementDefineValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefineVal}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefineVal(EclangParser.StatementDefineValContext ctx);
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
	 * Enter a parse tree produced by the {@code statementDefineVarTyped}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefineVarTyped(EclangParser.StatementDefineVarTypedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefineVarTyped}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefineVarTyped(EclangParser.StatementDefineVarTypedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefineValTyped}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefineValTyped(EclangParser.StatementDefineValTypedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefineValTyped}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefineValTyped(EclangParser.StatementDefineValTypedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefineConstTyped}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefineConstTyped(EclangParser.StatementDefineConstTypedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefineConstTyped}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefineConstTyped(EclangParser.StatementDefineConstTypedContext ctx);
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
	 * Enter a parse tree produced by the {@code statementPass}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementPass(EclangParser.StatementPassContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementPass}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementPass(EclangParser.StatementPassContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementYield}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementYield(EclangParser.StatementYieldContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementYield}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementYield(EclangParser.StatementYieldContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementThrow}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementThrow(EclangParser.StatementThrowContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementThrow}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementThrow(EclangParser.StatementThrowContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementAssert}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementAssert(EclangParser.StatementAssertContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementAssert}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementAssert(EclangParser.StatementAssertContext ctx);
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
	 * Enter a parse tree produced by the {@code statementTailIf}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementTailIf(EclangParser.StatementTailIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementTailIf}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementTailIf(EclangParser.StatementTailIfContext ctx);
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
	 * Enter a parse tree produced by the {@code statementDeferDo}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDeferDo(EclangParser.StatementDeferDoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDeferDo}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDeferDo(EclangParser.StatementDeferDoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementLetIf}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementLetIf(EclangParser.StatementLetIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementLetIf}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementLetIf(EclangParser.StatementLetIfContext ctx);
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
	 * Enter a parse tree produced by the {@code statementForInRange}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementForInRange(EclangParser.StatementForInRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementForInRange}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementForInRange(EclangParser.StatementForInRangeContext ctx);
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
	 * Enter a parse tree produced by the {@code statementTry}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementTry(EclangParser.StatementTryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementTry}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementTry(EclangParser.StatementTryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementTryCatch}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementTryCatch(EclangParser.StatementTryCatchContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementTryCatch}
	 * labeled alternative in {@link EclangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementTryCatch(EclangParser.StatementTryCatchContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionDeclare}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclare(EclangParser.FunctionDeclareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionDeclare}
	 * labeled alternative in {@link EclangParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclare(EclangParser.FunctionDeclareContext ctx);
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
	 * Enter a parse tree produced by the {@code globalDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDefine(EclangParser.GlobalDefineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDefine(EclangParser.GlobalDefineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalDefineConst}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDefineConst(EclangParser.GlobalDefineConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalDefineConst}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDefineConst(EclangParser.GlobalDefineConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalDefineTyped}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDefineTyped(EclangParser.GlobalDefineTypedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalDefineTyped}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDefineTyped(EclangParser.GlobalDefineTypedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalDefineConstTyped}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDefineConstTyped(EclangParser.GlobalDefineConstTypedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalDefineConstTyped}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDefineConstTyped(EclangParser.GlobalDefineConstTypedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalDeclareDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDeclareDefine(EclangParser.GlobalDeclareDefineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalDeclareDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDeclareDefine(EclangParser.GlobalDeclareDefineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalDeclareConst}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDeclareConst(EclangParser.GlobalDeclareConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalDeclareConst}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDeclareConst(EclangParser.GlobalDeclareConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalTypeDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalTypeDefine(EclangParser.GlobalTypeDefineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalTypeDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalTypeDefine(EclangParser.GlobalTypeDefineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalUnitDeclare}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalUnitDeclare(EclangParser.GlobalUnitDeclareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalUnitDeclare}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalUnitDeclare(EclangParser.GlobalUnitDeclareContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalRecordDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalRecordDefine(EclangParser.GlobalRecordDefineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalRecordDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalRecordDefine(EclangParser.GlobalRecordDefineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalRecordDeclareDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalRecordDeclareDefine(EclangParser.GlobalRecordDeclareDefineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalRecordDeclareDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalRecordDeclareDefine(EclangParser.GlobalRecordDeclareDefineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalRecordDeclare}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalRecordDeclare(EclangParser.GlobalRecordDeclareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalRecordDeclare}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalRecordDeclare(EclangParser.GlobalRecordDeclareContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalStructDeclareDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalStructDeclareDefine(EclangParser.GlobalStructDeclareDefineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalStructDeclareDefine}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalStructDeclareDefine(EclangParser.GlobalStructDeclareDefineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalStructDeclare}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalStructDeclare(EclangParser.GlobalStructDeclareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalStructDeclare}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalStructDeclare(EclangParser.GlobalStructDeclareContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalNamespace}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobalNamespace(EclangParser.GlobalNamespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalNamespace}
	 * labeled alternative in {@link EclangParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobalNamespace(EclangParser.GlobalNamespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link EclangParser#use}.
	 * @param ctx the parse tree
	 */
	void enterUse(EclangParser.UseContext ctx);
	/**
	 * Exit a parse tree produced by {@link EclangParser#use}.
	 * @param ctx the parse tree
	 */
	void exitUse(EclangParser.UseContext ctx);
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