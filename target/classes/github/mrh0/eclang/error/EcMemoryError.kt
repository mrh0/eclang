package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

/**
 * An error of the checks of memory (Lifetimes, Escapes): memory used after it is freed, or kept longer than it lives. A compilation
 * that fails with one is tried again with what it found that functions return (see Escapes.knownResults), which may let it pass.
 */
class EcMemoryError(location: Loc, reason: String) : EcError(location, reason)
