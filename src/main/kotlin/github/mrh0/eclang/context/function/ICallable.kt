package github.mrh0.eclang.context.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcTypeAny

interface ICallable {
    fun test(location: Loc, args: Array<EcTypeAny>): EcTypeAny
}