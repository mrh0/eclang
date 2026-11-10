package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.ect2.*

/** `T[N]`: N elements stored in the value itself, N being known at compile time. */
class TTypeFixedArray(location: Loc, val type: ITok, private val size: ITok) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val element = type.process(cd, Types.None).first
        val n = compileTimeInt(cd, location, size, "The length of a `T[N]`")
        if (n <= 0) throw EcError(location, "The length of a `T[N]` must be at least 1, but this is $n. For an array whose length changes, use `$element[]`.")
        val array = cd.types.fixedArray(element, n)
        return array to IRType(location, array)
    }

    override fun toString() = "TTypeFixedArray($type, $size)"
}
