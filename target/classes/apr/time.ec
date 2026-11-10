use "apr_time.h"
use "core:apr/apr.ec"
use "core:apr/pools.ec"
use "core:apr/errno.ec"
use "core:c/time.ec"

declare val apr_month_snames as @Char
declare val apr_day_snames as @Char

// APR_TIME_C(val)   APR_INT64_C(val)
// APR_TIME_T_FMT   APR_INT64_T_FMT
// APR_USEC_PER_SEC   APR_TIME_C(1000000)
// apr_time_sec(time)   ((time) / APR_USEC_PER_SEC)
// apr_time_usec(time)   ((time) % APR_USEC_PER_SEC)
// apr_time_msec(time)   (((time) / 1000) % 1000)
// apr_time_as_msec(time)   ((time) / 1000)
// apr_time_from_msec(msec)   ((apr_time_t)(msec) * 1000)
// apr_time_from_sec(sec)   ((apr_time_t)(sec) * APR_USEC_PER_SEC)
// apr_time_make(sec, usec)
// APR_RFC822_DATE_LEN   (30)
// APR_CTIME_LEN   (25)

declare type apr_time_t as apr_int64_t
declare type apr_interval_time_t as apr_int64_t
declare type apr_short_interval_time_t as apr_int32_t

// A time split into parts (apr_time_exp_lt, apr_time_exp_gmt).
declare rec apr_time_exp_t as
    tm_usec: apr_int32_t
    tm_sec: apr_int32_t
    tm_min: apr_int32_t
    tm_hour: apr_int32_t
    tm_mday: apr_int32_t
    tm_mon: apr_int32_t
    tm_year: apr_int32_t
    tm_wday: apr_int32_t
    tm_yday: apr_int32_t
    tm_isdst: apr_int32_t
    tm_gmtoff: apr_int32_t

declare fn apr_time_now as (): apr_time_t
declare fn apr_time_ansi_put as (out result: apr_time_t, input: time_t): apr_status_t
declare fn apr_time_exp_tz as (out result: apr_time_exp_t, input: apr_time_t, offs: apr_int32_t): apr_status_t
declare fn apr_time_exp_gmt as (out result: apr_time_exp_t, input: apr_time_t): apr_status_t
declare fn apr_time_exp_lt as (out result: apr_time_exp_t, input: apr_time_t): apr_status_t
declare fn apr_time_exp_get as (out result: apr_time_t, input: @apr_time_exp_t): apr_status_t
declare fn apr_time_exp_gmt_get as (out result: apr_time_t, input: @apr_time_exp_t): apr_status_t
declare fn apr_sleep as (t: apr_interval_time_t)
declare fn apr_rfc822_date as (date_str: CString, t: apr_time_t): apr_status_t
declare fn apr_ctime as (date_str: CString, t: apr_time_t): apr_status_t
declare fn apr_strftime as (s: CString, out retsize: apr_size_t, max: apr_size_t, format: CString, tm: @apr_time_exp_t): apr_status_t
declare fn apr_time_clock_hires as (p: @apr_pool_t)
