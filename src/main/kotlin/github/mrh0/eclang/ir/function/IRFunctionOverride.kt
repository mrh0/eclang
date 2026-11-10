package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.FunctionType
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * One compiled function. [returnType] is the C return type (a result struct for functions that `throw`).
 * [endsWithSuccess]: a `None throws E` function that reaches its end returns "no error". [cVarArgs]: it ends
 * with C's `...` (a typed `...rest: T` is an ordinary array parameter in [params]). [exported]: an `export fn`. In a
 * library C calls it from any thread, which may not have a current pool yet; the other functions are `static` there.
 * [closure]: the function is what a function value calls (a lambda's, or one made for `fn name`), see [ClosureC].
 */
class IRFunctionOverride(
    location: Loc, private val block: IRBlock, val id: String, val params: IRParameters,
    val returnType: Type, private val endsWithSuccess: Boolean, private val cVarArgs: Boolean, private val exported: Boolean = false,
    private val closure: ClosureC? = null,
) : IR(location) {
    /**
     * What makes the C function [id] a function value of [type]. Its first parameter is the closure (`void* __ec_self`).
     * Without [fields], the closure is a static object, `static __ec_closure_M __ec_value_id = { id };`. With them (the
     * captured variables, by C name and type), each value is allocated in the current pool by `__ec_new_id(...)`, as a
     * `__ec_env_id`, which the function reads as `__ec_env`.
     */
    class ClosureC(val type: FunctionType, val fields: List<Pair<String, Type>>)

    private fun signature(sb: CSourceBuilder, c: Context) {
        sb.putStatement("${if (sb.types.isHidden(exported)) "static " else ""}${if (returnType === Types.Never) "_Noreturn " else ""}${sb.types.cName(returnType)} $id(")
        params.toC(sb, c)
        if (cVarArgs) sb.put(if (params.args.isEmpty()) "..." else ", ...")
        if (params.args.isEmpty() && !cVarArgs) sb.put("void")
        sb.put(")")
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        signature(sb, c)
        sb.put(" ")
        sb.pushScope(BlockScope())
        if (exported && sb.types.isLibrary) {
            sb.putStatement("__ec_enter()")
            sb.endStatement()
        }
        val body = sb.render { out ->
            if (closure != null && closure.fields.isNotEmpty()) {
                out.putStatement("${envName(id)}* __ec_env = __ec_self")
                out.endStatement()
            }
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
        if (closure == null) return
        val base = sb.types.closureStruct(closure.type)
        if (closure.fields.isEmpty()) {
            sb.putLine("static $base ${valueName(id)} = { $id };")
            return
        }
        val fields = closure.fields.joinToString("") { (name, type) -> " ${sb.types.cName(type)} $name;" }
        sb.putLine("typedef struct { $base __ec_base;$fields } ${envName(id)};")
        val params = closure.fields.joinToString(", ") { (name, type) -> "${sb.types.cName(type)} $name" }
        val sets = closure.fields.joinToString("") { (name, _) -> "\t__ec_e->$name = $name;\n" }
        sb.putLine("static ${sb.types.cName(closure.type)} ${newName(id)}($params) {\n\t${envName(id)}* __ec_e = __ec_palloc(__ec_pool, sizeof *__ec_e);\n" +
            "\t__ec_e->__ec_base.call = $id;\n$sets\treturn &__ec_e->__ec_base;\n}")
    }

    override fun toString(): String {
        return "IRFunctionOverride($id, $params, $returnType)"
    }

    companion object {
        /** The static closure of the C function [id], when it captures nothing. */
        fun valueName(id: String) = "__ec_value_${id.removePrefix("__ec_")}"
        /** The struct of the closures of the lambda [id], with what it captured. */
        fun envName(id: String) = "__ec_env_$id"
        /** Allocates a closure of the lambda [id], given what it captures. */
        fun newName(id: String) = "__ec_new_$id"

        /** C string and character literals, which may contain a parameter's name without using it. */
        private val LITERALS = Regex(""""(?:\\.|[^"\\])*"|'(?:\\.|[^'\\])*'""")
    }
}
