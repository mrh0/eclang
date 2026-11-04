/**
 * A value in a box.
 * @param <T> what it holds
 */
rec Box<T> as
    /** The value. */
    value: T

/** The value of `b`. */
fn open(b: Box<T>): <T> = b.value
