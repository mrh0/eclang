// error: it needs a statement of its own, so it can only be used inside a function
val x = (when 1 do
    1 do 2
    else do 3
)

fn main(): Int do
    ret x
