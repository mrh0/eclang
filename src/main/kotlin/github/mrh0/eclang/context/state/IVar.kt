package github.mrh0.eclang.context.state

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType

interface IVar {
    fun getName(): String
    fun getType(): EcType
    fun canAssign(): Boolean = false
    fun toIR(location: Loc, cd: CompileData, hint: EcType): IIR
}