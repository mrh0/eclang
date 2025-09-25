package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUnknownTypeError
import java.util.HashMap

typealias CreateType = (location: Loc, types: Array<EcType>?) -> EcType
typealias ParamsType = (count: Int) -> Int
class TypeRegistry(private val location: Loc) {
    data class TypeIdentity(
        val namespace: String,
        val identifier: String,
        val create: CreateType,
        val params: ParamsType = { 0 }) {}

    companion object {
        val ALL_IDENTITY_TYPES: HashMap<String, TypeIdentity> = hashMapOf()

        fun registerIdentity(
            location: Loc,
            namespace: String,
            identifier: String,
            create: (location: Loc, types: Array<EcType>?) -> EcType,
            params: (count: Int) -> Int = { 0 }) {
            val uniqueName = "$namespace:$identifier"
            if (ALL_IDENTITY_TYPES.contains(uniqueName)) throw EcError(location,"Type by name $uniqueName is already defined.")
            ALL_IDENTITY_TYPES[uniqueName] = TypeIdentity(namespace, identifier, create, params)
        }

        fun get(location: Loc, namespace: String, identifier: String, types: Array<EcType>): EcType {
            val uniqueName = "$namespace:$identifier"
            val identity = ALL_IDENTITY_TYPES.getOrElse(uniqueName) { throw EcUnknownTypeError(location, uniqueName) }
            val expectedParameterLength = identity.params(types.size)
            if (types.size != expectedParameterLength) throw EcError(location, "Expected $expectedParameterLength type parameters, got ${types.size}.")
            return identity.create(location, types)
        }

        fun get(location: Loc, namespace: String, identifier: String): EcType {
            val uniqueName = "$namespace:$identifier"
            val identity = ALL_IDENTITY_TYPES.getOrElse(uniqueName) { throw EcUnknownTypeError(location, uniqueName) }
            val expectedParameterLength = identity.params(0)
            if (expectedParameterLength != 0) throw EcError(location, "Expected $expectedParameterLength type parameters, got 0.")
            return identity.create(location, null)
        }
    }

    private fun register(name: String, create: CreateType, params: ParamsType) {
        registerIdentity(location, "core", name, create, params)
    }

    private fun register(name: String, create: CreateType) {
        registerIdentity(location, "core", name, create) { 0 }
    }

    private fun register(name: String, create: CreateType, count: Int) {
        registerIdentity(location, "core", name, create) { count }
    }

    private fun singleton(type: EcType): CreateType = { _, _ -> type }

    fun registerBuiltin() {
        register("Any", singleton(EcTypeAny))
        register("Array", { _, types -> EcTypeArray.of(types!![0]) }, 1)
        register("Atom", singleton(EcTypeAtom))
        register("Bool", singleton(EcTypeBool))
        register("Boxed", { _, types -> EcTypeBoxed.of(types!![0]) }, 1)
        register("CArray", { _, types -> EcTypeArray.of(types!![0]) }, 1)
        register("CString", singleton(EcTypeAny))
        register("None", singleton(EcTypeNone))
        register("Null", singleton(EcTypeNull))
        register("Pointer", { _, types -> EcTypeArray.of(types!![0]) }, 1)
        register("String", singleton(EcTypeString))
        register("Tuple", { _, types -> EcTypeTuple.of(types!!) }, { c -> c })
        register("Union", { _, types -> EcTypeUnion.of(types!!.toHashSet()) }, { c -> c })
    }
}
