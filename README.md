# ec-lang

"Easy C Lang"

eclang is a systems language whose compiler generates human-readable C source, with the goal of full interoperability with C.
The compiler is written in Kotlin and uses an ANTLR 4 grammar ([Eclang.g4](src/main/antlr4/Eclang.g4)) with Python-style significant indentation.

> **Status:** early and experimental. The language and compiler change often; features marked *Not Yet Implemented* are parsed by the grammar, and the compiler reports them as not supported yet.

## Table of Contents

0. [Goals](#goals)
1. [Setup](#setup)
2. [Modules and Imports](#modules-and-imports)
3. [Variables](#variables)
4. [Data Types](#data-types)
5. [Operators](#operators)
6. [Functions](#functions)
7. [External Declarations](#external-declarations)
8. [Records](#records)
9. [Control Flow](#control-flow)
10. [Error Handling](#error-handling)
11. [Type System](#type-system)
12. [Memory Management](#memory-management)
13. [Ecdocs](#ecdocs)
14. [Standard Library](#standard-library)

## Goals

- Full interoperability with C.
- Union types, duck-typing, interfaces.
- Generating human-readable C source.
- Function overloads, functional paradigms.
- Null safety. Lifetimes. Catch most common memory safety issues.
- High preformance, but generally prefer safe code over performance.
- Pool memory management, defer, deep-clone, APR (Apache Portable Runtime).
- Modernized syntax (inspired by languages such as Python, Kotlin, Go, Elixir).
- Error handling by value.

## Setup

### Requirements

- JDK 17+
- Maven
- gcc
- APR (`libapr1-dev`)

```
sudo apt update
sudo apt install gcc build-essential maven
sudo apt install libapr1 libapr1-dev
```

or `make install`, which installs gcc, build-essential and APR (Maven is not included).

### Build eclang.jar
```
mvn clean package
```
or `make build`.

### Compile .ec source with eclang.jar
```
java -jar ./target/eclang-0.1.0.jar -c ./examples/fib.ec -o out.c
```

If `-o` is omitted, the output is written to `./out.c`.

| Option | Description |
| --- | --- |
| `-c, --compile <file>` | File to compile, must have a `main` function |
| `-o, --output <path>` | Output `.c` source file path (default `./out.c`) |
| `-d, --debug` | Print tokens, intermediate representation and output |
| `-t, --time` | Print compile time |
| `-nb, --no-bounds-check` | Don't check array and String indices at runtime (see [Arrays](#arrays-and-c-arrays)) |
| `-na, --no-arithmetic-check` | Don't check division by zero and shift counts at runtime (see [Arithmetic Operators](#arithmetic-operators)) |
| `-oc, --overflow-check` | Check at runtime that whole-number `+`, `-` and `*` fit their type |
| `-v, --version` | Print version |
| `-h, --help` | Print help |

Compile errors are reported as `file:line:column: error: ...`, with a hint on how to fix them:
```
examples/demo.ec:3:5: error: Cannot use Long as Int (variable 'i'): Long → Int may lose data (Int is smaller); convert it explicitly with `as Int`
```
One run reports the errors of every function (up to 30). Inside a function, checking continues after a failed call, assignment or `ret`; after a failed declaration or `if`, the rest of that block is skipped, because it would mostly report follow-up errors. Syntax errors stop at the first one, with a message for common mistakes (a missing `do`, tab indentation, `return` for `ret`, `;`, an unclosed bracket, ...). Warnings (`file:line:column: warning: ...`) don't stop the compilation.

### Compile and run in one step
```
java -jar target/eclang-0.1.0.jar run examples/fizzbuzz.ec
```
`run` compiles the program, builds it with `gcc` (or `$CC`) using the APR flags from `apr-1-config`, `-lm`, `-fwrapv` and `-fno-strict-aliasing`, runs it, and exits with its exit code. It takes `-nb`, `-na`, `-oc`, `-d`, and `-k <dir>` to keep the generated `.c` file and the executable. What follows `--` goes to the program, which reads it with `io.args()`: `eclang run app.ec -- -v in.txt`.

### Generate documentation
```
java -jar target/eclang-0.1.0.jar doc examples/fib.ec [more.ec ...] [-o docs.md] [-p]
```
`doc` writes Markdown documentation of the files' declarations from their [ecdocs](#ecdocs) (to stdout without `-o`; `-p` includes `priv` declarations). It only parses the files, so a library without `main` works, and so does a bundled file (`core:string.ec`).

### Compile generated .c source with gcc
```
gcc -fwrapv -fno-strict-aliasing out.c -o out -I/usr/include/apr-1.0 -L/usr/lib -lapr-1 -lm
```

The generated C needs both flags, and `run` and `lib` pass them: `-fwrapv` because `+` on an Int wraps, and `-fno-strict-aliasing` because a pointer to a child record is used as a pointer to its parent.

The Makefile wraps both steps:
```
make compile FILE=./examples/fizzbuzz.ec
make compile-run FILE=./examples/fizzbuzz.ec
```

### Run the tests
```
tests/run.sh
```
Programs in [tests/pass](tests/pass) must compile, build and print their `.out` file; programs in [tests/fail](tests/fail) must be rejected with the error on their first line; programs in [tests/panic](tests/panic) must compile, then stop at runtime with the message on their first line. Files in [tests/doc](tests/doc) must give their `.md` file from `eclang doc`, with the warnings in their `.err` file. Libraries in [tests/lib](tests/lib) must build with `eclang lib`, and the C program next to them must link to them and print their `.out` file.

## Modules and Imports

A file may put its definitions in a module with `module "name"` (one per file, for the whole file). Several files can share a module. Files without `module` are at the top level.
```plaintext
// geo.ec
module "geo"

rec Point as
    x: Int
    y: Int

val origin = Point { x = 0, y = 0 }

fn make(x: Int, y: Int): Point = Point { x = x, y = y }
```
Code sees the names of its own module and of the top level (such as `describe` from `core:errors.ec`). Names from other modules are written with the module name in front: functions, values, types, record literals and function references.
```plaintext
use "geo.ec"

fn main(): Int do
    val a: geo.Point = geo.make(3, 4)
    val b = geo.Point { x = 1, y = 2 }
    val f = fn geo.make
    io.log(geo.origin.x)
    ret 0
```
A declaration marked `priv` (`priv fn`, `priv val`, `priv var`, `priv const`, `priv rec`, `priv inter`) can only be used by code of its own module: helpers stay out of the module's public names.
```plaintext
// geo.ec
module "geo"
priv fn square(v: Int): Int = v * v
fn distance2(a: Point, b: Point): Int = square(a.x - b.x) + square(a.y - b.y)
```
`geo.square(2)` in another module is an error.

Two modules can define the same name without clashing. A module may add overloads to a top-level function (`fn describe(p: Point)` in geo); inside the module both are visible, and on a tie its own function wins. In C, a module's definitions are prefixed with its name: `geo__make_0`, `geo__Point`.

Include a C header or source file with the `use` keyword. It is emitted as an `#include`.
```plaintext
use "stdio.h"
```

Import another `.ec` file. A plain path is looked up next to the file that has the `use`, then in the working directory. The `core:` prefix loads files bundled with the compiler (from [src/main/resources](src/main/resources)). Each file is loaded once, however many files use it.
```plaintext
use "core:io.ec"
use "core:apr/pools.ec"
use "geo.ec"
```

Bundled libraries:
- The standard library (see [Standard Library](#standard-library)): `core:string.ec`, `core:errors.ec` (`Error` and the error types that extend it), `core:array.ec`, `core:structures.ec`, and the modules `core:io.ec` (`io.log`, ...), `core:math.ec`, `core:time.ec`, `core:thread.ec`, `core:os.ec`, `core:term.ec`, `core:encoding.ec`, `core:net.ec`, `core:fs.ec`, `core:json.ec`, `core:hash.ec` and `core:args.ec`.
- [c/](src/main/resources/c): declarations for C's standard headers, one file per header (`core:c/stdio.ec`, `core:c/stdlib.ec`, `core:c/string.ec`, `core:c/ctype.ec`, `core:c/time.ec`, `core:c/unistd.ec`, `core:c/termios.ec`, `core:c/ioctl.ec`, ...).
- [apr/](src/main/resources/apr): declarations for the Apache Portable Runtime, one file per APR header (`core:apr/pools.ec`, `core:apr/strings.ec`, `core:apr/tables.ec`, `core:apr/hash.ec`, ...).

## Variables

```plaintext
val a = 10          // immutable
var b: Int = 20     // mutable
const c = 30        // compile-time constant (see Constants)
vol d: Int = 0      // volatile

b = a + b           // assignment
b += 5              // also -= *= /= %= &= |= ^= <<= >>=
var e: Int          // starts out empty: 0
```

Assignment works on anything that names a place: a variable, a field (`p.x = 1`, `line.a.x = 1`), an element (`a[i] = 1`) or what a pointer points to (`@p = 1`). A compound assignment such as `x += v` needs a `v` that fits `x`'s type (`x += 1.5` on an Int is an error).

A record held in a `val` (or a parameter) is a fixed value, so its fields can't change either: `val p = Point { x = 1, y = 2 }` followed by `p.x = 3` is an error. Declare it with `var`, or change it through a pointer (`p: @Point` in `fn move(p: @Point) do p.x += 1`); a pointer in a `val` still lets you change what it points to. An array's elements live behind a pointer too, so `a[0] = 1` works on a `val` array.

`var x: T` without a value starts out empty: `0`, `false`, an empty String or array, `null` for a nullable type, and a record with every field empty, which suits C out-parameters (`var info: SomeCStruct` then `fill(addrof(info))`). Types that have no empty value (pointers, CStrings, atoms, unions, and records holding them) need a value or a `?`.

A name can only be defined once in a function: a `val` inside an `if` can't reuse the name of an outer variable (no shadowing), so a name means the same thing everywhere in a function. Choose a different name.

`val`, `var`, `vol` and `const` can also be used at the top level to define globals. A global whose value isn't a C constant (`val start = now()`) is set when the program starts, before `main` runs, in the order the globals are written.

### Constants

`const` defines a value that is known at compile time: a literal, another constant, or arithmetic on them. The compiler computes it, so constants can build on each other and be used in other globals' initial values.

```plaintext
const LIMIT = 10
const TWICE = LIMIT * 2          // 20, computed by the compiler
const BIG: Long = 5000000000
const GREETING = "hello"c

val c: Char = LIMIT + 55         // an untyped number constant fits any type that holds it
```

A constant without a type behaves like the literal it stands for (see [Numbers](#numbers)). A value only known when the program runs, such as a function's result, needs `val`. A top-level constant becomes a C `#define` (`#define LIMIT (10)`); a local one is replaced by its value.

## Data Types

### Numbers

A number literal has no fixed type until it is used: it fits any number type that can hold its value exactly, so no suffix is needed. On its own, a whole number is an `Int` (a `Long` if it is too big for an `Int`) and a decimal number is a `Double`. Arithmetic on literals is done by the compiler, so the result can still fit a small type.

```plaintext
val c: Char = 65          // fits a Char
val big = 5000000000      // too big for an Int: Long
val ratio = 2.5           // Double
val f: Float = 2.5        // fits a Float
val folded: Char = 60 + 6 // computed at compile time: 66
val bad: Char = 300       // error: 300 does not fit in Char (-128 to 127)
```

A hexadecimal or binary literal above the largest Long (`0xFFFFFFFFFFFFFFFF`) is a ULong.

A literal can also be given a specific type with a suffix:

| Type | C type | Suffix |
| --- | --- | --- |
| `Char` | `char` | `b`, `i8` |
| `UChar` | `unsigned char` | `ub`, `u8` |
| `Short` | `short` | `s`, `i16` |
| `UShort` | `unsigned short` | `us`, `u16` |
| `Int` | `int` | `i`, `i32` |
| `UInt` | `unsigned int` | `u`, `ui`, `u32` |
| `Long` | `long` | `l`, `i64` |
| `ULong` | `unsigned long` | `ul`, `u64` |
| `Float` | `float` | `f`, `f32` |
| `Double` | `double` | `d`, `f64` |
| `Size` | `size_t` | `z` |

```plaintext
val tchar: Char = 10b
val tshort: Short = 10s
val tint: Int = 10i
val tlong: Long = 10l
val tuint: UInt = 10ui
val tulong: ULong = 10ul
val f: Float = 6f

val big = 1_000_000
val hex = 0xFF
val bin = 0b1010
```

The suffix is written right after the number: `5 b` (with a space) is an error, not the Char 5.

Numbers only convert automatically when no information can be lost: to a wider type of the same kind (`Short` → `Int` → `Long`), from unsigned to a wider signed type (`UInt` → `Long`), from a whole number to a float that holds it exactly (`Int` → `Double`), and `Float` → `Double`. Anything else needs `as` (see [Type Casting](#type-casting)). Mixing types in an operator follows the same rule: `Int + Long` is a `Long`, while `Int + UInt` is an error, because neither fits the other.

### Characters and Booleans

```plaintext
val char1: Char = 'a'
val char2: Char = '\n'
val flag: Bool = true
```

### Strings and C-Strings

A plain string literal is a `String`. Suffix it with `c` for a C string (`CString`, `char*`).

```plaintext
val string1 = "Hello World"
val string2: String = "Hello World"

val cstring1 = "Hello World"c
val cstring2: CString = "Hello World"c
```

Strings can contain the escapes `\n` `\r` `\t` `\0` `\\` `\"` `\'` `\$` `\a` `\b` `\f` `\v`, `\xHH` (one byte in hex) and `\uHHHH` or `\u{H...}` (a Unicode character, stored as UTF-8). A `String`'s `len` counts bytes of UTF-8: `"a\tb".len` is 3 and `"hé".len` is 3.

`s[i]` is the byte at `i` (a `Char`), and `s[a..<b]` (or `s[a..=b]`, including `b`) is the part from `a` to `b`. Either end can be left out: `s[1..]` goes to the end, and `s[..<3]` starts at the beginning. A slice shares the String's memory, so it costs nothing. Both check their bounds (see [Arrays](#arrays-and-c-arrays)). A String's bytes can't be changed in place.

`cstr(s)` (or `s.cstr()`) is a copy of a String that ends with a 0 byte, as a `CString` for C functions; `cstr(p, s)` copies it into pool p. A String's own bytes need not end with a 0 (a slice doesn't), so pass `cstr(s)` (or `%.*s` with `s.len as Int, s.data`) to C.

[core:string.ec](src/main/resources/string.ec) has the String functions: `startsWith(s, prefix)`, `indexOf(s, part)` (a `Size?`), `trim(s)` (a slice), `split(s, ',')` (a `String[]` of slices, in the current pool), `replace`, `join`, `toUpper`, `parseInt(s)` (a `Long throws Error`) and more; see [Standard Library](#standard-library).

Building a new String needs memory, which comes from the current pool (see [Memory Pools](#memory-pools)). A String with `${...}` fills in values, and `concat` joins Strings:

```plaintext
val s = "${name} is ${age} years old"
val path = concat(dir, "/", file)
val kept = format(p, "${name}")         // in pool p instead; also concat(p, a, b)
```

After the value, `:` gives a format, and `=` also prints the expression itself, as in Python:

```plaintext
"${price:.2f}"      // 3.14         two decimals
"${count:>8}"       //       42     at least 8 wide, aligned right (`<` aligns left)
"${count:08}"       // 00000042     padded with zeros
"${count:x}"        // 2a           also `X`, `o`, `d`; `+` shows the sign of a positive number
"${count=}"         // count=42     `${count=:08}` combines both
```

A format is `[<|>][+][0][width][.precision][type]`. The type is `d`, `x`, `X` or `o` for whole numbers, `f`, `e`, `E`, `g` or `G` for Float and Double, and `s` for text. Numbers are aligned right by default and text left; on a String or CString the precision is the most bytes to print. A format that doesn't fit the value's type is a compile error. It becomes a `printf` conversion, so a fill character and centering (`^`) aren't available, and an atom can't be padded.

`${...}` takes numbers (`%g` for Float and Double), Bools, Chars, atoms, Strings, CStrings, `T[N]` arrays, and records and tuples ([printed with their fields](#printing-and-hashing), or pointers to them); for anything else, put in a field or convert it first. The expression inside can't contain a string literal. Write `\$` for a plain dollar sign. `format` and `concat` are built in, but a function of your own with either name takes precedence. In C, `${...}` becomes a small generated function calling `snprintf` and allocating in the pool.

### Arrays and C-Arrays

```plaintext
val array1 = ["One", "Two", "Three"]
val array2: Int[] = [5, 3, 6, 0]
io.log(array1.len)

val carray1 = ["One", "Two", "Three"]c
val carray2: Int[]c = [5, 3, 6, 0]c
```

`a[i]` on an array (or String) checks that `i` is less than `a.len`; otherwise the program stops with `file:line: index 10 is out of bounds for length 3`. To skip the check where speed matters, index the raw data instead: `a.data[i]` (a C array, which has no length). The `--no-bounds-check` option turns all the checks off.

`a[from..<to]` and `a[from..=to]` slice an array, sharing its elements: changing an element of the slice changes the array. C arrays have no length, so they can't be sliced.

### Fixed-Size Arrays

`T[N]` holds N elements in the value itself, like an array inside a C struct. N is a number or a `const`, known when the program compiles. Assigning, passing or returning one copies its elements, so it behaves like a number, not like a `T[]`, which only points to its elements.

```plaintext
const SIZE = 3

fn doubled(a: Int[SIZE]): Int[SIZE] do
    var b = a                   // a copy
    for i in 0..<b.len do
        b[i] = b[i] * 2
    ret b

var empty: Int[4]               // all zero
val values: Int[3] = [1, 2, 3]  // the literal must have all 3 elements
```

`a[i]` checks i against N: when the program runs, or when it compiles if i is a literal. `==`, `hash`, `clone`, `for` and `${...}` work on the elements; a `Char[N]` prints as text, up to its first 0.

A `T[N]` turns into nothing else by itself, because what it turns into would point into it:

| You want | Write | You get |
| --- | --- | --- |
| an array | `a[..]`, `a[1..<3]` | a `T[]` sharing a's memory |
| a C pointer | `a.data` | a `T[]c`, or a `CString` for a `Char[N]` |
| the length | `a.len` | a Size, known at compile time |

These pointers follow the usual [lifetime](#lifetimes) rules: returning `a[..]` of a local `a` is an error. `T[N]` has a different meaning from `new T[n]`, which allocates a `T[]`. Note that in `x as Int[i]` the `[i]` belongs to the type; write `(x as Int)[i]` to index the result. `@Int[4]` is an array of four pointers, and `@(Int[4])` is a pointer to an array.

### Tuples

A tuple is a fixed group of values, of any types, written in parentheses. Its type is written the same way: `(Int, String)`. It is the simple way to return several values, without declaring a record. In C it is a small struct.

```plaintext
fn divmod(a: Int, b: Int): (Int, Int) = (a / b, a % b)

val t = (1, "one")
io.log(t.first)                 // 1
io.log(t.second)                // one

val (q, r) = divmod(17, 5)      // q is 3, r is 2
val (_, rest) = divmod(17, 5)   // `_` skips a part
var (x, (y, z)) = (1, (2, 3))   // Nested, and `var` to change them later
(x, y) = (y, x)                 // Assigns both at once: this swaps them
(p.x, a[0]) = (1, 2)            // Any place that can be assigned

val entries = [(1, "one"), (2, "two")]
for (key, name) in entries do
    io.log(name)
for i, (key, name) in entries do
    io.log(i)
```

- The parts are called `.first`, `.second`, `.third` and so on up to `.twentieth`. They can be assigned in a `var`: `t.first = 2`. They also work through a pointer to a tuple.
- A tuple needs at least two parts: `(x)` is just `x` in parentheses.
- A long tuple can be spread over several lines, like an array: start the parts on an indented line after `(`, with one part per line.
- Parts take the type they would have in a variable, or the types that are expected: `val t: (Long, Double?) = (1, null)`.
- Tuple types must match exactly: a `(Int, Int)` value isn't a `(Long, Int)`, though the literal `(1, 2)` fits both.
- `==` and `!=` compare part by part, as for records.
- `val (a, b): (Long, Long) = ...` gives the type of the whole tuple.
- A pattern must name every part. `val (a, b) = ...` only works inside a function, and only on tuples: read a record's fields by name.

### Atoms

Atoms are named symbols, prefixed with `:`.

```plaintext
val k = :test_atom
```

Their spelling counts: `:scrollUp` and `:scrollup` are two atoms, and `${...}` prints an atom as it was written.

## Operators

Operators bind in this order, tightest first:

| Operators | |
| --- | --- |
| `.x` `?.x` `[i]` `f()` `x!` `as` `!as` | postfix |
| `@` `addrof` `try` `new` | prefix |
| `**` | power (right to left) |
| `-` `not` `!` `!!` `~` | prefix |
| `*` `/` `%` | |
| `+` `-` | |
| `<<` `>>` | shift |
| `&` | bitwise and |
| `^` | bitwise xor |
| `\|` | bitwise or |
| `..<` `..=` | range |
| `<` `>` `<=` `>=` | |
| `is` `!is` | type test |
| `==` `!=` `===` `!==` | |
| `and` `&&` | |
| `xor` `^^` | |
| `or` `\|\|` | |
| `??` | nullish coalescing (right to left) |
| `if ... do ... else ...` | inline if |
| `\|>` | pipe |

#### Logical Operators
`not` `!` `!!` `and` `&&` `or` `||` `xor` `^^`

`and`, `or` and `xor` need two `Bool`s. `!!x` is true for `true`, non-zero numbers and non-null values; `!x` is the opposite.

#### Arithmetic Operators
`+` `-` `*` `/` `%` `**`

`**` binds tighter than prefix `-`, as in maths: `-2 ** 2` is `-(2 ** 2)`, which is -4, and `2 ** -1` is `2 ** (-1)`. Write `(-2) ** 2` for 4.

`%` is the remainder of two whole numbers. For Floats and Doubles, call `math.fmod(a, b)` from core:math.ec.

`/` and `%` on whole numbers check at runtime that the divisor isn't 0 (and that the smallest Int or Long isn't divided by -1, which doesn't fit): the program stops with `file:line: division by zero` instead of crashing. A constant divisor other than 0 and -1 needs no check. `--no-arithmetic-check` turns the checks off. Whole-number `+`, `-` and `*` that don't fit their type wrap around as in C; with `--overflow-check` they stop the program instead.

`a ** b` on two whole numbers is a whole number of their common type, computed exactly (`2 ** 10` is 1024, a Long stays a Long); a negative exponent gives 0, except for a base of 1 or -1. On other numbers it is a `Double`, computed by C's `pow`, so link with `-lm`.

#### Bitwise Operators
`&` `|` `^` `~` `<<` `>>`

They work on whole numbers. `&`, `|` and `^` follow the same type rules as `+`; a shift has the type of its left side, and shifting by as many bits as the type has (or more), or by a negative number, is an error: at compile time for a constant, otherwise at runtime (`--no-arithmetic-check` turns that check off). Unlike in C, `flags & MASK != 0` means `(flags & MASK) != 0`.

```plaintext
val READ = 1 << 0
val WRITE = 1 << 1
val flags = READ | WRITE
val canWrite = flags & WRITE != 0
```

#### Compare Operators
`==` `!=` `===` `!==` `<` `>` `<=` `>=`

`==` compares by value and `===` by identity. Two records of the same type are equal when all their fields are (Strings and CStrings by content, pointers by address, nested records field by field); records with array or union fields can't be compared with `==` yet.


```plaintext
val a: CString = "Hello"c
val b: CString = "Hello"c
val c: Bool = a == b // true
val d: Bool = a === b // false
```
The above compiles into the following C code:
```c
char* a = "Hello";
char* b = "Hello";
bool c = strcmp(a, b) == 0; // strcmp from string.h
bool d = a==b;
```

`<` `<=` `>` `>=` can be chained: `0 <= x < n` means `0 <= x and x < n`, with `x` evaluated once, and later parts only when the earlier ones hold.

#### Nullish Coalescing Operator
`??` gives the left side, or the right side when the left side is null (see [Nullable Types](#nullable-types)). On a call that can fail it gives the call's value, or the right side when the call failed (see [Error Handling](#error-handling)). The right side is only evaluated when it is needed.

The right side of `??` can also leave: `ret`, `throw`, `break` or `continue`. This removes the usual two-line guard, and after it a variable on the left is known not to be null. It runs as an `if` before the statement, so it can't be in code that may not run (a `while` condition, the right side of `and`, ...).

```plaintext
fn nameOf(id: Int): CString do
    val user = find(id) ?? ret "nobody"c      // `?? ret` alone in a function that returns nothing
    val age = parseInt(user.age) ?? throw Error { 1, "bad age" }
    ret user.name
```

#### Safe Access
`x?.name` reads a field of a nullable value: it is null when `x` is null, and the field otherwise, so its type is nullable. Chains stop at the first null. `x?.f(args)` does the same for a call: it is null when `x` is null, and `x.f(args)` otherwise, and the arguments are only evaluated when it isn't. A function that can fail can't be called this way, because `?.` has no way to handle the error.

```plaintext
fn nextValue(node: @Node?): Int = node?.next?.value ?? 0

val size = queue?.count() ?? 0
queue?.clear()
```

#### Pointer Operators
`addrof` `@`

#### Built-ins
`sizeof Type` is the size of a type in bytes, `alignof Type` its alignment (what its address must be a multiple of), and `offsetof(Record, field)` where a field starts inside a record. All three are `Size`s, computed by C.

```plaintext
val bytes = sizeof Point
val align = alignof Long
val at = offsetof(Point, y)
```

#### Ranges
`a..<b` goes from a up to, but not including, b, and `a..=b` includes b. Ranges count in a `for` loop (`for i in 0..<n do`, see [For](#for)), slice arrays and Strings (`s[1..<3]`, and with an open end `s[1..]` and `s[..<3]`), and match in a `when` arm (`1..<10 do`, see [When](#when)).

#### Pipe
`x |> f(2)` passes x to the next call as its first argument: it is `f(x, 2)`, and `x |> f` is `f(x)`. Where the value should go somewhere else, write `it`: `x |> g(1, it)`, `x |> it * it`. A chain runs left to right, so `5 |> double |> add(1)` is `add(double(5), 1)`. The value before each `|>` is evaluated once: when `it` appears more than once, the compiler stores it in a temporary first. A stage can also be a lambda: `x |> fn (v: Int) = v - 1`.

#### Type Tests
`x is T` and `x !is T` test what a union value holds, or what kind of record a pointer points to (see [Union Types](#union-types)).

#### Not Yet Implemented
These are parsed, but the compiler reports that they are not supported yet:
- The range `..>`
- `yield`, a trailing `while`, `unit`, `anon` unions, and `var`/`val`/`vol` on parameters

## Functions

### Syntax

```plaintext
fn function_name(parameter_list): return_type do
    // function body
```

or as a single expression:

```plaintext
fn function_name(parameter_list): return_type = expression
```

The return type may be omitted for functions that return nothing. A function that returns something must end with `ret` on every path. One that returns nothing can leave early with a bare `ret`, or `ret if condition`.

### Hello World

```plaintext
use "core:io.ec"

fn main(): Int do
    io.log("Hello World")
    ret 0
```

### Default Values and Union Parameters

```plaintext
fn test(a: Int, b: Int | CString, c: Int = a): Int do
    ret c
```

In this example:
- `a` and `b` are mandatory parameters.
- `c` is optional and defaults to the value of `a`.
- `b` accepts either type. The compiler generates one C function per combination: (Int, Int), (Int, CString), (Int, Int, Int), (Int, CString, Int).

A union with `Null` is split the same way, with one more function for `null`: `Int | CString | Null` gives Int, CString and Null versions. A plain `T?` stays one function, and so does a union of atoms. Because each version is compiled separately, a check such as `if v is Int` is decided at compile time inside each version, and the branch that can't run is skipped.

### Named Arguments

An argument can name its parameter. The named ones come last, in any order, and can skip parameters with default values. They only exist for the compiler: the C call is positional.

```plaintext
fn connect(host: CString, port: Int = 80, timeout: Int = 30): Int do
    ...

connect("a"c)                        // port 80, timeout 30
connect("a"c, 8080)                  // timeout 30
connect("a"c, timeout = 5)           // port 80
connect(timeout = 5, host = "a"c)
```

Arguments in order fill the parameters from the first one, so `connect("a"c, 5)` sets `port`. Builtins and calls of function values take no names, because a function type doesn't name its parameters.

### Overloading

```plaintext
fn x(a: Int) do
    io.log("Int"c)

fn x(a: CString) do
    io.log("Str"c)
```

When several overloads fit a call, the closest one wins: an exact match beats a conversion, and a smaller conversion beats a bigger one. With `f(Int)` and `f(Long)`, both `f(5)` and `f(10s)` (a Short) call `f(Int)`. If two overloads fit equally well, the call is an error that lists them; convert an argument with `as` to choose one.

### Method Call Syntax

The first argument of any function can be passed as a receiver:

```plaintext
10.x()
val a: Int = 12
a.x()
```

When the receiver is a record with a field of function type, `button.onClick(5)` calls that field instead.

### Variadic Parameters

```plaintext
// Typed variadic parameter
fn varArgTest(a: Int, b: Int, ...rest: String): Int do
    ret a + b

// C-style variadic parameter
fn varArgCTest(a: Int, b: Int, ...): Int do
    ret a + b

varArgTest(5, 8, "Hello", "World", "!")
```

Inside the function, a typed variadic parameter is an array of the extra arguments: `rest` is a `String[]` here, with `rest.len` elements (0 when there are none).

### Function References

`fn name` refers to a function as a value. When the function has several overloads, the variable's type picks one.

```plaintext
val ref = fn testCallSigFunc
val pick: (Int) => Int = fn overloadedFunction
```

### Lambdas

`fn (parameters) = expression` is a function without a name, as a value. Parameter types can be left out when the function type expected there gives them, and the return type is the expression's unless written (`fn (x: Int): Long = x`).

```plaintext
val inc = fn (x: Int) = x + 1
val square: (Int) => Int = fn (x) = x * x
io.log(apply(fn (x) = x * 3, 7))
```

A lambda becomes an ordinary C function (`main_0_lambda0`), and its value is a pointer to it. It can't use the local variables or parameters of the function around it, since lambdas don't capture variables yet. Pass what it needs as a parameter, or use a global. `try` can't be used in a lambda.

## External Declarations

`declare` makes C functions, variables, types and structs visible to eclang without generating code for them.

### Functions

```plaintext
declare fn name extern "c_name" as (parameter_list): return_type
```

`...` at the end of the parameter list accepts any number of extra arguments, like C's `printf`: numbers, Bools, Chars, CStrings, atoms and pointers. A String, array, record or tagged union is a struct, which C's `...` can't read, so passing one is an error (for a String, use `cstr(s)` or `%.*s` with `s.len as Int, s.data`).

```plaintext
declare fn printf as (format: CString, ...): Int
```

A pointer result (a pointer, `CString` or C array) of a `declare fn` is never null for eclang, unless it is declared `T?`. C can't promise that, so the call checks it: if the C function returns NULL anyway, the program stops with `file:line: the C function getenv returned NULL, but its `declare fn` says it never does`. Declare the result `T?` when it can be NULL.

`extern "c_name"` is optional and defaults to `name`.

```plaintext
declare fn printf as (format: CString, value: Any): None
declare fn abs extern "fabs" as (x: Double): Double
```

### Variables, Types and Structs

```plaintext
declare val APR_POOL_DEBUG as Int
declare var errno as Int

declare type apr_abortfunc_t as (retcode: Int) => Int

declare rec apr_pool_t
declare rec Pool extern "apr_pool_t"

declare struct Point extern "point_t" as
    x: Int
    y: Int
```

### C Arrays, Bit-Fields and Unions in Structs

A struct's fields use the same types, so a binding can match its C definition exactly. Only the fields you need have to be listed; `sizeof` and `offsetof` are C's own. C's layout comes from its header, so `packed` and `align(n)` don't apply here.

```plaintext
declare struct utsname as       // struct utsname { char sysname[65]; ... }
    sysname: Char[65]

declare rec Packet as           // typedef struct { unsigned kind : 4; } Packet;
    kind: UInt : 4

declare union sigval as         // union sigval
    sival_int: Int
    sival_ptr: @Any

declare union rec value_t as    // typedef union { ... } value_t;
    i: Int

declare var tzname as CString[2]    // a global array

var u: utsname
uname(addrof u)
io.log(u.sysname.data)
```

`int m[2][3]` is `m: Int[3][2]`, read from the left: 2 rows of `Int[3]`. C passes arrays as pointers, so `declare fn` rejects `T[N]` parameters: declare `int a[4]` as `T[]c` or `@T`, and pass `a.data`.

### Exporting Functions

`export fn` lets C call an eclang function by its own name, whichever module it is in. `export extern "c_name" fn` gives it another name.

```plaintext
module "geo"

export fn distance(p: Point): Int = p.x + p.y   // `distance` in C
export extern "geo_count" fn count(): Int = 3   // `geo_count` in C
```

C sees one function with one signature, so an exported function can't have default values, `...` parameters, generic or interface parameters, or union parameters other than `T?`. Types keep their module prefix in C (`geo__Point`).

A file with `export fn`s and no `main` is a library. `eclang lib` builds it, with a header declaring the exported functions and the types they use:

```
java -jar eclang.jar lib geo.ec -o build      # build/libgeo.so, build/geo.h
java -jar eclang.jar lib geo.ec -o build -s   # build/libgeo.a, build/geo.h (link with -lapr-1 -lm)
java -jar eclang.jar -c geo.ec --lib -o geo.c --header geo.h    # only the C
```

Only exported functions are visible to the linker. Loading the library sets up APR and its globals, and unloading it frees them. Each thread that calls into the library gets a pool of its own, and what the exported functions allocate stays there, so a returned String or pointer stays valid. It stays until that thread calls `NAME_clear()` (here `geo_clear()`), which frees all of it. A long-running program should call it once it is done with the results; a thread's pool is otherwise only freed when the library is unloaded.

### Enums

eclang code uses atoms instead of C enums. `declare enum` gives each C constant (an enum member or a `#define`) an atom:

```plaintext
declare enum apr_shutdown_how_e as
    :read = APR_SHUTDOWN_READ
    :write = APR_SHUTDOWN_WRITE
    :readwrite = APR_SHUTDOWN_READWRITE

declare fn apr_socket_shutdown as (thesocket: @apr_socket_t, how: apr_shutdown_how_e): apr_status_t

apr_socket_shutdown(socket, :write)
```

In eclang, `apr_shutdown_how_e` is just another name for `:read | :write | :readwrite`. Only a call of a `declare fn` turns the atom into its C constant, and a `declare fn` that returns the enum gives back the atom; a C value that isn't listed stops the program. Two enums can't have exactly the same atoms. Enums in fields of declared structs, in `declare var`/`val`, and behind pointers (`x_e*`) aren't converted: declare those as `Int`.

## Records

Records are user-defined data structures that group related fields under a single name. `record` is an alias for `rec`.

```plaintext
rec Test as
    a: Int
    b: Int

val t1: Test = { 0, 0 }
val t2 = Test { 1, 2 }
val e = t1.a
```

Fields can also be given by name, in any order:

```plaintext
val t3 = Test { b = 2, a = 1 }
```

A field can have a default value, which a record literal gets when it leaves the field out (by name, or at the end in field order). The default is computed each time a record is made; it can use globals and functions, but not local variables.

```plaintext
rec Config as
    name: CString
    retries: Int = 3
    verbose: Bool = false

val a = Config { name = "a"c }       // retries 3, verbose false
val b = Config { "b"c, 5 }           // verbose false
```

When every field has a default, `Config {}` gives them all.

`var c: Config` without a value is still empty (every field 0, see [Variables](#variables)); defaults apply to record literals.

A record may refer to records declared later, and to itself through a pointer:

```plaintext
rec Node as
    value: Int
    next: @Node?
```

It can't contain itself directly (`next: Node`, or through another record or a union): that would make it infinitely large.

`...` makes a copy of a record with some fields changed, which is how to change a field of a record in a `val`. Without a name, the copy has the type of the original. The original is evaluated once, and may also be a record that extends the named one.

```plaintext
val p = Point { 1, 2 }
val q = Point { ...p, x = 5 }     // 5, 2
val r = { ...p, y = 0 }           // a Point too
```

### Memory Layout

By default a record has the layout a C compiler gives a struct with the same fields. These change it. `packed`, `align` and `union` are only special here, so they stay usable as names.

```plaintext
packed rec Header as        // no padding: 5 bytes, not 8
    tag: UChar
    size: Int

align(64) rec Line as       // its address is a multiple of 64
    x: Int

packed align(4) rec Both as // both
    a: UChar
    b: Int

rec Flags as                // bit-fields: `name: Type : width`
    mode: UInt : 3
    delta: Int : 4
    on: Bool : 1

union Value as              // the fields share their memory
    i: Int
    f: Float
    bytes: UChar[4]
```

**`packed`** fields may be at any address, where C can't use a pointer to them. So `addrof`, `a[..]` and `.data` are errors on a field wider than a byte; reading and assigning work. **`align(n)`** takes a power of two; `new` and `clone` honour it too, past what a pool aligns to. Records with `packed` or `align` can't take part in `extends`, since a child must start like its parent, but they can be generic.

**Bit-fields** are whole numbers or Bools. A literal must fit (`mode = 8` is an error for 3 bits), but other values are cut to the width as in C, so check them first if that matters. They have no address, so `addrof` and `offsetof` are errors.

**Unions** hold one field at a time. A literal gives exactly one (`Value { f = 1.5 }`, or `Value { 1 }` for the first), and reading another field reinterprets the same bytes, which is only meaningful for plain data. eclang doesn't know which field is set, so a union has no `==` or `hash`, prints as `Value { ... }`, and `clone` copies its bytes without following pointers. Keep a field next to it to say what it holds, or use a [union type](#union-types), which does that for you.

### Printing and Hashing

Like `==` and `clone`, printing and hashing a record need no code of your own:

```plaintext
rec Point as
    x: Int
    y: Int

val p = Point { 1, 2 }
io.log("${p}")                          // Point { x: 1, y: 2 }
val seen: @Set<Point> = set()           // needs `hash(p)`, which is generated
seen.add(p)
```

**Printing.** `${...}` shows a record with its name and fields, and a tuple as `(1, "a")`. Inside them, Strings and CStrings are in `"` quotes (as they are, nothing is escaped) and Chars in `'` quotes, arrays are `[1, 2]`, an empty nullable is `null`, a function is `<fn>`, and a pointer shows what it points to, so a `@Animal` holding a `Cat` prints the Cat. A pointer that is already being printed (a cycle), or one more than 32 pointers deep, prints `...`. A pointer eclang can't look through (`@Any`, C types without fields, `T[]c`) prints its address. A format applies to the whole text: `${p:>24}`.

Define `str` to print a type your own way. It is used wherever a value of that type is printed, also as a field of another record:

```plaintext
rec Money as
    cents: Int

fn str(m: Money): String = "${m.cents / 100}.${m.cents % 100:02}"

rec Wallet as
    balance: Money

io.log("${Wallet { Money { 150 } }}")   // Wallet { balance: 1.50 }
```

`str` takes the value (`fn str(m: Money)`) or a pointer to it (`fn str(m: @Money)`), returns a String and can't fail. A record with no `str` uses its parent's, and `fn str(b: Box<T>)` works for every `Box`. It must be in the module that declares the type or at the top level, so a type prints the same everywhere. Writing `${m}` of its own parameter in `str` is an error, because that would call `str` again forever.

**Hashing.** `hash(x)` (or `x.hash()`) gives a `ULong` for anything `==` can compare, when no `hash` function takes it. It agrees with `==`: Strings and CStrings count by content, pointers by address, and `0.0` and `-0.0` are the same. A `fn hash(p: Point): ULong` of your own takes precedence. As `==` can't compare arrays yet, a record with an array in it has no generated `hash`.

### Extending Records

A record may extend another record. It gets the parent's fields first, then its own.

```plaintext
rec Animal as
    name: CString

rec Cat extends Animal as
    lives: Int

val tom = Cat { name = "Tom"c, lives = 9 }
```

A record that adds no fields is written without `as`: `rec Tabby extends Cat`. It is still a type of its own, so `is` and `as` tell it apart (core:errors.ec uses this for error types such as `rec RangeError extends Error`). A record of another module is named with its module: `rec Square extends shapes.Shape as`.

A `Cat` fits wherever an `Animal` is expected. Through a pointer (`@Cat` → `@Animal`) it is the same value; by value, the `Animal` fields are copied. Going back down is checked at runtime:

```plaintext
var pet: @Animal = addrof tom
if pet is @Cat do
    io.log(pet.lives)            // pet is a @Cat here
val cat = pet as @Cat         // stops the program if pet is not a Cat
```

## Control Flow

### If

Inside an `if`, the compiler knows what the condition proves; see [Flow Typing](#flow-typing).

```plaintext
if condition do
    // code block
else if condition do
    // code block
else
    // code block
```

Inline if expression, written like the statement with `do`:
```plaintext
ret if x > 0 do 1 else 0
val sign = if x > 0 do 1 else if x < 0 do -1 else 0
```

`if val` runs its body only when a value is there: when a nullable value isn't null, or when a call that can fail succeeded. Inside, the name holds the value itself (a `T`, not a `T?`). The value is evaluated once. `if var` makes the name a variable.
```plaintext
if val user = findUser(id) do       // findUser returns @User?
    io.log(user.name)
else do
    io.log("no such user"c)

if val n = parse(text) do           // parse can fail
    io.log(n)
else if text == "none" do           // `else if` and `else if val` work on both kinds of `if`
    io.log(0)
```

`assert condition` stops the program with an error naming the file, the line and the condition when the condition is false. `assert condition, "message"` reports the message instead. The code after an `assert` knows that the condition holds (see [Flow Typing](#flow-typing)).
```plaintext
assert count > 0
assert node != null, "the list is empty"
io.log(node.value)                      // node is not null here
```

Tail if, for any statement:
```plaintext
io.log("positive"c) if x > 0
ret 0 if done
break if i > 10
continue if i % 2 == 0
```

### When

`when` runs the first arm that matches its value: `is T do` (the value holds a T), `a, b do` (it equals one of the values) or `else do` (anything else). An arm's body is one statement after `do`, or an indented block. A variable is narrowed in each arm, as in an `if`.

```plaintext
fn describe(v: Int | CString | Null): CString do
    when v do
        is Int do ret "a number"c
        is CString do
            io.log(v)                 // v is a CString here
            ret "a string"c
        null do ret "nothing"c

when mode do
    :read do io.log(1)
    :write, :both do io.log(2)

when val n = parse(text) ?? 0 do   // names a value that isn't a variable
    0 do io.log("zero"c)
    else do io.log(n)
```

An arm can also be a range, and `if` adds a condition to any arm. `n if ... do` matches whatever the condition accepts, with `n` naming the value in the condition and the body. An arm with a condition covers nothing when the compiler checks that every case is handled, since it may not match.

```plaintext
when score do
    ..<0 do io.log("invalid"c)
    0 do io.log("zero"c)
    1..<10 do io.log("low"c)
    10..=99 if bonus do io.log("boosted"c)
    n if n % 2 == 0 do io.log(n)
    100.. do io.log("high"c)
```

`when` is also a value. Each arm gives a value after `do`, or leaves, with `ret`, `throw`, `break`, `continue`, a block that always leaves, or a function that returns `Never`. Every case must be handled, so it needs an `else do` arm unless its arms cover a whole union or Bool. Its type is the expected type, or else a type that fits every arm (a union when they differ).

```plaintext
fn name(n: Int): CString = when n do
    1 do "one"c
    2 do "two"c
    else do "many"c

val area = when shape do
    is Circle do 3 * shape.r * shape.r
    is Square do shape.side * shape.side
```

It runs before the statement it is in, so it can't be in code that may not run (a `while` condition, the right side of `and`, ...). It can end a `val`, `var`, `ret`, assignment or `fn f() =`; anywhere else, put it in parentheses.

Without `else`, a `when` on a union (including `T?`, unions of atoms and union parameters) or a Bool must handle every member, or it is an error that lists what is missing. Its last arm then counts as the `else`, so a `when` whose arms all `ret` ends the function. Another value (`when n % 3 do`) is compared like a C `switch`, and nothing runs when no arm matches.

### While

```plaintext
var iter = 5
while !!iter do
    io.log("I"c)
    iter = iter - 1
```

A loop (`while` or `for`) may have an `else` block. It runs when the loop ends normally, but not when it is left with `break`, which makes "search, otherwise ..." loops simple:

```plaintext
for value in values do
    if value == wanted do
        io.log("found"c)
        break
else do
    io.log("not found"c)
```

Inside a `while` loop's `else` block, the condition is known to be false (see [Flow Typing](#flow-typing)). A `break` inside the `else` block belongs to an enclosing loop.

### For

Loop over the elements of an array, or the Chars of a String:
```plaintext
for name in names do
    io.log(name)
```

Name the index (a `Size`) too with `for index, value in`:
```plaintext
for i, name in names do
    io.log(i)
    io.log(name)
```

Count with a range: `..<` stops before the end, `..=` includes it.
```plaintext
for i in 0..<10 do          // 0 to 9
    io.log(i)
for i in 1..=10 do          // 1 to 10
    io.log(i)
```

`where` skips the rounds where its condition is false. The body knows the condition holds:
```plaintext
for m in maybeValues where m != null do
    io.log(m + 1)                // m is not null here
```

Any type can be looped over. Give it a `next` that takes a pointer and gives the next element, or `null` at the end:
```plaintext
rec Countdown as
    left: Int

fn next(c: @Countdown): Int? do
    ret null if c.left == 0
    c.left -= 1
    ret c.left + 1

for n in Countdown { 3 } do      // 3, 2, 1
    io.log(n)
for i, n in Countdown { 3 } do   // i counts the rounds, from 0
    io.log(n)
```

A value is copied, and the copy advances. A pointer advances what it points to, so a later loop goes on from there: `for n in addrof c do`.

A collection keeps no position of its own, so that two loops over it don't get in each other's way. It has an `iter` instead, giving an array, a String, or a new iterator to loop over:
```plaintext
fn iter(s: @Shelf): Book[] = s.books
```

`for a, b in x` asks for `pairs(x)`, which works like `iter` for elements that are 2-tuples, and gives `a` and `b` the two parts. Without a `pairs`, `a` counts the rounds, as for an array. `for i, (a, b) in x` always counts.

`next`, `iter` and `pairs` can be anywhere a call could find them, or in the module that declares the type. `for` calls them directly, so this costs what a `while` loop would, and a generic function looping over a `<T>` gets its own copy for each type. `next` can't fail: to handle errors, call it yourself in a `while` loop. As `<T>?` of an `Int?` is an `Int?`, a generic `next` can't tell a null element from the end, and such a loop is an error: loop over an array instead.

`break` and `continue` end or skip one round of the innermost loop. A loop with a label can be named to act on it from a loop inside it, running the deferred statements of everything they leave:

```plaintext
outer: for row in rows do
    for cell in row do
        continue outer if cell < 0
        break outer if cell == 0
```

## Error Handling

Functions declare the error they can fail with using `throws`. Errors are returned by value, not with exceptions, so nothing is allocated. Any record can be an error type; [core:errors.ec](src/main/resources/errors.ec) provides `Error` (`code: Int`, `message: String`) and error types that extend it (`ParseError`, `RangeError`, `IoError`, ...). `try` converts an error to the one the function throws when it extends it, so a function that `throws Error` can pass any of them on.

```plaintext
fn throwsTest(i: Int): Int throws Error do
    throw Error { 1, "Number is too large" } if i > 10
    ret i + 1

fn check(v: Int) throws Error do      // returns nothing, but may fail
    throw Error { 2, "negative" } if v < 0
```

The caller must handle the error, in one of three ways:

```plaintext
// 1. A default value when the call fails.
val result = throwsTest(0) ?? 10

// 2. Pass the error on: if the call fails, this function returns the same error
//    (after running its deferred statements). It must declare a matching `throws`.
fn twice(i: Int): Int throws Error do
    val n = try throwsTest(i)
    ret n * 2

// 3. Run a block when the call fails.
try throwsTest(50) catch error do
    io.log(error.message)
```

Ignoring the error is a compile error:

```
error: The error from throwsTest(...) is ignored: it can fail with Error. Handle the error with `try`, `?? default` or `try ... catch e do`.
```

If `main` can fail and does, the program prints an error and exits with status 1.

`errdefer` undoes work when an error leaves the function: see [Defer](#defer).

## Type System

### Type Casting
`as` converts a value when the conversion is safe, or checked at runtime:
- anything that converts automatically (`Int as Long`);
- between any two number types (`Long as Int` cuts the value to fit; that is what `as` is for);
- a union to one of its members (`x as Int`): the program stops with an error if x holds something else;
- a record pointer to a record that extends it (`animal as @Cat`): checked the same way.

```plaintext
val intValue: Int = 56
val longValue: Long = intValue as Long
val small = longValue as Short
```

Unsafe casting is a plain C cast, with no checks. `!as` is shorthand for `as unsafe`.
```plaintext
val intValue: Int = 56
val pointer: @Int = intValue as unsafe @Int
val bytes = pointer !as @Char
```

A C cast only converts numbers and pointers, so `!as` between other types (records, Strings, arrays, tagged unions) is an error. The one exception is a String and a `Char[]` or `UChar[]`, which have the same layout: `buffer !as String` makes a String of the bytes of a `Char[]` without copying them, which is how a String is built byte by byte. `as` to a wider number type is a C cast too (`(a as Double) / b` divides Doubles).

### Type Aliases

`type` gives a type another name. The two are the same type, and aliases can be declared in any order. `declare type` does the same for a C type.

```plaintext
type Shape = Circle | Square
declare type DefinedType as Int
```

`distinct` makes a new type with the same values, so an id can't be mixed up with an ordinary number or another id. In C it is a typedef, so it costs nothing at runtime.

```plaintext
type UserId = distinct Int

val id: UserId = 5              // number literals fit
val next = id + 1               // a UserId; both sides must be UserIds or literals
val n = id as Int               // `as` converts to and from its base
val wrong: UserId = n           // error
```

A `distinct` number has its base's arithmetic and comparisons, and any `distinct` value compares, prints in `${...}`, and gives its fields, elements and `for` loops like its base. Overloads can tell it from its base: `fn show(id: UserId)` and `fn show(n: Int)` are two functions, and `show(5)` picks the Int. `distinct` isn't a keyword.

### Never

A function that never comes back returns `Never`. Code after a call of one is unreachable, so it counts like `ret`: a function can end in it, and after `if x == null do fail()`, x isn't null. `exit`, `abort` and `panic` return `Never`.

```plaintext
fn fail(message: String): Never do
    io.log(message)
    exit(1)

fn get(v: Int?): Int = v ?? fail("no value")
```

The function must not reach its end, and can't `ret` or `throw`. Deferred statements don't run, because the program stops. In C it is `_Noreturn void`.

### Pointer Types

Pointer types are written with the at sign `@`. Addresses are taken with the `addrof` keyword and dereferenced with `@`.

```plaintext
val variable: Int = 10
val addr: @Int = addrof variable
val otherVariable: Int = @addr
```
Generates the following C code:
```c
int variable = 10;
int* addr = &variable;
int otherVariable = *addr;
```

### Nullable Types

A nullable type `T?` holds either a value of type `T` or `null`. Other types can never be null, so `null` is only accepted where a `T?` is expected. A nullable value must be checked before it is used as a `T`:

```plaintext
fn greeting(name: CString?): CString do
    ret "Hello, stranger"c if name == null
    ret name                    // name is a CString here
```

`T?` for a pointer type (`@T?`, `CString?`) is a plain C pointer that may be `NULL`; for other types it is a small struct with a tag.

The nullish coalescing operator `??` replaces null with a default value.

```plaintext
fn n(a: CString?): CString do
    ret a ?? ""c
```
Or assert that the value is not null with a `!` suffix. This is checked: if the value is null, the program stops with an error that names the line.
```plaintext
fn n(a: CString?): CString do
    ret a!
```

`addrof x!` passes the address of a nullable variable to C code that fills it in (an out parameter), without checking it.

### Flow Typing

The compiler follows what conditions prove about local variables and parameters. Inside `if x != null do`, x is not null; inside `if x is Int do`, x is an Int. This works with `not`, `and` and `or`, in `else if` and `else` branches, in `while` loops, and after a guard that always leaves:

```plaintext
fn describe(v: Int | CString | Null): CString do
    ret "nothing"c if v == null      // v is Int | CString from here on
    if v is Int do
        ret "a number"c              // v is an Int here
    ret v                            // and a CString here
```

Assigning to a variable forgets what was known about it, also in the enclosing blocks, and a loop forgets it for the whole loop (the next round sees the assigned value).

A variable whose address the function takes (`addrof x` anywhere in it) is not narrowed, since a write through that pointer could change it after the check; the check gives a warning. Check a copy instead: `val now = x`, then `if now != null do`, or `if val now = x do`.

### Union Types

A union type `A | B` holds a value of any of its member types. As a variable type, it is a tagged union in C (`struct { int tag; union { ... } }`); test what it holds with `is`, as shown in [Flow Typing](#flow-typing). When used as a parameter, a function overload is generated for each member type.

```plaintext
fn y(a: Int | CString) do
    x(a)
```

Unions of atoms restrict a value to a set of symbols:

```plaintext
fn a(atom: :log | :dont_log) do
    io.log(atom) if atom == :log
```

### Duck Typing

```plaintext
rec Duck as
    name: CString
    coolness: Int

rec Goose as
    name: CString
    loudness: Int

fn quack(bird: Duck | Goose) do
    io.log(bird.name)
```

### Generic Types

Each call compiles a copy of the function for the argument types:

```plaintext
fn quack(bird: <B>) do
    io.log(bird)

quack("Hello"c)
quack(420)

fn first(values: <T>[]): <T> = values[0]
```

A `<T>` is decided by the arguments, by a lambda's return type (`<U>` in `(<T>) => <U>`), or, when only the result has it, by the type expected where the result goes:

```plaintext
fn empty(): <T>[] = new (<T>)[0]
val names: String[] = empty()

fn listOf(...values: <T>): @List<T>              // a variadic parameter can be generic: listOf(1, 2, 3)
fn orElse(value: <T>?, other: <T>): <T> = value ?? other
```

A lambda given to a generic function needs no parameter types when the other arguments decide them: `numbers.map(fn (x) = x * 2)`.

In a generic function, `new (<T>)[n]` makes room for n elements of any T, also a pointer type, which has no empty value. Those elements hold zeros until they are written, so read only what was written.

`Any`, interfaces and `<T>` have no fixed size in C, so they can only be parameter types (and, for `<T>`, return types). `@Any` is an untyped pointer (`void*`) and can be stored. To store a `<T>`, use a generic record.

### Generic Records

A record can have type parameters, written after its name. Inside it they are plain names.

```plaintext
rec Pair<A, B> as
    first: A
    second: B

rec Node<T> as
    value: T
    next: @Node<T>?

val p = Pair<Int, String> { 1, "one" }
val q: Pair<Int, String> = Pair { 2, "two" }     // the expected type gives the arguments
val nested = Pair<Pair<Int, Int>, Bool> { Pair<Int, Int> { 1, 2 }, true }
```

Each `Pair<Int, String>` used is a record of its own, like a copy of a generic function, and in C it is a struct of its own (`Pair_Int_String`). It works like any record: `==`, `clone`, pointers, arrays and fields of other records.

In a function, a generic record takes the function's type parameters. Among the type arguments of `Name<...>`, a capital letter (with at most one digit after it) that isn't a type stands for `<T>`; `List<<T>>` wouldn't parse, because `<<` is a shift:

```plaintext
fn swapped(p: Pair<A, B>): Pair<B, A> = Pair<B, A> { p.second, p.first }
fn push(list: @Node<T>?, value: <T>): @Node<T> = new Node<T> { value, list }
```

A generic record can't extend a record, or be extended, yet. See [core:structures.ec](src/main/resources/structures.ec) for lists, maps and sets built this way.

### Function Types

A function type `(A, B) => R` (or `(A, B) => R throws E`) is the type of a function value: a parameter, variable or record field holding a function, made with `fn name` (see [Function References](#function-references)). It is called like a function. In C it is a function pointer.

```plaintext
fn double(x: Int): Int = x * 2
fn apply(func: (Int) => Int, value: Int): Int = func(value)

rec Button as
    onClick: (Int) => Int

fn main(): Int do
    io.log(apply(fn double, 5))         // 10
    val b = Button { onClick = fn double }
    io.log(b.onClick(4))                // 8
    ret 0
```

A local variable or parameter of function type takes precedence over a function with the same name. A nullable function value (`((Int) => Int)?`) must be checked against null before it is called.

### Interfaces

An interface lists fields. Any record that has them (same names and types) fits, without declaring so. Each record passed to a function with an interface parameter gets its own copy of the function.

```plaintext
inter Named as
    name: CString

rec Duck as
    name: CString
    coolness: Int

fn greet(who: Named) do
    io.log(who.name)

greet(Duck { "Donald"c, 9 })
```

A pointer parameter works the same way: `@Named` accepts a pointer to any record with a `name: CString`, so the function can change it.

An interface can also ask for functions, written without the record parameter: a record fits when a function of that name takes it first (so `x.area()` works on it) with the other parameter types, and returns the type. It may say `throws`: `fn tell(): Long throws Error`, or `fn write(String) throws Error` when it returns nothing.

```plaintext
inter Shape as
    name: CString
    fn area(): Double
    fn scaled(Double): Double

fn area(s: Square): Double = s.side * s.side
fn scaled(s: Square, k: Double): Double = s.area() * k

fn report(s: Shape) do
    printf("%s %g\n"c, s.name, s.area())
```

Passing a record that lacks one is an error that names the function to define, e.g. `fn area(x: Tri): Double`.

```plaintext
fn rename(who: @Named, name: CString) do
    who.name = name

var duck = Duck { "Donald"c, 9 }
rename(addrof(duck), "Daisy"c)
```

## Memory Management

### Defer

`defer` runs a statement when the enclosing scope exits, including on early return, when `try` passes an error on, and when `break` or `continue` leaves a loop body. Deferred statements run in reverse order: the last one deferred runs first.

```plaintext
fn defTest(input: DefinedType): Int do
    defer io.log("1"c)
    io.log("2"c)
    if true do
        defer io.log("3"c)
        io.log("4"c)
    else
        defer io.log("5"c)
        io.log("test"c)
        ret 1
    ret input
```
The above generates the following C code:
```c
int defTest_0(int input) {
	printf("2");
	if (true) {
		printf("4");
		printf("3");
	}
	else {
		printf(("test"));
		int __ec_ret = 1;
		printf("5");
		printf("1");
		return __ec_ret;
	}
	int __ec_ret = input;
	printf("1");
	return __ec_ret;
}
```

Defer a block of code:

```plaintext
fn defTest(input: DefinedType): Int do
    defer do
        io.log("1"c)
        io.log("2"c)
    io.log("3"c)
```

`errdefer` is a `defer` that only runs when the scope is left by an error: by `throw`, or `try` passing one on. Use it to undo what a function has done so far when it can't finish. It needs a function that declares `throws`.

```plaintext
fn open(path: CString): @File throws Error do
    val f = try acquire(path)
    errdefer release(f)             // only when something below fails
    try configure(f)
    ret f
```

### Memory Pools

eclang allocates memory in pools (from APR, the Apache Portable Runtime) instead of freeing values one by one: everything in a pool is freed at once when the pool is freed. Pools need no `use`; the type of a pool is `Pool`.

There is always a **current pool**, called `pool`. When the program starts it is a pool that lives until the program ends. A `pool do` block makes a new pool, a child of the current one, and that pool is current inside the block. The pool and everything in it are freed when the block ends, however it ends (at its end, or by `ret`, `break`, `continue` or `try`):

```plaintext
fn main(): Int do
    for i in 0..<1000 do
        pool do                          // freed at the end of every round
            val line = "line ${i}"
            io.log(line)
    ret 0
```

Strings with `${...}`, `concat`, `new` and `clone` allocate in the current pool, and so do the functions they're called from: a function that returns `new Point { ... }` allocates in its caller's current pool. Give a value its own pool by passing a `Pool` (`fn load(p: Pool): @Config`), and allocate in it with `new ... in p`, `clone(x, p)`, `format(p, "...")` and `concat(p, a, b)`.

`pool name do` names the block's pool, so code in a nested block can still allocate in it:

```plaintext
pool results do
    pool do                              // a scratch pool for the work
        val point = new Point { x = 1, y = 2 } in results
```

A pool that isn't tied to a block is made with `newPool()` (a child of the current pool, freed with it at the latest) or `newPool(parent)`. `clearPool(p)` frees everything in `p` and keeps the pool, and `freePool(p)` frees it. Either also frees the pools made in `p`. The pool of a `pool do` block, and the current pool, are freed for you, so `freePool` rejects them, also through another name (`val q = pool`, or a field `h.p`). `clearPool` rejects the current pool outside a `pool do` block, as that pool is also the caller's, and clearing it would free what the callers hold in it. Inside a `pool do` block, `clearPool(pool)` clears that block's pool.

`Pool` is `@apr_pool_t`, so pools can be passed to the APR functions in [apr/](src/main/resources/apr) (`apr_psprintf(pool, "%d items"c, 3)`, with `use "core:apr/strings.ec"`). In C, the current pool is a thread-local `__ec_pool` that the generated `main` creates. A thread started from C code has no current pool until its code opens a `pool do` block. Running out of memory stops the program with an error.

### New

`new value` copies a value into the current pool and gives a pointer to the copy, which lives as long as the pool. `new T` points to an empty T (see [Variables](#variables)), and `new T[n]` makes an array of n empty elements; for any other element type, put it in parentheses: `new (@Point?)[n]`, `new (Int[])[n]` (a pointer has no empty value, so an array of pointers holds `@T?`). Add `in p` to use pool p:

```plaintext
fn makePoint(x: Int, y: Int): @Point = new Point { x = x, y = y }

val counter = new 0                     // @Int
val empty = new Point                   // @Point, x and y are 0
val buffer = new Char[size] in p        // Char[], in pool p
```

### Clone

`clone(x)` (or `x.clone()`) makes a deep copy of x in the current pool, and `clone(x, p)` makes one in pool p. Strings, CStrings and arrays are copied, and what a pointer points to is copied too, recursively, so the copy shares no memory with the original. A pointer to a record that is extended copies the record it really points to (`@Animal` holding a `Cat` copies the Cat). What `clone` can't copy is kept as it is: C arrays (`T[]c`, which have no length), `@Any`, function values, C unions, and pointers to C types whose fields eclang doesn't know (such as `Pool`). Each pointer is copied once: a value that two parts of x point to is one value in the copy too, and a value that points back to itself (a cycle) is copied as the same cycle. A pointer into a value (`addrof r.first`) is a copy of its own: in the copy it doesn't point into the copy of r.

```plaintext
val copy = clone(list)                  // a @Node? with every node and name copied
val numbers = clone([1, 2, 3])          // an array literal copied out of the stack
```

### Lifetimes

The compiler rejects pointers that would outlive the memory they point to. That memory is a local variable or parameter (freed when its block ends), the elements of an array literal (stored on the stack, like those of a default value and the array of a `...rest` parameter), a literal that `addrof` points at, or a `pool do` block's pool, together with whatever was allocated in it, including by the functions called inside the block. A value pointing into such memory can't be returned or thrown, and can't be stored in a variable defined outside its block, in a global, or behind a pointer:

```plaintext
fn leak(): @Int do
    var x = 42
    ret addrof x            // error: it points to 'x', a local variable, ... `new x` is a copy of it in the current pool

fn numbers(): Int[] = [1, 2, 3]          // error: ... Copy it into a pool with `clone(...)`

var name = "none"
pool do
    name = "n is ${n}"      // error: ... 'name' lives longer
```

Passing such pointers to functions is fine (`fill(addrof(info))`). The compiler follows values through variables, record and array literals, `new`, pipes, and calls: a call's result may point into its arguments (`ret id(addrof x)` is an error), or into the pools it is given (a C function given a pool is taken to allocate its result there). What is read out of memory (`r.field`, `p.next`, `s[a..<b]`, `@p`) may point where r, p and s do, because anything stored there must live at least as long. An element of a stack array is an exception: `ret names[0]` is fine when the Strings in `names` outlive the function. Names bound by `if val`, `when` arms, `catch` and `for` are followed like variables, a write through a pointer changes what it points at (`@pp = addrof x` after `pp = addrof o`), and a value left by one round of a loop reaches the code before it in the next round. `clone` makes new memory, except for what it can't copy (`@Any`, for one), which still points where it did. `try` inside a `pool do` block copies the error into the pool that was current before the block, because leaving the block frees its pool.

A pool made with `newPool()` is followed the same way: after `clearPool(p)` or `freePool(p)`, a variable holding something allocated in p or a child pool of p before can't be used (and after `freePool(p)`, neither can p). A global can't hold such memory when the pool is freed. This also covers another name for the pool, a `Pool` field (`h.p`), a global pool, and reading through a pointer (`@ps`).

```plaintext
val p = newPool()
val s = format(p, "hello")
clearPool(p)
io.log(s)                      // error: 's' points to memory of pool 'p' that `clearPool(p)` freed
```

This holds across `if`s and across the rounds of a loop: a value left by one round can't be used in the next if the loop clears its pool in between, and a loop can't free a pool it made outside.

`ret` and `throw` also count what `defer` frees: with `defer freePool(p)`, `ret s` of a String in p is an error, since the pool is freed after s is read. Return a copy: `ret clone(s)`. The result of a call isn't rejected there, because a function working in a scratch pool often returns a call's result that isn't in that pool. `try` copies the error it passes on when a deferred statement frees a pool.

### APR Bindings

The APR bindings declare APR's functions with eclang types. Functions that return `NULL` when nothing is found (`apr_table_get`, `apr_hash_get`, `apr_hash_next`, ...) return nullable types. C `const` has no eclang equivalent: a `CString` is passed where C wants a `const char*`, and the compiler adds the casts C needs for pointers to pointers and function pointers. `tests/run.sh` checks every binding against the APR headers ([tools/check-bindings.py](tools/check-bindings.py)).

## Ecdocs
An ecdoc is a comment starting with `/**`; it documents the declaration right after it, as in Javadoc. `eclang doc` turns them into Markdown. The description is Markdown, and block tags follow it. Types are never written in an ecdoc: `eclang doc` takes them from the declaration, the thrown type included.
```
/**
 * The distance between two points, always zero or more.
 *
 *     val d = distance(Point{0.0, 0.0}, Point{3.0, 4.0})   // 5.0
 *
 * @param a where to start
 * @param b where to end; the same as {@code a} gives 0
 * @return the straight-line distance
 * @see Point
 */
fn distance(a: Point, b: Point): Double = ...

/**
 * Reads the whole file.
 * @throws when the file can't be opened
 */
fn readAll(path: String): String throws Error do
    ...
```
| Tag | Meaning |
| --- | --- |
| `@param name text` | A parameter (or a type parameter, `@param <T> text`). |
| `@return text` | What a function returns (`@returns` works too). |
| `@throws text` | When a function throws. Name the type first (`@throws ParseError text`) when it throws a union of several. `@exception` works too. |
| `@see name` | A related declaration (`name`, `Type.field`, `module.name`; `Type#field` works too), optionally followed by a label, or a link or `"text"`. |
| `@since text`, `@deprecated text`, `@author text` | As in Javadoc. |

In the text, `{@link name}` (or `{@link name label}`) links to a declaration and `{@code text}` is code. An ecdoc can document any top-level declaration (`fn`, `declare fn`, `rec`, `inter`, `val`, `var`, `const`, `declare type`, ...), a record or interface field, and an enum value. The one before `module "name"` (or, in a file without `module`, before the first `use`) describes the file. The lines' leading `*` are optional.

Ecdocs are checked whenever a file is compiled or documented. A `@param` that names no parameter, `@return` on a function that returns nothing, `@throws` on one that doesn't throw, an unknown tag, or an ecdoc that documents nothing (inside a function body, say) gives a warning. Write `/*` for an ordinary block comment.

## Standard Library

The standard library is bundled with the compiler. Each part is loaded with `use`. There is no prelude: `io.log` is in `core:io.ec`, `Error` in `core:errors.ec` (which every part loads), and C's `printf` in `core:c/stdio.ec`.

| File | | |
| --- | --- | --- |
| `core:string.ec` | top level | String functions, callable on a String (`s.trim().toUpper()`) |
| `core:errors.ec` | top level | common error types and APR status errors |
| `core:math.ec` | `math.` | C's math.h, constants, number helpers, checked arithmetic, random numbers |
| `core:io.ec` | `io.` | `log`, console, command-line arguments, files, directories, paths, environment variables |
| `core:time.ec` | `time.` | `Time`, `Duration`, a steady clock, calendar dates, ISO 8601 |
| `core:thread.ec` | `thread.` | threads, `Mutex`, `RwLock`, `Cond`, `WaitGroup`, `Atomic`, `Channel` |
| `core:os.ec` | `os.` | system information, environment variables, users, running programs, signals |
| `core:array.ec` | top level | algorithms on arrays of any type: `sort`, `map`, `filter`, `reduce`, `binarySearch`, ... |
| `core:structures.ec` | top level | data structures: `List`, `Map`, `Set`, `Deque`, `Heap` |
| `core:encoding.ec` | `encoding.` | hexadecimal, Base64 and URL escapes |
| `core:net.ec` | `net.` | TCP clients and servers, UDP, DNS lookups, timeouts |
| `core:fs.ec` | `fs.` | walking directory trees, glob patterns, temporary files, normalising paths, memory-mapped reads |
| `core:json.ec` | `json.` | parsing JSON with error positions, building values, writing them |
| `core:hash.ec` | `hash.` | CRC32, FNV-1a, MD5, SHA-1, SHA-256, HMAC |
| `core:args.ec` | `args.` | flags, options, subcommands, a generated `--help` |
| `core:unicode.ec` | `unicode.` | code points of UTF-8 text: going through them, encoding, validating, repairing |
| `core:bytes.ec` | `bytes.` | a binary reader and writer with a byte order, varints |
| `core:csv.ec` | `csv.` | reading and writing CSV (RFC 4180), tables with a header |
| `core:log.ec` | `log.` | levels, timestamps, output to stderr or a file, from any thread |
| `core:regex.ec` | `regex.` | matching, capturing, replacing and splitting, in linear time |
| `core:http.ec` | `http.` | URLs, an HTTP/1.1 client and a small server |
| `core:test.ec` | `test.` | assertions with readable diffs and a small test runner |

Every file starts with an ecdoc that shows how to use it, and every declaration has one, so `eclang doc core:string.ec` writes its reference.

### Strings

`core:string.ec` works on bytes, and its character tests (`isDigit`, `isAlpha`, `toUpper`, ...) are ASCII only. Functions that return a part of a String (`trim`, `before`, `after`, `removePrefix`, `take`, `split`, `lines`, `words`) return slices, which share its memory; functions that make a new String (`toUpper`, `replace`, `join`, `repeat`, `padStart`, `quote`, ...) allocate it in the current pool.

```plaintext
use "core:string.ec"

val name = " Ada Lovelace ".trim().toUpper()          // "ADA LOVELACE"
val csv = join("a, b, c".split(", "), ";")            // "a;b;c"
val n = try parseLong("-ff", 16)                       // -255, or a ParseError
val b = stringBuilder()
b.append("x = ")
b.append(42)
io.log(b.build())
```

Also: `indexOf`/`lastIndexOf`/`count`/`contains` (of a String or a Char), `compare`, `equalsIgnoreCase`, `naturalCompare`, `hash`, `toBase`, `hex`, `parseDouble`, `parseBool`, `charCount` and `isUtf8` (UTF-8).

### Arrays

`core:array.ec` works on arrays of any type. `sort` and `reverse` change the array in place, so `clone` an array literal first; `map`, `filter` and the like make a new array in the current pool. Lambdas need no parameter types here.

```plaintext
use "core:array.ec"

val numbers = clone([3, 1, 2])
numbers.sort()                                          // stable, O(n log n)
val squares = numbers.map(fn (x) = x * x)
val total = numbers.reduce(0, fn (sum, x) = sum + x)
people.sortBy(fn (a, b) = a.age < b.age)
```

Also: `contains`, `indexOf`, `lastIndexOf`, `count`, `find`, `findIndex`, `any`, `all`, `binarySearch`, `filter`, `copyOf`, `joined`, `distinct`, `take`, `drop`, `fill`, `swap`, `reverse`, `isSorted`.

### Data Structures

`core:structures.ec` has `List<T>` (a growing array), `Map<K, V>` and `Set<T>` (hash tables that keep the order things were added in), `Deque<T>` (a queue with two ends) and `Heap<T>` (a priority queue). `list()`, `map()` and the like have no argument to tell what they will hold, so write the type where the result goes.

```plaintext
use "core:structures.ec"

val names: @List<String> = list()
names.push("Ada")
for name in names do
    io.log(name)
val numbers = listOf(3, 1, 2)                 // a @List<Int>

val ages: @Map<String, Int> = map()
ages.put("Ada", 36)
io.log(ages.get("Ada") ?? 0)                  // get gives an Int?
for name, age in ages do                      // in the order they were added
    io.log("${name}: ${age}")

val seen: @Set<Int> = set()
seen.add(7)
for value in seen do
    io.log(value)

val queue: @Deque<Int> = deque()
queue.pushBack(1)
val next = queue.popFront() ?? 0

val tasks: @Heap<Int> = heap(fn (a: Int, b: Int) = a < b)
```

A structure's memory is in the pool that was current when it was made (`list(p, 16)` and `map(p)` take a pool), and it grows there. It doesn't copy what is put in it, so Strings and pointers in it must live at least as long as it does; the compiler doesn't check that through these functions.

Map and Set find keys with `hash(key)` and `==`, so anything `==` can compare works as a key, with a [generated hash](#printing-and-hashing) unless a `hash` function takes it. For a hash of your own, define `fn hash(p: Point): ULong` at the top level.

### Errors

Each error type of `core:errors.ec` extends `Error`, and its `code` says what kind it is (`ERR_PARSE`, `ERR_NOT_FOUND`, ...). `ParseError` has the `input` and the `position` of the problem, `IoError` the `path` and the APR `status`.

```plaintext
fn load(path: String): Long throws Error do
    val text = try io.readFile(path)          // throws IoError
    ret try parseLong(trim(text), 10)          // throws ParseError

try load("count.txt") catch e do
    io.log(e)                                     // "not found: count.txt: No such file or directory"
```

`osError(status, what)` and `ioError(status, path)` turn an APR status into an error with its message, `checkStatus(status, what)` throws one when an APR call failed, and `panic(message)` stops the program.

### Math, IO, Time and Threads

These are modules, so their names start with the module's: `math.sqrt(2.0)`, `io.readLines(path)`, `time.now()`, `thread.spawn(fn work, addrof(job))`.

```plaintext
use "core:math.ec"
use "core:io.ec"
use "core:time.ec"
use "core:thread.ec"

val r = math.hypot(3.0, 4.0)
val product = try math.checkedMul(a, b)             // a RangeError instead of overflowing
var rng = math.random(42)                            // the same numbers on every run
val roll = math.nextInt(addrof(rng), 1, 7)

for line in try io.readLines("data.txt") do
    io.println(line)

val start = time.instant()
io.log(time.iso(time.now()))                            // 2024-02-29T12:30:00.25Z
io.log(time.text(time.elapsed(start)))                  // 1.5ms

val t = try thread.spawn(fn work, addrof(job))       // work(job: @Job) runs in the thread
try thread.join(t)
```

`io.args()` gives the program's command-line arguments and `io.programName()` its name. `core:encoding.ec` turns bytes into text and back: `encoding.base64(s)`, `try encoding.fromBase64(text)`, `encoding.hex(s)`, `encoding.urlEscape(s)`.

A thread gets a pointer to its data, since lambdas don't capture variables: join it before that data's block ends. Each thread has its own current pool. Locks, conditions and channels are allocated in the current pool and freed with it; lock calls that fail (only possible when they are used wrongly) stop the program.

### Terminals

[core:term.ec](src/main/resources/term.ec) is for programs that talk to a person in a terminal: colours, prompts, key and mouse input, and full-screen programs. It needs a POSIX system; [examples/tui.ec](examples/tui.ec) is a small full-screen program.

```plaintext
use "core:term.ec"

io.log(term.color("done", term.GREEN))              // plain text when stdout isn't a terminal or NO_COLOR is set
val name = term.ask("name: ") ?? ""
val secret = term.password("password: ") ?? ""
val ok = term.confirm("Delete it?")
val pick = term.select("Colour", ["red", "green"])   // arrow keys and Enter; null for Esc
term.progress("copying", i, total)

try term.enterFullscreen()
defer term.leaveFullscreen()
val s = term.screen()
while true do
    term.clear(s)
    term.box(s, 0, 0, s.cols, s.rows, :rounded, "Demo")
    term.text(s, 2, 1, "press q", term.Style { bold = true })
    term.render(s)                                   // writes only the cells that changed
    val e = term.readEvent()
    when e do
        is term.Key do break if term.isChar(e, 'q')
        is term.Resize do term.resize(s, e.size)
        is term.Mouse do continue
        is term.Eof do break
```

`term.pollEvent(micros)` gives up after a while, for a program with other things to do. The terminal is put back however the program ends, also after a panic or a fatal signal, and while it is stopped with Ctrl-Z. In full-screen mode stderr is held back until the mode ends, so a panic's message stays readable.

### Operating System

`core:os.ec` tells about the system and runs other programs. It is built on APR where APR has the call.

```plaintext
use "core:os.ec"

io.log("${os.name()} ${os.arch()}, ${os.cpuCount()} CPUs")   // Linux x86_64, 8 CPUs
io.log(try os.hostName())
io.log((try os.user()).home)
try os.setEnv("LANG", "C")

val r = try os.run("git", ["status", "--short"])     // waits; r.stdout, r.stderr, r.code, r.signal
io.print(r.stdout) if os.ok(r)
val n = try os.shell("ls | wc -l")                    // a command line, run by /bin/sh

val p = try os.spawn("sort", ["-r"], :pipe)           // runs next to this program
try io.writeLine(p.stdin!, "a")
io.close(p.stdin!)
io.print(try io.readAll(p.stdout!))
val exit = try os.wait(p)                             // or os.poll(p), os.kill(p, SIGKILL)
```

`run` and `spawn` find a program as a shell does and pass the arguments as they are, so text from outside the program is safe there, and not in `shell`. A program that can't be found is an error; one that fails is not: check `os.ok`. `os.run` reads both of a program's streams at once, so a program writing a lot can't get stuck. With `spawn` and `:pipe` that is up to the caller. Writing to a program that ended raises SIGPIPE, which ends this program unless `os.ignoreSignal(SIGPIPE)` was called.

### Networking

`core:net.ec` has TCP, UDP and DNS, built on APR's sockets and poll. There is no TLS.

```plaintext
use "core:net.ec"

val c = try net.connect("example.com", 80, time.seconds(5))    // gives up after 5 seconds
defer net.close(c)
net.setTimeout(c, time.seconds(10))                            // for each read and write from now on
try net.send(c, "GET / HTTP/1.0\r\nHost: example.com\r\n\r\n")
val status = try net.readLine(c)                               // or net.read, net.readExactly, net.readAll

val server = try net.listen(8080)                              // 0 lets the system choose; server.address.port tells
while true do
    pool do                                                    // what one connection uses is freed after it
        val client = try net.accept(server)
        try net.send(client, "hello ${client.remote.host}\n")
        net.close(client)

val u = try net.udp()
try net.sendTo(u, "127.0.0.1", 9000, "ping")
val d = try net.receiveFrom(u)                                 // d.data, d.source.host, d.source.port

val ips = try net.lookup("example.com")
```

A timeout is an error with the code `ERR_TIMEOUT`, and the connection can still be used. `net.waitReadable(c, timeout)` waits without reading. To serve many clients at once, give each its own thread. Opening a connection makes the program ignore SIGPIPE, so writing to a peer that has gone is an error and doesn't end the program.

### Files and Directories

`core:fs.ec` adds to [core:io.ec](src/main/resources/io.ec) what works on trees and paths, built on APR.

```plaintext
use "core:fs.ec"

for e in try fs.walk("src") do                   // parents first, sorted; e.path, e.kind, e.size, e.modified, e.depth
    io.log(e.path) if e.kind == :file

val sources = fs.glob("src/**/*.ec")             // sorted; a whole part ** is any number of directories
val isText = fs.matches("*.txt", "notes.txt")    // the same patterns, without the file system

val t = try fs.tempFile("report")                // deleted when it is closed
try io.writeLine(t.file, "hello")
io.close(t.file)
val dir = try fs.tempDir()                       // stays until it is removed
try fs.removeAll(dir)

io.log(fs.normalize("a/./b/../c"))               // a/c, as text
io.log(try fs.absolute("notes.txt"))             // /home/ada/notes.txt
io.log(try fs.relative("/a/b", "/a/c/d"))        // ../c/d

val m = try fs.map("big.log")                    // m.text isn't a copy: the system reads what is used
io.log(m.text.count('\n'))
fs.unmap(m)
```

`walk` and `**` don't follow symbolic links, so a link can't make them loop. As in a shell, a name starting with a dot only fits a pattern that says so.

### JSON

`core:json.ec` reads and writes JSON. Parsing follows RFC 8259 strictly, and an error says what is wrong and where (`line 3, column 3: ...`, also as `e.line`, `e.column`, `e.position`).

```plaintext
use "core:json.ec"

val v = try json.parse(text)
val name = json.asString(json.field(v, "name")) ?? "?"
val city = json.asString(json.find(v, "users[2].address.city")) ?? "?"
for tag in json.items(json.field(v, "tags")) do
    io.log(json.asString(tag) ?? "?")

val out = json.object()
json.put(out, "ok", true)
val list = json.array()
json.add(list, 1)
json.put(out, "list", list)
io.log(json.stringify(out))                      // {"ok":true,"list":[1]}
io.log(json.pretty(out))
```

The readers take and give null, so they chain: what isn't there comes out as null at the end. An object keeps its fields in order, and whole numbers that fit a Long stay exact.

### Unicode

`core:unicode.ec` works with the code points of UTF-8 text. A String is bytes, and nothing checks that they are UTF-8: reading never fails, and each broken byte counts as one U+FFFD. Overlong forms, surrogates and what is above U+10FFFF are broken too.

```plaintext
use "core:unicode.ec"

for cp in unicode.codePoints("héllo") do         // 104, 233, 108, 108, 111
    io.log(cp)
for at, cp in unicode.codePoints("héllo") do     // at is the byte where it starts
    io.log(at)
io.log(unicode.length("héllo"))                  // 5, where .len is 6
io.log(unicode.slice("héllo", 1, 3))             // él
io.log(unicode.encode(0x1F600))                  // 😀
val (cp, n) = unicode.decode("é!", 0)            // 233, and its 2 bytes
val at = unicode.firstInvalid(text) ?? text.len  // where it stops being UTF-8
val shown = unicode.sanitize(text)               // broken bytes become U+FFFD
```

There are no tables of the Unicode character database, so no case mapping, normalisation or categories.

### Binary Data

`core:bytes.ec` reads and writes binary formats. A `Reader` and a `Writer` each have a byte order, in their `endian` field, which can change on the way. Reading past the end is a `ParseError` that says where, and reads nothing.

```plaintext
use "core:bytes.ec"

val w = bytes.writer()                           // big-endian; bytes.writer(:little) for the other
bytes.writeU16(w, 0xCAFE)
val size = bytes.position(w)
bytes.writeU32(w, 0)                             // filled in below
bytes.writeString(w, "hello")
bytes.setU32(w, size, 5)
val data = bytes.finish(w)                       // a String of 11 bytes

val r = bytes.reader(data)
val magic = try bytes.readU16(r)
val n = try bytes.readU32(r)
val text = try bytes.read(r, n as Size)          // a slice of the data
```

There are `U8` to `U64`, `I8` to `I64`, `F32` and `F64`, C strings, and varints (`writeVarint`, `writeSignedVarint`), which are the same bytes as in Protocol Buffers.

### CSV

`core:csv.ec` follows RFC 4180. Reading also takes `\n` and `\r` for line breaks, a last line without one, and a UTF-8 byte order mark; writing uses `\r\n`, and quotes only the fields that need it. An error has the line and column, also as `e.line` and `e.column`.

```plaintext
use "core:csv.ec"

val rows = try csv.parse("name,age\nAda,36\n\"Hopper, Grace\",85\n")
io.log(rows[2][0])                               // Hopper, Grace

val t = try csv.table("name,age\nAda,36\n")      // the first row names the columns
io.log(csv.value(t, 0, "age") ?? "?")            // 36

val r = csv.reader(text)                         // a row at a time
while true do
    val row = try csv.next(r) ?? break
    io.log(row.len)

io.log(csv.stringify([["a,b", "c"], ["1", "2"]]))
val tabs = csv.Options { separator = '\t' }       // also quote, lineBreak, skipBlankLines, strict
val cells = try csv.parse(text, tabs)
```

### Logging

`core:log.ec` writes messages with a level and a time, to stderr or a file. Each line is written whole, so lines from several threads don't mix, and logging can't fail: when a file can't be written, the line goes to stderr.

```plaintext
use "core:log.ec"

log.info("started")                              // 2026-03-14T09:26:53.589Z INFO  started
log.warn("disk ${percent}% full")
log.error(problem)                               // a message, or any Error

log.setLevel(:debug)                             // :debug, :info (the first setting), :warn, :error, :off
try log.toFile("app.log")                        // adds to the end of the file
log.setTimestamps(:local)                        // :utc (the first setting), :local, :none

val db = log.logger("db")                        // settings of its own
log.info(db, "connected")                        // ... INFO  db: connected
log.debug("state: ${expensive()}") if log.enabled(:debug)
```

### Regular Expressions

`core:regex.ec` runs in linear time: at worst, the size of the pattern times the length of the text, so no pattern can hang a program. That rules out backreferences and look-around, which `compile` rejects with the reason. A pattern is limited in size too. A `Regex` doesn't change once it is made, so threads can share it.

```plaintext
use "core:regex.ec"

val date = try regex.compile("(\\d{4})-(\\d{2})-(?P<day>\\d{2})")
io.log(regex.matches(date, "2024-02-29"))        // the whole text
io.log(regex.contains(date, "on 2024-02-29!"))   // a part of it

if val m = regex.find(date, "on 2024-02-29!") do
    io.log(regex.text(m))                        // 2024-02-29
    io.log(regex.group(m, 1) ?? "")              // 2024; null when it took no part
    io.log(regex.group(m, "day") ?? "")          // 29
    io.log(regex.start(m))                       // 3

for m in regex.findAll(try regex.compile("\\w+"), "two words") do
    io.log(regex.text(m))

io.log(try regex.replaceAll(date, "2024-02-29", "$3/$2/$1"))
val parts = regex.split(try regex.compile(",\\s*"), "a, b,c")
val quiet = try regex.compile("hello", ignoreCase = true)   // also multiline, dotAll
val exact = try regex.compile(regex.escape(userText))
```

The syntax is that of Perl and Python, with the leftmost match and greedy repeats; [its reference](docs/core/regex.md) has all of it. Text is UTF-8, so `.` takes a whole code point, while positions are bytes. `ignoreCase`, `\d`, `\w`, `\s` and `\b` are ASCII only.

### HTTP

`core:http.ec` parses URLs and has an HTTP/1.1 client and a small server, on top of `core:net.ec`. There is no HTTPS: that needs a TLS library, which a program can't link yet, so an `https:` URL is an error that says so.

```plaintext
use "core:http.ec"

val r = try http.get("http://example.com/")      // follows redirects
io.log(r.status)                                 // 404 and the like aren't errors
io.log(http.get(r.headers, "Content-Type") ?? "?")
io.log(r.body)
val sent = try http.post("http://example.com/api", "{}", "application/json")
val quick = try http.get(url, http.Options { timeout = time.seconds(5), redirects = 0 })

val u = try http.parseUrl("http://example.com:8080/a/b?x=1#top")
io.log(u.host)                                   // example.com
io.log(http.param(u, "x") ?? "?")                // 1
io.log(http.text(try http.resolve(u, "../c")))   // http://example.com:8080/c
```

A server is a function from a request to a response. It runs on a fixed number of threads, so handlers run at the same time; each request has its own pool. As functions can't capture variables, a handler reaches the program's data through `req.context`, from `ServerOptions`.

```plaintext
fn hello(req: @http.Request): @http.Response do
    ret http.text(404, "nothing here\n") if req.path != "/"
    val name = http.param(req.url, "name") ?? "world"
    ret http.text(200, "hello ${name}\n")

fn main(): Int throws Error do
    val server = try http.server(8080)           // also a host, and http.ServerOptions
    try http.run(server, fn hello)               // until http.stop(server)
    ret 0
```

`http.start` serves on other threads and goes on, and a handler that `throws Error` gives the client a 500 and writes to `core:log.ec`. The server refuses what isn't valid HTTP/1.1, including what could smuggle a request past a proxy, and limits the size of a request. It is small, though: in front of the open Internet, put it behind a reverse proxy.

### Tests

`core:test.ec` has assertions that say what differs, and a small runner. A failed check doesn't end a test. Tests are listed by hand, because finding them needs compiler support.

```plaintext
use "core:test.ec"

fn addition(t: @test.Test) do
    test.equal(t, 1 + 1, 2)
    test.equal(t, render(), "line one\nline two\n", "the rendered text")

fn parsing(t: @test.Test) throws Error do        // an error that gets out fails the test
    test.equal(t, try parseInt("12"), 12)

fn main(): Int do
    val s = test.suite()                         // `./tests pars` only runs tests with "pars" in their name
    test.run(s, "addition", fn addition)
    test.run(s, "parsing", fn parsing)
    ret test.finish(s)                           // 0, or 1 when something failed
```

Strings show a line by line diff, or a mark under the first difference. There are also `equalArrays`, `notEqual`, `isTrue`, `isFalse`, `near`, `isNull`, `notNull`, `hasText`, `fail`, and `require`, for a check the rest of the test can't do without.

```plaintext
FAIL addition
  check 2, the rendered text: the strings differ (- expected, + actual)
      line one
    - line two
    + line 2
```

### Hashes

`core:hash.ec` has checksums and hashes. MD5, SHA-1 and SHA-256 are [C code bundled with eclang](src/main/resources/vendor/README.md); a program using them still builds from one C file.

```plaintext
use "core:hash.ec"

io.log(hash.sha256("hello"))                     // in hexadecimal; also hash.md5, hash.sha1
io.log(hash.crc32("hello"))                      // also hash.fnv32, hash.fnv64
io.log(hash.hmac(:sha256, key, message))
io.log(try hash.file("big.iso"))                 // read a part at a time

val h = hash.hasher(:sha1)                       // data that arrives in parts
hash.update(h, "hel")
hash.update(h, "lo")
io.log(hash.hex(h))                              // or hash.finish(h) for the raw bytes
```

MD5 and SHA-1 are broken for security, none of these is a password hash, and the code isn't hardened against timing attacks. Compare signatures with `hash.equal`, not `==`.

### Command-Line Arguments

`core:args.ec` reads the command line, and makes the `--help` and the errors.

```plaintext
use "core:args.ec"

val p = args.parser("tool", "Builds things.")
args.flag(p, "verbose", "Say more", 'v')
args.option(p, "output", "Where to write", 'o', "FILE", "out.txt")   // with a default
val build = args.command(p, "build", "Build a target")
args.positional(build, "target", "What to build")
args.requiredOption(build, "level", "How much", 'l', "N")
val r = args.run(p)                              // --help or a mistake ends the program here

io.log(args.times(r, "verbose"))                 // 2 for -vv; args.isSet tells if it was given
io.log(args.value(r, "output") ?? "")
if val sub = r.sub do                            // what follows the command in r.command
    io.log(args.value(sub, "target") ?? "")
```

A mistake gets an answer such as `tool: unknown option '--verbos'. Did you mean '--verbose'?`, with exit code 2. `args.parse(p, words)` does the same work, but gives the error to the program.
