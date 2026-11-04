use "core:c/stdio.ec"

// An interface can ask for functions: any record they can be called on (`s.area()`) fits.

inter Shape as
    name: CString
    fn area(): Double
    fn scaled(Double): Double

rec Square as
    name: CString
    side: Double

rec Circle as
    name: CString
    r: Double

fn area(s: Square): Double = s.side * s.side
fn scaled(s: Square, k: Double): Double = s.area() * k
fn area(c: Circle): Double = 3.0 * c.r * c.r
fn scaled(c: Circle, k: Double): Double = c.area() * k

fn report(s: Shape) do
    printf("%s %g %g\n"c, s.name, s.area(), s.scaled(2.0))

fn main(): Int do
    report(Square { "sq"c, 2.0 })
    report(Circle { "ci"c, 1.0 })
    ret 0
