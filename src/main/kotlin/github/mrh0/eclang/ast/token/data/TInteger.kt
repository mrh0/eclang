package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRValue
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.numbers.EcTypeInt
import github.mrh0.eclang.values.numbers.GsInt

class TInteger(location: Loc, private val value: Int) : Tok(location) {
    override fun toString(): String {
        return "${value}i"
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        return EcTypeInt to IRValue(location, GsInt(value))
    }
}