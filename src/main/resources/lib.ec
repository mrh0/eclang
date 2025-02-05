use "math.h"
use "stdio.h"
use "stdlib.h"
use "apr_general.h"
use "apr_pools.h"

// stdio
declare fn printf(format: CString, value: CString): None
declare fn printf(format: CString, value: Number): None

fn log(value: CString): None do
    printf("%s\n"c, value)
fn log(value: String): None do
    printf("%s\n"c, value.data)
fn log(value: Number): None do
    printf("%d\n"c, value)

declare type SignedIntegerNumber as Char | Short | Int | Long
declare type UnsignedIntegerNumber as UChar | UShort | UInt | ULong | Size
declare type FloatingNumber as Float | Double
declare type SignedNumber as SignedIntegerNumber | FloatingNumber
declare type UnsignedNumber as UnsignedIntegerNumber
declare type Number as SignedNumber | UnsignedNumber

// apr
declare rec Pool from "apr_pool_t"
declare rec PoolStatus from "apr_status_t"

var ROOT_POOL: @Pool? = Null

declare fn createPool(pool: @@Pool, parent: @Pool?): PoolStatus from "apr_pool_create"
fn createPool(pool: @@Pool): PoolStatus = createPool(pool, Null)
declare fn freePool(pool: @Pool)  from "apr_pool_destroy"
declare fn alloc(pool: @Pool, size: Size): @Any from "apr_palloc"

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
declare fn abs(x: Double): Double from "fabs"
declare fn floor(x: Double): Double
// declare fn fmod(x: Double, y: Double): Double
declare fn round(x: Double): Double

fn min(x: Double, y: Double): Double = if (x < y) x else y
fn max(x: Double, y: Double): Double = if (x > y) x else y
fn clamp(v: Double, min: Double, max: Double): Double = max(min(v, max), min)

// stdlib
declare fn toDouble(string: CString): Double from "atof"
declare fn toInt(string: CString): Int from "atoi"
declare fn toLong(string: CString): Long from "atol"

declare fn abort()
declare fn exit(status: Int)
fn exit() = exit(0)
declare fn getEnv(name: CString): CString from "getenv"
declare fn  executeSystemCommand(name: CString): Int from "system"

declare fn abs(x: Int): Int
declare fn abs(x: Long): Long from "labs"

declare rec IntDivResult as
     quot: Int
     rem: Int
     from "div_t"

declare rec LongDivResult as
     quot: Long
     rem: Long
     from "ldiv_t"

declare fn div(numer: Int, denom: Int): IntDivResult from "div"
declare fn div(numer: Long, denom: Long): LongDivResult from "ldiv"

declare fn randomInt(): Int from "rand"
declare fn randomInt(seed: Int): Int from "srand"

// string
// declare fn memchr(str: @Any, c: Int, n: Size): @Any
// declare fn memcmp(str1: @Any, str2: @Any, n: Size): @Any
// declare fn memcpy(dest: @Any, src: @Any, n: Size): @Any
// declare fn memset(str: @Any, c: Int, n: Size): @Any
// declare fn "strcat" as append(dest: CString, src: CString): CString
// declare fn "strncat" as append(dest: CString, src: CString, n: Size): CString
// declare fn strchr(str: CString, c: Int): CString
// declare fn "strcmp" as compare(dest: CString, src: CString): CString
// declare fn "strncmp" as compare(dest: CString, src: CString, n: Size): CString
// declare fn "strcoll"
// declare fn "strcpy" as copy(dest: CString, src: CString): CString
// declare fn "strncpy" as copy(dest: CString, src: CString, n: Size): CString
// declare fn "strcspn"
// declare fn "strerror"
// declare fn "strlen" as lengthOf(str: CString): Size
// declare fn "strpbrk"
// declare fn "strrchr"
// declare fn "strspn"
// declare fn "strstr" as strstr(dest: CString, src: CString): CString
// declare fn "strtok"
// declare fn "strxfrm"

fn clone(str: CString, pool: @Pool?): CString do
    val n = str.lengthOf()
    val newStr = alloc(pool ?? ROOT_POOL, sizeof(CString) * (n+1) as Size) as unsafe CString
    copy(newStr, str, n)
    ret newStr

fn clone(str: CString): CString = clone(str, Null)

rec Error as
    code: Int
    message: String
