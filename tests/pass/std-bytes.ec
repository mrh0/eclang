use "core:io.ec"
use "core:string.ec"
use "core:encoding.ec"
use "core:bytes.ec"

fn dump(w: @bytes.Writer) do
    io.log(encoding.hex(bytes.finish(w)))
    bytes.clear(w)

fn main(): Int throws Error do
    // Every width, in both orders.
    val w = bytes.writer()
    bytes.writeU8(w, 0xAB)
    bytes.writeU16(w, 0x0102)
    bytes.writeU32(w, 0x01020304)
    bytes.writeU64(w, 0x0102030405060708ul)
    dump(w)
    w.endian = :little
    bytes.writeU8(w, 0xAB)
    bytes.writeU16(w, 0x0102)
    bytes.writeU32(w, 0x01020304)
    bytes.writeU64(w, 0x0102030405060708ul)
    dump(w)

    // Signed numbers, in two's complement.
    w.endian = :big
    bytes.writeI8(w, -1)
    bytes.writeI16(w, -2)
    bytes.writeI32(w, -3)
    bytes.writeI64(w, -4)
    val signed = bytes.reader(clone(bytes.finish(w)))
    dump(w)
    io.log((try bytes.readI8(signed)) as Int)
    io.log(try bytes.readI16(signed))
    io.log(try bytes.readI32(signed))
    io.log(try bytes.readI64(signed))
    io.log(bytes.isAtEnd(signed))

    // The largest values come back whole.
    bytes.writeU64(w, ~(0 as ULong))
    bytes.writeI64(w, -9223372036854775807 - 1)
    bytes.writeU32(w, 0xFFFFFFFFu)
    bytes.writeI16(w, -32768)
    val limits = bytes.reader(clone(bytes.finish(w)))
    bytes.clear(w)
    io.log(try bytes.readU64(limits))
    io.log(try bytes.readI64(limits))
    io.log(try bytes.readU32(limits))
    io.log(try bytes.readI16(limits))

    // Floats.
    bytes.writeF32(w, 1.5)
    bytes.writeF64(w, -2.25)
    val floats = bytes.reader(clone(bytes.finish(w)))
    dump(w)
    io.log(try bytes.readF32(floats))
    io.log(try bytes.readF64(floats))
    io.log("${bytes.floatBits(1.0):x}")
    io.log("${bytes.doubleBits(1.0):x}")
    io.log(bytes.floatFromBits(0x40490FDBu))
    w.endian = :little
    bytes.writeF32(w, 1.5)
    dump(w)
    w.endian = :big

    // Strings.
    bytes.writeString(w, "ab")
    bytes.writeCString(w, "cd")
    bytes.writeCString(w, "")
    bytes.writeZeros(w, 2)
    bytes.alignTo(w, 4)
    io.log(bytes.position(w))
    bytes.alignTo(w, 4)
    io.log(bytes.position(w))
    val strings = bytes.reader(clone(bytes.finish(w)))
    dump(w)
    io.log(try bytes.read(strings, 2))
    io.log(try bytes.readCString(strings))
    io.log((try bytes.readCString(strings)).len)
    io.log(bytes.peek(strings) ?? 99)
    try bytes.skip(strings, 1)
    io.log(bytes.remaining(strings))
    try bytes.seek(strings, 1)
    io.log(bytes.readRest(strings).len)
    io.log(bytes.peek(strings) == null)

    // Filling in a length afterwards.
    bytes.writeU8(w, 7)
    val at = bytes.position(w)
    bytes.writeU32(w, 0)
    bytes.writeU16(w, 0)
    bytes.writeString(w, "hello")
    bytes.setU32(w, at, 5)
    bytes.setU16(w, at + 4, 0xBEEF)
    bytes.setU8(w, 0, 8)
    dump(w)
    w.endian = :little
    bytes.writeU64(w, 0)
    bytes.setU64(w, 0, 0x0102030405060708ul)
    dump(w)
    w.endian = :big

    // Varints: the same bytes as Protocol Buffers.
    for n in [0ul, 1ul, 127ul, 128ul, 300ul, ~(0 as ULong)] do
        bytes.writeVarint(w, n)
        val r = bytes.reader(clone(bytes.finish(w)))
        dump(w)
        io.log(try bytes.readVarint(r))
    for n in [0l, -1l, 1l, -2l, 63l, -64l, 64l, 9223372036854775807l, -9223372036854775807l - 1] do
        bytes.writeSignedVarint(w, n)
        val r2 = bytes.reader(clone(bytes.finish(w)))
        dump(w)
        io.log(try bytes.readSignedVarint(r2))

    // Errors say where, and read nothing.
    val short = bytes.reader("\x01\x02\x03")
    try bytes.readU32(short) catch e do
        io.log(e.message)
        io.log(e.position)
    io.log(bytes.position(short))
    io.log(try bytes.readU16(short))
    try bytes.read(short, 2) catch e2 do
        io.log(e2.message)
    try bytes.skip(short, 2) catch e3 do
        io.log(e3.message)
    try bytes.seek(short, 4) catch e4 do
        io.log(e4.message)
    try bytes.seek(short, 3)
    try bytes.readU8(short) catch e5 do
        io.log(e5.message)
    try bytes.readCString(bytes.reader("abc")) catch e6 do
        io.log(e6.message)
    try bytes.readVarint(bytes.reader("\x80\x80")) catch e7 do
        io.log(e7.message)
    val long = bytes.reader("\xFF\xFF\xFF\xFF\xFF\xFF\xFF\xFF\xFF\x02")
    try bytes.readVarint(long) catch e8 do
        io.log(e8.message)
    io.log(bytes.position(long))

    // The order of a reader can change on the way.
    val mixed = bytes.reader("\x00\x01\x01\x00", :little)
    mixed.endian = :big
    io.log(try bytes.readU16(mixed))
    mixed.endian = :little
    io.log(try bytes.readU16(mixed))
    ret 0
