package github.mrh0.eclang.context

/**
 * A namespace. Each file belongs to the module it names with `module "name"`, or to the top level ([name] "")
 * when it names none. Code sees its own module's names and the top level's; other modules' names are written
 * qualified, `name.member`.
 */
class Module(val name: String) {
    /** The module's global variables and constants. */
    val globals = ContextBuilder("__globals_$name")

    val isTopLevel get() = name.isEmpty()

    /** Names declared with `priv` (by kind), which only code of this module can use. */
    val privateFunctions = HashSet<String>()
    val privateGlobals = HashSet<String>()
    val privateTypes = HashSet<String>()

    /** The C name of a definition called [member] in this module: `fib__add` in module fib, `add` at the top level. */
    fun cName(member: String) = if (isTopLevel) member else "${name}__$member"

    override fun toString() = if (isTopLevel) "the top level" else "module '$name'"

    companion object {
        /** Why [name] can't name a module, or null when it can. */
        fun problem(name: String): String? = when {
            !Regex("[a-zA-Z][a-zA-Z0-9_]*").matches(name) -> "a module name must start with a letter and contain only letters, digits and _"
            "__" in name -> "a module name can't contain `__` (it separates the module from the name in C)"
            else -> null
        }
    }
}
