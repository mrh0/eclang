package github.mrh0.eclang.types

class EcTypeCallSignature(val args: Array<EcType>, val ret: EcType) : EcType("Ec", "CallSignature") {
    override fun toString() = args.joinToString(",", "(", "): $ret") { it.toString() }
}