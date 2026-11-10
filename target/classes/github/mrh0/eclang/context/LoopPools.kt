package github.mrh0.eclang.context

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import java.util.Collections
import java.util.IdentityHashMap

/**
 * Gives a loop's rounds a pool of their own when they allocate in the current pool and nothing a round allocates is used after
 * that round: the code is the same as with a `pool do` block around the body, which the program doesn't have to write. Without
 * it, every round would add to a pool that lives as long as the function's caller wants (in `main`, until the program ends).
 * Only loops outside `pool do` blocks, in functions (not lambdas), are looked at. The C of a round's pool ([IRRound], [RoundLeave])
 * is written once that is decided ([decide]), after every body is compiled; until then it is there, but writes nothing.
 *
 * Whether `pool do` would compile is what [Lifetimes] decides for a real block, so it is asked the same thing: each step it
 * follows in such a loop runs a second time, [ContextStack.imagining], where memory allocated in the current pool is
 * memory of the round ([memory]), freed when the round ends. A step that would keep it longer marks the loop [Loop.kept]
 * instead of reporting an error. Errors and the real analysis aren't affected: imagined steps have their own origins
 * ([ContextStack.imaginedOrigins]) and calls ([Escapes.imaginedCall]), and what goes wrong in them is dropped.
 *
 * Allocations are noted where they are compiled ([note]); a call of an eclang function counts when that function allocates
 * in its caller's pool, which is known once every body is compiled ([warn]).
 */
object LoopPools {
    /**
     * A loop outside `pool do` blocks, whose round starts at scope [depth]. [kept]: something a round allocates outlives it.
     * [parent] and [pool] are the C variables of the pool around the loop and the round's own; [registered]: every way out of a round
     * frees it (see [ContextStack.PendingRound]); [enabled]: the rounds get a pool.
     */
    class Loop(val location: Loc, val depth: Int, val parent: String, val pool: String) {
        var kept = false
        var registered = false
        var enabled = false
    }

    /**
     * Something compiled at [location] that allocates in the current pool, in the function [body] (null: a lambda or a global),
     * inside [loops] (innermost first). With [callee], it is a call, which allocates only if that function does. [what] says what it is.
     */
    class Allocation(val location: Loc, val what: String, val body: TBlock?, val loops: List<Loop>, val callee: TBlock?)

    /** The memory a round of the innermost loop of [loops] allocates in the current pool. */
    fun memory(loops: List<Loop>) = Local(loops.last().depth, "memory a round of the loop at line ${loops.last().location.getLine()} allocates",
        "when the round ends", "", loopPools = loops.toList())

    /** [local] is kept until the scope at [depth] ends (null: longer than the function): the rounds it would outlive keep it. Whether it is a round's memory. */
    fun keep(local: Local, depth: Int?): Boolean {
        if (local.loopPools.isEmpty()) return false
        for (loop in local.loopPools) if (depth == null || loop.depth > depth) loop.kept = true
        return true
    }

    /** Runs [step] as if the loops [loops] gave each round a pool of its own; what goes wrong in it is dropped. */
    fun imagine(cd: CompileData, loops: List<Loop>, step: () -> Unit) {
        if (loops.isEmpty()) return
        val stack = cd.ctx().stack
        val saved = cd.loopPools.toList()
        cd.loopPools.clear(); cd.loopPools += loops
        stack.imagining = true
        try {
            step()
        } catch (_: github.mrh0.eclang.error.EcError) {
            // An error of the program itself, which the real step reports.
        } finally {
            stack.imagining = false
            cd.loopPools.clear(); cd.loopPools += saved
        }
    }

    /** After every body is compiled (and [Escapes.check]): gives a pool to the rounds of the loops that allocate, where nothing outlives a round. */
    fun decide(cd: CompileData) {
        val allocating = Collections.newSetFromMap(IdentityHashMap<TBlock, Boolean>())
        do {
            var changed = false
            for (a in cd.allocations) if (a.body != null && a.body !in allocating && (a.callee == null || a.callee in allocating)) {
                allocating += a.body
                changed = true
            }
        } while (changed)
        for (a in cd.allocations) {
            if (a.callee != null && a.callee !in allocating) continue
            // The innermost loop it can free: a value kept from the round of an inner loop may still be freed by an outer one.
            val loop = a.loops.firstOrNull { !it.kept } ?: continue
            if (loop.registered) loop.enabled = true
        }
    }

    /** The C of a pool around [body], a round of [loop], when [Loop.enabled]: a `pool do` block's. */
    class IRRound(location: Loc, private val loop: Loop, private val body: IIR) : IR(location) {
        override fun toC(sb: CSourceBuilder, c: Context) {
            if (!loop.enabled) return body.toC(sb, c)
            sb.putStatement()
            sb.pushScope(BlockScope())
            sb.putStatement("apr_pool_t* ${loop.parent} = __ec_pool")
            sb.endStatement()
            sb.putStatement("apr_pool_t* ${loop.pool} = __ec_pool = __ec_pool_create(${loop.parent})")
            sb.endStatement()
            body.toC(sb, c)
            leave(sb, loop)
            sb.popScope()
        }

        override fun toString() = "round($body)"
    }

    /** Frees the round's pool on a way out of a round of [loop] (`break`, `continue`, `ret`, `try`), deferred in its body's scope. */
    class RoundLeave(location: Loc, private val loop: Loop) : NonValueTok(location) {
        override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = Types.None to object : IR(location) {
            override fun toC(sb: CSourceBuilder, c: Context) {
                if (!loop.enabled) return
                IRBlock.lineOf(sb, this)
                leave(sb, loop)
            }
        }
    }

    private fun leave(sb: CSourceBuilder, loop: Loop) {
        sb.putStatement("apr_pool_destroy(${loop.pool})")
        sb.endStatement()
        sb.putStatement("__ec_pool = ${loop.parent}")
        sb.endStatement()
    }

    /**
     * The pool that was current when the function was called, as C, inside the loops [loops] (outermost first): the pool around the
     * outermost one whose rounds get a pool, or else [otherwise]. Decided when the C is written.
     */
    class IREntryPool(location: Loc, private val loops: List<Loop>, private val otherwise: String) : IR(location) {
        override fun toC(sb: CSourceBuilder, c: Context) { sb.put(name()) }

        fun name(): String = loops.firstOrNull { it.enabled }?.parent ?: otherwise

        override fun toString() = "entryPool"
    }
}
