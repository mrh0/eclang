package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.atom.Atoms
import github.mrh0.eclang.context.function.Overload
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * The whole C file. The code is rendered first, so every type and helper it uses is known; then the file is
 * assembled: includes, runtime helpers, types, atoms, conversion helpers, prototypes, globals, functions,
 * and finally the C `main` that calls the program's `main`.
 */
class IRProgram(
    location: Loc, private val functions: List<IIR>, private val globals: List<IIR>, private val uses: List<IIR>,
    private val main: Overload, private val mainType: Type, private val atoms: Atoms,
) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        val code = sb.render { out ->
            out.commentLine("Declarations")
            functions.forEach { if (it is IRFunctionOverride) it.toCDeclaration(out, c) }
            out.putLine()
            out.commentLine("Globals")
            globals.forEach { it.toC(out, c) }
            out.putLine()
            out.commentLine("Code")
            functions.forEach { it.toC(out, c); out.putLine() }
            out.commentLine("Main Entry")
            out.put(mainEntry(out, out.render { init -> globals.forEach { (it as? IRGlobalDefine)?.toCInit(init, c) } }))
        }
        val helpers = sb.types.helperFunctions()
        sb.types.helperAtoms.forEach { atoms.get(it) }
        val types = sb.types.definitions()

        val standard = listOf("stdio.h", "stdlib.h", "string.h", "math.h", "stdbool.h", "stddef.h", "stdalign.h", "apr_general.h", "apr_pools.h")
        standard.forEach { sb.putLine("#include <$it>") }
        sb.putLine()
        sb.commentLine("Uses")
        uses.filter { it !is IRInclude || it.path !in standard }.forEach { it.toC(sb, c) }
        sb.putLine()
        sb.commentLine("Runtime")
        sb.putLine(RUNTIME)
        sb.commentLine("Types")
        sb.put(types)
        sb.putLine()
        sb.commentLine("Atoms")
        atoms.getAll().forEach { sb.putLine("static char* const ${it.getId()} = \"${it.label}\";") }
        sb.putLine()
        sb.commentLine("Helpers")
        sb.put(helpers)
        sb.putLine()
        sb.put(code)
    }

    /**
     * The C `main`: sets up APR and the program's root pool (the current pool until a `pool do` block), runs
     * [globalInits] (globals whose value isn't a C constant), then calls the program's `main`.
     */
    private fun mainEntry(sb: CSourceBuilder, globalInits: String): String {
        val call = "${main.cName}()"
        val body = when {
            main.throws != null -> {
                val value = if (main.returns === Types.None) "0" else "__ec_result.value"
                "${sb.types.cName(mainType)} __ec_result = $call;\n" +
                    "\tif (__ec_result.code != 0) { ${uncaught(main.throws)} __ec_ret = 1; }\n" +
                    "\telse __ec_ret = $value;"
            }
            main.returns === Types.None -> "$call;\n\t__ec_ret = 0;"
            else -> "__ec_ret = $call;"
        }
        return "int main(void) {\n\tapr_initialize();\n\t__ec_pool = __ec_pool_create(NULL);\n$globalInits\tint __ec_ret;\n\t$body\n" +
            "\tapr_pool_destroy(__ec_pool);\n\tapr_terminate();\n\treturn __ec_ret;\n}\n"
    }

    /** The C statement that reports an uncaught [error] from `main`: its `message` field, or the error itself if it is text. */
    private fun uncaught(error: Type): String {
        val (text, type) = (error as? Record)?.field("message")?.let { "__ec_result.error.message" to it } ?: ("__ec_result.error" to error)
        return when (type) {
            Types.CString, Types.Atom -> "fprintf(stderr, \"error: main failed: %s\\n\", $text);"
            Types.String -> "fprintf(stderr, \"error: main failed: %.*s\\n\", (int)$text.len, $text.data);"
            else -> "fprintf(stderr, \"error: main failed with an uncaught $error\\n\");"
        }
    }

    override fun toString(): String {
        return "IRProgram($functions)"
    }

    companion object {
        private val RUNTIME = """
            typedef struct { size_t len; char* data; } __ec_string_t;
            typedef struct { int line; int position; char* path; } __ec_location_t;
            _Noreturn static inline void __ec_panic(const char* at, const char* message) { fprintf(stderr, "%s: %s\n", at, message); abort(); }
            static inline void* __ec_nn(void* p, const char* at) { if (p == NULL) __ec_panic(at, "null value used where a value was required"); return p; }
            static inline bool __ec_str_eq(__ec_string_t a, __ec_string_t b) { return a.len == b.len && memcmp(a.data, b.data, a.len) == 0; }
            static _Thread_local apr_pool_t* __ec_pool; /* the current pool (`pool`): the root pool, or the innermost `pool do` block's */
            static int __ec_out_of_memory(int status) { (void)status; __ec_panic("eclang", "out of memory"); }
            static inline apr_pool_t* __ec_pool_create(apr_pool_t* parent) { apr_pool_t* p = NULL; if (apr_pool_create_ex(&p, parent, __ec_out_of_memory, NULL) != APR_SUCCESS) __ec_panic("eclang", "can't create a pool"); return p; }
            static inline void* __ec_palloc(apr_pool_t* pool, size_t size) { if (pool == NULL) __ec_panic("eclang", "this thread has no current pool: start its code with a `pool do` block"); return apr_palloc(pool, size); }
            static inline __ec_string_t __ec_str_concat(apr_pool_t* pool, __ec_string_t a, __ec_string_t b) { char* s = __ec_palloc(pool, a.len + b.len + 1); if (a.len) memcpy(s, a.data, a.len); if (b.len) memcpy(s + a.len, b.data, b.len); s[a.len + b.len] = 0; return (__ec_string_t){a.len + b.len, s}; }
            _Noreturn static inline void __ec_panic_index(const char* at, size_t i, size_t len) { fprintf(stderr, "%s: index %lld is out of bounds for length %zu\n", at, (long long)i, len); abort(); }
            _Noreturn static inline void __ec_panic_slice(const char* at, size_t from, size_t to, size_t len) { fprintf(stderr, "%s: slice %lld..<%lld is out of bounds for length %zu\n", at, (long long)from, (long long)to, len); abort(); }
        """.trimIndent()
    }
}
