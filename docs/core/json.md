# Module `json`

JSON (RFC 8259): parsing with error positions, building values, and writing them, compact or pretty.

    use "core:json.ec"
    val v = try json.parse("{\"name\": \"Ada\", \"langs\": [\"c\", \"ec\"], \"age\": 36}")
    io.log(json.asString(json.field(v, "name")) ?? "?")              // Ada
    io.log(json.asLong(json.find(v, "age")) ?? 0)                    // 36
    io.log(json.asString(json.find(v, "langs[1]")) ?? "?")           // ec
    for lang in json.items(json.field(v, "langs")) do
        io.log(json.asString(lang) ?? "?")

    val out = json.object()
    json.put(out, "ok", true)
    json.put(out, "count", 3)
    val list = json.array()
    json.add(list, "a")
    json.put(out, "list", list)
    io.log(json.stringify(out))                                      // {"ok":true,"count":3,"list":["a"]}
    io.log(json.pretty(out))

The readers (`asString`, `field`, `find`, ...) take and give null, so they chain: what isn't there, or isn't of
that kind, comes out as null at the end. The fields of an object stay in the order they were added. Whole numbers
that fit a Long stay exact; others are Doubles. Everything is allocated in the current pool.

## Types

### <a id="json.Kind"></a>`Kind`

```ec
declare type Kind as :null | :bool | :number | :string | :array | :object
```

What a [`Value`](#json.Value) is.

### <a id="json.Value"></a>`Value`

```ec
ref rec Value
```

A JSON value. Make one with [`parse`](#json.parse) or the functions below, and read it with [`asString`](#json.asString) and the like.

**Fields**

- <a id="json.Value.kind"></a>`kind: Kind` — What it is; the fields that go with that are set.
- <a id="json.Value.flag"></a>`flag: Bool = false` — The value of a `:bool`.
- <a id="json.Value.number"></a>`number: Double = 0.0` — The value of a `:number`.
- <a id="json.Value.whole"></a>`whole: Long = 0` — The value of a `:number` that is whole and fits, when `isWhole`.
- <a id="json.Value.isWhole"></a>`isWhole: Bool = false` — Whether a `:number` is exactly `whole`.
- <a id="json.Value.text"></a>`text: String = ""` — The value of a `:string`.
- <a id="json.Value.elements"></a>`elements: List<Value>? = null` — The elements of an `:array`.
- <a id="json.Value.fields"></a>`fields: Map<String, Value>? = null` — The fields of an `:object`.

### <a id="json.JsonError"></a>`JsonError`

```ec
rec JsonError extends ParseError
```

Text isn't valid JSON.

**Fields**

- <a id="json.JsonError.line"></a>`line: Size` — The line of the problem, from 1.
- <a id="json.JsonError.column"></a>`column: Size` — The column of the problem, in bytes, from 1.

## Functions

### <a id="json.nil"></a>`nil`

```ec
fn nil(): Value
```

The JSON value `null`.

### <a id="json.boolean"></a>`boolean`

```ec
fn boolean(b: Bool): Value
```

A JSON `true` or `false`.

### <a id="json.number"></a>`number`

```ec
fn number(x: Double): Value
```

A JSON number.

### `number`

```ec
fn number(x: Long): Value
```

A JSON number that stays exact.

### <a id="json.string"></a>`string`

```ec
fn string(s: String): Value
```

A JSON string.

### <a id="json.array"></a>`array`

```ec
fn array(): Value
```

A new empty JSON array; see [`add`](#json.add).

### <a id="json.object"></a>`object`

```ec
fn object(): Value
```

A new empty JSON object; see [`put`](#json.put).

### <a id="json.add"></a>`add`

```ec
fn add(a: Value, item: Value)
```

Adds `item` at the end of the array `a`. The program stops when `a` isn't an array.

### `add`

```ec
fn add(a: Value, item: String)
```

Adds a string at the end of the array `a`.

### `add`

```ec
fn add(a: Value, item: Long)
```

Adds a number at the end of the array `a`.

### `add`

```ec
fn add(a: Value, item: Double)
```

Adds a number at the end of the array `a`.

### `add`

```ec
fn add(a: Value, item: Bool)
```

Adds a `true` or `false` at the end of the array `a`.

### <a id="json.put"></a>`put`

```ec
fn put(o: Value, key: String, value: Value)
```

Sets the field `key` of the object `o`. The program stops when `o` isn't an object.

### `put`

```ec
fn put(o: Value, key: String, value: String)
```

Sets the field `key` of the object `o` to a string.

### `put`

```ec
fn put(o: Value, key: String, value: Long)
```

Sets the field `key` of the object `o` to a number.

### `put`

```ec
fn put(o: Value, key: String, value: Double)
```

Sets the field `key` of the object `o` to a number.

### `put`

```ec
fn put(o: Value, key: String, value: Bool)
```

Sets the field `key` of the object `o` to `true` or `false`.

### <a id="json.isNull"></a>`isNull`

```ec
fn isNull(v: Value?): Bool
```

Whether `v` is the JSON value `null`. False when there is no value at all.

### <a id="json.asString"></a>`asString`

```ec
fn asString(v: Value?): String?
```

The text of a string, or null for anything else.

### <a id="json.asNumber"></a>`asNumber`

```ec
fn asNumber(v: Value?): Double?
```

The value of a number, or null for anything else.

### <a id="json.asLong"></a>`asLong`

```ec
fn asLong(v: Value?): Long?
```

The value of a whole number that fits a Long, or null for anything else. 3.0 and 1e3 are whole; 3.5 isn't.

### <a id="json.asBool"></a>`asBool`

```ec
fn asBool(v: Value?): Bool?
```

The value of a `true` or `false`, or null for anything else.

### <a id="json.field"></a>`field`

```ec
fn field(v: Value?, key: String): Value?
```

The field `key` of an object; null when there is no such field, or `v` isn't an object.

### <a id="json.item"></a>`item`

```ec
fn item(v: Value?, i: Size): Value?
```

Element `i` of an array; null when there is no such element, or `v` isn't an array.

### <a id="json.items"></a>`items`

```ec
fn items(v: Value?): (Value)[]
```

The elements of an array, for `for`; none for anything else.

### <a id="json.keys"></a>`keys`

```ec
fn keys(v: Value?): String[]
```

The names of the fields of an object, in their order; none for anything else.

### <a id="json.count"></a>`count`

```ec
fn count(v: Value?): Size
```

How many elements an array, or fields an object, has; 0 for anything else.

### <a id="json.find"></a>`find`

```ec
fn find(v: Value?, path: String): Value?
```

Follows a path of fields and indexes: `json.find(v, "users[2].name")`. Field names with "." or "[" in them need
[`field`](#json.field).

**Returns** what is there, or null when the path leads nowhere

### <a id="json.parse"></a>`parse`

```ec
fn parse(text: String): Value throws JsonError
```

Parses JSON text. Of a field that appears twice, the last one counts.

**Parameters**

- `text` — exactly one JSON value, with spaces around it if need be

**Throws**

- `JsonError` — when it isn't valid; the error tells the line and column, and `position` is the byte

### <a id="json.stringify"></a>`stringify`

```ec
fn stringify(v: Value): String
```

`v` as compact JSON text, on one line.

### <a id="json.pretty"></a>`pretty`

```ec
fn pretty(v: Value, indent: Size = 2): String
```

`v` as JSON text for people, on several lines.

**Parameters**

- `v` — the value
- `indent` — how many spaces each level is indented
