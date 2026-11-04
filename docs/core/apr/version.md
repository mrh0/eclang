# `version.ec`

## Types

### <a id="apr_version_t"></a>`apr_version_t`

```ec
declare rec apr_version_t
```

**Fields**

- <a id="apr_version_t.major"></a>`major: Int`
- <a id="apr_version_t.minor"></a>`minor: Int`
- <a id="apr_version_t.patch"></a>`patch: Int`
- <a id="apr_version_t.is_dev"></a>`is_dev: Int`

## Values

### <a id="APR_MAJOR_VERSION"></a>`APR_MAJOR_VERSION`

```ec
declare val APR_MAJOR_VERSION as Int
```

### <a id="APR_MINOR_VERSION"></a>`APR_MINOR_VERSION`

```ec
declare val APR_MINOR_VERSION as Int
```

### <a id="APR_PATCH_VERSION"></a>`APR_PATCH_VERSION`

```ec
declare val APR_PATCH_VERSION as Int
```

### <a id="APR_VERSION_STRING"></a>`APR_VERSION_STRING`

```ec
declare val APR_VERSION_STRING as CString
```

### <a id="APR_VERSION_STRING_CSV"></a>`APR_VERSION_STRING_CSV`

```ec
declare val APR_VERSION_STRING_CSV as CString
```

## Functions

### <a id="apr_version"></a>`apr_version`

```ec
fn apr_version(pvsn: @apr_version_t)
```

*Binds the C function `apr_version`.*

### <a id="apr_version_string"></a>`apr_version_string`

```ec
fn apr_version_string(): CString
```

*Binds the C function `apr_version_string`.*
