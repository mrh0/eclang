package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.output.c.CSourceBuilder

/**
 * A global variable. C only allows constant initializers for globals, so `val a = f()` is declared without a
 * value and set at the start of the program instead ([toCInit], called from the C `main`), in the order of
 * [github.mrh0.eclang.context.function.Functions.initializationOrder].
 */
open class IRGlobalDefine(location: Loc, private val ivar: IVar, private val expr: IIR, private val volatile: Boolean = false) : IR(location) {
    private val initializedInMain = !expr.isConstant()

    /** The temporaries [expr] uses, declared in the C `main` next to it. */
    var temps: List<IIR> = listOf()

    /** The C name, and whether it can be left out of the C file when nothing refers to it (a constant value has no effects). */
    val cName get() = ivar.cName()
    val name get() = ivar.getName()
    val removable get() = !initializedInMain

    /** Whether this is set at the start of the program, so that code reading it earlier sees no value. */
    val setAtStart get() = initializedInMain

    override fun toC(sb: CSourceBuilder, c: Context) {
        // `T volatile x`: after the type, so that a pointer variable is volatile itself, not what it points at.
        sb.putStatement("static ${sb.types.cName(ivar.getType())}${if (volatile) " volatile" else ""} ${ivar.cName()}")
        if (!initializedInMain) {
            sb.put(" = ")
            expr.toC(sb, c)
        }
        sb.endStatement()
    }

    /** The statement that sets this global at the start of `main`, if its value isn't a C constant. */
    fun toCInit(sb: CSourceBuilder, c: Context) {
        if (!initializedInMain) return
        temps.forEach { it.toC(sb, c) }
        sb.putStatement("\t${ivar.cName()} = ")
        expr.toC(sb, c)
        sb.endStatement()
    }

    override fun toString(): String {
        return "IRGlobalDefine(${ivar.getName()}, $expr)"
    }
}
