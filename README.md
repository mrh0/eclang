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
- Duck-typing, interfaces.
- Generating human-readable C source.
- Function overrides, Functional paradigms.
- Null safety.
- Pool memory management, defer, deep-clone, APR (Apache Portable Runtime).
- Modernized syntax (Inspired by languages such as Python, Kotlin, Go, Elixir).
- Error handling.

## Compile

```
gcc out.c -o out -I/usr/include/apr-1.0 -L/usr/lib -lapr-1
```

## Imports and Standard Library

Include a c header or source file with the `use` keyword.
```
use "stdio.h"
```
or ec files
```
use "lib.ec"
```

See: [lib.ec](https://github.com/mrh0/eclang/blob/main/src/main/resources/lib.ec)

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
The generated C code:
```
int main_0(void) {
    printf("Hello World");
    return 0;
}
```

#### Function with Multiple Parameters and Default Values

```plaintext
fn test(a: Int, b: Int & CString, c: Int = a): Int do
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

### Operators


#### Logical Operators
`not` `!` `!!` `and` `&&` `or` `||` `xor` `^^`

#### Arithmetic Operators
`+` `-` `*` `/` `%` `**`

#### Compare Operators
`==` `!=` `===` `!==` `<` `>` `<=` `>=`

```plaintext
val a: CString = "Hello"
val b: CString = "Hello"
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

#### Binary Operators (Not Yet Implemented)
`<<` `>>` `<<<` `>>>` `&` `|` `^`

#### Nullish Coalescing Operators
`??`

#### Function Overloading

```plaintext
fn x(a: Int) do
    log "Int"

fn x(a: CString) do
    log "Str"
```

Here, the function `x` is overloaded to handle both `Int` and `CString` types.

The first argument of a function can be used as a self, and called with the following syntax:

```plaintext
10.x()
val a: Int = 12
a.x()
```

## Conditionals

### Syntax

```plaintext
if condition do
    // code block
else if condition do
    // code block
else
    // code block
```

### Example

```plaintext
if true do
    log "Test1"
else if false do
    log "Test2"
else
    log "Test3"
```


## External Functions

External functions allow you to interface with functions outside of the language's core syntax, such as C library functions.

### Syntax

```plaintext
declare fn "external_name" as alias_name(parameter_list): return_type
```

### Examples

```plaintext
declare fn "printf" as log(value: CString): None
declare fn "printf" as log(format: CString, value: String): None
```

In the example above:
- `log(value: CString): None` logs a string without formatting.
- `log(format: CString, value: CString): None` logs a formatted string.

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

### Type Casting
Safe casting
```plaintext
val intValue: Int = 56
val longValue: Long = intValue as Long
```
Unsafe casting allows you to cast any type to any other type without restriction
```plaintext
val intValue: Int = 56
val pointer: @Int = intValue as unsafe @Int
```

### Define Custom Types

Define custom named types:

```plaintext
declare type DefinedType = Int
```

### Pointer Types

Pointer types are defined with the at sign `@`. Addresses are given by the `addrof` keyword.

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

Nullable types allow a variable to have either a value of the specified type or be `Null`.
NullishCoalescing operator `??` can be used to transform a null type with a default value.

```plaintext
fn n(a: CString?): CString do
    ret a ?? ""
```
The above example generates the following C code.
```c
char* n_0(char* a) {
	return (char*)__ec_nc(a, "");
}
```
Or ignore the nullable type with a `!` suffix.
```plaintext
fn n(a: CString?): CString do
    ret a!
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

### Duck Typing

```plaintext
rec Duck as
    name: CString
    coolness: Int
    
rec Goose as
    name: CString
    loudness: Int

fn quack(bird: Duck & Goose) do
    log bird.name
```

### Interfaces (Not Yet Implemented)

```plaintext
inter ExampleInterface as
    a: Int
    b: Int

rec ExampleRecord impl ExampleInterface as
    a: Int
    b: Int
    c: CString
```

---

## Memory management

### Defer keyword

Deferring can be used to clean up memory at the end of scope

```plaintext
fn defTest(input: DefinedType): Int do
    defer log("1")
    log("2")
    if true do
        defer log("3")
        log("4")
    else
        defer log("5")
        log("test")
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
		printf("1");
		printf("5");
		return __ec_ret;
	}
	int __ec_ret = input;
	printf("1");
	return __ec_ret;
}
```

### Memory Pools

The global variable `ROOT_POOL: @Pool?` is intended to be the default pool to use when no pool is specified when invoking a function with takes a Pool as an optional.
`ROOT_POOL` is not initialized by default so your program needs to do it at the start of your main function or always provide a user defined Pool.
```plaintext
fn main(): Int do
    createPool(addrof(ROOT_POOL!), NULL)
    defer freePool(ROOT_POOL!)
    log ("%s", "Cloned String".clone())
```
Where clone from [lib.ec](https://github.com/mrh0/eclang/blob/main/src/main/resources/lib.ec) is implemented as such:
```plaintext
fn clone(str: CString, pool: @Pool?): CString do
    val n = str.lengthOf() // string.h strlen
    val newStr = alloc(pool ?? ROOT_POOL, sizeof(CString) * (n+1) as Size) as unsafe CString
    copy(newStr, str, n) // string.h strncpy
    ret newStr

fn clone(str: CString): CString = clone(str, NULL)
```
