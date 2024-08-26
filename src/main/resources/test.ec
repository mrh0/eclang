external fn "printf" as log(value: String): None

rec Test as
    a: Int
    b: Int

fn main(): Int do
    log "Hello World"
    test(0, "")
    y("")
    z(1)
    if true do
        log "Test"
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

fn z(a: Int): Int = a + 1