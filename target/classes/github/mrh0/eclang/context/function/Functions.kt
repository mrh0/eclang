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
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcGenericNotEstablishedError
import github.mrh0.eclang.error.fn.EcAmbiguousSignatureError
import github.mrh0.eclang.error.fn.EcFunctionNotDefinedError
import github.mrh0.eclang.error.fn.EcInvalidVarArgError
import github.mrh0.eclang.error.fn.EcNoMatchingCallSignatureError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.eclang.ir.IRStatementReturn
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.ir.IRTakeCallSite
import github.mrh0.eclang.ir.IRSetCallSite
import github.mrh0.eclang.ir.IRCCast
import github.mrh0.eclang.ir.IRGlobalDefine
import github.mrh0.eclang.ir.IREnumConvert
import github.mrh0.eclang.ir.IRStatementDefineVal
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRCreateArray
import github.mrh0.eclang.ir.data.IRFormat
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.token.type.mutArgumentProblem
import github.mrh0.ect2.*
import github.mrh0.eclang.context.Escapes
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.data.record.THere
import github.mrh0.eclang.ast.unnested

/**
 * A function parameter as written: `name: type = default`, `...name: type` or `...`. A `mut p: T` parameter ([isMut]) has
 * the type `@T`: the caller passes the address of a variable (`f(mut x)`), and the body reads and writes it as `@p` (the
 * Visitor writes every `p` of the body that way). An `out p: T` ([isOut], only in `declare fn`) is a `mut` one that C only writes.
 */
class Parameter(
    val location: Loc,
    val name: String,
    val type: Type,
    val default: ITok? = null,
    val isVarArg: Boolean = false,
    val isCVarArg: Boolean = false,
    val isMut: Boolean = false,
    val isOut: Boolean = false,
    /** `escaping p: T`: the function keeps the argument after the call, where the compiler can't see it (see TParameterEscaping). */
    val isEscaping: Boolean = false,
    /** `own p: Pool`: the function takes the pool over, and may free it; the caller can't use it after the call (see TParameterOwn). */
    val isOwn: Boolean = false,
    /** `kept p: T` (`declare fn` only): C keeps the argument in what the call's other pointer arguments point to (see TParameterKept). */
    val isKept: Boolean = false,
    /** `flat p: T`: the argument may go to another thread, so it may reach no memory but `sync` records (see Sharing). */
    val isFlat: Boolean = false,
) {
    /** This parameter with the type [t] instead, as a union copy or generic instance makes it. */
    fun withType(t: Type) = Parameter(location, name, t, default, isVarArg, isCVarArg, isMut, isOut, isEscaping, isOwn, isKept, isFlat)

    /** `at: Location = caller`: left out, it is the place of the call (see THere), which the caller passes. */
    val isCaller: Boolean get() = (default?.unnested() as? TNamed)?.name == "caller"
}

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
    /**
     * When the caller leaves out `at: Location = caller` parameters: the overload that has them, whose C function this calls, and
     * where each goes among its parameters. This one is never compiled; the call fills them in with the place of the call.
     */
    val full: Overload? = null,
    val callerFilled: Map<Int, Parameter> = mapOf(),
    /** Its `requires` and `ensures` clauses (see Contract), checked in its body; a call of it goes through [Functions.contractCall]. */
    val contracts: List<github.mrh0.eclang.ast.token.function.Contract> = listOf(),
    /** For a generic instance: the call it was first made for, for errors about what a `<T>` must be (see TConstAssert). */
    val calledAt: Loc? = null,
    /** The type parameters written `<mut T>`: a call must give them types whose fields can change (see [Functions.instantiate]). */
    val mutGenerics: Set<String> = setOf(),
) {
    /** Whether it has a `requires`, which is reported where it is called. */
    val hasRequires get() = contracts.any { it.requires }

    /**
     * True when the function has `<T>`, interface or Any parameters, or a `<T>` only in its result. It is never compiled itself; each call
     * compiles an instance for the concrete argument types instead.
     */
    val isTemplate = !isExternal && (params.any { isTemplateType(it.type) } || containsTypeParam(returns) ||
        varArg?.takeIf { !it.isCVarArg }?.let { containsTypeParam(it.type) } == true)

    val instances = HashMap<List<Type>, Overload>()

    fun describe() = "$name(${(params.map { if (it.isMut) "${if (it.isOut) "out" else "mut"} ${(it.type as Pointer).target}" else it.type.toString() } + listOfNotNull(varArg?.let { if (it.isCVarArg) "..." else "...${it.type}" })).joinToString(", ")}) at $location"

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
        // `x.hidden()` finds nothing here, and `shapes.hidden(x)` wouldn't either, so that is no advice to give.
        fun public(m: Module) = overloads.containsKey(key(m, name)) && name !in m.privateFunctions
        if (qualifier == null) overloads.values.flatten().firstOrNull { it.name == name && it.module !== cd.module }?.module?.takeIf { name in it.privateFunctions && cd.modulesWhere(::public).isEmpty() }?.let {
            throw EcError(location, "'$name' is private to $it (`priv fn`), so only code in it can call it. Remove `priv` to use it here.")
        }
        val hint = if (qualifier != null) CompileData.suggestion(name, visibleNames(qualifier))
            else cd.whereElse(name, ::public).ifEmpty { CompileData.suggestion(name, visibleNames(null)) }
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
        val isExternal = func is TFuncExternal
        // C calls and is called with plain function pointers: in a `declare fn` or `export fn`, a function type is one.
        val inC = isExternal || func.exportName != null
        val mutGenerics = HashSet<String>()
        cd.mutTypeParams = mutGenerics
        val (params, declaredReturn) = try {
            func.processSignature(cd).let { (ps, r) ->
                if (inC) ps.map { it.withType(types.inC(it.type)) } to types.inC(r) else ps to r
            }
        } finally {
            cd.mutTypeParams = null
        }
        val (returns, throws) = if (declaredReturn is Result) declaredReturn.value to declaredReturn.error else declaredReturn to null

        params.forEachIndexed { i, p -> if ((p.isVarArg || p.isCVarArg) && i != params.size - 1) throw EcInvalidVarArgError(p.location, name) }
        val varArg = params.lastOrNull()?.takeIf { it.isVarArg || it.isCVarArg }
        val fixed = if (varArg != null) params.dropLast(1) else params
        if (isExternal) (fixed.map { it.type } + returns).firstOrNull { CTypes.fixedOf(it) != null }?.let {
            throw EcError(func.location, "C functions don't take or return arrays by value, so '$name' can't use a $it: in C, an `int a[4]` parameter is a pointer. Declare it as a pointer (`T[]c` or `@T`) and pass `a.data`.")
        }
        func.exportName?.let { checkExport(func, name, it, returns, fixed, varArg) }
        if (!isExternal) fixed.firstOrNull { it.isOut }?.let {
            throw EcError(it.location, "`out` is for C functions (`declare fn`), which return their results through pointers. An eclang function returns them: give '$name' a result, or several as a tuple (`: (Int, String)`). To change the caller's variable, write `mut ${it.name}`.")
        }
        if (returns === Types.Never && throws != null) throw EcError(func.location, "'$name' returns Never, so it never comes back, not even with an error. Remove `throws`, or give it another return type.")
        func.contracts.firstOrNull { !it.requires }?.let { c ->
            if (returns === Types.Never) throw EcError(c.location, "'$name' returns Never, so it never returns and an `ensures` would never be checked. Remove it.")
            fixed.firstOrNull { it.name == RESULT }?.let { throw EcError(it.location, "In an `ensures`, `$RESULT` is the value '$name' returns, so a parameter can't be called '$RESULT'. Rename the parameter.") }
        }
        if (returns !== Types.None && returns !== Types.Never && !Overload.containsTypeParam(returns)) cd.storable(func.location, returns, "return type of '$name'")
        throws?.let { cd.storable(func.location, it, "error type of '$name'") }
        varArg?.takeIf { it.isVarArg && !Overload.containsTypeParam(it.type) }?.let { cd.storable(it.location, it.type, "the variadic parameter '${it.name}'") }
        fixed.filter { it.isCaller }.forEach { p ->
            if (isExternal) throw EcError(p.location, "C functions have no default values, so parameter '${p.name}' can't be `caller`. Declare an eclang function that takes the place and calls it.")
            if (p.isMut || !THere.takes(cd, p.type)) throw EcError(p.location, "`caller` gives a parameter the place of the call, as a Location (core:errors.ec), a String (\"file.ec:12\") or an Int (the line), " +
                "but '${p.name}' is a ${if (p.isMut) "mut " else ""}${p.type}. Write `${p.name}: Location = caller`.")
        }

        // Every combination of union members, and of passing or leaving out parameters with defaults.
        var combinations: List<List<Parameter?>> = listOf(listOf())
        var isUnionCopy = false
        for (p in fixed) {
            val split = variants(p.type)
            if (split.size > 1) isUnionCopy = true
            val options: List<Parameter?> = split.map { p.withType(it) } +
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
        // A copy that leaves out a `caller` parameter calls the one that has it: those come first, and [made] finds them.
        val made = HashMap<List<Type?>, Overload>()
        fun filled(combo: List<Parameter?>) = combo.indices.filter { combo[it] == null && fixed[it].isCaller }
        for (combo in combinations.sortedBy { filled(it).isNotEmpty() }) {
            val callers = filled(combo)
            val full = if (callers.isEmpty()) null else made.getValue(combo.mapIndexed { i, p -> if (i in callers) fixed[i].type else p?.type })
            val present = combo.filterNotNull()
            val absent = fixed.filterIndexed { i, _ -> combo[i] == null && i !in callers }
            val same = list.indexOfFirst { it.params.map { p -> p.type } == present.map { p -> p.type } && (it.varArg == null) == (varArg == null) }
            if (same in 0 until earlier) throw EcError(func.location, "Function $name(${present.joinToString { it.type.toString() }}) is already defined at ${list[same].location}.")
            val base = module.cName(name)
            val index = counters.merge(base, 1, Int::plus)!! - 1
            val cName = full?.cName ?: func.exportName ?: if (isExternal) (func.getSourceName() ?: name) else "${base}_$index"
            val body = if (func is IFuncBody) func.getBody().also { it.addressTaken = func.addressTaken } else null
            // What an `escaping` parameter is given outlives the call, as if stored in a global: see Escapes, and Lifetimes.checkEscaping.
            if (body != null) fixed.filter { it.isEscaping }.forEach { cd.escapes.store(body, Escapes.Store(it.name, Lifetimes.GLOBAL_SOURCE, escaping = true)) }
            fixed.firstOrNull { it.isKept }?.let { if (!isExternal) throw EcError(it.location, "`kept` is for a `declare fn`, which has no body to show what it keeps. An eclang function's body says so itself.") }
            val declaredUnions = fixed.filter { variants(it.type).size > 1 }.associate { it.name to it.type }
            // Where each `caller` parameter goes among the parameters of [full]: after those before it that are passed.
            val positions = callers.associate { i -> (0 until i).count { combo[it] != null || it in callers } to fixed[i] }
            val overload = Overload(func.location, module, name, cName, present, absent, returns, throws, varArg, body, isExternal, isUnionCopy = isUnionCopy, declaredUnions = declaredUnions,
                declared = fixed.map { it.name }, namedOnly = same >= 0, exportName = if (full == null) func.exportName else null, full = full, callerFilled = positions, contracts = func.contracts,
                mutGenerics = mutGenerics)
            list += overload
            // What a C function keeps (`kept`) is stored in its other pointer arguments, which its calls are checked against (see Escapes).
            if (isExternal) for (k in present.filter { it.isKept }) for (o in present) if (o !== k && Lifetimes.holdsPointer(o.type)) cd.escapes.store(overload, Escapes.Store(k.name, o.name))
            made[combo.map { it?.type }] = overload
            if (full != null) continue
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

        /** In an `ensures`, the value the function returns. */
        const val RESULT = "result"
        private const val ROOT = ""
        private const val INITIALIZER = "#initializer"
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
     * called with an `Int[]`. A return type that is still generic comes from the lambda, so it is left open (Any; None would ask for a lambda that returns nothing).
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
            else types.function(bound.params, if (Overload.containsTypeParam(bound.returns)) Types.Any else bound.returns, bound.throws)
        }.distinct()
        return hints.singleOrNull() ?: Types.None
    }

    /**
     * Resolves a call of [name] with [args] (already compiled) to the best overload. [qualifier]: the module of a
     * qualified call, `fib.add(1, 2)`. [expected]: the type wanted of the result, which decides a `<T>` that only the
     * return type has (`val names: @List<String> = list()`). [names]: the parameter each argument is for, if any has one.
     */
    fun call(location: Loc, name: String, written: List<Pair<Type, IIR>>, qualifier: Module? = null, expected: Type = Types.None, names: List<String?>? = null,
             mutable: List<Boolean>? = null, receiver: Boolean = false): CallTarget {
        val candidates = visible(name, qualifier).ifEmpty { notDefined(location, name, qualifier) }
        written.forEachIndexed { i, a -> cd.usable(location, a, "argument ${i + 1} of '$name'") }

        val scored = candidates.mapNotNull { o ->
            val order = arrange(o, written.indices.toList(), names) ?: return@mapNotNull null
            val ordered = order.map { written[it] }
            val flags = order.map { mutable?.getOrNull(it) == true }
            val bindings = mutableMapOf<TypeParam, Type>()
            score(o, ordered.map { it.first }, bindings, flags, receiver)?.let { Scored(o, it, bindings, ordered) }
        }
        if (scored.isEmpty()) {
            names?.let { namedProblem(name, candidates, it) }?.let { throw EcError(location, it) }
            val types = written.map { it.first }
            if (names == null) mutProblem(name, candidates, types, mutable, receiver)?.let { throw EcError(location, it) }
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

        val (picked, _, bindings, given) = winners[0]
        // `at: Location = caller` left out: the overload that has it, given the place of this call.
        val chosen = picked.full ?: picked
        val args = if (picked.full == null) given else given.toMutableList().apply {
            picked.callerFilled.toSortedMap().forEach { (i, p) -> add(i, THere.value(cd, location, p.type)) }
        }
        val argTypes = args.map { it.first }
        val target = if (chosen.isTemplate) instantiate(location, chosen, argTypes, bindings, expected) else chosen
        val irs = target.params.mapIndexed { i, p ->
            // [score] has checked a `mut` argument's type: exactly the parameter's, or a nullable one for `out`.
            val ir = if (p.isMut) args[i].second else cd.expect(location, args[i], p.type, "argument ${i + 1} of '$name'")
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
     * doesn't convert: a pointer to a pointer (`char**` vs `const char**`), a C array of pointers (`UChar[]c?[]c` vs
     * `const uint8_t* const*`) and function pointers. Arguments of these types are passed to C functions as `void*`;
     * eclang has already checked them against the declaration.
     */
    private fun constSensitive(t: Type): Boolean = t is FunctionType || (t is Pointer && pointerLike(t.target)) ||
        (t is ArrayType && t.isC && pointerLike(t.element)) ||
        (t is Union && t.isNullable && types.nonNull(t).let { it !is Union && constSensitive(it) })

    /** A type C holds as a pointer, and may declare `const`. */
    fun pointerLike(t: Type): Boolean = t is Pointer || t === Types.CString || t is FunctionType || (t is ArrayType && t.isC) ||
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
                (visible(m.name) + owners(arg).flatMap { visible(m.name, it) }).none { o ->
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

    /**
     * Total cost of calling [o] with [argTypes], or null when it doesn't fit. Generic parameters cost extra. [mutable] says which
     * arguments are written `mut x` (their type is then `@T`), and [receiver] that the first is the receiver of `x.name(...)`.
     */
    private fun score(o: Overload, argTypes: List<Type>, bindings: MutableMap<TypeParam, Type>, mutable: List<Boolean>? = null, receiver: Boolean = false): Int? {
        var total = 0
        for ((i, a) in argTypes.withIndex()) {
            val p = o.params.getOrNull(i)
            val isMutArg = mutable?.getOrNull(i) == true
            if (isMutArg || p?.isMut == true) {
                total += mutCost(o, p, a, isMutArg, receiver && i == 0, bindings) ?: return null
                continue
            }
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

    /**
     * What it costs to pass [a] to the `mut` parameter [p] (null: it can't). It must be `mut x` ([isMutArg], a `@T`), or the
     * receiver of `x.name()` ([isReceiver]) when that is a pointer already. Its type must be exactly the parameter's: the
     * function may store any value of its type there, so an `Int?` variable can't take a `mut Int`, nor a `Cat` a `mut Animal`.
     * Only an `out` parameter of a pointer type also takes a variable that may be null, which C fills in.
     */
    private fun mutCost(o: Overload, p: Parameter?, a: Type, isMutArg: Boolean, isReceiver: Boolean, bindings: MutableMap<TypeParam, Type>): Int? {
        if (p == null || !p.isMut) return null
        if (!isMutArg && !(isReceiver && a is Pointer)) return null
        return when {
            Overload.containsTypeParam(p.type) -> if (types.bind(p.type, a, bindings)) genericCost else null
            Overload.isInterface(p.type) -> types.cost(a, p.type).let { if (it == TypeContext.NO || missingMethod(a, p.type) != null) null else it + genericCost }
            a === p.type -> 0
            p.isOut && a is Pointer && a.target.let { t -> t is Union && isPlainUnion(t) && types.nonNull(t) === (p.type as Pointer).target } -> 0
            else -> null
        }
    }

    /**
     * Why no overload of [name] takes these arguments, when the reason is `mut`: an overload would take them if `mut` were
     * where its parameters have it, or if a `mut` argument had the parameter's exact type. Null otherwise.
     */
    private fun mutProblem(name: String, candidates: List<Overload>, argTypes: List<Type>, mutable: List<Boolean>?, receiver: Boolean): String? {
        for (o in candidates.filter { fitsArity(it, argTypes.size) && !it.namedOnly }) {
            // The other arguments must fit: those for `mut` parameters, or written with `mut`, are what is wrong.
            val plain = argTypes.mapIndexed { i, a ->
                val p = o.params.getOrNull(i)
                if (p != null && (p.isMut || mutable?.getOrNull(i) == true)) (if (p.isMut) (p.type as Pointer).target else p.type) else a
            }
            val unmarked = o.params.map { p -> if (p.isMut) Parameter(p.location, p.name, (p.type as Pointer).target) else p }
            val relaxed = Overload(o.location, o.module, o.name, o.cName, unmarked, o.absent, o.returns, o.throws, o.varArg, null, o.isExternal)
            if (score(relaxed, plain, HashMap()) == null) continue
            for ((i, p) in o.params.withIndex()) {
                val isMutArg = mutable?.getOrNull(i) == true
                val target = (p.type as? Pointer)?.target
                when {
                    p.isMut && !isMutArg && !(receiver && i == 0) -> return "Parameter '${p.name}' of '$name' is `${if (p.isOut) "out" else "mut"}`: the function changes the caller's variable. Pass a variable with `mut`, e.g. `$name(${argList(o, i)})`."
                    p.isMut && !isMutArg -> return "`x.$name(...)` passes x to the `mut` parameter '${p.name}', which needs a variable of type $target, but this is a ${argTypes[i]}. Store it in a `var` of type $target first."
                    !p.isMut && isMutArg -> return "Argument ${i + 1} of '$name' is passed with `mut`, but parameter '${p.name}' isn't `mut`, so the function can't change it. Remove `mut`, or make the parameter `mut ${p.name}: ${p.type}`."
                    p.isMut && mutCost(o, p, argTypes[i], true, false, HashMap()) == null ->
                        return "`mut` argument ${i + 1} of '$name' is a variable of type ${(argTypes[i] as? Pointer)?.target ?: argTypes[i]}, but parameter '${p.name}' is a `${if (p.isOut) "out" else "mut"} $target`. " +
                            "A `mut` parameter takes a variable of exactly its type, as the function may store any $target in it" +
                            (if (p.isOut) " (an `out` parameter also takes a nullable variable, but only of a pointer type, whose null C can overwrite)" else "") +
                            ". Copy the value into a `var` of type $target, pass that, and copy it back."
                }
            }
        }
        return null
    }

    /** The arguments of a call of [o] with `mut x` at [index], for a hint: `move(mut x)`, `push(a, mut x)`. */
    private fun argList(o: Overload, index: Int) = o.params.mapIndexed { i, p -> if (i == index) "mut x" else p.name }.joinToString(", ")

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
        // `io.log(s.data)`: several overloads, but the reason none fits is that `.data` isn't a CString.
        val charData = argTypes.indexOfFirst { it is ArrayType && it.isC && it.element === Types.Char }
        val because = if (charData >= 0 && sameArity.any { it.params.getOrNull(charData)?.type === Types.CString })
            "Argument ${charData + 1} isn't a CString: ${types.explain(argTypes[charData], Types.CString)}. " else ""
        return "${because}Available: ${candidates.joinToString("; ") { it.describe() }}."
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
        for ((param, arg) in bindings) if (param.name in o.mutGenerics) types.fixedRecord(arg)?.let { r ->
            throw EcError(location, mutArgumentProblem("The type that '${o.name}' (${o.location}) takes for `<mut ${param.name}>`", arg, r))
        }
        val returns = types.substitute(o.returns, bindings)
        if (Overload.containsTypeParam(returns)) throw EcGenericNotEstablishedError(location, returns.toString())
        val varArg = o.varArg?.let { Parameter(it.location, it.name, types.substitute(it.type, bindings), null, it.isVarArg, it.isCVarArg) }
        varArg?.takeIf { Overload.containsTypeParam(it.type) }?.let { throw EcGenericNotEstablishedError(location, it.type.toString()) }
        // The result and the variadic part count: `list()` is a different function for each element type.
        return o.instances.getOrPut(paramTypes + returns + listOfNotNull(varArg?.type)) {
            val params = o.params.mapIndexed { i, p -> p.withType(paramTypes[i]) }
            Overload(o.location, o.module, o.name, "${o.cName}_g${o.instances.size}", params, o.absent, returns, o.throws, varArg,
                o.body, false, bindings.mapKeys { it.key.name }, isUnionCopy = true, declared = o.declared, contracts = o.contracts, calledAt = location).also { toBuild += it; cd.reserveCName(it.cName) }
        }
    }

    /** Whether a function [name] can be called with one [argument]. */
    fun accepts(name: String, argument: Type, qualifier: Module? = null, receiver: Boolean = false) =
        visible(name, qualifier).any { !it.namedOnly && fitsArity(it, 1) && score(it, listOf(argument), HashMap(), receiver = receiver) != null }

    /**
     * The call `name(argument)` that a language feature makes for a type (`next` and `iter` of `for`), or null when nothing takes
     * [argument]. The function may be in the module that declares the type, as the feature can be used anywhere the value gets to.
     */
    fun protocolCall(location: Loc, name: String, argument: Pair<Type, IIR>): CallTarget? {
        // As a receiver: a pointer to the iterator may go to `fn next(mut c: Countdown)`.
        if (accepts(name, argument.first, receiver = true)) return call(location, name, listOf(argument), receiver = true)
        return owner(argument.first)?.takeIf { accepts(name, argument.first, it, receiver = true) }?.let { call(location, name, listOf(argument), it, receiver = true) }
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
            val best = overloads[key(m, "str")].orEmpty().filter { !it.isExternal && !it.namedOnly && it.full == null && it.params.size == 1 && it.varArg == null }.mapNotNull { o ->
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

    /** The modules that declare [t] and, for a record, its parents: a function of a parent's module takes a child too. */
    private fun owners(t: Type): List<Module> {
        val declared = if (t is Pointer) t.target else t
        val chain = if (declared is Record) generateSequence(declared) { it.parent }.toList() else listOf(declared)
        return chain.mapNotNull { owner(it) }.distinct()
    }

    /**
     * The module to look in for the method call `receiver.name(...)` of [count] arguments, the receiver included, when
     * only the module that declares the receiver's type has it: `f.close()` outside module io. Null when a function
     * this code sees takes the receiver, as those come first, or when no function does.
     */
    fun methodModule(name: String, receiver: Type, count: Int, addressed: Boolean = false): Module? {
        fun takes(o: Overload) = fitsArity(o, count) && score(o, listOf(receiver), HashMap(), listOf(addressed), receiver = true) != null
        return if (visible(name).any(::takes)) null else owners(receiver).firstOrNull { m -> visible(name, m).any(::takes) }
    }

    /** Whether some function called [name], in any module, has a `mut` first parameter, so that `x.name()` may pass the address of x. */
    fun hasMutReceiver(name: String): Boolean = overloads.values.any { list -> list.any { it.name == name && it.params.firstOrNull()?.isMut == true } }

    /**
     * Whether `x.name(...)` ([count] arguments) passes the address of x, a place of type [r]: a function it can call has a `mut`
     * first parameter that takes it, and none takes x as it is. Only the receiver is known yet, so the others don't decide.
     */
    fun receiverByAddress(name: String, r: Type, count: Int): Boolean {
        val candidates = (visible(name) + owners(r).flatMap { visible(name, it) }).filter { fitsArity(it, count) }
        fun fits(o: Overload, t: Type, addressed: Boolean) = score(o, listOf(t), HashMap(), listOf(addressed), receiver = true) != null
        return candidates.none { fits(it, r, false) } && candidates.any { fits(it, types.pointer(r), true) }
    }

    /** The single overload of [name], or null when there are none or several. */
    fun onlyOverload(name: String, qualifier: Module? = null): Overload? = visible(name, qualifier).filter { it.full == null }.singleOrNull()?.takeIf { it.varArg == null && !it.isTemplate }

    /** The function type of [o], e.g. `(Int) => Int`. */
    fun typeOf(o: Overload): FunctionType = types.function(o.params.map { it.type }, o.returns, o.throws)

    /** `fn name` as a value of function type [signature]: the overload with exactly that signature. */
    fun reference(location: Loc, name: String, signature: FunctionType, qualifier: Module? = null): Overload {
        val candidates = visible(name, qualifier).ifEmpty { notDefined(location, name, qualifier) }
        return candidates.firstOrNull { o ->
            o.full == null && o.varArg == null && !o.isTemplate && o.params.map { it.type } == signature.params &&
                o.returns === signature.returns && o.throws === signature.throws
        }?.also { if (!it.isExternal) use(it.cName) } ?: throw EcError(location, "No function $name has the type $signature. Available: ${candidates.joinToString("; ") { it.describe() }}.")
    }

    /** The C functions made for `fn name` as a function value, by C name (see [closureOf]). */
    private val closures = HashSet<String>()

    /**
     * `fn name` of [o] as a function value of [type]: a static closure of a C function that takes the closure first and calls o,
     * `R __ec_ref_f_0(void* __ec_self, int a0) { return f_0(a0); }`. Where C expects a function pointer, o itself is used instead.
     */
    fun closureOf(location: Loc, o: Overload, type: FunctionType): IIR {
        val id = "__ec_ref_${o.cName}"
        if (closures.add(id)) {
            val params = type.params.mapIndexed { i, t -> IRParameter(location, "a$i", t, null) }
            val returns = type.throws?.let { types.result(type.returns, it) } ?: type.returns
            val call = "${o.cName}(${params.joinToString(", ") { it.name }})"
            val gives = returns !== Types.None && returns !== Types.Never
            val block = IRBlock(location, listOf(IRStatement(location, IRRaw(location, if (gives) "return $call" else call))), listOf(), diverges = gives)
            lambdas += IRFunctionOverride(location, block, id, IRParameters(location, listOf(IRParameter(location, github.mrh0.eclang.ast.token.function.TLambda.SELF, types.pointer(Types.Any), null)) + params),
                returns, false, false, closure = IRFunctionOverride.ClosureC(type, listOf()))
            if (!o.isExternal) uses.getOrPut(id) { HashSet() } += o.cName
        }
        use(id)
        return github.mrh0.eclang.ir.function.IRClosure(location, id, listOf())
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
        uses.getOrPut(lambdaOwners.lastOrNull() ?: cd.function?.cName ?: initializer ?: ROOT) { HashSet() } += cName
    }

    /** Notes that the code being compiled reads or changes the global [cName]. C names are unique, so these share [uses] with functions. */
    fun useGlobal(cName: String) = use(cName)

    /** Stands for the global being compiled in [uses], until [initializerOf] knows its C name. */
    private var initializer: String? = null

    /** The globals with code in [uses], under their C names. */
    private val initialized = HashSet<String>()

    /** Compiles the global with [compile], and from then on [uses] has what its initial value uses under its C name. */
    fun initializerOf(compile: () -> IIR): IIR {
        initializer = INITIALIZER
        val ir = try { compile() } finally { initializer = null }
        val used = uses.remove(INITIALIZER) ?: return ir
        val name = (ir as? IRGlobalDefine)?.cName
        if (name == null) uses.getOrPut(ROOT) { HashSet() } += used
        else { uses[name] = used; initialized += name }
        return ir
    }

    /**
     * The order to set [globals] in: as written, except that a global comes after those its value needs, also through the
     * functions it calls, so that `val a = f()` sees the `val b = new 1` that f reads, wherever b is written. Globals that
     * need each other can't be ordered, so they are warned about and keep their written order.
     */
    fun initializationOrder(globals: List<IRGlobalDefine>): List<IRGlobalDefine> {
        val byName = globals.filter { it.setAtStart }.associateBy { it.cName }
        val order = LinkedHashSet<IRGlobalDefine>()
        val open = LinkedHashSet<IRGlobalDefine>()

        /** The globals set at the start that the code of [global] reaches. Those stop the search: their own needs are theirs. */
        fun needs(global: IRGlobalDefine): List<IRGlobalDefine> {
            val found = LinkedHashSet<IRGlobalDefine>()
            val seen = HashSet<String>()
            val waiting = ArrayDeque(uses[global.cName].orEmpty().sorted())
            while (waiting.isNotEmpty()) {
                val next = waiting.removeFirst()
                if (!seen.add(next)) continue
                val needed = byName[next]
                if (needed != null) found += needed else if (next !in initialized) uses[next]?.let { waiting.addAll(it.sorted()) }
            }
            return globals.filter { it in found }
        }

        fun visit(global: IRGlobalDefine) {
            if (global in order) return
            open += global
            for (needed in needs(global)) {
                if (needed in open) cycleWarning(global, needed, open.toList()) else visit(needed)
            }
            open -= global
            order += global
        }
        globals.forEach { visit(it) }
        return order.toList()
    }

    /** [global]'s value needs [needed], which is waiting for it, as the last of [open] are. */
    private fun cycleWarning(global: IRGlobalDefine, needed: IRGlobalDefine, open: List<IRGlobalDefine>) {
        val cycle = open.dropWhile { it !== needed }.joinToString(" -> ") { "'${it.name}'" } + " -> '${needed.name}'"
        cd.warn(global.location, "The value of '${global.name}' depends on itself ($cycle), through the functions it calls, so code reads a global that isn't set yet. " +
            "That value is zero, and a crash if it is a pointer. Set one of them in `main`, or stop those functions reading it.")
    }

    /**
     * The C functions among [functions] that `main` (null in a library), a global or an exported function can reach. The others
     * (most often copies made for union parameters that no call needs) are left out of the C file. They were still compiled, so
     * their errors are reported.
     */
    fun reachable(main: Overload?, functions: List<IIR>): List<IIR> {
        val seen = HashSet<String>()
        val waiting = ArrayDeque(listOfNotNull(ROOT, main?.cName) + initialized + exported.map { it.cName })
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
        return built + lambdas + contractEntries.values
    }

    /**
     * The checks of [o]'s `ensures` where it returns, at [location]: with `result` the value it returns, held in the C variable
     * [result] (its name and type), when it returns one.
     */
    fun ensures(o: Overload, location: Loc, result: Pair<String, Type>?): List<IIR> {
        val clauses = o.contracts.filter { !it.requires }
        if (clauses.isEmpty()) return listOf()
        cd.ctx().push()
        try {
            result?.let { (cName, type) -> cd.ctx().define(location, Constant(RESULT, type, cName, kind = "the value '${o.name}' returns")) }
            return cd.noHoist("`try` can't be used in `ensures`. Handle the error with `??` instead.") {
                clauses.map { it.check(o.name, IRConvert.where(location)).process(cd, Types.None).second }
            }
        } finally {
            cd.ctx().pop()
        }
    }

    /** The C functions direct calls of functions with `requires` go through, by the function's C name (see [contractCall]). */
    private val contractEntries = LinkedHashMap<String, IRFunctionOverride>()

    /**
     * The C function a direct call of [o], which has `requires`, calls instead of it, which takes the place of the call as one more
     * argument: `f_0_at(x, "main.ec:12")` sets `__ec_call_site` and calls `f_0(x)`, which takes it first thing (see IRTakeCallSite).
     * The arguments are computed before it is set, so a call among them can't take it instead. A call through a function value
     * or from C goes to `f_0` directly, which then reports at its own place.
     */
    fun contractCall(o: Overload): String {
        val name = "${o.cName}_at"
        use(name)
        contractEntries.getOrPut(o.cName) {
            uses.getOrPut(name) { HashSet() } += o.cName
            cd.reserveCName(name)
            val callType = o.throws?.let { types.result(o.returns, it) } ?: o.returns
            val rest = o.varArg?.takeIf { !it.isCVarArg }?.let { Parameter(it.location, it.name, types.array(it.type)) }
            val params = (o.params + listOfNotNull(rest)).mapIndexed { i, p -> IRParameter(o.location, "__ec_p$i", p.type, null) }
            val call = IRRaw(o.location, "${o.cName}(${params.joinToString(", ") { it.name }})")
            val ret = if (o.returns === Types.None && o.throws == null || o.returns === Types.Never) IRStatement(o.location, call) else IRStatementReturn(o.location, call)
            IRFunctionOverride(o.location, IRBlock(o.location, listOf(IRSetCallSite(o.location), ret), listOf(), diverges = true), name,
                IRParameters(o.location, params + IRParameter(o.location, "__ec_at", Types.CString, null)), callType, false, false)
        }
        return name
    }

    private fun build(o: Overload): IIR = cd.inModule(o.module) {
        cd.newContext(o.cName, o.bindings)
        cd.function = o
        cd.functionHasErrors = false
        cd.unsafeDepth = 0
        cd.addressTaken = o.body!!.addressTaken
        cd.changedByCalls = HashSet()
        cd.changedByClosures = HashMap()
        cd.unusedResources.clear()
        cd.escapes.startFunction()
        cd.escapes.startResult(o)
        o.params.forEach { cd.ctx().define(it.location, Constant(it.name, it.type, cd.localCName(it.name), kind = "a parameter")) }
        // A pool the function takes over (`own p: Pool`) is its own to free.
        o.params.filter { it.isOwn }.forEach { cd.ctx().stack.ownedPools += cd.ctx().stack.poolOf(it.name) }
        // `...rest: T` is the array of the extra arguments, the last C parameter.
        val rest = o.varArg?.takeIf { !it.isCVarArg }?.let { Parameter(it.location, it.name, types.array(it.type)) }
        rest?.let {
            cd.ctx().define(it.location, Constant(it.name, it.type, cd.localCName(it.name), kind = "a parameter"))
            Lifetimes.defineRest(cd, it.name)
        }
        // `requires`: the place of the call first (see contractCall), as the defaults may call functions with `requires` too.
        val entry = if (o.hasRequires) listOf(IRTakeCallSite(o.location, "${o.location.getFilename()}:${o.location.getLine()} (${o.name} called through a function value or from C)")) else listOf()
        // A parameter left out is a local set on entry, so its default runs once however often the body reads it.
        val defaults = o.absent.flatMap { p ->
            val default = p.default!!
            val (hoisted, value) = cd.collectHoisted { cd.expect(p.location, default.process(cd, p.type), p.type, "default value of '${p.name}'") }
            val local = cd.ctx().define(p.location, Constant(p.name, p.type, cd.localCName(p.name), kind = "a parameter"))
            Lifetimes.define(cd, p.name, default, p.type)
            hoisted + IRStatementDefineVal(p.location, local, value)
        }
        // What the `requires` prove holds in the body: they are checked before it.
        val requires = o.contracts.filter { it.requires }.flatMap { c ->
            cd.recover { cd.collectHoisted { c.check(o.name, "__ec_at").process(cd, Types.None).second }.let { (hoisted, ir) -> hoisted + ir } } ?: listOf()
        }
        val body = o.body!!.process(cd, Types.None).second as IRBlock
        // `ensures` of a function that returns nothing, at its end; a `ret` checks them itself (TStatementReturn).
        val ensures = if (o.returns === Types.None && !body.diverges) cd.recover { ensures(o, o.contracts.firstOrNull { !it.requires }?.location ?: o.location, null) } ?: listOf() else listOf()
        if (!cd.functionHasErrors) cd.unusedResources.forEach { (name, found) -> cd.report(cd.resourceError(found.first, found.second, "'$name' is never used")) }
        val block = IRBlock(body.location, entry + defaults + requires + body.statements + ensures, body.deferred, body.diverges)
        // What it returns is known when every statement was followed, and no write through a pointer to a local, or by a lambda, was missed.
        cd.escapes.finishResult(o, complete = !cd.functionHasErrors && cd.addressTaken.isEmpty() && cd.changedByClosures.isEmpty())
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
