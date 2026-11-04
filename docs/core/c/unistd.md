# `unistd.ec`

## Types

### <a id="pid_t"></a>`pid_t`

```ec
declare type pid_t as Int
```

## Values

### <a id="_SC_NPROCESSORS_ONLN"></a>`_SC_NPROCESSORS_ONLN`

```ec
declare val _SC_NPROCESSORS_ONLN as Int
```

### <a id="_SC_PAGESIZE"></a>`_SC_PAGESIZE`

```ec
declare val _SC_PAGESIZE as Int
```

### <a id="STDIN_FILENO"></a>`STDIN_FILENO`

```ec
declare val STDIN_FILENO as Int
```

### <a id="STDOUT_FILENO"></a>`STDOUT_FILENO`

```ec
declare val STDOUT_FILENO as Int
```

### <a id="STDERR_FILENO"></a>`STDERR_FILENO`

```ec
declare val STDERR_FILENO as Int
```

## Functions

### <a id="sysconf"></a>`sysconf`

```ec
fn sysconf(name: Int): Long
```

*Binds the C function `sysconf`.*

### <a id="getpid"></a>`getpid`

```ec
fn getpid(): pid_t
```

*Binds the C function `getpid`.*

### <a id="getppid"></a>`getppid`

```ec
fn getppid(): pid_t
```

*Binds the C function `getppid`.*

### <a id="isatty"></a>`isatty`

```ec
fn isatty(fd: Int): Int
```

*Binds the C function `isatty`.*
