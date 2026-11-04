package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.data.TInterpolated
import github.mrh0.eclang.ast.token.data.TString
import github.mrh0.eclang.ast.token.statement.TStatementPool
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRRawCall
import github.mrh0.eclang.ir.data.IRClone
import github.mrh0.eclang.ir.data.IRFormat
import github.mrh0.eclang.util.StringLiteral
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*

/**
 * Functions the compiler provides, used when the program defines no function of that name. Those that allocate
 * use the current pool (`pool`), or the pool given as their optional first argument (for `clone`, the second):
 * - `format("x is ${x}")`: the interpolated String (a String literal with `${...}` on its own does the same);
 * - `concat(a, b, ...)`: the Strings joined;
 * - `clone(x)`: a deep copy of x (see CTypes.clone);
 * - `newPool()`: a new pool, a child of the current one (or of the given parent); `clearPool(p)` frees everything
 *   in p, `freePool(p)` frees p itself.
 */
internal object Builtins {
    private val names = setOf("format", "concat", "clone", "newPool", "clearPool", "freePool")

    fun has(name: String) = name in names

    /**
     * Calls builtin [name] with [args] (for `x.name(...)`, x is `args[0]`, already compiled as [first]). Adds to
     * [pools] each pool the result may be allocated in (null: the current pool), for Lifetimes.
     */
    fun call(cd: CompileData, location: Loc, name: String, args: List<ITok>, first: Pair<Type, IIR>?, pools: MutableList<ITok?>): Pair<Type, IIR> {
        val compiled = HashMap<Int, Pair<Type, IIR>>()
        first?.let { compiled[0] = it }
        fun arg(i: Int, hint: Type = Types.None) = compiled.getOrPut(i) { args[i].process(cd, hint) }
        fun isPoolArg(i: Int) = isPoolType(cd, cd.usable(location, arg(i), "argument ${i + 1} of `$name`").first)
        return when (name) {
            "format" -> when {
                args.size == 1 -> { pools += null; format(cd, location, current(location), args[0]) }
                args.size == 2 -> {
                    if (!isPoolArg(0)) throw EcError(location, "`format` takes a String with `\${...}` in it, and optionally the pool to allocate it in first: `format(\"x is \${x}\")` or `format(p, \"x is \${x}\")`. Its first argument here is a ${arg(0).first}, not a Pool.")
                    pools += args[0]
                    format(cd, location, pool(cd, location, "`$name`", arg(0)), args[1])
                }
                else -> throw EcError(location, "`format` takes one String literal with `\${...}` in it (and optionally a pool first), e.g. `format(\"x is \${x}\")`.")
            }
            "concat" -> {
                val explicit = args.isNotEmpty() && isPoolArg(0)
                val strings = if (explicit) args.indices.drop(1) else args.indices.toList()
                if (strings.size < 2) throw EcError(location, "`concat` joins two or more Strings: `concat(a, b)` (or `concat(p, a, b)` to allocate in pool p).")
                pools += if (explicit) args[0] else null
                val parts = strings.mapIndexed { i, index ->
                    val what = "String ${i + 1} of `concat`"
                    cd.expect(location, cd.usable(location, arg(index, Types.String), what), Types.String, what)
                }
                val into = if (explicit) pool(cd, location, "`$name`", arg(0)) else current(location)
                Types.String to parts.drop(1).fold(parts[0]) { joined, next -> IRRawCall(location, "__ec_str_concat", listOf(into, joined, next)) }
            }
            "clone" -> {
                if (args.size !in 1..2) throw EcError(location, "`clone` takes the value to copy, and optionally the pool to copy it into: `clone(x)` or `clone(x, p)`.")
                val value = cd.usable(location, arg(0), "`clone`")
                val type = cd.storable(location, value.first, "`clone`")
                pools += args.getOrNull(1)
                val into = if (args.size == 2) pool(cd, location, "`$name`", arg(1)) else current(location)
                type to IRClone(location, type, cd.expect(location, value, type, "`clone`"), into)
            }
            "newPool" -> {
                if (args.size > 1) throw EcError(location, "`newPool` takes no arguments (the new pool is a child of the current one), or the parent pool: `newPool(parent)`.")
                pools += args.firstOrNull()
                val parent = if (args.size == 1) pool(cd, location, "`$name`", arg(0)) else current(location)
                cd.types.pool to IRRawCall(location, "__ec_pool_create", listOf(parent))
            }
            else -> { // clearPool, freePool
                if (args.size != 1) throw EcError(location, "`$name` takes one pool: `$name(p)`.")
                if (name == "freePool") freeProblem(cd, args[0])?.let { throw EcError(location, it) }
                Types.None to IRRawCall(location, if (name == "freePool") "apr_pool_destroy" else "apr_pool_clear", listOf(pool(cd, location, "`$name`", arg(0))))
            }
        }
    }

    /** Why `freePool(tok)` would free a pool that something else frees, or null when it's fine. */
    private fun freeProblem(cd: CompileData, tok: ITok): String? {
        val t = tok.unnested() as? TNamed ?: return null
        if (TNamed.isCurrentPool(cd, t.name))
            return "Can't free the current pool: what is still running allocates in it. To free memory early, put the code in a `pool do` block (its pool is freed when the block ends), or use `clearPool(pool)`."
        if (cd.findVar(Util.testIdentifier(t.location, t.name))?.kind() == TStatementPool.KIND)
            return "'${t.name}' is ${TStatementPool.KIND}, which is freed when the block ends; freeing it here would free it twice. Use `clearPool(${t.name})` to free what is in it now."
        return null
    }

    /** `format(pool, template)`: [template] must be a String literal, with or without `${...}`. */
    private fun format(cd: CompileData, location: Loc, pool: IIR, template: ITok): Pair<Type, IIR> = when (val t = template.unnested()) {
        is TString -> t.process(cd, Types.None)
        is TInterpolated -> interpolate(cd, location, pool, t)
        else -> throw EcError(location, "`format` takes a String literal with `\${...}` in it, e.g. `format(\"x is \${x}\")`.")
    }

    /** The String [template] (`"x is ${x}"`) builds, allocated in [pool]. */
    fun interpolate(cd: CompileData, location: Loc, pool: IIR, template: TInterpolated): Pair<Type, IIR> {
        val format = StringBuilder()
        val args = mutableListOf<Pair<Type, IIR>>()
        template.parts.forEach { part ->
            if (part is StringLiteral) { format.append(part.toC().replace("%", "%%")); return@forEach }
            val value = cd.usable(location, (part as ITok).process(cd, Types.None), "`\${...}`")
            val type = value.first.let { if (it is AtomLiteral) Types.Atom else cd.types.concrete(it) }
            val spec = IRFormat.spec(type) ?: throw EcError(location, "Can't put a $type into a String with `\${...}`. " + when {
                type is Union && type.isNullable -> "It may be null: check it first, or write `\${x ?? default}`."
                type is Declared -> "Put its fields in instead, e.g. `\${p.x}`."
                else -> "Numbers, Bools, Chars, atoms, Strings and CStrings can be put in."
            })
            format.append(spec.first)
            args += type to cd.expect(location, value, type, "`\${...}`")
        }
        return Types.String to IRFormat(location, pool, format.toString(), args)
    }

    /** The current pool (`pool`), as C. */
    fun current(location: Loc): IIR = IRRaw(location, TNamed.CURRENT_POOL)

    /** The pool [tok] given to `new ... in` ([what]), checked to be a pool; the current pool when [tok] is null. */
    fun poolArgument(cd: CompileData, location: Loc, tok: ITok?, what: String): IIR =
        if (tok == null) current(location) else pool(cd, location, what, tok.process(cd, Types.None))

    /** A `Pool`: `@apr_pool_t`, also when a program declares `apr_pool_t` itself. */
    private fun isPoolType(cd: CompileData, t: Type) = t === cd.types.pool ||
        (t is Pointer && (t.target as? Record)?.let { (it.externalName ?: it.name) == "apr_pool_t" } == true)

    /** Checks [value] is a pool (a `Pool`) to allocate the result of [name] in. */
    private fun pool(cd: CompileData, location: Loc, name: String, value: Pair<Type, IIR>): IIR {
        val t = cd.usable(location, value, "the pool of $name").first
        if (t is Union && t.isNullable && isPoolType(cd, cd.types.nonNull(t)))
            throw EcError(location, "The pool given to $name may be null. Check it first with `if p != null`, or assert it with `p!`.")
        if (!isPoolType(cd, t))
            throw EcError(location, "$name needs a pool (a `Pool`) to allocate in, but this is a ${cd.types.concrete(t)}. Leave it out to use the current pool (`pool`), or create one with `newPool()` or a `pool p do` block.")
        return value.second
    }
}
