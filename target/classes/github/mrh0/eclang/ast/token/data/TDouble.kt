package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRDouble
import github.mrh0.eclang.ir.data.IRFloat
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.EcTypeFloat

class TDouble(location: Loc, private val value: Double) : Tok(location) {
    override fun toString(): String {
        return "${value}d"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeFloat to IRDouble(location, value)
    }
}