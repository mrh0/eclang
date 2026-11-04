package github.mrh0.eclang.ir.data.record

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Record

/** `(Name){.field = value, ...}`; records that take part in `extends` also get their type tag. */
class IRCreateRecord(location: Loc, val record: Record, val names: List<String>, val values: List<IIR>) : IR(location) {
    override fun isConstant() = values.all { it.isConstant() }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("((${sb.types.cName(record)}){")
        if (record.inHierarchy) sb.put(".__ec_tid = ${record.id}${if (values.isEmpty()) "" else ", "}")
        values.forEachIndexed { index, it ->
            if (index != 0) sb.put(", ")
            sb.put(".${names[index]} = ")
            it.toC(sb, c)
        }
        sb.put("})")
    }

    override fun toString() = "${record.name}{${names.zip(values).joinToString { "${it.first}=${it.second}" }}}"
}
