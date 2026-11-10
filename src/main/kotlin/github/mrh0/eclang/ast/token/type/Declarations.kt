package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ir.arithmetic.IRCheckedBits
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRConstantRef
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.ir.IRExprNest
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Declared
import github.mrh0.ect2.IntLiteral
import github.mrh0.ect2.Layout
import github.mrh0.ect2.Prim
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * Gives [type] its fields, checking names are unique (also against inherited fields) and every type can be stored.
 * [widths] are the widths of bit-fields (`flags: UInt : 3`), null for other fields.
 */
internal fun defineFields(cd: CompileData, location: Loc, type: Declared, names: List<String>, types: List<ITok>, widths: List<ITok?> = listOf(), cNames: List<String?> = listOf()) {
    val inherited = (type as? Record)?.parent?.fields ?: mapOf()
    val fields = LinkedHashMap<String, github.mrh0.ect2.Type>()
    names.forEachIndexed { i, raw ->
        val name = testIdentifier(location, raw)
        if (name in fields || name in inherited) throw EcError(location, "${type.name} has two fields called '$raw'.")
        val written = types[i].process(cd, Types.None).first
        // A field of a C struct holds what C put there: a function type is a C function pointer.
        fields[name] = cd.storable(location, if ((type as? Record)?.isExternal == true) cd.types.inC(written) else written, "field '$raw' of ${type.name}")
        widths.getOrNull(i)?.let { (type as Record).bitWidths[name] = bitWidth(cd, location, it, fields[name]!!, raw) }
        cNames.getOrNull(i)?.let { c ->
            if (!Regex("[A-Za-z_][A-Za-z0-9_]*").matches(c))
                throw EcError(location, "\"$c\", the C name of field '$raw' of ${type.name}, isn't a C identifier. Write the name the C header gives the field, e.g. `input extern \"in\": Int`.")
            (type as Record).cFieldNames[name] = c
        }
    }
    type.define(fields)
}

/** The width of the bit-field [field] of type [type], written as [width]. */
private fun bitWidth(cd: CompileData, location: Loc, width: ITok, type: Type, field: String): Int {
    val prim = Ops.base(type) as? Prim
    if (prim == null || !(prim.isInteger || prim === Types.Bool))
        throw EcError(location, "Bit-field '$field' must be a whole number or a Bool, but this is a $type. Remove the `: width`, or use a type such as UInt.")
    val bits = compileTimeInt(cd, location, width, "The width of bit-field '$field'")
    val most = if (prim === Types.Bool) 1 else prim.bits
    if (bits !in 1..most.toLong()) throw EcError(location, "The width of bit-field '$field' must be from 1 to $most, the bits of a $type, but this is $bits.")
    return bits.toInt()
}

/** Checks [value], the type of what is stored in a field of [record] (a number literal says how large it is), fits bit-field [field]. */
internal fun checkBits(location: Loc, record: Record, field: String, fieldType: Type, value: Type) {
    val width = record.bitWidths[field] ?: return
    val number = (value as? IntLiteral)?.value ?: return
    val prim = Ops.base(fieldType) as Prim
    val (lowest, highest) = if (prim.category == github.mrh0.ect2.Category.SINT) -(1L shl (width - 1)) to (1L shl (width - 1)) - 1
        else 0L to (if (width >= 63) Long.MAX_VALUE else (1L shl width) - 1)
    if (number !in lowest..highest) throw EcError(location, "$number doesn't fit bit-field '$field', which has $width bit(s): it holds $lowest to $highest. Make the field wider, or store a smaller value.")
}

/**
 * [ir], a value of type [source] converted to the type of bit-field [field] of [record], with a runtime check that it
 * fits when it may not ([IRCheckedBits]). A literal is checked by [checkBits]; [ir] is returned as it is for other fields.
 */
internal fun fitBits(location: Loc, record: Record, field: String, fieldType: Type, source: Type, ir: IIR): IIR {
    val width = bitsToCheck(record, field, fieldType) ?: return ir
    if (source is IntLiteral || source === Types.Never) return ir
    val prim = Ops.base(fieldType) as Prim
    val from = Ops.base(source) as? Prim
    if (from != null && from.isInteger) {
        val signed = prim.category == github.mrh0.ect2.Category.SINT
        val fits = if (from.category == github.mrh0.ect2.Category.SINT) signed && from.bits <= width else from.bits <= (if (signed) width - 1 else width)
        if (fits) return ir
    }
    return IRCheckedBits(location, ir, prim, width, field)
}

/** The width of bit-field [field] of [record] when a value of its type may not fit it, or null. */
internal fun bitsToCheck(record: Record, field: String, fieldType: Type): Int? {
    val width = record.bitWidths[field] ?: return null
    val prim = Ops.base(fieldType) as Prim
    return width.takeIf { prim !== Types.Bool && it < prim.bits }
}

/** The whole number [tok] stands for at compile time, a number or a `const`. [what] is the sentence's subject in the error. */
internal fun compileTimeInt(cd: CompileData, location: Loc, tok: ITok, what: String): Long {
    val (type, ir) = tok.process(cd, Types.None)
    return (type as? IntLiteral)?.value ?: knownInt(ir)
        ?: throw EcError(location, "$what must be known to eclang when it compiles: a number or a `const` defined in eclang, so not a `val` or a value from a C header. Write the number, e.g. `const NAME_LENGTH = 64`.")
}

private fun knownInt(ir: IIR): Long? = when (ir) {
    is IRNumber -> ir.value.takeIf { it !is Double }?.toLong()
    is IRConstantRef -> knownInt(ir.value)
    is IRConvert -> if (ir.mode == IRConvert.Mode.IMPLICIT) knownInt(ir.expr) else null
    is IRExprNest -> knownInt(ir.expr)
    else -> null
}

/** What is written before `rec` and `union`: `packed`, `align(n)`, `mut`, and whether it is a `union`. */
class TLayout(private val packed: Boolean = false, private val align: ITok? = null, val isUnion: Boolean = false, val isRef: Boolean = false, private val isResource: Boolean = false,
              private val isSync: Boolean = false, val isMut: Boolean = false) {
    fun resolve(cd: CompileData, location: Loc): Layout {
        val n = align?.let { compileTimeInt(cd, location, it, "The alignment of `align(n)`") }
        if (n != null && (n <= 0 || n and (n - 1) != 0L)) throw EcError(location, "The alignment of `align(n)` must be a power of two, such as 8 or 16, but this is $n.")
        return Layout(packed, n, isUnion, isRef, isResource, isSync, isMut)
    }
}

internal fun declared(cd: CompileData, location: Loc, name: String): Declared =
    cd.lookupType(location, name) as? Declared ?: throw EcError(location, "BUG: $name was not declared")

/** Checks the current module doesn't define a type called [name] yet. Call before declaring it. */
internal fun checkNewTypeName(cd: CompileData, location: Loc, name: String) {
    if (cd.types.lookupIn(cd.module.name, name) != null) throw EcError(location, "A type called '$name' is already defined in ${cd.module}.")
}
