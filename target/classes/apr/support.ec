use "apr_support.h"
use "core:apr/apr.ec"
use "core:apr/network_io.ec"
use "core:apr/file_io.ec"

declare fn apr_wait_for_io_or_timeout as (f: @apr_file_t, s: @apr_socket_t, for_read: Int): apr_status_t
