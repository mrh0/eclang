package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.function.TLambda
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.CallTarget
import github.mrh0.eclang.context.Module
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRCCast
import github.mrh0.eclang.ir.IRCNotNull
import github.mrh0.eclang.ir.IREnumConvert
import github.mrh0.eclang.ir.accessor.IRAccessorNamed
import github.mrh0.eclang.ir.function.call.IRArgument
import github.mrh0.eclang.ir.function.call.IRArguments
import github.mrh0.eclang.ir.function.call.IRCallValue
import github.mrh0.eclang.ir.function.call.IRGlobalFunctionCall
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.ect2.*

/**
 * `name(args)` or `first.name(rest)` ([isMethod]). In order, this is:
 * - `module.name(rest)`: a function of another module, when `first` names a module (and no variable);
 * - `first.name(rest)` where `first` is a record with a field `name` of function type: that function is called;
 * - `name(args)` where `name` is a variable of function type: that function is called;
 * - otherwise the closest matching overload of the function `name`.
 * [argNames]: for each argument after the receiver, the parameter it is for (null: the next one in order), when any has one.
 * A call to a function that can fail has a Result type, which the caller must handle (`try`, `??`, `try ... catch`).
 */
open class TExprCall(location: Loc, val name: String, val args: List<ITok>, val isMethod: Boolean = false, val argNames: List<String?>? = null) : Tok(location) {
    override fun toString(): String {
        return "TCall(${name}, $args)"
    }

    /**
     * The pools the result of the last compiled call may be allocated in (null: the current pool): the arguments
     * that are pools, or those a builtin allocates in. See Lifetimes.
     */
    var pools: List<ITok?> = listOf()
        private set

    /**
     * The arguments the result of the last compiled call may point into (a function may return a pointer it was
     * given): all of them, except for builtins, which return new memory (but `clone` keeps what it can't copy), and for C functions given a pool, which
     * allocate their result in it. See Lifetimes.
     */
    var aliased: List<ITok> = listOf()
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        pools = listOf()
        aliased = listOf()
        val fixedName = testIdentifier(location, name)

        if (isMethod) {
            val receiver = args[0]
            if (receiver is TNamed && cd.findVar(receiver.name) == null) cd.findModule(receiver.name)?.let {
                return callFunction(cd, fixedName, args.drop(1), it, hint = hint)
            }
            val first = receiver.process(cd, cd.functions.hintFor(fixedName, args.size, 0))
            functionField(cd, first.first, fixedName)?.let { (type, throughPointer) ->
                return callValue(cd, "field '$name'", type to IRAccessorNamed(location, first.second, fixedName, throughPointer), args.drop(1))
            }
            return callFunction(cd, fixedName, args.drop(1), null, first, hint)
        }

        cd.findVar(fixedName)?.let { v ->
            if (v.getType().let { it is FunctionType || isNullableFunction(it) } || !cd.functions.exists(fixedName))
                return callValue(cd, "'$name'", v.getType() to v.toIR(location, cd, Types.None), args)
        }
        return callFunction(cd, fixedName, args, null, hint = hint)
    }

    /** Calls the function [name] with [rest], after the already compiled [first] argument of a method-style call. */
    private fun callFunction(cd: CompileData, name: String, rest: List<ITok>, qualifier: Module?, first: Pair<Type, IIR>? = null, hint: Type = Types.None): Pair<Type, IIR> {
        if (qualifier == null && Builtins.has(name) && !cd.functions.exists(name)) {
            if (argNames != null) throw EcError(location, "`$name` is built in, and its arguments have no names. Pass them in order.")
            val (used, kept) = mutableListOf<ITok?>() to mutableListOf<ITok>()
            return Builtins.call(cd, location, name, if (first != null) args else rest, first, used, kept).also { pools = used; aliased = kept }
        }
        val count = rest.size + (if (first != null) 1 else 0)
        val offset = count - rest.size
        val slots = arrayOfNulls<Pair<Type, IIR>>(count)
        first?.let { slots[0] = it }
        val names = argNames?.map { n -> n?.let { testIdentifier(location, it) } }
        fun hint(i: Int) = names?.get(i)?.let { cd.functions.hintForName(name, it, qualifier) } ?: cd.functions.hintFor(name, count, i + offset, qualifier)
        // Lambdas without parameter types last: the other arguments decide a generic function's `<T>`, which gives them their types.
        rest.forEachIndexed { i, arg -> if (!isUntypedLambda(arg)) slots[i + offset] = arg.process(cd, hint(i)) }
        rest.forEachIndexed { i, arg ->
            if (slots[i + offset] == null) {
                val fixed = hint(i)
                slots[i + offset] = arg.process(cd, if (fixed !== Types.None || names?.get(i) != null) fixed else cd.functions.lambdaHint(name, count, i + offset, slots.map { it?.first }, qualifier))
            }
        }
        val processed = slots.map { it!! }
        // `hash(x)` of a type without a function of its own is generated, like `==`.
        if (qualifier == null && name == "hash" && names == null && count == 1 && !cd.functions.accepts(name, cd.usable(location, processed[0], "the argument of `hash`").first))
            return Builtins.call(cd, location, name, if (first != null) args else rest, processed[0], mutableListOf(), mutableListOf())
        // A function given a pool may return memory from it; an eclang function may also allocate in the current one.
        pools = (if (first != null) args else rest).filterIndexed { i, _ -> processed[i].first === cd.types.pool }
        val target = cd.functions.call(location, name, processed, qualifier, hint, names?.let { List<String?>(offset) { null } + it })
        // A number can't point anywhere: `list(values.len)` has nothing to do with the memory of `values`.
        aliased = if (target.overload.isExternal && pools.isNotEmpty()) listOf()
            else (if (first != null) args else rest).filterIndexed { i, _ -> processed[i].first.let { !Ops.isNumeric(it) && it !== Types.Bool } }
        if (!target.overload.isExternal) pools = pools + null
        return target.type to callIR(cd, location, name, target)
    }

    private fun isUntypedLambda(arg: ITok) = (arg.unnested() as? TLambda)?.hasUntypedParameters == true

    /** Calls a function value ([callee]: its type and IR), e.g. a parameter `f: (Int) => Int`. [what] names it in errors. */
    private fun callValue(cd: CompileData, what: String, callee: Pair<Type, IIR>, rest: List<ITok>): Pair<Type, IIR> {
        val type = callee.first
        if (argNames != null) throw EcError(location, "A function type doesn't name its parameters, so the arguments of $what can't have names. Pass them in order.")
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
        aliased = rest
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

/** The C call of [target], a resolved call of the function [name]: what comes back from C is checked and converted to eclang's type. */
internal fun callIR(cd: CompileData, location: Loc, name: String, target: CallTarget): IIR {
    val call = IRGlobalFunctionCall(location, name, target.overload.cName, IRArguments(location, target.args.map { IRArgument(location, it) }))
    val enum = if (target.overload.isExternal && target.overload.throws == null) cd.enums[target.type] else null
    val checked = if (target.overload.isExternal && target.overload.throws == null && mayBeNullInC(target.type))
        IRCNotNull(location, call, target.type, target.overload.cName) else call
    return when {
        enum != null -> IREnumConvert(location, enum, call, toC = false)
        checked !== call -> checked
        cd.functions.returnsConst(target.overload) -> IRCCast(location, call, target.type)
        else -> call
    }
}

/** A C pointer result that eclang treats as never null (a pointer, CString or C array, not `T?`), so it's checked. */
private fun mayBeNullInC(t: Type) = t is Pointer || t === Types.CString || (t is ArrayType && t.isC)
