package github.mrh0.eclang.vm.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.function.IRFunc
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM
import java.util.function.Supplier

class UserCallable(private val func: Supplier<IRFunc>) : ICallable {
    override fun call(location: Loc, vm: VM, c: Context, args: Array<GsBase>): GsBase {
        args.forEachIndexed { i, it ->  c.setValue(location, i, it) }
        return func.get().evaluate(vm, c)
    }
    override fun test(location: Loc, args: Array<EcTypeAny>): EcTypeAny {
        throw NotImplementedError()
    }
}