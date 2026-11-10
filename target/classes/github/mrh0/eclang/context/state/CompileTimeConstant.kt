package github.mrh0.eclang.context.state

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRConstantRef
import github.mrh0.ect2.Type

/**
 * `const NAME = value`: a value known at compile time. An untyped number keeps its literal type, so it adapts
 * like a literal (`val c: Char = NAME`) and arithmetic on it is computed by the compiler. A top-level constant
 * is a C `#define` read by its [cName] ([byName]); a local one is replaced by its value.
 */
class CompileTimeConstant(private val name: String, private val type: Type, private val cName: String, val value: IIR, private val byName: Boolean) : IVar {
    override fun kind() = "a `const`"
    override fun toString() = "const $name: $type"
    override fun getName() = name
    override fun getType() = type
    override fun cName() = cName
    override fun toIR(location: Loc, cd: CompileData, hint: Type): IIR = if (byName) IRConstantRef(location, cName, value) else value
}
