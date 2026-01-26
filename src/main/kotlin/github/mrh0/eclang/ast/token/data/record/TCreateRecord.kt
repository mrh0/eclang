package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUnableToInferTypeError
import github.mrh0.eclang.error.EcUnexpectedTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRBool
import github.mrh0.eclang.ir.data.record.IRCreateRecord
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.EcTypeRecord

class TCreateRecord(location: Loc, private val values: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "TCreateRecord($values)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        if (hint !is EcTypeRecord) throw EcUnableToInferTypeError(location)
        val irs = values.mapIndexed { index, it -> it.process(cd, hint.props[index].second) }
        irs.forEachIndexed { index, it -> if(!hint.props[index].second.accepts(location, it.first)) throw EcUnexpectedTypeError(location, hint.props[index].second, it.first) }
        return hint to IRCreateRecord(location, hint.name, irs.map { it.second })
    }
}