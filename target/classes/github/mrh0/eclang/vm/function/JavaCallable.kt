package github.mrh0.eclang.vm.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.reflect.Reflection
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM
import java.lang.reflect.Method

class JavaCallable(private val method: Method) : ICallable {
    override fun call(location: Loc, vm: VM, c: Context, args: Array<GsBase>): GsBase = Reflection.call(location, method, args)
    override fun test(location: Loc, args: Array<EcTypeAny>): EcTypeAny {
        throw NotImplementedError()
    }
}