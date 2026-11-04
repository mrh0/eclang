# `portable.ec`

## Types

### <a id="apr_os_global_mutex_t"></a>`apr_os_global_mutex_t`

```ec
declare rec apr_os_global_mutex_t
```

### <a id="apr_os_file_t"></a>`apr_os_file_t`

```ec
declare type apr_os_file_t as Int
```

### <a id="apr_os_dir_t"></a>`apr_os_dir_t`

```ec
declare rec apr_os_dir_t
```

### <a id="apr_os_sock_t"></a>`apr_os_sock_t`

```ec
declare type apr_os_sock_t as Int
```

### <a id="apr_os_proc_mutex_t"></a>`apr_os_proc_mutex_t`

```ec
declare rec apr_os_proc_mutex_t
```

### <a id="apr_os_thread_t"></a>`apr_os_thread_t`

```ec
declare rec apr_os_thread_t
```

### <a id="apr_os_threadkey_t"></a>`apr_os_threadkey_t`

```ec
declare rec apr_os_threadkey_t
```

### <a id="apr_os_proc_t"></a>`apr_os_proc_t`

```ec
declare type apr_os_proc_t as Int
```

### <a id="apr_os_imp_time_t"></a>`apr_os_imp_time_t`

```ec
declare rec apr_os_imp_time_t
```

### <a id="apr_os_exp_time_t"></a>`apr_os_exp_time_t`

```ec
declare rec apr_os_exp_time_t
```

### <a id="apr_os_dso_handle_t"></a>`apr_os_dso_handle_t`

```ec
declare type apr_os_dso_handle_t as @Any
```

### <a id="apr_os_shm_t"></a>`apr_os_shm_t`

```ec
declare type apr_os_shm_t as @Any
```

### <a id="apr_os_sock_info_t"></a>`apr_os_sock_info_t`

```ec
declare rec apr_os_sock_info_t
```

## Functions

### <a id="apr_os_file_get"></a>`apr_os_file_get`

```ec
fn apr_os_file_get(thefile: @apr_os_file_t, file: @apr_file_t): apr_status_t
```

*Binds the C function `apr_os_file_get`.*

### <a id="apr_os_dir_get"></a>`apr_os_dir_get`

```ec
fn apr_os_dir_get(thedir: @@apr_os_dir_t, dir: @apr_dir_t): apr_status_t
```

*Binds the C function `apr_os_dir_get`.*

### <a id="apr_os_sock_get"></a>`apr_os_sock_get`

```ec
fn apr_os_sock_get(thesock: @apr_os_sock_t, sock: @apr_socket_t): apr_status_t
```

*Binds the C function `apr_os_sock_get`.*

### <a id="apr_os_global_mutex_get"></a>`apr_os_global_mutex_get`

```ec
fn apr_os_global_mutex_get(ospmutex: @apr_os_global_mutex_t, pmutex: @apr_global_mutex_t): apr_status_t
```

*Binds the C function `apr_os_global_mutex_get`.*

### <a id="apr_os_proc_mutex_get"></a>`apr_os_proc_mutex_get`

```ec
fn apr_os_proc_mutex_get(ospmutex: @apr_os_proc_mutex_t, pmutex: @apr_proc_mutex_t): apr_status_t
```

*Binds the C function `apr_os_proc_mutex_get`.*

### <a id="apr_os_exp_time_get"></a>`apr_os_exp_time_get`

```ec
fn apr_os_exp_time_get(ostime: @@apr_os_exp_time_t, aprtime: @apr_time_exp_t): apr_status_t
```

*Binds the C function `apr_os_exp_time_get`.*

### <a id="apr_os_imp_time_get"></a>`apr_os_imp_time_get`

```ec
fn apr_os_imp_time_get(ostime: @@apr_os_imp_time_t, aprtime: @apr_time_t): apr_status_t
```

*Binds the C function `apr_os_imp_time_get`.*

### <a id="apr_os_shm_get"></a>`apr_os_shm_get`

```ec
fn apr_os_shm_get(osshm: @apr_os_shm_t, shm: @apr_shm_t): apr_status_t
```

*Binds the C function `apr_os_shm_get`.*

### <a id="apr_os_thread_get"></a>`apr_os_thread_get`

```ec
fn apr_os_thread_get(thethd: @@apr_os_thread_t, thd: @apr_thread_t): apr_status_t
```

*Binds the C function `apr_os_thread_get`.*

### <a id="apr_os_threadkey_get"></a>`apr_os_threadkey_get`

```ec
fn apr_os_threadkey_get(thekey: @apr_os_threadkey_t, key: @apr_threadkey_t): apr_status_t
```

*Binds the C function `apr_os_threadkey_get`.*

### <a id="apr_os_thread_put"></a>`apr_os_thread_put`

```ec
fn apr_os_thread_put(thd: @@apr_thread_t, thethd: @apr_os_thread_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_thread_put`.*

### <a id="apr_os_threadkey_put"></a>`apr_os_threadkey_put`

```ec
fn apr_os_threadkey_put(key: @@apr_threadkey_t, thekey: @apr_os_threadkey_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_threadkey_put`.*

### <a id="apr_os_thread_current"></a>`apr_os_thread_current`

```ec
fn apr_os_thread_current(): apr_os_thread_t
```

*Binds the C function `apr_os_thread_current`.*

### <a id="apr_os_thread_equal"></a>`apr_os_thread_equal`

```ec
fn apr_os_thread_equal(tid1: apr_os_thread_t, tid2: apr_os_thread_t): Int
```

*Binds the C function `apr_os_thread_equal`.*

### <a id="apr_os_file_put"></a>`apr_os_file_put`

```ec
fn apr_os_file_put(file: @@apr_file_t, thefile: @apr_os_file_t, flags: apr_int32_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_file_put`.*

### <a id="apr_os_pipe_put"></a>`apr_os_pipe_put`

```ec
fn apr_os_pipe_put(file: @@apr_file_t, thefile: @apr_os_file_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_pipe_put`.*

### <a id="apr_os_pipe_put_ex"></a>`apr_os_pipe_put_ex`

```ec
fn apr_os_pipe_put_ex(file: @@apr_file_t, thefile: @apr_os_file_t, register_cleanup: Int, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_pipe_put_ex`.*

### <a id="apr_os_dir_put"></a>`apr_os_dir_put`

```ec
fn apr_os_dir_put(dir: @@apr_dir_t, thedir: @apr_os_dir_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_dir_put`.*

### <a id="apr_os_sock_put"></a>`apr_os_sock_put`

```ec
fn apr_os_sock_put(sock: @@apr_socket_t, thesock: @apr_os_sock_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_sock_put`.*

### <a id="apr_os_sock_make"></a>`apr_os_sock_make`

```ec
fn apr_os_sock_make(apr_sock: @@apr_socket_t, os_sock_info: @apr_os_sock_info_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_sock_make`.*

### <a id="apr_os_proc_mutex_put"></a>`apr_os_proc_mutex_put`

```ec
fn apr_os_proc_mutex_put(pmutex: @@apr_proc_mutex_t, ospmutex: @apr_os_proc_mutex_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_proc_mutex_put`.*

### <a id="apr_os_imp_time_put"></a>`apr_os_imp_time_put`

```ec
fn apr_os_imp_time_put(aprtime: @apr_time_t, ostime: @@apr_os_imp_time_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_imp_time_put`.*

### <a id="apr_os_exp_time_put"></a>`apr_os_exp_time_put`

```ec
fn apr_os_exp_time_put(aprtime: @apr_time_exp_t, ostime: @@apr_os_exp_time_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_exp_time_put`.*

### <a id="apr_os_shm_put"></a>`apr_os_shm_put`

```ec
fn apr_os_shm_put(shm: @@apr_shm_t, osshm: @apr_os_shm_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_shm_put`.*

### <a id="apr_os_dso_handle_put"></a>`apr_os_dso_handle_put`

```ec
fn apr_os_dso_handle_put(dso: @@apr_dso_handle_t, thedso: apr_os_dso_handle_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_os_dso_handle_put`.*

### <a id="apr_os_dso_handle_get"></a>`apr_os_dso_handle_get`

```ec
fn apr_os_dso_handle_get(dso: @apr_os_dso_handle_t, aprdso: @apr_dso_handle_t): apr_status_t
```

*Binds the C function `apr_os_dso_handle_get`.*

### <a id="apr_os_default_encoding"></a>`apr_os_default_encoding`

```ec
fn apr_os_default_encoding(pool: @apr_pool_t): CString
```

*Binds the C function `apr_os_default_encoding`.*

### <a id="apr_os_locale_encoding"></a>`apr_os_locale_encoding`

```ec
fn apr_os_locale_encoding(pool: @apr_pool_t): CString
```

*Binds the C function `apr_os_locale_encoding`.*
