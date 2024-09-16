package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.atom.AtomInstance
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

class IRProgram(location: Loc, val functions: List<IIR>, val globals: List<IIR>, val uses: List<IIR>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putLine("#include <stdio.h>")
        sb.putLine("#include <string.h>")
        sb.putLine("#include <math.h>")
        sb.putLine("#include <stdbool.h>")
        sb.putLine("#include <stddef.h>")
        sb.putLine("#include <stdalign.h>")
        sb.putLine("#include <apr_general.h>")
        sb.putLine("#include <apr_pools.h>")
        sb.putLine()

        sb.commentLine("Uses")
        uses.forEach { it.toC(sb, c) }
        sb.putLine()

        sb.commentLine("Atoms")
        AtomInstance.getAll().forEach { sb.putLine("char* ${it.getId()} = \"${it.label}\";") }
        sb.putLine()

        sb.commentLine("Built-In")
        sb.put("""
        void* __ec_nc(void* left, void* right) {
            return left == NULL ? right : left;
        }
        """.trimIndent())
        sb.putLine()
        sb.putLine()

        sb.commentLine("Declarations")
        functions.forEach { if (it is IRFunctionOverride) it.toCDeclaration(sb, c) }
        sb.putLine()

        sb.commentLine("Globals")
        globals.forEach { it.toC(sb, c) }
        sb.putLine()

        sb.commentLine("Code")
        functions.forEach { it.toC(sb, c) }
        sb.putLine()


        sb.commentLine("Main Entry")

        sb.put("""
        int main(int argc, const char *const argv[]) {
            apr_initialize();
            //apr_pool_t *pool;
            //apr_pool_create(&pool, NULL);
            
            int result = main_0();
            
            //apr_pool_destroy(pool);
            apr_terminate();
            return result;
        }
        """.trimIndent())
    }

    override fun toString(): String {
        return "IRProgram($functions)"
    }
}