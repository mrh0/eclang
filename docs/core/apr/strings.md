# `strings.ec`

## Functions

### <a id="apr_strnatcmp"></a>`apr_strnatcmp`

```ec
fn apr_strnatcmp(a: CString, b: CString): Int
```

*Binds the C function `apr_strnatcmp`.*

### <a id="apr_strnatcasecmp"></a>`apr_strnatcasecmp`

```ec
fn apr_strnatcasecmp(a: CString, b: CString): Int
```

*Binds the C function `apr_strnatcasecmp`.*

### <a id="apr_pstrdup"></a>`apr_pstrdup`

```ec
fn apr_pstrdup(p: @apr_pool_t, s: CString): CString
```

*Binds the C function `apr_pstrdup`.*

### <a id="apr_pstrmemdup"></a>`apr_pstrmemdup`

```ec
fn apr_pstrmemdup(p: @apr_pool_t, s: CString, n: apr_size_t): CString
```

*Binds the C function `apr_pstrmemdup`.*

### <a id="apr_pstrndup"></a>`apr_pstrndup`

```ec
fn apr_pstrndup(p: @apr_pool_t, s: CString, n: apr_size_t): CString
```

*Binds the C function `apr_pstrndup`.*

### <a id="apr_pmemdup"></a>`apr_pmemdup`

```ec
fn apr_pmemdup(p: @apr_pool_t, m: @Any, n: apr_size_t): @Any
```

*Binds the C function `apr_pmemdup`.*

### <a id="apr_pstrcat"></a>`apr_pstrcat`

```ec
fn apr_pstrcat(p: @apr_pool_t, ...): CString
```

*Binds the C function `apr_pstrcat`.*

### <a id="apr_pstrcatv"></a>`apr_pstrcatv`

```ec
fn apr_pstrcatv(p: @apr_pool_t, vec: @iovec, nvec: apr_size_t, nbytes: @apr_size_t): CString
```

*Binds the C function `apr_pstrcatv`.*

### <a id="apr_pvsprintf"></a>`apr_pvsprintf`

```ec
fn apr_pvsprintf(p: @apr_pool_t, fmt: CString, ap: va_list): CString
```

*Binds the C function `apr_pvsprintf`.*

### <a id="apr_psprintf"></a>`apr_psprintf`

```ec
fn apr_psprintf(p: @apr_pool_t, fmt: CString, ...): CString
```

*Binds the C function `apr_psprintf`.*

### <a id="apr_cpystrn"></a>`apr_cpystrn`

```ec
fn apr_cpystrn(dst: CString, src: CString, dst_size: apr_size_t): CString
```

*Binds the C function `apr_cpystrn`.*

### <a id="apr_collapse_spaces"></a>`apr_collapse_spaces`

```ec
fn apr_collapse_spaces(dest: CString, src: CString): CString
```

*Binds the C function `apr_collapse_spaces`.*

### <a id="apr_tokenize_to_argv"></a>`apr_tokenize_to_argv`

```ec
fn apr_tokenize_to_argv(arg_str: CString, out argv_out: @CString, token_context: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_tokenize_to_argv`.*

### <a id="apr_strtok"></a>`apr_strtok`

```ec
fn apr_strtok(str: CString?, sep: CString, mut last: CString): CString?
```

*Binds the C function `apr_strtok`.*

### <a id="apr_itoa"></a>`apr_itoa`

```ec
fn apr_itoa(p: @apr_pool_t, n: Int): CString
```

*Binds the C function `apr_itoa`.*

### <a id="apr_ltoa"></a>`apr_ltoa`

```ec
fn apr_ltoa(p: @apr_pool_t, n: Long): CString
```

*Binds the C function `apr_ltoa`.*

### <a id="apr_off_t_toa"></a>`apr_off_t_toa`

```ec
fn apr_off_t_toa(p: @apr_pool_t, n: apr_off_t): CString
```

*Binds the C function `apr_off_t_toa`.*

### <a id="apr_strtoff"></a>`apr_strtoff`

```ec
fn apr_strtoff(out offset: apr_off_t, buf: CString, out end: CString, base: Int): apr_status_t
```

*Binds the C function `apr_strtoff`.*

### <a id="apr_strtoi64"></a>`apr_strtoi64`

```ec
fn apr_strtoi64(buf: CString, out end: CString, base: Int): apr_int64_t
```

*Binds the C function `apr_strtoi64`.*

### <a id="apr_atoi64"></a>`apr_atoi64`

```ec
fn apr_atoi64(buf: CString): apr_int64_t
```

*Binds the C function `apr_atoi64`.*

### <a id="apr_strfsize"></a>`apr_strfsize`

```ec
fn apr_strfsize(size: apr_off_t, buf: CString): CString
```

*Binds the C function `apr_strfsize`.*
