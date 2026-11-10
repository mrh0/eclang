package github.mrh0.ect2

import github.mrh0.ect2.Category.*

/**
 * Creates, names and compares types for one compilation. There is no global state: two contexts never
 * share anything except the built-ins in [Types], so compilations can run side by side (or in tests).
 *
 * The central question is [cost]: "can a value of type `from` be used where `to` is expected, and how
 * much converting does that take?" Everything else (accepts, subtyping, union cleanup, overload
 * resolution) is built on top of it.
 */
class TypeContext {
    companion object {
        /** [cost] result meaning "not allowed". */
        const val NO = -1
    }

    private val created = ArrayList<Type>(Types.ALL)
    private val interned = HashMap<List<Any>, Type>()
    private val names = LinkedHashMap<String, Type>()
    private val costCache = HashMap<Long, Int>()

    /** All types made in this context (built-ins first), in creation order. Stable, so generated C is reproducible. */
    val allTypes: List<Type> get() = created

    val number: Type
    val integer: Type

    /** `Pool`: an APR memory pool, `@apr_pool_t` (`apr_pool_t*` in C), so it works with every APR binding. */
    val pool: Pointer

    init {
        Types.ALL.forEach { names["core:${it.name}"] = it }
        number = union(Types.numbers).also { define("core", "Number", it) }
        integer = union(Types.integers).also { define("core", "Integer", it) }
        val aprPool = record("core", "apr_pool_t", isExternal = true).also { it.define(mapOf()) }
        pool = pointer(aprPool).also { define("core", "Pool", it) }
    }

    // ---------------------------------------------------------------------------------------------------
    // Creating types. Structural types are interned: asking twice gives the same object.

    fun pointer(target: Type): Pointer = intern(listOf("@", target)) { Pointer(it, target) }
    fun array(element: Type): ArrayType = intern(listOf("[]", element)) { ArrayType(it, element, false) }
    fun cArray(element: Type): ArrayType = intern(listOf("[]c", element)) { ArrayType(it, element, true) }
    fun fixedArray(element: Type, size: Long): FixedArray = intern(listOf("[n]", element, size)) { FixedArray(it, element, size) }
    fun tuple(elements: List<Type>): Tuple = intern(listOf<Any>("()") + elements) { Tuple(it, elements.toList()) }
    fun atom(name: String): AtomLiteral = intern(listOf(":", name)) { AtomLiteral(it, name) }
    fun typeParam(name: String): TypeParam = intern(listOf("<>", name)) { TypeParam(it, name) }
    fun function(params: List<Type>, returns: Type, throws: Type? = null, isC: Boolean = false): FunctionType =
        intern(listOf(if (isC) "=>c" else "=>", returns, throws ?: Types.None) + params) { FunctionType(it, params.toList(), returns, throws, isC) }

    /**
     * [t] as C sees it, for what a `declare` says: its function types (also inside pointers, unions, arrays and other
     * function types) are C function pointers rather than eclang function values.
     */
    fun inC(t: Type): Type = when (t) {
        is FunctionType -> function(t.params.map { inC(it) }, inC(t.returns), t.throws?.let { inC(it) }, isC = true)
        is Pointer -> pointer(inC(t.target))
        is ArrayType -> inC(t.element).let { if (t.isC) cArray(it) else array(it) }
        is FixedArray -> fixedArray(inC(t.element), t.size)
        is Union -> union(t.members.map { inC(it) })
        else -> t
    }
    fun result(value: Type, error: Type): Result = intern(listOf("throws", value, error)) { Result(it, value, error) }
    fun intLiteral(value: Long): IntLiteral = intern(listOf("int", value)) { IntLiteral(it, value) }
    fun floatLiteral(value: Double): FloatLiteral = intern(listOf("float", value)) { FloatLiteral(it, value) }

    /**
     * The type a value gets once it is stored: literals take their default type (Int, or Long when the value is
     * too big for an Int; Double for decimals). `val x = 5` makes x an Int.
     */
    fun concrete(type: Type): Type = when (type) {
        is IntLiteral -> when {
            fits(type.value, Types.Int) -> Types.Int
            else -> Types.Long
        }
        is FloatLiteral -> Types.Double
        else -> type
    }

    /** Can [value] be stored exactly in the integer or float type [to]? */
    fun fits(value: Long, to: Prim): Boolean = when (to.category) {
        SINT -> value >= -(1L shl (to.bits - 1)) && (to.bits == 64 || value < (1L shl (to.bits - 1)))
        UINT -> value >= 0 && (to.bits == 64 || value < (1L shl to.bits))
        FLOAT -> Math.abs(value) <= (1L shl mantissaBits(to))
        else -> false
    }

    /** `T?` */
    fun nullable(type: Type): Type = union(type, Types.Null)

    /** `T!`: [type] without Null. */
    fun nonNull(type: Type): Type = if (type is Union) union(type.members.filter { it !== Types.Null }) else type

    fun union(vararg types: Type): Type = union(types.asList())

    /**
     * Builds `A | B | ...` in normal form, so that equal unions are always the same object:
     * nested unions are flattened, duplicates and Never are dropped, Any swallows everything, members already
     * covered by another member are dropped (`:a | Atom` is `Atom`), and the rest is sorted by id.
     * One member left gives that member, zero gives Never.
     */
    fun union(types: Collection<Type>): Type {
        val flat = LinkedHashSet<Type>()
        for (t in types) if (t is Union) flat.addAll(t.members) else flat.add(t)
        flat.remove(Types.Never)
        if (Types.Any in flat) return Types.Any
        val members = flat.filter { m -> flat.none { other -> other !== m && isSubtype(m, other) } }.sortedBy { it.id }
        return when (members.size) {
            0 -> Types.Never
            1 -> members[0]
            else -> intern(listOf<Any>("|") + members) { Union(it, members) }
        }
    }

    /** Declares a record. Call [Declared.define] on the result to give it fields. */
    fun record(
        namespace: String, name: String, parent: Record? = null,
        externalName: String? = null, isExternal: Boolean = false, isCStruct: Boolean = false, layout: Layout = Layout(),
    ): Record = Record(nextId(), namespace, name, parent, externalName, isExternal, isCStruct, layout = layout)
        .also { created += it; define(namespace, name, it) }

    /** Declares a generic record; [instantiate] makes each instance with [recordInstance] and gives it its fields. */
    fun genericRecord(namespace: String, name: String, params: List<String>, isRef: Boolean = false, mutParams: Set<String> = setOf(), instantiate: (List<Type>) -> Record): GenericRecord =
        GenericRecord(nextId(), namespace, name, params, instantiate, isRef, mutParams).also { created += it; define(namespace, name, it) }

    /**
     * The record whose fields can't change that a value of type [t] is or points to (through `distinct`, `?` and one `@`),
     * or null when [t] is no such record: a `mut rec`, a `declare mut rec` or `declare struct`, an opaque C type (no fields to change), or not a
     * record. `<mut T>` takes only types where this is null.
     */
    fun fixedRecord(t: Type): Record? = when (t) {
        is Record -> t.takeIf { !it.isMutable && !(it.isExternal && (!it.isDefined || it.fields.isEmpty())) }
        is Distinct -> fixedRecord(t.base)
        is Pointer -> (t.target as? Record)?.let { fixedRecord(it) }
        is Union -> if (t.isNullable) fixedRecord(nonNull(t)) else null
        else -> null
    }

    /** Makes the instance of [generic] for [args], with the C name [cBase], and remembers it, so a field can refer to it. */
    fun recordInstance(generic: GenericRecord, args: List<Type>, cBase: String, layout: Layout = Layout()): Record =
        Record(nextId(), generic.namespace, args.joinToString(", ", "${generic.name}<", ">"), null, generic = generic, typeArgs = args, cBase = cBase, layout = layout)
            .also { created += it; generic.instances[args] = it }

    /** `List<Int>`: the instance of [generic] for [args], or an [Applied] while an argument is still generic. */
    fun apply(generic: GenericRecord, args: List<Type>): Type {
        val concrete = args.map { concrete(it) }
        if (concrete.any { hasTypeParam(it) }) return intern(listOf<Any>("<>", generic) + concrete) { Applied(it, generic, concrete) }
        return generic.instances[concrete] ?: generic.instantiate(concrete)
    }

    /** Whether [t] still has a `<T>` in it, so that it is only known once a call binds it. */
    fun hasTypeParam(t: Type): Boolean = when (t) {
        is TypeParam, is Applied -> true
        is Pointer -> hasTypeParam(t.target)
        is ArrayType -> hasTypeParam(t.element)
        is FixedArray -> hasTypeParam(t.element)
        is Tuple -> t.elements.any { hasTypeParam(it) }
        is Union -> t.members.any { hasTypeParam(it) }
        is FunctionType -> t.params.any { hasTypeParam(it) } || hasTypeParam(t.returns)
        else -> false
    }

    /** `type Name = distinct T`. */
    fun distinct(namespace: String, name: String, base: Type): Distinct =
        Distinct(nextId(), namespace, name, base).also { created += it; define(namespace, name, it) }

    /** Declares an interface. Call [Declared.define] on the result to give it fields. */
    fun interfaceType(namespace: String, name: String): Interface =
        Interface(nextId(), namespace, name).also { created += it; define(namespace, name, it) }

    // ---------------------------------------------------------------------------------------------------
    // Names. Aliases (`type Name as Int`) are just extra names for an existing type.

    fun define(namespace: String, name: String, type: Type) {
        val key = "$namespace:$name"
        require(key !in names) { "Type '$name' is already defined in '$namespace'" }
        names[key] = type
    }

    /** Looks [name] up in [namespace], then in the top level (namespace ""), then in core (built-ins). */
    fun lookup(namespace: String, name: String): Type? = names["$namespace:$name"] ?: names[":$name"] ?: names["core:$name"]

    /** The type called exactly [name] in [namespace], for qualified names (`fib.Point`). */
    fun lookupIn(namespace: String, name: String): Type? = names["$namespace:$name"]

    /** The namespaces that define a type called [name], for "it's in module x" hints. */
    fun namespacesOf(name: String): List<String> = names.keys.filter { it.substringAfter(':') == name }.map { it.substringBefore(':') }

    /** The types named in [namespace] itself, by name (for completion). */
    fun namedIn(namespace: String): Map<String, Type> =
        names.entries.filter { it.key.substringBefore(':') == namespace }.associate { it.key.substringAfter(':') to it.value }

    /** Every type name, for "did you mean" suggestions. */
    fun typeNames(): Set<String> = names.keys.map { it.substringAfter(':') }.toSet()

    // ---------------------------------------------------------------------------------------------------
    // Comparing types.

    /** Can a value of type [from] be used where [to] is expected (possibly with an implicit conversion)? */
    fun accepts(to: Type, from: Type) = cost(from, to) != NO

    /** Is every [from] already a [to], with no conversion at all? */
    fun isSubtype(from: Type, to: Type) = cost(from, to) == 0

    /**
     * How much converting it takes to use a [from] where a [to] is expected:
     * - `0`: nothing, [from] already is a [to] (same type, subtype, member of a union, ...),
     * - `1+`: an implicit, lossless conversion such as `Int -> Long`; smaller is a closer match,
     * - [NO]: not allowed without an explicit `as`.
     *
     * Results are cached, so calling this repeatedly is cheap.
     */
    fun cost(from: Type, to: Type): Int {
        if (from === to) return 0
        val key = (from.id.toLong() shl 32) or to.id.toLong()
        costCache[key]?.let { return it }
        val result = computeCost(from, to)
        costCache[key] = result
        return result
    }

    private fun computeCost(from: Type, to: Type): Int = when {
        to === Types.Any || from === Types.Never -> 0
        // Every member must fit; the union is as costly as its worst member.
        from is Union -> from.members.map { cost(it, to) }.let { if (NO in it) NO else it.max() }
        // Fits if it fits any member; pick the cheapest.
        to is Union -> to.members.map { cost(from, it) }.filter { it != NO }.minOrNull() ?: NO
        // A little more than its base, so `f(5)` picks `f(Int)` over `f(UserId)`.
        (from is IntLiteral || from is FloatLiteral) && to is Distinct -> cost(from, to.base).let { if (it == NO) NO else it + 1 }
        from is IntLiteral && to is Prim -> intLiteralCost(from.value, to)
        from is FloatLiteral && to is Prim -> when (to) { Types.Double -> 0; Types.Float -> 1; else -> NO }
        from is AtomLiteral -> if (to === Types.Atom) 0 else NO
        from is Record && to is Record -> from.distanceTo(to) ?: NO
        from is Declared && to is Interface -> if (implements(from, to)) 0 else NO
        from is Pointer && to is Pointer -> pointerCost(from.target, to.target)
        // `@Any` is C's `void*`: other data pointers (CString is `char*`, `T[]c` is `T*`) convert to it too.
        (from === Types.CString || (from is ArrayType && from.isC)) && to is Pointer && to.target === Types.Any -> 1
        from === Types.String && to is ArrayType && !to.isC && to.element === Types.Char -> 1
        // Copies the bytes, checked to fit when the program runs (a literal is checked when it compiles, see TString).
        from === Types.String && to is FixedArray && to.element === Types.Char -> 1
        from is Prim && to is Prim -> numberCost(from, to)
        // Arrays, tuples and function types must match exactly: their memory layout depends on the exact
        // element types, so `Int[]` is not a `Long[]` even though an Int fits in a Long.
        else -> NO
    }

    /**
     * `@Child -> @Parent` (the parent is laid out first), `@T -> @Any` (`void*`) and `@R -> @I` when record R has
     * interface I's fields (a function taking `@I` gets a copy for `@R`). Otherwise exact only.
     */
    private fun pointerCost(from: Type, to: Type): Int = when {
        to === Types.Any -> 1
        from is Record && to is Record -> from.distanceTo(to) ?: NO
        from is Declared && to is Interface -> if (implements(from, to)) 0 else NO
        else -> NO
    }

    /** True when [from] has every field of [to], each with exactly the same type. */
    private fun implements(from: Declared, to: Interface): Boolean {
        val fields = from.fields
        return to.fields.all { (name, type) -> fields[name] === type }
    }

    /**
     * Implicit number conversions. Only conversions that can never lose information are allowed:
     * to a wider type of the same signedness, unsigned to a strictly wider signed type, an integer to a
     * float that can hold it exactly, and Float to Double. Anything else (Long -> Int, Int -> UInt,
     * Long -> Double) needs `as`.
     */
    private fun numberCost(from: Prim, to: Prim): Int {
        if (!from.isNumber || !to.isNumber) return NO
        val widen = 1 + log2(to.bits) - log2(from.bits) // Char->Short 2, Char->Int 3, ULong->Size 1
        return when {
            from.category == FLOAT -> if (to.category == FLOAT && to.bits > from.bits) widen else NO
            to.category == FLOAT -> if (from.bits < mantissaBits(to)) 10 + widen else NO // prefer any integer match
            from.category == to.category -> if (to.bits >= from.bits) widen else NO
            from.category == UINT -> if (to.bits > from.bits) 1 + widen else NO
            else -> NO // signed -> unsigned
        }
    }

    /**
     * An integer literal fits every number type that holds it exactly. Int is preferred, then wider types,
     * then narrower ones, then floats, so `f(5)` picks `f(Int)` over `f(Long)` over `f(Char)` over `f(Double)`.
     */
    private fun intLiteralCost(value: Long, to: Prim): Int {
        if (!to.isNumber || !fits(value, to)) return NO
        val distance = log2(to.bits) - log2(Types.Int.bits)
        return when {
            to.category == FLOAT -> 10 + distance
            to === Types.Int -> 0
            else -> (if (distance > 0) distance else 1 - distance) + (if (to.category == UINT) 1 else 0)
        }
    }

    private fun mantissaBits(float: Prim) = if (float.bits == 32) 24 else 53
    private fun log2(bits: Int) = Integer.numberOfTrailingZeros(bits)

    /**
     * Explains, in words a beginner can act on, why a [from] can't be used where a [to] is expected.
     * Only meaningful when [accepts] is false.
     */
    fun explain(from: Type, to: Type): String {
        val fixWithAs = "convert it explicitly with `as $to`"
        return when {
            from is Result -> "it can fail with ${from.error}. Handle the error with `try`, `?? default` or `try ... catch e do`"
            from === Types.Null -> "null is not allowed here. To allow it, make the type nullable: `${nullable(to)}`"
            from is Distinct || to is Distinct -> "${(to as? Distinct ?: from)} is a `distinct` type, so nothing converts to or from it by itself; $fixWithAs"
            from is Union && from.isNullable && accepts(to, nonNull(from)) ->
                "the value may be null. Check it first with `if x != null`, give a default with `?? value`, or assert it is set with `!`"
            from is Union -> {
                val misfits = from.members.filter { !accepts(to, it) }
                "the value may be ${misfits.joinToString(" or ")}. Narrow it first, for example with `if x is $to`"
            }
            from is IntLiteral && to is Prim && to.isInteger -> "${from.value} does not fit in $to (${range(to)})"
            from is IntLiteral && to is Prim && to.category == FLOAT -> "${from.value} cannot be stored exactly in $to"
            from is FloatLiteral && to is Prim && to.isInteger -> "a decimal number can't be stored in $to; $fixWithAs (drops the fraction)"
            from is Prim && from.isNumber && to is Prim && to.isNumber -> when {
                from.category == FLOAT && to.category != FLOAT -> "$from → $to drops the fractional part; $fixWithAs"
                to.category == FLOAT -> "$from → $to may lose precision; $fixWithAs"
                from.category == SINT && to.category == UINT -> "$from may be negative but $to can't hold negative numbers; $fixWithAs"
                else -> "$from → $to may lose data ($to is smaller); $fixWithAs"
            }
            from is Tuple && to is Tuple -> if (from.elements.size != to.elements.size) "they have a different number of parts (${from.elements.size} and ${to.elements.size})"
                else "tuple part types must match exactly, because the parts are stored differently"
            from is FunctionType && to is FunctionType && from.isC != to.isC -> if (to.isC)
                "C calls a plain function pointer, which can't carry the variables a lambda uses. Pass `fn name` or a lambda here directly (one that uses no local variables); " +
                    "for data the callback needs, use the `@Any` argument such C functions take"
                else "a C function pointer isn't an eclang function value. Wrap the call in a lambda: `fn (x) = f(x)`"
            from is FunctionType && to is FunctionType -> "function types must match exactly: the same parameter types, return type and `throws`"
            from is ArrayType && to is ArrayType -> "array element types must match exactly, because the elements are stored differently"
            from is FixedArray && to is FixedArray -> if (from.element === to.element) "they have a different length (${from.size} and ${to.size})"
                else "array element types must match exactly, because the elements are stored differently"
            from is FixedArray && to is ArrayType && !to.isC && from.element === to.element -> "a fixed-size array holds its elements itself. To use it as a $to, slice it: `a[..]`"
            from is FixedArray && from.element === Types.Char && to === Types.CString -> "a fixed-size array holds its Chars itself, and need not end with a 0. `cstr(a)` is a copy of its text (up to its first 0) that does"
            from is FixedArray && (to === Types.CString || to is ArrayType || to is Pointer) -> "a fixed-size array holds its elements itself. `a.data` is a pointer to them"
            from is ArrayType && from.isC && from.element === Types.Char && to === Types.CString ->
                "a CString must end with a 0, and `.data` of a String or Char[N] need not (a slice doesn't). Use `cstr(s)` for a copy that does, or `x.data !as CString` when you know it ends with a 0"
            to is FixedArray && from is ArrayType -> "the length of a $from is only known when the program runs. Copy the elements one by one, or give an array literal"
            from is Pointer && to is Pointer && to.target is Interface ->
                if (from.target is Declared) explain(from.target, to.target) else "only a pointer to a record with the fields of ${to.target} fits $to"
            from is Pointer && to is Pointer -> "pointer types must match exactly; use `!as $to` to reinterpret the pointer"
            from is Record && to is Record -> "$from is not a $to (a record only fits the records it extends)"
            from is Declared && to is Interface -> {
                val fields = from.fields
                val missing = to.fields.filter { (name, type) -> fields[name] !== type }
                "$from is missing ${missing.entries.joinToString { "`${it.key}: ${it.value}`" }}, required by $to"
            }
            to is Pointer && from !is Pointer -> "a pointer is expected; take the address with `addrof`"
            from is Pointer && to !is Pointer -> "the value is a pointer; read what it points to with `@`"
            else -> "$from and $to are different types"
        }
    }

    private fun range(t: Prim) = when (t.category) {
        SINT -> "${-(1L shl (t.bits - 1))} to ${if (t.bits == 64) Long.MAX_VALUE else (1L shl (t.bits - 1)) - 1}"
        else -> "0 to ${if (t.bits == 64) "18446744073709551615" else ((1L shl t.bits) - 1).toString()}"
    }

    /**
     * Why a value of [type] can't be stored in a variable, field or return value, or null if it can.
     * Some types only make sense as parameter types, where each call gets its own copy of the function.
     */
    fun storageProblem(type: Type): String? = when (type) {
        Types.Any -> "Any has no fixed size in C. Use it only as a parameter type, or `@Any` for an untyped pointer"
        Types.Never -> "Never has no values"
        is Interface -> "interface ${type.name} has no fixed size in C. Use it as a parameter type (each record gets its own copy of the function), or store a union of records"
        is TypeParam, is Applied -> "generic type $type is only known when the function is called; use it in parameters and return types"
        is GenericRecord -> "${type.name} is generic, so it needs its type arguments, e.g. `${type.name}<${type.params.joinToString(", ") { "Int" }}>`"
        is IntLiteral, is FloatLiteral -> null
        is Distinct -> storageProblem(type.base)
        is Union -> type.members.firstNotNullOfOrNull { storageProblem(it) }
        is ArrayType -> storageProblem(type.element)
        is FixedArray -> storageProblem(type.element)
        is Pointer -> if (type.target is Interface) "a pointer to interface ${type.target.name} has no C type of its own. Use it as a parameter type (each record gets its own copy of the function)" else null
        is Tuple -> type.elements.firstNotNullOfOrNull { storageProblem(it) }
        is Result -> "the result of a call that can fail must be handled where the call is made"
        else -> null
    }

    /** The member of [union] that a [from] is stored as: the cheapest fit (Short into `Int | CString` is the Int). */
    fun memberFor(from: Type, union: Union): Type? =
        union.members.filter { cost(from, it) != NO }.minByOrNull { cost(from, it) }

    // ---------------------------------------------------------------------------------------------------
    // Overloads and generics.

    sealed class Resolution<out T> {
        data class Found<T>(val match: T, val cost: Int) : Resolution<T>()
        data class Ambiguous<T>(val matches: List<T>) : Resolution<T>()
        object NoMatch : Resolution<Nothing>()
    }

    /**
     * Picks the overload whose [params] fit [args] with the lowest total [cost]. For example, with
     * `f(Int)` and `f(Long)`, `f(10s)` (a Short) picks `f(Int)`. A tie between the best candidates is
     * reported as [Resolution.Ambiguous] instead of being picked at random.
     */
    fun <T> resolve(candidates: List<T>, args: List<Type>, params: (T) -> List<Type>): Resolution<T> {
        var best = Int.MAX_VALUE
        val bestMatches = ArrayList<T>()
        for (candidate in candidates) {
            val ps = params(candidate)
            if (ps.size != args.size) continue
            var total = 0
            for (i in args.indices) {
                val c = cost(args[i], ps[i])
                if (c == NO) { total = NO; break }
                total += c
            }
            if (total == NO || total > best) continue
            if (total < best) { best = total; bestMatches.clear() }
            bestMatches += candidate
        }
        return when (bestMatches.size) {
            0 -> Resolution.NoMatch
            1 -> Resolution.Found(bestMatches[0], best)
            else -> Resolution.Ambiguous(bestMatches)
        }
    }

    /**
     * Matches a parameter type that may contain generics against an argument type, recording what each
     * `<T>` stands for in [bindings]. Returns false if they do not fit. For `fn first(a: <T>[]): <T>` called
     * with an `Int[]`, this binds `T` to `Int`; then [substitute] turns the return type `<T>` into `Int`.
     */
    fun bind(param: Type, arg: Type, bindings: MutableMap<TypeParam, Type>): Boolean = when (param) {
        is TypeParam -> bindings[param]?.let { accepts(it, arg) } ?: run { bindings[param] = concrete(arg); true }
        is Pointer -> arg is Pointer && bind(param.target, arg.target, bindings)
        is ArrayType -> arg is ArrayType && arg.isC == param.isC && bind(param.element, arg.element, bindings)
        is FixedArray -> arg is FixedArray && arg.size == param.size && bind(param.element, arg.element, bindings)
        is Tuple -> arg is Tuple && arg.elements.size == param.elements.size &&
            param.elements.indices.all { bind(param.elements[it], arg.elements[it], bindings) }
        is FunctionType -> arg is FunctionType && arg.isC == param.isC && arg.params.size == param.params.size &&
            param.params.indices.all { bind(param.params[it], arg.params[it], bindings) } &&
            bind(param.returns, arg.returns, bindings)
        is Applied -> arg is Record && arg.generic === param.generic && param.args.indices.all { bind(param.args[it], arg.typeArgs[it], bindings) }
        // `<T>?` takes a T or null; null says nothing about T.
        is Union -> if (!hasTypeParam(param)) accepts(param, arg) else {
            val member = param.members.singleOrNull { it !== Types.Null }
            member != null && param.isNullable && (arg === Types.Null || bind(member, nonNull(arg), bindings))
        }
        else -> accepts(param, arg)
    }

    /** Replaces every bound `<T>` inside [type]. */
    fun substitute(type: Type, bindings: Map<TypeParam, Type>): Type = when (type) {
        is TypeParam -> bindings[type] ?: type
        is Pointer -> pointer(substitute(type.target, bindings))
        is ArrayType -> substitute(type.element, bindings).let { if (type.isC) cArray(it) else array(it) }
        is FixedArray -> fixedArray(substitute(type.element, bindings), type.size)
        is Tuple -> tuple(type.elements.map { substitute(it, bindings) })
        is Applied -> apply(type.generic, type.args.map { substitute(it, bindings) })
        is Union -> union(type.members.map { substitute(it, bindings) })
        is FunctionType -> function(type.params.map { substitute(it, bindings) }, substitute(type.returns, bindings),
            type.throws?.let { substitute(it, bindings) }, type.isC)
        else -> type
    }

    // ---------------------------------------------------------------------------------------------------

    private fun nextId() = created.size

    @Suppress("UNCHECKED_CAST")
    private fun <T : Type> intern(key: List<Any>, create: (id: Int) -> T): T =
        interned.getOrPut(key) { create(nextId()).also { created += it } } as T
}
