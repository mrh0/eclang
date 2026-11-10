# `time.ec`

## Types

### <a id="apr_time_t"></a>`apr_time_t`

```ec
declare type apr_time_t as apr_int64_t
```

### <a id="apr_interval_time_t"></a>`apr_interval_time_t`

```ec
declare type apr_interval_time_t as apr_int64_t
```

### <a id="apr_short_interval_time_t"></a>`apr_short_interval_time_t`

```ec
declare type apr_short_interval_time_t as apr_int32_t
```

### <a id="apr_time_exp_t"></a>`apr_time_exp_t`

```ec
declare rec apr_time_exp_t
```

**Fields**

- <a id="apr_time_exp_t.tm_usec"></a>`tm_usec: apr_int32_t`
- <a id="apr_time_exp_t.tm_sec"></a>`tm_sec: apr_int32_t`
- <a id="apr_time_exp_t.tm_min"></a>`tm_min: apr_int32_t`
- <a id="apr_time_exp_t.tm_hour"></a>`tm_hour: apr_int32_t`
- <a id="apr_time_exp_t.tm_mday"></a>`tm_mday: apr_int32_t`
- <a id="apr_time_exp_t.tm_mon"></a>`tm_mon: apr_int32_t`
- <a id="apr_time_exp_t.tm_year"></a>`tm_year: apr_int32_t`
- <a id="apr_time_exp_t.tm_wday"></a>`tm_wday: apr_int32_t`
- <a id="apr_time_exp_t.tm_yday"></a>`tm_yday: apr_int32_t`
- <a id="apr_time_exp_t.tm_isdst"></a>`tm_isdst: apr_int32_t`
- <a id="apr_time_exp_t.tm_gmtoff"></a>`tm_gmtoff: apr_int32_t`

## Values

### <a id="apr_month_snames"></a>`apr_month_snames`

```ec
declare val apr_month_snames as @Char
```

### <a id="apr_day_snames"></a>`apr_day_snames`

```ec
declare val apr_day_snames as @Char
```

## Functions

### <a id="apr_time_now"></a>`apr_time_now`

```ec
fn apr_time_now(): apr_time_t
```

*Binds the C function `apr_time_now`.*

### <a id="apr_time_ansi_put"></a>`apr_time_ansi_put`

```ec
fn apr_time_ansi_put(out result: apr_time_t, input: time_t): apr_status_t
```

*Binds the C function `apr_time_ansi_put`.*

### <a id="apr_time_exp_tz"></a>`apr_time_exp_tz`

```ec
fn apr_time_exp_tz(out result: apr_time_exp_t, input: apr_time_t, offs: apr_int32_t): apr_status_t
```

*Binds the C function `apr_time_exp_tz`.*

### <a id="apr_time_exp_gmt"></a>`apr_time_exp_gmt`

```ec
fn apr_time_exp_gmt(out result: apr_time_exp_t, input: apr_time_t): apr_status_t
```

*Binds the C function `apr_time_exp_gmt`.*

### <a id="apr_time_exp_lt"></a>`apr_time_exp_lt`

```ec
fn apr_time_exp_lt(out result: apr_time_exp_t, input: apr_time_t): apr_status_t
```

*Binds the C function `apr_time_exp_lt`.*

### <a id="apr_time_exp_get"></a>`apr_time_exp_get`

```ec
fn apr_time_exp_get(out result: apr_time_t, input: @apr_time_exp_t): apr_status_t
```

*Binds the C function `apr_time_exp_get`.*

### <a id="apr_time_exp_gmt_get"></a>`apr_time_exp_gmt_get`

```ec
fn apr_time_exp_gmt_get(out result: apr_time_t, input: @apr_time_exp_t): apr_status_t
```

*Binds the C function `apr_time_exp_gmt_get`.*

### <a id="apr_sleep"></a>`apr_sleep`

```ec
fn apr_sleep(t: apr_interval_time_t)
```

*Binds the C function `apr_sleep`.*

### <a id="apr_rfc822_date"></a>`apr_rfc822_date`

```ec
fn apr_rfc822_date(date_str: CString, t: apr_time_t): apr_status_t
```

*Binds the C function `apr_rfc822_date`.*

### <a id="apr_ctime"></a>`apr_ctime`

```ec
fn apr_ctime(date_str: CString, t: apr_time_t): apr_status_t
```

*Binds the C function `apr_ctime`.*

### <a id="apr_strftime"></a>`apr_strftime`

```ec
fn apr_strftime(s: CString, out retsize: apr_size_t, max: apr_size_t, format: CString, tm: @apr_time_exp_t): apr_status_t
```

*Binds the C function `apr_strftime`.*

### <a id="apr_time_clock_hires"></a>`apr_time_clock_hires`

```ec
fn apr_time_clock_hires(p: @apr_pool_t)
```

*Binds the C function `apr_time_clock_hires`.*
