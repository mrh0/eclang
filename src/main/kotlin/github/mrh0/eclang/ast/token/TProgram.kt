package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.data.record.TRecord
import github.mrh0.eclang.ast.token.function.TFunc
import github.mrh0.eclang.ast.token.function.TFuncBlock
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRProgram
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRFunctionOverrides
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TProgram(location: Loc, private val functions: List<TFunc>, val records: List<TRecord>, val uses: List<ITok>) : Tok(location) {

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        uses.map { it.process(cd) }
        functions.forEach { analyzeFunction(it, cd) } //it.process(cd).second

        val iir: MutableList<IIR> = mutableListOf()
        GlobalFunctions.getAllOverrides(location).forEach { fos ->
            fos.overrides.forEach { fo ->
                val params = IRParameters(location, fo.argTypes.mapIndexed { index, value -> IRParameter(location, fo.argNames.get(index), value) })
                if (!fo.isExternal()) iir.add(IRFunctionOverride(location, fo.block!!.process(cd).second as IRBlock, fo.id, params, fo.ret))
            }
        }

        // val irs = functions.map { it.process(cd).second }
        return EcTypeNone to IRProgram(location, iir)
    }

    private fun indexPermutation(list: MutableList<Int>, limit: List<Int>, index: Int): Boolean {
        if (index >= list.size) return true
        list[index] = list[index]+1
        if (list[index] >= limit[index]) {
            list[index] = 0
            return indexPermutation(list, limit, index+1)
        }
        return false
    }

    private fun permutateFunctionArguments(argTypes: Array<EcType>, callback: (Array<EcType>) -> Any) {
        val expanded = argTypes.map { it.expand() }
        val indices = expanded.map { 0 }.toMutableList()
        val limits = expanded.map { it.size }
        while(true) {
            println("$indices $limits")
            callback(indices.mapIndexed { index, i -> expanded[index][i] }.toTypedArray())
            if (indexPermutation(indices, limits, 0)) break
        }
    }

    private fun analyzeFunction(func: TFunc, cd: CompileData) {
        val res = func.processSignature(cd)
        val argNames = res.first.map { it.first }.toTypedArray()
        val argTypes = res.first.map { it.second }.toTypedArray()
        val retType = res.second

        permutateFunctionArguments(argTypes) { list ->
            println("${func.name} ${list.map { it.toString() }}")
            GlobalFunctions.addOverride(
                func.location,
                func.name,
                argNames,
                list,
                retType,
                if (func is TFuncBlock) func.block else null,
                func.getSourceName()
            )
        }
    }

    override fun toString(): String {
        return "TProgram($records, $functions)"
    }
}