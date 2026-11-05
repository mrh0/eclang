package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.IAssignable
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Place
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRReadPointer
import github.mrh0.ect2.Pointer
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/** `@p`: the value p points to. */
class TReadPointer(location: Loc, val expr: ITok) : Tok(location), IAssignable {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = read(cd).also { Lifetimes.checkThrough(cd, location, expr) }

    private fun read(cd: CompileData): Pair<Type, IIR> {
        val (t, ir) = cd.usable(location, expr.process(cd, Types.None), "`@`")
        if (t is Union && t.isNullable && cd.types.nonNull(t) is Pointer)
            throw EcError(location, "This pointer may be null. Check it first with `if p != null`, or assert it with `@p!`.")
        if (t !is Pointer) throw EcError(location, "`@` reads what a pointer points to, but this is a $t.")
        if (t.target === Types.Any || t.target === Types.None) throw EcError(location, "Can't read through an untyped pointer (@Any); convert it first, e.g. `p !as @Int`.")
        return t.target to IRReadPointer(location, ir)
    }

    /** `@p = x` writes where p points, so p itself may be a `val`. */
    override fun place(cd: CompileData): Place = read(cd).let { (t, ir) -> Place(t, ir) }

    override fun toString(): String {
        return "TReadPointer($expr)"
    }
}
