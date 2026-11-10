package github.mrh0.eclang.ast


abstract class Tok(val location: Loc) : ITok {
    override fun toString(): String {
        return this.javaClass.name
    }
}

/** A statement, declaration, type or parameter: a token that has no value (see [ITok.pointerFlow]). */
abstract class NonValueTok(location: Loc) : Tok(location) {
    final override fun pointerFlow(): PointerFlow = PointerFlow.NotAValue
}
