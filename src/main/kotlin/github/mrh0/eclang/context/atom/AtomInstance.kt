package github.mrh0.eclang.context.atom

class AtomInstance private constructor(val label: String) {
    override fun toString() = ":${label.lowercase()}"

     companion object {
        private val atoms: MutableMap<String, AtomInstance> = mutableMapOf()

        fun get(label: String): AtomInstance {
            val normalized = label.lowercase()
            return atoms.getOrPut(normalized) { AtomInstance(normalized) }
        }

        fun getAll() = atoms.values
    }

    fun getId() = "__ec_atom_${label}"
}