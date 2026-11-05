/** A header on the wire. */
packed rec Wire as
    /** The kind of message. */
    tag: UChar
    flags: UInt : 3

/** One of two views. */
union Value as
    i: Int
    f: Float

/**
 * Adds one.
 * @param a The number.
 * @return One more.
 */
export fn plus(a: Int): Int = a + 1
