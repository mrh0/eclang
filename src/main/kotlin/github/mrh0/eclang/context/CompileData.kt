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
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ast.token.TNamed
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

    /** While a function's signature is compiled (Functions.declare): the type parameters written `<mut T>` in it. */
    var mutTypeParams: MutableSet<String>? = null

    /** How many generic records are being made, one inside the other (see TTypeRecord). */
    var instantiating = 0

    val functions = Functions(this)

    /** What functions keep of their arguments, for Lifetimes to follow memory through calls. */
    val escapes = Escapes()

    /** The atoms (`:name`) the program uses. */
    val atoms = Atoms()

    /** The `declare enum`s, by their type (the union of their atoms). */
    val enums = HashMap<Type, CEnum>()

    /** The function whose body is being compiled, or null at the top level. */
    var function: Overload? = null

    /** `.ec` files already loaded with `use`, so each is only loaded once. */
    val loadedFiles = mutableSetOf<String>()

    /** The C libraries the program links to (`use "lib:z"`), by the name `-l` takes. */
    val linkedLibraries = linkedSetOf<String>()

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

    /** A release build (`--release`): `debug` statements and `debug requires`/`ensures` are left out of the program (see TDebug), though still checked. */
    var release = false

    /**
     * `--check-memory`: every allocation is a `malloc` of its own, freed with its pool, and statements carry `#line`s, so that a
     * build with AddressSanitizer (`-fsanitize=address`) reports a use of freed pool memory at its eclang line (see IRProgram).
     */
    var checkMemory = false

    /** How many `const assert`s only C can check (`_Static_assert`, see TConstAssert), for the message when the C compiler fails. */
    var staticAsserts = 0

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
    fun findVar(name: String): IVar? = ctx().getRaw(name) ?: capture(name)
        ?: visibleModules().firstNotNullOfOrNull { m -> m.globals.getRaw(name)?.takeIf { m === module || name !in m.privateGlobals } }?.also { functions.useGlobal(it.cName()) }

    fun getVar(location: Loc, name: String): IVar = findVar(name) ?: throw EcError(location, "'$name' is not defined.${whereElse(name) { it.globals.getRaw(name) != null }}" +
        (if (name == "it") " `it` is the value before the `|>` in a pipe stage (`x |> it * 2`); outside one it is an ordinary name, and no variable has it." else "") +
        (ctx().stack.debugDefined[name]?.let { " It is defined in the `debug` statement at line ${it.getLine()}, so only that statement can use it: a release build leaves it out. Use it inside that statement (`debug do` makes a block), or define it outside." } ?: ""))

    /** The lambda whose body is being compiled, or null (see [Closure]). */
    var closure: Closure? = null
        private set

    /** Compiles [compile] (the body of a lambda, inside [isolated]) with [c] as the lambda it is in. */
    fun <T> inClosure(c: Closure, compile: () -> T): T {
        val saved = closure
        closure = c
        try { return compile() } finally { closure = saved }
    }

    /**
     * [name] inside a lambda, when it is a local of the function (or lambda) around it: the lambda captures it, and from then
     * on [name] is the copy in its closure (`__ec_env->name`) or, for a `var`, the variable itself through a pointer
     * (`(*__ec_env->name)`). The value the closure starts with is compiled here, where the lambda is: in the function around it.
     */
    private fun capture(name: String): IVar? {
        val c = closure ?: return null
        c.captures[name]?.let { return it.inner }
        val outer = outside(c) { ctx().getRaw(name) ?: capture(name) } ?: return null
        if (outer is github.mrh0.eclang.context.state.CompileTimeConstant) return outer // a local `const` is its value
        if (c.inC) throw EcError(c.location, "This lambda is passed to C as a plain function pointer, which can't carry variables, so it can't use '$name', a local variable of the function around it. " +
            "Pass what it needs through the `@Any` data argument that such C functions take, or use a global.")
        if (c.outerFunction?.params?.any { it.name == name && it.isMut } == true)
            throw EcError(c.location, "A lambda can't use '$name', a `mut` parameter: that is the caller's variable, which the lambda could outlive. Copy it first (`val copy = $name`) and use the copy in the lambda.")
        val base = (outer as? github.mrh0.eclang.context.state.NarrowedVar)?.base ?: outer
        val field = localCName(name)
        val captured = outside(c) {
            Lifetimes.checkAlive(this, c.location, name, name)
            unusedResources.remove(name)
            if (base.canAssign()) {
                // A `var` is shared: the closure points to it, so a change made on either side is seen on the other.
                if (name in c.changes) {
                    ctx().forget(name)
                    changedByCalls += name
                    changedByClosures.putIfAbsent(name, c.location)
                }
                Closure.Capture(name, field, base.getType(), true, github.mrh0.eclang.ir.data.IRAddressOf(c.location, base.toIR(c.location, this, Types.None)),
                    github.mrh0.eclang.ast.token.op.TAddressOf(c.location, github.mrh0.eclang.ast.token.TNamed(c.location, name)),
                    github.mrh0.eclang.context.state.Variable(name, base.getType(), "(*__ec_env->$field)"))
            } else {
                // A `val` or parameter never changes, so a copy is the same; it keeps what checks before the lambda proved.
                Closure.Capture(name, field, outer.getType(), false, outer.toIR(c.location, this, Types.None), github.mrh0.eclang.ast.token.TNamed(c.location, name),
                    github.mrh0.eclang.context.state.Constant(name, outer.getType(), "__ec_env->$field", base.kind()))
            }
        }
        c.captures[name] = captured
        c.escapeParams += Escapes.capture(name)
        if (captured.byReference) c.escapeParams += Escapes.captureValue(name)
        return captured.inner
    }

    /** Runs [compile] where the lambda [c] is written: in the function (or lambda) around it. */
    private fun <T> outside(c: Closure, compile: CompileData.() -> T): T {
        val saved = Triple(currentLocalContextBuilder, function, genericContext)
        currentLocalContextBuilder = c.outer; function = c.outerFunction; genericContext = c.outerGenerics; closure = c.parent
        try { return compile() } finally {
            currentLocalContextBuilder = saved.first; function = saved.second; genericContext = saved.third; closure = c
        }
    }

    /**
     * The locals of the function being compiled that a closure created so far may change (it uses them with `mut x`, `addrof x`
     * or as the receiver of a method), with where: like `addrof x`, a check of them before a call proves nothing after it.
     */
    var changedByClosures: MutableMap<String, Loc> = HashMap()

    /** `other.name`: the global [name] of module [m]. */
    fun getVarIn(location: Loc, m: Module, name: String): IVar = m.globals.getRaw(name)?.also {
            functions.useGlobal(it.cName())
            if (m !== module && name in m.privateGlobals) throw EcError(location, "'$name' is private to $m (`priv`), so only code in it can use it. Remove `priv` to use it here.")
        }
        ?: throw EcError(location, "$m has no value called '$name'.${suggestion(name, m.globals.stack.getVars().map { it.getName() })}")

    /**
     * A hint for a name that isn't visible here but exists in other modules ([has] tells), e.g.
     * " It is defined in module 'fib'; write `fib.name`." Empty when there's none.
     */
    fun whereElse(name: String, has: (Module) -> Boolean): String {
        val found = modulesWhere(has)
        if (found.isEmpty()) return ""
        return " It is defined in ${found.joinToString(" and ")}; write `${found[0].name}.$name`."
    }

    /** The modules other than this one and the top level for which [has] holds. */
    fun modulesWhere(has: (Module) -> Boolean) = modules.values.filter { !it.isTopLevel && it !== module && has(it) }

    private val cNames = HashMap<String, Loc>()
    /** Every C name used at file scope: [cNames], functions and `declare`d C names. Locals must not hide them. */
    private val fileScopeNames = HashSet<String>()

    /**
     * Reserves the C name of a global definition, failing if a definition in another module ends up with the same one.
     * A name C already uses (`stdout`, `printf`, ...) gets the `__ec_v_` prefix; with [renamable] false (records,
     * whose C name is fixed) that is an error instead.
     */
    fun claimCName(location: Loc, cName: String, what: String, renamable: Boolean = true): String {
        val name = if (!isCReserved(cName)) cName
            else if (renamable) "__ec_v_$cName"
            else throw EcError(location, "The name of $what is already used by C (it is declared by the C headers this program includes). Rename it.")
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
    fun localCName(name: String): String = if (name in fileScopeNames || isCReserved(name)) "__ec_v_$name" else name

    /** What the C headers this program includes with `use "x.h"` declare, on top of what every program has. */
    private val headerNames = HashSet<String>()

    /** Notes that the program includes [header]. `use`s are compiled first, so this is known before any name is claimed. */
    fun includeHeader(header: String) { headerNames += Util.namesOfHeader(header) }

    private fun isCReserved(cName: String) = Util.isCReserved(cName) || cName in headerNames

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
     * nothing to hoist into, then restores the state of the function around it. With [keepTemps], code that runs where
     * it is written (the default of a record field) can still declare temporaries before the statement ([declareTemp]),
     * which [ArgumentOrder] sets in place.
     */
    fun <T> isolated(keepTemps: Boolean = false, compile: () -> T): T {
        val (savedContext, savedGenerics, savedFunction) = Triple(currentLocalContextBuilder, genericContext, function)
        val (savedHoisted, savedBarrier) = hoisted to hoistBarrier
        val savedLiteralHome = literalHome
        val savedEffects = effects
        val savedLoops = ArrayList(loops)
        val savedPipes = ArrayList(pipeValues)
        val savedPools = ArrayList(poolBlocks)
        val savedLoopPools = ArrayList(loopPools)
        val savedClosure = closure
        closure = null
        enclosing += currentLocalContextBuilder
        loops.clear(); pipeValues.clear(); poolBlocks.clear(); loopPools.clear()
        val savedGlobalTemps = globalTemps
        function = null; hoisted = null; hoistBarrier = null; literalHome = null
        globalTemps = if (keepTemps) savedHoisted ?: savedGlobalTemps else null
        try {
            return compile()
        } finally {
            globalTemps = savedGlobalTemps
            enclosing.removeAt(enclosing.size - 1)
            closure = savedClosure
            currentLocalContextBuilder = savedContext; genericContext = savedGenerics; function = savedFunction
            hoisted = savedHoisted; hoistBarrier = savedBarrier; literalHome = savedLiteralHome
            effects = savedEffects // a lambda's body doesn't run where it is written
            loops.clear(); loops += savedLoops
            pipeValues.clear(); pipeValues += savedPipes
            poolBlocks.clear(); poolBlocks += savedPools
            loopPools.clear(); loopPools += savedLoopPools
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

    /** The pool that was current when the function (or lambda) was called, as C: where `keep` copies to, and `try` copies an error to. */
    fun entryPool(location: Loc): IIR = entryPoolOf(location, if (poolBlocks.isNotEmpty()) poolEntry!! else TNamed.CURRENT_POOL)

    /** [otherwise], or, around the loops whose rounds get a pool of their own (see LoopPools), the pool around the outermost. */
    fun entryPoolOf(location: Loc, otherwise: String): IIR = if (loopPools.isEmpty()) IRRaw(location, otherwise) else LoopPools.IREntryPool(location, loopPools.toList(), otherwise)

    /** The program's `main`, whose current pool, outside `pool do` blocks, lives until the program ends (see [LoopPools]). */
    var programMain: Overload? = null

    /** The loops of `main` around the code being compiled that [LoopPools] looks at, outermost first. */
    val loopPools = ArrayList<LoopPools.Loop>()

    /** Every loop whose rounds [LoopPools] may give a pool of their own, in the order they were compiled. */
    val roundLoops = ArrayList<LoopPools.Loop>()

    /** What allocates in the current pool, so far (see [LoopPools]). */
    val allocations = ArrayList<LoopPools.Allocation>()

    /**
     * Notes that the code compiled at [location] ([what]) allocates in the current pool; with [callee], it calls that function,
     * which may. In a `pool do` block it allocates in the block's pool, which is freed with it.
     */
    fun noteAllocation(location: Loc, what: String, callee: github.mrh0.eclang.ast.token.TBlock? = null) {
        if (poolBlocks.isNotEmpty() || ctx().stack.imagining) return
        val body = if (enclosing.isEmpty()) function?.body else null
        if (body == null && loopPools.isEmpty()) return
        allocations += LoopPools.Allocation(location, what, body, loopPools.reversed(), callee)
    }

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

    /** How many `unsafe` expressions or blocks the code being compiled is in (see [TUnsafe]); 0 at the start of each function. */
    var unsafeDepth = 0

    /** Whether the code being compiled is inside `unsafe`, so operations the compiler can't check are allowed. */
    val isUnsafe get() = unsafeDepth > 0

    /**
     * The locals of the function being compiled whose address is taken somewhere in it (`addrof x`), with where.
     * A write through such a pointer changes the variable behind flow typing's back, so they aren't narrowed.
     */
    var addressTaken: Map<String, Loc> = mapOf()

    /**
     * The locals of the function being compiled that a call has been given to change so far (`f(mut x)`, `x.f()` with a `mut`
     * receiver, see `MutPlace`): ArgumentOrder can't take them as fixed, since a call among the same arguments may change them.
     */
    var changedByCalls: MutableSet<String> = HashSet()

    /**
     * The locals of the function being compiled that hold a `resource` record (a `thread.Thread`) and haven't been read yet, with
     * where they were defined; each must be read before the function ends (see [resourceRecord]).
     */
    val unusedResources = LinkedHashMap<String, Pair<Loc, github.mrh0.ect2.Record>>()

    /** The `resource` record [type] is (`resource rec Thread`), or null: a value of it can't be dropped. */
    fun resourceRecord(type: Type): github.mrh0.ect2.Record? = (type as? github.mrh0.ect2.Record)?.takeIf { it.layout.isResource }

    /** Why dropping a value of [record], a `resource` record, is an error, with what to do. [what] says how it is dropped. */
    fun resourceError(location: Loc, record: github.mrh0.ect2.Record, what: String): EcError = EcError(location,
        "$what, but ${record.name} is a `resource`: each one has to be passed on, as it holds something that must be ended. " +
            if (record.name == "Thread") "Join it (`try thread.join(t)`) or let it run on its own (`try thread.detach(t)`)." else "Store it and give it to the function that ends it.")

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
    class LoopBody<T>(val value: T, val label: String?, val nextLabel: String?, val breaks: Boolean, val round: LoopPools.Loop? = null)

    /** Compiles a loop body. [hasElse]: the loop has an `else` block. [name]: its label, if it has one. */
    fun <T> loop(location: Loc, hasElse: Boolean, name: String?, compile: () -> T): LoopBody<T> {
        if (name != null && loops.any { it.name == name }) throw EcError(location, "A loop around this one is already labelled '$name', so `break $name` wouldn't say which. Choose another label.")
        val id = labelCounter++
        val frame = LoopFrame(name, "__ec_loop_end$id", "__ec_loop_next$id", hasElse, ctx().depth())
        loops += frame
        // A round of a loop outside `pool do` starts in the scope the body opens, and may get a pool of its own; see LoopPools.
        val loopPool = if (function != null && enclosing.isEmpty() && poolBlocks.isEmpty())
            LoopPools.Loop(location, ctx().depth() + 1, temp().replace("tmp", "outer"), temp().replace("tmp", "round")).also { loopPools += it; roundLoops += it } else null
        val stack = ctx().stack
        if (loopPool != null) stack.pendingRound = ContextStack.PendingRound(loopPool.depth, location, LoopPools.RoundLeave(location, loopPool)) { loopPool.registered = true }
        try {
            val value = compile()
            Lifetimes.loopCompiled(this)
            return LoopBody(value, frame.end.takeIf { it in usedLabels }, frame.next.takeIf { it in usedLabels }, frame.breaks, loopPool)
        } finally {
            loops.removeAt(loops.size - 1)
            Lifetimes.loopLeft(this)
            if (loopPool != null) { loopPools.remove(loopPool); stack.pendingRound = null }
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
        // `@p`: what the `mut` parameter p points to (see Narrowing.local).
        assigned.forEach { ctx().forget(if (it.startsWith("@")) "@" + Util.testIdentifier(location, it.drop(1)) else Util.testIdentifier(location, it)) }
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
    fun declareTemp(location: Loc, type: () -> Type): String = temp().also { declareTemp(location, it, type) }

    /** Declares the temporary [name], see the other [declareTemp]. */
    fun declareTemp(location: Loc, name: String, type: () -> Type) {
        (hoisted ?: globalTemps ?: throw EcError(location, "This needs a temporary variable, so it can only be used inside a function body.")).add(IRTempDeclare(location, name, type))
    }

    /** False where there is nowhere to declare a temporary: outside function bodies and the values of globals. */
    val canDeclareTemps get() = hoisted != null || globalTemps != null

    /** The temporaries of the global being compiled, see [withGlobalTemps]. */
    private var globalTemps: MutableList<IIR>? = null

    /**
     * Compiles the initializer of a global, which has no statement to hoist anything before, but is set in the C
     * `main`, where it can have temporaries. Gives them with the result of [compile].
     */
    fun <T> withGlobalTemps(compile: () -> T): Pair<List<IIR>, T> {
        val temps = mutableListOf<IIR>()
        globalTemps = temps
        try {
            return temps to compile()
        } finally {
            globalTemps = null
        }
    }

    /** How many calls have been compiled, so that comparing two counts tells whether the code between them runs any (see [ArgumentOrder]). */
    var effects = 0
        private set
    private var effectsAtStatement = 0

    /** Records that the code being compiled calls something, which can do anything. */
    fun noteEffect() { effects++ }

    /** True while the statement being compiled has called nothing so far, so hoisting something ahead of it keeps the order. */
    val nothingRanInStatement get() = effects == effectsAtStatement

    /** Where the next hoisted statement goes, for [hoistedSince] and [hoistAt]. */
    val hoistMark get() = hoisted?.size ?: 0

    /** Whether statements that run were hoisted since [mark]. Temporaries that are only declared don't count. */
    fun hoistedSince(mark: Int) = hoisted?.drop(mark)?.any { it !is IRTempDeclare } ?: false

    /** Like [hoist], but at the place [mark] in the list, to run before what was hoisted after it. */
    fun hoistAt(mark: Int, ir: IIR) {
        hoisted!!.add(mark, ir)
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
        val savedStart = effectsAtStatement
        val list = mutableListOf<IIR>()
        hoisted = list
        hoistBarrier = null
        effectsAtStatement = effects
        try {
            val result = compile()
            return list to result
        } finally {
            hoisted = savedList
            hoistBarrier = savedBarrier
            effectsAtStatement = savedStart
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
