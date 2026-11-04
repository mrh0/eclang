package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.state.IVar
import github.mrh0.ect2.Type

open class ContextBuilder(val contextName: String) {
    val stack: ContextStack = ContextStack()

    fun define(location: Loc, variable: IVar): IVar = stack.define(location, variable)

    fun defer(location: Loc, tok: ITok) = stack.defer(location, tok)

    fun defer(location: Loc, toks: List<ITok>) = toks.forEach { stack.defer(location, it) }

    fun getRaw(name: String): IVar? = stack.getRaw(name)

    fun get(location: Loc, name: String): IVar = stack.get(location, name)

    fun getIndexRaw(name: String): Int? = stack.getIndexRaw(name)

    fun getIndex(location: Loc, name: String): Int = stack.getIndex(location, name)

    fun narrow(name: String, type: Type) = stack.narrow(name, type)

    fun forget(name: String) = stack.forget(name)

    fun depth() = stack.depth()

    fun deferredSince(depth: Int) = stack.deferredSince(depth)

    fun build(): Context {
        return Context(contextName, stack.getVars())
    }

    fun push() = stack.push()

    fun pop() = stack.pop()

    fun getDeferred() = stack.getDeferred()

    fun getNewDeferred() = stack.getNewDeferred()

    fun hasReturned() = stack.hasReturned()
    fun setReturned(statically: Boolean = false) = stack.setReturned(statically)
    fun hasReturnedStatically() = stack.hasReturnedStatically()
}
