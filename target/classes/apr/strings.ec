use "apr_strings.h"
use "core:apr.ec"
use "core:apr_errno.ec"
use "core:apr_pools.ec"
use "core:apr_want.ec"
use "core:stdarg.ec"

declare fn apr_strnatcmp as (a: CString, b: CString): Int
declare fn apr_strnatcasecmp as (a: CString, b: CString): Int
declare fn apr_pstrdup as (p: @apr_pool_t, s: CString): CString
declare fn apr_pstrmemdup as (p: @apr_pool_t, s: CString, n: apr_size_t): CString
declare fn apr_pstrndup as (p: @apr_pool_t, s: CString, n: apr_size_t): CString
declare fn apr_pmemdup as (p: @apr_pool_t, m: @Any, n: apr_size_t): @Any
declare fn apr_pstrcat as (p: @apr_pool_t, ...): CString
declare fn apr_pstrcatv as (p: @apr_pool_t, vec: @iovec, nvec: apr_size_t, nbytes: @apr_size_t): CString
declare fn apr_pvsprintf as (p: @apr_pool_t, fmt: CString, ap: va_list): CString
declare fn apr_psprintf as (p: @apr_pool_t, fmt: CString, ...): CString
declare fn apr_cpystrn as (dst: CString, src: CString, dst_size: apr_size_t): CString
declare fn apr_collapse_spaces as (dest: CString, src: CString): CString
declare fn apr_tokenize_to_argv as (arg_str: CString, argv_out: @@CString, token_context: @apr_pool_t): apr_status_t
declare fn apr_strtok as (str: CString, sep: CString, last: @CString): CString
declare fn apr_itoa as (p: @apr_pool_t, n: Int): CString
declare fn apr_ltoa as (p: @apr_pool_t, n: Long): CString
declare fn apr_off_t_toa as (p: @apr_pool_t, n: apr_off_t): CString
declare fn apr_strtoff as (offset: @apr_off_t, buf: CString, end: @CString, base: Int): apr_status_t
declare fn apr_strtoi64 as (buf: CString, end: @CString, base: Int): apr_int64_t
declare fn apr_atoi64 as (buf: CString): apr_int64_t
declare fn apr_strfsize as (size: apr_off_t, buf: CString): CString
