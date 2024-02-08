package github.mrh0.eclang.ast.token.use

import github.mrh0.eclang.ast.*
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRDeadEnd
import github.mrh0.eclang.reflect.GsExport
import github.mrh0.eclang.reflect.Reflection
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.values.GsGlobalFunction
import github.mrh0.eclang.vm.Modules
import github.mrh0.eclang.vm.function.FunctionManager
import github.mrh0.eclang.vm.state.GlobalFunctionReference

class TUseAllFromModule(location: Loc, private val moduleRef: String) : Tok(location) {
    override fun toString() = "TUse(* from $moduleRef)"

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        val (namespace, moduleName) = Modules.parseModuleReference(location, moduleRef)
        val clazz = StandardLib.getModule(location, namespace, moduleName)
        val methods = clazz.methods.filter { it.isAnnotationPresent(GsExport::class.java) }
        methods.forEach {
            val fos = Reflection.loadMethod(location, FunctionManager.INSTANCE, it)
            if(fos.getNumberOfOverrides() == 1)
                cd.getGlobal().define(location, GlobalFunctionReference(fos.name, fos.getType(), GsGlobalFunction(fos)))
        }

        return Pair(EcTypeNone, IRDeadEnd)
    }
}
