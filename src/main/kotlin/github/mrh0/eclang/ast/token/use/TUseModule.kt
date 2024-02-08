package github.mrh0.eclang.ast.token.use

import github.mrh0.eclang.ast.*
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRDeadEnd
import github.mrh0.eclang.reflect.Reflection
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.context.function.FunctionManager

class TUseModule(location: Loc, private val module: String, private val asName: String?) : Tok(location) {
    override fun toString(): String {
        return if(asName == null) "TUse($module as $asName)" else "TUse($module)"
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        val fos = Reflection.loadClass(Loc.IDENTITY, FunctionManager.INSTANCE, "github.mrh0.goodscript.lib.GlobalKt")

        return Pair(EcTypeNone, IRDeadEnd)
    }
}
