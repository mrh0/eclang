# `time.ec`

## Types

### <a id="time_t"></a>`time_t`

```ec
declare type time_t as Long
```

### <a id="clock_t"></a>`clock_t`

```ec
declare type clock_t as Long
```

### <a id="clockid_t"></a>`clockid_t`

```ec
declare type clockid_t as Int
```

### <a id="timespec"></a>`timespec`

```ec
declare struct timespec
```

**Fields**

- <a id="timespec.tv_sec"></a>`tv_sec: time_t`
- <a id="timespec.tv_nsec"></a>`tv_nsec: Long`

### <a id="tm"></a>`tm`

```ec
declare struct tm
```

**Fields**

- <a id="tm.tm_sec"></a>`tm_sec: Int`
- <a id="tm.tm_min"></a>`tm_min: Int`
- <a id="tm.tm_hour"></a>`tm_hour: Int`
- <a id="tm.tm_mday"></a>`tm_mday: Int`
- <a id="tm.tm_mon"></a>`tm_mon: Int`
- <a id="tm.tm_year"></a>`tm_year: Int`
- <a id="tm.tm_wday"></a>`tm_wday: Int`
- <a id="tm.tm_yday"></a>`tm_yday: Int`
- <a id="tm.tm_isdst"></a>`tm_isdst: Int`

## Values

### <a id="CLOCKS_PER_SEC"></a>`CLOCKS_PER_SEC`

```ec
declare val CLOCKS_PER_SEC as Long
```

### <a id="CLOCK_REALTIME"></a>`CLOCK_REALTIME`

```ec
declare val CLOCK_REALTIME as clockid_t
```

### <a id="CLOCK_MONOTONIC"></a>`CLOCK_MONOTONIC`

```ec
declare val CLOCK_MONOTONIC as clockid_t
```

### <a id="CLOCK_PROCESS_CPUTIME_ID"></a>`CLOCK_PROCESS_CPUTIME_ID`

```ec
declare val CLOCK_PROCESS_CPUTIME_ID as clockid_t
```

## Functions

### <a id="time"></a>`time`

```ec
fn time(t: @time_t?): time_t
```

*Binds the C function `time`.*

### <a id="clock"></a>`clock`

```ec
fn clock(): clock_t
```

*Binds the C function `clock`.*

### <a id="difftime"></a>`difftime`

```ec
fn difftime(end: time_t, start: time_t): Double
```

*Binds the C function `difftime`.*

### <a id="mktime"></a>`mktime`

```ec
fn mktime(t: @tm): time_t
```

*Binds the C function `mktime`.*

### <a id="timegm"></a>`timegm`

```ec
fn timegm(t: @tm): time_t
```

*Binds the C function `timegm`.*

### <a id="gmtime_r"></a>`gmtime_r`

```ec
fn gmtime_r(t: @time_t, result: @tm): @tm?
```

*Binds the C function `gmtime_r`.*

### <a id="localtime_r"></a>`localtime_r`

```ec
fn localtime_r(t: @time_t, result: @tm): @tm?
```

*Binds the C function `localtime_r`.*

### <a id="strftime"></a>`strftime`

```ec
fn strftime(s: CString, max: Size, format: CString, t: @tm): Size
```

*Binds the C function `strftime`.*

### <a id="clock_gettime"></a>`clock_gettime`

```ec
fn clock_gettime(clock: clockid_t, out ts: timespec): Int
```

*Binds the C function `clock_gettime`.*

### <a id="nanosleep"></a>`nanosleep`

```ec
fn nanosleep(request: @timespec, remaining: @timespec?): Int
```

*Binds the C function `nanosleep`.*
