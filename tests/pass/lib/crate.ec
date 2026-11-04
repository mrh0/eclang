// A generic record in a module, for generic-module.ec.
module "crate"

priv rec Label as
    text: String

priv rec Hidden<T> as
    value: T

rec Crate<T> as
    item: T
    label: Label

priv fn tag(text: String): Label = Label { text }

fn pack(item: <T>, text: String): Crate<T> = Crate<T> { item, tag(text) }

fn labelOf(c: Crate<T>): String = c.label.text

fn hiddenValue(x: <T>): <T> do
    val h = Hidden<T> { x }
    ret h.value
