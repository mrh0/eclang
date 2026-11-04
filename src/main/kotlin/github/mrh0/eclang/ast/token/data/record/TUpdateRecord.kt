package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.accessor.readField
import github.mrh0.eclang.ast.token.type.checkBits
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRComma
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempSet
import github.mrh0.eclang.ir.data.record.IRCreateRecord
import github.mrh0.ect2.Pointer
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `Point { ...p, x = 1 }`: a copy of the record p with some fields changed. Without a name, `{ ...p, x = 1 }` is the same
 * type as p. A record extending the named one fits too, and gives the fields they share. p is evaluated once.
 */
class TUpdateRecord(location: Loc, private val named: TCreateRecordTyped?, val base: ITok, val values: List<ITok>, private val names: List<String>) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (baseType, baseIR) = cd.usable(location, base.process(cd, hint), "`...`")
        if (baseType is Pointer && baseType.target is Record)
            throw EcError(location, "`...` copies the fields of a record, but this is a pointer to one ($baseType). Read the record first: `...@p`.")
        val record = named?.record(cd, baseType) ?: baseType as? Record
            ?: throw EcError(location, "`...` copies the fields of a record, but this is a $baseType.")
        if (baseType !is Record || !cd.types.accepts(record, baseType))
            throw EcError(location, "`...` must be a ${record.name} to make a ${record.name} from it, but this is a $baseType. ${cd.types.explain(baseType, record)}.")
        if (record.isUnion) throw EcError(location, "`...` copies the fields of a record one by one, but the fields of union ${record.name} share their memory. Make a new one (`${record.name} { field = value }`), or copy it and assign the field.")
        val fields = record.fields
        names.forEach { if (it !in fields) throw EcError(location, "${record.name} has no field '$it'.${CompileData.suggestion(it, fields.keys)}") }
        names.groupBy { it }.filter { it.value.size > 1 }.keys.firstOrNull()?.let { throw EcError(location, "Field '$it' is given twice.") }

        val given = names.mapIndexed { i, name ->
            val type = fields[name]!!
            val value = values[i].process(cd, type)
            checkBits(location, record, name, type, value.first)
            cd.expect(location, value, type, "field '$name' of ${record.name}")
        }
        // Each other field is read from the value, so anything but a variable goes into a temporary.
        val simple = base.unnested() is TNamed
        val temp = if (simple) null else cd.declareTemp(location) { baseType }
        val read = temp?.let { IRRaw(location, it) } ?: baseIR
        val kept = fields.keys.filter { it !in names }
        val copy = IRCreateRecord(location, record, names + kept, given + kept.map { readField(location, read, baseType, it, fields[it]!!, false) })
        return record to if (temp == null) copy else IRComma(location, IRTempSet(location, temp, baseIR), copy)
    }

    override fun toString() = "TUpdateRecord($base, $names)"
}
