package github.mrh0.eclang.ast.token.branch

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.ir.IRIs
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRStatementDefineVal
import github.mrh0.eclang.ir.IRStatementDefineVar
import github.mrh0.eclang.ir.IRTempDefine
import github.mrh0.eclang.ir.branch.IRStatementIf
import github.mrh0.eclang.ir.branch.IRStatementIfElse
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `if val x = value do ... else ...`: runs the body with x set to value when value is not null (value is a `T?`,
 * and x a `T`), or when value is a call that can fail and it succeeded (x is its result). Otherwise the `else`
 * block runs, if there is one. `if var` makes x a variable. value is evaluated once.
 */
class TStatementLetIf(
    location: Loc, private val varName: String, private val mutable: Boolean, private val value: ITok, private val body: ITok, private val elseBody: ITok?,
) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val name = testIdentifier(location, varName)
        val keyword = if (mutable) "var" else "val"
        val (t, ir) = value.process(cd, Types.None)
        if (t === Types.None) throw EcError(location, "This has no value, so `if $keyword $varName = ...` can't test it.")
        if (t === Types.Null) throw EcError(location, "This is always null, so the body of `if $keyword` would never run.")
        val (condition, read, type) = when {
            t is Result -> {
                if (t.value === Types.None) throw EcError(location, "This call can fail, but has no value to put in '$varName'. Use `try ... catch e do` instead.")
                val temp = cd.temp()
                cd.hoist(location, IRTempDefine(location, temp, t, ir))
                Triple(IRRaw(location, "$temp.code == 0"), IRRaw(location, "$temp.value"), cd.storable(location, t.value, "variable '$varName'"))
            }
            t is Union && t.isNullable -> {
                // The value is read twice (checked, then stored), so anything but a variable goes into a temporary.
                val source = if (value.unnested() is TNamed) ir else IRRaw(location, cd.temp().also { cd.hoist(location, IRTempDefine(location, it, t, ir)) })
                val nonNull = cd.storable(location, cd.types.nonNull(t), "variable '$varName'")
                Triple(IRIs(location, source, t, Types.Null, negated = true), IRConvert(location, source, t, nonNull, IRConvert.Mode.NARROW), nonNull)
            }
            else -> throw EcError(location, "`if $keyword` needs a value that may be null (a `T?`) or a call that can fail, but this is always a $t. " +
                "Write `$keyword $varName = ...` instead.")
        }
        cd.ctx().push()
        val cName = cd.localCName(name)
        val define = if (mutable) IRStatementDefineVar(location, cd.ctx().define(location, Variable(name, type, cName)), read)
            else IRStatementDefineVal(location, cd.ctx().define(location, Constant(name, type, cName)), read)
        Lifetimes.define(cd, name, value, type)
        val block = body.process(cd, hint).second as IRBlock
        cd.ctx().pop()
        val bodyIR = IRBlock(location, listOf(define) + block.statements, block.deferred, block.diverges)
        val elseIR = elseBody?.process(cd, hint)?.second as IRBlock?
        if (block.diverges && elseIR?.diverges == true) cd.ctx().setReturned()
        return Types.None to if (elseIR == null) IRStatementIf(location, listOf(condition), listOf(bodyIR))
            else IRStatementIfElse(location, listOf(condition), listOf(bodyIR), elseIR)
    }

    override fun toString() = "TStatementLetIf($varName, $value)"
}
