package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

data class EcTypeRecord(val name: String, val props: List<Pair<String, EcType>>, private val externalName: String?) : EcType("Record") {
    val map: Map<String, EcType> = props.associate { it }
    override fun accepts(location: Loc, type: EcType): Boolean = type == this

    override fun isReferenceType(): Boolean = true

    override fun getProperty(location: Loc, name: String): EcType? = map[name]

    fun getSourceName(): String = externalName ?: name
}