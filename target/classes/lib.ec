use "math.h"
use "stdio.h"
use "stdlib.h"
use "ctype.h"
use "apr_general.h"
use "apr_pools.h"

declare fn "printf" as log(value: CString): None
declare fn "printf" as log(format: CString, value: CString): None

// apr

// stdio
declare val "NULL" as NULL: Null
declare val "EOF" as EOF: Int

declare fn printf(value: CString): None
declare fn printf(format: CString, value: CString): None

// math
declare fn acos(x: Double): Double
declare fn asin(x: Double): Double
declare fn atan(x: Double): Double
declare fn atan2(x: Double): Double
declare fn cos(x: Double): Double
declare fn cosh(x: Double): Double
declare fn sin(x: Double): Double
declare fn sinh(x: Double): Double
declare fn tan(x: Double): Double
declare fn exp(x: Double): Double
// declare fn frexp(): Double
// declare fn ldexp(): Double
declare fn log(x: Double): Double
declare fn log10(x: Double): Double
// declare fn modf(): Double
// declare fn pow(x: Double, y: Double): Double
declare fn sqrt(x: Double): Double
declare fn ceil(x: Double): Double
declare fn "fabs" as abs(x: Double): Double
declare fn floor(x: Double): Double
// declare fn fmod(x: Double, y: Double): Double
declare fn round(x: Double): Double

fn min(x: Double, y: Double): Double = if (x < y) x else y
fn max(x: Double, y: Double): Double = if (x > y) x else y
fn clamp(v: Double, min: Double, max: Double): Double = max(min(v, max), min)

// stdlib
declare fn "atof" as toDouble(string: CString): Double
declare fn "atoi" as toInt(string: CString): Int
declare fn "atol" as toLong(string: CString): Long

declare fn abort()
declare fn exit(status: Int)
fn exit() = exit(0)
declare fn "getenv" as getEnv(name: CString): CString
declare fn "system" as executeSystemCommand(name: CString): Int

declare fn abs(x: Int): Int
declare fn "labs" as abs(x: Long): Long

declare rec "div_t" as IntDivResult as
     quot: Int
     rem: Int

declare rec "ldiv_t" as LongDivResult as
     quot: Long
     rem: Long

declare fn div(numer: Int, denom: Int): IntDivResult
declare fn "ldiv" as div(numer: Long, denom: Long): LongDivResult

declare fn "rand" as randomInt(): Int
declare fn "srand" as randomInt(seed: Int): Int

// ctype