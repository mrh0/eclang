use "apr_signal.h"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "signal.h"

declare type apr_sigfunc_t as (Int) => None

declare fn apr_signal as (signo: Int, func: apr_sigfunc_t): apr_sigfunc_t
declare fn apr_signal_description_get as (signum: Int): CString
declare fn apr_signal_init as (pglobal: @apr_pool_t)
declare fn apr_signal_block as (signum: Int): apr_status_t
declare fn apr_signal_unblock as (signum: Int): apr_status_t
