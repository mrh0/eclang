package github.mrh0.eclang.ast.token.use

import github.mrh0.eclang.ast.*
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRDeadEnd
import github.mrh0.eclang.reflect.GsExport
import github.mrh0.eclang.reflect.Reflection
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.values.GsGlobalFunction
import github.mrh0.eclang.context.Modules
import github.mrh0.eclang.context.function.FunctionManager
import github.mrh0.eclang.context.state.GlobalFunctionReference

class TUseFromModule(location: Loc, private val names: List<String>, private val moduleRef: String) : Tok(location) {
    override fun toString() = "TUse($names from $moduleRef)"

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        val (namespace, moduleName) = Modules.parseModuleReference(location, moduleRef)
        val clazz = StandardLib.getModule(location, namespace, moduleName)

        val nameSet = names.toHashSet()
        val methods = clazz.methods.filter { it.isAnnotationPresent(GsExport::class.java) }.filter { nameSet.contains(it.name) }

        methods.forEach {
            val fos = Reflection.loadMethod(location, FunctionManager.INSTANCE, it)
            if(fos.getNumberOfOverrides() == 1)
                cd.getGlobal().define(location, GlobalFunctionReference(fos.name, fos.getType(), GsGlobalFunction(fos)))
        }

        return Pair(EcTypeNone, IRDeadEnd)
    }
}
