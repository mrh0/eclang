# Core library reference

Reference for the `core:` standard library, generated from the ecdocs in `src/main/resources/`. There is no prelude: bring a file in with `use "core:<file>.ec"`.

Each page is the output of `eclang doc core:<file>.ec`; edit the sources, not these pages. `priv` declarations are left out.

## Library

| File | `use` | Contents |
|---|---|---|
| [errors](errors.md) | `core:errors.ec` | Common error types, for `throws`. |
| [io](io.md) | `core:io.ec` | Input and output: the console, files, directories and paths. |
| [math](math.md) | `core:math.ec` | Maths: constants, C's math.h, whole-number helpers, checked arithmetic, and random numbers. |
| [stdarg](stdarg.md) | `core:stdarg.ec` | stdarg.h: C's va_list, for the v* variants of formatting functions (vprintf, apr_pvsprintf, ...). |
| [string](string.md) | `core:string.ec` | Strings: searching, changing case, splitting and joining, padding, parsing numbers, and a StringBuilder. |
| [thread](thread.md) | `core:thread.ec` | Threads and the tools to share data between them: Mutex, RwLock, Cond, WaitGroup, Atomic and Channel. |
| [time](time.md) | `core:time.ec` | Time: moments (Time), lengths of time (Duration), a steady clock for measuring (Instant), and calendar dates (Date) in UTC, local time or a fixed offset. |

## C bindings

One file per C header, with the C names unchanged. Undocumented declarations are listed with their signature; see the C documentation for their behaviour.

| File | `use` | Header | Declarations |
|---|---|---|---|
| [ctype](c/ctype.md) | `core:c/ctype.ec` | `ctype.h` | 14 |
| [stdio](c/stdio.md) | `core:c/stdio.ec` | `stdio.h` | 26 |
| [stdlib](c/stdlib.md) | `core:c/stdlib.ec` | `stdlib.h` | 29 |
| [string](c/string.md) | `core:c/string.ec` | `string.h` | 14 |
| [time](c/time.md) | `core:c/time.ec` | `time.h` | 19 |
| [unistd](c/unistd.md) | `core:c/unistd.ec` | `unistd.h` | 10 |

## APR bindings

One file per APR header, with the C names unchanged. These have no ecdocs, so the pages list signatures; see the APR documentation for behaviour.

| File | `use` | Header | Declarations |
|---|---|---|---|
| [allocator](apr/allocator.md) | `core:apr/allocator.ec` | `apr_allocator.h` | 13 |
| [apr](apr/apr.md) | `core:apr/apr.ec` | `apr.h`, `sys/uio.h` | 17 |
| [atomic](apr/atomic.md) | `core:apr/atomic.ec` | `apr_atomic.h` | 11 |
| [dso](apr/dso.md) | `core:apr/dso.ec` | `apr_dso.h` | 6 |
| [env](apr/env.md) | `core:apr/env.ec` | `apr_env.h` | 3 |
| [errno](apr/errno.md) | `core:apr/errno.ec` | `apr_errno.h` | 28 |
| [file_info](apr/file_info.md) | `core:apr/file_info.ec` | `apr_file_info.h` | 85 |
| [file_io](apr/file_io.md) | `core:apr/file_io.ec` | `apr_file_io.h` | 104 |
| [fnmatch](apr/fnmatch.md) | `core:apr/fnmatch.ec` | `apr_fnmatch.h` | 8 |
| [general](apr/general.md) | `core:apr/general.ec` | `apr_general.h` | 10 |
| [getopt](apr/getopt.md) | `core:apr/getopt.ec` | `apr_getopt.h` | 6 |
| [global_mutex](apr/global_mutex.md) | `core:apr/global_mutex.ec` | `apr_global_mutex.h` | 10 |
| [hash](apr/hash.md) | `core:apr/hash.ec` | `apr_hash.h` | 20 |
| [inherit](apr/inherit.md) | `core:apr/inherit.ec` | `apr_inherit.h` | none |
| [lib](apr/lib.md) | `core:apr/lib.ec` | `apr_lib.h`, `apr.h`, `ctype.h` | 19 |
| [mmap](apr/mmap.md) | `core:apr/mmap.ec` | `apr_mmap.h` | 9 |
| [network_io](apr/network_io.md) | `core:apr/network_io.ec` | `apr_network_io.h`, `netinet/in.h`, `arpa/inet.h` | 79 |
| [poll](apr/poll.md) | `core:apr/poll.ec` | `apr_poll.h`, `netinet/in.h` | 32 |
| [pools](apr/pools.md) | `core:apr/pools.ec` | `apr_pools.h` | 39 |
| [portable](apr/portable.md) | `core:apr/portable.ec` | `apr_portable.h`, `dirent.h`, `fcntl.h`, `pthread.h` | 41 |
| [proc_mutex](apr/proc_mutex.md) | `core:apr/proc_mutex.ec` | `apr_proc_mutex.h` | 13 |
| [random](apr/random.md) | `core:apr/random.ec` | `apr_random.h` | 15 |
| [ring](apr/ring.md) | `core:apr/ring.ec` | `apr_ring.h` | none |
| [shm](apr/shm.md) | `core:apr/shm.ec` | `apr_shm.h` | 9 |
| [signal](apr/signal.md) | `core:apr/signal.ec` | `apr_signal.h`, `signal.h` | 6 |
| [strings](apr/strings.md) | `core:apr/strings.ec` | `apr_strings.h` | 21 |
| [support](apr/support.md) | `core:apr/support.ec` | `apr_support.h` | 1 |
| [tables](apr/tables.md) | `core:apr/tables.ec` | `apr_tables.h`, `stdarg.h` | 35 |
| [thread_cond](apr/thread_cond.md) | `core:apr/thread_cond.ec` | `apr_thread_cond.h` | 8 |
| [thread_mutex](apr/thread_mutex.md) | `core:apr/thread_mutex.ec` | `apr_thread_mutex.h` | 10 |
| [thread_proc](apr/thread_proc.md) | `core:apr/thread_proc.ec` | `apr_thread_proc.h`, `sys/time.h`, `sys/resource.h` | 86 |
| [thread_rwlock](apr/thread_rwlock.md) | `core:apr/thread_rwlock.ec` | `apr_thread_rwlock.h` | 9 |
| [time](apr/time.md) | `core:apr/time.ec` | `apr_time.h` | 18 |
| [user](apr/user.md) | `core:apr/user.ec` | `apr_user.h` | 8 |
| [version](apr/version.md) | `core:apr/version.ec` | `apr_version.h` | 8 |
| [want](apr/want.md) | `core:apr/want.ec` | `apr_want.h` | none |
