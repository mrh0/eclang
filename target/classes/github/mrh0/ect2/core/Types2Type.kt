package github.mrh0.ect2.core

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcUndefinedAccessorPropertyError
import github.mrh0.eclang.types.EcType
import java.util.HashMap

abstract class Types2Type(val namespace: String, val id: String, val inherits: Types2Type?) {
    companion object {
        val ALL_TYPES: HashMap<String, Types2Type> = hashMapOf()
    }

    val uniqueName: String = "$namespace:$id"
    val uid: Int = ALL_TYPES.size

    init {
        if (ALL_TYPES.contains(uniqueName)) throw Error("BUG: Type $uniqueName is already instantiated.")
        ALL_TYPES[this.uniqueName] = this
    }

    open fun accepts(type: Types2Type): Boolean = eq(type) || (type.inherits != null && this.accepts(type.inherits))
    open fun eq(type: Types2Type): Boolean = type === this || type.uniqueName === this.uniqueName
    open fun canCast(type: Types2Type): Boolean = type.accepts(this)
    open fun cast(type: Types2Type): Types2Type {
        if (canCast(this)) return type
        throw Error("$this cannot be cast to $type")
    }
    open fun union(other: Types2Type): Types2TypeUnion {
        return if (other is Types2TypeUnion) other.union(this) else Types2TypeUnion.of(this, other)
    }
    fun alias(namespace: String, id: String): Types2Type {
        val uniqueAliasName = "$namespace:$id"
        if (ALL_TYPES.contains(uniqueAliasName)) throw Error("BUG: Type $uniqueAliasName is already instantiated.")
        ALL_TYPES[uniqueAliasName] = this
        return this
    }

    open fun isReferenceType(): Boolean = false

    open fun canBeInstantiated() = false

    open fun getProperty(location: Loc, name: String): Types2Type = throw Error("")

    open fun accessor(location: Loc, indexType: Types2Type): Types2Type = throw Error("")

    override fun toString(): String = uniqueName
}