// error: lists :read twice
declare enum Mode as
    :read = EXIT_SUCCESS
    :read = EXIT_FAILURE

fn main() do
    pass
