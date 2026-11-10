use "apr_mmap.h"
use "core:apr/pools.ec"
use "core:apr/errno.ec"
use "core:apr/ring.ec"
use "core:apr/file_io.ec"

declare val APR_MMAP_READ as Int
declare val APR_MMAP_WRITE as Int
declare val APR_MMAP_THRESHOLD as Int
declare val APR_MMAP_LIMIT as Int
// #define 	APR_MMAP_CANDIDATE(filelength)

declare rec apr_mmap_t

declare fn apr_mmap_create as (out newmmap: @apr_mmap_t, file: @apr_file_t, offset: apr_off_t, size: apr_size_t, flag: apr_int32_t, cntxt: @apr_pool_t): apr_status_t
declare fn apr_mmap_dup as (out new_mmap: @apr_mmap_t, old_mmap: @apr_mmap_t, p: @apr_pool_t): apr_status_t
declare fn apr_mmap_delete as (mm: @apr_mmap_t): apr_status_t
declare fn apr_mmap_offset as (out addr: @Any, mm: @apr_mmap_t, offset: apr_off_t): apr_status_t
