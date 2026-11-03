package github.mrh0.ect2.core

object Types2None : Types2Type("core", "None", Types2Any) {
    override fun accepts(type: Types2Type) = false
}