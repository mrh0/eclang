package github.mrh0.eclang.ir.data.record

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.data.IRCreateFixed
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.ect2.Record

/**
 * `(Name){.field = value, ...}`; records that take part in `extends` also get their type tag. C can't initialise an array
 * field from a value, only from a list of elements, so an array that isn't a literal is set by a helper afterwards.
 */
class IRCreateRecord(location: Loc, val record: Record, val names: List<String>, val values: List<IIR>) : IR(location) {
    private fun setLater(i: Int) = CTypes.fixedOf(record.fields[names[i]]!!) != null && values[i] !is IRCreateFixed

    override fun isConstant() = values.all { it.isConstant() } && names.indices.none { setLater(it) }

    override fun toC(sb: CSourceBuilder, c: Context) {
        val later = names.indices.filter { setLater(it) }
        later.reversed().forEach { sb.put("${sb.types.setFixed(record, names[it], record.fields[names[it]]!!)}(") }
        sb.put("((${sb.types.cName(record)}){")
        val now = names.indices - later.toSet()
        if (record.inHierarchy) sb.put(".__ec_tid = ${record.id}${if (now.isEmpty()) "" else ", "}")
        now.forEachIndexed { index, i ->
            if (index != 0) sb.put(", ")
            sb.put(".${names[i]} = ")
            (values[i] as? IRCreateFixed)?.takeIf { CTypes.fixedOf(record.fields[names[i]]!!) != null }?.braces(sb, c) ?: values[i].toC(sb, c)
        }
        sb.put("})")
        later.forEach {
            sb.put(", ")
            values[it].toC(sb, c)
            sb.put(")")
        }
    }

    override fun toString() = "${record.name}{${names.zip(values).joinToString { "${it.first}=${it.second}" }}}"
}
