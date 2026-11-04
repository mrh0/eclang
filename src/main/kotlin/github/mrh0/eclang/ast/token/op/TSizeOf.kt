package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRSizeOf
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `sizeof T`: the size of T in bytes, a Size. */
class TSizeOf(location: Loc, val type: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = type.process(cd, Types.None)
        cd.storable(location, t, "`sizeof`")
        return Types.Size to IRSizeOf(location, ir)
    }

    override fun toString(): String {
        return "TSizeOf($type)"
    }
}

/** `alignof T`: the alignment of T in bytes (what its address must be a multiple of), a Size. */
class TAlignOf(location: Loc, val type: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = type.process(cd, Types.None)
        cd.storable(location, t, "`alignof`")
        return Types.Size to IRSizeOf(location, ir, "alignof")
    }

    override fun toString() = "TAlignOf($type)"
}

/** `offsetof(T, field)`: where [field] starts inside the record T, in bytes from its start, a Size. */
class TOffsetOf(location: Loc, val type: ITok, private val field: String) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = type.process(cd, Types.None)
        val name = testIdentifier(location, field)
        if (t !is Record) throw EcError(location, "`offsetof` needs a record type, but $t has no fields.")
        if (t.field(name) == null) throw EcError(location, "$t has no field '$field'.${CompileData.suggestion(field, t.fields.keys)}")
        return Types.Size to IRSizeOf(location, ir, "offsetof", name)
    }

    override fun toString() = "TOffsetOf($type, $field)"
}
