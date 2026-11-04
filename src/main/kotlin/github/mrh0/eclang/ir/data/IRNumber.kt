package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** A number literal. [type] picks the C suffix (`10L`, `10UL`, `2.5f`); integers too big for `int` get `L`. */
class IRNumber(location: Loc, val value: Number, val type: Type) : IRValue(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(when {
            value is Double -> {
                val text = value.toString()
                if (type === Types.Float) "${text}f" else text
            }
            // C has no negative literals: `-9223372036854775808L` is `-` applied to a number too big for a long.
            value is Long && value == Long.MIN_VALUE -> "(-9223372036854775807L - 1)"
            value.toLong() == Int.MIN_VALUE.toLong() && type === Types.Int -> "(-2147483647 - 1)"
            type === Types.ULong || type === Types.Size -> "${value}UL"
            type === Types.UInt -> "${value}U"
            type === Types.Long || value.toLong() !in Int.MIN_VALUE..Int.MAX_VALUE -> "${value}L"
            else -> value.toString()
        })
    }

    override fun toString() = "$value"
}
