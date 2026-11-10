use "apr_getopt.h"
use "core:apr/pools.ec"

// void (apr_getopt_err_fn_t)(void* arg, const char* err, ...): eclang function types have no C varargs.
declare rec apr_getopt_err_fn_t
declare rec apr_getopt_t
declare rec apr_getopt_option_t

declare fn apr_getopt_init as (out os: @apr_getopt_t, cont: @apr_pool_t, argc: Int, argv: @CString): apr_status_t
declare fn apr_getopt as (os: @apr_getopt_t, opts: CString, out option_ch: Char, out option_arg: CString): apr_status_t
declare fn apr_getopt_long as (os: @apr_getopt_t, opts: @apr_getopt_option_t, out option_ch: Int, out option_arg: CString): apr_status_t
