package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.requireBool
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Narrowing
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRAssert
import github.mrh0.eclang.util.StringLiteral
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `assert condition` or `assert condition, "message"`: stops the program with an error naming the line (and the
 * message, or else the condition as written) when condition is false. The code after it sees what condition proves:
 * after `assert x != null`, x is not null.
 */
class TAssert(location: Loc, private val condition: ITok, private val message: StringLiteral?, private val source: String) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val ir = requireBool(cd, location, "The `assert` condition", condition.process(cd, Types.None))
        Narrowing.apply(cd, Narrowing.of(cd, condition, true))
        return Types.None to IRAssert(location, ir, message ?: StringLiteral.parse(location, "assertion failed: $source".replace("\\", "\\\\")))
    }

    override fun toString() = "TAssert($condition)"
}
