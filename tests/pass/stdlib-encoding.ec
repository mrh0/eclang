use "core:io.ec"
use "core:encoding.ec"
use "core:string.ec"
use "core:math.ec"

fn main(): Int do
    io.log(encoding.hex("\xffA"))
    io.log(encoding.fromHex("48694A") ?? "bad")
    io.log(encoding.fromHex("4") ?? "odd")
    io.log(encoding.fromHex("zz") ?? "not hex")
    for s in ["", "f", "fo", "foo", "foob", "fooba", "foobar"] do
        val text = encoding.base64(s)
        io.log(text)
        io.log((encoding.fromBase64(text) ?? "bad") == s)
    io.log(encoding.base64("\xff\xfe\x00"))
    io.log((encoding.fromBase64("//4A") ?? "bad") == "\xff\xfe\x00")
    io.log(encoding.fromBase64("a") ?? "short")
    io.log(encoding.fromBase64("a!==") ?? "bad letter")
    io.log(encoding.urlEscape("a b&c/é-_.~"))
    io.log(encoding.urlUnescape("a%20b%26c") ?? "bad")
    io.log(encoding.urlUnescape("a+b", true) ?? "bad")
    io.log(encoding.urlUnescape("a%2") ?? "cut off")

    // The smallest Long is a number too.
    io.log(parseInt("-9223372036854775808") ?? 0)
    io.log(parseInt("9223372036854775807") ?? 0)
    io.log(parseInt("9223372036854775808") ?? -1)
    io.log(math.nextPowerOfTwo(1000))
    io.log(math.variance([2.0, 4.0, 4.0, 4.0, 5.0, 5.0, 7.0, 9.0]))
    io.log(math.stdDev([2.0, 4.0, 4.0, 4.0, 5.0, 5.0, 7.0, 9.0]))
    ret 0
