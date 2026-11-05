use "core:io.ec"
use "core:string.ec"
use "core:unicode.ec"

fn show(s: String) do
    val out = stringBuilder()
    for cp in unicode.codePoints(s) do
        out.append(" ${cp:x}")
    io.log("${s.len} bytes:${out.build()}")

fn main(): Int throws Error do
    // Going through code points, by value and with their byte.
    show("aé€😀")
    for at, cp in unicode.codePoints("aé€😀") do
        io.log("${at}: ${cp:x}")
    val all = unicode.toArray("é€")
    io.log(all.len)
    io.log(unicode.encode(all))

    // Encoding, at each length and at the edges.
    for cp in [0x7Fu, 0x80u, 0x7FFu, 0x800u, 0xFFFFu, 0x10000u, 0x10FFFFu] do
        val text = unicode.encode(cp)
        val (back, n) = unicode.decode(text, 0)
        io.log("${cp:x} ${unicode.encodedLength(cp)} ${n} ${back == cp}")
    // What UTF-8 can't hold becomes U+FFFD.
    show(unicode.encode(0xD800))
    show(unicode.encode(0x110000))
    io.log(unicode.encodedLength(0x110000))

    // Everything UTF-8 forbids is broken, one byte at a time.
    for bad in ["\xC0\x80", "\xC1\xBF", "\xE0\x80\x80", "\xED\xA0\x80", "\xF0\x80\x80\x80", "\xF4\x90\x80\x80", "\xF5\x80\x80\x80", "\xE2\x82", "\x80", "\xFF"] do
        io.log("${unicode.isValid(bad)} ${unicode.length(bad)} ${isUtf8(bad)} ${unicode.firstInvalid(bad) ?? 99}")
    // A real U+FFFD isn't a broken byte.
    io.log(unicode.isValid("�"))
    io.log(unicode.firstInvalid("ab\xFFcd") ?? 99)
    io.log(unicode.firstInvalid("ab") == null)
    io.log(unicode.validate("héllo") ?? "bad")
    try unicode.validate("h\xFFi") catch e do
        io.log(e.message)
        io.log(e.position)

    // Repairing.
    io.log(unicode.sanitize("ok"))
    show(unicode.sanitize("a\xFFb\xE2\x82c"))
    io.log(unicode.isValid(unicode.sanitize("\xC0\x80\xED\xA0\x80")))

    // Going backwards.
    val text = "aé€😀"
    var at = text.len
    while at > 0 do
        val (cp, n) = unicode.decodeBefore(text, at)
        io.log("${cp:x} ${n}")
        at -= n
    val (broken, n1) = unicode.decodeBefore("a\x80", 2)
    io.log("${broken:x} ${n1}")
    val (cut, n2) = unicode.decodeBefore("\xE2\x82", 2)
    io.log("${cut:x} ${n2}")
    val (tooMany, n3) = unicode.decodeBefore("\x80\x80\x80\x80\x80", 5)
    io.log("${tooMany:x} ${n3}")

    // Counting and cutting.
    io.log(unicode.length("héllo"))
    io.log(unicode.length(""))
    io.log(unicode.byteOffset("héllo", 2) ?? 99)
    io.log(unicode.byteOffset("héllo", 5) ?? 99)
    io.log(unicode.byteOffset("héllo", 6) ?? 99)
    io.log(unicode.slice("héllo", 1, 3))
    io.log(unicode.slice("héllo", 3, 100))
    io.log(unicode.slice("héllo", 100, 200).len)
    io.log(unicode.slice("héllo", 3, 2).len)
    io.log(unicode.truncate("aé€😀", 100))
    for max in 0..=10 do
        val part = unicode.truncate("aé€😀", max as Size)
        io.log("${max} ${part.len} ${unicode.isValid(part)}")

    // Surrogates.
    io.log(unicode.joinSurrogates(0xD83D, 0xDE00) == 0x1F600)
    val (high, low) = unicode.splitSurrogates(0x1F600)
    io.log("${high:x} ${low:x}")
    io.log(unicode.isScalar(0xD7FF))
    io.log(unicode.isScalar(0xDFFF))
    io.log(unicode.isScalar(0x10FFFF))
    io.log(unicode.isScalar(0x110000))

    // Classes.
    io.log(unicode.isSpace(0x20) and unicode.isSpace(0x3000) and unicode.isSpace(0xA0))
    io.log(unicode.isSpace(0x41))
    io.log(unicode.isAscii("abc"))
    io.log(unicode.isAscii("é"))
    io.log(unicode.width(0x41))
    io.log(unicode.width(0x4E2D))
    io.log(unicode.width(0x301))
    io.log(unicode.width("a中é"))
    ret 0
