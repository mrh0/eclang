# Module `math`

Maths: constants, C's math.h, whole-number helpers, checked arithmetic, and random numbers.

    use "core:math.ec"
    val r = math.sqrt(2.0)
    val angle = math.radians(90.0)
    val n = try math.checkedMul(a, b)        // a RangeError instead of overflowing
    val die = math.randomInt(1, 7)            // 1 to 6

The C functions take and return Doubles; most also have a Float overload. Programs using them link with -lm
(`eclang run` does).

## Types

### <a id="math.Random"></a>`Random`

```ec
rec Random
```

A PCG32 random number generator: fast and of good quality, but predictable, so use [`secureRandom`](#math.secureRandom) for keys
and tokens. Make one with [`random`](#math.random), and pass its address to the next* functions:

    var r = math.random(42)
    val die = math.nextInt(addrof(r), 1, 7)

**Fields**

- <a id="math.Random.state"></a>`state: ULong` — Where the generator is in its sequence.
- <a id="math.Random.increment"></a>`increment: ULong` — Which stream it is: odd, from the stream number.

## Values

### <a id="math.PI"></a>`PI`

```ec
const PI = 3.141592653589793
```

π, half a turn in radians.

### <a id="math.TAU"></a>`TAU`

```ec
const TAU = 6.283185307179586
```

τ = 2π, a whole turn in radians.

### <a id="math.E"></a>`E`

```ec
const E = 2.718281828459045
```

e, the base of the natural logarithm.

### <a id="math.SQRT2"></a>`SQRT2`

```ec
const SQRT2 = 1.4142135623730951
```

The square root of 2.

### <a id="math.LN2"></a>`LN2`

```ec
const LN2 = 0.6931471805599453
```

The natural logarithm of 2.

### <a id="math.LN10"></a>`LN10`

```ec
const LN10 = 2.302585092994046
```

The natural logarithm of 10.

### <a id="math.PHI"></a>`PHI`

```ec
const PHI = 1.618033988749895
```

φ, the golden ratio: (1 + √5) / 2.

### <a id="math.INT_MIN"></a>`INT_MIN`

```ec
const INT_MIN: Int = -2147483647 - 1
```

The smallest Int: -2147483648.

### <a id="math.INT_MAX"></a>`INT_MAX`

```ec
const INT_MAX: Int = 2147483647
```

The largest Int: 2147483647.

### <a id="math.LONG_MIN"></a>`LONG_MIN`

```ec
const LONG_MIN: Long = -9223372036854775807 - 1
```

The smallest Long: -9223372036854775808.

### <a id="math.LONG_MAX"></a>`LONG_MAX`

```ec
const LONG_MAX: Long = 9223372036854775807
```

The largest Long: 9223372036854775807.

### <a id="math.INFINITY"></a>`INFINITY`

```ec
declare val INFINITY as Double
```

Positive infinity; `-INFINITY` is negative infinity.

### <a id="math.NAN"></a>`NAN`

```ec
declare val NAN as Double
```

"Not a number", the result of 0.0 / 0.0. It isn't equal to anything, itself included: test with [`isNan`](#math.isNan).

### <a id="math.DBL_EPSILON"></a>`DBL_EPSILON`

```ec
declare val DBL_EPSILON as Double
```

The difference between 1.0 and the next Double after it.

### <a id="math.DBL_MAX"></a>`DBL_MAX`

```ec
declare val DBL_MAX as Double
```

The largest finite Double.

### <a id="math.DBL_MIN"></a>`DBL_MIN`

```ec
declare val DBL_MIN as Double
```

The smallest normal Double greater than 0.

### <a id="math.PCG_MULTIPLIER"></a>`PCG_MULTIPLIER`

```ec
const PCG_MULTIPLIER: ULong = 6364136223846793005
```

The multiplier of the PCG32 generator.

### <a id="math.defaultSeed"></a>`defaultSeed`

```ec
const defaultSeed: ULong = 853146490016488653
```

The seed [`random`](#math.random) uses when the system's random source fails.

## Functions

### <a id="math.sqrt"></a>`sqrt`

```ec
fn sqrt(x: Double): Double
```

The square root.

*Binds the C function `sqrt`.*

### `sqrt`

```ec
fn sqrt(x: Float): Float
```

The square root.

*Binds the C function `sqrtf`.*

### <a id="math.cbrt"></a>`cbrt`

```ec
fn cbrt(x: Double): Double
```

The cube root (negative for a negative `x`).

*Binds the C function `cbrt`.*

### <a id="math.pow"></a>`pow`

```ec
fn pow(x: Double, y: Double): Double
```

`x` to the power `y`.

*Binds the C function `pow`.*

### `pow`

```ec
fn pow(x: Float, y: Float): Float
```

`x` to the power `y`.

*Binds the C function `powf`.*

### <a id="math.hypot"></a>`hypot`

```ec
fn hypot(x: Double, y: Double): Double
```

The length of the hypotenuse, sqrt(x² + y²), without overflowing in between.

*Binds the C function `hypot`.*

### <a id="math.exp"></a>`exp`

```ec
fn exp(x: Double): Double
```

e to the power `x`.

*Binds the C function `exp`.*

### <a id="math.exp2"></a>`exp2`

```ec
fn exp2(x: Double): Double
```

2 to the power `x`.

*Binds the C function `exp2`.*

### <a id="math.expm1"></a>`expm1`

```ec
fn expm1(x: Double): Double
```

e to the power `x`, less 1: precise when `x` is near 0.

*Binds the C function `expm1`.*

### <a id="math.log"></a>`log`

```ec
fn log(x: Double): Double
```

The natural logarithm (base e). `log` outside this module still prints.

*Binds the C function `log`.*

### `log`

```ec
fn log(x: Float): Float
```

The natural logarithm (base e).

*Binds the C function `logf`.*

### <a id="math.log2"></a>`log2`

```ec
fn log2(x: Double): Double
```

The logarithm in base 2.

*Binds the C function `log2`.*

### <a id="math.log10"></a>`log10`

```ec
fn log10(x: Double): Double
```

The logarithm in base 10.

*Binds the C function `log10`.*

### <a id="math.log1p"></a>`log1p`

```ec
fn log1p(x: Double): Double
```

The natural logarithm of 1 + `x`: precise when `x` is near 0.

*Binds the C function `log1p`.*

### <a id="math.sin"></a>`sin`

```ec
fn sin(x: Double): Double
```

The sine of `x` radians.

*Binds the C function `sin`.*

### `sin`

```ec
fn sin(x: Float): Float
```

The sine of `x` radians.

*Binds the C function `sinf`.*

### <a id="math.cos"></a>`cos`

```ec
fn cos(x: Double): Double
```

The cosine of `x` radians.

*Binds the C function `cos`.*

### `cos`

```ec
fn cos(x: Float): Float
```

The cosine of `x` radians.

*Binds the C function `cosf`.*

### <a id="math.tan"></a>`tan`

```ec
fn tan(x: Double): Double
```

The tangent of `x` radians.

*Binds the C function `tan`.*

### `tan`

```ec
fn tan(x: Float): Float
```

The tangent of `x` radians.

*Binds the C function `tanf`.*

### <a id="math.asin"></a>`asin`

```ec
fn asin(x: Double): Double
```

The arc sine, in radians from -PI/2 to PI/2.

*Binds the C function `asin`.*

### <a id="math.acos"></a>`acos`

```ec
fn acos(x: Double): Double
```

The arc cosine, in radians from 0 to PI.

*Binds the C function `acos`.*

### <a id="math.atan"></a>`atan`

```ec
fn atan(x: Double): Double
```

The arc tangent, in radians from -PI/2 to PI/2. See [`atan2`](#math.atan2) for the angle of a point.

*Binds the C function `atan`.*

### <a id="math.atan2"></a>`atan2`

```ec
fn atan2(y: Double, x: Double): Double
```

The angle of the point (x, y), from -PI to PI.

*Binds the C function `atan2`.*

### `atan2`

```ec
fn atan2(y: Float, x: Float): Float
```

The angle of the point (x, y), from -PI to PI.

*Binds the C function `atan2f`.*

### <a id="math.sinh"></a>`sinh`

```ec
fn sinh(x: Double): Double
```

The hyperbolic sine.

*Binds the C function `sinh`.*

### <a id="math.cosh"></a>`cosh`

```ec
fn cosh(x: Double): Double
```

The hyperbolic cosine.

*Binds the C function `cosh`.*

### <a id="math.tanh"></a>`tanh`

```ec
fn tanh(x: Double): Double
```

The hyperbolic tangent.

*Binds the C function `tanh`.*

### <a id="math.asinh"></a>`asinh`

```ec
fn asinh(x: Double): Double
```

The inverse hyperbolic sine.

*Binds the C function `asinh`.*

### <a id="math.acosh"></a>`acosh`

```ec
fn acosh(x: Double): Double
```

The inverse hyperbolic cosine.

*Binds the C function `acosh`.*

### <a id="math.atanh"></a>`atanh`

```ec
fn atanh(x: Double): Double
```

The inverse hyperbolic tangent.

*Binds the C function `atanh`.*

### <a id="math.floor"></a>`floor`

```ec
fn floor(x: Double): Double
```

Rounds down: the largest whole number that is `x` or less.

*Binds the C function `floor`.*

### `floor`

```ec
fn floor(x: Float): Float
```

Rounds down: the largest whole number that is `x` or less.

*Binds the C function `floorf`.*

### <a id="math.ceil"></a>`ceil`

```ec
fn ceil(x: Double): Double
```

Rounds up: the smallest whole number that is `x` or more.

*Binds the C function `ceil`.*

### `ceil`

```ec
fn ceil(x: Float): Float
```

Rounds up: the smallest whole number that is `x` or more.

*Binds the C function `ceilf`.*

### <a id="math.round"></a>`round`

```ec
fn round(x: Double): Double
```

Rounds halfway cases away from zero (2.5 is 3.0).

*Binds the C function `round`.*

### `round`

```ec
fn round(x: Float): Float
```

Rounds halfway cases away from zero (2.5 is 3.0).

*Binds the C function `roundf`.*

### <a id="math.trunc"></a>`trunc`

```ec
fn trunc(x: Double): Double
```

Rounds towards zero: drops the part after the decimal point.

*Binds the C function `trunc`.*

### `trunc`

```ec
fn trunc(x: Float): Float
```

Rounds towards zero: drops the part after the decimal point.

*Binds the C function `truncf`.*

### <a id="math.fmod"></a>`fmod`

```ec
fn fmod(x: Double, y: Double): Double
```

The remainder of x / y, with the sign of x (what `%` is for whole numbers).

*Binds the C function `fmod`.*

### `fmod`

```ec
fn fmod(x: Float, y: Float): Float
```

The remainder of x / y, with the sign of x.

*Binds the C function `fmodf`.*

### <a id="math.fma"></a>`fma`

```ec
fn fma(x: Double, y: Double, z: Double): Double
```

x * y + z, rounded once.

*Binds the C function `fma`.*

### <a id="math.copysign"></a>`copysign`

```ec
fn copysign(x: Double, y: Double): Double
```

The size of `x` with the sign of `y`.

*Binds the C function `copysign`.*

### <a id="math.abs"></a>`abs`

```ec
fn abs(x: Double): Double
```

The absolute value.

*Binds the C function `fabs`.*

### `abs`

```ec
fn abs(x: Float): Float
```

The absolute value.

*Binds the C function `fabsf`.*

### <a id="math.labs"></a>`labs`

```ec
fn labs(x: Long): Long
```

The absolute value. Call it as [`abs`](#math.abs).

*Binds the C function `labs`.*

### <a id="math.isnan"></a>`isnan`

```ec
fn isnan(x: Double): Int
```

C's isnan, non-zero for true; use [`isNan`](#math.isNan) instead.

*Binds the C function `isnan`.*

### <a id="math.isinf"></a>`isinf`

```ec
fn isinf(x: Double): Int
```

C's isinf, non-zero for true; use [`isInf`](#math.isInf) instead.

*Binds the C function `isinf`.*

### <a id="math.isfinite"></a>`isfinite`

```ec
fn isfinite(x: Double): Int
```

C's isfinite, non-zero for true; use [`isFinite`](#math.isFinite) instead.

*Binds the C function `isfinite`.*

### <a id="math.isNan"></a>`isNan`

```ec
fn isNan(x: Double): Bool
```

Whether `x` is NaN. Test it this way, as NaN == NaN is false.

### <a id="math.isInf"></a>`isInf`

```ec
fn isInf(x: Double): Bool
```

True for INFINITY and -INFINITY.

### <a id="math.isFinite"></a>`isFinite`

```ec
fn isFinite(x: Double): Bool
```

Neither NaN nor infinite.

### `abs`

```ec
fn abs(x: Int): Int
```

The absolute value. For the smallest Int (INT_MIN) it is INT_MIN again, as in C.

### `abs`

```ec
fn abs(x: Long): Long
```

The absolute value. For LONG_MIN it is LONG_MIN again, as in C.

### <a id="math.min"></a>`min`

```ec
fn min(a: <T>, b: <T>): <T>
```

The smaller of `a` and `b` (`a` when they are equal). Works for any type `<` compares.

### <a id="math.max"></a>`max`

```ec
fn max(a: <T>, b: <T>): <T>
```

The larger of `a` and `b` (`a` when they are equal). Works for any type `>` compares.

### <a id="math.clamp"></a>`clamp`

```ec
fn clamp(x: <T>, low: <T>, high: <T>): <T>
```

`x`, moved into the range `low` to `high`: `clamp(15, 0, 10)` is 10.

**Parameters**

- `low` — the smallest result
- `high` — the largest result, `low` or more

### <a id="math.sign"></a>`sign`

```ec
fn sign(x: <T>): Int
```

The sign of `x`.

**Returns** -1 when `x` is negative, 0 when it is 0, 1 when it is positive

### <a id="math.minOf"></a>`minOf`

```ec
fn minOf(values: <T>[]): <T>
```

The smallest element. The array must not be empty.

### <a id="math.maxOf"></a>`maxOf`

```ec
fn maxOf(values: <T>[]): <T>
```

The largest element. The array must not be empty.

### <a id="math.sum"></a>`sum`

```ec
fn sum(values: Int[]): Long
```

The sum of the elements, as a Long so that it doesn't overflow as soon; 0 for an empty array.

### `sum`

```ec
fn sum(values: Long[]): Long
```

The sum of the elements; 0 for an empty array.

### `sum`

```ec
fn sum(values: Double[]): Double
```

The sum of the elements; 0.0 for an empty array.

### <a id="math.mean"></a>`mean`

```ec
fn mean(values: Double[]): Double
```

The average; NaN for an empty array.

### `mean`

```ec
fn mean(values: Long[]): Double
```

The average; NaN for an empty array.

### `mean`

```ec
fn mean(values: Int[]): Double
```

The average; NaN for an empty array.

### <a id="math.radians"></a>`radians`

```ec
fn radians(degrees: Double): Double
```

Converts an angle in degrees to radians: `radians(180.0)` is PI.

### <a id="math.degrees"></a>`degrees`

```ec
fn degrees(radians: Double): Double
```

Converts an angle in radians to degrees: `degrees(PI)` is 180.0.

### <a id="math.lerp"></a>`lerp`

```ec
fn lerp(a: Double, b: Double, t: Double): Double
```

Linear interpolation: the value `t` of the way from `a` to `b`.

**Parameters**

- `t` — 0.0 gives `a`, 1.0 gives `b`, 0.5 the middle; outside 0.0 to 1.0 goes past them

### <a id="math.approxEqual"></a>`approxEqual`

```ec
fn approxEqual(a: Double, b: Double, epsilon: Double = 0.000000001): Bool
```

Whether `a` and `b` are equal up to rounding: `approxEqual(0.1 + 0.2, 0.3)` is true.

**Parameters**

- `epsilon` — how far apart they may be, relative to the larger one

### <a id="math.roundTo"></a>`roundTo`

```ec
fn roundTo(x: Double, digits: Int): Double
```

`x` rounded to `digits` digits after the decimal point: `roundTo(3.14159, 2)` is 3.14.

### <a id="math.gcd"></a>`gcd`

```ec
fn gcd(a: Long, b: Long): Long
```

The greatest common divisor: the largest number that divides both. Always 0 or more; `gcd(0, 0)` is 0.

### <a id="math.lcm"></a>`lcm`

```ec
fn lcm(a: Long, b: Long): Long
```

The least common multiple: the smallest number both divide. Always 0 or more; 0 when either is 0.

### <a id="math.isPowerOfTwo"></a>`isPowerOfTwo`

```ec
fn isPowerOfTwo(n: Long): Bool
```

Whether `n` is 1, 2, 4, 8, ...

### <a id="math.nextPowerOfTwo"></a>`nextPowerOfTwo`

```ec
fn nextPowerOfTwo(n: Long): Long
```

The smallest power of two that is n or more (1 for n <= 1).

### <a id="math.isPrime"></a>`isPrime`

```ec
fn isPrime(n: Long): Bool
```

Whether `n` is a prime number (trial division, so slow for very large `n`).

### <a id="math.floorDiv"></a>`floorDiv`

```ec
fn floorDiv(a: Long, b: Long): Long
```

Division rounding down (towards minus infinity): `floorDiv(-7, 2)` is -4, where `-7 / 2` is -3.

### <a id="math.floorMod"></a>`floorMod`

```ec
fn floorMod(a: Long, b: Long): Long
```

The remainder with the sign of `b`: `floorMod(-7, 3)` is 2, where `-7 % 3` is -1. Good for wrapping indices.

### <a id="math.addOverflow"></a>`addOverflow`

```ec
fn addOverflow(a: Long, b: Long, result: @Long): Bool
```

GCC's __builtin_add_overflow for Long: stores `a + b` (wrapped) in `result`, and returns whether it overflowed.

*Binds the C function `__builtin_add_overflow`.*

### <a id="math.subOverflow"></a>`subOverflow`

```ec
fn subOverflow(a: Long, b: Long, result: @Long): Bool
```

GCC's __builtin_sub_overflow for Long: stores `a - b` (wrapped) in `result`, and returns whether it overflowed.

*Binds the C function `__builtin_sub_overflow`.*

### <a id="math.mulOverflow"></a>`mulOverflow`

```ec
fn mulOverflow(a: Long, b: Long, result: @Long): Bool
```

GCC's __builtin_mul_overflow for Long: stores `a * b` (wrapped) in `result`, and returns whether it overflowed.

*Binds the C function `__builtin_mul_overflow`.*

### `addOverflow`

```ec
fn addOverflow(a: Int, b: Int, result: @Int): Bool
```

GCC's __builtin_add_overflow for Int: stores `a + b` (wrapped) in `result`, and returns whether it overflowed.

*Binds the C function `__builtin_add_overflow`.*

### `subOverflow`

```ec
fn subOverflow(a: Int, b: Int, result: @Int): Bool
```

GCC's __builtin_sub_overflow for Int: stores `a - b` (wrapped) in `result`, and returns whether it overflowed.

*Binds the C function `__builtin_sub_overflow`.*

### `mulOverflow`

```ec
fn mulOverflow(a: Int, b: Int, result: @Int): Bool
```

GCC's __builtin_mul_overflow for Int: stores `a * b` (wrapped) in `result`, and returns whether it overflowed.

*Binds the C function `__builtin_mul_overflow`.*

### `addOverflow`

```ec
fn addOverflow(a: ULong, b: ULong, result: @ULong): Bool
```

GCC's __builtin_add_overflow for ULong: stores `a + b` (wrapped) in `result`, and returns whether it overflowed.

*Binds the C function `__builtin_add_overflow`.*

### `mulOverflow`

```ec
fn mulOverflow(a: ULong, b: ULong, result: @ULong): Bool
```

GCC's __builtin_mul_overflow for ULong: stores `a * b` (wrapped) in `result`, and returns whether it overflowed.

*Binds the C function `__builtin_mul_overflow`.*

### `addOverflow`

```ec
fn addOverflow(a: UInt, b: UInt, result: @UInt): Bool
```

GCC's __builtin_add_overflow for UInt: stores `a + b` (wrapped) in `result`, and returns whether it overflowed.

*Binds the C function `__builtin_add_overflow`.*

### `mulOverflow`

```ec
fn mulOverflow(a: UInt, b: UInt, result: @UInt): Bool
```

GCC's __builtin_mul_overflow for UInt: stores `a * b` (wrapped) in `result`, and returns whether it overflowed.

*Binds the C function `__builtin_mul_overflow`.*

### <a id="math.checkedAdd"></a>`checkedAdd`

```ec
fn checkedAdd(a: Long, b: Long): Long throws RangeError
```

`a + b`.

**Throws**

- `RangeError` — when the result doesn't fit in a Long

### <a id="math.checkedSub"></a>`checkedSub`

```ec
fn checkedSub(a: Long, b: Long): Long throws RangeError
```

`a - b`.

**Throws**

- `RangeError` — when the result doesn't fit in a Long

### <a id="math.checkedMul"></a>`checkedMul`

```ec
fn checkedMul(a: Long, b: Long): Long throws RangeError
```

`a * b`.

**Throws**

- `RangeError` — when the result doesn't fit in a Long

### `checkedAdd`

```ec
fn checkedAdd(a: Int, b: Int): Int throws RangeError
```

`a + b`.

**Throws**

- `RangeError` — when the result doesn't fit in an Int

### `checkedSub`

```ec
fn checkedSub(a: Int, b: Int): Int throws RangeError
```

`a - b`.

**Throws**

- `RangeError` — when the result doesn't fit in an Int

### `checkedMul`

```ec
fn checkedMul(a: Int, b: Int): Int throws RangeError
```

`a * b`.

**Throws**

- `RangeError` — when the result doesn't fit in an Int

### <a id="math.toInt"></a>`toInt`

```ec
fn toInt(x: Long): Int throws RangeError
```

Converts `x` to an Int.

**Throws**

- `RangeError` — when `x` doesn't fit in an Int

### <a id="math.toLong"></a>`toLong`

```ec
fn toLong(x: Double): Long throws RangeError
```

Rounds `x` to the nearest Long (halfway cases away from zero).

**Throws**

- `RangeError` — when `x` is NaN or doesn't fit in a Long

### <a id="math.wrappingAdd"></a>`wrappingAdd`

```ec
fn wrappingAdd(a: ULong, b: ULong): ULong
```

`a + b`, wrapping around past the largest ULong.

### <a id="math.wrappingMul"></a>`wrappingMul`

```ec
fn wrappingMul(a: ULong, b: ULong): ULong
```

`a * b`, wrapping around past the largest ULong.

### `wrappingAdd`

```ec
fn wrappingAdd(a: UInt, b: UInt): UInt
```

`a + b`, wrapping around past the largest UInt.

### `wrappingMul`

```ec
fn wrappingMul(a: UInt, b: UInt): UInt
```

`a * b`, wrapping around past the largest UInt.

### <a id="math.random"></a>`random`

```ec
fn random(seed: ULong): Random
```

A generator that starts from `seed`: the same seed always gives the same numbers.

### `random`

```ec
fn random(seed: ULong, stream: ULong): Random
```

A generator for one of 2^63 streams: generators with different streams give different numbers from the same seed.

**Parameters**

- `seed` — where to start
- `stream` — which sequence of numbers

### `random`

```ec
fn random(): Random
```

A generator seeded from the system's random source (different numbers on every run).

### <a id="math.nextUInt"></a>`nextUInt`

```ec
fn nextUInt(r: @Random): UInt
```

The next 32 random bits.

### <a id="math.nextULong"></a>`nextULong`

```ec
fn nextULong(r: @Random): ULong
```

The next 64 random bits.

### <a id="math.nextInt"></a>`nextInt`

```ec
fn nextInt(r: @Random, low: Int, high: Int): Int
```

A whole number from `low` up to, but not including, `high`, with every value equally likely.

**Parameters**

- `low` — the smallest result
- `high` — one more than the largest result; more than `low`

### <a id="math.nextDouble"></a>`nextDouble`

```ec
fn nextDouble(r: @Random): Double
```

A Double from 0.0 up to, but not including, 1.0.

### <a id="math.nextBool"></a>`nextBool`

```ec
fn nextBool(r: @Random): Bool
```

A random Bool, true or false equally likely.

### <a id="math.nextGaussian"></a>`nextGaussian`

```ec
fn nextGaussian(r: @Random): Double
```

A Double from the standard normal distribution: mean 0.0, standard deviation 1.0 (Box-Muller).

### `nextGaussian`

```ec
fn nextGaussian(r: @Random, mean: Double, deviation: Double): Double
```

A Double from a normal distribution.

**Parameters**

- `mean` — the average of the numbers
- `deviation` — their standard deviation

### <a id="math.shuffle"></a>`shuffle`

```ec
fn shuffle(r: @Random, values: <T>[])
```

Puts the elements of `values` in a random order, in place (Fisher-Yates).

### <a id="math.pick"></a>`pick`

```ec
fn pick(r: @Random, values: <T>[]): <T>
```

A random element of `values`, which must not be empty.

### <a id="math.seed"></a>`seed`

```ec
fn seed(value: ULong)
```

Seeds the shared generator, so that [`randomInt`](#math.randomInt), [`randomDouble`](#math.randomDouble) and [`randomBool`](#math.randomBool) repeat the same numbers.

### <a id="math.randomInt"></a>`randomInt`

```ec
fn randomInt(low: Int, high: Int): Int
```

A whole number from `low` up to, but not including, `high`, from the shared generator: `randomInt(1, 7)` rolls a die.

### <a id="math.randomDouble"></a>`randomDouble`

```ec
fn randomDouble(): Double
```

A Double from 0.0 up to, but not including, 1.0, from the shared generator.

### <a id="math.randomBool"></a>`randomBool`

```ec
fn randomBool(): Bool
```

A random Bool from the shared generator.

### <a id="math.secureRandom"></a>`secureRandom`

```ec
fn secureRandom(bytes: UChar[]) throws OsError
```

Fills `bytes` from the system's cryptographically secure random source: for keys and tokens.

**Throws**

- `OsError` — when the source can't be read
