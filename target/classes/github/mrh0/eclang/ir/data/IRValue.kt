package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IR

abstract class IRValue(location: Loc) : IR(location) {
    override fun deterministic() = true
}