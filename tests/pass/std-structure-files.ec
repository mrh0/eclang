// Each structure of core:structures.ec has a file of its own, which works without the others.
use "core:io.ec"
use "core:tree.ec"
use "core:lru.ec"

fn main() do
    val s = sortedSetOf(5, 1, 3)
    io.log("${s.first() ?? 0} ${s.ceiling(2) ?? 0}")
    val c: LruCache<String, Int> = lruCache(1)
    c.put("a", 1)
    c.put("b", 2)
    io.log(c.has("a"))
    val m: Map<String, Int> = map()              // lru.ec brings core:map.ec, which brings core:list.ec
    m.put("x", 7)
    val l = listOf(1, 2, 3)
    io.log("${m.size()} ${l.size()}")
