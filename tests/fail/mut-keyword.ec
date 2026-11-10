// error: `inout` can't go before a parameter. These can: `mut`

declare fn f as (inout x: Int): Int

fn main() do
    val a = f(1)
