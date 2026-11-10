package github.mrh0.eclang.context.state

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.ect2.Type

class Variable(private val name: String, private val type: Type, private val cName: String = name) : IVar {
    override fun toString() = "$name: $type"
    override fun getName() = name
    override fun getType() = type
    override fun canAssign(): Boolean = true
    override fun kind() = "a `var`"
    override fun cName() = cName
    override fun toIR(location: Loc, cd: CompileData, hint: Type): IIR = IRNamed(location, cName)
}