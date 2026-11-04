package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.context.state.NarrowedVar
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.ect2.Type

open class ContextStack {
    /**
     * One block scope. A child scope starts with copies of its parent's variables, deferred statements and
     * narrowed types, so narrowing inside an `if` body never leaks out of it.
     */
    class Scope(
        val vars: MutableMap<String, Int> = mutableMapOf(),
        val deferred: MutableList<ITok> = mutableListOf(),
        val narrowed: MutableMap<String, Type> = mutableMapOf(),
    ) {
        /** True once this scope always leaves early (ret, throw, break, continue); later code is unreachable. */
        var returned: Boolean = false
        /** True when [returned] only because a branch was picked at compile time (code after it is skipped, not an error). */
        var returnedStatically: Boolean = false
        val newDeferred: MutableList<ITok> = mutableListOf()
    }

    val vars: MutableList<IVar> = mutableListOf()
    val scopeStack: MutableList<Scope> = mutableListOf(Scope())

    /** The local memory each variable (by index in [vars]) may point into; see [Lifetimes]. */
    private val origins = HashMap<Int, Local>()

    fun define(location: Loc, variable: IVar): IVar {
        val top = scopeStack.last()
        top.vars[variable.getName()]?.let { index ->
            val name = variable.getName()
            val outer = scopeStack.size > 1 && scopeStack[scopeStack.size - 2].vars[name] == index
            val where = if (outer) "in an enclosing block" else "in this block"
            val why = if (outer) " eclang doesn't let a nested block hide (shadow) a name, so a name means the same thing everywhere in a function." else ""
            throw EcError(location, "'$name' is already defined $where, as ${vars[index].kind()}.$why Choose a different name, e.g. one that says what this value is; or, to change the existing one, assign it (`$name = ...`, which needs a `var`).")
        }
        top.vars[variable.getName()] = vars.size
        top.narrowed.remove(variable.getName())
        vars.add(variable)
        return variable
    }

    fun defer(location: Loc, tok: ITok) {
        val top = scopeStack.last()
        top.deferred.add(tok)
        top.newDeferred.add(tok)
    }

    fun getIndexRaw(name: String): Int? = scopeStack.last().vars[name]

    fun getIndex(location: Loc, name: String): Int = getIndexRaw(name) ?: throw EcNotDefinedError(location, name)

    /** The variable called [name], with its narrowed type if flow analysis narrowed it in this scope. */
    fun getRaw(name: String): IVar? {
        val top = scopeStack.last()
        val variable = top.vars[name]?.let { vars[it] } ?: return null
        return top.narrowed[name]?.let { NarrowedVar(variable, it) } ?: variable
    }

    fun get(location: Loc, name: String): IVar = getRaw(name) ?: throw EcNotDefinedError(location, name)

    /** The depth of the scope that defined the local [name] (it is freed when that scope ends), or null if it isn't local. */
    fun definedDepth(name: String): Int? {
        val index = scopeStack.last().vars[name] ?: return null
        return scopeStack.indexOfFirst { it.vars[name] == index } + 1
    }

    /** The local memory the variable [name] may point into, or null. */
    fun originOf(name: String): Local? = scopeStack.last().vars[name]?.let { origins[it] }

    /** Notes that [name] may point into [local] (keeping the memory freed first when it already may point elsewhere). */
    fun setOrigin(name: String, local: Local) {
        val index = scopeStack.last().vars[name] ?: return
        val old = origins[index]
        if (old == null || local.depth > old.depth) origins[index] = local
    }

    /** From here to the end of the current scope, [name] is known to hold a [type]. */
    fun narrow(name: String, type: Type) { scopeStack.last().narrowed[name] = type }

    /**
     * Forgets what is known about [name] (it was just assigned), in every enclosing scope too: after
     * `if v is Int do (if c do v = "s"c)` the outer body can no longer rely on v being an Int.
     */
    fun forget(name: String) {
        val index = scopeStack.last().vars[name] ?: return
        scopeStack.forEach { if (it.vars[name] == index) it.narrowed.remove(name) }
    }

    /** How many scopes are open; [deferredSince] takes such a depth. */
    fun depth(): Int = scopeStack.size

    /** The statements deferred in the scopes opened after [depth] was taken, in the order they must run (last deferred first). */
    fun deferredSince(depth: Int): List<ITok> = scopeStack.drop(depth).flatMap { it.newDeferred }.reversed()

    fun push() {
        val top = scopeStack.last()
        scopeStack.add(Scope(top.vars.toMutableMap(), top.deferred.toMutableList(), top.narrowed.toMutableMap()))
    }

    fun pop(): Scope = scopeStack.removeLastOrNull() ?: throw Error("Scope empty")

    fun setReturned(statically: Boolean = false) {
        val top = scopeStack.last()
        if (!top.returned) top.returnedStatically = statically
        top.returned = true
    }

    fun hasReturnedStatically(): Boolean = scopeStack.last().returnedStatically

    fun hasReturned(): Boolean = scopeStack.last().returned

    fun getVars() = vars.toTypedArray()

    fun getDeferred(): MutableList<ITok> = scopeStack.last().deferred

    fun getNewDeferred(): MutableList<ITok> = scopeStack.last().newDeferred
}
