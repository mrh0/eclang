package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUndefinedAccessorPropertyError
import github.mrh0.eclang.error.EcUnknownTypeError
import github.mrh0.ect2.core.Types2Type
import java.util.HashMap

abstract class EcType(val identifier: String, val inherits: EcType? = EcTypeAny, val namespace: String = "core") {
    override fun toString(): String = identifier

    data class TypeIdentity(
        val namespace: String,
        val identifier: String,
        val create: (location: Loc, types: Array<out EcType>?) -> EcType,
        val params: (count: Int) -> Int = { 0 }) {}

    companion object {
        val ALL_TYPES: HashMap<String, EcType> = hashMapOf()
        val ALL_IDENTITY_TYPES: HashMap<String, TypeIdentity> = hashMapOf()

        fun registerIdentity(
            location: Loc,
            namespace: String,
            identifier: String,
            create: (location: Loc, types: Array<out EcType>?) -> EcType,
            params: (count: Int) -> Int = { 0 }) {
            val uniqueName = "$namespace:$identifier"
            if (ALL_IDENTITY_TYPES.contains(uniqueName)) throw EcError(location,"Type by name $uniqueName is already defined.")
            ALL_IDENTITY_TYPES[uniqueName] = TypeIdentity(namespace, identifier, create, params)
        }

        fun nullable(wrapped: EcType): EcType = EcTypeUnion.of(wrapped, EcTypeNull)
        fun notNullable(type: EcType): EcType = if (type is EcTypeUnion) type.notNullableCopy() else type

        fun from(location: Loc, namespace: String, identifier: String, types: Array<out EcType>): EcType {
            val uniqueName = "$namespace:$identifier"
            val identity = ALL_IDENTITY_TYPES.getOrElse(uniqueName) { throw EcUnknownTypeError(location, uniqueName) }
            val expectedParameterLength = identity.params(types.size)
            if (types.size != expectedParameterLength) throw EcError(location, "Expected $expectedParameterLength type parameters, got ${types.size}.")
            return identity.create(location, types)
        }

        fun from(location: Loc, namespace: String, identifier: String): EcType {
            val uniqueName = "$namespace:$identifier"
            val identity = ALL_IDENTITY_TYPES.getOrElse(uniqueName) { throw EcUnknownTypeError(location, uniqueName) }
            val expectedParameterLength = identity.params(0)
            if (expectedParameterLength != 0) throw EcError(location, "Expected $expectedParameterLength type parameters, got 0.")
            return identity.create(location, null)
        }
    }

    fun from(location: Loc, vararg types: EcType) = Companion.from(location, namespace, identifier, types)
    fun from(location: Loc) = Companion.from(location, namespace, identifier)

    private val uniqueName: String = "$namespace:$identifier"
    val uid: Int = ALL_TYPES.size

    init {
        onNew()
    }

    open fun onNew() {
        if (ALL_TYPES.contains(uniqueName)) throw Error("BUG: Type $uniqueName is already instantiated.")
        ALL_TYPES[this.uniqueName] = this
    }

    open fun accepts(location: Loc, type: EcType): Boolean = eq(type) || (type.inherits != null && this.accepts(location, type.inherits))
    open fun eq(type: EcType): Boolean = type === this || type.uniqueName === this.uniqueName
    open fun canCast(location: Loc, type: EcType): Boolean = type.accepts(location, this)
    open fun cast(location: Loc, type: EcType): EcType {
        if (canCast(location, this)) return type
        throw EcError(location, "$this cannot be cast to $type")
    }

    @Deprecated("Use EcTypeUnion.of()",
        ReplaceWith("EcTypeUnion.of()")
    )
    open fun union(other: EcType): EcType {
        return if (other is EcTypeUnion) other.union(this) else EcTypeUnion.of(this, other)
    }

    @Deprecated("Remove",
        ReplaceWith("Nothing")
    )
    fun alias(namespace: String, id: String): EcType {
        val uniqueAliasName = "$namespace:$id"
        if (ALL_TYPES.contains(uniqueAliasName)) throw Error("BUG: Type $uniqueAliasName is already instantiated.")
        ALL_TYPES[uniqueAliasName] = this
        return this
    }

    override fun hashCode(): Int {
        return uid
    }

    open fun expand(): List<EcType> = listOf(this)

    open fun isReferenceType() = false

    open fun canBeInstantiated() = true

    open fun getProperty(location: Loc, name: String): EcType = throw EcUndefinedAccessorPropertyError(location, name, this)

    open fun accessor(location: Loc, indexType: EcType): EcType = throw EcUndefinedAccessorPropertyError(location, "[$indexType]", this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EcType
        return uid == other.uid
    }
}