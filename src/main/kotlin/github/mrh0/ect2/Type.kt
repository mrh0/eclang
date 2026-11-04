package github.mrh0.ect2

/**
 * An eclang type.
 *
 * Types are immutable and never built with `new` directly: built-ins live in [Types], everything else
 * is made by a [TypeContext], which interns them. Interning means two structurally equal types are the
 * same object, so equality is `===` and [id] is a small, stable number usable as a cache key.
 */
sealed class Type(val id: Int) {
    final override fun equals(other: Any?) = this === other
    final override fun hashCode() = id
}

/** What a [Prim] is, used by the conversion rules. */
enum class Category { TOP, BOTTOM, UNIT, NULL, BOOL, SINT, UINT, FLOAT, ATOM, STRING, CSTRING }

/** A built-in type without type parameters: numbers, Bool, Any, None, ... See [Types]. */
class Prim internal constructor(id: Int, val name: String, val category: Category, val bits: Int = 0) : Type(id) {
    val isInteger get() = category == Category.SINT || category == Category.UINT
    val isNumber get() = isInteger || category == Category.FLOAT
    override fun toString() = name
}

/** `@T`: a non-null pointer to [target]. A nullable pointer is `@T?`. */
class Pointer internal constructor(id: Int, val target: Type) : Type(id) {
    override fun toString() = "@${paren(target)}"
}

/** `T[]` (eclang array with length) or `T[]c` (plain C array) when [isC]. */
class ArrayType internal constructor(id: Int, val element: Type, val isC: Boolean) : Type(id) {
    override fun toString() = "${paren(element)}[]${if (isC) "c" else ""}"
}

/** `(A, B, C)`: an ordered, fixed-size group of values. */
class Tuple internal constructor(id: Int, val elements: List<Type>) : Type(id) {
    override fun toString() = elements.joinToString(", ", "(", ")")
}

/**
 * `A | B`: a value of any one of [members]. Always normalized by [TypeContext.union]: flat (no nested unions),
 * no duplicates, sorted by id, at least two members, and never containing Any or Never.
 */
class Union internal constructor(id: Int, val members: List<Type>) : Type(id) {
    val isNullable get() = Types.Null in members
    override fun toString(): String {
        val nonNull = members.filter { it !== Types.Null }
        return if (isNullable && nonNull.size == 1) "${paren(nonNull[0])}?" else members.joinToString(" | ")
    }
}

/** `(A, B) => R`, optionally `throws E`. */
class FunctionType internal constructor(id: Int, val params: List<Type>, val returns: Type, val throws: Type?) : Type(id) {
    override fun toString() = "${params.joinToString(", ", "(", ")")} => $returns" + (throws?.let { " throws $it" } ?: "")
}

/**
 * The result of calling a function declared `fn f(): T throws E`: either a [value] of type T or an [error] of type E.
 * It can't be used directly; the caller has to handle it with `try`, `??` or `try ... catch`.
 */
class Result internal constructor(id: Int, val value: Type, val error: Type) : Type(id) {
    override fun toString() = "$value throws $error"
}

/**
 * An integer literal such as `300`, before its type is decided. It fits any number type that can hold [value]
 * exactly, so `val c: Char = 10` works. When nothing decides its type, it becomes Int (see [TypeContext.concrete]).
 */
class IntLiteral internal constructor(id: Int, val value: Long) : Type(id) {
    override fun toString() = "$value"
}

/** A decimal literal such as `2.5`. Fits Float and Double; becomes Double when nothing else decides. */
class FloatLiteral internal constructor(id: Int, val value: Double) : Type(id) {
    override fun toString() = "$value"
}

/** `:name`: a single atom. `Atom` (in [Types]) is the type of every atom. */
class AtomLiteral internal constructor(id: Int, val name: String) : Type(id) {
    override fun toString() = ":$name"
}

/** `<T>`: a generic parameter, replaced by a concrete type at each call site (see [TypeContext.bind]). */
class TypeParam internal constructor(id: Int, val name: String) : Type(id) {
    override fun toString() = "<$name>"
}

/**
 * A type declared by the user with fields. Records are nominal: two records with the same fields are still
 * different types. [fields] are set once with [define], after creation, so a record can refer to itself
 * (`rec Node as next: @Node?`).
 */
sealed class Declared(id: Int, val namespace: String, val name: String) : Type(id) {
    private var ownFields: Map<String, Type>? = null
    val isDefined get() = ownFields != null

    fun define(fields: Map<String, Type>) {
        check(ownFields == null) { "$name is already defined" }
        ownFields = LinkedHashMap(fields)
    }

    /** Fields declared directly on this type, in declaration order. */
    val ownFieldMap: Map<String, Type> get() = ownFields ?: error("$name is used before its fields are defined")

    /** All fields, including inherited ones (parent fields first). */
    open val fields: Map<String, Type> get() = ownFieldMap

    fun field(name: String): Type? = fields[name]

    /** The C name of a type defined in eclang: `fib__Point` for Point in module fib, plain `Point` elsewhere. */
    val cName: String get() = if (namespace.isEmpty() || namespace == "core") name else "${namespace}__$name"

    override fun toString() = name
}

/**
 * `rec Name extends Parent as ...`, or a C type made visible with `declare rec` / `declare struct`.
 * - [externalName]: the C name, when it differs from [name] (`declare rec Pool extern "apr_pool_t"`).
 * - [isExternal]: declared in C, so eclang never emits its definition.
 * - [isCStruct]: named `struct x` in C rather than through a typedef.
 */
class Record internal constructor(
    id: Int, namespace: String, name: String, val parent: Record?,
    val externalName: String? = null, val isExternal: Boolean = false, val isCStruct: Boolean = false,
) : Declared(id, namespace, name) {
    private val childList = ArrayList<Record>()

    /** Records that directly extend this one. */
    val children: List<Record> get() = childList

    /** True when this record extends, or is extended by, another record (it then carries a runtime type tag). */
    val inHierarchy get() = parent != null || childList.isNotEmpty()

    /** This record and every record below it. */
    val descendants: List<Record> get() = listOf(this) + childList.flatMap { it.descendants }

    init { parent?.childList?.add(this) }

    override val fields: Map<String, Type> get() = if (parent == null) ownFieldMap else parent.fields + ownFieldMap

    /** Number of `extends` steps from this record up to [ancestor], or null if it is not an ancestor. */
    fun distanceTo(ancestor: Record): Int? {
        var current: Record? = this
        var steps = 0
        while (current != null) {
            if (current === ancestor) return steps
            current = current.parent
            steps++
        }
        return null
    }
}

/** `inter Name as ...`: any record with (at least) these fields, with exactly these types, matches. */
class Interface internal constructor(id: Int, namespace: String, name: String) : Declared(id, namespace, name)

private fun paren(t: Type) = if (t is Union && !(t.isNullable && t.members.size == 2) || t is FunctionType) "($t)" else t.toString()
