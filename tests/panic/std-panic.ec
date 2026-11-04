// panic: panic: the configuration is missing
use "core:io.ec"
use "core:errors.ec"

fn main(): Int do
    io.log("before")
    panic("the configuration is missing")
