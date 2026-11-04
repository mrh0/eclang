package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.token.function.call.Builtins
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.util.FormatSpec
import github.mrh0.eclang.util.StringLiteral
import github.mrh0.ect2.Type

/**
 * A String literal with `${expr}` in it. [parts] are [StringLiteral] text and [Field]s, in order.
 * The String is built in the current pool, like `format("...")` (see Builtins).
 */
class TInterpolated(location: Loc, val parts: List<Any>) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = Builtins.interpolate(cd, location, Builtins.current(location), this)

    /** `${value}`, or `${value:spec}`. */
    class Field(val value: ITok, val spec: FormatSpec?) {
        override fun toString() = value.toString()
    }

    override fun toString() = "TInterpolated(${parts.joinToString(", ") { if (it is StringLiteral) "'$it'" else it.toString() }})"
}
