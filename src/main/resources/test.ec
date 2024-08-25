external fn log(value: String): None as "printf"

rec Test as
    a: Int
    b: Int

fn main(): Int do
    log "Hello World"
    test(0, "")
    y("")
    ret 0

fn test(a: Int, b: Int | String, c: Int = a): Int do
    val d: Int = 5
    ret c

fn x(a: Int) do
    log "Int"

fn x(a: String) do
    log "Str"

fn y(a: Int | String) do
    x(a)