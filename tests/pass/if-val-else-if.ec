use "core:io.ec"

fn half(n: Int): Int? do
    ret null if n % 2 == 1
    ret n / 2

fn third(n: Int): Int? do
    ret null if n % 3 != 0
    ret n / 3

fn describe(n: Int): String do
    if val h = half(n) do
        ret "half ${h}"
    else if n == 7 do
        ret "seven"
    else if val t = third(n) do
        ret "third ${t}"
    else do
        ret "none"

fn plainFirst(n: Int) do
    if n == 0 do
        io.log("zero")
    else if n == 1 do
        io.log("one")
    else if val h = half(n) do
        io.log("half ${h}")
    else
        io.log("odd")

// Every path leaves, so no `ret` is missing after the chain.
fn leaves(n: Int): Int do
    if val h = half(n) do
        ret h
    else if val t = third(n) do
        ret t
    else do
        ret 0

fn main() do
    io.log(describe(4))
    io.log(describe(7))
    io.log(describe(9))
    io.log(describe(11))
    plainFirst(0)
    plainFirst(1)
    plainFirst(6)
    plainFirst(5)
    io.log(leaves(9))
