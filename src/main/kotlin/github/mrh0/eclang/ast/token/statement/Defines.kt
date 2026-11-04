package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * The type and converted value of a variable definition: `val x = expr` takes the expression's type (literals
 * become Int/Double), `val x: T = expr` checks the value fits T.
 */
internal fun defineValue(cd: CompileData, location: Loc, name: String, expr: ITok, typeTok: ITok?): Pair<Type, IIR> {
    val what = "variable '$name'"
    val declared = typeTok?.let { cd.storable(location, it.process(cd, Types.None).first, what) }
    val value = expr.process(cd, declared ?: Types.None)
    val type = declared ?: cd.storable(location, value.first, what)
    return type to cd.expect(location, value, type, what)
}
