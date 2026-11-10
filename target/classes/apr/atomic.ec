use "apr_atomic.h"
use "core:apr/pools.ec"

declare fn apr_atomic_init as (p: @apr_pool_t): apr_status_t
declare fn apr_atomic_read32 as (mem: @apr_uint32_t): apr_uint32_t
declare fn apr_atomic_set32 as (mem: @apr_uint32_t, value: apr_uint32_t)
declare fn apr_atomic_add32 as (mem: @apr_uint32_t, value: apr_uint32_t): apr_uint32_t
declare fn apr_atomic_sub32 as (mem: @apr_uint32_t, value: apr_uint32_t)
declare fn apr_atomic_inc32 as (mem: @apr_uint32_t): apr_uint32_t
declare fn apr_atomic_dec32 as (mem: @apr_uint32_t): Int
declare fn apr_atomic_cas32 as (mem: @apr_uint32_t, with: apr_uint32_t, cmp: apr_uint32_t): apr_uint32_t
declare fn apr_atomic_xchg32 as (mem: @apr_uint32_t, value: apr_uint32_t): apr_uint32_t
declare fn apr_atomic_casptr as (mem: @@Any, with: @Any, cmp: @Any): @Any
declare fn apr_atomic_xchgptr as (mem: @@Any, with: @Any): @Any
