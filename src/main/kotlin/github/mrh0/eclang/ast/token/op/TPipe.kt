package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.data.TBoolean
import github.mrh0.eclang.ast.token.data.TChar
import github.mrh0.eclang.ast.token.data.TFloat
import github.mrh0.eclang.ast.token.data.TInteger
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRRaw
import github.mrh0.eclang.ir.IRTempDefine
import github.mrh0.eclang.ir.function.call.IRCallValue
import github.mrh0.ect2.FunctionType
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `value |> stage`: [stage] is compiled with `it` standing for value, which it uses [itUses] times (the visitor
 * rewrites `x |> f(2)` to `f(it, 2)`). A chain `a |> f |> g` is `(a |> f) |> g`. The value is evaluated once: when
 * the stage uses `it` more than once, it goes into a temporary first (unless it is a plain variable or literal).
 */
class TPipe(location: Loc, private val value: ITok, private val stage: ITok, private val itUses: Int) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        var v = cd.usable(location, value.process(cd, Types.None), "value before `|>`")
        val simple = value.unnested().let { it is TNamed || it is TInteger || it is TFloat || it is TBoolean || it is TChar }
        if (itUses > 1 && !simple) {
            if (cd.hoistProblem() != null) throw EcError(location, "`it` is used $itUses times after this `|>`, so the value before it must be stored first, " +
                "which isn't possible here (this code may run more than once, or not at all). Put the value in a variable before this statement.")
            val temp = cd.temp()
            val type = cd.storable(location, v.first, "value before `|>`")
            cd.hoist(location, IRTempDefine(location, temp, type, cd.expect(location, v, type, "value before `|>`")))
            v = type to IRRaw(location, temp)
        }
        cd.pipeValues += v
        try {
            return stage.process(cd, hint)
        } finally {
            cd.pipeValues.removeAt(cd.pipeValues.size - 1)
        }
    }

    override fun toString() = "TPipe($value, $stage)"
}

/** `it`: the value before `|>` in the stage being compiled. */
class TIt(location: Loc) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> =
        cd.pipeValues.lastOrNull() ?: throw EcError(location, "`it` can only be used in a `|>` stage, where it is the value before the `|>`, e.g. `x |> it * 2`.")

    override fun toString() = "TIt"
}

/** `callee(args)` where callee is any expression of function type, e.g. a lambda as a `|>` stage. */
class TCallValue(location: Loc, private val callee: ITok, private val args: List<ITok>) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val values = args.mapIndexed { i, a -> cd.usable(location, a.process(cd, Types.None), "argument ${i + 1}") }
        // The arguments' types tell a lambda the types of parameters written without one.
        val (type, ir) = callee.process(cd, cd.types.function(values.map { cd.types.concrete(it.first) }, Types.None, null))
        if (type !is FunctionType) throw EcError(location, "Can't call this: it is a $type, not a function.")
        if (values.size != type.params.size) throw EcError(location, "This function takes ${type.params.size} arguments ($type), but ${values.size} were given.")
        val irs = values.mapIndexed { i, v -> cd.expect(location, v, type.params[i], "argument ${i + 1}") }
        val result = type.throws?.let { cd.types.result(type.returns, it) } ?: type.returns
        return result to IRCallValue(location, ir, irs)
    }

    override fun toString() = "TCallValue($callee, $args)"
}
