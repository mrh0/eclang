package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.record.IRCreateRecord
import github.mrh0.ect2.Record

/**
 * Builds a value of [record] from [values], given in field order, or by name when [names] is set
 * (`{name = "Jeff", age = 3}`). Every field must be given, including inherited ones.
 */
internal fun createRecord(cd: CompileData, location: Loc, record: Record, values: List<ITok>, names: List<String>?): IIR {
    if (!record.isDefined || (record.isExternal && record.fields.isEmpty()))
        throw EcError(location, "${record.name} is declared without fields, so it can't be created here (it is created by C code).")
    val fields = record.fields
    val describe = fields.entries.joinToString { "${it.key}: ${it.value}" }
    val order: List<String> = if (names == null) {
        if (values.size != fields.size) throw EcError(location, "${record.name} has ${fields.size} field(s) ($describe), but ${values.size} value(s) were given.")
        fields.keys.toList()
    } else {
        names.forEach { if (it !in fields) throw EcError(location, "${record.name} has no field '$it'.${CompileData.suggestion(it, fields.keys)}") }
        names.groupBy { it }.filter { it.value.size > 1 }.keys.firstOrNull()?.let { throw EcError(location, "Field '$it' is given twice.") }
        val missing = fields.keys - names.toSet()
        if (missing.isNotEmpty()) throw EcError(location, "Missing field(s) of ${record.name}: ${missing.joinToString { "$it: ${fields[it]}" }}.")
        names
    }
    val irs = order.mapIndexed { i, name ->
        val type = fields[name]!!
        cd.expect(location, values[i].process(cd, type), type, "field '$name' of ${record.name}")
    }
    return IRCreateRecord(location, record, order, irs)
}
