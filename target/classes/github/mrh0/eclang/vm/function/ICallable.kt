package github.mrh0.eclang.vm.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

interface ICallable {
    fun call(location: Loc, vm: VM, c: Context, args: Array<GsBase>): GsBase
    fun test(location: Loc, args: Array<EcTypeAny>): EcTypeAny
}