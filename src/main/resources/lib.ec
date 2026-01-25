use "math.h"
use "stdio.h"
use "stdlib.h"
use "apr_general.h"
use "apr_pools.h"

// stdio
declare fn printf as (format: CString, value: Any): None

fn log(value: CString): None do
    printf("%s\n"c, value)

fn log(value: Int): None do
    printf("%d\n"c, value)