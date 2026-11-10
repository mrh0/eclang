package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.statement.constantValue
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.state.CompileTimeConstant
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRDefineConstant
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `const NAME = value` at the top level: a compile-time constant, a C `#define`. */
class TGlobalConstant(location: Loc, private val name: String, private val expr: ITok, private val type: ITok?) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fixedName = testIdentifier(location, name)
        val (constType, value) = constantValue(cd, location, name, expr, type)
        val cName = cd.claimCName(location, cd.module.cName(fixedName), "constant '$name'")
        cd.getGlobal().define(location, CompileTimeConstant(fixedName, constType, cName, value, byName = true))
        return Types.None to IRDefineConstant(location, cName, value)
    }

    override fun toString() = "TGlobalConstant($name, $expr)"
}
