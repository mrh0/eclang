# `random.ec`

## Types

### <a id="apr_crypto_hash_t"></a>`apr_crypto_hash_t`

```ec
declare rec apr_crypto_hash_t
```

### <a id="apr_crypto_hash_init_t"></a>`apr_crypto_hash_init_t`

```ec
declare type apr_crypto_hash_init_t as (hash: @apr_crypto_hash_t) => None
```

### <a id="apr_crypto_hash_add_t"></a>`apr_crypto_hash_add_t`

```ec
declare type apr_crypto_hash_add_t as (hash: @apr_crypto_hash_t, data: @Any, bytes: apr_size_t) => None
```

### <a id="apr_crypto_hash_finish_t"></a>`apr_crypto_hash_finish_t`

```ec
declare type apr_crypto_hash_finish_t as (hash: @apr_crypto_hash_t, result: @UChar) => None
```

### <a id="apr_random_t"></a>`apr_random_t`

```ec
declare rec apr_random_t
```

## Functions

### <a id="apr_crypto_sha256_new"></a>`apr_crypto_sha256_new`

```ec
fn apr_crypto_sha256_new(p: @apr_pool_t): @apr_crypto_hash_t
```

*Binds the C function `apr_crypto_sha256_new`.*

### <a id="apr_random_init"></a>`apr_random_init`

```ec
fn apr_random_init(g: @apr_random_t, p: @apr_pool_t, pool_hash: @apr_crypto_hash_t, key_hash: @apr_crypto_hash_t, prng_hash: @apr_crypto_hash_t)
```

*Binds the C function `apr_random_init`.*

### <a id="apr_random_standard_new"></a>`apr_random_standard_new`

```ec
fn apr_random_standard_new(p: @apr_pool_t): @apr_random_t
```

*Binds the C function `apr_random_standard_new`.*

### <a id="apr_random_add_entropy"></a>`apr_random_add_entropy`

```ec
fn apr_random_add_entropy(g: @apr_random_t, entropy_: @Any, bytes: apr_size_t)
```

*Binds the C function `apr_random_add_entropy`.*

### <a id="apr_random_insecure_bytes"></a>`apr_random_insecure_bytes`

```ec
fn apr_random_insecure_bytes(g: @apr_random_t, random: @Any, bytes: apr_size_t): apr_status_t
```

*Binds the C function `apr_random_insecure_bytes`.*

### <a id="apr_random_secure_bytes"></a>`apr_random_secure_bytes`

```ec
fn apr_random_secure_bytes(g: @apr_random_t, random: @Any, bytes: apr_size_t): apr_status_t
```

*Binds the C function `apr_random_secure_bytes`.*

### <a id="apr_random_barrier"></a>`apr_random_barrier`

```ec
fn apr_random_barrier(g: @apr_random_t)
```

*Binds the C function `apr_random_barrier`.*

### <a id="apr_random_secure_ready"></a>`apr_random_secure_ready`

```ec
fn apr_random_secure_ready(r: @apr_random_t): apr_status_t
```

*Binds the C function `apr_random_secure_ready`.*

### <a id="apr_random_insecure_ready"></a>`apr_random_insecure_ready`

```ec
fn apr_random_insecure_ready(r: @apr_random_t): apr_status_t
```

*Binds the C function `apr_random_insecure_ready`.*

### <a id="apr_random_after_fork"></a>`apr_random_after_fork`

```ec
fn apr_random_after_fork(proc: @apr_proc_t)
```

*Binds the C function `apr_random_after_fork`.*
