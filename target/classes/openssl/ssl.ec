// OpenSSL 3 (ssl.h, err.h, bio.h, x509_vfy.h): what core:tls.ec uses of it. Links -lssl -lcrypto, which needs OpenSSL's
// headers and libraries (libssl-dev, openssl-devel). Macros that are calls of SSL_ctrl/SSL_CTX_ctrl are written as those.
use "openssl/ssl.h"
use "openssl/err.h"
use "openssl/x509v3.h"
use "lib:ssl"
use "lib:crypto"

declare rec SSL_CTX
declare rec SSL
declare rec SSL_METHOD
declare rec BIO
declare rec BIO_METHOD
declare rec X509_VERIFY_PARAM

declare val SSL_FILETYPE_PEM as Int
declare val SSL_VERIFY_NONE as Int
declare val SSL_VERIFY_PEER as Int
declare val SSL_OP_IGNORE_UNEXPECTED_EOF as ULong
declare val SSL_CTRL_SET_MIN_PROTO_VERSION as Int
declare val SSL_CTRL_SET_TLSEXT_HOSTNAME as Int
declare val TLSEXT_NAMETYPE_host_name as Int
declare val TLS1_2_VERSION as Int
declare val SSL_ERROR_NONE as Int
declare val SSL_ERROR_SSL as Int
declare val SSL_ERROR_WANT_READ as Int
declare val SSL_ERROR_WANT_WRITE as Int
declare val SSL_ERROR_SYSCALL as Int
declare val SSL_ERROR_ZERO_RETURN as Int
declare val X509_V_OK as Long
declare val BIO_TYPE_SOURCE_SINK as Int
declare val BIO_FLAGS_READ as Int
declare val BIO_FLAGS_WRITE as Int
declare val BIO_FLAGS_SHOULD_RETRY as Int
declare val BIO_CTRL_FLUSH as Int

declare fn TLS_client_method as (): @SSL_METHOD
declare fn TLS_server_method as (): @SSL_METHOD
declare fn SSL_CTX_new as (meth: @SSL_METHOD): @SSL_CTX?
declare fn SSL_CTX_free as (ctx: @SSL_CTX)
declare fn SSL_CTX_set_default_verify_paths as (ctx: @SSL_CTX): Int
declare fn SSL_CTX_load_verify_locations as (ctx: @SSL_CTX, CAfile: CString, CApath: CString?): Int
declare fn SSL_CTX_set_verify as (ctx: @SSL_CTX, mode: Int, callback: @Any?)
declare fn SSL_CTX_set_options as (ctx: @SSL_CTX, op: ULong): ULong
declare fn SSL_CTX_ctrl as (ctx: @SSL_CTX, cmd: Int, larg: Long, parg: @Any?): Long
declare fn SSL_CTX_use_certificate_chain_file as (ctx: @SSL_CTX, file: CString): Int
declare fn SSL_CTX_use_PrivateKey_file as (ctx: @SSL_CTX, file: CString, kind: Int): Int
declare fn SSL_CTX_check_private_key as (ctx: @SSL_CTX): Int

declare fn SSL_new as (ctx: @SSL_CTX): @SSL?
declare fn SSL_free as (ssl: @SSL)
declare fn SSL_set_bio as (s: @SSL, rbio: @BIO, wbio: @BIO)
declare fn SSL_set_connect_state as (s: @SSL)
declare fn SSL_set_accept_state as (s: @SSL)
declare fn SSL_ctrl as (s: @SSL, cmd: Int, larg: Long, parg: @Any?): Long
declare fn SSL_set1_host as (s: @SSL, host: CString): Int
declare fn SSL_get0_param as (s: @SSL): @X509_VERIFY_PARAM
declare fn X509_VERIFY_PARAM_set1_ip_asc as (param: @X509_VERIFY_PARAM, ipasc: CString): Int
declare fn SSL_do_handshake as (s: @SSL): Int
declare fn SSL_read_ex as (s: @SSL, buf: @Any, num: Size, out readbytes: Size): Int
declare fn SSL_write_ex as (s: @SSL, buf: @Any, num: Size, out written: Size): Int
declare fn SSL_pending as (s: @SSL): Int
declare fn SSL_shutdown as (s: @SSL): Int
declare fn SSL_get_error as (s: @SSL, ret_code: Int): Int
declare fn SSL_get_verify_result as (s: @SSL): Long
declare fn SSL_get_version as (s: @SSL): CString
declare fn X509_verify_cert_error_string as (n: Long): CString
declare fn ERR_get_error as (): ULong
declare fn ERR_clear_error as ()
declare fn ERR_error_string_n as (e: ULong, buf: CString, len: Size)

declare fn BIO_get_new_index as (): Int
declare fn BIO_meth_new as (kind: Int, name: CString): @BIO_METHOD?
declare fn BIO_meth_set_write_ex as (biom: @BIO_METHOD, bwrite: fn (@BIO, CString, Size, @Size): Int): Int
declare fn BIO_meth_set_read_ex as (biom: @BIO_METHOD, bread: fn (@BIO, CString, Size, @Size): Int): Int
declare fn BIO_meth_set_ctrl as (biom: @BIO_METHOD, ctrl: fn (@BIO, Int, Long, @Any?): Long): Int
declare fn BIO_new as (kind: @BIO_METHOD): @BIO?
declare fn BIO_set_data as (a: @BIO, ptr: @Any)
declare fn BIO_get_data as (a: @BIO): @Any
declare fn BIO_set_init as (a: @BIO, init: Int)
declare fn BIO_set_flags as (b: @BIO, flags: Int)
declare fn BIO_clear_flags as (b: @BIO, flags: Int)

