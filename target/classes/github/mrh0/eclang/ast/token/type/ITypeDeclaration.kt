package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.context.CompileData

/**
 * A top-level type declaration (record, struct, interface). Declarations are handled in two steps, so types can
 * refer to each other in any order, including themselves (`next: @Node?`): first every name is declared, then
 * every type gets its fields.
 */
interface ITypeDeclaration {
    /** The type this one extends, which must be declared first, or null. */
    val parentName: String?
    fun declareType(cd: CompileData)
    fun defineType(cd: CompileData)
}
