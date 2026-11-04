use "apr_poll.h"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "core:apr/errno.ec"
use "core:apr/inherit.ec"
use "core:apr/file_io.ec"
use "core:apr/network_io.ec"
use "netinet/in.h"

declare val APR_POLLIN as Int
declare val APR_POLLPRI as Int
declare val APR_POLLOUT as Int
declare val APR_POLLERR as Int
declare val APR_POLLHUP as Int
declare val APR_POLLNVAL as Int
declare val APR_POLLSET_THREADSAFE as Int
declare val APR_POLLSET_NOCOPY as Int
declare val APR_POLLSET_WAKEABLE as Int
declare val APR_POLLSET_NODEFAULT as Int

declare rec apr_descriptor

// enum  	apr_datatype_e { APR_NO_DESC, APR_POLL_SOCKET, APR_POLL_FILE, APR_POLL_LASTDESC }
declare enum apr_pollset_method_e as
    :default = APR_POLLSET_DEFAULT
    :select = APR_POLLSET_SELECT
    :kqueue = APR_POLLSET_KQUEUE
    :port = APR_POLLSET_PORT
    :epoll = APR_POLLSET_EPOLL
    :poll = APR_POLLSET_POLL
declare type apr_datatype_e as Int

declare rec apr_pollfd_t
declare rec apr_pollset_t
declare rec apr_pollcb_t

declare type apr_pollcb_cb_t as (baton: @Any, descriptor: @apr_pollfd_t) => apr_status_t

declare fn apr_pollset_create as (pollset: @@apr_pollset_t, size: apr_uint32_t, p: @apr_pool_t, flags: apr_uint32_t): apr_status_t
declare fn apr_pollset_create_ex as (pollset: @@apr_pollset_t, size: apr_uint32_t, p: @apr_pool_t, flags: apr_uint32_t, method: apr_pollset_method_e): apr_status_t
declare fn apr_pollset_destroy as (pollset: @apr_pollset_t): apr_status_t
declare fn apr_pollset_add as (pollset: @apr_pollset_t, descriptor: @apr_pollfd_t): apr_status_t
declare fn apr_pollset_remove as (pollset: @apr_pollset_t, descriptor: @apr_pollfd_t): apr_status_t
declare fn apr_pollset_poll as (pollset: @apr_pollset_t, timeout: apr_interval_time_t, num: @apr_int32_t, descriptors: @@apr_pollfd_t): apr_status_t
declare fn apr_pollset_wakeup as (pollset: @apr_pollset_t): apr_status_t
declare fn apr_poll as (aprset: @apr_pollfd_t, numsock: apr_int32_t, nsds: @apr_int32_t, timeout: apr_interval_time_t): apr_status_t
declare fn apr_pollset_method_name as (pollset: @apr_pollset_t): CString
declare fn apr_poll_method_defname as (): CString
declare fn apr_pollcb_create as (pollcb: @@apr_pollcb_t, size: apr_uint32_t, p: @apr_pool_t, flags: apr_uint32_t): apr_status_t
declare fn apr_pollcb_create_ex as (pollcb: @@apr_pollcb_t, size: apr_uint32_t, p: @apr_pool_t, flags: apr_uint32_t, method: apr_pollset_method_e): apr_status_t
declare fn apr_pollcb_add as (pollcb: @apr_pollcb_t, descriptor: @apr_pollfd_t): apr_status_t
declare fn apr_pollcb_remove as (pollcb: @apr_pollcb_t, descriptor: @apr_pollfd_t): apr_status_t
declare fn apr_pollcb_poll as (pollcb: @apr_pollcb_t, timeout: apr_interval_time_t, func: apr_pollcb_cb_t, baton: @Any): apr_status_t
