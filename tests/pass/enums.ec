use "core:lib.ec"

// A C enum (or set of #define constants) seen as atoms: eclang code uses :success and :failure,
// and only calls of `declare fn` functions pass and get the C constants.
declare enum ExitCode as
    :success = EXIT_SUCCESS
    :failure = EXIT_FAILURE

// abs(EXIT_FAILURE) is EXIT_FAILURE: the atom goes to C and comes back.
declare fn abs as (code: ExitCode): ExitCode

fn describe(code: ExitCode): CString = if code == :success do "ok"c else "failed"c

fn main(): Int do
    val a = abs(:failure)
    log(a)
    log(describe(a))
    val b: ExitCode = :success
    log(abs(b))
    log(describe(abs(b)))
    ret 0
