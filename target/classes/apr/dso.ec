declare rec apr_dso_handle_t
declare type apr_dso_handle_sym_t as @Any

declare fn apr_dso_load as (res_handle: @@apr_dso_handle_t, path: CString, ctx: @apr_pool_t): apr_status_t
declare fn apr_dso_unload as (handle: @apr_dso_handle_t): apr_status_t
declare fn apr_dso_sym as (ressym: @apr_dso_handle_sym_t, handle: @apr_dso_handle_t, symname: CString): apr_status_t
declare fn apr_dso_error as (dso: @apr_dso_handle_t, buf: CString, bufsize: apr_size_t): CString
