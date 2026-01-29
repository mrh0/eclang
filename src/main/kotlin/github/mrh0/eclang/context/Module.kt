package github.mrh0.eclang.context

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.fn2.FunctionTemplate
import github.mrh0.eclang.context.fn2.OverrideTemplate
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.error.EcNameCollisionError
import github.mrh0.eclang.error.fn.EcFunctionNotDefinedError
import github.mrh0.eclang.error.fn.EcNoMatchingCallSignatureError
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeCallSignature
import github.mrh0.eclang.types.EcTypeFunctionReference
import github.mrh0.eclang.util.Util

class Module private constructor(val namespace: String) {
    companion object {
        val globalModules = mutableMapOf<String, Module>()
        val nameCollisionSet = mutableSetOf<String>()

        fun getModules() = globalModules
        fun get(namespace: String) = globalModules.getOrPut(namespace) { Module(namespace) }
    }

    private val functions = mutableMapOf<String, FunctionTemplate>()
    val vars: MutableList<IVar> = mutableListOf()

    fun checkCollision(location: Loc, sourceName: String) {
        if (nameCollisionSet.contains(sourceName)) {
            throw EcNameCollisionError(location, sourceName)
        }
    }

    fun defineModuleVar(location: Loc, variable: IVar): IVar {
        checkCollision(location ,getModuleVarSourceName(variable))
        vars.add(variable)
        return variable
    }

    fun addOverride(identifier: String, override: OverrideTemplate) {
        checkCollision(override.location ,override.sourceName)
        nameCollisionSet.add(override.sourceName)
        val template = functions.getOrPut(identifier) {
            val template = FunctionTemplate(override.location, namespace, identifier)
            defineModuleVar(
                override.location,
                Constant(identifier, EcTypeFunctionReference.of(template))
            )
            template
        }
        template.insert(override)
    }

    fun getOverride(location: Loc, identifier: String, arguments: Array<EcType>): OverrideTemplate {
        val template = functions.getOrElse(Util.testIdentifier(location, identifier)) {
            throw EcFunctionNotDefinedError(location, identifier, namespace)
        }
        return template.get(location, arguments) ?: throw EcNoMatchingCallSignatureError(
            location,
            identifier,
            arguments
        )
    }

    override fun toString(): String {
        return functions.toString()
    }

    fun getNextOverrideId(location: Loc, identifier: String): Int {
        return functions.getOrElse(identifier) { return 0 }.getOverrideCount()
    }

    fun getModuleVarSourceName(variable: IVar) = "${namespace}__${variable.getName()}"
}