# `file_io.ec`

## Types

### <a id="apr_fileattrs_t"></a>`apr_fileattrs_t`

```ec
declare type apr_fileattrs_t as apr_uint32_t
```

### <a id="apr_seek_where_t"></a>`apr_seek_where_t`

```ec
declare type apr_seek_where_t as Int
```

### <a id="apr_file_t"></a>`apr_file_t`

```ec
declare rec apr_file_t
```

## Values

### <a id="APR_WANT_STDI"></a>`APR_WANT_STDI`

```ec
declare val APR_WANT_STDI as Int
```

### <a id="APR_WANT_IOVE"></a>`APR_WANT_IOVE`

```ec
declare val APR_WANT_IOVE as Int
```

### <a id="APR_FOPEN_READ"></a>`APR_FOPEN_READ`

```ec
declare val APR_FOPEN_READ as Int
```

### <a id="APR_FOPEN_WRITE"></a>`APR_FOPEN_WRITE`

```ec
declare val APR_FOPEN_WRITE as Int
```

### <a id="APR_FOPEN_CREATE"></a>`APR_FOPEN_CREATE`

```ec
declare val APR_FOPEN_CREATE as Int
```

### <a id="APR_FOPEN_APPEND"></a>`APR_FOPEN_APPEND`

```ec
declare val APR_FOPEN_APPEND as Int
```

### <a id="APR_FOPEN_TRUNCATE"></a>`APR_FOPEN_TRUNCATE`

```ec
declare val APR_FOPEN_TRUNCATE as Int
```

### <a id="APR_FOPEN_BINARY"></a>`APR_FOPEN_BINARY`

```ec
declare val APR_FOPEN_BINARY as Int
```

### <a id="APR_FOPEN_EXCL"></a>`APR_FOPEN_EXCL`

```ec
declare val APR_FOPEN_EXCL as Int
```

### <a id="APR_FOPEN_BUFFERED"></a>`APR_FOPEN_BUFFERED`

```ec
declare val APR_FOPEN_BUFFERED as Int
```

### <a id="APR_FOPEN_DELONCLOSE"></a>`APR_FOPEN_DELONCLOSE`

```ec
declare val APR_FOPEN_DELONCLOSE as Int
```

### <a id="APR_FOPEN_XTHREAD"></a>`APR_FOPEN_XTHREAD`

```ec
declare val APR_FOPEN_XTHREAD as Int
```

### <a id="APR_FOPEN_SHARELOCK"></a>`APR_FOPEN_SHARELOCK`

```ec
declare val APR_FOPEN_SHARELOCK as Int
```

### <a id="APR_FOPEN_NOCLEANUP"></a>`APR_FOPEN_NOCLEANUP`

```ec
declare val APR_FOPEN_NOCLEANUP as Int
```

### <a id="APR_FOPEN_SENDFILE_ENABLED"></a>`APR_FOPEN_SENDFILE_ENABLED`

```ec
declare val APR_FOPEN_SENDFILE_ENABLED as Int
```

### <a id="APR_FOPEN_LARGEFILE"></a>`APR_FOPEN_LARGEFILE`

```ec
declare val APR_FOPEN_LARGEFILE as Int
```

### <a id="APR_FOPEN_SPARSE"></a>`APR_FOPEN_SPARSE`

```ec
declare val APR_FOPEN_SPARSE as Int
```

### <a id="APR_READ"></a>`APR_READ`

```ec
declare val APR_READ as Int
```

### <a id="APR_WRITE"></a>`APR_WRITE`

```ec
declare val APR_WRITE as Int
```

### <a id="APR_CREATE"></a>`APR_CREATE`

```ec
declare val APR_CREATE as Int
```

### <a id="APR_APPEND"></a>`APR_APPEND`

```ec
declare val APR_APPEND as Int
```

### <a id="APR_TRUNCATE"></a>`APR_TRUNCATE`

```ec
declare val APR_TRUNCATE as Int
```

### <a id="APR_BINARY"></a>`APR_BINARY`

```ec
declare val APR_BINARY as Int
```

### <a id="APR_EXCL"></a>`APR_EXCL`

```ec
declare val APR_EXCL as Int
```

### <a id="APR_BUFFERED"></a>`APR_BUFFERED`

```ec
declare val APR_BUFFERED as Int
```

### <a id="APR_DELONCLOSE"></a>`APR_DELONCLOSE`

```ec
declare val APR_DELONCLOSE as Int
```

### <a id="APR_XTHREAD"></a>`APR_XTHREAD`

```ec
declare val APR_XTHREAD as Int
```

### <a id="APR_SHARELOCK"></a>`APR_SHARELOCK`

```ec
declare val APR_SHARELOCK as Int
```

### <a id="APR_FILE_NOCLEANUP"></a>`APR_FILE_NOCLEANUP`

```ec
declare val APR_FILE_NOCLEANUP as Int
```

### <a id="APR_SENDFILE_ENABLED"></a>`APR_SENDFILE_ENABLED`

```ec
declare val APR_SENDFILE_ENABLED as Int
```

### <a id="APR_LARGEFILE"></a>`APR_LARGEFILE`

```ec
declare val APR_LARGEFILE as Int
```

### <a id="APR_SET"></a>`APR_SET`

```ec
declare val APR_SET as Int
```

### <a id="APR_CUR"></a>`APR_CUR`

```ec
declare val APR_CUR as Int
```

### <a id="APR_END"></a>`APR_END`

```ec
declare val APR_END as Int
```

### <a id="APR_FILE_ATTR_READONLY"></a>`APR_FILE_ATTR_READONLY`

```ec
declare val APR_FILE_ATTR_READONLY as Int
```

### <a id="APR_FILE_ATTR_EXECUTABLE"></a>`APR_FILE_ATTR_EXECUTABLE`

```ec
declare val APR_FILE_ATTR_EXECUTABLE as Int
```

### <a id="APR_FILE_ATTR_HIDDEN"></a>`APR_FILE_ATTR_HIDDEN`

```ec
declare val APR_FILE_ATTR_HIDDEN as Int
```

### <a id="APR_MAX_IOVEC_SIZE"></a>`APR_MAX_IOVEC_SIZE`

```ec
declare val APR_MAX_IOVEC_SIZE as Int
```

### <a id="APR_FLOCK_SHARED"></a>`APR_FLOCK_SHARED`

```ec
declare val APR_FLOCK_SHARED as Int
```

### <a id="APR_FLOCK_EXCLUSIVE"></a>`APR_FLOCK_EXCLUSIVE`

```ec
declare val APR_FLOCK_EXCLUSIVE as Int
```

### <a id="APR_FLOCK_TYPEMASK"></a>`APR_FLOCK_TYPEMASK`

```ec
declare val APR_FLOCK_TYPEMASK as Int
```

### <a id="APR_FLOCK_NONBLOCK"></a>`APR_FLOCK_NONBLOCK`

```ec
declare val APR_FLOCK_NONBLOCK as Int
```

## Functions

### <a id="apr_file_open"></a>`apr_file_open`

```ec
fn apr_file_open(newf: @@apr_file_t, fname: CString, flag: apr_int32_t, perm: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_open`.*

### <a id="apr_file_close"></a>`apr_file_close`

```ec
fn apr_file_close(file: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_close`.*

### <a id="apr_file_remove"></a>`apr_file_remove`

```ec
fn apr_file_remove(path: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_remove`.*

### <a id="apr_file_rename"></a>`apr_file_rename`

```ec
fn apr_file_rename(from_path: CString, to_path: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_rename`.*

### <a id="apr_file_link"></a>`apr_file_link`

```ec
fn apr_file_link(from_path: CString, to_path: CString): apr_status_t
```

*Binds the C function `apr_file_link`.*

### <a id="apr_file_copy"></a>`apr_file_copy`

```ec
fn apr_file_copy(from_path: CString, to_path: CString, perms: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_copy`.*

### <a id="apr_file_append"></a>`apr_file_append`

```ec
fn apr_file_append(from_path: CString, to_path: CString, perms: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_append`.*

### <a id="apr_file_eof"></a>`apr_file_eof`

```ec
fn apr_file_eof(fptr: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_eof`.*

### <a id="apr_file_open_stderr"></a>`apr_file_open_stderr`

```ec
fn apr_file_open_stderr(thefile: @@apr_file_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_open_stderr`.*

### <a id="apr_file_open_stdout"></a>`apr_file_open_stdout`

```ec
fn apr_file_open_stdout(thefile: @@apr_file_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_open_stdout`.*

### <a id="apr_file_open_stdin"></a>`apr_file_open_stdin`

```ec
fn apr_file_open_stdin(thefile: @@apr_file_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_open_stdin`.*

### <a id="apr_file_open_flags_stderr"></a>`apr_file_open_flags_stderr`

```ec
fn apr_file_open_flags_stderr(thefile: @@apr_file_t, flags: apr_int32_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_open_flags_stderr`.*

### <a id="apr_file_open_flags_stdout"></a>`apr_file_open_flags_stdout`

```ec
fn apr_file_open_flags_stdout(thefile: @@apr_file_t, flags: apr_int32_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_open_flags_stdout`.*

### <a id="apr_file_open_flags_stdin"></a>`apr_file_open_flags_stdin`

```ec
fn apr_file_open_flags_stdin(thefile: @@apr_file_t, flags: apr_int32_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_open_flags_stdin`.*

### <a id="apr_file_read"></a>`apr_file_read`

```ec
fn apr_file_read(thefile: @apr_file_t, buf: @Any, nbytes: @apr_size_t): apr_status_t
```

*Binds the C function `apr_file_read`.*

### <a id="apr_file_write"></a>`apr_file_write`

```ec
fn apr_file_write(thefile: @apr_file_t, buf: @Any, nbytes: @apr_size_t): apr_status_t
```

*Binds the C function `apr_file_write`.*

### <a id="apr_file_writev"></a>`apr_file_writev`

```ec
fn apr_file_writev(thefile: @apr_file_t, vec: @iovec, nvec: apr_size_t, nbytes: @apr_size_t): apr_status_t
```

*Binds the C function `apr_file_writev`.*

### <a id="apr_file_read_full"></a>`apr_file_read_full`

```ec
fn apr_file_read_full(thefile: @apr_file_t, buf: @Any, nbytes: apr_size_t, bytes_read: @apr_size_t): apr_status_t
```

*Binds the C function `apr_file_read_full`.*

### <a id="apr_file_write_full"></a>`apr_file_write_full`

```ec
fn apr_file_write_full(thefile: @apr_file_t, buf: @Any, nbytes: apr_size_t, bytes_written: @apr_size_t): apr_status_t
```

*Binds the C function `apr_file_write_full`.*

### <a id="apr_file_writev_full"></a>`apr_file_writev_full`

```ec
fn apr_file_writev_full(thefile: @apr_file_t, vec: @iovec, nvec: apr_size_t, nbytes: @apr_size_t): apr_status_t
```

*Binds the C function `apr_file_writev_full`.*

### <a id="apr_file_putc"></a>`apr_file_putc`

```ec
fn apr_file_putc(ch: Char, thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_putc`.*

### <a id="apr_file_getc"></a>`apr_file_getc`

```ec
fn apr_file_getc(ch: @Char, thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_getc`.*

### <a id="apr_file_ungetc"></a>`apr_file_ungetc`

```ec
fn apr_file_ungetc(ch: Char, thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_ungetc`.*

### <a id="apr_file_gets"></a>`apr_file_gets`

```ec
fn apr_file_gets(str: CString, len: Int, thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_gets`.*

### <a id="apr_file_puts"></a>`apr_file_puts`

```ec
fn apr_file_puts(str: CString, thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_puts`.*

### <a id="apr_file_flush"></a>`apr_file_flush`

```ec
fn apr_file_flush(thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_flush`.*

### <a id="apr_file_sync"></a>`apr_file_sync`

```ec
fn apr_file_sync(thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_sync`.*

### <a id="apr_file_datasync"></a>`apr_file_datasync`

```ec
fn apr_file_datasync(thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_datasync`.*

### <a id="apr_file_dup"></a>`apr_file_dup`

```ec
fn apr_file_dup(new_file: @@apr_file_t, old_file: @apr_file_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_dup`.*

### <a id="apr_file_dup2"></a>`apr_file_dup2`

```ec
fn apr_file_dup2(new_file: @apr_file_t, old_file: @apr_file_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_dup2`.*

### <a id="apr_file_setaside"></a>`apr_file_setaside`

```ec
fn apr_file_setaside(new_file: @@apr_file_t, old_file: @apr_file_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_setaside`.*

### <a id="apr_file_buffer_set"></a>`apr_file_buffer_set`

```ec
fn apr_file_buffer_set(thefile: @apr_file_t, buffer: @Any, bufsize: apr_size_t): apr_status_t
```

*Binds the C function `apr_file_buffer_set`.*

### <a id="apr_file_buffer_size_get"></a>`apr_file_buffer_size_get`

```ec
fn apr_file_buffer_size_get(thefile: @apr_file_t): apr_size_t
```

*Binds the C function `apr_file_buffer_size_get`.*

### <a id="apr_file_seek"></a>`apr_file_seek`

```ec
fn apr_file_seek(thefile: @apr_file_t, whence: apr_seek_where_t, offset: @apr_off_t): apr_status_t
```

*Binds the C function `apr_file_seek`.*

### <a id="apr_file_pipe_create"></a>`apr_file_pipe_create`

```ec
fn apr_file_pipe_create(input: @@apr_file_t, output: @@apr_file_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_pipe_create`.*

### <a id="apr_file_pipe_create_ex"></a>`apr_file_pipe_create_ex`

```ec
fn apr_file_pipe_create_ex(input: @@apr_file_t, output: @@apr_file_t, blocking: apr_int32_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_pipe_create_ex`.*

### <a id="apr_file_namedpipe_create"></a>`apr_file_namedpipe_create`

```ec
fn apr_file_namedpipe_create(filename: CString, perm: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_namedpipe_create`.*

### <a id="apr_file_pipe_timeout_get"></a>`apr_file_pipe_timeout_get`

```ec
fn apr_file_pipe_timeout_get(thepipe: @apr_file_t, timeout: @apr_interval_time_t): apr_status_t
```

*Binds the C function `apr_file_pipe_timeout_get`.*

### <a id="apr_file_pipe_timeout_set"></a>`apr_file_pipe_timeout_set`

```ec
fn apr_file_pipe_timeout_set(thepipe: @apr_file_t, timeout: apr_interval_time_t): apr_status_t
```

*Binds the C function `apr_file_pipe_timeout_set`.*

### <a id="apr_file_lock"></a>`apr_file_lock`

```ec
fn apr_file_lock(thefile: @apr_file_t, lock_type: Int): apr_status_t
```

*Binds the C function `apr_file_lock`.*

### <a id="apr_file_unlock"></a>`apr_file_unlock`

```ec
fn apr_file_unlock(thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_unlock`.*

### <a id="apr_file_name_get"></a>`apr_file_name_get`

```ec
fn apr_file_name_get(new_path: @CString, thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_name_get`.*

### <a id="apr_file_data_get"></a>`apr_file_data_get`

```ec
fn apr_file_data_get(data: @@Any, key: CString, file: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_data_get`.*

### <a id="apr_file_data_set"></a>`apr_file_data_set`

```ec
fn apr_file_data_set(file: @apr_file_t, data: @Any, key: CString, cleanup: fn (@Any): apr_status_t): apr_status_t
```

*Binds the C function `apr_file_data_set`.*

### <a id="apr_file_printf"></a>`apr_file_printf`

```ec
fn apr_file_printf(fptr: @apr_file_t, format: CString, ...): Int
```

*Binds the C function `apr_file_printf`.*

### <a id="apr_file_perms_set"></a>`apr_file_perms_set`

```ec
fn apr_file_perms_set(fname: CString, perms: apr_fileperms_t): apr_status_t
```

*Binds the C function `apr_file_perms_set`.*

### <a id="apr_file_attrs_set"></a>`apr_file_attrs_set`

```ec
fn apr_file_attrs_set(fname: CString, attributes: apr_fileattrs_t, attr_mask: apr_fileattrs_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_attrs_set`.*

### <a id="apr_file_mtime_set"></a>`apr_file_mtime_set`

```ec
fn apr_file_mtime_set(fname: CString, mtime: apr_time_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_mtime_set`.*

### <a id="apr_dir_make"></a>`apr_dir_make`

```ec
fn apr_dir_make(path: CString, perm: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_dir_make`.*

### <a id="apr_dir_make_recursive"></a>`apr_dir_make_recursive`

```ec
fn apr_dir_make_recursive(path: CString, perm: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_dir_make_recursive`.*

### <a id="apr_dir_remove"></a>`apr_dir_remove`

```ec
fn apr_dir_remove(path: CString, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_dir_remove`.*

### <a id="apr_file_info_get"></a>`apr_file_info_get`

```ec
fn apr_file_info_get(finfo: @apr_finfo_t, wanted: apr_int32_t, thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_info_get`.*

### <a id="apr_file_trunc"></a>`apr_file_trunc`

```ec
fn apr_file_trunc(fp: @apr_file_t, offset: apr_off_t): apr_status_t
```

*Binds the C function `apr_file_trunc`.*

### <a id="apr_file_flags_get"></a>`apr_file_flags_get`

```ec
fn apr_file_flags_get(f: @apr_file_t): apr_int32_t
```

*Binds the C function `apr_file_flags_get`.*

### <a id="apr_file_pool_get"></a>`apr_file_pool_get`

```ec
fn apr_file_pool_get(thefile: @apr_file_t): @apr_pool_t
```

*Binds the C function `apr_file_pool_get`.*

### <a id="apr_file_inherit_set"></a>`apr_file_inherit_set`

```ec
fn apr_file_inherit_set(thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_inherit_set`.*

### <a id="apr_file_inherit_unset"></a>`apr_file_inherit_unset`

```ec
fn apr_file_inherit_unset(thefile: @apr_file_t): apr_status_t
```

*Binds the C function `apr_file_inherit_unset`.*

### <a id="apr_file_mktemp"></a>`apr_file_mktemp`

```ec
fn apr_file_mktemp(fp: @@apr_file_t, templ: CString, flags: apr_int32_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_file_mktemp`.*

### <a id="apr_temp_dir_get"></a>`apr_temp_dir_get`

```ec
fn apr_temp_dir_get(temp_dir: @CString, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_temp_dir_get`.*
