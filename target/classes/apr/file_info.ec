use "file_info.h"

use "core:apr/apr.ec"
use "core:apr/user.ec"
use "core:apr/pools.ec"
use "core:apr/tables.ec"
use "core:apr/time.ec"
use "core:apr/errno.ec"

declare val APR_FPROT_USETID as Int
declare val APR_FPROT_UREAD as Int
declare val APR_FPROT_UWRITE as Int
declare val APR_FPROT_UEXECUTE as Int
declare val APR_FPROT_GSETID as Int
declare val APR_FPROT_GREAD as Int
declare val APR_FPROT_GWRITE as Int
declare val APR_FPROT_GEXECUTE as Int
declare val APR_FPROT_WSTICKY as Int
declare val APR_FPROT_WREAD as Int
declare val APR_FPROT_WWRITE as Int
declare val APR_FPROT_WEXECUTE as Int
declare val APR_FPROT_OS_DEFAULT as Int
declare val APR_FPROT_FILE_SOURCE_PERMS as Int
declare val APR_USETID as Int
declare val APR_UREAD as Int
declare val APR_UWRITE as Int
declare val APR_UEXECUTE as Int
declare val APR_GSETID as Int
declare val APR_GREAD as Int
declare val APR_GWRITE as Int
declare val APR_GEXECUTE as Int
declare val APR_WSTICKY as Int
declare val APR_WREAD as Int
declare val APR_WWRITE as Int
declare val APR_WEXECUTE as Int
declare val APR_OS_DEFAULT as Int
declare val APR_FILE_SOURCE_PERMS as Int
declare val APR_FINFO_LINK as Int
declare val APR_FINFO_MTIME as Int
declare val APR_FINFO_CTIME as Int
declare val APR_FINFO_ATIME as Int
declare val APR_FINFO_SIZE as Int
declare val APR_FINFO_CSIZE as Int
declare val APR_FINFO_DEV as Int
declare val APR_FINFO_INODE as Int
declare val APR_FINFO_NLINK as Int
declare val APR_FINFO_TYPE as Int
declare val APR_FINFO_USER as Int
declare val APR_FINFO_GROUP as Int
declare val APR_FINFO_UPROT as Int
declare val APR_FINFO_GPROT as Int
declare val APR_FINFO_WPROT as Int
declare val APR_FINFO_ICASE as Int
declare val APR_FINFO_NAME as Int
declare val APR_FINFO_MIN as Int
declare val APR_FINFO_IDENT as Int
declare val APR_FINFO_OWNER as Int
declare val APR_FINFO_PROT as Int
declare val APR_FINFO_NORM as Int
declare val APR_FINFO_DIRENT as Int
declare val APR_FILEPATH_NOTABOVEROOT as Int
declare val APR_FILEPATH_SECUREROOTTEST as Int
declare val APR_FILEPATH_SECUREROOT as Int
declare val APR_FILEPATH_NOTRELATIVE as Int
declare val APR_FILEPATH_NOTABSOLUTE as Int
declare val APR_FILEPATH_NATIVE as Int
declare val APR_FILEPATH_TRUENAME as Int
declare val APR_FILEPATH_ENCODING_UNKNOWN as Int
declare val APR_FILEPATH_ENCODING_LOCALE as Int
declare val APR_FILEPATH_ENCODING_UTF8 as Int

declare rec apr_dir_t
declare type apr_fileperms_t as apr_int32_t
// declare type apr_dev_t as dev_t
declare rec apr_finfo_t

declare fn apr_stat as (finfo: @apr_finfo_t, fname: CString, wanted: apr_int32_t, pool: @apr_pool_t): apr_status_t
declare fn apr_dir_open as (new_dir: @@apr_dir_t, dirname: CString, pool: @apr_pool_t): apr_status_t
declare fn apr_dir_close as (thedir: @apr_dir_t): apr_status_t
declare fn apr_dir_read as (finfo: @apr_finfo_t, wanted: apr_int32_t, thedir: @apr_dir_t): apr_status_t
declare fn apr_dir_rewind as (thedir: @apr_dir_t): apr_status_t
declare fn apr_filepath_root as (rootpath: @CString, filepath: @CString, flags: apr_int32_t, p: @apr_pool_t): apr_status_t
declare fn apr_filepath_merge as (newpath: @CString, rootpath: CString, addpath: CString, flags: apr_int32_t, p: @apr_pool_t): apr_status_t
declare fn apr_filepath_list_split as (pathelts: @@apr_array_header_t, liststr: CString, p: @apr_pool_t): apr_status_t
declare fn apr_filepath_list_merge as (liststr: @CString, pathelts: @apr_array_header_t, p: @apr_pool_t): apr_status_t
declare fn apr_filepath_get as (path: @CString, flags: apr_int32_t, p: @apr_pool_t): apr_status_t
declare fn apr_filepath_set as (path: CString, p: @apr_pool_t): apr_status_t
declare fn apr_filepath_encoding as (style: @Int, p: @apr_pool_t): apr_status_t
