// `try f()` as a statement: the value is discarded, the error is still passed on.
use "core:errors.ec"
use "core:io.ec"

fn value(i: Int): Int throws Error do
    throw Error { 1, "too large" } if i > 10
    io.log("value ${i}")
    ret i

fn nothing(i: Int) throws Error do
    throw Error { 2, "negative" } if i < 0
    io.log("nothing ${i}")

fn plain(i: Int) throws Error do
    try value(i)
    try nothing(i)
    io.log("plain done")

fn tailIf(i: Int, run: Bool) throws Error do
    try value(i) if run
    io.log("tailIf done")

fn deferred(i: Int) throws Error do
    defer io.log("deferred")
    try value(i)
    io.log("deferred done")

fn pooled(i: Int) throws Error do
    pool do
        try value(i)
        io.log("pooled done")

fn main() do
    try plain(1) catch e do
        io.log("unexpected ${e.code}")
    try plain(50) catch e do
        io.log("plain failed ${e.code}")
    try plain(-1) catch e do
        io.log("plain failed ${e.code}")
    try tailIf(1, true) catch e do
        io.log("unexpected ${e.code}")
    try tailIf(50, false) catch e do
        io.log("unexpected ${e.code}")
    try tailIf(50, true) catch e do
        io.log("tailIf failed ${e.code}")
    try deferred(50) catch e do
        io.log("deferred failed ${e.code}")
    try pooled(2) catch e do
        io.log("unexpected ${e.code}")
    try pooled(50) catch e do
        io.log("pooled failed ${e.code}")
