package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRProgram(location: Loc, val functions: List<IIR>, val records: List<IIR>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("#include <stdio.h>")
        sb.putLine()
        records.forEach { it.toC(sb, c) }
        functions.forEach { it.toC(sb, c) }
    }

    override fun toString(): String {
        return "IRProgram"
    }
}