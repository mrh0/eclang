module "ids"

type Id = distinct Int
type Pair = Id | CString

fn next(i: Id): Id = i + 1
