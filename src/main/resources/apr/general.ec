declare type apr_signum_t as Int

declare val APR_ASCII_BLANK as Char
declare val APR_ASCII_CR as Char
declare val APR_ASCII_LF as Char
declare val APR_ASCII_TAB as Char

declare fn apr_initialize as (): apr_status_t
declare fn apr_app_initialize as (argc: @Int, argv: @@CString, env: @@CString): apr_status_t
declare fn apr_terminate as (): Void
declare fn apr_terminate2 as (): Void
declare fn apr_generate_random_bytes as (buf: @UChar, length: apr_size_t): apr_status_t
