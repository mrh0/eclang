use "math.h"
use "stdio.h"
use "stdlib.h"
use "apr_general.h"
use "apr_pools.h"

// stdio
declare fn printf as (format: CString, value: CString): None
declare fn printf as (format: CString, value: Number): None

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

declare fn createPool from "apr_pool_create" as (pool: @@Pool, parent: @Pool?): PoolStatus
fn createPool(pool: @@Pool): PoolStatus = createPool(pool, Null)
declare fn freePool from "apr_pool_destroy" as (pool: @Pool)
declare fn alloc from "apr_palloc" as (pool: @Pool, size: Size): @Any

// math
declare fn acos as (x: Double): Double
declare fn asin as (x: Double): Double
declare fn atan as (x: Double): Double
declare fn atan2 as (x: Double): Double
declare fn cos as (x: Double): Double
declare fn cosh as (x: Double): Double
declare fn sin as (x: Double): Double
declare fn sinh as (x: Double): Double
declare fn tan as (x: Double): Double
declare fn exp as (x: Double): Double
// declare fn frexp as (): Double
// declare fn ldexp as (): Double
declare fn log as (x: Double): Double
declare fn log10 as (x: Double): Double
// declare fn modf as (): Double
declare fn pow as (x: Double, y: Double): Double
declare fn sqrt as (x: Double): Double
declare fn ceil as (x: Double): Double
declare fn abs from "fabs" as (x: Double): Double
declare fn floor as (x: Double): Double
// declare fn fmod as (x: Double, y: Double): Double
declare fn round as (x: Double): Double

fn min(x: Double, y: Double): Double = if (x < y) x else y
fn max(x: Double, y: Double): Double = if (x > y) x else y
fn clamp(v: Double, min: Double, max: Double): Double = max(min(v, max), min)

// stdlib
declare fn toDouble from "atof" as (string: CString): Double
declare fn toInt from "atoi" as (string: CString): Int
declare fn toLong from "atol" as (string: CString): Long

declare fn abort as ()
declare fn exit as (status: Int)
fn exit() = exit(0)
declare fn getEnv from "getenv" as (name: CString): CString
declare fn executeSystemCommand from "system" as (name: CString): Int

declare fn abs as (x: Int): Int
declare fn abs from "labs" as (x: Long): Long

declare rec IntDivResult from "div_t" as
     quot: Int
     rem: Int

declare rec LongDivResult from "ldiv_t" as
     quot: Long
     rem: Long

declare fn div from "div" as (numer: Int, denom: Int): IntDivResult
declare fn div from "ldiv" as (numer: Long, denom: Long): LongDivResult

declare fn randomInt from "rand" as (): Int
declare fn randomInt from "srand" as (seed: Int): Int

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
    //val n = str.lengthOf()
    //val newStr = alloc(pool ?? ROOT_POOL, sizeof(CString) * (n+1) as Size) as unsafe CString
    //copy(newStr, str, n)
    ret str

fn clone(str: CString): CString = clone(str, Null)

rec Error as
    code: Int
    message: String
