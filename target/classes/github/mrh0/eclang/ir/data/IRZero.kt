package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.ect2.Prim
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** The empty value of [type] as a C initializer: `0`, `false`, or `{0}` for structs (a record in a hierarchy keeps its type tag). */
class IRZero(location: Loc, private val type: Type) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(when {
            type === Types.Bool -> "false"
            type is Prim && type !== Types.String -> "0"
            type is Record && type.inHierarchy -> "{.${CTypes.tagPath(type)} = ${type.id}}"
            else -> "{0}"
        })
    }

    override fun toString() = "zero($type)"
}
