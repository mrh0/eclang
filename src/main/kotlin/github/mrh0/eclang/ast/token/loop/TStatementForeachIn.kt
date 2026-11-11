package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.data.TCreateTuple
import github.mrh0.eclang.ast.token.statement.defineLocal
import github.mrh0.eclang.ast.token.statement.destructured
import github.mrh0.eclang.ast.token.statement.hiddenName
import github.mrh0.eclang.ast.token.function.call.callIR
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.ast.token.op.TRange
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.Narrowing
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.ir.IRIs
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempDefine
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.accessor.IRAccessorArray
import github.mrh0.eclang.ir.accessor.IRAccessorNamed
import github.mrh0.eclang.ir.branch.IRStatementIf
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.eclang.ir.loop.IRBreak
import github.mrh0.eclang.ir.loop.IRStatementForeachInRange
import github.mrh0.eclang.ir.loop.IRStatementForeachNext
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.ArrayType
import github.mrh0.ect2.FixedArray
import github.mrh0.ect2.Pointer
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `for x in values do`: x is each element of an array or String in turn; `for i, x in values do` also names the
 * index i (a Size), and `for (a, b) in values do` takes each element, a tuple, apart ([pattern]; [varName] is its text). `for i in a..<b do` counts from a up to, but not including, b, and `for i in a..=b do` includes b.
 * Any other value is looped over through functions of its type: `next(@X)` gives each element and then null, `iter(x)` gives
 * an array, String or iterator to loop over instead, and `pairs(x)` does that for `for k, v in x` (a type without it is counted).
 * `where condition` skips the rounds where condition is false (the body sees what it proves). An `else` block runs
 * when the loop ends without `break`.
 */
class TStatementForeachIn(
    location: Loc, private val indexName: String?, private val varName: String, private val iterable: ITok, private val filter: ITok?,
    private val body: ITok, private val elseBody: ITok?, private val assigned: Set<String>, private val pattern: TCreateTuple? = null,
) : NonValueTok(location), ILabeledLoop {
    override var label: String? = null

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val range = iterable.unnested() as? TRange
        return Types.None to if (range != null) range(cd, hint, range) else elements(cd, hint)
    }

    private fun range(cd: CompileData, hint: Type, range: TRange): IIR {
        val op = range.op
        if (pattern != null) throw EcError(location, "A range gives one number per round, so there is no tuple for `$varName` to take apart. Write `for i in a$op" + "b do`.")
        if (indexName != null) throw EcError(location, "A range has no index: write `for $varName in a$op" + "b do`. `for i, x in values` is for arrays and Strings.")
        val name = testIdentifier(location, varName)
        val l = cd.usable(location, range.from!!.process(cd, Types.None), "start of the range")
        val u = cd.usable(location, range.to!!.process(cd, Types.None), "end of the range")
        if (!Ops.isInteger(l.first) || !Ops.isInteger(u.first))
            throw EcError(location, "`for $varName in a$op" + "b` needs whole numbers, but got ${l.first} and ${u.first}.")
        val type = cd.types.concrete(Ops.common(cd, location, "range of `for`", l.first, u.first))
        cd.forgetAssigned(location, assigned)
        val cName = cd.localCName(name)
        return compileLoop(cd, location, hint, elseBody, label = label, compileBody = {
            cd.ctx().push()
            cd.ctx().define(location, Constant(name, type, cName, kind = "a loop variable"))
            filtered(cd, hint, listOf()).also { cd.ctx().pop() }
        }) {
            IRStatementForeachInRange(location, cName, IRType(location, type),
                cd.expect(location, l, type, "start of the range"), cd.expect(location, u, type, "end of the range"), it, range.inclusive, range.descending)
        }
    }

    /** The names of a loop over elements: the index (if counted), the value and its C name, and the pattern taking the value apart. */
    private class Names(val index: String?, val value: String, val valueC: String, val pattern: TCreateTuple?)

    private fun elements(cd: CompileData, hint: Type): IIR {
        val written = cd.usable(location, iterable.process(cd, Types.None), "`for ... in`")
        val index = indexName?.let { testIdentifier(location, it) }
        if (index != null && index == testIdentifier(location, varName)) throw EcError(location, "The index and the value of `for $indexName, $varName in` need different names.")
        // `for k, v in m`: a type with `pairs` gives the two values. Without it they are a count and an element, as for an array.
        val pairs = if (indexName != null && pattern == null) converted(cd, "pairs", written) else null
        val parts = if (pairs != null) TCreateTuple(location, listOf(TNamed(location, indexName!!), TNamed(location, varName))) else pattern
        val valueC = if (parts == null) cd.localCName(testIdentifier(location, varName)) else cd.temp()
        val names = Names(if (pairs != null) null else index, if (parts == null) testIdentifier(location, varName) else hiddenName(valueC), valueC, parts)
        return loopOver(cd, hint, names, pairs ?: written, converted = pairs != null)
    }

    /** The loop over [value]; [converted]: it is what `pairs` or `iter` gave, not what was written after `in`. */
    private fun loopOver(cd: CompileData, hint: Type, names: Names, value: Pair<Type, IIR>, converted: Boolean): IIR {
        val (written, ir) = value
        val t = Ops.base(written)
        val element = when {
            t is ArrayType && !t.isC -> t.element
            t is FixedArray -> t.element
            t === Types.String -> Types.Char
            else -> null
        }
        if (element != null) return overElements(cd, hint, names, t, element, ir, converted)
        iterator(cd, value)?.let { return overIterator(cd, hint, names, it) }
        if (!converted) converted(cd, "iter", value)?.let { return loopOver(cd, hint, names, it, converted = true) }
        throw notIterable(cd, value.first, converted)
    }

    private fun overElements(cd: CompileData, hint: Type, names: Names, t: Type, element: Type, ir: IIR, converted: Boolean): IIR {
        // The length is read on every round, so anything but a variable is evaluated once, into a temporary.
        val source = if (!converted && iterable.unnested() is TNamed) ir else cd.temp().also { cd.hoist(location, IRTempDefine(location, it, t, ir)) }.let { IRRaw(location, it) }
        cd.forgetAssigned(location, assigned)
        val indexC = names.index?.let { cd.localCName(it) } ?: cd.temp()
        return compileLoop(cd, location, hint, elseBody, label = label, compileBody = {
            // The index is always in bounds, so the element is read without a check.
            round(cd, hint, names, indexC, element, listOf(), IRAccessorArray(location, source, IRRaw(location, indexC), t, checked = false), elements = !converted)
        }) {
            IRStatementForeachInRange(location, indexC, IRType(location, Types.Size), IRNumber(location, 0, Types.Size),
                if (t is FixedArray) IRNumber(location, t.size, Types.Size) else IRAccessorNamed(location, source, "len"), it)
        }
    }

    /** A call of `next` that [overIterator] repeats, which gives [returns]: [element]s, then null. */
    private class Next(val call: IIR, val returns: Type, val element: Type)

    /** `next` for [value], which goes into a temporary (a value that isn't a pointer is copied, and the copy is what advances), or null when [value] has no `next`. */
    private fun iterator(cd: CompileData, value: Pair<Type, IIR>): Next? {
        val (t, ir) = value
        val temp = cd.temp()
        val argument = if (t is Pointer) t to IRRaw(location, temp) else cd.types.pointer(t) to IRRaw(location, "&$temp")
        val target = cd.functions.protocolCall(location, "next", argument) ?: return null
        val returns = target.type
        if (returns is Result) throw EcError(location, "`for $varName in` can't use `next` of $t, because it can fail and a `for` has nowhere to send the error. " +
            "Call it yourself: `while true do` with `val x = try next(...) ?? break`.")
        if (returns !is Union || !returns.isNullable) throw EcError(location, "`for $varName in` calls `next` until it gives null, but `next` of $t returns $returns, so the loop could never end. Make it return a `T?`.")
        // `<T>?` of an `Int?` is an `Int?` again, so a null element would look like the end.
        if (target.overload.bindings.values.any { it === returns }) throw EcError(location, "The elements of $t can be null, and `for $varName in` would take the first null for the end of `next`. " +
            "Loop over an array of them instead, e.g. `for $varName in values.toArray() do`.")
        cd.hoist(location, IRTempDefine(location, temp, t, ir))
        return Next(callIR(cd, location, "next", target), returns, cd.storable(location, cd.types.nonNull(returns), "the loop variable '$varName'"))
    }

    private fun overIterator(cd: CompileData, hint: Type, names: Names, next: Next): IIR {
        cd.forgetAssigned(location, assigned)
        val indexC = names.index?.let { cd.localCName(it) }
        val result = cd.temp()
        return compileLoop(cd, location, hint, elseBody, label = label, compileBody = {
            // A plain C `break`: the loop ended normally, so its `else` block runs.
            val end = IRStatementIf(location, listOf(IRIs(location, IRRaw(location, result), next.returns, Types.Null)), listOf(IRBreak(location)))
            round(cd, hint, names, indexC ?: "", next.element, listOf(IRTempDefine(location, result, next.returns, next.call), end),
                IRConvert(location, IRRaw(location, result), next.returns, next.element, IRConvert.Mode.NARROW), elements = false)
        }) { IRStatementForeachNext(location, indexC, it) }
    }

    /** `name(value)` for `iter` and `pairs`, with the address of a copy for a function that takes a pointer to a value; null when there is no such function. */
    private fun converted(cd: CompileData, name: String, value: Pair<Type, IIR>): Pair<Type, IIR>? {
        val (t, ir) = value
        val copy = cd.temp()
        val target = cd.functions.protocolCall(location, name, value)
            ?: (if (t is Pointer) null else cd.functions.protocolCall(location, name, cd.types.pointer(t) to IRRaw(location, "&$copy"))?.also { cd.hoist(location, IRTempDefine(location, copy, t, ir)) })
            ?: return null
        return cd.usable(location, target.type to callIR(cd, location, name, target), "the result of `$name` in `for ... in`")
    }

    private fun notIterable(cd: CompileData, written: Type, converted: Boolean): EcError {
        val t = Ops.base(written)
        return EcError(location, when {
            converted -> "`for $varName in` loops over what `iter` or `pairs` gives, but this is a $written. Return an array, a String, or a type with `fn next(x: @T): Item?`."
            t is ArrayType -> "`for $varName in` can't loop over a C array, because it has no length. Count instead: `for i in 0..<length do`."
            Ops.isInteger(t) -> "`for $varName in` needs an array, a String or a range, but got the number $t. To count, write `for $varName in 0..<n do`."
            t is Union && t.isNullable -> "`for $varName in` can't loop over a $t, because it may be null. Check it first with `if x != null do`."
            cd.functions.accepts("next", t) -> "`for $varName in` needs a `next` that takes a pointer, `fn next(x: @$t)`, or it would start over on every round."
            else -> "`for $varName in` needs an array, a String or a range (`a..<b`), but got $t. To loop over a type of your own, define `fn next(x: @$t): Item?`, " +
                "which gives the next element or null at the end, or `fn iter(x: $t)`, which gives something to loop over."
        })
    }

    /** One round: [before], then the loop variable set to [read] (and taken apart), then the body. [elements]: it is an element of the array after `in`; otherwise, a call gave it, so it may point into what is looped over (see Lifetimes). */
    private fun round(cd: CompileData, hint: Type, names: Names, indexC: String, element: Type, before: List<IIR>, read: IIR, elements: Boolean): IIR {
        cd.ctx().push()
        names.index?.let { cd.ctx().define(location, Constant(it, Types.Size, indexC, kind = "a loop variable")) }
        cd.ctx().define(location, Constant(names.value, element, names.valueC, kind = "a loop variable"))
        Lifetimes.define(cd, names.value, iterable, element, element = elements)
        val parts = names.pattern?.let { destructured(location, it, element, TNamed(location, names.value)) }?.map { (part, value) -> defineLocal(cd, location, part, value, false) }
        return filtered(cd, hint, before + IRTempDefine(location, names.valueC, element, read) + parts.orEmpty()).also { cd.ctx().pop() }
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
