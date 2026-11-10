package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TFixed
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.ir.IRIs
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.eclang.ir.IRTempSet
import github.mrh0.eclang.ir.branch.IRInlineIf
import github.mrh0.eclang.ir.branch.IRTailIf
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `q?.f(args)`: null when q is null, otherwise `q.f(args)` as a nullable value. q is evaluated once, and args only when q
 * isn't null. As a [statement] the call's value is dropped.
 */
class TSafeCall(
    location: Loc, val receiver: ITok, private val name: String, private val rest: List<ITok>, private val argNames: List<String?>?,
    private val statement: Boolean = false,
) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(receiver, call)

    /** The call on the receiver, once compiled (see Lifetimes). */
    var call: TExprCall? = null
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = cd.usable(location, receiver.process(cd, Types.None), "`?.$name(...)`")
        if (t !is Union || !t.isNullable) throw EcError(location, "A value of type $t is never null, so `?.` is not needed. Write `.$name(...)` instead.")
        val nonNull = cd.types.nonNull(t)
        // The value is read twice (checked, then called on), so anything but a variable goes into a temporary.
        val (first, read) = if (receiver.unnested() is TNamed) ir to ir else {
            val temp = cd.declareTemp(location) { t }
            IRTempSet(location, temp, ir) to IRRaw(location, temp)
        }
        val value = TFixed(location, nonNull, IRConvert(location, read, t, nonNull, IRConvert.Mode.NARROW), receiver)
        val tok = TExprCall(location, name, listOf<ITok>(value) + rest, isMethod = true, argNames).also { call = it }
        val (callType, callIR) = cd.noHoist("`try` can't be used in the arguments of `?.`, because they only run when the value isn't null. Assign it to a variable first.") {
            tok.process(cd, if (hint === Types.None) hint else cd.types.nonNull(hint))
        }
        if (callType is Result) throw EcError(location, "$name(...) can fail, and `?.` has no way to handle that. Check the value first (`if x != null do`), then handle the error with `try` or `??`.")
        val present = IRIs(location, first, t, Types.Null, negated = true)
        if (statement) return Types.None to IRTailIf(location, IRBlock(location, listOf(IRStatement(location, callIR)), listOf()), present)
        if (callType === Types.None) return Types.None to IRInlineIf(location, present, callIR, IRRaw(location, "(void)0"))
        val what = "`?.$name(...)`"
        val result = cd.storable(location, cd.types.union(cd.types.concrete(callType), Types.Null), what)
        return result to IRInlineIf(location, present, cd.expect(location, callType to callIR, result, what),
            cd.expect(location, Types.Null to IRRaw(location, "NULL"), result, what))
    }

    override fun toString() = "TSafeCall($receiver, $name, $rest)"
}
