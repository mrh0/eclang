package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.ast.token.function.IFuncBody
import github.mrh0.eclang.ast.token.function.TFunc
import github.mrh0.eclang.ast.token.function.TFuncExternal
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.InterfaceMethod
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.Module
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.context.state.InlineConstant
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcGenericNotEstablishedError
import github.mrh0.eclang.error.fn.EcAmbiguousSignatureError
import github.mrh0.eclang.error.fn.EcFunctionNotDefinedError
import github.mrh0.eclang.error.fn.EcInvalidVarArgError
import github.mrh0.eclang.error.fn.EcNoMatchingCallSignatureError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRCCast
import github.mrh0.eclang.ir.IREnumConvert
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRCreateArray
import github.mrh0.eclang.ir.data.IRFormat
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.*

/** A function parameter as written: `name: type = default`, `...name: type` or `...`. */
class Parameter(
    val location: Loc,
    val name: String,
    val type: Type,
    val default: ITok? = null,
    val isVarArg: Boolean = false,
    val isCVarArg: Boolean = false,
)

/**
 * One concrete version of a function, with its own C function. `fn f(a: Int | CString, b: Int = 0)` becomes four
 * overloads: every member of the union, with and without `b`.
 */
class Overload(
    val location: Loc,
    /** The module the function is defined in. */
    val module: Module,
    val name: String,
    val cName: String,
    /** Parameters the caller passes, in order. */
    val params: List<Parameter>,
    /** Parameters the caller left out; inside the body they are their default value. */
    val absent: List<Parameter>,
    val returns: Type,
    val throws: Type?,
    val varArg: Parameter?,
    val body: TBlock?,
    val isExternal: Boolean,
    /** For a generic instance: what each `<T>` stands for. */
    val bindings: Map<String, Type> = mapOf(),
    /**
     * One of several copies made from a union parameter. A check like `v is Int` may then be decided by the
     * types (always false in the CString copy), which is fine here but a likely mistake in ordinary code.
     */
    val isUnionCopy: Boolean = false,
    /** In a union copy: the declared type of each parameter that was split (`v: Int | CString`), for `when`. */
    val declaredUnions: Map<String, Type> = mapOf(),
    /** The names of all the function's parameters, in order, passed or not: arguments in order fill these. */
    val declared: List<String> = listOf(),
    /**
     * Only reachable with named arguments: `fn f(a: Int = 0, b: Int = 0)` called as `f(1)` means a, so the copy that leaves
     * a out and has the same parameter types only serves `f(b = 1)`.
     */
    val namedOnly: Boolean = false,
    /** For `export fn`: the name C calls it by, which is [cName]. */
    val exportName: String? = null,
) {
    /**
     * True when the function has `<T>`, interface or Any parameters, or a `<T>` only in its result. It is never compiled itself; each call
     * compiles an instance for the concrete argument types instead.
     */
    val isTemplate = !isExternal && (params.any { isTemplateType(it.type) } || containsTypeParam(returns) ||
        varArg?.takeIf { !it.isCVarArg }?.let { containsTypeParam(it.type) } == true)

    val instances = HashMap<List<Type>, Overload>()

    fun describe() = "$name(${(params.map { it.type.toString() } + listOfNotNull(varArg?.let { if (it.isCVarArg) "..." else "...${it.type}" })).joinToString(", ")}) at $location"

    companion object {
        fun isTemplateType(t: Type): Boolean = isInterface(t) || t === Types.Any || containsTypeParam(t)

        /** An interface, or a pointer to one (`@Named`): each call gets a copy of the function for the argument's type. */
        fun isInterface(t: Type): Boolean = t is Interface || (t is Pointer && t.target is Interface)

        fun containsTypeParam(t: Type): Boolean = when (t) {
            is TypeParam, is Applied -> true
            is Pointer -> containsTypeParam(t.target)
            is ArrayType -> containsTypeParam(t.element)
            is FixedArray -> containsTypeParam(t.element)
            is Tuple -> t.elements.any { containsTypeParam(it) }
            is Union -> t.members.any { containsTypeParam(it) }
            is FunctionType -> t.params.any { containsTypeParam(it) } || containsTypeParam(t.returns)
            else -> false
        }
    }
}

/** What a call resolved to: the overload, its arguments converted to the parameter types, and the call's type. */
class CallTarget(val overload: Overload, val args: List<IIR>, val type: Type)

/**
 * All functions of the program. Declaring a function registers its overloads; calls are resolved with
 * [TypeContext.cost], so the closest match wins (`f(Short)` picks `f(Int)` over `f(Long)`); and [buildAll]
 * compiles each body exactly once, including generic instances discovered along the way.
 */
class Functions(private val cd: CompileData) {
    private val types get() = cd.types
    /** Overloads by module and name ("fib:add"). */
    private val overloads = LinkedHashMap<String, MutableList<Overload>>()
    /** How many C functions use each base C name, to number them (`add_0`, `add_1`). */
    private val counters = HashMap<String, Int>()
    private val toBuild = ArrayDeque<Overload>()

    private fun key(m: Module, name: String) = "${m.name}:$name"

    /**
     * The overloads a call of [name] can pick from: those of module [qualifier] for `module.name(...)`, otherwise
     * those of the current module and of the top level.
     */
    private fun visible(name: String, qualifier: Module? = null): List<Overload> =
        (if (qualifier != null) listOf(qualifier) else cd.visibleModules()).flatMap { overloads[key(it, name)].orEmpty() }
            .filter { it.module === cd.module || it.name !in it.module.privateFunctions }

    private fun visibleNames(qualifier: Module?): List<String> {
        val prefixes = (if (qualifier != null) listOf(qualifier) else cd.visibleModules()).map { "${it.name}:" }
        return overloads.keys.filter { k -> prefixes.any { k.startsWith(it) } }.map { it.substringAfter(':') }
    }

    private fun notDefined(location: Loc, name: String, qualifier: Module?): Nothing {
        (qualifier ?: cd.topLevel).takeIf { name in it.privateFunctions && it !== cd.module }?.let {
            throw EcError(location, "'$name' is private to $it (`priv fn`), so only code in it can call it. Remove `priv` to use it here.")
        }
        val hint = if (qualifier != null) CompileData.suggestion(name, visibleNames(qualifier))
            else cd.whereElse(name) { overloads.containsKey(key(it, name)) }.ifEmpty { CompileData.suggestion(name, visibleNames(null)) }
                .ifEmpty { suggestionElsewhere(name) }
        throw EcFunctionNotDefinedError(location, name, qualifier?.toString(), hint)
    }

    /** " Did you mean 'io.log'?" when [name] is close to a public function of a module that isn't visible here, or "". */
    private fun suggestionElsewhere(name: String): String {
        val visible = cd.visibleModules().toSet()
        val qualified = overloads.values.flatten().filter { it.module !in visible && it.name !in it.module.privateFunctions }
            .associate { it.name to "${it.module.name}.${it.name}" }
        return CompileData.closest(name, qualified.keys)?.let { " Did you mean '${qualified[it]}'?" } ?: ""
    }

    /** Whether a function called [name] can be called from here. */
    fun exists(name: String, qualifier: Module? = null) = visible(name, qualifier).isNotEmpty()

    fun main(entry: Module): Overload? = overloads[key(entry, "main")]?.firstOrNull { it.params.isEmpty() && it.varArg == null }

    fun declare(func: TFunc) {
        val name = testIdentifier(func.location, func.name)
        val (params, declaredReturn) = func.processSignature(cd)
        val (returns, throws) = if (declaredReturn is Result) declaredReturn.value to declaredReturn.error else declaredReturn to null
        val isExternal = func is TFuncExternal

        params.forEachIndexed { i, p -> if ((p.isVarArg || p.isCVarArg) && i != params.size - 1) throw EcInvalidVarArgError(p.location, name) }
        val varArg = params.lastOrNull()?.takeIf { it.isVarArg || it.isCVarArg }
        val fixed = if (varArg != null) params.dropLast(1) else params
        if (isExternal) (fixed.map { it.type } + returns).firstOrNull { CTypes.fixedOf(it) != null }?.let {
            throw EcError(func.location, "C functions don't take or return arrays by value, so '$name' can't use a $it: in C, an `int a[4]` parameter is a pointer. Declare it as a pointer (`T[]c` or `@T`) and pass `a.data`.")
        }
        func.exportName?.let { checkExport(func, name, it, returns, fixed, varArg) }
        if (returns === Types.Never && throws != null) throw EcError(func.location, "'$name' returns Never, so it never comes back, not even with an error. Remove `throws`, or give it another return type.")
        if (returns !== Types.None && returns !== Types.Never && !Overload.containsTypeParam(returns)) cd.storable(func.location, returns, "return type of '$name'")
        throws?.let { cd.storable(func.location, it, "error type of '$name'") }
        varArg?.takeIf { it.isVarArg && !Overload.containsTypeParam(it.type) }?.let { cd.storable(it.location, it.type, "the variadic parameter '${it.name}'") }

        // Every combination of union members, and of passing or leaving out parameters with defaults.
        var combinations: List<List<Parameter?>> = listOf(listOf())
        var isUnionCopy = false
        for (p in fixed) {
            val split = variants(p.type)
            if (split.size > 1) isUnionCopy = true
            val options: List<Parameter?> = split.map { Parameter(p.location, p.name, it, p.default) } +
                if (p.default != null) listOf(null) else listOf()
            combinations = combinations.flatMap { combo -> options.map { combo + it } }
        }

        val copies = fixed.fold(1) { n, p -> n * variants(p.type).size }
        if (copies > MANY_COPIES) {
            val split = fixed.filter { variants(it.type).size > 1 }
            val factors = split.joinToString(" × ") { variants(it.type).size.toString() } + " for " + split.joinToString(", ") { it.name }
            cd.warn(func.location, "'$name' is compiled $copies times, once for every combination of the types of its union " +
                "parameters ($factors). Only the copies the program calls are written to the C file, but each one is checked. " +
                "If the body treats the types alike, give the function fewer union parameters, e.g. convert the values before the call.")
        }

        val module = cd.module
        val list = overloads.getOrPut(key(module, name)) { mutableListOf() }
        val earlier = list.size
        for (combo in combinations) {
            val present = combo.filterNotNull()
            val absent = fixed.filterIndexed { i, _ -> combo[i] == null }
            val same = list.indexOfFirst { it.params.map { p -> p.type } == present.map { p -> p.type } && (it.varArg == null) == (varArg == null) }
            if (same in 0 until earlier) throw EcError(func.location, "Function $name(${present.joinToString { it.type.toString() }}) is already defined at ${list[same].location}.")
            val base = module.cName(name)
            val index = counters.merge(base, 1, Int::plus)!! - 1
            val cName = func.exportName ?: if (isExternal) (func.getSourceName() ?: name) else "${base}_$index"
            val body = if (func is IFuncBody) func.getBody().also { it.addressTaken = func.addressTaken } else null
            val declaredUnions = fixed.filter { variants(it.type).size > 1 }.associate { it.name to it.type }
            val overload = Overload(func.location, module, name, cName, present, absent, returns, throws, varArg, body, isExternal, isUnionCopy = isUnionCopy, declaredUnions = declaredUnions,
                declared = fixed.map { it.name }, namedOnly = same >= 0, exportName = func.exportName)
            list += overload
            if (overload.exportName != null) { cd.claimCName(func.location, cName, "the exported function '$name'", renamable = false); exported += overload }
            cd.reserveCName(cName)
            if (!isExternal && !overload.isTemplate) toBuild += overload
        }
    }

    /** The `export fn`s, in the order they were declared. */
    val exported = ArrayList<Overload>()

    /** C has to see an exported function as one function with one signature, so what makes eclang compile copies of it can't be used. */
    private fun checkExport(func: TFunc, name: String, cName: String, returns: Type, params: List<Parameter>, varArg: Parameter?) {
        fun reject(why: String): Nothing = throw EcError(func.location, "'$name' can't be exported: $why")
        if (func is TFuncExternal) reject("it is already a C function.")
        if (cName == "main") reject("C's `main` is the one eclang writes to start the program. Give it another name with `export extern \"name\" fn`.")
        if (varArg != null) reject("C can't call a function with `...`. Take an array instead.")
        params.firstOrNull { it.default != null }?.let { reject("C has no default values, so parameter '${it.name}' must be given. Remove its default, and keep a function with a default next to it if eclang code needs one.") }
        params.firstOrNull { Overload.isTemplateType(it.type) }?.let { reject("parameter '${it.name}' is a ${it.type}, so each call compiles a copy of the function. Export a function for each concrete type you need.") }
        if (Overload.containsTypeParam(returns)) reject("it returns a $returns, so each call compiles a copy of the function. Export a function for each concrete type you need.")
        params.firstOrNull { variants(it.type).size > 1 }?.let { reject("parameter '${it.name}' is a ${it.type}, which makes one C function per type. Export a function for each type, or take a single type.") }
    }

    /**
     * The parameter types a union parameter is split into, one overload each: `Int | CString | Null` becomes
     * Int, CString and Null. Atoms stay together (they are all the same C type), and so does a plain `T?`.
     */
    private fun variants(type: Type): List<Type> {
        if (type !is Union) return listOf(type)
        val values = type.members.filter { it !== Types.Null }
        val atoms = values.filter { it === Types.Atom || it is AtomLiteral }
        val groups = (values - atoms.toSet()) + if (atoms.isEmpty()) listOf() else listOf(types.union(atoms))
        if (groups.size <= 1) return listOf(type)
        return groups + if (type.isNullable) listOf(Types.Null) else listOf()
    }

    // ---------------------------------------------------------------------------------------------------
    // Calls.

    private val genericCost = 5

    companion object {
        /** Above this many copies of one function (from splitting its union parameters), declaring it gives a warning. */
        const val MANY_COPIES = 16
        private const val ROOT = ""
    }

    /** The parameter type to use as a hint for argument [index], when every candidate agrees on it. */
    fun hintFor(name: String, argCount: Int, index: Int, qualifier: Module? = null): Type {
        val hints = visible(name, qualifier).filter { fitsArity(it, argCount) }.map { it.params.getOrNull(index)?.type ?: it.varArg?.type }
        val first = hints.firstOrNull() ?: return Types.None
        return if (hints.all { it === first } && !Overload.isTemplateType(first)) first else Types.None
    }

    /** The type to use as a hint for the argument named [parameter], when every candidate agrees on it. */
    fun hintForName(name: String, parameter: String, qualifier: Module? = null): Type {
        val hints = visible(name, qualifier).mapNotNull { o -> o.params.firstOrNull { it.name == parameter }?.type }
        val first = hints.firstOrNull() ?: return Types.None
        return if (hints.all { it === first } && !Overload.isTemplateType(first)) first else Types.None
    }

    /**
     * [args] in the order of [o]'s parameters, or null when [o] doesn't take them. [names] gives each argument's parameter
     * (null: it fills the next parameter in order), or is null when no argument has one.
     */
    private fun <T> arrange(o: Overload, args: List<T>, names: List<String?>?): List<T>? {
        if (!fitsArity(o, args.size)) return null
        if (names == null) return if (o.namedOnly) null else args
        if (args.size != o.params.size) return null
        val inOrder = names.count { it == null }
        if (o.params.take(inOrder).map { it.name } != o.declared.take(inOrder)) return null
        return args.take(inOrder) + o.params.drop(inOrder).map { p -> args[names.indexOf(p.name).takeIf { it >= 0 } ?: return null] }
    }

    /** Why no overload takes the named arguments [names], or null when the usual error says it better. */
    private fun namedProblem(name: String, candidates: List<Overload>, names: List<String?>): String? {
        val parameters = candidates.flatMap { it.declared }.distinct()
        names.filterNotNull().firstOrNull { it !in parameters }?.let {
            return "$name(...) has no parameter called '$it'.${CompileData.suggestion(it, parameters)} Its parameters are: ${parameters.joinToString(", ")}."
        }
        val inOrder = names.count { it == null }
        names.filterNotNull().firstOrNull { n -> candidates.all { n in it.declared.take(inOrder) } }?.let {
            return "Parameter '$it' of $name(...) is given twice: by an argument in order, and by name."
        }
        val required = candidates.flatMap { o -> o.declared.drop(inOrder).filter { p -> p !in names && o.absent.none { it.name == p } && o.params.any { it.name == p } && candidates.none { c -> c.absent.any { it.name == p } } } }
        return required.firstOrNull()?.let { "$name(...) also needs its parameter '$it', which has no default value." }
    }

    private fun fitsArity(o: Overload, count: Int) = if (o.varArg != null) count >= o.params.size else count == o.params.size

    /**
     * The function type a lambda without parameter types gets as argument [index] of a generic function: what the
     * [known] argument types (null: not compiled yet) make of its parameter, `(Int) => None` for `<T>[]` and `(<T>) => <U>`
     * called with an `Int[]`. A return type that is still generic comes from the lambda, so it is left open (None).
     */
    fun lambdaHint(name: String, count: Int, index: Int, known: List<Type?>, qualifier: Module? = null): Type {
        val hints = visible(name, qualifier).filter { fitsArity(it, count) }.mapNotNull { o ->
            val param = o.params.getOrNull(index)?.type as? FunctionType ?: return@mapNotNull null
            val bindings = mutableMapOf<TypeParam, Type>()
            val fits = known.withIndex().all { (i, t) ->
                val p = o.params.getOrNull(i)?.type
                t == null || p == null || !Overload.containsTypeParam(p) || types.bind(p, t, bindings)
            }
            val bound = types.substitute(param, bindings) as FunctionType
            if (!fits || bound.params.any { Overload.containsTypeParam(it) }) null
            else types.function(bound.params, if (Overload.containsTypeParam(bound.returns)) Types.None else bound.returns, bound.throws)
        }.distinct()
        return hints.singleOrNull() ?: Types.None
    }

    /**
     * Resolves a call of [name] with [args] (already compiled) to the best overload. [qualifier]: the module of a
     * qualified call, `fib.add(1, 2)`. [expected]: the type wanted of the result, which decides a `<T>` that only the
     * return type has (`val names: @List<String> = list()`). [names]: the parameter each argument is for, if any has one.
     */
    fun call(location: Loc, name: String, written: List<Pair<Type, IIR>>, qualifier: Module? = null, expected: Type = Types.None, names: List<String?>? = null): CallTarget {
        val candidates = visible(name, qualifier).ifEmpty { notDefined(location, name, qualifier) }
        written.forEachIndexed { i, a -> cd.usable(location, a, "argument ${i + 1} of '$name'") }

        val scored = candidates.mapNotNull { o ->
            val ordered = arrange(o, written, names) ?: return@mapNotNull null
            val bindings = mutableMapOf<TypeParam, Type>()
            score(o, ordered.map { it.first }, bindings)?.let { Scored(o, it, bindings, ordered) }
        }
        if (scored.isEmpty()) {
            names?.let { namedProblem(name, candidates, it) }?.let { throw EcError(location, it) }
            val types = written.map { it.first }
            throw EcNoMatchingCallSignatureError(location, name, types, if (names == null) whyNoMatch(candidates, types) else "Available: ${candidates.joinToString("; ") { it.describe() }}.")
        }
        val best = scored.minOf { it.cost }
        // On a tie, a concrete function beats a generic one, and the current module's function beats the top level's.
        val winners = scored.filter { it.cost == best }
            .let { w -> w.filter { !it.overload.isTemplate }.ifEmpty { w } }
            .let { w -> w.filter { it.overload.module === cd.module }.ifEmpty { w } }
            // Copies of one function, made for its default values: arguments in order fill its parameters in order.
            .let { w -> if (w.distinctBy { it.overload.location }.size > 1) w else w.filter { (o) -> o.params.map { it.name } == o.declared.take(o.params.size) }.ifEmpty { w } }
        if (winners.size > 1) throw EcAmbiguousSignatureError(location, name, written.map { it.first }, winners.map { it.overload.describe() })

        val (chosen, _, bindings, args) = winners[0]
        val argTypes = args.map { it.first }
        val target = if (chosen.isTemplate) instantiate(location, chosen, argTypes, bindings, expected) else chosen
        val irs = target.params.mapIndexed { i, p ->
            val ir = cd.expect(location, args[i], p.type, "argument ${i + 1} of '$name'")
            val enum = if (target.isExternal) cd.enums[p.type] else null
            when {
                enum != null -> IREnumConvert(location, enum, ir, toC = true)
                target.isExternal && constSensitive(p.type) -> IRCCast(location, ir, null)
                else -> ir
            }
        }.toMutableList()
        val extra = args.drop(target.params.size)
        target.varArg?.let { va ->
            if (va.isCVarArg) extra.forEachIndexed { i, a ->
                val t = cd.storable(location, a.first, "argument ${target.params.size + i + 1} of '$name'")
                if (target.isExternal) cVarArgProblem(t)?.let { throw EcError(location, "Argument ${target.params.size + i + 1} of '$name' is a $t, which C's `...` can't take: $it") }
                irs += cd.expect(location, a, t, "argument ${target.params.size + i + 1} of '$name'")
            } else {
                val values = extra.mapIndexed { i, a -> cd.expect(location, a, va.type, "argument ${target.params.size + i + 1} of '$name'") }
                if (target.isExternal) irs += listOf(IRInt(location, extra.size)) + values
                // An eclang function gets them as one array parameter, `...rest: T` is a `T[]`.
                else irs += IRCreateArray(location, IRType(location, types.array(va.type)), IRType(location, va.type), values)
            }
        }
        val type = target.throws?.let { types.result(target.returns, it) } ?: target.returns
        if (!target.isExternal) use(target.cName)
        return CallTarget(target, irs, type)
    }

    /**
     * C types where eclang's type and the C declaration may differ only in `const` (eclang has none), which C
     * doesn't convert: a pointer to a pointer (`char**` vs `const char**`) and function pointers. Arguments of
     * these types are passed to C functions as `void*`; eclang has already checked them against the declaration.
     */
    private fun constSensitive(t: Type): Boolean = t is FunctionType || (t is Pointer && pointerLike(t.target)) ||
        (t is Union && t.isNullable && types.nonNull(t).let { it !is Union && constSensitive(it) })

    private fun pointerLike(t: Type): Boolean = t is Pointer || t === Types.CString || t is FunctionType || (t is ArrayType && t.isC) ||
        (t is Union && t.isNullable && types.nonNull(t).let { it !is Union && pointerLike(it) })

    /** A C function returning `const char*` (or another const pointer) gives an eclang value without the const. */
    fun returnsConst(o: Overload): Boolean = o.isExternal && o.throws == null && pointerLike(o.returns)

    /**
     * Why a value of type [t] can't be passed to a C function's `...` (C reads it as a number or a pointer, as its
     * format says), with the fix, or null when it can.
     */
    private fun cVarArgProblem(t: Type): String? = when {
        t === Types.String -> "a String is a struct with a length, and its bytes need not end with a 0. Use `%.*s` with `s.len as Int, s.data`, or put it in a String with `\${...}` and use that."
        t is ArrayType && !t.isC -> "an array is a struct with a length. Pass its elements' memory with `.data` (a C array)."
        t is FixedArray -> "C can't take an array by value. Pass its elements' memory with `.data`."
        t is Declared -> "C functions with `...` take numbers and pointers. Pass its fields instead, or a pointer to it (`addrof x`)."
        t is Union && !isPlainUnion(t) -> "a tagged union is a struct. Check what it holds first (`if x is T do`), or use `x ?? default`."
        t is Tuple || t is Result -> "it is a struct. Pass its parts instead."
        else -> null
    }

    /** A union C holds as one pointer (`T?` of a pointer type, atoms), see CTypes.isPlain. */
    private fun isPlainUnion(u: Union): Boolean {
        val values = u.members.filter { it !== Types.Null }
        return values.all { it === Types.Atom || it is AtomLiteral } ||
            (values.size == 1 && values[0].let { it is Pointer || it === Types.CString || it === Types.Atom || it is AtomLiteral || (it is ArrayType && it.isC) || it is FunctionType })
    }

    /** An overload a call fits: what it costs, what its `<T>`s stand for, and the arguments in the order of its parameters. */
    private data class Scored(val overload: Overload, val cost: Int, val bindings: Map<TypeParam, Type>, val args: List<Pair<Type, IIR>>)

    /** The (record, interface) pairs being checked by [missingMethod], so a method taking the interface itself doesn't loop. */
    private val checking = HashSet<Pair<Type, Type>>()

    /**
     * The first function interface [iface] (or `@Interface`) asks for that can't be called on an [arg] (`arg.name(...)`
     * with the interface's parameter types, returning its type), or null when the record has them all.
     */
    fun missingMethod(arg: Type, iface: Type): InterfaceMethod? {
        val i = (if (iface is Pointer) iface.target else iface) as? Interface ?: return null
        val methods = cd.interfaceMethods[i] ?: return null
        if (!checking.add(arg to i)) return null
        try {
            return methods.firstOrNull { m ->
                visible(m.name).none { o ->
                    fitsArity(o, m.params.size + 1) && score(o, listOf(arg) + m.params, HashMap()) != null &&
                        (m.returns === Types.None || Overload.containsTypeParam(o.returns) || types.cost(resultOf(o), m.returns) != TypeContext.NO)
                }
            }
        } finally {
            checking.remove(arg to i)
        }
    }

    /** What a call of [o] gives: its return type, as a Result when it can fail. */
    private fun resultOf(o: Overload): Type = o.throws?.let { types.result(o.returns, it) } ?: o.returns

    /** Total cost of calling [o] with [argTypes], or null when it doesn't fit. Generic parameters cost extra. */
    private fun score(o: Overload, argTypes: List<Type>, bindings: MutableMap<TypeParam, Type>): Int? {
        var total = 0
        for ((i, a) in argTypes.withIndex()) {
            val p = o.params.getOrNull(i)
            val cost = when {
                p == null -> when {
                    o.varArg!!.isCVarArg -> 0
                    Overload.containsTypeParam(o.varArg.type) -> if (types.bind(o.varArg.type, a, bindings)) genericCost else TypeContext.NO
                    else -> types.cost(a, o.varArg.type)
                }
                Overload.containsTypeParam(p.type) -> if (types.bind(p.type, a, bindings)) genericCost else TypeContext.NO
                Overload.isInterface(p.type) -> types.cost(a, p.type).let { if (it == TypeContext.NO || missingMethod(a, p.type) != null) TypeContext.NO else it + genericCost }
                p.type === Types.Any && !o.isExternal -> genericCost
                else -> types.cost(a, p.type)
            }
            if (cost == TypeContext.NO) return null
            total += cost
        }
        return total
    }

    private fun whyNoMatch(candidates: List<Overload>, argTypes: List<Type>): String {
        val sameArity = candidates.filter { fitsArity(it, argTypes.size) }
        if (sameArity.size == 1) {
            val o = sameArity[0]
            argTypes.forEachIndexed { i, a ->
                val p = o.params.getOrNull(i)?.type ?: o.varArg?.takeIf { !it.isCVarArg }?.type ?: return@forEachIndexed
                if (!Overload.containsTypeParam(p) && p !== Types.Any && !types.accepts(p, a))
                    return "Argument ${i + 1} should be $p: ${types.explain(a, p)}."
                if (Overload.isInterface(p)) missingMethod(a, p)?.let { m ->
                    val params = (listOf("x: $a") + m.params.mapIndexed { j, t -> "p${j + 1}: $t" }).joinToString(", ")
                    val returns = (m.returns as? Result)?.let { r -> (if (r.value === Types.None) "" else ": ${r.value}") + " throws ${r.error}" } ?: if (m.returns === Types.None) "" else ": ${m.returns}"
                    return "Argument ${i + 1} should be $p, which asks for `$m`, but there is no function '${m.name}' for a $a. Define one: `fn ${m.name}($params)$returns`."
                }
            }
        }
        return "Available: ${candidates.joinToString("; ") { it.describe() }}."
    }

    /** The instance of template [o] for these argument types, compiled later by [buildAll]. */
    private fun instantiate(location: Loc, o: Overload, argTypes: List<Type>, argBindings: Map<TypeParam, Type>, expected: Type): Overload {
        val bindings = argBindings.toMutableMap()
        if (Overload.containsTypeParam(types.substitute(o.returns, bindings)) && expected !== Types.None) types.bind(o.returns, types.nonNull(expected), bindings)
        val paramTypes = o.params.mapIndexed { i, p ->
            when {
                Overload.containsTypeParam(p.type) -> types.substitute(p.type, bindings)
                Overload.isInterface(p.type) || p.type === Types.Any -> cd.storable(location, argTypes[i], "argument ${i + 1} of '${o.name}'")
                else -> p.type
            }
        }
        val returns = types.substitute(o.returns, bindings)
        if (Overload.containsTypeParam(returns)) throw EcGenericNotEstablishedError(location, returns.toString())
        val varArg = o.varArg?.let { Parameter(it.location, it.name, types.substitute(it.type, bindings), null, it.isVarArg, it.isCVarArg) }
        varArg?.takeIf { Overload.containsTypeParam(it.type) }?.let { throw EcGenericNotEstablishedError(location, it.type.toString()) }
        // The result and the variadic part count: `list()` is a different function for each element type.
        return o.instances.getOrPut(paramTypes + returns + listOfNotNull(varArg?.type)) {
            val params = o.params.mapIndexed { i, p -> Parameter(p.location, p.name, paramTypes[i], p.default) }
            Overload(o.location, o.module, o.name, "${o.cName}_g${o.instances.size}", params, o.absent, returns, o.throws, varArg,
                o.body, false, bindings.mapKeys { it.key.name }, isUnionCopy = true, declared = o.declared).also { toBuild += it; cd.reserveCName(it.cName) }
        }
    }

    /** Whether a function [name] can be called with one [argument]. */
    fun accepts(name: String, argument: Type, qualifier: Module? = null) = visible(name, qualifier).any { !it.namedOnly && fitsArity(it, 1) && score(it, listOf(argument), HashMap()) != null }

    /**
     * The call `name(argument)` that a language feature makes for a type (`next` and `iter` of `for`), or null when nothing takes
     * [argument]. The function may be in the module that declares the type, as the feature can be used anywhere the value gets to.
     */
    fun protocolCall(location: Loc, name: String, argument: Pair<Type, IIR>): CallTarget? {
        if (accepts(name, argument.first)) return call(location, name, listOf(argument))
        return owner(argument.first)?.takeIf { accepts(name, argument.first, it) }?.let { call(location, name, listOf(argument), it) }
    }

    /** For each type printed so far, the `str` function that prints it, or null when CTypes.show generates that. */
    val printers = HashMap<Type, Overload?>()

    /** Resolves the `str` functions used to print a [t] in `${...}`, and those of everything it holds (see CTypes.show). */
    fun resolvePrinters(location: Loc, t: Type, seen: MutableSet<Type> = HashSet()) {
        if (!seen.add(t) || IRFormat.spec(t) != null) return
        if (t is Pointer) (t.target as? Record)?.descendants?.drop(1)?.forEach { resolvePrinters(location, types.pointer(it), seen) }
        val own = if (t is Record || t is Tuple || t is Pointer || t is Distinct) printers.getOrPut(t) { findPrinter(location, t) } else null
        if (own != null) { use(own.cName); return }
        when (t) {
            is Distinct -> resolvePrinters(location, t.base, seen)
            is Record -> if (!t.isExternal) t.fields.values.forEach { resolvePrinters(location, it, seen) }
            is Tuple -> t.elements.forEach { resolvePrinters(location, it, seen) }
            is ArrayType -> if (!t.isC) resolvePrinters(location, t.element, seen)
            is FixedArray -> resolvePrinters(location, t.element, seen)
            is Union -> t.members.forEach { resolvePrinters(location, it, seen) }
            is Pointer -> resolvePrinters(location, t.target, seen)
            else -> {}
        }
    }

    /**
     * The `str` that prints a [t]: from the module that declares it, or the top level, wherever the value is printed, so one
     * type prints one way in the whole program. A record without a `str` of its own also uses one that takes a pointer to it.
     */
    private fun findPrinter(location: Loc, t: Type): Overload? {
        val modules = listOfNotNull(owner(t), cd.topLevel).distinct()
        for (form in if (t is Record || t is Tuple) listOf(t, types.pointer(t)) else listOf(t)) for (m in modules) {
            val best = overloads[key(m, "str")].orEmpty().filter { !it.isExternal && !it.namedOnly && it.params.size == 1 && it.varArg == null }.mapNotNull { o ->
                val bindings = mutableMapOf<TypeParam, Type>()
                score(o, listOf(form), bindings)?.let { Scored(o, it, bindings, listOf()) }
            }.minByOrNull { it.cost } ?: continue
            val target = if (best.overload.isTemplate) instantiate(location, best.overload, listOf(form), best.bindings, Types.None) else best.overload
            if (target.returns !== Types.String || target.throws != null)
                throw EcError(target.location, "`\${...}` prints a $t with `str`, so `str` must return a String and can't fail. Give it the result type String, or call it something else.")
            return target
        }
        return null
    }

    /** The module that declares [t], when a module does. */
    private fun owner(t: Type): Module? = when (t) {
        is Declared -> cd.findModule(t.namespace)
        is Distinct -> cd.findModule(t.namespace)
        is Pointer -> owner(t.target)
        else -> null
    }

    /** The single overload of [name], or null when there are none or several. */
    fun onlyOverload(name: String, qualifier: Module? = null): Overload? = visible(name, qualifier).singleOrNull()?.takeIf { it.varArg == null && !it.isTemplate }

    /** The function type of [o], e.g. `(Int) => Int`. */
    fun typeOf(o: Overload): FunctionType = types.function(o.params.map { it.type }, o.returns, o.throws)

    /** `fn name` as a value of function type [signature]: the overload with exactly that signature. */
    fun reference(location: Loc, name: String, signature: FunctionType, qualifier: Module? = null): Overload {
        val candidates = visible(name, qualifier).ifEmpty { notDefined(location, name, qualifier) }
        return candidates.firstOrNull { o ->
            o.varArg == null && !o.isTemplate && o.params.map { it.type } == signature.params &&
                o.returns === signature.returns && o.throws === signature.throws
        }?.also { if (!it.isExternal) use(it.cName) } ?: throw EcError(location, "No function $name has the type $signature. Available: ${candidates.joinToString("; ") { it.describe() }}.")
    }

    // ---------------------------------------------------------------------------------------------------
    // Compiling bodies.

    /** The C functions of lambdas (`fn (x: Int) = x + 1`), compiled where they appear. */
    private val lambdas = ArrayList<IIR>()

    private var lambdaCounter = 0
    /** The C names of the lambdas being compiled, innermost last. */
    private val lambdaOwners = ArrayList<String>()

    /** A fresh C name for a lambda, after the function (or lambda) it is in: `main_0_lambda0`. */
    fun lambdaName(): String = "${lambdaOwners.lastOrNull() ?: cd.function?.cName ?: cd.module.cName("global")}_lambda${lambdaCounter++}"

    /** Compiles the body of the lambda [cName] with [compile], then adds its C function [make] returns. */
    fun <T> lambda(cName: String, compile: () -> T, make: (T) -> IIR): T {
        use(cName)
        lambdaOwners += cName
        val value = try { compile() } finally { lambdaOwners.removeAt(lambdaOwners.size - 1) }
        lambdas += make(value)
        return value
    }

    /** The functions (and lambdas) each function uses, by C name; [ROOT] stands for the code outside functions. */
    private val uses = HashMap<String, MutableSet<String>>()

    /** Notes that the code being compiled calls or refers to the function [cName]. */
    private fun use(cName: String) {
        uses.getOrPut(lambdaOwners.lastOrNull() ?: cd.function?.cName ?: ROOT) { HashSet() } += cName
    }

    /**
     * The C functions among [functions] that `main` (null in a library), a global or an exported function can reach. The others
     * (most often copies made for union parameters that no call needs) are left out of the C file. They were still compiled, so
     * their errors are reported.
     */
    fun reachable(main: Overload?, functions: List<IIR>): List<IIR> {
        val seen = HashSet<String>()
        val waiting = ArrayDeque(listOfNotNull(ROOT, main?.cName) + exported.map { it.cName })
        while (waiting.isNotEmpty()) {
            val next = waiting.removeFirst()
            if (seen.add(next)) uses[next]?.let { waiting.addAll(it) }
        }
        return functions.filter { it !is IRFunctionOverride || it.id in seen }
    }

    /** Compiles every function body, including generic instances that calls ask for while compiling, and the lambdas in them. */
    fun buildAll(): List<IIR> {
        val built = ArrayList<IIR>()
        while (toBuild.isNotEmpty()) {
            cd.recover { build(toBuild.removeFirst()) }?.let { built += it }
            cd.function = null
        }
        return built + lambdas
    }

    private fun build(o: Overload): IIR = cd.inModule(o.module) {
        cd.newContext(o.cName, o.bindings)
        cd.function = o
        cd.functionHasErrors = false
        cd.addressTaken = o.body!!.addressTaken
        o.params.forEach { cd.ctx().define(it.location, Constant(it.name, it.type, cd.localCName(it.name), kind = "a parameter")) }
        // `...rest: T` is the array of the extra arguments, the last C parameter.
        val rest = o.varArg?.takeIf { !it.isCVarArg }?.let { Parameter(it.location, it.name, types.array(it.type)) }
        rest?.let {
            cd.ctx().define(it.location, Constant(it.name, it.type, cd.localCName(it.name), kind = "a parameter"))
            Lifetimes.defineRest(cd, it.name)
        }
        o.absent.forEach { p ->
            val value = cd.expect(p.location, p.default!!.process(cd, p.type), p.type, "default value of '${p.name}'")
            cd.ctx().define(p.location, InlineConstant(p.name, p.type, value))
            Lifetimes.define(cd, p.name, p.default, p.type)
        }
        val block = o.body!!.process(cd, Types.None).second as IRBlock
        if (o.returns !== Types.None && !block.diverges && !cd.functionHasErrors)
            throw EcError(o.location, if (o.returns === Types.Never) "Function ${o.name} is declared `Never`, so it must not reach its end. End every path with something that doesn't come back, such as `exit(1)`, a call of another `Never` function, or a loop that doesn't end."
                else "Function ${o.name} returns ${o.returns}, so every path through it must end with `ret`.")
        cd.function = null
        cd.addressTaken = mapOf()
        val callType = o.throws?.let { types.result(o.returns, it) } ?: o.returns
        val params = IRParameters(o.location, (o.params + listOfNotNull(rest)).map { IRParameter(it.location, cd.localCName(it.name), it.type, null) })
        IRFunctionOverride(o.location, block, o.cName, params, callType, o.throws != null && o.returns === Types.None, o.varArg?.isCVarArg == true, o.exportName != null)
    }
}
