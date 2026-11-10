package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRPoolBlock
import github.mrh0.eclang.ir.IRPoolLeave
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `pool do` / `pool name do`: runs [body] with a new pool, a child of the current one, as the current pool, so
 * `new`, `clone`, `format` and `"${...}"` allocate in it. The pool, and everything in it, is freed when the block
 * ends, however it ends (`ret`, `break`, `try`, ...); [Lifetimes] rejects keeping anything from it longer.
 * [name] names the pool inside the block, for `new x in name` from a nested block.
 */
class TStatementPool(location: Loc, private val name: String?, private val body: ITok) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val parent = cd.temp().replace("tmp", "parent")
        cd.ctx().push()
        val stack = cd.ctx().stack
        val fixedName = name?.let { testIdentifier(location, it) }
        val ivar = fixedName?.let { cd.ctx().define(location, Constant(it, cd.types.pool, cd.localCName(it), kind = KIND)) }
        val id = fixedName?.let { stack.poolOf(it) } ?: "block${stack.next()}"
        val local = Lifetimes.poolBlock(cd.ctx().depth(), location.getLine(), id)
        stack.blockPools += id
        stack.setPoolParents(id, setOfNotNull(cd.poolOrigin()?.pool ?: Lifetimes.CALLER_POOL))
        fixedName?.let { stack.setOrigin(it, local) }
        val pool = ivar?.cName() ?: "__ec_pool"
        // Deferred like any statement, so every way out of the block frees the pool after the block's own defers.
        cd.ctx().defer(location, Leave(location, parent, pool))
        if (cd.poolBlocks.isEmpty()) cd.poolEntry = parent
        cd.poolBlocks += local
        val block = try {
            body.process(cd, hint).second
        } finally {
            cd.poolBlocks.removeAt(cd.poolBlocks.size - 1)
        }
        val diverges = (block as? IRBlock)?.diverges == true
        val leave = if (diverges) listOf() else cd.noHoist("`try` can't be used in `defer`.") {
            cd.ctx().getNewDeferred().reversed().map { it.process(cd, Types.None).second }
        }
        cd.ctx().pop()
        if (diverges) cd.ctx().setReturned()
        return Types.None to IRBlock(location, listOf(IRPoolBlock(location, parent, if (name == null) null else pool, block, leave)), listOf(), diverges)
    }

    override fun toString() = "TStatementPool($name, $body)"

    /** The deferred statement that leaves the block: frees [pool] and makes [parent] the current pool again. */
    private class Leave(location: Loc, private val parent: String, private val pool: String) : NonValueTok(location) {
        override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = Types.None to IRPoolLeave(location, parent, pool)
    }

    companion object {
        /** What the name of a `pool name do` block is, in errors ("'p' is the pool of a `pool` block"). */
        const val KIND = "the pool of a `pool` block"
    }
}
