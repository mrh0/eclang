package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

/**
 * `pool do` (`pool name do`): a C block that makes a child of the current pool current, runs [body], then runs
 * [leave] (the block's deferred statements, ending with [IRPoolLeave]). [parent] holds the pool to restore, and
 * [name] is the C variable of a named pool.
 *
 * ```c
 * {
 *     apr_pool_t* __ec_parent0 = __ec_pool;
 *     apr_pool_t* scratch = __ec_pool = __ec_pool_create(__ec_parent0);
 *     ...
 *     apr_pool_destroy(scratch);
 *     __ec_pool = __ec_parent0;
 * }
 * ```
 */
class IRPoolBlock(location: Loc, private val parent: String, private val name: String?, private val body: IIR, private val leave: List<IIR>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement()
        sb.pushScope(BlockScope())
        sb.putStatement("apr_pool_t* $parent = __ec_pool")
        sb.endStatement()
        sb.putStatement("${if (name != null) "apr_pool_t* $name = " else ""}__ec_pool = __ec_pool_create($parent)")
        sb.endStatement()
        body.toC(sb, c)
        leave.forEach { it.toC(sb, c) }
        sb.popScope()
    }

    override fun toString() = "pool($name, $body)"
}

/** Leaving a `pool do` block (at its end, or on `ret`, `break`, ...): frees its pool [pool] and makes [parent] current again. */
class IRPoolLeave(location: Loc, private val parent: String, private val pool: String) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        IRBlock.lineOf(sb, this) // what the pool's memory was freed by, under `--check-memory`: the `pool do` line
        sb.putStatement("apr_pool_destroy($pool)")
        sb.endStatement()
        sb.putStatement("__ec_pool = $parent")
        sb.endStatement()
    }

    override fun toString() = "leavePool($pool)"
}
