// Built by tests/run.sh with libgreet.a from greet.c on LIBRARY_PATH.
use "greet.h"
use "lib:greet"
use "lib:greet"
use "core:io.ec"

declare fn greet_add as (a: Int, b: Int): Int

fn main() do
    io.log("${greet_add(2, 3)}")
