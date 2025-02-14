package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.types.internal.EcTypeVarArgC

class IRFunctionOverride(location: Loc, private val block: IRBlock, val id: String, val params: IRParameters, val returnType: EcType, val throws: EcType?, val varArg: EcType?) : IR(location) {
    override fun toString(): String {
        return "fn($id, $params, $returnType, $block)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        if (!GlobalFunctions.calledFunctionOverrides.containsKey(id)) return // TODO: do this better
        sb.putStatement()
        IRType(location, returnType).toC(sb, c)
        sb.put(' ')
        sb.put(id)
        sb.put('(')
        params.toC(sb, c)
        if (varArg != null) {
            if (varArg is EcTypeVarArgC) sb.put(", ...")
            else sb.put(", int __ec_va_count, ...")
        }
        sb.put(") ")
        sb.pushScope(BlockScope())
        block.toC(sb, c)
        sb.popScope()
    }

    fun toCDeclaration(sb: CSourceBuilder, c: Context) {
        if (!GlobalFunctions.calledFunctionOverrides.containsKey(id)) return // TODO: do this better
        sb.putStatement()
        IRType(location, returnType).toC(sb, c)
        sb.put(' ')
        sb.put(id)
        sb.put('(')
        params.toC(sb, c)
        if (varArg != null) {
            if (varArg is EcTypeVarArgC) sb.put(", ...")
            else sb.put(", int __ec_va_count, ...")
        }
        sb.put(')')
        sb.endStatement()
    }
}