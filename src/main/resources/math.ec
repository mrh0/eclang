/**
 * Maths: constants, C's math.h, whole-number helpers, checked arithmetic, and random numbers.
 *
 *     use "core:math.ec"
 *     val r = math.sqrt(2.0)
 *     val angle = math.radians(90.0)
 *     val n = try math.checkedMul(a, b)        // a RangeError instead of overflowing
 *     val die = math.randomInt(1, 7)            // 1 to 6
 *
 * The C functions take and return Doubles; most also have a Float overload. Programs using them link with -lm
 * (`eclang run` does).
 */
module "math"
use "core:errors.ec"
use "core:apr/general.ec"
use "math.h"
use "float.h"

/** π, half a turn in radians. */
const PI = 3.141592653589793
/** τ = 2π, a whole turn in radians. */
const TAU = 6.283185307179586
/** e, the base of the natural logarithm. */
const E = 2.718281828459045
/** The square root of 2. */
const SQRT2 = 1.4142135623730951
/** The natural logarithm of 2. */
const LN2 = 0.6931471805599453
/** The natural logarithm of 10. */
const LN10 = 2.302585092994046
/** φ, the golden ratio: (1 + √5) / 2. */
const PHI = 1.618033988749895

/** The smallest Int: -2147483648. */
const INT_MIN: Int = -2147483647 - 1
/** The largest Int: 2147483647. */
const INT_MAX: Int = 2147483647
/** The smallest Long: -9223372036854775808. */
const LONG_MIN: Long = -9223372036854775807 - 1
/** The largest Long: 9223372036854775807. */
const LONG_MAX: Long = 9223372036854775807

/** Positive infinity; `-INFINITY` is negative infinity. */
declare val INFINITY as Double
/**
 * "Not a number", the result of 0.0 / 0.0. It isn't equal to anything, itself included: test with {@link isNan}.
 */
declare val NAN as Double
/** The difference between 1.0 and the next Double after it. */
declare val DBL_EPSILON as Double
/** The largest finite Double. */
declare val DBL_MAX as Double
/** The smallest normal Double greater than 0. */
declare val DBL_MIN as Double

// ---------------------------------------------------------------------------------------------------
// math.h

/** The square root. */
declare fn sqrt as (x: Double): Double
/** The square root. */
declare fn sqrt extern "sqrtf" as (x: Float): Float
/** The cube root (negative for a negative `x`). */
declare fn cbrt as (x: Double): Double
/** `x` to the power `y`. */
declare fn pow as (x: Double, y: Double): Double
/** `x` to the power `y`. */
declare fn pow extern "powf" as (x: Float, y: Float): Float
/** The length of the hypotenuse, sqrt(x² + y²), without overflowing in between. */
declare fn hypot as (x: Double, y: Double): Double
/** e to the power `x`. */
declare fn exp as (x: Double): Double
/** 2 to the power `x`. */
declare fn exp2 as (x: Double): Double
/** e to the power `x`, less 1: precise when `x` is near 0. */
declare fn expm1 as (x: Double): Double
/** The natural logarithm (base e). `log` outside this module still prints. */
declare fn log as (x: Double): Double
/** The natural logarithm (base e). */
declare fn log extern "logf" as (x: Float): Float
/** The logarithm in base 2. */
declare fn log2 as (x: Double): Double
/** The logarithm in base 10. */
declare fn log10 as (x: Double): Double
/** The natural logarithm of 1 + `x`: precise when `x` is near 0. */
declare fn log1p as (x: Double): Double

/** The sine of `x` radians. */
declare fn sin as (x: Double): Double
/** The sine of `x` radians. */
declare fn sin extern "sinf" as (x: Float): Float
/** The cosine of `x` radians. */
declare fn cos as (x: Double): Double
/** The cosine of `x` radians. */
declare fn cos extern "cosf" as (x: Float): Float
/** The tangent of `x` radians. */
declare fn tan as (x: Double): Double
/** The tangent of `x` radians. */
declare fn tan extern "tanf" as (x: Float): Float
/** The arc sine, in radians from -PI/2 to PI/2. */
declare fn asin as (x: Double): Double
/** The arc cosine, in radians from 0 to PI. */
declare fn acos as (x: Double): Double
/** The arc tangent, in radians from -PI/2 to PI/2. See {@link atan2} for the angle of a point. */
declare fn atan as (x: Double): Double
/** The angle of the point (x, y), from -PI to PI. */
declare fn atan2 as (y: Double, x: Double): Double
/** The angle of the point (x, y), from -PI to PI. */
declare fn atan2 extern "atan2f" as (y: Float, x: Float): Float
/** The hyperbolic sine. */
declare fn sinh as (x: Double): Double
/** The hyperbolic cosine. */
declare fn cosh as (x: Double): Double
/** The hyperbolic tangent. */
declare fn tanh as (x: Double): Double
/** The inverse hyperbolic sine. */
declare fn asinh as (x: Double): Double
/** The inverse hyperbolic cosine. */
declare fn acosh as (x: Double): Double
/** The inverse hyperbolic tangent. */
declare fn atanh as (x: Double): Double

/** Rounds down: the largest whole number that is `x` or less. */
declare fn floor as (x: Double): Double
/** Rounds down: the largest whole number that is `x` or less. */
declare fn floor extern "floorf" as (x: Float): Float
/** Rounds up: the smallest whole number that is `x` or more. */
declare fn ceil as (x: Double): Double
/** Rounds up: the smallest whole number that is `x` or more. */
declare fn ceil extern "ceilf" as (x: Float): Float
/** Rounds halfway cases away from zero (2.5 is 3.0). */
declare fn round as (x: Double): Double
/** Rounds halfway cases away from zero (2.5 is 3.0). */
declare fn round extern "roundf" as (x: Float): Float
/** Rounds towards zero: drops the part after the decimal point. */
declare fn trunc as (x: Double): Double
/** Rounds towards zero: drops the part after the decimal point. */
declare fn trunc extern "truncf" as (x: Float): Float
/** The remainder of x / y, with the sign of x (what `%` is for whole numbers). */
declare fn fmod as (x: Double, y: Double): Double
/** The remainder of x / y, with the sign of x. */
declare fn fmod extern "fmodf" as (x: Float, y: Float): Float
/** x * y + z, rounded once. */
declare fn fma as (x: Double, y: Double, z: Double): Double
/** The size of `x` with the sign of `y`. */
declare fn copysign as (x: Double, y: Double): Double

/** The absolute value. */
declare fn abs extern "fabs" as (x: Double): Double
/** The absolute value. */
declare fn abs extern "fabsf" as (x: Float): Float
/** The absolute value. Call it as {@link abs}. */
declare fn labs as (x: Long): Long

/** C's isnan, non-zero for true; use {@link isNan} instead. */
declare fn isnan as (x: Double): Int
/** C's isinf, non-zero for true; use {@link isInf} instead. */
declare fn isinf as (x: Double): Int
/** C's isfinite, non-zero for true; use {@link isFinite} instead. */
declare fn isfinite as (x: Double): Int

/** Whether `x` is NaN. Test it this way, as NaN == NaN is false. */
fn isNan(x: Double): Bool = isnan(x) != 0

/** True for INFINITY and -INFINITY. */
fn isInf(x: Double): Bool = isinf(x) != 0

/** Neither NaN nor infinite. */
fn isFinite(x: Double): Bool = isfinite(x) != 0

// ---------------------------------------------------------------------------------------------------
// Numbers of any type

/** The absolute value. For the smallest Int (INT_MIN) it is INT_MIN again, as in C. */
fn abs(x: Int): Int = if x < 0 do -x else x

/** The absolute value. For LONG_MIN it is LONG_MIN again, as in C. */
fn abs(x: Long): Long = labs(x)

/** The smaller of `a` and `b` (`a` when they are equal). Works for any type `<` compares. */
fn min(a: <T>, b: <T>): <T> = if b < a do b else a

/** The larger of `a` and `b` (`a` when they are equal). Works for any type `>` compares. */
fn max(a: <T>, b: <T>): <T> = if b > a do b else a

/**
 * `x`, moved into the range `low` to `high`: `clamp(15, 0, 10)` is 10.
 * @param low the smallest result
 * @param high the largest result, `low` or more
 */
fn clamp(x: <T>, low: <T>, high: <T>): <T> = if x < low do low else if x > high do high else x

/**
 * The sign of `x`.
 * @return -1 when `x` is negative, 0 when it is 0, 1 when it is positive
 */
fn sign(x: <T>): Int = if x > 0 do 1 else if x < 0 do -1 else 0

/** The smallest element. The array must not be empty. */
fn minOf(values: <T>[]): <T> do
    var best = values[0]
    for v in values where v < best do
        best = v
    ret best

/** The largest element. The array must not be empty. */
fn maxOf(values: <T>[]): <T> do
    var best = values[0]
    for v in values where v > best do
        best = v
    ret best

/** The sum of the elements, as a Long so that it doesn't overflow as soon; 0 for an empty array. */
fn sum(values: Int[]): Long do
    var total: Long = 0
    for v in values do
        total += v
    ret total

/** The sum of the elements; 0 for an empty array. */
fn sum(values: Long[]): Long do
    var total: Long = 0
    for v in values do
        total += v
    ret total

/** The sum of the elements; 0.0 for an empty array. */
fn sum(values: Double[]): Double do
    var total = 0.0
    for v in values do
        total += v
    ret total

/** The average; NaN for an empty array. */
fn mean(values: Double[]): Double = if values.len == 0 do NAN else sum(values) / (values.len as Double)

/** The average; NaN for an empty array. */
fn mean(values: Long[]): Double = if values.len == 0 do NAN else (sum(values) as Double) / (values.len as Double)

/** The average; NaN for an empty array. */
fn mean(values: Int[]): Double = if values.len == 0 do NAN else (sum(values) as Double) / (values.len as Double)

/** How far the values are from their mean, on average, squared (the population variance); NaN for an empty array. */
fn variance(values: Double[]): Double do
    val center = mean(values)
    var total = 0.0
    for v in values do
        total += (v - center) * (v - center)
    ret if values.len == 0 do NAN else total / (values.len as Double)

/** The population standard deviation: the square root of {@link variance}. */
fn stdDev(values: Double[]): Double = sqrt(variance(values))

// ---------------------------------------------------------------------------------------------------
// Doubles

/** Converts an angle in degrees to radians: `radians(180.0)` is PI. */
fn radians(degrees: Double): Double = degrees * (PI / 180.0)

/** Converts an angle in radians to degrees: `degrees(PI)` is 180.0. */
fn degrees(radians: Double): Double = radians * (180.0 / PI)

/**
 * Linear interpolation: the value `t` of the way from `a` to `b`.
 * @param t 0.0 gives `a`, 1.0 gives `b`, 0.5 the middle; outside 0.0 to 1.0 goes past them
 */
fn lerp(a: Double, b: Double, t: Double): Double = a + (b - a) * t

/**
 * Whether `a` and `b` are equal up to rounding: `approxEqual(0.1 + 0.2, 0.3)` is true.
 * @param epsilon how far apart they may be, relative to the larger one
 */
fn approxEqual(a: Double, b: Double, epsilon: Double = 0.000000001): Bool do
    ret true if a == b
    val diff = abs(a - b)
    ret diff <= epsilon * max(abs(a), abs(b)) or diff < DBL_MIN

/** `x` rounded to `digits` digits after the decimal point: `roundTo(3.14159, 2)` is 3.14. */
fn roundTo(x: Double, digits: Int): Double do
    val scale = pow(10.0, digits as Double)
    ret round(x * scale) / scale

// ---------------------------------------------------------------------------------------------------
// Whole numbers

/** The greatest common divisor: the largest number that divides both. Always 0 or more; `gcd(0, 0)` is 0. */
fn gcd(a: Long, b: Long): Long do
    var x = abs(a)
    var y = abs(b)
    while y != 0 do
        val t = x % y
        x = y
        y = t
    ret x

/** The least common multiple: the smallest number both divide. Always 0 or more; 0 when either is 0. */
fn lcm(a: Long, b: Long): Long = if a == 0 or b == 0 do 0 else abs(a / gcd(a, b) * b)

/** Whether `n` is 1, 2, 4, 8, ... */
fn isPowerOfTwo(n: Long): Bool = n > 0 and n & (n - 1) == 0

/** The smallest power of two that is n or more (1 for n <= 1). The program stops when that doesn't fit in a Long. */
fn nextPowerOfTwo(n: Long): Long do
    assert n <= 4611686018427387904, "nextPowerOfTwo: the result doesn't fit in a Long"
    var p: Long = 1
    while p < n do
        p = p << 1
    ret p

/** Whether `n` is a prime number (trial division, so slow for very large `n`). */
fn isPrime(n: Long): Bool do
    ret false if n < 2
    ret n == 2 or n == 3 if n < 4
    ret false if n % 2 == 0 or n % 3 == 0
    var i: Long = 5
    while i <= n / i do
        ret false if n % i == 0 or n % (i + 2) == 0
        i += 6
    ret true

/** Division rounding down (towards minus infinity): `floorDiv(-7, 2)` is -4, where `-7 / 2` is -3. */
fn floorDiv(a: Long, b: Long): Long do
    val q = a / b
    ret if (a % b != 0) and ((a < 0) != (b < 0)) do q - 1 else q

/** The remainder with the sign of `b`: `floorMod(-7, 3)` is 2, where `-7 % 3` is -1. Good for wrapping indices. */
fn floorMod(a: Long, b: Long): Long do
    val r = a % b
    ret if r != 0 and ((r < 0) != (b < 0)) do r + b else r

// ---------------------------------------------------------------------------------------------------
// Checked and wrapping arithmetic. checked* throw a RangeError instead of overflowing; wrapping* wrap around
// (as C's unsigned arithmetic does), even when the program is compiled with --overflow-check.

/** GCC's __builtin_add_overflow for Long: stores `a + b` (wrapped) in `result`, and returns whether it overflowed. */
declare fn addOverflow extern "__builtin_add_overflow" as (a: Long, b: Long, out result: Long): Bool
/** GCC's __builtin_sub_overflow for Long: stores `a - b` (wrapped) in `result`, and returns whether it overflowed. */
declare fn subOverflow extern "__builtin_sub_overflow" as (a: Long, b: Long, out result: Long): Bool
/** GCC's __builtin_mul_overflow for Long: stores `a * b` (wrapped) in `result`, and returns whether it overflowed. */
declare fn mulOverflow extern "__builtin_mul_overflow" as (a: Long, b: Long, out result: Long): Bool
/** GCC's __builtin_add_overflow for Int: stores `a + b` (wrapped) in `result`, and returns whether it overflowed. */
declare fn addOverflow extern "__builtin_add_overflow" as (a: Int, b: Int, out result: Int): Bool
/** GCC's __builtin_sub_overflow for Int: stores `a - b` (wrapped) in `result`, and returns whether it overflowed. */
declare fn subOverflow extern "__builtin_sub_overflow" as (a: Int, b: Int, out result: Int): Bool
/** GCC's __builtin_mul_overflow for Int: stores `a * b` (wrapped) in `result`, and returns whether it overflowed. */
declare fn mulOverflow extern "__builtin_mul_overflow" as (a: Int, b: Int, out result: Int): Bool
/** GCC's __builtin_add_overflow for ULong: stores `a + b` (wrapped) in `result`, and returns whether it overflowed. */
declare fn addOverflow extern "__builtin_add_overflow" as (a: ULong, b: ULong, out result: ULong): Bool
/** GCC's __builtin_mul_overflow for ULong: stores `a * b` (wrapped) in `result`, and returns whether it overflowed. */
declare fn mulOverflow extern "__builtin_mul_overflow" as (a: ULong, b: ULong, out result: ULong): Bool
/** GCC's __builtin_add_overflow for UInt: stores `a + b` (wrapped) in `result`, and returns whether it overflowed. */
declare fn addOverflow extern "__builtin_add_overflow" as (a: UInt, b: UInt, out result: UInt): Bool
/** GCC's __builtin_mul_overflow for UInt: stores `a * b` (wrapped) in `result`, and returns whether it overflowed. */
declare fn mulOverflow extern "__builtin_mul_overflow" as (a: UInt, b: UInt, out result: UInt): Bool

/**
 * `a + b`.
 * @throws when the result doesn't fit in a Long
 */
fn checkedAdd(a: Long, b: Long): Long throws RangeError do
    var r: Long = 0
    throw rangeError("${a} + ${b} is too big for a Long") if addOverflow(a, b, mut r)
    ret r

/**
 * `a - b`.
 * @throws when the result doesn't fit in a Long
 */
fn checkedSub(a: Long, b: Long): Long throws RangeError do
    var r: Long = 0
    throw rangeError("${a} - ${b} is too big for a Long") if subOverflow(a, b, mut r)
    ret r

/**
 * `a * b`.
 * @throws when the result doesn't fit in a Long
 */
fn checkedMul(a: Long, b: Long): Long throws RangeError do
    var r: Long = 0
    throw rangeError("${a} * ${b} is too big for a Long") if mulOverflow(a, b, mut r)
    ret r

/**
 * `a + b`.
 * @throws when the result doesn't fit in an Int
 */
fn checkedAdd(a: Int, b: Int): Int throws RangeError do
    var r = 0
    throw rangeError("${a} + ${b} is too big for an Int") if addOverflow(a, b, mut r)
    ret r

/**
 * `a - b`.
 * @throws when the result doesn't fit in an Int
 */
fn checkedSub(a: Int, b: Int): Int throws RangeError do
    var r = 0
    throw rangeError("${a} - ${b} is too big for an Int") if subOverflow(a, b, mut r)
    ret r

/**
 * `a * b`.
 * @throws when the result doesn't fit in an Int
 */
fn checkedMul(a: Int, b: Int): Int throws RangeError do
    var r = 0
    throw rangeError("${a} * ${b} is too big for an Int") if mulOverflow(a, b, mut r)
    ret r

/**
 * Converts `x` to an Int.
 * @throws when `x` doesn't fit in an Int
 */
fn toInt(x: Long): Int throws RangeError do
    throw rangeError("${x} doesn't fit in an Int") if x < INT_MIN or x > INT_MAX
    ret x as Int

/**
 * Rounds `x` to the nearest Long (halfway cases away from zero).
 * @throws when `x` is NaN or doesn't fit in a Long
 */
fn toLong(x: Double): Long throws RangeError do
    val r = round(x)
    throw rangeError("${x} doesn't fit in a Long") if isNan(r) or r < -9223372036854775808.0 or r >= 9223372036854775808.0
    ret r as Long

/** `a + b`, wrapping around past the largest ULong. */
fn wrappingAdd(a: ULong, b: ULong): ULong do
    var r: ULong = 0
    addOverflow(a, b, mut r)
    ret r

/** `a * b`, wrapping around past the largest ULong. */
fn wrappingMul(a: ULong, b: ULong): ULong do
    var r: ULong = 0
    mulOverflow(a, b, mut r)
    ret r

/** `a + b`, wrapping around past the largest UInt. */
fn wrappingAdd(a: UInt, b: UInt): UInt do
    var r: UInt = 0
    addOverflow(a, b, mut r)
    ret r

/** `a * b`, wrapping around past the largest UInt. */
fn wrappingMul(a: UInt, b: UInt): UInt do
    var r: UInt = 0
    mulOverflow(a, b, mut r)
    ret r

// ---------------------------------------------------------------------------------------------------
// Random numbers: a PCG32 generator (pcg-random.org). Fast and of good quality, but predictable: use
// secureRandom for keys and tokens. The same seed gives the same numbers on every system.

/**
 * A PCG32 random number generator: fast and of good quality, but predictable, so use {@link secureRandom} for keys
 * and tokens. Make one with {@link random}, and keep it in a `var`: the next* functions change it (`mut`).
 *
 *     var r = math.random(42)
 *     val die = r.nextInt(1, 7)                   // or math.nextInt(mut r, 1, 7)
 */
mut rec Random as
    /** Where the generator is in its sequence. */
    state: ULong
    /** Which stream it is: odd, from the stream number. */
    increment: ULong

/** The multiplier of the PCG32 generator. */
const PCG_MULTIPLIER: ULong = 6364136223846793005

/** A generator that starts from `seed`: the same seed always gives the same numbers. */
fn random(seed: ULong): Random = random(seed, seed)

/**
 * A generator for one of 2^63 streams: generators with different streams give different numbers from the same seed.
 * @param seed where to start
 * @param stream which sequence of numbers
 */
fn random(seed: ULong, stream: ULong): Random do
    var r = Random { 0, (stream << 1) | 1 }
    nextUInt(mut r)
    r.state = wrappingAdd(r.state, seed)
    nextUInt(mut r)
    ret r

/** A generator seeded from the system's random source (different numbers on every run). */
fn random(): Random do
    var seed: ULong = 0
    if apr_generate_random_bytes((addrof seed) !as @UChar, sizeof ULong) != APR_SUCCESS do
        seed = defaultSeed
    ret random(seed)

/** The next 32 random bits. */
fn nextUInt(mut r: Random): UInt do
    val old = r.state
    r.state = wrappingAdd(wrappingMul(old, PCG_MULTIPLIER), r.increment)
    val shifted = (((old >> 18) ^ old) >> 27) as UInt
    val rotation = (old >> 59) as UInt
    ret (shifted >> rotation) | (shifted << ((32 - rotation) & 31))

/** The next 64 random bits. */
fn nextULong(mut r: Random): ULong = ((nextUInt(mut r) as ULong) << 32) | (nextUInt(mut r) as ULong)

/**
 * A whole number from `low` up to, but not including, `high`, with every value equally likely.
 * @param low the smallest result
 * @param high one more than the largest result; more than `low`
 */
fn nextInt(mut r: Random, low: Int, high: Int): Int do
    assert low < high, "math.nextInt needs low < high"
    val range = ((high as Long) - (low as Long)) as ULong
    // Values below `threshold` would make some results more likely than others, so they are drawn again.
    val threshold = (4294967296 - range) % range
    var value = nextUInt(mut r) as ULong
    while value < threshold do
        value = nextUInt(mut r) as ULong
    ret ((low as Long) + ((value % range) as Long)) as Int

/** A Double from 0.0 up to, but not including, 1.0. */
fn nextDouble(mut r: Random): Double = ((nextULong(mut r) >> 11) as Double) * (1.0 / 9007199254740992.0)

/** A random Bool, true or false equally likely. */
fn nextBool(mut r: Random): Bool = nextUInt(mut r) & 1 == 1

/** A Double from the standard normal distribution: mean 0.0, standard deviation 1.0 (Box-Muller). */
fn nextGaussian(mut r: Random): Double do
    var u = nextDouble(mut r)
    while u == 0.0 do
        u = nextDouble(mut r)
    ret sqrt(-2.0 * log(u)) * cos(TAU * nextDouble(mut r))

/**
 * A Double from a normal distribution.
 * @param mean the average of the numbers
 * @param deviation their standard deviation
 */
fn nextGaussian(mut r: Random, mean: Double, deviation: Double): Double = mean + deviation * nextGaussian(mut r)

/** Puts the elements of `values` in a random order, in place (Fisher-Yates). */
fn shuffle(mut r: Random, values: <T>[]) do
    var i = values.len
    while i > 1 do
        val j = nextInt(mut r, 0, i as Int) as Size
        i -= 1
        val t = values[i]
        values[i] = values[j]
        values[j] = t

/** A random element of `values`, which must not be empty. */
fn pick(mut r: Random, values: <T>[]): <T> = values[nextInt(mut r, 0, values.len as Int) as Size]

/** The seed {@link random} uses when the system's random source fails. */
const defaultSeed: ULong = 853146490016488653
/**
 * The generator behind randomInt, randomDouble and randomBool, seeded on first use. It is shared by the
 * whole program, so threads that use it at the same time get repeated numbers: give each thread its own
 * `math.random()` instead.
 */
priv var shared = Random { 0, 0 }
/** Whether {@code shared} was seeded yet. */
priv var sharedReady = false

/** Seeds the shared generator on first use. */
priv fn ensureShared() do
    if not sharedReady do
        shared = random()
        sharedReady = true

/**
 * Seeds the shared generator, so that {@link randomInt}, {@link randomDouble} and {@link randomBool} repeat the same numbers.
 */
fn seed(value: ULong) do
    shared = random(value)
    sharedReady = true

/**
 * A whole number from `low` up to, but not including, `high`, from the shared generator: `randomInt(1, 7)` rolls a die.
 */
fn randomInt(low: Int, high: Int): Int do
    ensureShared()
    ret nextInt(mut shared, low, high)

/** A Double from 0.0 up to, but not including, 1.0, from the shared generator. */
fn randomDouble(): Double do
    ensureShared()
    ret nextDouble(mut shared)

/** A random Bool from the shared generator. */
fn randomBool(): Bool do
    ensureShared()
    ret nextBool(mut shared)

/**
 * Fills `bytes` from the system's cryptographically secure random source: for keys and tokens.
 * @throws when the source can't be read
 */
fn secureRandom(bytes: UChar[]) throws OsError do
    try checkStatus(apr_generate_random_bytes(bytes.data !as @UChar, bytes.len), "read secure random bytes"c)
