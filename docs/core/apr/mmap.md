# `mmap.ec`

## Types

### <a id="apr_mmap_t"></a>`apr_mmap_t`

```ec
declare rec apr_mmap_t
```

## Values

### <a id="APR_MMAP_READ"></a>`APR_MMAP_READ`

```ec
declare val APR_MMAP_READ as Int
```

### <a id="APR_MMAP_WRITE"></a>`APR_MMAP_WRITE`

```ec
declare val APR_MMAP_WRITE as Int
```

### <a id="APR_MMAP_THRESHOLD"></a>`APR_MMAP_THRESHOLD`

```ec
declare val APR_MMAP_THRESHOLD as Int
```

### <a id="APR_MMAP_LIMIT"></a>`APR_MMAP_LIMIT`

```ec
declare val APR_MMAP_LIMIT as Int
```

## Functions

### <a id="apr_mmap_create"></a>`apr_mmap_create`

```ec
fn apr_mmap_create(newmmap: @@apr_mmap_t, file: @apr_file_t, offset: apr_off_t, size: apr_size_t, flag: apr_int32_t, cntxt: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_mmap_create`.*

### <a id="apr_mmap_dup"></a>`apr_mmap_dup`

```ec
fn apr_mmap_dup(new_mmap: @@apr_mmap_t, old_mmap: @apr_mmap_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_mmap_dup`.*

### <a id="apr_mmap_delete"></a>`apr_mmap_delete`

```ec
fn apr_mmap_delete(mm: @apr_mmap_t): apr_status_t
```

*Binds the C function `apr_mmap_delete`.*

### <a id="apr_mmap_offset"></a>`apr_mmap_offset`

```ec
fn apr_mmap_offset(addr: @@Any, mm: @apr_mmap_t, offset: apr_off_t): apr_status_t
```

*Binds the C function `apr_mmap_offset`.*
