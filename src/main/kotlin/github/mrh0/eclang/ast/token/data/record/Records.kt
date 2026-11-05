package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.type.checkBits
import github.mrh0.eclang.context.ArgumentOrder
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.record.IRCreateRecord
import github.mrh0.ect2.Record

/**
 * What a record literal holds, for Lifetimes: [given] are the fields it gives, [allocates] whether a default that may
 * point to memory was used (it may allocate in the current pool), and [onStack] whether a default is an array on the stack.
 */
class RecordDefaults(val given: Map<String, ITok> = mapOf(), val allocates: Boolean = false, val onStack: Boolean = false)

/**
 * Builds a value of [record] from [values], given in field order, or by name when [names] is set
 * (`{name = "Jeff", age = 3}`). Every field must be given, including inherited ones, except fields with a default
 * value (`age: Int = 0`): left out by name, or at the end in field order, they get it. Returns what Lifetimes needs with the IR.
 */
internal fun createRecord(cd: CompileData, location: Loc, record: Record, values: List<ITok>, names: List<String>?): Pair<RecordDefaults, IIR> {
    if (!record.isDefined || (record.isExternal && record.fields.isEmpty()))
        throw EcError(location, "${record.name} is declared without fields, so it can't be created here (it is created by C code).")
    val fields = record.fields
    val describe = fields.entries.joinToString { "${it.key}: ${it.value}" }
    if (record.isUnion && values.size != 1)
        throw EcError(location, "The fields of ${record.name} share their memory, so a literal gives exactly one of them (${values.size} given): `${record.name} { ${fields.keys.first()} = ... }`.")
    val given: List<String> = if (record.isUnion) {
        names?.get(0)?.also { if (it !in fields) throw EcError(location, "${record.name} has no field '$it'.${CompileData.suggestion(it, fields.keys)}") }.let { listOf(it ?: fields.keys.first()) }
    } else if (names == null) {
        val left = fields.keys.drop(values.size)
        if (values.size > fields.size || left.any { defaultOf(cd, record, it) == null })
            throw EcError(location, "${record.name} has ${fields.size} field(s) ($describe), but ${values.size} value(s) were given." +
                if (values.size < fields.size) " Only fields with a default value (`name: Type = value`) can be left out." else "")
        fields.keys.take(values.size)
    } else {
        names.forEach { if (it !in fields) throw EcError(location, "${record.name} has no field '$it'.${CompileData.suggestion(it, fields.keys)}") }
        names.groupBy { it }.filter { it.value.size > 1 }.keys.firstOrNull()?.let { throw EcError(location, "Field '$it' is given twice.") }
        val missing = (fields.keys - names.toSet()).filter { defaultOf(cd, record, it) == null }
        if (missing.isNotEmpty()) throw EcError(location, "Missing field(s) of ${record.name}: ${missing.joinToString { "$it: ${fields[it]}" }}. " +
            "Give them a value, or give the field a default in the record (`${missing[0]}: ${fields[missing[0]]} = ...`).")
        names
    }
    val order = ArgumentOrder(cd)
    given.forEachIndexed { i, name ->
        val type = fields[name]!!
        order.compile(i, values[i]) { values[i].process(cd, type).also { checkBits(location, record, name, type, it.first) } }
    }
    val defaulted = if (record.isUnion) listOf() else fields.keys.filter { it !in given }
    // A default is compiled where the record is made, as if in a function of its own: it can't see local variables,
    // and it sees the names of the module that declares it, not those of the code making the record.
    defaulted.forEachIndexed { i, name ->
        val type = fields[name]!!
        val default = defaultOf(cd, record, name)!!
        order.compile(given.size + i, default) {
            cd.inModule(cd.moduleNamed(defaultOwner(cd, record, name).namespace)) {
                cd.isolated {
                    cd.newContext("${record.name} default", record.typeBindings)
                    val value = default.process(cd, type)
                    checkBits(location, record, name, type, value.first)
                    type to cd.expect(location, value, type, "the default of field '$name' of ${record.name}")
                }
            }
        }
    }
    val finished = order.finish(location)
    val irs = given.indices.map { i -> cd.expect(location, finished.getValue(i), fields[given[i]]!!, "field '${given[i]}' of ${record.name}") }
    val defaults = defaulted.indices.map { finished.getValue(given.size + it).second }
    val allocates = defaulted.any { Lifetimes.holdsPointer(fields[it]!!) }
    val onStack = defaulted.any { Lifetimes.usesStack(defaultOf(cd, record, it)!!) }
    return RecordDefaults(given.zip(values).toMap(), allocates, onStack) to order.wrap(location, IRCreateRecord(location, record, given + defaulted, irs + defaults))
}

/** The record that gives [field] of [record] its default value: [record], or a record it extends. */
private fun defaultOwner(cd: CompileData, record: Record, field: String): Record {
    var r: Record = record
    while (cd.recordDefaults[r]?.containsKey(field) != true) r = r.parent ?: return record
    return r
}

/** The default value of [field] of [record] (or of the record it extends), or null. */
private fun defaultOf(cd: CompileData, record: Record, field: String): ITok? {
    var r: Record? = record
    while (r != null) {
        cd.recordDefaults[r]?.get(field)?.let { return it }
        r = r.parent
    }
    return null
}
