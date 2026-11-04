package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.ect2.*

/** Converts [expr] from [from] to [to]; how is decided by the C representation rules in CTypes. */
class IRConvert(location: Loc, val expr: IIR, val from: Type, val to: Type, val mode: Mode) : IR(location) {
    enum class Mode {
        /** An implicit, always-safe conversion (widening, wrapping into a union). */
        IMPLICIT,
        /** `as`: may narrow numbers; unwraps unions and downcasts records with a runtime check. */
        EXPLICIT,
        /** Reading a variable whose type flow analysis narrowed; already known to be safe. */
        NARROW,
        /** `!as`: a plain C cast, no checks. */
        UNSAFE,
    }

    /** Conversions out of a union or between records call a helper function, so they're never constant. */
    override fun isConstant() = expr.isConstant() && (mode == Mode.IMPLICIT || mode == Mode.UNSAFE) && from !is Union &&
        !(from is Record && to is Record) && !(from === Types.String && to is ArrayType) && !(mode == Mode.UNSAFE && from !== to && !CTypes.isScalar(from))

    override fun toC(sb: CSourceBuilder, c: Context) {
        val inner = sb.render { expr.toC(it, c) }
        sb.put(when (mode) {
            Mode.IMPLICIT -> sb.types.convert(from, to, inner, where(location))
            Mode.EXPLICIT -> sb.types.convert(from, to, inner, where(location), explicit = true)
            Mode.NARROW -> sb.types.narrow(from, to, inner, where(location))
            Mode.UNSAFE -> sb.types.unsafeCast(from, to, inner)
        })
    }

    override fun toString() = "Convert($expr: $from -> $to, $mode)"

    companion object {
        /** A C string literal naming [location], for runtime error messages. */
        fun where(location: Loc) = "\"${location.getFilename()}:${location.getLine()}\""
    }
}
