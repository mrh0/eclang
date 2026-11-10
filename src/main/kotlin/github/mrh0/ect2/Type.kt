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
    /** A pointer to a `ref rec` is written as the record's name: that is what `Counter` means in a type. */
    override fun toString() = when {
        target is Record && target.layout.isRef -> target.name
        target is Applied && target.generic.isRef -> target.toString()
        else -> "@${paren(target)}"
    }
}

/** `T[]` (eclang array with length) or `T[]c` (plain C array) when [isC]. */
class ArrayType internal constructor(id: Int, val element: Type, val isC: Boolean) : Type(id) {
    override fun toString() = "${paren(element)}[]${if (isC) "c" else ""}"
}

/**
 * `T[N]`: [size] elements stored in the value itself, like a C array in a struct. Assigning one copies its elements.
 * Each length is a type of its own; the elements are reached as an array with `a[..]`, or as a C array with `a.data`.
 */
class FixedArray internal constructor(id: Int, val element: Type, val size: Long) : Type(id) {
    override fun toString() = "${paren(element)}[$size]"
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

/**
 * `(A, B) => R`, optionally `throws E`. In eclang it is a function value, which can carry the variables a lambda uses
 * (a closure). With [isC] it is a plain C function pointer: a function type written in a `declare`, which C calls
 * directly, so only a function without captured variables (`fn name`, or a lambda that uses none) converts to it.
 */
class FunctionType internal constructor(id: Int, val params: List<Type>, val returns: Type, val throws: Type?, val isC: Boolean = false) : Type(id) {
    override fun toString() = (if (isC) "extern " else "") + "${params.joinToString(", ", "(", ")")} => $returns" + (throws?.let { " throws $it" } ?: "")
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
sealed class Declared(id: Int, val namespace: String, val name: String, private val cBase: String = name) : Type(id) {
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
    val cName: String get() = if (namespace.isEmpty() || namespace == "core") cBase else "${namespace}__$cBase"

    override fun toString() = name
}

/**
 * How a record is laid out in memory: `packed` (no padding between fields), `align(n)` (its address is a multiple of n),
 * and [isUnion], for `union Name as` (all fields start at the same address, so it holds one of them at a time).
 */
/**
 * How a record is laid out in C: `packed`, `align(n)`, a `union`. [isRef] is for `ref rec`, which doesn't change the struct but
 * what the record's name means in a type: a pointer to it (see [Pointer.toString]).
 */
/**
 * [isResource]: `resource rec`, a value that can't be dropped: each one must be passed on (a thread to join, say).
 * [isSync]: `sync rec`, a record its own functions make safe to share between threads (a lock, a queue): see `Sharing`.
 * [isMut]: `mut rec`, whose fields can change after it is made. Other records are fixed: a change makes a new one
 * (`{ ...p, x = 1 }`). See [Record.isMutable].
 */
class Layout(val packed: Boolean = false, val align: Long? = null, val isUnion: Boolean = false, val isRef: Boolean = false, val isResource: Boolean = false,
             val isSync: Boolean = false, val isMut: Boolean = false) {
    /** Whether C's default layout of a struct applies, so that a child record starts like its parent. */
    val isDefault get() = !packed && align == null && !isUnion
}

/**
 * `rec Name extends Parent as ...`, or a C type made visible with `declare rec` / `declare struct`.
 * - [externalName]: the C name, when it differs from [name] (`declare rec Pool extern "apr_pool_t"`).
 * - [isExternal]: declared in C, so eclang never emits its definition.
 * - [isCStruct]: named `struct x` in C rather than through a typedef.
 * - [generic], [typeArgs]: for an instance of a generic record, `List<Int>`: its template and `[Int]`. Its [name] is
 *   `List<Int>`, and [cBase] what C calls it.
 * - [layout]: `packed`, `align(n)` and `union`.
 */
class Record internal constructor(
    id: Int, namespace: String, name: String, val parent: Record?,
    val externalName: String? = null, val isExternal: Boolean = false, val isCStruct: Boolean = false,
    val generic: GenericRecord? = null, val typeArgs: List<Type> = listOf(), cBase: String = name, val layout: Layout = Layout(),
) : Declared(id, namespace, name, cBase) {
    private val childList = ArrayList<Record>()

    /** The bit-fields among the fields (`flags: UInt : 3`), with their widths in bits. */
    val bitWidths = HashMap<String, Int>()

    /** Fields of a `declare rec` whose C name differs from their eclang name (`input extern "in": ...`): eclang name to C name. */
    val cFieldNames = HashMap<String, String>()

    val isUnion get() = layout.isUnion

    /** Whether eclang code can change its fields after it is made: a `mut rec`, a `declare mut rec`, or a `declare struct` (always, for C compatibility). */
    val isMutable get() = layout.isMut || isCStruct

    /** What each type parameter of [generic] stands for in this instance: `T` to Int. */
    val typeBindings: Map<String, Type> get() = generic?.params?.zip(typeArgs)?.toMap() ?: mapOf()

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

/**
 * `rec List<T> as ...`: not a type of values, but a template for them. Each `List<Int>` is a [Record] of its own, made
 * once by [instantiate] (see [TypeContext.apply]), as each call of a generic function compiles a copy of the function.
 */
class GenericRecord internal constructor(
    id: Int, val namespace: String, val name: String, val params: List<String>, val instantiate: (List<Type>) -> Record,
    /** `ref rec List<T>`: `List<Int>` in a type is a pointer to the instance. */
    val isRef: Boolean = false,
    /** The parameters written `mut T` (`rec Box<mut T>`): their arguments must be types whose fields can change (see [TypeContext.fixedRecord]). */
    val mutParams: Set<String> = setOf(),
) : Type(id) {
    /** The instances made so far, by their type arguments. */
    val instances = HashMap<List<Type>, Record>()
    override fun toString() = name
}

/**
 * `List<T>` where a type argument is still generic, in the signature of a generic function. [TypeContext.bind] matches it
 * against an instance of [generic], and [TypeContext.substitute] turns it into one.
 */
class Applied internal constructor(id: Int, val generic: GenericRecord, val args: List<Type>) : Type(id) {
    override fun toString() = args.joinToString(", ", "${generic.name}<", ">")
}

/**
 * `type Name = distinct T`: a type of its own with the values of [base], at no cost at runtime (a C typedef). Nothing
 * converts to or from it implicitly, except number literals; `as` converts between the two.
 */
class Distinct internal constructor(id: Int, val namespace: String, val name: String, val base: Type) : Type(id) {
    /** The C name: `fib__UserId` in module fib, plain `UserId` elsewhere. */
    val cName: String get() = if (namespace.isEmpty()) name else "${namespace}__$name"
    override fun toString() = name
}

/** `inter Name as ...`: any record with (at least) these fields, with exactly these types, matches. */
class Interface internal constructor(id: Int, namespace: String, name: String) : Declared(id, namespace, name)

private fun paren(t: Type) = if (t is Union && !(t.isNullable && t.members.size == 2) || t is FunctionType) "($t)" else t.toString()
