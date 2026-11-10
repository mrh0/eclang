package github.mrh0.eclang.ast.token.data.record

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.data.TInteger
import github.mrh0.eclang.ast.token.data.TString
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.util.StringLiteral
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `here`: a `Location` (core:errors.ec) of where it is written, the file, line, column and function. A parameter with the
 * default `caller` (`at: Location = caller`) is given the `here` of each call instead (see `Parameter.isCaller`).
 */
class THere(location: Loc) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.Nothing

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = value(cd, location, record(cd, location, "`here`"))

    override fun toString(): String {
        return "THere"
    }

    companion object {
        private const val USE = "Add `use \"core:errors.ec\"`, which defines it."

        /** The top-level record `Location` of core:errors.ec. [what] names what needs it. */
        fun record(cd: CompileData, location: Loc, what: String): Record =
            cd.types.lookupIn("", "Location") as? Record ?: throw EcError(location, "$what is a Location, but no Location is defined. $USE")

        /** Whether a parameter of type [t] can be filled with the place of the call: a `Location`, a String ("file:line"), or an Int (the line). */
        fun takes(cd: CompileData, t: Type): Boolean = t === Types.String || t === Types.Int || t === cd.types.lookupIn("", "Location")

        /** [location] as a value of type [type] (see [takes]): a `Location` record, "file:line", or the line. */
        fun value(cd: CompileData, location: Loc, type: Type): Pair<Type, IIR> {
            val file = location.getFilename()
            val line = location.getLine().toLong()
            val values: List<ITok> = when (type) {
                Types.String -> return TString(location, StringLiteral.of("$file:$line")).process(cd, type)
                Types.Int -> return TInteger(location, line).process(cd, type)
                else -> listOf(TString(location, StringLiteral.of(file)), TInteger(location, line), TInteger(location, location.getCharPos() + 1L),
                    TString(location, StringLiteral.of(cd.function?.name ?: "")))
            }
            val record = type as Record
            return record to createRecord(cd, location, record, values, listOf("file", "line", "column", "function")).second
        }
    }
}
