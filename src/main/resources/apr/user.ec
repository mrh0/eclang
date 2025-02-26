use "apr_user.h"
use "core:apr/apr.ec"
use "core:apr/errno.ec"
use "core:apr/pools.ec"

// apr_uid_compare(left, right)   (((left) == (right)) ? APR_SUCCESS : APR_EMISMATCH)
// apr_gid_compare(left, right)   (((left) == (right)) ? APR_SUCCESS : APR_EMISMATCH)

declare type apr_uid_t as uid_t
declare type apr_gid_t as gid_t

declare fn apr_uid_current as (userid: @apr_uid_t, groupid: @apr_gid_t, p: @apr_pool_t): apr_status_t
declare fn apr_uid_name_get as (username: @CString, userid: apr_uid_t, p: @apr_pool_t): apr_status_t
declare fn apr_uid_get as (userid: @apr_uid_t, groupid: @apr_gid_t, username: CString, p: @apr_pool_t): apr_status_t
declare fn apr_uid_homepath_get as (dirname: @CString, username: CString, p: @apr_pool_t): apr_status_t
declare fn apr_gid_name_get as (groupname: @CString, groupid: apr_gid_t, p: @apr_pool_t): apr_status_t
declare fn apr_gid_get as (groupid: @apr_gid_t, groupname: CString, p: @apr_pool_t): apr_status_t
