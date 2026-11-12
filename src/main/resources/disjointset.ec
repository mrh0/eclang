/**
 * DisjointSet, which keeps numbers (from 0) in groups and joins groups (union-find).
 *
 *     use "core:disjointset.ec"
 *     val groups = disjointSet(5)
 *     groups.union(0, 1)
 *     io.log(groups.connected(0, 1))                  // true
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made, and it grows there. It isn't part of core:structures.ec.
 */
use "core:errors.ec"
use "core:string.ec"

/**
 * The numbers 0 to n - 1 in groups: each starts in a group of its own, {@link union} joins two groups, and
 * {@link connected} says whether two numbers are in the same one. Both take nearly constant time. For connected parts
 * of a graph, or Kruskal's minimum spanning tree.
 *
 *     val g = disjointSet(4)
 *     g.union(0, 1)
 *     g.union(2, 3)
 *     io.log(g.groups())                              // 2
 *     io.log(g.connected(1, 2))                       // false
 */
mut ref rec DisjointSet as
    /** The number each one points to, up to the representative of its group, which points to itself. */
    parent: Size[]
    /** For a representative, how many numbers its group has. */
    sizes: Size[]
    /** How many numbers it holds; {@code parent} has room for more. */
    len: Size
    /** How many groups there are. */
    count: Size
    /** The pool its memory is in. */
    mem: Pool

/** A new {@link DisjointSet} of the numbers 0 to n - 1, each in a group of its own, in the current pool. */
fn disjointSet(n: Size = 0): DisjointSet do
    val room: Size = if n < 8 do 8 else n
    val d = new DisjointSet { new Size[room], new Size[room], 0, 0, pool }
    for _ in 0..<n do
        add(d)
    ret d

/** Adds the next number, in a group of its own. @return the number */
fn add(d: DisjointSet): Size do
    if d.len == d.parent.len do
        val parent = new Size[d.len * 2] in d.mem
        val sizes = new Size[d.len * 2] in d.mem
        for i in 0..<d.len do
            parent[i] = d.parent[i]
            sizes[i] = d.sizes[i]
        d.parent = parent
        d.sizes = sizes
    val i = d.len
    d.parent[i] = i
    d.sizes[i] = 1
    d.len += 1
    d.count += 1
    ret i

/** The representative of the group of `x`: the same number for every number in the group. The program stops when it has no `x`. */
fn find(d: DisjointSet, x: Size): Size do
    assert x < d.len, "the number is past the end of the DisjointSet"
    var i = x
    while d.parent[i] != i do
        d.parent[i] = d.parent[d.parent[i]]          // path halving
        i = d.parent[i]
    ret i

/** Joins the groups of `a` and `b`. @return whether they were in different groups */
fn union(d: DisjointSet, a: Size, b: Size): Bool do
    var ra = find(d, a)
    var rb = find(d, b)
    ret false if ra == rb
    if d.sizes[ra] < d.sizes[rb] do
        val t = ra
        ra = rb
        rb = t
    d.parent[rb] = ra
    d.sizes[ra] += d.sizes[rb]
    d.count -= 1
    ret true

/** Whether `a` and `b` are in the same group. */
fn connected(d: DisjointSet, a: Size, b: Size): Bool = find(d, a) == find(d, b)

/** How many numbers the group of `x` has. */
fn groupSize(d: DisjointSet, x: Size): Size = d.sizes[find(d, x)]

/** How many groups there are. */
fn groups(d: DisjointSet): Size = d.count

/** How many numbers it holds. */
fn size(d: DisjointSet): Size = d.len

/** Whether it holds no numbers. */
fn isEmpty(d: DisjointSet): Bool = d.len == 0

/** A new array of the groups, each its numbers from the smallest, ordered by their smallest number, in the current pool. */
fn toArray(d: DisjointSet): Size[][] do
    val slot = new Size[d.len]                       // for a representative, 1 + the index of its group in out
    val out = new (Size[])[d.count]
    val filled = new Size[d.count]
    var n: Size = 0
    for i in 0..<d.len do
        val r = find(d, i)
        if slot[r] == 0 do
            out[n] = new Size[d.sizes[r]]
            n += 1
            slot[r] = n
        val g = slot[r] - 1
        out[g][filled[g]] = i
        filled[g] += 1
    ret out
