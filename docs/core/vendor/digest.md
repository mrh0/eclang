# `digest.ec`

## Functions

### <a id="digestSize"></a>`digestSize`

```ec
fn digestSize(): Size
```

*Binds the C function `__ec_digest_size`.*

### <a id="digestInit"></a>`digestInit`

```ec
fn digestInit(state: @Any, algorithm: Int)
```

*Binds the C function `__ec_digest_init`.*

### <a id="digestUpdate"></a>`digestUpdate`

```ec
fn digestUpdate(state: @Any, algorithm: Int, data: CString, len: Size)
```

*Binds the C function `__ec_digest_update`.*

### <a id="digestFinal"></a>`digestFinal`

```ec
fn digestFinal(state: @Any, algorithm: Int, out: CString)
```

*Binds the C function `__ec_digest_final`.*
