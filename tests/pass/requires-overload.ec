// Functions with the same parameter types, told apart by `requires`: a call goes to the first, in declaration order, whose
// `requires` hold, or else to the one without. Each narrows in its body; the place of a failure is the original call.
use "core:io.ec"

fn a(i: Int) requires i > 0 do
  io.log("Hello")

fn a(i: Int) do
  io.log("World")

// The one without `requires` may come first: it is still tried last.
fn b(i: Int) do
  io.log("other")

fn b(i: Int) requires i == 0 do
  io.log("zero")

fn sign(x: Int): String requires x > 0 = "positive"
fn sign(x: Int): String requires x < 0 and x > -100 = "negative"
fn sign(x: Int): String = "zero or far"

fn size(s: String?): Size requires s != null = s.len
fn size(s: String?): Size = 0

// A copy of a union parameter takes part too.
fn show(v: Int | String) do
  io.log("not seven")

fn show(v: Int) requires v == 7 do
  io.log("seven")

fn main() do
  a(0)
  a(1)
  b(0)
  b(1)
  io.log("${sign(5)} ${sign(-5)} ${sign(0)} ${sign(-500)}")
  val abc = "abc"
  io.log("${size(abc)} ${size(null)}")
  val f: (Int) => String = fn sign
  io.log(f(-3))
  show(7)
  show(8)
  show("s")
