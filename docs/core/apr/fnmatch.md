# `fnmatch.ec`

## Values

### <a id="APR_FNM_NOMATCH"></a>`APR_FNM_NOMATCH`

```ec
declare val APR_FNM_NOMATCH as Int
```

### <a id="APR_FNM_NOESCAPE"></a>`APR_FNM_NOESCAPE`

```ec
declare val APR_FNM_NOESCAPE as Int
```

### <a id="APR_FNM_PATHNAME"></a>`APR_FNM_PATHNAME`

```ec
declare val APR_FNM_PATHNAME as Int
```

### <a id="APR_FNM_PERIOD"></a>`APR_FNM_PERIOD`

```ec
declare val APR_FNM_PERIOD as Int
```

### <a id="APR_FNM_CASE_BLIND"></a>`APR_FNM_CASE_BLIND`

```ec
declare val APR_FNM_CASE_BLIND as Int
```

## Functions

### <a id="apr_fnmatch"></a>`apr_fnmatch`

```ec
fn apr_fnmatch(pattern: CString, strings: CString, flags: Int): apr_status_t
```

*Binds the C function `apr_fnmatch`.*

### <a id="apr_fnmatch_test"></a>`apr_fnmatch_test`

```ec
fn apr_fnmatch_test(pattern: CString): Int
```

*Binds the C function `apr_fnmatch_test`.*

### <a id="apr_match_glob"></a>`apr_match_glob`

```ec
fn apr_match_glob(pattern: CString, out result: @apr_array_header_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_match_glob`.*
