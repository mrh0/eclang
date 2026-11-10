package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.util.StringLiteral

/**
 * `if (!(condition)) __ec_panic(at, "message");`, or with [report] (a String, see Assertions): `__ec_panic_text(at, report)`,
 * which is only built when the condition is false. [at] is a C expression giving the place to report, usually `"file:line"`.
 * Without [condition] it always fails (`assert false`).
 */
class IRAssert(location: Loc, private val condition: IIR?, private val at: String, private val message: StringLiteral, private val report: IIR? = null) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        if (condition == null) sb.putStatement()
        else {
            sb.putStatement("if (!(")
            condition.toC(sb, c)
            sb.put(")) ")
        }
        if (report == null) sb.put("__ec_panic($at, \"${message.toC()}\")")
        else {
            sb.put("__ec_panic_text($at, ")
            report.toC(sb, c)
            sb.put(")")
        }
        sb.endStatement()
    }

    override fun toString() = "Assert($condition)"
}

/** `_Static_assert(condition, "message");`: a `const assert` that only C can decide, as it needs `sizeof` (see TConstAssert). */
class IRStaticAssert(location: Loc, private val condition: IIR, private val message: StringLiteral) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("_Static_assert(")
        condition.toC(sb, c)
        sb.put(", \"${message.toC()}\")")
        sb.endStatement()
    }

    override fun toString() = "StaticAssert($condition)"
}

/** `__EC_DEBUG`: whether `debug` statements run, 1 or (in a release build) 0. See TDebug and CTypes.useDebugFlag. */
class IRDebugFlag(location: Loc, private val release: Boolean) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.types.useDebugFlag(release)
        sb.put("__EC_DEBUG")
    }

    override fun toString() = "__EC_DEBUG"
}

/**
 * `const char* __ec_at = __ec_take_call_site("f.ec:3 (...)");`: the first statement of a function with `requires`, which
 * reports a failed `requires` where it was called (see Functions.contractCall), or at [fallback] when that isn't known.
 */
class IRTakeCallSite(location: Loc, private val fallback: String) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.types.useCallSite()
        sb.putStatement("const char* __ec_at = __ec_take_call_site(\"${StringLiteral.of(fallback).toC()}\")")
        sb.endStatement()
    }

    override fun toString() = "takeCallSite()"
}

/** `__ec_call_site = __ec_at;`: in the function a direct call of a function with `requires` goes through (see Functions.contractCall). */
class IRSetCallSite(location: Loc) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.types.useCallSite()
        sb.putStatement("__ec_call_site = __ec_at")
        sb.endStatement()
    }

    override fun toString() = "setCallSite()"
}

/** What member of the union [type] the value [expr] holds, as a CString: "a String" (see CTypes.heldType). */
class IRHeldType(location: Loc, private val expr: IIR, private val type: github.mrh0.ect2.Union) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(sb.types.heldType(type, sb.render { expr.toC(it, c) }))
    }

    override fun toString() = "heldType($expr)"
}
