package github.mrh0.eclang.context.atom

/** An atom (`:name`), the same instance for every use of that name in one program (see [Atoms]). */
class AtomInstance internal constructor(val label: String) {
    override fun toString() = ":$label"

    fun getId() = "__ec_atom_${label}"
}

/** The atoms of one program, by name; each becomes one C string constant. */
class Atoms {
    private val atoms: MutableMap<String, AtomInstance> = linkedMapOf()

    fun get(label: String): AtomInstance = atoms.getOrPut(label) { AtomInstance(label) }

    fun getAll(): Collection<AtomInstance> = atoms.values
}
