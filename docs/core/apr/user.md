# `user.ec`

## Types

### <a id="apr_uid_t"></a>`apr_uid_t`

```ec
declare type apr_uid_t as UInt
```

### <a id="apr_gid_t"></a>`apr_gid_t`

```ec
declare type apr_gid_t as UInt
```

## Functions

### <a id="apr_uid_current"></a>`apr_uid_current`

```ec
fn apr_uid_current(out userid: apr_uid_t, out groupid: apr_gid_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_uid_current`.*

### <a id="apr_uid_name_get"></a>`apr_uid_name_get`

```ec
fn apr_uid_name_get(out username: CString, userid: apr_uid_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_uid_name_get`.*

### <a id="apr_uid_get"></a>`apr_uid_get`

```ec
fn apr_uid_get(out userid: apr_uid_t, out groupid: apr_gid_t, username: CString, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_uid_get`.*

### <a id="apr_uid_homepath_get"></a>`apr_uid_homepath_get`

```ec
fn apr_uid_homepath_get(out dirname: CString, username: CString, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_uid_homepath_get`.*

### <a id="apr_gid_name_get"></a>`apr_gid_name_get`

```ec
fn apr_gid_name_get(out groupname: CString, groupid: apr_gid_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_gid_name_get`.*

### <a id="apr_gid_get"></a>`apr_gid_get`

```ec
fn apr_gid_get(out groupid: apr_gid_t, groupname: CString, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_gid_get`.*
