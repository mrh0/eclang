external fn log(value: String): None as "printf"

rec Test as
    a: Int
    b: Int

fn main(): Int do
    log "Hello World"
    test(0, "")
    ret 0

fn test(a: Int | String, b: Int | String, c: Int = 5): Int do
    ret 1

fn test(a: Int): Int do
    ret a