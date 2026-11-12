// panic: the index is past the end of the RingBuffer
use "core:io.ec"
use "core:ringbuffer.ec"

fn main(): Int do
    val r: RingBuffer<Int> = ringBuffer(4)
    r.push(1)
    io.log(r.get(1))
    ret 0
