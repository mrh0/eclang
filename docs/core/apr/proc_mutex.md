# `proc_mutex.ec`

## Types

### <a id="apr_lockmech_e"></a>`apr_lockmech_e`

```ec
declare enum apr_lockmech_e
```

**Values**

- <a id="apr_lockmech_e.:fcntl"></a>`:fcntl = APR_LOCK_FCNTL`
- <a id="apr_lockmech_e.:flock"></a>`:flock = APR_LOCK_FLOCK`
- <a id="apr_lockmech_e.:sysvsem"></a>`:sysvsem = APR_LOCK_SYSVSEM`
- <a id="apr_lockmech_e.:proc_pthread"></a>`:proc_pthread = APR_LOCK_PROC_PTHREAD`
- <a id="apr_lockmech_e.:posixsem"></a>`:posixsem = APR_LOCK_POSIXSEM`
- <a id="apr_lockmech_e.:default"></a>`:default = APR_LOCK_DEFAULT`

### <a id="apr_proc_mutex_t"></a>`apr_proc_mutex_t`

```ec
declare rec apr_proc_mutex_t
```

## Functions

### <a id="apr_proc_mutex_create"></a>`apr_proc_mutex_create`

```ec
fn apr_proc_mutex_create(mutex: @@apr_proc_mutex_t, fname: CString, mech: apr_lockmech_e, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_proc_mutex_create`.*

### <a id="apr_proc_mutex_child_init"></a>`apr_proc_mutex_child_init`

```ec
fn apr_proc_mutex_child_init(mutex: @@apr_proc_mutex_t, fname: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_proc_mutex_child_init`.*

### <a id="apr_proc_mutex_lock"></a>`apr_proc_mutex_lock`

```ec
fn apr_proc_mutex_lock(mutex: @apr_proc_mutex_t): apr_status_t
```

*Binds the C function `apr_proc_mutex_lock`.*

### <a id="apr_proc_mutex_trylock"></a>`apr_proc_mutex_trylock`

```ec
fn apr_proc_mutex_trylock(mutex: @apr_proc_mutex_t): apr_status_t
```

*Binds the C function `apr_proc_mutex_trylock`.*

### <a id="apr_proc_mutex_unlock"></a>`apr_proc_mutex_unlock`

```ec
fn apr_proc_mutex_unlock(mutex: @apr_proc_mutex_t): apr_status_t
```

*Binds the C function `apr_proc_mutex_unlock`.*

### <a id="apr_proc_mutex_destroy"></a>`apr_proc_mutex_destroy`

```ec
fn apr_proc_mutex_destroy(mutex: @apr_proc_mutex_t): apr_status_t
```

*Binds the C function `apr_proc_mutex_destroy`.*

### <a id="apr_proc_mutex_cleanup"></a>`apr_proc_mutex_cleanup`

```ec
fn apr_proc_mutex_cleanup(mutex: @Any): apr_status_t
```

*Binds the C function `apr_proc_mutex_cleanup`.*

### <a id="apr_proc_mutex_lockfile"></a>`apr_proc_mutex_lockfile`

```ec
fn apr_proc_mutex_lockfile(mutex: @apr_proc_mutex_t): CString
```

*Binds the C function `apr_proc_mutex_lockfile`.*

### <a id="apr_proc_mutex_name"></a>`apr_proc_mutex_name`

```ec
fn apr_proc_mutex_name(mutex: @apr_proc_mutex_t): CString
```

*Binds the C function `apr_proc_mutex_name`.*

### <a id="apr_proc_mutex_defname"></a>`apr_proc_mutex_defname`

```ec
fn apr_proc_mutex_defname(): CString
```

*Binds the C function `apr_proc_mutex_defname`.*

### <a id="apr_proc_mutex_pool_get"></a>`apr_proc_mutex_pool_get`

```ec
fn apr_proc_mutex_pool_get(theproc_mutex: @apr_proc_mutex_t): @apr_pool_t
```

*Binds the C function `apr_proc_mutex_pool_get`.*
