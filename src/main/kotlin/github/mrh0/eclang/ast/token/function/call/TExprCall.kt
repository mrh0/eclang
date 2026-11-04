package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Module
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRCCast
import github.mrh0.eclang.ir.IREnumConvert
import github.mrh0.eclang.ir.accessor.IRAccessorNamed
import github.mrh0.eclang.ir.function.call.IRArgument
import github.mrh0.eclang.ir.function.call.IRArguments
import github.mrh0.eclang.ir.function.call.IRCallValue
import github.mrh0.eclang.ir.function.call.IRGlobalFunctionCall
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.*

/**
 * `name(args)` or `first.name(rest)` ([isMethod]). In order, this is:
 * - `module.name(rest)`: a function of another module, when `first` names a module (and no variable);
 * - `first.name(rest)` where `first` is a record with a field `name` of function type: that function is called;
 * - `name(args)` where `name` is a variable of function type: that function is called;
 * - otherwise the closest matching overload of the function `name`.
 * A call to a function that can fail has a Result type, which the caller must handle (`try`, `??`, `try ... catch`).
 */
open class TExprCall(location: Loc, val name: String, val args: List<ITok>, val isMethod: Boolean = false) : Tok(location) {
    override fun toString(): String {
        return "TCall(${name}, $args)"
    }

    /**
     * The pools the result of the last compiled call may be allocated in (null: the current pool): the arguments
     * that are pools, or those a builtin allocates in. See Lifetimes.
     */
    var pools: List<ITok?> = listOf()
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        pools = listOf()
        val fixedName = testIdentifier(location, name)

        if (isMethod) {
            val receiver = args[0]
            if (receiver is TNamed && cd.findVar(receiver.name) == null) cd.findModule(receiver.name)?.let {
                return callFunction(cd, fixedName, args.drop(1), it)
            }
            val first = receiver.process(cd, cd.functions.hintFor(fixedName, args.size, 0))
            functionField(cd, first.first, fixedName)?.let { (type, throughPointer) ->
                return callValue(cd, "field '$name'", type to IRAccessorNamed(location, first.second, fixedName, throughPointer), args.drop(1))
            }
            return callFunction(cd, fixedName, args.drop(1), null, first)
        }

        cd.findVar(fixedName)?.let { v ->
            if (v.getType().let { it is FunctionType || isNullableFunction(it) } || !cd.functions.exists(fixedName))
                return callValue(cd, "'$name'", v.getType() to v.toIR(location, cd, Types.None), args)
        }
        return callFunction(cd, fixedName, args, null)
    }

    /** Calls the function [name] with [rest], after the already compiled [first] argument of a method-style call. */
    private fun callFunction(cd: CompileData, name: String, rest: List<ITok>, qualifier: Module?, first: Pair<Type, IIR>? = null): Pair<Type, IIR> {
        if (qualifier == null && Builtins.has(name) && !cd.functions.exists(name))
            return mutableListOf<ITok?>().let { used -> Builtins.call(cd, location, name, if (first != null) args else rest, first, used).also { pools = used } }
        val count = rest.size + (if (first != null) 1 else 0)
        val offset = count - rest.size
        val processed = listOfNotNull(first) + rest.mapIndexed { i, arg -> arg.process(cd, cd.functions.hintFor(name, count, i + offset, qualifier)) }
        // A function given a pool may return memory from it; an eclang function may also allocate in the current one.
        pools = (if (first != null) args else rest).filterIndexed { i, _ -> processed[i].first === cd.types.pool }
        val target = cd.functions.call(location, name, processed, qualifier)
        if (!target.overload.isExternal) pools = pools + null
        val call = IRGlobalFunctionCall(location, name, target.overload.cName, IRArguments(location, target.args.map { IRArgument(location, it) }))
        val enum = if (target.overload.isExternal && target.overload.throws == null) cd.enums[target.type] else null
        return target.type to when {
            enum != null -> IREnumConvert(location, enum, call, toC = false)
            cd.functions.returnsConst(target.overload) -> IRCCast(location, call, target.type)
            else -> call
        }
    }

    /** Calls a function value ([callee]: its type and IR), e.g. a parameter `f: (Int) => Int`. [what] names it in errors. */
    private fun callValue(cd: CompileData, what: String, callee: Pair<Type, IIR>, rest: List<ITok>): Pair<Type, IIR> {
        val type = callee.first
        if (isNullableFunction(type))
            throw EcError(location, "Can't call $what because it may be null. Check it first, e.g. `if $name != null do`.")
        if (type !is FunctionType) throw EcError(location, "Can't call $what: it is a $type, not a function.")
        if (rest.size != type.params.size)
            throw EcError(location, "$what takes ${type.params.size} argument${if (type.params.size == 1) "" else "s"} ($type), but ${rest.size} were given.")
        val irs = rest.mapIndexed { i, arg ->
            val value = cd.usable(location, arg.process(cd, type.params[i]), "argument ${i + 1} of $what")
            cd.expect(location, value, type.params[i], "argument ${i + 1} of $what")
        }
        val result = type.throws?.let { cd.types.result(type.returns, it) } ?: type.returns
        pools = listOf(null)
        return result to IRCallValue(location, callee.second, irs)
    }

    private fun isNullableFunction(t: Type) = t is Union && t.isNullable && t.members.any { it is FunctionType }

    /** The type of field [name] of a record (or pointer to one) of type [t] when it holds a function, and whether it's read through a pointer. */
    private fun functionField(cd: CompileData, t: Type, name: String): Pair<Type, Boolean>? {
        val throughPointer = t is Pointer && t.target is Declared
        val record = (if (throughPointer) (t as Pointer).target else t) as? Declared ?: return null
        val field = record.field(name) ?: return null
        return if (field is FunctionType || isNullableFunction(field)) field to throughPointer else null
    }
}
