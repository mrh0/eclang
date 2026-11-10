# Module `big`

Numbers of any size: whole numbers ([`Integer`](#big.Integer)) and exact decimals ([`Decimal`](#big.Decimal)), for money and anything
else that a Long would overflow or a Double would round.

    use "core:big.ec"
    var n = big.integer(1)
    for i in 1..=30 do
        n = n.mul(big.integer(i))
    io.log("${n}")                                       // 265252859812191058636308480000000
    val x = try big.parse("123456789012345678901234567890")
    io.log("${x.div(n)} ${x.rem(n)}")
    io.log(x.text(16))                                   // 18ee90ff6c373e0ee4e3f0ad2

    val price = try big.parseDecimal("19.99")
    val total = price.mul(big.decimal(3)).add(try big.parseDecimal("0.05"))
    io.log("${total}")                                   // 60.02
    val each = total.div(big.decimal(7), 2)              // 8.57, to 2 places, half to even
    io.log("${each.round(1, :halfUp)}")                  // 8.6

Both are values: every operation makes a new one, in the current pool, and never changes its arguments. Call them
as functions or as methods (`big.add(a, b)` or `a.add(b)`); eclang has no operators for records. `==` doesn't
compare them (they hold arrays): use [`equals`](#big.equals) or [`compare`](#big.compare). Dividing by zero stops the program, as `/`
does.

An Integer is a sign and a magnitude in 32-bit limbs; multiplication is the schoolbook method and division Knuth's
algorithm D, so numbers of a few thousand digits are quick, but this is not a library for numbers of millions.
A Decimal is an Integer scaled by a power of ten: `unscaled` / 10^`scale`. It keeps its scale, so "1.50" stays
"1.50"; [`add`](#big.add) and [`sub`](#big.sub) take the larger scale, [`mul`](#big.mul) the sum of both, and [`div`](#big.div) the one it
is given, rounded as it says ([`Rounding`](#big.Rounding)).

## Types

### <a id="big.Integer"></a>`Integer`

```ec
rec Integer
```

A whole number of any size.

**Fields**

- <a id="big.Integer.negative"></a>`negative: Bool` — Whether it is below zero (never true of zero).
- <a id="big.Integer.limbs"></a>`limbs: UInt[]` — Its size, in 32-bit limbs, the lowest first, with no zero limbs at the top: zero has none.

### <a id="big.Decimal"></a>`Decimal`

```ec
rec Decimal
```

An exact decimal number: `unscaled` / 10^`scale`. 1.50 is 150 with the scale 2.

**Fields**

- <a id="big.Decimal.unscaled"></a>`unscaled: Integer` — The digits, as a whole number.
- <a id="big.Decimal.scale"></a>`scale: Int` — How many of them are after the decimal point; never negative.

### <a id="big.Rounding"></a>`Rounding`

```ec
declare type Rounding as :halfEven | :halfUp | :halfDown | :down | :up | :floor | :ceiling
```

How [`div`](#big.div) and [`round`](#big.round) drop digits. `:halfEven` (the default, "banker's rounding") takes a tie to the
even neighbour, so that rounding many values adds no bias; `:halfUp` takes it away from zero, as taught in school;
`:halfDown` towards zero; `:down` drops the digits (towards zero), `:up` goes away from zero, `:floor` down and
`:ceiling` up.

## Values

### <a id="big.zero"></a>`zero`

```ec
val zero = Integer { false, new UInt[0] }
```

Zero.

## Functions

### <a id="big.integer"></a>`integer`

```ec
fn integer(n: Long): Integer
```

The Integer `n`.

### `integer`

```ec
fn integer(n: ULong): Integer
```

The Integer `n`.

### `integer`

```ec
fn integer(n: Int): Integer
```

The Integer `n`.

### <a id="big.add"></a>`add`

```ec
fn add(a: Integer, b: Integer): Integer
```

`a` + `b`.

### `add`

```ec
fn add(a: Integer, n: Long): Integer
```

`a` + `n`.

### <a id="big.sub"></a>`sub`

```ec
fn sub(a: Integer, b: Integer): Integer
```

`a` - `b`.

### `sub`

```ec
fn sub(a: Integer, n: Long): Integer
```

`a` - `n`.

### <a id="big.mul"></a>`mul`

```ec
fn mul(a: Integer, b: Integer): Integer
```

`a` × `b`.

### `mul`

```ec
fn mul(a: Integer, n: Long): Integer
```

`a` × `n`.

### <a id="big.divRem"></a>`divRem`

```ec
fn divRem(a: Integer, b: Integer, at: Location = caller): (Integer, Integer)
```

`a` / `b` and `a` % `b`, at once: the quotient rounded towards zero, and a remainder with the sign of `a`, as `/`
and `%` work on Longs. Stops the program when `b` is zero.

### <a id="big.div"></a>`div`

```ec
fn div(a: Integer, b: Integer, at: Location = caller): Integer
```

`a` / `b`, rounded towards zero, as `/` on Longs. Stops the program when `b` is zero.

### <a id="big.rem"></a>`rem`

```ec
fn rem(a: Integer, b: Integer, at: Location = caller): Integer
```

The remainder of `a` / `b`, with the sign of `a`, as `%` on Longs. Stops the program when `b` is zero.

### <a id="big.mod"></a>`mod`

```ec
fn mod(a: Integer, b: Integer, at: Location = caller): Integer
```

`a` modulo `b`: the remainder of `a` / `b` with the sign of `b`, as `math.floorMod`.

### <a id="big.neg"></a>`neg`

```ec
fn neg(a: Integer): Integer
```

`-a`.

### <a id="big.abs"></a>`abs`

```ec
fn abs(a: Integer): Integer
```

`a` without its sign.

### <a id="big.sign"></a>`sign`

```ec
fn sign(a: Integer): Int
```

-1, 0 or 1, as `a` is below, at or above zero.

### <a id="big.isZero"></a>`isZero`

```ec
fn isZero(a: Integer): Bool
```

Whether `a` is zero.

### <a id="big.isEven"></a>`isEven`

```ec
fn isEven(a: Integer): Bool
```

Whether `a` is even.

### <a id="big.compare"></a>`compare`

```ec
fn compare(a: Integer, b: Integer): Int
```

-1, 0 or 1 as `a` is less than, equal to or greater than `b`.

### `compare`

```ec
fn compare(a: Integer, n: Long): Int
```

-1, 0 or 1 as `a` is less than, equal to or greater than `n`.

### <a id="big.equals"></a>`equals`

```ec
fn equals(a: Integer, b: Integer): Bool
```

Whether `a` and `b` are the same number.

### `equals`

```ec
fn equals(a: Integer, n: Long): Bool
```

Whether `a` is the number `n`.

### <a id="big.pow"></a>`pow`

```ec
fn pow(a: Integer, e: Long): Integer
```

`a` to the power `e`, which must not be negative.

### <a id="big.powMod"></a>`powMod`

```ec
fn powMod(a: Integer, e: Integer, m: Integer, at: Location = caller): Integer
```

`a` to the power `e`, modulo `m`, without the whole power: for modular arithmetic as in RSA.

**Returns** a number from 0 up to `m`

### <a id="big.gcd"></a>`gcd`

```ec
fn gcd(a: Integer, b: Integer): Integer
```

The greatest common divisor of `a` and `b`, never negative; 0 when both are 0.

### <a id="big.shl"></a>`shl`

```ec
fn shl(a: Integer, bits: Size): Integer
```

`a` × 2^`bits`.

### <a id="big.shr"></a>`shr`

```ec
fn shr(a: Integer, bits: Size): Integer
```

`a` / 2^`bits`, rounded down (towards minus infinity, as `>>` on a negative Long).

### <a id="big.bitLength"></a>`bitLength`

```ec
fn bitLength(a: Integer): Size
```

How many bits the magnitude of `a` takes: 0 for zero, 1 for 1 and -1, 8 for 255.

### <a id="big.fitsLong"></a>`fitsLong`

```ec
fn fitsLong(a: Integer): Bool
```

Whether `a` fits in a Long.

### <a id="big.toLong"></a>`toLong`

```ec
fn toLong(a: Integer): Long throws RangeError
```

`a` as a Long.

**Throws**

- `RangeError` — when it doesn't fit

### <a id="big.toDouble"></a>`toDouble`

```ec
fn toDouble(a: Integer): Double
```

`a` as the nearest Double; infinity when it is too large for one.

### <a id="big.text"></a>`text`

```ec
fn text(a: Integer, base: Int = 10): String
```

`a` in the base `base` (2 to 36), with lower-case letters for the digits above 9 and a "-" before a negative
number: `integer(255).text(16)` is "ff".

### <a id="big.str"></a>`str`

```ec
fn str(a: Integer): String
```

`${a}` writes [`text`](#big.text) in base 10.

### <a id="big.parse"></a>`parse`

```ec
fn parse(s: String, base: Int = 10): Integer throws ParseError
```

The Integer that `s` writes in the base `base` (2 to 36): digits, with a "-" or "+" before them and single "_"s
between them allowed. Letters can be in either case.

**Throws**

- `ParseError` — when `s` has no digits, or something that isn't a digit of the base; the error says where

### <a id="big.decimal"></a>`decimal`

```ec
fn decimal(n: Long): Decimal
```

The Decimal `n`, with no digits after the point.

### `decimal`

```ec
fn decimal(units: Long, scale: Int): Decimal
```

The Decimal `units` / 10^`scale`: `decimal(150, 2)` is 1.50, as cents give dollars.

### `decimal`

```ec
fn decimal(n: Integer): Decimal
```

The Decimal of a whole number.

### `add`

```ec
fn add(a: Decimal, b: Decimal): Decimal
```

`a` + `b`, with the larger of their scales.

### `sub`

```ec
fn sub(a: Decimal, b: Decimal): Decimal
```

`a` - `b`, with the larger of their scales.

### `mul`

```ec
fn mul(a: Decimal, b: Decimal): Decimal
```

`a` × `b`, with the sum of their scales: 1.5 × 1.25 is 1.875. Use [`round`](#big.round) for fewer digits.

### `div`

```ec
fn div(a: Decimal, b: Decimal, scale: Int, mode: Rounding = :halfEven, at: Location = caller): Decimal
```

`a` / `b` with `scale` digits after the point, the last rounded as `mode` says: `div(decimal(1), decimal(3), 4)`
is 0.3333. Stops the program when `b` is zero.

### <a id="big.round"></a>`round`

```ec
fn round(d: Decimal, scale: Int, mode: Rounding = :halfEven): Decimal
```

`d` with `scale` digits after the point: more zeros, or its last digits dropped and rounded as `mode` says.

### <a id="big.stripped"></a>`stripped`

```ec
fn stripped(d: Decimal): Decimal
```

`d` without the zeros at the end of its digits after the point: 1.500 becomes 1.5, and 2.00 becomes 2.

### `neg`

```ec
fn neg(d: Decimal): Decimal
```

`-d`.

### `abs`

```ec
fn abs(d: Decimal): Decimal
```

`d` without its sign.

### `sign`

```ec
fn sign(d: Decimal): Int
```

-1, 0 or 1, as `d` is below, at or above zero.

### `isZero`

```ec
fn isZero(d: Decimal): Bool
```

Whether `d` is zero.

### `compare`

```ec
fn compare(a: Decimal, b: Decimal): Int
```

-1, 0 or 1 as `a` is less than, equal to or greater than `b`, whatever their scales: 1.5 and 1.50 are equal.

### `equals`

```ec
fn equals(a: Decimal, b: Decimal): Bool
```

Whether `a` and `b` are the same number, whatever their scales: 1.5 and 1.50 are.

### <a id="big.toInteger"></a>`toInteger`

```ec
fn toInteger(d: Decimal): Integer
```

The whole part of `d`, its digits after the point dropped (towards zero).

### `toDouble`

```ec
fn toDouble(d: Decimal): Double
```

`d` as the nearest Double, for showing or for maths that needn't be exact.

### `text`

```ec
fn text(d: Decimal): String
```

`d` as text, with all its digits after the point: "1.50", "-0.05", "12".

### `str`

```ec
fn str(d: Decimal): String
```

`${d}` writes [`text`](#big.text).

### <a id="big.parseDecimal"></a>`parseDecimal`

```ec
fn parseDecimal(s: String): Decimal throws ParseError
```

The Decimal that `s` writes: digits with a "." between the whole part and the fraction (either may be left out,
not both), with a "-" or "+" before them, single "_"s between digits and an exponent after them allowed: "19.99",
"-.5", "1_000.25", "1.5e3", "25e-2".
The scale is the digits after the point, less the exponent ("1.50" has the scale 2, "1.5e3" is 1500).

**Throws**

- `ParseError` — when `s` isn't a decimal number; the error says where
