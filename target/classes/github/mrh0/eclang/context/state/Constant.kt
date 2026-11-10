package github.mrh0.eclang.context.state

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.ir.IRNamedConstant
import github.mrh0.ect2.Type

/** A name that can't be assigned: a `val`, or a parameter, loop variable or caught error ([kind]). */
class Constant(private val name: String, private val type: Type, private val cName: String = name, private val kind: String = "a `val`") : IVar {
    override fun kind() = kind
    override fun toString() = "$name: $type"
    override fun getName() = name
    override fun getType() = type
    override fun cName() = cName
    override fun toIR(location: Loc, cd: CompileData, hint: Type): IIR = IRNamedConstant(location, cName)
}