// time.h: C's clocks and calendar time. For the eclang API, see core:time.ec.
use "time.h"

declare type time_t as Long
declare type clock_t as Long
declare type clockid_t as Int

declare val CLOCKS_PER_SEC as Long
declare val CLOCK_REALTIME as clockid_t
// A clock that only moves forward, at a steady rate (not changed by setting the system time).
declare val CLOCK_MONOTONIC as clockid_t
declare val CLOCK_PROCESS_CPUTIME_ID as clockid_t

declare struct timespec as
    tv_sec: time_t
    tv_nsec: Long

// A calendar time split into parts. tm_mon is 0-11, tm_year counts from 1900, tm_wday is 0 for Sunday.
declare struct tm as
    tm_sec: Int
    tm_min: Int
    tm_hour: Int
    tm_mday: Int
    tm_mon: Int
    tm_year: Int
    tm_wday: Int
    tm_yday: Int
    tm_isdst: Int

declare fn time as (t: @time_t?): time_t
declare fn clock as (): clock_t
declare fn difftime as (end: time_t, start: time_t): Double
declare fn mktime as (t: @tm): time_t
// mktime for UTC (a GNU and BSD extension).
declare fn timegm as (t: @tm): time_t
// The *_r variants are safe to call from several threads. They return null when the time can't be represented.
declare fn gmtime_r as (t: @time_t, result: @tm): @tm?
declare fn localtime_r as (t: @time_t, result: @tm): @tm?
declare fn strftime as (s: CString, max: Size, format: CString, t: @tm): Size
declare fn clock_gettime as (clock: clockid_t, ts: @timespec): Int
declare fn nanosleep as (request: @timespec, remaining: @timespec?): Int
