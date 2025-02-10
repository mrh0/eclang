package github.mrh0.eclang.ast.token

import github.mrh0.eclang.Compiler
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.type.TTypeRecord
import github.mrh0.eclang.ast.token.function.IFuncBody
import github.mrh0.eclang.ast.token.function.TFunc
import github.mrh0.eclang.ast.token.global.TGlobalUse
import github.mrh0.eclang.context.function.FunctionParameter
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRProgram
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.types.internal.EcTypeDefaultParameterWrapper
import github.mrh0.eclang.util.Util
import github.mrh0.eclang.util.Util.testIdentifier

class TProgram(location: Loc, private val functionsIn: List<TFunc>, private val usesIn: List<TGlobalUse>, private val globalsIn: List<ITok>) : Tok(location) {
    companion object {
        val useMap: MutableMap<String, Boolean> = mutableMapOf()
        val functions: MutableList<TFunc> = mutableListOf()
        val records: MutableList<TTypeRecord> = mutableListOf()
        val uses: MutableList<TGlobalUse> = mutableListOf()
        val globals: MutableList<ITok> = mutableListOf()

        private fun indexPermutation(list: MutableList<Int>, limit: List<Int>, index: Int): Boolean {
            if (index >= list.size) return true
            list[index] = list[index]+1
            if (list[index] >= limit[index]) {
                list[index] = 0
                return indexPermutation(list, limit, index+1)
            }
            return false
        }

        fun permutateFunctionParams(params: List<FunctionParameter>, callback: (Array<FunctionParameter>) -> Any) {
            val expanded = params.map { it.type.expand() }
            val indices = expanded.map { 0 }.toMutableList()
            val limits = expanded.map { it.size }
            while(true) {
                callback(
                    indices.mapIndexed { index, i -> expanded[index][i] }
                        .mapIndexed { index, type ->
                            FunctionParameter(
                                params[index].name,
                                if (type is EcTypeDefaultParameterWrapper) type.getContained() else type,
                                if (type is EcTypeDefaultParameterWrapper) null else params[index].def
                            )
                        }
                        .toTypedArray()
                )
                if (indexPermutation(indices, limits, 0)) break
            }
        }
    }
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        usesIn.forEach {
            if (useMap[it.path] != true && it.path.endsWith(".ec")) {
                useMap[it.path] = true
                val file = Util.getPath(location, it.path).toFile()
                val tree = Compiler.tokenizeFile(file)
                functions.addAll(tree.functionsIn)
                uses.addAll(tree.usesIn)
                globals.addAll(tree.globalsIn)
            }
        }

        functions.addAll(functionsIn)
        uses.addAll(usesIn)
        globals.addAll(globalsIn)

        val usesIRs = uses.map { it.process(cd, hint).second }

        val globalIRs = globals.map { it.process(cd, hint).second }
        functions.forEach { analyzeFunction(it, cd) } //it.process(cd).second

        val functionIRs: MutableList<IIR> = mutableListOf()
        GlobalFunctions.getAllOverrides(location).forEach { fos ->
            fos.overrides.forEach { fo ->
                if (!fo.isExternal() && !fo.hasGenerics) fo.buildIR(location, cd, fo.ret)
            }
        }
        GlobalFunctions.getAllOverrides(location).forEach { fos ->
            fos.overrides.forEach { fo ->
                if (!fo.isExternal() && !fo.hasGenerics) functionIRs.add(fo.buildIR(location, cd, fo.ret))
            }
        }

        return EcTypeNone to IRProgram(location, functionIRs, globalIRs, usesIRs)
    }

    private fun analyzeFunction(func: TFunc, cd: CompileData) {
        val fixedName = testIdentifier(location, func.name)
        val res = func.processSignature(cd)

        permutateFunctionParams(res.first) { list ->
            GlobalFunctions.addOverride(
                func.location,
                fixedName,
                list,
                res.second, // Ret type
                if (func is IFuncBody) func.getBody() else null,
                func.getSourceName(),
                null
            )
        }
    }

    override fun toString(): String {
        return "TProgram($records, $functions)"
    }
}