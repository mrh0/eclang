package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Result

/** `try f() catch e do ...`: runs the body with `e` bound to the error when f failed. */
class IRTryCatch(location: Loc, val temp: String, val type: Result, val call: IIR, val errorName: String, val body: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("${sb.types.cName(type)} $temp = ")
        call.toC(sb, c)
        sb.endStatement()
        sb.putStatement("if ($temp.code != 0) ")
        sb.pushScope(BlockScope())
        sb.putStatement("${sb.types.cName(type.error)} $errorName = $temp.error")
        sb.endStatement()
        sb.putStatement("(void)$errorName") // the block may not use the error
        sb.endStatement()
        body.toC(sb, c)
        sb.popScope()
    }

    override fun toString() = "TryCatch($call, $errorName, $body)"
}
