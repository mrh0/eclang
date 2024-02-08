package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRTuple
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeTuple

class TTuple(location: Loc, private val values: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return values.joinToString("&", "TTuple(", ")")
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        val pairs = values.map { it.process(cd) }
        return EcTypeTuple(pairs.map { it.first }.toTypedArray()) to IRTuple(location, pairs.map { it.second })
    }
}