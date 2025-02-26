use "apr_thread_cond.h"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "core:apr/errno.ec"
use "core:apr/time.ec"
use "core:apr/thread_mutex.ec"

declare rec apr_thread_cond_t

declare fn apr_thread_cond_create as (cond: @@apr_thread_cond_t, pool: @apr_pool_t): apr_status_t
declare fn apr_thread_cond_wait as (cond: @apr_thread_cond_t, mutex: @apr_thread_mutex_t): apr_status_t
declare fn apr_thread_cond_timedwait as (cond: @apr_thread_cond_t, mutex: @apr_thread_mutex_t, timeout: apr_interval_time_t): apr_status_t
declare fn apr_thread_cond_signal as (cond: @apr_thread_cond_t): apr_status_t
declare fn apr_thread_cond_broadcast as (cond: @apr_thread_cond_t): apr_status_t
declare fn apr_thread_cond_destroy as (cond: @apr_thread_cond_t): apr_status_t
declare fn apr_thread_cond_pool_get as (thethread_cond: @apr_thread_cond_t): @apr_pool_t
