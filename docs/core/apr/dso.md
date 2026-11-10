# `dso.ec`

## Types

### <a id="apr_dso_handle_t"></a>`apr_dso_handle_t`

```ec
declare rec apr_dso_handle_t
```

### <a id="apr_dso_handle_sym_t"></a>`apr_dso_handle_sym_t`

```ec
declare type apr_dso_handle_sym_t as @Any
```

## Functions

### <a id="apr_dso_load"></a>`apr_dso_load`

```ec
fn apr_dso_load(out res_handle: @apr_dso_handle_t, path: CString, ctx: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_dso_load`.*

### <a id="apr_dso_unload"></a>`apr_dso_unload`

```ec
fn apr_dso_unload(handle: @apr_dso_handle_t): apr_status_t
```

*Binds the C function `apr_dso_unload`.*

### <a id="apr_dso_sym"></a>`apr_dso_sym`

```ec
fn apr_dso_sym(ressym: @apr_dso_handle_sym_t, handle: @apr_dso_handle_t, symname: CString): apr_status_t
```

*Binds the C function `apr_dso_sym`.*

### <a id="apr_dso_error"></a>`apr_dso_error`

```ec
fn apr_dso_error(dso: @apr_dso_handle_t, buf: CString, bufsize: apr_size_t): CString
```

*Binds the C function `apr_dso_error`.*
