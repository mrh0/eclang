package github.mrh0.eclang.types

data class EcTypeCallSignature(val args: Array<EcType>, val ret: EcType) : EcType("CallSignature") {
    override fun toString() = args.joinToString(",", "(", "): $ret") { it.toString() }
}