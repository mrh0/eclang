package github.mrh0.eclang.ast.token.branch

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Narrowing
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.branch.IRInlineIf
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `if (condition) a else b` as a value. Its type fits both a and b (a union when they differ). */
class TInlineIf(location: Loc, private val condition: ITok, val body: ITok, val elseBody: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        when (Narrowing.constant(cd, condition)) {
            true -> return body.process(cd, hint)
            false -> return elseBody.process(cd, hint)
            null -> {}
        }
        val conditionIR = requireBool(cd, location, "The `if` condition", condition.process(cd, Types.None))
        val noTry = "`try` can't be used inside an inline `if`; assign it to a variable first."
        val a = branch(cd, true, hint, noTry, body)
        val b = branch(cd, false, hint, noTry, elseBody)
        val types = cd.types
        val type = when {
            hint !== Types.None && types.accepts(hint, a.first) && types.accepts(hint, b.first) -> hint
            types.accepts(a.first, b.first) -> types.concrete(a.first)
            types.accepts(b.first, a.first) -> types.concrete(b.first)
            else -> types.union(types.concrete(a.first), types.concrete(b.first))
        }
        val what = "branch of inline `if`"
        return type to IRInlineIf(location, conditionIR, cd.expect(location, a, type, what), cd.expect(location, b, type, what))
    }

    private fun branch(cd: CompileData, whenTrue: Boolean, hint: Type, noTry: String, tok: ITok): Pair<Type, IIR> {
        cd.ctx().push()
        try {
            Narrowing.apply(cd, Narrowing.of(cd, condition, whenTrue))
            return cd.usable(location, cd.noHoist(noTry) { tok.process(cd, hint) }, "inline `if`")
        } finally {
            cd.ctx().pop()
        }
    }

    override fun toString(): String {
        return "TInlineIf($condition, $body, $elseBody)"
    }
}
