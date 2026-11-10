use "apr_thread_proc.h"
use "core:apr/apr.ec"
use "core:apr/file_io.ec"
use "core:apr/pools.ec"
use "core:apr/errno.ec"
use "sys/time.h"
use "sys/resource.h"

// declare val APR_PROC_CHECK_EXIT(x)   (x & APR_PROC_EXIT)
// declare val APR_PROC_CHECK_SIGNALED(x)   (x & APR_PROC_SIGNAL)
// declare val APR_PROC_CHECK_CORE_DUMP(x)   (x & APR_PROC_SIGNAL_CORE)
declare val APR_NO_PIPE as Int
declare val APR_FULL_BLOCK as Int
declare val APR_FULL_NONBLOCK as Int
declare val APR_PARENT_BLOCK as Int
declare val APR_CHILD_BLOCK as Int
declare val APR_NO_FILE as Int
declare val APR_READ_BLOCK as Int
declare val APR_WRITE_BLOCK as Int
declare val APR_LIMIT_CPU as Int
declare val APR_LIMIT_MEM as Int
declare val APR_LIMIT_NPROC as Int
declare val APR_LIMIT_NOFILE as Int
declare val APR_OC_REASON_DEATH as Int
declare val APR_OC_REASON_UNWRITABLE as Int
declare val APR_OC_REASON_RESTART as Int
declare val APR_OC_REASON_UNREGISTER as Int
declare val APR_OC_REASON_LOST as Int
declare val APR_OC_REASON_RUNNING as Int
declare val APR_PROC_DETACH_FOREGROUND as Int
declare val APR_PROC_DETACH_DAEMONIZE as Int

// `in` is an eclang keyword, so that field has another name here.
declare rec apr_proc_t as
    pid: Int
    input extern "in": @apr_file_t?
    out: @apr_file_t?
    err: @apr_file_t?
declare rec apr_thread_t
declare rec apr_threadattr_t
declare rec apr_procattr_t
declare rec apr_thread_once_t
declare rec apr_threadkey_t
declare rec apr_other_child_rec_t

declare type apr_child_errfn_t as (proc: @apr_pool_t, err: apr_status_t, description: CString) => None
declare type apr_thread_start_t as (thread: @apr_thread_t, data: @Any) => @Any

// enum  	apr_exit_why_e { APR_PROC_EXIT = 1, APR_PROC_SIGNAL = 2, APR_PROC_SIGNAL_CORE = 4 }

// struct rlimit (sys/resource.h)
declare struct rlimit

declare enum apr_cmdtype_e as
    :shellcmd = APR_SHELLCMD
    :program = APR_PROGRAM
    :program_env = APR_PROGRAM_ENV
    :program_path = APR_PROGRAM_PATH
    :shellcmd_env = APR_SHELLCMD_ENV
declare enum apr_wait_how_e as
    :wait = APR_WAIT
    :nowait = APR_NOWAIT
declare type apr_exit_why_e as Int
declare val APR_PROC_EXIT as Int
declare val APR_PROC_SIGNAL as Int
declare val APR_PROC_SIGNAL_CORE as Int
declare enum apr_kill_conditions_e as
    :never = APR_KILL_NEVER
    :always = APR_KILL_ALWAYS
    :after_timeout = APR_KILL_AFTER_TIMEOUT
    :just_wait = APR_JUST_WAIT
    :only_once = APR_KILL_ONLY_ONCE

declare fn apr_threadattr_create as (out new_attr: @apr_threadattr_t, cont: @apr_pool_t): apr_status_t
declare fn apr_threadattr_detach_set as (attr: @apr_threadattr_t, on: apr_int32_t): apr_status_t
declare fn apr_threadattr_detach_get as (attr: @apr_threadattr_t): apr_status_t
declare fn apr_threadattr_stacksize_set as (attr: @apr_threadattr_t, stacksize: apr_size_t): apr_status_t
declare fn apr_threadattr_guardsize_set as (attr: @apr_threadattr_t, guardsize: apr_size_t): apr_status_t

declare fn apr_thread_create as (out new_thread: @apr_thread_t, attr: @apr_threadattr_t?, func: apr_thread_start_t, escaping data: @Any, cont: @apr_pool_t): apr_status_t
declare fn apr_thread_exit as (thd: @apr_thread_t, retval: apr_status_t): apr_status_t
declare fn apr_thread_join as (retval: @apr_status_t, thd: @apr_thread_t): apr_status_t
declare fn apr_thread_yield as ()

declare fn apr_thread_once_init as (out control: @apr_thread_once_t, p: @apr_pool_t): apr_status_t
declare fn apr_thread_once as (control: @apr_thread_once_t, func: () => None): apr_status_t
declare fn apr_thread_detach as (thd: @apr_thread_t): apr_status_t

declare fn apr_thread_data_get as (out data: @Any, key: CString, thread: @apr_thread_t): apr_status_t
declare fn apr_thread_data_set as (data: @Any, key: CString, cleanup: (@Any) => apr_status_t, thread: @apr_thread_t): apr_status_t

declare fn apr_threadkey_private_create as (out key: @apr_threadkey_t, dest: (@Any) => None, cont: @apr_pool_t): apr_status_t
declare fn apr_threadkey_private_get as (out new_mem: @Any, key: @apr_threadkey_t): apr_status_t
declare fn apr_threadkey_private_set as (data: @Any, key: @apr_threadkey_t): apr_status_t
declare fn apr_threadkey_private_delete as (key: @apr_threadkey_t): apr_status_t

declare fn apr_threadkey_data_get as (out data: @Any, key: CString, threadkey: @apr_threadkey_t): apr_status_t
declare fn apr_threadkey_data_set as (data: @Any, key: CString, cleanup: (@Any) => apr_status_t, threadkey: @apr_threadkey_t): apr_status_t

declare fn apr_procattr_create as (out new_attr: @apr_procattr_t, cont: @apr_pool_t): apr_status_t
declare fn apr_procattr_io_set as (attr: @apr_procattr_t, input: apr_int32_t, output: apr_int32_t, err: apr_int32_t): apr_status_t

declare fn apr_procattr_child_in_set as (attr: @apr_procattr_t, child_in: @apr_file_t, parent_in: @apr_file_t): apr_status_t
declare fn apr_procattr_child_out_set as (attr: @apr_procattr_t, child_out: @apr_file_t, parent_out: @apr_file_t): apr_status_t
declare fn apr_procattr_child_err_set as (attr: @apr_procattr_t, child_err: @apr_file_t, parent_err: @apr_file_t): apr_status_t
declare fn apr_procattr_dir_set as (attr: @apr_procattr_t, dir: CString): apr_status_t
declare fn apr_procattr_cmdtype_set as (attr: @apr_procattr_t, cmd: apr_cmdtype_e): apr_status_t
declare fn apr_procattr_detach_set as (attr: @apr_procattr_t, detach: apr_int32_t): apr_status_t
declare fn apr_procattr_limit_set as (attr: @apr_procattr_t, what: apr_int32_t, limit: @rlimit): apr_status_t
declare fn apr_procattr_child_errfn_set as (attr: @apr_procattr_t, errfn: apr_child_errfn_t): apr_status_t
declare fn apr_procattr_error_check_set as (attr: @apr_procattr_t, chk: apr_int32_t): apr_status_t
declare fn apr_procattr_addrspace_set as (attr: @apr_procattr_t, addrspace: apr_int32_t): apr_status_t
declare fn apr_procattr_user_set as (attr: @apr_procattr_t, username: CString, password: CString): apr_status_t
declare fn apr_procattr_group_set as (attr: @apr_procattr_t, groupname: CString): apr_status_t
declare fn apr_proc_fork as (proc: @apr_proc_t, cont: @apr_pool_t): apr_status_t
declare fn apr_proc_create as (new_proc: @apr_proc_t, progname: CString, args: @CString, env: @CString?, attr: @apr_procattr_t, pool: @apr_pool_t): apr_status_t
// exitwhy: the address of an Int (C: apr_exit_why_e*), set to APR_PROC_EXIT, APR_PROC_SIGNAL or APR_PROC_SIGNAL_CORE.
declare fn apr_proc_wait as (proc: @apr_proc_t, out exitcode: Int, exitwhy: @Any, waithow: apr_wait_how_e): apr_status_t
declare fn apr_proc_wait_all_procs as (proc: @apr_proc_t, exitcode: @Int, exitwhy: @Any, waithow: apr_wait_how_e, p: @apr_pool_t): apr_status_t
declare fn apr_proc_detach as (daemonize: Int): apr_status_t
declare fn apr_proc_other_child_register as (proc: @apr_proc_t, maintenance: (Int, @Any, Int) => None, data: @Any, write_fd: @apr_file_t, p: @apr_pool_t)
declare fn apr_proc_other_child_unregister as (data: @Any)
declare fn apr_proc_other_child_alert as (proc: @apr_proc_t, reason: Int, status: Int): apr_status_t
declare fn apr_proc_other_child_refresh as (ocr: @apr_other_child_rec_t, reason: Int)
declare fn apr_proc_other_child_refresh_all as (reason: Int)
declare fn apr_proc_kill as (proc: @apr_proc_t, sig: Int): apr_status_t
declare fn apr_pool_note_subprocess as (a: @apr_pool_t, proc: @apr_proc_t, how: apr_kill_conditions_e)
declare fn apr_setup_signal_thread as (): apr_status_t
declare fn apr_signal_thread as (signal_handler: (Int) => Int): apr_status_t
declare fn apr_thread_pool_get as (thethread: @apr_thread_t): @apr_pool_t
