package github.mrh0.eclang.ast

import github.mrh0.eclang.util.Util
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.antlr.EclangBaseVisitor
import github.mrh0.eclang.antlr.EclangParser
import github.mrh0.eclang.ast.token.*
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.ast.token.accessor.TAccessor
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.branch.TInlineIf
import github.mrh0.eclang.ast.token.branch.TStatementIf
import github.mrh0.eclang.ast.token.branch.TStatementLetIf
import github.mrh0.eclang.ast.token.branch.TTailIf
import github.mrh0.eclang.ast.token.data.*
import github.mrh0.eclang.ast.token.data.record.TCreateRecord
import github.mrh0.eclang.ast.token.data.record.TCreateRecordTyped
import github.mrh0.eclang.ast.token.data.record.THere
import github.mrh0.eclang.ast.token.data.unit.TPrimitiveUnit
import github.mrh0.eclang.ast.token.type.TTypeRecord
import github.mrh0.eclang.ast.token.function.*
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.ast.token.function.call.TStatementCall
import github.mrh0.eclang.ast.token.global.*
import github.mrh0.eclang.ast.token.loop.*
import github.mrh0.eclang.ast.token.op.*
import github.mrh0.eclang.ast.token.op.arithmetic.*
import github.mrh0.eclang.ast.token.op.bitwise.*
import github.mrh0.eclang.ast.token.op.compare.*
import github.mrh0.eclang.ast.token.op.logical.*
import github.mrh0.eclang.ast.token.type.*
import github.mrh0.eclang.ast.token.statement.*
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.misc.Interval
import github.mrh0.eclang.util.StringLiteral
import java.io.File

/** Builds the tokens of [file]. With [fixed], every token gets that location (an expression inside a string's `${...}`). */
class Visitor(private val file: File, private val fixed: Loc? = null) : EclangBaseVisitor<ITok>() {
    /** The name of every assignment visited so far, for [assignedIn]. */
    private val assignedNames = mutableListOf<String>()

    /** Runs [visit] (the body of a loop) and returns its result with the names it assigns (see `CompileData.forgetAssigned`). */
    private fun <T> assignedIn(visit: () -> T): Pair<T, Set<String>> {
        val start = assignedNames.size
        val result = visit()
        return result to assignedNames.subList(start, assignedNames.size).toSet()
    }

    fun <T : ParserRuleContext, O : ITok?> visit(list: MutableList<T>): MutableList<O> {
        val t: MutableList<O> = mutableListOf()
        for (pt in list) t.add(visit(pt) as O)
        return t
    }

    fun <T : Token?> tvisit(list: MutableList<T>): List<String> {
        return list.map { it!!.getText() }
    }

    fun <T : ParserRuleContext, O : ITok?> cvisit(rule: T?): O {
        return if (rule == null) throw Exception("Undefined rule.") else visit(rule) as O
    }

    fun loc(ctx: ParserRuleContext) = fixed ?: Loc(ctx.start, file)

    /** The source text of [ctx], as written (with its spaces). */
    private fun source(ctx: ParserRuleContext): String = ctx.start.inputStream.getText(Interval(ctx.start.startIndex, ctx.stop.stopIndex))

    /** How many times `it` is used in each `|>` stage being visited, innermost last. */
    private val itUses = ArrayList<Int>()

    // Program
    override fun visitProgram(ctx: EclangParser.ProgramContext): ITok = TProgram(loc(ctx), visit(ctx.functions), visit(ctx.uses), visit(ctx.globals))

    // Use
    // override fun visitUseFromModule(ctx: EclangParser.UseFromModuleContext): ITok {
    //     return TUseFromModule(loc(ctx), ctx.exports.map { it.text }, ctx.from.text.substring(1, ctx.from.text.length-1))
    // }

    // override fun visitUseAllFromModule(ctx: EclangParser.UseAllFromModuleContext): ITok {
    //     return TUseAllFromModule(loc(ctx), ctx.from.text.substring(1, ctx.from.text.length-1))
    // }

    // Records
    override fun visitGlobalRecordDefine(ctx: EclangParser.GlobalRecordDefineContext): ITok = TTypeRecord(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), ctx.extending?.text)
    override fun visitGlobalRecordDeclare(ctx: EclangParser.GlobalRecordDeclareContext): ITok = TTypeDeclareRecord(loc(ctx), ctx.name.text, listOf(), listOf(), null, Util.getStringContent(ctx.externalName?.text))
    override fun visitGlobalRecordDeclareDefine(ctx: EclangParser.GlobalRecordDeclareDefineContext): ITok = TTypeDeclareRecord(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), null, Util.getStringContent(ctx.externalName?.text))

    override fun visitGlobalStructDeclare(ctx: EclangParser.GlobalStructDeclareContext): ITok  = TTypeDeclareStruct(loc(ctx), ctx.name.text, listOf(), listOf(), Util.getStringContent(ctx.externalName?.text))
    override fun visitGlobalStructDeclareDefine(ctx: EclangParser.GlobalStructDeclareDefineContext): ITok  = TTypeDeclareStruct(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), Util.getStringContent(ctx.externalName?.text))

    // Types
    override fun visitTypeByName(ctx: EclangParser.TypeByNameContext): ITok = TTypeByName(loc(ctx), ctx.text)

    override fun visitTypeAddressByName(ctx: EclangParser.TypeAddressByNameContext): ITok = TTypePointerByName(loc(ctx), visit(ctx.type()))

    override fun visitTypeUnion(ctx: EclangParser.TypeUnionContext): ITok = TTypeUnion(loc(ctx), visit(ctx.types))

    override fun visitTypeEnum(ctx: EclangParser.TypeEnumContext): ITok = TTypeEnum(loc(ctx), visit(ctx.types))

    override fun visitTypeNullable(ctx: EclangParser.TypeNullableContext): ITok = TTypeNullable(loc(ctx), visit(ctx.type()))

    override fun visitTypeAtom(ctx: EclangParser.TypeAtomContext): ITok = TTypeAtom(loc(ctx), ctx.text)

    override fun visitTypeNest(ctx: EclangParser.TypeNestContext): ITok = visit(ctx.type())

    override fun visitTypeGeneric(ctx: EclangParser.TypeGenericContext): ITok = TTypeGeneric(loc(ctx), ctx.NAME().text)

    override fun visitTypeTypeOf(ctx: EclangParser.TypeTypeOfContext): ITok = TTypeOf(loc(ctx), visit(ctx.expr()))

    override fun visitTypeArray(ctx: EclangParser.TypeArrayContext): ITok =
        if(ctx.text.endsWith('c')) TTypeCArray(loc(ctx), visit(ctx.type())) else TTypeArray(loc(ctx), visit(ctx.type()))

    override fun visitTypeThrows(ctx: EclangParser.TypeThrowsContext): ITok = TTypeThrows(loc(ctx), visit(ctx.left), visit(ctx.throwing))

    override fun visitTypeCallSignature(ctx: EclangParser.TypeCallSignatureContext): ITok = TTypeCallSignature(loc(ctx), visit(ctx.argTypes), visit(ctx.returnType))

    // Functions
    override fun visitFunctionBlock(ctx: EclangParser.FunctionBlockContext): ITok {
        return TFuncBlock(loc(ctx), cvisit(ctx.body), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), returns(ctx, ctx.returnType, ctx.throwing))
    }

    override fun visitFunctionInline(ctx: EclangParser.FunctionInlineContext): ITok {
        return TFuncInline(loc(ctx), cvisit(ctx.expr()), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), returns(ctx, ctx.returnType, ctx.throwing))
    }

    override fun visitFunctionDeclare(ctx: EclangParser.FunctionDeclareContext): ITok {
        return TFuncExternal(loc(ctx), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), returns(ctx, ctx.returnType, ctx.throwing), Util.getStringContent(ctx.externalName?.text))
    }

    /** A function's return type: `: T`, `: T throws E`, `throws E` (returns nothing, may fail) or nothing. */
    private fun returns(ctx: ParserRuleContext, returnType: EclangParser.TypeContext?, throwing: EclangParser.TypeContext?): ITok? {
        val value: ITok? = returnType?.let { visit(it) }
        if (throwing == null) return value
        return TTypeThrows(loc(ctx), value ?: TTypeByName(loc(ctx), "None"), visit(throwing))
    }

    override fun visitExprFunctionReference(ctx: EclangParser.ExprFunctionReferenceContext): ITok = TNamedFunction(loc(ctx), ctx.name.text, ctx.qualifier?.text)

    override fun visitBlock(ctx: EclangParser.BlockContext): ITok = TBlock(loc(ctx), visit(ctx.statements))

    override fun visitParameterTyped(ctx: EclangParser.ParameterTypedContext): ITok = TParameterTyped(loc(ctx), ctx.NAME().text, visit(ctx.type()))
    override fun visitParameterDefault(ctx: EclangParser.ParameterDefaultContext): ITok = TParameterDefault(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitParameterTypedDefault(ctx: EclangParser.ParameterTypedDefaultContext): ITok = TParameterTypedDefault(loc(ctx), ctx.NAME().text, visit(ctx.type()), visit(ctx.expr()))
    override fun visitParameterVarArg(ctx: EclangParser.ParameterVarArgContext): ITok = TParameterVarArg(loc(ctx), ctx.NAME().text, visit(ctx.type()))
    override fun visitParameterVarArgC(ctx: EclangParser.ParameterVarArgCContext): ITok = TParameterVarArgC(loc(ctx))

    /** True for `first.name(...)`, which starts with an expression rather than the name. */
    private fun isMethod(ctx: ParserRuleContext) = ctx.getChild(0) is ParserRuleContext

    override fun visitExprFunctionCallNoArgs(ctx: EclangParser.ExprFunctionCallNoArgsContext): ITok = TExprCall(loc(ctx), ctx.NAME().text, visit(ctx.args), isMethod(ctx))
    override fun visitExprFunctionCallWithArgs(ctx: EclangParser.ExprFunctionCallWithArgsContext): ITok = TExprCall(loc(ctx), ctx.NAME().text, visit(ctx.args), isMethod(ctx))

    override fun visitStatementFunctionCallNoArgs(ctx: EclangParser.StatementFunctionCallNoArgsContext): ITok = TStatementCall(loc(ctx), ctx.NAME().text, visit(ctx.args), isMethod(ctx))
    override fun visitStatementFunctionCallWithArgs(ctx: EclangParser.StatementFunctionCallWithArgsContext): ITok = TStatementCall(loc(ctx), ctx.NAME().text, visit(ctx.args), isMethod(ctx))

    // Expressions
    override fun visitExprNest(ctx: EclangParser.ExprNestContext): ITok = TExprNest(loc(ctx), visit(ctx.expr()))

    override fun visitExprAccessName(ctx: EclangParser.ExprAccessNameContext): ITok = TAccessorNamed(loc(ctx), visit(ctx.accesssed), ctx.NAME().text)
    override fun visitExprAccessor(ctx: EclangParser.ExprAccessorContext): ITok = TAccessor(loc(ctx), visit(ctx.accesssed), visit(ctx.index))

    override fun visitExprAddressOf(ctx: EclangParser.ExprAddressOfContext): ITok = TAddressOf(loc(ctx), visit(ctx.expr()))
    override fun visitExprSizeOf(ctx: EclangParser.ExprSizeOfContext): ITok = TSizeOf(loc(ctx), visit(ctx.type()))
    override fun visitExprAlignOf(ctx: EclangParser.ExprAlignOfContext): ITok = TAlignOf(loc(ctx), visit(ctx.type()))
    override fun visitExprOffsetOf(ctx: EclangParser.ExprOffsetOfContext): ITok = TOffsetOf(loc(ctx), visit(ctx.type()), ctx.field.text)
    override fun visitExprHere(ctx: EclangParser.ExprHereContext): ITok = THere(loc(ctx))

    // Primitives
    override fun visitNumberInteger(ctx: EclangParser.NumberIntegerContext): ITok = TInteger(loc(ctx), number(ctx, ctx.text.replace("_", ""), 10))
    //override fun visitNumberLong(ctx: EclangParser.NumberLongContext): ITok = TLong(loc(ctx), ctx.text.replace("_", "").dropLast(1).toLong())
    //override fun visitNumberShort(ctx: EclangParser.NumberShortContext): ITok = TShort(loc(ctx), ctx.text.replace("_", "").dropLast(1).toShort())
    //override fun visitNumberChar(ctx: EclangParser.NumberCharContext): ITok = TCharNum(loc(ctx), ctx.text.replace("_", "").dropLast(1).toInt())

    //override fun visitNumberUInt(ctx: EclangParser.NumberUIntContext): ITok = TUnsignedInteger(loc(ctx), ctx.text.replace("_", "").replace("i", "").replace("u", "").toInt())
    //override fun visitNumberULong(ctx: EclangParser.NumberULongContext): ITok = TUnsignedLong(loc(ctx), ctx.text.replace("_", "").dropLast(2).toLong())
    //override fun visitNumberUShort(ctx: EclangParser.NumberUShortContext): ITok = TUnsignedShort(loc(ctx), ctx.text.replace("_", "").dropLast(2).toShort())
    //override fun visitNumberUChar(ctx: EclangParser.NumberUCharContext): ITok = TUnsignedCharNum(loc(ctx), ctx.text.replace("_", "").dropLast(2).toInt())

    override fun visitNumberBin(ctx: EclangParser.NumberBinContext): ITok = TInteger(loc(ctx), number(ctx, ctx.text.substring(2), 2))
    override fun visitNumberHex(ctx: EclangParser.NumberHexContext): ITok = TInteger(loc(ctx), number(ctx, ctx.text.substring(2), 16))
    override fun visitNumberFloating(ctx: EclangParser.NumberFloatingContext): ITok = TFloat(loc(ctx), ctx.text.replace("_", "").toDouble())

    /** Parses an integer literal; hex and binary literals may use all 64 bits (`0xFFFFFFFFFFFFFFFF`). */
    private fun number(ctx: ParserRuleContext, digits: String, radix: Int): Long = try {
        if (digits.isEmpty()) throw EcError(loc(ctx), "The number ${ctx.text} has no digits. Write them after the prefix, e.g. ${ctx.text}${if (radix == 16) "FF" else "101"}.")
        if (radix == 10) digits.toLong() else java.lang.Long.parseUnsignedLong(digits, radix)
    } catch (e: NumberFormatException) {
        throw EcError(loc(ctx), "The number ${ctx.text} is too big; the largest whole number is ${Long.MAX_VALUE}.")
    }
    //override fun visitNumberDouble(ctx: EclangParser.NumberDoubleContext): ITok = TDouble(loc(ctx), ctx.text.toDouble())

    override fun visitPrimitiveBool(ctx: EclangParser.PrimitiveBoolContext): ITok = TBoolean(loc(ctx), ctx.BOOL().text == "true")
    //override fun visitPrimitiveCString(ctx: EclangParser.PrimitiveCStringContext): ITok = TCString(loc(ctx), Util.getCStringContent(ctx.text) ?: "")
    override fun visitPrimitiveString(ctx: EclangParser.PrimitiveStringContext): ITok =
        if (ctx.text.endsWith('c')) TCString(loc(ctx), StringLiteral.parse(loc(ctx), Util.getCStringContent(ctx.text) ?: ""))
        else string(loc(ctx), Util.getStringContent(ctx.text) ?: "")

    /** A String literal; with `${expr}` in it, an interpolated one (which `format(pool, ...)` turns into a String). */
    private fun string(at: Loc, content: String): ITok {
        val parts = StringLiteral.splitInterpolation(at, content)
        if (parts.size == 1) return TString(at, StringLiteral.parse(at, content))
        return TInterpolated(at, parts.mapIndexed { i, part ->
            if (i % 2 == 0) StringLiteral.parse(at, part) else github.mrh0.eclang.Compiler.parseInterpolation(part, at)
        })
    }
    override fun visitPrimitiveAtom(ctx: EclangParser.PrimitiveAtomContext): ITok = TAtom(loc(ctx), ctx.text.substring(1).lowercase())
    override fun visitPrimitiveChar(ctx: EclangParser.PrimitiveCharContext): ITok = TChar(loc(ctx), ctx.text)

    override fun visitExprNull(ctx: EclangParser.ExprNullContext): ITok = TNull(loc(ctx))

    /** `Point` or `geo.Point` in `Point { ... }`. */
    private fun recordTypeName(qualifier: Token?, type: Token) = if (qualifier == null) type.text else "${qualifier.text}.${type.text}"

    override fun visitExprCreateRecord(ctx: EclangParser.ExprCreateRecordContext): ITok =
        if (ctx.recordType == null) TCreateRecord(loc(ctx), visit(ctx.expr())) else TCreateRecordTyped(loc(ctx), recordTypeName(ctx.qualifier, ctx.recordType), visit(ctx.expr()))
    override fun visitExprCreateRecordNamed(ctx: EclangParser.ExprCreateRecordNamedContext): ITok =
        if (ctx.recordType == null) TCreateRecord(loc(ctx), visit(ctx.expr()), tvisit(ctx.names)) else TCreateRecordTyped(loc(ctx), recordTypeName(ctx.qualifier, ctx.recordType), visit(ctx.expr()), tvisit(ctx.names))

    override fun visitExprCreateArray(ctx: EclangParser.ExprCreateArrayContext): ITok =
        if(ctx.text.endsWith('c')) TCreateCArray(loc(ctx), visit(ctx.expr())) else TCreateArray(loc(ctx), visit(ctx.expr()))
    //Natives
    // override fun visitExprTuple(ctx: EclangParser.ExprTupleContext): ITok = TTuple(loc(ctx), visit(ctx.values))

    // Ops
    override fun visitExprBinOp(ctx: EclangParser.ExprBinOpContext): ITok = when (ctx.op.text) {
        "+" -> TAdd(loc(ctx), visit(ctx.left), visit(ctx.right))
        "-" -> TSub(loc(ctx), visit(ctx.left), visit(ctx.right))
        "*" -> TMul(loc(ctx), visit(ctx.left), visit(ctx.right))
        "/" -> TDiv(loc(ctx), visit(ctx.left), visit(ctx.right))
        "**" -> TPow(loc(ctx), visit(ctx.left), visit(ctx.right))
        "%" -> TMod(loc(ctx), visit(ctx.left), visit(ctx.right))

        "==" -> TEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        "===" -> TEqualsEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        ">=" -> TGreaterOrEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        ">" -> TGreaterThan(loc(ctx), visit(ctx.left), visit(ctx.right))
        "<=" -> TLessOrEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        "<" -> TLessThan(loc(ctx), visit(ctx.left), visit(ctx.right))
        "!=" -> TNotEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        "!==" -> TNotEqualsEquals(loc(ctx), visit(ctx.left), visit(ctx.right))

        "??" -> TNullishCoalescing(loc(ctx), visit(ctx.left), visit(ctx.right))

        "<<", ">>", "&", "|", "^" -> TBitwise(loc(ctx), ctx.op.text, visit(ctx.left), visit(ctx.right))
        "..<", "..=" -> TRange(loc(ctx), visit(ctx.left), visit(ctx.right), inclusive = ctx.op.text == "..=")
        "..>" -> throw EcError(loc(ctx), "Range operator `..>` is not supported yet.")
        "&&", "and" -> TAnd(loc(ctx), visit(ctx.left), visit(ctx.right))
        "||", "or" -> TOr(loc(ctx), visit(ctx.left), visit(ctx.right))
        "^^", "xor" -> TXOr(loc(ctx), visit(ctx.left), visit(ctx.right))
        else -> throw NotImplementedError("Binary Operator '${ctx.op.text}' is not implemented.")
    }

    override fun visitExprUnOp(ctx: EclangParser.ExprUnOpContext): ITok = when (ctx.op.text) {
        "-" -> TNegate(loc(ctx), visit(ctx.expr()))
        "!!" -> TNotNot(loc(ctx), visit(ctx.expr()))
        "!", "not" -> TNot(loc(ctx), visit(ctx.expr()))
        "@" -> TReadPointer(loc(ctx), visit(ctx.expr()))
        "~" -> TBitwiseNot(loc(ctx), visit(ctx.expr()))
        else -> throw NotImplementedError("Unary Operator '${ctx.op.text}' is not implemented.")
    }

    override fun visitExprPrimitiveUnit(ctx: EclangParser.ExprPrimitiveUnitContext): ITok {
        // `5b` is a Char, but `5 b` is a mistake (a missing operator or comma), not a number with a suffix.
        if (ctx.primitive().stop.stopIndex + 1 != ctx.NAME().symbol.startIndex)
            throw EcError(loc(ctx), "`${source(ctx)}`: a type suffix is written right after the number, without a space (`${ctx.primitive().text}${ctx.NAME().text}`). " +
                "If '${ctx.NAME().text}' is a separate value, put an operator or a comma between them.")
        return TPrimitiveUnit(loc(ctx), visit(ctx.primitive()), ctx.NAME().text)
    }

    override fun visitExprAs(ctx: EclangParser.ExprAsContext): ITok = TCast(loc(ctx), visit(ctx.expr()), visit(ctx.type()))
    override fun visitExprAsUnsafe(ctx: EclangParser.ExprAsUnsafeContext): ITok = TCastUnsafe(loc(ctx), visit(ctx.expr()), visit(ctx.type()))
    override fun visitExprCastNotNull(ctx: EclangParser.ExprCastNotNullContext): ITok = TCastNotNull(loc(ctx), visit(ctx.expr()))
    override fun visitExprIs(ctx: EclangParser.ExprIsContext): ITok = TIs(loc(ctx), visit(ctx.expr()), visit(ctx.type()), false)
    override fun visitExprIsNot(ctx: EclangParser.ExprIsNotContext): ITok = TIs(loc(ctx), visit(ctx.expr()), visit(ctx.type()), true)
    override fun visitExprTry(ctx: EclangParser.ExprTryContext): ITok = TTry(loc(ctx), visit(ctx.expr()))
    override fun visitStatementTry(ctx: EclangParser.StatementTryContext): ITok = TStatement(loc(ctx), TTry(loc(ctx), visit(ctx.left)))
    override fun visitStatementTryCatch(ctx: EclangParser.StatementTryCatchContext): ITok = TStatementTryCatch(loc(ctx), visit(ctx.left), ctx.NAME().text, visit(ctx.elseBody))
    override fun visitGlobalInterfaceDefine(ctx: EclangParser.GlobalInterfaceDefineContext): ITok = TInterface(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types))
    // Variable
    //override fun visitGlobalDefine(ctx: EclangParser.GlobalDefineContext): ITok = TStatementDefine(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    //override fun visitGlobalDefineConst(ctx: EclangParser.GlobalDefineConstContext): ITok = TStatementDefineConst(loc(ctx), ctx.NAME().text, visit(ctx.expr()))

    override fun visitGlobalDefine(ctx: EclangParser.GlobalDefineContext): ITok = TGlobalDefine(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitGlobalDefineConst(ctx: EclangParser.GlobalDefineConstContext): ITok = TGlobalDefineConst(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitGlobalDeclareConst(ctx: EclangParser.GlobalDeclareConstContext): ITok = TGlobalDeclareConst(loc(ctx), ctx.NAME().text, visit(ctx.type()), if (ctx.externalName != null) Util.getStringContent(ctx.externalName.text) else null)
    override fun visitGlobalDefineTyped(ctx: EclangParser.GlobalDefineTypedContext): ITok = TGlobalDefineTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    override fun visitGlobalDefineConstTyped(ctx: EclangParser.GlobalDefineConstTypedContext): ITok = TGlobalDefineConstTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    override fun visitGlobalConstant(ctx: EclangParser.GlobalConstantContext): ITok = TGlobalConstant(loc(ctx), ctx.NAME().text, visit(ctx.expr()), ctx.type()?.let { visit(it) })
    override fun visitGlobalTypeDefine(ctx: EclangParser.GlobalTypeDefineContext): ITok = TGlobalTypeDefine(loc(ctx), ctx.NAME().text, visit(ctx.type()))
    override fun visitGlobalEnumDeclare(ctx: EclangParser.GlobalEnumDeclareContext): ITok =
        TGlobalEnumDeclare(loc(ctx), ctx.name.text, ctx.atoms.map { it.text }, ctx.constants.map { it.text })
    override fun visitUse(ctx: EclangParser.UseContext): ITok = TGlobalUse(loc(ctx), Util.getStringContent(ctx.from.text) ?: "")
    override fun visitGlobalNamespace(ctx: EclangParser.GlobalNamespaceContext): ITok = TGlobalModule(loc(ctx), Util.getStringContent(ctx.name.text) ?: "")

    override fun visitStatementDefineVar(ctx: EclangParser.StatementDefineVarContext): ITok = TStatementDefineVar(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitStatementDefineVarTyped(ctx: EclangParser.StatementDefineVarTypedContext): ITok = TStatementDefineVarTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))

    override fun visitStatementDeclareVar(ctx: EclangParser.StatementDeclareVarContext): ITok = TStatementDeclareVar(loc(ctx), ctx.NAME().text, visit(ctx.type()))

    override fun visitStatementDefineVal(ctx: EclangParser.StatementDefineValContext): ITok = TStatementDefineVal(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitStatementDefineValTyped(ctx: EclangParser.StatementDefineValTypedContext): ITok = TStatementDefineValTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))

    override fun visitStatementDefineConst(ctx: EclangParser.StatementDefineConstContext): ITok = TStatementDefineConst(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitStatementDefineConstTyped(ctx: EclangParser.StatementDefineConstTypedContext): ITok = TStatementDefineConstTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))

    override fun visitStatementAssignment(ctx: EclangParser.StatementAssignmentContext): ITok {
        val target: ITok = visit(ctx.target)
        (target.unnested() as? TNamed)?.let { assignedNames += it.name }
        return TStatementAssign(loc(ctx), target, ctx.op.text.dropLast(1), visit(ctx.value))
    }
    override fun visitExprNamed(ctx: EclangParser.ExprNamedContext): ITok = TNamed(loc(ctx), ctx.NAME().text)

    override fun visitStatementTailIf(ctx: EclangParser.StatementTailIfContext): ITok = TTailIf(loc(ctx), TStatement(loc(ctx), visit(ctx.expression)), if (ctx.condition != null) visit(ctx.condition) else null)

    // Branches
    override fun visitStatementIf(ctx: EclangParser.StatementIfContext): ITok {
        return TStatementIf(loc(ctx), visit(ctx.conditions), visit(ctx.bodies), if(ctx.elseBody == null) null else visit(ctx.elseBody))
    }

    override fun visitExprInlineIf(ctx: EclangParser.ExprInlineIfContext): ITok = TInlineIf(loc(ctx), visit(ctx.condition), visit(ctx.body), visit(ctx.elseBody))
    override fun visitExprInlineIfOld(ctx: EclangParser.ExprInlineIfOldContext): ITok =
        throw EcError(loc(ctx), "An inline `if` is written like a statement `if`, with `do`: `if ${source(ctx.condition)} do ${source(ctx.body)} else ${source(ctx.elseBody)}`.")

    // Loops
    override fun visitStatementWhile(ctx: EclangParser.StatementWhileContext): ITok {
        val condition = visit(ctx.condition)
        val (body, assigned) = assignedIn { visit(ctx.body) }
        return TStatementWhile(loc(ctx), condition, body, ctx.elseBody?.let { visit(it) }, assigned)
    }

    override fun visitStatementForInRange(ctx: EclangParser.StatementForInRangeContext): ITok =
        throw EcError(loc(ctx), "`a to b` is written as a range: `for ${ctx.NAME().text} in ${source(ctx.lower)}..<${source(ctx.upper)} do` stops before ${source(ctx.upper)}, " +
            "and `${source(ctx.lower)}..=${source(ctx.upper)}` includes it.")

    override fun visitStatementForIn(ctx: EclangParser.StatementForInContext): ITok {
        val iterable = visit(ctx.iterable)
        val filter: ITok? = ctx.filter?.let { visit(it) }
        val (body, assigned) = assignedIn { visit(ctx.body) }
        return TStatementForeachIn(loc(ctx), ctx.index?.text, ctx.name.text, iterable, filter, body, ctx.elseBody?.let { visit(it) }, assigned)
    }

    override fun visitStatementContinue(ctx: EclangParser.StatementContinueContext): ITok = TTailIf(loc(ctx), TStatementContinue(loc(ctx)), if (ctx.condition != null) visit(ctx.condition) else null)
    override fun visitStatementBreak(ctx: EclangParser.StatementBreakContext): ITok = TTailIf(loc(ctx), TStatementBreak(loc(ctx)), if (ctx.condition != null) visit(ctx.condition) else null)
    override fun visitStatementPass(ctx: EclangParser.StatementPassContext): ITok = TTailIf(loc(ctx), TPass(loc(ctx)), if (ctx.condition != null) visit(ctx.condition) else null)
    override fun visitStatementThrow(ctx: EclangParser.StatementThrowContext): ITok = TTailIf(loc(ctx), TStatementThrow(loc(ctx), visit(ctx.throw_)), if (ctx.condition != null) visit(ctx.condition) else null)
    override fun visitStatementReturn(ctx: EclangParser.StatementReturnContext): ITok = TTailIf(loc(ctx), TStatementReturn(loc(ctx), visit(ctx.return_)), if (ctx.condition != null) visit(ctx.condition) else null)

    // Volatile variables (`vol`) and C variables (`declare var`)
    override fun visitStatementDefineVol(ctx: EclangParser.StatementDefineVolContext): ITok = TStatementDefineVar(loc(ctx), ctx.NAME().text, visit(ctx.expr()), volatile = true)
    override fun visitStatementDefineVolTyped(ctx: EclangParser.StatementDefineVolTypedContext): ITok = TStatementDefineVarTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()), volatile = true)
    override fun visitGlobalDefineVolatile(ctx: EclangParser.GlobalDefineVolatileContext): ITok = TGlobalDefine(loc(ctx), ctx.NAME().text, visit(ctx.expr()), volatile = true)
    override fun visitGlobalDefineVolatileTyped(ctx: EclangParser.GlobalDefineVolatileTypedContext): ITok = TGlobalDefineTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()), volatile = true)
    override fun visitGlobalDeclareDefine(ctx: EclangParser.GlobalDeclareDefineContext): ITok = TGlobalDeclareConst(loc(ctx), ctx.name.text, visit(ctx.type()), Util.getStringContent(ctx.externalName?.text), assignable = true)

    override fun visitStatementAssert(ctx: EclangParser.StatementAssertContext): ITok {
        val message = ctx.message?.let {
            if (it.text.endsWith("\"c")) throw EcError(loc(ctx), "The message of `assert` is a plain string literal; remove the `c`.")
            val content = Util.getStringContent(it.text) ?: ""
            if (StringLiteral.splitInterpolation(loc(ctx), content).size > 1) throw EcError(loc(ctx), "The message of `assert` can't contain `\${...}`; write `\\$` for a plain dollar sign.")
            StringLiteral.parse(loc(ctx), content)
        }
        return TAssert(loc(ctx), visit(ctx.condition), message, source(ctx.condition))
    }

    override fun visitStatementLetIf(ctx: EclangParser.StatementLetIfContext): ITok =
        TStatementLetIf(loc(ctx), ctx.NAME().text, ctx.kind.text == "var", visit(ctx.value), visit(ctx.body), ctx.elseBody?.let { visit(it) })

    // Pipes and lambdas
    override fun visitExprIt(ctx: EclangParser.ExprItContext): ITok {
        if (itUses.isEmpty()) throw EcError(loc(ctx), "`it` can only be used in a `|>` stage, where it is the value before the `|>`, e.g. `x |> it * 2`.")
        itUses[itUses.size - 1]++
        return TIt(loc(ctx))
    }

    override fun visitExprPipe(ctx: EclangParser.ExprPipeContext): ITok {
        val value: ITok = visit(ctx.left)
        itUses += 0
        val tok: ITok = visit(ctx.right)
        val uses = itUses.removeAt(itUses.size - 1)
        if (uses > 0) return TPipe(loc(ctx), value, tok, uses)
        // Without `it`, the value goes in front: `x |> f(2)` is `f(x, 2)` and `x |> f` is `f(x)`.
        val at = loc(ctx.right)
        val stage = when (tok) {
            is TExprCall -> if (tok.isMethod) {
                val withIt = source(ctx.right).removeSuffix(")") + (if (tok.args.size > 1) ", it)" else "it)")
                throw EcError(at, "In `|> ${source(ctx.right)}`, say where the value goes with `it`, e.g. `|> $withIt`.")
            } else TExprCall(at, tok.name, listOf(TIt(at)) + tok.args)
            is TNamed -> TExprCall(at, tok.name, listOf(TIt(at)))
            is TLambda -> TCallValue(at, tok, listOf(TIt(at)))
            else -> throw EcError(at, "A `|>` stage must be a call (`x |> f(2)` is `f(x, 2)`), a function name (`x |> f`), a lambda, " +
                "or use `it` (`x |> it * 2`), but `${source(ctx.right)}` is none of these.")
        }
        return TPipe(loc(ctx), value, stage, 1)
    }

    override fun visitExprLambda(ctx: EclangParser.ExprLambdaContext): ITok {
        // `it` inside a lambda would refer to a value of the function around it, which a lambda can't use.
        val saved = ArrayList(itUses)
        itUses.clear()
        try {
            val params = ctx.params.map { p -> p.NAME().text to p.type()?.let { visit(it) as ITok } }
            return TLambda(loc(ctx), params, ctx.returnType?.let { visit(it) }, visit(ctx.body))
        } finally {
            itUses.clear(); itUses += saved
        }
    }

    // Parsed, but not part of the language yet: a clear error instead of a crash.
    private fun unsupported(ctx: ParserRuleContext, what: String): Nothing = throw EcError(loc(ctx), "$what is not supported yet.")
    override fun visitExprAccessNameNullishCoalescing(ctx: EclangParser.ExprAccessNameNullishCoalescingContext): ITok = TAccessorNamed(loc(ctx), visit(ctx.accesssed), ctx.NAME().text, safe = true)
    override fun visitGlobalUnitDeclare(ctx: EclangParser.GlobalUnitDeclareContext): ITok = unsupported(ctx, "`unit`")
    override fun visitStatementTailWhile(ctx: EclangParser.StatementTailWhileContext): ITok = unsupported(ctx, "A trailing `while`")
    override fun visitStatementYield(ctx: EclangParser.StatementYieldContext): ITok = unsupported(ctx, "`yield`")
    override fun visitTypeByNameParameters(ctx: EclangParser.TypeByNameParametersContext): ITok = unsupported(ctx, "Type parameters `Name<T>`")
    override fun visitTypeUnionAnon(ctx: EclangParser.TypeUnionAnonContext): ITok = unsupported(ctx, "`anon` unions")
    override fun visitParameterVariable(ctx: EclangParser.ParameterVariableContext): ITok = unsupported(ctx, "A `var` parameter")
    override fun visitParameterConst(ctx: EclangParser.ParameterConstContext): ITok = unsupported(ctx, "A `val` parameter")
    override fun visitParameterVolatile(ctx: EclangParser.ParameterVolatileContext): ITok = unsupported(ctx, "A `vol` parameter")
    override fun visitStatementDefer(ctx: EclangParser.StatementDeferContext): ITok = TDefer(loc(ctx), visit(ctx.statement()))
    override fun visitStatementDeferDo(ctx: EclangParser.StatementDeferDoContext): ITok = TDefer(loc(ctx), visit(ctx.block()))

    override fun visitExprNew(ctx: EclangParser.ExprNewContext): ITok = TNew(loc(ctx), visit(ctx.value), ctx.pool?.let { visit(it) })

    /** `pool do` / `pool name do`. `pool` isn't a keyword (it stays usable as a name), so other words are rejected here. */
    override fun visitStatementPool(ctx: EclangParser.StatementPoolContext): ITok {
        if (ctx.keyword.text != "pool") throw EcError(loc(ctx), "`${ctx.keyword.text}${ctx.name?.let { " " + it.text } ?: ""} do` isn't a statement. A block that starts with a name is a pool block: `pool do` (or `pool name do`).")
        return TStatementPool(loc(ctx), ctx.name?.text, visit(ctx.body))
    }
}