// apr.h: APR's basic types, as eclang types with the same C representation (64-bit Linux).
use "apr.h"
use "stdarg.h"
use "sys/uio.h"

declare type apr_byte_t as UChar
declare type apr_int16_t as Short
declare type apr_uint16_t as UShort
declare type apr_int32_t as Int
declare type apr_uint32_t as UInt
declare type apr_int64_t as Long
declare type apr_uint64_t as ULong
declare type apr_size_t as Size
declare type apr_ssize_t as Long
declare type apr_off_t as Long
declare type apr_socklen_t as UInt
declare type apr_ino_t as ULong
declare type apr_uintptr_t as ULong
declare type apr_wait_t as Int

declare val APR_SIZE_MAX as Size
declare val APR_INT64_MAX as Long

// C's va_list, for the v* variants of formatting functions.
declare rec va_list

// struct iovec (sys/uio.h): one buffer of a scatter/gather write.
declare struct iovec as
    iov_base: @Any
    iov_len: Size
