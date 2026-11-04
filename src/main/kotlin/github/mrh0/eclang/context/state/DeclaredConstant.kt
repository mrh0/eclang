package github.mrh0.eclang.context.state

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRNamedConstant
import github.mrh0.ect2.Type

/** A value defined in C (`declare val`), or a C variable when [assignable] (`declare var`). */
class DeclaredConstant(private val name: String, private val externalName: String, private val type: Type, private val assignable: Boolean = false) : IVar {
    override fun kind() = "a `declare val`"
    override fun canAssign() = assignable
    override fun cName() = externalName
    override fun toString() = "$name: $type"
    override fun getName() = name
    override fun getType() = type
    override fun toIR(location: Loc, cd: CompileData, hint: Type): IIR = IRNamedConstant(location, externalName)
}