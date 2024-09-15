package github.mrh0.eclang.context.state

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRNamedConstant
import github.mrh0.eclang.types.EcType

class DeclaredConstant(private val name: String, private val externalName: String, private val type: EcType) : IVar {
    override fun toString() = "$name: $type"
    override fun getName() = name
    override fun getType() = type
    override fun toIR(location: Loc, cd: CompileData, hint: EcType): IIR = IRNamedConstant(location, externalName)
}