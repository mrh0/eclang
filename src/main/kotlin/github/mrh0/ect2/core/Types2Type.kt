package github.mrh0.ect2.core

import java.util.HashMap

abstract class Types2Type(val namespace: String, val id: String, val inherits: Types2Type?) {
    val uniqueName: String = "$namespace:$id"

    open fun accepts(type: Types2Type): Boolean = eq(type) || (type.inherits != null && this.accepts(type.inherits))
    open fun eq(type: Types2Type): Boolean = type === this || type.uniqueName === this.uniqueName
    open fun canCast(type: Types2Type): Boolean = type.accepts(this)
    open fun cast(type: Types2Type): Types2Type {
        if (canCast(this)) return type
        throw Error("$this cannot be cast to $type")
    }
    fun union(union: Types2Type) {
        Types2TypeUnion.of(this, union)
    }
    fun alias(namespace: String, id: String): Types2Type {
        val uniqueAliasName = "$namespace:$id"
        if (ALL_TYPES.contains(uniqueAliasName)) throw Error("BUG: Type $uniqueAliasName is already instantiated.")
        ALL_TYPES[uniqueAliasName] = this
        return this
    }

    override fun toString(): String = uniqueName

    init {
        if (ALL_TYPES.contains(uniqueName)) throw Error("BUG: Type $uniqueName is already instantiated.")
        ALL_TYPES[this.uniqueName] = this
    }

    companion object {
        val ALL_TYPES: HashMap<String, Types2Type> = hashMapOf()
    }
}