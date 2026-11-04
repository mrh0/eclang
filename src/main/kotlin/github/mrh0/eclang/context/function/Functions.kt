package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.ast.token.function.IFuncBody
import github.mrh0.eclang.ast.token.function.TFunc
import github.mrh0.eclang.ast.token.function.TFuncExternal
import github.mrh0.eclang.context.CompileData
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
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
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
) {
    /**
     * True when the function has `<T>`, interface or Any parameters. It is never compiled itself; each call
     * compiles an instance for the concrete argument types instead.
     */
    val isTemplate = !isExternal && params.any { isTemplateType(it.type) }

    val instances = HashMap<List<Type>, Overload>()

    fun describe() = "$name(${(params.map { it.type.toString() } + listOfNotNull(varArg?.let { if (it.isCVarArg) "..." else "...${it.type}" })).joinToString(", ")}) at $location"

    companion object {
        fun isTemplateType(t: Type): Boolean = isInterface(t) || t === Types.Any || containsTypeParam(t)

        /** An interface, or a pointer to one (`@Named`): each call gets a copy of the function for the argument's type. */
        fun isInterface(t: Type): Boolean = t is Interface || (t is Pointer && t.target is Interface)

        fun containsTypeParam(t: Type): Boolean = when (t) {
            is TypeParam -> true
            is Pointer -> containsTypeParam(t.target)
            is ArrayType -> containsTypeParam(t.element)
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

    private fun visibleNames(qualifier: Module?): List<String> {
        val prefixes = (if (qualifier != null) listOf(qualifier) else cd.visibleModules()).map { "${it.name}:" }
        return overloads.keys.filter { k -> prefixes.any { k.startsWith(it) } }.map { it.substringAfter(':') }
    }

    private fun notDefined(location: Loc, name: String, qualifier: Module?): Nothing {
        val hint = if (qualifier != null) CompileData.suggestion(name, visibleNames(qualifier))
            else cd.whereElse(name) { overloads.containsKey(key(it, name)) }.ifEmpty { CompileData.suggestion(name, visibleNames(null)) }
        throw EcFunctionNotDefinedError(location, name, qualifier?.toString(), hint)
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
        if (returns !== Types.None && !Overload.containsTypeParam(returns)) cd.storable(func.location, returns, "return type of '$name'")
        throws?.let { cd.storable(func.location, it, "error type of '$name'") }
        varArg?.takeIf { it.isVarArg }?.let { cd.storable(it.location, it.type, "the variadic parameter '${it.name}'") }

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
        for (combo in combinations) {
            val present = combo.filterNotNull()
            val absent = fixed.filterIndexed { i, _ -> combo[i] == null }
            list.firstOrNull { it.params.map { p -> p.type } == present.map { p -> p.type } && (it.varArg == null) == (varArg == null) }?.let {
                throw EcError(func.location, "Function $name(${present.joinToString { it.type.toString() }}) is already defined at ${it.location}.")
            }
            val base = module.cName(name)
            val index = counters.merge(base, 1, Int::plus)!! - 1
            val cName = if (isExternal) (func.getSourceName() ?: name) else "${base}_$index"
            val body = if (func is IFuncBody) func.getBody() else null
            val overload = Overload(func.location, module, name, cName, present, absent, returns, throws, varArg, body, isExternal, isUnionCopy = isUnionCopy)
            list += overload
            cd.reserveCName(cName)
            if (!isExternal && !overload.isTemplate) toBuild += overload
        }
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

    private fun fitsArity(o: Overload, count: Int) = if (o.varArg != null) count >= o.params.size else count == o.params.size

    /**
     * Resolves a call of [name] with [args] (already compiled) to the best overload. [qualifier]: the module of a
     * qualified call, `fib.add(1, 2)`.
     */
    fun call(location: Loc, name: String, args: List<Pair<Type, IIR>>, qualifier: Module? = null): CallTarget {
        val candidates = visible(name, qualifier).ifEmpty { notDefined(location, name, qualifier) }
        args.forEachIndexed { i, a -> cd.usable(location, a, "argument ${i + 1} of '$name'") }
        val argTypes = args.map { it.first }

        val scored = candidates.filter { fitsArity(it, args.size) }.mapNotNull { o ->
            val bindings = mutableMapOf<TypeParam, Type>()
            score(o, argTypes, bindings)?.let { Triple(o, it, bindings) }
        }
        if (scored.isEmpty()) throw EcNoMatchingCallSignatureError(location, name, argTypes, whyNoMatch(candidates, argTypes))
        val best = scored.minOf { it.second }
        // On a tie, a concrete function beats a generic one, and the current module's function beats the top level's.
        val winners = scored.filter { it.second == best }
            .let { w -> w.filter { !it.first.isTemplate }.ifEmpty { w } }
            .let { w -> w.filter { it.first.module === cd.module }.ifEmpty { w } }
        if (winners.size > 1) throw EcAmbiguousSignatureError(location, name, argTypes, winners.map { it.first.describe() })

        val (chosen, _, bindings) = winners[0]
        val target = if (chosen.isTemplate) instantiate(location, chosen, argTypes, bindings) else chosen
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

    /** Total cost of calling [o] with [argTypes], or null when it doesn't fit. Generic parameters cost extra. */
    private fun score(o: Overload, argTypes: List<Type>, bindings: MutableMap<TypeParam, Type>): Int? {
        var total = 0
        for ((i, a) in argTypes.withIndex()) {
            val p = o.params.getOrNull(i)
            val cost = when {
                p == null -> if (o.varArg!!.isCVarArg) 0 else types.cost(a, o.varArg.type)
                Overload.containsTypeParam(p.type) -> if (types.bind(p.type, a, bindings)) genericCost else TypeContext.NO
                Overload.isInterface(p.type) -> types.cost(a, p.type).let { if (it == TypeContext.NO) it else it + genericCost }
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
            }
        }
        return "Available: ${candidates.joinToString("; ") { it.describe() }}."
    }

    /** The instance of template [o] for these argument types, compiled later by [buildAll]. */
    private fun instantiate(location: Loc, o: Overload, argTypes: List<Type>, bindings: Map<TypeParam, Type>): Overload {
        val paramTypes = o.params.mapIndexed { i, p ->
            when {
                Overload.containsTypeParam(p.type) -> types.substitute(p.type, bindings)
                Overload.isInterface(p.type) || p.type === Types.Any -> cd.storable(location, argTypes[i], "argument ${i + 1} of '${o.name}'")
                else -> p.type
            }
        }
        return o.instances.getOrPut(paramTypes) {
            val returns = types.substitute(o.returns, bindings)
            if (Overload.containsTypeParam(returns)) throw EcGenericNotEstablishedError(location, returns.toString())
            val params = o.params.mapIndexed { i, p -> Parameter(p.location, p.name, paramTypes[i], p.default) }
            Overload(o.location, o.module, o.name, "${o.cName}_g${o.instances.size}", params, o.absent, returns, o.throws, o.varArg,
                o.body, false, bindings.mapKeys { it.key.name }, isUnionCopy = true).also { toBuild += it; cd.reserveCName(it.cName) }
        }
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
     * The C functions among [functions] that `main` or a global can reach. The others (most often copies made for union
     * parameters that no call needs) are left out of the C file. They were still compiled, so their errors are reported.
     */
    fun reachable(main: Overload, functions: List<IIR>): List<IIR> {
        val seen = HashSet<String>()
        val waiting = ArrayDeque(listOf(ROOT, main.cName))
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
        o.params.forEach { cd.ctx().define(it.location, Constant(it.name, it.type, cd.localCName(it.name), kind = "a parameter")) }
        // `...rest: T` is the array of the extra arguments, the last C parameter.
        val rest = o.varArg?.takeIf { !it.isCVarArg }?.let { Parameter(it.location, it.name, types.array(it.type)) }
        rest?.let { cd.ctx().define(it.location, Constant(it.name, it.type, cd.localCName(it.name), kind = "a parameter")) }
        o.absent.forEach { p ->
            val value = cd.expect(p.location, p.default!!.process(cd, p.type), p.type, "default value of '${p.name}'")
            cd.ctx().define(p.location, InlineConstant(p.name, p.type, value))
        }
        val block = o.body!!.process(cd, Types.None).second as IRBlock
        if (o.returns !== Types.None && !block.diverges && !cd.functionHasErrors)
            throw EcError(o.location, "Function ${o.name} returns ${o.returns}, so every path through it must end with `ret`.")
        cd.function = null
        val callType = o.throws?.let { types.result(o.returns, it) } ?: o.returns
        val params = IRParameters(o.location, (o.params + listOfNotNull(rest)).map { IRParameter(it.location, cd.localCName(it.name), it.type, null) })
        IRFunctionOverride(o.location, block, o.cName, params, callType, o.throws != null && o.returns === Types.None, o.varArg?.isCVarArg == true)
    }
}
