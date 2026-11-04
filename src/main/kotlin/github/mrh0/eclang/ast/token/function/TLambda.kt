package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.eclang.ir.IRStatementReturn
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.FunctionType
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `fn (x: Int) = x + 1`: a function as a value, compiled to a static C function of its own. Parameters without a
 * type take it from the function type expected there (`val f: (Int) => Int = fn (x) = x + 1`), and the return type
 * is the body's unless written (`fn (x: Int): Long = x`). The body can't use the local variables of the function
 * around it (closures aren't supported yet), but it can use globals and call functions.
 */
class TLambda(location: Loc, private val params: List<Pair<String, ITok?>>, private val returns: ITok?, private val body: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val expected = hint as? FunctionType ?: (hint as? Union)?.members?.filterIsInstance<FunctionType>()?.singleOrNull()
        if (expected != null && expected.params.size != params.size && params.any { it.second == null })
            throw EcError(location, "This lambda takes ${params.size} parameter${if (params.size == 1) "" else "s"}, but a $expected is expected here.")
        val names = params.map { testIdentifier(location, it.first) }
        names.groupBy { it }.values.firstOrNull { it.size > 1 }?.let { throw EcError(location, "The lambda has two parameters called '${it[0]}'.") }
        val types = params.mapIndexed { i, (name, typeTok) ->
            val type = typeTok?.process(cd, Types.None)?.first ?: expected?.params?.getOrNull(i)
                ?: throw EcError(location, "The type of the lambda parameter '$name' isn't known here. Write it, e.g. `fn ($name: Int) = ...`.")
            cd.storable(location, type, "lambda parameter '$name'")
        }
        val declared = returns?.process(cd, Types.None)?.first?.let { cd.storable(location, it, "return type of the lambda") }
        val cName = cd.claimCName(location, cd.functions.lambdaName(), "a lambda")
        val parameters = IRParameters(location, names.mapIndexed { i, name -> IRParameter(location, cd.localCName(name), types[i], null) })
        val (returnType, _) = cd.functions.lambda(cName, { cd.isolated {
            cd.newContext(cName)
            names.forEachIndexed { i, name -> cd.ctx().define(location, Constant(name, types[i], cd.localCName(name), kind = "a parameter")) }
            val (hoisted, value) = cd.collectHoisted { body.process(cd, declared ?: expected?.returns ?: Types.None) }
            if (value.first === Types.None) {
                if (declared != null) throw EcError(location, "The lambda is declared to return $declared, but its body has no value.")
                Types.None to IRBlock(location, hoisted + IRStatement(location, value.second), listOf())
            } else {
                val type = declared ?: cd.storable(location, value.first, "return value of the lambda")
                Lifetimes.checkReturn(cd, location, body, type)
                type to IRBlock(location, hoisted + IRStatementReturn(location, cd.expect(location, value, type, "return value of the lambda")), listOf(), diverges = true)
            }
        } }) { (type, block) -> IRFunctionOverride(location, block, cName, parameters, type, false, false) }
        return cd.types.function(types, returnType, null) to IRNamed(location, cName)
    }

    override fun toString() = "TLambda(${params.map { it.first }}, $body)"
}
