package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `format(pool, "...")`: a call of a generated helper that prints [format] (a C printf format, escaped) with
 * [args] into memory from [pool] and returns it as a String.
 */
class IRFormat(location: Loc, private val pool: IIR, private val format: String, private val args: List<Pair<Type, IIR>>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("${sb.types.format(format, args.map { it.first })}(")
        pool.toC(sb, c)
        args.forEach { sb.put(", "); it.second.toC(sb, c) }
        sb.put(")")
    }

    override fun toString() = "format($pool, \"$format\", ${args.map { it.second }})"

    companion object {
        /** How `${x}` prints a value of type [t]: the printf conversion, and the C arguments for a parameter named `a`. Null: it can't. */
        fun spec(t: Type): Pair<String, (String) -> String>? = when (t) {
            Types.String -> "%.*s" to { a -> "(int)$a.len, $a.data" }
            Types.CString -> "%s" to { a -> a }
            Types.Atom -> ":%s" to { a -> a }
            Types.Bool -> "%s" to { a -> "$a ? \"true\" : \"false\"" }
            Types.Char -> "%c" to { a -> a }
            Types.Short, Types.Int -> "%d" to { a -> a }
            Types.UChar, Types.UShort, Types.UInt -> "%u" to { a -> a }
            Types.Long -> "%ld" to { a -> a }
            Types.ULong -> "%lu" to { a -> a }
            Types.Size -> "%zu" to { a -> a }
            Types.Float, Types.Double -> "%g" to { a -> a }
            else -> null
        }
    }
}
