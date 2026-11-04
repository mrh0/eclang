package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.state.IVar

/** A global `val`: C-wise the same as a global variable (eclang checks that it isn't assigned). */
class IRGlobalDefineConst(location: Loc, private val ivar: IVar, expr: IIR) : IRGlobalDefine(location, ivar, expr) {
    override fun toString(): String {
        return "IRGlobalDefineConst(${ivar.getName()})"
    }
}
