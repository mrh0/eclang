# `utsname.ec`

## Types

### <a id="utsname"></a>`utsname`

```ec
declare struct utsname
```

**Fields**

- <a id="utsname.sysname"></a>`sysname: Char[]c`
- <a id="utsname.nodename"></a>`nodename: Char[]c`
- <a id="utsname.release"></a>`release: Char[]c`
- <a id="utsname.version"></a>`version: Char[]c`
- <a id="utsname.machine"></a>`machine: Char[]c`

## Functions

### <a id="uname"></a>`uname`

```ec
fn uname(buf: @utsname): Int
```

*Binds the C function `uname`.*
