# `generic-record.ec`

## Types

### <a id="Box"></a>`Box`

```ec
rec Box<T>
```

A value in a box.

**Parameters**

- `<T>` — what it holds

**Fields**

- <a id="Box.value"></a>`value: T` — The value.

## Functions

### <a id="open"></a>`open`

```ec
fn open(b: Box<T>): <T>
```

The value of `b`.
