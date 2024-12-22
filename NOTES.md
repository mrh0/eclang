Error handling

Im building a programming language compiler, it takes my custom syntax and compiles it into standard C code. I want to add error handling, (errors by value similar to GO) how would i go about translating this into C?

Parameters:
- If the function does not throw no memory should be allocated on the heap.
- The return value should never be heap allocated.
- The Error struct may be heap allocated if that is simpler.
- The error thrown may be of any struct type.

My language syntax
```
fn throwsTest(i: Int): Int throws Error do
    if (i > 10) throw Error { "Number is too large" }
    ret i+1
   
fn main(): Int do
    val result = try throwsTest(0) else 10
    
    try throwsTest(0) catch error do
        print(error.message)
    ret 0
```
