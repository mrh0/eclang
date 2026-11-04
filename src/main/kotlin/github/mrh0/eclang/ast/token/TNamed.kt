package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.IAssignable
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Place
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.state.NarrowedVar
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** A variable, constant or global by name. `pool`, when nothing else has that name, is the current pool. */
class TNamed (location: Loc, val name: String) : Tok(location), IAssignable {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (isCurrentPool(cd, name)) return cd.types.pool to IRRaw(location, CURRENT_POOL)
        val fixedName = testIdentifier(location, name)
        val v = cd.getVar(location, fixedName)
        return v.getType() to v.toIR(location, cd, hint)
    }

    /** The variable as the start of a longer target (`p.x = 1`), so with its narrowed type. */
    override fun place(cd: CompileData): Place {
        if (isCurrentPool(cd, name))
            return Place(cd.types.pool, IRRaw(location, CURRENT_POOL), "`pool` is the current pool and can't be assigned. To allocate in another pool for a while, use a `pool do` block; to allocate one value elsewhere, write `new x in p`.")
        val fixedName = testIdentifier(location, name)
        val v = cd.getVar(location, fixedName)
        val base = (v as? NarrowedVar)?.base ?: v
        val readOnly = when {
            base.canAssign() -> null
            base.kind() == "a parameter" -> "'$name' is a parameter, so its fields can't change. Copy it into a variable first (`var copy = $name`), or take a pointer parameter (`@${v.getType()}`) to change the caller's value."
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
    }
}
