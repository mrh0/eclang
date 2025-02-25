use "apr_env.h"
use "core:apr/errno.ec"
use "core:apr/pools.ec"

declare fn apr_env_get as (value: @CString, envvar: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_env_set as (envvar: CString, value: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_env_delete as (envvar: CString, pool: @apr_pool_t): apr_status_t
