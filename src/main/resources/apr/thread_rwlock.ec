use "apr_thread_rwlock.h"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "core:apr/errno.ec"

declare rec apr_thread_rwlock_t

declare fn apr_thread_rwlock_create as (rwlock: @@apr_thread_rwlock_t, pool: @apr_pool_t): apr_status_t
declare fn apr_thread_rwlock_rdlock as (rwlock: @apr_thread_rwlock_t): apr_status_t
declare fn apr_thread_rwlock_tryrdlock as (rwlock: @apr_thread_rwlock_t): apr_status_t
declare fn apr_thread_rwlock_wrlock as (rwlock: @apr_thread_rwlock_t): apr_status_t
declare fn apr_thread_rwlock_trywrlock as (rwlock: @apr_thread_rwlock_t): apr_status_t
declare fn apr_thread_rwlock_unlock as (rwlock: @apr_thread_rwlock_t): apr_status_t
declare fn apr_thread_rwlock_destroy as (rwlock: @apr_thread_rwlock_t): apr_status_t
declare fn apr_thread_rwlock_pool_get as (thethread_rwlock: @apr_thread_rwlock_t): @apr_pool_t
