// error: has the same atoms as the enum A
declare enum A as
    :x = EXIT_SUCCESS
    :y = EXIT_FAILURE

declare enum B as
    :y = EXIT_SUCCESS
    :x = EXIT_FAILURE

fn main() do
    pass
