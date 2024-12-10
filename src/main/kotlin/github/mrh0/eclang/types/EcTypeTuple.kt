package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

open class EcTypeTuple(val types: Array<EcType>) : EcType("Tuple") {
    companion object {
        fun isThrowableTuple(type: EcType): Boolean {
            if (type !is EcTypeTuple) return false
            if (type.types.size != 2) return false
            // if (!type.types[1].isReferenceType()) return false
            return true
        }
    }
    override fun accepts(location: Loc, type: EcType): Boolean = type == this

    override fun isReferenceType() = true
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EcTypeTuple

        return types.contentEquals(other.types)
    }

    override fun hashCode(): Int {
        return types.contentHashCode()
    }

}