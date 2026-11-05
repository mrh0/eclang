package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ast.token.type.applyGeneric
import github.mrh0.ect2.GenericRecord
import github.mrh0.ect2.Pointer
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type

/** `Point { 1, 2 }` or `Point { x = 1, y = 2 }`; for a generic record `Box<Int> { 1 }`, or `Box { 1 }` where a `Box<Int>` is expected. */
class TCreateRecordTyped(location: Loc, private val typeName: String, val values: List<ITok>, private val names: List<String>? = null, private val typeArgs: List<ITok> = listOf()) : Tok(location) {
    /** What the last compiled literal gave and left to defaults (see Lifetimes). */
    var defaults = RecordDefaults()
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val record = record(cd, hint)
        val (used, ir) = createRecord(cd, location, record, values, names)
        defaults = used
        return record to ir
    }

    /** The record this literal makes; [hint] decides the type arguments of a generic one written without them. */
    fun record(cd: CompileData, hint: Type): Record {
        val type = cd.lookupType(location, typeName)
        if (type !is GenericRecord) {
            if (typeArgs.isNotEmpty()) throw EcError(location, "$typeName has no type parameters, so write it without `<...>`.")
            return type as? Record ?: throw EcError(location, "$typeName is not a record.")
        }
        if (typeArgs.isNotEmpty()) return applyGeneric(cd, location, type, typeArgs) as? Record
            ?: throw EcError(location, "The type arguments of $typeName aren't known here. Use types the function knows, such as its own `<T>`.")
        val expected = cd.types.nonNull(hint).let { (it as? Pointer)?.target ?: it }
        return (expected as? Record)?.takeIf { it.generic === type }
            ?: throw EcError(location, "Can't tell which $typeName this is. Write its type arguments, e.g. `$typeName<${type.params.joinToString(", ") { "Int" }}> { ... }`.")
    }

    override fun toString(): String {
        return "TCreateRecordTyped($typeName, $values)"
    }
}
