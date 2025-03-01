use "apr.h"
use "global_mutex.h"
use "core:apr/proc_mutex.ec"
use "core:apr/pools.ec"
use "core:apr/errno.ec"

declare rec apr_global_mutex_t

declare fn apr_global_mutex_create as (mutex: @@apr_global_mutex_t, fname: CString, mech: apr_lockmech_e, pool: @apr_pool_t): apr_status_t
declare fn apr_global_mutex_child_init as (mutex: @@apr_global_mutex_t, fname: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_global_mutex_lock as (mutex: @apr_global_mutex_t): apr_status_t
declare fn apr_global_mutex_trylock as (mutex: @apr_global_mutex_t): apr_status_t
declare fn apr_global_mutex_unlock as (mutex: @apr_global_mutex_t): apr_status_t
declare fn apr_global_mutex_destroy as (mutex: @apr_global_mutex_t): apr_status_t
declare fn apr_global_mutex_lockfile as (mutex: @apr_global_mutex_t): CString
declare fn apr_global_mutex_name as (mutex: @apr_global_mutex_t): CString
declare fn apr_global_mutex_pool_get as (theglobal_mutex: @apr_global_mutex_t): @apr_pool_t
