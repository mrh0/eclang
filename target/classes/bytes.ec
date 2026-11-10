/**
 * Binary data: reading and writing numbers and strings in a given byte order, for network protocols and file formats.
 *
 *     use "core:bytes.ec"
 *     val w = bytes.writer()                              // big-endian, the order of network protocols
 *     bytes.writeU16(w, 0xCAFE)
 *     val size = bytes.position(w)                        // come back to this later
 *     bytes.writeU32(w, 0)
 *     bytes.writeString(w, "hello")
 *     bytes.setU32(w, size, 5)
 *     val data = bytes.finish(w)                          // a String of 11 bytes
 *
 *     val r = bytes.reader(data)
 *     val magic = try bytes.readU16(r)                    // 0xCAFE
 *     val n = try bytes.readU32(r)                        // 5
 *     val text = try bytes.read(r, n as Size)             // "hello"
 *     io.log(bytes.remaining(r))                          // 0
 *
 *     val bmp = bytes.reader(try io.readFile("a.bmp"), :little)
 *
 * A String is bytes, so data is a String, read with `io.readFile` or `net.read`, and written with `io.writeFile` or
 * `net.send`. The byte order is the `endian` field of a {@link Reader} or {@link Writer}, which can be changed at any time.
 * Reading past the end is a ParseError that says where, and reads nothing. What is read as a String is a slice, sharing
 * the memory of the data. Signed numbers are two's complement and floats are IEEE 754.
 */
module "bytes"
use "core:errors.ec"
use "core:string.ec"

/** The order of the bytes of a number: `:big` puts the most significant byte first, `:little` the least significant. */
declare type Endian as :big | :little

// ---------------------------------------------------------------------------------------------------
// Bit patterns

/** The bits of a Float. */
priv union FloatBits as
    value: Float
    bits: UInt

/** The bits of a Double. */
priv union DoubleBits as
    value: Double
    bits: ULong

/** The bits of `x`, as IEEE 754: `floatBits(1.0)` is 0x3F800000. */
fn floatBits(x: Float): UInt do
    val u = FloatBits { value = x }
    ret u.bits

/** The Float with the IEEE 754 bits `bits`. */
fn floatFromBits(bits: UInt): Float do
    val u = FloatBits { bits = bits }
    ret u.value

/** The bits of `x`, as IEEE 754: `doubleBits(1.0)` is 0x3FF0000000000000. */
fn doubleBits(x: Double): ULong do
    val u = DoubleBits { value = x }
    ret u.bits

/** The Double with the IEEE 754 bits `bits`. */
fn doubleFromBits(bits: ULong): Double do
    val u = DoubleBits { bits = bits }
    ret u.value

// ---------------------------------------------------------------------------------------------------
// Writing

/** Builds binary data, growing as needed: {@link writer}. */
mut ref rec Writer as
    /** The order of the bytes of the numbers written from now on. */
    endian: Endian
    /** What is written so far. */
    out: StringBuilder

/**
 * A new empty {@link Writer}, in the current pool.
 * @param endian the byte order
 * @param capacity how many bytes to make room for at first
 */
fn writer(endian: Endian = :big, capacity: Size = 64): Writer = new Writer { endian, stringBuilder(capacity) }

/** How many bytes are written so far, which is where the next one goes. */
fn position(w: Writer): Size = w.out.len

/** All that is written, as a String. It shares the memory of `w`, so copy it with `clone` to go on writing after this. */
fn finish(w: Writer): String = w.out.build()

/** Forgets what is written, keeping the memory. */
fn clear(w: Writer) do
    w.out.clear()

/** The byte number `i` of `n`, counted from the least significant one. */
priv fn byteOf(n: ULong, i: Size): Char = ((n >> ((i * 8) as Int)) & 0xFF) as Char

/** Adds the lowest `count` bytes of `n`. */
priv fn writeNumber(w: Writer, n: ULong, count: Size) do
    for i in 0..<count do
        w.out.append(byteOf(n, if w.endian == :big do count - 1 - i else i))

/** Writes one byte. */
fn writeU8(w: Writer, x: UChar) do
    writeNumber(w, x as ULong, 1)

/** Writes a number in 2 bytes. */
fn writeU16(w: Writer, x: UShort) do
    writeNumber(w, x as ULong, 2)

/** Writes a number in 4 bytes. */
fn writeU32(w: Writer, x: UInt) do
    writeNumber(w, x as ULong, 4)

/** Writes a number in 8 bytes. */
fn writeU64(w: Writer, x: ULong) do
    writeNumber(w, x, 8)

/** Writes a signed byte. */
fn writeI8(w: Writer, x: Char) do
    writeNumber(w, (x as UChar) as ULong, 1)

/** Writes a signed number in 2 bytes, in two's complement. */
fn writeI16(w: Writer, x: Short) do
    writeNumber(w, (x as UShort) as ULong, 2)

/** Writes a signed number in 4 bytes, in two's complement. */
fn writeI32(w: Writer, x: Int) do
    writeNumber(w, (x as UInt) as ULong, 4)

/** Writes a signed number in 8 bytes, in two's complement. */
fn writeI64(w: Writer, x: Long) do
    writeNumber(w, x as ULong, 8)

/** Writes a Float in 4 bytes, as IEEE 754. */
fn writeF32(w: Writer, x: Float) do
    writeNumber(w, floatBits(x) as ULong, 4)

/** Writes a Double in 8 bytes, as IEEE 754. */
fn writeF64(w: Writer, x: Double) do
    writeNumber(w, doubleBits(x), 8)

/** Writes the bytes of `s` as they are, with nothing to say how many there are. */
fn writeString(w: Writer, s: String) do
    w.out.append(s)

/** Writes the bytes of `s` and a 0 byte after them, as a C string; see {@link readCString}. */
fn writeCString(w: Writer, s: String) do
    w.out.append(s)
    w.out.append(0 as Char)

/** Writes `n` zero bytes, for padding. */
fn writeZeros(w: Writer, n: Size) do
    for i in 0..<n do
        w.out.append(0 as Char)

/** Writes zero bytes until the position is a multiple of `alignment`, which must not be 0. */
fn alignTo(w: Writer, alignment: Size) do
    val over = position(w) % alignment
    writeZeros(w, alignment - over) if over != 0

/**
 * Writes a number in as many bytes as it needs: LEB128, as in Protocol Buffers, WebAssembly and DWARF. There are 7 bits
 * in each byte, the lowest first, whatever `w.endian` is.
 */
fn writeVarint(w: Writer, x: ULong) do
    var rest = x
    while rest >= 0x80 do
        w.out.append(((rest & 0x7F) | 0x80) as Char)
        rest = rest >> 7
    w.out.append(rest as Char)

/** Writes a signed number with {@link writeVarint}, after a zigzag step, so that small negative numbers stay short. */
fn writeSignedVarint(w: Writer, x: Long) do
    writeVarint(w, ((x as ULong) << 1) ^ (if x < 0 do ~(0 as ULong) else 0 as ULong))

/** Replaces the `count` bytes at `at` with the lowest bytes of `n`. The program stops when they aren't all written yet. */
priv fn setNumber(w: Writer, at: Size, n: ULong, count: Size) do
    assert at + count <= w.out.len, "bytes.set* can only change what is already written"
    for i in 0..<count do
        w.out.buffer[at + i] = byteOf(n, if w.endian == :big do count - 1 - i else i)

/**
 * Changes a byte written before, for what is only known later. The program stops when `at` isn't written yet.
 * @param w the writer
 * @param at the position of the byte, as {@link position} gave it
 * @param x the new value
 */
fn setU8(w: Writer, at: Size, x: UChar) do
    setNumber(w, at, x as ULong, 1)

/** Changes 2 bytes written before, as {@link setU8} does. */
fn setU16(w: Writer, at: Size, x: UShort) do
    setNumber(w, at, x as ULong, 2)

/** Changes 4 bytes written before, as {@link setU8} does: a length in front of what it measures, say. */
fn setU32(w: Writer, at: Size, x: UInt) do
    setNumber(w, at, x as ULong, 4)

/** Changes 8 bytes written before, as {@link setU8} does. */
fn setU64(w: Writer, at: Size, x: ULong) do
    setNumber(w, at, x, 8)

// ---------------------------------------------------------------------------------------------------
// Reading

/** Reads binary data from the start: {@link reader}. */
mut ref rec Reader as
    /** The order of the bytes of the numbers read from now on. */
    endian: Endian
    /** All the data. */
    data: String
    /** The byte to read next. */
    at: Size

/**
 * A {@link Reader} at the start of `data`, in the current pool.
 * @param data what to read
 * @param endian the byte order
 */
fn reader(data: String, endian: Endian = :big): Reader = new Reader { endian, data, 0 }

/** The byte to read next. */
fn position(r: Reader): Size = r.at

/** How many bytes are left to read. */
fn remaining(r: Reader): Size = r.data.len - r.at

/** Whether everything is read. */
fn isAtEnd(r: Reader): Bool = r.at >= r.data.len

/**
 * Checks that `count` bytes are left.
 * @throws when they aren't
 */
priv fn need(r: Reader, count: Size) throws ParseError do
    throw parseError("the data ended early: reading ${count} byte(s) at position ${r.at}, with ${remaining(r)} left", r.data, r.at) if count > remaining(r)

/** Reads `count` bytes as a number. There are enough of them. */
priv fn readNumber(r: Reader, count: Size): ULong do
    var n: ULong = 0
    for i in 0..<count do
        val byte = (r.data[r.at + (if r.endian == :big do i else count - 1 - i)] as UChar) as ULong
        n = (n << 8) | byte
    r.at += count
    ret n

/**
 * Reads one byte.
 * @throws when the data has ended
 */
fn readU8(r: Reader): UChar throws ParseError do
    try need(r, 1)
    ret readNumber(r, 1) as UChar

/**
 * Reads a number from 2 bytes.
 * @throws when the data has ended
 */
fn readU16(r: Reader): UShort throws ParseError do
    try need(r, 2)
    ret readNumber(r, 2) as UShort

/**
 * Reads a number from 4 bytes.
 * @throws when the data has ended
 */
fn readU32(r: Reader): UInt throws ParseError do
    try need(r, 4)
    ret readNumber(r, 4) as UInt

/**
 * Reads a number from 8 bytes.
 * @throws when the data has ended
 */
fn readU64(r: Reader): ULong throws ParseError do
    try need(r, 8)
    ret readNumber(r, 8)

/**
 * Reads a signed byte.
 * @throws when the data has ended
 */
fn readI8(r: Reader): Char throws ParseError do
    try need(r, 1)
    ret readNumber(r, 1) as Char

/**
 * Reads a signed number from 2 bytes, in two's complement.
 * @throws when the data has ended
 */
fn readI16(r: Reader): Short throws ParseError do
    try need(r, 2)
    ret readNumber(r, 2) as Short

/**
 * Reads a signed number from 4 bytes, in two's complement.
 * @throws when the data has ended
 */
fn readI32(r: Reader): Int throws ParseError do
    try need(r, 4)
    ret readNumber(r, 4) as Int

/**
 * Reads a signed number from 8 bytes, in two's complement.
 * @throws when the data has ended
 */
fn readI64(r: Reader): Long throws ParseError do
    try need(r, 8)
    ret readNumber(r, 8) as Long

/**
 * Reads a Float from 4 bytes, as IEEE 754.
 * @throws when the data has ended
 */
fn readF32(r: Reader): Float throws ParseError do
    try need(r, 4)
    ret floatFromBits(readNumber(r, 4) as UInt)

/**
 * Reads a Double from 8 bytes, as IEEE 754.
 * @throws when the data has ended
 */
fn readF64(r: Reader): Double throws ParseError do
    try need(r, 8)
    ret doubleFromBits(readNumber(r, 8))

/**
 * Reads `n` bytes, as a slice of the data.
 * @throws when fewer are left
 */
fn read(r: Reader, n: Size): String throws ParseError do
    try need(r, n)
    val part = r.data[r.at..<r.at + n]
    r.at += n
    ret part

/** Reads all that is left, as a slice of the data. */
fn readRest(r: Reader): String do
    val part = r.data[r.at..<r.data.len]
    r.at = r.data.len
    ret part

/**
 * Reads up to the next 0 byte, and goes past it, as {@link writeCString} wrote it.
 * @return the bytes before the 0, as a slice of the data
 * @throws when there is no 0 byte
 */
fn readCString(r: Reader): String throws ParseError do
    var end = r.at
    while end < r.data.len and r.data[end] != 0 do
        end += 1
    throw parseError("the data ended early: the string at byte ${r.at} has no 0 byte after it", r.data, r.at) if end >= r.data.len
    val part = r.data[r.at..<end]
    r.at = end + 1
    ret part

/**
 * Reads a number that {@link writeVarint} wrote. When that fails, `r` stays where it was.
 * @throws when the data ends inside the number, or the number doesn't fit 64 bits
 */
fn readVarint(r: Reader): ULong throws ParseError do
    var n: ULong = 0
    var i: Size = 0
    while true do
        throw parseError("the data ended early: the number at byte ${r.at} isn't finished", r.data, r.at) if r.at + i >= r.data.len
        val byte = (r.data[r.at + i] as UChar) as ULong
        // The tenth byte holds the 64th bit, and nothing above it.
        throw parseError("the number at byte ${r.at} doesn't fit 64 bits", r.data, r.at) if i == 9 and byte > 1
        n = n | ((byte & 0x7F) << ((i * 7) as Int))
        i += 1
        break if byte & 0x80 == 0
    r.at += i
    ret n

/**
 * Reads a number that {@link writeSignedVarint} wrote.
 * @throws when the data ends inside the number, or the number doesn't fit 64 bits
 */
fn readSignedVarint(r: Reader): Long throws ParseError do
    val n = try readVarint(r)
    ret ((n >> 1) as Long) ^ (-((n & 1) as Long))

/**
 * Goes `n` bytes forward.
 * @throws when fewer are left
 */
fn skip(r: Reader, n: Size) throws ParseError do
    try need(r, n)
    r.at += n

/**
 * Goes to byte `at` of the data, forward or back.
 * @throws when the data is shorter than that; `r.data.len` is the end, and allowed
 */
fn seek(r: Reader, at: Size) throws ParseError do
    throw parseError("byte ${at} is past the end of the ${r.data.len} bytes of data", r.data, r.data.len) if at > r.data.len
    r.at = at

/**
 * The next byte, without going past it.
 * @return the byte, or null at the end
 */
fn peek(r: Reader): UChar? do
    ret null if isAtEnd(r)
    ret r.data[r.at] as UChar
