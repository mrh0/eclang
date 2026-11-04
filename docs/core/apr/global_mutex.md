# `global_mutex.ec`

## Types

### <a id="apr_global_mutex_t"></a>`apr_global_mutex_t`

```ec
declare rec apr_global_mutex_t
```

## Functions

### <a id="apr_global_mutex_create"></a>`apr_global_mutex_create`

```ec
fn apr_global_mutex_create(mutex: @@apr_global_mutex_t, fname: CString, mech: apr_lockmech_e, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_global_mutex_create`.*

### <a id="apr_global_mutex_child_init"></a>`apr_global_mutex_child_init`

```ec
fn apr_global_mutex_child_init(mutex: @@apr_global_mutex_t, fname: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_global_mutex_child_init`.*

### <a id="apr_global_mutex_lock"></a>`apr_global_mutex_lock`

```ec
fn apr_global_mutex_lock(mutex: @apr_global_mutex_t): apr_status_t
```

*Binds the C function `apr_global_mutex_lock`.*

### <a id="apr_global_mutex_trylock"></a>`apr_global_mutex_trylock`

```ec
fn apr_global_mutex_trylock(mutex: @apr_global_mutex_t): apr_status_t
```

*Binds the C function `apr_global_mutex_trylock`.*

### <a id="apr_global_mutex_unlock"></a>`apr_global_mutex_unlock`

```ec
fn apr_global_mutex_unlock(mutex: @apr_global_mutex_t): apr_status_t
```

*Binds the C function `apr_global_mutex_unlock`.*

### <a id="apr_global_mutex_destroy"></a>`apr_global_mutex_destroy`

```ec
fn apr_global_mutex_destroy(mutex: @apr_global_mutex_t): apr_status_t
```

*Binds the C function `apr_global_mutex_destroy`.*

### <a id="apr_global_mutex_lockfile"></a>`apr_global_mutex_lockfile`

```ec
fn apr_global_mutex_lockfile(mutex: @apr_global_mutex_t): CString
```

*Binds the C function `apr_global_mutex_lockfile`.*

### <a id="apr_global_mutex_name"></a>`apr_global_mutex_name`

```ec
fn apr_global_mutex_name(mutex: @apr_global_mutex_t): CString
```

*Binds the C function `apr_global_mutex_name`.*

### <a id="apr_global_mutex_pool_get"></a>`apr_global_mutex_pool_get`

```ec
fn apr_global_mutex_pool_get(theglobal_mutex: @apr_global_mutex_t): @apr_pool_t
```

*Binds the C function `apr_global_mutex_pool_get`.*
