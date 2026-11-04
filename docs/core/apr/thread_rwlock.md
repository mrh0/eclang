# `thread_rwlock.ec`

## Types

### <a id="apr_thread_rwlock_t"></a>`apr_thread_rwlock_t`

```ec
declare rec apr_thread_rwlock_t
```

## Functions

### <a id="apr_thread_rwlock_create"></a>`apr_thread_rwlock_create`

```ec
fn apr_thread_rwlock_create(rwlock: @@apr_thread_rwlock_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_thread_rwlock_create`.*

### <a id="apr_thread_rwlock_rdlock"></a>`apr_thread_rwlock_rdlock`

```ec
fn apr_thread_rwlock_rdlock(rwlock: @apr_thread_rwlock_t): apr_status_t
```

*Binds the C function `apr_thread_rwlock_rdlock`.*

### <a id="apr_thread_rwlock_tryrdlock"></a>`apr_thread_rwlock_tryrdlock`

```ec
fn apr_thread_rwlock_tryrdlock(rwlock: @apr_thread_rwlock_t): apr_status_t
```

*Binds the C function `apr_thread_rwlock_tryrdlock`.*

### <a id="apr_thread_rwlock_wrlock"></a>`apr_thread_rwlock_wrlock`

```ec
fn apr_thread_rwlock_wrlock(rwlock: @apr_thread_rwlock_t): apr_status_t
```

*Binds the C function `apr_thread_rwlock_wrlock`.*

### <a id="apr_thread_rwlock_trywrlock"></a>`apr_thread_rwlock_trywrlock`

```ec
fn apr_thread_rwlock_trywrlock(rwlock: @apr_thread_rwlock_t): apr_status_t
```

*Binds the C function `apr_thread_rwlock_trywrlock`.*

### <a id="apr_thread_rwlock_unlock"></a>`apr_thread_rwlock_unlock`

```ec
fn apr_thread_rwlock_unlock(rwlock: @apr_thread_rwlock_t): apr_status_t
```

*Binds the C function `apr_thread_rwlock_unlock`.*

### <a id="apr_thread_rwlock_destroy"></a>`apr_thread_rwlock_destroy`

```ec
fn apr_thread_rwlock_destroy(rwlock: @apr_thread_rwlock_t): apr_status_t
```

*Binds the C function `apr_thread_rwlock_destroy`.*

### <a id="apr_thread_rwlock_pool_get"></a>`apr_thread_rwlock_pool_get`

```ec
fn apr_thread_rwlock_pool_get(thethread_rwlock: @apr_thread_rwlock_t): @apr_pool_t
```

*Binds the C function `apr_thread_rwlock_pool_get`.*
