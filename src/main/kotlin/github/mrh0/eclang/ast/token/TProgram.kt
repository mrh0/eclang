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
import github.mrh0.eclang.doc.FileDocs
import github.mrh0.eclang.ast.token.global.TGlobalModule
import github.mrh0.eclang.ast.token.global.TGlobalConstant
import github.mrh0.eclang.error.EcAbort
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUnknownTypeError
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
class TProgram(location: Loc, private val functionsIn: List<TFunc>, private val usesIn: List<TGlobalUse>, private val globalsIn: List<ITok>, private val privates: List<Private> = listOf()) : Tok(location) {
    /** A declaration marked `priv`: a `fn`, a `value` (global) or a `type`, called [name]. */
    class Private(val tok: ITok, val kind: String, val name: String)

    /** This file's declarations with their ecdocs (see `doc/DocExtractor`). */
    var docs: FileDocs? = null

    /** A top-level declaration and the module of its file. */
    private class Item<T : ITok>(val module: Module, val tok: T)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val functions = mutableListOf<Item<TFunc>>()
        val uses = mutableListOf<TGlobalUse>()
        val globals = mutableListOf<Item<ITok>>()
        location.file?.let { cd.loadedFiles.add(Util.sourceName(it)) }
        val entryModule = collect(cd, this, functions, uses, globals)

        val usesIRs = uses.mapNotNull { cd.recover { it.process(cd, Types.None).second } }
        val constants = earlyConstants(cd, globals)

        // Within a phase every declaration is checked, so all of its errors are reported; a phase with errors
        // stops the compilation (cd.checkpoint), because the later phases depend on it.
        val declarations = globals.filter { it.tok is ITypeDeclaration }
        declareTypes(cd, declarations)
        cd.checkpoint()
        aliases(cd, globals.filter { it.tok is TGlobalTypeDefine || it.tok is TGlobalEnumDeclare })
        cd.checkpoint()
        declarations.forEach { each(cd, it) { tok -> (tok as ITypeDeclaration).defineType(cd) } }
        cd.checkpoint()
        declarations.forEach { each(cd, it) { tok -> (tok as? TTypeRecord)?.checkFinite(cd) } }
        // Signatures before globals, so a global that calls a function gets a clear error rather than "not defined".
        functions.forEach { each(cd, it) { tok -> cd.functions.declare(tok) } }
        cd.checkpoint()
        // Constants first: other globals' initial values may use them.
        val globalIRs = constants.values.toList() + globals.filter { it.tok !is ITypeDeclaration && it.tok !is TGlobalTypeDefine && it.tok !is TGlobalEnumDeclare && it !in constants }.sortedBy { it.tok !is TGlobalConstant }
            .mapNotNull { each(cd, it) { tok -> tok.process(cd, Types.None).second } }
        cd.checkpoint()

        val functionIRs = cd.functions.buildAll()
        cd.checkpoint()

        cd.library?.let { library ->
            if (cd.functions.exported.isEmpty()) throw EcError(location, "A library needs at least one `export fn`: C code can't call anything else in it.")
            cd.claimCName(location, "${library}_clear", "the function a library is given to free its memory", renamable = false)
            return Types.None to IRProgram(location, cd.functions.reachable(null, functionIRs), globalIRs, usesIRs, null, Types.None, cd.atoms, library, cd.functions.exported)
        }
        if (cd.checkOnly && cd.functions.main(entryModule) == null) throw EcAbort()
        val main = cd.functions.main(entryModule) ?: throw EcError(location, "The program has no `main` function. Add one, for example:\n\nfn main(): Int do\n    ret 0\n\nA library has none: mark what C can call with `export fn`, and build it with `eclang lib`.")
        val mainType = main.throws?.let { cd.types.result(main.returns, it) } ?: main.returns
        if (mainType !== Types.Int && mainType !== Types.None && main.throws == null)
            throw EcError(main.location, "`main` must return Int or nothing, not ${main.returns}.")
        return Types.None to IRProgram(location, cd.functions.reachable(main, functionIRs), globalIRs, usesIRs, main, mainType, cd.atoms)
    }

    /**
     * Compiles the constants that can be compiled before any type is known, so types can use them (`Char[LENGTH]`, `align(N)`).
     * One that uses another comes after it; one that needs a type declared in the program waits for the phase of the globals.
     */
    private fun earlyConstants(cd: CompileData, globals: List<Item<ITok>>): Map<Item<ITok>, IIR> {
        val done = LinkedHashMap<Item<ITok>, IIR>()
        var waiting = globals.filter { it.tok is TGlobalConstant }
        while (true) {
            val blocked = waiting.filter { item ->
                try {
                    done[item] = cd.inModule(item.module) { item.tok.process(cd, Types.None).second }
                    false
                } catch (_: EcError) {
                    true
                }
            }
            if (blocked.size == waiting.size) return done
            waiting = blocked
        }
    }

    /** Defines the type names in [items], in any order: one that uses a name defined later waits for it. */
    private fun aliases(cd: CompileData, items: List<Item<ITok>>) {
        var waiting = items
        while (waiting.isNotEmpty()) {
            val blocked = waiting.filter { item ->
                cd.recover {
                    try {
                        cd.inModule(item.module) { item.tok.process(cd, Types.None) }
                        false
                    } catch (e: EcUnknownTypeError) {
                        true
                    }
                } == true
            }
            // Nothing new was defined: the names really don't exist (or the types are defined by each other).
            if (blocked.size == waiting.size) {
                blocked.forEach { item -> cd.recover { cd.inModule(item.module) { item.tok.process(cd, Types.None) } } }
                return
            }
            waiting = blocked
        }
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
        program.docs?.problems?.forEach { cd.warn(it.location, it.message) }
        program.usesIn.forEach {
            val path = if (it.path.endsWith(".ec")) Util.resolveUse(it.location, it.path) else return@forEach
            if (cd.loadedFiles.add(path)) collect(cd, Compiler.tokenizeStream(cd.unsaved(path) ?: Util.getStream(it.location, path), path), functions, uses, globals)
        }
        program.privates.forEach { p ->
            val name = Util.testIdentifier((p.tok as Tok).location, p.name)
            when (p.kind) {
                "fn" -> module.privateFunctions += name
                "value" -> module.privateGlobals += name
                else -> module.privateTypes += p.name
            }
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
