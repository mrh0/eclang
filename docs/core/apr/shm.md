# `shm.ec`

## Types

### <a id="apr_shm_t"></a>`apr_shm_t`

```ec
declare rec apr_shm_t
```

## Functions

### <a id="apr_shm_create"></a>`apr_shm_create`

```ec
fn apr_shm_create(m: @@apr_shm_t, reqsize: apr_size_t, filename: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_shm_create`.*

### <a id="apr_shm_remove"></a>`apr_shm_remove`

```ec
fn apr_shm_remove(filename: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_shm_remove`.*

### <a id="apr_shm_destroy"></a>`apr_shm_destroy`

```ec
fn apr_shm_destroy(m: @apr_shm_t): apr_status_t
```

*Binds the C function `apr_shm_destroy`.*

### <a id="apr_shm_attach"></a>`apr_shm_attach`

```ec
fn apr_shm_attach(m: @@apr_shm_t, filename: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_shm_attach`.*

### <a id="apr_shm_detach"></a>`apr_shm_detach`

```ec
fn apr_shm_detach(m: @apr_shm_t): apr_status_t
```

*Binds the C function `apr_shm_detach`.*

### <a id="apr_shm_baseaddr_get"></a>`apr_shm_baseaddr_get`

```ec
fn apr_shm_baseaddr_get(m: @apr_shm_t): @Any
```

*Binds the C function `apr_shm_baseaddr_get`.*

### <a id="apr_shm_size_get"></a>`apr_shm_size_get`

```ec
fn apr_shm_size_get(m: @apr_shm_t): apr_size_t
```

*Binds the C function `apr_shm_size_get`.*

### <a id="apr_shm_pool_get"></a>`apr_shm_pool_get`

```ec
fn apr_shm_pool_get(theshm: @apr_shm_t): @apr_pool_t
```

*Binds the C function `apr_shm_pool_get`.*
