use "apr_getopt.h"
use "core:apr/pools.ec"

declare type apr_getopt_err_fn_t as (arg: Ptr, err: CString, ...) => None
declare rec apr_getopt_t
declare rec apr_getopt_option_t

declare fn apr_getopt_init as (os: @@apr_getopt_t, cont: @apr_pool_t, argc: Int, argv: @Ptr): apr_status_t
declare fn apr_getopt as (os: @apr_getopt_t, opts: CString, option_ch: @Char, option_arg: @CString): apr_status_t
declare fn apr_getopt_long as (os: @apr_getopt_t, opts: @apr_getopt_option_t, option_ch: @Int, option_arg: @CString): apr_status_t
