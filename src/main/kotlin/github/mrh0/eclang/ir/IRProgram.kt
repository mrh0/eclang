package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.array.ArrayInstance
import github.mrh0.eclang.context.atom.AtomInstance
import github.mrh0.eclang.context.signature.CallSignatureInstance
import github.mrh0.eclang.context.tuple.TupleInstance
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.util.Util

class IRProgram(location: Loc, private val functions: List<IIR>, private val globals: List<IIR>, private val uses: List<IIR>) : IR(location) {
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

        sb.commentLine("Built-In")
        sb.put("""
        void* __ec_nc(void* left, void* right) {
            return left == NULL ? right : left;
        }
        typedef struct { int line; int position; char* path; } __ec_location_t;
        typedef struct { size_t len; char* data; } __ec_string_t;
        """.trimIndent())
        sb.putLine()
        sb.putLine()

        sb.commentLine("Atoms")
        AtomInstance.getAll().forEach { sb.putLine("char* ${it.getId()} = \"${it.label}\";") }
        sb.putLine()

        sb.commentLine("Tuples")
        TupleInstance.getAll().forEach { it.toC(sb, c) }
        sb.putLine()

        sb.commentLine("Arrays")
        ArrayInstance.getAll().forEach { it.toC(sb, c) }
        sb.putLine()

        sb.commentLine("Declarations")
        functions.forEach { if (it is IRFunctionOverride) it.toCDeclaration(sb, c) }
        sb.putLine()

        sb.commentLine("Call Signatures")
        CallSignatureInstance.getAll().forEach { it.toC(sb, c) }
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
            
            int __ec_ret = main_0();
            
            apr_terminate();
            return __ec_ret;
        }
        """.trimIndent())
    }

    override fun toString(): String {
        return "IRProgram($functions)"
    }
}