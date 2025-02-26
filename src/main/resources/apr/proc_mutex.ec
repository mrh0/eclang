use "proc_mutex.ec"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "core:apr/errno.ec"

// enum apr_lockmech_e {
//   APR_LOCK_FCNTL, APR_LOCK_FLOCK, APR_LOCK_SYSVSEM, APR_LOCK_PROC_PTHREAD,
//   APR_LOCK_POSIXSEM, APR_LOCK_DEFAULT
// }
declare type apr_lockmech_e as Int

declare rec apr_proc_mutex_t

declare fn apr_proc_mutex_create as (mutex: @@apr_proc_mutex_t, fname: CString, mech: apr_lockmech_e, pool: @apr_pool_t): apr_status_t
declare fn apr_proc_mutex_child_init as (mutex: @@apr_proc_mutex_t, fname: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_proc_mutex_lock as (mutex: @apr_proc_mutex_t): apr_status_t
declare fn apr_proc_mutex_trylock as (mutex: @apr_proc_mutex_t): apr_status_t
declare fn apr_proc_mutex_unlock as (mutex: @apr_proc_mutex_t): apr_status_t
declare fn apr_proc_mutex_destroy as (mutex: @apr_proc_mutex_t): apr_status_t
declare fn apr_proc_mutex_cleanup as (mutex: Ptr): apr_status_t
declare fn apr_proc_mutex_lockfile as (mutex: @apr_proc_mutex_t): CString
declare fn apr_proc_mutex_name as (mutex: @apr_proc_mutex_t): CString
declare fn apr_proc_mutex_defname as (): CString
declare fn apr_proc_mutex_pool_get as (theproc_mutex: @apr_proc_mutex_t): @apr_pool_t
