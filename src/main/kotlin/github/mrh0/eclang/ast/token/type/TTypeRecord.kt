package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Record
import github.mrh0.ect2.Tuple
import github.mrh0.ect2.Union
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `rec Name extends Parent as` + fields. */
class TTypeRecord(location: Loc, val name: String, val names: List<String>, val types: List<ITok>, private val extending: String?) : Tok(location), ITypeDeclaration {
    override val parentName get() = extending

    override fun declareType(cd: CompileData) {
        val parent = extending?.let {
            val p = cd.lookupType(location, it)
            if (p !is Record || p.isExternal) throw EcError(location, "$name can only extend a record declared in eclang, but $it is not one.")
            p
        }
        checkNewTypeName(cd, location, name)
        cd.claimCName(location, cd.module.cName(name), "record '$name'", renamable = false)
        cd.types.record(cd.module.name, name, parent)
    }

    /**
     * Rejects a record that contains itself by value (`a: A`, or through other records, unions and tuples): it
     * would be infinitely large. A pointer (`@A?`) or an array breaks the cycle.
     */
    fun checkFinite(cd: CompileData) {
        val record = declared(cd, location, name) as Record
        val seen = HashSet<Type>()
        /** The fields leading from [t] back to [record], e.g. ["a"] or ["b", "a"], or null. */
        fun path(t: Type): List<String>? = when (t) {
            is Record -> if (!seen.add(t)) null else t.fields.entries.firstNotNullOfOrNull { (field, ft) ->
                if (ft === record) listOf(field) else path(ft)?.let { listOf(field) + it }
            }
            is Union -> t.members.firstNotNullOfOrNull { if (it === record) listOf() else path(it) }
            is Tuple -> t.elements.firstNotNullOfOrNull { if (it === record) listOf() else path(it) }
            else -> null
        }
        val cycle = path(record) ?: return
        val first = cycle[0]
        throw EcError(location, "Record $name contains itself (through ${cycle.joinToString(".", "$name.")}), so it would be infinitely large. " +
            "Store a pointer instead, e.g. `$first: @${record.fields[first]?.let { if (it is Record) it.name else name } ?: name}?`.")
    }

    override fun defineType(cd: CompileData) = defineFields(cd, location, declared(cd, location, name), names, types)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        declareType(cd)
        defineType(cd)
        return Types.None to IRPass(location, null)
    }

    override fun toString(): String {
        return "TTypeRecord($name)"
    }
}
