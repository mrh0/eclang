package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type

/** `Point { 1, 2 }` or `Point { x = 1, y = 2 }`. */
class TCreateRecordTyped(location: Loc, private val typeName: String, val values: List<ITok>, private val names: List<String>? = null) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val record = cd.lookupType(location, typeName) as? Record ?: throw EcError(location, "$typeName is not a record.")
        return record to createRecord(cd, location, record, values, names)
    }

    override fun toString(): String {
        return "TCreateRecordTyped($typeName, $values)"
    }
}
