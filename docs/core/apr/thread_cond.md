# `thread_cond.ec`

## Types

### <a id="apr_thread_cond_t"></a>`apr_thread_cond_t`

```ec
declare rec apr_thread_cond_t
```

## Functions

### <a id="apr_thread_cond_create"></a>`apr_thread_cond_create`

```ec
fn apr_thread_cond_create(out cond: @apr_thread_cond_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_thread_cond_create`.*

### <a id="apr_thread_cond_wait"></a>`apr_thread_cond_wait`

```ec
fn apr_thread_cond_wait(cond: @apr_thread_cond_t, mutex: @apr_thread_mutex_t): apr_status_t
```

*Binds the C function `apr_thread_cond_wait`.*

### <a id="apr_thread_cond_timedwait"></a>`apr_thread_cond_timedwait`

```ec
fn apr_thread_cond_timedwait(cond: @apr_thread_cond_t, mutex: @apr_thread_mutex_t, timeout: apr_interval_time_t): apr_status_t
```

*Binds the C function `apr_thread_cond_timedwait`.*

### <a id="apr_thread_cond_signal"></a>`apr_thread_cond_signal`

```ec
fn apr_thread_cond_signal(cond: @apr_thread_cond_t): apr_status_t
```

*Binds the C function `apr_thread_cond_signal`.*

### <a id="apr_thread_cond_broadcast"></a>`apr_thread_cond_broadcast`

```ec
fn apr_thread_cond_broadcast(cond: @apr_thread_cond_t): apr_status_t
```

*Binds the C function `apr_thread_cond_broadcast`.*

### <a id="apr_thread_cond_destroy"></a>`apr_thread_cond_destroy`

```ec
fn apr_thread_cond_destroy(cond: @apr_thread_cond_t): apr_status_t
```

*Binds the C function `apr_thread_cond_destroy`.*

### <a id="apr_thread_cond_pool_get"></a>`apr_thread_cond_pool_get`

```ec
fn apr_thread_cond_pool_get(thethread_cond: @apr_thread_cond_t): @apr_pool_t
```

*Binds the C function `apr_thread_cond_pool_get`.*
