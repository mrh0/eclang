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
import github.mrh0.eclang.ast.token.branch.TStatementWhen
import github.mrh0.eclang.ast.token.branch.WhenArm
import github.mrh0.eclang.ast.token.branch.TStatementLetIf
import github.mrh0.eclang.ast.token.branch.TWhenExpr
import github.mrh0.eclang.ast.token.branch.TTailIf
import github.mrh0.eclang.ast.token.data.*
import github.mrh0.eclang.ast.token.data.record.TCreateRecord
import github.mrh0.eclang.ast.token.data.record.TCreateRecordTyped
import github.mrh0.eclang.ast.token.data.record.TUpdateRecord
import github.mrh0.eclang.ast.token.data.record.THere
import github.mrh0.eclang.ast.token.data.unit.TPrimitiveUnit
import github.mrh0.eclang.ast.token.type.TLayout
import github.mrh0.eclang.ast.token.type.TTypeFixedArray
import github.mrh0.eclang.ast.token.type.TTypeRecord
import github.mrh0.eclang.ast.token.function.*
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.ast.token.function.call.TSafeCall
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
import github.mrh0.eclang.util.FormatSpec
import github.mrh0.eclang.util.StringLiteral
import java.io.File

/** Builds the tokens of [file]. With [fixed], every token gets that location (an expression inside a string's `${...}`). */
class Visitor(private val file: File, private val fixed: Loc? = null) : EclangBaseVisitor<ITok>() {
    /** The name of every assignment visited so far, for [assignedIn]. */
    private val assignedNames = mutableListOf<String>()

    /** Every local whose address is taken (`addrof x`, `addrof x.f`) so far, for [addressTakenIn]. */
    private val addressTakenNames = mutableListOf<Pair<String, Loc>>()

    /** Runs [visit] (a function) and gives the function the names whose address its body takes. */
    private fun <T : TFunc> addressTakenIn(visit: () -> T): T {
        val start = addressTakenNames.size
        val func = visit()
        func.addressTaken = addressTakenNames.subList(start, addressTakenNames.size).reversed().toMap()
        return func
    }

    /** The variable whose memory `addrof` of [place] points into: x for `x`, `x!`, `x.f` (an array's elements aren't in x). */
    private fun addressedName(place: ITok): String? = when (val t = place.unnested()) {
        is TNamed -> t.name
        is TCastNotNull -> addressedName(t.expr)
        is TAccessorNamed -> addressedName(t.expr)
        else -> null
    }

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
    override fun visitProgram(ctx: EclangParser.ProgramContext): ITok {
        val functions: MutableList<TFunc> = visit(ctx.functions)
        val globals: MutableList<ITok> = visit(ctx.globals)
        val privates = mutableListOf<TProgram.Private>()
        for (p in ctx.privates) when (p) {
            is EclangParser.PrivateFunctionContext -> {
                val f = visit(p.func()) as TFunc
                if (f is TFuncExternal) throw EcError(loc(p), "`priv` can't be put before `declare fn`: a C function is visible to all C code anyway.")
                functions += f
                privates += TProgram.Private(f, "fn", f.name)
            }
            is EclangParser.PrivateGlobalContext -> {
                val g = p.global()
                val (kind, name) = when (g) {
                    is EclangParser.GlobalDefineContext, is EclangParser.GlobalDefineConstContext, is EclangParser.GlobalDefineVolatileContext,
                    is EclangParser.GlobalDefineTypedContext, is EclangParser.GlobalDefineConstTypedContext, is EclangParser.GlobalDefineVolatileTypedContext,
                    is EclangParser.GlobalConstantContext -> "value" to g.getToken(EclangParser.NAME, 0).text
                    is EclangParser.GlobalRecordDefineContext -> "type" to g.name.text
                    is EclangParser.GlobalUnionDefineContext -> "type" to g.name.text
                    is EclangParser.GlobalInterfaceDefineContext -> "type" to g.name.text
                    else -> throw EcError(loc(p), "`priv` can be put before `fn`, `val`, `var`, `vol`, `const`, `rec` and `inter`.")
                }
                val tok: ITok = visit(g)
                globals += tok
                privates += TProgram.Private(tok, kind, name)
            }
        }
        for (e in ctx.exports) {
            if (e.keyword.text != "export") throw EcError(loc(e), "`${e.keyword.text}` can't go before a function. To let C code call it, write `export fn`.")
            val f = visit(e.func()) as TFunc
            f.exportName = Util.getStringContent(e.externalName?.text) ?: Util.testIdentifier(loc(e), f.name)
            functions += f
        }
        return TProgram(loc(ctx), functions, visit(ctx.uses), globals, privates)
    }

    /** The widths of the bit-fields among [widths]: `: 3`, or `:3`, which the lexer reads as an atom. */
    private fun widths(widths: List<EclangParser.FieldWidthContext>): List<ITok?> = widths.map { w ->
        val atom = w.atomWidth?.text?.drop(1)
        when {
            w.width != null -> visit(w.width)
            atom == null -> null
            atom[0].isDigit() -> TInteger(loc(w), number(w, atom.replace("_", ""), 10))
            else -> TNamed(loc(w), atom)
        }
    }

    /** `packed` and `align(n)` before `rec` and `union`. */
    private fun layout(modifiers: List<EclangParser.RecordModifierContext>, isUnion: Boolean = false): TLayout {
        val words = modifiers.map { it.name.text }
        words.groupBy { it }.filter { it.value.size > 1 }.keys.firstOrNull()?.let { throw EcError(loc(modifiers[0]), "`$it` is written twice.") }
        for (m in modifiers) when (m.name.text) {
            "packed" -> if (m.size != null) throw EcError(loc(m), "`packed` takes no value. To set an alignment, write `align(n)`.")
            "align" -> if (m.size == null) throw EcError(loc(m), "`align` needs the alignment, such as `align(16)`.")
            else -> throw EcError(loc(m), "`${m.name.text}` can't go before `rec` or `union`. These can: `packed`, and `align(n)`.")
        }
        return TLayout("packed" in words, modifiers.firstOrNull { it.name.text == "align" }?.let { visit(it.size) }, isUnion)
    }

    private fun checkUnion(ctx: ParserRuleContext, word: Token) {
        if (word.text != "union") throw EcError(loc(ctx), "`${word.text}` isn't a kind of declaration. A type is a `rec`, `union` or `inter`.")
    }

    // Use
    // override fun visitUseFromModule(ctx: EclangParser.UseFromModuleContext): ITok {
    //     return TUseFromModule(loc(ctx), ctx.exports.map { it.text }, ctx.from.text.substring(1, ctx.from.text.length-1))
    // }

    // override fun visitUseAllFromModule(ctx: EclangParser.UseAllFromModuleContext): ITok {
    //     return TUseAllFromModule(loc(ctx), ctx.from.text.substring(1, ctx.from.text.length-1))
    // }

    // Records
    override fun visitGlobalRecordDefine(ctx: EclangParser.GlobalRecordDefineContext): ITok =
        TTypeRecord(loc(ctx), ctx.name.text, ctx.fields.map { it.name.text }, ctx.fields.map { visit(it.type()) }, ctx.extending?.let { recordTypeName(ctx.qualifier, it) }, ctx.fields.map { f -> f.value?.let { visit(it) } }, ctx.typeParams.map { it.text },
            layout(ctx.modifiers), widths(ctx.fields.map { it.fieldWidth() }))
    override fun visitGlobalUnionDefine(ctx: EclangParser.GlobalUnionDefineContext): ITok {
        checkUnion(ctx, ctx.keyword)
        return TTypeRecord(loc(ctx), ctx.name.text, ctx.fields.map { it.name.text }, ctx.fields.map { visit(it.type()) }, null, ctx.fields.map { f -> f.value?.let { visit(it) } }, listOf(),
            layout(ctx.modifiers, isUnion = true), widths(ctx.fields.map { it.fieldWidth() }))
    }
    override fun visitGlobalUnionDeclare(ctx: EclangParser.GlobalUnionDeclareContext): ITok {
        checkUnion(ctx, ctx.keyword)
        val external = Util.getStringContent(ctx.externalName?.text)
        return if (ctx.typedef != null) TTypeDeclareRecord(loc(ctx), ctx.name.text, listOf(), listOf(), null, external, isUnion = true)
            else TTypeDeclareStruct(loc(ctx), ctx.name.text, listOf(), listOf(), external, isUnion = true)
    }
    override fun visitGlobalUnionDeclareDefine(ctx: EclangParser.GlobalUnionDeclareDefineContext): ITok {
        checkUnion(ctx, ctx.keyword)
        val external = Util.getStringContent(ctx.externalName?.text)
        return if (ctx.typedef != null) TTypeDeclareRecord(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), null, external, widths(ctx.widths), isUnion = true)
            else TTypeDeclareStruct(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), external, widths(ctx.widths), isUnion = true)
    }
    override fun visitGlobalRecordDeclare(ctx: EclangParser.GlobalRecordDeclareContext): ITok = TTypeDeclareRecord(loc(ctx), ctx.name.text, listOf(), listOf(), null, Util.getStringContent(ctx.externalName?.text))
    override fun visitGlobalRecordDeclareDefine(ctx: EclangParser.GlobalRecordDeclareDefineContext): ITok = TTypeDeclareRecord(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), null, Util.getStringContent(ctx.externalName?.text), widths(ctx.widths))

    override fun visitGlobalStructDeclare(ctx: EclangParser.GlobalStructDeclareContext): ITok  = TTypeDeclareStruct(loc(ctx), ctx.name.text, listOf(), listOf(), Util.getStringContent(ctx.externalName?.text))
    override fun visitGlobalStructDeclareDefine(ctx: EclangParser.GlobalStructDeclareDefineContext): ITok  = TTypeDeclareStruct(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), Util.getStringContent(ctx.externalName?.text), widths(ctx.widths))

    // Types
    override fun visitTypeByName(ctx: EclangParser.TypeByNameContext): ITok = TTypeByName(loc(ctx), ctx.text)

    override fun visitTypeByNameParameters(ctx: EclangParser.TypeByNameParametersContext): ITok =
        TTypeApplied(loc(ctx), recordTypeName(ctx.qualifier, ctx.name), typeArgs(ctx.args))

    /** The type arguments of `List<T>`: a bare T, K or V2 there may be a type parameter (`<<T>>` would lex as a shift). */
    private fun typeArgs(args: List<EclangParser.TypeContext>): List<ITok> = args.map {
        if (it is EclangParser.TypeByNameContext && TTypeArgument.isShort(it.text)) TTypeArgument(loc(it), it.text) else visit(it)
    }

    override fun visitTypeAddressByName(ctx: EclangParser.TypeAddressByNameContext): ITok = TTypePointerByName(loc(ctx), visit(ctx.type()))

    override fun visitTypeUnion(ctx: EclangParser.TypeUnionContext): ITok = TTypeUnion(loc(ctx), visit(ctx.types))

    override fun visitTypeEnum(ctx: EclangParser.TypeEnumContext): ITok = TTypeEnum(loc(ctx), visit(ctx.types))

    override fun visitTypeNullable(ctx: EclangParser.TypeNullableContext): ITok = TTypeNullable(loc(ctx), visit(ctx.type()))

    override fun visitTypeAtom(ctx: EclangParser.TypeAtomContext): ITok = TTypeAtom(loc(ctx), ctx.text)

    override fun visitTypeNest(ctx: EclangParser.TypeNestContext): ITok = visit(ctx.type())
    override fun visitTypeTuple(ctx: EclangParser.TypeTupleContext): ITok = TTypeTuple(loc(ctx), visit(ctx.types))

    override fun visitTypeGeneric(ctx: EclangParser.TypeGenericContext): ITok = TTypeGeneric(loc(ctx), ctx.NAME().text)

    override fun visitTypeTypeOf(ctx: EclangParser.TypeTypeOfContext): ITok = TTypeOf(loc(ctx), visit(ctx.expr()))

    override fun visitTypeArray(ctx: EclangParser.TypeArrayContext): ITok =
        if(ctx.text.endsWith('c')) TTypeCArray(loc(ctx), visit(ctx.type())) else TTypeArray(loc(ctx), visit(ctx.type()))
    override fun visitTypeFixedArray(ctx: EclangParser.TypeFixedArrayContext): ITok = TTypeFixedArray(loc(ctx), visit(ctx.type()), visit(ctx.size))

    override fun visitTypeThrows(ctx: EclangParser.TypeThrowsContext): ITok = TTypeThrows(loc(ctx), visit(ctx.left), visit(ctx.throwing))

    override fun visitTypeCallSignature(ctx: EclangParser.TypeCallSignatureContext): ITok = TTypeCallSignature(loc(ctx), visit(ctx.argTypes), visit(ctx.returnType))

    // Functions
    override fun visitFunctionBlock(ctx: EclangParser.FunctionBlockContext): ITok {
        return addressTakenIn { TFuncBlock(loc(ctx), cvisit(ctx.body), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), returns(ctx, ctx.returnType, ctx.throwing)) }
    }

    override fun visitFunctionInline(ctx: EclangParser.FunctionInlineContext): ITok {
        return addressTakenIn { TFuncInline(loc(ctx), cvisit(ctx.tail()), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), returns(ctx, ctx.returnType, ctx.throwing)) }
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
    override fun visitExprFunctionCallWithArgs(ctx: EclangParser.ExprFunctionCallWithArgsContext): ITok {
        val (rest, names) = arguments(ctx.rest)
        return TExprCall(loc(ctx), ctx.NAME().text, poolKill(ctx.NAME().text, (visit<_, ITok>(ctx.args) + rest).toMutableList()), isMethod(ctx), names)
    }

    override fun visitStatementFunctionCallNoArgs(ctx: EclangParser.StatementFunctionCallNoArgsContext): ITok = TStatementCall(loc(ctx), ctx.NAME().text, visit(ctx.args), isMethod(ctx))
    override fun visitStatementFunctionCallWithArgs(ctx: EclangParser.StatementFunctionCallWithArgsContext): ITok {
        val (rest, names) = arguments(ctx.rest)
        return TStatementCall(loc(ctx), ctx.NAME().text, poolKill(ctx.NAME().text, (visit<_, ITok>(ctx.args) + rest).toMutableList()), isMethod(ctx), names)
    }

    /** The arguments of a call, and the parameter each is for (null when none has a name; see `TExprCall.argNames`). */
    private fun arguments(list: List<EclangParser.ArgumentContext>): Pair<List<ITok>, List<String?>?> {
        val names = list.map { it.name?.text }
        list.zipWithNext().firstOrNull { (a, b) -> a.name != null && b.name == null }?.let { (a, b) ->
            throw EcError(loc(b), "`${source(b)}` comes after the named argument `${source(a)}`. Arguments in order come first, then the named ones.")
        }
        names.filterNotNull().groupBy { it }.values.firstOrNull { it.size > 1 }?.let { throw EcError(loc(list[0]), "Parameter '${it[0]}' is given twice.") }
        return list.map { visit(it.expr()) as ITok } to names.takeIf { n -> n.any { it != null } }
    }

    override fun visitExprSafeCall(ctx: EclangParser.ExprSafeCallContext): ITok {
        val (rest, names) = arguments(ctx.rest)
        return TSafeCall(loc(ctx), visit(ctx.args[0]), ctx.NAME().text, rest, names)
    }

    override fun visitStatementSafeCall(ctx: EclangParser.StatementSafeCallContext): ITok {
        val (rest, names) = arguments(ctx.rest)
        return TSafeCall(loc(ctx), visit(ctx.args[0]), ctx.NAME().text, rest, names, statement = true)
    }

    /** `clearPool(p)` and `freePool(p)` free what p holds, which a loop must know like an assignment of p (see `Lifetimes.loopKills`). */
    private fun poolKill(name: String, args: MutableList<ITok>): MutableList<ITok> {
        if (name == "clearPool" || name == "freePool") (args.firstOrNull()?.unnested() as? TNamed)?.let { assignedNames += it.name }
        return args
    }

    // Expressions
    override fun visitExprNest(ctx: EclangParser.ExprNestContext): ITok =
        if (ctx.expr().size == 1) TExprNest(loc(ctx), visit(ctx.expr(0))) else TCreateTuple(loc(ctx), visit(ctx.expr()))

    override fun visitExprAccessName(ctx: EclangParser.ExprAccessNameContext): ITok = TAccessorNamed(loc(ctx), visit(ctx.accesssed), ctx.NAME().text)
    override fun visitExprAccessor(ctx: EclangParser.ExprAccessorContext): ITok = TAccessor(loc(ctx), visit(ctx.accesssed), visit(ctx.index))
    override fun visitExprSliceOpen(ctx: EclangParser.ExprSliceOpenContext): ITok =
        TAccessor(loc(ctx), visit(ctx.accesssed), TRange(loc(ctx), ctx.from?.let { visit(it) }, ctx.to?.let { visit(it) }, ctx.op?.text == "..="))

    override fun visitExprAddressOf(ctx: EclangParser.ExprAddressOfContext): ITok {
        val place: ITok = visit(ctx.expr())
        addressedName(place)?.let { addressTakenNames += it to loc(ctx) }
        return TAddressOf(loc(ctx), place)
    }
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
        return TInterpolated(at, parts.flatMapIndexed { i, part ->
            if (i % 2 == 0) return@flatMapIndexed listOf(StringLiteral.parse(at, part))
            val field = FormatSpec.split(at, part)
            listOfNotNull(field.label?.let { StringLiteral.parse(at, it) }, TInterpolated.Field(github.mrh0.eclang.Compiler.parseInterpolation(field.expression, at), field.spec))
        })
    }
    override fun visitPrimitiveAtom(ctx: EclangParser.PrimitiveAtomContext): ITok = TAtom(loc(ctx), ctx.text.substring(1))
    override fun visitPrimitiveChar(ctx: EclangParser.PrimitiveCharContext): ITok = TChar(loc(ctx), ctx.text)

    override fun visitExprNull(ctx: EclangParser.ExprNullContext): ITok = TNull(loc(ctx))

    /** `Point` or `geo.Point` in `Point { ... }`. */
    private fun recordTypeName(qualifier: Token?, type: Token) = if (qualifier == null) type.text else "${qualifier.text}.${type.text}"

    override fun visitExprCreateRecord(ctx: EclangParser.ExprCreateRecordContext): ITok =
        if (ctx.recordType == null) TCreateRecord(loc(ctx), visit(ctx.expr())) else TCreateRecordTyped(loc(ctx), recordTypeName(ctx.qualifier, ctx.recordType), visit(ctx.expr()), null, typeArgs(ctx.typeArgs))
    override fun visitExprCreateRecordNamed(ctx: EclangParser.ExprCreateRecordNamedContext): ITok =
        if (ctx.recordType == null) TCreateRecord(loc(ctx), visit(ctx.expr()), tvisit(ctx.names)) else TCreateRecordTyped(loc(ctx), recordTypeName(ctx.qualifier, ctx.recordType), visit(ctx.expr()), tvisit(ctx.names), typeArgs(ctx.typeArgs))

    override fun visitExprCreateArray(ctx: EclangParser.ExprCreateArrayContext): ITok =
        if(ctx.text.endsWith('c')) TCreateCArray(loc(ctx), visit(ctx.expr())) else TCreateArray(loc(ctx), visit(ctx.expr()))
    override fun visitExprUpdateRecord(ctx: EclangParser.ExprUpdateRecordContext): ITok {
        val named = ctx.recordType?.let { TCreateRecordTyped(loc(ctx), recordTypeName(ctx.qualifier, it), listOf(), null, typeArgs(ctx.typeArgs)) }
        return TUpdateRecord(loc(ctx), named, visit(ctx.base), visit(ctx.values), tvisit(ctx.names))
    }

    //Natives
    // override fun visitExprTuple(ctx: EclangParser.ExprTupleContext): ITok = TTuple(loc(ctx), visit(ctx.values))

    // Ops
    override fun visitExprBinOp(ctx: EclangParser.ExprBinOpContext): ITok = if (isOrdering(ctx) && isOrdering(ctx.left)) chain(ctx) else binaryOp(ctx)

    /** True for `a < b`, `a <= b`, `a > b` and `a >= b` (not in parentheses, which are another context). */
    private fun isOrdering(ctx: ParserRuleContext) = ctx is EclangParser.ExprBinOpContext && ctx.shr == null && ctx.op.text in setOf("<", "<=", ">", ">=")

    private fun ordering(op: String, at: Loc, left: ITok, right: ITok): ITok = when (op) {
        "<" -> TLessThan(at, left, right)
        "<=" -> TLessOrEquals(at, left, right)
        ">" -> TGreaterThan(at, left, right)
        else -> TGreaterOrEquals(at, left, right)
    }

    /** `a < b <= c` is `a < b and b <= c`, with b evaluated once: a variable or a literal is just read twice. */
    private fun chain(ctx: EclangParser.ExprBinOpContext): ITok {
        val links = generateSequence(ctx) { it.left as? EclangParser.ExprBinOpContext }.takeWhile { isOrdering(it) }.toList().reversed()
        val at = loc(ctx)
        var left: ITok = visit(links.first().left)
        val checks = links.mapIndexed { i, link ->
            val right: ITok = visit(link.right)
            val readTwice = i == links.lastIndex || right.unnested().let { it is TNamed || it is TInteger || it is TFloat || it is TChar }
            if (readTwice) ordering(link.op.text, at, left, right).also { left = right }
            else TChainFirst(at, right).let { first -> ordering(link.op.text, at, left, first).also { left = first.Again(at) } }
        }
        return TExprNest(at, checks.reduce { a, b -> TAnd(at, a, b) })
    }

    override fun visitExprNullishJump(ctx: EclangParser.ExprNullishJumpContext): ITok = TNullishJump(loc(ctx), visit(ctx.left), visit(ctx.jump()))
    override fun visitJumpReturn(ctx: EclangParser.JumpReturnContext): ITok = TStatementReturn(loc(ctx), ctx.value?.let { visit(it) } ?: TNoValue(loc(ctx)))
    override fun visitJumpThrow(ctx: EclangParser.JumpThrowContext): ITok = TStatementThrow(loc(ctx), visit(ctx.value))
    override fun visitJumpBreak(ctx: EclangParser.JumpBreakContext): ITok = TStatementBreak(loc(ctx), ctx.label?.text)
    override fun visitJumpContinue(ctx: EclangParser.JumpContinueContext): ITok = TStatementContinue(loc(ctx), ctx.label?.text)

    private fun binaryOp(ctx: EclangParser.ExprBinOpContext): ITok = when (if (ctx.shr != null) ">>" else ctx.op.text) {
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

        ">>" -> {
            if (ctx.op.stopIndex + 1 != ctx.shr.startIndex) throw EcError(loc(ctx), "The shift `>>` is written without a space between the `>`s.")
            TBitwise(loc(ctx), ">>", visit(ctx.left), visit(ctx.right))
        }
        "<<", "&", "|", "^" -> TBitwise(loc(ctx), ctx.op.text, visit(ctx.left), visit(ctx.right))
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
    override fun visitGlobalInterfaceDefine(ctx: EclangParser.GlobalInterfaceDefineContext): ITok {
        val fields = ctx.members.filterIsInstance<EclangParser.InterfaceFieldContext>()
        val methods = ctx.members.filterIsInstance<EclangParser.InterfaceMethodContext>().map { m ->
            TInterface.MethodDecl(loc(m), m.name.text, m.params.map { visit(it) }, returns(m, m.returnType, m.throwing))
        }
        return TInterface(loc(ctx), ctx.name.text, fields.map { it.name.text }, fields.map { visit(it.type()) }, methods)
    }
    // Variable
    //override fun visitGlobalDefine(ctx: EclangParser.GlobalDefineContext): ITok = TStatementDefine(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    //override fun visitGlobalDefineConst(ctx: EclangParser.GlobalDefineConstContext): ITok = TStatementDefineConst(loc(ctx), ctx.NAME().text, visit(ctx.expr()))

    override fun visitGlobalDefine(ctx: EclangParser.GlobalDefineContext): ITok = TGlobalDefine(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitGlobalDefineConst(ctx: EclangParser.GlobalDefineConstContext): ITok = TGlobalDefineConst(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitGlobalDeclareConst(ctx: EclangParser.GlobalDeclareConstContext): ITok = TGlobalDeclareConst(loc(ctx), ctx.NAME().text, visit(ctx.type()), if (ctx.externalName != null) Util.getStringContent(ctx.externalName.text) else null)
    override fun visitGlobalDefineTyped(ctx: EclangParser.GlobalDefineTypedContext): ITok = TGlobalDefineTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    override fun visitGlobalDefineConstTyped(ctx: EclangParser.GlobalDefineConstTypedContext): ITok = TGlobalDefineConstTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    override fun visitGlobalConstant(ctx: EclangParser.GlobalConstantContext): ITok = TGlobalConstant(loc(ctx), ctx.NAME().text, visit(ctx.expr()), ctx.type()?.let { visit(it) })
    override fun visitGlobalTypeDefine(ctx: EclangParser.GlobalTypeDefineContext): ITok = TGlobalTypeDefine(loc(ctx), ctx.NAME().text, visit(ctx.type()), external = true)
    override fun visitGlobalTypeAlias(ctx: EclangParser.GlobalTypeAliasContext): ITok {
        // `distinct` isn't a keyword, so it stays usable as a name.
        if (ctx.modifier != null && ctx.modifier.text != "distinct") throw EcError(loc(ctx), "`type ${ctx.name.text} = ${ctx.modifier.text} ...`: the only word that can come before the type is `distinct`.")
        return TGlobalTypeDefine(loc(ctx), ctx.name.text, visit(ctx.type()), distinct = ctx.modifier != null)
    }
    override fun visitGlobalEnumDeclare(ctx: EclangParser.GlobalEnumDeclareContext): ITok =
        TGlobalEnumDeclare(loc(ctx), ctx.name.text, ctx.atoms.map { it.text }, ctx.constants.map { it.text })
    override fun visitUse(ctx: EclangParser.UseContext): ITok = TGlobalUse(loc(ctx), Util.getStringContent(ctx.from.text) ?: "")
    override fun visitGlobalNamespace(ctx: EclangParser.GlobalNamespaceContext): ITok = TGlobalModule(loc(ctx), Util.getStringContent(ctx.name.text) ?: "")

    /** A value that ends a statement: an expression and its NL, or a `when`, which ends without one. */
    override fun visitTail(ctx: EclangParser.TailContext): ITok = visit(ctx.expr() ?: ctx.whenExpr())

    override fun visitStatementDefineVar(ctx: EclangParser.StatementDefineVarContext): ITok = TStatementDefineVar(loc(ctx), ctx.NAME().text, visit(ctx.tail()))
    override fun visitStatementDefineVarTyped(ctx: EclangParser.StatementDefineVarTypedContext): ITok = TStatementDefineVarTyped(loc(ctx), ctx.NAME().text, visit(ctx.tail()), visit(ctx.type()))

    override fun visitStatementDeclareVar(ctx: EclangParser.StatementDeclareVarContext): ITok = TStatementDeclareVar(loc(ctx), ctx.NAME().text, visit(ctx.type()))

    override fun visitStatementDefineVal(ctx: EclangParser.StatementDefineValContext): ITok = TStatementDefineVal(loc(ctx), ctx.NAME().text, visit(ctx.tail()))
    override fun visitStatementDefineValTyped(ctx: EclangParser.StatementDefineValTypedContext): ITok = TStatementDefineValTyped(loc(ctx), ctx.NAME().text, visit(ctx.tail()), visit(ctx.type()))

    override fun visitStatementDefineConst(ctx: EclangParser.StatementDefineConstContext): ITok = TStatementDefineConst(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitStatementDefineConstTyped(ctx: EclangParser.StatementDefineConstTypedContext): ITok = TStatementDefineConstTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))

    override fun visitStatementAssignment(ctx: EclangParser.StatementAssignmentContext): ITok {
        val target: ITok = visit(ctx.target)
        (target.unnested() as? TCreateTuple)?.let { tuple ->
            if (ctx.op.text != "=") throw EcError(loc(ctx), "`${ctx.op.text}` changes one value, so it can't have a tuple on its left. Change each part on its own line, e.g. `a ${ctx.op.text} 1`.")
            assignedNames += tupleTargets(tuple)
            return TDestructure(loc(ctx), tuple, visit(ctx.value), null, TDestructure.Kind.ASSIGN)
        }
        (target.unnested() as? TNamed)?.let { assignedNames += it.name }
        return TStatementAssign(loc(ctx), target, ctx.op.text.dropLast(1), visit(ctx.value))
    }
    override fun visitExprNamed(ctx: EclangParser.ExprNamedContext): ITok = TNamed(loc(ctx), ctx.NAME().text)

    /** The plain variables among the targets of `(a, (b, c.x)) = ...`. */
    private fun tupleTargets(tuple: TCreateTuple): List<String> = tuple.values.flatMap { v ->
        when (val t = v.unnested()) {
            is TCreateTuple -> tupleTargets(t)
            is TNamed -> listOf(t.name)
            else -> listOf()
        }
    }

    /** `(a, (b, _))`, as the tuple of names it looks like. */
    private fun destructure(ctx: EclangParser.DestructureContext): TCreateTuple =
        TCreateTuple(loc(ctx), ctx.parts.map { part -> part.destructure()?.let { destructure(it) } ?: TNamed(loc(part), part.NAME().text) })

    override fun visitStatementDestructure(ctx: EclangParser.StatementDestructureContext): ITok = TDestructure(loc(ctx), destructure(ctx.destructure()), visit(ctx.tail()),
        ctx.type()?.let { visit(it) }, if (ctx.kind.text == "var") TDestructure.Kind.VAR else TDestructure.Kind.VAL)

    override fun visitStatementTailIf(ctx: EclangParser.StatementTailIfContext): ITok = TTailIf(loc(ctx), TStatement(loc(ctx), visit(ctx.expression)), if (ctx.condition != null) visit(ctx.condition) else null)

    override fun visitStatementNullish(ctx: EclangParser.StatementNullishContext): ITok {
        val action = ctx.jump()?.let { visit(it) } ?: when (val right = visit(ctx.right)) {
            is TExprCall -> TStatementCall(right.location, right.name, right.args, right.isMethod, right.argNames)
            else -> throw EcError(loc(ctx.right), "As a statement, `a ?? b` runs b when a is null or failed, so b must be a call or a jump: `f() ?? panic(\"failed\")` or `f() ?? ret`. To use the value, assign it: `val x = a ?? b`.")
        }
        return TStatementNullish(loc(ctx), visit(ctx.left), action)
    }

    // Branches
    override fun visitStatementWhen(ctx: EclangParser.StatementWhenContext): ITok =
        TStatementWhen(loc(ctx), ctx.NAME()?.text, ctx.kind?.text == "var", visit(ctx.subject), ctx.arms.map { whenArm(it.whenPattern(), armBody(it.body, it.line), false) })

    override fun visitExprWhen(ctx: EclangParser.ExprWhenContext): ITok = visit(ctx.whenExpr())

    override fun visitWhenExpr(ctx: EclangParser.WhenExprContext): ITok =
        TWhenExpr(loc(ctx), ctx.NAME()?.text, ctx.kind?.text == "var", visit(ctx.subject), ctx.arms.map { arm ->
            if (arm.value != null) whenArm(arm.whenPattern(), visit(arm.value), true) else whenArm(arm.whenPattern(), armBody(arm.body, arm.line), false)
        })

    private fun armBody(block: EclangParser.BlockContext?, line: EclangParser.StatementContext?): ITok =
        block?.let { visit(it) } ?: TBlock(loc(line!!), listOf(visit(line)))

    private fun whenArm(pattern: EclangParser.WhenPatternContext, body: ITok, isValue: Boolean): WhenArm {
        val at = loc(pattern)
        return when (pattern) {
            is EclangParser.WhenPatternIsContext -> WhenArm(at, visit(pattern.type()), listOf(), body, pattern.guard?.let { visit(it) }, isValue = isValue)
            is EclangParser.WhenPatternBindContext -> WhenArm(at, null, listOf(), body, visit(pattern.guard), pattern.NAME().text, isValue)
            is EclangParser.WhenPatternValuesContext -> WhenArm(at, null, visit(pattern.values), body, pattern.guard?.let { visit(it) }, isValue = isValue)
            is EclangParser.WhenPatternElseContext -> WhenArm(at, null, listOf(), body, isValue = isValue)
            else -> throw EcError(at, "Unknown `when` arm.")
        }
    }

    override fun visitWhenValuePlain(ctx: EclangParser.WhenValuePlainContext): ITok = visit(ctx.expr())
    override fun visitWhenValueFrom(ctx: EclangParser.WhenValueFromContext): ITok = TRange(loc(ctx), visit(ctx.from), null, false)
    override fun visitWhenValueUpTo(ctx: EclangParser.WhenValueUpToContext): ITok = TRange(loc(ctx), null, visit(ctx.to), ctx.op.text == "..=")

    override fun visitStatementIf(ctx: EclangParser.StatementIfContext): ITok {
        return TStatementIf(loc(ctx), visit(ctx.conditions), visit(ctx.bodies), elseOf(ctx.elseBody, ctx.next))
    }

    /** What follows `else`: a block, or [next], one more `if`, as a block of its own. */
    private fun elseOf(body: EclangParser.BlockContext?, next: EclangParser.StatementContext?): ITok? = when {
        body != null -> visit(body)
        next == null -> null
        next is EclangParser.StatementIfContext || next is EclangParser.StatementLetIfContext -> TBlock(loc(next), listOf(visit(next)))
        else -> throw EcError(loc(next), "`else` is followed by `do` and a block, or by another `if`. Put this on an indented line of its own after `else do`.")
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
        val pattern = ctx.parts?.let { destructure(it) }
        return TStatementForeachIn(loc(ctx), ctx.index?.text, ctx.name?.text ?: pattern!!.pattern(), iterable, filter, body, ctx.elseBody?.let { visit(it) }, assigned, pattern)
    }

    override fun visitStatementContinue(ctx: EclangParser.StatementContinueContext): ITok = TTailIf(loc(ctx), TStatementContinue(loc(ctx), ctx.label?.text), if (ctx.condition != null) visit(ctx.condition) else null)
    override fun visitStatementBreak(ctx: EclangParser.StatementBreakContext): ITok = TTailIf(loc(ctx), TStatementBreak(loc(ctx), ctx.label?.text), if (ctx.condition != null) visit(ctx.condition) else null)
    override fun visitStatementPass(ctx: EclangParser.StatementPassContext): ITok = TTailIf(loc(ctx), TPass(loc(ctx)), if (ctx.condition != null) visit(ctx.condition) else null)
    override fun visitStatementThrow(ctx: EclangParser.StatementThrowContext): ITok = TTailIf(loc(ctx), TStatementThrow(loc(ctx), visit(ctx.throw_)), if (ctx.condition != null) visit(ctx.condition) else null)
    override fun visitStatementReturn(ctx: EclangParser.StatementReturnContext): ITok = TTailIf(loc(ctx), TStatementReturn(loc(ctx), (ctx.return_ ?: ctx.whenExpr())?.let { visit(it) } ?: TNoValue(loc(ctx))), if (ctx.condition != null) visit(ctx.condition) else null)

    override fun visitStatementLabeled(ctx: EclangParser.StatementLabeledContext): ITok {
        val tok: ITok = visit(ctx.statement())
        (tok as? ILabeledLoop ?: throw EcError(loc(ctx), "Only a loop can have a label: `${ctx.label.text}: for ... do` or `${ctx.label.text}: while ... do`.")).label = ctx.label.text
        return tok
    }

    // Volatile variables (`vol`) and C variables (`declare var`)
    override fun visitStatementDefineVol(ctx: EclangParser.StatementDefineVolContext): ITok = TStatementDefineVar(loc(ctx), ctx.NAME().text, visit(ctx.tail()), volatile = true)
    override fun visitStatementDefineVolTyped(ctx: EclangParser.StatementDefineVolTypedContext): ITok = TStatementDefineVarTyped(loc(ctx), ctx.NAME().text, visit(ctx.tail()), visit(ctx.type()), volatile = true)
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
        TStatementLetIf(loc(ctx), ctx.NAME().text, ctx.kind.text == "var", visit(ctx.value), visit(ctx.body), elseOf(ctx.elseBody, ctx.next))

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
            } else TExprCall(at, tok.name, listOf(TIt(at)) + tok.args, argNames = tok.argNames?.let { listOf(null) + it })
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
    override fun visitTypeUnionAnon(ctx: EclangParser.TypeUnionAnonContext): ITok = unsupported(ctx, "`anon` unions")
    override fun visitParameterVariable(ctx: EclangParser.ParameterVariableContext): ITok = unsupported(ctx, "A `var` parameter")
    override fun visitParameterConst(ctx: EclangParser.ParameterConstContext): ITok = unsupported(ctx, "A `val` parameter")
    override fun visitParameterVolatile(ctx: EclangParser.ParameterVolatileContext): ITok = unsupported(ctx, "A `vol` parameter")
    override fun visitStatementDefer(ctx: EclangParser.StatementDeferContext): ITok = TDefer(loc(ctx), visit(ctx.statement()))
    override fun visitStatementDeferDo(ctx: EclangParser.StatementDeferDoContext): ITok = TDefer(loc(ctx), visit(ctx.block()))
    override fun visitStatementErrDefer(ctx: EclangParser.StatementErrDeferContext): ITok = TErrDefer(loc(ctx), visit(ctx.statement()))
    override fun visitStatementErrDeferDo(ctx: EclangParser.StatementErrDeferDoContext): ITok = TErrDefer(loc(ctx), visit(ctx.block()))

    override fun visitExprNew(ctx: EclangParser.ExprNewContext): ITok = TNew(loc(ctx), visit(ctx.value), ctx.pool?.let { visit(it) })
    override fun visitExprNewArrayOfType(ctx: EclangParser.ExprNewArrayOfTypeContext): ITok =
        TNew(loc(ctx), visit(ctx.count), ctx.pool?.let { visit(it) }, visit(ctx.elementType))

    /** `pool do` / `pool name do`. `pool` isn't a keyword (it stays usable as a name), so other words are rejected here. */
    override fun visitStatementPool(ctx: EclangParser.StatementPoolContext): ITok {
        if (ctx.keyword.text != "pool") throw EcError(loc(ctx), "`${ctx.keyword.text}${ctx.name?.let { " " + it.text } ?: ""} do` isn't a statement. A block that starts with a name is a pool block: `pool do` (or `pool name do`).")
        return TStatementPool(loc(ctx), ctx.name?.text, visit(ctx.body))
    }
}