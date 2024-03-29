package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.record.IRRecord
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TRecord(location: Loc, val name: String, val names: List<String>, val types: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "TRecord(${names.mapIndexed { index, it -> it to types[index] }})"
    }

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        return EcTypeNone to IRRecord(location, name, names.mapIndexed { index, it -> it to types[index].process(cd).second as IRType })
    }
}