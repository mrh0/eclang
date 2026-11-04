# Module `geo`

Points and shapes on a plane.

All lengths are in the same unit; nothing here converts between units.

**Since** 0.1  
**Author** Ada

## Types

### <a id="geo.Point"></a>`Point`

```ec
rec Point
```

A point on the plane.

**Fields**

- <a id="geo.Point.x"></a>`x: Double` — Distance from the y axis.
- <a id="geo.Point.y"></a>`y: Double = 0.0` — Distance from the x axis.

### <a id="geo.Shape"></a>`Shape`

```ec
inter Shape
```

Something with an area. See [`area`](#geo.area).

**Members**

- <a id="geo.Shape.width"></a>`width: Double` — How wide it is.
- <a id="geo.Shape.area"></a>`fn area(): Double`

## Values

### <a id="geo.EPSILON"></a>`EPSILON`

```ec
const EPSILON: Double = 0.000001
```

How close two lengths must be to count as equal.

## Functions

### <a id="geo.distance"></a>`distance`

```ec
fn distance(a: Point, b: Point): Double
```

The distance between [a] and [b], always zero or more:

    val d = distance(Point{0.0, 0.0}, Point{3.0, 4.0})   // 5.0

**Parameters**

- `a` — where to start
- `b` — where to end; the same as `a` gives 0

**Returns** the straight-line distance

**See also** [`Point#x`](#geo.Point.x), [the area of a shape](#geo.area)

### <a id="geo.parse"></a>`parse`

```ec
fn parse(text: String): Point throws Error
```

Parses "x,y".

**Parameters**

- `text` — the text to parse,
  over two lines

**Throws**

- `Error` — when `text` has no comma

### <a id="geo.area"></a>`area`

```ec
fn area(side: Double): Double
```

> **Deprecated.** use [`distance`](#geo.distance) instead

The area of a square.

### <a id="geo.undocumented"></a>`undocumented`

```ec
fn undocumented(n: Int): Int
```
