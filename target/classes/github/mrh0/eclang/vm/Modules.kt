package github.mrh0.eclang.vm

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

object Modules {
    fun parseModuleReference(location: Loc, ref: String): Pair<String, String> {
        val list = ref.trim().split(":")
        if (list.size == 1) return Pair("gs", list[0])
        if (list.size != 2) throw EcError(location, "Invalid module reference '$ref'")
        return Pair(list[0], list[1])
    }
}