use "apr_version.h"
use "core:apr/apr.ec"

declare val APR_MAJOR_VERSION as Int
declare val APR_MINOR_VERSION as Int
declare val APR_PATCH_VERSION as Int
// APR_VERSION_AT_LEAST(major, minor, patch)
// APR_STRINGIFY(n)   APR_STRINGIFY_HELPER(n)
// APR_STRINGIFY_HELPER(n)
declare val APR_VERSION_STRING as CString
declare val APR_VERSION_STRING_CSV as CString

declare rec apr_version_t as
    major: Int
    minor: Int
    patch: Int
    is_dev: Int

declare fn apr_version as (pvsn: @apr_version_t)
declare fn apr_version_string as (): CString
