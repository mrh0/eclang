use "apr_portable.h"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "core:apr/thread_proc.ec"
use "core:apr/file_io.ec"
use "core:apr/network_io.ec"
use "core:apr/errno.ec"
use "core:apr/global_mutex.ec"
use "core:apr/proc_mutex.ec"
use "core:apr/time.ec"
use "core:apr/dso.ec"
use "core:apr/shm.ec"
use "dirent.ec"
use "fcntl.ec"
use "pthread.ec"

declare type apr_os_global_mutex_t as apr_os_proc_mutex_t
declare type apr_os_global_mutex_get as apr_os_proc_mutex_get

declare type apr_os_file_t as Int
declare type apr_os_dir_t as DIR
declare type apr_os_sock_t as Int
declare rec apr_os_proc_mutex_t
declare type apr_os_thread_t as pthread_t
declare type apr_os_threadkey_t as pthread_key_t
declare type apr_os_proc_t as pid_t
declare type apr_os_imp_time_t as timeval
declare type apr_os_exp_time_t as tm
declare type apr_os_dso_handle_t as @Any
declare type apr_os_shm_t as @Any
declare rec apr_os_sock_info_t

declare fn apr_os_file_get as (thefile: @apr_os_file_t, file: @apr_file_t): apr_status_t
declare fn apr_os_dir_get as (thedir: @@apr_os_dir_t, dir: @apr_dir_t): apr_status_t
declare fn apr_os_sock_get as (thesock: @apr_os_sock_t, sock: @apr_socket_t): apr_status_t
declare fn apr_os_proc_mutex_get as (ospmutex: @apr_os_proc_mutex_t, pmutex: @apr_proc_mutex_t): apr_status_t
declare fn apr_os_exp_time_get as (ostime: @@apr_os_exp_time_t, aprtime: @apr_time_exp_t): apr_status_t
declare fn apr_os_imp_time_get as (ostime: @@apr_os_imp_time_t, aprtime: @apr_time_t): apr_status_t
declare fn apr_os_shm_get as (osshm: @apr_os_shm_t, shm: @apr_shm_t): apr_status_t
declare fn apr_os_thread_get as (thethd: @@apr_os_thread_t, thd: @apr_thread_t): apr_status_t
declare fn apr_os_threadkey_get as (thekey: @apr_os_threadkey_t, key: @apr_threadkey_t): apr_status_t
declare fn apr_os_thread_put as (thd: @@apr_thread_t, thethd: @apr_os_thread_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_threadkey_put as (key: @@apr_threadkey_t, thekey: @apr_os_threadkey_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_thread_current as (): apr_os_thread_t
declare fn apr_os_thread_equal as (tid1: apr_os_thread_t, tid2: apr_os_thread_t): Int
declare fn apr_os_file_put as (file: @@apr_file_t, thefile: @apr_os_file_t, flags: apr_int32_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_pipe_put as (file: @@apr_file_t, thefile: @apr_os_file_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_pipe_put_ex as (file: @@apr_file_t, thefile: @apr_os_file_t, register_cleanup: Int, cont: @apr_pool_t): apr_status_t
declare fn apr_os_dir_put as (dir: @@apr_dir_t, thedir: @apr_os_dir_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_sock_put as (sock: @@apr_socket_t, thesock: @apr_os_sock_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_sock_make as (apr_sock: @@apr_socket_t, os_sock_info: @apr_os_sock_info_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_proc_mutex_put as (pmutex: @@apr_proc_mutex_t, ospmutex: @apr_os_proc_mutex_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_imp_time_put as (aprtime: @apr_time_t, ostime: @@apr_os_imp_time_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_exp_time_put as (aprtime: @apr_time_exp_t, ostime: @@apr_os_exp_time_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_shm_put as (shm: @@apr_shm_t, osshm: @apr_os_shm_t, cont: @apr_pool_t): apr_status_t
declare fn apr_os_dso_handle_put as (dso: @@apr_dso_handle_t, thedso: apr_os_dso_handle_t, pool: @apr_pool_t): apr_status_t
declare fn apr_os_dso_handle_get as (dso: @apr_os_dso_handle_t, aprdso: @apr_dso_handle_t): apr_status_t
declare fn apr_os_default_encoding as (pool: @apr_pool_t): CString
declare fn apr_os_locale_encoding as (pool: @apr_pool_t): CString
