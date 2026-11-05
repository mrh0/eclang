module "closable"

rec Door as
    name: String
    open: Bool

rec Gate extends Door as
    width: Int

fn door(name: String): @Door = new Door { name = name, open = true }
fn gate(name: String, width: Int): @Gate = new Gate { name = name, open = true, width = width }
fn maybe(present: Bool): @Door? do
    ret door("maybe") if present
    ret null

fn close(d: @Door) do
    d.open = false

fn label(d: @Door): String = d.name
fn weight(d: @Door): Int = 1
fn heavy(d: @Door, scale: Int = 2): Int = d.width() * scale
fn width(d: @Door): Int = 10
fn opened(d: @Door): Bool throws String do
    throw "closed" if !d.open
    ret true
fn iterate(d: @Door, f: (String) => None) do
    f(d.name)

// Hides the builtin for Doors only.
fn clone(d: @Door): String = "not a copy"
