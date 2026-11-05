// Binding C types whose layout needs `T[N]`, bit-fields and unions. Every size below is C's own.
use "core:io.ec"
use "sys/utsname.h"
use "sys/socket.h"
use "netinet/in.h"
use "signal.h"
use "stdlib.h"
use "time.h"

declare struct utsname as
    sysname: Char[65]
    nodename: Char[65]

declare fn uname as (buf: @utsname): Int

// A C global that is an array: `char* tzname[2]`.
declare var tzname as CString[2]
declare fn tzset as ()

declare struct sockaddr as
    sa_family: UShort
    sa_data: Char[14]

// In glibc `sigval` is the union, with the typedef `sigval_t`.
declare union sigval as
    sival_int: Int
    sival_ptr: @Any

declare union rec sigval_t as
    sival_int: Int
    sival_ptr: @Any

fn main(): Int do
    var u: utsname
    io.log(uname(addrof u))
    io.log("${u.sysname}")
    io.log(u.sysname[0] == 'L')
    io.log(strlen(u.sysname.data) == 5)

    var s = sockaddr { 2, [0 as Char, 1 as Char, 2 as Char, 3 as Char, 4 as Char, 5 as Char, 6 as Char, 7 as Char, 8 as Char, 9 as Char, 10 as Char, 11 as Char, 12 as Char, 13 as Char] }
    s.sa_data[13] = 99 as Char
    val copied = s.sa_data
    io.log("${sizeof sockaddr} ${offsetof(sockaddr, sa_data)} ${copied[13] as Int} ${copied[1] as Int}")

    tzset()
    val zones = tzname
    io.log("${tzname.len} ${zones[0] == tzname[0]} ${strlen(tzname[1]) > 0}")
    tzname[1] = tzname[0]
    io.log(tzname[1] == zones[0])

    var a = sigval { sival_int = 7 }
    var b: sigval_t = sigval_t { sival_int = 8 }
    io.log("${a.sival_int} ${b.sival_int} ${sizeof sigval} ${sizeof sigval_t}")
    ret 0
