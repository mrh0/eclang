package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeCallSignature

class FunctionOverride(val id: String, val params: Array<FunctionParameter>, val ret: EcType, val block: TBlock?) {
    fun match(location: Loc, types: Array<EcType>): Boolean {
        if(types.size != params.size) return false
        for(i in types.indices) {
            if(!types[i].accepts(location, params[i].type)) return false
        }
        return true
    }

    fun getType() = EcTypeCallSignature(params.map { it.type }.toTypedArray(), ret)

    fun isExternal() = block == null
}