// panic: requires-overload.ec:9: requirement of c failed: x < 0
fn c(x: Int): Int requires x > 0 = 1
fn c(x: Int): Int requires x < 0 = -1

fn main(): Int do
  val a = c(1)
  val b = c(-1)
  // Neither fits: the last one fails, reported here.
  ret a + b + c(0)
