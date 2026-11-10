package github.mrh0.eclang.context.state

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

interface IVar {
    fun getName(): String
    fun getType(): Type
    fun canAssign(): Boolean = false
    /** What the name is, for "'x' is a `val`" in errors. */
    fun kind(): String = "a constant"
    /** The name in the generated C. */
    fun cName(): String = getName()
    fun toIR(location: Loc, cd: CompileData, hint: Type): IIR
}