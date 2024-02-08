package github.mrh0.eclang.types

class EcTypeCallSignature(val args: Array<EcTypeAny>, val ret: EcTypeAny) : EcTypeAny("Gs", "CallSignature") {
    override fun toString() = args.joinToString(",", "(", "): $ret") { it.toString() }
}