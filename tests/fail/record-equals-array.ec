// error: `==` can't compare Bag records: field `items`: a Int[] can't be compared with `==` yet.
rec Bag as
    items: Int[]

fn main(): Int do
    val a = Bag { items = [1] }
    ret if a == a do 1 else 0
