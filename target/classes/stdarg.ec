/** stdarg.h: C's va_list, for the v* variants of formatting functions (vprintf, apr_pvsprintf, ...). */
use "stdarg.h"

/**
 * A list of variable arguments, as a C function with `...` gets them. eclang can't make one; it passes one on, from
 * a C function that has it to a v* function.
 */
declare rec va_list
