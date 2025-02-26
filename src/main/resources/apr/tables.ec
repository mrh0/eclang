use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "stdarg.ec"

// APR_ARRAY_IDX(ary, i, type)   (((type *)(ary)->elts)[i])
// APR_ARRAY_PUSH(ary, type)   (*((type *)apr_array_push(ary)))
declare val APR_OVERLAP_TABLES_SET as Int
declare val APR_OVERLAP_TABLES_MERGE as Int

declare rec apr_table_t
declare rec apr_array_header_t
declare rec apr_table_entry_t
declare type apr_table_do_callback_fn_t as (@Any, CString, CString) => Int

declare fn apr_table_elts as (t: @apr_table_t): @apr_array_header_t
declare fn apr_is_empty_table as (t: @apr_table_t): Int
declare fn apr_is_empty_array as (a: @apr_array_header_t): Int
declare fn apr_array_make as (p: @apr_pool_t, nelts: Int, elt_size: Int): @apr_array_header_t
declare fn apr_array_push as (arr: @apr_array_header_t): @Any
declare fn apr_array_pop as (arr: @apr_array_header_t): @Any
declare fn apr_array_clear as (arr: @apr_array_header_t)
declare fn apr_array_cat as (dst: @apr_array_header_t, src: @apr_array_header_t)
declare fn apr_array_copy as (p: @apr_pool_t, arr: @apr_array_header_t): @apr_array_header_t
declare fn apr_array_copy_hdr as (p: @apr_pool_t, arr: @apr_array_header_t): @apr_array_header_t
declare fn apr_array_append as (p: @apr_pool_t, first: @apr_array_header_t, second: @apr_array_header_t): @apr_array_header_t
declare fn apr_array_pstrcat as (p: @apr_pool_t, arr: @apr_array_header_t, sep: Char): CString
declare fn apr_table_make as (p: @apr_pool_t, nelts: Int): @apr_table_t
declare fn apr_table_copy as (p: @apr_pool_t, t: @apr_table_t): @apr_table_t
declare fn apr_table_clone as (p: @apr_pool_t, t: @apr_table_t): @apr_table_t
declare fn apr_table_clear as (t: @apr_table_t)
declare fn apr_table_get as (t: @apr_table_t, key: CString): CString
declare fn apr_table_set as (t: @apr_table_t, key: CString, val: CString)
declare fn apr_table_setn as (t: @apr_table_t, key: CString, val: CString)
declare fn apr_table_unset as (t: @apr_table_t, key: CString)
declare fn apr_table_merge as (t: @apr_table_t, key: CString, val: CString)
declare fn apr_table_mergen as (t: @apr_table_t, key: CString, val: CString)
declare fn apr_table_add as (t: @apr_table_t, key: CString, val: CString)
declare fn apr_table_addn as (t: @apr_table_t, key: CString, val: CString)
declare fn apr_table_overlay as (p: @apr_pool_t, overlay: @apr_table_t, base: @apr_table_t): @apr_table_t
declare fn apr_table_do as (comp: apr_table_do_callback_fn_t, rec: @Any, t: @apr_table_t, ...): Int
declare fn apr_table_vdo as (comp: apr_table_do_callback_fn_t, rec: @Any, t: @apr_table_t, vp: @va_list): Int
declare fn apr_table_overlap as (a: @apr_table_t, b: @apr_table_t, flags: UInt)
declare fn apr_table_compress as (t: @apr_table_t, flags: UInt)
