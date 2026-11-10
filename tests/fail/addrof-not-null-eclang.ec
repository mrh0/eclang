// error: take the address of a copy. 'keep' is an eclang function.
rec P as
    x: Int

var kept: @@P? = null

fn keep(pp: @@P) do
    kept = pp

fn main() do
    var p: @P? = null
    keep(addrof p!)
