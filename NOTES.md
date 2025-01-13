Error handling

I'm building a programming language compiler, it takes my custom syntax and compiles it into standard C code. I want to add error handling, (errors by value similar to GO) how would I go about translating this into C?

Parameters:
- If the function does not throw no memory should be allocated on the heap.
- The return value should never be heap allocated.
- The Error struct may be heap allocated if that is simpler.
- The error thrown may be of any struct type.
- Use unions, select the property to use with a long number.

My language syntax
```
struct Error as
    code: Int
    message: String

fn throwsTest(i: Int): Int throws Error do
    if (i > 10) throw Error { 1, "Number is too large" }
    ret i+1
   
fn main(): Int do
    // With default value
    val result = throwsTest(0) ?? 10
    
    try throwsTest(0) catch error do
        print(error.message)
    ret 0
```

```c
typedef struct {
    int code;
    const char *message;
} Error;

// Define a wrapper struct
typedef struct {
    int is_error;  // 0 for success, 1 for error
    union {
        int value;   // Replace `int` with any return type
        Error error;
    };
} ReturnWrapper;

#define SUCCESS(val) (ReturnWrapper){0, .value = (val)}
#define ERROR(code, message) (ReturnWrapper){1, .result.error = (Error){code, message}}
```