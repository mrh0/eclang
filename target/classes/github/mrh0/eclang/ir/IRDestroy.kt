package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** `destroy p`: `apr_pool_destroy(p);`, or with [nullable] (a `Pool?`, read twice) `if (p != NULL) apr_pool_destroy(p);`. */
class IRDestroy(location: Loc, private val pool: IIR, private val nullable: Boolean) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement()
        if (nullable) {
            sb.put("if (")
            pool.toC(sb, c)
            sb.put(" != NULL) ")
        }
        sb.put("apr_pool_destroy(")
        pool.toC(sb, c)
        sb.put(")")
        sb.endStatement()
    }

    override fun toString() = "destroy($pool)"
}
