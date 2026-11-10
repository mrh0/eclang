package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.data.TString
import github.mrh0.eclang.ast.token.function.TLambda
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.state.NarrowedVar
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRComma
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempDefine
import github.mrh0.eclang.ir.IRTempSet
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * Makes the values of one C expression (arguments, the parts of a `${...}`, the sides of an operator, the values of a
 * literal) run left to right, which C doesn't promise: gcc runs arguments right to left, and interleaves the sides of a `+`.
 *
 * A value goes into a temporary when something after it can run code that could change it or see what it did. Values
 * that can't be affected stay where they are, as does the last one that runs code, so most calls have no temporaries.
 * Temporaries are statements before the current one when that keeps the order: nothing in this statement ran
 * before this call, or a later value (`try`) has already moved there, and the earlier ones must get there first.
 * Otherwise, and where nothing can be hoisted, they are set in a comma expression around the call ([wrap]).
 *
 * [compile] each value, in any order; `index` is its place from the left. Then [finish] gives what to use for each
 * of them, and [wrap] the call made of those.
 */
class ArgumentOrder(private val cd: CompileData) {
    private class Entry(val tok: ITok, val value: Pair<Type, IIR>, val runsCode: Boolean, val hoists: Boolean, val hoistedUpTo: Int)

    private val entries = sortedMapOf<Int, Entry>()
    private val startsStatement = cd.nothingRanInStatement
    private val sets = mutableListOf<IIR>()

    /** Compiles the value at [index], unless it already was. */
    fun compile(index: Int, tok: ITok, compile: () -> Pair<Type, IIR>): Pair<Type, IIR> {
        entries[index]?.let { return it.value }
        val (effects, mark) = cd.effects to cd.hoistMark
        val value = compile()
        val hoists = cd.hoistedSince(mark)
        entries[index] = Entry(tok, value, cd.effects != effects || hoists, hoists, cd.hoistMark)
        return value
    }

    /** Compiles all of [values] with [compile], which gets each one's place, then gives what to use for them, as [finish] does. */
    fun inOrder(location: Loc, values: List<ITok>, compile: (Int, ITok) -> Pair<Type, IIR>): List<Pair<Type, IIR>> {
        values.forEachIndexed { i, tok -> this.compile(i, tok) { compile(i, tok) } }
        return finish(location).values.toList()
    }

    /** What to use for each compiled value, by index: the value itself, or a read of the temporary holding it. */
    fun finish(location: Loc): Map<Int, Pair<Type, IIR>> {
        val result = entries.mapValuesTo(sortedMapOf()) { it.value.value }
        val list = entries.entries.toList()
        val last = list.indexOfLast { it.value.runsCode }
        if (last < 0 || !cd.canDeclareTemps) return result
        fun hoistsAfter(i: Int) = list.drop(i + 1).any { it.value.hoists }
        val saved = list.indices.filter { i ->
            val e = list[i].value
            canHold(e.value.first) && !isFixed(e, hoistsAfter(i)) &&
                (i < last || (i == last && list.drop(i + 1).any { !isFixed(it.value, false) }))
        }
        val asStatements = cd.hoistProblem() == null && (startsStatement || list.any { it.value.hoists })
        val temps = saved.associateWith { cd.temp() }
        // Last first, so that the earlier places in the list of hoisted statements stay where they are.
        (if (asStatements) saved.reversed() else saved).forEach { i ->
            val (index, e) = list[i]
            val (type, ir) = e.value
            val temp = temps.getValue(i)
            if (asStatements) cd.hoistAt(e.hoistedUpTo, IRTempDefine(location, temp, type, ir))
            else {
                cd.declareTemp(location, temp) { type }
                sets += IRTempSet(location, temp, ir)
            }
            result[index] = type to IRRaw(location, temp)
        }
        return result
    }

    /** [call], the C call of the values from [finish], after setting the temporaries that aren't statements. */
    fun wrap(location: Loc, call: IIR): IIR = sets.foldRight(call) { set, rest -> IRComma(location, set, rest) }

    /** Whether a temporary can hold a value of type [t] just as it is. A value that can't is an error or a literal. */
    private fun canHold(t: Type) = t !== Types.None && t !== Types.Never && t !is Result && cd.types.concrete(t) === t && cd.types.storageProblem(t) == null

    /** Whether nothing that runs later can change what [e] gives: a constant, or a local no one else can reach. */
    private fun isFixed(e: Entry, hoistsAfter: Boolean): Boolean {
        if (e.runsCode) return false
        if (e.value.second.isConstant() || e.value.first === Types.Null) return true
        return when (val tok = e.tok.unnested()) {
            is TString, is TLambda -> true
            is TNamed -> isFixedVariable(cd, tok, hoistsAfter)
            else -> false
        }
    }

    companion object {
        /**
         * Whether a call can't change the variable [tok]: a local whose address isn't taken (and that no `mut` argument
         * or, with [hoistsAfter], hoisted `when` assigns), or a global that can't be assigned.
         */
        fun isFixedVariable(cd: CompileData, tok: TNamed, hoistsAfter: Boolean = false): Boolean {
            if (TNamed.isCurrentPool(cd, tok.name)) return true
            val name = testIdentifier(tok.location, tok.name)
            val v = cd.findVar(name) ?: return false
            val assignable = ((v as? NarrowedVar)?.base ?: v).canAssign()
            // A `when` hoisted from a later value can assign a local.
            return if (cd.ctx().getRaw(name) != null) name !in cd.addressTaken && name !in cd.changedByCalls && !(hoistsAfter && assignable) else !assignable
        }
    }
}
