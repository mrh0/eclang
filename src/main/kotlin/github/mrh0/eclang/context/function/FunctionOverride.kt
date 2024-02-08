package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeCallSignature

class FunctionOverride(val name: String, val argNames: Array<String>, val argTypes: Array<EcTypeAny>, val ret: EcTypeAny, val callable: ICallable) {
    fun match(location: Loc, types: Array<EcTypeAny>): Boolean {
        if(types.size != argTypes.size) return false
        for(i in types.indices) {
            if(!types[i].accepts(location, argTypes[i])) return false
        }
        return true
    }

    fun getType() = EcTypeCallSignature(argTypes, ret)
}