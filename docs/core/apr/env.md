# `env.ec`

## Functions

### <a id="apr_env_get"></a>`apr_env_get`

```ec
fn apr_env_get(value: @CString, envvar: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_env_get`.*

### <a id="apr_env_set"></a>`apr_env_set`

```ec
fn apr_env_set(envvar: CString, value: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_env_set`.*

### <a id="apr_env_delete"></a>`apr_env_delete`

```ec
fn apr_env_delete(envvar: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_env_delete`.*
