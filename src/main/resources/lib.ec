use "math.h"
use "stdio.h"
use "stdlib.h"
use "apr_general.h"
use "apr_pools.h"

// stdio
declare fn "printf" as printf(format: CString, value: CString): None
declare fn "printf" as printf(format: CString, value: Number): None

fn log(value: CString): None do
    printf("%s\n"c, value)
fn log(value: String): None do
    printf("%s\n"c, value.data)
fn log(value: Number): None do
    printf("%d\n"c, value)

declare type IntegerNumber = Char & Short & Int & Long
declare type FloatingNumber = Float & Double
declare type Number = IntegerNumber & FloatingNumber

// apr
declare type rec "apr_pool_t" as Pool
declare type rec "apr_status_t" as PoolStatus

var ROOT_POOL: @Pool? = Null

declare fn "apr_pool_create" as createPool(pool: @@Pool, parent: @Pool?): PoolStatus
fn createPool(pool: @@Pool): PoolStatus = createPool(pool, Null)
declare fn "apr_pool_destroy" as freePool(pool: @Pool)
declare fn "apr_palloc" as alloc(pool: @Pool, size: Size): @Any

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
declare fn pow(x: Double, y: Double): Double
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

declare type rec "div_t" as IntDivResult as
     quot: Int
     rem: Int

declare type rec "ldiv_t" as LongDivResult as
     quot: Long
     rem: Long

declare fn "div" as div(numer: Int, denom: Int): IntDivResult
declare fn "ldiv" as div(numer: Long, denom: Long): LongDivResult

declare fn "rand" as randomInt(): Int
declare fn "srand" as randomInt(seed: Int): Int

// string
declare fn memchr(str: @Any, c: Int, n: Size): @Any
declare fn memcmp(str1: @Any, str2: @Any, n: Size): @Any
declare fn memcpy(dest: @Any, src: @Any, n: Size): @Any
declare fn memset(str: @Any, c: Int, n: Size): @Any
declare fn "strcat" as append(dest: CString, src: CString): CString
declare fn "strncat" as append(dest: CString, src: CString, n: Size): CString
declare fn strchr(str: CString, c: Int): CString
declare fn "strcmp" as compare(dest: CString, src: CString): CString
declare fn "strncmp" as compare(dest: CString, src: CString, n: Size): CString
// declare fn "strcoll"
declare fn "strcpy" as copy(dest: CString, src: CString): CString
declare fn "strncpy" as copy(dest: CString, src: CString, n: Size): CString
// declare fn "strcspn"
// declare fn "strerror"
declare fn "strlen" as lengthOf(str: CString): Size
// declare fn "strpbrk"
// declare fn "strrchr"
// declare fn "strspn"
declare fn "strstr" as strstr(dest: CString, src: CString): CString
// declare fn "strtok"
// declare fn "strxfrm"

fn clone(str: CString, pool: @Pool?): CString do
    val n = str.lengthOf()
    val newStr = alloc(pool ?? ROOT_POOL, sizeof(CString) * (n+1) as Size) as unsafe CString
    copy(newStr, str, n)
    ret newStr

fn clone(str: CString): CString = clone(str, Null)
