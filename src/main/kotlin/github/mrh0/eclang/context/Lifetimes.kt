package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.accessor.TAccessor
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.branch.TInlineIf
import github.mrh0.eclang.ast.token.data.TCreateArray
import github.mrh0.eclang.ast.token.data.TCreateCArray
import github.mrh0.eclang.ast.token.data.TInterpolated
import github.mrh0.eclang.ast.token.data.record.TCreateRecord
import github.mrh0.eclang.ast.token.data.record.TCreateRecordTyped
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.ast.token.op.TAddressOf
import github.mrh0.eclang.ast.token.op.TCastNotNull
import github.mrh0.eclang.ast.token.op.TNew
import github.mrh0.eclang.ast.token.op.TNullishCoalescing
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
 * (`val p = addrof x; ret p`), record and array literals, `new`, and calls that are given a pool; what a pointer
 * read from memory (`p.next`, `a[i]`, `@p`) points to is not tracked.
 */
object Lifetimes {
    /** The local memory the value of [tok] points into, or null when it lives at least as long as the function call. */
    fun value(cd: CompileData, tok: ITok): Local? = when (val t = tok.unnested()) {
        is TAddressOf -> place(cd, t.expr)
        is TNamed -> if (TNamed.isCurrentPool(cd, t.name)) cd.poolOrigin() else cd.ctx().stack.originOf(Util.testIdentifier(t.location, t.name))
        is TCreateArray -> deepest(listOf(arrayLiteral(cd)) + t.values.map { value(cd, it) })
        is TCreateCArray -> deepest(listOf(arrayLiteral(cd)) + t.values.map { value(cd, it) })
        is TCreateRecord -> deepest(t.values.map { value(cd, it) })
        is TCreateRecordTyped -> deepest(t.values.map { value(cd, it) })
        is TNew -> deepest(listOf(pool(cd, t.pool), if (t.copies) value(cd, t.value) else null))
        is TExprCall -> deepest(t.pools.map { pool(cd, it) })
        is TInterpolated -> cd.poolOrigin()
        is TInlineIf -> deepest(listOf(value(cd, t.body), value(cd, t.elseBody)))
        is TNullishCoalescing -> deepest(listOf(value(cd, t.left), value(cd, t.right)))
        is TCast -> value(cd, t.expr)
        is TCastUnsafe -> value(cd, t.expr)
        is TCastNotNull -> value(cd, t.expr)
        is TTry -> value(cd, t.expr)
        else -> null
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
        is TAccessor -> value(cd, t.expr) // elements are behind a pointer
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
        val depth = if (root != null) cd.ctx().stack.definedDepth(root) else place(cd, target)?.depth
        val local = checkStore(cd, location, value, type, depth, what) ?: return
        if (root != null) cd.ctx().stack.setOrigin(root, local)
    }

    /** The local variable whose own memory [tok] is (`x`, `x.field`, not through a pointer), or null. */
    private fun localRoot(cd: CompileData, tok: ITok): String? = when (val t = tok.unnested()) {
        is TNamed -> Util.testIdentifier(t.location, t.name).takeIf { cd.ctx().stack.definedDepth(it) != null }
        is TAccessorNamed -> if (t.throughPointer) null else localRoot(cd, t.expr)
        else -> null
    }

    /** Remembers what memory the local [name] points into after `val name = value`. */
    fun define(cd: CompileData, name: String, value: ITok, type: Type) {
        // [name] is the variable's fixed name (see Util.testIdentifier), as it was defined.
        if (holdsPointer(type)) value(cd, value)?.let { cd.ctx().stack.setOrigin(name, it) }
    }

    /** Whether a value of [type] can point to memory (a pointer, String, array, or something holding one). */
    fun holdsPointer(type: Type, seen: MutableSet<Type> = HashSet()): Boolean {
        if (!seen.add(type)) return false
        return when (type) {
            is Pointer, is ArrayType -> true
            Types.String, Types.CString -> true
            is Union -> type.members.any { holdsPointer(it, seen) }
            is Tuple -> type.elements.any { holdsPointer(it, seen) }
            is Record -> type.fields.values.any { holdsPointer(it, seen) }
            is Result -> holdsPointer(type.value, seen) || holdsPointer(type.error, seen)
            else -> false
        }
    }
}
