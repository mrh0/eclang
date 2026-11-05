// core:vendor/digest.c: MD5, SHA-1 and SHA-256 in C, written into programs that use core:hash.ec.
use "core:vendor/digest.c"
use "core:apr/file_io.ec"
use "core:apr/errno.ec"

declare fn digestSize extern "__ec_digest_size" as (): Size
declare fn digestInit extern "__ec_digest_init" as (state: @Any, algorithm: Int)
declare fn digestUpdate extern "__ec_digest_update" as (state: @Any, algorithm: Int, data: CString, len: Size)
declare fn digestFinal extern "__ec_digest_final" as (state: @Any, algorithm: Int, out: CString)
