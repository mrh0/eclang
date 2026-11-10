package github.mrh0.eclang.error.fn

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

/** No function called [name] is visible here ([where]: the module of a qualified call). [hint] says where it is. */
class EcFunctionNotDefinedError(location: Loc, name: String, where: String?, hint: String = "") :
    EcError(location, "Function '$name' is not defined${where?.let { " in $it" } ?: ""}.$hint")
