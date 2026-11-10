package github.mrh0.eclang.ast.token

import github.mrh0.eclang.Completion
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.IAssignable
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Place
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.data.record.THere
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.state.NarrowedVar
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * A variable, constant or global by name. When nothing else has that name, `pool` is the current pool and `here` a `Location`
 * of where it is written ([THere]). Neither is a keyword.
 */
class TNamed (location: Loc, val name: String) : Tok(location), IAssignable {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing // Lifetimes follows variables itself

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (name == Completion.PROBE && cd.completion != null) Completion.name(cd, hint)
        if (isCurrentPool(cd, name)) return cd.types.pool to IRRaw(location, CURRENT_POOL)
        if (isHere(cd, name)) return THere(location).process(cd, hint)
        val fixedName = testIdentifier(location, name)
        val v = cd.getVar(location, fixedName)
        Lifetimes.checkAlive(cd, location, fixedName, name)
        cd.unusedResources.remove(fixedName)
        return v.getType() to v.toIR(location, cd, hint)
    }

    /** The variable as the start of a longer target (`p.x = 1`), so with its narrowed type. */
    override fun place(cd: CompileData): Place {
        if (name == Completion.PROBE && cd.completion != null) Completion.name(cd, Types.None)
        if (isCurrentPool(cd, name))
            return Place(cd.types.pool, IRRaw(location, CURRENT_POOL), "`pool` is the current pool and can't be assigned. To allocate in another pool for a while, use a `pool do` block; to allocate one value elsewhere, write `new x in p`.")
        if (isHere(cd, name)) {
            val (type, ir) = THere(location).process(cd, Types.None)
            return Place(type, ir, "`here` is the place where it is written, and can't be assigned. Copy it into a variable first (`var at = here`).")
        }
        val fixedName = testIdentifier(location, name)
        val v = cd.getVar(location, fixedName)
        Lifetimes.checkAlive(cd, location, fixedName, name)
        val base = (v as? NarrowedVar)?.base ?: v
        val readOnly = when {
            base.canAssign() -> null
            base.kind() == "a parameter" -> "'$name' is a parameter, so its fields can't change. Copy it into a variable first (`var copy = $name`), or make it a `mut` parameter (`mut $name: ${v.getType()}`) to change the caller's variable."
            else -> "'$name' is ${base.kind()}, so its fields can't change either. Declare it with `var` to change them."
        }
        return Place(v.getType(), v.toIR(location, cd, Types.None), readOnly)
    }

    override fun toString(): String {
        return "TNamed($name)"
    }

    companion object {
        /** The C variable holding the current pool (a thread-local the C `main` and `pool do` blocks set). */
        const val CURRENT_POOL = "__ec_pool"

        /** Whether [name] means the current pool: it is `pool`, and no variable or global has that name. */
        fun isCurrentPool(cd: CompileData, name: String) = name == "pool" && cd.findVar(name) == null

        /** Whether [name] means the place where it is written: it is `here`, and no variable or global has that name. */
        fun isHere(cd: CompileData, name: String) = name == "here" && cd.findVar(name) == null
    }
}
