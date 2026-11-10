package github.mrh0.eclang.context

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.context.function.Overload
import github.mrh0.eclang.error.EcMemoryError
import github.mrh0.eclang.error.EcError
import java.util.IdentityHashMap

/**
 * What a function keeps of its arguments after it returns, so that [Lifetimes] can follow memory through calls:
 * `fn push(l: @List<T>, value: <T>)` stores `value` in what `l` points to, so `l.push("${i}")` in a `pool do` block, with l
 * from outside it, would leave the list pointing to freed memory, as `l.buffer[n] = "${i}"` written there would.
 *
 * While a body is compiled, [Lifetimes.assign] records each store through a parameter or into a global as a [Store] of the
 * function, in terms of its parameters ([Lifetimes.sources]). A call records a [Call], with what each argument points into
 * there. Bodies are compiled in any order (a call usually comes before the body of a generic function it calls), so the
 * calls are checked after all of them ([check]), which also passes stores on to callers: a function that calls `push(l, v)`
 * with its own parameters stores v in l too.
 *
 * A lambda is followed like a function whose parameters are its own and what it uses of the function around it ([Closure.escapeParams]),
 * and checked as if it were called where it is written ([Call.lambda]). What a function value keeps in its own arguments is
 * followed by its type: each lambda and `fn name` of a function type adds what it keeps there to that type's stores (with the
 * parameters [position]s), and each call of a value of the type is checked against them. What it keeps elsewhere (a global, a
 * variable it uses) can't be checked where it is called, so that is an error.
 * Calls of C functions aren't followed either (an `escaping` parameter says what one keeps), nor is what a callee does with what
 * it was given other than store it: see KNOWN_ISSUES.md.
 */
class Escapes {
    /**
     * The function stores what [value] points into in what [behind] points to. [value] is a parameter's name or
     * [Lifetimes.POOL_SOURCE] (memory it allocates in its caller's pool), [behind] a parameter's name or
     * [Lifetimes.GLOBAL_SOURCE] (a global). A name ending in `[]` stands for the elements of that parameter ([element]).
     * [escaping]: the store is an `escaping` parameter's, kept where the compiler can't see (another thread, C), not in a global.
     */
    data class Store(val value: String, val behind: String, val escaping: Boolean = false)

    /**
     * An argument given for the parameter [param]: the local memory its value points into ([memory], null when it outlives
     * the caller), whether it [holdsPointer], what its value and what it points at come from in the caller ([sources],
     * [pointees], see [Lifetimes.sources]), and whether the callee may store in what it points to ([isTarget]: not for `mut`
     * parameters, whose stores [Lifetimes.assign] already follows).
     */
    class Arg(val param: String, val memory: Local?, val holdsPointer: Boolean, val sources: Set<String>, val pointees: Set<String>, val isTarget: Boolean, val shown: String? = null)

    /**
     * A call of [name], whose body is [callee], from the body [caller] (null in a lambda or the value of a global) with the
     * parameters [callerParams]. [pool] is the current pool there (null: the caller's), [poolSources] what it is in the
     * caller's terms. A call inside `unsafe` isn't checked.
     */
    class Call(val location: Loc, val name: String, val caller: Any?, val callerParams: Set<String>, val callee: Any,
               val args: List<Arg>, val pool: Local?, val poolSources: Set<String>, val unsafe: Boolean, val lambda: Closure? = null) {
        /** Through the call of a lambda where it is written, what it keeps of its own parameters isn't followed: that is an error of its own. */
        fun follows(s: Store) = lambda == null || (base(s.value) !in lambda.params && s.value != Lifetimes.POOL_SOURCE)
    }

    private val stores = IdentityHashMap<Any, MutableSet<Store>>()
    private val calls = ArrayList<Call>()
    private val locals = HashMap<String, MutableSet<String>>()
    private val localPointees = HashMap<String, MutableSet<String>>()

    /** Compiles the body of a lambda with locals of its own, which are another function's in C. */
    fun <T> inLambda(compile: () -> T): T {
        val saved = HashMap(locals) to HashMap(localPointees)
        locals.clear(); localPointees.clear()
        try { return compile() } finally {
            locals.clear(); locals += saved.first
            localPointees.clear(); localPointees += saved.second
        }
    }

    /** Starts the body of a function: what its locals come from is its own. */
    fun startFunction() {
        locals.clear()
        localPointees.clear()
    }

    /** What the local [name] may point into, in terms of the function's parameters (see [Lifetimes.sources]). */
    fun localSources(name: String): Set<String> = locals[name].orEmpty()

    /** What the local [name], a pointer, may point at (see [Lifetimes.pointeeSources]). */
    fun localPointees(name: String): Set<String> = localPointees[name].orEmpty()

    /**
     * The local [name] may now also point into [sources], and at [pointees]. Locals are followed by name, for the whole
     * body, so this only adds.
     */
    fun addLocal(name: String, sources: Set<String>, pointees: Set<String>) {
        if (sources.isNotEmpty()) locals.getOrPut(name) { HashSet() } += sources
        if (pointees.isNotEmpty()) localPointees.getOrPut(name) { HashSet() } += pointees
    }

    /** Records that [body] keeps [store]. Moving what a parameter holds within it (`l.buffer = grown` of l's elements) keeps nothing new. */
    fun store(body: Any, store: Store) {
        if (base(store.value) != base(store.behind)) stores.getOrPut(body) { HashSet() } += store
    }

    /** `const assert not escapes(p)` in [body], at [location]: the function keeps nothing of its parameter [param] after it returns. */
    class Claim(val location: Loc, val body: TBlock, val param: String, val message: String?)

    private val claims = ArrayList<Claim>()

    /** Notes [claim], which [check] checks once it knows everything [Claim.body] stores, through the functions it calls too. */
    fun claim(claim: Claim) { claims += claim }

    /** Calls recorded while [ContextStack.imagining] (see [LoopPools]): they report nothing, but mark the loops a call keeps memory of. */
    private val imaginedCalls = ArrayList<Call>()

    fun call(call: Call, imagined: Boolean = false) {
        if (imagined) imaginedCalls += call else calls += call
    }

    /**
     * A function used as a value of [type]: `fn name` ([closure] null, its [body]) or a lambda, with the parameters [params]. What it
     * keeps in its arguments becomes [type]'s; what it keeps elsewhere is an error, unless it was made inside `unsafe` ([unsafe]).
     */
    private class FunctionValue(val location: Loc, val name: String, val key: Any, val params: List<String>, val type: Any, val closure: Closure?, val unsafe: Boolean)

    private val functionValues = ArrayList<FunctionValue>()

    /** `fn name` of the function [body] with the parameters [params], as a value of [type] (a FunctionType). */
    fun functionValue(location: Loc, name: String, body: TBlock, params: List<String>, type: Any, unsafe: Boolean) {
        functionValues += FunctionValue(location, name, body, params, type, null, unsafe)
    }

    /** The lambda [closure], a value of [type], made inside `unsafe` if [unsafe]. */
    fun lambda(closure: Closure, type: Any, unsafe: Boolean) {
        functionValues += FunctionValue(closure.location, "the lambda", closure, closure.params, type, closure, unsafe)
    }

    /** What [v] keeps of its arguments, or of memory it allocates in the current pool, outside its own arguments. */
    private fun keptElsewhere(v: FunctionValue): Store? = stores[v.key].orEmpty().firstOrNull {
        (base(it.value) in v.params || it.value == Lifetimes.POOL_SOURCE) && !byType(v, it)
    }

    /** Whether [s], a store of [v], can be checked where a value of its type is called: in an argument, or kept after the call (`escaping`). */
    private fun byType(v: FunctionValue, s: Store) = base(s.behind) in v.params || (s.escaping && s.behind == Lifetimes.GLOBAL_SOURCE)

    /** Which function value (`fn add`, a lambda at line 4) gave a function type each of its stores, for errors. */
    private val typeOrigins = HashMap<Pair<Any, Store>, String>()

    /** Adds to the stores of [v]'s type what it keeps in its own arguments, by position. Whether that added any. */
    private fun addToType(v: FunctionValue): Boolean {
        var added = false
        fun at(p: String) = v.params.indexOf(base(p)).let { i -> if (i < 0) p else position(i) + (if (p.endsWith(ELEMENTS)) ELEMENTS else "") }
        for (s in stores[v.key].orEmpty().toList()) {
            if (!(base(s.value) in v.params || s.value == Lifetimes.POOL_SOURCE) || !byType(v, s)) continue
            val store = Store(at(s.value), at(s.behind), s.escaping)
            if (stores.getOrPut(v.type) { HashSet() }.add(store)) {
                added = true
                typeOrigins[v.type to store] = if (v.closure != null) "the lambda at line ${v.location.getLine()}" else "`fn ${v.name}` (line ${v.location.getLine()})"
            }
        }
        return added
    }

    /**
     * What the result of each function may point into, by [key]: the names of its parameters (see [Lifetimes.noteResult]). Like a
     * lifetime parameter that is never written, as Goose infers its roots: `fn id(s: String): String` returns into s, and
     * `fn describe(l: List<Int>): String` into its caller's pool only, so `describe(local)` doesn't keep `local` alive.
     *
     * Bodies are compiled in any order, so a call can only use what a whole compilation found: [Compiler] compiles again with
     * [knownResults] from the last one when that one failed on memory ([EcMemoryError]). Until then a result may point into every
     * argument, as before; a body with errors, `addrof` of a local or a lambda that changes a `var` has no summary ([unknownResults]).
     */
    var knownResults: Map<String, Set<String>> = mapOf()
    private val results = HashMap<String, MutableSet<String>>()
    private val resultBodies = HashMap<String, Any>()
    private val unknownResults = HashSet<String>()

    /** Starts the body of [o]: what it returns isn't known until [finishResult] says the body was followed completely. */
    fun startResult(o: Overload) {
        val key = key(o)
        unknownResults += key
        results.getOrPut(key) { HashSet() }
        o.body?.let { resultBodies[key] = it }
    }

    /** The function [o] may return (or throw, or pass on with `try`) a value that points into [sources] (see [Lifetimes.sources]). */
    fun returns(o: Overload, sources: Set<String>) { results.getOrPut(key(o)) { HashSet() } += sources.map(::base) }

    /** [o]'s body is compiled; [complete]: everything it returns was seen. */
    fun finishResult(o: Overload, complete: Boolean) { if (complete) unknownResults -= key(o) }

    /** The parameters the result of [o] may point into, from the last compilation, or null when that isn't known. */
    fun resultRoots(o: Overload): Set<String>? = knownResults[key(o)]

    /**
     * What this compilation found that results point into, for the next one ([knownResults]). Also what a function stores
     * behind a pointer it returns: `fn f(mut p: String, s: String): String do p = s; ret p` returns into s too.
     */
    fun computedResults(): Map<String, Set<String>> {
        propagate()
        return results.filterKeys { it !in unknownResults }.mapValues { (key, roots) ->
            val all = HashSet(roots)
            val stored = resultBodies[key]?.let { stores[it] }.orEmpty()
            do { val more = stored.filter { base(it.behind) in all && base(it.value) !in all }.map { base(it.value) } } while (all.addAll(more))
            all
        }
    }

    /** `ret x` at [location] of a value that may be in memory that leaving frees, through the calls [via]: [message] if it is. */
    private class Leaving(val location: Loc, val message: String, val via: Via)

    private val leavings = ArrayList<Leaving>()

    /**
     * `ret` (or `throw`) at [location] of a value that may point to memory a deferred statement frees as the function leaves,
     * because it came out of the calls [via] given something that does. Once every body is compiled, [check] knows whether each
     * of those calls returns into the argument it was given that memory through, and reports [message] if so.
     */
    fun leaving(location: Loc, message: String, via: Via) { leavings += Leaving(location, message, via) }

    /** After every body is compiled: passes stores on from callees to callers, then reports the calls that leave memory dangling. */
    fun check(cd: CompileData) {
        propagate()
        val found = computedResults()
        for (l in leavings) {
            if (generateSequence(l.via) { it.next }.all { found[it.key]?.contains(it.param) == true })
                cd.recover { throw EcMemoryError(l.location, l.message) }
        }
        checkStores(cd)
    }

    /** A store a function has because the call [call] in its body has the callee's store [store]. */
    private class Origin(val call: Call, val store: Store)

    /** Why each store passed on by [propagate] is the caller's, for errors: which call, and which store of the callee. */
    private val storeOrigins = HashMap<Pair<Any, Store>, Origin>()

    /**
     * The calls through which [callee] has [store], for errors: " ('outer' keeps it because it calls `inner(...)` at line 3, which
     * keeps its 'v' in what 'l' points to)". Empty when [callee]'s own body stores it.
     */
    private fun chain(callee: Any, store: Store): String {
        val steps = ArrayList<String>()
        var at = storeOrigins[callee to store]
        val seen = HashSet<Pair<Any, Store>>()
        while (at != null && steps.size < 4 && seen.add(at.call.callee to at.store)) {
            val keeps = "keeps its '${base(at.store.value)}' in ${if (at.store.behind == Lifetimes.GLOBAL_SOURCE) (if (at.store.escaping) "memory that outlives the call" else "a global") else "what its '${base(at.store.behind)}' points to"}"
            steps += "it calls `${at.call.name}(...)` at line ${at.call.location.getLine()}, which $keeps"
            at = storeOrigins[at.call.callee to at.store]
        }
        return if (steps.isEmpty()) "" else " (it keeps it because ${steps.joinToString("; ")})"
    }

    /** Passes stores on from callees to callers, until nothing changes. */
    private fun propagate() {
        do {
            var changed = false
            for (v in functionValues) if (addToType(v)) changed = true
            for (c in calls) {
                val caller = c.caller ?: continue
                for (s in stores[c.callee].orEmpty().toList()) {
                    if (!c.follows(s)) continue
                    val values = valueOf(c, s)?.second ?: continue
                    val targets = if (s.behind == Lifetimes.GLOBAL_SOURCE) setOf(Lifetimes.GLOBAL_SOURCE)
                        else targetOf(c, s)?.second ?: (if (c.lambda != null) setOf(Lifetimes.GLOBAL_SOURCE) else continue)
                    for (j in values) if (base(j) in c.callerParams || j == Lifetimes.POOL_SOURCE)
                        for (k in targets) if ((base(k) in c.callerParams || k == Lifetimes.GLOBAL_SOURCE) && base(j) != base(k))
                            Store(j, k, s.escaping && k == Lifetimes.GLOBAL_SOURCE).let { store ->
                                if (stores.getOrPut(caller) { HashSet() }.add(store)) {
                                    changed = true
                                    if (c.lambda == null && c.callee !is github.mrh0.ect2.FunctionType) storeOrigins[caller to store] = Origin(c, s)
                                }
                            }
                }
            }
        } while (changed)
    }

    /** Reports the claims, function values and calls whose stores leave memory dangling (after [propagate]). */
    private fun checkStores(cd: CompileData) {
        for (claim in claims) {
            val store = stores[claim.body].orEmpty().firstOrNull { base(it.value) == claim.param } ?: continue
            val place = when {
                store.escaping -> "an `escaping` parameter, which keeps it after the call"
                store.behind == Lifetimes.GLOBAL_SOURCE -> "a global"
                else -> "what '${base(store.behind)}' points to, which the caller keeps"
            }
            cd.recover { throw EcMemoryError(claim.location, "${claim.message?.let { "$it: " } ?: ""}`const assert` failed: '${claim.param}' escapes. The function keeps it${if (store.value.endsWith(ELEMENTS)) " (or its elements)" else ""} in $place, " +
                "so the caller's memory must outlive the call. Store a copy instead (`clone`), or remove the claim.") }
        }
        for (fv in functionValues) {
            if (fv.unsafe || fv.closure != null) continue
            val store = keptElsewhere(fv) ?: continue
            val what = if (store.value == Lifetimes.POOL_SOURCE) "allocates in the current pool and keeps that memory" else "keeps '${base(store.value)}'"
            cd.recover { throw EcMemoryError(fv.location, "`fn ${fv.name}` can't be used as a function value: it $what in ${placeOf(store.behind, null)}, but where a function value is called, " +
                "what it is given and the current pool can't be checked against that. Call it directly instead, make it keep a copy in a pool of its own, or, when every call gives it memory that lives long enough, say so with `unsafe fn ${fv.name}`.") }
        }
        for (fv in functionValues) {
            val closure = fv.closure ?: continue
            if (fv.unsafe) continue
            val store = keptElsewhere(fv) ?: continue
            val place = placeOf(store.behind, closure)
            val message = if (store.value == Lifetimes.POOL_SOURCE)
                "This lambda allocates in the current pool and keeps that memory in $place, but the current pool where a function value is called isn't known here, and may be freed first. " +
                    "Allocate it in a pool that lives as long as $place instead (`clone(x, p)`, `format(p, \"...\")`, `new ... in p`), with `val p = pool` written before the lambda."
            else "This lambda keeps its parameter '${base(store.value)}' in $place, but what a function value is called with isn't followed, so it could be freed first. " +
                "Keep a copy in a pool that lives as long as $place instead: `clone(${base(store.value)}, p)`, with `val p = pool` written before the lambda."
            cd.recover { throw EcMemoryError(closure.location, "$message Or, when every call gives it memory that lives long enough, say so with `unsafe` before the lambda.") }
        }
        for (c in calls) {
            if (c.unsafe) continue
            for (s in stores[c.callee].orEmpty()) {
                if (!c.follows(s)) continue
                val memory = valueOf(c, s)?.first ?: continue
                val target = if (s.behind == Lifetimes.GLOBAL_SOURCE) null else (targetOf(c, s) ?: if (c.lambda != null) null to setOf() else continue).first
                if (target != null && memory.depth <= target.depth) continue
                cd.recover { throw EcMemoryError(c.location, message(c, s, memory)) }
            }
        }
        // `unsafe` vouches for the code as written, not for a pool the compiler would add: what it keeps counts here.
        for (c in imaginedCalls) {
            for (s in stores[c.callee].orEmpty()) {
                if (!c.follows(s)) continue
                val memory = valueOf(c, s)?.first ?: continue
                val target = if (s.behind == Lifetimes.GLOBAL_SOURCE) null else (targetOf(c, s) ?: if (c.lambda != null) null to setOf() else continue).first
                if (target == null || memory.depth > target.depth) LoopPools.keep(memory, target?.depth)
            }
        }
    }

    /** The memory the value [s] stores points into at the call [c], and what that is in the caller's terms; null when it stores nothing that points. */
    private fun valueOf(c: Call, s: Store): Pair<Local?, Set<String>>? {
        if (s.value == Lifetimes.POOL_SOURCE) return c.pool to c.poolSources
        val arg = c.args.firstOrNull { it.param == base(s.value) && it.holdsPointer } ?: return null
        return if (s.value.endsWith(ELEMENTS)) arg.memory?.stored() to arg.sources.map(::element).toSet() else arg.memory to arg.sources
    }

    /** The memory [s] stores into at the call [c], and what it is in the caller's terms; null when that argument isn't given. */
    private fun targetOf(c: Call, s: Store): Pair<Local?, Set<String>>? {
        val arg = c.args.firstOrNull { it.param == base(s.behind) && it.isTarget } ?: return null
        return if (s.behind.endsWith(ELEMENTS)) arg.memory?.stored() to arg.sources.map(::element).toSet() else arg.memory to arg.pointees
    }

    /** Where [behind] is, for errors about what a function or the lambda [closure] keeps. */
    private fun placeOf(behind: String, closure: Closure?): String {
        val name = base(behind)
        return when {
            behind == Lifetimes.GLOBAL_SOURCE -> "a global"
            name.startsWith(CAPTURE) -> {
                val variable = name.removePrefix(CAPTURE).removeSuffix(VALUE)
                if (closure?.captures?.get(variable)?.byReference == true && !name.endsWith(VALUE)) "'$variable', a variable of the function around it" else "what '$variable' points to"
            }
            else -> "what '$name' points to"
        }
    }

    /** A parameter's name for errors: those of a function value are known by [position]. */
    private fun label(param: String): String = base(param).let { if (it.startsWith(POSITION)) "argument ${it.removePrefix(POSITION).toInt() + 1}" else "'$it'" }

    private fun message(c: Call, s: Store, memory: Local): String {
        if (c.callee is github.mrh0.ect2.FunctionType) {
            val behind = if (s.escaping) "memory that outlives the call" else if (s.behind == Lifetimes.GLOBAL_SOURCE) "a global" else "what ${label(s.behind)} points to"
            val origin = typeOrigins[c.callee to s] ?: "a function"
            val what = if (s.value == Lifetimes.POOL_SOURCE) "allocates in the current pool and keeps that memory in $behind, but here the current pool is ${memory.memory}"
                else "keeps ${label(s.value)} in $behind, but here ${label(s.value)} points to ${memory.memory}"
            val shown = c.args.firstOrNull { it.param == base(s.value) }?.shown
            return "${c.name.replaceFirstChar { it.uppercase() }} is a function value of type ${c.callee}, and $origin, one of that type, $what, which is freed ${memory.ends}, first. ${memory.fixFor(shown)}"
        }
        if (c.lambda != null) {
            val arg = c.args.firstOrNull { it.param == base(s.value) }
            val shown = arg?.shown?.let { if (it.startsWith("`")) it else "'$it'" } ?: "'${base(s.value)}'"
            val place = if (s.behind == Lifetimes.GLOBAL_SOURCE || targetOf(c, s) == null) "${placeOf(s.behind, c.lambda).takeIf { s.behind != Lifetimes.GLOBAL_SOURCE } ?: "a global"}, which outlives the lambda's call"
                else placeOf(s.behind, c.lambda)
            return "The lambda keeps $shown in $place, but $shown points to ${memory.memory}, which is freed ${memory.ends}, and the lambda may run after that. ${memory.fixFor(arg?.shown?.takeIf { !it.startsWith("`") })}"
        }
        val behind = base(s.behind)
        val (place, lives) = when {
            s.escaping -> "memory that outlives the call (its parameter is `escaping`, as another thread or C code uses it)" to "while the function goes on using it"
            s.behind == Lifetimes.GLOBAL_SOURCE -> "a global" to "but a global outlives it"
            else -> "what '$behind' points to" to "before '$behind' is"
        }
        if (s.value == Lifetimes.POOL_SOURCE) {
            val fix = if (memory.pool != null) "Call it outside the `pool` block, or give it a pool that outlives the block if it takes one." else memory.fix
            return "'${c.name}' allocates in the current pool and keeps that memory in $place${chain(c.callee, s)}, but here the current pool is ${memory.memory}, which is freed ${memory.ends}, $lives. $fix"
        }
        val shown = c.args.firstOrNull { it.param == base(s.value) }?.shown
        return "'${c.name}' keeps '${base(s.value)}'${if (s.value.endsWith(ELEMENTS)) " (or its elements)" else ""} in $place${chain(c.callee, s)}, but here '${base(s.value)}' points to ${memory.memory}, " +
            "which is freed ${memory.ends}, $lives. ${memory.fixFor(shown)}"
    }

    companion object {
        /**
         * Names the function [o] the same way in every compilation of a program (its C name may not be): where it is written,
         * and the types of this copy of it (a union copy, a generic instance, a copy that leaves out parameters with defaults).
         */
        fun key(o: Overload): String =
            "${o.location}|${o.name}(${o.params.joinToString { "${it.name}: ${it.type}" }}${o.varArg?.let { ", ...${it.type}" } ?: ""}): ${o.returns} throws ${o.throws}"

        /** Ends a source that stands for what the elements of an array point into. */
        private const val ELEMENTS = "[]"

        /** Starts the parameter of a lambda that stands for something it uses of the function around it ([Closure.escapeParams]). */
        private const val CAPTURE = "^"

        /** Ends that of the value of a captured `var` (the plain one is its place). */
        private const val VALUE = "*"

        /** Starts the parameters of a function type, which [position] names. */
        private const val POSITION = "%"

        /** The parameter at [index] of a function type, whose stores are those of every function value of that type. */
        fun position(index: Int) = "$POSITION$index"

        /** The lambda's parameter for the captured `val` x, or the place of the `var` x. */
        fun capture(name: String) = "$CAPTURE$name"

        /** The lambda's parameter for the value of the captured `var` x. */
        fun captureValue(name: String) = "$CAPTURE$name$VALUE"

        /** What the elements of a value from [source] point into: `p[]` for a parameter p. Pools and globals stay as they are. */
        fun element(source: String): String = if (source.startsWith("#")) source else base(source) + ELEMENTS

        /** The parameter (or pool, or global) that [source] is about. */
        fun base(source: String): String = source.removeSuffix(ELEMENTS)
    }
}
