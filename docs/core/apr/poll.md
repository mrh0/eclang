# `poll.ec`

## Types

### <a id="apr_descriptor"></a>`apr_descriptor`

```ec
declare struct apr_descriptor
```

**Fields**

- <a id="apr_descriptor.f"></a>`f: @apr_file_t?`
- <a id="apr_descriptor.s"></a>`s: @apr_socket_t?`

### <a id="apr_pollset_method_e"></a>`apr_pollset_method_e`

```ec
declare enum apr_pollset_method_e
```

**Values**

- <a id="apr_pollset_method_e.:default"></a>`:default = APR_POLLSET_DEFAULT`
- <a id="apr_pollset_method_e.:select"></a>`:select = APR_POLLSET_SELECT`
- <a id="apr_pollset_method_e.:kqueue"></a>`:kqueue = APR_POLLSET_KQUEUE`
- <a id="apr_pollset_method_e.:port"></a>`:port = APR_POLLSET_PORT`
- <a id="apr_pollset_method_e.:epoll"></a>`:epoll = APR_POLLSET_EPOLL`
- <a id="apr_pollset_method_e.:poll"></a>`:poll = APR_POLLSET_POLL`

### <a id="apr_datatype_e"></a>`apr_datatype_e`

```ec
declare type apr_datatype_e as Int
```

### <a id="apr_pollfd_t"></a>`apr_pollfd_t`

```ec
declare struct apr_pollfd_t
```

**Fields**

- <a id="apr_pollfd_t.p"></a>`p: @apr_pool_t?`
- <a id="apr_pollfd_t.desc_type"></a>`desc_type: apr_datatype_e`
- <a id="apr_pollfd_t.reqevents"></a>`reqevents: Short`
- <a id="apr_pollfd_t.rtnevents"></a>`rtnevents: Short`
- <a id="apr_pollfd_t.desc"></a>`desc: apr_descriptor`
- <a id="apr_pollfd_t.client_data"></a>`client_data: @Any?`

### <a id="apr_pollset_t"></a>`apr_pollset_t`

```ec
declare rec apr_pollset_t
```

### <a id="apr_pollcb_t"></a>`apr_pollcb_t`

```ec
declare rec apr_pollcb_t
```

### <a id="apr_pollcb_cb_t"></a>`apr_pollcb_cb_t`

```ec
declare type apr_pollcb_cb_t as (baton: @Any, descriptor: @apr_pollfd_t) => apr_status_t
```

## Values

### <a id="APR_POLLIN"></a>`APR_POLLIN`

```ec
declare val APR_POLLIN as Int
```

### <a id="APR_POLLPRI"></a>`APR_POLLPRI`

```ec
declare val APR_POLLPRI as Int
```

### <a id="APR_POLLOUT"></a>`APR_POLLOUT`

```ec
declare val APR_POLLOUT as Int
```

### <a id="APR_POLLERR"></a>`APR_POLLERR`

```ec
declare val APR_POLLERR as Int
```

### <a id="APR_POLLHUP"></a>`APR_POLLHUP`

```ec
declare val APR_POLLHUP as Int
```

### <a id="APR_POLLNVAL"></a>`APR_POLLNVAL`

```ec
declare val APR_POLLNVAL as Int
```

### <a id="APR_POLLSET_THREADSAFE"></a>`APR_POLLSET_THREADSAFE`

```ec
declare val APR_POLLSET_THREADSAFE as Int
```

### <a id="APR_POLLSET_NOCOPY"></a>`APR_POLLSET_NOCOPY`

```ec
declare val APR_POLLSET_NOCOPY as Int
```

### <a id="APR_POLLSET_WAKEABLE"></a>`APR_POLLSET_WAKEABLE`

```ec
declare val APR_POLLSET_WAKEABLE as Int
```

### <a id="APR_POLLSET_NODEFAULT"></a>`APR_POLLSET_NODEFAULT`

```ec
declare val APR_POLLSET_NODEFAULT as Int
```

### <a id="APR_NO_DESC"></a>`APR_NO_DESC`

```ec
declare val APR_NO_DESC as apr_datatype_e
```

### <a id="APR_POLL_SOCKET"></a>`APR_POLL_SOCKET`

```ec
declare val APR_POLL_SOCKET as apr_datatype_e
```

### <a id="APR_POLL_FILE"></a>`APR_POLL_FILE`

```ec
declare val APR_POLL_FILE as apr_datatype_e
```

## Functions

### <a id="apr_pollset_create"></a>`apr_pollset_create`

```ec
fn apr_pollset_create(pollset: @@apr_pollset_t, size: apr_uint32_t, p: @apr_pool_t, flags: apr_uint32_t): apr_status_t
```

*Binds the C function `apr_pollset_create`.*

### <a id="apr_pollset_create_ex"></a>`apr_pollset_create_ex`

```ec
fn apr_pollset_create_ex(pollset: @@apr_pollset_t, size: apr_uint32_t, p: @apr_pool_t, flags: apr_uint32_t, method: apr_pollset_method_e): apr_status_t
```

*Binds the C function `apr_pollset_create_ex`.*

### <a id="apr_pollset_destroy"></a>`apr_pollset_destroy`

```ec
fn apr_pollset_destroy(pollset: @apr_pollset_t): apr_status_t
```

*Binds the C function `apr_pollset_destroy`.*

### <a id="apr_pollset_add"></a>`apr_pollset_add`

```ec
fn apr_pollset_add(pollset: @apr_pollset_t, descriptor: @apr_pollfd_t): apr_status_t
```

*Binds the C function `apr_pollset_add`.*

### <a id="apr_pollset_remove"></a>`apr_pollset_remove`

```ec
fn apr_pollset_remove(pollset: @apr_pollset_t, descriptor: @apr_pollfd_t): apr_status_t
```

*Binds the C function `apr_pollset_remove`.*

### <a id="apr_pollset_poll"></a>`apr_pollset_poll`

```ec
fn apr_pollset_poll(pollset: @apr_pollset_t, timeout: apr_interval_time_t, num: @apr_int32_t, descriptors: @@apr_pollfd_t): apr_status_t
```

*Binds the C function `apr_pollset_poll`.*

### <a id="apr_pollset_wakeup"></a>`apr_pollset_wakeup`

```ec
fn apr_pollset_wakeup(pollset: @apr_pollset_t): apr_status_t
```

*Binds the C function `apr_pollset_wakeup`.*

### <a id="apr_poll"></a>`apr_poll`

```ec
fn apr_poll(aprset: @apr_pollfd_t, numsock: apr_int32_t, nsds: @apr_int32_t, timeout: apr_interval_time_t): apr_status_t
```

*Binds the C function `apr_poll`.*

### <a id="apr_pollset_method_name"></a>`apr_pollset_method_name`

```ec
fn apr_pollset_method_name(pollset: @apr_pollset_t): CString
```

*Binds the C function `apr_pollset_method_name`.*

### <a id="apr_poll_method_defname"></a>`apr_poll_method_defname`

```ec
fn apr_poll_method_defname(): CString
```

*Binds the C function `apr_poll_method_defname`.*

### <a id="apr_pollcb_create"></a>`apr_pollcb_create`

```ec
fn apr_pollcb_create(pollcb: @@apr_pollcb_t, size: apr_uint32_t, p: @apr_pool_t, flags: apr_uint32_t): apr_status_t
```

*Binds the C function `apr_pollcb_create`.*

### <a id="apr_pollcb_create_ex"></a>`apr_pollcb_create_ex`

```ec
fn apr_pollcb_create_ex(pollcb: @@apr_pollcb_t, size: apr_uint32_t, p: @apr_pool_t, flags: apr_uint32_t, method: apr_pollset_method_e): apr_status_t
```

*Binds the C function `apr_pollcb_create_ex`.*

### <a id="apr_pollcb_add"></a>`apr_pollcb_add`

```ec
fn apr_pollcb_add(pollcb: @apr_pollcb_t, descriptor: @apr_pollfd_t): apr_status_t
```

*Binds the C function `apr_pollcb_add`.*

### <a id="apr_pollcb_remove"></a>`apr_pollcb_remove`

```ec
fn apr_pollcb_remove(pollcb: @apr_pollcb_t, descriptor: @apr_pollfd_t): apr_status_t
```

*Binds the C function `apr_pollcb_remove`.*

### <a id="apr_pollcb_poll"></a>`apr_pollcb_poll`

```ec
fn apr_pollcb_poll(pollcb: @apr_pollcb_t, timeout: apr_interval_time_t, func: apr_pollcb_cb_t, baton: @Any): apr_status_t
```

*Binds the C function `apr_pollcb_poll`.*
