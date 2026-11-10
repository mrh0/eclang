package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

class EcGenericNotEstablishedError (location: Loc, name: String) : EcError(location, "Can't tell what the result type '$name' is: no argument of this call decides it. Say it where the result goes, e.g. give the variable a type: `val x: ${name.replace(Regex("(^|[<, (])<\\w+>"), "$1Int")} = ...`.") {
}