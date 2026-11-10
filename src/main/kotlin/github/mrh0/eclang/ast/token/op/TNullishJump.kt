package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.data.TNull
import github.mrh0.eclang.ast.token.op.compare.TEquals
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Narrowing
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.ir.IRIs
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempDefine
import github.mrh0.eclang.ir.branch.IRStatementIf
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `a ?? ret 0`, also with `throw`, `break` and `continue`: a without its null (or the value of a call that can fail), and
 * otherwise [jump] leaves. It is an `if` before the statement it is in, so after it a variable a is known not to be null.
 */
class TNullishJump(location: Loc, val left: ITok, private val jump: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(left)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (cd.hoistProblem() != null) throw cd.needsStatement(location, "`??` with a jump", "Put it in a statement of its own: `val x = a ?? ret 0`.")
        val (lt, lir) = left.process(cd, hint)
        if (lt is Result) {
            val value = cd.storable(location, lt.value, "`??`")
            val temp = cd.temp()
            cd.hoist(location, IRTempDefine(location, temp, lt, lir))
            cd.hoist(location, leaveIf(cd, IRRaw(location, "$temp.code != 0")))
            return value to IRRaw(location, "$temp.value")
        }
        if (lt !is Union || !lt.isNullable) throw EcError(location, "A value of type $lt is never null, so `??` is not needed here.")
        val read = if (left.unnested() is TNamed) lir else {
            val temp = cd.temp()
            cd.hoist(location, IRTempDefine(location, temp, lt, lir))
            IRRaw(location, temp)
        }
        cd.hoist(location, leaveIf(cd, IRIs(location, read, lt, Types.Null)))
        Narrowing.apply(cd, Narrowing.of(cd, TEquals(location, left.unnested(), TNull(location)), false))
        val nonNull = cd.types.nonNull(lt)
        return nonNull to IRConvert(location, read, lt, nonNull, IRConvert.Mode.NARROW)
    }

    /** `if (condition) jump`, in a scope of its own: the code after it does run. */
    private fun leaveIf(cd: CompileData, condition: IIR): IIR {
        cd.ctx().push()
        val (hoisted, ir) = cd.collectHoisted { jump.process(cd, Types.None).second }
        cd.ctx().pop()
        return IRStatementIf(location, listOf(condition), listOf(IRBlock(location, hoisted + ir, listOf(), diverges = true)))
    }

    override fun toString() = "TNullishJump($left, $jump)"
}

/** The missing value of `ret` on the right of `??`, in a function that returns nothing. */
class TNoValue(location: Loc) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.NotAValue

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = Types.None to IRRaw(location, "(void)0")
}
