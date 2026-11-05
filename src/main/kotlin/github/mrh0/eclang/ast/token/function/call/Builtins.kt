package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.data.TInterpolated
import github.mrh0.eclang.ast.token.data.TString
import github.mrh0.eclang.ast.token.op.equalityProblem
import github.mrh0.eclang.ast.token.statement.TStatementPool
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRRawCall
import github.mrh0.eclang.ir.data.IRClone
import github.mrh0.eclang.ir.data.IRFormat
import github.mrh0.eclang.ir.data.IRHash
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.eclang.ir.data.IRShow
import github.mrh0.eclang.util.StringLiteral
import github.mrh0.ect2.*

/**
 * Functions the compiler provides, used when the program defines no function of that name. Those that allocate
 * use the current pool (`pool`), or the pool given as their optional first argument (for `clone`, the second):
 * - `format("x is ${x}")`: the interpolated String (a String literal with `${...}` on its own does the same);
 * - `concat(a, b, ...)`: the Strings joined;
 * - `clone(x)`: a deep copy of x (see CTypes.clone);
 * - `hash(x)`: a hash of x that fits `==` (see CTypes.hash), also when other `hash` functions exist but none takes x;
 * - `newPool()`: a new pool, a child of the current one (or of the given parent); `clearPool(p)` frees everything
 *   in p, `freePool(p)` frees p itself;
 * - `cstr(s)`: a copy of the String s that ends with a 0 byte, as a CString for C functions.
 */
internal object Builtins {
    private val names = setOf("format", "concat", "clone", "hash", "newPool", "clearPool", "freePool", "cstr")

    fun has(name: String) = name in names

    /**
     * Calls builtin [name] with [args] (for `x.name(...)`, x is `args[0]`, already compiled as [first]). Adds to
     * [pools] each pool the result may be allocated in (null: the current pool), and to [kept] each argument the
     * result may still point into, for Lifetimes.
     */
    fun call(cd: CompileData, location: Loc, name: String, args: List<ITok>, first: Pair<Type, IIR>?, pools: MutableList<ITok?>, kept: MutableList<ITok>): Pair<Type, IIR> {
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
                if (CTypes.cloneKeepsPointers(type)) kept += args[0]
                val into = if (args.size == 2) pool(cd, location, "`$name`", arg(1)) else current(location)
                type to IRClone(location, type, cd.expect(location, value, type, "`clone`"), into)
            }
            "hash" -> {
                if (args.size != 1) throw EcError(location, "`hash` takes the value to hash: `hash(x)`.")
                val value = cd.usable(location, arg(0), "`hash`")
                val type = cd.storable(location, value.first, "`hash`")
                equalityProblem(type)?.let { throw EcError(location, "`hash` can't be generated for a $type, because `==` can't compare it: $it. Define one yourself: `fn hash(x: $type): ULong`.") }
                Types.ULong to IRHash(location, type, cd.expect(location, value, type, "`hash`"))
            }
            "cstr" -> {
                val explicit = args.size == 2
                if (args.size !in 1..2 || (explicit && !isPoolArg(0)))
                    throw EcError(location, "`cstr` takes a String, and optionally the pool to copy it into first: `cstr(s)` or `cstr(p, s)`.")
                val what = "the String given to `cstr`"
                val string = cd.expect(location, cd.usable(location, arg(if (explicit) 1 else 0, Types.String), what), Types.String, what)
                pools += if (explicit) args[0] else null
                val into = if (explicit) pool(cd, location, "`$name`", arg(0)) else current(location)
                Types.CString to IRRawCall(location, "__ec_str_cstr", listOf(into, string))
            }
            "newPool" -> {
                if (args.size > 1) throw EcError(location, "`newPool` takes no arguments (the new pool is a child of the current one), or the parent pool: `newPool(parent)`.")
                pools += args.firstOrNull()
                val parent = if (args.size == 1) pool(cd, location, "`$name`", arg(0)) else current(location)
                cd.types.pool to IRRawCall(location, "__ec_pool_create", listOf(parent))
            }
            else -> { // clearPool, freePool
                if (args.size != 1) throw EcError(location, "`$name` takes one pool: `$name(p)`.")
                val pool = pool(cd, location, "`$name`", arg(0))
                (if (name == "freePool") freeProblem(cd, args[0]) else clearProblem(cd, args[0]))?.let { throw EcError(location, it) }
                val call = IRRawCall(location, if (name == "freePool") "apr_pool_destroy" else "apr_pool_clear", listOf(pool))
                Lifetimes.kill(cd, location, args[0], free = name == "freePool")
                Types.None to call
            }
        }
    }

    /** Why `freePool(tok)` would free a pool that something else frees, or null when it's fine. */
    private fun freeProblem(cd: CompileData, tok: ITok): String? {
        val id = Lifetimes.poolId(cd, tok) ?: return null
        val shown = Lifetimes.shown(tok)
        val fix = "To free memory early, put the code in a `pool do` block (its pool is freed when the block ends), or allocate in a pool of your own (`val p = newPool()`) and free that."
        if ((tok.unnested() as? TNamed)?.let { TNamed.isCurrentPool(cd, it.name) } == true) return "Can't free the current pool: what is still running allocates in it. $fix"
        if (id == Lifetimes.CALLER_POOL) return "Can't free '$shown': it holds the current pool of this function, which its callers allocate in too. $fix"
        if (id in cd.ctx().stack.blockPools) return "'$shown' is ${TStatementPool.KIND}, which is freed when the block ends; freeing it here would free it twice. Use `clearPool($shown)` to free what is in it now."
        return null
    }

    /** Why `clearPool(tok)` would free what code outside this function holds, or null when it's fine. */
    private fun clearProblem(cd: CompileData, tok: ITok): String? {
        val id = Lifetimes.poolId(cd, tok) ?: return null
        if (id != Lifetimes.CALLER_POOL) {
            // Clearing a pool frees the pools made in it, and a block still running frees its own when it ends.
            val running = cd.poolBlocks.any { it.pool != id && it.pool in cd.ctx().stack.freedWith(id) }
            return if (running) "Can't clear '${Lifetimes.shown(tok)}' here: a `pool` block inside it is still running, and clearing it would free that block's pool too. Clear it after that block." else null
        }
        val what = if ((tok.unnested() as? TNamed)?.let { TNamed.isCurrentPool(cd, it.name) } == true) "the current pool here" else "'${Lifetimes.shown(tok)}', the current pool of this function"
        return "Can't clear $what: it belongs to the callers too, so this would also free everything they hold in it. Put the code in a `pool do` block (its pool is freed when the block ends), or allocate in a pool of your own (`val p = newPool()`) and clear that."
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
        val readers = mutableListOf<(String) -> String>()
        template.parts.forEach { part ->
            if (part is StringLiteral) {
                // A 0 in a printf format would end it, so it is printed as a value.
                part.splitAtZero().forEachIndexed { i, text ->
                    if (i > 0) { format.append("%c"); args += Types.Char to IRNumber(location, 0, Types.Char); readers += { a -> a } }
                    format.append(text.toC().replace("%", "%%"))
                }
                return@forEach
            }
            val field = part as TInterpolated.Field
            val value = cd.usable(location, field.value.process(cd, Types.None), "`\${...}`")
            val type = value.first.let { if (it is AtomLiteral || isAtoms(it)) Types.Atom else cd.types.concrete(it) }
            val printed = printable(type)
            val shown = if (printed) Types.String else type
            val spec = IRFormat.spec(shown) ?: throw EcError(location, "Can't put a $type into a String with `\${...}`. " + when {
                type is Union && type.isNullable -> "It may be null: check it first, or write `\${x ?? default}`."
                type is Declared -> "Put its fields in instead, e.g. `\${p.x}`."
                else -> "Numbers, Bools, Chars, atoms, Strings, CStrings, `T[N]` arrays, records and tuples can be put in."
            })
            val (conversion, read) = field.spec?.apply(location, shown, spec) ?: spec
            format.append(conversion)
            readers += read
            val converted = cd.expect(location, value, type, "`\${...}`")
            if (printed) cd.functions.resolvePrinters(location, type)
            if (printed) printsItself(cd, field, type)?.let { throw EcError(location, it) }
            args += shown to if (printed) IRShow(location, type, converted) else converted
        }
        return Types.String to IRFormat(location, pool, format.toString(), args, readers)
    }

    /** Whether [t] is a union of atoms, which C holds as one atom. */
    private fun isAtoms(t: Type) = t is Union && t.members.all { it is AtomLiteral || it === Types.Atom }

    /** Whether `${...}` prints a [t] with CTypes.show: records and tuples, and pointers to them. */
    private fun printable(t: Type): Boolean = when (t) {
        is Distinct -> printable(t.base)
        is Record -> !t.isExternal
        is Tuple, is FixedArray -> true
        is Pointer -> printable(t.target)
        else -> false
    }

    /** Why [field], of type [t], would call the function `str` it is in with its own parameter, forever; or null. */
    private fun printsItself(cd: CompileData, field: TInterpolated.Field, t: Type): String? {
        val self = cd.function ?: return null
        val name = (field.value.unnested() as? TNamed)?.name ?: return null
        if (cd.functions.printers[t] !== self || self.params.singleOrNull()?.name != name) return null
        return "`\${$name}` here calls this same `str` with the same value, which would never end. Put its fields in instead, e.g. `\${$name.x}`."
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
