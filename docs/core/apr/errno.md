# `errno.ec`

## Types

### <a id="apr_status_t"></a>`apr_status_t`

```ec
declare type apr_status_t as Int
```

## Values

### <a id="APR_SUCCESS"></a>`APR_SUCCESS`

```ec
declare val APR_SUCCESS as Int
```

### <a id="APR_EGENERAL"></a>`APR_EGENERAL`

```ec
declare val APR_EGENERAL as Int
```

### <a id="APR_ENOMEM"></a>`APR_ENOMEM`

```ec
declare val APR_ENOMEM as Int
```

### <a id="APR_EINVAL"></a>`APR_EINVAL`

```ec
declare val APR_EINVAL as Int
```

### <a id="APR_ENOENT"></a>`APR_ENOENT`

```ec
declare val APR_ENOENT as Int
```

### <a id="APR_EEXIST"></a>`APR_EEXIST`

```ec
declare val APR_EEXIST as Int
```

### <a id="APR_EAGAIN"></a>`APR_EAGAIN`

```ec
declare val APR_EAGAIN as Int
```

### <a id="APR_TIMEUP"></a>`APR_TIMEUP`

```ec
declare val APR_TIMEUP as Int
```

### <a id="APR_INCOMPLETE"></a>`APR_INCOMPLETE`

```ec
declare val APR_INCOMPLETE as Int
```

### <a id="APR_EOF"></a>`APR_EOF`

```ec
declare val APR_EOF as Int
```

### <a id="APR_NOTFOUND"></a>`APR_NOTFOUND`

```ec
declare val APR_NOTFOUND as Int
```

### <a id="APR_EACCES"></a>`APR_EACCES`

```ec
declare val APR_EACCES as Int
```

### <a id="APR_EBUSY"></a>`APR_EBUSY`

```ec
declare val APR_EBUSY as Int
```

### <a id="APR_ENOTIMPL"></a>`APR_ENOTIMPL`

```ec
declare val APR_ENOTIMPL as Int
```

### <a id="APR_ETIMEDOUT"></a>`APR_ETIMEDOUT`

```ec
declare val APR_ETIMEDOUT as Int
```

## Functions

### <a id="apr_strerror"></a>`apr_strerror`

```ec
fn apr_strerror(statcode: apr_status_t, buf: CString, bufsize: apr_size_t): CString
```

*Binds the C function `apr_strerror`.*

### <a id="APR_STATUS_IS_ENOENT"></a>`APR_STATUS_IS_ENOENT`

```ec
fn APR_STATUS_IS_ENOENT(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_ENOENT`.*

### <a id="APR_STATUS_IS_EEXIST"></a>`APR_STATUS_IS_EEXIST`

```ec
fn APR_STATUS_IS_EEXIST(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_EEXIST`.*

### <a id="APR_STATUS_IS_EACCES"></a>`APR_STATUS_IS_EACCES`

```ec
fn APR_STATUS_IS_EACCES(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_EACCES`.*

### <a id="APR_STATUS_IS_EINVAL"></a>`APR_STATUS_IS_EINVAL`

```ec
fn APR_STATUS_IS_EINVAL(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_EINVAL`.*

### <a id="APR_STATUS_IS_ENOMEM"></a>`APR_STATUS_IS_ENOMEM`

```ec
fn APR_STATUS_IS_ENOMEM(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_ENOMEM`.*

### <a id="APR_STATUS_IS_EAGAIN"></a>`APR_STATUS_IS_EAGAIN`

```ec
fn APR_STATUS_IS_EAGAIN(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_EAGAIN`.*

### <a id="APR_STATUS_IS_EBUSY"></a>`APR_STATUS_IS_EBUSY`

```ec
fn APR_STATUS_IS_EBUSY(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_EBUSY`.*

### <a id="APR_STATUS_IS_EOF"></a>`APR_STATUS_IS_EOF`

```ec
fn APR_STATUS_IS_EOF(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_EOF`.*

### <a id="APR_STATUS_IS_TIMEUP"></a>`APR_STATUS_IS_TIMEUP`

```ec
fn APR_STATUS_IS_TIMEUP(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_TIMEUP`.*

### <a id="APR_STATUS_IS_ETIMEDOUT"></a>`APR_STATUS_IS_ETIMEDOUT`

```ec
fn APR_STATUS_IS_ETIMEDOUT(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_ETIMEDOUT`.*

### <a id="APR_STATUS_IS_ENOTIMPL"></a>`APR_STATUS_IS_ENOTIMPL`

```ec
fn APR_STATUS_IS_ENOTIMPL(s: apr_status_t): Int
```

*Binds the C function `APR_STATUS_IS_ENOTIMPL`.*
