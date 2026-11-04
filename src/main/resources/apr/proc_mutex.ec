use "apr_proc_mutex.h"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "core:apr/errno.ec"

declare enum apr_lockmech_e as
    :fcntl = APR_LOCK_FCNTL
    :flock = APR_LOCK_FLOCK
    :sysvsem = APR_LOCK_SYSVSEM
    :proc_pthread = APR_LOCK_PROC_PTHREAD
    :posixsem = APR_LOCK_POSIXSEM
    :default = APR_LOCK_DEFAULT

declare rec apr_proc_mutex_t

declare fn apr_proc_mutex_create as (mutex: @@apr_proc_mutex_t, fname: CString, mech: apr_lockmech_e, pool: @apr_pool_t): apr_status_t
declare fn apr_proc_mutex_child_init as (mutex: @@apr_proc_mutex_t, fname: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_proc_mutex_lock as (mutex: @apr_proc_mutex_t): apr_status_t
declare fn apr_proc_mutex_trylock as (mutex: @apr_proc_mutex_t): apr_status_t
declare fn apr_proc_mutex_unlock as (mutex: @apr_proc_mutex_t): apr_status_t
declare fn apr_proc_mutex_destroy as (mutex: @apr_proc_mutex_t): apr_status_t
declare fn apr_proc_mutex_cleanup as (mutex: @Any): apr_status_t
declare fn apr_proc_mutex_lockfile as (mutex: @apr_proc_mutex_t): CString
declare fn apr_proc_mutex_name as (mutex: @apr_proc_mutex_t): CString
declare fn apr_proc_mutex_defname as (): CString
declare fn apr_proc_mutex_pool_get as (theproc_mutex: @apr_proc_mutex_t): @apr_pool_t
