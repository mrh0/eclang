package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.eclang.ir.IRTryCatch
import github.mrh0.eclang.ir.branch.IRWhenAssign
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `val n = try f() catch e do 0`, or with a block after `do` that must leave: the value of f() when it succeeds, and
 * otherwise the value after `do`, with `e` set to the error. Like `??` with a jump, it runs before the statement it is
 * in (an [IRTryCatch] whose temporary stays visible), and the arm's value is written into that temporary's value.
 * [value] is null for the block form, which has no value and must leave.
 */
class TTryCatchExpr(location: Loc, private val left: ITok, private val errorName: String, private val value: ITok?, private val body: ITok?) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(listOfNotNull(left, value))

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (cd.hoistProblem() != null) throw cd.needsStatement(location, "`try ... catch` used as a value", "Put it in a statement of its own: `val x = try f() catch e do 0`.")
        val (lt, lir) = left.process(cd, hint)
        if (lt !is Result) throw EcError(location, "`try ... catch` is for calls that can fail, but this is a $lt, which can't fail. Use the value directly.")
        if (lt.value === Types.None) throw EcError(location, "This call returns nothing, so `try ... catch` has no value to give. Write it as a statement of its own: `try f() catch e do ...`.")
        val type = cd.storable(location, lt.value, "`try ... catch`")
        val temp = cd.temp()
        val name = testIdentifier(location, errorName)
        cd.ctx().push()
        cd.ctx().define(location, Constant(name, lt.error, cd.localCName(name), kind = "a caught error"))
        Lifetimes.define(cd, name, left, lt.error)
        val bodyIr = cd.keepingLiterals {
            if (value != null) TBlock(location, listOf(Value(location, value, type, "$temp.value"))).process(cd, Types.None).second
            else leaving(cd, body!!)
        }
        cd.ctx().pop()
        cd.hoist(location, IRTryCatch(location, temp, lt, lir, cd.localCName(name), bodyIr))
        return type to IRRaw(location, "$temp.value")
    }

    /** The block after `do`: it has no value to give, so it must leave. */
    private fun leaving(cd: CompileData, body: ITok): IIR {
        val ir = body.process(cd, Types.None).second
        if ((ir as IRBlock).diverges || cd.functionHasErrors) return ir
        throw EcError(location, "The block after `catch e do` has no value, so it must leave with `ret`, `throw`, `break` or `continue`. " +
            "To give a value instead, write it on the same line: `val x = try f() catch e do 0`.")
    }

    override fun toString() = "TTryCatchExpr($left, $errorName, ${value ?: body})"

    /** The value given when the call fails, converted to the call's value type and stored in [target]. */
    private class Value(location: Loc, private val value: ITok, private val type: Type, private val target: String) : Tok(location) {
        override fun pointerFlow(): PointerFlow = PointerFlow.From(value)

        override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
            val v = value.process(cd, type)
            if (v.first === Types.Never) {
                cd.ctx().setReturned()
                return Types.None to IRStatement(location, v.second)
            }
            val assign = IRWhenAssign(location, target)
            assign.value = cd.expect(location, cd.usable(location, v, "the value after `catch e do`"), type, "the value after `catch e do`")
            return Types.None to assign
        }
    }
}
