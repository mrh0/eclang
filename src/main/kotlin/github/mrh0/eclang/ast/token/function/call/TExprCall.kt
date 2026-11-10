package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.token.op.OUT_PARAMETER_ONLY
import github.mrh0.eclang.ast.token.op.TAddressOf
import github.mrh0.eclang.ast.token.op.TReadPointer
import github.mrh0.eclang.ir.mayBeNullInC
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.function.TLambda
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.ArgumentOrder
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
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.function.Overload
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.ect2.*

/**
 * `name(args)` or `first.name(rest)` ([isMethod]). In order, this is:
 * - `module.name(rest)`: a function of another module, when `first` names a module (and no variable);
 * - `first.name(rest)` where `first` is a record with a field `name` of function type: that function is called;
 * - `name(args)` where `name` is a variable of function type: that function is called;
 * - otherwise the closest matching overload of the function `name`. A method-style call also finds the functions of the
 *   module that declares `first`'s type, when no function visible here takes it.
 * [argNames]: for each argument after the receiver, the parameter it is for (null: the next one in order), when any has one.
 * A call to a function that can fail has a Result type, which the caller must handle (`try`, `??`, `try ... catch`).
 */
open class TExprCall(location: Loc, val name: String, val args: List<ITok>, val isMethod: Boolean = false, val argNames: List<String?>? = null) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(aliased)

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

    /** The arguments a store through the result of the last compiled call may write into (see Lifetimes.storedInto). */
    var storedInto: List<ITok> = listOf()
        private set

    /** For each of [aliased], the callee's parameter it was given to (null: not known, or a C function's). */
    var aliasedParams: List<String?> = listOf()
        private set

    /** The callee of the last compiled call, when it is an eclang function by name (see Escapes.key), for Lifetimes' [Via]. */
    var calleeKey: String? = null
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        pools = listOf()
        aliased = listOf()
        storedInto = listOf()
        aliasedParams = listOf()
        calleeKey = null
        val fixedName = testIdentifier(location, name)
        val order = ArgumentOrder(cd)

        if (isMethod) {
            val receiver = args[0]
            if (receiver is TNamed && cd.findVar(receiver.name) == null) cd.findModule(receiver.name)?.let {
                return callFunction(cd, order, fixedName, args.drop(1), it, hint = hint)
            }
            // `x.f()` passes the address of x when f's first parameter is `mut` (and no f takes x as it is). That is decided
            // before x is compiled, from its declared type, as its address must be taken when it is compiled: ArgumentOrder
            // may keep the value in a temporary, and a pointer to that would change the temporary.
            var addressed = false
            val first = order.compile(0, receiver) {
                val type = if (cd.functions.hasMutReceiver(fixedName)) MutPlace.typeOf(cd, receiver) else null
                if (type != null && functionField(cd, type, fixedName) == null && cd.functions.receiverByAddress(fixedName, type, args.size)) {
                    addressed = true
                    checkNarrowedInLoop(cd, receiver)
                    MutPlace.address(cd, location, receiver, "`${MutPlace.shown(receiver)}.$name(...)`, whose first parameter is `mut`,")
                } else receiver.process(cd, cd.functions.hintFor(fixedName, args.size, 0))
            }
            if (!addressed) functionField(cd, first.first, fixedName)?.let { (type, throughPointer) ->
                return callValue(cd, order, "field '$name'", type, args.drop(1), receiver) { values -> IRAccessorNamed(location, values.getValue(0).second, fieldPathIn(first.first, fixedName), throughPointer) }
            }
            return callFunction(cd, order, fixedName, args.drop(1), cd.functions.methodModule(fixedName, first.first, args.size, addressed), first, hint, addressed)
        }

        cd.findVar(fixedName)?.let { v ->
            if (v.getType().let { it is FunctionType || isNullableFunction(it) } || !cd.functions.exists(fixedName))
                return callValue(cd, order, "'$name'", v.getType(), args, TNamed(location, name)) { v.toIR(location, cd, Types.None) }
        }
        return callFunction(cd, order, fixedName, args, null, hint = hint)
    }

    /**
     * A loop forgets the narrowing of what its body changes when it starts (`CompileData.forgetAssigned`), as the next round
     * runs after the change; the Visitor finds `x = ...` and `f(mut x)` for it. Whether `x.f()` changes x is only known now, after
     * the code before it in the loop may have relied on a check of x made before the loop, so that is an error.
     */
    private fun checkNarrowedInLoop(cd: CompileData, receiver: ITok) {
        if (cd.loopDepth == 0) return
        val r = receiver.unnested()
        val narrowed = when {
            r is TNamed -> cd.findVar(testIdentifier(r.location, r.name)) is github.mrh0.eclang.context.state.NarrowedVar
            r is TReadPointer && r.mutParameter -> (r.expr as? TNamed)?.let { cd.ctx().stack.narrowedType("@" + testIdentifier(it.location, it.name)) } != null
            else -> false
        }
        if (!narrowed) return
        val shown = MutPlace.shown(receiver)
        throw EcError(location, "`$shown.$name(...)` changes '$shown' (the first parameter of '$name' is `mut`) inside a loop, where a check before the loop narrowed its type: in the loop's next round, that check no longer holds. " +
            "Write it as `$name(mut $shown, ...)`, which tells the loop that it changes '$shown', and check '$shown' inside the loop where that is needed.")
    }

    /** Calls the function [name] with [rest], after the already compiled [first] argument of a method-style call. */
    private fun callFunction(cd: CompileData, order: ArgumentOrder, name: String, rest: List<ITok>, qualifier: Module?, first: Pair<Type, IIR>? = null, hint: Type = Types.None,
                             addressed: Boolean = false): Pair<Type, IIR> {
        if (qualifier == null && Builtins.has(name) && !cd.functions.exists(name)) {
            rest.firstOrNull { it.unnested() is TMutArg }?.let { throw EcError((it.unnested() as TMutArg).location, "`$name` is built in, and takes values: none of its parameters is `mut`. Remove `mut`.") }
            if (argNames != null) throw EcError(location, "`$name` is built in, and its arguments have no names. Pass them in order.")
            val (used, kept) = mutableListOf<ITok?>() to mutableListOf<ITok>()
            return Builtins.call(cd, order, location, name, if (first != null) args else rest, first, used, kept).also { pools = used; aliased = kept; storedInto = kept }
        }
        rest.forEach { (it.unnested() as? TAddressOf)?.isArgument = true }
        val count = rest.size + (if (first != null) 1 else 0)
        val offset = count - rest.size
        val names = argNames?.map { n -> n?.let { testIdentifier(location, it) } }
        fun hint(i: Int) = names?.get(i)?.let { cd.functions.hintForName(name, it, qualifier) } ?: cd.functions.hintFor(name, count, i + offset, qualifier)
        // Lambdas without parameter or return types last: the other arguments decide a generic function's `<T>`, which gives them their types.
        val types = arrayOfNulls<Type>(count).also { if (first != null) it[0] = first.first }
        rest.forEachIndexed { i, arg -> if (!isUntypedLambda(arg)) types[i + offset] = order.compile(i + offset, arg) { arg.process(cd, hint(i)) }.first }
        rest.forEachIndexed { i, arg ->
            if (types[i + offset] == null) order.compile(i + offset, arg) {
                val fixed = hint(i)
                arg.process(cd, if (fixed !== Types.None || names?.get(i) != null) fixed else cd.functions.lambdaHint(name, count, i + offset, types.toList(), qualifier))
            }.also { types[i + offset] = it.first }
        }
        val processed = order.finish(location).values.toList()
        // `hash(x)` of a type without a function of its own is generated, like `==`.
        if (qualifier == null && name == "hash" && names == null && count == 1 && !cd.functions.accepts(name, cd.usable(location, processed[0], "the argument of `hash`").first))
            return Builtins.call(cd, ArgumentOrder(cd), location, name, if (first != null) args else rest, processed[0], mutableListOf(), mutableListOf())
        // A function given a pool may return memory from it; an eclang function may also allocate in the current one.
        pools = (if (first != null) args else rest).filterIndexed { i, _ -> processed[i].first === cd.types.pool }
        val mutable = List(count) { i -> if (i < offset) addressed else rest[i - offset].unnested() is TMutArg }
        val target = cd.functions.call(location, name, processed, qualifier, hint, names?.let { List<String?>(offset) { null } + it },
            mutable.takeIf { it.any { m -> m } }, receiver = first != null)
        if (!target.overload.isExternal) rest.firstOrNull { (it.unnested() as? TAddressOf)?.isOutParameter == true }?.let {
            throw EcError((it as TAddressOf).location, "$OUT_PARAMETER_ONLY '$name' is an eclang function.")
        }
        // A number can't point anywhere: `list(values.len)` has nothing to do with the memory of `values`. Nor does the result of an
        // eclang function point into an argument its body never returns into, once a compilation has found that (Escapes.resultRoots);
        // not with `mut` arguments, as what the call may store in them is followed as its result (followChanges).
        val given = if (first != null) args else rest
        val fullNames = names?.let { List<String?>(offset) { null } + it }
        val params = given.indices.map { i -> fullNames?.getOrNull(i) ?: target.overload.declared.getOrNull(i) }
        val roots = if (target.overload.isExternal || mutable.any { it }) null else cd.escapes.resultRoots(target.overload)
        val kept = if (target.overload.isExternal && pools.isNotEmpty()) listOf()
            else given.indices.filter { i -> processed[i].first.let { !Ops.isNumeric(it) && it !== Types.Bool } && (roots == null || params[i]?.let { it in roots } != false) }
        aliased = kept.map { given[it] }
        if (!target.overload.isExternal) {
            aliasedParams = kept.map { params[it] }
            calleeKey = github.mrh0.eclang.context.Escapes.key(target.overload)
        }
        storedInto = Lifetimes.storedInto(if (first != null) args else rest, processed.map { it.first }, target.type).filter { a -> aliased.any { it === a } }
        // What may allocate in the current pool (see LoopPools): an eclang function that does, a C function given that pool.
        if (!target.overload.isExternal) target.overload.body?.let { cd.noteAllocation(location, "the call of '$name'", it) }
        else if (pools.any { it != null && Lifetimes.poolId(cd, it) == Lifetimes.CALLER_POOL }) cd.noteAllocation(location, "the call of '$name', which is given the pool")
        if (!target.overload.isExternal) pools = pools + null
        val changed = (if (first != null) args else rest).filterIndexed { i, _ -> mutable[i] }.map { (it.unnested() as? TMutArg)?.expr ?: it }
        if (changed.isNotEmpty()) followChanges(cd, name, changed, processed.filterIndexed { i, _ -> mutable[i] }.map { (it.first as Pointer).target })
        noteCall(cd, name, target.overload, given, fullNames, mutable, processed.map { it.first })
        return target.type to order.wrap(location, callIR(cd, location, name, target))
    }

    /**
     * Tells Lifetimes which parameter of [callee] each of [given] (named [names], `mut` where [mutable]) is for, so that what
     * the callee keeps of them is checked (see Escapes). Arguments in order fill the declared parameters; extra ones aren't followed.
     */
    private fun noteCall(cd: CompileData, name: String, callee: Overload, given: List<ITok>, names: List<String?>?, mutable: List<Boolean>, types: List<Type>) {
        val args = given.indices.mapNotNull { i ->
            val param = names?.getOrNull(i) ?: callee.declared.getOrNull(i) ?: return@mapNotNull null
            val type = if (mutable[i]) (types[i] as? Pointer)?.target ?: types[i] else types[i]
            Triple(param, (given[i].unnested() as? TMutArg)?.expr ?: given[i], mutable[i]) to type
        }
        args.forEach { (arg, type) ->
            if (callee.params.any { it.name == arg.first && it.isEscaping }) Lifetimes.checkEscaping(cd, location, name, arg.first, arg.second, type, followed = callee.body != null)
            if (callee.params.any { it.name == arg.first && it.isOwn }) Lifetimes.giveAway(cd, location, name, arg.first, arg.second)
            callee.params.firstOrNull { it.name == arg.first && it.isFlat }?.let { github.mrh0.eclang.context.Sharing.checkArgument(cd, location, name, it, arg.second, type) }
        }
        if (callee.body == null && callee.params.none { it.isKept }) return
        Lifetimes.noteCall(cd, location, name, callee, args.map { it.first }, args.map { it.second })
    }

    /**
     * After a call that changes the places [changed] (`mut x`, or the receiver of `x.f()`), of the types [types]: no place may be
     * given twice, and each may now hold what the call's result could point to, its other arguments and the pools it allocates in.
     */
    private fun followChanges(cd: CompileData, name: String, changed: List<ITok>, types: List<Type>) {
        val paths = changed.map { MutPlace.path(it) }
        for (i in paths.indices) for (j in i + 1 until paths.size) {
            val (a, b) = paths[i] to paths[j]
            if (a == null || b == null || !(a == b || a.startsWith("$b.") || b.startsWith("$a."))) continue
            throw EcError(location, "'$name' is given '${if (a.length <= b.length) a else b}' twice to change (`mut $a` and `mut $b`). A function can't be given one variable twice to change, as a change through one would undo the other. Pass a copy for one of them, and copy it back after the call.")
        }
        changed.forEachIndexed { i, place ->
            val shown = MutPlace.shown(place)
            try {
                Lifetimes.assign(cd, location, place, this, types[i], "'$shown'")
            } catch (e: EcError) {
                throw EcError(e.location, "'$name' changes '$shown' (`mut $shown`), and may store in it what its other arguments point to. ${e.reason}")
            }
        }
    }

    private fun isUntypedLambda(arg: ITok) = (arg.unnested() as? TLambda)?.wantsExpectedType == true

    /**
     * Calls a function value of type [type], e.g. a parameter `f: (Int) => Int`. [what] names it in errors. [callee]
     * gives its IR, from what [order] holds when that is a field of the receiver at index 0. [holder] is the variable or
     * record the function value is read from: its result may also point into what the closure captured.
     */
    private fun callValue(cd: CompileData, order: ArgumentOrder, what: String, type: Type, rest: List<ITok>, holder: ITok, callee: (Map<Int, Pair<Type, IIR>>) -> IIR): Pair<Type, IIR> {
        if (argNames != null) throw EcError(location, "A function type doesn't name its parameters, so the arguments of $what can't have names. Pass them in order.")
        rest.firstOrNull { it.unnested() is TMutArg }?.let { throw EcError((it.unnested() as TMutArg).location, "$what is a function value, whose parameters are plain values, so it can't take `mut`. Pass a pointer instead (`addrof x`), or call a function by name.") }
        if (isNullableFunction(type))
            throw EcError(location, "Can't call $what because it may be null. Check it first, e.g. `if $name != null do`.")
        if (type !is FunctionType) throw EcError(location, "Can't call $what: it is a $type, not a function.")
        if (rest.size != type.params.size)
            throw EcError(location, "$what takes ${type.params.size} argument${if (type.params.size == 1) "" else "s"} ($type), but ${rest.size} were given.")
        rest.forEachIndexed { i, arg -> order.compile(i + 1, arg) { arg.process(cd, type.params[i]) } }
        val values = order.finish(location)
        val irs = rest.indices.map { i ->
            val value = cd.usable(location, values.getValue(i + 1), "argument ${i + 1} of $what")
            cd.expect(location, value, type.params[i], "argument ${i + 1} of $what")
        }
        val result = type.throws?.let { cd.types.result(type.returns, it) } ?: type.returns
        pools = listOf(null)
        aliased = listOf(holder) + rest
        storedInto = Lifetimes.storedInto(rest, type.params, result)
        Lifetimes.noteValueCall(cd, location, what, type, rest)
        cd.noteEffect()
        return result to order.wrap(location, IRCallValue(location, callee(values), irs, type))
    }

    private fun isNullableFunction(t: Type) = t is Union && t.isNullable && t.members.any { it is FunctionType }

    /** The type of field [name] of a record (or pointer to one) of type [t] when it holds a function, and whether it's read through a pointer. */
    /** Where the field [name] of [t] (a record, or a pointer to one) is in C; see [CTypes.fieldPath]. */
    private fun fieldPathIn(t: Type, name: String): String =
        ((if (t is Pointer) t.target else t) as? Record)?.let { CTypes.fieldPath(it, name) } ?: name

    private fun functionField(cd: CompileData, t: Type, name: String): Pair<Type, Boolean>? {
        val throughPointer = t is Pointer && t.target is Declared
        val record = (if (throughPointer) (t as Pointer).target else t) as? Declared ?: return null
        val field = record.field(name) ?: return null
        return if (field is FunctionType || isNullableFunction(field)) field to throughPointer else null
    }
}

/** The C call of [target], a resolved call of the function [name]: what comes back from C is checked and converted to eclang's type. */
internal fun callIR(cd: CompileData, location: Loc, name: String, target: CallTarget): IIR {
    cd.noteEffect()
    // A function with `requires` is called through a C function that is also given the place of the call, to report a failed one.
    val (cName, args) = if (target.overload.hasRequires) cd.functions.contractCall(target.overload) to target.args + github.mrh0.eclang.ir.IRRaw(location, github.mrh0.eclang.ir.IRConvert.where(location))
        else target.overload.cName to target.args
    val call = IRGlobalFunctionCall(location, name, cName, IRArguments(location, args.map { IRArgument(location, it) }))
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

