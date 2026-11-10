use "core:io.ec"
use "core:string.ec"
use "core:time.ec"
use "core:bench.ec"

var calls: Long = 0

fn work() do
    calls += 1
    var total: Long = 0
    for i in 0..<100 do
        total += i * i
    bench.keep(total)

fn allocates() do
    bench.keep("${calls}")

fn main(): Int do
    val quick = bench.Options { warmup = time.millis(5), measure = time.millis(20), samples = 7 }
    val r = bench.measure("work", fn work, quick)
    io.log(r.name)
    io.log(r.samples)
    io.log(r.runs >= 1)
    io.log(r.min > 0.0 and r.min <= r.median and r.median <= r.max)
    io.log(r.mean >= r.min and r.mean <= r.max)
    io.log(r.spread >= 0.0)
    io.log(r.cpu > 0.0)
    io.log(calls >= r.runs * 7)
    io.log(bench.line(r).contains("median"))

    io.log(bench.text(812.0))
    io.log(bench.text(41234.0))
    io.log(bench.text(1500000.0))
    io.log(bench.text(2500000000.0))
    io.log(bench.text(0.25))

    val none = bench.suite("nothing", quick)
    bench.run(none, "work", fn work)
    io.log(bench.finish(none))

    val m = bench.measure("allocates", fn allocates, quick)
    io.log(m.median > 0.0)
    val l = bench.measure("lambda", fn () = bench.keep(1 as Long), quick)
    io.log(l.runs >= 1)
    ret 0
