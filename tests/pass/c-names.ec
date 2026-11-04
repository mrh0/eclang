use "core:lib.ec"

// Names that are also C names: generated functions (log_6), C library names (stdout, strcmp, printf, EOF).
val stdout = 3
const EOF = 7

fn show(printf: Int) do
    log(printf)

fn main(): Int do
    val log_6 = 2
    log(log_6)
    log(stdout)
    log(EOF)
    var strcmp = "a"c
    if strcmp == "a"c do
        log("same"c)
    for index in 0..<1 do
        log(index)
    show(5)
    ret 0
