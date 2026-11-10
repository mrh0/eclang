package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.data.TNull
import github.mrh0.eclang.ast.token.function.call.Builtins
import github.mrh0.eclang.ast.token.op.TCastNotNull
import github.mrh0.eclang.ast.token.op.TReadPointer
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.error.EcAssignConstError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRDestroy
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `destroy p`: frees the pool p, and the pools made in it. It must be a pool this function made with `new Pool`
 * ([Lifetimes.owns]), unless it is written `unsafe destroy p`: another pool may hold memory that code elsewhere still
 * uses. Never the current pool or that of a `pool do` block, which are freed for you ([Builtins.destroyProblem]).
 * Afterwards p can't be used ([Lifetimes.kill]). A `Pool?` variable or field may be null, which is skipped, and is set to null.
 */
class TStatementDestroy(location: Loc, private val target: ITok) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val shown = Lifetimes.shown(target)
        val (type, pool) = cd.usable(location, target.process(cd, Types.None), "`destroy`")
        val nullable = type is Union && type.isNullable && Builtins.isPoolType(cd, cd.types.nonNull(type))
        if (!nullable && !Builtins.isPoolType(cd, type))
            throw EcError(location, "`destroy` frees a whole pool (a `Pool`), but this is a ${cd.types.concrete(type)}: one value can't be freed on its own. " +
                "Allocate it in a pool of its own (`val p = new Pool`, then `new ... in p`), and destroy that pool when the value is no longer needed.")
        Builtins.destroyProblem(cd, target)?.let { throw EcError(location, it) }
        if (!cd.isUnsafe && !Lifetimes.owns(cd, target, clear = false)) {
            val written = if (repeatable(target)) "`destroy $shown`" else "This `destroy`"
            val fix = if (repeatable(target)) "`unsafe destroy $shown`" else "`unsafe` before `destroy`"
            throw EcError(location, "$written frees a pool this function didn't make with `new Pool`: ${Lifetimes.notOwned(cd, target)}. Code elsewhere could be left pointing to freed memory, or destroy it again. " +
                "Destroy it where it was made (`val p = new Pool`), or, when nothing else uses it any more, say so with $fix.")
        }
        if (nullable && !repeatable(target))
            throw EcError(location, "`destroy` sets a `Pool?` to null once it is freed, so it takes a variable or a field (`destroy p`, `destroy h.p`). Put the pool in a variable first: `var p = ...`, then `destroy p`.")
        val id = Lifetimes.poolId(cd, target)
        cd.noteEffect()
        Lifetimes.kill(cd, location, target, free = true, id)
        val destroy = IRDestroy(location, pool, nullable)
        if (!nullable) return Types.None to destroy
        val cleared = try {
            TStatementAssign(location, target, "", TNull(location)).process(cd, Types.None).second
        } catch (e: EcAssignConstError) {
            throw EcError(location, "`destroy $shown` sets the `Pool?` '$shown' to null once it is freed, so it must be a `var`. Declare it with `var`, or give it the type `Pool` if it is never null.")
        }
        return Types.None to IRBlock(location, listOf(destroy, cleared), listOf())
    }

    /** Whether [tok] can be read again, for setting it to null: a variable, or a field of one. */
    private fun repeatable(tok: ITok): Boolean = when (val t = tok.unnested()) {
        is TNamed -> true
        is TAccessorNamed -> repeatable(t.expr)
        is TReadPointer -> repeatable(t.expr)
        is TCastNotNull -> repeatable(t.expr)
        else -> false
    }

    override fun toString() = "TStatementDestroy($target)"
}
