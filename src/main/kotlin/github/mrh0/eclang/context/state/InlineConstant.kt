package github.mrh0.eclang.context.state

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

class InlineConstant(private val name: String, private val type: Type, private val ir: IIR) : IVar {
    override fun kind() = "a parameter"
    override fun toString() = "$name: $type"
    override fun getName() = name
    override fun getType() = type
    override fun toIR(location: Loc, cd: CompileData, hint: Type) = ir
}