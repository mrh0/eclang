package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempDefine
import github.mrh0.eclang.ir.branch.IRInlineIf
import github.mrh0.eclang.ir.branch.IRNullish
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `a ?? b`: a, or b when a is null. On a call that can fail it is the call's value, or b when it failed
 * (`val n = parse(s) ?? 0`). b is only evaluated when needed.
 */
class TNullishCoalescing(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (lt, lir) = left.process(cd, hint)
        if (lt is Result) {
            val value = cd.storable(location, lt.value, "`??`")
            val temp = cd.temp()
            cd.hoist(location, IRTempDefine(location, temp, lt, lir))
            val r = cd.expect(location, cd.noHoist(noTry) { right.process(cd, value) }, value, "right side of `??`")
            return value to IRInlineIf(location, IRRaw(location, "$temp.code == 0"), IRRaw(location, "$temp.value"), r)
        }
        if (lt === Types.Null) return right.process(cd, hint)
        if (lt !is Union || !lt.isNullable) throw EcError(location, "A value of type $lt is never null, so `??` is not needed here.")
        val nonNull = cd.types.nonNull(lt)
        val r = cd.noHoist(noTry) { right.process(cd, nonNull) }
        // The result is the non-null type when the default fits it (`name ?? "anonymous"c` is a CString).
        val resultType = if (cd.types.accepts(nonNull, r.first)) nonNull else cd.storable(location, cd.types.union(nonNull, r.first), "`??`")
        val rir = cd.expect(location, r, resultType, "right side of `??`")
        // The left side is read twice, so anything but a plain variable goes into a temporary first.
        val leftRead = if (left is TNamed) lir else {
            val temp = cd.temp()
            cd.hoist(location, IRTempDefine(location, temp, lt, lir))
            IRRaw(location, temp)
        }
        return resultType to IRNullish(location, leftRead, lt, rir, resultType)
    }

    override fun toString(): String {
        return "TNullishCoalescing($left, $right)"
    }

    private val noTry = "`try` can't be used on the right side of `??`; assign it to a variable first."
}
