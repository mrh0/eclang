// error: `addrof x!` points at x as if it could never be null, which is only safe for a C function (`declare fn`) that fills x in
rec P as
    x: Int

fn main() do
    var p: @P? = null
    val a = addrof p!               // @@P, but p is null
    p = null
