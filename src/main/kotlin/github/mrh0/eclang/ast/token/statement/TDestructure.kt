package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.IAssignable
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.data.TCreateTuple
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.context.state.NarrowedVar
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRStatementDefineVal
import github.mrh0.eclang.util.Util
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Record
import github.mrh0.ect2.Tuple
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/** The name of a hidden local with the C name [cName]. Nothing in a source file can spell it, so it can't clash. */
internal fun hiddenName(cName: String) = "#$cName"

/**
 * What [pattern] takes out of [source], a value of type [type]: each target with the token reading its part, with
 * nested patterns flattened and `_` left out. `(a, (b, _))` gives a = source.first, b = source.second.first.
 */
internal fun destructured(location: Loc, pattern: TCreateTuple, type: Type, source: ITok): List<Pair<ITok, ITok>> {
    val tuple = Ops.base(type) as? Tuple ?: throw EcError(location, notATuple(pattern, type))
    if (tuple.elements.size != pattern.values.size) throw EcError(location,
        "`${pattern.pattern()}` names ${pattern.values.size} parts, but this tuple has ${tuple.elements.size}: $tuple. Name every part, with `_` for the ones you don't need.")
    return pattern.values.flatMapIndexed { i, part ->
        val read = TAccessorNamed(location, source, Util.getTupleEntryName(i, 0))
        when (val target = part.unnested()) {
            is TCreateTuple -> destructured(location, target, tuple.elements[i], read)
            is TNamed -> if (target.name == "_") listOf() else listOf(target to read)
            else -> listOf(target to read)
        }
    }
}

private fun notATuple(pattern: TCreateTuple, type: Type): String {
    val start = "`${pattern.pattern()}` takes a tuple apart, but this value's type is $type."
    return when {
        type is Union && type.isNullable -> "$start It may be null: check it first with `if x != null`, or give a default with `?? value`."
        type is Record -> "$start Read its fields one by one instead, e.g. `x.${type.fields.keys.firstOrNull() ?: "field"}`."
        else -> "$start For a single value, write `val name = ...`."
    }
}

/** `val name = value` for the part [target] of a pattern of names. */
internal fun defineLocal(cd: CompileData, location: Loc, target: ITok, value: ITok, variable: Boolean): IIR {
    val name = (target as TNamed).name
    val define = if (variable) TStatementDefineVar(location, name, value) else TStatementDefineVal(location, name, value)
    return define.process(cd, Types.None).second
}

/**
 * `val (a, b) = value`, `var (a, b) = value` and `(a, b) = value`. The value goes into a hidden local first, and each
 * part is then an ordinary definition or assignment reading from it. So `(a, b) = (b, a)` swaps, and flow typing,
 * lifetimes and `val` rules work as they do for one variable.
 */
class TDestructure(location: Loc, private val pattern: TCreateTuple, private val expr: ITok, private val typeTok: ITok?, private val kind: Kind) : Tok(location) {
    enum class Kind { VAL, VAR, ASSIGN }

    /** `(a, b) = value`, which declares no name. */
    val assigns get() = kind == Kind.ASSIGN

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val what = "`${pattern.pattern()}`"
        val declared = typeTok?.let { cd.storable(location, it.process(cd, Types.None).first, what) }
        val value = expr.process(cd, declared ?: (if (assigns) targetType(cd, pattern) else null) ?: Types.None)
        val type = declared ?: cd.storable(location, value.first, what)
        val parts = cd.temp().let { temp ->
            val hidden = cd.ctx().define(location, Constant(hiddenName(temp), type, temp))
            Lifetimes.define(cd, hiddenName(temp), expr, type)
            val define = IRStatementDefineVal(location, hidden, cd.expect(location, value, type, what))
            listOf<IIR>(define) + destructured(location, pattern, type, TNamed(location, hiddenName(temp))).map { (target, read) ->
                if (assigns) TStatementAssign(location, target, "", read).process(cd, Types.None).second
                else defineLocal(cd, location, target, read, kind == Kind.VAR)
            }
        }
        return Types.None to IRBlock(location, parts, listOf())
    }

    /** The tuple type that fits the targets of [tuple], so that `(a, b) = (1, null)` converts its parts; null when one is `_`. */
    private fun targetType(cd: CompileData, tuple: TCreateTuple): Type? {
        val parts = tuple.values.map { part ->
            when (val t = part.unnested()) {
                is TCreateTuple -> targetType(cd, t)
                is TNamed -> cd.findVar(testIdentifier(t.location, t.name)).let { (it as? NarrowedVar)?.base ?: it }?.getType()
                // Only its type is needed here: the target is compiled for real when it is assigned.
                is IAssignable -> cd.collectHoisted { t.place(cd).type }.second
                else -> null
            } ?: return null
        }
        return cd.types.tuple(parts)
    }

    override fun toString(): String {
        return "TDestructure(${pattern.pattern()}, $expr)"
    }
}
