# `stdlib.ec`

## Types

### <a id="div_t"></a>`div_t`

```ec
declare rec div_t
```

**Fields**

- <a id="div_t.quot"></a>`quot: Int`
- <a id="div_t.rem"></a>`rem: Int`

### <a id="ldiv_t"></a>`ldiv_t`

```ec
declare rec ldiv_t
```

**Fields**

- <a id="ldiv_t.quot"></a>`quot: Long`
- <a id="ldiv_t.rem"></a>`rem: Long`

## Values

### <a id="EXIT_SUCCESS"></a>`EXIT_SUCCESS`

```ec
declare val EXIT_SUCCESS as Int
```

### <a id="EXIT_FAILURE"></a>`EXIT_FAILURE`

```ec
declare val EXIT_FAILURE as Int
```

### <a id="RAND_MAX"></a>`RAND_MAX`

```ec
declare val RAND_MAX as Int
```

## Functions

### <a id="exit"></a>`exit`

```ec
fn exit(status: Int): Never
```

*Binds the C function `exit`.*

### <a id="abort"></a>`abort`

```ec
fn abort(): Never
```

*Binds the C function `abort`.*

### <a id="atexit"></a>`atexit`

```ec
fn atexit(func: () => None): Int
```

*Binds the C function `atexit`.*

### <a id="getenv"></a>`getenv`

```ec
fn getenv(name: CString): CString?
```

*Binds the C function `getenv`.*

### <a id="system"></a>`system`

```ec
fn system(command: CString): Int
```

*Binds the C function `system`.*

### <a id="mkdtemp"></a>`mkdtemp`

```ec
fn mkdtemp(template: CString): CString?
```

*Binds the C function `mkdtemp`.*

### <a id="getloadavg"></a>`getloadavg`

```ec
fn getloadavg(loadavg: @Double, count: Int): Int
```

*Binds the C function `getloadavg`.*

### <a id="strtol"></a>`strtol`

```ec
fn strtol(s: CString, end: @CString?, base: Int): Long
```

*Binds the C function `strtol`.*

### <a id="strtoul"></a>`strtoul`

```ec
fn strtoul(s: CString, end: @CString?, base: Int): ULong
```

*Binds the C function `strtoul`.*

### <a id="strtod"></a>`strtod`

```ec
fn strtod(s: CString, end: @CString?): Double
```

*Binds the C function `strtod`.*

### <a id="strtof"></a>`strtof`

```ec
fn strtof(s: CString, end: @CString?): Float
```

*Binds the C function `strtof`.*

### <a id="atoi"></a>`atoi`

```ec
fn atoi(s: CString): Int
```

*Binds the C function `atoi`.*

### <a id="atol"></a>`atol`

```ec
fn atol(s: CString): Long
```

*Binds the C function `atol`.*

### <a id="atof"></a>`atof`

```ec
fn atof(s: CString): Double
```

*Binds the C function `atof`.*

### <a id="abs"></a>`abs`

```ec
fn abs(x: Int): Int
```

*Binds the C function `abs`.*

### <a id="labs"></a>`labs`

```ec
fn labs(x: Long): Long
```

*Binds the C function `labs`.*

### <a id="rand"></a>`rand`

```ec
fn rand(): Int
```

*Binds the C function `rand`.*

### <a id="srand"></a>`srand`

```ec
fn srand(seed: UInt)
```

*Binds the C function `srand`.*

### <a id="qsort"></a>`qsort`

```ec
fn qsort(base: @Any, count: Size, size: Size, compare: (@Any, @Any) => Int)
```

*Binds the C function `qsort`.*

### <a id="bsearch"></a>`bsearch`

```ec
fn bsearch(key: @Any, base: @Any, count: Size, size: Size, compare: (@Any, @Any) => Int): @Any?
```

*Binds the C function `bsearch`.*

### <a id="malloc"></a>`malloc`

```ec
fn malloc(size: Size): @Any?
```

*Binds the C function `malloc`.*

### <a id="calloc"></a>`calloc`

```ec
fn calloc(count: Size, size: Size): @Any?
```

*Binds the C function `calloc`.*

### <a id="realloc"></a>`realloc`

```ec
fn realloc(p: @Any?, size: Size): @Any?
```

*Binds the C function `realloc`.*

### <a id="free"></a>`free`

```ec
fn free(p: @Any?)
```

*Binds the C function `free`.*

### <a id="div"></a>`div`

```ec
fn div(numer: Int, denom: Int): div_t
```

*Binds the C function `div`.*

### <a id="ldiv"></a>`ldiv`

```ec
fn ldiv(numer: Long, denom: Long): ldiv_t
```

*Binds the C function `ldiv`.*
