package github.mrh0.eclang.output.c

import github.mrh0.eclang.context.function.Overload
import github.mrh0.eclang.ir.data.IRFormat
import github.mrh0.ect2.*

/**
 * Decides how every eclang type is represented in C, and writes the C definitions and helper functions that
 * the generated code needs. This is the only place that knows the representation rules:
 *
 * - Numbers, Bool, CString, pointers and C arrays map directly to their C types.
 * - `T?` where T is already a pointer (`@T?`, `CString?`, ...) is a plain C pointer that may be NULL, and so is
 *   any union of atoms. These are "plain" unions.
 * - Every other union is a tagged union: `struct { int tag; union { A m0; B m1; } v; }`, where `tag` is the
 *   index of the member in [Union.members].
 * - Arrays, tuples and `T throws E` results are small generated structs. Records are structs. A record that extends
 *   another holds its parent as its first member, named like the parent (`struct Dog { Animal Animal; bool good; }`, so
 *   `d.Animal.legs`; see [fieldPath]): C lets a pointer to a struct be used as a pointer to its first member, which a
 *   copy of the parent's fields wouldn't allow (gcc's `-O2` assumes two unrelated structs don't overlap). The record at
 *   the top of a hierarchy starts with a hidden `__ec_tid` type tag, so every record of it starts with the tag.
 * - A function value (`(A) => R`) is a pointer to a closure: a struct whose first member is the C function, called with
 *   the closure itself first (`f->call(f, a)`), so that a lambda finds the variables it uses after it ([closureStruct]).
 *   A C function type (from a `declare`, [FunctionType.isC]) is a plain C function pointer.
 * - `T[N]` is a struct with one C array in it (`__ec_fixed_Int_4`), so that C can assign and return it, except as a
 *   record field: that is a C array (`int d[4];`) as C code expects, read through a cast of it to the struct ([member]).
 *
 * Types are named and defined lazily: asking for [cName] schedules the definition, and [definitions] writes
 * them all in an order where every struct is complete before it is used by value.
 */
class CTypes(private val types: TypeContext, private val printers: Map<Type, Overload?> = mapOf(), private val library: Boolean = false) {
    /** `--check-memory` (see CompileData.checkMemory): a `malloc` per allocation, and `#line`s before statements. */
    var checkMemory = false

    private val names = HashMap<Type, String>()
    private val scheduled = LinkedHashSet<Type>()
    private val completed = HashSet<Type>()
    private val inProgress = HashSet<Type>()
    private val forward = StringBuilder()
    private val bodies = StringBuilder()
    private val helpers = LinkedHashMap<String, String?>()
    private val mangled = HashMap<String, Type>()

    /**
     * Atoms the C refers to (by label), from code and helpers: the program defines only those, as gcc's
     * `-Wunused-const-variable` rejects the others (an unused member of a `declare type`).
     */
    val usedAtoms = LinkedHashSet<String>()

    // ---------------------------------------------------------------------------------------------------
    // Representation rules.

    /** Types that are a single C pointer. */
    fun isPointerLike(t: Type): Boolean = Companion.isPointerLike(t)

    /** A union stored as a plain C pointer (NULL meaning null) instead of a tagged struct. */
    fun isPlain(u: Union): Boolean = Companion.isPlain(u)

    fun isTagged(t: Type) = t is Union && !isPlain(t)

    // ---------------------------------------------------------------------------------------------------
    // Names.

    /** The C spelling of [t], e.g. `int`, `char*`, `__ec_union_Int_CString`. */
    fun cName(t: Type): String = when (t) {
        is Prim -> primName(t)
        is IntLiteral, is FloatLiteral -> cName(types.concrete(t))
        is AtomLiteral -> "char*"
        is Pointer -> if (t.target === Types.Any || t.target === Types.None) "void*" else "${cName(t.target)}*"
        is ArrayType -> if (t.isC) "${cName(t.element)}*" else struct(t, "array")
        is FixedArray -> struct(t, "fixed")
        is Tuple -> struct(t, "tuple")
        is Union -> if (isPlain(t)) t.members.firstOrNull { it !== Types.Null }?.let { cName(it) } ?: "void*" else struct(t, "union")
        is Result -> struct(t, "result")
        is FunctionType -> names[t] ?: (if (t.isC) "__ec_cfn_${mangle(t)}" else "__ec_fn_${mangle(t)}").also { name ->
            names[t] = name
            scheduled += t
            if (!t.isC) "__ec_closure_${name.removePrefix("__ec_fn_")}".let { forward.append("typedef struct $it $it;\ntypedef $it* $name;\n") }
        }
        is Record -> recordName(t)
        is Distinct -> distinctName(t)
        is Interface, is TypeParam, is Applied, is GenericRecord -> error("BUG: ${t} has no C representation")
    }

    private fun primName(t: Prim) = when (t) {
        Types.Bool -> "bool"
        Types.Char -> "char"; Types.Short -> "short"; Types.Int -> "int"; Types.Long -> "long"
        Types.UChar -> "unsigned char"; Types.UShort -> "unsigned short"; Types.UInt -> "unsigned int"
        Types.ULong -> "unsigned long"; Types.Size -> "size_t"
        Types.Float -> "float"; Types.Double -> "double"
        Types.CString, Types.Atom -> "char*"
        Types.String -> "__ec_string_t"
        Types.Null -> "void*"
        else -> "void" // Any, None, Never
    }

    /** The typedef of a `distinct` type. It goes with the forward declarations: an array of it only names it. */
    private fun distinctName(t: Distinct): String = names[t] ?: t.cName.also { name ->
        names[t] = name
        if (t.base is FunctionType) scheduled += t else forward.append("typedef ${cName(t.base)} $name;\n")
    }

    private fun recordName(r: Record): String {
        val name = r.externalName ?: r.cName
        if (r.isExternal) usesCTypes = true
        if (r.isCStruct) return "${keyword(r)} $name"
        if (!r.isExternal) struct(r, null)
        return name
    }

    /** Whether anything named so far is a type from a C header. */
    var usesCTypes = false
        private set

    private fun keyword(r: Record) = if (r.isUnion) "union" else "struct"

    /** Names a generated struct, forward-declares it and schedules its definition. */
    private fun struct(t: Type, kind: String?): String = names.getOrPut(t) {
        val name = if (kind == null) (t as Record).cName else "__ec_${kind}_${mangle(t)}"
        forward.append("typedef ${if (t is Record) keyword(t) else "struct"} $name $name;\n")
        scheduled += t
        name
    }

    /** A readable, C-safe name fragment for [t], used in generated type names. Falls back to the id when long. */
    fun mangle(t: Type): String {
        if (t is IntLiteral || t is FloatLiteral) return mangle(types.concrete(t))
        val m = when (t) {
            is Prim -> t.name
            is Pointer -> "P" + mangle(t.target)
            is ArrayType -> (if (t.isC) "C" else "A") + mangle(t.element)
            is FixedArray -> "${mangle(t.element)}_${t.size}"
            is Declared -> t.cName
            is Distinct -> t.cName
            is Union -> t.members.joinToString("_") { mangle(it) }
            is Tuple -> t.elements.joinToString("_") { mangle(it) }
            is Result -> mangle(t.value) + "_" + mangle(t.error)
            is FunctionType -> (if (t.isC) "c" else "") + t.params.joinToString("_") { mangle(it) } + "_to_" + mangle(t.returns) + (t.throws?.let { "_throws_" + mangle(it) } ?: "")
            is AtomLiteral -> "a" + t.name
            is TypeParam, is Applied, is GenericRecord -> "t${t.id}"
            is IntLiteral, is FloatLiteral -> error("BUG: handled above")
        }
        // Different types can read the same, such as `(Int, (Int, Int))` and `((Int, Int), Int)`.
        val name = if (m.length <= 40) m else "t${t.id}"
        return if (mangled.getOrPut(name) { t } === t) name else "t${t.id}"
    }

    /** The struct a function value of type [t] points to: `struct __ec_closure_M { R (*call)(void*, A); }`. A lambda's adds its captured variables. */
    fun closureStruct(t: FunctionType): String = "__ec_closure_" + cName(t).removePrefix("__ec_fn_")

    /** What a C function a function value of type [t] calls returns: R, or the result struct when it `throws`. */
    fun closureReturns(t: FunctionType): Type = t.throws?.let { types.result(t.returns, it) } ?: t.returns

    /**
     * Calls a function value of type [t] that isn't a plain name, which `f->call(f, ...)` would compute twice:
     * `static inline R __ec_call_M(__ec_fn_M f, A0 a0) { return f->call(f, a0); }`.
     */
    fun closureCall(t: FunctionType): String = helper("__ec_call_" + cName(t).removePrefix("__ec_fn_")) { name ->
        val returns = closureReturns(t)
        val params = t.params.mapIndexed { i, p -> "${cName(p)} a$i" }
        val args = (listOf("f") + t.params.indices.map { "a$it" }).joinToString(", ")
        val ret = if (returns === Types.None || returns === Types.Never) "" else "return "
        "static inline ${cName(returns)} $name(${(listOf("${cName(t)} f") + params).joinToString(", ")}) {\n\t${ret}f->call($args);\n}\n"
    }

    // ---------------------------------------------------------------------------------------------------
    // Definitions.

    /** Makes sure the full definition of [t] is written before anything that uses it by value. */
    private fun complete(t: Type) {
        if (t in completed || t in inProgress) return
        inProgress += t
        when (t) {
            is ArrayType -> if (!t.isC) bodies.append("struct ${cName(t)} { size_t len; ${cName(t.element)}* data; };\n")
            is FixedArray -> {
                complete(t.element)
                bodies.append("struct ${cName(t)} { ${cName(t.element)} data[${t.size}]; };\n")
            }
            is Tuple -> {
                val fields = t.elements.mapIndexed { i, e -> complete(e); "${cName(e)} ${tupleField(i)};" }
                bodies.append("struct ${cName(t)} { ${fields.joinToString(" ")} };\n")
            }
            is Union -> if (isTagged(t)) {
                val payload = t.members.mapIndexedNotNull { i, m ->
                    if (m === Types.Null) null else { complete(m); "${cName(m)} m$i;" }
                }
                bodies.append("struct ${cName(t)} { int tag; union { ${payload.joinToString(" ")} } v; }; /* $t */\n")
            } else t.members.filterIsInstance<FunctionType>().forEach { complete(it) } // Held as the function's typedef, see cName.
            is Result -> {
                complete(t.error)
                val value = if (t.value === Types.None) "" else { complete(t.value); "${cName(t.value)} value; " }
                bodies.append("struct ${cName(t)} { int code; union { $value${cName(t.error)} error; }; }; /* $t */\n")
            }
            is FunctionType -> {
                t.params.forEach { complete(it) }
                val returns = t.throws?.let { types.result(t.returns, it) } ?: t.returns
                complete(returns)
                if (t.isC) {
                    val params = if (t.params.isEmpty()) "void" else t.params.joinToString(", ") { cName(it) }
                    bodies.append("typedef ${cName(returns)} (*${cName(t)})($params);\n")
                } else {
                    val params = (listOf("void*") + t.params.map { cName(it) }).joinToString(", ")
                    bodies.append("struct ${closureStruct(t)} { ${cName(returns)} (*call)($params); };\n")
                }
            }
            is Distinct -> {
                complete(t.base)
                if (t.base is FunctionType) bodies.append("typedef ${cName(t.base)} ${cName(t)};\n")
            }
            is Record -> if (!t.isExternal) {
                val parent = t.parent?.let { complete(it); "\t${cName(it)} ${parentMember(t)};\n" } ?: ""
                val fields = t.ownFieldMap.map { (name, type) -> complete(type); "\t${fieldDeclaration(t, name, type)};\n" }
                val tid = if (t.inHierarchy && t.parent == null) "\tint __ec_tid;\n" else ""
                bodies.append("${keyword(t)} ${attributes(t.layout)}${t.cName} {\n$tid$parent${fields.joinToString("")}};\n")
            }
            else -> {}
        }
        inProgress -= t
        completed += t
    }

    /** The field [name] of [r] in its C definition: `int d[4]`, `unsigned int flags : 3` or `int x`. */
    private fun fieldDeclaration(r: Record, name: String, type: Type): String {
        val fixed = fixedOf(type)
        return when {
            fixed != null -> "${cName(fixed.element)} $name[${fixed.size}]"
            name in r.bitWidths -> "${cName(type)} $name : ${r.bitWidths[name]}"
            else -> "${cName(type)} $name"
        }
    }

    private fun attributes(layout: Layout): String {
        val attributes = listOfNotNull("packed".takeIf { layout.packed }, layout.align?.let { "aligned($it)" })
        return if (attributes.isEmpty()) "" else "__attribute__((${attributes.joinToString(", ")})) "
    }

    /**
     * The field [field] of [r] in the C value [base], as a [type]. A C array is read as the struct eclang holds a `T[N]`
     * in, which has the same bytes; the result stays assignable. See [storage].
     */
    fun member(r: Record, base: String, field: String, type: Type): String =
        "$base.${fieldPath(r, field)}".let { if (fixedOf(type) != null) storage(it, type, r.layout.packed) else it }

    /** The C array [expr] as the [type] (a `T[N]`) it holds. In a [packed] record the array may be at any address. */
    fun storage(expr: String, type: Type, packed: Boolean): String = "(*(${if (packed) unalignedName(type) else cName(type)}*)$expr)"

    /** [type] at any address: a typedef, so values of it are values of [type] to C, and C reads it byte by byte where it must. */
    private fun unalignedName(type: Type): String = "${cName(type)}_u".also { unaligned += type }

    private val unaligned = LinkedHashSet<Type>()

    /** A helper giving the record [r] with its `T[N]` field [field] set: a C array can't be given a value in a compound literal. */
    fun setFixed(r: Record, field: String, type: Type): String = helper("__ec_set_${r.cName}__$field") { name ->
        val c = cName(r)
        "static inline $c $name($c r, ${cName(type)} v) {\n\t${member(r, "r", field, type)} = v;\n\treturn r;\n}\n"
    }

    fun tupleField(index: Int) = github.mrh0.eclang.util.Util.getTupleEntryName(index, 0)

    /** Forward declarations, then every type definition, in dependency order. */
    fun definitions(): String {
        // Scheduling can discover more types while completing, so loop until nothing new appears.
        var done = 0
        while (done < scheduled.size) {
            val next = scheduled.drop(done)
            next.forEach { complete(it) }
            done += next.size
        }
        unaligned.forEach { bodies.append("typedef ${cName(it)} ${cName(it)}_u __attribute__((aligned(1)));\n") }
        return "$forward\n$bodies"
    }

    /** Helper functions requested by the generated code (conversions, runtime checks). */
    fun helperFunctions(): String {
        // Writing a helper can request another helper, so keep going until all are written.
        while (helpers.values.any { it == null }) {
            helpers.keys.filter { helpers[it] == null }.forEach { key -> helpers[key] = helperBodies.remove(key)!!(key) }
        }
        // Any helper can call any other, wherever they end up: each one starts with its signature.
        return prototypes.toString() + helpers.values.joinToString("") { it!!.substringBefore(" {") + ";\n" } + helpers.values.joinToString("")
    }

    /** What the helpers share (and the program's functions they call), written before all helpers. */
    private val prototypes = StringBuilder()

    private val helperBodies = HashMap<String, (name: String) -> String>()

    /** Registers a helper C function named [name] (once) and returns its name. [body] writes it, given the name. */
    private fun helper(name: String, body: (name: String) -> String): String {
        if (name !in helpers) { helpers[name] = null; helperBodies[name] = body }
        return name
    }

    // ---------------------------------------------------------------------------------------------------
    // Values and conversions. All of these return C expressions as strings; [where] is a "file:line" string
    // literal used in runtime error messages.

    fun tagOf(u: Union, member: Type) = u.members.indexOf(member)

    /** `(U){.tag = i, .v.mi = expr}`: puts a value of [member] into the tagged union [u]. */
    fun wrap(u: Union, member: Type, expr: String?): String {
        val i = tagOf(u, member)
        return if (member === Types.Null || expr == null) "((${cName(u)}){.tag = $i})" else "((${cName(u)}){.tag = $i, .v.m$i = $expr})"
    }

    /**
     * Converts [expr] from [from] to [to]. For implicit conversions this only ever widens. With [explicit]
     * (`as`) it also narrows numbers, and unwraps unions and downcasts records with a runtime check.
     */
    fun convert(from: Type, to: Type, expr: String, where: String, explicit: Boolean = false): String {
        if (from === to) return expr
        // A call that never returns, used where a value is expected: `x ?? panic("...")`. The value only satisfies C.
        if (from === Types.Never) return "($expr, ${if (isScalar(to)) "(${cName(to)})0" else zero(to)})"
        // The same C value as its base, except as a member of a union, which holds it under its own type.
        if (from is Distinct && to !is Union) return convert(from.base, to, expr, where, explicit)
        if (to is Distinct && from !is Union) return convert(from, to.base, expr, where, explicit)
        val literal = from is IntLiteral || from is FloatLiteral
        return when {
            to === Types.Any -> expr
            to is Union && isTagged(to) -> when {
                from is Union -> converter(from, to, expr, where)
                else -> {
                    val member = types.memberFor(from, to) ?: error("BUG: $from does not fit $to")
                    wrap(to, member, if (member === Types.Null) null else convert(from, member, expr, where))
                }
            }
            to is Union -> when { // plain union: same representation as its pointer member
                from === Types.Null -> "NULL"
                from is Union && isTagged(from) -> converter(from, to, expr, where)
                else -> expr
            }
            literal -> if (explicit) "((${cName(to)})$expr)" else expr
            from is Union && isTagged(from) -> converter(from, to, expr, where)
            from is Union -> when { // plain union to its member: the same pointer, checked when explicit (`x!`, `as`)
                to === Types.Null -> "NULL"
                explicit && from.isNullable && isPointerLike(to) -> "((${cName(to)})__ec_nn((void*)($expr), $where))"
                else -> expr
            }
            from is Record && to is Record -> "${upcast(from, to)}($expr)"
            from is Pointer && to is Pointer -> {
                val f = from.target; val t = to.target
                when {
                    t === Types.Any || t === Types.None -> expr
                    f is Record && t is Record && f.distanceTo(t) != null -> "((${cName(to)})($expr))"
                    f is Record && t is Record && t.distanceTo(f) != null && explicit -> "((${cName(to)})${downcast(t)}($expr, $where))"
                    else -> "((${cName(to)})($expr))"
                }
            }
            from === Types.String && to is ArrayType -> "${stringToArray(to)}($expr)"
            from === Types.String && to is FixedArray -> "${stringToFixed(to)}($expr, $where)"
            from is Prim && to is Prim -> if (explicit) "((${cName(to)})($expr))" else expr
            else -> if (explicit) "((${cName(to)})($expr))" else expr
        }
    }

    /**
     * Reads a value of union type [from] as the narrower type [to], when flow analysis already proved it
     * (`if x != null`, `if x is Int`). No runtime check is needed for a single member.
     */
    fun narrow(from: Type, to: Type, expr: String, where: String): String = when {
        from is Union && isTagged(from) && to in from.members -> "($expr).v.m${tagOf(from, to)}"
        from is Union && to in from.members && to !== Types.Null -> expr // a plain union holds its member as is
        else -> convert(from, to, expr, where, explicit = true)
    }

    /** A helper comparing two [r] records field by field (`a == b`); see `equalityProblem` for the fields it allows. */
    fun recordEquals(r: Record): String = helper("__ec_eq_${r.cName}") { name ->
        val checks = r.fields.map { (field, t) -> equals(t, member(r, "a", field, t), member(r, "b", field, t)) }
        val body = if (checks.isEmpty()) "(void)a; (void)b;\n\treturn true;" else "return ${checks.joinToString("\n\t\t&& ")};"
        "static inline bool $name(${cName(r)} a, ${cName(r)} b) {\n\t$body\n}\n"
    }

    /** A helper comparing two [t] tuples part by part (`a == b`). */
    fun tupleEquals(t: Tuple): String = helper("__ec_eq_${mangle(t)}") { name ->
        val checks = t.elements.mapIndexed { i, e -> equals(e, "a.${tupleField(i)}", "b.${tupleField(i)}") }
        "static inline bool $name(${cName(t)} a, ${cName(t)} b) {\n\treturn ${checks.joinToString("\n\t\t&& ")};\n}\n"
    }

    /** A helper comparing two [t] element by element (`a == b`). */
    fun fixedEquals(t: FixedArray): String = helper("__ec_eq_${mangle(t)}") { name ->
        "static inline bool $name(${cName(t)} a, ${cName(t)} b) {\n\tfor (size_t i = 0; i < ${t.size}; i++) if (!(${equals(t.element, "a.data[i]", "b.data[i]")})) return false;\n\treturn true;\n}\n"
    }

    /** C that compares [a] and [b] of type [t] like `==` does. */
    private fun equals(t: Type, a: String, b: String): String = when {
        t is Distinct -> equals(t.base, a, b)
        t === Types.CString -> "strcmp($a, $b) == 0"
        t === Types.String -> "__ec_str_eq($a, $b)"
        t is Record -> "${recordEquals(t)}($a, $b)"
        t is Tuple -> "${tupleEquals(t)}($a, $b)"
        t is FixedArray -> "${fixedEquals(t)}($a, $b)"
        t is Union && t.members.contains(Types.CString) -> "($a == $b || ($a != NULL && $b != NULL && strcmp($a, $b) == 0))"
        else -> "$a == $b"
    }

    // ---------------------------------------------------------------------------------------------------
    // `hash(x)`: fits `==`, so it takes the same types (see `equalityProblem`) and equal values get equal hashes.

    /** C that hashes [expr], a [t]. */
    fun hash(t: Type, expr: String): String = "__ec_hash_mix(0, ${hashed(t, expr)})".also { hashRuntime() }

    /** The bits of [expr] that `==` looks at: the content of text, a number's value (so 0.0 and -0.0 are the same), and a pointer's address. */
    private fun hashed(t: Type, expr: String): String = when {
        t is Distinct -> hashed(t.base, expr)
        t === Types.String -> "__ec_hash_bytes($expr.data, $expr.len)"
        t === Types.CString || (t is Union && t.members.contains(Types.CString)) -> "__ec_hash_cstr($expr)"
        t === Types.Float || t === Types.Double -> "__ec_hash_float($expr)"
        t is Record || t is Tuple || t is FixedArray -> "${hasher(t)}($expr)"
        else -> "(uint64_t)(uintptr_t)($expr)"
    }

    /** A helper hashing a record or tuple field by field, like [recordEquals] compares it (padding has no fixed value). */
    private fun hasher(t: Type): String = helper("__ec_hash_${mangle(t)}") { name ->
        hashRuntime()
        if (t is FixedArray) return@helper "static inline uint64_t $name(${cName(t)} v) {\n\tuint64_t h = 0;\n" +
            "\tfor (size_t i = 0; i < ${t.size}; i++) h = __ec_hash_mix(h, ${hashed(t.element, "v.data[i]")});\n\treturn h;\n}\n"
        val parts = if (t is Tuple) t.elements.mapIndexed { i, e -> e to "v.${tupleField(i)}" } else (t as Record).let { r -> r.fields.map { (field, ft) -> ft to member(r, "v", field, ft) } }
        val mixes = parts.joinToString("") { (ft, expr) -> "\th = __ec_hash_mix(h, ${hashed(ft, expr)});\n" }
        "static inline uint64_t $name(${cName(t)} v) {\n${if (parts.isEmpty()) "\t(void)v;\n" else ""}\tuint64_t h = 0;\n${mixes}\treturn h;\n}\n"
    }

    private var hashRuntime = false

    private fun hashRuntime() {
        if (hashRuntime) return
        hashRuntime = true
        prototypes.append("""
            static inline uint64_t __ec_hash_mix(uint64_t h, uint64_t v) { h ^= v + 0x9E3779B97F4A7C15ull + (h << 6) + (h >> 2); h = (h ^ (h >> 30)) * 0xBF58476D1CE4E5B9ull; h = (h ^ (h >> 27)) * 0x94D049BB133111EBull; return h ^ (h >> 31); }
            static inline uint64_t __ec_hash_bytes(const char* s, size_t n) { uint64_t h = 0xCBF29CE484222325ull; for (size_t i = 0; i < n; i++) h = (h ^ (unsigned char)s[i]) * 0x100000001B3ull; return h; }
            static inline uint64_t __ec_hash_cstr(const char* s) { return s == NULL ? 0 : __ec_hash_bytes(s, strlen(s)); }
            static inline uint64_t __ec_hash_float(double d) { if (d == 0) return 0; uint64_t bits; memcpy(&bits, &d, sizeof bits); return bits; }
        """.trimIndent() + "\n")
    }

    // ---------------------------------------------------------------------------------------------------
    // `"${x}"` of a record or tuple: `Point { x: 1, y: 2 }`, or what the program's `str` says (see Functions.resolvePrinters).

    /** C giving [expr], a [t], as a String in [pool]. */
    fun show(t: Type, expr: String, pool: String): String = "${shower(t)}($pool, $expr)"

    /** `__ec_show_T(pool, v)`: a [t] as text, for types `${...}` has no printf conversion for. Each part is allocated in `pool`. */
    private fun shower(t: Type): String = helper("__ec_show_${mangle(t)}") { name ->
        val c = cName(t)
        val body = showBody(t)
        val unused = if (Regex("\\bpool\\b").containsMatchIn(body)) "" else "\t(void)pool;\n"
        "static inline __ec_string_t $name(apr_pool_t* pool, $c v) {\n$unused$body}\n"
    }

    private fun showBody(t: Type): String {
        val own = printers[t]
        return when {
            t is Pointer -> showPointer(t, own)
            own != null -> "\treturn ${callPrinter(own, t, "v")};\n"
            t is Distinct -> "\treturn ${text(t.base, "v")};\n"
            // What a union holds isn't known, and reading the wrong field of it could follow a pointer that isn't one.
            t is Record && (t.isExternal || t.isUnion) -> "\t(void)v;\n\treturn ${literal("${t.name} { ... }")};\n"
            t is Record -> {
                val parts = t.fields.map { (field, ft) -> part(ft, member(t, "v", field, ft)).let { it.copy(conversion = "$field: ${it.conversion}") } }
                showParts(if (parts.isEmpty()) "${t.name} {" else "${t.name} { ", parts, if (parts.isEmpty()) "}" else " }")
            }
            t is Tuple -> showParts("(", t.elements.mapIndexed { i, e -> part(e, "v.${tupleField(i)}") }, ")")
            t is ArrayType && !t.isC -> {
                showRuntime()
                "\t__ec_string_t* parts = __ec_palloc(pool, (v.len + 1) * sizeof(__ec_string_t));\n" +
                    "\tfor (size_t i = 0; i < v.len; i++) parts[i] = ${text(t.element, "v.data[i]")};\n\treturn __ec_show_join(pool, parts, v.len);\n"
            }
            // The text in a C buffer: up to its first 0, or all of it.
            t is FixedArray && t.element === Types.Char -> "\tconst char* end = memchr(v.data, 0, ${t.size});\n" +
                "\treturn __ec_str_concat(pool, (__ec_string_t){end == NULL ? ${t.size} : (size_t)(end - v.data), v.data}, (__ec_string_t){0, NULL});\n"
            t is FixedArray -> {
                showRuntime()
                "\t__ec_string_t* parts = __ec_palloc(pool, ${t.size + 1} * sizeof(__ec_string_t));\n" +
                    "\tfor (size_t i = 0; i < ${t.size}; i++) parts[i] = ${text(t.element, "v.data[i]")};\n\treturn __ec_show_join(pool, parts, ${t.size});\n"
            }
            t is Union && isPlain(t) -> "\tif (v == NULL) return ${literal("null")};\n\treturn ${text(t.members.first { it !== Types.Null }, "v")};\n"
            t is Union -> {
                val cases = t.members.mapIndexed { i, m -> "\tcase $i: return ${text(m, "v.v.m$i")};\n" }
                "\tswitch (v.tag) {\n${cases.joinToString("")}\tdefault: return ${literal("null")};\n\t}\n"
            }
            t is FunctionType -> "\t(void)v;\n\treturn ${literal("<fn>")};\n"
            else -> "\treturn ${format("%p", listOf(t)) { a -> "(void*)$a" }}(pool, v);\n" // a C array
        }
    }

    /** What a pointer points to, or `...` when printing it already (a cycle) or too deep. What it holds is what's printed, as by `clone`. */
    private fun showPointer(t: Pointer, own: Overload?): String {
        val target = t.target
        if (own == null && !sized(target)) return "\treturn ${format("%p", listOf(t)) { a -> "(void*)$a" }}(pool, v);\n"
        showRuntime()
        val cases = (target as? Record)?.descendants?.drop(1).orEmpty().joinToString("") { child -> "\tcase ${child.id}: return ${shower(types.pointer(child))}(pool, (${cName(child)}*)v);\n" }
        val dispatch = if (cases.isEmpty()) "" else "\tswitch (*(int*)v) {\n$cases\tdefault: break;\n\t}\n"
        val value = if (own != null) callPrinter(own, t, "v") else text(target, "(*v)")
        return "$dispatch\tif (!__ec_show_enter(v)) return ${literal("...")};\n\t__ec_string_t r = $value;\n\t__ec_show_leave();\n\treturn r;\n"
    }

    /** One value in a printed record or tuple: its printf conversion, and how to pass [value] (a [type]) to it. */
    private data class ShownPart(val conversion: String, val type: Type, val value: String, val read: (String) -> String)

    private fun part(t: Type, expr: String): ShownPart {
        val plain = IRFormat.spec(if (t is AtomLiteral) Types.Atom else t)
        val base = if (t is Distinct) t.base else t
        return when {
            plain == null -> ShownPart("%.*s", Types.String, show(t, expr, "pool"), IRFormat.spec(Types.String)!!.second)
            base === Types.String || base === Types.CString -> ShownPart("\\\"${plain.first}\\\"", t, expr, plain.second)
            base === Types.Char -> ShownPart("'%c'", t, expr, plain.second)
            else -> ShownPart(plain.first, t, expr, plain.second)
        }
    }

    /** `return` of [parts] printed between [open] and [close], with `, ` between them. */
    private fun showParts(open: String, parts: List<ShownPart>, close: String): String {
        val helper = format(parts.joinToString(", ", open, close) { it.conversion }, parts.map { it.type }, parts.map { it.read })
        return "\treturn $helper(pool${parts.joinToString("") { ", ${it.value}" }});\n"
    }

    /** C giving [expr], a [t], as a String on its own. */
    private fun text(t: Type, expr: String): String {
        if (t === Types.Null) return literal("null")
        val part = part(t, expr)
        if (part.type === Types.String && part.conversion == "%.*s") return part.value
        return "${format(part.conversion, listOf(part.type), listOf(part.read))}(pool, $expr)"
    }

    /** The call of the program's `str` [o] with [expr], a [t]; `&expr` for a `str` that takes a pointer to the record. */
    private fun callPrinter(o: Overload, t: Type, expr: String): String {
        val param = o.params[0].type
        if (declaredPrinters.add(o.cName)) prototypes.append("${if (library && o.exportName == null) "static " else ""}__ec_string_t ${o.cName}(${cName(param)} p);\n")
        return "${o.cName}(${if (param is Pointer && (t is Record || t is Tuple)) "&$expr" else convert(t, param, expr, "NULL")})"
    }

    private val declaredPrinters = HashSet<String>()

    /** A String with the fixed [text]. */
    private fun literal(text: String) = "(__ec_string_t){${text.length}, \"$text\"}"

    private var showRuntime = false

    /** The pointers being printed, innermost last, which stop a value that points back to itself; and `[a, b]` from its parts. */
    private fun showRuntime() {
        if (showRuntime) return
        showRuntime = true
        prototypes.append("""
            enum { __EC_SHOW_DEPTH = 32 };
            static _Thread_local struct { const void* open[__EC_SHOW_DEPTH]; int depth; } __ec_show_path;
            static inline bool __ec_show_enter(const void* v) {
            	if (__ec_show_path.depth == __EC_SHOW_DEPTH) return false;
            	for (int i = 0; i < __ec_show_path.depth; i++) if (__ec_show_path.open[i] == v) return false;
            	__ec_show_path.open[__ec_show_path.depth++] = v;
            	return true;
            }
            static inline void __ec_show_leave(void) { __ec_show_path.depth--; }
            static inline __ec_string_t __ec_show_join(apr_pool_t* pool, const __ec_string_t* parts, size_t n) {
            	size_t len = 2 + (n > 1 ? (n - 1) * 2 : 0);
            	for (size_t i = 0; i < n; i++) len += parts[i].len;
            	char* s = __ec_palloc(pool, len + 1);
            	char* p = s;
            	*p++ = '[';
            	for (size_t i = 0; i < n; i++) {
            		if (i > 0) { *p++ = ','; *p++ = ' '; }
            		if (parts[i].len) memcpy(p, parts[i].data, parts[i].len);
            		p += parts[i].len;
            	}
            	*p++ = ']';
            	*p = 0;
            	return (__ec_string_t){len, s};
            }
        """.trimIndent() + "\n")
    }

    private val formats = HashMap<String, String>()

    /**
     * A helper printing [format] with arguments of [args] types into a String from an APR pool (`format(pool, "...")`).
     * [readers] give the C arguments for each, from a parameter's name.
     */
    fun format(format: String, args: List<Type>, reader: (String) -> String) = format(format, args, listOf(reader))

    fun format(format: String, args: List<Type>, readers: List<(String) -> String>): String {
        val values = readers.mapIndexed { i, read -> read("a$i") }.joinToString("") { ", $it" }
        val key = format + "|" + args.joinToString(",") { it.id.toString() } + "|" + values
        val name = formats.getOrPut(key) { "__ec_format_${formats.size}" }
        return helper(name) { _ ->
            val params = args.mapIndexed { i, t -> "${cName(t)} a$i" }
            "static __ec_string_t $name(apr_pool_t* pool${params.joinToString("") { ", $it" }}) {\n" +
                "\tint n = snprintf(NULL, 0, \"$format\"$values);\n" +
                "\tchar* s = __ec_palloc(pool, (size_t)n + 1);\n" +
                "\tsnprintf(s, (size_t)n + 1, \"$format\"$values);\n" +
                "\treturn (__ec_string_t){(size_t)n, s};\n}\n"
        }
    }

    // ---------------------------------------------------------------------------------------------------
    // Pools: `new`, `new T[n]` and `clone`. Every allocation goes through `__ec_palloc` (see the runtime in IRProgram).

    /** The empty value of [t] as a C expression: `0`, `false`, `(T){0}`, or a record with its type tag. */
    fun zero(t: Type): String = when {
        t === Types.Bool -> "false"
        t is Prim && t !== Types.String -> "0"
        t is Record && t.inHierarchy -> "(${cName(t)}){.${tagPath(t)} = ${t.id}}"
        t is FixedArray && t.element is Record && t.element.inHierarchy -> "(${cName(t)}){{[0 ... ${t.size - 1}] = ${zero(t.element)}}}"
        else -> "(${cName(t)}){0}"
    }

    /** `__ec_new_T(pool, v)`: a pointer to a copy of `v` (a [t]) in `pool`, for `new v`. */
    fun newValue(t: Type): String = helper("__ec_new_${mangle(t)}") { name ->
        val c = cName(t)
        "static inline $c* $name(apr_pool_t* pool, $c v) {\n\t$c* p = ${allocate("pool", "sizeof($c)", t)};\n\t*p = v;\n\treturn p;\n}\n"
    }

    /**
     * `__ec_alloc_A(pool, n)`: an array [t] of `n` empty elements in `pool`, for `new T[n]`. A length whose size in
     * bytes doesn't fit a `size_t` (including a negative length, which C turns into a huge one) stops the program.
     */
    fun newArray(t: ArrayType): String = helper("__ec_alloc_${mangle(t)}") { name ->
        val (a, e) = cName(t) to cName(t.element)
        "static inline $a $name(apr_pool_t* pool, size_t n) {\n" +
            "\tif (n > SIZE_MAX / sizeof($e)) __ec_panic(\"eclang\", \"`new T[n]` is too large: n elements don't fit in memory (is n negative?)\");\n" +
            "\t$a a = {n, ${allocate("pool", "n * sizeof($e)", t.element)}};\n" +
            "\tfor (size_t i = 0; i < n; i++) a.data[i] = ${zero(t.element)};\n\treturn a;\n}\n"
    }

    /** C allocating [size] bytes of [t]s in [pool]. Pools give memory aligned to 8 bytes, which is too little for an `align(16)` record. */
    private fun allocate(pool: String, size: String, t: Type): String {
        val align = alignmentOf(t)
        return if (align > POOL_ALIGNMENT) "__ec_palloc_aligned($pool, $size, $align)" else "__ec_palloc($pool, $size)"
    }

    /** The largest `align(n)` in [t], by value (0 for none). */
    private fun alignmentOf(t: Type, seen: MutableSet<Type> = HashSet()): Long {
        if (!seen.add(t)) return 0
        return when (t) {
            is Distinct -> alignmentOf(t.base, seen)
            is FixedArray -> alignmentOf(t.element, seen)
            is Tuple -> t.elements.maxOf { alignmentOf(it, seen) }
            is Union -> if (isPlain(t)) 0 else t.members.maxOf { alignmentOf(it, seen) }
            is Record -> maxOf(t.layout.align ?: 0, if (t.isDefined) t.fields.values.maxOfOrNull { alignmentOf(it, seen) } ?: 0 else 0)
            else -> 0
        }
    }

    /**
     * C that deep-copies [expr] (a [t]) into the pool [pool] (`clone(x)`): Strings, CStrings, arrays and what
     * pointers point to are copied, recursively. [expr] itself when a [t] holds no memory to copy.
     */
    fun clone(t: Type, expr: String, pool: String): String = when {
        t is Distinct -> clone(t.base, expr, pool)
        !ownsMemory(t) -> expr
        t is Pointer || !copiesPointers(t) -> "${cloner(t)}($pool, $expr)"
        // One map of copied pointers for the whole value, so a value two elements share stays shared in the copy.
        else -> "${helper("__ec_clone_root_${mangle(t)}") { name ->
            val c = cName(t)
            "static inline $c $name(apr_pool_t* pool, $c v) {\n\t__ec_clone_enter(pool, NULL, 0);\n\t$c r = ${cloner(t)}(pool, v);\n\t__ec_clone_leave(NULL);\n\treturn r;\n}\n"
        }}($pool, $expr)"
    }

    /** A deep copy of [expr] inside a clone, using its map of copied pointers. */
    private fun copy(t: Type, expr: String, pool: String): String = if (!ownsMemory(t)) expr else "${cloner(t)}($pool, $expr)"

    /** Whether cloning a [t] copies a pointer (so uses the map of [cloneRuntime]). */
    private fun copiesPointers(t: Type, seen: MutableSet<Type> = HashSet()): Boolean {
        if (!seen.add(t)) return false
        return when (t) {
            is Distinct -> copiesPointers(t.base, seen)
            is Pointer -> sized(t.target).also { if (it) cloneRuntime() }
            is ArrayType -> !t.isC && copiesPointers(t.element, seen)
            is FixedArray -> copiesPointers(t.element, seen)
            is Union -> t.members.any { copiesPointers(it, seen) }
            is Tuple -> t.elements.any { copiesPointers(it, seen) }
            is Record -> !t.isUnion && t.fields.values.any { copiesPointers(it, seen) }
            else -> false
        }
    }

    /**
     * Whether a [t] points to memory that `clone` copies. Not: numbers and atoms, function pointers, C arrays (they
     * have no length), `@Any`, pointers to C types eclang knows no fields of (`@Pool`), and C unions (which field they
     * hold isn't known); those are kept as they are.
     */
    private fun ownsMemory(t: Type, seen: MutableSet<Type> = HashSet()): Boolean {
        if (!seen.add(t)) return false
        return when (t) {
            Types.String, Types.CString -> true
            is Distinct -> ownsMemory(t.base, seen)
            is ArrayType -> !t.isC
            is FixedArray -> ownsMemory(t.element, seen)
            is Pointer -> sized(t.target)
            is Union -> t.members.any { ownsMemory(it, seen) }
            is Tuple -> t.elements.any { ownsMemory(it, seen) }
            is Record -> !t.isUnion && t.fields.values.any { ownsMemory(it, seen) }
            else -> false
        }
    }

    private fun sized(t: Type) = isSized(t)

    /**
     * `__ec_div_Int(a, b, at)` and friends: `a op b` on whole numbers of [t] that stops the program instead of crashing
     * or having undefined behaviour in C. `/` and `%`: division by zero, and MIN / -1 (too big). `<<` and `>>`: a count
     * (of type [count]) that is negative or not less than the number of bits; `<<` is done unsigned, so shifting a bit
     * out is defined. `+`, `-` and `*` (with `--overflow-check`): a result that doesn't fit.
     */
    fun checkedArith(op: String, t: Prim, count: Prim?): String {
        val kind = mapOf("/" to "div", "%" to "mod", "<<" to "shl", ">>" to "shr", "+" to "add", "-" to "sub", "*" to "mul")[op]!!
        return helper("__ec_${kind}_${mangle(t)}" + (count?.let { "_${mangle(it)}" } ?: "")) { name ->
            val c = cName(t)
            val signed = t.category == Category.SINT
            val min = mapOf(32 to "INT_MIN", 64 to "LONG_MIN")[t.bits]?.takeIf { signed && t !== Types.Size }
            val body = when (kind) {
                "div", "mod" -> "\tif (b == 0) __ec_panic(at, \"division by zero\");\n" +
                    (if (min != null) "\tif (b == -1 && a == $min) __ec_panic(at, \"the result of `$op` is too big for $t (the smallest $t $op -1)\");\n" else "") +
                    "\treturn a $op b;\n"
                "shl", "shr" -> "\tif ((unsigned long long)b >= ${t.bits}) __ec_panic_shift(at, (long long)b, ${t.bits});\n" +
                    (if (kind == "shl" && signed) "\treturn ($c)((unsigned $c)a << b);\n" else "\treturn ($c)(a $op b);\n")
                else -> "\t$c r;\n\tif (__builtin_${kind}_overflow(a, b, &r)) __ec_panic(at, \"the result of `$op` is too big for $t\");\n\treturn r;\n"
            }
            if (kind == "shl" || kind == "shr") prototypes.let { if (!shiftRuntime) { shiftRuntime = true; it.append("_Noreturn static inline void __ec_panic_shift(const char* at, long long n, int bits) { fflush(stdout); fprintf(stderr, \"%s: can't shift by %lld bits: the value has %d\\n\", at, n, bits); abort(); }\n") } }
            val bType = count?.let { cName(it) } ?: c
            "static inline $c $name($c a, $bType b, const char* at) {\n$body}\n"
        }
    }

    private var shiftRuntime = false

    /**
     * `__EC_DEBUG`, the condition of `debug` statements (see TDebug): 1, or 0 in a release build. A C file can be built either way
     * with `-D__EC_DEBUG=0` or `1`; code in a `debug` statement stays in the C, so the helpers it uses are still used.
     */
    fun useDebugFlag(release: Boolean) {
        if (debugFlag) return
        debugFlag = true
        prototypes.append("#ifndef __EC_DEBUG\n#define __EC_DEBUG ${if (release) 0 else 1} /* 0: a release build, without `debug` statements */\n#endif\n")
    }
    private var debugFlag = false

    /**
     * Where the call of a function with `requires` is (see Functions.contractEntry): the caller sets it right before the call, and
     * the function takes it first thing, so a call through a function value or from C sees NULL.
     */
    fun useCallSite() {
        if (callSite) return
        callSite = true
        prototypes.append("static _Thread_local const char* __ec_call_site; /* where the function with `requires` being entered was called */\n" +
            "static inline const char* __ec_take_call_site(const char* otherwise) { const char* at = __ec_call_site; __ec_call_site = NULL; return at != NULL ? at : otherwise; }\n")
    }
    private var callSite = false

    private var cloneRuntime = false
    private val cloneKinds = HashMap<Type, Int>()

    /**
     * What pointer clones use to copy each pointer once (see [cloner]): a map from the pointers copied so far to their
     * copies, made by the outermost pointer clone in a scratch pool and freed when it returns. Without it a value that
     * points back to itself would be copied forever.
     */
    private fun cloneRuntime() {
        if (cloneRuntime) return
        cloneRuntime = true
        prototypes.append("""
            typedef struct { const void* from; void* to; int kind; } __ec_clone_entry_t;
            typedef struct { __ec_clone_entry_t* entries; size_t cap, n; int depth; apr_pool_t* scratch; } __ec_clone_map_t;
            static _Thread_local __ec_clone_map_t __ec_clone_map;
            static inline size_t __ec_clone_slot(const void* v, int kind, size_t cap) { size_t h = (size_t)(uintptr_t)v ^ (size_t)kind; h ^= h >> 17; h *= 0x9E3779B97F4A7C15u; return (h ^ (h >> 29)) & (cap - 1); }
            static inline void* __ec_clone_enter(apr_pool_t* pool, const void* v, int kind) {
            	__ec_clone_map_t* m = &__ec_clone_map;
            	if (m->depth++ == 0) { m->scratch = __ec_pool_create(pool); m->cap = 64; m->n = 0; m->entries = apr_pcalloc(m->scratch, m->cap * sizeof(__ec_clone_entry_t)); }
            	if (v == NULL) return NULL;
            	for (size_t i = __ec_clone_slot(v, kind, m->cap); m->entries[i].from != NULL; i = (i + 1) & (m->cap - 1)) if (m->entries[i].from == v && m->entries[i].kind == kind) return m->entries[i].to;
            	return NULL;
            }
            static inline void __ec_clone_put(const void* v, int kind, void* copy) {
            	__ec_clone_map_t* m = &__ec_clone_map;
            	if (2 * (m->n + 1) > m->cap) {
            		size_t cap = m->cap * 2;
            		__ec_clone_entry_t* entries = apr_pcalloc(m->scratch, cap * sizeof(__ec_clone_entry_t));
            		for (size_t j = 0; j < m->cap; j++) if (m->entries[j].from != NULL) {
            			size_t i = __ec_clone_slot(m->entries[j].from, m->entries[j].kind, cap);
            			while (entries[i].from != NULL) i = (i + 1) & (cap - 1);
            			entries[i] = m->entries[j];
            		}
            		m->entries = entries; m->cap = cap;
            	}
            	size_t i = __ec_clone_slot(v, kind, m->cap);
            	while (m->entries[i].from != NULL) i = (i + 1) & (m->cap - 1);
            	m->entries[i] = (__ec_clone_entry_t){v, copy, kind}; m->n++;
            }
            static inline void* __ec_clone_leave(void* copy) {
            	__ec_clone_map_t* m = &__ec_clone_map;
            	if (--m->depth == 0) { apr_pool_destroy(m->scratch); m->scratch = NULL; }
            	return copy;
            }

        """.trimIndent() + "\n")
    }

    /**
     * What tells copies at one address apart in the map of copied pointers: a record and its first field start at the
     * same address, but a copy of one can't stand in for the other. Pointers to the same object share a kind, so that
     * they stay shared: all the records of a hierarchy (a pointer to the parent copies the whole child) and a `distinct`
     * type and its base.
     */
    private fun cloneKind(target: Type): Int {
        val plain = generateSequence(target) { (it as? Distinct)?.base }.last()
        val root = if (plain is Record) generateSequence(plain) { it.parent }.last() else plain
        return cloneKinds.getOrPut(root) { cloneKinds.size + 1 }
    }

    /** `__ec_clone_T(pool, v)`: a deep copy of `v`, a [t]; see [clone]. */
    private fun cloner(t: Type): String = if (t is Distinct) cloner(t.base) else helper("__ec_clone_${mangle(t)}") { name ->
        val c = cName(t)
        val body = when {
            t === Types.String -> "\tchar* s = __ec_palloc(pool, v.len + 1);\n\tif (v.len) memcpy(s, v.data, v.len);\n\ts[v.len] = 0;\n\treturn (__ec_string_t){v.len, s};\n"
            t === Types.CString -> "\tsize_t n = strlen(v) + 1;\n\tchar* s = __ec_palloc(pool, n);\n\tmemcpy(s, v, n);\n\treturn s;\n"
            t is ArrayType -> {
                val e = cName(t.element)
                val copy = if (ownsMemory(t.element)) "\tfor (size_t i = 0; i < v.len; i++) a.data[i] = ${copy(t.element, "v.data[i]", "pool")};\n"
                    else "\tif (v.len) memcpy(a.data, v.data, v.len * sizeof($e));\n"
                "\t$c a = {v.len, ${allocate("pool", "v.len * sizeof($e)", t.element)}};\n$copy\treturn a;\n"
            }
            t is Pointer -> {
                // Each pointer is copied once: a value reached twice (shared, or a cycle) is the same copy twice.
                cloneRuntime()
                // A pointer to a record that is extended may point to a child record: copy the whole child.
                val target = t.target
                val kind = cloneKind(target)
                val children = (target as? Record)?.descendants?.drop(1) ?: listOf()
                val cases = children.joinToString("") { child ->
                    val cc = cName(child)
                    "\tcase ${child.id}: { $cc* p = ${allocate("pool", "sizeof($cc)", child)}; __ec_clone_put(v, $kind, p); *p = ${copy(child, "*($cc*)v", "pool")}; return ($c)__ec_clone_leave(p); }\n"
                }
                val dispatch = if (cases.isEmpty()) "" else "\tswitch (*(int*)v) {\n$cases\tdefault: break;\n\t}\n"
                val tc = cName(target)
                "\tvoid* seen = __ec_clone_enter(pool, v, $kind);\n\tif (seen != NULL) return ($c)__ec_clone_leave(seen);\n" +
                    "$dispatch\t$tc* p = ${allocate("pool", "sizeof($tc)", target)};\n\t__ec_clone_put(v, $kind, p);\n\t*p = ${copy(target, "*v", "pool")};\n\treturn __ec_clone_leave(p);\n"
            }
            t is Union && isPlain(t) -> {
                val member = t.members.first { it !== Types.Null }
                "\treturn v == NULL ? NULL : ${copy(member, "v", "pool")};\n"
            }
            t is Union -> {
                val cases = t.members.mapIndexedNotNull { i, m ->
                    if (m === Types.Null || !ownsMemory(m)) null else "\tcase $i: r.v.m$i = ${copy(m, "v.v.m$i", "pool")}; break;\n"
                }
                "\t$c r = v;\n\tswitch (v.tag) {\n${cases.joinToString("")}\tdefault: break;\n\t}\n\treturn r;\n"
            }
            t is Tuple -> {
                val fields = t.elements.mapIndexedNotNull { i, e -> if (ownsMemory(e)) "\tr.${tupleField(i)} = ${copy(e, "v.${tupleField(i)}", "pool")};\n" else null }
                "\t$c r = v;\n${fields.joinToString("")}\treturn r;\n"
            }
            t is Record -> {
                val fields = t.fields.mapNotNull { (field, ft) -> if (ownsMemory(ft)) "\t${member(t, "r", field, ft)} = ${copy(ft, member(t, "v", field, ft), "pool")};\n" else null }
                "\t$c r = v;\n${fields.joinToString("")}\treturn r;\n"
            }
            t is FixedArray -> "\t$c r = v;\n\tfor (size_t i = 0; i < ${t.size}; i++) r.data[i] = ${copy(t.element, "v.data[i]", "pool")};\n\treturn r;\n"
            else -> error("BUG: can't clone a $t")
        }
        "static inline $c $name(apr_pool_t* pool, $c v) {\n$body}\n"
    }

    /** The element type of [t], an array or String (Char). */
    private fun element(t: Type) = if (t === Types.String) "char" else cName((t as ArrayType).element)

    /** A helper returning a pointer to element `i` of [t] (a non-C array or String), stopping the program when `i` is out of bounds. */
    fun elementAt(t: Type): String = helper("__ec_at_${mangle(t)}") { name ->
        "static inline ${element(t)}* $name(${cName(t)} a, size_t i, const char* at) {\n\tif (i >= a.len) __ec_panic_index(at, i, a.len);\n\treturn &a.data[i];\n}\n"
    }

    /** A helper returning elements `from` up to (not including) `to` of [t] (a non-C array or String), without copying them. */
    fun slice(t: Type, checked: Boolean): String = helper("__ec_slice_${mangle(t)}${if (checked) "" else "_unchecked"}") { name ->
        val check = if (checked) "\tif (from > to || to > a.len) __ec_panic_slice(at, from, to, a.len);\n" else "\t(void)at;\n"
        "static inline ${cName(t)} $name(${cName(t)} a, size_t from, size_t to, const char* at) {\n$check\treturn (${cName(t)}){to - from, a.data + from};\n}\n"
    }

    /** Like [slice], but up to the end of the value (a slice with no end). */
    fun sliceFrom(t: Type, checked: Boolean): String = helper("__ec_slice_from_${mangle(t)}${if (checked) "" else "_unchecked"}") { name ->
        val check = if (checked) "\tif (from > a.len) __ec_panic_slice(at, from, a.len, a.len);\n" else "\t(void)at;\n"
        "static inline ${cName(t)} $name(${cName(t)} a, size_t from, const char* at) {\n$check\treturn (${cName(t)}){a.len - from, a.data + from};\n}\n"
    }

    /** A C function converting tagged union [from] to [to]; members that don't fit stop the program with a message. */
    private fun converter(from: Union, to: Type, expr: String, where: String): String {
        val name = helper("__ec_cv_${mangle(from)}__${mangle(to)}") { name ->
            val cases = from.members.mapIndexed { i, m ->
                val fits = types.accepts(to, m) || (m !== Types.Null && types.isSubtype(m, to))
                val action = when {
                    fits -> "return ${convert(m, to, if (m === Types.Null) "NULL" else "v.v.m$i", "at")};"
                    m === Types.Null -> "__ec_panic(at, \"null value used where a value was required\");"
                    else -> "__ec_panic(at, \"value is $m, not $to\");"
                }
                "\t\tcase $i: $action\n"
            }
            val get = if (isTagged(from)) "v.tag" else "(v == NULL ? ${tagOf(from, Types.Null)} : ${from.members.indexOfFirst { it !== Types.Null }})"
            "static inline ${cName(to)} $name(${cName(from)} v, const char* at) {\n\tswitch ($get) {\n${cases.joinToString("")}\t}\n\t__ec_panic(at, \"invalid union tag\");\n\treturn *(${cName(to)}*)0;\n}\n"
        }
        return "$name($expr, $where)"
    }

    /** By-value upcast: copies the parent inside the child, with the parent's type tag (the copy really is a [to]). */
    private fun upcast(from: Record, to: Record): String = helper("__ec_up_${from.cName}__${to.cName}") { name ->
        val inside = generateSequence(from) { it.parent }.takeWhile { it !== to }.joinToString("") { ".${parentMember(it)}" }
        "static inline ${cName(to)} $name(${cName(from)} v) {\n\t${cName(to)} r = v$inside;\n\tr.${tagPath(to)} = ${to.id};\n\treturn r;\n}\n"
    }

    /**
     * `__ec_pow_Int(b, e)`: b to the power e for a whole-number type [t], by repeated squaring in [t] itself, so a
     * Long result keeps every digit. A negative exponent gives 0, except for a base of 1 or -1.
     */
    fun intPow(t: Type): String = if (t is Distinct) intPow(t.base) else helper("__ec_pow_${mangle(t)}") { name ->
        val c = cName(t)
        val negative = if ((t as Prim).category == Category.SINT) "\tif (e < 0) return b == 1 ? 1 : b == -1 ? ((e & 1) ? -1 : 1) : 0;\n" else ""
        "static inline $c $name($c b, $c e) {\n$negative\t$c r = 1;\n\twhile (e > 0) {\n\t\tif (e & 1) r *= b;\n\t\te >>= 1;\n\t\tif (e > 0) b *= b;\n\t}\n\treturn r;\n}\n"
    }

    /** `__ec_enum_to_c_Name(atom)`: the C constant of an atom of a `declare enum` ([members]: atom label to constant). */
    fun enumToC(cName: String, members: List<Pair<String, String>>): String = helper("__ec_enum_to_c_$cName") { name ->
        usedAtoms += members.dropLast(1).map { it.first }
        val chain = members.dropLast(1).joinToString("") { (atom, constant) -> "v == __ec_atom_$atom ? $constant : " } + members.last().second
        "static inline int $name(char* v) { return $chain; }\n"
    }

    /** `__ec_enum_of_c_Name(value, where)`: the atom of a C constant of a `declare enum`; any other value stops the program. */
    fun enumFromC(cName: String, enumName: String, members: List<Pair<String, String>>): String = helper("__ec_enum_of_c_$cName") { name ->
        usedAtoms += members.map { it.first }
        val checks = members.joinToString("") { (atom, constant) -> "\tif (v == $constant) return __ec_atom_$atom;\n" }
        "static inline char* $name(int v, const char* at) {\n$checks\t__ec_panic(at, \"a C function returned a value that the enum $enumName doesn't list\");\n}\n"
    }

    /** `__ec_is_Cat(tid)`: true when a record's runtime type tag is Cat or a record extending Cat. */
    fun isRecord(r: Record): String = helper("__ec_is_${r.cName}") { name ->
        val cases = r.descendants.joinToString(" ") { "case ${it.id}:" }
        "static inline bool $name(int tid) { switch (tid) { $cases return true; default: return false; } }\n"
    }

    private fun downcast(r: Record): String {
        isRecord(r)
        return helper("__ec_down_${r.cName}") { name ->
            "static inline void* $name(void* p, const char* at) {\n" +
                "\tif (p != NULL && !__ec_is_${r.cName}(*(int*)p)) __ec_panic(at, \"value is not a ${r.name}\");\n\treturn p;\n}\n"
        }
    }

    /**
     * `__ec_safe_U__field(v)`: for `v?.field`, null when v (a nullable union [from]) is null, otherwise its
     * [field] (of [fieldType]) as the nullable [result].
     */
    fun safeAccess(from: Union, nonNull: Type, field: String, fieldType: Type, result: Type, throughPointer: Boolean): String =
        helper("__ec_safe_${mangle(from)}__$field") { name ->
            // Only reached when v is not null, so the value needs no check.
            val value = if (isPlain(from)) "v" else narrow(from, nonNull, "v", "\"?.\"")
            val owner = (nonNull as? Pointer)?.target ?: nonNull
            val path = if (owner is Record) fieldPath(owner, field) else field
            val read = "($value)${if (throughPointer) "->" else "."}$path".let { field ->
                if (owner is Record && fixedOf(fieldType) != null) storage(field, fieldType, owner.layout.packed) else field
            }
            "static inline ${cName(result)} $name(${cName(from)} v) {\n" +
                "\tif (${isCheck(from, Types.Null, "v")}) return ${convert(Types.Null, result, "NULL", "\"?.\"")};\n" +
                "\treturn ${convert(fieldType, result, read, "\"?.\"")};\n}\n"
        }

    private fun stringToArray(to: ArrayType): String = helper("__ec_str_to_${mangle(to)}") { name ->
        "static inline ${cName(to)} $name(__ec_string_t s) { return (${cName(to)}){s.len, s.data}; }\n"
    }

    /** A String's bytes copied into a `Char[N]`, the rest zeros; one longer than N stops the program. */
    private fun stringToFixed(to: FixedArray): String = helper("__ec_str_to_${mangle(to)}") { name ->
        val c = cName(to)
        "static inline $c $name(__ec_string_t s, const char* at) {\n" +
            "\tif (s.len > ${to.size}) { fflush(stdout); fprintf(stderr, \"%s: a String of %zu bytes doesn't fit a $to\\n\", at, s.len); abort(); }\n" +
            "\t$c r;\n\tmemset(&r, 0, sizeof r);\n\tif (s.len > 0) memcpy(r.data, s.data, s.len);\n\treturn r;\n}\n"
    }

    /** A helper comparing the text of a `Char[N]` (up to its first 0, or all N Chars) with a String's (`a == s`). */
    fun fixedTextEquals(t: FixedArray): String = helper("__ec_eq_text_${mangle(t)}") { name ->
        "static inline bool $name(${cName(t)} a, __ec_string_t s) {\n\tconst char* end = memchr(a.data, 0, ${t.size});\n" +
            "\tsize_t len = end == NULL ? ${t.size} : (size_t)(end - a.data);\n\treturn len == s.len && (len == 0 || memcmp(a.data, s.data, len) == 0);\n}\n"
    }

    /**
     * A helper giving back a value for a bit-field of [t] with [width] bits, which stops the program when it doesn't fit
     * (C would cut it to the width). `field` names the bit-field in the message.
     */
    fun checkedBits(t: Prim, width: Int): String = helper("__ec_bits_${mangle(t)}_$width") { name ->
        val c = cName(t)
        val signed = t.category == Category.SINT
        val lowest = if (signed) -(1L shl (width - 1)) else 0L
        val highest = if (signed) (1L shl (width - 1)) - 1 else (1L shl width) - 1
        val (test, format, cast) = if (signed) Triple("v < ${lowest}LL || v > ${highest}LL", "%lld", "(long long)")
            else Triple("v > ${highest}ULL", "%llu", "(unsigned long long)")
        "static inline $c $name($c v, const char* field, const char* at) {\n" +
            "\tif ($test) { fflush(stdout); fprintf(stderr, \"%s: $format doesn't fit bit-field '%s', which has $width bit(s): it holds $lowest to $highest\\n\", at, ${cast}v, field); abort(); }\n" +
            "\treturn v;\n}\n"
    }

    /** `expr !as to` in C (see [canCastUnsafe]): a C cast between scalars, or a view of the same bytes between byte arrays. */
    fun unsafeCast(from: Type, to: Type, expr: String): String = when {
        from === to && !isScalar(from) -> expr
        isByteView(from) && isByteView(to) -> "${helper("__ec_view_${mangle(from)}_to_${mangle(to)}") { name ->
            "static inline ${cName(to)} $name(${cName(from)} a) { return (${cName(to)}){a.len, (${element(to)}*)a.data}; }\n"
        }}($expr)"
        else -> "((${cName(to)})($expr))"
    }

    /** A C expression naming the member of [u] that [expr] holds, as text ("a String", "null"), for the report of a failed `assert` (see Assertions). */
    fun heldType(u: Union, expr: String): String {
        fun named(t: Type) = "\"${if (t === Types.Null) "null" else "a $t"}\""
        if (isPlain(u)) return if (Types.Null in u.members) "(($expr) == NULL ? \"null\" : ${named(u.members.first { it !== Types.Null })})" else named(u)
        return u.members.foldRight("\"an unknown value\"") { m, rest -> "(($expr).tag == ${tagOf(u, m)} ? ${named(m)} : $rest)" }
    }

    /** A C condition that is true when [expr] (of type [from]) currently holds a [target]. */
    fun isCheck(from: Type, target: Type, expr: String): String {
        if (from is Pointer && target is Pointer) {
            val f = from.target; val t = target.target
            if (f is Record && t is Record && t.distanceTo(f) != null) return "${isRecord(t)}(*(int*)($expr))"
        }
        if (from !is Union) return if (types.isSubtype(from, target)) "true" else "false"
        val matching = from.members.filter { it === target || types.isSubtype(it, target) }
        if (matching.isEmpty()) return "false"
        if (isPlain(from)) return when {
            matching.all { it === Types.Null } -> "(($expr) == NULL)"
            matching.size == from.members.size -> "true"
            target is AtomLiteral -> { usedAtoms += target.name; "(($expr) == __ec_atom_${target.name})" }
            else -> "(($expr) != NULL)"
        }
        return matching.joinToString(" || ", "(", ")") { "($expr).tag == ${tagOf(from, it)}" }
    }

    val isLibrary get() = library

    /** Whether a C function is only for this C file: in a library, what isn't `export`ed, so two libraries can be linked together. */
    fun isHidden(exported: Boolean) = library && !exported

    /** The prototype of the exported function [o], for a library's header. */
    fun prototype(o: Overload): String {
        val returns = o.throws?.let { types.result(o.returns, it) } ?: o.returns
        val params = if (o.params.isEmpty()) "void" else o.params.joinToString(", ") { "${cName(it.type)} ${it.name}" }
        return "${if (returns === Types.Never) "_Noreturn " else ""}${cName(returns)} ${o.cName}($params);"
    }

    companion object {
        /**
         * The member of [child]'s C struct that holds its parent: named like the parent's C type (`Animal`), or with the
         * `__ec_` prefix when [child] has a field of that name.
         */
        fun parentMember(child: Record): String {
            val parent = child.parent ?: error("${child.name} extends nothing")
            return if (parent.cName in child.ownFieldMap) "__ec_${parent.cName}" else parent.cName
        }

        /** Where the field [field] of [r] is in a C value of [r]: `legs`, or `Animal.legs` when [r] inherits it from Animal. */
        fun fieldPath(r: Record, field: String): String {
            var owner = r
            val path = StringBuilder()
            while (owner.parent != null && field !in owner.ownFieldMap) {
                path.append(parentMember(owner)).append('.')
                owner = owner.parent!!
            }
            return path.append(owner.cFieldNames[field] ?: field).toString()
        }

        /** Where the type tag of [r], a record in a hierarchy, is: `__ec_tid` in the top record, `Animal.__ec_tid` below it. */
        fun tagPath(r: Record): String = generateSequence(r) { it.parent }.filter { it.parent != null }.joinToString("") { "${parentMember(it)}." } + "__ec_tid"

        /** What `apr_palloc` aligns memory to. */
        private const val POOL_ALIGNMENT = 8L

        /** Whether C knows the size of [t], so a copy of it can be allocated. */
        fun isSized(t: Type): Boolean = when (t) {
            Types.Any, Types.None, Types.Never -> false
            is Record -> !(t.isExternal && t.fields.isEmpty())
            is Interface, is TypeParam -> false
            else -> true
        }

        /** Whether a copy of a [t] made by `clone` may still hold a pointer of the original: see `ownsMemory` for what it doesn't copy. */
        fun cloneKeepsPointers(t: Type, seen: MutableSet<Type> = HashSet()): Boolean {
            if (!seen.add(t)) return false
            return when (t) {
                is Distinct -> cloneKeepsPointers(t.base, seen)
                is ArrayType -> t.isC || cloneKeepsPointers(t.element, seen)
                is FixedArray -> cloneKeepsPointers(t.element, seen)
                is Pointer -> !isSized(t.target) || cloneKeepsPointers(t.target, seen)
                is Union -> t.members.any { cloneKeepsPointers(it, seen) }
                is Tuple -> t.elements.any { cloneKeepsPointers(it, seen) }
                is Record -> t.isUnion || t.fields.values.any { cloneKeepsPointers(it, seen) }
                else -> false
            }
        }

        /** [t] when it is a `T[N]`, also under a `distinct` name. */
        fun fixedOf(t: Type): FixedArray? = t as? FixedArray ?: (t as? Distinct)?.let { fixedOf(it.base) }

        /** Types that are a single C pointer. */
        fun isPointerLike(t: Type): Boolean = t is Pointer || t === Types.CString || t === Types.Atom ||
            t is AtomLiteral || (t is ArrayType && t.isC) || t is FunctionType || t === Types.Null

        /** A union stored as a plain C pointer (NULL meaning null) instead of a tagged struct. */
        fun isPlain(u: Union): Boolean {
            val values = u.members.filter { it !== Types.Null }
            return values.all { it === Types.Atom || it is AtomLiteral } || (values.size == 1 && isPointerLike(values[0]))
        }

        /** Types that are a C scalar (a number or a pointer), which a C cast can convert. */
        fun isScalar(t: Type): Boolean = (t is Distinct && isScalar(t.base)) || isPointerLike(t) || t is IntLiteral || t is FloatLiteral || (t is Union && isPlain(t)) ||
            (t is Prim && t !== Types.String && t !== Types.None && t !== Types.Any && t !== Types.Never)

        /** Strings and arrays of bytes, which share one layout (`{len, data}` with a 1-byte element). */
        fun isByteView(t: Type) = t === Types.String || (t is ArrayType && !t.isC && (t.element === Types.Char || t.element === Types.UChar))

        /** Whether `!as` can convert [from] to [to]: C casts only scalars, and byte arrays share the String layout. */
        fun canCastUnsafe(from: Type, to: Type) = from === to || (isScalar(from) && isScalar(to)) || (isByteView(from) && isByteView(to))
    }
}
