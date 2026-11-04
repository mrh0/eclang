package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/**
 * One compiled function. [returnType] is the C return type (a result struct for functions that `throw`).
 * [endsWithSuccess]: a `None throws E` function that reaches its end returns "no error". [cVarArgs]: it ends
 * with C's `...` (a typed `...rest: T` is an ordinary array parameter in [params]).
 */
class IRFunctionOverride(
    location: Loc, private val block: IRBlock, val id: String, val params: IRParameters,
    val returnType: Type, private val endsWithSuccess: Boolean, private val cVarArgs: Boolean,
) : IR(location) {

    private fun signature(sb: CSourceBuilder, c: Context) {
        sb.putStatement("${sb.types.cName(returnType)} $id(")
        params.toC(sb, c)
        if (cVarArgs) sb.put(if (params.args.isEmpty()) "..." else ", ...")
        if (params.args.isEmpty() && !cVarArgs) sb.put("void")
        sb.put(")")
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        signature(sb, c)
        sb.put(" ")
        sb.pushScope(BlockScope())
        val body = sb.render { out ->
            block.toC(out, c)
            if (endsWithSuccess && !block.diverges) {
                out.putStatement("return ((${out.types.cName(returnType)}){.code = 0})")
                out.endStatement()
            }
        }
        // A parameter the body doesn't use (often in a copy made for one member of a union parameter, where the
        // branches for the other members were left out) is marked as used, so the C has no -Wunused-parameter warning.
        val code = body.replace(LITERALS, "")
        params.args.filter { !Regex("\\b${Regex.escape(it.name)}\\b").containsMatchIn(code) }.forEach {
            sb.putStatement("(void)${it.name}")
            sb.endStatement()
        }
        sb.put(body)
        sb.popScope()
    }

    fun toCDeclaration(sb: CSourceBuilder, c: Context) {
        signature(sb, c)
        sb.endStatement()
    }

    override fun toString(): String {
        return "IRFunctionOverride($id, $params, $returnType)"
    }

    companion object {
        /** C string and character literals, which may contain a parameter's name without using it. */
        private val LITERALS = Regex(""""(?:\\.|[^"\\])*"|'(?:\\.|[^'\\])*'""")
    }
}
