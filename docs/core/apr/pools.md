# `pools.ec`

## Types

### <a id="apr_abortfunc_t"></a>`apr_abortfunc_t`

```ec
declare type apr_abortfunc_t as (retcode: Int) => Int
```

## Values

### <a id="APR_POOL_DEBUG"></a>`APR_POOL_DEBUG`

```ec
declare val APR_POOL_DEBUG as Int
```

## Functions

### <a id="apr_pool_initialize"></a>`apr_pool_initialize`

```ec
fn apr_pool_initialize(): apr_status_t
```

*Binds the C function `apr_pool_initialize`.*

### <a id="apr_pool_terminate"></a>`apr_pool_terminate`

```ec
fn apr_pool_terminate()
```

*Binds the C function `apr_pool_terminate`.*

### <a id="apr_pool_create_ex"></a>`apr_pool_create_ex`

```ec
fn apr_pool_create_ex(newpool: @@apr_pool_t, parent: @apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t): apr_status_t
```

*Binds the C function `apr_pool_create_ex`.*

### <a id="apr_pool_create_core_ex"></a>`apr_pool_create_core_ex`

```ec
fn apr_pool_create_core_ex(newpool: @@apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t): apr_status_t
```

*Binds the C function `apr_pool_create_core_ex`.*

### <a id="apr_pool_create_unmanaged_ex"></a>`apr_pool_create_unmanaged_ex`

```ec
fn apr_pool_create_unmanaged_ex(newpool: @@apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t): apr_status_t
```

*Binds the C function `apr_pool_create_unmanaged_ex`.*

### <a id="apr_pool_create_ex_debug"></a>`apr_pool_create_ex_debug`

```ec
fn apr_pool_create_ex_debug(newpool: @@apr_pool_t, parent: @apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t, file_line: CString): apr_status_t
```

*Binds the C function `apr_pool_create_ex_debug`.*

### <a id="apr_pool_create_core_ex_debug"></a>`apr_pool_create_core_ex_debug`

```ec
fn apr_pool_create_core_ex_debug(newpool: @@apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t, file_line: CString): apr_status_t
```

*Binds the C function `apr_pool_create_core_ex_debug`.*

### <a id="apr_pool_create_unmanaged_ex_debug"></a>`apr_pool_create_unmanaged_ex_debug`

```ec
fn apr_pool_create_unmanaged_ex_debug(newpool: @@apr_pool_t, abort_fn: apr_abortfunc_t, allocator: @apr_allocator_t, file_line: CString): apr_status_t
```

*Binds the C function `apr_pool_create_unmanaged_ex_debug`.*

### <a id="apr_pool_create"></a>`apr_pool_create`

```ec
fn apr_pool_create(newpool: @@apr_pool_t, parent: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_pool_create`.*

### <a id="apr_pool_create_core"></a>`apr_pool_create_core`

```ec
fn apr_pool_create_core(newpool: @@apr_pool_t): apr_status_t
```

*Binds the C function `apr_pool_create_core`.*

### <a id="apr_pool_create_unmanaged"></a>`apr_pool_create_unmanaged`

```ec
fn apr_pool_create_unmanaged(newpool: @@apr_pool_t): apr_status_t
```

*Binds the C function `apr_pool_create_unmanaged`.*

### <a id="apr_pool_allocator_get"></a>`apr_pool_allocator_get`

```ec
fn apr_pool_allocator_get(pool: @apr_pool_t): @apr_allocator_t
```

*Binds the C function `apr_pool_allocator_get`.*

### <a id="apr_pool_clear"></a>`apr_pool_clear`

```ec
fn apr_pool_clear(p: @apr_pool_t)
```

*Binds the C function `apr_pool_clear`.*

### <a id="apr_pool_clear_debug"></a>`apr_pool_clear_debug`

```ec
fn apr_pool_clear_debug(p: @apr_pool_t, file_line: CString)
```

*Binds the C function `apr_pool_clear_debug`.*

### <a id="apr_pool_destroy"></a>`apr_pool_destroy`

```ec
fn apr_pool_destroy(p: @apr_pool_t)
```

*Binds the C function `apr_pool_destroy`.*

### <a id="apr_pool_destroy_debug"></a>`apr_pool_destroy_debug`

```ec
fn apr_pool_destroy_debug(p: @apr_pool_t, file_line: CString)
```

*Binds the C function `apr_pool_destroy_debug`.*

### <a id="apr_palloc"></a>`apr_palloc`

```ec
fn apr_palloc(p: @apr_pool_t, size: apr_size_t): @Any
```

*Binds the C function `apr_palloc`.*

### <a id="apr_palloc_debug"></a>`apr_palloc_debug`

```ec
fn apr_palloc_debug(p: @apr_pool_t, size: apr_size_t, file_line: CString): @Any
```

*Binds the C function `apr_palloc_debug`.*

### <a id="apr_pcalloc"></a>`apr_pcalloc`

```ec
fn apr_pcalloc(p: @apr_pool_t, size: apr_size_t): @Any
```

*Binds the C function `apr_pcalloc`.*

### <a id="apr_pcalloc_debug"></a>`apr_pcalloc_debug`

```ec
fn apr_pcalloc_debug(p: @apr_pool_t, size: apr_size_t, file_line: CString): @Any
```

*Binds the C function `apr_pcalloc_debug`.*

### <a id="apr_pool_abort_set"></a>`apr_pool_abort_set`

```ec
fn apr_pool_abort_set(abortfunc: apr_abortfunc_t, pool: @apr_pool_t)
```

*Binds the C function `apr_pool_abort_set`.*

### <a id="apr_pool_abort_get"></a>`apr_pool_abort_get`

```ec
fn apr_pool_abort_get(pool: @apr_pool_t): apr_abortfunc_t
```

*Binds the C function `apr_pool_abort_get`.*

### <a id="apr_pool_parent_get"></a>`apr_pool_parent_get`

```ec
fn apr_pool_parent_get(pool: @apr_pool_t): @apr_pool_t?
```

*Binds the C function `apr_pool_parent_get`.*

### <a id="apr_pool_is_ancestor"></a>`apr_pool_is_ancestor`

```ec
fn apr_pool_is_ancestor(a: @apr_pool_t, b: @apr_pool_t): Int
```

*Binds the C function `apr_pool_is_ancestor`.*

### <a id="apr_pool_tag"></a>`apr_pool_tag`

```ec
fn apr_pool_tag(pool: @apr_pool_t, tag: CString)
```

*Binds the C function `apr_pool_tag`.*

### <a id="apr_pool_userdata_set"></a>`apr_pool_userdata_set`

```ec
fn apr_pool_userdata_set(data: @Any, key: CString, cleanup: fn (@Any): apr_status_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_pool_userdata_set`.*

### <a id="apr_pool_userdata_setn"></a>`apr_pool_userdata_setn`

```ec
fn apr_pool_userdata_setn(data: @Any, key: CString, cleanup: fn (@Any): apr_status_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_pool_userdata_setn`.*

### <a id="apr_pool_userdata_get"></a>`apr_pool_userdata_get`

```ec
fn apr_pool_userdata_get(data: @@Any, key: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_pool_userdata_get`.*

### <a id="apr_pool_cleanup_register"></a>`apr_pool_cleanup_register`

```ec
fn apr_pool_cleanup_register(p: @apr_pool_t, data: @Any, plain_cleanup: fn (@Any): apr_status_t, child_cleanup: fn (@Any): apr_status_t)
```

*Binds the C function `apr_pool_cleanup_register`.*

### <a id="apr_pool_pre_cleanup_register"></a>`apr_pool_pre_cleanup_register`

```ec
fn apr_pool_pre_cleanup_register(p: @apr_pool_t, data: @Any, plain_cleanup: fn (@Any): apr_status_t)
```

*Binds the C function `apr_pool_pre_cleanup_register`.*

### <a id="apr_pool_cleanup_kill"></a>`apr_pool_cleanup_kill`

```ec
fn apr_pool_cleanup_kill(p: @apr_pool_t, data: @Any, cleanup: fn (@Any): apr_status_t)
```

*Binds the C function `apr_pool_cleanup_kill`.*

### <a id="apr_pool_child_cleanup_set"></a>`apr_pool_child_cleanup_set`

```ec
fn apr_pool_child_cleanup_set(p: @apr_pool_t, data: @Any, plain_cleanup: fn (@Any): apr_status_t, child_cleanup: fn (@Any): apr_status_t)
```

*Binds the C function `apr_pool_child_cleanup_set`.*

### <a id="apr_pool_cleanup_run"></a>`apr_pool_cleanup_run`

```ec
fn apr_pool_cleanup_run(p: @apr_pool_t, data: @Any, cleanup: fn (@Any): apr_status_t): apr_status_t
```

*Binds the C function `apr_pool_cleanup_run`.*

### <a id="apr_pool_cleanup_null"></a>`apr_pool_cleanup_null`

```ec
fn apr_pool_cleanup_null(data: @Any): apr_status_t
```

*Binds the C function `apr_pool_cleanup_null`.*

### <a id="apr_pool_cleanup_for_exec"></a>`apr_pool_cleanup_for_exec`

```ec
fn apr_pool_cleanup_for_exec()
```

*Binds the C function `apr_pool_cleanup_for_exec`.*

### <a id="apr_pool_join"></a>`apr_pool_join`

```ec
fn apr_pool_join(p: @apr_pool_t, sub: @apr_pool_t)
```

*Binds the C function `apr_pool_join`.*

### <a id="apr_pool_lock"></a>`apr_pool_lock`

```ec
fn apr_pool_lock(pool: @apr_pool_t, flag: Int)
```

*Binds the C function `apr_pool_lock`.*
