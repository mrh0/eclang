package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TProgram.Companion.permutateFunctionParams
import github.mrh0.eclang.context.function.FunctionParameter
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.error.fn.EcAmbiguousSignatureError
import github.mrh0.eclang.error.EcGenericNotEstablishedError
import github.mrh0.eclang.error.fn.EcNoMatchingCallSignatureError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.data.IRValue
import github.mrh0.eclang.ir.function.call.IRGlobalFunctionCall
import github.mrh0.eclang.ir.function.call.IRArgument
import github.mrh0.eclang.ir.function.call.IRArguments
import github.mrh0.eclang.types.*
import github.mrh0.eclang.types.internal.EcTypeVarArgC
import github.mrh0.eclang.types.numbers.signed.EcTypeInt

open class TExprCall (location: Loc, val name: String, val args: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "TCall(${name}, $args)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val overrides = GlobalFunctions.getOverridesByName(location, name)
        // EcTypeUnion(overrides.overrides.map { it.params[index].type }.toHashSet() ) )
        val processedArgs = args.map { it.process(cd, EcTypeAny) }.toMutableList() // TODO: Create union hint from overrides
        val argTypes = processedArgs.map { it.first }.toTypedArray()

        val matching = overrides.getMatching(location, argTypes)

        if (matching.isEmpty()) throw EcNoMatchingCallSignatureError(location, name, argTypes)
        val matchingNonGeneric = matching.filter { !it.hasGenerics }
        if (matchingNonGeneric.size > 1) throw EcAmbiguousSignatureError(location, cd.namespace, name, argTypes, matching[0].location)
        val first = matching[0]

        if (first.varArg != null && first.varArg !is EcTypeVarArgC) {
            val varArgLen = processedArgs.count() - first.params.size
            processedArgs.add(varArgLen-1, EcTypeInt to IRInt(location, varArgLen))
        }

        if (first.hasGenerics) {
            val genericMap: MutableMap<String, EcType> = mutableMapOf()
            first.params.forEachIndexed { index, it ->
                if (it.type is EcTypeGeneric) {
                    val existing = genericMap[it.type.name]
                    if (existing == null) {
                        genericMap[it.type.name] = argTypes[index]
                    } else {
                        if (!existing.accepts(location, argTypes[index])) throw EcNoMatchingCallSignatureError(
                            location,
                            name,
                            arrayOf(argTypes[index])
                        )
                    }
                }
            }

            val newArgs: List<FunctionParameter> = first.params.map() { if (it.type is EcTypeGeneric) FunctionParameter(it.name, genericMap[it.type.name] ?: TODO("Should never happen."), null) else it }
            val ret = if (first.ret is EcTypeGeneric) genericMap[first.ret.name] ?: throw EcGenericNotEstablishedError(location, first.ret.name) else first.ret
            permutateFunctionParams(newArgs) { list ->
                GlobalFunctions.addOverride(
                    location,
                    name,
                    list,
                    ret, // Ret type
                    first.block,
                    first.id,
                    genericMap,
                    true
                )
            }
            return Pair(
                ret,
                IRGlobalFunctionCall(
                    location,
                    name,
                    first.id,
                    IRArguments(location, processedArgs.map { IRArgument(location, it.second) })
                )
            )
        }

        first.setCalled()

        return Pair(
            first.ret,
            IRGlobalFunctionCall(
                location,
                name,
                first.id,
                IRArguments(location, processedArgs.map { IRArgument(location, it.second) })
            )
        )
    }
}