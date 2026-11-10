/**
 * UUIDs (RFC 9562): random ones (version 4), ones that sort by when they were made (version 7), and their text.
 *
 *     use "core:uuid.ec"
 *     val id = try uuid.v4()                              // 0b5c9a3e-6f1d-4c8a-9d2e-7a41b3c5e6f8
 *     val key = try uuid.v7()                             // starts with the time: later ones sort after it
 *     io.log("${id}")                                     // as text, lower case with hyphens
 *     val same = try uuid.parse(uuid.text(id).toUpper())  // either case, with or without hyphens
 *     io.log(same == id)                                  // true: `==`, `hash` and Sets work on a Uuid
 *     io.log(uuid.version(key))                           // 7
 *     if val made = uuid.timeOf(key) do                   // when it was made, to the millisecond
 *         io.log(time.unixMillis(made))
 *
 * Both kinds take their random bits from the system's secure random source ({@link math.secureRandom}), so they
 * can't be guessed. A v4 is the one for a session id or a link to share; a v7 tells when it was made. Two v7 UUIDs made
 * in the same millisecond are in no particular order. A Uuid is two numbers, not memory in a pool.
 */
module "uuid"
use "core:errors.ec"
use "core:string.ec"
use "core:math.ec"
use "core:time.ec"

/** A UUID: 128 bits, as two halves read with the first byte in the highest bits. */
rec Uuid as
    /** The first 8 bytes (the time of a v7, and the version). */
    high: ULong
    /** The last 8 bytes (the variant, and random bits). */
    low: ULong

/** The nil UUID, all zero: 00000000-0000-0000-0000-000000000000. */
val zero = Uuid { 0, 0 }

/** The digits of hexadecimal, by their value. */
priv val HEX = "0123456789abcdef"

/** The Uuid of 16 bytes, the first in the highest bits. */
priv fn ofBytes(b: UChar[]): Uuid do
    var high: ULong = 0
    var low: ULong = 0
    for i in 0..<8 do
        high = (high << 8) | (b[i] as ULong)
        low = (low << 8) | (b[i + 8] as ULong)
    ret Uuid { high, low }

/** `u` with its version set to `version` and its variant to RFC 9562's (the bits 10). */
priv fn stamped(u: Uuid, version: ULong): Uuid = Uuid { (u.high & ~(0xF000 as ULong)) | (version << 12), (u.low & 0x3FFFFFFFFFFFFFFF) | 0x8000000000000000 }

/**
 * A new random UUID (version 4): 122 random bits.
 * @throws when the system's random source can't be read
 */
fn v4(): Uuid throws OsError do
    val b = new UChar[16]
    try math.secureRandom(b)
    ret stamped(ofBytes(b), 4)

/**
 * A new UUID that starts with the time (version 7): the milliseconds since 1970 in its first 48 bits, then 74 random
 * bits. Sorted by their text or with {@link compare}, they are in the order they were made (to the millisecond), which
 * keeps a database index on them in order.
 * @throws when the system's random source can't be read
 */
fn v7(): Uuid throws OsError = try v7(time.now())

/**
 * A UUID of version 7 for the moment `at`, with fresh random bits. A moment before 1970 counts as 1970.
 * @throws when the system's random source can't be read
 */
fn v7(at: time.Time): Uuid throws OsError do
    val b = new UChar[16]
    try math.secureRandom(b)
    val millis = math.max(time.unixMillis(at), 0) as ULong
    val random = ofBytes(b)
    ret stamped(Uuid { ((millis & 0xFFFFFFFFFFFF) << 16) | (random.high & 0xFFFF), random.low }, 7)

/** The version of `u`: 4 for {@link v4}, 7 for {@link v7}, 0 for {@link zero}. */
fn version(u: Uuid): Int = ((u.high >> 12) & 15) as Int

/** Whether `u` is the nil UUID, all zero. */
fn isZero(u: Uuid): Bool = u.high == 0 and u.low == 0

/**
 * When a version 7 UUID was made, to the millisecond.
 * @return the moment, null for a UUID of another version
 */
fn timeOf(u: Uuid): time.Time? do
    ret null if version(u) != 7
    ret time.fromUnixMillis((u.high >> 16) as Long)

/** -1, 0 or 1 as `a` sorts before, the same as or after `b`: the order of their bytes, and of their text. */
fn compare(a: Uuid, b: Uuid): Int do
    ret if a.high < b.high do -1 else 1 if a.high != b.high
    ret if a.low < b.low do -1 else if a.low > b.low do 1 else 0

/** The 16 bytes of `u`, the first byte first, in the current pool. */
fn bytes(u: Uuid): UChar[] do
    val b = new UChar[16]
    for i in 0..<8 do
        b[i] = ((u.high >> ((7 - i) * 8)) & 255) as UChar
        b[i + 8] = ((u.low >> ((7 - i) * 8)) & 255) as UChar
    ret b

/**
 * The Uuid of 16 bytes, as {@link bytes} gives them.
 * @throws when there aren't exactly 16
 */
fn fromBytes(b: UChar[]): Uuid throws ArgumentError do
    throw argumentError("b", "a UUID is 16 bytes, but this has ${b.len}") if b.len != 16
    ret ofBytes(b)

/** `u` as text: 36 lower-case characters in groups of 8, 4, 4, 4 and 12 hexadecimal digits, joined by hyphens. */
fn text(u: Uuid): String do
    val out = new Char[36]
    var at: Size = 0
    for i in 0..<32 do
        if i == 8 or i == 12 or i == 16 or i == 20 do
            out[at] = '-'
            at += 1
        val half = if i < 16 do u.high else u.low
        val shift = (15 - (i % 16)) * 4
        out[at] = HEX[((half >> shift) & 15) as Size]
        at += 1
    ret out !as String

/** `${u}` writes {@link text}. */
fn str(u: Uuid): String = text(u)

/** The value of the hexadecimal digit `c`, or null. */
priv fn digit(c: Char): ULong? do
    ret (c - '0') as ULong if c >= '0' and c <= '9'
    ret (c - 'a' + 10) as ULong if c >= 'a' and c <= 'f'
    ret (c - 'A' + 10) as ULong if c >= 'A' and c <= 'F'
    ret null

/**
 * The Uuid that `s` writes: 32 hexadecimal digits in either case, with hyphens between the groups as {@link text}
 * writes them or with none at all. Braces around it ("{...}") and a "urn:uuid:" in front are allowed too.
 * @throws when `s` isn't a UUID; the error says where
 */
fn parse(s: String): Uuid throws ParseError do
    var start: Size = 0
    var end = s.len
    if startsWith(s, "urn:uuid:") do
        start = 9
    else if s.len >= 2 and s[0] == '{' and s[s.len - 1] == '}' do
        start = 1
        end = s.len - 1
    val hyphens = end - start == 36
    throw parseError("a UUID is 32 hexadecimal digits, or 36 characters with hyphens, but this has ${end - start} characters", s, start) if end - start != 32 and not hyphens
    var high: ULong = 0
    var low: ULong = 0
    var digits: Size = 0
    for i in start..<end do
        val c = s[i]
        val offset = i - start
        if hyphens and (offset == 8 or offset == 13 or offset == 18 or offset == 23) do
            throw parseError("expected '-' after ${digits} digits", s, i) if c != '-'
        else do
            if val d = digit(c) do
                if digits < 16 do
                    high = (high << 4) | d
                else do
                    low = (low << 4) | d
                digits += 1
            else do
                throw parseError("'${c}' is not a hexadecimal digit", s, i)
    ret Uuid { high, low }

/** Whether `s` is a UUID that {@link parse} would accept. */
fn isValid(s: String): Bool do
    try parse(s) catch e do
        ret false
    ret true
