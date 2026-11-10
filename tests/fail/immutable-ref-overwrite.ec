// error: `@` of a Counter is the record itself, which everyone who refers to it shares, and Counter isn't a `mut ref rec`
ref rec Counter as
    count: Int

fn main() do
    val c = new Counter { 0 }
    @c = Counter { 1 }
