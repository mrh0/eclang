package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.ect2.Type

/**
 * `a..<b` (b excluded) or `a..=b` (b included). A range is only a loop bound (`for i in a..<b`, see TStatementForeachIn), an
 * index (`s[a..<b]`, see TAccessor) or a `when` arm (`1..<10 do`); they read its parts directly. In the last two a side may
 * be missing (null): `s[1..]`, `s[..<3]`.
 */
class TRange(location: Loc, val from: ITok?, val to: ITok?, val inclusive: Boolean) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> =
        throw EcError(location, "A range can't be stored or passed around yet; use it directly, to count (`for i in a..<b do`) or to slice an array or String (`s[a..<b]`).")

    override fun toString() = "TRange($from ..${if (inclusive) "=" else "<"} $to)"
}
