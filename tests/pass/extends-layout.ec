use "core:io.ec"

// A child record holds its parent as its first member in C (`d.Animal.legs`), so a write through an @Dog is seen
// through an @Animal to the same Dog, also at gcc's -O2 without -fno-strict-aliasing.

mut rec Animal as
    name: String
    legs: Int
    marks: Int[3]
    speak: (Int) => String

mut rec Dog extends Animal as
    good: Bool

// A field named like the parent's C type: the parent member is then `__ec_Dog`.
mut rec Puppy extends Dog as
    Dog: Int

mut rec Base as
    x: Int

mut rec Mid extends Base

mut rec Leaf extends Mid as
    y: Int

rec Kennel as
    animal: @Animal
    dog: @Dog

fn bark(n: Int): String = "woof x${n}"

fn legsOf(a: @Animal): Int = a.legs

fn main() do
    val p = new Puppy { name = "Rex", legs = 4, marks = [1, 2, 3], speak = fn bark, good = true, Dog = 7 }
    val k = clone(Kennel { animal = p, dog = p })
    k.dog.legs = 3
    io.log("${k.animal.legs} ${legsOf(k.dog)}")
    p.marks[1] = 20
    io.log("${p.marks[1]} ${p.Dog} ${p.speak(2)}")

    // By value, two levels up: the copy is an Animal, with the Animal's type tag.
    val a: Animal = @p
    var pet: @Animal = new a
    io.log("${a.name} ${a.marks[2]} ${pet is @Dog}")
    pet = p
    io.log("${pet is @Puppy}")
    if pet is @Dog do
        io.log("${pet.good}")

    val maybe: @Puppy? = p
    io.log("${maybe?.legs ?? 0} ${(maybe?.marks)!}")
    val older = Puppy { ...@p, legs = 5, Dog = 1 }
    io.log("${older.legs} ${older.name} ${older.Dog}")
    io.log("${offsetof(Puppy, name) == offsetof(Animal, name)} ${offsetof(Puppy, Dog) > offsetof(Puppy, good)}")
    var empty: Leaf
    empty.y = 2
    io.log("${empty.x} ${empty.y} ${(addrof empty) as @Base is @Leaf} ${empty}")
