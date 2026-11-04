// signal.h: signal numbers and raise. To set a handler, use apr_signal from core:apr/signal.ec.
use "signal.h"

declare val SIGABRT as Int
declare val SIGBUS as Int
declare val SIGCONT as Int
declare val SIGFPE as Int
declare val SIGHUP as Int
declare val SIGILL as Int
declare val SIGINT as Int
declare val SIGQUIT as Int
declare val SIGSEGV as Int
declare val SIGSTOP as Int
declare val SIGTERM as Int
declare val SIGTSTP as Int
declare val SIGWINCH as Int

// The default action of a signal, and ignoring it, as handlers.
declare val SIG_DFL as (Int) => None
declare val SIG_IGN as (Int) => None

// Sends a signal to this thread. 0 on success.
declare fn raise as (sig: Int): Int
