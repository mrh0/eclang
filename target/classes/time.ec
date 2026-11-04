/**
 * Time: moments (Time), lengths of time (Duration), a steady clock for measuring (Instant), and calendar
 * dates (Date) in UTC, local time or a fixed offset. Built on APR's apr_time.h and C's time.h.
 *
 *     use "core:time.ec"
 *     val start = time.instant()
 *     time.sleep(time.millis(20))
 *     io.log(time.toMillis(time.elapsed(start)))           // about 20
 *
 *     val t = try time.parseIso("2024-02-29T12:30:00Z")
 *     io.log(time.iso(time.add(t, time.days(1))))           // 2024-03-01T12:30:00Z
 *     val d = time.utc(t)                                  // d.year 2024, d.month 2, d.day 29, d.weekday 4 (Thursday)
 *     io.log(time.strftime(d, "%A %d %B %Y"c))               // Thursday 29 February 2024
 *
 * Times are counted in microseconds since 1970-01-01 00:00:00 UTC (the Unix epoch), like apr_time_t.
 */
module "time"
use "core:errors.ec"
use "core:apr/time.ec"
use "core:c/time.ec"

/** The microseconds in a millisecond. */
const MICROS_PER_MILLI: Long = 1000
/** The microseconds in a second. */
const MICROS_PER_SECOND: Long = 1000000
/** The microseconds in a minute. */
const MICROS_PER_MINUTE: Long = 60000000
/** The microseconds in a hour. */
const MICROS_PER_HOUR: Long = 3600000000
/** The microseconds in a day. */
const MICROS_PER_DAY: Long = 86400000000

/** A moment in time, in microseconds since 1970-01-01 00:00:00 UTC. */
rec Time as
    /** Microseconds since the epoch; negative before it. */
    micros: Long

/** A length of time in microseconds; negative when it goes backwards. */
rec Duration as
    /** The length in microseconds. */
    micros: Long

/**
 * A reading of the steady clock, in nanoseconds since some fixed moment (such as the system's start). It only
 * moves forward, also when the system time is set, so it is what to measure durations with.
 */
rec Instant as
    /** Nanoseconds since the clock's fixed moment. */
    nanos: Long

/** A date and time of day, as a calendar shows it in some time zone. */
rec Date as
    /** The year, such as 2024. */
    year: Int
    /** 1 (January) to 12. */
    month: Int
    /** 1 to 31. */
    day: Int
    /** 0 to 23. */
    hour: Int = 0
    /** 0 to 59. */
    minute: Int = 0
    /** 0 to 60 (60 for a leap second). */
    second: Int = 0
    /** 0 to 999999. */
    micro: Int = 0
    /** 0 (Sunday) to 6; filled in by {@link utc}, {@link local}, {@link zoned} and {@link date}. */
    weekday: Int = 0
    /** 0 (January 1st) to 365; filled in the same way as {@code weekday}. */
    yearDay: Int = 0
    /** Seconds east of UTC: 3600 for UTC+01:00. */
    offset: Int = 0
    /** Whether daylight saving time is in effect. */
    dst: Bool = false

// ---------------------------------------------------------------------------------------------------
// Time

/** The current moment, from the system clock. See {@link instant} for measuring how long something takes. */
fn now(): Time = Time { apr_time_now() }

/** The Time of a Unix timestamp: `seconds` since 1970-01-01 00:00:00 UTC. */
fn fromUnix(seconds: Long): Time = Time { seconds * MICROS_PER_SECOND }

/** The Time of a Unix timestamp in milliseconds, as JavaScript and Java count them. */
fn fromUnixMillis(millis: Long): Time = Time { millis * MICROS_PER_MILLI }

/** The Unix timestamp of `t`, in whole seconds (rounded down). */
fn unix(t: Time): Long = floorDiv(t.micros, MICROS_PER_SECOND)

/** The Unix timestamp of `t`, in whole milliseconds (rounded down). */
fn unixMillis(t: Time): Long = floorDiv(t.micros, MICROS_PER_MILLI)

/** The moment `d` after `t`. */
fn add(t: Time, d: Duration): Time = Time { t.micros + d.micros }

/** The moment `d` before `t`. */
fn sub(t: Time, d: Duration): Time = Time { t.micros - d.micros }

/** How long it is from `start` to `end` (negative when `end` is earlier). */
fn between(start: Time, end: Time): Duration = Duration { end.micros - start.micros }

/** How long ago `t` was (negative when it is in the future). */
fn since(t: Time): Duration = between(t, now())

/** How long it is until `t` (negative when it has passed). */
fn until(t: Time): Duration = between(now(), t)

/** Whether `a` is earlier than `b`. */
fn isBefore(a: Time, b: Time): Bool = a.micros < b.micros

/** Whether `a` is later than `b`. */
fn isAfter(a: Time, b: Time): Bool = a.micros > b.micros

/**
 * Compares two moments.
 * @return -1, 0 or 1 as `a` is before, at or after `b`
 */
fn compare(a: Time, b: Time): Int = if a.micros < b.micros do -1 else if a.micros > b.micros do 1 else 0

/**
 * `t` rounded down to a multiple of `d` since the epoch: `truncate(t, hours(1))` is the start of its hour (UTC).
 */
fn truncate(t: Time, d: Duration): Time = Time { floorDiv(t.micros, d.micros) * d.micros }

// ---------------------------------------------------------------------------------------------------
// Duration

/** A Duration of `n` microseconds. */
fn micros(n: Long): Duration = Duration { n }

/** A Duration of `n` milliseconds. */
fn millis(n: Long): Duration = Duration { n * MICROS_PER_MILLI }

/** A Duration of `n` seconds. */
fn seconds(n: Long): Duration = Duration { n * MICROS_PER_SECOND }

/** A Duration of a fraction of seconds: `seconds(1.5)` is 1.5 seconds. */
fn seconds(n: Double): Duration = Duration { (n * 1000000.0) as Long }

/** A Duration of `n` minutes. */
fn minutes(n: Long): Duration = Duration { n * MICROS_PER_MINUTE }

/** A Duration of `n` hours. */
fn hours(n: Long): Duration = Duration { n * MICROS_PER_HOUR }

/** A Duration of `n` days of 24 hours. */
fn days(n: Long): Duration = Duration { n * MICROS_PER_DAY }

/** A Duration of `n` weeks of 7 days. */
fn weeks(n: Long): Duration = Duration { n * 7 * MICROS_PER_DAY }

/** The whole milliseconds in `d` (rounded towards zero). */
fn toMillis(d: Duration): Long = d.micros / MICROS_PER_MILLI

/** The whole seconds in `d` (rounded towards zero). */
fn toSeconds(d: Duration): Long = d.micros / MICROS_PER_SECOND

/** `d` in seconds, with the fraction: 1.5 for 1500 milliseconds. */
fn inSeconds(d: Duration): Double = (d.micros as Double) / 1000000.0

/** The two lengths added up. */
fn add(a: Duration, b: Duration): Duration = Duration { a.micros + b.micros }

/** `a` less `b`, negative when `b` is longer. */
fn sub(a: Duration, b: Duration): Duration = Duration { a.micros - b.micros }

/** `d`, `factor` times over. */
fn scale(d: Duration, factor: Long): Duration = Duration { d.micros * factor }

/** `d` times `factor`: `scale(d, 0.5)` is half of it. */
fn scale(d: Duration, factor: Double): Duration = Duration { ((d.micros as Double) * factor) as Long }

/** The length of `d`, without its sign. */
fn abs(d: Duration): Duration = if d.micros < 0 do Duration { -d.micros } else d

/** Whether `d` goes backwards. */
fn isNegative(d: Duration): Bool = d.micros < 0

/**
 * `d` written the way {@link parseDuration} reads it: "1h30m", "2.5s", "250ms", "1.5ms", "40us", "-3d4h", "0s".
 */
fn text(d: Duration): String do
    ret "0s" if d.micros == 0
    // Count with a negative number, so the smallest Duration doesn't overflow.
    var rest = if d.micros > 0 do -d.micros else d.micros
    var out = if d.micros < 0 do "-" else ""
    val day = rest / MICROS_PER_DAY
    rest -= day * MICROS_PER_DAY
    val hour = rest / MICROS_PER_HOUR
    rest -= hour * MICROS_PER_HOUR
    val minute = rest / MICROS_PER_MINUTE
    rest -= minute * MICROS_PER_MINUTE
    if day != 0 do
        out = concat(out, "${-day}d")
    if hour != 0 do
        out = concat(out, "${-hour}h")
    if minute != 0 do
        out = concat(out, "${-minute}m")
    if rest != 0 do
        if rest % MICROS_PER_SECOND == 0 do
            out = concat(out, "${-rest / MICROS_PER_SECOND}s")
        else if -rest < MICROS_PER_MILLI do
            out = concat(out, "${-rest}us")
        else if -rest < MICROS_PER_SECOND and rest % MICROS_PER_MILLI == 0 do
            out = concat(out, "${-rest / MICROS_PER_MILLI}ms")
        else if -rest < MICROS_PER_SECOND do
            out = concat(out, "${(-rest as Double) / 1000.0}ms")
        else do
            out = concat(out, "${(-rest as Double) / 1000000.0}s")
    ret out

/**
 * Reads a Duration: numbers with a unit each, added up. The units are d, h, m, s, ms and us; a number may have
 * a fraction ("1.5h") and the whole text a sign. "1h30m", "90s", "2.5s", "-250ms".
 * @throws when `s` is empty, a number or unit is missing or unknown, or the duration is too long
 */
fn parseDuration(s: String): Duration throws ParseError do
    throw parseError("a duration can't be empty; write a number with a unit, such as 5s", s, 0) if s.len == 0
    val negative = s[0] == '-'
    var i: Size = if negative or s[0] == '+' do 1 else 0
    throw parseError("a duration needs a number and a unit, such as 5s", s, i) if i == s.len
    var total = 0.0
    while i < s.len do
        val start = i
        var whole = 0.0
        while i < s.len and s[i] >= '0' and s[i] <= '9' do
            whole = whole * 10.0 + ((s[i] - '0') as Double)
            i += 1
        if i < s.len and s[i] == '.' do
            i += 1
            var place = 0.1
            while i < s.len and s[i] >= '0' and s[i] <= '9' do
                whole += place * ((s[i] - '0') as Double)
                place /= 10.0
                i += 1
        throw parseError("expected a number", s, start) if i == start
        val unitStart = i
        while i < s.len and ((s[i] >= 'a' and s[i] <= 'z') or (s[i] >= 'A' and s[i] <= 'Z')) do
            i += 1
        val name = s[unitStart..<i]
        val per = if name == "d" do 86400000000.0 else if name == "h" do 3600000000.0 else if name == "m" do 60000000.0 else if name == "s" do 1000000.0 else if name == "ms" do 1000.0 else if name == "us" do 1.0 else 0.0
        throw parseError("expected a unit after the number: d, h, m, s, ms or us", s, unitStart) if per == 0.0
        total += whole * per
    throw parseError("the duration is too long", s, 0) if total >= 9223372036854775807.0
    ret Duration { if negative do -(total as Long) else total as Long }

// ---------------------------------------------------------------------------------------------------
// Steady clock

/** The steady clock now (see {@link Instant}). */
fn instant(): Instant do
    var ts: timespec
    clock_gettime(CLOCK_MONOTONIC, addrof(ts))
    ret Instant { ts.tv_sec * 1000000000 + ts.tv_nsec }

/** How long ago `start` was read. */
fn elapsed(start: Instant): Duration = Duration { (instant().nanos - start.nanos) / 1000 }

/** The time between two readings, in nanoseconds (for measurements finer than a microsecond). */
fn nanosBetween(start: Instant, end: Instant): Long = end.nanos - start.nanos

/** Waits for `d` (at least; the system may wake the thread a little later). Does nothing for a negative `d`. */
fn sleep(d: Duration) do
    apr_sleep(d.micros) if d.micros > 0

/** The processor time this program has used (all its threads), for profiling. */
fn cpuTime(): Duration do
    var ts: timespec
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, addrof(ts))
    ret Duration { ts.tv_sec * MICROS_PER_SECOND + ts.tv_nsec / 1000 }

// ---------------------------------------------------------------------------------------------------
// Calendar

/** The Date that APR's broken-down time `e` stands for. */
priv fn fromExp(e: apr_time_exp_t): Date = Date {
    year = e.tm_year + 1900,
    month = e.tm_mon + 1,
    day = e.tm_mday,
    hour = e.tm_hour,
    minute = e.tm_min,
    second = e.tm_sec,
    micro = e.tm_usec,
    weekday = e.tm_wday,
    yearDay = e.tm_yday,
    offset = e.tm_gmtoff,
    dst = e.tm_isdst != 0
}

/** APR's broken-down time for `d`. */
priv fn toExp(d: Date): apr_time_exp_t = apr_time_exp_t {
    tm_usec = d.micro,
    tm_sec = d.second,
    tm_min = d.minute,
    tm_hour = d.hour,
    tm_mday = d.day,
    tm_mon = d.month - 1,
    tm_year = d.year - 1900,
    tm_wday = d.weekday,
    tm_yday = d.yearDay,
    tm_isdst = if d.dst do 1 else 0,
    tm_gmtoff = d.offset
}

/** The date and time of `t` in UTC. */
fn utc(t: Time): Date do
    var e: apr_time_exp_t
    apr_time_exp_gmt(addrof(e), t.micros)
    ret fromExp(e)

/** The date and time of `t` in the system's time zone (the TZ environment variable, or /etc/localtime). */
fn local(t: Time): Date do
    var e: apr_time_exp_t
    apr_time_exp_lt(addrof(e), t.micros)
    ret fromExp(e)

/**
 * The date and time of `t` at a fixed offset from UTC: `zoned(t, 2 * 3600)` is UTC+02:00.
 * @param offset seconds east of UTC
 */
fn zoned(t: Time, offset: Int): Date do
    var e: apr_time_exp_t
    apr_time_exp_tz(addrof(e), t.micros, offset)
    ret fromExp(e)

/** Whether `year` has a February 29th (in the Gregorian calendar). */
fn isLeapYear(year: Int): Bool = (year % 4 == 0 and year % 100 != 0) or year % 400 == 0

/**
 * The number of days in a month of a year.
 * @param month 1 (January) to 12
 */
fn daysInMonth(year: Int, month: Int): Int do
    ret 29 if month == 2 and isLeapYear(year)
    ret [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][(month - 1) as Size]

/**
 * Whether every field of `d` is in its range (`weekday` and `yearDay` aren't checked: {@link toTime} ignores them).
 */
fn isValid(d: Date): Bool do
    ret false if d.month < 1 or d.month > 12 or d.day < 1 or d.day > daysInMonth(d.year, d.month)
    ret false if d.hour < 0 or d.hour > 23 or d.minute < 0 or d.minute > 59 or d.second < 0 or d.second > 60
    ret d.micro >= 0 and d.micro <= 999999 and d.offset > -86400 and d.offset < 86400

/**
 * The moment `d` stands for, using its `offset`. Its `weekday` and `yearDay` don't matter.
 * @throws when `d` isn't valid (see {@link isValid})
 */
fn toTime(d: Date): Time throws ArgumentError do
    throw argumentError("date", "${d.year}-${d.month}-${d.day} ${d.hour}:${d.minute}:${d.second} is not a valid date and time") if not isValid(d)
    // Days since the epoch, from the civil date (Howard Hinnant's days_from_civil), so any year works.
    val y = (if d.month <= 2 do d.year - 1 else d.year) as Long
    val era = floorDiv(y, 400)
    val yearOfEra = y - era * 400
    val m = d.month as Long
    val dayOfYear = (153 * (if m > 2 do m - 3 else m + 9) + 2) / 5 + (d.day as Long) - 1
    val dayOfEra = yearOfEra * 365 + yearOfEra / 4 - yearOfEra / 100 + dayOfYear
    val days = era * 146097 + dayOfEra - 719468
    val seconds = days * 86400 + (d.hour as Long) * 3600 + (d.minute as Long) * 60 + (d.second as Long) - (d.offset as Long)
    ret Time { seconds * MICROS_PER_SECOND + (d.micro as Long) }

/**
 * A Date in UTC, with its `weekday` and `yearDay` filled in.
 * @throws when that date and time doesn't exist
 */
fn date(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Date throws ArgumentError do
    val t = try toTime(Date { year = year, month = month, day = day, hour = hour, minute = minute, second = second })
    ret utc(t)

/**
 * Midnight (UTC) of a day.
 * @throws when that day doesn't exist
 */
fn date(year: Int, month: Int, day: Int): Date throws ArgumentError = try date(year, month, day, 0, 0, 0)

/**
 * The same time of day, `n` days later (or earlier, for a negative `n`), at the same offset.
 * @throws when `d` isn't valid
 */
fn addDays(d: Date, n: Int): Date throws ArgumentError do
    val t = try toTime(d)
    ret zoned(add(t, days(n as Long)), d.offset)

/**
 * The same day and time, `n` months later (or earlier), at the same offset. The day is kept in the month:
 * January 31st plus one month is February 28th (or 29th).
 * @throws when `d` isn't valid
 */
fn addMonths(d: Date, n: Int): Date throws ArgumentError do
    val months = (d.year as Long) * 12 + (d.month as Long) - 1 + (n as Long)
    val year = floorDiv(months, 12) as Int
    val month = (months - floorDiv(months, 12) * 12) as Int + 1
    val last = daysInMonth(year, month)
    var moved = d
    moved.year = year
    moved.month = month
    moved.day = if d.day > last do last else d.day
    ret zoned(try toTime(moved), d.offset)

/** The names of the months, from January. */
priv val MONTHS = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]
/** The names of the weekdays, from Sunday. */
priv val WEEKDAYS = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]

/**
 * The name of a month.
 * @param month 1 (January) to 12
 */
fn monthName(month: Int): String = MONTHS[(month - 1) as Size]

/**
 * The name of a weekday.
 * @param weekday 0 (Sunday) to 6
 */
fn weekdayName(weekday: Int): String = WEEKDAYS[weekday as Size]

// ---------------------------------------------------------------------------------------------------
// Text

/** `t` in UTC as ISO 8601: "2024-02-29T12:30:00Z", with a fraction (".25") when it isn't a whole second. */
fn iso(t: Time): String = iso(utc(t))

/** `d` as ISO 8601, with its offset: "2024-02-29T13:30:00+01:00" ("Z" for UTC). */
fn iso(d: Date): String do
    var fraction = ""
    if d.micro != 0 do
        val padded = pad(d.micro, 6)
        var end = padded.len
        while padded[end - 1] == '0' do
            end -= 1
        fraction = concat(".", padded[0..<end])
    val zone = if d.offset == 0 do "Z" else format("${if d.offset < 0 do '-' else '+'}${pad(abs(d.offset) / 3600, 2)}:${pad(abs(d.offset) % 3600 / 60, 2)}")
    ret "${pad(d.year, 4)}-${pad(d.month, 2)}-${pad(d.day, 2)}T${pad(d.hour, 2)}:${pad(d.minute, 2)}:${pad(d.second, 2)}${fraction}${zone}"

/** Just the date part of `d`, as ISO 8601: "2024-02-29". */
fn isoDate(d: Date): String = "${pad(d.year, 4)}-${pad(d.month, 2)}-${pad(d.day, 2)}"

/** `t` as an RFC 822 date, as in HTTP and e-mail headers: "Thu, 29 Feb 2024 12:30:00 GMT". */
fn rfc822(t: Time): String do
    val buffer = new Char[32]
    apr_rfc822_date(buffer.data !as CString, t.micros)
    ret format("${buffer.data !as CString}")

/**
 * `d` written with C's strftime codes.
 * @param pattern the codes, such as "%Y-%m-%d %H:%M" or "%A %d %B" (see `man strftime`)
 */
fn strftime(d: Date, pattern: CString): String do
    var e = toExp(d)
    val buffer = new Char[256]
    var written: Size = 0
    apr_strftime(buffer.data !as CString, addrof(written), buffer.len, pattern, addrof(e))
    ret format("${buffer.data !as CString}")[0..<written]

/** `n` with zeros before it, at least `width` digits long (at most 6 zeros are added). */
priv fn pad(n: Int, width: Int): String do
    val text = format("${n}")
    ret text if text.len >= width as Size
    val zeros = "000000"
    ret concat(zeros[0..<(width as Size) - text.len], text)

/** The absolute value of `n`. */
priv fn abs(n: Int): Int = if n < 0 do -n else n

/** Division rounding down, towards minus infinity. */
priv fn floorDiv(a: Long, b: Long): Long do
    val q = a / b
    ret if (a % b != 0) and ((a < 0) != (b < 0)) do q - 1 else q

/**
 * Reads an ISO 8601 date and time: "2024-02-29", "2024-02-29T12:30", "2024-02-29T12:30:00Z",
 * "2024-02-29 12:30:00.250+01:00". Without a time it is midnight; without an offset, UTC.
 * @throws when `s` isn't in that format, or the date doesn't exist
 */
fn parseIso(s: String): Time throws ParseError do
    val d = try parseIsoDate(s)
    ret toTime(d) ?? Time { 0 }

/**
 * Like {@link parseIso}, but gives the Date as written, with its offset.
 * @throws when `s` isn't in that format, or the date doesn't exist
 */
fn parseIsoDate(s: String): Date throws ParseError do
    var at: Size = 0
    val year = try digits(s, at, 4)
    try expect(s, at + 4, '-')
    val month = try digits(s, at + 5, 2)
    try expect(s, at + 7, '-')
    val day = try digits(s, at + 8, 2)
    at = 10
    var d = Date { year = year, month = month, day = day }
    if at < s.len do
        throw parseError("expected 'T' or a space between the date and the time", s, at) if s[at] != 'T' and s[at] != 't' and s[at] != ' '
        d.hour = try digits(s, at + 1, 2)
        try expect(s, at + 3, ':')
        d.minute = try digits(s, at + 4, 2)
        at += 6
        if at < s.len and s[at] == ':' do
            d.second = try digits(s, at + 1, 2)
            at += 3
            if at < s.len and (s[at] == '.' or s[at] == ',') do
                at += 1
                var place = 100000
                val start = at
                while at < s.len and s[at] >= '0' and s[at] <= '9' do
                    d.micro += place * ((s[at] - '0') as Int)
                    place /= 10
                    at += 1
                throw parseError("expected digits after the decimal point", s, at) if at == start
        if at < s.len do
            val c = s[at]
            if c == 'Z' or c == 'z' do
                at += 1
            else if c == '+' or c == '-' do
                val hours = try digits(s, at + 1, 2)
                var mins = 0
                if at + 3 < s.len and s[at + 3] == ':' do
                    mins = try digits(s, at + 4, 2)
                    at += 6
                else if at + 3 < s.len do
                    mins = try digits(s, at + 3, 2)
                    at += 5
                else do
                    at += 3
                d.offset = (hours * 3600 + mins * 60) * (if c == '-' do -1 else 1)
            else do
                throw parseError("expected 'Z' or an offset such as +01:00 after the time", s, at)
        throw parseError("unexpected text after the date and time", s, at) if at != s.len
    throw parseError("${s} is not a valid date and time", s, 0) if not isValid(d)
    ret zoned(toTime(d) ?? Time { 0 }, d.offset)

/**
 * The number in the `n` digits of `s` at `at`.
 * @throws when there are fewer, or one isn't a digit
 */
priv fn digits(s: String, at: Size, n: Size): Int throws ParseError do
    throw parseError("expected ${n} digits", s, at) if at + n > s.len
    var value = 0
    for i in at..<at + n do
        throw parseError("expected a digit", s, i) if s[i] < '0' or s[i] > '9'
        value = value * 10 + ((s[i] - '0') as Int)
    ret value

/**
 * Checks that `s` has `c` at `at`.
 * @throws when it doesn't
 */
priv fn expect(s: String, at: Size, c: Char) throws ParseError do
    throw parseError("expected '${c}'", s, at) if at >= s.len or s[at] != c
