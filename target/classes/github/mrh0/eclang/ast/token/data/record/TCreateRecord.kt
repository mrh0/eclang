package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.Union

/** `{ 1, 2 }` or `{ a = 1, b = 2 }`: a record whose type comes from where it is used. */
class TCreateRecord(location: Loc, val values: List<ITok>, private val names: List<String>? = null) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(values)

    /** What the last compiled literal gave and left to defaults (see Lifetimes). */
    var defaults = RecordDefaults()
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val record = hint as? Record ?: (hint as? Union)?.members?.filterIsInstance<Record>()?.singleOrNull()
            ?: throw EcError(location, "Can't tell which record this is. Write its name in front, e.g. `Point { ... }`.")
        val (used, ir) = createRecord(cd, location, record, values, names)
        defaults = used
        return record to ir
    }

    override fun toString(): String {
        return "TCreateRecord($values)"
    }
}
