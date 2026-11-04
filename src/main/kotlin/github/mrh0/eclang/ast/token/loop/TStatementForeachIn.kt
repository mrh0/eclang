package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.ast.token.op.TRange
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Narrowing
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempDefine
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.accessor.IRAccessorArray
import github.mrh0.eclang.ir.accessor.IRAccessorNamed
import github.mrh0.eclang.ir.branch.IRStatementIf
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.eclang.ir.loop.IRStatementForeachInRange
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.ArrayType
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `for x in values do`: x is each element of an array or String in turn; `for i, x in values do` also names the
 * index i (a Size). `for i in a..<b do` counts from a up to, but not including, b, and `for i in a..=b do` includes b.
 * `where condition` skips the rounds where condition is false (the body sees what it proves). An `else` block runs
 * when the loop ends without `break`.
 */
class TStatementForeachIn(
    location: Loc, private val indexName: String?, private val varName: String, private val iterable: ITok, private val filter: ITok?,
    private val body: ITok, private val elseBody: ITok?, private val assigned: Set<String>,
) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val range = iterable.unnested() as? TRange
        return Types.None to if (range != null) range(cd, hint, range) else elements(cd, hint)
    }

    private fun range(cd: CompileData, hint: Type, range: TRange): IIR {
        val op = if (range.inclusive) "..=" else "..<"
        if (indexName != null) throw EcError(location, "A range has no index: write `for $varName in a$op" + "b do`. `for i, x in values` is for arrays and Strings.")
        val name = testIdentifier(location, varName)
        val l = cd.usable(location, range.from.process(cd, Types.None), "start of the range")
        val u = cd.usable(location, range.to.process(cd, Types.None), "end of the range")
        if (!Ops.isInteger(l.first) || !Ops.isInteger(u.first))
            throw EcError(location, "`for $varName in a$op" + "b` needs whole numbers, but got ${l.first} and ${u.first}.")
        val type = cd.types.concrete(Ops.common(cd, location, "range of `for`", l.first, u.first))
        cd.forgetAssigned(location, assigned)
        val cName = cd.localCName(name)
        return compileLoop(cd, location, hint, elseBody, compileBody = {
            cd.ctx().push()
            cd.ctx().define(location, Constant(name, type, cName, kind = "a loop variable"))
            filtered(cd, hint, listOf()).also { cd.ctx().pop() }
        }) {
            IRStatementForeachInRange(location, cName, IRType(location, type),
                cd.expect(location, l, type, "start of the range"), cd.expect(location, u, type, "end of the range"), it, range.inclusive)
        }
    }

    private fun elements(cd: CompileData, hint: Type): IIR {
        val name = testIdentifier(location, varName)
        val index = indexName?.let { testIdentifier(location, it) }
        if (index == name) throw EcError(location, "The index and the value of `for $indexName, $varName in` need different names.")
        val (t, ir) = cd.usable(location, iterable.process(cd, Types.None), "`for ... in`")
        val element = when {
            t is ArrayType && !t.isC -> t.element
            t === Types.String -> Types.Char
            t is ArrayType -> throw EcError(location, "`for $varName in` can't loop over a C array, because it has no length. Count instead: `for i in 0..<length do`.")
            Ops.isInteger(t) -> throw EcError(location, "`for $varName in` needs an array, a String or a range, but got the number $t. To count, write `for $varName in 0..<n do`.")
            else -> throw EcError(location, "`for $varName in` needs an array, a String or a range (`a..<b`), but got $t.")
        }
        // The length is read on every round, so anything but a variable is evaluated once, into a temporary.
        val source = if (iterable.unnested() is TNamed) ir else cd.temp().also { cd.hoist(location, IRTempDefine(location, it, t, ir)) }.let { IRRaw(location, it) }
        cd.forgetAssigned(location, assigned)
        val indexC = index?.let { cd.localCName(it) } ?: cd.temp()
        val valueC = cd.localCName(name)
        return compileLoop(cd, location, hint, elseBody, compileBody = {
            cd.ctx().push()
            index?.let { cd.ctx().define(location, Constant(it, Types.Size, indexC, kind = "a loop variable")) }
            cd.ctx().define(location, Constant(name, element, valueC, kind = "a loop variable"))
            // The index is always in bounds, so the element is read without a check.
            val read = IRTempDefine(location, valueC, element, IRAccessorArray(location, source, IRRaw(location, indexC), t, checked = false))
            filtered(cd, hint, listOf(read)).also { cd.ctx().pop() }
        }) {
            IRStatementForeachInRange(location, indexC, IRType(location, Types.Size), IRNumber(location, 0, Types.Size),
                IRAccessorNamed(location, source, "len"), it)
        }
    }

    /** The body after [prelude] (defining the loop variable), inside `if (filter)` for `where filter`. */
    private fun filtered(cd: CompileData, hint: Type, prelude: List<IIR>): IIR {
        if (filter == null) return (body.process(cd, hint).second as IRBlock).let { IRBlock(location, prelude + it.statements, it.deferred, it.diverges) }
        val condition = requireBool(cd, location, "The `where` condition",
            cd.noHoist("`try` can't be used in a `where` condition, because it runs on every round. Call it inside the loop instead.") { filter.process(cd, Types.None) })
        cd.ctx().push()
        Narrowing.apply(cd, Narrowing.of(cd, filter, true))
        val inner = body.process(cd, hint).second
        cd.ctx().pop()
        return IRBlock(location, prelude + IRStatementIf(location, listOf(condition), listOf(inner)), listOf())
    }

    override fun toString(): String {
        return "TStatementForeachIn(${indexName?.let { "$it, " } ?: ""}$varName, $iterable)"
    }
}
