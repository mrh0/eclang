package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

abstract class TFunc(location: Loc, val name: String, val params: TParameters, val returns: ITok?) : NonValueTok(location) {
    /** Locals whose address the body takes (`addrof x`), with where; flow typing doesn't narrow them (see `Narrowing`). */
    var addressTaken: Map<String, Loc> = mapOf()

    /** For `export fn`: the name C calls it by. */
    var exportName: String? = null

    /** Its `requires` and `ensures` clauses, in order. */
    var contracts: List<Contract> = listOf()

    /** The parameters and the declared return type (a Result for `T throws E`). */
    fun processSignature(cd: CompileData): Pair<List<Parameter>, Type> {
        val parameters = params.get().map { it.toFunctionParameter(cd) }
        val returnType = returns?.process(cd, Types.None)?.first ?: Types.None
        return parameters to returnType
    }

    /** The C name for functions declared in C, or null. */
    open fun getSourceName(): String? = null
}
