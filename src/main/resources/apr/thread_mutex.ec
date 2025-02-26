use "apr_thread_mutex.h"
use "core:apr/apr.ec"
use "core:apr/errno.ec"
use "core:apr/pools.ec"

declare val APR_THREAD_MUTEX_DEFAULT as Int
declare val APR_THREAD_MUTEX_NESTED as Int
declare val APR_THREAD_MUTEX_UNNESTED as Int

declare rec apr_thread_mutex_t

declare fn apr_thread_mutex_create as (mutex: @@apr_thread_mutex_t, flags: UInt, pool: @apr_pool_t): apr_status_t
declare fn apr_thread_mutex_lock as (mutex: @apr_thread_mutex_t): apr_status_t
declare fn apr_thread_mutex_trylock as (mutex: @apr_thread_mutex_t): apr_status_t
declare fn apr_thread_mutex_unlock as (mutex: @apr_thread_mutex_t): apr_status_t
declare fn apr_thread_mutex_destroy as (mutex: @apr_thread_mutex_t): apr_status_t
declare fn apr_thread_mutex_pool_get as (thethread_mutex: @apr_thread_mutex_t): @apr_pool_t
