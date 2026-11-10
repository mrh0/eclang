package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRString
import github.mrh0.eclang.ir.data.IRCreateFixed
import github.mrh0.eclang.ir.data.IRFixedText
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.error.EcError
import github.mrh0.ect2.FixedArray
import github.mrh0.ect2.Union
import github.mrh0.eclang.util.StringLiteral
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TString(location: Loc, private val value: StringLiteral) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        textArray(hint)?.let { (expected, fixed) ->
            if (value.size > fixed.size) throw EcError(location, "This String has ${value.size} bytes, but a $expected holds ${fixed.size}. Shorten the text, or make the array larger.")
            return expected to IRCreateFixed(location, fixed, listOf(IRFixedText(location, value, fixed.size)))
        }
        return Types.String to IRString(location, value)
    }

    /** The `Char[N]` that [hint] expects (also in `Char[N]?`), with its plain FixedArray: a literal fills one like C's `char a[N] = "..."`. */
    private fun textArray(hint: Type): Pair<Type, FixedArray>? {
        val expected = ((hint as? Union)?.members ?: listOf(hint)).filter { (Ops.base(it) as? FixedArray)?.element === Types.Char }.singleOrNull() ?: return null
        if (hint is Union && hint.members.any { Ops.base(it) === Types.String }) return null
        return expected to Ops.base(expected) as FixedArray
    }

    override fun toString(): String {
        return "TString($value)"
    }
}
