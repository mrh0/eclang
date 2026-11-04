package github.mrh0.eclang.context.state

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.ect2.Type

/**
 * A variable whose type flow analysis has narrowed, e.g. `x` inside `if x != null do` is `CString`, not
 * `CString?`. Reading it converts the stored value (of [base]'s type) to the narrower [type]; assigning to it
 * goes to [base] and ends the narrowing.
 */
class NarrowedVar(val base: IVar, private val type: Type) : IVar {
    override fun toString() = "${base.getName()}: $type (narrowed from ${base.getType()})"
    override fun getName() = base.getName()
    override fun getType() = type
    override fun canAssign() = base.canAssign()
    override fun kind() = base.kind()
    override fun toIR(location: Loc, cd: CompileData, hint: Type): IIR =
        IRConvert(location, base.toIR(location, cd, hint), base.getType(), type, IRConvert.Mode.NARROW)
}
