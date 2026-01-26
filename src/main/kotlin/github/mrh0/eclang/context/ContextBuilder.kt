package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.context.state.IVar

open class ContextBuilder(val contextName: String) {
    val stack: ContextStack = ContextStack()

    fun define(location: Loc, variable: IVar): IVar = stack.define(location, variable)

    fun defer(location: Loc, tok: ITok) = stack.defer(location, tok)

    fun defer(location: Loc, toks: List<ITok>) = toks.forEach { stack.defer(location, it) } 

    fun getRaw(location: Loc, name: String): IVar? = stack.getRaw(location, name)

    fun get(location: Loc, name: String): IVar = stack.get(location, name)

    fun getIndex(location: Loc, name: String): Int = stack.getIndex(location, name)

    fun assign(location: Loc, name: String, type: EcType): IVar = stack.assign(location, name, type)

    fun build(): Context {
        return Context(contextName, stack.getVars())
    }

    fun push() = stack.push()

    fun pop() = stack.pop()

    fun getDeferred() = stack.getDeferred()

    fun getNewDeferred() = stack.getNewDeferred()

    fun hasReturned() = stack.hasReturned()
    fun setReturned() = stack.setReturned()
}