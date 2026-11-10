# Module `bytes`

Binary data: reading and writing numbers and strings in a given byte order, for network protocols and file formats.

    use "core:bytes.ec"
    val w = bytes.writer()                              // big-endian, the order of network protocols
    bytes.writeU16(w, 0xCAFE)
    val size = bytes.position(w)                        // come back to this later
    bytes.writeU32(w, 0)
    bytes.writeString(w, "hello")
    bytes.setU32(w, size, 5)
    val data = bytes.finish(w)                          // a String of 11 bytes

    val r = bytes.reader(data)
    val magic = try bytes.readU16(r)                    // 0xCAFE
    val n = try bytes.readU32(r)                        // 5
    val text = try bytes.read(r, n as Size)             // "hello"
    io.log(bytes.remaining(r))                          // 0

    val bmp = bytes.reader(try io.readFile("a.bmp"), :little)

A String is bytes, so data is a String, read with `io.readFile` or `net.read`, and written with `io.writeFile` or
`net.send`. The byte order is the `endian` field of a [`Reader`](#bytes.Reader) or [`Writer`](#bytes.Writer), which can be changed at any time.
Reading past the end is a ParseError that says where, and reads nothing. What is read as a String is a slice, sharing
the memory of the data. Signed numbers are two's complement and floats are IEEE 754.

## Types

### <a id="bytes.Endian"></a>`Endian`

```ec
declare type Endian as :big | :little
```

The order of the bytes of a number: `:big` puts the most significant byte first, `:little` the least significant.

### <a id="bytes.Writer"></a>`Writer`

```ec
mut ref rec Writer
```

Builds binary data, growing as needed: [`writer`](#bytes.writer).

**Fields**

- <a id="bytes.Writer.endian"></a>`endian: Endian` — The order of the bytes of the numbers written from now on.
- <a id="bytes.Writer.out"></a>`out: StringBuilder` — What is written so far.

### <a id="bytes.Reader"></a>`Reader`

```ec
mut ref rec Reader
```

Reads binary data from the start: [`reader`](#bytes.reader).

**Fields**

- <a id="bytes.Reader.endian"></a>`endian: Endian` — The order of the bytes of the numbers read from now on.
- <a id="bytes.Reader.data"></a>`data: String` — All the data.
- <a id="bytes.Reader.at"></a>`at: Size` — The byte to read next.

## Functions

### <a id="bytes.floatBits"></a>`floatBits`

```ec
fn floatBits(x: Float): UInt
```

The bits of `x`, as IEEE 754: `floatBits(1.0)` is 0x3F800000.

### <a id="bytes.floatFromBits"></a>`floatFromBits`

```ec
fn floatFromBits(bits: UInt): Float
```

The Float with the IEEE 754 bits `bits`.

### <a id="bytes.doubleBits"></a>`doubleBits`

```ec
fn doubleBits(x: Double): ULong
```

The bits of `x`, as IEEE 754: `doubleBits(1.0)` is 0x3FF0000000000000.

### <a id="bytes.doubleFromBits"></a>`doubleFromBits`

```ec
fn doubleFromBits(bits: ULong): Double
```

The Double with the IEEE 754 bits `bits`.

### <a id="bytes.writer"></a>`writer`

```ec
fn writer(endian: Endian = :big, capacity: Size = 64): Writer
```

A new empty [`Writer`](#bytes.Writer), in the current pool.

**Parameters**

- `endian` — the byte order
- `capacity` — how many bytes to make room for at first

### <a id="bytes.position"></a>`position`

```ec
fn position(w: Writer): Size
```

How many bytes are written so far, which is where the next one goes.

### <a id="bytes.finish"></a>`finish`

```ec
fn finish(w: Writer): String
```

All that is written, as a String. It shares the memory of `w`, so copy it with `clone` to go on writing after this.

### <a id="bytes.clear"></a>`clear`

```ec
fn clear(w: Writer)
```

Forgets what is written, keeping the memory.

### <a id="bytes.writeU8"></a>`writeU8`

```ec
fn writeU8(w: Writer, x: UChar)
```

Writes one byte.

### <a id="bytes.writeU16"></a>`writeU16`

```ec
fn writeU16(w: Writer, x: UShort)
```

Writes a number in 2 bytes.

### <a id="bytes.writeU32"></a>`writeU32`

```ec
fn writeU32(w: Writer, x: UInt)
```

Writes a number in 4 bytes.

### <a id="bytes.writeU64"></a>`writeU64`

```ec
fn writeU64(w: Writer, x: ULong)
```

Writes a number in 8 bytes.

### <a id="bytes.writeI8"></a>`writeI8`

```ec
fn writeI8(w: Writer, x: Char)
```

Writes a signed byte.

### <a id="bytes.writeI16"></a>`writeI16`

```ec
fn writeI16(w: Writer, x: Short)
```

Writes a signed number in 2 bytes, in two's complement.

### <a id="bytes.writeI32"></a>`writeI32`

```ec
fn writeI32(w: Writer, x: Int)
```

Writes a signed number in 4 bytes, in two's complement.

### <a id="bytes.writeI64"></a>`writeI64`

```ec
fn writeI64(w: Writer, x: Long)
```

Writes a signed number in 8 bytes, in two's complement.

### <a id="bytes.writeF32"></a>`writeF32`

```ec
fn writeF32(w: Writer, x: Float)
```

Writes a Float in 4 bytes, as IEEE 754.

### <a id="bytes.writeF64"></a>`writeF64`

```ec
fn writeF64(w: Writer, x: Double)
```

Writes a Double in 8 bytes, as IEEE 754.

### <a id="bytes.writeString"></a>`writeString`

```ec
fn writeString(w: Writer, s: String)
```

Writes the bytes of `s` as they are, with nothing to say how many there are.

### <a id="bytes.writeCString"></a>`writeCString`

```ec
fn writeCString(w: Writer, s: String)
```

Writes the bytes of `s` and a 0 byte after them, as a C string; see [`readCString`](#bytes.readCString).

### <a id="bytes.writeZeros"></a>`writeZeros`

```ec
fn writeZeros(w: Writer, n: Size)
```

Writes `n` zero bytes, for padding.

### <a id="bytes.alignTo"></a>`alignTo`

```ec
fn alignTo(w: Writer, alignment: Size)
```

Writes zero bytes until the position is a multiple of `alignment`, which must not be 0.

### <a id="bytes.writeVarint"></a>`writeVarint`

```ec
fn writeVarint(w: Writer, x: ULong)
```

Writes a number in as many bytes as it needs: LEB128, as in Protocol Buffers, WebAssembly and DWARF. There are 7 bits
in each byte, the lowest first, whatever `w.endian` is.

### <a id="bytes.writeSignedVarint"></a>`writeSignedVarint`

```ec
fn writeSignedVarint(w: Writer, x: Long)
```

Writes a signed number with [`writeVarint`](#bytes.writeVarint), after a zigzag step, so that small negative numbers stay short.

### <a id="bytes.setU8"></a>`setU8`

```ec
fn setU8(w: Writer, at: Size, x: UChar)
```

Changes a byte written before, for what is only known later. The program stops when `at` isn't written yet.

**Parameters**

- `w` — the writer
- `at` — the position of the byte, as [`position`](#bytes.position) gave it
- `x` — the new value

### <a id="bytes.setU16"></a>`setU16`

```ec
fn setU16(w: Writer, at: Size, x: UShort)
```

Changes 2 bytes written before, as [`setU8`](#bytes.setU8) does.

### <a id="bytes.setU32"></a>`setU32`

```ec
fn setU32(w: Writer, at: Size, x: UInt)
```

Changes 4 bytes written before, as [`setU8`](#bytes.setU8) does: a length in front of what it measures, say.

### <a id="bytes.setU64"></a>`setU64`

```ec
fn setU64(w: Writer, at: Size, x: ULong)
```

Changes 8 bytes written before, as [`setU8`](#bytes.setU8) does.

### <a id="bytes.reader"></a>`reader`

```ec
fn reader(data: String, endian: Endian = :big): Reader
```

A [`Reader`](#bytes.Reader) at the start of `data`, in the current pool.

**Parameters**

- `data` — what to read
- `endian` — the byte order

### `position`

```ec
fn position(r: Reader): Size
```

The byte to read next.

### <a id="bytes.remaining"></a>`remaining`

```ec
fn remaining(r: Reader): Size
```

How many bytes are left to read.

### <a id="bytes.isAtEnd"></a>`isAtEnd`

```ec
fn isAtEnd(r: Reader): Bool
```

Whether everything is read.

### <a id="bytes.readU8"></a>`readU8`

```ec
fn readU8(r: Reader): UChar throws ParseError
```

Reads one byte.

**Throws**

- `ParseError` — when the data has ended

### <a id="bytes.readU16"></a>`readU16`

```ec
fn readU16(r: Reader): UShort throws ParseError
```

Reads a number from 2 bytes.

**Throws**

- `ParseError` — when the data has ended

### <a id="bytes.readU32"></a>`readU32`

```ec
fn readU32(r: Reader): UInt throws ParseError
```

Reads a number from 4 bytes.

**Throws**

- `ParseError` — when the data has ended

### <a id="bytes.readU64"></a>`readU64`

```ec
fn readU64(r: Reader): ULong throws ParseError
```

Reads a number from 8 bytes.

**Throws**

- `ParseError` — when the data has ended

### <a id="bytes.readI8"></a>`readI8`

```ec
fn readI8(r: Reader): Char throws ParseError
```

Reads a signed byte.

**Throws**

- `ParseError` — when the data has ended

### <a id="bytes.readI16"></a>`readI16`

```ec
fn readI16(r: Reader): Short throws ParseError
```

Reads a signed number from 2 bytes, in two's complement.

**Throws**

- `ParseError` — when the data has ended

### <a id="bytes.readI32"></a>`readI32`

```ec
fn readI32(r: Reader): Int throws ParseError
```

Reads a signed number from 4 bytes, in two's complement.

**Throws**

- `ParseError` — when the data has ended

### <a id="bytes.readI64"></a>`readI64`

```ec
fn readI64(r: Reader): Long throws ParseError
```

Reads a signed number from 8 bytes, in two's complement.

**Throws**

- `ParseError` — when the data has ended

### <a id="bytes.readF32"></a>`readF32`

```ec
fn readF32(r: Reader): Float throws ParseError
```

Reads a Float from 4 bytes, as IEEE 754.

**Throws**

- `ParseError` — when the data has ended

### <a id="bytes.readF64"></a>`readF64`

```ec
fn readF64(r: Reader): Double throws ParseError
```

Reads a Double from 8 bytes, as IEEE 754.

**Throws**

- `ParseError` — when the data has ended

### <a id="bytes.read"></a>`read`

```ec
fn read(r: Reader, n: Size): String throws ParseError
```

Reads `n` bytes, as a slice of the data.

**Throws**

- `ParseError` — when fewer are left

### <a id="bytes.readRest"></a>`readRest`

```ec
fn readRest(r: Reader): String
```

Reads all that is left, as a slice of the data.

### <a id="bytes.readCString"></a>`readCString`

```ec
fn readCString(r: Reader): String throws ParseError
```

Reads up to the next 0 byte, and goes past it, as [`writeCString`](#bytes.writeCString) wrote it.

**Returns** the bytes before the 0, as a slice of the data

**Throws**

- `ParseError` — when there is no 0 byte

### <a id="bytes.readVarint"></a>`readVarint`

```ec
fn readVarint(r: Reader): ULong throws ParseError
```

Reads a number that [`writeVarint`](#bytes.writeVarint) wrote. When that fails, `r` stays where it was.

**Throws**

- `ParseError` — when the data ends inside the number, or the number doesn't fit 64 bits

### <a id="bytes.readSignedVarint"></a>`readSignedVarint`

```ec
fn readSignedVarint(r: Reader): Long throws ParseError
```

Reads a number that [`writeSignedVarint`](#bytes.writeSignedVarint) wrote.

**Throws**

- `ParseError` — when the data ends inside the number, or the number doesn't fit 64 bits

### <a id="bytes.skip"></a>`skip`

```ec
fn skip(r: Reader, n: Size) throws ParseError
```

Goes `n` bytes forward.

**Throws**

- `ParseError` — when fewer are left

### <a id="bytes.seek"></a>`seek`

```ec
fn seek(r: Reader, at: Size) throws ParseError
```

Goes to byte `at` of the data, forward or back.

**Throws**

- `ParseError` — when the data is shorter than that; `r.data.len` is the end, and allowed

### <a id="bytes.peek"></a>`peek`

```ec
fn peek(r: Reader): UChar?
```

The next byte, without going past it.

**Returns** the byte, or null at the end
