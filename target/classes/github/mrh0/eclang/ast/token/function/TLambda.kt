package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.Closure
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.eclang.ir.IRStatementReturn
import github.mrh0.eclang.ir.data.IRResultValue
import github.mrh0.eclang.ir.function.IRClosure
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.FunctionType
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `fn (x: Int) = x + limit`: a function as a value, compiled to a static C function of its own. Parameters without a
 * type take it from the function type expected there (`val f: (Int) => Int = fn (x) = x + 1`), and the return type
 * is the body's unless written (`fn (x: Int): Long = x`, `fn (s: String): Int throws Error = try parse(s)`).
 *
 * The body can use the locals of the function around it: the lambda captures them (see [Closure]), and its value is a
 * closure allocated in the current pool. One that captures nothing is a static object. Where C expects a plain function
 * pointer (a function type of a `declare`), the value is the C function itself, which can't capture anything.
 * A `try` in the body passes the error on from the lambda, which then `throws` (what the `try`s can fail with, unless written).
 * [changes] are the names the body may change (see [Closure.changes]).
 */
class TLambda(location: Loc, private val params: List<Pair<String, ITok?>>, private val returns: ITok?, private val body: ITok,
              private val changes: Set<String> = setOf()) : Tok(location) {
    /** What the last compile captured, as Lifetimes follows it: `x` for a copy, `addrof x` for a `var` (see [Closure.Capture.token]). */
    var captured: List<ITok> = listOf()
        private set

    /** The `var`s the last compile shares with the function around it, by the token in [captured] that stands for each. */
    var shared: Map<ITok, String> = mapOf()
        private set

    /** What the last compile captured, with types and whether by reference (a `var`), for `flat` parameters (see Sharing). */
    var captures: List<github.mrh0.eclang.context.Closure.Capture> = listOf()
        private set

    /** Whether the last compile made a closure in the current pool: it captures something. */
    val allocates get() = captured.isNotEmpty()

    override fun pointerFlow(): PointerFlow = PointerFlow.From(captured) // and the closure itself, in the current pool: Lifetimes.value

    /** Whether a parameter's type must come from the function type expected here. */
    val hasUntypedParameters get() = params.any { it.second == null }

    /** Whether the function type expected here gives it something: a parameter's type, or the type its body should have. */
    val wantsExpectedType get() = hasUntypedParameters || returns == null

    /** Set for a lambda called where it is written (a `|>` stage): the expected type only gives its parameter types. */
    var calledDirectly = false

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        captured = listOf()
        shared = mapOf()
        this.captures = listOf()
        val expected = hint as? FunctionType ?: (hint as? Union)?.members?.filterIsInstance<FunctionType>()?.singleOrNull()
        val inC = expected?.isC == true
        if (expected != null && expected.params.size != params.size && params.any { it.second == null })
            throw EcError(location, "This lambda takes ${params.size} parameter${if (params.size == 1) "" else "s"}, but a $expected is expected here.")
        val names = params.map { testIdentifier(location, it.first) }
        names.groupBy { it }.values.firstOrNull { it.size > 1 }?.let { throw EcError(location, "The lambda has two parameters called '${it[0]}'.") }
        val types = params.mapIndexed { i, (name, typeTok) ->
            val type = typeTok?.process(cd, Types.None)?.first ?: expected?.params?.getOrNull(i)
                ?: throw EcError(location, "The type of the lambda parameter '$name' isn't known here. Write it, e.g. `fn ($name: Int) = ...`.")
            cd.storable(location, type, "lambda parameter '$name'")
        }
        val written = returns?.process(cd, Types.None)?.first
        val declared = ((written as? Result)?.value ?: written)?.let { if (it === Types.None) it else cd.storable(location, it, "return type of the lambda") }
        val fixedThrows = (written as? Result)?.error?.let { cd.storable(location, it, "error type of the lambda") }
            ?: expected?.throws.takeIf { !calledDirectly }
        val cName = cd.claimCName(location, cd.functions.lambdaName(), "a lambda")
        val closure = Closure(location, cd.ctx(), cd.function, cd.genericContext, cd.closure, inC, changes, fixedThrows, names)
        val self = if (inC) listOf() else listOf(IRParameter(location, SELF, cd.types.pointer(Types.Any), null))
        val parameters = IRParameters(location, self + names.mapIndexed { i, name -> IRParameter(location, cd.localCName(name), types[i], null) })
        // Any: the return type is left to the lambda (a generic `<U>` it decides, see Functions.lambdaHint).
        val expectedReturns = expected?.returns.takeIf { !calledDirectly && it !== Types.Any }

        val unsafe = cd.isUnsafe
        val (returnType, _) = cd.functions.lambda(cName, { cd.isolated { cd.inClosure(closure) { cd.escapes.inLambda {
            cd.newContext(cName)
            names.forEachIndexed { i, name -> cd.ctx().define(location, Constant(name, types[i], cd.localCName(name), kind = "a parameter")) }
            val (hoisted, value) = cd.collectHoisted { body.process(cd, declared ?: expectedReturns ?: Types.None) }
            val throws = throwsOf(cd, closure, expected)
            val neverReturns = throws == null && value.first === Types.Never && declared == null && expectedReturns.let { it == null || it === Types.None || it === Types.Never }
            // Where a function that returns nothing is expected, the body's value is dropped, as a statement's is.
            val dropped = declared == null && expectedReturns === Types.None && value.first !== Types.Never && value.first !is Result
            if (value.first === Types.None || neverReturns || dropped) {
                if (declared != null && declared !== Types.None) throw EcError(location, "The lambda is declared to return $declared, but its body has no value.")
                val type = if (neverReturns && expectedReturns !== Types.None) Types.Never else Types.None
                closure.result = throws?.let { cd.types.result(type, it) }
                type to IRBlock(location, hoisted + IRStatement(location, if (dropped) Dropped(location, value.second) else value.second), listOf())
            } else {
                val wanted = expectedReturns?.takeIf { it !== Types.None && cd.types.accepts(it, value.first) }
                val type = declared ?: wanted ?: cd.types.concrete(cd.storable(location, value.first, "return value of the lambda"))
                Lifetimes.checkReturn(cd, location, body, type)
                val result = throws?.let { cd.types.result(type, it) }
                closure.result = result
                val returned = cd.expect(location, value, type, "return value of the lambda").let { if (result != null) IRResultValue(location, result, false, it) else it }
                type to IRBlock(location, hoisted + IRStatementReturn(location, returned), listOf(), diverges = true)
            }
        } } } }) { (type, block) ->
            val result = closure.result
            val fields = closure.captures.values.map { it.field to if (it.byReference) cd.types.pointer(it.type) else it.type }
            IRFunctionOverride(location, block, cName, parameters, result ?: type, result != null, false,
                closure = if (inC) null else IRFunctionOverride.ClosureC(cd.types.function(types, type, result?.error), fields))
        }
        val captures = closure.captures.values.toList()
        captured = captures.map { it.token }
        this.captures = captures
        shared = captures.filter { it.byReference }.associate { it.token to it.name }
        if (captures.isNotEmpty()) cd.noteAllocation(location, "a lambda that uses local variables (its closure)")
        val type = cd.types.function(types, returnType, closure.result?.error, isC = inC)
        // What the body keeps of what it uses is checked here, as if it were called where it is written (see Escapes).
        Lifetimes.noteLambda(cd, location, closure, type, unsafe)
        return type to if (inC) IRNamed(location, cName) else IRClosure(location, cName, captures.map { it.init })
    }

    /**
     * What the lambda throws: its written or expected `throws`, or else what its `try`s can fail with. A lambda expected to be
     * one that can't fail mustn't use `try`.
     */
    private fun throwsOf(cd: CompileData, closure: Closure, expected: FunctionType?): Type? {
        closure.throws?.let { return it }
        if (closure.thrown.isEmpty()) return null
        val thrown = cd.types.union(closure.thrown)
        if (expected != null && !calledDirectly)
            throw EcError(location, "This lambda uses `try`, so it can fail with $thrown, but here a $expected is expected, which can't fail. " +
                "Handle the error inside the lambda, with `?? default` or `try ... catch e do ...`.")
        return thrown
    }

    override fun toString() = "TLambda(${params.map { it.first }}, $body)"

    /** A value the lambda drops: `(void)(x + 1)`, so that C doesn't warn that it has no effect. */
    private class Dropped(location: Loc, private val value: IIR) : github.mrh0.eclang.ir.IR(location) {
        override fun toC(sb: github.mrh0.eclang.output.c.CSourceBuilder, c: github.mrh0.eclang.context.Context) {
            sb.put("(void)(")
            value.toC(sb, c)
            sb.put(")")
        }
    }

    companion object {
        /** The closure, the first parameter of a lambda's C function (see [IRFunctionOverride.ClosureC]). */
        const val SELF = "__ec_self"
    }
}
