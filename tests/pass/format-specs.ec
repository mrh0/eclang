use "core:c/stdio.ec"

type Id = distinct Int

rec P as
    x: Double

fn show(s: String) do
    printf("[%.*s]\n"c, s.len as Int, s.data)

fn main(): Int do
    val d = 3.14159
    val n = 42
    val s = "ab"
    val c = "cd"c
    val big: Long = 5000000000
    val u: UInt = 255
    show("${d:.2f}")
    show("${d:8.3f}")
    show("${d:<8.1f}|")
    show("${d:e}")
    show("${d:.3}")
    show("${n:>8}")
    show("${n:<8}|")
    show("${n:8}")
    show("${n:08}")
    show("${n:+}")
    show("${n:x} ${n:X} ${n:o} ${n:d}")
    show("${big:15}")
    show("${big:x}")
    show("${u:x} ${u:d}")
    show("${s:>5}")
    show("${s:5}|")
    show("${s:.1}")
    show("${c:>5}")
    show("${c:<5.1}|")
    show("${true:>6}")
    show("${'x':>3}")
    show("${'x':d}")
    show("${n=}")
    show("${n + 1=}")
    show("${d=:.1f}")
    show("${n = }")
    val status = :ok
    show("${status == :ok}")
    show("${(status == :ok):>6}")
    val p = P { 2.5 }
    show("${p.x:.2f} and ${n:>4} and 100%")
    show("${[1, 2][1]:03}")
    val id: Id = 7
    show("${id:03}")
    show("${(if n > 1 do n else 0):04}")
    show("${n == 42=}")
    ret 0
