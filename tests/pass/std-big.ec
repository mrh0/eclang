use "core:io.ec"
use "core:big.ec"

fn main(): Int throws Error do
    var n = big.integer(1)
    for i in 1..=30 do
        n = n.mul(big.integer(i))
    io.log("${n}")
    val x = try big.parse("123456789012345678901234567890")
    io.log("${x.div(n)} ${x.rem(n)}")
    io.log(x.text(16))
    io.log(big.integer(255).text(2))
    io.log(big.integer(-255).text(36))
    for text in ["-1_000", "+7", "0", "-0", "000123"] do
        io.log("${try big.parse(text)}")
    val hex = try big.parse("ff_FF", 16)
    io.log("${hex}")

    val min = big.integer(-9223372036854775807 - 1)
    io.log("${min}")
    io.log(min.fitsLong())
    io.log(try min.toLong())
    io.log(min.sub(1).fitsLong())
    try min.sub(1).toLong() catch e do
        io.log(e.message)
    io.log("${big.integer(0xFFFFFFFFFFFFFFFF)}")
    io.log(big.integer(3).pow(100).toDouble())
    io.log("${big.integer(2).pow(127).sub(1)}")
    io.log("${big.powMod(big.integer(4), big.integer(13), big.integer(497))}")
    io.log("${big.gcd(big.integer(-48), big.integer(18))}")
    io.log("${big.integer(-7).mod(big.integer(3))} ${big.integer(-7).rem(big.integer(3))} ${big.integer(-7).div(big.integer(3))}")
    io.log("${big.integer(-5).shr(1)} ${big.integer(1).shl(100)}")
    io.log(big.integer(-5).sign())
    io.log(big.zero.isZero())
    io.log(big.integer(10).equals(10))
    io.log(big.integer(10).compare(11))
    for bad in ["", "-", "12a", "1__2", "1_"] do
        try big.parse(bad) catch e do
            io.log("'${bad}': ${e.message} at ${e.position}")

    val price = try big.parseDecimal("19.99")
    val total = price.mul(big.decimal(3)).add(try big.parseDecimal("0.05"))
    io.log("${total}")
    val each = total.div(big.decimal(7), 2)
    io.log("${each}")
    io.log("${each.round(1, :halfUp)}")
    io.log("${big.decimal(150, 2)} ${big.decimal(-5, 3)} ${big.decimal(42)}")
    io.log("${big.decimal(1).div(big.decimal(3), 10)}")
    for text in ["1.5e3", "25e-2", "-.5", "7.", "+0.000", "1_000.25", "1E+2"] do
        io.log("${try big.parseDecimal(text)}")
    io.log("${big.decimal(25, 1).round(0)} ${big.decimal(35, 1).round(0)} ${big.decimal(-25, 1).round(0, :halfUp)} ${big.decimal(-21, 1).round(0, :floor)}")
    io.log(big.decimal(15, 1).equals(big.decimal(150, 2)))
    io.log("${big.decimal(15000, 4).stripped()} ${big.decimal(200, 2).stripped()}")
    io.log("${big.decimal(-1999, 2).toInteger()}")
    io.log(big.decimal(-1999, 2).toDouble())
    for bad in ["", ".", "1.2.3", "1e", "1ex", "--1", "1__0", "2_"] do
        try big.parseDecimal(bad) catch e do
            io.log("'${bad}': ${e.message} at ${e.position}")
    ret 0
