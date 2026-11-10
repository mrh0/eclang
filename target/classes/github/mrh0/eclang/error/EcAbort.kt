package github.mrh0.eclang.error

/**
 * Stops the compilation after errors were reported with `CompileData.report` (not an [EcError], so nothing recovers
 * from it). Thrown between phases whose later work would only repeat the errors, and after too many errors.
 */
class EcAbort : RuntimeException("compilation stopped after errors")
