package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Declared
import github.mrh0.ect2.Record
import github.mrh0.ect2.Types

/** Gives [type] its fields, checking names are unique (also against inherited fields) and every type can be stored. */
internal fun defineFields(cd: CompileData, location: Loc, type: Declared, names: List<String>, types: List<ITok>) {
    val inherited = (type as? Record)?.parent?.fields ?: mapOf()
    val fields = LinkedHashMap<String, github.mrh0.ect2.Type>()
    names.forEachIndexed { i, raw ->
        val name = testIdentifier(location, raw)
        if (name in fields || name in inherited) throw EcError(location, "${type.name} has two fields called '$raw'.")
        fields[name] = cd.storable(location, types[i].process(cd, Types.None).first, "field '$raw' of ${type.name}")
    }
    type.define(fields)
}

internal fun declared(cd: CompileData, location: Loc, name: String): Declared =
    cd.lookupType(location, name) as? Declared ?: throw EcError(location, "BUG: $name was not declared")

/** Checks the current module doesn't define a type called [name] yet. Call before declaring it. */
internal fun checkNewTypeName(cd: CompileData, location: Loc, name: String) {
    if (cd.types.lookupIn(cd.module.name, name) != null) throw EcError(location, "A type called '$name' is already defined in ${cd.module}.")
}
