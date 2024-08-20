external fn log(value: String): None as "printf"

rec Test as
    a: Int
    b: Int

fn main(): Int do
    log "Hello World"
    test(0, "")
    y("")
    ret 0

fn test(a: Int | String, b: Int | String, c: Int = 5): Int do
    ret c

fn x(a: Int) do
    log "Int"

fn x(a: String) do
    log "Str"

fn y(a: Int | String) do
    x(a)