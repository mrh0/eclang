# `atomic.ec`

## Functions

### <a id="apr_atomic_init"></a>`apr_atomic_init`

```ec
fn apr_atomic_init(p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_atomic_init`.*

### <a id="apr_atomic_read32"></a>`apr_atomic_read32`

```ec
fn apr_atomic_read32(mem: @apr_uint32_t): apr_uint32_t
```

*Binds the C function `apr_atomic_read32`.*

### <a id="apr_atomic_set32"></a>`apr_atomic_set32`

```ec
fn apr_atomic_set32(mem: @apr_uint32_t, value: apr_uint32_t)
```

*Binds the C function `apr_atomic_set32`.*

### <a id="apr_atomic_add32"></a>`apr_atomic_add32`

```ec
fn apr_atomic_add32(mem: @apr_uint32_t, value: apr_uint32_t): apr_uint32_t
```

*Binds the C function `apr_atomic_add32`.*

### <a id="apr_atomic_sub32"></a>`apr_atomic_sub32`

```ec
fn apr_atomic_sub32(mem: @apr_uint32_t, value: apr_uint32_t)
```

*Binds the C function `apr_atomic_sub32`.*

### <a id="apr_atomic_inc32"></a>`apr_atomic_inc32`

```ec
fn apr_atomic_inc32(mem: @apr_uint32_t): apr_uint32_t
```

*Binds the C function `apr_atomic_inc32`.*

### <a id="apr_atomic_dec32"></a>`apr_atomic_dec32`

```ec
fn apr_atomic_dec32(mem: @apr_uint32_t): Int
```

*Binds the C function `apr_atomic_dec32`.*

### <a id="apr_atomic_cas32"></a>`apr_atomic_cas32`

```ec
fn apr_atomic_cas32(mem: @apr_uint32_t, with: apr_uint32_t, cmp: apr_uint32_t): apr_uint32_t
```

*Binds the C function `apr_atomic_cas32`.*

### <a id="apr_atomic_xchg32"></a>`apr_atomic_xchg32`

```ec
fn apr_atomic_xchg32(mem: @apr_uint32_t, value: apr_uint32_t): apr_uint32_t
```

*Binds the C function `apr_atomic_xchg32`.*

### <a id="apr_atomic_casptr"></a>`apr_atomic_casptr`

```ec
fn apr_atomic_casptr(mem: @@Any, with: @Any, cmp: @Any): @Any
```

*Binds the C function `apr_atomic_casptr`.*

### <a id="apr_atomic_xchgptr"></a>`apr_atomic_xchgptr`

```ec
fn apr_atomic_xchgptr(mem: @@Any, with: @Any): @Any
```

*Binds the C function `apr_atomic_xchgptr`.*
