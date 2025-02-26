use "apr_hash.h"
use "core:apr/pools.ec"

declare rec apr_hash_t
declare rec apr_hash_index_t
declare type apr_hashfunc_t as (CString, @apr_ssize_t) => UInt
declare type apr_hash_do_callback_fn_t as (@Any, @Any, apr_ssize_t, @Any) => Int

declare fn apr_hashfunc_default as (key: CString, klen: @apr_ssize_t): UInt
declare fn apr_hash_make as (pool: @apr_pool_t): @apr_hash_t
declare fn apr_hash_make_custom as (pool: @apr_pool_t, hash_func: apr_hashfunc_t): @apr_hash_t
declare fn apr_hash_copy as (pool: @apr_pool_t, h: @apr_hash_t): @apr_hash_t
declare fn apr_hash_set as (ht: @apr_hash_t, key: @Any, klen: apr_ssize_t, val: @Any)
declare fn apr_hash_get as (ht: @apr_hash_t, key: @Any, klen: apr_ssize_t): @Any
declare fn apr_hash_first as (p: @apr_pool_t, ht: @apr_hash_t): @apr_hash_index_t
declare fn apr_hash_next as (hi: @apr_hash_index_t): @apr_hash_index_t
declare fn apr_hash_this as (hi: @apr_hash_index_t, key: @@Any, klen: @apr_ssize_t, val: @@Any)
declare fn apr_hash_count as (ht: @apr_hash_t): UInt
declare fn apr_hash_clear as (ht: @apr_hash_t)
declare fn apr_hash_overlay as (p: @apr_pool_t, overlay: @apr_hash_t, base: @apr_hash_t): @apr_hash_t
declare fn apr_hash_merge as (
    p: @apr_pool_t,
    h1: @apr_hash_t,
    h2: @apr_hash_t,
    merger: (@apr_pool_t, @Any, apr_ssize_t, @Any, @Any, @Any) => @Any,
    data: @Any
): @apr_hash_t
declare fn apr_hash_do as (comp: apr_hash_do_callback_fn_t, _rec: @Any, ht: @apr_hash_t): Int
declare fn apr_hash_pool_get as (thehash: @apr_hash_t): @apr_pool_t
