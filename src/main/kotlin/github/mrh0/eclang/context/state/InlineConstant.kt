package github.mrh0.eclang.context.state

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType

class InlineConstant(private val name: String, private val type: EcType, private val ir: IIR) : IVar {
    override fun toString() = "$name: $type"
    override fun getName() = name
    override fun getType() = type
    override fun toIR(location: Loc, cd: CompileData, hint: EcType) = ir
}