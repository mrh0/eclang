package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.data.record.TRecord
import github.mrh0.eclang.ast.token.function.TFunc
import github.mrh0.eclang.ast.token.function.TFuncBlock
import github.mrh0.eclang.context.function.FunctionParameter
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRProgram
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TProgram(location: Loc, private val functions: List<TFunc>, val records: List<TRecord>, val uses: List<ITok>) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        uses.map { it.process(cd, hint) }
        val recordIRs = records.map { it.process(cd, hint).second }
        functions.forEach { analyzeFunction(it, cd) } //it.process(cd).second

        val functionIRs: MutableList<IIR> = mutableListOf()
        GlobalFunctions.getAllOverrides(location).forEach { fos ->
            fos.overrides.forEach { fo ->
                if (!fo.isExternal()) functionIRs.add(fo.buildIR(location, cd, fo.ret))
            }
        }

        return EcTypeNone to IRProgram(location, functionIRs, recordIRs)
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

    private fun permutateFunctionArguments(params: List<FunctionParameter>, callback: (Array<FunctionParameter>) -> Any) {
        val expanded = params.map { it.type.expand() }
        val indices = expanded.map { 0 }.toMutableList()
        val limits = expanded.map { it.size }
        while(true) {
            callback(
                indices.mapIndexed { index, i -> expanded[index][i] }
                .mapIndexed { index, type -> FunctionParameter(params[index].name, type, if (type == EcTypeNone) null else params[index].def) }
                .toTypedArray()
            )
            if (indexPermutation(indices, limits, 0)) break
        }
    }

    private fun analyzeFunction(func: TFunc, cd: CompileData) {
        val res = func.processSignature(cd)

        permutateFunctionArguments(res.first) { list ->
            GlobalFunctions.addOverride(
                func.location,
                func.name,
                list,
                res.second, // Ret type
                if (func is TFuncBlock) func.block else null,
                func.getSourceName()
            )
        }
    }

    override fun toString(): String {
        return "TProgram($records, $functions)"
    }
}