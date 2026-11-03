package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

data class EcTypeRecord(val name: String, val props: List<Pair<String, EcType>>, private val extending: String?, private val externalName: String?) : EcType(name) {
    val map: Map<String, EcType> = props.associate { it }

    override fun isReferenceType(): Boolean = true

    override fun getProperty(location: Loc, name: String): EcType = map[name] ?: super.getProperty(location, name)

    fun getSourceName(): String = externalName ?: name

    fun getExtending() = extending

    override fun toString() = name
}