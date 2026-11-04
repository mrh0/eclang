// error: A generic record can't extend another record yet
rec Base as
    id: Int

rec Box<T> extends Base as
    value: T

fn main(): Int = 0
