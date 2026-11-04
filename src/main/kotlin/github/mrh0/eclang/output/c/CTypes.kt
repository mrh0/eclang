package github.mrh0.eclang.output.c

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
 * - Arrays, tuples and `T throws E` results are small generated structs. Records are structs with their parent's
 *   fields first, plus a hidden `__ec_tid` type tag when they take part in `extends`.
 *
 * Types are named and defined lazily: asking for [cName] schedules the definition, and [definitions] writes
 * them all in an order where every struct is complete before it is used by value.
 */
class CTypes(private val types: TypeContext) {
    private val names = HashMap<Type, String>()
    private val scheduled = LinkedHashSet<Type>()
    private val completed = HashSet<Type>()
    private val inProgress = HashSet<Type>()
    private val forward = StringBuilder()
    private val bodies = StringBuilder()
    private val helpers = LinkedHashMap<String, String?>()

    /** Atoms the helpers refer to (by label), so the program defines them even if its own code doesn't use them. */
    val helperAtoms = LinkedHashSet<String>()

    // ---------------------------------------------------------------------------------------------------
    // Representation rules.

    /** Types that are a single C pointer. */
    fun isPointerLike(t: Type): Boolean = t is Pointer || t === Types.CString || t === Types.Atom ||
        t is AtomLiteral || (t is ArrayType && t.isC) || t is FunctionType || t === Types.Null

    /** A union stored as a plain C pointer (NULL meaning null) instead of a tagged struct. */
    fun isPlain(u: Union): Boolean {
        val values = u.members.filter { it !== Types.Null }
        return values.all { it === Types.Atom || it is AtomLiteral } || (values.size == 1 && isPointerLike(values[0]))
    }

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
        is Tuple -> struct(t, "tuple")
        is Union -> if (isPlain(t)) t.members.firstOrNull { it !== Types.Null }?.let { cName(it) } ?: "void*" else struct(t, "union")
        is Result -> struct(t, "result")
        is FunctionType -> names.getOrPut(t) { "__ec_fn_${mangle(t)}" }.also { scheduled += t }
        is Record -> recordName(t)
        is Interface, is TypeParam -> error("BUG: ${t} has no C representation")
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

    private fun recordName(r: Record): String {
        val name = r.externalName ?: r.cName
        if (r.isCStruct) return "struct $name"
        if (!r.isExternal) struct(r, null)
        return name
    }

    /** Names a generated struct, forward-declares it and schedules its definition. */
    private fun struct(t: Type, kind: String?): String = names.getOrPut(t) {
        val name = if (kind == null) (t as Record).cName else "__ec_${kind}_${mangle(t)}"
        forward.append("typedef struct $name $name;\n")
        scheduled += t
        name
    }

    /** A readable, C-safe name fragment for [t], used in generated type names. Falls back to the id when long. */
    fun mangle(t: Type): String {
        val m = when (t) {
            is Prim -> t.name
            is IntLiteral, is FloatLiteral -> mangle(types.concrete(t))
            is Pointer -> "P" + mangle(t.target)
            is ArrayType -> (if (t.isC) "C" else "A") + mangle(t.element)
            is Declared -> t.cName
            is Union -> t.members.joinToString("_") { mangle(it) }
            is Tuple -> t.elements.joinToString("_") { mangle(it) }
            is Result -> mangle(t.value) + "_" + mangle(t.error)
            is FunctionType -> t.params.joinToString("_") { mangle(it) } + "_to_" + mangle(t.returns) + (t.throws?.let { "_throws_" + mangle(it) } ?: "")
            is AtomLiteral -> "a" + t.name
            is TypeParam -> "t${t.id}"
        }
        return if (m.length <= 40) m else "t${t.id}"
    }

    // ---------------------------------------------------------------------------------------------------
    // Definitions.

    /** Makes sure the full definition of [t] is written before anything that uses it by value. */
    private fun complete(t: Type) {
        if (t in completed || t in inProgress) return
        inProgress += t
        when (t) {
            is ArrayType -> if (!t.isC) bodies.append("struct ${cName(t)} { size_t len; ${cName(t.element)}* data; };\n")
            is Tuple -> {
                val fields = t.elements.mapIndexed { i, e -> complete(e); "${cName(e)} ${tupleField(i)};" }
                bodies.append("struct ${cName(t)} { ${fields.joinToString(" ")} };\n")
            }
            is Union -> if (isTagged(t)) {
                val payload = t.members.mapIndexedNotNull { i, m ->
                    if (m === Types.Null) null else { complete(m); "${cName(m)} m$i;" }
                }
                bodies.append("struct ${cName(t)} { int tag; union { ${payload.joinToString(" ")} } v; }; /* $t */\n")
            }
            is Result -> {
                complete(t.error)
                val value = if (t.value === Types.None) "" else { complete(t.value); "${cName(t.value)} value; " }
                bodies.append("struct ${cName(t)} { int code; union { $value${cName(t.error)} error; }; }; /* $t */\n")
            }
            is FunctionType -> {
                t.params.forEach { complete(it) }
                val returns = t.throws?.let { types.result(t.returns, it) } ?: t.returns
                complete(returns)
                val params = if (t.params.isEmpty()) "void" else t.params.joinToString(", ") { cName(it) }
                bodies.append("typedef ${cName(returns)} (*${cName(t)})($params);\n")
            }
            is Record -> if (!t.isExternal) {
                val fields = t.fields.map { (name, type) -> complete(type); "\t${cName(type)} $name;\n" }
                val tid = if (t.inHierarchy) "\tint __ec_tid;\n" else ""
                bodies.append("struct ${t.cName} {\n$tid${fields.joinToString("")}};\n")
            }
            else -> {}
        }
        inProgress -= t
        completed += t
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
        return "$forward\n$bodies"
    }

    /** Helper functions requested by the generated code (conversions, runtime checks). */
    fun helperFunctions(): String {
        // Writing a helper can request another helper, so keep going until all are written.
        while (helpers.values.any { it == null }) {
            helpers.keys.filter { helpers[it] == null }.forEach { key -> helpers[key] = helperBodies.remove(key)!!(key) }
        }
        return prototypes.toString() + helpers.values.joinToString("")
    }

    /** Prototypes of helpers that may call each other (clones of recursive types), written before all helpers. */
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
        val checks = r.fields.map { (field, t) -> equals(t, "a.$field", "b.$field") }
        val body = if (checks.isEmpty()) "(void)a; (void)b;\n\treturn true;" else "return ${checks.joinToString("\n\t\t&& ")};"
        "static inline bool $name(${cName(r)} a, ${cName(r)} b) {\n\t$body\n}\n"
    }

    /** C that compares [a] and [b] of type [t] like `==` does. */
    private fun equals(t: Type, a: String, b: String): String = when {
        t === Types.CString -> "strcmp($a, $b) == 0"
        t === Types.String -> "__ec_str_eq($a, $b)"
        t is Record -> "${recordEquals(t)}($a, $b)"
        t is Union && t.members.contains(Types.CString) -> "($a == $b || ($a != NULL && $b != NULL && strcmp($a, $b) == 0))"
        else -> "$a == $b"
    }

    private val formats = HashMap<String, String>()

    /** A helper printing [format] with arguments of [args] types into a String from an APR pool (`format(pool, "...")`). */
    fun format(format: String, args: List<Type>): String {
        val key = format + "|" + args.joinToString(",") { it.id.toString() }
        val name = formats.getOrPut(key) { "__ec_format_${formats.size}" }
        return helper(name) { _ ->
            val params = args.mapIndexed { i, t -> "${cName(t)} a$i" }
            val values = args.mapIndexed { i, t -> github.mrh0.eclang.ir.data.IRFormat.spec(t)!!.second("a$i") }.joinToString("") { ", $it" }
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
        t is Record && t.inHierarchy -> "(${cName(t)}){.__ec_tid = ${t.id}}"
        else -> "(${cName(t)}){0}"
    }

    /** `__ec_new_T(pool, v)`: a pointer to a copy of `v` (a [t]) in `pool`, for `new v`. */
    fun newValue(t: Type): String = helper("__ec_new_${mangle(t)}") { name ->
        val c = cName(t)
        "static inline $c* $name(apr_pool_t* pool, $c v) {\n\t$c* p = __ec_palloc(pool, sizeof($c));\n\t*p = v;\n\treturn p;\n}\n"
    }

    /** `__ec_alloc_A(pool, n)`: an array [t] of `n` empty elements in `pool`, for `new T[n]`. */
    fun newArray(t: ArrayType): String = helper("__ec_alloc_${mangle(t)}") { name ->
        val (a, e) = cName(t) to cName(t.element)
        "static inline $a $name(apr_pool_t* pool, size_t n) {\n\t$a a = {n, __ec_palloc(pool, n * sizeof($e))};\n" +
            "\tfor (size_t i = 0; i < n; i++) a.data[i] = ${zero(t.element)};\n\treturn a;\n}\n"
    }

    /**
     * C that deep-copies [expr] (a [t]) into the pool [pool] (`clone(x)`): Strings, CStrings, arrays and what
     * pointers point to are copied, recursively. [expr] itself when a [t] holds no memory to copy.
     */
    fun clone(t: Type, expr: String, pool: String): String = if (!ownsMemory(t)) expr else "${cloner(t)}($pool, $expr)"

    /**
     * Whether a [t] points to memory that `clone` copies. Not: numbers and atoms, function pointers, C arrays (they
     * have no length), `@Any`, and pointers to C types eclang knows no fields of (`@Pool`); those are kept as they are.
     */
    private fun ownsMemory(t: Type, seen: MutableSet<Type> = HashSet()): Boolean {
        if (!seen.add(t)) return false
        return when (t) {
            Types.String, Types.CString -> true
            is ArrayType -> !t.isC
            is Pointer -> sized(t.target)
            is Union -> t.members.any { ownsMemory(it, seen) }
            is Tuple -> t.elements.any { ownsMemory(it, seen) }
            is Record -> t.fields.values.any { ownsMemory(it, seen) }
            else -> false
        }
    }

    /** Whether C knows the size of [t], so a copy of it can be allocated. */
    private fun sized(t: Type): Boolean = when (t) {
        Types.Any, Types.None, Types.Never -> false
        is Record -> !(t.isExternal && t.fields.isEmpty())
        is Interface, is TypeParam -> false
        else -> true
    }

    /** `__ec_clone_T(pool, v)`: a deep copy of `v`, a [t]; see [clone]. */
    private fun cloner(t: Type): String = helper("__ec_clone_${mangle(t)}") { name ->
        val c = cName(t)
        prototypes.append("static inline $c $name(apr_pool_t* pool, $c v);\n")
        val body = when {
            t === Types.String -> "\tchar* s = __ec_palloc(pool, v.len + 1);\n\tif (v.len) memcpy(s, v.data, v.len);\n\ts[v.len] = 0;\n\treturn (__ec_string_t){v.len, s};\n"
            t === Types.CString -> "\tsize_t n = strlen(v) + 1;\n\tchar* s = __ec_palloc(pool, n);\n\tmemcpy(s, v, n);\n\treturn s;\n"
            t is ArrayType -> {
                val e = cName(t.element)
                val copy = if (ownsMemory(t.element)) "\tfor (size_t i = 0; i < v.len; i++) a.data[i] = ${clone(t.element, "v.data[i]", "pool")};\n"
                    else "\tif (v.len) memcpy(a.data, v.data, v.len * sizeof($e));\n"
                "\t$c a = {v.len, __ec_palloc(pool, v.len * sizeof($e))};\n$copy\treturn a;\n"
            }
            t is Pointer -> {
                // A pointer to a record that is extended may point to a child record: copy the whole child.
                val target = t.target
                val children = (target as? Record)?.descendants?.drop(1) ?: listOf()
                val cases = children.joinToString("") { child ->
                    val cc = cName(child)
                    "\tcase ${child.id}: { $cc* p = __ec_palloc(pool, sizeof($cc)); *p = ${clone(child, "*($cc*)v", "pool")}; return ($c)p; }\n"
                }
                val dispatch = if (cases.isEmpty()) "" else "\tswitch (*(int*)v) {\n$cases\tdefault: break;\n\t}\n"
                val tc = cName(target)
                "$dispatch\t$tc* p = __ec_palloc(pool, sizeof($tc));\n\t*p = ${clone(target, "*v", "pool")};\n\treturn p;\n"
            }
            t is Union && isPlain(t) -> {
                val member = t.members.first { it !== Types.Null }
                "\treturn v == NULL ? NULL : ${clone(member, "v", "pool")};\n"
            }
            t is Union -> {
                val cases = t.members.mapIndexedNotNull { i, m ->
                    if (m === Types.Null || !ownsMemory(m)) null else "\tcase $i: r.v.m$i = ${clone(m, "v.v.m$i", "pool")}; break;\n"
                }
                "\t$c r = v;\n\tswitch (v.tag) {\n${cases.joinToString("")}\tdefault: break;\n\t}\n\treturn r;\n"
            }
            t is Tuple -> {
                val fields = t.elements.mapIndexedNotNull { i, e -> if (ownsMemory(e)) "\tr.${tupleField(i)} = ${clone(e, "v.${tupleField(i)}", "pool")};\n" else null }
                "\t$c r = v;\n${fields.joinToString("")}\treturn r;\n"
            }
            t is Record -> {
                val fields = t.fields.mapNotNull { (field, ft) -> if (ownsMemory(ft)) "\tr.$field = ${clone(ft, "v.$field", "pool")};\n" else null }
                "\t$c r = v;\n${fields.joinToString("")}\treturn r;\n"
            }
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

    /** By-value upcast: copies the parent's fields out of the child (the copy really is a [to]). */
    private fun upcast(from: Record, to: Record): String = helper("__ec_up_${from.cName}__${to.cName}") { name ->
        val fields = to.fields.keys.map { ".$it = v.$it" } + if (to.inHierarchy) listOf(".__ec_tid = ${to.id}") else listOf()
        "static inline ${cName(to)} $name(${cName(from)} v) { return (${cName(to)}){${fields.joinToString(", ")}}; }\n"
    }

    /**
     * `__ec_pow_Int(b, e)`: b to the power e for a whole-number type [t], by repeated squaring in [t] itself, so a
     * Long result keeps every digit. A negative exponent gives 0, except for a base of 1 or -1.
     */
    fun intPow(t: Type): String = helper("__ec_pow_${mangle(t)}") { name ->
        val c = cName(t)
        val negative = if ((t as Prim).category == Category.SINT) "\tif (e < 0) return b == 1 ? 1 : b == -1 ? ((e & 1) ? -1 : 1) : 0;\n" else ""
        "static inline $c $name($c b, $c e) {\n$negative\t$c r = 1;\n\twhile (e > 0) {\n\t\tif (e & 1) r *= b;\n\t\te >>= 1;\n\t\tif (e > 0) b *= b;\n\t}\n\treturn r;\n}\n"
    }

    /** `__ec_enum_to_c_Name(atom)`: the C constant of an atom of a `declare enum` ([members]: atom label to constant). */
    fun enumToC(cName: String, members: List<Pair<String, String>>): String = helper("__ec_enum_to_c_$cName") { name ->
        helperAtoms += members.dropLast(1).map { it.first }
        val chain = members.dropLast(1).joinToString("") { (atom, constant) -> "v == __ec_atom_$atom ? $constant : " } + members.last().second
        "static inline int $name(char* v) { return $chain; }\n"
    }

    /** `__ec_enum_of_c_Name(value, where)`: the atom of a C constant of a `declare enum`; any other value stops the program. */
    fun enumFromC(cName: String, enumName: String, members: List<Pair<String, String>>): String = helper("__ec_enum_of_c_$cName") { name ->
        helperAtoms += members.map { it.first }
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
            val read = "($value)${if (throughPointer) "->" else "."}$field"
            "static inline ${cName(result)} $name(${cName(from)} v) {\n" +
                "\tif (${isCheck(from, Types.Null, "v")}) return ${convert(Types.Null, result, "NULL", "\"?.\"")};\n" +
                "\treturn ${convert(fieldType, result, read, "\"?.\"")};\n}\n"
        }

    private fun stringToArray(to: ArrayType): String = helper("__ec_str_to_${mangle(to)}") { name ->
        "static inline ${cName(to)} $name(__ec_string_t s) { return (${cName(to)}){s.len, s.data}; }\n"
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
            target is AtomLiteral -> "(($expr) == __ec_atom_${target.name})"
            else -> "(($expr) != NULL)"
        }
        return matching.joinToString(" || ", "(", ")") { "($expr).tag == ${tagOf(from, it)}" }
    }
}
