external fn log(value: String): None as "printf"

fn main(): Int do
    log "Hello World"
    ret 0

fn test(a: Int | String): Int do
    ret 1