# `general.ec`

## Types

### <a id="apr_signum_t"></a>`apr_signum_t`

```ec
declare type apr_signum_t as Int
```

## Values

### <a id="APR_ASCII_BLANK"></a>`APR_ASCII_BLANK`

```ec
declare val APR_ASCII_BLANK as Char
```

### <a id="APR_ASCII_CR"></a>`APR_ASCII_CR`

```ec
declare val APR_ASCII_CR as Char
```

### <a id="APR_ASCII_LF"></a>`APR_ASCII_LF`

```ec
declare val APR_ASCII_LF as Char
```

### <a id="APR_ASCII_TAB"></a>`APR_ASCII_TAB`

```ec
declare val APR_ASCII_TAB as Char
```

## Functions

### <a id="apr_initialize"></a>`apr_initialize`

```ec
fn apr_initialize(): apr_status_t
```

*Binds the C function `apr_initialize`.*

### <a id="apr_app_initialize"></a>`apr_app_initialize`

```ec
fn apr_app_initialize(argc: @Int, argv: @@CString, env: @@CString): apr_status_t
```

*Binds the C function `apr_app_initialize`.*

### <a id="apr_terminate"></a>`apr_terminate`

```ec
fn apr_terminate()
```

*Binds the C function `apr_terminate`.*

### <a id="apr_terminate2"></a>`apr_terminate2`

```ec
fn apr_terminate2()
```

*Binds the C function `apr_terminate2`.*

### <a id="apr_generate_random_bytes"></a>`apr_generate_random_bytes`

```ec
fn apr_generate_random_bytes(buf: @UChar, length: apr_size_t): apr_status_t
```

*Binds the C function `apr_generate_random_bytes`.*
