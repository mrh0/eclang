package github.mrh0.ect2.core

class Types2TypeUnion private constructor(val type: Types2Type, val right: Types2Type) : Types2Type("core", getUnionId(type, right), type) {
    companion object {
        fun getUnionId(type: Types2Type, right: Types2Type) = "${type.uniqueName}|${right.uniqueName}"
        fun of(type: Types2Type, right: Types2Type): Types2TypeUnion = ALL_TYPES.getOrPut(getUnionId(type, right)) { Types2TypeUnion(type, right) } as Types2TypeUnion
    }

    init {
        if (type is Types2TypeUnion) throw Error("BUG: type cannot be union")
    }
}