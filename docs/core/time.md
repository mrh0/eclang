# Module `time`

Time: moments (Time), lengths of time (Duration), a steady clock for measuring (Instant), and calendar
dates (Date) in UTC, local time or a fixed offset. Built on APR's apr_time.h and C's time.h.

    use "core:time.ec"
    val start = time.instant()
    time.sleep(time.millis(20))
    io.log(time.toMillis(time.elapsed(start)))           // about 20

    val t = try time.parseIso("2024-02-29T12:30:00Z")
    io.log(time.iso(time.add(t, time.days(1))))           // 2024-03-01T12:30:00Z
    val d = time.utc(t)                                  // d.year 2024, d.month 2, d.day 29, d.weekday 4 (Thursday)
    io.log(time.strftime(d, "%A %d %B %Y"c))               // Thursday 29 February 2024

Times are counted in microseconds since 1970-01-01 00:00:00 UTC (the Unix epoch), like apr_time_t.

## Types

### <a id="time.Time"></a>`Time`

```ec
rec Time
```

A moment in time, in microseconds since 1970-01-01 00:00:00 UTC.

**Fields**

- <a id="time.Time.micros"></a>`micros: Long` — Microseconds since the epoch; negative before it.

### <a id="time.Duration"></a>`Duration`

```ec
rec Duration
```

A length of time in microseconds; negative when it goes backwards.

**Fields**

- <a id="time.Duration.micros"></a>`micros: Long` — The length in microseconds.

### <a id="time.Instant"></a>`Instant`

```ec
rec Instant
```

A reading of the steady clock, in nanoseconds since some fixed moment (such as the system's start). It only
moves forward, also when the system time is set, so it is what to measure durations with.

**Fields**

- <a id="time.Instant.nanos"></a>`nanos: Long` — Nanoseconds since the clock's fixed moment.

### <a id="time.Date"></a>`Date`

```ec
rec Date
```

A date and time of day, as a calendar shows it in some time zone.

**Fields**

- <a id="time.Date.year"></a>`year: Int` — The year, such as 2024.
- <a id="time.Date.month"></a>`month: Int` — 1 (January) to 12.
- <a id="time.Date.day"></a>`day: Int` — 1 to 31.
- <a id="time.Date.hour"></a>`hour: Int = 0` — 0 to 23.
- <a id="time.Date.minute"></a>`minute: Int = 0` — 0 to 59.
- <a id="time.Date.second"></a>`second: Int = 0` — 0 to 60 (60 for a leap second).
- <a id="time.Date.micro"></a>`micro: Int = 0` — 0 to 999999.
- <a id="time.Date.weekday"></a>`weekday: Int = 0` — 0 (Sunday) to 6; filled in by [`utc`](#time.utc), [`local`](#time.local), [`zoned`](#time.zoned) and [`date`](#time.date).
- <a id="time.Date.yearDay"></a>`yearDay: Int = 0` — 0 (January 1st) to 365; filled in the same way as `weekday`.
- <a id="time.Date.offset"></a>`offset: Int = 0` — Seconds east of UTC: 3600 for UTC+01:00.
- <a id="time.Date.dst"></a>`dst: Bool = false` — Whether daylight saving time is in effect.

## Values

### <a id="time.MICROS_PER_MILLI"></a>`MICROS_PER_MILLI`

```ec
const MICROS_PER_MILLI: Long = 1000
```

The microseconds in a millisecond.

### <a id="time.MICROS_PER_SECOND"></a>`MICROS_PER_SECOND`

```ec
const MICROS_PER_SECOND: Long = 1000000
```

The microseconds in a second.

### <a id="time.MICROS_PER_MINUTE"></a>`MICROS_PER_MINUTE`

```ec
const MICROS_PER_MINUTE: Long = 60000000
```

The microseconds in a minute.

### <a id="time.MICROS_PER_HOUR"></a>`MICROS_PER_HOUR`

```ec
const MICROS_PER_HOUR: Long = 3600000000
```

The microseconds in a hour.

### <a id="time.MICROS_PER_DAY"></a>`MICROS_PER_DAY`

```ec
const MICROS_PER_DAY: Long = 86400000000
```

The microseconds in a day.

## Functions

### <a id="time.now"></a>`now`

```ec
fn now(): Time
```

The current moment, from the system clock. See [`instant`](#time.instant) for measuring how long something takes.

### <a id="time.fromUnix"></a>`fromUnix`

```ec
fn fromUnix(seconds: Long): Time
```

The Time of a Unix timestamp: `seconds` since 1970-01-01 00:00:00 UTC.

### <a id="time.fromUnixMillis"></a>`fromUnixMillis`

```ec
fn fromUnixMillis(millis: Long): Time
```

The Time of a Unix timestamp in milliseconds, as JavaScript and Java count them.

### <a id="time.unix"></a>`unix`

```ec
fn unix(t: Time): Long
```

The Unix timestamp of `t`, in whole seconds (rounded down).

### <a id="time.unixMillis"></a>`unixMillis`

```ec
fn unixMillis(t: Time): Long
```

The Unix timestamp of `t`, in whole milliseconds (rounded down).

### <a id="time.add"></a>`add`

```ec
fn add(t: Time, d: Duration): Time
```

The moment `d` after `t`.

### <a id="time.sub"></a>`sub`

```ec
fn sub(t: Time, d: Duration): Time
```

The moment `d` before `t`.

### <a id="time.between"></a>`between`

```ec
fn between(start: Time, end: Time): Duration
```

How long it is from `start` to `end` (negative when `end` is earlier).

### <a id="time.since"></a>`since`

```ec
fn since(t: Time): Duration
```

How long ago `t` was (negative when it is in the future).

### <a id="time.until"></a>`until`

```ec
fn until(t: Time): Duration
```

How long it is until `t` (negative when it has passed).

### <a id="time.isBefore"></a>`isBefore`

```ec
fn isBefore(a: Time, b: Time): Bool
```

Whether `a` is earlier than `b`.

### <a id="time.isAfter"></a>`isAfter`

```ec
fn isAfter(a: Time, b: Time): Bool
```

Whether `a` is later than `b`.

### <a id="time.compare"></a>`compare`

```ec
fn compare(a: Time, b: Time): Int
```

Compares two moments.

**Returns** -1, 0 or 1 as `a` is before, at or after `b`

### <a id="time.truncate"></a>`truncate`

```ec
fn truncate(t: Time, d: Duration): Time
```

`t` rounded down to a multiple of `d` since the epoch: `truncate(t, hours(1))` is the start of its hour (UTC).

### <a id="time.micros"></a>`micros`

```ec
fn micros(n: Long): Duration
```

A Duration of `n` microseconds.

### <a id="time.millis"></a>`millis`

```ec
fn millis(n: Long): Duration
```

A Duration of `n` milliseconds.

### <a id="time.seconds"></a>`seconds`

```ec
fn seconds(n: Long): Duration
```

A Duration of `n` seconds.

### `seconds`

```ec
fn seconds(n: Double): Duration
```

A Duration of a fraction of seconds: `seconds(1.5)` is 1.5 seconds.

### <a id="time.minutes"></a>`minutes`

```ec
fn minutes(n: Long): Duration
```

A Duration of `n` minutes.

### <a id="time.hours"></a>`hours`

```ec
fn hours(n: Long): Duration
```

A Duration of `n` hours.

### <a id="time.days"></a>`days`

```ec
fn days(n: Long): Duration
```

A Duration of `n` days of 24 hours.

### <a id="time.weeks"></a>`weeks`

```ec
fn weeks(n: Long): Duration
```

A Duration of `n` weeks of 7 days.

### <a id="time.toMillis"></a>`toMillis`

```ec
fn toMillis(d: Duration): Long
```

The whole milliseconds in `d` (rounded towards zero).

### <a id="time.toSeconds"></a>`toSeconds`

```ec
fn toSeconds(d: Duration): Long
```

The whole seconds in `d` (rounded towards zero).

### <a id="time.inSeconds"></a>`inSeconds`

```ec
fn inSeconds(d: Duration): Double
```

`d` in seconds, with the fraction: 1.5 for 1500 milliseconds.

### `add`

```ec
fn add(a: Duration, b: Duration): Duration
```

The two lengths added up.

### `sub`

```ec
fn sub(a: Duration, b: Duration): Duration
```

`a` less `b`, negative when `b` is longer.

### <a id="time.scale"></a>`scale`

```ec
fn scale(d: Duration, factor: Long): Duration
```

`d`, `factor` times over.

### `scale`

```ec
fn scale(d: Duration, factor: Double): Duration
```

`d` times `factor`: `scale(d, 0.5)` is half of it.

### <a id="time.abs"></a>`abs`

```ec
fn abs(d: Duration): Duration
```

The length of `d`, without its sign.

### <a id="time.isNegative"></a>`isNegative`

```ec
fn isNegative(d: Duration): Bool
```

Whether `d` goes backwards.

### <a id="time.text"></a>`text`

```ec
fn text(d: Duration): String
```

`d` written the way [`parseDuration`](#time.parseDuration) reads it: "1h30m", "2.5s", "250ms", "1.5ms", "40us", "-3d4h", "0s".

### <a id="time.parseDuration"></a>`parseDuration`

```ec
fn parseDuration(s: String): Duration throws ParseError
```

Reads a Duration: numbers with a unit each, added up. The units are d, h, m, s, ms and us; a number may have
a fraction ("1.5h") and the whole text a sign. "1h30m", "90s", "2.5s", "-250ms".

**Throws**

- `ParseError` — when `s` is empty, a number or unit is missing or unknown, or the duration is too long

### <a id="time.instant"></a>`instant`

```ec
fn instant(): Instant
```

The steady clock now (see [`Instant`](#time.Instant)).

### <a id="time.elapsed"></a>`elapsed`

```ec
fn elapsed(start: Instant): Duration
```

How long ago `start` was read.

### <a id="time.nanosBetween"></a>`nanosBetween`

```ec
fn nanosBetween(start: Instant, end: Instant): Long
```

The time between two readings, in nanoseconds (for measurements finer than a microsecond).

### <a id="time.sleep"></a>`sleep`

```ec
fn sleep(d: Duration)
```

Waits for `d` (at least; the system may wake the thread a little later). Does nothing for a negative `d`.

### <a id="time.cpuTime"></a>`cpuTime`

```ec
fn cpuTime(): Duration
```

The processor time this program has used (all its threads), for profiling.

### <a id="time.utc"></a>`utc`

```ec
fn utc(t: Time): Date
```

The date and time of `t` in UTC.

### <a id="time.local"></a>`local`

```ec
fn local(t: Time): Date
```

The date and time of `t` in the system's time zone (the TZ environment variable, or /etc/localtime).

### <a id="time.zoned"></a>`zoned`

```ec
fn zoned(t: Time, offset: Int): Date
```

The date and time of `t` at a fixed offset from UTC: `zoned(t, 2 * 3600)` is UTC+02:00.

**Parameters**

- `offset` — seconds east of UTC

### <a id="time.isLeapYear"></a>`isLeapYear`

```ec
fn isLeapYear(year: Int): Bool
```

Whether `year` has a February 29th (in the Gregorian calendar).

### <a id="time.daysInMonth"></a>`daysInMonth`

```ec
fn daysInMonth(year: Int, month: Int): Int
```

The number of days in a month of a year.

**Parameters**

- `month` — 1 (January) to 12

### <a id="time.isValid"></a>`isValid`

```ec
fn isValid(d: Date): Bool
```

Whether every field of `d` is in its range (`weekday` and `yearDay` aren't checked: [`toTime`](#time.toTime) ignores them).

### <a id="time.toTime"></a>`toTime`

```ec
fn toTime(d: Date): Time throws ArgumentError
```

The moment `d` stands for, using its `offset`. Its `weekday` and `yearDay` don't matter.

**Throws**

- `ArgumentError` — when `d` isn't valid (see [`isValid`](#time.isValid))

### <a id="time.date"></a>`date`

```ec
fn date(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Date throws ArgumentError
```

A Date in UTC, with its `weekday` and `yearDay` filled in.

**Throws**

- `ArgumentError` — when that date and time doesn't exist

### `date`

```ec
fn date(year: Int, month: Int, day: Int): Date throws ArgumentError
```

Midnight (UTC) of a day.

**Throws**

- `ArgumentError` — when that day doesn't exist

### <a id="time.addDays"></a>`addDays`

```ec
fn addDays(d: Date, n: Int): Date throws ArgumentError
```

The same time of day, `n` days later (or earlier, for a negative `n`), at the same offset.

**Throws**

- `ArgumentError` — when `d` isn't valid

### <a id="time.addMonths"></a>`addMonths`

```ec
fn addMonths(d: Date, n: Int): Date throws ArgumentError
```

The same day and time, `n` months later (or earlier), at the same offset. The day is kept in the month:
January 31st plus one month is February 28th (or 29th).

**Throws**

- `ArgumentError` — when `d` isn't valid

### <a id="time.monthName"></a>`monthName`

```ec
fn monthName(month: Int): String
```

The name of a month.

**Parameters**

- `month` — 1 (January) to 12

### <a id="time.weekdayName"></a>`weekdayName`

```ec
fn weekdayName(weekday: Int): String
```

The name of a weekday.

**Parameters**

- `weekday` — 0 (Sunday) to 6

### <a id="time.iso"></a>`iso`

```ec
fn iso(t: Time): String
```

`t` in UTC as ISO 8601: "2024-02-29T12:30:00Z", with a fraction (".25") when it isn't a whole second.

### `iso`

```ec
fn iso(d: Date): String
```

`d` as ISO 8601, with its offset: "2024-02-29T13:30:00+01:00" ("Z" for UTC).

### <a id="time.isoDate"></a>`isoDate`

```ec
fn isoDate(d: Date): String
```

Just the date part of `d`, as ISO 8601: "2024-02-29".

### <a id="time.rfc822"></a>`rfc822`

```ec
fn rfc822(t: Time): String
```

`t` as an RFC 822 date, as in HTTP and e-mail headers: "Thu, 29 Feb 2024 12:30:00 GMT".

### <a id="time.strftime"></a>`strftime`

```ec
fn strftime(d: Date, pattern: CString): String
```

`d` written with C's strftime codes.

**Parameters**

- `pattern` — the codes, such as "%Y-%m-%d %H:%M" or "%A %d %B" (see `man strftime`)

### <a id="time.parseIso"></a>`parseIso`

```ec
fn parseIso(s: String): Time throws ParseError
```

Reads an ISO 8601 date and time: "2024-02-29", "2024-02-29T12:30", "2024-02-29T12:30:00Z",
"2024-02-29 12:30:00.250+01:00". Without a time it is midnight; without an offset, UTC.

**Throws**

- `ParseError` — when `s` isn't in that format, or the date doesn't exist

### <a id="time.parseIsoDate"></a>`parseIsoDate`

```ec
fn parseIsoDate(s: String): Date throws ParseError
```

Like [`parseIso`](#time.parseIso), but gives the Date as written, with its offset.

**Throws**

- `ParseError` — when `s` isn't in that format, or the date doesn't exist
