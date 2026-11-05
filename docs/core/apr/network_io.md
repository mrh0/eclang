# `network_io.ec`

## Types

### <a id="apr_shutdown_how_e"></a>`apr_shutdown_how_e`

```ec
declare enum apr_shutdown_how_e
```

**Values**

- <a id="apr_shutdown_how_e.:read"></a>`:read = APR_SHUTDOWN_READ`
- <a id="apr_shutdown_how_e.:write"></a>`:write = APR_SHUTDOWN_WRITE`
- <a id="apr_shutdown_how_e.:readwrite"></a>`:readwrite = APR_SHUTDOWN_READWRITE`

### <a id="apr_interface_e"></a>`apr_interface_e`

```ec
declare enum apr_interface_e
```

**Values**

- <a id="apr_interface_e.:local"></a>`:local = APR_LOCAL`
- <a id="apr_interface_e.:remote"></a>`:remote = APR_REMOTE`

### <a id="apr_socket_t"></a>`apr_socket_t`

```ec
declare rec apr_socket_t
```

### <a id="apr_hdtr_t"></a>`apr_hdtr_t`

```ec
declare rec apr_hdtr_t
```

### <a id="apr_in_addr_t"></a>`apr_in_addr_t`

```ec
declare rec apr_in_addr_t
```

### <a id="apr_ipsubnet_t"></a>`apr_ipsubnet_t`

```ec
declare rec apr_ipsubnet_t
```

### <a id="apr_port_t"></a>`apr_port_t`

```ec
declare type apr_port_t as apr_uint16_t
```

### <a id="apr_sockaddr_t"></a>`apr_sockaddr_t`

```ec
declare rec apr_sockaddr_t
```

**Fields**

- <a id="apr_sockaddr_t.port"></a>`port: apr_port_t`
- <a id="apr_sockaddr_t.family"></a>`family: apr_int32_t`
- <a id="apr_sockaddr_t.next"></a>`next: @apr_sockaddr_t?`

## Values

### <a id="APR_MAX_SECS_TO_LINGER"></a>`APR_MAX_SECS_TO_LINGER`

```ec
declare val APR_MAX_SECS_TO_LINGER as Int
```

### <a id="APRMAXHOSTLEN"></a>`APRMAXHOSTLEN`

```ec
declare val APRMAXHOSTLEN as Int
```

### <a id="APR_ANYADDR"></a>`APR_ANYADDR`

```ec
declare val APR_ANYADDR as CString
```

### <a id="APR_SO_LINGER"></a>`APR_SO_LINGER`

```ec
declare val APR_SO_LINGER as Int
```

### <a id="APR_SO_KEEPALIVE"></a>`APR_SO_KEEPALIVE`

```ec
declare val APR_SO_KEEPALIVE as Int
```

### <a id="APR_SO_DEBUG"></a>`APR_SO_DEBUG`

```ec
declare val APR_SO_DEBUG as Int
```

### <a id="APR_SO_NONBLOCK"></a>`APR_SO_NONBLOCK`

```ec
declare val APR_SO_NONBLOCK as Int
```

### <a id="APR_SO_REUSEADDR"></a>`APR_SO_REUSEADDR`

```ec
declare val APR_SO_REUSEADDR as Int
```

### <a id="APR_SO_SNDBUF"></a>`APR_SO_SNDBUF`

```ec
declare val APR_SO_SNDBUF as Int
```

### <a id="APR_SO_RCVBUF"></a>`APR_SO_RCVBUF`

```ec
declare val APR_SO_RCVBUF as Int
```

### <a id="APR_SO_DISCONNECTED"></a>`APR_SO_DISCONNECTED`

```ec
declare val APR_SO_DISCONNECTED as Int
```

### <a id="APR_TCP_NODELAY"></a>`APR_TCP_NODELAY`

```ec
declare val APR_TCP_NODELAY as Int
```

### <a id="APR_TCP_NOPUSH"></a>`APR_TCP_NOPUSH`

```ec
declare val APR_TCP_NOPUSH as Int
```

### <a id="APR_RESET_NODELAY"></a>`APR_RESET_NODELAY`

```ec
declare val APR_RESET_NODELAY as Int
```

### <a id="APR_INCOMPLETE_READ"></a>`APR_INCOMPLETE_READ`

```ec
declare val APR_INCOMPLETE_READ as Int
```

### <a id="APR_INCOMPLETE_WRITE"></a>`APR_INCOMPLETE_WRITE`

```ec
declare val APR_INCOMPLETE_WRITE as Int
```

### <a id="APR_IPV6_V6ONLY"></a>`APR_IPV6_V6ONLY`

```ec
declare val APR_IPV6_V6ONLY as Int
```

### <a id="APR_TCP_DEFER_ACCEPT"></a>`APR_TCP_DEFER_ACCEPT`

```ec
declare val APR_TCP_DEFER_ACCEPT as Int
```

### <a id="APR_IPV4_ADDR_OK"></a>`APR_IPV4_ADDR_OK`

```ec
declare val APR_IPV4_ADDR_OK as Int
```

### <a id="APR_IPV6_ADDR_OK"></a>`APR_IPV6_ADDR_OK`

```ec
declare val APR_IPV6_ADDR_OK as Int
```

### <a id="APR_INADDR_NONE"></a>`APR_INADDR_NONE`

```ec
declare val APR_INADDR_NONE as UInt
```

### <a id="APR_INET"></a>`APR_INET`

```ec
declare val APR_INET as Int
```

### <a id="APR_UNSPEC"></a>`APR_UNSPEC`

```ec
declare val APR_UNSPEC as Int
```

### <a id="APR_INET6"></a>`APR_INET6`

```ec
declare val APR_INET6 as Int
```

### <a id="APR_PROTO_TCP"></a>`APR_PROTO_TCP`

```ec
declare val APR_PROTO_TCP as Int
```

### <a id="APR_PROTO_UDP"></a>`APR_PROTO_UDP`

```ec
declare val APR_PROTO_UDP as Int
```

### <a id="APR_PROTO_SCTP"></a>`APR_PROTO_SCTP`

```ec
declare val APR_PROTO_SCTP as Int
```

### <a id="APR_SENDFILE_DISCONNECT_SOCKET"></a>`APR_SENDFILE_DISCONNECT_SOCKET`

```ec
declare val APR_SENDFILE_DISCONNECT_SOCKET as Int
```

### <a id="SOCK_STREAM"></a>`SOCK_STREAM`

```ec
declare val SOCK_STREAM as Int
```

### <a id="SOCK_DGRAM"></a>`SOCK_DGRAM`

```ec
declare val SOCK_DGRAM as Int
```

## Functions

### <a id="apr_inet_addr"></a>`apr_inet_addr`

```ec
fn apr_inet_addr(cp: CString): UInt
```

*Binds the C function `apr_inet_addr`.*

### <a id="apr_socket_create"></a>`apr_socket_create`

```ec
fn apr_socket_create(new_sock: @@apr_socket_t, family: Int, sock_type: Int, protocol: Int, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_socket_create`.*

### <a id="apr_socket_shutdown"></a>`apr_socket_shutdown`

```ec
fn apr_socket_shutdown(thesocket: @apr_socket_t, how: apr_shutdown_how_e): apr_status_t
```

*Binds the C function `apr_socket_shutdown`.*

### <a id="apr_socket_close"></a>`apr_socket_close`

```ec
fn apr_socket_close(thesocket: @apr_socket_t): apr_status_t
```

*Binds the C function `apr_socket_close`.*

### <a id="apr_socket_bind"></a>`apr_socket_bind`

```ec
fn apr_socket_bind(sock: @apr_socket_t, sa: @apr_sockaddr_t): apr_status_t
```

*Binds the C function `apr_socket_bind`.*

### <a id="apr_socket_listen"></a>`apr_socket_listen`

```ec
fn apr_socket_listen(sock: @apr_socket_t, backlog: apr_int32_t): apr_status_t
```

*Binds the C function `apr_socket_listen`.*

### <a id="apr_socket_accept"></a>`apr_socket_accept`

```ec
fn apr_socket_accept(new_sock: @@apr_socket_t, sock: @apr_socket_t, connection_pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_socket_accept`.*

### <a id="apr_socket_connect"></a>`apr_socket_connect`

```ec
fn apr_socket_connect(sock: @apr_socket_t, sa: @apr_sockaddr_t): apr_status_t
```

*Binds the C function `apr_socket_connect`.*

### <a id="apr_socket_atreadeof"></a>`apr_socket_atreadeof`

```ec
fn apr_socket_atreadeof(sock: @apr_socket_t, atreadeof: @Int): apr_status_t
```

*Binds the C function `apr_socket_atreadeof`.*

### <a id="apr_sockaddr_info_get"></a>`apr_sockaddr_info_get`

```ec
fn apr_sockaddr_info_get(sa: @@apr_sockaddr_t, hostname: CString, family: apr_int32_t, port: apr_port_t, flags: apr_int32_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_sockaddr_info_get`.*

### <a id="apr_getnameinfo"></a>`apr_getnameinfo`

```ec
fn apr_getnameinfo(hostname: @CString, sa: @apr_sockaddr_t, flags: apr_int32_t): apr_status_t
```

*Binds the C function `apr_getnameinfo`.*

### <a id="apr_parse_addr_port"></a>`apr_parse_addr_port`

```ec
fn apr_parse_addr_port(addr: @CString, scope_id: @CString, port: @apr_port_t, str: CString, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_parse_addr_port`.*

### <a id="apr_gethostname"></a>`apr_gethostname`

```ec
fn apr_gethostname(buf: CString, len: Int, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_gethostname`.*

### <a id="apr_socket_data_get"></a>`apr_socket_data_get`

```ec
fn apr_socket_data_get(data: @@Any, key: CString, sock: @apr_socket_t): apr_status_t
```

*Binds the C function `apr_socket_data_get`.*

### <a id="apr_socket_data_set"></a>`apr_socket_data_set`

```ec
fn apr_socket_data_set(sock: @apr_socket_t, data: @Any, key: CString, cleanup: fn (@Any): apr_status_t): apr_status_t
```

*Binds the C function `apr_socket_data_set`.*

### <a id="apr_socket_send"></a>`apr_socket_send`

```ec
fn apr_socket_send(sock: @apr_socket_t, buf: CString, len: @apr_size_t): apr_status_t
```

*Binds the C function `apr_socket_send`.*

### <a id="apr_socket_sendv"></a>`apr_socket_sendv`

```ec
fn apr_socket_sendv(sock: @apr_socket_t, vec: @iovec, nvec: apr_int32_t, len: @apr_size_t): apr_status_t
```

*Binds the C function `apr_socket_sendv`.*

### <a id="apr_socket_sendto"></a>`apr_socket_sendto`

```ec
fn apr_socket_sendto(sock: @apr_socket_t, target: @apr_sockaddr_t, flags: apr_int32_t, buf: CString, len: @apr_size_t): apr_status_t
```

*Binds the C function `apr_socket_sendto`.*

### <a id="apr_socket_recvfrom"></a>`apr_socket_recvfrom`

```ec
fn apr_socket_recvfrom(from: @apr_sockaddr_t, sock: @apr_socket_t, flags: apr_int32_t, buf: CString, len: @apr_size_t): apr_status_t
```

*Binds the C function `apr_socket_recvfrom`.*

### <a id="apr_socket_sendfile"></a>`apr_socket_sendfile`

```ec
fn apr_socket_sendfile(sock: @apr_socket_t, file: @apr_file_t, hdtr: @apr_hdtr_t, offset: @apr_off_t, len: @apr_size_t, flags: apr_int32_t): apr_status_t
```

*Binds the C function `apr_socket_sendfile`.*

### <a id="apr_socket_recv"></a>`apr_socket_recv`

```ec
fn apr_socket_recv(sock: @apr_socket_t, buf: CString, len: @apr_size_t): apr_status_t
```

*Binds the C function `apr_socket_recv`.*

### <a id="apr_socket_opt_set"></a>`apr_socket_opt_set`

```ec
fn apr_socket_opt_set(sock: @apr_socket_t, opt: apr_int32_t, on: apr_int32_t): apr_status_t
```

*Binds the C function `apr_socket_opt_set`.*

### <a id="apr_socket_timeout_set"></a>`apr_socket_timeout_set`

```ec
fn apr_socket_timeout_set(sock: @apr_socket_t, t: apr_interval_time_t): apr_status_t
```

*Binds the C function `apr_socket_timeout_set`.*

### <a id="apr_socket_opt_get"></a>`apr_socket_opt_get`

```ec
fn apr_socket_opt_get(sock: @apr_socket_t, opt: apr_int32_t, on: @apr_int32_t): apr_status_t
```

*Binds the C function `apr_socket_opt_get`.*

### <a id="apr_socket_timeout_get"></a>`apr_socket_timeout_get`

```ec
fn apr_socket_timeout_get(sock: @apr_socket_t, t: @apr_interval_time_t): apr_status_t
```

*Binds the C function `apr_socket_timeout_get`.*

### <a id="apr_socket_atmark"></a>`apr_socket_atmark`

```ec
fn apr_socket_atmark(sock: @apr_socket_t, atmark: @Int): apr_status_t
```

*Binds the C function `apr_socket_atmark`.*

### <a id="apr_socket_addr_get"></a>`apr_socket_addr_get`

```ec
fn apr_socket_addr_get(sa: @@apr_sockaddr_t, which: apr_interface_e, sock: @apr_socket_t): apr_status_t
```

*Binds the C function `apr_socket_addr_get`.*

### <a id="apr_sockaddr_ip_get"></a>`apr_sockaddr_ip_get`

```ec
fn apr_sockaddr_ip_get(addr: @CString, sockaddr: @apr_sockaddr_t): apr_status_t
```

*Binds the C function `apr_sockaddr_ip_get`.*

### <a id="apr_sockaddr_ip_getbuf"></a>`apr_sockaddr_ip_getbuf`

```ec
fn apr_sockaddr_ip_getbuf(buf: CString, buflen: apr_size_t, sockaddr: @apr_sockaddr_t): apr_status_t
```

*Binds the C function `apr_sockaddr_ip_getbuf`.*

### <a id="apr_sockaddr_equal"></a>`apr_sockaddr_equal`

```ec
fn apr_sockaddr_equal(addr1: @apr_sockaddr_t, addr2: @apr_sockaddr_t): Int
```

*Binds the C function `apr_sockaddr_equal`.*

### <a id="apr_socket_type_get"></a>`apr_socket_type_get`

```ec
fn apr_socket_type_get(sock: @apr_socket_t, sock_type: @Int): apr_status_t
```

*Binds the C function `apr_socket_type_get`.*

### <a id="apr_getservbyname"></a>`apr_getservbyname`

```ec
fn apr_getservbyname(sockaddr: @apr_sockaddr_t, servname: CString): apr_status_t
```

*Binds the C function `apr_getservbyname`.*

### <a id="apr_ipsubnet_create"></a>`apr_ipsubnet_create`

```ec
fn apr_ipsubnet_create(ipsub: @@apr_ipsubnet_t, ipstr: CString, mask_or_numbits: CString, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_ipsubnet_create`.*

### <a id="apr_ipsubnet_test"></a>`apr_ipsubnet_test`

```ec
fn apr_ipsubnet_test(ipsub: @apr_ipsubnet_t, sa: @apr_sockaddr_t): Int
```

*Binds the C function `apr_ipsubnet_test`.*

### <a id="apr_socket_protocol_get"></a>`apr_socket_protocol_get`

```ec
fn apr_socket_protocol_get(sock: @apr_socket_t, protocol: @Int): apr_status_t
```

*Binds the C function `apr_socket_protocol_get`.*

### <a id="apr_socket_pool_get"></a>`apr_socket_pool_get`

```ec
fn apr_socket_pool_get(thesocket: @apr_socket_t): @apr_pool_t
```

*Binds the C function `apr_socket_pool_get`.*

### <a id="apr_socket_inherit_set"></a>`apr_socket_inherit_set`

```ec
fn apr_socket_inherit_set(thesocket: @apr_socket_t): apr_status_t
```

*Binds the C function `apr_socket_inherit_set`.*

### <a id="apr_socket_inherit_unset"></a>`apr_socket_inherit_unset`

```ec
fn apr_socket_inherit_unset(thesocket: @apr_socket_t): apr_status_t
```

*Binds the C function `apr_socket_inherit_unset`.*

### <a id="apr_mcast_join"></a>`apr_mcast_join`

```ec
fn apr_mcast_join(sock: @apr_socket_t, join: @apr_sockaddr_t, iface: @apr_sockaddr_t, source: @apr_sockaddr_t): apr_status_t
```

*Binds the C function `apr_mcast_join`.*

### <a id="apr_mcast_leave"></a>`apr_mcast_leave`

```ec
fn apr_mcast_leave(sock: @apr_socket_t, addr: @apr_sockaddr_t, iface: @apr_sockaddr_t, source: @apr_sockaddr_t): apr_status_t
```

*Binds the C function `apr_mcast_leave`.*

### <a id="apr_mcast_hops"></a>`apr_mcast_hops`

```ec
fn apr_mcast_hops(sock: @apr_socket_t, ttl: apr_byte_t): apr_status_t
```

*Binds the C function `apr_mcast_hops`.*

### <a id="apr_mcast_loopback"></a>`apr_mcast_loopback`

```ec
fn apr_mcast_loopback(sock: @apr_socket_t, opt: apr_byte_t): apr_status_t
```

*Binds the C function `apr_mcast_loopback`.*

### <a id="apr_mcast_interface"></a>`apr_mcast_interface`

```ec
fn apr_mcast_interface(sock: @apr_socket_t, iface: @apr_sockaddr_t): apr_status_t
```

*Binds the C function `apr_mcast_interface`.*
