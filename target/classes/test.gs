external fn log(value: String): None as "printf"

record Test as
    a: Int
    b: Int

fn main(): Int do
    log "Hello World"
    ret 0

fn test(a: Int | String, b: Int | String, c: Int): Int do
    ret 1