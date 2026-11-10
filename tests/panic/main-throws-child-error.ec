// panic: error: main failed: missing.txt: No such file or directory
use "core:errors.ec"

fn open(path: String) throws IoError do
    throw ioError(2, path)

// An error record below Error holds the message in its parent: the C main reads it there.
fn main() throws IoError do
    try open("missing.txt")
