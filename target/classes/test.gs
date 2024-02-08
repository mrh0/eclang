use * from "Standard"
use abs from "Math"

fn main(a: Int): Int do
    log dub(6, 8)
    log "Hello World"
    log abs(-5)
    log 5.55f
    log 0xff
    log 0xAA
    log 0b1110
    log 1&1
    //log 1&(2&3)
    log if (true) 1 else 2
    log :test
    log :test == :TEST

    var i = 0
    while i != 1_000_000_000 do
        i = i + 1
    log i

fn dub(a: Int): Int do
    ret a+a

fn dub(a: Int, b: Int): Int do
    ret a+b
