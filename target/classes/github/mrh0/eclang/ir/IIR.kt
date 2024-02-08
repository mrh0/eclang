package github.mrh0.eclang.ir

import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

interface IIR {
    fun evaluate(vm: VM, c: Context): GsBase
    fun deterministic() = false
}