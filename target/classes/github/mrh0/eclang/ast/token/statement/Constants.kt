package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRConstantRef
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.ir.IRExprNest
import github.mrh0.eclang.ir.data.IRValue
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * The type and value of `const name = expr` or `const name: T = expr`. The value must be known at compile time:
 * a literal, another constant, or arithmetic on them. Without a type, a number keeps its literal type.
 */
internal fun constantValue(cd: CompileData, location: Loc, name: String, expr: ITok, typeTok: ITok?): Pair<Type, IIR> {
    val what = "constant '$name'"
    val declared = typeTok?.let { cd.storable(location, it.process(cd, Types.None).first, what) }
    val value = cd.usable(location, expr.process(cd, declared ?: Types.None), what)
    if (value.first === Types.Null) throw EcError(location, "A constant can't be null. Use `val $name: T? = null` instead.")
    val type = declared ?: value.first.also { cd.storable(location, it, what) }
    val ir = cd.expect(location, value, type, what)
    if (!isCompileTime(ir))
        throw EcError(location, "The value of $what must be known at compile time: a literal, another constant, or arithmetic on them. For a value computed when the program runs, use `val`.")
    return type to ir
}

private fun isCompileTime(ir: IIR): Boolean = when (ir) {
    is IRValue, is IRConstantRef -> true
    is IRConvert -> ir.mode == IRConvert.Mode.IMPLICIT && isCompileTime(ir.expr)
    is IRExprNest -> isCompileTime(ir.expr)
    else -> false
}
