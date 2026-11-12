/**
 * Grid<T>, a two-dimensional array: a value at each (x, y), with width columns and height rows.
 *
 *     use "core:grid.ec"
 *     val board = grid(8, 8, '.')
 *     board.set(3, 4, '#')
 *     io.log(board.get(3, 4))                         // #
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made, and it doesn't grow. What it holds isn't copied: Strings and pointers put in it must live at least as long as
 * it does, which the compiler checks. It isn't part of core:structures.ec.
 */
use "core:errors.ec"
use "core:string.ec"
use "core:array.ec"

/**
 * A value at each (x, y), for x from 0 to width - 1 and y from 0 to height - 1. The values are kept row after row, so
 * {@link row} gives one row as an array without copying it.
 *
 *     val heights = grid(3, 2, 0)
 *     heights.set(2, 1, 5)
 *     for (x, y) in heights.neighbors(1, 1) do       // (1, 0), (0, 1), (2, 1)
 *         io.log("${x},${y}: ${heights.get(x, y)}")
 *
 * @param <T> the type of the values
 */
mut ref rec Grid<T> as
    /** The values, row after row: (x, y) is at y * width + x. */
    cells: T[]
    /** How many columns it has. */
    width: Size
    /** How many rows it has. */
    height: Size

/** A new {@link Grid} with every value `value`, in the current pool: `grid(80, 25, ' ')`. */
fn grid(width: Size, height: Size, value: <T>): Grid<T> do
    val cells = unsafe new (<T>)[width * height]
    for i in 0..<cells.len do
        cells[i] = value
    ret new Grid<T> { cells, width, height }

/** A new {@link Grid} with the value `f(x, y)` at each (x, y), in the current pool. */
fn gridOf(width: Size, height: Size, f: (Size, Size) => <T>): Grid<T> do
    val cells = unsafe new (<T>)[width * height]
    for y in 0..<height do
        for x in 0..<width do
            cells[y * width + x] = f(x, y)
    ret new Grid<T> { cells, width, height }

/** Whether (x, y) is inside the grid. */
fn contains(g: Grid<T>, x: Size, y: Size): Bool = x < g.width and y < g.height

/** Whether (x, y) is inside the grid. Negative coordinates are outside it. */
fn contains(g: Grid<T>, x: Long, y: Long): Bool = x >= 0 and y >= 0 and x < g.width as Long and y < g.height as Long

/** The value at (x, y). The program stops when it is outside the grid. */
fn get(g: Grid<T>, x: Size, y: Size): <T> do
    assert x < g.width and y < g.height, "the position is outside the Grid"
    ret g.cells[y * g.width + x]

/** Replaces the value at (x, y). The program stops when it is outside the grid. */
fn set(g: Grid<T>, x: Size, y: Size, value: <T>) do
    assert x < g.width and y < g.height, "the position is outside the Grid"
    g.cells[y * g.width + x] = value

/** Sets every value to `value`. */
fn fill(g: Grid<T>, value: <T>) do
    for i in 0..<g.cells.len do
        g.cells[i] = value

/** The values of row `y`, from x = 0, as a part of the grid: writing to it writes to the grid. The program stops when there is no such row. */
fn row(g: Grid<T>, y: Size): <T>[] do
    assert y < g.height, "the row is outside the Grid"
    ret g.cells[y * g.width..<(y + 1) * g.width]

/** A new array of the values of column `x`, from y = 0, in the current pool. The program stops when there is no such column. */
fn column(g: Grid<T>, x: Size): <T>[] do
    assert x < g.width, "the column is outside the Grid"
    val out = unsafe new (<T>)[g.height]
    for y in 0..<g.height do
        out[y] = g.cells[y * g.width + x]
    ret out

/** The positions above, left of, right of and below (x, y) that are inside the grid, in that order, in the current pool. */
fn neighbors(g: Grid<T>, x: Size, y: Size): (Size, Size)[] = around(g, x, y, false)

/** The positions of the eight cells around (x, y) that are inside the grid, row by row, in the current pool. */
fn neighbors8(g: Grid<T>, x: Size, y: Size): (Size, Size)[] = around(g, x, y, true)

/** The positions around (x, y) inside the grid: four, or eight with `diagonal`. */
priv fn around(g: Grid<T>, x: Size, y: Size, diagonal: Bool): (Size, Size)[] do
    val found = filled(8, (0 as Size, 0 as Size))     // new ((Size, Size))[8] doesn't parse: see KNOWN_ISSUES.md
    var n: Size = 0
    for dy in 0..<3 do
        for dx in 0..<3 do
            val corner = dx != 1 and dy != 1
            val outside = (dx == 0 and x == 0) or (dy == 0 and y == 0)
            if (dx != 1 or dy != 1) and (diagonal or not corner) and not outside do
                val nx = x + (dx as Size) - 1
                val ny = y + (dy as Size) - 1
                if nx < g.width and ny < g.height do
                    found[n] = (nx, ny)
                    n += 1
    ret found[0..<n]

/** The first position, row by row, whose value `test` is true for, or null when there is none. */
fn find(g: Grid<T>, test: (<T>) => Bool): (Size, Size)? do
    for i in 0..<g.cells.len where test(g.cells[i]) do
        ret (i % g.width, i / g.width)
    ret null

/** How many values `test` is true for. */
fn count(g: Grid<T>, test: (<T>) => Bool): Size do
    var n: Size = 0
    for i in 0..<g.cells.len where test(g.cells[i]) do
        n += 1
    ret n

/** What `for v in g do` loops over: the values, row by row. */
fn iter(g: Grid<T>): <T>[] = g.cells

/** A new {@link Grid} of the same size with `f` of each value, in the current pool. */
fn map(g: Grid<T>, f: (<T>) => <U>): Grid<U> do
    val cells = unsafe new (<U>)[g.cells.len]
    for i in 0..<g.cells.len do
        cells[i] = f(g.cells[i])
    ret new Grid<U> { cells, g.width, g.height }

/** Combines the values into one value, row by row. */
fn fold(g: Grid<T>, initial: <U>, f: (<U>, <T>) => <U>): <U> do
    var result = initial
    for v in g.cells do
        result = f(result, v)
    ret result

/** A new {@link Grid} with the same values, in the current pool. */
fn copy(g: Grid<T>): Grid<T> = map(g, fn (v) = v)
