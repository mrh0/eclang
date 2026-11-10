package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.function.TLambda
import github.mrh0.eclang.ast.token.function.TNamedFunction
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.function.Parameter
import github.mrh0.eclang.error.EcMemoryError
import github.mrh0.ect2.*

/**
 * What may go from one thread to another: an argument of a `flat` parameter (`thread.spawn`'s data, an item of a
 * `thread.Queue`). As in Goose, where threads share no memory and only values without pointers cross between them, such a
 * value must be flat: numbers, Bools, atoms, and records, tuples and `T[N]` of those. The one kind of pointer allowed is one to a
 * `sync` record, whose own functions make sharing it safe (a Mutex, an Atomic, a Queue): the compiler doesn't look inside one,
 * so `sync rec` vouches for what it holds, as `unsafe` does for code. Anything else that points (a String, an array, a List, a
 * pointer to a plain record, a function value) could reach memory the other thread frees, or change it without a lock.
 */
object Sharing {
    /**
     * Why a value of [t] isn't flat, with [what] naming it ("it", "its field 'name'"), or null when it is. [seen] stops at a
     * record already being looked at.
     */
    fun problem(t: Type, what: String = "it", seen: MutableSet<Type> = HashSet()): String? {
        if (!seen.add(t)) return null
        return when (t) {
            Types.String -> "$what is a String, which points to its text"
            Types.CString -> "$what is a CString, which points to its text"
            is Pointer -> {
                val target = t.target
                if (target is Record && target.layout.isSync) null
                else "$what is a $t, a pointer to ${if (target is Record) "a record that isn't `sync`" else "a $target"}"
            }
            is ArrayType -> "$what is a $t, which points to its elements"
            is FunctionType -> if (t.isC) null else "$what is a function value, which points to what its lambda captured"
            is Distinct -> problem(t.base, what, seen)
            is Union -> t.members.firstNotNullOfOrNull { problem(it, what, seen) }
            is Tuple -> t.elements.withIndex().firstNotNullOfOrNull { (i, e) -> problem(e, "${element(what)} ${i + 1}", seen) }
            is FixedArray -> problem(t.element, "an element of ${if (what == "it") "it" else what.removePrefix("its ")}", seen)
            is Result -> problem(t.value, what, seen) ?: problem(t.error, what, seen)
            is Record -> if (t.layout.isSync) null else t.fields.entries.firstNotNullOfOrNull { (name, type) -> problem(type, field(what, name), seen) }
            else -> if (Lifetimes.holdsPointer(t)) "$what is a $t, which points to other memory" else null
        }
    }

    /** `its field 'a'`, then `its field 'a.b'`; `'x.a'` for a capture `'x'`. */
    private fun field(what: String, name: String) = when {
        what == "it" -> "its field '$name'"
        what.endsWith("'") -> what.dropLast(1) + ".$name'"
        else -> "$what's field '$name'"
    }

    private fun element(what: String) = if (what == "it") "its element" else "$what's element"

    /**
     * The argument [value] (of [type]) of [param], a `flat` parameter of [callee], at [location]: what it reaches must be flat
     * ([problem]). For a pointer parameter that is what it points to; for a lambda, what it captures. Not checked inside `unsafe`.
     */
    fun checkArgument(cd: CompileData, location: Loc, callee: String, param: Parameter, value: ITok, type: Type) {
        if (cd.isUnsafe) return
        val goes = "'$callee' may give '${param.name}' to another thread (it is `flat`), so it must reach no memory but `sync` records"
        val fix = "Give the thread copies of numbers and records of them, and share what threads use together through a `sync` record " +
            "(a thread.Mutex or thread.Queue, or a `sync rec` of your own that guards its fields with one)"
        val unsafe = "or, when nothing else frees or changes that memory while the thread runs, say so with `unsafe $callee(...)`."
        when (val v = value.unnested()) {
            is TNamedFunction -> return
            is TLambda -> {
                for (c in v.captures) {
                    // An `escaping` parameter rejects a shared `var` itself (Escapes), with what it is about: its lifetime.
                    if (c.byReference && !param.isEscaping) throw EcMemoryError(location, "$goes, but the lambda shares the `var` '${c.name}' with this function, which goes on changing it. " +
                        "Copy it into a `val` before the lambda (`val ${c.name}Now = ${c.name}`) and use that, $unsafe")
                    if (c.byReference) continue
                    problem(c.type, "'${c.name}'")?.let { why ->
                        throw EcMemoryError(location, "$goes, but the lambda captures '${c.name}', and $why. $fix, $unsafe")
                    }
                }
                return
            }
        }
        val checked = if (cd.types.nonNull(param.type) is Pointer) (cd.types.nonNull(type) as? Pointer)?.target ?: type else type
        if (type is FunctionType && !type.isC)
            throw EcMemoryError(location, "$goes, but this is a function value, and what its lambda captured can't be seen here. Write the lambda in the call, or pass `fn name`, $unsafe")
        problem(checked)?.let { why ->
            val but = if (checked === type && why.startsWith("it ")) "'${param.name}' ${why.removePrefix("it ")}"
                else if (checked === type) "in '${param.name}', $why" else "in what '${param.name}' points to (a $checked), $why"
            throw EcMemoryError(location, "$goes, but $but. $fix, $unsafe")
        }
    }
}
