package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.accessor.TAccessor
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.branch.TInlineIf
import github.mrh0.eclang.ast.token.branch.TWhenExpr
import github.mrh0.eclang.ast.token.data.record.TUpdateRecord
import github.mrh0.eclang.ast.token.function.call.TSafeCall
import github.mrh0.eclang.ast.token.op.TNullishJump
import github.mrh0.eclang.ast.token.data.TCreateArray
import github.mrh0.eclang.ast.token.data.TCreateCArray
import github.mrh0.eclang.ast.token.data.TCreateTuple
import github.mrh0.eclang.ast.token.data.TInterpolated
import github.mrh0.eclang.ast.token.data.record.TCreateRecord
import github.mrh0.eclang.ast.token.data.record.TCreateRecordTyped
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.ast.token.op.TAddressOf
import github.mrh0.eclang.ast.token.op.TCastNotNull
import github.mrh0.eclang.ast.token.op.TNew
import github.mrh0.eclang.ast.token.op.TNullishCoalescing
import github.mrh0.eclang.ast.token.op.TPipe
import github.mrh0.eclang.ast.token.op.TIt
import github.mrh0.eclang.ast.token.op.TCallValue
import github.mrh0.eclang.ast.token.op.TReadPointer
import github.mrh0.eclang.ast.token.op.TTry
import github.mrh0.eclang.ast.token.type.TCast
import github.mrh0.eclang.ast.token.type.TCastUnsafe
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*

/**
 * Memory that is freed before the function returns: a local variable, the elements of an array literal (C stack
 * memory), or the pool of a `pool do` block. It is freed when the scope at [depth] ends (see `ContextStack.depth`),
 * so a pointer into it may only be kept in variables defined at that depth or deeper.
 * [memory] names it in errors ("'x', a local variable"), [ends] says when it is freed, [fix] how to keep the value.
 */
class Local(val depth: Int, val memory: String, val ends: String, val fix: String)

/**
 * Catches dangling pointers at compile time: a pointer to memory that is freed when a block or the function ends
 * must not be returned, thrown, or stored where it outlives that memory. Values are followed through variables
 * (`val p = addrof x; ret p`), record and array literals, `new`, pipes, calls (a call's result may point into its
 * arguments, or into the pools it is given), and what is read out of memory: `p.next`, `a[i]`, `a[x..<y]` and `@p`
 * may point where p and a do, since what is stored there must live at least as long as p and a's memory.
 *
 * Memory of a pool that isn't tied to a block (`newPool()`) is followed the same way by [allocations]: after
 * `clearPool(p)` or `freePool(p)`, a variable holding something allocated in p before can't be used.
 */
object Lifetimes {
    /** The local memory the value of [tok] points into, or null when it lives at least as long as the function call. */
    fun value(cd: CompileData, tok: ITok): Local? = when (val t = tok.unnested()) {
        is TAddressOf -> place(cd, t.expr)
        is TNamed -> if (TNamed.isCurrentPool(cd, t.name)) cd.poolOrigin() else cd.ctx().stack.originOf(Util.testIdentifier(t.location, t.name))
        is TCreateArray -> deepest(listOf(if (t.isFixed) null else arrayLiteral(cd)) + flows(t).map { value(cd, it) })
        is TCreateCArray -> deepest(listOf(arrayLiteral(cd)) + flows(t).map { value(cd, it) })
        is TAccessorNamed -> deepest(listOf(if (t.viewsValue) place(cd, t.expr) else null) + flows(t).map { value(cd, it) })
        is TAccessor -> deepest(listOf(if (t.viewsValue) place(cd, t.expr) else null) + flows(t).map { value(cd, it) })
        is TNew -> deepest(listOf(pool(cd, t.pool)) + flows(t).map { value(cd, it) })
        is TExprCall -> deepest(t.pools.map { pool(cd, it) } + flows(t).map { value(cd, it) })
        is TInterpolated -> cd.poolOrigin()
        is TCreateRecord -> deepest(listOf(if (t.usedDefaults) cd.poolOrigin() else null) + flows(t).map { value(cd, it) })
        is TCreateRecordTyped -> deepest(listOf(if (t.usedDefaults) cd.poolOrigin() else null) + flows(t).map { value(cd, it) })
        is TPipe -> piped(cd, t) { value(cd, it) }
        is TIt -> unpiped(cd) { value(cd, it) }
        else -> deepest(flows(t).map { value(cd, it) })
    }

    /**
     * The values whose memory the value of [t] may point into, besides memory [t] allocates or is itself: the parts of
     * a literal, a call's arguments, the value a field, element, slice or `@` reads from, the branches of `if`/`??`.
     */
    private fun flows(t: ITok): List<ITok> = when (t) {
        is TCreateArray -> t.values
        is TCreateCArray -> t.values
        is TCreateTuple -> t.values
        is TCreateRecord -> t.values
        is TCreateRecordTyped -> t.values
        is TNew -> if (t.copies) listOf(t.value) else listOf()
        is TExprCall -> t.aliased
        is TCallValue -> t.args
        is TInlineIf -> listOf(t.body, t.elseBody)
        is TNullishCoalescing -> listOf(t.left, t.right)
        is TNullishJump -> listOf(t.left)
        is TWhenExpr -> t.flows
        is TSafeCall -> listOfNotNull(t.receiver, t.call)
        is TUpdateRecord -> t.values + t.base
        is TCast -> listOf(t.expr)
        is TCastUnsafe -> listOf(t.expr)
        is TCastNotNull -> listOf(t.expr)
        is TTry -> listOf(t.expr)
        is TAccessor -> listOf(t.expr)
        is TAccessorNamed -> listOf(t.expr)
        is TReadPointer -> listOf(t.expr)
        else -> listOf()
    }

    /** [analyze] of the stage of `value |> stage`, with `it` standing for the value. */
    private fun <R> piped(cd: CompileData, t: TPipe, analyze: (ITok) -> R): R {
        cd.pipedTokens += t.value
        try { return analyze(t.stage) } finally { cd.pipedTokens.removeAt(cd.pipedTokens.size - 1) }
    }

    /** [analyze] of what `it` stands for (in the scope of pipes around that value), or null outside a pipe. */
    private fun <R> unpiped(cd: CompileData, analyze: (ITok) -> R): R? {
        val value = cd.pipedTokens.removeLastOrNull() ?: return null
        try { return analyze(value) } finally { cd.pipedTokens += value }
    }

    /** The memory of named pools (not `pool do` blocks or the current pool) the value of [tok] may be in. */
    fun allocations(cd: CompileData, tok: ITok): Set<Alloc> = when (val t = tok.unnested()) {
        is TNamed -> cd.ctx().stack.allocationsOf(Util.testIdentifier(t.location, t.name))
        is TAddressOf -> placeAllocations(cd, t.expr)
        is TNew -> setOfNotNull(allocIn(cd, t.pool)) + flows(t).flatMap { allocations(cd, it) }
        is TExprCall -> t.pools.mapNotNull { allocIn(cd, it) }.toSet() + flows(t).flatMap { allocations(cd, it) }
        is TPipe -> piped(cd, t) { allocations(cd, it) }
        is TIt -> unpiped(cd) { allocations(cd, it) } ?: setOf()
        else -> flows(t).flatMap { allocations(cd, it) }.toSet()
    }

    /** The named-pool memory the place [tok] (what `addrof` points at) is in. */
    private fun placeAllocations(cd: CompileData, tok: ITok): Set<Alloc> = when (val t = tok.unnested()) {
        is TCastNotNull -> placeAllocations(cd, t.expr)
        is TAccessorNamed -> if (t.throughPointer) allocations(cd, t.expr) else placeAllocations(cd, t.expr)
        is TAccessor, is TReadPointer -> flows(t).flatMap { allocations(cd, it) }.toSet()
        else -> setOf()
    }

    /** Memory allocated now in the pool [tok] (a local pool variable), or null for the current pool or one that isn't followed. */
    private fun allocIn(cd: CompileData, tok: ITok?): Alloc? {
        val t = tok?.unnested() as? TNamed ?: return null
        val pool = cd.ctx().stack.poolOf(Util.testIdentifier(t.location, t.name)) ?: return null
        return cd.ctx().stack.alloc(pool, t.name)
    }

    /**
     * `clearPool(tok)` or, with [free], `freePool(tok)` at [location]: what was allocated in that pool so far is freed,
     * and with [free] the pool can't be used any more.
     */
    fun kill(cd: CompileData, location: Loc, tok: ITok, free: Boolean) {
        val t = tok.unnested() as? TNamed ?: return
        val pool = cd.ctx().stack.poolOf(Util.testIdentifier(t.location, t.name)) ?: return
        val how = "`${if (free) "freePool" else "clearPool"}(${t.name})` at line ${location.getLine()}"
        cd.ctx().stack.kill(pool, how, free)
    }

    /** A loop that assigns, clears or frees the pool variable [name] (in [assigned]) frees its memory for the next round. */
    fun loopKills(cd: CompileData, location: Loc, assigned: Set<String>) = assigned.forEach { raw ->
        val name = Util.testIdentifier(location, raw)
        if (cd.ctx().getRaw(name)?.getType() !== cd.types.pool) return@forEach
        val pool = cd.ctx().stack.poolOf(name) ?: return@forEach
        cd.ctx().stack.kill(pool, "an earlier round of the loop at line ${location.getLine()} (the loop clears, frees or replaces '$raw')", free = false)
    }

    /** Checks that the local [name] (read or changed at [location]) doesn't point to pool memory that was freed. */
    fun checkAlive(cd: CompileData, location: Loc, name: String, shown: String) {
        val stack = cd.ctx().stack
        stack.poolOf(name)?.takeIf { cd.ctx().getRaw(name)?.getType() === cd.types.pool }?.let { pool ->
            stack.freedBy(pool)?.let { how ->
                throw EcError(location, "Can't use the pool '$shown' here: $how freed it. Use it before freeing it, or make a new one first (`$shown = newPool()`, which needs a `var`).")
            }
        }
        for (alloc in stack.allocationsOf(name)) {
            val how = stack.killedBy(alloc) ?: continue
            throw EcError(location, "Can't use '$shown' here: it points to memory of pool '${alloc.poolName}' that $how freed. " +
                "Use it before the pool is cleared or freed, or keep a copy in a pool that lives on: `clone($shown, other)`.")
        }
    }

    /** The local memory that the place [tok] (what `addrof` points at) is in, or null when it isn't local. */
    fun place(cd: CompileData, tok: ITok): Local? = when (val t = tok.unnested()) {
        is TNamed -> cd.ctx().stack.definedDepth(Util.testIdentifier(t.location, t.name))?.let { depth ->
            val kind = if (cd.ctx().getRaw(Util.testIdentifier(t.location, t.name))?.kind() == "a parameter") "parameter" else "variable"
            Local(depth, "'${t.name}', a local $kind", "when its block ends",
                "Allocate the value in a pool instead: `new ${t.name}` is a copy of it in the current pool (`new ${t.name} in p` in pool p).")
        }
        is TCastNotNull -> place(cd, t.expr)
        is TAccessorNamed -> if (t.throughPointer) value(cd, t.expr) else place(cd, t.expr)
        is TAccessor -> if (t.inValue) place(cd, t.expr) else value(cd, t.expr) // elements are behind a pointer, except those of a `T[N]`
        is TReadPointer -> value(cd, t.expr)
        else -> null
    }

    /** The memory [tok], a pool given to `new` or a call (null: the current pool), is freed with. */
    private fun pool(cd: CompileData, tok: ITok?): Local? = if (tok == null) cd.poolOrigin() else value(cd, tok)

    private fun arrayLiteral(cd: CompileData): Local? = if (!cd.inBody) null else
        Local(cd.ctx().depth(), "the elements of an array literal (on the stack)", "when its block ends",
            "Copy it into a pool with `clone(...)`, e.g. `clone([1, 2, 3])`.")

    /** The memory freed first (the deepest scope) of [locals]. */
    private fun deepest(locals: List<Local?>): Local? = locals.filterNotNull().maxByOrNull { it.depth }

    /** The local memory a `pool do` block at [line] (opened at scope [depth]) frees when it ends. */
    fun poolBlock(depth: Int, line: Int) = Local(depth, "memory from the pool of the `pool` block at line $line", "when that block ends",
        "Code in a `pool` block allocates in its pool, and so do the functions it calls. To keep a value, copy it into a pool that outlives the block: write `val outer = pool` before the block, then `clone(x, outer)` (or `new ... in outer`).")

    /** `ret value` (also `throw`, and a lambda's result): [value] must not point into memory the function frees. */
    fun checkReturn(cd: CompileData, location: Loc, value: ITok, type: Type, verb: String = "return") {
        if (!holdsPointer(type)) return
        val local = value(cd, value) ?: return
        throw EcError(location, "Can't $verb this: it points to ${local.memory}, which is freed when the function returns, so it would point to freed memory. ${local.fix}")
    }

    /**
     * Storing [value] (of [type]) in a place that lives until the scope at [targetDepth] ends (null: longer than the
     * function, e.g. a global or memory behind a pointer). [target] names the place in errors.
     */
    fun checkStore(cd: CompileData, location: Loc, value: ITok, type: Type, targetDepth: Int?, target: String): Local? {
        if (!holdsPointer(type)) return null
        val local = value(cd, value) ?: return null
        if (targetDepth != null && local.depth <= targetDepth) return local
        val lives = if (targetDepth == null) "$target outlives this function" else "$target lives longer"
        throw EcError(location, "Can't store this in $target: it points to ${local.memory}, which is freed ${local.ends}, but $lives, so it would be left pointing to freed memory. ${local.fix}")
    }

    /**
     * `target = value` (of [type]): checks [value] doesn't outlive the place it's stored in, called [what] in errors.
     * A place inside a local variable (`x`, or a field of a record `x` holds) then remembers what it points to.
     */
    fun assign(cd: CompileData, location: Loc, target: ITok, value: ITok, type: Type, what: String) {
        val root = localRoot(cd, target)
        trackAllocations(cd, target, value, type)
        val depth = if (root != null) cd.ctx().stack.definedDepth(root) else place(cd, target)?.depth
        val local = checkStore(cd, location, value, type, depth, what) ?: return
        if (root != null) cd.ctx().stack.setOrigin(root, local)
    }

    /**
     * After `target = value`: a plain variable now holds what [value] holds (a pool variable, the pool), and a
     * variable whose field or memory is set (`x.f = v`, `x.next = v`, `x[i] = v`) also holds what v holds.
     */
    private fun trackAllocations(cd: CompileData, target: ITok, value: ITok, type: Type) {
        val stack = cd.ctx().stack
        val t = target.unnested()
        if (t is TNamed) {
            val name = Util.testIdentifier(t.location, t.name)
            if (type === cd.types.pool) stack.setPool(name, poolSource(cd, value))
            if (holdsPointer(type)) stack.setAllocations(name, allocations(cd, value))
            return
        }
        if (!holdsPointer(type)) return
        val base = baseVariable(target) ?: return
        stack.setAllocations(Util.testIdentifier(base.location, base.name), allocations(cd, value), add = true)
    }

    /** The variable a place is in or reached from: x for `x.f`, `x.next.f`, `x[i]`, `@x`. */
    private fun baseVariable(tok: ITok): TNamed? = when (val t = tok.unnested()) {
        is TNamed -> t
        is TAccessorNamed -> baseVariable(t.expr)
        is TAccessor -> baseVariable(t.expr)
        is TReadPointer -> baseVariable(t.expr)
        is TCastNotNull -> baseVariable(t.expr)
        else -> null
    }

    /** The pool a Pool value [tok] is, when it is a local pool variable (`val q = p`); null for a new or unknown pool. */
    private fun poolSource(cd: CompileData, tok: ITok): String? {
        val t = (tok.unnested().let { if (it is TCastNotNull) it.expr.unnested() else it }) as? TNamed ?: return null
        return cd.ctx().stack.poolOf(Util.testIdentifier(t.location, t.name))
    }

    /** The local variable whose own memory [tok] is (`x`, `x.field`, not through a pointer), or null. */
    private fun localRoot(cd: CompileData, tok: ITok): String? = when (val t = tok.unnested()) {
        is TNamed -> Util.testIdentifier(t.location, t.name).takeIf { cd.ctx().stack.definedDepth(it) != null }
        is TAccessorNamed -> if (t.throughPointer) null else localRoot(cd, t.expr)
        is TAccessor -> if (t.inValue) localRoot(cd, t.expr) else null
        else -> null
    }

    /** Remembers what memory the local [name] points into after `val name = value`. */
    fun define(cd: CompileData, name: String, value: ITok, type: Type) {
        // [name] is the variable's fixed name (see Util.testIdentifier), as it was defined.
        if (type === cd.types.pool) cd.ctx().stack.setPool(name, poolSource(cd, value))
        if (!holdsPointer(type)) return
        value(cd, value)?.let { cd.ctx().stack.setOrigin(name, it) }
        cd.ctx().stack.setAllocations(name, allocations(cd, value))
    }

    /** Whether a value of [type] can point to memory (a pointer, String, array, or something holding one). */
    fun holdsPointer(type: Type, seen: MutableSet<Type> = HashSet()): Boolean {
        if (!seen.add(type)) return false
        return when (type) {
            is Pointer, is ArrayType -> true
            Types.String, Types.CString -> true
            is Distinct -> holdsPointer(type.base, seen)
            is Union -> type.members.any { holdsPointer(it, seen) }
            is Tuple -> type.elements.any { holdsPointer(it, seen) }
            is FixedArray -> holdsPointer(type.element, seen)
            is Record -> type.fields.values.any { holdsPointer(it, seen) }
            is Result -> holdsPointer(type.value, seen) || holdsPointer(type.error, seen)
            else -> false
        }
    }
}
