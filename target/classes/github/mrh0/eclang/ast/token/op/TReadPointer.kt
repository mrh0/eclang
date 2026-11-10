package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.IAssignable
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Place
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRReadPointer
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.ect2.Pointer
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `@p`: the value p points to. With [mutParameter], p is a `mut` parameter, which the code writes as plain `p`: the Visitor
 * makes each such `p` an `@p`. Lifetimes then treats `addrof p` as memory the function can't keep (see `Lifetimes.place`).
 */
class TReadPointer(location: Loc, val expr: ITok, val mutParameter: Boolean = false) : Tok(location), IAssignable {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(expr)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = read(cd).also { Lifetimes.checkThrough(cd, location, expr) }
        // `if p != null do` narrows a `mut` parameter p, under `@p` (see Narrowing.local).
        val narrowed = narrowingKey(cd)?.let { cd.ctx().stack.narrowedType(it) } ?: return t to ir
        return narrowed to IRConvert(location, ir, t, narrowed, IRConvert.Mode.NARROW)
    }

    /**
     * The target of `p op= v` for a `mut` parameter p, with its narrowed type (`x += 1` inside `if x != null`), which stays
     * true, as for a variable (see TStatementAssign.variable); null when it isn't narrowed. A plain `=` takes [place].
     */
    fun narrowedPlace(cd: CompileData): Place? {
        val narrowed = narrowingKey(cd)?.let { cd.ctx().stack.narrowedType(it) } ?: return null
        val (t, ir) = read(cd)
        return Place(narrowed, IRConvert(location, ir, t, narrowed, IRConvert.Mode.NARROW))
    }

    /** For a `mut` parameter p, `@p`: the key its narrowing is kept under. */
    private fun narrowingKey(cd: CompileData): String? =
        if (!mutParameter) null else (expr as? TNamed)?.let { "@" + github.mrh0.eclang.util.Util.testIdentifier(it.location, it.name) }

    private fun read(cd: CompileData): Pair<Type, IIR> {
        val (t, ir) = cd.usable(location, expr.process(cd, Types.None), "`@`")
        if (t is Union && t.isNullable && cd.types.nonNull(t) is Pointer)
            throw EcError(location, "This pointer may be null. Check it first with `if p != null`, or assert it with `@p!`.")
        if (t !is Pointer) throw EcError(location, "`@` reads what a pointer points to, but this is a $t.")
        if (t.target === Types.Any || t.target === Types.None) throw EcError(location, "Can't read through an untyped pointer (@Any); convert it first, e.g. `p !as @Int`.")
        return t.target to IRReadPointer(location, ir, parenthesized = mutParameter)
    }

    /**
     * `@p = x` writes where p points, so p itself may be a `val`. Of a `ref rec`, `@c` is the record everyone who has c shares,
     * so overwriting it changes its fields: that needs a `mut ref rec`.
     */
    override fun place(cd: CompileData): Place = read(cd).let { (t, ir) ->
        val shared = (t as? Record)?.takeIf { it.layout.isRef && !it.isMutable }
        val readOnly = shared?.let { r ->
            val name = r.generic?.name ?: r.name
            "`@` of a ${r.name} is the record itself, which everyone who refers to it shares, and ${r.name} isn't a `mut ref rec`: it is fixed once it is made. " +
                "Refer to a new one instead (`c = new $name { ...@c, field = value }`), or declare `mut ref rec $name`."
        }
        Place(t, ir, readOnly, name = narrowingKey(cd))
    }

    override fun toString(): String {
        return "TReadPointer($expr)"
    }
}
