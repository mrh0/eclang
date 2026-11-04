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
declare val APR_NOTFOUND as Int

declare fn apr_strerror as (statcode: apr_status_t, buf: CString, bufsize: apr_size_t): CString
