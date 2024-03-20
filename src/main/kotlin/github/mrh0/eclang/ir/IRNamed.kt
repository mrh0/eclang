package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc

class IRNamed(location: Loc, private val name: String, private val index: Int) : IR(location) {
    override fun toString(): String {
        return "$$index"
    }
}