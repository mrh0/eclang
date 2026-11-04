# `signal.ec`

## Types

### <a id="apr_sigfunc_t"></a>`apr_sigfunc_t`

```ec
declare type apr_sigfunc_t as (Int) => None
```

## Functions

### <a id="apr_signal"></a>`apr_signal`

```ec
fn apr_signal(signo: Int, func: apr_sigfunc_t): apr_sigfunc_t
```

*Binds the C function `apr_signal`.*

### <a id="apr_signal_description_get"></a>`apr_signal_description_get`

```ec
fn apr_signal_description_get(signum: Int): CString
```

*Binds the C function `apr_signal_description_get`.*

### <a id="apr_signal_init"></a>`apr_signal_init`

```ec
fn apr_signal_init(pglobal: @apr_pool_t)
```

*Binds the C function `apr_signal_init`.*

### <a id="apr_signal_block"></a>`apr_signal_block`

```ec
fn apr_signal_block(signum: Int): apr_status_t
```

*Binds the C function `apr_signal_block`.*

### <a id="apr_signal_unblock"></a>`apr_signal_unblock`

```ec
fn apr_signal_unblock(signum: Int): apr_status_t
```

*Binds the C function `apr_signal_unblock`.*
