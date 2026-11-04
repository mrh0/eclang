use "core:lib.ec"

fn main(): Int do
    // An assignment in a nested block undoes the narrowing of the enclosing block.
    var v: Int | CString = 1
    if v is Int do
        if true do
            v = "s"c
        if v is CString do
            log(v)
    // A loop that assigns a narrowed variable: the condition and the body see its declared type again.
    var w: Int | CString = 1
    if w is Int do
        while w is Int do
            if w is Int do
                log(w + 1)
            w = "x"c
        if w is CString do
            log(w)
    var p: CString? = "p"c
    if p != null do
        for i in 0..<2 do
            if p != null do
                log(p)
            p = null
    ret 0
