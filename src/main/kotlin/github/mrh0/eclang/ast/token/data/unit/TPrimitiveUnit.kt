package github.mrh0.eclang.ast.token.data.unit

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUnknownUnitTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.FloatLiteral
import github.mrh0.ect2.IntLiteral
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** A number with a type suffix: `10b` is a Char, `10ul` a ULong, `6f` a Float. The value must fit. */
class TPrimitiveUnit(location: Loc, private val expr: ITok, private val name: String) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        testIdentifier(location, name)
        val (literal, _) = expr.process(cd, Types.None)
        val to = when (name) {
            "i", "i32" -> Types.Int
            "u", "ui", "u32" -> Types.UInt
            "l", "i64" -> Types.Long
            "ul", "u64" -> Types.ULong
            "s", "i16" -> Types.Short
            "us", "u16" -> Types.UShort
            "b", "i8" -> Types.Char
            "ub", "u8" -> Types.UChar
            "f", "f32" -> Types.Float
            "d", "f64" -> Types.Double
            "z" -> Types.Size
            else -> throw EcUnknownUnitTypeError(location, name, literal)
        }
        if (!cd.types.accepts(to, literal)) throw EcError(location, "The literal $literal can't be a $to: ${cd.types.explain(literal, to)}.")
        val value: Number = when (literal) {
            is IntLiteral -> if (to === Types.Float || to === Types.Double) literal.value.toDouble() else literal.value
            is FloatLiteral -> literal.value
            else -> throw EcUnknownUnitTypeError(location, name, literal)
        }
        return to to IRNumber(location, value, to)
    }

    override fun toString(): String {
        return "TPrimitiveUnit($expr, $name)"
    }
}
