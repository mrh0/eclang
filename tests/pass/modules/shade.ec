// A record whose defaults use names only this module sees.
module "shade"

rec Tint as
    level: Int = 0

val DARK = Tint { 9 }

rec Paint as
    tint: Tint = Tint { 3 }
    other: Tint = DARK
    coats: Int = 1

rec Primer extends Paint as
    dry: Bool = true
