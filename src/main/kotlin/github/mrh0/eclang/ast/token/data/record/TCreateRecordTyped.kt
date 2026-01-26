package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUnknownTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRBool
import github.mrh0.eclang.ir.data.record.IRCreateRecord
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.EcTypeRecord

class TCreateRecordTyped(location: Loc, private val typeName: String, private val values: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "TCreateRecord($values)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val recordType = BuiltInTypes.getType(typeName)
        if (recordType !is EcTypeRecord) throw EcUnknownTypeError(location, typeName)
        val irs = values.mapIndexed { index, it -> it.process(cd, recordType.props[index].second).second }
        return hint to IRCreateRecord(location, recordType.name, irs)
    }
}