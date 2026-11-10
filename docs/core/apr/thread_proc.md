# `thread_proc.ec`

## Types

### <a id="apr_proc_t"></a>`apr_proc_t`

```ec
declare rec apr_proc_t
```

**Fields**

- <a id="apr_proc_t.pid"></a>`pid: Int`
- <a id="apr_proc_t.input"></a>`input: @apr_file_t?`
- <a id="apr_proc_t.out"></a>`out: @apr_file_t?`
- <a id="apr_proc_t.err"></a>`err: @apr_file_t?`

### <a id="apr_thread_t"></a>`apr_thread_t`

```ec
declare rec apr_thread_t
```

### <a id="apr_threadattr_t"></a>`apr_threadattr_t`

```ec
declare rec apr_threadattr_t
```

### <a id="apr_procattr_t"></a>`apr_procattr_t`

```ec
declare rec apr_procattr_t
```

### <a id="apr_thread_once_t"></a>`apr_thread_once_t`

```ec
declare rec apr_thread_once_t
```

### <a id="apr_threadkey_t"></a>`apr_threadkey_t`

```ec
declare rec apr_threadkey_t
```

### <a id="apr_other_child_rec_t"></a>`apr_other_child_rec_t`

```ec
declare rec apr_other_child_rec_t
```

### <a id="apr_child_errfn_t"></a>`apr_child_errfn_t`

```ec
declare type apr_child_errfn_t as (proc: @apr_pool_t, err: apr_status_t, description: CString) => None
```

### <a id="apr_thread_start_t"></a>`apr_thread_start_t`

```ec
declare type apr_thread_start_t as (thread: @apr_thread_t, data: @Any) => @Any
```

### <a id="rlimit"></a>`rlimit`

```ec
declare struct rlimit
```

### <a id="apr_cmdtype_e"></a>`apr_cmdtype_e`

```ec
declare enum apr_cmdtype_e
```

**Values**

- <a id="apr_cmdtype_e.:shellcmd"></a>`:shellcmd = APR_SHELLCMD`
- <a id="apr_cmdtype_e.:program"></a>`:program = APR_PROGRAM`
- <a id="apr_cmdtype_e.:program_env"></a>`:program_env = APR_PROGRAM_ENV`
- <a id="apr_cmdtype_e.:program_path"></a>`:program_path = APR_PROGRAM_PATH`
- <a id="apr_cmdtype_e.:shellcmd_env"></a>`:shellcmd_env = APR_SHELLCMD_ENV`

### <a id="apr_wait_how_e"></a>`apr_wait_how_e`

```ec
declare enum apr_wait_how_e
```

**Values**

- <a id="apr_wait_how_e.:wait"></a>`:wait = APR_WAIT`
- <a id="apr_wait_how_e.:nowait"></a>`:nowait = APR_NOWAIT`

### <a id="apr_exit_why_e"></a>`apr_exit_why_e`

```ec
declare type apr_exit_why_e as Int
```

### <a id="apr_kill_conditions_e"></a>`apr_kill_conditions_e`

```ec
declare enum apr_kill_conditions_e
```

**Values**

- <a id="apr_kill_conditions_e.:never"></a>`:never = APR_KILL_NEVER`
- <a id="apr_kill_conditions_e.:always"></a>`:always = APR_KILL_ALWAYS`
- <a id="apr_kill_conditions_e.:after_timeout"></a>`:after_timeout = APR_KILL_AFTER_TIMEOUT`
- <a id="apr_kill_conditions_e.:just_wait"></a>`:just_wait = APR_JUST_WAIT`
- <a id="apr_kill_conditions_e.:only_once"></a>`:only_once = APR_KILL_ONLY_ONCE`

## Values

### <a id="APR_NO_PIPE"></a>`APR_NO_PIPE`

```ec
declare val APR_NO_PIPE as Int
```

### <a id="APR_FULL_BLOCK"></a>`APR_FULL_BLOCK`

```ec
declare val APR_FULL_BLOCK as Int
```

### <a id="APR_FULL_NONBLOCK"></a>`APR_FULL_NONBLOCK`

```ec
declare val APR_FULL_NONBLOCK as Int
```

### <a id="APR_PARENT_BLOCK"></a>`APR_PARENT_BLOCK`

```ec
declare val APR_PARENT_BLOCK as Int
```

### <a id="APR_CHILD_BLOCK"></a>`APR_CHILD_BLOCK`

```ec
declare val APR_CHILD_BLOCK as Int
```

### <a id="APR_NO_FILE"></a>`APR_NO_FILE`

```ec
declare val APR_NO_FILE as Int
```

### <a id="APR_READ_BLOCK"></a>`APR_READ_BLOCK`

```ec
declare val APR_READ_BLOCK as Int
```

### <a id="APR_WRITE_BLOCK"></a>`APR_WRITE_BLOCK`

```ec
declare val APR_WRITE_BLOCK as Int
```

### <a id="APR_LIMIT_CPU"></a>`APR_LIMIT_CPU`

```ec
declare val APR_LIMIT_CPU as Int
```

### <a id="APR_LIMIT_MEM"></a>`APR_LIMIT_MEM`

```ec
declare val APR_LIMIT_MEM as Int
```

### <a id="APR_LIMIT_NPROC"></a>`APR_LIMIT_NPROC`

```ec
declare val APR_LIMIT_NPROC as Int
```

### <a id="APR_LIMIT_NOFILE"></a>`APR_LIMIT_NOFILE`

```ec
declare val APR_LIMIT_NOFILE as Int
```

### <a id="APR_OC_REASON_DEATH"></a>`APR_OC_REASON_DEATH`

```ec
declare val APR_OC_REASON_DEATH as Int
```

### <a id="APR_OC_REASON_UNWRITABLE"></a>`APR_OC_REASON_UNWRITABLE`

```ec
declare val APR_OC_REASON_UNWRITABLE as Int
```

### <a id="APR_OC_REASON_RESTART"></a>`APR_OC_REASON_RESTART`

```ec
declare val APR_OC_REASON_RESTART as Int
```

### <a id="APR_OC_REASON_UNREGISTER"></a>`APR_OC_REASON_UNREGISTER`

```ec
declare val APR_OC_REASON_UNREGISTER as Int
```

### <a id="APR_OC_REASON_LOST"></a>`APR_OC_REASON_LOST`

```ec
declare val APR_OC_REASON_LOST as Int
```

### <a id="APR_OC_REASON_RUNNING"></a>`APR_OC_REASON_RUNNING`

```ec
declare val APR_OC_REASON_RUNNING as Int
```

### <a id="APR_PROC_DETACH_FOREGROUND"></a>`APR_PROC_DETACH_FOREGROUND`

```ec
declare val APR_PROC_DETACH_FOREGROUND as Int
```

### <a id="APR_PROC_DETACH_DAEMONIZE"></a>`APR_PROC_DETACH_DAEMONIZE`

```ec
declare val APR_PROC_DETACH_DAEMONIZE as Int
```

### <a id="APR_PROC_EXIT"></a>`APR_PROC_EXIT`

```ec
declare val APR_PROC_EXIT as Int
```

### <a id="APR_PROC_SIGNAL"></a>`APR_PROC_SIGNAL`

```ec
declare val APR_PROC_SIGNAL as Int
```

### <a id="APR_PROC_SIGNAL_CORE"></a>`APR_PROC_SIGNAL_CORE`

```ec
declare val APR_PROC_SIGNAL_CORE as Int
```

## Functions

### <a id="apr_threadattr_create"></a>`apr_threadattr_create`

```ec
fn apr_threadattr_create(out new_attr: @apr_threadattr_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_threadattr_create`.*

### <a id="apr_threadattr_detach_set"></a>`apr_threadattr_detach_set`

```ec
fn apr_threadattr_detach_set(attr: @apr_threadattr_t, on: apr_int32_t): apr_status_t
```

*Binds the C function `apr_threadattr_detach_set`.*

### <a id="apr_threadattr_detach_get"></a>`apr_threadattr_detach_get`

```ec
fn apr_threadattr_detach_get(attr: @apr_threadattr_t): apr_status_t
```

*Binds the C function `apr_threadattr_detach_get`.*

### <a id="apr_threadattr_stacksize_set"></a>`apr_threadattr_stacksize_set`

```ec
fn apr_threadattr_stacksize_set(attr: @apr_threadattr_t, stacksize: apr_size_t): apr_status_t
```

*Binds the C function `apr_threadattr_stacksize_set`.*

### <a id="apr_threadattr_guardsize_set"></a>`apr_threadattr_guardsize_set`

```ec
fn apr_threadattr_guardsize_set(attr: @apr_threadattr_t, guardsize: apr_size_t): apr_status_t
```

*Binds the C function `apr_threadattr_guardsize_set`.*

### <a id="apr_thread_create"></a>`apr_thread_create`

```ec
fn apr_thread_create(out new_thread: @apr_thread_t, attr: @apr_threadattr_t?, func: apr_thread_start_t, escaping data: @Any, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_thread_create`.*

### <a id="apr_thread_exit"></a>`apr_thread_exit`

```ec
fn apr_thread_exit(thd: @apr_thread_t, retval: apr_status_t): apr_status_t
```

*Binds the C function `apr_thread_exit`.*

### <a id="apr_thread_join"></a>`apr_thread_join`

```ec
fn apr_thread_join(retval: @apr_status_t, thd: @apr_thread_t): apr_status_t
```

*Binds the C function `apr_thread_join`.*

### <a id="apr_thread_yield"></a>`apr_thread_yield`

```ec
fn apr_thread_yield()
```

*Binds the C function `apr_thread_yield`.*

### <a id="apr_thread_once_init"></a>`apr_thread_once_init`

```ec
fn apr_thread_once_init(out control: @apr_thread_once_t, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_thread_once_init`.*

### <a id="apr_thread_once"></a>`apr_thread_once`

```ec
fn apr_thread_once(control: @apr_thread_once_t, func: () => None): apr_status_t
```

*Binds the C function `apr_thread_once`.*

### <a id="apr_thread_detach"></a>`apr_thread_detach`

```ec
fn apr_thread_detach(thd: @apr_thread_t): apr_status_t
```

*Binds the C function `apr_thread_detach`.*

### <a id="apr_thread_data_get"></a>`apr_thread_data_get`

```ec
fn apr_thread_data_get(out data: @Any, key: CString, thread: @apr_thread_t): apr_status_t
```

*Binds the C function `apr_thread_data_get`.*

### <a id="apr_thread_data_set"></a>`apr_thread_data_set`

```ec
fn apr_thread_data_set(data: @Any, key: CString, cleanup: (@Any) => apr_status_t, thread: @apr_thread_t): apr_status_t
```

*Binds the C function `apr_thread_data_set`.*

### <a id="apr_threadkey_private_create"></a>`apr_threadkey_private_create`

```ec
fn apr_threadkey_private_create(out key: @apr_threadkey_t, dest: (@Any) => None, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_threadkey_private_create`.*

### <a id="apr_threadkey_private_get"></a>`apr_threadkey_private_get`

```ec
fn apr_threadkey_private_get(out new_mem: @Any, key: @apr_threadkey_t): apr_status_t
```

*Binds the C function `apr_threadkey_private_get`.*

### <a id="apr_threadkey_private_set"></a>`apr_threadkey_private_set`

```ec
fn apr_threadkey_private_set(data: @Any, key: @apr_threadkey_t): apr_status_t
```

*Binds the C function `apr_threadkey_private_set`.*

### <a id="apr_threadkey_private_delete"></a>`apr_threadkey_private_delete`

```ec
fn apr_threadkey_private_delete(key: @apr_threadkey_t): apr_status_t
```

*Binds the C function `apr_threadkey_private_delete`.*

### <a id="apr_threadkey_data_get"></a>`apr_threadkey_data_get`

```ec
fn apr_threadkey_data_get(out data: @Any, key: CString, threadkey: @apr_threadkey_t): apr_status_t
```

*Binds the C function `apr_threadkey_data_get`.*

### <a id="apr_threadkey_data_set"></a>`apr_threadkey_data_set`

```ec
fn apr_threadkey_data_set(data: @Any, key: CString, cleanup: (@Any) => apr_status_t, threadkey: @apr_threadkey_t): apr_status_t
```

*Binds the C function `apr_threadkey_data_set`.*

### <a id="apr_procattr_create"></a>`apr_procattr_create`

```ec
fn apr_procattr_create(out new_attr: @apr_procattr_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_procattr_create`.*

### <a id="apr_procattr_io_set"></a>`apr_procattr_io_set`

```ec
fn apr_procattr_io_set(attr: @apr_procattr_t, input: apr_int32_t, output: apr_int32_t, err: apr_int32_t): apr_status_t
```

*Binds the C function `apr_procattr_io_set`.*

### <a id="apr_procattr_child_in_set"></a>`apr_procattr_child_in_set`

```ec
fn apr_procattr_child_in_set(attr: @apr_procattr_t, child_in: @apr_file_t, parent_in: @apr_file_t): apr_status_t
```

*Binds the C function `apr_procattr_child_in_set`.*

### <a id="apr_procattr_child_out_set"></a>`apr_procattr_child_out_set`

```ec
fn apr_procattr_child_out_set(attr: @apr_procattr_t, child_out: @apr_file_t, parent_out: @apr_file_t): apr_status_t
```

*Binds the C function `apr_procattr_child_out_set`.*

### <a id="apr_procattr_child_err_set"></a>`apr_procattr_child_err_set`

```ec
fn apr_procattr_child_err_set(attr: @apr_procattr_t, child_err: @apr_file_t, parent_err: @apr_file_t): apr_status_t
```

*Binds the C function `apr_procattr_child_err_set`.*

### <a id="apr_procattr_dir_set"></a>`apr_procattr_dir_set`

```ec
fn apr_procattr_dir_set(attr: @apr_procattr_t, dir: CString): apr_status_t
```

*Binds the C function `apr_procattr_dir_set`.*

### <a id="apr_procattr_cmdtype_set"></a>`apr_procattr_cmdtype_set`

```ec
fn apr_procattr_cmdtype_set(attr: @apr_procattr_t, cmd: apr_cmdtype_e): apr_status_t
```

*Binds the C function `apr_procattr_cmdtype_set`.*

### <a id="apr_procattr_detach_set"></a>`apr_procattr_detach_set`

```ec
fn apr_procattr_detach_set(attr: @apr_procattr_t, detach: apr_int32_t): apr_status_t
```

*Binds the C function `apr_procattr_detach_set`.*

### <a id="apr_procattr_limit_set"></a>`apr_procattr_limit_set`

```ec
fn apr_procattr_limit_set(attr: @apr_procattr_t, what: apr_int32_t, limit: @rlimit): apr_status_t
```

*Binds the C function `apr_procattr_limit_set`.*

### <a id="apr_procattr_child_errfn_set"></a>`apr_procattr_child_errfn_set`

```ec
fn apr_procattr_child_errfn_set(attr: @apr_procattr_t, errfn: apr_child_errfn_t): apr_status_t
```

*Binds the C function `apr_procattr_child_errfn_set`.*

### <a id="apr_procattr_error_check_set"></a>`apr_procattr_error_check_set`

```ec
fn apr_procattr_error_check_set(attr: @apr_procattr_t, chk: apr_int32_t): apr_status_t
```

*Binds the C function `apr_procattr_error_check_set`.*

### <a id="apr_procattr_addrspace_set"></a>`apr_procattr_addrspace_set`

```ec
fn apr_procattr_addrspace_set(attr: @apr_procattr_t, addrspace: apr_int32_t): apr_status_t
```

*Binds the C function `apr_procattr_addrspace_set`.*

### <a id="apr_procattr_user_set"></a>`apr_procattr_user_set`

```ec
fn apr_procattr_user_set(attr: @apr_procattr_t, username: CString, password: CString): apr_status_t
```

*Binds the C function `apr_procattr_user_set`.*

### <a id="apr_procattr_group_set"></a>`apr_procattr_group_set`

```ec
fn apr_procattr_group_set(attr: @apr_procattr_t, groupname: CString): apr_status_t
```

*Binds the C function `apr_procattr_group_set`.*

### <a id="apr_proc_fork"></a>`apr_proc_fork`

```ec
fn apr_proc_fork(proc: @apr_proc_t, cont: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_proc_fork`.*

### <a id="apr_proc_create"></a>`apr_proc_create`

```ec
fn apr_proc_create(new_proc: @apr_proc_t, progname: CString, args: @CString, env: @CString?, attr: @apr_procattr_t, pool: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_proc_create`.*

### <a id="apr_proc_wait"></a>`apr_proc_wait`

```ec
fn apr_proc_wait(proc: @apr_proc_t, out exitcode: Int, exitwhy: @Any, waithow: apr_wait_how_e): apr_status_t
```

*Binds the C function `apr_proc_wait`.*

### <a id="apr_proc_wait_all_procs"></a>`apr_proc_wait_all_procs`

```ec
fn apr_proc_wait_all_procs(proc: @apr_proc_t, exitcode: @Int, exitwhy: @Any, waithow: apr_wait_how_e, p: @apr_pool_t): apr_status_t
```

*Binds the C function `apr_proc_wait_all_procs`.*

### <a id="apr_proc_detach"></a>`apr_proc_detach`

```ec
fn apr_proc_detach(daemonize: Int): apr_status_t
```

*Binds the C function `apr_proc_detach`.*

### <a id="apr_proc_other_child_register"></a>`apr_proc_other_child_register`

```ec
fn apr_proc_other_child_register(proc: @apr_proc_t, maintenance: (Int, @Any, Int) => None, data: @Any, write_fd: @apr_file_t, p: @apr_pool_t)
```

*Binds the C function `apr_proc_other_child_register`.*

### <a id="apr_proc_other_child_unregister"></a>`apr_proc_other_child_unregister`

```ec
fn apr_proc_other_child_unregister(data: @Any)
```

*Binds the C function `apr_proc_other_child_unregister`.*

### <a id="apr_proc_other_child_alert"></a>`apr_proc_other_child_alert`

```ec
fn apr_proc_other_child_alert(proc: @apr_proc_t, reason: Int, status: Int): apr_status_t
```

*Binds the C function `apr_proc_other_child_alert`.*

### <a id="apr_proc_other_child_refresh"></a>`apr_proc_other_child_refresh`

```ec
fn apr_proc_other_child_refresh(ocr: @apr_other_child_rec_t, reason: Int)
```

*Binds the C function `apr_proc_other_child_refresh`.*

### <a id="apr_proc_other_child_refresh_all"></a>`apr_proc_other_child_refresh_all`

```ec
fn apr_proc_other_child_refresh_all(reason: Int)
```

*Binds the C function `apr_proc_other_child_refresh_all`.*

### <a id="apr_proc_kill"></a>`apr_proc_kill`

```ec
fn apr_proc_kill(proc: @apr_proc_t, sig: Int): apr_status_t
```

*Binds the C function `apr_proc_kill`.*

### <a id="apr_pool_note_subprocess"></a>`apr_pool_note_subprocess`

```ec
fn apr_pool_note_subprocess(a: @apr_pool_t, proc: @apr_proc_t, how: apr_kill_conditions_e)
```

*Binds the C function `apr_pool_note_subprocess`.*

### <a id="apr_setup_signal_thread"></a>`apr_setup_signal_thread`

```ec
fn apr_setup_signal_thread(): apr_status_t
```

*Binds the C function `apr_setup_signal_thread`.*

### <a id="apr_signal_thread"></a>`apr_signal_thread`

```ec
fn apr_signal_thread(signal_handler: (Int) => Int): apr_status_t
```

*Binds the C function `apr_signal_thread`.*

### <a id="apr_thread_pool_get"></a>`apr_thread_pool_get`

```ec
fn apr_thread_pool_get(thethread: @apr_thread_t): @apr_pool_t
```

*Binds the C function `apr_thread_pool_get`.*
