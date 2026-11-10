// error: keep must not hold on: `const assert` failed: 'n' escapes. The function keeps it in a global
rec Node as
    value: Int
var kept: @Node? = null
fn store(n: @Node) do
    kept = n
fn keep(n: @Node) do
    const assert not escapes(n), "keep must not hold on"
    store(n)
fn main(): Int do
    keep(new Node { value = 1 })
    ret 0
