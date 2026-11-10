module "countdown"

/** Its `next` and `iter` are only in this module, and `for` finds them from anywhere. */
mut rec Steps as
    left: Int

fn from(n: Int): Steps = Steps { n }

fn next(s: @Steps): Int? do
    ret null if s.left == 0
    s.left -= 1
    ret s.left + 1

rec Shelf as
    size: Int

fn shelf(n: Int): Shelf = Shelf { n }

fn iter(s: Shelf): Steps = Steps { s.size }
