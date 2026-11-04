// error: :c is missing. Add `:c do`, or an `else do` arm for the rest.
use "core:io.ec"

fn main(): Int do
    val m: :a | :b | :c = :a
    when m do
        :a do io.log(1)
        :b do io.log(2)
    ret 0
