package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.output.c.CSourceBuilder

/**
 * A global variable. C only allows constant initializers for globals, so `val a = f()` is declared without a
 * value and set at the start of the program instead ([toCInit], called from the C `main`), in declaration order.
 */
open class IRGlobalDefine(location: Loc, private val ivar: IVar, private val expr: IIR, private val volatile: Boolean = false) : IR(location) {
    private val initializedInMain = !expr.isConstant()

    /** The C name, and whether it can be left out of the C file when nothing refers to it (a constant value has no effects). */
    val cName get() = ivar.cName()
    val removable get() = !initializedInMain

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("static ${if (volatile) "volatile " else ""}${sb.types.cName(ivar.getType())} ${ivar.cName()}")
        if (!initializedInMain) {
            sb.put(" = ")
            expr.toC(sb, c)
        }
        sb.endStatement()
    }

    /** The statement that sets this global at the start of `main`, if its value isn't a C constant. */
    fun toCInit(sb: CSourceBuilder, c: Context) {
        if (!initializedInMain) return
        sb.putStatement("\t${ivar.cName()} = ")
        expr.toC(sb, c)
        sb.endStatement()
    }

    override fun toString(): String {
        return "IRGlobalDefine(${ivar.getName()}, $expr)"
    }
}
