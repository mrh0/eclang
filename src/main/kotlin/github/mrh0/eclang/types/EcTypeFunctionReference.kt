package github.mrh0.eclang.types

data class EcTypeFunctionReference(val name: String) : EcType("FunctionReference") {
    override fun toString() = "fnRef($name)"
}