package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.type.checkNewTypeName
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * A C enum seen from eclang (`declare enum`). In eclang its type is the union of its atoms; only calls of
 * `declare fn` functions convert between the atoms and the C constants (see [github.mrh0.eclang.ir.IREnumConvert]).
 * [members]: atom label to C constant, in declaration order.
 */
class CEnum(val name: String, val cName: String, val members: List<Pair<String, String>>)

/** `declare enum Name as` followed by lines `:atom = C_CONSTANT`. */
class TGlobalEnumDeclare(location: Loc, val name: String, val atoms: List<String>, val constants: List<String>) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        checkNewTypeName(cd, location, name)
        val labels = atoms.map { it.drop(1).lowercase() }
        labels.groupBy { it }.filter { it.value.size > 1 }.keys.firstOrNull()?.let {
            throw EcError(location, "The enum $name lists :$it twice. Give each C constant its own atom.")
        }
        val type = cd.types.union(labels.map { cd.types.atom(it) })
        cd.enums[type]?.let {
            throw EcError(location, "The enum $name has the same atoms as the enum ${it.name}, so a value can't tell which C constants it means. " +
                "Use the same enum for both, or give one of them different atoms.")
        }
        cd.enums[type] = CEnum(name, cd.module.cName(name), labels.zip(constants))
        cd.types.define(cd.module.name, name, type)
        return Types.None to IRPass(location, null)
    }

    override fun toString() = "TGlobalEnumDeclare($name)"
}
