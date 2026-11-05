// apr_errno.h: status codes. Every APR function that can fail returns an apr_status_t; APR_SUCCESS (0) means it didn't.
use "apr_errno.h"
use "core:apr/apr.ec"

declare type apr_status_t as Int

declare val APR_SUCCESS as Int
declare val APR_EGENERAL as Int
declare val APR_ENOMEM as Int
declare val APR_EINVAL as Int
declare val APR_ENOENT as Int
declare val APR_EEXIST as Int
declare val APR_EAGAIN as Int
declare val APR_TIMEUP as Int
declare val APR_INCOMPLETE as Int
declare val APR_EOF as Int
declare val APR_CHILD_DONE as Int
declare val APR_CHILD_NOTDONE as Int
declare val APR_NOTFOUND as Int

declare fn apr_strerror as (statcode: apr_status_t, buf: CString, bufsize: apr_size_t): CString
declare val APR_EACCES as Int
declare val APR_EBUSY as Int
declare val APR_ENOTIMPL as Int
declare val APR_ETIMEDOUT as Int

// The APR_STATUS_IS_* tests (macros): non-zero when a status means that error on any platform.
declare fn APR_STATUS_IS_ENOENT as (s: apr_status_t): Int
declare fn APR_STATUS_IS_EEXIST as (s: apr_status_t): Int
declare fn APR_STATUS_IS_EACCES as (s: apr_status_t): Int
declare fn APR_STATUS_IS_EINVAL as (s: apr_status_t): Int
declare fn APR_STATUS_IS_ENOMEM as (s: apr_status_t): Int
declare fn APR_STATUS_IS_EAGAIN as (s: apr_status_t): Int
declare fn APR_STATUS_IS_EBUSY as (s: apr_status_t): Int
declare fn APR_STATUS_IS_EOF as (s: apr_status_t): Int
declare fn APR_STATUS_IS_EINTR as (s: apr_status_t): Int
declare fn APR_STATUS_IS_TIMEUP as (s: apr_status_t): Int
declare fn APR_STATUS_IS_ETIMEDOUT as (s: apr_status_t): Int
declare fn APR_STATUS_IS_ENOTIMPL as (s: apr_status_t): Int

// The status of the last failed system call (errno on Unix), for calls that aren't APR's.
declare fn apr_get_os_error as (): apr_status_t
