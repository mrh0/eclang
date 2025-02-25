use "apr_file_io.h"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "core:apr/time.ec"
use "core:apr/errno.ec"
use "core:apr/file_info.ec"
use "core:apr/inherit.ec"

declare val APR_WANT_STDI as Int
declare val APR_WANT_IOVE as Int
declare val APR_FOPEN_READ as Int
declare val APR_FOPEN_WRITE as Int
declare val APR_FOPEN_CREATE as Int
declare val APR_FOPEN_APPEND as Int
declare val APR_FOPEN_TRUNCATE as Int
declare val APR_FOPEN_BINARY as Int
declare val APR_FOPEN_EXCL as Int
declare val APR_FOPEN_BUFFERED as Int
declare val APR_FOPEN_DELONCLOSE as Int
declare val APR_FOPEN_XTHREAD as Int
declare val APR_FOPEN_SHARELOCK as Int
declare val APR_FOPEN_NOCLEANUP as Int
declare val APR_FOPEN_SENDFILE_ENABLED as Int
declare val APR_FOPEN_LARGEFILE as Int
declare val APR_FOPEN_SPARSE as Int
declare val APR_READ as Int
declare val APR_WRITE as Int
declare val APR_CREATE as Int
declare val APR_APPEND as Int
declare val APR_TRUNCATE as Int
declare val APR_BINARY as Int
declare val APR_EXCL as Int
declare val APR_BUFFERED as Int
declare val APR_DELONCLOSE as Int
declare val APR_XTHREAD as Int
declare val APR_SHARELOCK as Int
declare val APR_FILE_NOCLEANUP as Int
declare val APR_SENDFILE_ENABLED as Int
declare val APR_LARGEFILE as Int
declare val APR_SET as Int
declare val APR_CUR as Int
declare val APR_END as Int
declare val APR_FILE_ATTR_READONLY as Int
declare val APR_FILE_ATTR_EXECUTABLE as Int
declare val APR_FILE_ATTR_HIDDEN as Int
declare val APR_MAX_IOVEC_SIZE as Int
declare val APR_FLOCK_SHARED as Int
declare val APR_FLOCK_EXCLUSIVE as Int
declare val APR_FLOCK_TYPEMASK as Int
declare val APR_FLOCK_NONBLOCK as Int

declare type apr_fileattrs_t as apr_uint32_t
declare type apr_seek_where_t as Int
declare rec apr_file_t

declare fn apr_file_open as (newf: @@apr_file_t, fname: CString, flag: apr_int32_t, perm: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_close as (file: @apr_file_t): apr_status_t
declare fn apr_file_remove as (path: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_file_rename as (from_path: CString, to_path: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_file_link as (from_path: CString, to_path: CString): apr_status_t
declare fn apr_file_copy as (from_path: CString, to_path: CString, perms: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_append as (from_path: CString, to_path: CString, perms: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_eof as (fptr: @apr_file_t): apr_status_t
declare fn apr_file_open_stderr as (thefile: @@apr_file_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_open_stdout as (thefile: @@apr_file_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_open_stdin as (thefile: @@apr_file_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_open_flags_stderr as (thefile: @@apr_file_t, flags: apr_int32_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_open_flags_stdout as (thefile: @@apr_file_t, flags: apr_int32_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_open_flags_stdin as (thefile: @@apr_file_t, flags: apr_int32_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_read as (thefile: @apr_file_t, buf: Ptr, nbytes: @apr_size_t): apr_status_t
declare fn apr_file_write as (thefile: @apr_file_t, buf: Ptr, nbytes: @apr_size_t): apr_status_t
declare fn apr_file_writev as (thefile: @apr_file_t, vec: @iovec, nvec: apr_size_t, nbytes: @apr_size_t): apr_status_t
declare fn apr_file_read_full as (thefile: @apr_file_t, buf: Ptr, nbytes: apr_size_t, bytes_read: @apr_size_t): apr_status_t
declare fn apr_file_write_full as (thefile: @apr_file_t, buf: Ptr, nbytes: apr_size_t, bytes_written: @apr_size_t): apr_status_t
declare fn apr_file_writev_full as (thefile: @apr_file_t, vec: @iovec, nvec: apr_size_t, nbytes: @apr_size_t): apr_status_t
declare fn apr_file_putc as (ch: Char, thefile: @apr_file_t): apr_status_t
declare fn apr_file_getc as (ch: @Char, thefile: @apr_file_t): apr_status_t
declare fn apr_file_ungetc as (ch: Char, thefile: @apr_file_t): apr_status_t
declare fn apr_file_gets as (str: CString, len: Int, thefile: @apr_file_t): apr_status_t
declare fn apr_file_puts as (str: CString, thefile: @apr_file_t): apr_status_t
declare fn apr_file_flush as (thefile: @apr_file_t): apr_status_t
declare fn apr_file_sync as (thefile: @apr_file_t): apr_status_t
declare fn apr_file_datasync as (thefile: @apr_file_t): apr_status_t
declare fn apr_file_dup as (new_file: @@apr_file_t, old_file: @apr_file_t, p: @apr_pool_t): apr_status_t
declare fn apr_file_dup2 as (new_file: @apr_file_t, old_file: @apr_file_t, p: @apr_pool_t): apr_status_t
declare fn apr_file_setaside as (new_file: @@apr_file_t, old_file: @apr_file_t, p: @apr_pool_t): apr_status_t
declare fn apr_file_buffer_set as (thefile: @apr_file_t, buffer: Ptr, bufsize: apr_size_t): apr_status_t
declare fn apr_file_buffer_size_get as (thefile: @apr_file_t): apr_size_t
declare fn apr_file_seek as (thefile: @apr_file_t, where: apr_seek_where_t, offset: @apr_off_t): apr_status_t
declare fn apr_file_pipe_create as (in: @@apr_file_t, out: @@apr_file_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_pipe_create_ex as (in: @@apr_file_t, out: @@apr_file_t, blocking: apr_int32_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_namedpipe_create as (filename: CString, perm: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_pipe_timeout_get as (thepipe: @apr_file_t, timeout: @apr_interval_time_t): apr_status_t
declare fn apr_file_pipe_timeout_set as (thepipe: @apr_file_t, timeout: apr_interval_time_t): apr_status_t
declare fn apr_file_lock as (thefile: @apr_file_t, type: Int): apr_status_t
declare fn apr_file_unlock as (thefile: @apr_file_t): apr_status_t
declare fn apr_file_name_get as (new_path: @CString, thefile: @apr_file_t): apr_status_t
declare fn apr_file_data_get as (data: @Ptr, key: CString, file: @apr_file_t): apr_status_t
declare fn apr_file_data_set as (file: @apr_file_t, data: Ptr, key: CString, cleanup: Fn(Ptr): apr_status_t): apr_status_t
declare fn apr_file_printf as (fptr: @apr_file_t, format: CString, ...): Int
declare fn apr_file_perms_set as (fname: CString, perms: apr_fileperms_t): apr_status_t
declare fn apr_file_attrs_set as (fname: CString, attributes: apr_fileattrs_t, attr_mask: apr_fileattrs_t, pool: @apr_pool_t): apr_status_t
declare fn apr_file_mtime_set as (fname: CString, mtime: apr_time_t, pool: @apr_pool_t): apr_status_t
declare fn apr_dir_make as (path: CString, perm: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
declare fn apr_dir_make_recursive as (path: CString, perm: apr_fileperms_t, pool: @apr_pool_t): apr_status_t
declare fn apr_dir_remove as (path: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_file_info_get as (finfo: @apr_finfo_t, wanted: apr_int32_t, thefile: @apr_file_t): apr_status_t
declare fn apr_file_trunc as (fp: @apr_file_t, offset: apr_off_t): apr_status_t
declare fn apr_file_flags_get as (f: @apr_file_t): apr_int32_t
declare fn apr_file_pool_get as (thefile: @apr_file_t): @apr_pool_t
declare fn apr_file_inherit_set as (thefile: @apr_file_t): apr_status_t
declare fn apr_file_inherit_unset as (thefile: @apr_file_t): apr_status_t
declare fn apr_file_mktemp as (fp: @@apr_file_t, templ: CString, flags: apr_int32_t, p: @apr_pool_t): apr_status_t
declare fn apr_temp_dir_get as (temp_dir: @CString, p: @apr_pool_t): apr_status_t
