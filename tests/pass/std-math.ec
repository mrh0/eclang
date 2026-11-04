use "core:io.ec"
use "core:math.ec"

// core:math.ec: math.h, generic min/max, whole-number helpers, checked and wrapping arithmetic, random numbers.
fn main(): Int do
    io.log(math.sqrt(2.0))
    io.log(math.sqrt(2.0f))
    io.log(math.PI)
    io.log(math.log(math.E))
    io.log(math.pow(2.0, 10.0))
    io.log(math.atan2(1.0, 1.0) * 4.0)
    io.log(math.floor(-2.5))
    io.log(math.round(2.5))
    io.log(math.abs(-3))
    io.log(math.abs(-3.5))
    io.log(math.isNan(math.NAN))
    io.log(math.isInf(math.INFINITY))
    io.log(math.isFinite(1.0))
    io.log(math.degrees(math.PI))
    io.log(math.lerp(10.0, 20.0, 0.25))
    io.log(math.roundTo(math.PI, 2))
    io.log(math.approxEqual(0.1 + 0.2, 0.3))

    io.log(math.min(3, 4))
    io.log(math.max(3.5, 1.5))
    io.log(math.clamp(15, 0, 10))
    io.log(math.sign(-7))
    io.log(math.minOf([4, 9, 2]))
    io.log(math.maxOf([4.5, 9.5, 2.5]))
    io.log(math.sum([1, 2, 3]))
    io.log(math.mean([1.0, 2.0, 4.0]))

    io.log(math.gcd(12, -18))
    io.log(math.lcm(4, 6))
    io.log(math.isPrime(97))
    io.log(math.isPrime(91))
    io.log(math.isPowerOfTwo(64))
    io.log(math.nextPowerOfTwo(100))
    io.log(math.floorDiv(-7, 2))
    io.log(math.floorMod(-7, 3))

    io.log(math.checkedMul(3, 4) ?? -1)
    try math.checkedMul(9000000000, 9000000000) catch e do
        io.log(e.message)
    io.log(math.checkedAdd(math.INT_MAX, 1) ?? -1)
    io.log(math.toInt(5000000000) ?? -1)
    io.log(math.toLong(2.6) ?? 0)
    io.log(math.wrappingMul(0xffffffffffl as ULong, 0xffffffffffl as ULong))
    io.log(math.LONG_MIN)

    // PCG32's reference stream for seed 42, stream 54: a15c02b7 7b47f409 ba1d3330.
    var r = math.random(42, 54)
    val a = math.nextUInt(addrof(r))
    val b = math.nextUInt(addrof(r))
    val c = math.nextUInt(addrof(r))
    printf("%x %x %x\n"c, a, b, c)
    var dice = math.random(7)
    var inRange = true
    for i in 0..<1000 do
        val v = math.nextInt(addrof(dice), 1, 7)
        val d = math.nextDouble(addrof(dice))
        if v < 1 or v > 6 or d < 0.0 or d >= 1.0 do
            inRange = false
    io.log(inRange)
    val xs = [1, 2, 3, 4, 5]
    math.shuffle(addrof(dice), xs)
    io.log(math.sum(xs))
    math.seed(1)
    val first = math.randomInt(0, 1000000)
    math.seed(1)
    io.log(math.randomInt(0, 1000000) == first)
    val bytes = new UChar[16]
    try math.secureRandom(bytes) catch e do
        io.log(e.message)
    ret 0
