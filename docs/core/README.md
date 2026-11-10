# Core library reference

Reference for the `core:` standard library, generated from the ecdocs in `src/main/resources/`. There is no prelude: bring a file in with `use "core:<file>.ec"`.

Each page is the output of `eclang doc core:<file>.ec`; edit the sources, not these pages. `priv` declarations are left out.

## Library

| File | `use` | Contents |
|---|---|---|
| [args](args.md) | `core:args.ec` | Command-line arguments: flags, options, positional arguments and subcommands, with a generated --help. |
| [bench](bench.md) | `core:bench.ec` | Benchmarks: run a function many times, after a warm-up, and report how long one run takes: the fastest, the median, and how much the runs spread. |
| [big](big.md) | `core:big.ec` | Numbers of any size: whole numbers (Integer) and exact decimals (Decimal), for money and anything else that a Long would overflow or a Double would round. |
| [bytes](bytes.md) | `core:bytes.ec` | Binary data: reading and writing numbers and strings in a given byte order, for network protocols and file formats. |
| [csv](csv.md) | `core:csv.ec` | CSV (RFC 4180): reading and writing tables of text, with error positions. |
| [errors](errors.md) | `core:errors.ec` | Common error types, for `throws`. |
| [fs](fs.md) | `core:fs.ec` | The file system beyond single files: walking a directory tree, glob patterns, temporary files and memory-mapped reads. |
| [hash](hash.md) | `core:hash.ec` | Checksums and hashes: CRC32, FNV-1a, MD5, SHA-1, SHA-256 and HMAC. |
| [http](http.md) | `core:http.ec` | HTTP/1.1: parsing URLs, a client, and a small server. |
| [io](io.md) | `core:io.ec` | Input and output: the console, files and directories. Paths are in core:path.ec. |
| [json](json.md) | `core:json.ec` | JSON (RFC 8259): parsing with error positions, building values, and writing them, compact or pretty. |
| [log](log.md) | `core:log.ec` | Logging: levels, timestamps, and output to stderr or a file, from any thread. |
| [math](math.md) | `core:math.ec` | Maths: constants, C's math.h, whole-number helpers, checked arithmetic, and random numbers. |
| [net](net.md) | `core:net.ec` | Networking: TCP clients and servers, UDP, DNS lookups and timeouts. |
| [os](os.md) | `core:os.ec` | The operating system: what system this is, the environment, users, and running other programs. |
| [parallel](parallel.md) | `core:parallel.ec` | Runs a function on the elements of an array on several threads at once, and waits until it is done: `parallel.map`. |
| [path](path.md) | `core:path.ec` | Paths of files and directories, as text: joining, taking apart, normalising, and making absolute or relative. |
| [regex](regex.md) | `core:regex.ec` | Regular expressions: matching, capturing, replacing and splitting, in linear time. |
| [stdarg](stdarg.md) | `core:stdarg.ec` | stdarg.h: C's va_list, for the v* variants of formatting functions (vprintf, apr_pvsprintf, ...). |
| [string](string.md) | `core:string.ec` | Strings: searching, changing case, splitting and joining, padding, parsing numbers, and a StringBuilder. |
| [term](term.md) | `core:term.ec` | Terminal tools: colours and styles, the cursor, key and mouse input, a screen buffer to draw on, and prompts. |
| [test](test.md) | `core:test.ec` | Tests: assertions that say what differs, and a small runner. |
| [thread](thread.md) | `core:thread.ec` | Threads and the tools to share data between them: Mutex, RwLock, Cond, WaitGroup, Atomic, Queue, Channel and WorkerPool. |
| [time](time.md) | `core:time.ec` | Time: moments (Time), lengths of time (Duration), a steady clock for measuring (Instant), and calendar dates (Date) in UTC, local time or a fixed offset. |
| [tls](tls.md) | `core:tls.ec` | TLS (the "S" of HTTPS) through OpenSSL: encrypted connections that check who is at the other end. |
| [unicode](unicode.md) | `core:unicode.ec` | Unicode text in UTF-8: going through code points, encoding and decoding them, validating, and repairing broken text. |
| [uuid](uuid.md) | `core:uuid.ec` | UUIDs (RFC 9562): random ones (version 4), ones that sort by when they were made (version 7), and their text. |

## C bindings

One file per C header, with the C names unchanged. Undocumented declarations are listed with their signature; see the C documentation for their behaviour.

| File | `use` | Header | Declarations |
|---|---|---|---|
| [ctype](c/ctype.md) | `core:c/ctype.ec` | `ctype.h` | 14 |
| [ioctl](c/ioctl.md) | `core:c/ioctl.ec` | `sys/ioctl.h` | 3 |
| [pthread](c/pthread.md) | `core:c/pthread.ec` | `pthread.h` | 3 |
| [signal](c/signal.md) | `core:c/signal.ec` | `signal.h` | 22 |
| [stdio](c/stdio.md) | `core:c/stdio.ec` | `stdio.h` | 29 |
| [stdlib](c/stdlib.md) | `core:c/stdlib.ec` | `stdlib.h` | 31 |
| [string](c/string.md) | `core:c/string.ec` | `string.h` | 14 |
| [termios](c/termios.md) | `core:c/termios.ec` | `termios.h` | 12 |
| [time](c/time.md) | `core:c/time.ec` | `time.h` | 19 |
| [unistd](c/unistd.md) | `core:c/unistd.ec` | `unistd.h` | 16 |
| [utsname](c/utsname.md) | `core:c/utsname.ec` | `sys/utsname.h` | 2 |
| [openssl/ssl](openssl/ssl.md) | `core:openssl/ssl.ec` | `openssl/ssl.h`, `err.h`, `x509v3.h` | 70 |

## APR bindings

One file per APR header, with the C names unchanged. These have no ecdocs, so the pages list signatures; see the APR documentation for behaviour.

| File | `use` | Header | Declarations |
|---|---|---|---|
| [allocator](apr/allocator.md) | `core:apr/allocator.ec` | `apr_allocator.h` | 13 |
| [apr](apr/apr.md) | `core:apr/apr.ec` | `apr.h`, `sys/uio.h` | 17 |
| [atomic](apr/atomic.md) | `core:apr/atomic.ec` | `apr_atomic.h` | 11 |
| [dso](apr/dso.md) | `core:apr/dso.ec` | `apr_dso.h` | 6 |
| [env](apr/env.md) | `core:apr/env.ec` | `apr_env.h` | 3 |
| [errno](apr/errno.md) | `core:apr/errno.ec` | `apr_errno.h` | 32 |
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
| [network_io](apr/network_io.md) | `core:apr/network_io.ec` | `apr_network_io.h`, `netinet/in.h`, `arpa/inet.h`, `sys/socket.h` | 81 |
| [poll](apr/poll.md) | `core:apr/poll.ec` | `apr_poll.h`, `netinet/in.h` | 35 |
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

## Bundled C

C code that neither C nor APR provide, written into the generated C of the programs that use it. See [vendor/README.md](../../src/main/resources/vendor/README.md) for where it comes from.

| File | `use` | Source | Declarations |
|---|---|---|---|
| [digest](vendor/digest.md) | `core:vendor/digest.ec` | `core:vendor/digest.c` | 4 |
