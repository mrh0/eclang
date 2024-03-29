package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeCallSignature

class FunctionOverride(val id: String, val argNames: Array<String>, val argTypes: Array<EcType>, val ret: EcType, val block: TBlock?) {
    fun match(location: Loc, types: Array<EcType>): Boolean {
        if(types.size != argTypes.size) return false
        for(i in types.indices) {
            if(!types[i].accepts(location, argTypes[i])) return false
        }
        return true
    }

    fun getType() = EcTypeCallSignature(argTypes, ret)

    fun isExternal() = block == null
}