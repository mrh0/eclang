# `lib.ec`

## Types

### <a id="apr_vformatter_buff_t"></a>`apr_vformatter_buff_t`

```ec
declare rec apr_vformatter_buff_t
```

## Values

### <a id="HUGE_STRING_LEN"></a>`HUGE_STRING_LEN`

```ec
declare val HUGE_STRING_LEN as Int
```

## Functions

### <a id="apr_isalnum"></a>`apr_isalnum`

```ec
fn apr_isalnum(c: UChar): Bool
```

*Binds the C function `apr_isalnum`.*

### <a id="apr_isalpha"></a>`apr_isalpha`

```ec
fn apr_isalpha(c: UChar): Bool
```

*Binds the C function `apr_isalpha`.*

### <a id="apr_iscntrl"></a>`apr_iscntrl`

```ec
fn apr_iscntrl(c: UChar): Bool
```

*Binds the C function `apr_iscntrl`.*

### <a id="apr_isdigit"></a>`apr_isdigit`

```ec
fn apr_isdigit(c: UChar): Bool
```

*Binds the C function `apr_isdigit`.*

### <a id="apr_isgraph"></a>`apr_isgraph`

```ec
fn apr_isgraph(c: UChar): Bool
```

*Binds the C function `apr_isgraph`.*

### <a id="apr_islower"></a>`apr_islower`

```ec
fn apr_islower(c: UChar): Bool
```

*Binds the C function `apr_islower`.*

### <a id="apr_isascii"></a>`apr_isascii`

```ec
fn apr_isascii(c: UChar): Bool
```

*Binds the C function `apr_isascii`.*

### <a id="apr_isprint"></a>`apr_isprint`

```ec
fn apr_isprint(c: UChar): Bool
```

*Binds the C function `apr_isprint`.*

### <a id="apr_ispunct"></a>`apr_ispunct`

```ec
fn apr_ispunct(c: UChar): Bool
```

*Binds the C function `apr_ispunct`.*

### <a id="apr_isspace"></a>`apr_isspace`

```ec
fn apr_isspace(c: UChar): Bool
```

*Binds the C function `apr_isspace`.*

### <a id="apr_isupper"></a>`apr_isupper`

```ec
fn apr_isupper(c: UChar): Bool
```

*Binds the C function `apr_isupper`.*

### <a id="apr_isxdigit"></a>`apr_isxdigit`

```ec
fn apr_isxdigit(c: UChar): Bool
```

*Binds the C function `apr_isxdigit`.*

### <a id="apr_tolower"></a>`apr_tolower`

```ec
fn apr_tolower(c: UChar): Int
```

*Binds the C function `apr_tolower`.*

### <a id="apr_toupper"></a>`apr_toupper`

```ec
fn apr_toupper(c: UChar): Int
```

*Binds the C function `apr_toupper`.*

### <a id="apr_filepath_name_get"></a>`apr_filepath_name_get`

```ec
fn apr_filepath_name_get(pathname: CString): CString
```

*Binds the C function `apr_filepath_name_get`.*

### <a id="apr_vformatter"></a>`apr_vformatter`

```ec
fn apr_vformatter(flush_func: (@apr_vformatter_buff_t) => Int, c: @apr_vformatter_buff_t, fmt: CString, ap: va_list): Int
```

*Binds the C function `apr_vformatter`.*

### <a id="apr_password_get"></a>`apr_password_get`

```ec
fn apr_password_get(prompt: CString, pwbuf: CString, bufsize: @apr_size_t): apr_status_t
```

*Binds the C function `apr_password_get`.*
