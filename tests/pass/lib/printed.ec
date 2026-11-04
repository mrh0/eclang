module "printed"

/** Its `str` is beside it, in this module. */
rec Coin as
    value: Int

fn str(c: Coin): String = "${c.value} coins"

/** Printed by the compiler; holds a Coin. */
rec Purse as
    coin: Coin

/** Printing inside the module. */
fn describe(p: Purse): String = "${p}"
