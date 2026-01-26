package github.mrh0.eclang.context.fn2

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcNameCollisionError
import github.mrh0.eclang.error.fn.EcNoMatchingCallSignatureError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.eclang.error.fn.EcFunctionNotDefinedError
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeTuple
import github.mrh0.eclang.util.Util.testIdentifier

class ModuleScope(val namespace: String) {
    companion object {
        val globalNamespaces = mutableSetOf<String>()
        val nameCollisionSet = mutableSetOf<String>()
    }

    init {
        globalNamespaces.add(namespace)
    }

    private val functions = mutableMapOf<String, FunctionTemplate>()

    fun addOverride(identifier: String, override: OverrideTemplate) {
        if (nameCollisionSet.contains(override.sourceName)) { throw EcNameCollisionError(override.location, override.sourceName) }
        nameCollisionSet.add(override.sourceName)
        val template = functions.getOrPut(identifier) { FunctionTemplate(override.location, namespace, identifier) }
        template.insert(override)
    }

    fun getOverride(location: Loc, identifier: String, arguments: Array<EcType>): OverrideTemplate {
        val template = functions.getOrElse(testIdentifier(location, identifier)) { throw EcFunctionNotDefinedError(location, identifier, namespace) }
        return template.get(location, arguments) ?: throw EcNoMatchingCallSignatureError(location, identifier, arguments)
    }

    override fun toString(): String {
        return functions.toString()
    }

    fun getNextOverrideId(location: Loc, identifier: String): Int {
        return functions.getOrElse(identifier) { return 0 }.getOverrideCount()
    }
}