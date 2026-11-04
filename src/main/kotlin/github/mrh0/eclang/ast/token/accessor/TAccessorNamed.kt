package github.mrh0.eclang.ast.token.accessor

import github.mrh0.eclang.ast.IAssignable
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Place
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUndefinedAccessorPropertyError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.accessor.IRAccessorNamed
import github.mrh0.eclang.ir.accessor.IRSafeAccess
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*

/**
 * `x.name`: a record field (through a pointer too), `.len`/`.data` of arrays and Strings, `.first` of tuples, or
 * the global `name` of module x. With [safe] it is `x?.name`: null when x is null, otherwise the field (the
 * result is nullable).
 */
class TAccessorNamed(location: Loc, val expr: ITok, val name: String, private val safe: Boolean = false) : Tok(location), IAssignable {
    /** Whether the last compiled access read the field through a pointer (`p.x` with p a `@Point`); see Lifetimes. */
    var throughPointer = false
        private set

    /** The global `name` of module x, when x names a module (and no variable). */
    private fun moduleGlobal(cd: CompileData): github.mrh0.eclang.context.state.IVar? {
        if (safe || expr !is TNamed || cd.findVar(expr.name) != null) return null
        return cd.findModule(expr.name)?.let { m -> cd.getVarIn(location, m, Util.testIdentifier(location, name)) }
    }

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fixedName = Util.testIdentifier(location, name)
        moduleGlobal(cd)?.let { v -> return v.getType() to v.toIR(location, cd, hint) }
        val op = if (safe) "?." else "."
        val (t, ir) = cd.usable(location, expr.process(cd, Types.None), "`$op$name`")
        if (!safe) return field(cd, t, fixedName).let { (type, throughPointer) ->
            this.throughPointer = throughPointer
            type to IRAccessorNamed(location, ir, fixedName, throughPointer)
        }

        if (t !is Union || !t.isNullable) throw EcError(location, "A value of type $t is never null, so `?.` is not needed. Write `.$name` instead.")
        val nonNull = cd.types.nonNull(t)
        val (fieldType, throughPointer) = field(cd, nonNull, fixedName)
        val result = cd.storable(location, cd.types.union(fieldType, Types.Null), "`?.$name`")
        return result to IRSafeAccess(location, ir, t, nonNull, fixedName, fieldType, result, throughPointer)
    }

    override fun place(cd: CompileData): Place {
        val fixedName = Util.testIdentifier(location, name)
        moduleGlobal(cd)?.let { v ->
            val readOnly = if (v.canAssign()) null else "'${(expr as TNamed).name}.$name' is ${v.kind()}, so it can't be assigned a new value."
            return Place(v.getType(), v.toIR(location, cd, Types.None), readOnly)
        }
        if (safe) throw EcError(location, "Can't assign through `?.`: when the value is null there is nothing to assign to. Check it first with `if x != null do`.")
        val e = expr.unnested()
        val base = if (e is IAssignable) e.place(cd)
            else cd.usable(location, e.process(cd, Types.None), "`.$name`").let { (t, ir) ->
                Place(t, ir, "This is a temporary value, so changing its field `.$name` would have no effect. Store it in a `var` first.")
            }
        cd.usable(location, base.type to base.ir, "`.$name`")
        val (type, throughPointer) = field(cd, base.type, fixedName)
        this.throughPointer = throughPointer
        val readOnly = when {
            throughPointer -> null
            base.type === Types.String || base.type is ArrayType ->
                "`.$name` can't be assigned: it describes the memory this ${base.type} points to. Make a new value instead, e.g. a slice `x[0..<n]`."
            else -> base.readOnly
        }
        return Place(type, IRAccessorNamed(location, base.ir, fixedName, throughPointer), readOnly)
    }

    /** The type of field [name] of a value of type [t], and whether it's read through a pointer. */
    private fun field(cd: CompileData, t: Type, name: String): Pair<Type, Boolean> {
        val throughPointer = t is Pointer && t.target is Declared
        val target = if (t is Pointer && t.target is Declared) t.target else t
        val fieldType: Type = when {
            target is Declared -> target.field(name)
                ?: throw EcError(location, "${target.name} has no field '$name'.${CompileData.suggestion(name, target.fields.keys)}")
            target === Types.String || (target is ArrayType && !target.isC) -> when (name) {
                "len" -> Types.Size
                "data" -> if (target === Types.String) Types.CString else cd.types.cArray((target as ArrayType).element)
                else -> throw EcUndefinedAccessorPropertyError(location, name, t)
            }
            target is Tuple -> target.elements.getOrNull((0 until target.elements.size).firstOrNull { Util.getTupleEntryName(it, 0) == name } ?: -1)
                ?: throw EcUndefinedAccessorPropertyError(location, name, t)
            target is Union && target.isNullable ->
                throw EcError(location, "Can't read '.$name' because the value may be null. Check it first with `if x != null`, use `x?.$name` (null when x is null) or `x!.$name` (stops the program when x is null).")
            target is Union -> throw EcError(location, "Can't read '.$name' of a $t; narrow it first, e.g. `if x is ${target.members.first()} do`.")
            else -> throw EcUndefinedAccessorPropertyError(location, name, t)
        }
        return fieldType to throughPointer
    }

    override fun toString(): String {
        return "TAccessorNamed($expr, ${if (safe) "?" else ""}$name)"
    }
}
