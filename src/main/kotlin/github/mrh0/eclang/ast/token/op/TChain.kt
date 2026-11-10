package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempSet
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * The first use of the middle of a chained comparison, `a < f() < c`: `a < (tmp = f()) && tmp < c`, so f() runs once, and
 * only when the comparisons before it hold. A middle that is a variable or a literal is simply read twice instead.
 */
class TChainFirst(location: Loc, private val expr: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(expr)

    private lateinit var value: Pair<Type, String>

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val what = "a chained comparison"
        val v = cd.usable(location, expr.process(cd, Types.None), what)
        val type = cd.storable(location, v.first, what)
        val temp = cd.declareTemp(location) { type }
        value = type to temp
        return type to IRTempSet(location, temp, cd.expect(location, v, type, what))
    }

    /** The second use of the value, compiled after this token. */
    inner class Again(location: Loc) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(expr)

        override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = value.first to IRRaw(location, value.second)
    }

    override fun toString() = "TChainFirst($expr)"
}
