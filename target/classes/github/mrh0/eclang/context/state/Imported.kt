package github.mrh0.eclang.context.state

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType

class Imported(private val name: String, private val type: EcType) : IVar {
    override fun getName() = name
    override fun getType() = type
    override fun toIR(location: Loc, cd: CompileData, hint: EcType): IIR = TODO("Not yet implemented")
}