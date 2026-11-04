# `allocator.ec`

## Types

### <a id="apr_allocator_t"></a>`apr_allocator_t`

```ec
declare rec apr_allocator_t
```

### <a id="apr_memnode_t"></a>`apr_memnode_t`

```ec
declare rec apr_memnode_t
```

## Values

### <a id="APR_MEMNODE_T_SIZE"></a>`APR_MEMNODE_T_SIZE`

```ec
declare val APR_MEMNODE_T_SIZE as Int
```

### <a id="APR_ALLOCATOR_MAX_FREE_UNLIMITED"></a>`APR_ALLOCATOR_MAX_FREE_UNLIMITED`

```ec
declare val APR_ALLOCATOR_MAX_FREE_UNLIMITED as Int
```

## Functions

### <a id="apr_allocator_create"></a>`apr_allocator_create`

```ec
fn apr_allocator_create(allocator: @@apr_allocator_t): apr_status_t
```

*Binds the C function `apr_allocator_create`.*

### <a id="apr_allocator_destroy"></a>`apr_allocator_destroy`

```ec
fn apr_allocator_destroy(allocator: @apr_allocator_t)
```

*Binds the C function `apr_allocator_destroy`.*

### <a id="apr_allocator_alloc"></a>`apr_allocator_alloc`

```ec
fn apr_allocator_alloc(allocator: @apr_allocator_t, size: apr_size_t): @apr_memnode_t
```

*Binds the C function `apr_allocator_alloc`.*

### <a id="apr_allocator_free"></a>`apr_allocator_free`

```ec
fn apr_allocator_free(allocator: @apr_allocator_t, memnode: @apr_memnode_t)
```

*Binds the C function `apr_allocator_free`.*

### <a id="apr_allocator_owner_set"></a>`apr_allocator_owner_set`

```ec
fn apr_allocator_owner_set(allocator: @apr_allocator_t, pool: @apr_pool_t)
```

*Binds the C function `apr_allocator_owner_set`.*

### <a id="apr_allocator_owner_get"></a>`apr_allocator_owner_get`

```ec
fn apr_allocator_owner_get(allocator: @apr_allocator_t): @apr_pool_t
```

*Binds the C function `apr_allocator_owner_get`.*

### <a id="apr_allocator_max_free_set"></a>`apr_allocator_max_free_set`

```ec
fn apr_allocator_max_free_set(allocator: @apr_allocator_t, size: apr_size_t)
```

*Binds the C function `apr_allocator_max_free_set`.*

### <a id="apr_allocator_mutex_set"></a>`apr_allocator_mutex_set`

```ec
fn apr_allocator_mutex_set(allocator: @apr_allocator_t, mutex: @apr_thread_mutex_t)
```

*Binds the C function `apr_allocator_mutex_set`.*

### <a id="apr_allocator_mutex_get"></a>`apr_allocator_mutex_get`

```ec
fn apr_allocator_mutex_get(allocator: @apr_allocator_t): @apr_thread_mutex_t
```

*Binds the C function `apr_allocator_mutex_get`.*
