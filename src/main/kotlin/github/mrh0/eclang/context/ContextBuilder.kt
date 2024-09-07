package github.mrh0.eclang.context

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.context.state.IVar

class ContextBuilder(val contextName: String) {
    val stack: ContextStack = ContextStack()

    fun define(location: Loc, variable: IVar): IVar = stack.define(location, variable)

    fun getRaw(location: Loc, name: String): IVar? = stack.getRaw(location, name)

    fun get(location: Loc, name: String): IVar = stack.get(location, name)

    fun getIndex(location: Loc, name: String): Int = stack.getIndex(location, name)

    fun assign(location: Loc, name: String, type: EcType): Int = stack.assign(location, name, type)

    fun build(): Context {
        return Context(contextName, stack.getVars())
    }

    fun push() = stack.push()

    fun pop() = stack.pop()
}