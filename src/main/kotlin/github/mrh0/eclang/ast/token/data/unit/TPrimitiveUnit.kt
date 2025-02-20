package github.mrh0.eclang.ast.token.data.unit

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.atom.AtomInstance
import github.mrh0.eclang.error.EcUnknownUnitTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRAtom
import github.mrh0.eclang.ir.data.IRValue
import github.mrh0.eclang.types.EcTypeAtomInstance
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.EcTypeDouble
import github.mrh0.eclang.types.numbers.EcTypeFloat
import github.mrh0.eclang.types.numbers.signed.EcTypeChar
import github.mrh0.eclang.types.numbers.signed.EcTypeInt
import github.mrh0.eclang.types.numbers.signed.EcTypeLong
import github.mrh0.eclang.types.numbers.signed.EcTypeShort
import github.mrh0.eclang.types.numbers.unsigned.*

class TPrimitiveUnit(location: Loc, private val expr: ITok, private val name: String) : Tok(location) {
    override fun toString(): String {
        return "TPrimitiveUnit($expr, $name)"
    }

    private fun simpleCast(from: Pair<EcType, IIR>, toType: EcType): Pair<EcType, IIR> {
        val casted = from.first.cast(location, toType)
        return casted to from.second
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        testIdentifier(location, name)
        val irPair = expr.process(cd, hint)
        return when(name) {
            "i", "i32" -> simpleCast(irPair, EcTypeInt)
            "u", "ui", "u32" -> simpleCast(irPair, EcTypeUInt)
            "l", "i64" -> simpleCast(irPair, EcTypeLong)
            "ul", "u64" -> simpleCast(irPair, EcTypeULong)
            "s", "i16" -> simpleCast(irPair, EcTypeShort)
            "us", "u16" -> simpleCast(irPair, EcTypeUShort)
            "b", "i8" -> simpleCast(irPair, EcTypeChar)
            "ub", "u8" -> simpleCast(irPair, EcTypeUChar)
            "f", "f32" -> simpleCast(irPair, EcTypeFloat)
            "d", "f64" -> simpleCast(irPair, EcTypeDouble)
            else -> throw EcUnknownUnitTypeError(location, name, irPair.first)
        }
    }
}