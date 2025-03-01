use "apr_shm.h"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "core:apr/errno.ec"

declare rec apr_shm_t

declare fn apr_shm_create as (m: @@apr_shm_t, reqsize: apr_size_t, filename: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_shm_remove as (filename: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_shm_destroy as (m: @apr_shm_t): apr_status_t
declare fn apr_shm_attach as (m: @@apr_shm_t, filename: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_shm_detach as (m: @apr_shm_t): apr_status_t
declare fn apr_shm_baseaddr_get as (m: @apr_shm_t): @Any
declare fn apr_shm_size_get as (m: @apr_shm_t): apr_size_t
declare fn apr_shm_pool_get as (theshm: @apr_shm_t): @apr_pool_t
