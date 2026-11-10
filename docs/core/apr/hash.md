# `hash.ec`

## Types

### <a id="apr_hash_t"></a>`apr_hash_t`

```ec
declare rec apr_hash_t
```

### <a id="apr_hash_index_t"></a>`apr_hash_index_t`

```ec
declare rec apr_hash_index_t
```

### <a id="apr_hashfunc_t"></a>`apr_hashfunc_t`

```ec
declare type apr_hashfunc_t as (CString, @apr_ssize_t) => UInt
```

### <a id="apr_hash_do_callback_fn_t"></a>`apr_hash_do_callback_fn_t`

```ec
declare type apr_hash_do_callback_fn_t as (@Any, @Any, apr_ssize_t, @Any) => Int
```

## Values

### <a id="APR_HASH_KEY_STRING"></a>`APR_HASH_KEY_STRING`

```ec
declare val APR_HASH_KEY_STRING as apr_ssize_t
```

## Functions

### <a id="apr_hashfunc_default"></a>`apr_hashfunc_default`

```ec
fn apr_hashfunc_default(key: CString, klen: @apr_ssize_t): UInt
```

*Binds the C function `apr_hashfunc_default`.*

### <a id="apr_hash_make"></a>`apr_hash_make`

```ec
fn apr_hash_make(pool: @apr_pool_t): @apr_hash_t
```

*Binds the C function `apr_hash_make`.*

### <a id="apr_hash_make_custom"></a>`apr_hash_make_custom`

```ec
fn apr_hash_make_custom(pool: @apr_pool_t, hash_func: apr_hashfunc_t): @apr_hash_t
```

*Binds the C function `apr_hash_make_custom`.*

### <a id="apr_hash_copy"></a>`apr_hash_copy`

```ec
fn apr_hash_copy(pool: @apr_pool_t, h: @apr_hash_t): @apr_hash_t
```

*Binds the C function `apr_hash_copy`.*

### <a id="apr_hash_set"></a>`apr_hash_set`

```ec
fn apr_hash_set(ht: @apr_hash_t, kept key: @Any, klen: apr_ssize_t, kept value: @Any)
```

*Binds the C function `apr_hash_set`.*

### <a id="apr_hash_get"></a>`apr_hash_get`

```ec
fn apr_hash_get(ht: @apr_hash_t, key: @Any, klen: apr_ssize_t): @Any?
```

*Binds the C function `apr_hash_get`.*

### <a id="apr_hash_first"></a>`apr_hash_first`

```ec
fn apr_hash_first(p: @apr_pool_t, ht: @apr_hash_t): @apr_hash_index_t?
```

*Binds the C function `apr_hash_first`.*

### <a id="apr_hash_next"></a>`apr_hash_next`

```ec
fn apr_hash_next(hi: @apr_hash_index_t): @apr_hash_index_t?
```

*Binds the C function `apr_hash_next`.*

### <a id="apr_hash_this"></a>`apr_hash_this`

```ec
fn apr_hash_this(hi: @apr_hash_index_t, out key: @Any, out klen: apr_ssize_t, out value: @Any)
```

*Binds the C function `apr_hash_this`.*

### <a id="apr_hash_count"></a>`apr_hash_count`

```ec
fn apr_hash_count(ht: @apr_hash_t): UInt
```

*Binds the C function `apr_hash_count`.*

### <a id="apr_hash_clear"></a>`apr_hash_clear`

```ec
fn apr_hash_clear(ht: @apr_hash_t)
```

*Binds the C function `apr_hash_clear`.*

### <a id="apr_hash_overlay"></a>`apr_hash_overlay`

```ec
fn apr_hash_overlay(p: @apr_pool_t, overlay: @apr_hash_t, base: @apr_hash_t): @apr_hash_t
```

*Binds the C function `apr_hash_overlay`.*

### <a id="apr_hash_merge"></a>`apr_hash_merge`

```ec
fn apr_hash_merge(p: @apr_pool_t, h1: @apr_hash_t, h2: @apr_hash_t, merger: (@apr_pool_t, @Any, apr_ssize_t, @Any, @Any, @Any) => @Any, data: @Any): @apr_hash_t
```

*Binds the C function `apr_hash_merge`.*

### <a id="apr_hash_do"></a>`apr_hash_do`

```ec
fn apr_hash_do(comp: apr_hash_do_callback_fn_t, _rec: @Any, ht: @apr_hash_t): Int
```

*Binds the C function `apr_hash_do`.*

### <a id="apr_hash_pool_get"></a>`apr_hash_pool_get`

```ec
fn apr_hash_pool_get(thehash: @apr_hash_t): @apr_pool_t
```

*Binds the C function `apr_hash_pool_get`.*
