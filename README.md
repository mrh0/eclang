# ec-lang

"Easy C Lang"

## Table of Contents

0. [Goals](#goals)
1. [Functions](#functions)
2. [External Functions](#external-functions)
3. [Records](#record-structs)
4. [Conditionals](#conditionals)
5. [Type System](#type-system)

eclang is a system language compiler which generates human-readable C source and aims for full interoperability with C.

## Goals

- Full interoperability with C.
- Generating human-readable C source.
- Function overrides, Functional paradigms.
- Null safety.
- Manual memory management, defer, deep-clone on return, APR (Apache Portable Runtime).
- Modernized syntax (Inspired by languages such as Python, Kotlin, Go, Elixir).
- Error handling.

## Compile

```
gcc out.c -o out -I/usr/include/apr-1.0 -L/usr/lib -lapr-1
```

## Functions

### Syntax

```plaintext
fn function_name(parameter_list): return_type do
    // function body
```

or

```plaintext
fn function_name(parameter_list): return_type = // returning expression
```

Functions may also have overloaded definitions, default parameter values, and type constraints.

### Examples

#### Hello World


```plaintext
fn main(): Int do
    log "Hello World"
    ret 0
```

#### Function with Multiple Parameters and Default Values

```plaintext
fn test(a: Int, b: Int & String, c: Int = a): Int do
    ret c
```

In this example:
- `a` and `b` are mandatory parameters.
- `c` is an optional parameter that defaults to the value of `a` if not provided.
- Creates four overloads: (Int, Int), (Int, String), (Int, Int, Int), (Int, String, Int)

### Example: Function with Simple Arithmetic

```plaintext
fn z(a: Int): Int = a + 1
```

This function returns the input integer `a` incremented by `1`.

#### Function Overloading

```plaintext
fn x(a: Int) do
    log "Int"

fn x(a: String) do
    log "Str"
```

Here, the function `x` is overloaded to handle both `Int` and `String` types.

## Conditionals

### Syntax

```plaintext
if condition do
    // code block
eif condition do
    // code block
else
    // code block
```

### Example

```plaintext
if true do
    log "Test1"
eif false do
    log "Test2"
else
    log "Test3"
```


## External Functions

External functions allow you to interface with functions outside of the language's core syntax, such as C library functions.

### Syntax

```plaintext
ext fn "external_name" as alias_name(parameter_list): return_type
```

### Examples

```plaintext
ext fn "printf" as log(value: String): None
ext fn "printf" as log(format: String, value: String): None
```

In the example above:
- `log(value: String): None` logs a string without formatting.
- `log(format: String, value: String): None` logs a formatted string.

## Record Structs

Records are user-defined data structures that group related variables under a single name.

### Syntax

```plaintext
rec RecordName as
    field1: Type1
    field2: Type2
```

### Example

```plaintext
rec Test as
    a: Int
    b: Int
```

In this example, `Test` is a record with two fields: `a` and `b`, both of type `Int`.

## Type System

### Nullable Types

Nullable types allow a variable to have either a value of the specified type or be `Null`.

```plaintext
fn n(a: Int?): Int? do
    ret a
```

### Example: Type Inference and Atoms

```plaintext
val k = :test_atom

fn a(atom: :log | :dont_log) do
    if atom == :log do log (atom)
```

In this example:
- `k` is inferred to be of type `Atom`.
- The function `a` accepts one of two possible atom type and logs the atom name if the `:log` atom is passed

---
