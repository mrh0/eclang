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

## Goals

- Full interoperability with C.
- Union types, duck-typing, interfaces.
- Generating human-readable C source.
- Function overloads, functional paradigms.
- Null safety.
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
`run` compiles the program, builds it with `gcc` (or `$CC`) using the APR flags from `apr-1-config` and `-lm`, runs it, and exits with its exit code. It takes `-nb`, `-d`, and `-k <dir>` to keep the generated `.c` file and the executable.

### Compile generated .c source with gcc
```
gcc out.c -o out -I/usr/include/apr-1.0 -L/usr/lib -lapr-1 -lm
```

The Makefile wraps both steps:
```
make compile FILE=./examples/fizzbuzz.ec
make compile-run FILE=./examples/fizzbuzz.ec
```

### Run the tests
```
tests/run.sh
```
Programs in [tests/pass](tests/pass) must compile, build and print their `.out` file; programs in [tests/fail](tests/fail) must be rejected with the error on their first line.

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
Code sees the names of its own module and of the top level (such as `log` from `core:lib.ec`). Names from other modules are written with the module name in front: functions, values, types, record literals and function references.
```plaintext
use "geo.ec"

fn main(): Int do
    val a: geo.Point = geo.make(3, 4)
    val b = geo.Point { x = 1, y = 2 }
    val f = fn geo.make
    log(geo.origin.x)
    ret 0
```
Two modules can define the same name without clashing. A module may add overloads to a top-level function (`fn log(p: Point)` in geo); inside the module both are visible, and on a tie its own function wins. In C, a module's definitions are prefixed with its name: `geo__make_0`, `geo__Point`.

Include a C header or source file with the `use` keyword. It is emitted as an `#include`.
```plaintext
use "stdio.h"
```

Import another `.ec` file. A plain path is looked up next to the file that has the `use`, then in the working directory. The `core:` prefix loads files bundled with the compiler (from [src/main/resources](src/main/resources)). Each file is loaded once, however many files use it.
```plaintext
use "core:lib.ec"
use "core:apr/pools.ec"
use "lib.ec"
```

Bundled libraries:
- [lib.ec](src/main/resources/lib.ec): a minimal standard library (`log`, `printf`, common headers).
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

`s[i]` is the byte at `i` (a `Char`), and `s[a..<b]` (or `s[a..=b]`, including `b`) is the part from `a` to `b`. A slice shares the String's memory, so it costs nothing. Both check their bounds (see [Arrays](#arrays-and-c-arrays)). A String's bytes can't be changed in place.

Building a new String needs memory, which comes from the current pool (see [Memory Pools](#memory-pools)). A String with `${...}` fills in values, and `concat` joins Strings:

```plaintext
val s = "${name} is ${age} years old"
val path = concat(dir, "/", file)
val kept = format(p, "${name}")         // in pool p instead; also concat(p, a, b)
```

`${...}` takes numbers (`%g` for Float and Double), Bools, Chars, atoms, Strings and CStrings; for anything else, put in a field or convert it first. The expression inside can't contain a string literal. Write `\$` for a plain dollar sign. `format` and `concat` are built in, but a function of your own with either name takes precedence. In C, `${...}` becomes a small generated function calling `snprintf` and allocating in the pool.

### Arrays and C-Arrays

```plaintext
val array1 = ["One", "Two", "Three"]
val array2: Int[] = [5, 3, 6, 0]
log(array1.len)

val carray1 = ["One", "Two", "Three"]c
val carray2: Int[]c = [5, 3, 6, 0]c
```

`a[i]` on an array (or String) checks that `i` is less than `a.len`; otherwise the program stops with `file:line: index 10 is out of bounds for length 3`. To skip the check where speed matters, index the raw data instead: `a.data[i]` (a C array, which has no length). The `--no-bounds-check` option turns all the checks off.

`a[from..<to]` and `a[from..=to]` slice an array, sharing its elements: changing an element of the slice changes the array. C arrays have no length, so they can't be sliced.

### Atoms

Atoms are named symbols, prefixed with `:`.

```plaintext
val k = :test_atom
```

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

`%` is the remainder of two whole numbers. For Floats and Doubles, call `fmod(a, b)` from core:lib.ec.

`a ** b` on two whole numbers is a whole number of their common type, computed exactly (`2 ** 10` is 1024, a Long stays a Long); a negative exponent gives 0, except for a base of 1 or -1. On other numbers it is a `Double`, computed by C's `pow`, so link with `-lm`.

#### Bitwise Operators
`&` `|` `^` `~` `<<` `>>`

They work on whole numbers. `&`, `|` and `^` follow the same type rules as `+`; a shift has the type of its left side, and shifting by as many bits as the type has (or more) is an error. Unlike in C, `flags & MASK != 0` means `(flags & MASK) != 0`.

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

#### Nullish Coalescing Operator
`??` gives the left side, or the right side when the left side is null (see [Nullable Types](#nullable-types)). On a call that can fail it gives the call's value, or the right side when the call failed (see [Error Handling](#error-handling)). The right side is only evaluated when it is needed.

#### Safe Access
`x?.name` reads a field of a nullable value: it is null when `x` is null, and the field otherwise, so its type is nullable. Chains stop at the first null.

```plaintext
fn nextValue(node: @Node?): Int = node?.next?.value ?? 0
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
`a..<b` goes from a up to, but not including, b, and `a..=b` includes b. Ranges count in a `for` loop (`for i in 0..<n do`, see [For](#for)) and slice arrays and Strings (`s[1..<3]`).

#### Pipe
`x |> f(2)` passes x to the next call as its first argument: it is `f(x, 2)`, and `x |> f` is `f(x)`. Where the value should go somewhere else, write `it`: `x |> g(1, it)`, `x |> it * it`. A chain runs left to right, so `5 |> double |> add(1)` is `add(double(5), 1)`. The value before each `|>` is evaluated once: when `it` appears more than once, the compiler stores it in a temporary first. A stage can also be a lambda: `x |> fn (v: Int) = v - 1`.

#### Type Tests
`x is T` and `x !is T` test what a union value holds, or what kind of record a pointer points to (see [Union Types](#union-types)).

#### Not Yet Implemented
These are parsed, but the compiler reports that they are not supported yet:
- The range `..>`
- `yield`, a trailing `while`, `unit`, `anon` unions, type parameters `Name<T>`, and `var`/`val`/`vol` on parameters

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

The return type may be omitted for functions that return nothing. A function that returns something must end with `ret` on every path.

### Hello World

```plaintext
use "core:lib.ec"

fn main(): Int do
    log("Hello World")
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

### Overloading

```plaintext
fn x(a: Int) do
    log("Int"c)

fn x(a: CString) do
    log("Str"c)
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
log(apply(fn (x) = x * 3, 7))
```

A lambda becomes an ordinary C function (`main_0_lambda0`), and its value is a pointer to it. It can't use the local variables or parameters of the function around it, since lambdas don't capture variables yet. Pass what it needs as a parameter, or use a global. `try` can't be used in a lambda.

## External Declarations

`declare` makes C functions, variables, types and structs visible to eclang without generating code for them.

### Functions

```plaintext
declare fn name extern "c_name" as (parameter_list): return_type
```

`...` at the end of the parameter list accepts any number of extra arguments of any type, like C's `printf`:

```plaintext
declare fn printf as (format: CString, ...): Int
```

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

A record may refer to records declared later, and to itself through a pointer:

```plaintext
rec Node as
    value: Int
    next: @Node?
```

It can't contain itself directly (`next: Node`, or through another record or a union): that would make it infinitely large.

### Extending Records

A record may extend another record. It gets the parent's fields first, then its own.

```plaintext
rec Animal as
    name: CString

rec Cat extends Animal as
    lives: Int

val tom = Cat { name = "Tom"c, lives = 9 }
```

A `Cat` fits wherever an `Animal` is expected. Through a pointer (`@Cat` → `@Animal`) it is the same value; by value, the `Animal` fields are copied. Going back down is checked at runtime:

```plaintext
var pet: @Animal = addrof tom
if pet is @Cat do
    log(pet.lives)            // pet is a @Cat here
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
    log(user.name)
else do
    log("no such user"c)

if val n = parse(text) do           // parse can fail
    log(n)
```

`assert condition` stops the program with an error naming the file, the line and the condition when the condition is false. `assert condition, "message"` reports the message instead. The code after an `assert` knows that the condition holds (see [Flow Typing](#flow-typing)).
```plaintext
assert count > 0
assert node != null, "the list is empty"
log(node.value)                      // node is not null here
```

Tail if, for any statement:
```plaintext
log("positive"c) if x > 0
ret 0 if done
break if i > 10
continue if i % 2 == 0
```

### While

```plaintext
var iter = 5
while !!iter do
    log("I"c)
    iter = iter - 1
```

A loop (`while` or `for`) may have an `else` block. It runs when the loop ends normally, but not when it is left with `break`, which makes "search, otherwise ..." loops simple:

```plaintext
for value in values do
    if value == wanted do
        log("found"c)
        break
else do
    log("not found"c)
```

Inside a `while` loop's `else` block, the condition is known to be false (see [Flow Typing](#flow-typing)). A `break` inside the `else` block belongs to an enclosing loop.

### For

Loop over the elements of an array, or the Chars of a String:
```plaintext
for name in names do
    log(name)
```

Name the index (a `Size`) too with `for index, value in`:
```plaintext
for i, name in names do
    log(i)
    log(name)
```

Count with a range: `..<` stops before the end, `..=` includes it.
```plaintext
for i in 0..<10 do          // 0 to 9
    log(i)
for i in 1..=10 do          // 1 to 10
    log(i)
```

`where` skips the rounds where its condition is false. The body knows the condition holds:
```plaintext
for m in maybeValues where m != null do
    log(m + 1)                // m is not null here
```

`break` and `continue` end or skip one round of the innermost loop.

## Error Handling

Functions declare the error they can fail with using `throws`. Errors are returned by value, not with exceptions, so nothing is allocated. Any record can be an error type; [lib.ec](src/main/resources/lib.ec) provides `Error` (`code: Int`, `message: String`).

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
    log(error.message)
```

Ignoring the error is a compile error:

```
error: The error from throwsTest(...) is ignored: it can fail with Error. Handle the error with `try`, `?? default` or `try ... catch e do`.
```

If `main` can fail and does, the program prints an error and exits with status 1.

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

### Type Aliases

```plaintext
declare type DefinedType as Int
```

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

### Union Types

A union type `A | B` holds a value of any of its member types. As a variable type, it is a tagged union in C (`struct { int tag; union { ... } }`); test what it holds with `is`, as shown in [Flow Typing](#flow-typing). When used as a parameter, a function overload is generated for each member type.

```plaintext
fn y(a: Int | CString) do
    x(a)
```

Unions of atoms restrict a value to a set of symbols:

```plaintext
fn a(atom: :log | :dont_log) do
    log(atom) if atom == :log
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
    log(bird.name)
```

### Generic Types

Each call compiles a copy of the function for the argument types:

```plaintext
fn quack(bird: <B>) do
    log(bird)

quack("Hello"c)
quack(420)

fn first(values: <T>[]): <T> = values[0]
```

`Any`, interfaces and `<T>` have no fixed size in C, so they can only be parameter types (and, for `<T>`, return types). `@Any` is an untyped pointer (`void*`) and can be stored.

### Function Types

A function type `(A, B) => R` (or `(A, B) => R throws E`) is the type of a function value: a parameter, variable or record field holding a function, made with `fn name` (see [Function References](#function-references)). It is called like a function. In C it is a function pointer.

```plaintext
fn double(x: Int): Int = x * 2
fn apply(func: (Int) => Int, value: Int): Int = func(value)

rec Button as
    onClick: (Int) => Int

fn main(): Int do
    log(apply(fn double, 5))         // 10
    val b = Button { onClick = fn double }
    log(b.onClick(4))                // 8
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
    log(who.name)

greet(Duck { "Donald"c, 9 })
```

A pointer parameter works the same way: `@Named` accepts a pointer to any record with a `name: CString`, so the function can change it.

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
    defer log("1"c)
    log("2"c)
    if true do
        defer log("3"c)
        log("4"c)
    else
        defer log("5"c)
        log("test"c)
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
        log("1"c)
        log("2"c)
    log("3"c)
```

### Memory Pools

eclang allocates memory in pools (from APR, the Apache Portable Runtime) instead of freeing values one by one: everything in a pool is freed at once when the pool is freed. Pools need no `use`; the type of a pool is `Pool`.

There is always a **current pool**, called `pool`. When the program starts it is a pool that lives until the program ends. A `pool do` block makes a new pool, a child of the current one, and that pool is current inside the block. The pool and everything in it are freed when the block ends, however it ends (at its end, or by `ret`, `break`, `continue` or `try`):

```plaintext
fn main(): Int do
    for i in 0..<1000 do
        pool do                          // freed at the end of every round
            val line = "line ${i}"
            log(line)
    ret 0
```

Strings with `${...}`, `concat`, `new` and `clone` allocate in the current pool, and so do the functions they're called from: a function that returns `new Point { ... }` allocates in its caller's current pool. Give a value its own pool by passing a `Pool` (`fn load(p: Pool): @Config`), and allocate in it with `new ... in p`, `clone(x, p)`, `format(p, "...")` and `concat(p, a, b)`.

`pool name do` names the block's pool, so code in a nested block can still allocate in it:

```plaintext
pool results do
    pool do                              // a scratch pool for the work
        val point = new Point { x = 1, y = 2 } in results
```

A pool that isn't tied to a block is made with `newPool()` (a child of the current pool, freed with it at the latest) or `newPool(parent)`. `clearPool(p)` frees everything in `p` and keeps the pool, and `freePool(p)` frees it. The pool of a `pool do` block, and the current pool, are freed for you, so `freePool` rejects them.

`Pool` is `@apr_pool_t`, so pools can be passed to the APR functions in [apr/](src/main/resources/apr) (`apr_psprintf(pool, "%d items"c, 3)`, with `use "core:apr/strings.ec"`). In C, the current pool is a thread-local `__ec_pool` that the generated `main` creates. A thread started from C code has no current pool until its code opens a `pool do` block. Running out of memory stops the program with an error.

### New

`new value` copies a value into the current pool and gives a pointer to the copy, which lives as long as the pool. `new T` points to an empty T (see [Variables](#variables)), and `new T[n]` makes an array of n empty elements. Add `in p` to use pool p:

```plaintext
fn makePoint(x: Int, y: Int): @Point = new Point { x = x, y = y }

val counter = new 0                     // @Int
val empty = new Point                   // @Point, x and y are 0
val buffer = new Char[size] in p        // Char[], in pool p
```

### Clone

`clone(x)` (or `x.clone()`) makes a deep copy of x in the current pool, and `clone(x, p)` makes one in pool p. Strings, CStrings and arrays are copied, and what a pointer points to is copied too, recursively, so the copy shares no memory with the original. A pointer to a record that is extended copies the record it really points to (`@Animal` holding a `Cat` copies the Cat). What `clone` can't copy is kept as it is: C arrays (`T[]c`, which have no length), `@Any`, function values, and pointers to C types whose fields eclang doesn't know (such as `Pool`). A value that points back to itself (a cycle) can't be cloned.

```plaintext
val copy = clone(list)                  // a @Node? with every node and name copied
val numbers = clone([1, 2, 3])          // an array literal copied out of the stack
```

### Lifetimes

The compiler rejects pointers that would outlive the memory they point to. That memory is a local variable or parameter (freed when its block ends), the elements of an array literal (stored on the stack), or a `pool do` block's pool, together with whatever was allocated in it, including by the functions called inside the block. A value pointing into such memory can't be returned or thrown, and can't be stored in a variable defined outside its block, in a global, or behind a pointer:

```plaintext
fn leak(): @Int do
    var x = 42
    ret addrof x            // error: it points to 'x', a local variable, ... `new x` is a copy of it in the current pool

fn numbers(): Int[] = [1, 2, 3]          // error: ... Copy it into a pool with `clone(...)`

var name = "none"
pool do
    name = "n is ${n}"      // error: ... 'name' lives longer
```

Passing such pointers to functions is fine (`fill(addrof(info))`). The compiler follows values through variables, record and array literals, `new`, and calls that are given a pool. It doesn't follow pointers read back out of memory (`p.next`, `a[i]`, `@p`), or a pointer a function returns from among its arguments. `try` inside a `pool do` block copies the error into the pool that was current before the block, because leaving the block frees its pool.

### APR Bindings

The APR bindings declare APR's functions with eclang types. Functions that return `NULL` when nothing is found (`apr_table_get`, `apr_hash_get`, `apr_hash_next`, ...) return nullable types. C `const` has no eclang equivalent: a `CString` is passed where C wants a `const char*`, and the compiler adds the casts C needs for pointers to pointers and function pointers. `tests/run.sh` checks every binding against the APR headers ([tools/check-bindings.py](tools/check-bindings.py)).
