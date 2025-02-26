use "apr_network_io.h"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "core:apr/file_io.ec"
use "core:apr/errno.ec"
use "core:apr/inherit.ec"
use "netinet/in.ec"

declare val APR_MAX_SECS_TO_LINGER as Int
declare val APRMAXHOSTLEN as Int
declare val APR_ANYADDR as CString
declare val APR_SO_LINGER as Int
declare val APR_SO_KEEPALIVE as Int
declare val APR_SO_DEBUG as Int
declare val APR_SO_NONBLOCK as Int
declare val APR_SO_REUSEADDR as Int
declare val APR_SO_SNDBUF as Int
declare val APR_SO_RCVBUF as Int
declare val APR_SO_DISCONNECTED as Int
declare val APR_TCP_NODELAY as Int
declare val APR_TCP_NOPUSH as Int
declare val APR_RESET_NODELAY as Int
declare val APR_INCOMPLETE_READ as Int
declare val APR_INCOMPLETE_WRITE as Int
declare val APR_IPV6_V6ONLY as Int
declare val APR_TCP_DEFER_ACCEPT as Int
declare val APR_IPV4_ADDR_OK as Int
declare val APR_IPV6_ADDR_OK as Int
declare val APR_INADDR_NONE as UInt
declare val APR_INET as Int
declare val APR_UNSPEC as Int
declare val APR_INET6 as Int
declare val APR_PROTO_TCP as Int
declare val APR_PROTO_UDP as Int
declare val APR_PROTO_SCTP as Int
declare val APR_SENDFILE_DISCONNECT_SOCKET as Int

declare type apr_inet_addr as inet_addr

// enum apr_shutdown_how_e { APR_SHUTDOWN_READ, APR_SHUTDOWN_WRITE, APR_SHUTDOWN_READWRITE }
// enum apr_interface_e { APR_LOCAL, APR_REMOTE }
declare type apr_shutdown_how_e as Int
declare type apr_interface_e as Int

declare rec apr_socket_t
declare rec apr_hdtr_t
declare rec apr_in_addr_t
declare rec apr_ipsubnet_t
declare type apr_port_t as apr_uint16_t
declare rec apr_sockaddr_t

declare fn apr_socket_create as (new_sock: @@apr_socket_t, family: Int, type: Int, protocol: Int, cont: @apr_pool_t): apr_status_t
declare fn apr_socket_shutdown as (thesocket: @apr_socket_t, how: apr_shutdown_how_e): apr_status_t
declare fn apr_socket_close as (thesocket: @apr_socket_t): apr_status_t
declare fn apr_socket_bind as (sock: @apr_socket_t, sa: @apr_sockaddr_t): apr_status_t
declare fn apr_socket_listen as (sock: @apr_socket_t, backlog: apr_int32_t): apr_status_t
declare fn apr_socket_accept as (new_sock: @@apr_socket_t, sock: @apr_socket_t, connection_pool: @apr_pool_t): apr_status_t
declare fn apr_socket_connect as (sock: @apr_socket_t, sa: @apr_sockaddr_t): apr_status_t
declare fn apr_socket_atreadeof as (sock: @apr_socket_t, atreadeof: @Int): apr_status_t
declare fn apr_sockaddr_info_get as (sa: @@apr_sockaddr_t, hostname: CString, family: apr_int32_t, port: apr_port_t, flags: apr_int32_t, p: @apr_pool_t): apr_status_t
declare fn apr_getnameinfo as (hostname: @CString, sa: @apr_sockaddr_t, flags: apr_int32_t): apr_status_t
declare fn apr_parse_addr_port as (addr: @CString, scope_id: @CString, port: @apr_port_t, str: CString, p: @apr_pool_t): apr_status_t
declare fn apr_gethostname as (buf: CString, len: Int, cont: @apr_pool_t): apr_status_t
declare fn apr_socket_data_get as (data: @Ptr, key: CString, sock: @apr_socket_t): apr_status_t
declare fn apr_socket_data_set as (sock: @apr_socket_t, data: Ptr, key: CString, cleanup: Fn(Ptr): apr_status_t): apr_status_t
declare fn apr_socket_send as (sock: @apr_socket_t, buf: CString, len: @apr_size_t): apr_status_t
declare fn apr_socket_sendv as (sock: @apr_socket_t, vec: @iovec, nvec: apr_int32_t, len: @apr_size_t): apr_status_t
declare fn apr_socket_sendto as (sock: @apr_socket_t, where: @apr_sockaddr_t, flags: apr_int32_t, buf: CString, len: @apr_size_t): apr_status_t
declare fn apr_socket_recvfrom as (from: @apr_sockaddr_t, sock: @apr_socket_t, flags: apr_int32_t, buf: CString, len: @apr_size_t): apr_status_t
declare fn apr_socket_sendfile as (sock: @apr_socket_t, file: @apr_file_t, hdtr: @apr_hdtr_t, offset: @apr_off_t, len: @apr_size_t, flags: apr_int32_t): apr_status_t
declare fn apr_socket_recv as (sock: @apr_socket_t, buf: CString, len: @apr_size_t): apr_status_t
declare fn apr_socket_opt_set as (sock: @apr_socket_t, opt: apr_int32_t, on: apr_int32_t): apr_status_t
declare fn apr_socket_timeout_set as (sock: @apr_socket_t, t: apr_interval_time_t): apr_status_t
declare fn apr_socket_opt_get as (sock: @apr_socket_t, opt: apr_int32_t, on: @apr_int32_t): apr_status_t
declare fn apr_socket_timeout_get as (sock: @apr_socket_t, t: @apr_interval_time_t): apr_status_t
declare fn apr_socket_atmark as (sock: @apr_socket_t, atmark: @Int): apr_status_t
declare fn apr_socket_addr_get as (sa: @@apr_sockaddr_t, which: apr_interface_e, sock: @apr_socket_t): apr_status_t
declare fn apr_sockaddr_ip_get as (addr: @CString, sockaddr: @apr_sockaddr_t): apr_status_t
declare fn apr_sockaddr_ip_getbuf as (buf: CString, buflen: apr_size_t, sockaddr: @apr_sockaddr_t): apr_status_t
declare fn apr_sockaddr_equal as (addr1: @apr_sockaddr_t, addr2: @apr_sockaddr_t): Int
declare fn apr_socket_type_get as (sock: @apr_socket_t, type: @Int): apr_status_t
declare fn apr_getservbyname as (sockaddr: @apr_sockaddr_t, servname: CString): apr_status_t
declare fn apr_ipsubnet_create as (ipsub: @@apr_ipsubnet_t, ipstr: CString, mask_or_numbits: CString, p: @apr_pool_t): apr_status_t
declare fn apr_ipsubnet_test as (ipsub: @apr_ipsubnet_t, sa: @apr_sockaddr_t): Int
declare fn apr_socket_accept_filter as (sock: @apr_socket_t, name: CString, args: CString): apr_status_t
declare fn apr_socket_protocol_get as (sock: @apr_socket_t, protocol: @Int): apr_status_t
declare fn apr_socket_pool_get as (thesocket: @apr_socket_t): @apr_pool_t
declare fn apr_socket_inherit_set as (thesocket: @apr_socket_t): apr_status_t
declare fn apr_socket_inherit_unset as (thesocket: @apr_socket_t): apr_status_t
declare fn apr_mcast_join as (sock: @apr_socket_t, join: @apr_sockaddr_t, iface: @apr_sockaddr_t, source: @apr_sockaddr_t): apr_status_t
declare fn apr_mcast_leave as (sock: @apr_socket_t, addr: @apr_sockaddr_t, iface: @apr_sockaddr_t, source: @apr_sockaddr_t): apr_status_t
declare fn apr_mcast_hops as (sock: @apr_socket_t, ttl: apr_byte_t): apr_status_t
declare fn apr_mcast_loopback as (sock: @apr_socket_t, opt: apr_byte_t): apr_status_t
declare fn apr_mcast_interface as (sock: @apr_socket_t, iface: @apr_sockaddr_t): apr_status_t
