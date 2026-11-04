package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementDefineVar
import github.mrh0.eclang.ir.data.IRNull
import github.mrh0.eclang.ir.data.IRZero
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.*

/**
 * `var x: T` without a value: x starts out empty, i.e. zero, false, an empty String or array, null for `T?`, and a
 * record with every field empty. Useful for out-parameters (`stat(path, addrof(st))`). Types that have no empty
 * value (pointers, CStrings, atoms, unions) need a value or a `?`.
 */
class TStatementDeclareVar(location: Loc, private val varName: String, private val typeTok: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fixedName = testIdentifier(location, varName)
        val type = cd.storable(location, typeTok.process(cd, Types.None).first, "variable '$varName'")
        emptyValueProblem(type)?.let {
            throw EcError(location, "'$varName' needs a starting value: $it. Give it one (`var $varName: $type = ...`), or make it nullable (`var $varName: $type?`) so it starts as null.")
        }
        val value = if (type is Union && type.isNullable) cd.expect(location, Types.Null to IRNull(location), type, "variable '$varName'")
            else IRZero(location, type)
        val ivar = cd.ctx().define(location, Variable(fixedName, type, cd.localCName(fixedName)))
        return Types.None to IRStatementDefineVar(location, ivar, value)
    }

    override fun toString() = "TStatementDeclareVar($varName: $typeTok)"
}

/** Why a [t] has no empty value (for `var x: T` and `new T`), or null when it has one. */
internal fun emptyValueProblem(t: Type): String? = when {
    t is Union && t.isNullable -> null
    t is Prim && (t.isNumber || t === Types.Bool || t === Types.Char || t === Types.String) -> null
    t is ArrayType && !t.isC -> null
    t is Tuple -> t.elements.firstNotNullOfOrNull { emptyValueProblem(it) }
    // A C struct's fields are C's business: an out-parameter is filled in by C anyway.
    t is Record && t.isExternal -> null
    t is Record -> t.fields.entries.firstNotNullOfOrNull { (name, field) -> emptyValueProblem(field)?.let { "its field `$name` has no empty value ($it)" } }
    else -> "a $t can't be null and has no empty value"
}
