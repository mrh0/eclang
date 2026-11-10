// panic: the part that failed: s.len > 3
fn main(): Int do
    val s: String? = "ab"
    // The second part sees what the first proves: s isn't null there.
    assert s != null and s.len > 3
    ret 0
