# `thread_mutex.ec`

## Types

### <a id="apr_thread_mutex_t"></a>`apr_thread_mutex_t`

```ec
declare rec apr_thread_mutex_t
```

## Values

### <a id="APR_THREAD_MUTEX_DEFAULT"></a>`APR_THREAD_MUTEX_DEFAULT`

```ec
declare val APR_THREAD_MUTEX_DEFAULT as Int
```

### <a id="APR_THREAD_MUTEX_NESTED"></a>`APR_THREAD_MUTEX_NESTED`

```ec
declare val APR_THREAD_MUTEX_NESTED as Int
```

### <a id="APR_THREAD_MUTEX_UNNESTED"></a>`APR_THREAD_MUTEX_UNNESTED`

```ec
declare val APR_THREAD_MUTEX_UNNESTED as Int
```

## Functions

### <a id="apr_thread_mutex_create"></a>`apr_thread_mutex_create`

```ec
fn apr_thread_mutex_create(mutex: @@apr_thread_mutex_t, flags: UInt, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_thread_mutex_create`.*

### <a id="apr_thread_mutex_lock"></a>`apr_thread_mutex_lock`

```ec
fn apr_thread_mutex_lock(mutex: @apr_thread_mutex_t): apr_status_t
```

*Binds the C function `apr_thread_mutex_lock`.*

### <a id="apr_thread_mutex_trylock"></a>`apr_thread_mutex_trylock`

```ec
fn apr_thread_mutex_trylock(mutex: @apr_thread_mutex_t): apr_status_t
```

*Binds the C function `apr_thread_mutex_trylock`.*

### <a id="apr_thread_mutex_unlock"></a>`apr_thread_mutex_unlock`

```ec
fn apr_thread_mutex_unlock(mutex: @apr_thread_mutex_t): apr_status_t
```

*Binds the C function `apr_thread_mutex_unlock`.*

### <a id="apr_thread_mutex_destroy"></a>`apr_thread_mutex_destroy`

```ec
fn apr_thread_mutex_destroy(mutex: @apr_thread_mutex_t): apr_status_t
```

*Binds the C function `apr_thread_mutex_destroy`.*

### <a id="apr_thread_mutex_pool_get"></a>`apr_thread_mutex_pool_get`

```ec
fn apr_thread_mutex_pool_get(thethread_mutex: @apr_thread_mutex_t): @apr_pool_t
```

*Binds the C function `apr_thread_mutex_pool_get`.*
