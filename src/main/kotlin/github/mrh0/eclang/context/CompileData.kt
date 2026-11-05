package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.atom.Atoms
import github.mrh0.eclang.context.function.Functions
import github.mrh0.eclang.context.function.Overload
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.error.EcAbort
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcTypeMismatchError
import github.mrh0.eclang.error.EcUnknownTypeError
import github.mrh0.eclang.ast.token.global.CEnum
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.ir.IRTempDeclare
import github.mrh0.eclang.ast.token.TErrDefer
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*

/** Everything the compiler knows while compiling one program. */
class CompileData(val types: TypeContext = TypeContext()) {
    private val modules = LinkedHashMap<String, Module>()

    /** The module of files without `module "name"`. Its names are visible everywhere. */
    val topLevel = moduleNamed("")

    /** The module whose code is being compiled. */
    var module: Module = topLevel
        private set

    private var currentLocalContextBuilder = ContextBuilder("__top__")
    var genericContext: Map<String, Type>? = null

    /** How many generic records are being made, one inside the other (see TTypeRecord). */
    var instantiating = 0

    val functions = Functions(this)

    /** The atoms (`:name`) the program uses. */
    val atoms = Atoms()

    /** The `declare enum`s, by their type (the union of their atoms). */
    val enums = HashMap<Type, CEnum>()

    /** The function whose body is being compiled, or null at the top level. */
    var function: Overload? = null

    /** `.ec` files already loaded with `use`, so each is only loaded once. */
    val loadedFiles = mutableSetOf<String>()

    /** The text of files as an editor has them, unsaved, by [Util.sourceName]. These are read instead of the disk. */
    var sources: Map<String, String> = mapOf()

    /** Checks the file without building a program from it, so it needn't have a `main`: it may be a library. */
    var checkOnly = false

    /** The text of [path] from [sources], or null when it should be read as usual. */
    fun unsaved(path: String): java.io.InputStream? = sources[path]?.byteInputStream()

    /** Whether `a[i]` and slices of arrays and Strings check their bounds (turned off with `--no-bounds-check`). */
    var boundsChecks = true

    /** The name of the library being compiled (`--lib`), which has no `main`; null for a program. */
    var library: String? = null

    /** Whether `/`, `%` and shifts by a computed count are checked at runtime (division by zero, ...); `--no-arithmetic-check` turns them off. */
    var arithmeticChecks = true

    /** Whether `+`, `-` and `*` on whole numbers stop the program when the result doesn't fit (`--overflow-check`). */
    var overflowChecks = false

    fun moduleNamed(name: String): Module = modules.getOrPut(name) { Module(name) }
    fun findModule(name: String): Module? = modules[name]?.takeIf { !it.isTopLevel }

    /** Compiles [compile] as code of module [m]: unqualified names resolve in [m], then at the top level. */
    fun <T> inModule(m: Module, compile: () -> T): T {
        val saved = module
        module = m
        try {
            return compile()
        } finally {
            module = saved
        }
    }

    /** The modules whose names code in the current module sees without qualifying them. */
    fun visibleModules(): List<Module> = if (module.isTopLevel) listOf(module) else listOf(module, topLevel)

    /** The value called [name]: a local, then a global of this module, then a global of the top level. */
    fun findVar(name: String): IVar? = ctx().getRaw(name) ?: visibleModules().firstNotNullOfOrNull { m -> m.globals.getRaw(name)?.takeIf { m === module || name !in m.privateGlobals } }

    fun getVar(location: Loc, name: String): IVar = findVar(name) ?: throw EcError(location, capturedProblem(name)
        ?: "'$name' is not defined.${whereElse(name) { it.globals.getRaw(name) != null }}")

    /** The error for using [name] inside a lambda when it is a local of a function around it, or null. */
    private fun capturedProblem(name: String): String? {
        if (enclosing.none { it.getRaw(name) != null }) return null
        return "A lambda can't use '$name', a local variable of the function around it (lambdas don't capture variables yet). " +
            "Pass it as a parameter instead, e.g. `fn (x: Int, $name: ...) = ...`, or make it a global."
    }

    /** `other.name`: the global [name] of module [m]. */
    fun getVarIn(location: Loc, m: Module, name: String): IVar = m.globals.getRaw(name)?.also {
            if (m !== module && name in m.privateGlobals) throw EcError(location, "'$name' is private to $m (`priv`), so only code in it can use it. Remove `priv` to use it here.")
        }
        ?: throw EcError(location, "$m has no value called '$name'.${suggestion(name, m.globals.stack.getVars().map { it.getName() })}")

    /**
     * A hint for a name that isn't visible here but exists in other modules ([has] tells), e.g.
     * " It is defined in module 'fib'; write `fib.name`." Empty when there's none.
     */
    fun whereElse(name: String, has: (Module) -> Boolean): String {
        val found = modules.values.filter { !it.isTopLevel && it !== module && has(it) }
        if (found.isEmpty()) return ""
        return " It is defined in ${found.joinToString(" and ")}; write `${found[0].name}.$name`."
    }

    private val cNames = HashMap<String, Loc>()
    /** Every C name used at file scope: [cNames], functions and `declare`d C names. Locals must not hide them. */
    private val fileScopeNames = HashSet<String>()

    /**
     * Reserves the C name of a global definition, failing if a definition in another module ends up with the same one.
     * A name C already uses (`stdout`, `printf`, ...) gets the `__ec_v_` prefix; with [renamable] false (records,
     * whose C name is fixed) that is an error instead.
     */
    fun claimCName(location: Loc, cName: String, what: String, renamable: Boolean = true): String {
        val name = if (!Util.isCReserved(cName)) cName
            else if (renamable) "__ec_v_$cName"
            else throw EcError(location, "The name of $what is already used by C (it is declared by the C headers every program includes). Rename it.")
        cNames.putIfAbsent(name, location)?.let { throw EcError(location, "The C name '$name' of $what is already used by the definition at $it. Rename one of them.") }
        fileScopeNames += name
        return name
    }

    /** Notes a C name used at file scope (a function, or a C name given with `declare`), so no local hides it. */
    fun reserveCName(cName: String) { fileScopeNames += cName }

    /**
     * The C name of the local variable or parameter [name]: the name itself, or with the `__ec_v_` prefix when it
     * would hide something at file scope that the generated code may use (a local `log_6` would hide the function
     * `log_6`, a local `strcmp` the C function).
     */
    fun localCName(name: String): String = if (name in fileScopeNames || Util.isCReserved(name)) "__ec_v_$name" else name

    fun newContext(contextName: String, generics: Map<String, Type> = mapOf()): ContextBuilder {
        genericContext = generics
        currentLocalContextBuilder = ContextBuilder(contextName)
        return currentLocalContextBuilder
    }

    fun ctx() = currentLocalContextBuilder

    /** The local scopes of the functions around the lambda being compiled, innermost last (see [isolated]). */
    private val enclosing = ArrayList<ContextBuilder>()

    /**
     * Compiles [compile] (a lambda's body) as a separate function: with its own scope, outside every loop, and with
     * nothing to hoist into, then restores the state of the function around it.
     */
    fun <T> isolated(compile: () -> T): T {
        val (savedContext, savedGenerics, savedFunction) = Triple(currentLocalContextBuilder, genericContext, function)
        val (savedHoisted, savedBarrier) = hoisted to hoistBarrier
        val savedLiteralHome = literalHome
        val savedLoops = ArrayList(loops)
        val savedPipes = ArrayList(pipeValues)
        val savedPools = ArrayList(poolBlocks)
        enclosing += currentLocalContextBuilder
        loops.clear(); pipeValues.clear(); poolBlocks.clear()
        function = null; hoisted = null; hoistBarrier = null; literalHome = null
        try {
            return compile()
        } finally {
            enclosing.removeAt(enclosing.size - 1)
            currentLocalContextBuilder = savedContext; genericContext = savedGenerics; function = savedFunction
            hoisted = savedHoisted; hoistBarrier = savedBarrier; literalHome = savedLiteralHome
            loops.clear(); loops += savedLoops
            pipeValues.clear(); pipeValues += savedPipes
            poolBlocks.clear(); poolBlocks += savedPools
        }
    }

    /** True inside a function or lambda body (false for the initializers of globals). */
    val inBody get() = function != null || enclosing.isNotEmpty()

    /** The `pool do` blocks around the code being compiled in this function, innermost last (see [Lifetimes]). */
    val poolBlocks = ArrayList<Local>()

    /**
     * The local memory that the current pool (`pool`) frees with: the innermost `pool do` block's, or null outside
     * one, where the current pool is the caller's and outlives the function.
     */
    fun poolOrigin(): Local? = poolBlocks.lastOrNull()

    /** The C variable holding the pool that was current before the outermost `pool do` block, while in one. */
    var poolEntry: String? = null

    /** The values `it` stands for in the `|>` stages being compiled, innermost last. */
    val pipeValues = ArrayList<Pair<Type, IIR>>()

    fun getGlobal() = module.globals

    // ---------------------------------------------------------------------------------------------------
    // Diagnostics: errors are collected so one run reports the problems of every function, not just the first.

    /** A warning: the program compiles, but [message] points at something worth changing. */
    class Warning(val location: Loc, val message: String)

    /** The errors reported so far, in the order they were found (each one once). */
    val errors = ArrayList<EcError>()
    val warnings = ArrayList<Warning>()

    /**
     * Set when an error was reported in the function being compiled. Checks that only make sense for a complete body
     * (unreachable code, a missing `ret`) are then skipped, because the failed statements are missing.
     */
    var functionHasErrors = false

    /**
     * The locals of the function being compiled whose address is taken somewhere in it (`addrof x`), with where.
     * A write through such a pointer changes the variable behind flow typing's back, so they aren't narrowed.
     */
    var addressTaken: Map<String, Loc> = mapOf()

    /** The checks that already warned that they don't narrow an address-taken local (a condition is analyzed more than once). */
    val warnedAliased = HashSet<String>()

    /** The functions each interface asks for (`fn area(): Double`), see `Functions.missingMethod`. */
    val interfaceMethods = HashMap<github.mrh0.ect2.Interface, List<InterfaceMethod>>()

    /** The default values of record fields (`age: Int = 0`), by record and field. */
    val recordDefaults = HashMap<github.mrh0.ect2.Record, Map<String, ITok>>()

    /** The values of the `|>` pipes Lifetimes is looking into, innermost last: what `it` stands for. */
    val pipedTokens = mutableListOf<ITok>()

    /** Records [e] and continues. The same error (a body compiled once per union copy) is only reported once. */
    fun report(e: EcError) {
        functionHasErrors = true
        if (errors.any { it.reason == e.reason && it.location.toString() == e.location.toString() }) return
        errors += e
        if (errors.size >= MAX_ERRORS) throw EcAbort()
    }

    fun warn(location: Loc, message: String) { warnings += Warning(location, message) }

    /** Runs [compile]; if it fails, reports the error and returns null. */
    fun <T> recover(compile: () -> T): T? = try { compile() } catch (e: EcError) { report(e); null }

    /** Stops after a phase that had errors: later phases depend on it and would only report follow-up errors. */
    fun checkpoint() { if (errors.isNotEmpty()) throw EcAbort() }

    // ---------------------------------------------------------------------------------------------------
    // Loops.

    /**
     * A loop the code being compiled is inside. [name]: its label in the program (`outer: for ...`). [end]: the C label
     * after it, where `break` jumps when a C `break` won't do: from a loop inside it, or past an `else` block. [next]:
     * the C label at the end of its body, for `continue` from a loop inside it. [breaks]: a `break` leaves this loop.
     * [depth]: the scope depth its body starts at, so `break` and `continue` know which deferred statements to run.
     */
    private class LoopFrame(val name: String?, val end: String, val next: String, val hasElse: Boolean, val depth: Int, var breaks: Boolean = false)

    /** The loops the code being compiled is inside, innermost last. */
    private val loops = ArrayList<LoopFrame>()
    private val usedLabels = HashSet<String>()
    private var labelCounter = 0

    val loopDepth get() = loops.size

    /**
     * A compiled loop body: [value], the label `break` jumps to (null when no `break` uses one), the label `continue`
     * from an inner loop jumps to (likewise), and whether any `break` leaves the loop.
     */
    class LoopBody<T>(val value: T, val label: String?, val nextLabel: String?, val breaks: Boolean)

    /** Compiles a loop body. [hasElse]: the loop has an `else` block. [name]: its label, if it has one. */
    fun <T> loop(location: Loc, hasElse: Boolean, name: String?, compile: () -> T): LoopBody<T> {
        if (name != null && loops.any { it.name == name }) throw EcError(location, "A loop around this one is already labelled '$name', so `break $name` wouldn't say which. Choose another label.")
        val id = labelCounter++
        val frame = LoopFrame(name, "__ec_loop_end$id", "__ec_loop_next$id", hasElse, ctx().depth())
        loops += frame
        try {
            val value = compile()
            Lifetimes.loopCompiled(this)
            return LoopBody(value, frame.end.takeIf { it in usedLabels }, frame.next.takeIf { it in usedLabels }, frame.breaks)
        } finally {
            loops.removeAt(loops.size - 1)
            Lifetimes.loopLeft(this)
        }
    }

    /** How far out the loop called [label] (or the innermost one) is: 0 for the innermost. */
    fun loopsOut(location: Loc, label: String?, what: String): Int = loops.size - 1 - loops.indexOf(loopNamed(location, label, what))

    /** The loop called [label], or the innermost one. */
    private fun loopNamed(location: Loc, label: String?, what: String): LoopFrame = if (label == null) loops.last() else loops.lastOrNull { it.name == label }
        ?: throw EcError(location, "`$what $label`: no loop around this is labelled '$label'.${suggestion(label, loops.mapNotNull { it.name })} A loop gets a label like this: `$label: for ... do`.")

    /** Where `break` jumps: a label, or null for a plain C `break`. */
    fun breakLabel(location: Loc, label: String?): String? {
        val frame = loopNamed(location, label, "break").also { it.breaks = true }
        return if (frame.hasElse || frame !== loops.last()) frame.end.also { usedLabels += it } else null
    }

    /** Where `continue` jumps: a label, or null for a plain C `continue`. */
    fun continueLabel(location: Loc, label: String?): String? {
        val frame = loopNamed(location, label, "continue")
        return if (frame !== loops.last()) frame.next.also { usedLabels += it } else null
    }

    /**
     * The deferred statements `break` or `continue` ([what]) runs before leaving the current round of the loop called
     * [label] (or the innermost one): those of the blocks inside the loop's body, the last deferred first.
     */
    fun loopDeferred(location: Loc, label: String?, what: String): List<IIR> =
        compileDeferred(TErrDefer.normal(ctx().deferredSince(loopNamed(location, label, what).depth)))

    /**
     * Compiles [deferred], statements to run at one exit of a scope. Each is compiled again at every exit. They can't
     * hoist (`try`), but they can have temporaries, which they declare where they run.
     */
    fun compileDeferred(deferred: List<ITok>): List<IIR> = deferred.map { tok ->
        val saved = hoisted
        val temporaries = mutableListOf<IIR>()
        hoisted = temporaries
        try {
            val ir = noHoist("`try` can't be used in `defer`.") { tok.process(this, Types.None).second }
            if (temporaries.isEmpty()) ir else IRBlock((tok as Tok).location, temporaries + ir, listOf())
        } finally {
            hoisted = saved
        }
    }

    /**
     * Called before compiling a loop's condition and body, with the variables the body assigns: they may hold
     * something else when the next round starts, so what flow typing knew about them no longer holds.
     */
    fun forgetAssigned(location: Loc, assigned: Set<String>) {
        assigned.forEach { ctx().forget(Util.testIdentifier(location, it)) }
        Lifetimes.loopKills(this, location, assigned)
        Lifetimes.loopStarts(this)
    }

    // ---------------------------------------------------------------------------------------------------
    // Type checks shared by all tokens.

    /**
     * Checks that [value] can be used as a [to] and returns its IR converted to [to] (widened, wrapped into a
     * union, ...). [what] names the place for the error message, e.g. "variable 'x'".
     */
    fun expect(location: Loc, value: Pair<Type, IIR>, to: Type, what: String): IIR {
        val (from, ir) = value
        if (from === Types.None && to !== Types.None) throw EcError(location, "This has no value, so it can't be used ($what).")
        if (!types.accepts(to, from) || (from is Result && to !== from))
            throw EcTypeMismatchError(location, to, from, what, types.explain(from, to))
        return if (from === to) ir else IRConvert(location, ir, from, to, IRConvert.Mode.IMPLICIT)
    }

    /** The type a value of [type] is stored as (literals become Int/Double), after checking it can be stored. */
    fun storable(location: Loc, type: Type, what: String): Type {
        if (type === Types.None) throw EcError(location, "This has no value, so it can't be stored ($what).")
        if (type is Result) throw EcError(location, "Cannot store the result of a call that can fail ($what): ${types.explain(type, types.concrete(type.value))}.")
        val concrete = types.concrete(type)
        types.storageProblem(concrete)?.let { throw EcError(location, "Cannot store a $concrete ($what): $it.") }
        return concrete
    }

    /** Rejects values that can't be used in an expression: calls that return nothing, and unhandled errors. */
    fun usable(location: Loc, value: Pair<Type, IIR>, what: String): Pair<Type, IIR> {
        val type = value.first
        if (type === Types.None) throw EcError(location, "This has no value, so it can't be used ($what).")
        if (type is Result) throw EcError(location, "Cannot use the result of a call that can fail ($what): ${types.explain(type, types.concrete(type.value))}.")
        return value
    }

    /** Finds the type called [name] (or `module.Name`), with a hint when it doesn't exist. */
    fun lookupType(location: Loc, name: String): Type = lookupTypeAnywhere(location, name).also { t ->
        val (namespace, typeName) = when (t) {
            is Declared -> t.namespace to t.name
            is GenericRecord -> t.namespace to t.name
            else -> null to name
        }
        val owner = namespace?.let { modules[it] }
        if (owner != null && owner !== module && typeName in owner.privateTypes)
            throw EcError(location, "Type $typeName is private to $owner (`priv`), so only code in it can use it. Remove `priv` to use it here.")
    }

    /** Whether [name], maybe `module.Type`, can be looked up yet. A module that doesn't exist is for [lookupType] to report. */
    fun typeDeclared(name: String): Boolean {
        if ('.' !in name) return types.lookup(module.name, name) != null
        val (moduleName, member) = name.split('.', limit = 2)
        return findModule(moduleName)?.let { types.lookupIn(it.name, member) != null } ?: true
    }

    private fun lookupTypeAnywhere(location: Loc, name: String): Type {
        if ('.' in name) {
            val (moduleName, member) = name.split('.', limit = 2)
            val m = findModule(moduleName) ?: throw EcError(location, "There is no module called '$moduleName' (in the type $name).")
            return types.lookupIn(m.name, member) ?: throw EcUnknownTypeError(location, name,
                suggestion(member, types.typeNames().filter { types.lookupIn(m.name, it) != null }))
        }
        return types.lookup(module.name, name) ?: throw EcUnknownTypeError(location, name,
            whereElse(name) { types.lookupIn(it.name, name) != null }.ifEmpty { suggestion(name, types.typeNames()) })
    }

    // ---------------------------------------------------------------------------------------------------
    // Hoisting: `try f()` and `f() ?? x` (on a call that can fail) need a temporary variable, declared in a
    // statement before the one being compiled. Blocks collect these with [collectHoisted].

    private var hoisted: MutableList<IIR>? = null
    private var hoistBarrier: String? = null
    private var literalHome: MutableList<IIR>? = null
    private var tempCounter = 0

    /** A fresh C variable name for generated temporaries. */
    fun temp(): String = "__ec_tmp${tempCounter++}"

    /** [what] can't be used here (see [hoistProblem]): an error saying why and what to do instead. */
    fun needsStatement(location: Loc, what: String, fix: String) = EcError(location, "$what can't be used here, because " +
        (if (inBody) "this code may not run, or may run more than once. $fix" else "it needs a statement of its own, so it can only be used inside a function."))

    /** Why nothing can be hoisted here (see [noHoist]), or null when [hoist] works. */
    fun hoistProblem(): String? = hoistBarrier ?: if (hoisted == null) "This can only be used inside a function body." else null

    /**
     * A new temporary of type [type], declared before the statement being compiled but set where it is used
     * ([github.mrh0.eclang.ir.IRTempSet]). Unlike [hoist], that also works in code that may not run, or runs repeatedly.
     */
    fun declareTemp(location: Loc, type: () -> Type): String {
        val name = temp()
        (hoisted ?: throw EcError(location, "This needs a temporary variable, so it can only be used inside a function body.")).add(IRTempDeclare(location, name, type))
        return name
    }

    /**
     * Compiles the arms of a `when` used as a value. They are C blocks, but what they give is used after them, so a
     * literal in them can't be a C compound literal, which is gone when its block ends: it asks for [literalStorage],
     * declared before the statement the `when` is in. That is the scope Lifetimes gives it.
     */
    fun <T> keepingLiterals(compile: () -> T): T {
        val saved = literalHome
        literalHome = saved ?: hoisted
        try {
            return compile()
        } finally {
            literalHome = saved
        }
    }

    /** The name of a C variable to hold a literal of type [type] ([length] of them: a C array), or null when a compound literal lives long enough. */
    fun literalStorage(location: Loc, type: Type, length: Int? = null): String? = literalHome?.let { home ->
        temp().also { home += IRTempDeclare(location, it, { type }, length) }
    }

    /** Adds [ir] to run just before the statement currently being compiled. */
    fun hoist(location: Loc, ir: IIR) {
        hoistBarrier?.let { throw EcError(location, it) }
        (hoisted ?: throw EcError(location, "This can only be used inside a function body.")).add(ir)
    }

    /** Compiles one statement, returning the statements it hoisted together with its own result. */
    fun <T> collectHoisted(compile: () -> T): Pair<List<IIR>, T> {
        val savedList = hoisted
        val savedBarrier = hoistBarrier
        val list = mutableListOf<IIR>()
        hoisted = list
        hoistBarrier = null
        try {
            val result = compile()
            return list to result
        } finally {
            hoisted = savedList
            hoistBarrier = savedBarrier
        }
    }

    /**
     * Compiles code that may not run, or may run more than once (a loop condition, the right side of `and`).
     * A hoisted temporary would run exactly once before the statement, so it isn't allowed there; [reason]
     * explains why to the user.
     */
    fun <T> noHoist(reason: String, compile: () -> T): T {
        val saved = hoistBarrier
        hoistBarrier = saved ?: reason
        try {
            return compile()
        } finally {
            hoistBarrier = saved
        }
    }

    companion object {
        /** After this many errors the compiler stops; later ones are usually caused by the first. */
        const val MAX_ERRORS = 30

        /** The closest of [candidates] to [name] (edit distance at most 2), as " Did you mean 'x'?", or "". */
        fun suggestion(name: String, candidates: Collection<String>): String = closest(name, candidates)?.let { " Did you mean '$it'?" } ?: ""

        /** The closest of [candidates] to [name], if its edit distance is at most 2. */
        fun closest(name: String, candidates: Collection<String>): String? {
            val best = candidates.minByOrNull { distance(name.lowercase(), it.lowercase()) } ?: return null
            return best.takeIf { distance(name.lowercase(), it.lowercase()) <= 2 }
        }

        private fun distance(a: String, b: String): Int {
            val d = Array(a.length + 1) { IntArray(b.length + 1) }
            for (i in 0..a.length) d[i][0] = i
            for (j in 0..b.length) d[0][j] = j
            for (i in 1..a.length) for (j in 1..b.length)
                d[i][j] = minOf(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + if (a[i - 1] == b[j - 1]) 0 else 1)
            return d[a.length][b.length]
        }
    }
}

/** A function an interface asks for: `x.name(params)` must be callable on a record that fits it, and return [returns]. */
class InterfaceMethod(val name: String, val params: List<Type>, val returns: Type) {
    override fun toString() = "fn $name(${params.joinToString()})" + if (returns === Types.None) "" else ": $returns"
}
