// stdlib.h: process control, number parsing, sorting and C's own heap (eclang code allocates in pools instead).
use "stdlib.h"

declare val EXIT_SUCCESS as Int
declare val EXIT_FAILURE as Int
declare val RAND_MAX as Int

declare fn exit as (status: Int): Never
declare fn abort as (): Never
declare fn atexit as (func: () => None): Int
// null when the variable isn't set.
declare fn getenv as (name: CString): CString?
declare fn system as (command: CString): Int
// Makes a new directory from a template ending in XXXXXX, which it fills in; null when it fails.
declare fn mkdtemp as (template: CString): CString?
// The system's load averages over 1, 5 and 15 minutes; gives how many it wrote, or -1.
declare fn getloadavg as (loadavg: @Double, count: Int): Int

// The strto* functions set `end` to where parsing stopped (pass null when not needed).
declare fn strtol as (s: CString, end: @CString?, base: Int): Long
declare fn strtoul as (s: CString, end: @CString?, base: Int): ULong
declare fn strtod as (s: CString, end: @CString?): Double
declare fn strtof as (s: CString, end: @CString?): Float
declare fn atoi as (s: CString): Int
declare fn atol as (s: CString): Long
declare fn atof as (s: CString): Double

declare fn abs as (x: Int): Int
declare fn labs as (x: Long): Long
declare fn rand as (): Int
declare fn srand as (seed: UInt)

declare fn qsort as (base: @Any, count: Size, size: Size, compare: (@Any, @Any) => Int)
declare fn bsearch as (key: @Any, base: @Any, count: Size, size: Size, compare: (@Any, @Any) => Int): @Any?

// null when there is no memory left. Memory from these must be given back with `free`.
declare fn malloc as (size: Size): @Any?
declare fn calloc as (count: Size, size: Size): @Any?
declare fn realloc as (p: @Any?, size: Size): @Any?
declare fn free as (p: @Any?)

// The quotient and remainder of a whole-number division at once.
declare rec div_t as
    quot: Int
    rem: Int

declare rec ldiv_t as
    quot: Long
    rem: Long

declare fn div as (numer: Int, denom: Int): div_t
declare fn ldiv as (numer: Long, denom: Long): ldiv_t
