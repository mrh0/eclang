use "apr_fnmatch.h"
use "core:apr/errno.ec"
use "core:apr/tables.ec"

declare val APR_FNM_NOMATCH as Int
declare val APR_FNM_NOESCAPE as Int
declare val APR_FNM_PATHNAME as Int
declare val APR_FNM_PERIOD as Int
declare val APR_FNM_CASE_BLIND as Int

declare fn apr_fnmatch as (pattern: CString, strings: CString, flags: Int): apr_status_t
declare fn apr_fnmatch_test as (pattern: CString): Int
declare fn apr_match_glob as (pattern: CString, result: @@apr_array_header_t, p: @apr_pool_t): apr_status_t
