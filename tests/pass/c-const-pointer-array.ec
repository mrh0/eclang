use "core:io.ec"
use "core:apr/getopt.ec"

// apr_getopt_init takes `const char * const *argv`: a CString[]c argument is cast, as eclang has no const.
declare fn getoptInit extern "apr_getopt_init" as (out os: @apr_getopt_t, cont: @apr_pool_t, argc: Int, argv: CString[]c): apr_status_t

fn main(): Int do
    val argv = ["prog"c, "-v"c, "file"c]
    var os: @apr_getopt_t? = null
    if getoptInit(mut os, pool, 3, argv.data) != 0 do
        io.log("init failed"c)
        ret 1
    if val o = os do
        var ch: Char = '?'
        var arg: CString? = null
        val status = apr_getopt(o, "v"c, mut ch, mut arg)
        io.log(if status == 0 && ch == 'v' do "got -v"c else "no -v"c)
    ret 0
