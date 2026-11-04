package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.context.InterfaceMethod
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Interface
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `inter Name as` + fields: any record with these fields (same names and types) fits. Used as a parameter type,
 * each record passed in gets its own copy of the function.
 */
class TInterface(location: Loc, val name: String, val names: List<String>, val types: List<ITok>, private val methods: List<MethodDecl> = listOf()) : Tok(location), ITypeDeclaration {
    /** `fn name(params): returns` in the interface. */
    class MethodDecl(val location: Loc, val name: String, val params: List<ITok>, val returns: ITok?)

    override val parentName: String? get() = null

    override fun declareType(cd: CompileData) {
        checkNewTypeName(cd, location, name)
        cd.types.interfaceType(cd.module.name, name)
    }

    override fun defineType(cd: CompileData) {
        val iface = declared(cd, location, name) as Interface
        defineFields(cd, location, iface, names, types)
        if (methods.isEmpty()) return
        methods.groupBy { it.name }.values.firstOrNull { it.size > 1 }?.let { throw EcError(it[1].location, "Interface $name asks for '${it[0].name}' twice.") }
        cd.interfaceMethods[iface] = methods.map { m ->
            InterfaceMethod(testIdentifier(m.location, m.name), m.params.map { cd.storable(m.location, it.process(cd, Types.None).first, "a parameter of '${m.name}'") },
                m.returns?.process(cd, Types.None)?.first ?: Types.None)
        }
    }

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        declareType(cd)
        defineType(cd)
        return Types.None to IRPass(location, null)
    }

    override fun toString(): String {
        return "TInterface($name)"
    }
}
