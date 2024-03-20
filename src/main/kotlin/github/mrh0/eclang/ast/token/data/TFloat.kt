package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRFloat
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.numbers.EcTypeFloat

class TFloat(location: Loc, private val value: Double) : Tok(location) {
    override fun toString(): String {
        return "${value}f"
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        return EcTypeFloat to IRFloat(location, value)
    }
}