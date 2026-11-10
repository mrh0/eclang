// A pointer field of a union is written freely, but read only with `unsafe`: the union may hold another field.
use "core:io.ec"

mut union Slot as
    n: Long
    p: @Int

mut rec Tagged as
    isPointer: Bool
    slot: Slot

fn show(t: Tagged): String do
    if t.isPointer do
        ret "pointer to ${@(unsafe t.slot.p)}"
    ret "number ${t.slot.n}"

fn main() do
    var x = 41
    var a = Tagged { false, Slot { n = 7 } }
    io.log(show(a))
    a.slot.p = addrof x
    a.isPointer = true
    unsafe do
        @a.slot.p += 1
    io.log(show(a))
