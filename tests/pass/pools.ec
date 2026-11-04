use "core:lib.ec"

// Pools: the current pool, `pool do` blocks, `new`, `new T`, `new T[n]`, `clone`, and Strings built without naming a pool.

rec Point as
    x: Int
    y: Int

rec Node as
    value: Int
    name: String
    next: @Node?

rec Animal as
    name: String

rec Cat extends Animal as
    lives: Int

// Memory from `new` outlives the function: it is in the caller's pool.
fn makePoint(x: Int, y: Int): @Point = new Point { x = x, y = y }

// Values can be built in a pool the caller gives.
fn label(p: Pool, n: Int): String = format(p, "item ${n}")

fn list(n: Int): @Node? do
    var head: @Node? = null
    var i = 0
    while i < n do
        head = new Node { value = i, name = "node ${i}", next = head }
        i += 1
    ret head

fn sum(node: @Node?): Int do
    ret 0 if node == null
    ret node.value + sum(node.next)

fn parse(n: Int): Int throws Error do
    throw Error { code = 1, message = "bad number ${n}" } if n < 0
    ret n

// An error passed on from inside a `pool` block is copied out before the block's pool is freed.
fn checked(n: Int): Int throws Error do
    pool do
        ret try parse(n)

fn count(): Int do
    var total = 0
    pool do
        // Freed when the block ends, also on this early return.
        val scratch = new Int[100]
        scratch[5] = 7
        ret scratch[5] if scratch.len == 100
        total = -1
    ret total

fn main(): Int do
    val p = makePoint(3, 4)
    log(p.x + p.y)

    val empty = new Point
    log(empty.x)
    val n = new 41
    @n += 1
    log(@n)

    val name = "Ada"
    val greeting = "Hello, ${name}!"
    log(greeting)
    log(concat(greeting, " Bye."))
    log(format("${1} + ${1} = ${2}"))

    val counts = new Int[3]
    counts[1] = 5
    log(counts.len)
    log(counts[0] + counts[1])

    val nodes = list(4)
    log(sum(nodes))
    log(nodes!.name)

    // A deep copy shares nothing with the original.
    val copy = clone(nodes)
    copy!.next!.value = 100
    log(sum(nodes))
    log(sum(copy))
    log(copy!.next!.name)

    val arr = clone([1, 2, 3])
    arr[0] = 10
    log(arr[0] + arr[2])

    // A pointer to an Animal that is a Cat is copied as a Cat.
    val cat: @Animal = new Cat { name = "Tom", lives = 9 }
    val catCopy = clone(cat)
    if catCopy is @Cat do
        log(catCopy.lives)

    var kept: String = "nothing"
    val outer = pool
    pool scratch do
        val temp = "temporary ${42}"
        log(temp)
        kept = clone(temp, outer)
        pool do
            val inner = new Point { x = 1, y = 2 } in scratch
            log(inner.y)
    log(kept)

    log(count())
    try checked(-5) catch e do
        log(e.message)

    val own = newPool()
    val s = label(own, 7)
    log(s)
    clearPool(own)
    freePool(own)
    ret 0
