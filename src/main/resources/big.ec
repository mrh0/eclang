/**
 * Numbers of any size: whole numbers ({@link Integer}) and exact decimals ({@link Decimal}), for money and anything
 * else that a Long would overflow or a Double would round.
 *
 *     use "core:big.ec"
 *     var n = big.integer(1)
 *     for i in 1..=30 do
 *         n = n.mul(big.integer(i))
 *     io.log("${n}")                                       // 265252859812191058636308480000000
 *     val x = try big.parse("123456789012345678901234567890")
 *     io.log("${x.div(n)} ${x.rem(n)}")
 *     io.log(x.text(16))                                   // 18ee90ff6c373e0ee4e3f0ad2
 *
 *     val price = try big.parseDecimal("19.99")
 *     val total = price.mul(big.decimal(3)).add(try big.parseDecimal("0.05"))
 *     io.log("${total}")                                   // 60.02
 *     val each = total.div(big.decimal(7), 2)              // 8.57, to 2 places, half to even
 *     io.log("${each.round(1, :halfUp)}")                  // 8.6
 *
 * Both are values: every operation makes a new one, in the current pool, and never changes its arguments. Call them
 * as functions or as methods (`big.add(a, b)` or `a.add(b)`); eclang has no operators for records. `==` doesn't
 * compare them (they hold arrays): use {@link equals} or {@link compare}. Dividing by zero stops the program, as `/`
 * does.
 *
 * An Integer is a sign and a magnitude in 32-bit limbs; multiplication is the schoolbook method and division Knuth's
 * algorithm D, so numbers of a few thousand digits are quick, but this is not a library for numbers of millions.
 * A Decimal is an Integer scaled by a power of ten: `unscaled` / 10^`scale`. It keeps its scale, so "1.50" stays
 * "1.50"; {@link add} and {@link sub} take the larger scale, {@link mul} the sum of both, and {@link div} the one it
 * is given, rounded as it says ({@link Rounding}).
 */
module "big"
use "core:errors.ec"
use "core:string.ec"
use "core:structures.ec"
use "core:math.ec"

// ---------------------------------------------------------------------------------------------------
// Integers

/** A whole number of any size. */
rec Integer as
    /** Whether it is below zero (never true of zero). */
    negative: Bool
    /** Its size, in 32-bit limbs, the lowest first, with no zero limbs at the top: zero has none. */
    limbs: UInt[]

/** 2^32, the base of the limbs. */
priv const BASE: ULong = 4294967296
/** The lower 32 bits of a ULong. */
priv const LOW: ULong = 4294967295

/** The digits of the bases up to 36, by their value. */
priv val DIGITS = "0123456789abcdefghijklmnopqrstuvwxyz"

/** `a` without its zero limbs at the top. */
priv fn trimmed(a: UInt[]): UInt[] do
    var n = a.len
    while n > 0 and a[n - 1] == 0 do
        n -= 1
    ret a[0..<n]

/** The Integer of a sign and a magnitude, which may have zero limbs at the top. */
priv fn make(negative: Bool, magnitude: UInt[]): Integer do
    val m = trimmed(magnitude)
    ret Integer { negative and m.len > 0, m }

/** The magnitude of a ULong. */
priv fn limbsOf(n: ULong): UInt[] do
    val out = new UInt[2]
    out[0] = (n & LOW) as UInt
    out[1] = (n >> 32) as UInt
    ret trimmed(out)

/** The Integer `n`. */
fn integer(n: Long): Integer do
    ret make(false, limbsOf(n as ULong)) if n >= 0
    ret make(true, limbsOf(~(n as ULong) + 1))

/** The Integer `n`. */
fn integer(n: ULong): Integer = make(false, limbsOf(n))

/** The Integer `n`. */
fn integer(n: Int): Integer = integer(n as Long)

/** Zero. */
val zero = Integer { false, new UInt[0] }

/** -1, 0 or 1 as the magnitude `a` is less than, equal to or greater than `b`. */
priv fn magCompare(a: UInt[], b: UInt[]): Int do
    ret if a.len < b.len do -1 else 1 if a.len != b.len
    var i = a.len
    while i > 0 do
        i -= 1
        ret if a[i] < b[i] do -1 else 1 if a[i] != b[i]
    ret 0

/** `a` + `b`, of magnitudes. */
priv fn magAdd(a: UInt[], b: UInt[]): UInt[] do
    val wide = if a.len >= b.len do a else b
    val narrow = if a.len >= b.len do b else a
    val out = new UInt[wide.len + 1]
    var carry: ULong = 0
    for i in 0..<wide.len do
        var sum = (wide[i] as ULong) + carry
        if i < narrow.len do
            sum += narrow[i] as ULong
        out[i] = (sum & LOW) as UInt
        carry = sum >> 32
    out[wide.len] = carry as UInt
    ret out

/** `a` - `b`, of magnitudes, where `a` is at least `b`. */
priv fn magSub(a: UInt[], b: UInt[]): UInt[] do
    val out = new UInt[a.len]
    var borrow: ULong = 0
    for i in 0..<a.len do
        val take = (if i < b.len do b[i] as ULong else 0) + borrow
        val have = a[i] as ULong
        if have >= take do
            out[i] = (have - take) as UInt
            borrow = 0
        else do
            out[i] = (have + BASE - take) as UInt
            borrow = 1
    ret out

/** `a` × `b`, of magnitudes. */
priv fn magMul(a: UInt[], b: UInt[]): UInt[] do
    ret new UInt[0] if a.len == 0 or b.len == 0
    val out = new UInt[a.len + b.len]
    for i in 0..<a.len do
        val ai = a[i] as ULong
        if ai != 0 do
            var carry: ULong = 0
            for j in 0..<b.len do
                // At most (2^32-1)^2 + 2(2^32-1) = 2^64-1: it fits.
                val t = ai * (b[j] as ULong) + (out[i + j] as ULong) + carry
                out[i + j] = (t & LOW) as UInt
                carry = t >> 32
            out[i + b.len] = carry as UInt
    ret out

/** `a` × `m` + `add`, of a magnitude and two limbs. */
priv fn magMulAdd(a: UInt[], m: UInt, add: UInt): UInt[] do
    val out = new UInt[a.len + 1]
    var carry = add as ULong
    for i in 0..<a.len do
        val t = (a[i] as ULong) * (m as ULong) + carry
        out[i] = (t & LOW) as UInt
        carry = t >> 32
    out[a.len] = carry as UInt
    ret out

/** The quotient and remainder of the magnitude `a` by the limb `d`, which isn't 0. */
priv fn magDivSmall(a: UInt[], d: UInt): (UInt[], UInt) do
    val q = new UInt[a.len]
    val divisor = d as ULong
    var r: ULong = 0
    var i = a.len
    while i > 0 do
        i -= 1
        val cur = (r << 32) | (a[i] as ULong)
        q[i] = (cur / divisor) as UInt
        r = cur % divisor
    ret (q, r as UInt)

/** How many zero bits are above the highest one of `x`, which isn't 0. */
priv fn leadingZeros(x: UInt): Int do
    var n = 0
    var v = x
    while v & 0x80000000 == 0 do
        v = v << 1
        n += 1
    ret n

/** The magnitude `a` shifted left by `s` bits (less than 32), in `len` limbs. */
priv fn shiftedLeft(a: UInt[], s: Int, len: Size): UInt[] do
    val out = new UInt[len]
    for i in 0..<a.len do
        if s == 0 do
            out[i] = a[i]
        else do
            out[i] = out[i] | (a[i] << s)
            if i + 1 < len do
                out[i + 1] = a[i] >> (32 - s)
    ret out

/** The magnitude `a` shifted right by `s` bits (less than 32). */
priv fn shiftedRight(a: UInt[], s: Int): UInt[] do
    val out = new UInt[a.len]
    for i in 0..<a.len do
        if s == 0 do
            out[i] = a[i]
        else do
            out[i] = a[i] >> s
            if i + 1 < a.len do
                out[i] = out[i] | (a[i + 1] << (32 - s))
    ret out

/**
 * The quotient and remainder of the magnitude `a` by `b`, which isn't zero: Knuth's algorithm D (The Art of
 * Computer Programming, 4.3.1), as Hacker's Delight writes it.
 */
priv fn magDivRem(a: UInt[], b: UInt[]): (UInt[], UInt[]) do
    ret (new UInt[0], a) if magCompare(a, b) < 0
    if b.len == 1 do
        val (q, r) = magDivSmall(a, b[0])
        ret (q, limbsOf(r as ULong))
    val n = b.len
    val m = a.len - n
    val s = leadingZeros(b[n - 1])
    val vn = shiftedLeft(b, s, n)
    val un = shiftedLeft(a, s, a.len + 1)
    val q = new UInt[m + 1]
    val top = vn[n - 1] as ULong
    val next = vn[n - 2] as ULong
    var j = m + 1
    while j > 0 do
        j -= 1
        // Guess the next limb of the quotient from the top two limbs; it is at most 2 too large.
        val num = ((un[j + n] as ULong) << 32) | (un[j + n - 1] as ULong)
        var qhat = num / top
        var rhat = num % top
        while qhat >= BASE or qhat * next > ((rhat << 32) | (un[j + n - 2] as ULong)) do
            qhat -= 1
            rhat += top
            break if rhat >= BASE
        // Subtract qhat × vn from the part of un it lines up with.
        var k: Long = 0
        var t: Long = 0
        for i in 0..<n do
            val p = qhat * (vn[i] as ULong)
            t = (un[i + j] as Long) - k - ((p & LOW) as Long)
            un[i + j] = (t & 0xFFFFFFFF) as UInt
            k = ((p >> 32) as Long) - (t >> 32)
        t = (un[j + n] as Long) - k
        un[j + n] = (t & 0xFFFFFFFF) as UInt
        if t < 0 do
            // qhat was one too large: add vn back.
            q[j] = (qhat - 1) as UInt
            var carry: ULong = 0
            for i in 0..<n do
                val sum = (un[i + j] as ULong) + (vn[i] as ULong) + carry
                un[i + j] = (sum & LOW) as UInt
                carry = sum >> 32
            un[j + n] = (((un[j + n] as ULong) + carry) & LOW) as UInt
        else do
            q[j] = qhat as UInt
    ret (q, shiftedRight(un[0..<n], s))

/** `a` + `b`. */
fn add(a: Integer, b: Integer): Integer do
    ret make(a.negative, magAdd(a.limbs, b.limbs)) if a.negative == b.negative
    val c = magCompare(a.limbs, b.limbs)
    ret zero if c == 0
    ret if c > 0 do make(a.negative, magSub(a.limbs, b.limbs)) else make(b.negative, magSub(b.limbs, a.limbs))

/** `a` + `n`. */
fn add(a: Integer, n: Long): Integer = add(a, integer(n))

/** `a` - `b`. */
fn sub(a: Integer, b: Integer): Integer = add(a, neg(b))

/** `a` - `n`. */
fn sub(a: Integer, n: Long): Integer = add(a, integer(n).neg())

/** `a` × `b`. */
fn mul(a: Integer, b: Integer): Integer = make(a.negative != b.negative, magMul(a.limbs, b.limbs))

/** `a` × `n`. */
fn mul(a: Integer, n: Long): Integer = mul(a, integer(n))

/**
 * `a` / `b` and `a` % `b`, at once: the quotient rounded towards zero, and a remainder with the sign of `a`, as `/`
 * and `%` work on Longs. Stops the program when `b` is zero.
 */
fn divRem(a: Integer, b: Integer, at: Location = caller): (Integer, Integer) do
    panic("division by zero", at) if b.limbs.len == 0
    val (q, r) = magDivRem(a.limbs, b.limbs)
    ret (make(a.negative != b.negative, q), make(a.negative, r))

/** `a` / `b`, rounded towards zero, as `/` on Longs. Stops the program when `b` is zero. */
fn div(a: Integer, b: Integer, at: Location = caller): Integer do
    val (q, _) = divRem(a, b, at)
    ret q

/** The remainder of `a` / `b`, with the sign of `a`, as `%` on Longs. Stops the program when `b` is zero. */
fn rem(a: Integer, b: Integer, at: Location = caller): Integer do
    val (_, r) = divRem(a, b, at)
    ret r

/** `a` modulo `b`: the remainder of `a` / `b` with the sign of `b`, as {@link math.floorMod}. */
fn mod(a: Integer, b: Integer, at: Location = caller): Integer do
    val r = rem(a, b, at)
    ret if r.limbs.len > 0 and r.negative != b.negative do add(r, b) else r

/** `-a`. */
fn neg(a: Integer): Integer = Integer { not a.negative and a.limbs.len > 0, a.limbs }

/** `a` without its sign. */
fn abs(a: Integer): Integer = Integer { false, a.limbs }

/** -1, 0 or 1, as `a` is below, at or above zero. */
fn sign(a: Integer): Int = if a.limbs.len == 0 do 0 else if a.negative do -1 else 1

/** Whether `a` is zero. */
fn isZero(a: Integer): Bool = a.limbs.len == 0

/** Whether `a` is even. */
fn isEven(a: Integer): Bool = a.limbs.len == 0 or a.limbs[0] & 1 == 0

/** -1, 0 or 1 as `a` is less than, equal to or greater than `b`. */
fn compare(a: Integer, b: Integer): Int do
    ret if a.negative do -1 else 1 if a.negative != b.negative
    val c = magCompare(a.limbs, b.limbs)
    ret if a.negative do -c else c

/** -1, 0 or 1 as `a` is less than, equal to or greater than `n`. */
fn compare(a: Integer, n: Long): Int = compare(a, integer(n))

/** Whether `a` and `b` are the same number. */
fn equals(a: Integer, b: Integer): Bool = compare(a, b) == 0

/** Whether `a` is the number `n`. */
fn equals(a: Integer, n: Long): Bool = compare(a, integer(n)) == 0

/** `a` to the power `e`, which must not be negative. */
fn pow(a: Integer, e: Long): Integer requires e >= 0, "a negative power of a whole number isn't whole" do
    var result = integer(1)
    var square = a
    var left = e
    while left > 0 do
        if left & 1 == 1 do
            result = mul(result, square)
        left = left >> 1
        if left > 0 do
            square = mul(square, square)
    ret result

/**
 * `a` to the power `e`, modulo `m`, without the whole power: for modular arithmetic as in RSA.
 * @return a number from 0 up to `m`
 */
fn powMod(a: Integer, e: Integer, m: Integer, at: Location = caller): Integer requires not e.negative, "the power can't be negative" do
    panic("division by zero", at) if m.limbs.len == 0
    var result = mod(integer(1), m)
    var square = mod(a, m)
    // The bits of e, from the lowest.
    for i in 0..<e.limbs.len do
        var word = e.limbs[i]
        for bit in 0..<32 do
            if word & 1 == 1 do
                result = mod(mul(result, square), m)
            word = word >> 1
            if i + 1 < e.limbs.len or word != 0 do
                square = mod(mul(square, square), m)
    ret result

/** The greatest common divisor of `a` and `b`, never negative; 0 when both are 0. */
fn gcd(a: Integer, b: Integer): Integer do
    var x = abs(a)
    var y = abs(b)
    while y.limbs.len > 0 do
        val r = rem(x, y)
        x = y
        y = r
    ret x

/** The magnitude `a` shifted left by `bits`. */
priv fn magShl(a: UInt[], bits: Size): UInt[] do
    ret a if a.len == 0
    val limbs = bits / 32
    val shifted = shiftedLeft(a, (bits % 32) as Int, a.len + 1)
    val out = new UInt[shifted.len + limbs]
    for i in 0..<shifted.len do
        out[i + limbs] = shifted[i]
    ret out

/** `a` × 2^`bits`. */
fn shl(a: Integer, bits: Size): Integer = make(a.negative, magShl(a.limbs, bits))

/** `a` / 2^`bits`, rounded down (towards minus infinity, as `>>` on a negative Long). */
fn shr(a: Integer, bits: Size): Integer do
    val limbs = bits / 32
    ret if a.negative do integer(-1) else zero if limbs >= a.limbs.len
    val kept = shiftedRight(a.limbs[limbs..<a.limbs.len], (bits % 32) as Int)
    val down = make(a.negative, kept)
    ret down if not a.negative
    // A negative number rounds down: one lower when a bit that was shifted out was set.
    var lost = false
    for i in 0..<limbs do
        lost = lost or a.limbs[i] != 0
    val rest = bits % 32
    if rest > 0 do
        lost = lost or a.limbs[limbs] & ((1 as UInt << (rest as Int)) - 1) != 0
    ret if lost do sub(down, 1) else down

/** How many bits the magnitude of `a` takes: 0 for zero, 1 for 1 and -1, 8 for 255. */
fn bitLength(a: Integer): Size do
    ret 0 if a.limbs.len == 0
    ret a.limbs.len * 32 - (leadingZeros(a.limbs[a.limbs.len - 1]) as Size)

/** Whether `a` fits in a Long. */
fn fitsLong(a: Integer): Bool do
    ret true if a.limbs.len < 2
    ret false if a.limbs.len > 2
    val top = a.limbs[1]
    ret top < 0x80000000 or (a.negative and top == 0x80000000 and a.limbs[0] == 0)

/**
 * `a` as a Long.
 * @throws when it doesn't fit
 */
fn toLong(a: Integer): Long throws RangeError do
    throw rangeError("${a} doesn't fit in a Long") if not fitsLong(a)
    var m: ULong = 0
    for i in 0..<a.limbs.len do
        m = m | ((a.limbs[i] as ULong) << (i * 32))
    ret if a.negative do (~m + 1) as Long else m as Long

/** `a` as the nearest Double; infinity when it is too large for one. */
fn toDouble(a: Integer): Double do
    var x = 0.0
    var i = a.limbs.len
    while i > 0 do
        i -= 1
        x = x * 4294967296.0 + (a.limbs[i] as Double)
    ret if a.negative do -x else x

/**
 * `a` in the base `base` (2 to 36), with lower-case letters for the digits above 9 and a "-" before a negative
 * number: `integer(255).text(16)` is "ff".
 */
fn text(a: Integer, base: Int = 10): String requires base >= 2 and base <= 36, "a base is from 2 to 36" do
    ret "0" if a.limbs.len == 0
    // Take several digits at once: the largest power of the base that fits in a limb.
    var chunk: UInt = base as UInt
    var perChunk = 1
    while (chunk as ULong) * (base as ULong) < BASE do
        chunk = chunk * (base as UInt)
        perChunk += 1
    val parts: List<UInt> = list()
    var m = a.limbs
    while m.len > 0 do
        val (q, r) = magDivSmall(m, chunk)
        parts.push(r)
        m = trimmed(q)
    val b = stringBuilder(parts.len * (perChunk as Size) + 1)
    b.append('-') if a.negative
    var i = parts.len
    var first = true
    val digits = new Char[perChunk as Size]
    while i > 0 do
        i -= 1
        var v = parts.buffer[i]
        var d = perChunk as Size
        while d > 0 do
            d -= 1
            digits[d] = DIGITS[(v % (base as UInt)) as Size]
            v = v / (base as UInt)
        var from: Size = 0
        if first do
            while from + 1 < digits.len and digits[from] == '0' do
                from += 1
            first = false
        for k in from..<digits.len do
            b.append(digits[k])
    ret b.build()

/** `${a}` writes {@link text} in base 10. */
fn str(a: Integer): String = text(a)

/** The value of the digit `c` in a base up to 36, or 99. */
priv fn digitValue36(c: Char): UInt do
    ret (c - '0') as UInt if c >= '0' and c <= '9'
    ret (c - 'a' + 10) as UInt if c >= 'a' and c <= 'z'
    ret (c - 'A' + 10) as UInt if c >= 'A' and c <= 'Z'
    ret 99

/**
 * The Integer that `s` writes in the base `base` (2 to 36): digits, with a "-" or "+" before them and single "_"s
 * between them allowed. Letters can be in either case.
 * @throws when `s` has no digits, or something that isn't a digit of the base; the error says where
 */
fn parse(s: String, base: Int = 10): Integer throws ParseError requires base >= 2 and base <= 36, "a base is from 2 to 36" do
    var i: Size = 0
    val negative = s.len > 0 and s[0] == '-'
    if s.len > 0 and (s[0] == '-' or s[0] == '+') do
        i = 1
    throw parseError("expected digits", s, i) if i >= s.len
    var m = new UInt[0]
    var digits = 0
    while i < s.len do
        val c = s[i]
        if c == '_' and digits > 0 and i + 1 < s.len and s[i - 1] != '_' and s[i + 1] != '_' do
            i += 1
        else do
            val d = digitValue36(c)
            throw parseError("'${c}' is not a digit in base ${base}", s, i) if d >= (base as UInt)
            m = trimmed(magMulAdd(m, base as UInt, d))
            digits += 1
            i += 1
    ret make(negative, m)

// ---------------------------------------------------------------------------------------------------
// Decimals

/** An exact decimal number: `unscaled` / 10^`scale`. 1.50 is 150 with the scale 2. */
rec Decimal as
    /** The digits, as a whole number. */
    unscaled: Integer
    /** How many of them are after the decimal point; never negative. */
    scale: Int

/**
 * How {@link div} and {@link round} drop digits. `:halfEven` (the default, "banker's rounding") takes a tie to the
 * even neighbour, so that rounding many values adds no bias; `:halfUp` takes it away from zero, as taught in school;
 * `:halfDown` towards zero; `:down` drops the digits (towards zero), `:up` goes away from zero, `:floor` down and
 * `:ceiling` up.
 */
declare type Rounding as :halfEven | :halfUp | :halfDown | :down | :up | :floor | :ceiling

/** The Decimal `n`, with no digits after the point. */
fn decimal(n: Long): Decimal = Decimal { integer(n), 0 }

/** The Decimal `units` / 10^`scale`: `decimal(150, 2)` is 1.50, as cents give dollars. */
fn decimal(units: Long, scale: Int): Decimal requires scale >= 0, "a scale can't be negative" = Decimal { integer(units), scale }

/** The Decimal of a whole number. */
fn decimal(n: Integer): Decimal = Decimal { n, 0 }

/** 10^`n`. */
priv fn tenTo(n: Int): Integer = pow(integer(10), n as Long)

/** `d` with the scale `scale`, which is at least its own: the same number with more zeros after the point. */
priv fn widened(d: Decimal, scale: Int): Decimal do
    ret d if scale == d.scale
    ret Decimal { mul(d.unscaled, tenTo(scale - d.scale)), scale }

/** `n` / `d` rounded to a whole number as `mode` says. */
priv fn roundedDiv(n: Integer, d: Integer, mode: Rounding, at: Location): Integer do
    val (q, r) = divRem(n, d, at)
    ret q if r.limbs.len == 0
    // The exact quotient is q + r/d, a part on the side of q's sign.
    val positive = n.negative == d.negative
    val half = magCompare(trimmed(magShl(r.limbs, 1)), d.limbs)
    val away = when mode do
        is :down do false
        is :up do true
        is :floor do not positive
        is :ceiling do positive
        is :halfUp do half >= 0
        is :halfDown do half > 0
        else do half > 0 or (half == 0 and not isEven(q))
    ret q if not away
    ret if positive do add(q, 1) else sub(q, 1)

/** `a` + `b`, with the larger of their scales. */
fn add(a: Decimal, b: Decimal): Decimal do
    val scale = math.max(a.scale, b.scale)
    ret Decimal { add(widened(a, scale).unscaled, widened(b, scale).unscaled), scale }

/** `a` - `b`, with the larger of their scales. */
fn sub(a: Decimal, b: Decimal): Decimal = add(a, neg(b))

/** `a` × `b`, with the sum of their scales: 1.5 × 1.25 is 1.875. Use {@link round} for fewer digits. */
fn mul(a: Decimal, b: Decimal): Decimal = Decimal { mul(a.unscaled, b.unscaled), a.scale + b.scale }

/**
 * `a` / `b` with `scale` digits after the point, the last rounded as `mode` says: `div(decimal(1), decimal(3), 4)`
 * is 0.3333. Stops the program when `b` is zero.
 */
fn div(a: Decimal, b: Decimal, scale: Int, mode: Rounding = :halfEven, at: Location = caller): Decimal requires scale >= 0, "a scale can't be negative" do
    // a/b × 10^scale = a.unscaled × 10^(scale + b.scale - a.scale) / b.unscaled
    val e = scale + b.scale - a.scale
    val n = if e >= 0 do mul(a.unscaled, tenTo(e)) else a.unscaled
    val d = if e >= 0 do b.unscaled else mul(b.unscaled, tenTo(-e))
    ret Decimal { roundedDiv(n, d, mode, at), scale }

/** `d` with `scale` digits after the point: more zeros, or its last digits dropped and rounded as `mode` says. */
fn round(d: Decimal, scale: Int, mode: Rounding = :halfEven): Decimal requires scale >= 0, "a scale can't be negative" do
    ret widened(d, scale) if scale >= d.scale
    ret Decimal { roundedDiv(d.unscaled, tenTo(d.scale - scale), mode, here), scale }

/** `d` without the zeros at the end of its digits after the point: 1.500 becomes 1.5, and 2.00 becomes 2. */
fn stripped(d: Decimal): Decimal do
    var u = d.unscaled
    var scale = d.scale
    val ten = integer(10)
    while scale > 0 and not isZero(u) do
        val (q, r) = divRem(u, ten)
        break if not isZero(r)
        u = q
        scale -= 1
    ret Decimal { u, if isZero(u) do 0 else scale }

/** `-d`. */
fn neg(d: Decimal): Decimal = Decimal { neg(d.unscaled), d.scale }

/** `d` without its sign. */
fn abs(d: Decimal): Decimal = Decimal { abs(d.unscaled), d.scale }

/** -1, 0 or 1, as `d` is below, at or above zero. */
fn sign(d: Decimal): Int = sign(d.unscaled)

/** Whether `d` is zero. */
fn isZero(d: Decimal): Bool = isZero(d.unscaled)

/** -1, 0 or 1 as `a` is less than, equal to or greater than `b`, whatever their scales: 1.5 and 1.50 are equal. */
fn compare(a: Decimal, b: Decimal): Int do
    val scale = math.max(a.scale, b.scale)
    ret compare(widened(a, scale).unscaled, widened(b, scale).unscaled)

/** Whether `a` and `b` are the same number, whatever their scales: 1.5 and 1.50 are. */
fn equals(a: Decimal, b: Decimal): Bool = compare(a, b) == 0

/** The whole part of `d`, its digits after the point dropped (towards zero). */
fn toInteger(d: Decimal): Integer = round(d, 0, :down).unscaled

/** `d` as the nearest Double, for showing or for maths that needn't be exact. */
fn toDouble(d: Decimal): Double = parseDouble(text(d)) ?? toDouble(d.unscaled) / math.pow(10.0, d.scale as Double)

/** `d` as text, with all its digits after the point: "1.50", "-0.05", "12". */
fn text(d: Decimal): String do
    val digits = text(abs(d.unscaled))
    val sign = if d.unscaled.negative do "-" else ""
    ret concat(sign, digits) if d.scale == 0
    val scale = d.scale as Size
    val padded = if digits.len <= scale do concat(repeat("0", scale + 1 - digits.len), digits) else digits
    val point = padded.len - scale
    ret concat(sign, padded[0..<point], ".", padded[point..<padded.len])

/** `${d}` writes {@link text}. */
fn str(d: Decimal): String = text(d)

/**
 * The Decimal that `s` writes: digits with a "." between the whole part and the fraction (either may be left out,
 * not both), with a "-" or "+" before them, single "_"s between digits and an exponent after them allowed: "19.99",
 * "-.5", "1_000.25", "1.5e3", "25e-2".
 * The scale is the digits after the point, less the exponent ("1.50" has the scale 2, "1.5e3" is 1500).
 * @throws when `s` isn't a decimal number; the error says where
 */
fn parseDecimal(s: String): Decimal throws ParseError do
    var i: Size = 0
    val negative = s.len > 0 and s[0] == '-'
    if s.len > 0 and (s[0] == '-' or s[0] == '+') do
        i = 1
    var m = new UInt[0]
    var digits = 0
    var scale = 0
    var seenPoint = false
    while i < s.len and (isDigit(s[i]) or (s[i] == '.' and not seenPoint) or (s[i] == '_' and digits > 0 and i + 1 < s.len and isDigit(s[i + 1]) and isDigit(s[i - 1]))) do
        if s[i] == '.' do
            seenPoint = true
        else if s[i] != '_' do
            m = trimmed(magMulAdd(m, 10, (s[i] - '0') as UInt))
            digits += 1
            if seenPoint do
                scale += 1
        i += 1
    throw parseError("expected digits", s, i) if digits == 0
    var exponent: Long = 0
    if i < s.len and (s[i] == 'e' or s[i] == 'E') do
        val start = i + 1
        exponent = try parseLong(s[start..<s.len], 10) catch e do
            throw parseError("expected a whole number after '${s[i]}'", s, start)
        throw parseError("the exponent is too large", s, start) if exponent > 1000000 or exponent < -1000000
        i = s.len
    throw parseError("'${s[i]}' can't be in a decimal number", s, i) if i < s.len
    val d = Decimal { make(negative, m), scale }
    val shifted = (scale as Long) - exponent
    ret if shifted >= 0 do Decimal { d.unscaled, shifted as Int } else Decimal { mul(d.unscaled, tenTo(-shifted as Int)), 0 }
