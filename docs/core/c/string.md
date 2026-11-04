# `string.ec`

## Functions

### <a id="memcpy"></a>`memcpy`

```ec
fn memcpy(dst: @Any, src: @Any, n: Size): @Any
```

*Binds the C function `memcpy`.*

### <a id="memmove"></a>`memmove`

```ec
fn memmove(dst: @Any, src: @Any, n: Size): @Any
```

*Binds the C function `memmove`.*

### <a id="memset"></a>`memset`

```ec
fn memset(dst: @Any, c: Int, n: Size): @Any
```

*Binds the C function `memset`.*

### <a id="memcmp"></a>`memcmp`

```ec
fn memcmp(a: @Any, b: @Any, n: Size): Int
```

*Binds the C function `memcmp`.*

### <a id="memchr"></a>`memchr`

```ec
fn memchr(s: @Any, c: Int, n: Size): @Any?
```

*Binds the C function `memchr`.*

### <a id="strlen"></a>`strlen`

```ec
fn strlen(s: CString): Size
```

*Binds the C function `strlen`.*

### <a id="strcmp"></a>`strcmp`

```ec
fn strcmp(a: CString, b: CString): Int
```

*Binds the C function `strcmp`.*

### <a id="strncmp"></a>`strncmp`

```ec
fn strncmp(a: CString, b: CString, n: Size): Int
```

*Binds the C function `strncmp`.*

### <a id="strchr"></a>`strchr`

```ec
fn strchr(s: CString, c: Int): CString?
```

*Binds the C function `strchr`.*

### <a id="strrchr"></a>`strrchr`

```ec
fn strrchr(s: CString, c: Int): CString?
```

*Binds the C function `strrchr`.*

### <a id="strstr"></a>`strstr`

```ec
fn strstr(haystack: CString, needle: CString): CString?
```

*Binds the C function `strstr`.*

### <a id="strspn"></a>`strspn`

```ec
fn strspn(s: CString, accept: CString): Size
```

*Binds the C function `strspn`.*

### <a id="strcspn"></a>`strcspn`

```ec
fn strcspn(s: CString, reject: CString): Size
```

*Binds the C function `strcspn`.*

### <a id="strerror"></a>`strerror`

```ec
fn strerror(errnum: Int): CString
```

*Binds the C function `strerror`.*
