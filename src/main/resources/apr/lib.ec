use "apr_lib.h"
use "apr.h"
use "core:apr/errno.ec"
use "core:ctype.ec"
use "core:stdarg.ec"

declare rec apr_vformatter_buff_t

const HUGE_STRING_LEN: Int = 8192

// declare fn apr_killpg as (x, y)
declare fn apr_isalnum as (c: UChar): Bool
declare fn apr_isalpha as (c: UChar): Bool
declare fn apr_iscntrl as (c: UChar): Bool
declare fn apr_isdigit as (c: UChar): Bool
declare fn apr_isgraph as (c: UChar): Bool
declare fn apr_islower as (c: UChar): Bool
declare fn apr_isascii as (c: UChar): Bool
declare fn apr_isprint as (c: UChar): Bool
declare fn apr_ispunct as (c: UChar): Bool
declare fn apr_isspace as (c: UChar): Bool
declare fn apr_isupper as (c: UChar): Bool
declare fn apr_isxdigit as (c: UChar): Bool
declare fn apr_tolower as (c: UChar)
declare fn apr_toupper as (c: UChar)

declare fn apr_filepath_name_get as (pathname: CString): CString
declare fn apr_vformatter as (flush_func: (@apr_vformatter_buff_t) => Int, c: @apr_vformatter_buff_t, fmt: CString, ap: va_list): Int
declare fn apr_password_get as (prompt: CString, pwbuf: CString, bufsize: @apr_size_t): apr_status_t
