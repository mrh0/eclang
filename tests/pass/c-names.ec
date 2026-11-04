use "core:io.ec"

// Names that are also C names: generated functions (log_6), C library names (FILENAME_MAX, strcmp, printf, SEEK_END).
val FILENAME_MAX = 3
const SEEK_END = 7

fn show(printf: Int) do
    io.log(printf)

fn main(): Int do
    val log_6 = 2
    io.log(log_6)
    io.log(FILENAME_MAX)
    io.log(SEEK_END)
    var strcmp = "a"c
    if strcmp == "a"c do
        io.log("same"c)
    for index in 0..<1 do
        io.log(index)
    show(5)
    ret 0
