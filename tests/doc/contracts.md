# `contracts.ec`

## Functions

### <a id="isqrt"></a>`isqrt`

```ec
fn isqrt(x: Int): Int
    requires x >= 0, "x must not be negative"
    ensures result * result <= x
```

The square root of [x], rounded down.

**Parameters**

- `x` — what to take the root of

**Returns** the root

### <a id="half"></a>`half`

```ec
fn half(n: Int): Int
    requires n % 2 == 0
    debug requires n - 1 > 0
    debug ensures result > 0
```

Halves an even number.

### <a id="twice"></a>`twice`

```ec
fn twice(n: Int): Int
```

No contracts here.
