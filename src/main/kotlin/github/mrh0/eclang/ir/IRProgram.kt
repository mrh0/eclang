package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.atom.Atoms
import github.mrh0.eclang.context.function.Overload
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.output.c.CTypes
import github.mrh0.ect2.Record
import github.mrh0.ect2.Type
import github.mrh0.ect2.TypeContext
import github.mrh0.ect2.Types

/**
 * The whole C file. The code is rendered first, so every type and helper it uses is known; then the file is
 * assembled: includes, runtime helpers, types, atoms, conversion helpers, prototypes, globals, functions,
 * and finally the C `main` that calls the program's `main`. A [library] (its name, with [exports] for C to call) has no
 * `main`: what `main` does on start and on exit happens when the library is loaded and unloaded.
 */
class IRProgram(
    location: Loc, private val functions: List<IIR>, private val globals: List<IIR>, private val initOrder: List<IRGlobalDefine>, private val uses: List<IIR>,
    private val main: Overload?, private val mainType: Type, private val atoms: Atoms,
    private val library: String? = null, private val exports: List<Overload> = listOf(),
) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        val code = sb.render { out ->
            val body = out.render { b ->
                b.commentLine("Code")
                functions.forEach { it.toC(b, c); b.putLine() }
                val inits = b.render { init -> initOrder.forEach { it.toCInit(init, c) } }
                if (main != null) {
                    b.commentLine("Main Entry")
                    b.put(mainEntry(main, b, inits))
                } else {
                    b.commentLine("Library Entry")
                    b.put(libraryEntry(library!!, inits))
                }
            }
            out.commentLine("Declarations")
            functions.forEach { if (it is IRFunctionOverride) it.toCDeclaration(out, c) }
            out.putLine()
            out.commentLine("Globals")
            out.put(usedGlobals(out, c, body))
            out.putLine()
            out.put(body)
        }
        val helpers = sb.types.helperFunctions()
        sb.types.helperAtoms.forEach { atoms.get(it) }
        val types = sb.types.definitions()

        val standard = listOf("stdio.h", "stdlib.h", "string.h", "math.h", "stdbool.h", "stddef.h", "stdint.h", "limits.h", "stdalign.h", "signal.h", "sys/resource.h", "apr_general.h", "apr_pools.h")
        standard.forEach { sb.putLine("#include <$it>") }
        sb.putLine()
        sb.commentLine("Uses")
        uses.filter { it !is IRInclude || it.path !in standard }.forEach { it.toC(sb, c) }
        sb.putLine()
        sb.commentLine("Runtime")
        sb.putLine(RUNTIME)
        sb.putLine(if (library != null) LIBRARY_RUNTIME else PROGRAM_RUNTIME)
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
     * The C definitions of the globals, without those nothing in [body] (or another global kept) refers to and whose value
     * is a constant: a library's globals that only functions this program doesn't use need, which C would warn about.
     */
    private fun usedGlobals(sb: CSourceBuilder, c: Context, body: String): String {
        val rendered = globals.map { g -> g to sb.render { g.toC(it, c) } }
        val kept = rendered.map { (g, _) -> g !is IRGlobalDefine || !g.removable }.toMutableList()
        var changed = true
        while (changed) {
            changed = false
            val text = body + rendered.filterIndexed { i, _ -> kept[i] }.joinToString("") { it.second }
            rendered.forEachIndexed { i, (g, _) ->
                if (!kept[i] && Regex("\\b${Regex.escape((g as IRGlobalDefine).cName)}\\b").containsMatchIn(text)) { kept[i] = true; changed = true }
            }
        }
        return rendered.filterIndexed { i, _ -> kept[i] }.joinToString("") { it.second }
    }

    /**
     * The C `main`: sets up APR and the program's root pool (the current pool until a `pool do` block), runs
     * [globalInits] (globals whose value isn't a C constant), then calls the program's `main`.
     */
    private fun mainEntry(main: Overload, sb: CSourceBuilder, globalInits: String): String {
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
        return "int main(int argc, char** argv) {\n\t__ec_watch_stack();\n\t__ec_argc = argc;\n\t__ec_argv = argv;\n\tapr_initialize();\n\t__ec_pool = __ec_pool_create(NULL);\n$globalInits\tint __ec_ret;\n\t$body\n" +
            // No apr_terminate: a detached thread may still be allocating, and it would free the allocator under it.
            "\tapr_pool_destroy(__ec_pool);\n\treturn __ec_ret;\n}\n"
    }

    /**
     * What loading and unloading the library does, and `name_clear`. The globals live in a pool of their own. Each thread that
     * calls in gets a pool (see `__ec_enter`), where what exported functions return stays until that thread calls `name_clear`.
     */
    private fun libraryEntry(name: String, globalInits: String) =
        "__attribute__((constructor)) static void __ec_library_load(void) {\n\t__ec_argc = 0;\n\t__ec_argv = NULL;\n\tapr_initialize();\n\t__ec_globals = __ec_pool_create(NULL);\n" +
            "\t__ec_pool = __ec_globals;\n$globalInits\t__ec_pool = NULL;\n}\n\n" +
            "__attribute__((destructor)) static void __ec_library_unload(void) {\n\tapr_pool_destroy(__ec_globals);\n\tapr_terminate();\n}\n\n" +
            "void ${name}_clear(void) {\n\tif (__ec_pool != NULL) apr_pool_clear(__ec_pool);\n}\n"

    /** The C header of a library: the exported functions and the types they use, or null for a program. */
    fun header(types: TypeContext): String? {
        val name = library ?: return null
        val c = CTypes(types)
        val prototypes = exports.map { c.prototype(it) } + "/* Frees what the exported functions gave this thread so far. */\nvoid ${name}_clear(void);"
        val definitions = c.definitions().trim()
        // Which header declares a C type isn't known, so a signature with one takes them all; most libraries need none.
        val includes = listOf("stdbool.h", "stddef.h", "stdint.h") + if (c.usesCTypes) listOf("apr_pools.h") + uses.filterIsInstance<IRInclude>().map { it.path } else listOf()
        val guard = "${name.uppercase()}_H"
        return "#ifndef $guard\n#define $guard\n\n${includes.distinct().joinToString("") { "#include <$it>\n" }}\n#ifdef __cplusplus\nextern \"C\" {\n#endif\n\n" +
            "typedef struct { size_t len; char* data; } __ec_string_t;\n$definitions\n\n${prototypes.joinToString("\n")}\n\n#ifdef __cplusplus\n}\n#endif\n\n#endif\n"
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
            _Noreturn static inline void __ec_panic(const char* at, const char* message) { fflush(stdout); fprintf(stderr, "%s: %s\n", at, message); abort(); }
            static inline void* __ec_nn(void* p, const char* at) { if (p == NULL) __ec_panic(at, "null value used where a value was required"); return p; }
            static inline void* __ec_c_nn(void* p, const char* at, const char* fn) { if (p == NULL) { fflush(stdout); fprintf(stderr, "%s: the C function %s returned NULL, but its `declare fn` says it never does. If it can, declare its result nullable (`: T?`) and check it.\n", at, fn); abort(); } return p; }
            static inline bool __ec_str_eq(__ec_string_t a, __ec_string_t b) { return a.len == b.len && memcmp(a.data, b.data, a.len) == 0; }
            static int __ec_argc; static char** __ec_argv; /* the command line, for io.args in core:io.ec */
            extern char** __ec_environ __asm__("environ"); /* the environment, for os.environment in core:os.ec (C headers only declare `environ` with _GNU_SOURCE) */
            static _Thread_local apr_pool_t* __ec_pool; /* the current pool (`pool`): the root pool, or the innermost `pool do` block's */
            static int __ec_out_of_memory(int status) { (void)status; __ec_panic("eclang", "out of memory"); }
            static inline apr_pool_t* __ec_pool_create(apr_pool_t* parent) { apr_pool_t* p = NULL; if (apr_pool_create_ex(&p, parent, __ec_out_of_memory, NULL) != APR_SUCCESS) __ec_panic("eclang", "can't create a pool"); return p; }
            static inline void* __ec_palloc(apr_pool_t* pool, size_t size) { if (pool == NULL) __ec_panic("eclang", "this thread has no current pool: start its code with a `pool do` block"); return apr_palloc(pool, size); }
            static inline char* __ec_str_cstr(apr_pool_t* pool, __ec_string_t s) { char* c = __ec_palloc(pool, s.len + 1); if (s.len) memcpy(c, s.data, s.len); c[s.len] = 0; return c; }
            static inline __ec_string_t __ec_str_concat(apr_pool_t* pool, __ec_string_t a, __ec_string_t b) { char* s = __ec_palloc(pool, a.len + b.len + 1); if (a.len) memcpy(s, a.data, a.len); if (b.len) memcpy(s + a.len, b.data, b.len); s[a.len + b.len] = 0; return (__ec_string_t){a.len + b.len, s}; }
            _Noreturn static inline void __ec_panic_index(const char* at, size_t i, size_t len) { fflush(stdout); fprintf(stderr, "%s: index %lld is out of bounds for length %zu\n", at, (long long)i, len); abort(); }
            static inline size_t __ec_index(size_t i, size_t len, const char* at) { if (i >= len) __ec_panic_index(at, i, len); return i; }
            static inline void* __ec_palloc_aligned(apr_pool_t* pool, size_t size, size_t align) { uintptr_t p = (uintptr_t)__ec_palloc(pool, size + align - 1); return (void*)((p + align - 1) & ~(uintptr_t)(align - 1)); }
            _Noreturn static inline void __ec_panic_slice(const char* at, size_t from, size_t to, size_t len) { fflush(stdout); fprintf(stderr, "%s: slice %lld..<%lld is out of bounds for length %zu\n", at, (long long)from, (long long)to, len); abort(); }
        """.trimIndent()

        /**
         * Says so when the main thread runs out of stack, which would be a plain segfault otherwise. That is a fault next to
         * the stack pointer, with the stack pointer at the end of the stack, which isn't known exactly: the arguments and
         * environment above `main`, which take up at most a quarter of it, count too. The handler needs a stack of its own,
         * and may only do what is safe in a signal handler: stdout may be locked by the code that stopped, and C headers only
         * declare `write` in unistd.h, whose names would clash with a program's. Any other fault goes back to the
         * default, and happens again when the handler returns.
         */
        private val PROGRAM_RUNTIME = """
            extern long __ec_write(int file, const void* data, size_t size) __asm__("write");
            static uintptr_t __ec_stack_low; /* a stack pointer below this is at the end of the main thread's stack */
            static uintptr_t __ec_stack_pointer(void* context) {
            #if defined(__linux__) && defined(__x86_64__)
                return (uintptr_t)((ucontext_t*)context)->uc_mcontext.gregs[15];
            #elif defined(__linux__) && defined(__aarch64__)
                return (uintptr_t)((ucontext_t*)context)->uc_mcontext.sp;
            #else
                (void)context; return UINTPTR_MAX;
            #endif
            }
            static void __ec_on_fault(int sig, siginfo_t* info, void* context) {
                uintptr_t at = (uintptr_t)info->si_addr, sp = __ec_stack_pointer(context), near = 1 << 16;
                if (sp < __ec_stack_low && at + near > sp && at < sp + near) {
                    if (ftrylockfile(stdout) == 0) { fflush(stdout); funlockfile(stdout); }
                    static const char message[] = "eclang: out of stack: the calls went too deep, or a local variable is too large. Check for recursion that never ends, and put large arrays in a pool with `new`.\n";
                    (void)__ec_write(2, message, sizeof message - 1);
                    abort();
                }
                signal(sig, SIG_DFL);
            }
            static void __ec_watch_stack(void) {
                static char stack[1 << 16];
                struct rlimit limit;
                char here;
                if (getrlimit(RLIMIT_STACK, &limit) != 0 || limit.rlim_cur == RLIM_INFINITY || limit.rlim_cur > (uintptr_t)&here) return;
                __ec_stack_low = (uintptr_t)&here - limit.rlim_cur + limit.rlim_cur / 4 + (1 << 16);
                stack_t alternate = { .ss_sp = stack, .ss_size = sizeof stack, .ss_flags = 0 };
                if (sigaltstack(&alternate, NULL) != 0) return;
                struct sigaction action;
                memset(&action, 0, sizeof action);
                action.sa_sigaction = __ec_on_fault;
                action.sa_flags = SA_SIGINFO | SA_ONSTACK;
                sigemptyset(&action.sa_mask);
                sigaction(SIGSEGV, &action, NULL);
                sigaction(SIGBUS, &action, NULL);
            }
        """.trimIndent()

        private val LIBRARY_RUNTIME = """
            static apr_pool_t* __ec_globals; /* the pool of a library's globals */
            static inline void __ec_enter(void) { if (__ec_pool == NULL) __ec_pool = __ec_pool_create(NULL); } /* a thread's first call from C */
        """.trimIndent()
    }
}
