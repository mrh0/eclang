package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `module "name"`: puts the whole file in module [namespace]. [TProgram][github.mrh0.eclang.ast.token.TProgram] applies it. */
class TGlobalModule(location: Loc, val namespace: String) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = Types.None to IRPass(location, "module $namespace")

    override fun toString(): String {
        return "TGlobalModule($namespace)"
    }
}
