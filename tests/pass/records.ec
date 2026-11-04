use "core:io.ec"

rec Animal as
    name: CString
    legs: Int

rec Cat extends Animal as
    lives: Int

rec Dog extends Animal as
    good: Bool

// Records may refer to records declared later, and to themselves through a pointer.
rec Node as
    value: Int
    next: @Node?

inter Named as
    name: CString

fn greet(who: Named) do
    io.log(who.name)

fn legs(a: @Animal): Int = a.legs

fn first(values: <T>[]): <T> = values[0]

fn sum(list: @Node?): Int do
    var total = 0
    var at = list
    while at != null do
        total = total + at.value
        at = at.next
    ret total

fn main(): Int do
    val tom = Cat { name = "Tom"c, legs = 4, lives = 9 }
    val rex: Dog = { "Rex"c, 4, true }
    io.log(tom.name)
    io.log(tom.lives)
    // Interfaces: any record with a `name: CString` field.
    greet(tom)
    greet(rex)
    // A child fits where its parent is expected (by pointer, the C layout is shared).
    io.log(legs(addrof tom))
    var pet: @Animal = addrof tom
    if pet is @Cat do
        io.log("pet is a cat"c)
    if pet is @Dog do
        io.log("pet is a dog"c)
    val cat = pet as @Cat
    io.log(cat.lives)
    // A child copied into a parent keeps the parent's fields.
    val copy: Animal = tom
    io.log(copy.name)
    // Generics: one copy of `first` per element type.
    io.log(first([3, 2, 1]))
    io.log(first(["a"c, "b"c]))
    // A linked list with nullable pointers.
    var c = Node { 3, null }
    var b = Node { 2, addrof c }
    var a = Node { 1, addrof b }
    io.log(sum(addrof a))
    io.log(sum(null))
    ret 0
