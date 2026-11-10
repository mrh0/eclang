# `file_info.ec`

## Types

### <a id="apr_dir_t"></a>`apr_dir_t`

```ec
declare rec apr_dir_t
```

### <a id="apr_fileperms_t"></a>`apr_fileperms_t`

```ec
declare type apr_fileperms_t as apr_int32_t
```

### <a id="apr_finfo_t"></a>`apr_finfo_t`

```ec
declare rec apr_finfo_t
```

**Fields**

- <a id="apr_finfo_t.valid"></a>`valid: apr_int32_t`
- <a id="apr_finfo_t.protection"></a>`protection: apr_fileperms_t`
- <a id="apr_finfo_t.filetype"></a>`filetype: Int`
- <a id="apr_finfo_t.nlink"></a>`nlink: apr_int32_t`
- <a id="apr_finfo_t.size"></a>`size: apr_off_t`
- <a id="apr_finfo_t.csize"></a>`csize: apr_off_t`
- <a id="apr_finfo_t.atime"></a>`atime: apr_time_t`
- <a id="apr_finfo_t.mtime"></a>`mtime: apr_time_t`
- <a id="apr_finfo_t.ctime"></a>`ctime: apr_time_t`
- <a id="apr_finfo_t.fname"></a>`fname: CString?`
- <a id="apr_finfo_t.name"></a>`name: CString?`

## Values

### <a id="APR_FPROT_USETID"></a>`APR_FPROT_USETID`

```ec
declare val APR_FPROT_USETID as Int
```

### <a id="APR_FPROT_UREAD"></a>`APR_FPROT_UREAD`

```ec
declare val APR_FPROT_UREAD as Int
```

### <a id="APR_FPROT_UWRITE"></a>`APR_FPROT_UWRITE`

```ec
declare val APR_FPROT_UWRITE as Int
```

### <a id="APR_FPROT_UEXECUTE"></a>`APR_FPROT_UEXECUTE`

```ec
declare val APR_FPROT_UEXECUTE as Int
```

### <a id="APR_FPROT_GSETID"></a>`APR_FPROT_GSETID`

```ec
declare val APR_FPROT_GSETID as Int
```

### <a id="APR_FPROT_GREAD"></a>`APR_FPROT_GREAD`

```ec
declare val APR_FPROT_GREAD as Int
```

### <a id="APR_FPROT_GWRITE"></a>`APR_FPROT_GWRITE`

```ec
declare val APR_FPROT_GWRITE as Int
```

### <a id="APR_FPROT_GEXECUTE"></a>`APR_FPROT_GEXECUTE`

```ec
declare val APR_FPROT_GEXECUTE as Int
```

### <a id="APR_FPROT_WSTICKY"></a>`APR_FPROT_WSTICKY`

```ec
declare val APR_FPROT_WSTICKY as Int
```

### <a id="APR_FPROT_WREAD"></a>`APR_FPROT_WREAD`

```ec
declare val APR_FPROT_WREAD as Int
```

### <a id="APR_FPROT_WWRITE"></a>`APR_FPROT_WWRITE`

```ec
declare val APR_FPROT_WWRITE as Int
```

### <a id="APR_FPROT_WEXECUTE"></a>`APR_FPROT_WEXECUTE`

```ec
declare val APR_FPROT_WEXECUTE as Int
```

### <a id="APR_FPROT_OS_DEFAULT"></a>`APR_FPROT_OS_DEFAULT`

```ec
declare val APR_FPROT_OS_DEFAULT as Int
```

### <a id="APR_FPROT_FILE_SOURCE_PERMS"></a>`APR_FPROT_FILE_SOURCE_PERMS`

```ec
declare val APR_FPROT_FILE_SOURCE_PERMS as Int
```

### <a id="APR_USETID"></a>`APR_USETID`

```ec
declare val APR_USETID as Int
```

### <a id="APR_UREAD"></a>`APR_UREAD`

```ec
declare val APR_UREAD as Int
```

### <a id="APR_UWRITE"></a>`APR_UWRITE`

```ec
declare val APR_UWRITE as Int
```

### <a id="APR_UEXECUTE"></a>`APR_UEXECUTE`

```ec
declare val APR_UEXECUTE as Int
```

### <a id="APR_GSETID"></a>`APR_GSETID`

```ec
declare val APR_GSETID as Int
```

### <a id="APR_GREAD"></a>`APR_GREAD`

```ec
declare val APR_GREAD as Int
```

### <a id="APR_GWRITE"></a>`APR_GWRITE`

```ec
declare val APR_GWRITE as Int
```

### <a id="APR_GEXECUTE"></a>`APR_GEXECUTE`

```ec
declare val APR_GEXECUTE as Int
```

### <a id="APR_WSTICKY"></a>`APR_WSTICKY`

```ec
declare val APR_WSTICKY as Int
```

### <a id="APR_WREAD"></a>`APR_WREAD`

```ec
declare val APR_WREAD as Int
```

### <a id="APR_WWRITE"></a>`APR_WWRITE`

```ec
declare val APR_WWRITE as Int
```

### <a id="APR_WEXECUTE"></a>`APR_WEXECUTE`

```ec
declare val APR_WEXECUTE as Int
```

### <a id="APR_OS_DEFAULT"></a>`APR_OS_DEFAULT`

```ec
declare val APR_OS_DEFAULT as Int
```

### <a id="APR_FILE_SOURCE_PERMS"></a>`APR_FILE_SOURCE_PERMS`

```ec
declare val APR_FILE_SOURCE_PERMS as Int
```

### <a id="APR_FINFO_LINK"></a>`APR_FINFO_LINK`

```ec
declare val APR_FINFO_LINK as Int
```

### <a id="APR_FINFO_MTIME"></a>`APR_FINFO_MTIME`

```ec
declare val APR_FINFO_MTIME as Int
```

### <a id="APR_FINFO_CTIME"></a>`APR_FINFO_CTIME`

```ec
declare val APR_FINFO_CTIME as Int
```

### <a id="APR_FINFO_ATIME"></a>`APR_FINFO_ATIME`

```ec
declare val APR_FINFO_ATIME as Int
```

### <a id="APR_FINFO_SIZE"></a>`APR_FINFO_SIZE`

```ec
declare val APR_FINFO_SIZE as Int
```

### <a id="APR_FINFO_CSIZE"></a>`APR_FINFO_CSIZE`

```ec
declare val APR_FINFO_CSIZE as Int
```

### <a id="APR_FINFO_DEV"></a>`APR_FINFO_DEV`

```ec
declare val APR_FINFO_DEV as Int
```

### <a id="APR_FINFO_INODE"></a>`APR_FINFO_INODE`

```ec
declare val APR_FINFO_INODE as Int
```

### <a id="APR_FINFO_NLINK"></a>`APR_FINFO_NLINK`

```ec
declare val APR_FINFO_NLINK as Int
```

### <a id="APR_FINFO_TYPE"></a>`APR_FINFO_TYPE`

```ec
declare val APR_FINFO_TYPE as Int
```

### <a id="APR_FINFO_USER"></a>`APR_FINFO_USER`

```ec
declare val APR_FINFO_USER as Int
```

### <a id="APR_FINFO_GROUP"></a>`APR_FINFO_GROUP`

```ec
declare val APR_FINFO_GROUP as Int
```

### <a id="APR_FINFO_UPROT"></a>`APR_FINFO_UPROT`

```ec
declare val APR_FINFO_UPROT as Int
```

### <a id="APR_FINFO_GPROT"></a>`APR_FINFO_GPROT`

```ec
declare val APR_FINFO_GPROT as Int
```

### <a id="APR_FINFO_WPROT"></a>`APR_FINFO_WPROT`

```ec
declare val APR_FINFO_WPROT as Int
```

### <a id="APR_FINFO_ICASE"></a>`APR_FINFO_ICASE`

```ec
declare val APR_FINFO_ICASE as Int
```

### <a id="APR_FINFO_NAME"></a>`APR_FINFO_NAME`

```ec
declare val APR_FINFO_NAME as Int
```

### <a id="APR_FINFO_MIN"></a>`APR_FINFO_MIN`

```ec
declare val APR_FINFO_MIN as Int
```

### <a id="APR_FINFO_IDENT"></a>`APR_FINFO_IDENT`

```ec
declare val APR_FINFO_IDENT as Int
```

### <a id="APR_FINFO_OWNER"></a>`APR_FINFO_OWNER`

```ec
declare val APR_FINFO_OWNER as Int
```

### <a id="APR_FINFO_PROT"></a>`APR_FINFO_PROT`

```ec
declare val APR_FINFO_PROT as Int
```

### <a id="APR_FINFO_NORM"></a>`APR_FINFO_NORM`

```ec
declare val APR_FINFO_NORM as Int
```

### <a id="APR_FINFO_DIRENT"></a>`APR_FINFO_DIRENT`

```ec
declare val APR_FINFO_DIRENT as Int
```

### <a id="APR_FILEPATH_NOTABOVEROOT"></a>`APR_FILEPATH_NOTABOVEROOT`

```ec
declare val APR_FILEPATH_NOTABOVEROOT as Int
```

### <a id="APR_FILEPATH_SECUREROOTTEST"></a>`APR_FILEPATH_SECUREROOTTEST`

```ec
declare val APR_FILEPATH_SECUREROOTTEST as Int
```

### <a id="APR_FILEPATH_SECUREROOT"></a>`APR_FILEPATH_SECUREROOT`

```ec
declare val APR_FILEPATH_SECUREROOT as Int
```

### <a id="APR_FILEPATH_NOTRELATIVE"></a>`APR_FILEPATH_NOTRELATIVE`

```ec
declare val APR_FILEPATH_NOTRELATIVE as Int
```

### <a id="APR_FILEPATH_NOTABSOLUTE"></a>`APR_FILEPATH_NOTABSOLUTE`

```ec
declare val APR_FILEPATH_NOTABSOLUTE as Int
```

### <a id="APR_FILEPATH_NATIVE"></a>`APR_FILEPATH_NATIVE`

```ec
declare val APR_FILEPATH_NATIVE as Int
```

### <a id="APR_FILEPATH_TRUENAME"></a>`APR_FILEPATH_TRUENAME`

```ec
declare val APR_FILEPATH_TRUENAME as Int
```

### <a id="APR_FILEPATH_ENCODING_UNKNOWN"></a>`APR_FILEPATH_ENCODING_UNKNOWN`

```ec
declare val APR_FILEPATH_ENCODING_UNKNOWN as Int
```

### <a id="APR_FILEPATH_ENCODING_LOCALE"></a>`APR_FILEPATH_ENCODING_LOCALE`

```ec
declare val APR_FILEPATH_ENCODING_LOCALE as Int
```

### <a id="APR_FILEPATH_ENCODING_UTF8"></a>`APR_FILEPATH_ENCODING_UTF8`

```ec
declare val APR_FILEPATH_ENCODING_UTF8 as Int
```

### <a id="APR_NOFILE"></a>`APR_NOFILE`

```ec
declare val APR_NOFILE as Int
```

### <a id="APR_REG"></a>`APR_REG`

```ec
declare val APR_REG as Int
```

### <a id="APR_DIR"></a>`APR_DIR`

```ec
declare val APR_DIR as Int
```

### <a id="APR_CHR"></a>`APR_CHR`

```ec
declare val APR_CHR as Int
```

### <a id="APR_BLK"></a>`APR_BLK`

```ec
declare val APR_BLK as Int
```

### <a id="APR_PIPE"></a>`APR_PIPE`

```ec
declare val APR_PIPE as Int
```

### <a id="APR_LNK"></a>`APR_LNK`

```ec
declare val APR_LNK as Int
```

### <a id="APR_SOCK"></a>`APR_SOCK`

```ec
declare val APR_SOCK as Int
```

### <a id="APR_UNKFILE"></a>`APR_UNKFILE`

```ec
declare val APR_UNKFILE as Int
```

## Functions

### <a id="apr_stat"></a>`apr_stat`

```ec
fn apr_stat(out finfo: apr_finfo_t, fname: CString, wanted: apr_int32_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_stat`.*

### <a id="apr_dir_open"></a>`apr_dir_open`

```ec
fn apr_dir_open(out new_dir: @apr_dir_t, dirname: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_dir_open`.*

### <a id="apr_dir_close"></a>`apr_dir_close`

```ec
fn apr_dir_close(thedir: @apr_dir_t): apr_status_t
```

*Binds the C function `apr_dir_close`.*

### <a id="apr_dir_read"></a>`apr_dir_read`

```ec
fn apr_dir_read(out finfo: apr_finfo_t, wanted: apr_int32_t, thedir: @apr_dir_t): apr_status_t
```

*Binds the C function `apr_dir_read`.*

### <a id="apr_dir_rewind"></a>`apr_dir_rewind`

```ec
fn apr_dir_rewind(thedir: @apr_dir_t): apr_status_t
```

*Binds the C function `apr_dir_rewind`.*

### <a id="apr_filepath_root"></a>`apr_filepath_root`

```ec
fn apr_filepath_root(out rootpath: CString, mut filepath: CString, flags: apr_int32_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_filepath_root`.*

### <a id="apr_filepath_merge"></a>`apr_filepath_merge`

```ec
fn apr_filepath_merge(out newpath: CString, rootpath: CString, addpath: CString, flags: apr_int32_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_filepath_merge`.*

### <a id="apr_filepath_list_split"></a>`apr_filepath_list_split`

```ec
fn apr_filepath_list_split(out pathelts: @apr_array_header_t, liststr: CString, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_filepath_list_split`.*

### <a id="apr_filepath_list_merge"></a>`apr_filepath_list_merge`

```ec
fn apr_filepath_list_merge(out liststr: CString, pathelts: @apr_array_header_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_filepath_list_merge`.*

### <a id="apr_filepath_get"></a>`apr_filepath_get`

```ec
fn apr_filepath_get(out path: CString, flags: apr_int32_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_filepath_get`.*

### <a id="apr_filepath_set"></a>`apr_filepath_set`

```ec
fn apr_filepath_set(path: CString, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_filepath_set`.*

### <a id="apr_filepath_encoding"></a>`apr_filepath_encoding`

```ec
fn apr_filepath_encoding(style: @Int, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_filepath_encoding`.*
