use "apr_pools.h"
use "core:apr/apr.ec"
use "core:apr/errno.ec"
use "core:apr/general.ec"
use "core:apr/want.ec"
use "core:apr/allocator.ec"

// #define APR_WANT_MEMFUNC
// #define 	APR_POOL_DECLARE_ACCESSOR(type)
// #define 	APR_POOL_IMPLEMENT_ACCESSOR(type)
declare val APR_POOL_DEBUG as Int
// #define 	APR_POOL__FILE_LINE__   __FILE__ ":" APR_STRINGIFY(__LINE__)

declare rec apr_pool_t
declare type apr_abortfunc_t as (retcode: Int) => Int

declare fn apr_pool_initialize as (): apr_status_t
declare fn apr_pool_terminate as ()
declare fn apr_pool_create_ex as (newpool: @@apr_pool_t, parent: @apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t): apr_status_t
declare fn apr_pool_create_core_ex as (newpool: @@apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t): apr_status_t
declare fn apr_pool_create_unmanaged_ex as (newpool: @@apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t): apr_status_t
declare fn apr_pool_create_ex_debug as (newpool: @@apr_pool_t, parent: @apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t, file_line: CString): apr_status_t
declare fn apr_pool_create_core_ex_debug as (newpool: @@apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t, file_line: CString): apr_status_t
declare fn apr_pool_create_unmanaged_ex_debug as (newpool: @@apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t, file_line: CString): apr_status_t
declare fn apr_pool_create as (newpool: @@apr_pool_t, parent: @apr_pool_t): apr_status_t
declare fn apr_pool_create_core as (newpool: @@apr_pool_t): apr_status_t
declare fn apr_pool_create_unmanaged as (newpool: @@apr_pool_t): apr_status_t
declare fn apr_pool_allocator_get as (pool: @apr_pool_t): @apr_allocator_t
declare fn apr_pool_clear as (p: @apr_pool_t):
declare fn apr_pool_clear_debug as (p: @apr_pool_t, file_line: CString)
declare fn apr_pool_destroy as (p: @apr_pool_t)
declare fn apr_pool_destroy_debug as (p: @apr_pool_t, file_line: CString)
declare fn apr_palloc as (p: @apr_pool_t, size: apr_size_t): @Any
declare fn apr_palloc_debug as (p: @apr_pool_t, size: apr_size_t, file_line: CString): @Any
declare fn apr_pcalloc as (p: @apr_pool_t, size: apr_size_t): @Any
declare fn apr_pcalloc_debug as (p: @apr_pool_t, size: apr_size_t, file_line: CString): @Any
declare fn apr_pool_abort_set as (abortfunc: apr_abortfunc_t, pool: @apr_pool_t)
declare fn apr_pool_abort_get as (pool: @apr_pool_t): apr_abortfunc_t
declare fn apr_pool_parent_get as (pool: @apr_pool_t): @apr_pool_t
declare fn apr_pool_is_ancestor as (a: @apr_pool_t, b: @apr_pool_t): Int
declare fn apr_pool_tag as (pool: @apr_pool_t, tag: CString)
declare fn apr_pool_userdata_set as (data: @Any, key: CString, cleanup: fn (@Any): apr_status_t, pool: @apr_pool_t): apr_status_t
declare fn apr_pool_userdata_setn as (data: @Any, key: CString, cleanup: fn (@Any): apr_status_t, pool: @apr_pool_t): apr_status_t
declare fn apr_pool_userdata_get as (data: @@Any, key: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_pool_cleanup_register as (p: @apr_pool_t, data: @Any, plain_cleanup: fn (@Any): apr_status_t, child_cleanup: fn (@Any): apr_status_t)
declare fn apr_pool_pre_cleanup_register as (p: @apr_pool_t, data: @Any, plain_cleanup: fn (@Any): apr_status_t)
declare fn apr_pool_cleanup_kill as (p: @apr_pool_t, data: @Any, cleanup: fn (@Any): apr_status_t)
declare fn apr_pool_child_cleanup_set as (p: @apr_pool_t, data: @Any, plain_cleanup: fn (@Any): apr_status_t, child_cleanup: fn (@Any): apr_status_t)
declare fn apr_pool_cleanup_run as (p: @apr_pool_t, data: @Any, cleanup: fn (@Any): apr_status_t): apr_status_t
declare fn apr_pool_cleanup_null as (data: @Any): apr_status_t
declare fn apr_pool_cleanup_for_exec as ()
declare fn apr_pool_join as (p: @apr_pool_t, sub: @apr_pool_t)
declare fn apr_pool_find as (mem: @Any): @apr_pool_t
declare fn apr_pool_num_bytes as (p: @apr_pool_t, recurse: Int): apr_size_t
declare fn apr_pool_lock as (pool: @apr_pool_t, flag: Int)
