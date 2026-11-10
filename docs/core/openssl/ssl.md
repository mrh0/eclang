# `ssl.ec`

## Types

### <a id="SSL_CTX"></a>`SSL_CTX`

```ec
declare rec SSL_CTX
```

### <a id="SSL"></a>`SSL`

```ec
declare rec SSL
```

### <a id="SSL_METHOD"></a>`SSL_METHOD`

```ec
declare rec SSL_METHOD
```

### <a id="BIO"></a>`BIO`

```ec
declare rec BIO
```

### <a id="BIO_METHOD"></a>`BIO_METHOD`

```ec
declare rec BIO_METHOD
```

### <a id="X509_VERIFY_PARAM"></a>`X509_VERIFY_PARAM`

```ec
declare rec X509_VERIFY_PARAM
```

## Values

### <a id="SSL_FILETYPE_PEM"></a>`SSL_FILETYPE_PEM`

```ec
declare val SSL_FILETYPE_PEM as Int
```

### <a id="SSL_VERIFY_NONE"></a>`SSL_VERIFY_NONE`

```ec
declare val SSL_VERIFY_NONE as Int
```

### <a id="SSL_VERIFY_PEER"></a>`SSL_VERIFY_PEER`

```ec
declare val SSL_VERIFY_PEER as Int
```

### <a id="SSL_OP_IGNORE_UNEXPECTED_EOF"></a>`SSL_OP_IGNORE_UNEXPECTED_EOF`

```ec
declare val SSL_OP_IGNORE_UNEXPECTED_EOF as ULong
```

### <a id="SSL_CTRL_SET_MIN_PROTO_VERSION"></a>`SSL_CTRL_SET_MIN_PROTO_VERSION`

```ec
declare val SSL_CTRL_SET_MIN_PROTO_VERSION as Int
```

### <a id="SSL_CTRL_SET_TLSEXT_HOSTNAME"></a>`SSL_CTRL_SET_TLSEXT_HOSTNAME`

```ec
declare val SSL_CTRL_SET_TLSEXT_HOSTNAME as Int
```

### <a id="TLSEXT_NAMETYPE_host_name"></a>`TLSEXT_NAMETYPE_host_name`

```ec
declare val TLSEXT_NAMETYPE_host_name as Int
```

### <a id="TLS1_2_VERSION"></a>`TLS1_2_VERSION`

```ec
declare val TLS1_2_VERSION as Int
```

### <a id="SSL_ERROR_NONE"></a>`SSL_ERROR_NONE`

```ec
declare val SSL_ERROR_NONE as Int
```

### <a id="SSL_ERROR_SSL"></a>`SSL_ERROR_SSL`

```ec
declare val SSL_ERROR_SSL as Int
```

### <a id="SSL_ERROR_WANT_READ"></a>`SSL_ERROR_WANT_READ`

```ec
declare val SSL_ERROR_WANT_READ as Int
```

### <a id="SSL_ERROR_WANT_WRITE"></a>`SSL_ERROR_WANT_WRITE`

```ec
declare val SSL_ERROR_WANT_WRITE as Int
```

### <a id="SSL_ERROR_SYSCALL"></a>`SSL_ERROR_SYSCALL`

```ec
declare val SSL_ERROR_SYSCALL as Int
```

### <a id="SSL_ERROR_ZERO_RETURN"></a>`SSL_ERROR_ZERO_RETURN`

```ec
declare val SSL_ERROR_ZERO_RETURN as Int
```

### <a id="X509_V_OK"></a>`X509_V_OK`

```ec
declare val X509_V_OK as Long
```

### <a id="BIO_TYPE_SOURCE_SINK"></a>`BIO_TYPE_SOURCE_SINK`

```ec
declare val BIO_TYPE_SOURCE_SINK as Int
```

### <a id="BIO_FLAGS_READ"></a>`BIO_FLAGS_READ`

```ec
declare val BIO_FLAGS_READ as Int
```

### <a id="BIO_FLAGS_WRITE"></a>`BIO_FLAGS_WRITE`

```ec
declare val BIO_FLAGS_WRITE as Int
```

### <a id="BIO_FLAGS_SHOULD_RETRY"></a>`BIO_FLAGS_SHOULD_RETRY`

```ec
declare val BIO_FLAGS_SHOULD_RETRY as Int
```

### <a id="BIO_CTRL_FLUSH"></a>`BIO_CTRL_FLUSH`

```ec
declare val BIO_CTRL_FLUSH as Int
```

## Functions

### <a id="TLS_client_method"></a>`TLS_client_method`

```ec
fn TLS_client_method(): @SSL_METHOD
```

*Binds the C function `TLS_client_method`.*

### <a id="TLS_server_method"></a>`TLS_server_method`

```ec
fn TLS_server_method(): @SSL_METHOD
```

*Binds the C function `TLS_server_method`.*

### <a id="SSL_CTX_new"></a>`SSL_CTX_new`

```ec
fn SSL_CTX_new(meth: @SSL_METHOD): @SSL_CTX?
```

*Binds the C function `SSL_CTX_new`.*

### <a id="SSL_CTX_free"></a>`SSL_CTX_free`

```ec
fn SSL_CTX_free(ctx: @SSL_CTX)
```

*Binds the C function `SSL_CTX_free`.*

### <a id="SSL_CTX_set_default_verify_paths"></a>`SSL_CTX_set_default_verify_paths`

```ec
fn SSL_CTX_set_default_verify_paths(ctx: @SSL_CTX): Int
```

*Binds the C function `SSL_CTX_set_default_verify_paths`.*

### <a id="SSL_CTX_load_verify_locations"></a>`SSL_CTX_load_verify_locations`

```ec
fn SSL_CTX_load_verify_locations(ctx: @SSL_CTX, CAfile: CString, CApath: CString?): Int
```

*Binds the C function `SSL_CTX_load_verify_locations`.*

### <a id="SSL_CTX_set_verify"></a>`SSL_CTX_set_verify`

```ec
fn SSL_CTX_set_verify(ctx: @SSL_CTX, mode: Int, callback: @Any?)
```

*Binds the C function `SSL_CTX_set_verify`.*

### <a id="SSL_CTX_set_options"></a>`SSL_CTX_set_options`

```ec
fn SSL_CTX_set_options(ctx: @SSL_CTX, op: ULong): ULong
```

*Binds the C function `SSL_CTX_set_options`.*

### <a id="SSL_CTX_ctrl"></a>`SSL_CTX_ctrl`

```ec
fn SSL_CTX_ctrl(ctx: @SSL_CTX, cmd: Int, larg: Long, parg: @Any?): Long
```

*Binds the C function `SSL_CTX_ctrl`.*

### <a id="SSL_CTX_use_certificate_chain_file"></a>`SSL_CTX_use_certificate_chain_file`

```ec
fn SSL_CTX_use_certificate_chain_file(ctx: @SSL_CTX, file: CString): Int
```

*Binds the C function `SSL_CTX_use_certificate_chain_file`.*

### <a id="SSL_CTX_use_PrivateKey_file"></a>`SSL_CTX_use_PrivateKey_file`

```ec
fn SSL_CTX_use_PrivateKey_file(ctx: @SSL_CTX, file: CString, kind: Int): Int
```

*Binds the C function `SSL_CTX_use_PrivateKey_file`.*

### <a id="SSL_CTX_check_private_key"></a>`SSL_CTX_check_private_key`

```ec
fn SSL_CTX_check_private_key(ctx: @SSL_CTX): Int
```

*Binds the C function `SSL_CTX_check_private_key`.*

### <a id="SSL_new"></a>`SSL_new`

```ec
fn SSL_new(ctx: @SSL_CTX): @SSL?
```

*Binds the C function `SSL_new`.*

### <a id="SSL_free"></a>`SSL_free`

```ec
fn SSL_free(ssl: @SSL)
```

*Binds the C function `SSL_free`.*

### <a id="SSL_set_bio"></a>`SSL_set_bio`

```ec
fn SSL_set_bio(s: @SSL, rbio: @BIO, wbio: @BIO)
```

*Binds the C function `SSL_set_bio`.*

### <a id="SSL_set_connect_state"></a>`SSL_set_connect_state`

```ec
fn SSL_set_connect_state(s: @SSL)
```

*Binds the C function `SSL_set_connect_state`.*

### <a id="SSL_set_accept_state"></a>`SSL_set_accept_state`

```ec
fn SSL_set_accept_state(s: @SSL)
```

*Binds the C function `SSL_set_accept_state`.*

### <a id="SSL_ctrl"></a>`SSL_ctrl`

```ec
fn SSL_ctrl(s: @SSL, cmd: Int, larg: Long, parg: @Any?): Long
```

*Binds the C function `SSL_ctrl`.*

### <a id="SSL_set1_host"></a>`SSL_set1_host`

```ec
fn SSL_set1_host(s: @SSL, host: CString): Int
```

*Binds the C function `SSL_set1_host`.*

### <a id="SSL_get0_param"></a>`SSL_get0_param`

```ec
fn SSL_get0_param(s: @SSL): @X509_VERIFY_PARAM
```

*Binds the C function `SSL_get0_param`.*

### <a id="X509_VERIFY_PARAM_set1_ip_asc"></a>`X509_VERIFY_PARAM_set1_ip_asc`

```ec
fn X509_VERIFY_PARAM_set1_ip_asc(param: @X509_VERIFY_PARAM, ipasc: CString): Int
```

*Binds the C function `X509_VERIFY_PARAM_set1_ip_asc`.*

### <a id="SSL_do_handshake"></a>`SSL_do_handshake`

```ec
fn SSL_do_handshake(s: @SSL): Int
```

*Binds the C function `SSL_do_handshake`.*

### <a id="SSL_read_ex"></a>`SSL_read_ex`

```ec
fn SSL_read_ex(s: @SSL, buf: @Any, num: Size, out readbytes: Size): Int
```

*Binds the C function `SSL_read_ex`.*

### <a id="SSL_write_ex"></a>`SSL_write_ex`

```ec
fn SSL_write_ex(s: @SSL, buf: @Any, num: Size, out written: Size): Int
```

*Binds the C function `SSL_write_ex`.*

### <a id="SSL_pending"></a>`SSL_pending`

```ec
fn SSL_pending(s: @SSL): Int
```

*Binds the C function `SSL_pending`.*

### <a id="SSL_shutdown"></a>`SSL_shutdown`

```ec
fn SSL_shutdown(s: @SSL): Int
```

*Binds the C function `SSL_shutdown`.*

### <a id="SSL_get_error"></a>`SSL_get_error`

```ec
fn SSL_get_error(s: @SSL, ret_code: Int): Int
```

*Binds the C function `SSL_get_error`.*

### <a id="SSL_get_verify_result"></a>`SSL_get_verify_result`

```ec
fn SSL_get_verify_result(s: @SSL): Long
```

*Binds the C function `SSL_get_verify_result`.*

### <a id="SSL_get_version"></a>`SSL_get_version`

```ec
fn SSL_get_version(s: @SSL): CString
```

*Binds the C function `SSL_get_version`.*

### <a id="X509_verify_cert_error_string"></a>`X509_verify_cert_error_string`

```ec
fn X509_verify_cert_error_string(n: Long): CString
```

*Binds the C function `X509_verify_cert_error_string`.*

### <a id="ERR_get_error"></a>`ERR_get_error`

```ec
fn ERR_get_error(): ULong
```

*Binds the C function `ERR_get_error`.*

### <a id="ERR_clear_error"></a>`ERR_clear_error`

```ec
fn ERR_clear_error()
```

*Binds the C function `ERR_clear_error`.*

### <a id="ERR_error_string_n"></a>`ERR_error_string_n`

```ec
fn ERR_error_string_n(e: ULong, buf: CString, len: Size)
```

*Binds the C function `ERR_error_string_n`.*

### <a id="BIO_get_new_index"></a>`BIO_get_new_index`

```ec
fn BIO_get_new_index(): Int
```

*Binds the C function `BIO_get_new_index`.*

### <a id="BIO_meth_new"></a>`BIO_meth_new`

```ec
fn BIO_meth_new(kind: Int, name: CString): @BIO_METHOD?
```

*Binds the C function `BIO_meth_new`.*

### <a id="BIO_meth_set_write_ex"></a>`BIO_meth_set_write_ex`

```ec
fn BIO_meth_set_write_ex(biom: @BIO_METHOD, bwrite: fn (@BIO, CString, Size, @Size): Int): Int
```

*Binds the C function `BIO_meth_set_write_ex`.*

### <a id="BIO_meth_set_read_ex"></a>`BIO_meth_set_read_ex`

```ec
fn BIO_meth_set_read_ex(biom: @BIO_METHOD, bread: fn (@BIO, CString, Size, @Size): Int): Int
```

*Binds the C function `BIO_meth_set_read_ex`.*

### <a id="BIO_meth_set_ctrl"></a>`BIO_meth_set_ctrl`

```ec
fn BIO_meth_set_ctrl(biom: @BIO_METHOD, ctrl: fn (@BIO, Int, Long, @Any?): Long): Int
```

*Binds the C function `BIO_meth_set_ctrl`.*

### <a id="BIO_new"></a>`BIO_new`

```ec
fn BIO_new(kind: @BIO_METHOD): @BIO?
```

*Binds the C function `BIO_new`.*

### <a id="BIO_set_data"></a>`BIO_set_data`

```ec
fn BIO_set_data(a: @BIO, ptr: @Any)
```

*Binds the C function `BIO_set_data`.*

### <a id="BIO_get_data"></a>`BIO_get_data`

```ec
fn BIO_get_data(a: @BIO): @Any
```

*Binds the C function `BIO_get_data`.*

### <a id="BIO_set_init"></a>`BIO_set_init`

```ec
fn BIO_set_init(a: @BIO, init: Int)
```

*Binds the C function `BIO_set_init`.*

### <a id="BIO_set_flags"></a>`BIO_set_flags`

```ec
fn BIO_set_flags(b: @BIO, flags: Int)
```

*Binds the C function `BIO_set_flags`.*

### <a id="BIO_clear_flags"></a>`BIO_clear_flags`

```ec
fn BIO_clear_flags(b: @BIO, flags: Int)
```

*Binds the C function `BIO_clear_flags`.*
