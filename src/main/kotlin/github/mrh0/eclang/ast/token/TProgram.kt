package github.mrh0.eclang.ast.token

import github.mrh0.eclang.Compiler
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.function.TFunc
import github.mrh0.eclang.ast.token.global.TGlobalTypeDefine
import github.mrh0.eclang.ast.token.global.TGlobalEnumDeclare
import github.mrh0.eclang.ast.token.global.TGlobalUse
import github.mrh0.eclang.ast.token.type.ITypeDeclaration
import github.mrh0.eclang.ast.token.type.TTypeRecord
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Module
import github.mrh0.eclang.ast.token.global.TGlobalModule
import github.mrh0.eclang.ast.token.global.TGlobalConstant
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRProgram
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * A whole program: this file and every `.ec` file it `use`s (recursively, each once). Compiled in phases so the
 * order of declarations doesn't matter: type names, type aliases, type fields, function signatures, constants,
 * other globals, and finally function bodies. Each declaration is compiled in the module of the file it is in.
 */
class TProgram(location: Loc, private val functionsIn: List<TFunc>, private val usesIn: List<TGlobalUse>, private val globalsIn: List<ITok>) : Tok(location) {
    /** A top-level declaration and the module of its file. */
    private class Item<T : ITok>(val module: Module, val tok: T)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val functions = mutableListOf<Item<TFunc>>()
        val uses = mutableListOf<TGlobalUse>()
        val globals = mutableListOf<Item<ITok>>()
        location.file?.let { cd.loadedFiles.add(it.canonicalPath) }
        val entryModule = collect(cd, this, functions, uses, globals)

        val usesIRs = uses.mapNotNull { cd.recover { it.process(cd, Types.None).second } }

        // Within a phase every declaration is checked, so all of its errors are reported; a phase with errors
        // stops the compilation (cd.checkpoint), because the later phases depend on it.
        val declarations = globals.filter { it.tok is ITypeDeclaration }
        declareTypes(cd, declarations)
        cd.checkpoint()
        globals.filter { it.tok is TGlobalTypeDefine || it.tok is TGlobalEnumDeclare }.forEach { each(cd, it) { tok -> tok.process(cd, Types.None) } }
        cd.checkpoint()
        declarations.forEach { each(cd, it) { tok -> (tok as ITypeDeclaration).defineType(cd) } }
        cd.checkpoint()
        declarations.forEach { each(cd, it) { tok -> (tok as? TTypeRecord)?.checkFinite(cd) } }
        // Signatures before globals, so a global that calls a function gets a clear error rather than "not defined".
        functions.forEach { each(cd, it) { tok -> cd.functions.declare(tok) } }
        cd.checkpoint()
        // Constants first: other globals' initial values may use them.
        val globalIRs = globals.filter { it.tok !is ITypeDeclaration && it.tok !is TGlobalTypeDefine && it.tok !is TGlobalEnumDeclare }.sortedBy { it.tok !is TGlobalConstant }
            .mapNotNull { each(cd, it) { tok -> tok.process(cd, Types.None).second } }
        cd.checkpoint()

        val functionIRs = cd.functions.buildAll()
        cd.checkpoint()

        val main = cd.functions.main(entryModule) ?: throw EcError(location, "The program has no `main` function. Add one, for example:\n\nfn main(): Int do\n    ret 0")
        val mainType = main.throws?.let { cd.types.result(main.returns, it) } ?: main.returns
        if (mainType !== Types.Int && mainType !== Types.None && main.throws == null)
            throw EcError(main.location, "`main` must return Int or nothing, not ${main.returns}.")
        return Types.None to IRProgram(location, cd.functions.reachable(main, functionIRs), globalIRs, usesIRs, main, mainType, cd.atoms)
    }

    /** Compiles [item] in its module; an error is reported and the next declaration is compiled. */
    private fun <T : ITok, R> each(cd: CompileData, item: Item<T>, compile: (T) -> R): R? =
        cd.recover { cd.inModule(item.module) { compile(item.tok) } }

    /**
     * Adds [program]'s declarations after those of the files it uses, so dependencies come first. Returns the
     * program's module.
     */
    private fun collect(cd: CompileData, program: TProgram, functions: MutableList<Item<TFunc>>, uses: MutableList<TGlobalUse>, globals: MutableList<Item<ITok>>): Module {
        val module = program.module(cd)
        program.usesIn.forEach {
            val path = if (it.path.endsWith(".ec")) Util.resolveUse(it.location, it.path) else return@forEach
            if (cd.loadedFiles.add(path)) collect(cd, Compiler.tokenizeStream(Util.getStream(it.location, path), path), functions, uses, globals)
        }
        program.functionsIn.forEach { functions += Item(module, it) }
        program.usesIn.forEach { u -> if (uses.none { it.path == u.path }) uses += u } // each #include once
        program.globalsIn.forEach { globals += Item(module, it) }
        return module
    }

    /** The module this file declares with `module "name"`, or the top level. */
    private fun module(cd: CompileData): Module {
        val declared = globalsIn.filterIsInstance<TGlobalModule>()
        if (declared.size > 1) throw EcError(declared[1].location, "A file can only belong to one module, but it already declares `module \"${declared[0].namespace}\"`.")
        val decl = declared.firstOrNull() ?: return cd.topLevel
        Module.problem(decl.namespace)?.let { throw EcError(decl.location, "`module \"${decl.namespace}\"`: $it.") }
        return cd.moduleNamed(decl.namespace)
    }

    /** Declares every type name; a record is declared after the record it extends. */
    private fun declareTypes(cd: CompileData, declarations: List<Item<ITok>>) {
        val waiting = declarations.toMutableList()
        while (waiting.isNotEmpty()) {
            val ready = waiting.filter { item ->
                val parent = (item.tok as ITypeDeclaration).parentName
                parent == null || cd.inModule(item.module) { cd.types.lookup(item.module.name, parent) } != null
            }
            if (ready.isEmpty()) {
                cd.checkpoint() // a record whose parent failed to declare
                val stuck = waiting.first().tok
                throw EcError((stuck as Tok).location, "Unknown type '${(stuck as ITypeDeclaration).parentName}' to extend (or the records extend each other in a circle).")
            }
            ready.forEach { each(cd, it) { tok -> (tok as ITypeDeclaration).declareType(cd) } }
            waiting.removeAll(ready)
        }
    }

    override fun toString(): String {
        return "TProgram($functionsIn)"
    }
}
