package github.mrh0.ect2.core

class Types2TypeUnion private constructor(val type: Types2Type, var right: Types2Type) : Types2Type("core", getUnionId(type, right), type) {
    companion object {
        fun getUnionId(type: Types2Type, right: Types2Type) = "Union<${type.uniqueName},${right.uniqueName}>"
        fun of(type: Types2Type, right: Types2Type): Types2TypeUnion = ALL_TYPES.getOrPut(getUnionId(type, right)) { Types2TypeUnion(type, right) } as Types2TypeUnion
    }

    override fun union(other: Types2Type): Types2TypeUnion {
        if (other.uid < this.uid) {
            return of(other, this)
        }

        var current = this
        while (current.right is Types2TypeUnion && (current.right).uid < other.uid) {
            current = current.right as Types2TypeUnion
        }

        val next = current.right
        current.right = of(other, next)
        return this
    }

    init {
        if (type is Types2TypeUnion) throw Error("BUG: type cannot be union")
    }
}