# `getopt.ec`

## Types

### <a id="apr_getopt_err_fn_t"></a>`apr_getopt_err_fn_t`

```ec
declare rec apr_getopt_err_fn_t
```

### <a id="apr_getopt_t"></a>`apr_getopt_t`

```ec
declare rec apr_getopt_t
```

### <a id="apr_getopt_option_t"></a>`apr_getopt_option_t`

```ec
declare rec apr_getopt_option_t
```

## Functions

### <a id="apr_getopt_init"></a>`apr_getopt_init`

```ec
fn apr_getopt_init(os: @@apr_getopt_t, cont: @apr_pool_t, argc: Int, argv: @CString): apr_status_t
```

*Binds the C function `apr_getopt_init`.*

### <a id="apr_getopt"></a>`apr_getopt`

```ec
fn apr_getopt(os: @apr_getopt_t, opts: CString, option_ch: @Char, option_arg: @CString): apr_status_t
```

*Binds the C function `apr_getopt`.*

### <a id="apr_getopt_long"></a>`apr_getopt_long`

```ec
fn apr_getopt_long(os: @apr_getopt_t, opts: @apr_getopt_option_t, option_ch: @Int, option_arg: @CString): apr_status_t
```

*Binds the C function `apr_getopt_long`.*
