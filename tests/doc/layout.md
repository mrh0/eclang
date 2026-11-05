# `layout.ec`

## Types

### <a id="Wire"></a>`Wire`

```ec
packed rec Wire
```

A header on the wire.

**Fields**

- <a id="Wire.tag"></a>`tag: UChar` — The kind of message.
- <a id="Wire.flags"></a>`flags: UInt : 3`

### <a id="Value"></a>`Value`

```ec
union Value
```

One of two views.

**Fields**

- <a id="Value.i"></a>`i: Int`
- <a id="Value.f"></a>`f: Float`

## Functions

### <a id="plus"></a>`plus`

```ec
export fn plus(a: Int): Int
```

Adds one.

**Parameters**

- `a` — The number.

**Returns** One more.
