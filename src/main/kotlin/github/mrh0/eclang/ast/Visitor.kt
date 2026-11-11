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
import github.mrh0.eclang.ast.token.op.TTryCatchExpr
import github.mrh0.eclang.ast.token.TUnsafe
import github.mrh0.eclang.ast.token.branch.TTailIf
import github.mrh0.eclang.ast.token.data.*
import github.mrh0.eclang.ast.token.data.record.TCreateRecord
import github.mrh0.eclang.ast.token.data.record.TCreateRecordTyped
import github.mrh0.eclang.ast.token.data.record.TUpdateRecord
import github.mrh0.eclang.ast.token.data.unit.TPrimitiveUnit
import github.mrh0.eclang.ast.token.type.TLayout
import github.mrh0.eclang.ast.token.type.TTypeFixedArray
import github.mrh0.eclang.ast.token.type.TTypeRecord
import github.mrh0.eclang.ast.token.function.*
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.ast.token.function.call.TMutArg
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
import org.antlr.v4.runtime.CommonToken
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ListTokenSource
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.TerminalNode
import github.mrh0.eclang.util.FormatSpec
import github.mrh0.eclang.util.StringLiteral
import java.io.File

/**
 * Builds the tokens of [file]. With [fixed], every token gets that location (an expression inside a string's `${...}`), and
 * [mutNames] are the `mut` parameters of the function it is in.
 */
class Visitor(private val file: File, private val fixed: Loc? = null, private var mutNames: Set<String> = setOf()) : EclangBaseVisitor<ITok>() {
    /**
     * Runs [visit] (a function) with the names of its `mut` parameters in [mutNames]: in its body, such a `p` is `@p`, as the
     * parameter is a pointer to the caller's variable. A function can't hide a name (no shadowing), so every `p` is that one.
     */
    private fun <T> withMutParameters(params: List<EclangParser.ParameterContext>, visit: () -> T): T {
        val saved = mutNames
        mutNames = params.mapNotNull { p ->
            when (p) {
                is EclangParser.ParameterMutContext -> p.parameter()
                is EclangParser.ParameterKeywordContext -> p.parameter().takeIf { p.keyword.text !in setOf("escaping", "own", "kept", "flat") }
                else -> null
            }?.getToken(EclangParser.NAME, 0)?.text
        }.toSet()
        try { return visit() } finally { mutNames = saved }
    }

    /** The name of every assignment visited so far, for [assignedIn]. */
    private val assignedNames = mutableListOf<String>()

    /** Every local whose address is taken (`addrof x`, `addrof x.f`) so far, for [addressTakenIn]. */
    private val addressTakenNames = mutableListOf<Pair<String, Loc>>()

    /** The variables that are the receiver of a method-style call (`x.f()`), which may pass x to change it, for lambdas. */
    private val receiverNames = mutableListOf<String>()

    /** [call], noting its receiver for [receiverNames]. */
    private fun noteReceiver(call: TExprCall): TExprCall {
        if (call.isMethod) (call.args.firstOrNull()?.unnested() as? TNamed)?.let { receiverNames += it.name }
        return call
    }

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

    /** While visiting the condition of an `assert` or contract: the text of each expression's token, for its report (see Assertions). */
    private var assertSources: java.util.IdentityHashMap<ITok, String>? = null

    override fun visit(tree: org.antlr.v4.runtime.tree.ParseTree): ITok {
        val tok = super.visit(tree)
        assertSources?.let { if (tok != null && tree is EclangParser.ExprContext) it.putIfAbsent(tok, source(tree)) }
        return tok
    }

    /** [visit], with the text of every expression in it (see [assertSources]). */
    private fun <T> withSources(visit: () -> T): Pair<T, Map<ITok, String>> {
        val saved = assertSources
        val sources = java.util.IdentityHashMap<ITok, String>()
        assertSources = sources
        try { return visit() to sources } finally { assertSources = saved }
    }

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
                    is EclangParser.GlobalDefineContext, is EclangParser.GlobalDefineConstContext,
                    is EclangParser.GlobalDefineTypedContext, is EclangParser.GlobalDefineConstTypedContext,
                    is EclangParser.GlobalConstantContext -> "value" to g.getToken(EclangParser.NAME, 0).text
                    is EclangParser.GlobalRecordDefineContext -> "type" to g.name.text
                    is EclangParser.GlobalUnionDefineContext -> "type" to g.name.text
                    is EclangParser.GlobalInterfaceDefineContext -> "type" to g.name.text
                    else -> throw EcError(loc(p), "`priv` can be put before `fn`, `val`, `var`, `const`, `rec` and `inter`.")
                }
                val tok: ITok = visit(g)
                globals += tok
                privates += TProgram.Private(tok, kind, name)
            }
        }
        for (e in ctx.exports) {
            if (e.keyword.text != "export") throw EcError(loc(e), "`${e.keyword.text}` can't go before a function. To let C code call it, write `export fn`.")
            val f = visit(e.func()) as TFunc
            f.exportName = externName(e, e.ext, e.externalName) ?: Util.testIdentifier(loc(e), f.name)
            functions += f
        }
        return TProgram(loc(ctx), functions, visit(ctx.uses), globals, privates)
    }

    /** The widths of the bit-fields among [widths]: `: 3`, or `:3`, which the lexer reads as an atom. */
    /** The C names of the fields of a `declare rec` (`input extern "in": ...`), null where it is the eclang name. */
    private fun cNames(externs: List<EclangParser.FieldExternContext>): List<String?> = externs.map { e -> externName(e, e.ext, e.cName) }

    /**
     * The C name in `extern "c_name"`, or null without one. `extern` isn't a keyword (it stays usable as a name), so the
     * grammar takes any name before the string, and [word] is checked here.
     */
    private fun externName(ctx: ParserRuleContext, word: Token?, name: Token?): String? {
        if (word == null || name == null) return null
        if (word.text != "extern") throw EcError(loc(ctx), "`${word.text} ${name.text}` isn't anything eclang knows. To give the name C uses, write `extern ${name.text}`.")
        return Util.getStringContent(name.text)
    }

    private fun widths(widths: List<EclangParser.FieldWidthContext>): List<ITok?> = widths.map { w ->
        val atom = w.atomWidth?.text?.drop(1)
        when {
            w.width != null -> visit(w.width)
            atom == null -> null
            atom[0].isDigit() -> TInteger(loc(w), number(w, atom.replace("_", ""), 10))
            else -> TNamed(loc(w), atom)
        }
    }

    /** `packed`, `align(n)` and `mut` before `rec` and `union`, and `ref` before `rec`. */
    private fun layout(modifiers: List<EclangParser.RecordModifierContext>, isUnion: Boolean = false): TLayout {
        val words = modifiers.map { if (it.mut != null) "mut" else it.name.text }
        words.groupBy { it }.filter { it.value.size > 1 }.keys.firstOrNull()?.let { throw EcError(loc(modifiers[0]), "`$it` is written twice.") }
        for (m in modifiers) when (if (m.mut != null) "mut" else m.name.text) {
            "mut" -> {}
            "packed" -> if (m.size != null) throw EcError(loc(m), "`packed` takes no value. To set an alignment, write `align(n)`.")
            "align" -> if (m.size == null) throw EcError(loc(m), "`align` needs the alignment, such as `align(16)`.")
            "resource" -> if (m.size != null) throw EcError(loc(m), "`resource` takes no value: `resource rec Name as`.")
            "sync" -> {
                if (m.size != null) throw EcError(loc(m), "`sync` takes no value: `sync rec Name as`.")
                if (isUnion) throw EcError(loc(m), "`sync` goes before `rec`: it says the record's own functions make sharing it between threads safe.")
            }
            "ref" -> {
                if (m.size != null) throw EcError(loc(m), "`ref` takes no value: `ref rec Name as`.")
                if (isUnion) throw EcError(loc(m), "`ref` goes before `rec`: a `union` is a value of one of its fields. Use `@Name` for a pointer to it.")
            }
            else -> throw EcError(loc(m), "`${m.name.text}` can't go before `rec` or `union`. These can: `mut`, `packed`, `align(n)`, `resource`, and before `rec`, `ref` and `sync`.")
        }
        return TLayout("packed" in words, modifiers.firstOrNull { it.mut == null && it.name.text == "align" }?.let { visit(it.size) }, isUnion, isRef = "ref" in words, isResource = "resource" in words, isSync = "sync" in words,
            isMut = "mut" in words)
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
        TTypeRecord(loc(ctx), ctx.name.text, ctx.fields.map { it.name.text }, ctx.fields.map { visit(it.type()) }, ctx.extending?.let { recordTypeName(ctx.qualifier, it) }, ctx.fields.map { f -> f.value?.let { visit(it) } }, ctx.typeParams.map { it.name.text },
            layout(ctx.modifiers), widths(ctx.fields.map { it.fieldWidth() }), ctx.typeParams.filter { it.mut != null }.map { it.name.text }.toSet())
    override fun visitGlobalUnionDefine(ctx: EclangParser.GlobalUnionDefineContext): ITok {
        checkUnion(ctx, ctx.keyword)
        return TTypeRecord(loc(ctx), ctx.name.text, ctx.fields.map { it.name.text }, ctx.fields.map { visit(it.type()) }, null, ctx.fields.map { f -> f.value?.let { visit(it) } }, listOf(),
            layout(ctx.modifiers, isUnion = true), widths(ctx.fields.map { it.fieldWidth() }))
    }
    // `declare struct x` and `declare union x` (C's `struct x` and `union x`), and `declare union rec x_t` (a typedef of a union).
    override fun visitGlobalUnionDeclare(ctx: EclangParser.GlobalUnionDeclareContext): ITok {
        val isUnion = checkDeclared(ctx, ctx.keyword, ctx.mut, ctx.typedef)
        val external = externName(ctx, ctx.ext, ctx.externalName)
        return if (ctx.typedef != null) TTypeDeclareRecord(loc(ctx), ctx.name.text, listOf(), listOf(), null, external, isUnion = true, isMut = ctx.mut != null)
            else TTypeDeclareStruct(loc(ctx), ctx.name.text, listOf(), listOf(), external, isUnion = isUnion)
    }
    override fun visitGlobalUnionDeclareDefine(ctx: EclangParser.GlobalUnionDeclareDefineContext): ITok {
        val isUnion = checkDeclared(ctx, ctx.keyword, ctx.mut, ctx.typedef)
        val external = externName(ctx, ctx.ext, ctx.externalName)
        return if (ctx.typedef != null) TTypeDeclareRecord(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), null, external, widths(ctx.widths), isUnion = true, cNames = cNames(ctx.externs), isMut = ctx.mut != null)
            else TTypeDeclareStruct(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), external, widths(ctx.widths), isUnion = isUnion, cNames = cNames(ctx.externs))
    }
    /**
     * The word after `declare` (and `mut`) in a C struct or union: `struct` or `union`, neither of them a keyword. Whether it is
     * a union. C's `struct x` and `union x` are always mutable, so `mut` is only for `declare mut union rec`, a typedef.
     */
    private fun checkDeclared(ctx: ParserRuleContext, word: Token, mut: Token?, typedef: Token?): Boolean {
        when (word.text) {
            "union" -> if (mut != null && typedef == null) throw EcError(loc(ctx), "`declare union` is C's `union x`, whose fields can always change, like a `declare struct`, so it takes no `mut`. Remove it, or write `declare mut union rec` for a typedef.")
            "struct" -> {
                if (typedef != null) throw EcError(loc(ctx), "`declare struct rec` isn't a declaration: `declare struct x` is C's `struct x`, and a typedef of a struct is a `declare rec` (`declare rec x_t`).")
                if (mut != null) throw EcError(loc(ctx), "`declare struct` is C's `struct x`, whose fields can always change, so it takes no `mut`. Remove it, or write `declare mut rec` for a typedef.")
            }
            else -> throw EcError(loc(ctx), "`declare ${word.text}` isn't a declaration. A C type is declared with `declare rec` (a typedef), `declare struct`, `declare union` or `declare type`.")
        }
        return word.text == "union"
    }

    override fun visitGlobalRecordDeclare(ctx: EclangParser.GlobalRecordDeclareContext): ITok = TTypeDeclareRecord(loc(ctx), ctx.name.text, listOf(), listOf(), null, externName(ctx, ctx.ext, ctx.externalName), isMut = ctx.mut != null)
    override fun visitGlobalRecordDeclareDefine(ctx: EclangParser.GlobalRecordDeclareDefineContext): ITok = TTypeDeclareRecord(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), null, externName(ctx, ctx.ext, ctx.externalName), widths(ctx.widths), cNames = cNames(ctx.externs), isMut = ctx.mut != null)


    // Types
    override fun visitTypeByName(ctx: EclangParser.TypeByNameContext): ITok = TTypeByName(loc(ctx), ctx.text)

    override fun visitTypeByNameParameters(ctx: EclangParser.TypeByNameParametersContext): ITok {
        if (isVolatile(ctx)) throw EcError(loc(ctx), "`Volatile<T>` can only be the type of a `var` (`var x: Volatile<Int> = 0`, also at the top level): it makes the variable itself volatile in C. " +
            "It can't be part of another type, a field's, a parameter's or a `val`'s.")
        return TTypeApplied(loc(ctx), recordTypeName(ctx.qualifier, ctx.name), typeArgs(ctx.args))
    }

    /** Whether [ctx] is `Volatile<T>`, which isn't a type of its own: a `var` declared with it is a C `volatile` variable of type T. */
    private fun isVolatile(ctx: EclangParser.TypeContext?): Boolean =
        ctx is EclangParser.TypeByNameParametersContext && ctx.qualifier == null && ctx.name.text == VOLATILE

    /** The type of a `var` declared with [ctx], and whether it is volatile: `var x: Volatile<Int>` is a volatile Int. */
    private fun varType(ctx: EclangParser.TypeContext): Pair<ITok, Boolean> {
        if (!isVolatile(ctx)) return visit(ctx) to false
        val args = (ctx as EclangParser.TypeByNameParametersContext).args
        if (args.size != 1) throw EcError(loc(ctx), "`Volatile` takes one type, the variable's: `Volatile<Int>`.")
        if (isVolatile(args[0])) throw EcError(loc(ctx), "`Volatile<Volatile<...>>`: a variable is volatile once. Write one `Volatile`.")
        return visit(args[0]) to true
    }

    /** The type arguments of `List<T>`: a bare T, K or V2 there may be a type parameter (`<<T>>` would lex as a shift). */
    private fun typeArgs(args: List<EclangParser.TypeContext>): List<ITok> = args.map {
        if (it is EclangParser.TypeByNameContext && TTypeArgument.isShort(it.text)) TTypeArgument(loc(it), it.text) else visit(it)
    }

    override fun visitTypeAddressByName(ctx: EclangParser.TypeAddressByNameContext): ITok = TTypePointerByName(loc(ctx), visit(ctx.type()))

    override fun visitTypeUnion(ctx: EclangParser.TypeUnionContext): ITok = TTypeUnion(loc(ctx), visit(unionTypes(ctx)))

    override fun visitTypeEnum(ctx: EclangParser.TypeEnumContext): ITok = TTypeEnum(loc(ctx), visit(enumTypes(ctx)))

    /** `A | B | C` parses as `(A | B) | C`, and is one union of three. Brackets are a [EclangParser.TypeNestContext], so they stay. */
    private fun unionTypes(ctx: EclangParser.TypeUnionContext): MutableList<EclangParser.TypeContext> =
        ctx.types.flatMapTo(mutableListOf()) { if (it is EclangParser.TypeUnionContext) unionTypes(it) else listOf(it) }

    private fun enumTypes(ctx: EclangParser.TypeEnumContext): MutableList<EclangParser.TypeContext> =
        ctx.types.flatMapTo(mutableListOf()) { if (it is EclangParser.TypeEnumContext) enumTypes(it) else listOf(it) }

    override fun visitTypeNullable(ctx: EclangParser.TypeNullableContext): ITok = TTypeNullable(loc(ctx), visit(ctx.type()))

    override fun visitTypeAtom(ctx: EclangParser.TypeAtomContext): ITok = TTypeAtom(loc(ctx), ctx.text)

    override fun visitTypeNest(ctx: EclangParser.TypeNestContext): ITok = visit(ctx.type())
    override fun visitTypeTuple(ctx: EclangParser.TypeTupleContext): ITok = TTypeTuple(loc(ctx), visit(ctx.types))

    override fun visitTypeGeneric(ctx: EclangParser.TypeGenericContext): ITok = TTypeGeneric(loc(ctx), ctx.NAME().text, isMut = ctx.mut != null)

    override fun visitTypeTypeOf(ctx: EclangParser.TypeTypeOfContext): ITok = TTypeOf(loc(ctx), visit(ctx.expr()))

    override fun visitTypeArray(ctx: EclangParser.TypeArrayContext): ITok =
        if(ctx.text.endsWith('c')) TTypeCArray(loc(ctx), visit(ctx.type())) else TTypeArray(loc(ctx), visit(ctx.type()))
    override fun visitTypeFixedArray(ctx: EclangParser.TypeFixedArrayContext): ITok = TTypeFixedArray(loc(ctx), visit(ctx.type()), visit(ctx.size))

    override fun visitTypeThrows(ctx: EclangParser.TypeThrowsContext): ITok = TTypeThrows(loc(ctx), visit(ctx.left), visit(ctx.throwing))

    override fun visitTypeCallSignature(ctx: EclangParser.TypeCallSignatureContext): ITok = TTypeCallSignature(loc(ctx), visit(ctx.argTypes), visit(ctx.returnType))

    // Functions
    override fun visitFunctionBlock(ctx: EclangParser.FunctionBlockContext): ITok {
        return addressTakenIn { withMutParameters(ctx.params) { TFuncBlock(loc(ctx), cvisit(ctx.body), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), returns(ctx, ctx.returnType, ctx.throwing)).also { it.contracts = contracts(ctx.contracts) } } }
    }

    override fun visitFunctionInline(ctx: EclangParser.FunctionInlineContext): ITok {
        return addressTakenIn { withMutParameters(ctx.params) { TFuncInline(loc(ctx), cvisit(ctx.tail()), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), returns(ctx, ctx.returnType, ctx.throwing)).also { it.contracts = contracts(ctx.contracts) } } }
    }

    override fun visitFunctionDeclare(ctx: EclangParser.FunctionDeclareContext): ITok {
        return TFuncExternal(loc(ctx), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), returns(ctx, ctx.returnType, ctx.throwing), externName(ctx, ctx.ext, ctx.externalName))
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
    override fun visitParameterMut(ctx: EclangParser.ParameterMutContext): ITok = TParameterMut(loc(ctx), visit(ctx.parameter()) as TParameter, isOut = false)
    /** `out p: T`. `out` isn't a keyword (it is a common name), so other words are rejected here. */
    override fun visitParameterKeyword(ctx: EclangParser.ParameterKeywordContext): ITok {
        if (ctx.keyword.text == "escaping") return TParameterEscaping(loc(ctx), visit(ctx.parameter()) as TParameter)
        if (ctx.keyword.text == "own") return TParameterOwn(loc(ctx), visit(ctx.parameter()) as TParameter)
        if (ctx.keyword.text == "kept") return TParameterKept(loc(ctx), visit(ctx.parameter()) as TParameter)
        if (ctx.keyword.text == "flat") return TParameterFlat(loc(ctx), visit(ctx.parameter()) as TParameter)
        if (ctx.keyword.text != "out") throw EcError(loc(ctx), "`${ctx.keyword.text}` can't go before a parameter. These can: `mut` (the function changes the caller's variable), `escaping` (the function keeps the value after the call), `own` (the function takes a pool over), `flat` (the value may go to another thread: it reaches no memory but `sync` records) and, in a `declare fn`, `out` (C only writes it) and `kept` (C keeps it in the other arguments).")
        return TParameterMut(loc(ctx), visit(ctx.parameter()) as TParameter, isOut = true)
    }

    /** True for `first.name(...)`, which starts with an expression rather than the name. */
    private fun isMethod(ctx: ParserRuleContext) = ctx.getChild(0) is ParserRuleContext

    override fun visitExprFunctionCallNoArgs(ctx: EclangParser.ExprFunctionCallNoArgsContext): ITok = noteReceiver(TExprCall(loc(ctx), ctx.NAME().text, visit(ctx.args), isMethod(ctx)))
    override fun visitExprFunctionCallWithArgs(ctx: EclangParser.ExprFunctionCallWithArgsContext): ITok {
        val (rest, names) = arguments(ctx.rest)
        return noteReceiver(TExprCall(loc(ctx), ctx.NAME().text, poolKill(ctx.NAME().text, (visit<_, ITok>(ctx.args) + rest).toMutableList()), isMethod(ctx), names))
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
        return list.map { a ->
            (visit(a.expr()) as ITok).let { if (a.mut == null) it else TMutArg(loc(a), it).also { _ -> changedName(it)?.let { n -> assignedNames += n } } }
        } to names.takeIf { n -> n.any { it != null } }
    }

    override fun visitExprSafeCall(ctx: EclangParser.ExprSafeCallContext): ITok {
        val (rest, names) = arguments(ctx.rest)
        return TSafeCall(loc(ctx), visit(ctx.args[0]), ctx.NAME().text, rest, names)
    }

    override fun visitStatementSafeCall(ctx: EclangParser.StatementSafeCallContext): ITok {
        val (rest, names) = arguments(ctx.rest)
        return TSafeCall(loc(ctx), visit(ctx.args[0]), ctx.NAME().text, rest, names, statement = true)
    }

    /** `clearPool(p)` frees what p holds, which a loop treats like an assignment of p (`cd.forgetAssigned`). */
    private fun poolKill(name: String, args: MutableList<ITok>): MutableList<ITok> {
        if (name == "clearPool") (args.firstOrNull()?.unnested() as? TNamed)?.let { assignedNames += it.name }
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
            listOfNotNull(field.label?.let { StringLiteral.parse(at, it) }, TInterpolated.Field(github.mrh0.eclang.Compiler.parseInterpolation(field.expression, at, mutNames), field.spec))
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
        "..>" -> TRange(loc(ctx), visit(ctx.left), visit(ctx.right), inclusive = false, descending = true)
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
    override fun visitExprUnsafe(ctx: EclangParser.ExprUnsafeContext): ITok = TUnsafe(loc(ctx), visit(ctx.expr()), isBlock = false)
    override fun visitStatementUnsafe(ctx: EclangParser.StatementUnsafeContext): ITok = TUnsafe(loc(ctx), visit(ctx.body), isBlock = true)
    override fun visitStatementUnsafeLine(ctx: EclangParser.StatementUnsafeLineContext): ITok = TUnsafe(loc(ctx), visit(ctx.statement()), isBlock = true)
    /** `destroy p` frees what p holds, which a loop treats like an assignment of p (`cd.forgetAssigned`). */
    override fun visitStatementDestroy(ctx: EclangParser.StatementDestroyContext): ITok {
        val target = visit(ctx.target)
        (target.unnested() as? TNamed)?.let { assignedNames += it.name }
        return TStatementDestroy(loc(ctx), target)
    }
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
    override fun visitGlobalDeclareConst(ctx: EclangParser.GlobalDeclareConstContext): ITok = TGlobalDeclareConst(loc(ctx), ctx.name.text, visit(ctx.type()), externName(ctx, ctx.ext, ctx.externalName))
    override fun visitGlobalDefineTyped(ctx: EclangParser.GlobalDefineTypedContext): ITok {
        val (type, volatile) = ctx.type()?.let { varType(it) } ?: (null to false)
        return if (type == null) TGlobalDefine(loc(ctx), ctx.NAME().text, visit(ctx.expr())) else TGlobalDefineTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), type, volatile)
    }
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
    override fun visitGlobalNamespace(ctx: EclangParser.GlobalNamespaceContext): ITok {
        // `module` isn't a keyword, so the grammar takes any name before the string.
        if (ctx.keyword.text != "module") throw EcError(loc(ctx), "`${ctx.keyword.text} ${ctx.name.text}` isn't a declaration. To name the module of this file, write `module ${ctx.name.text}`; to use another file, `use ${ctx.name.text}`.")
        return TGlobalModule(loc(ctx), Util.getStringContent(ctx.name.text) ?: "")
    }

    /** A value that ends a statement: an expression and its NL, or a `when`, which ends without one. */
    override fun visitTail(ctx: EclangParser.TailContext): ITok = visit(ctx.expr() ?: ctx.whenExpr() ?: ctx.tryCatch())
    override fun visitTryCatch(ctx: EclangParser.TryCatchContext): ITok =
        TTryCatchExpr(loc(ctx), visit(ctx.left), ctx.NAME().text, ctx.value?.let { visit(it) }, ctx.body?.let { visit(it) })

    override fun visitStatementDefineVar(ctx: EclangParser.StatementDefineVarContext): ITok = TStatementDefineVar(loc(ctx), ctx.NAME().text, visit(ctx.tail()))
    override fun visitStatementDefineVarTyped(ctx: EclangParser.StatementDefineVarTypedContext): ITok {
        val (type, volatile) = varType(ctx.type())
        return TStatementDefineVarTyped(loc(ctx), ctx.NAME().text, visit(ctx.tail()), type, volatile)
    }

    override fun visitStatementDeclareVar(ctx: EclangParser.StatementDeclareVarContext): ITok {
        val (type, volatile) = varType(ctx.type())
        return TStatementDeclareVar(loc(ctx), ctx.NAME().text, type, volatile)
    }

    override fun visitStatementDefineVal(ctx: EclangParser.StatementDefineValContext): ITok = TStatementDefineVal(loc(ctx), ctx.NAME().text, visit(ctx.tail()))
    override fun visitStatementDefineValTyped(ctx: EclangParser.StatementDefineValTypedContext): ITok = TStatementDefineValTyped(loc(ctx), ctx.NAME().text, visit(ctx.tail()), visit(ctx.type()))

    override fun visitStatementDefineConst(ctx: EclangParser.StatementDefineConstContext): ITok = TStatementDefineConst(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitStatementDefineConstTyped(ctx: EclangParser.StatementDefineConstTypedContext): ITok = TStatementDefineConstTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))

    override fun visitStatementAssignment(ctx: EclangParser.StatementAssignmentContext): ITok {
        // `unsafe p[i] = v` parses with `unsafe p[i]` as the target: it means `unsafe` + the whole assignment.
        val target: ITok = visit(ctx.target)
        if (target is TUnsafe) return TUnsafe(target.location, assignment(ctx, target.body), isBlock = true)
        return assignment(ctx, target)
    }

    private fun assignment(ctx: EclangParser.StatementAssignmentContext, target: ITok): ITok {
        (target.unnested() as? TCreateTuple)?.let { tuple ->
            if (ctx.op.text != "=") throw EcError(loc(ctx), "`${ctx.op.text}` changes one value, so it can't have a tuple on its left. Change each part on its own line, e.g. `a ${ctx.op.text} 1`.")
            assignedNames += tupleTargets(tuple)
            return TDestructure(loc(ctx), tuple, visit(ctx.value), null, TDestructure.Kind.ASSIGN)
        }
        changedName(target)?.let { assignedNames += it }
        return TStatementAssign(loc(ctx), target, ctx.op.text.dropLast(1), visit(ctx.value))
    }
    override fun visitExprNamed(ctx: EclangParser.ExprNamedContext): ITok {
        // `it` isn't a keyword: in a `|>` stage it is the value before the `|>`, elsewhere an ordinary name.
        if (ctx.NAME().text == "it" && itUses.isNotEmpty()) {
            itUses[itUses.size - 1]++
            return TIt(loc(ctx))
        }
        // `here` and `pool` aren't keywords either: TNamed gives them their meaning when no variable has the name.
        return TNamed(loc(ctx), ctx.NAME().text).let { if (it.name in mutNames) TReadPointer(it.location, it, mutParameter = true) else it }
    }

    /** The plain variables among the targets of `(a, (b, c.x)) = ...`. */
    private fun tupleTargets(tuple: TCreateTuple): List<String> = tuple.values.flatMap { v ->
        when (val t = v.unnested()) {
            is TCreateTuple -> tupleTargets(t)
            else -> listOfNotNull(changedName(t))
        }
    }

    /**
     * The name a loop must forget the narrowing of when [target] is assigned or passed as `mut`: x for a variable x, `@p` for a
     * `mut` parameter p (what it points to, see `Narrowing.local`), or null for a field or element, which isn't narrowed.
     */
    private fun changedName(target: ITok): String? = when (val t = target.unnested()) {
        is TNamed -> t.name
        is TReadPointer -> if (t.mutParameter) (t.expr as? TNamed)?.let { "@${it.name}" } else null
        else -> null
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
        // `else if` parses as an `if` nested in `next`; a chain of them is one statement.
        val chain = generateSequence(ctx) { it.next as? EclangParser.StatementIfContext }.toMutableList()
        val last = chain.last()
        return TStatementIf(loc(ctx), visit(chain.mapTo(mutableListOf()) { it.condition }), visit(chain.mapTo(mutableListOf()) { it.body }), elseOf(last.elseBody, last.next))
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
        // `where` isn't a keyword, so the grammar takes any name between the iterable and the filter.
        ctx.where?.let { if (it.text != "where") throw EcError(loc(ctx), "`${it.text}` after what a `for` loops over isn't anything eclang knows. To skip some elements, write `where` and a condition: `for x in xs where x > 0 do`.") }
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

    // C variables (`declare var`)
    override fun visitGlobalDeclareDefine(ctx: EclangParser.GlobalDeclareDefineContext): ITok = TGlobalDeclareConst(loc(ctx), ctx.name.text, visit(ctx.type()), externName(ctx, ctx.ext, ctx.externalName), assignable = true)

    override fun visitStatementAssert(ctx: EclangParser.StatementAssertContext): ITok {
        val (condition, sources) = withSources { visit(ctx.condition) }
        return TAssert(loc(ctx), condition, ctx.message?.let { visit(it) }, source(ctx.condition), sources)
    }

    // `const assert`
    override fun visitStatementConstAssert(ctx: EclangParser.StatementConstAssertContext): ITok = visit(ctx.constAssert())
    override fun visitGlobalConstAssert(ctx: EclangParser.GlobalConstAssertContext): ITok = visit(ctx.constAssert())

    override fun visitConstAssertCondition(ctx: EclangParser.ConstAssertConditionContext): ITok =
        TConstAssert(loc(ctx), TConstAssert.Condition(visit(ctx.condition)), constMessage(ctx.message), source(ctx.condition))

    override fun visitConstAssertIn(ctx: EclangParser.ConstAssertInContext): ITok =
        TConstAssert(loc(ctx), TConstAssert.InPool(visit(ctx.value), visit(ctx.pool), source(ctx.value)), constMessage(ctx.message), "${source(ctx.value)} in ${source(ctx.pool)}")

    override fun visitConstAssertRelation(ctx: EclangParser.ConstAssertRelationContext): ITok {
        val relation = ctx.relation.text
        val (left, right) = source(ctx.left) to source(ctx.right)
        val claim = when (relation) {
            "is", "fits" -> TConstAssert.Relation(visit(ctx.left), relation, visit(ctx.right), left, right)
            "outlives" -> TConstAssert.Outlives(valueNamed(ctx.left), valueNamed(ctx.right), left, right)
            else -> throw EcError(loc(ctx), "`const assert` doesn't know the relation `$relation`. It knows `A is B` and `A fits B` of types, `a outlives b` of values, and `x in p` of a value and a pool.")
        }
        return TConstAssert(loc(ctx), claim, constMessage(ctx.message), "$left $relation $right")
    }

    /** The variable or field `a` or `a.b` that `const assert a outlives b` names, which the grammar reads as a type. */
    private fun valueNamed(ctx: EclangParser.TypeContext): ITok {
        if (ctx !is EclangParser.TypeByNameContext) throw EcError(loc(ctx), "`outlives` compares two variables or fields, such as `node outlives list` or `h.data outlives h`.")
        val names = ctx.text.split('.')
        val first = TNamed(loc(ctx), names[0]).let { if (it.name in mutNames) TReadPointer(it.location, it, mutParameter = true) else it }
        return names.drop(1).fold(first as ITok) { tok, name -> TAccessorNamed(loc(ctx), tok, name) }
    }

    /** The message of a `const assert`: a plain String literal, as it is part of an error or of C. */
    private fun constMessage(token: org.antlr.v4.runtime.Token?): String? = token?.let {
        if (it.text.endsWith("\"c")) throw EcError(Loc(it, file), "The message of `const assert` is a plain String literal; remove the `c`.")
        val content = Util.getStringContent(it.text) ?: ""
        if (StringLiteral.splitInterpolation(Loc(it, file), content).size > 1) throw EcError(Loc(it, file), "The message of `const assert` is checked when compiling, so it can't contain `\${...}`; write `\\$` for a plain dollar sign.")
        content
    }

    /** `debug statement`: the statement only runs in a debug build. `debug` isn't a keyword, so other words are rejected here. */
    override fun visitStatementPrefixed(ctx: EclangParser.StatementPrefixedContext): ITok {
        val word = ctx.keyword.text
        if (word != "debug") throw EcError(loc(ctx), "`$word` before a statement isn't anything eclang knows. Only `debug` can be written there: `debug io.log(x)` runs only in a debug build.")
        return when (val inner = ctx.statement()) {
            is EclangParser.StatementConstAssertContext -> throw EcError(loc(ctx), "A `const assert` is checked when compiling, so there is nothing for `debug` to leave out of a release build. Remove `debug`.")
            is EclangParser.StatementPrefixedContext -> throw EcError(loc(ctx), "`${inner.keyword.text}` can't follow `debug`: write `debug` once.")
            // `debug defer x`: what is deferred is only done in a debug build (`debug` around the `defer` would end with its block).
            is EclangParser.StatementDeferContext, is EclangParser.StatementDeferDoContext -> TDefer(loc(inner), debugOf(loc(ctx), (visit(inner) as TDefer).statement))
            is EclangParser.StatementErrDeferContext, is EclangParser.StatementErrDeferDoContext -> TErrDefer(loc(inner), debugOf(loc(ctx), (visit(inner) as TErrDefer).statement))
            else -> debugOf(loc(ctx), visit(inner))
        }
    }

    private fun debugOf(at: Loc, tok: ITok): ITok = TDebug(at, tok as? TBlock ?: TBlock(at, listOf(tok)))

    /** The `requires` and `ensures` of a function. None of the three words is a keyword, so they are checked here. */
    private fun contracts(list: List<EclangParser.ContractContext>): List<Contract> {
        val contracts = ArrayList<Contract>()
        var i = 0
        while (i < list.size) {
            var c = list[i++]
            var debug = c.debug != null
            // `debug requires x - 1 > 0` parses as `debug requires` and `x -1 > 0` (see the grammar): parse the second part again.
            if (c.debug == null && c.kind.text == "debug" && i < list.size && (c.condition as? EclangParser.ExprNamedContext)?.text in KINDS) {
                c = reparseContract(c.condition.start, list[i++])
                debug = true
            }
            c.debug?.let { if (it.text != "debug") throw EcError(loc(c), "`${it.text} ${c.kind.text}` isn't anything eclang knows. After a function's signature come `requires` (checked at each call), " +
                "`ensures` (checked when it returns) and `debug requires`/`debug ensures` (only checked in a debug build).") }
            if (c.kind.text !in KINDS) throw EcError(loc(c), "`${c.kind.text}` isn't anything eclang knows after a function's signature. A condition there starts with `requires` (checked at each call) or `ensures` (checked when it returns): `requires ${source(c.condition)}`.")
            val (condition, sources) = withSources { visit(c.condition) }
            contracts += Contract(loc(c), c.kind.text == "requires", condition, c.message?.let { visit(it) }, source(c.condition), sources, debug)
        }
        return contracts
    }

    /** [kind] (`requires` or `ensures`) followed by the tokens of [rest], parsed as one contract. */
    private fun reparseContract(kind: Token, rest: EclangParser.ContractContext): EclangParser.ContractContext {
        // Copies: the token stream numbers the tokens it is given.
        val tokens = ArrayList<Token>()
        tokens += CommonToken(kind)
        fun collect(t: ParseTree) {
            if (t is TerminalNode) tokens += CommonToken(t.symbol) else for (i in 0 until t.childCount) collect(t.getChild(i))
        }
        collect(rest)
        val parser = EclangParser(CommonTokenStream(ListTokenSource(tokens)))
        parser.removeErrorListeners()
        val c = parser.contract()
        if (parser.numberOfSyntaxErrors > 0 || c.stop !== tokens.last() || c.debug != null)
            throw EcError(Loc(kind, file), "`debug ${kind.text} ${source(rest)}` can't be read as one condition. Put it in parentheses: `debug ${kind.text} (${source(rest)})`.")
        return c
    }

    override fun visitStatementLetIf(ctx: EclangParser.StatementLetIfContext): ITok =
        TStatementLetIf(loc(ctx), ctx.NAME().text, ctx.kind.text == "var", visit(ctx.value), visit(ctx.body), elseOf(ctx.elseBody, ctx.next))

    // Pipes and lambdas (`it` is in visitExprNamed)
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
            is TLambda -> TCallValue(at, tok.also { it.calledDirectly = true }, listOf(TIt(at)))
            else -> throw EcError(at, "A `|>` stage must be a call (`x |> f(2)` is `f(x, 2)`), a function name (`x |> f`), a lambda, " +
                "or use `it` (`x |> it * 2`), but `${source(ctx.right)}` is none of these.")
        }
        return TPipe(loc(ctx), value, stage, 1)
    }

    override fun visitExprLambda(ctx: EclangParser.ExprLambdaContext): ITok {
        // `it` inside a lambda isn't the value of a `|>` around it: a lambda is called later, after the pipe.
        // A `mut` parameter of the function around it is a name of the lambda's own (using one is an error, see CompileData.capture).
        val saved = ArrayList(itUses)
        val savedMut = mutNames
        itUses.clear()
        mutNames = setOf()
        val marks = Triple(assignedNames.size, addressTakenNames.size, receiverNames.size)
        try {
            val params = ctx.params.map { p -> p.NAME().text to p.type()?.let { visit(it) as ITok } }
            val body: ITok = visit(ctx.body)
            // What the body may change of the variables it captures (see Closure.changes).
            val changes = assignedNames.subList(marks.first, assignedNames.size).map { it.removePrefix("@") } +
                addressTakenNames.subList(marks.second, addressTakenNames.size).map { it.first } + receiverNames.subList(marks.third, receiverNames.size)
            return TLambda(loc(ctx), params, ctx.returnType?.let { visit(it) }, body, changes.toSet() - params.map { it.first }.toSet())
        } finally {
            itUses.clear(); itUses += saved
            mutNames = savedMut
        }
    }

    // Parsed, but not part of the language yet: a clear error instead of a crash.
    private fun unsupported(ctx: ParserRuleContext, what: String): Nothing = throw EcError(loc(ctx), "$what is not supported yet.")
    override fun visitExprAccessNameNullishCoalescing(ctx: EclangParser.ExprAccessNameNullishCoalescingContext): ITok = TAccessorNamed(loc(ctx), visit(ctx.accesssed), ctx.NAME().text, safe = true)
    override fun visitStatementTailWhile(ctx: EclangParser.StatementTailWhileContext): ITok = unsupported(ctx, "A trailing `while`")
    override fun visitStatementYield(ctx: EclangParser.StatementYieldContext): ITok = unsupported(ctx, "`yield`")
    override fun visitParameterVariable(ctx: EclangParser.ParameterVariableContext): ITok = unsupported(ctx, "A `var` parameter")
    override fun visitParameterConst(ctx: EclangParser.ParameterConstContext): ITok = unsupported(ctx, "A `val` parameter")
    override fun visitStatementDefer(ctx: EclangParser.StatementDeferContext): ITok = TDefer(loc(ctx), visit(ctx.statement()))
    override fun visitStatementDeferDo(ctx: EclangParser.StatementDeferDoContext): ITok = TDefer(loc(ctx), visit(ctx.block()))
    override fun visitStatementErrDefer(ctx: EclangParser.StatementErrDeferContext): ITok = TErrDefer(loc(ctx), visit(ctx.statement()))
    override fun visitStatementErrDeferDo(ctx: EclangParser.StatementErrDeferDoContext): ITok = TErrDefer(loc(ctx), visit(ctx.block()))

    override fun visitExprNew(ctx: EclangParser.ExprNewContext): ITok = TNew(loc(ctx), visit(ctx.value), ctx.pool?.let { visit(it) })
    override fun visitExprNewArrayOfType(ctx: EclangParser.ExprNewArrayOfTypeContext): ITok =
        TNew(loc(ctx), visit(ctx.count), ctx.pool?.let { visit(it) }, visit(ctx.elementType))

    /** `pool do` / `pool name do`. `pool` isn't a keyword (it stays usable as a name), so other words are rejected here. */
    override fun visitStatementPool(ctx: EclangParser.StatementPoolContext): ITok {
        // `debug do`, and `debug pool do`, which reads as a pool block named `pool`.
        if (ctx.keyword.text == "debug") return when (ctx.name?.text) {
            null -> TDebug(loc(ctx), visit(ctx.body))
            "pool" -> debugOf(loc(ctx), TStatementPool(loc(ctx), null, visit(ctx.body)))
            else -> throw EcError(loc(ctx), "`debug ${ctx.name.text} do` isn't a statement. `debug do` makes a block that only runs in a debug build; `debug pool ${ctx.name.text} do` a pool block in one.")
        }
        if (ctx.keyword.text != "pool") throw EcError(loc(ctx), "`${ctx.keyword.text}${ctx.name?.let { " " + it.text } ?: ""} do` isn't a statement. A block that starts with a name is a pool block, `pool do` (or `pool name do`), or one that only runs in a debug build, `debug do`.")
        return TStatementPool(loc(ctx), ctx.name?.text, visit(ctx.body))
    }

    companion object {
        /** The words that start a contract. */
        private val KINDS = setOf("requires", "ensures")

        /** `var x: Volatile<T>`: a C `volatile` variable. Not a type of its own, and not a keyword. */
        const val VOLATILE = "Volatile"
    }
}
