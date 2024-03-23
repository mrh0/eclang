package github.mrh0.eclang.ast

import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.ContextBuilder
import github.mrh0.eclang.context.state.IVar

class CompileData {
    val contextMap: MutableMap<String, Context> = mutableMapOf()
    private val global = ContextBuilder("__GLOBAL__")
    var currentContextBuilder: ContextBuilder = global

    fun getVar(location: Loc, name: String): IVar {
        val ivar = ctx().getRaw(location, name)
        return if(ivar != null) {
            ivar
        } else {
            val gvar = getGlobal().get(location, name)
            ctx().define(location, gvar)
            gvar
        }
    }

    fun getFunctionVarIndex(location: Loc, name: String, args: Array<EcType>): Pair<Int, IVar> {
        val ivar = ctx().getRaw(location, name)
        return if(ivar != null) {
            ctx().getIndex(location, name) to ivar
        } else {
            TODO("Implement global functions")
            //val gvar = getGlobal().get(location, name)
            //if(gvar !is GlobalFunctionReference) throw EcError(location, "Defined global $name is not a function")
            //val fn = gvar.ref.getFunction(location, args)

            //val lvar = Variable("$name:anonymous", EcTypeCallSignature(args, fn.getReturnType()), fn)
            //val definedIndex = ctx().defineAnonymous(location, lvar)
            //definedIndex to lvar
        }
    }

    fun newContext(contextName: String): ContextBuilder {
        contextMap[currentContextBuilder.contextName] = currentContextBuilder.build()
        currentContextBuilder = ContextBuilder(contextName)
        return currentContextBuilder
    }

    fun ctx() = currentContextBuilder
    fun getGlobal() = global

    fun finalize(): Context {
        contextMap[currentContextBuilder.contextName] = currentContextBuilder.build()
        return global.build()
    }
}