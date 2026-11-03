package github.mrh0.ect2.core

object Types2Any : Types2Type("core", "Any", null) {
    override fun accepts(type: Types2Type) = true
}