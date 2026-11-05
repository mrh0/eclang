use "core:io.ec"

fn scroll(up: Bool): :scrollUp | :scrollDown do
    ret :scrollUp if up
    ret :scrollDown

fn main() do
    val a = :scrollUp
    io.log("${a}")
    val u = scroll(false)
    io.log("${u} and ${scroll(true)}")
    io.log(u == :scrollDown)
    when u do
        :scrollUp do io.log("up")
        :scrollDown do io.log("down")
    // Spelled differently, so a different atom.
    io.log((:Mixed as Atom) == (:mixed as Atom))
