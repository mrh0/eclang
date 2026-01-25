package github.mrh0.eclang.context.fn2

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcNoMatchingCallSignatureError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeTuple

class ModuleScope {
    private val functions = mutableMapOf<String, FunctionTemplate>()

    fun addOverride(override: OverrideTemplate) {
        val template = functions.getOrPut(override.identifier) { FunctionTemplate(override.namespace, override.identifier, override.location) }
        template.insert(override)
    }

    fun getOverride(location: Loc, identifier: String, arguments: Array<EcType>): OverrideTemplate {
        val template = functions.getOrElse(identifier) { throw EcNotDefinedError(location, "Function $identifier") }
        return template.get(location, arguments) ?: throw EcNoMatchingCallSignatureError(location, identifier, arguments)
    }

    override fun toString(): String {
        return functions.toString()
    }
}