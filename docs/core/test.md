# Module `test`

Tests: assertions that say what differs, and a small runner.

    use "core:test.ec"

    fn addition(t: @test.Test) do
        test.equal(t, 1 + 1, 2)
        test.isTrue(t, 2 > 1, "2 is more than 1")
        test.equal(t, render(), "line one\nline two\n", "the rendered text")

    fn parsing(t: @test.Test) throws Error do           // an error that gets out fails the test
        val n = try parseInt("12")
        test.equal(t, n, 12)

    fn main(): Int do
        val s = test.suite()
        test.run(s, "addition", fn addition)
        test.run(s, "parsing", fn parsing)
        ret test.finish(s)                              // 0, or 1 when something failed

A failed check doesn't end the test: the next ones still run, and each is reported. [`require`](#test.require) is for a check
the rest of the test can't do without. Strings that differ are shown as a line by line diff, or with a mark at
the first difference when they are one line:

    FAIL addition
      check 3, the rendered text: the strings differ (- expected, + actual)
          line one
        - line two
        + line 2

A program run with an argument only runs the tests whose name has it in it: `./tests parsing`.

Tests are listed by hand, and a failure names the check by its number and the `what` given to it, not by its line,
since a function can't see where it was called from. A panic ends the whole run, with the results so far printed.
Each test runs in its own pool. Everything is written to stdout.

## Types

### <a id="test.Suite"></a>`Suite`

```ec
rec Suite
```

A group of tests that are run and counted together: [`suite`](#test.suite).

**Fields**

- <a id="test.Suite.filter"></a>`filter: String` — Only tests with this in their name run, unless it is empty.
- <a id="test.Suite.passed"></a>`passed: Size` — How many tests passed.
- <a id="test.Suite.failed"></a>`failed: @List<String>` — The names of the tests that failed.
- <a id="test.Suite.skipped"></a>`skipped: Size` — How many tests the filter left out.

### <a id="test.Test"></a>`Test`

```ec
rec Test
```

The test that is running, which its function is given.

**Fields**

- <a id="test.Test.name"></a>`name: String` — Its name.
- <a id="test.Test.checks"></a>`checks: Size` — How many checks it has made.
- <a id="test.Test.failures"></a>`failures: Size` — How many of them failed.
- <a id="test.Test.report"></a>`report: @StringBuilder` — What to print about them.
- <a id="test.Test.stopped"></a>`stopped: Bool = false` — Whether [`require`](#test.require) ended it.

## Functions

### <a id="test.suite"></a>`suite`

```ec
fn suite(): @Suite
```

A new empty suite, in the current pool. The first command-line argument of the program, if any, is its filter.

### `suite`

```ec
fn suite(filter: String): @Suite
```

A new suite that only runs the tests with `filter` in their name.

### <a id="test.run"></a>`run`

```ec
fn run(s: @Suite, name: String, body: (@Test) => None)
```

Runs a test, in a pool of its own, and prints how it went.

**Parameters**

- `s` — the suite
- `name` — what to call it
- `body` — the test

### `run`

```ec
fn run(s: @Suite, name: String, body: (@Test) => None throws Error)
```

Runs a test that can fail with an error, in a pool of its own, and prints how it went. An error that gets out of it fails the test.

**Parameters**

- `s` — the suite
- `name` — what to call it
- `body` — the test

### <a id="test.finish"></a>`finish`

```ec
fn finish(s: @Suite): Int
```

Prints how many tests passed and failed, naming those that failed.

**Returns** what `main` should return: 0 when all passed, and 1 when one failed, or the filter left nothing to run

### <a id="test.diff"></a>`diff`

```ec
fn diff(expected: String, actual: String): String
```

A diff of two texts, line by line: the lines only in `expected` start with "- ", those only in `actual` with "+ ", and
the two lines around a change with two spaces. Other lines are left out, with a "...". What can't be seen in a line is
shown as a literal: an ending space, a "\r".

**Returns** the diff, with a line break after each line; "" when the texts are the same

### <a id="test.isTrue"></a>`isTrue`

```ec
fn isTrue(t: @Test, condition: Bool, what: String = "")
```

Checks a condition.

**Parameters**

- `t` — the test
- `condition` — what must be true
- `what` — what is being checked, for the report

### <a id="test.isFalse"></a>`isFalse`

```ec
fn isFalse(t: @Test, condition: Bool, what: String = "")
```

Checks that a condition is false.

**Parameters**

- `t` — the test
- `condition` — what must be false
- `what` — what is being checked, for the report

### <a id="test.equal"></a>`equal`

```ec
fn equal(t: @Test, actual: <V>, expected: <V>, what: String = "")
```

Checks that two values are equal, with `==`. They can be of any type `==` compares and `${...}` shows.

**Parameters**

- `t` — the test
- `actual` — what the code gave
- `expected` — what it should give
- `what` — what is being checked, for the report

### `equal`

```ec
fn equal(t: @Test, actual: String, expected: String, what: String = "")
```

Checks that two Strings are equal. The report shows where they differ: with a diff when they have several lines.

**Parameters**

- `t` — the test
- `actual` — what the code gave
- `expected` — what it should give
- `what` — what is being checked, for the report

### `equal`

```ec
fn equal(t: @Test, actual: Atom, expected: Atom, what: String = "")
```

Checks that two atoms are the same.

**Parameters**

- `t` — the test
- `actual` — what the code gave
- `expected` — what it should give
- `what` — what is being checked, for the report

### <a id="test.equalArrays"></a>`equalArrays`

```ec
fn equalArrays(t: @Test, actual: <V>[], expected: <V>[], what: String = "")
```

Checks that two arrays have the same elements. The report names the first one that differs.

**Parameters**

- `t` — the test
- `actual` — what the code gave
- `expected` — what it should give
- `what` — what is being checked, for the report

### <a id="test.notEqual"></a>`notEqual`

```ec
fn notEqual(t: @Test, actual: <V>, other: <V>, what: String = "")
```

Checks that two values aren't equal.

**Parameters**

- `t` — the test
- `actual` — what the code gave
- `other` — what it shouldn't give
- `what` — what is being checked, for the report

### <a id="test.near"></a>`near`

```ec
fn near(t: @Test, actual: Double, expected: Double, tolerance: Double = 0.000000001, what: String = "")
```

Checks that two numbers are close, for results of floating point arithmetic.

**Parameters**

- `t` — the test
- `actual` — what the code gave
- `expected` — what it should give
- `tolerance` — how far off it may be
- `what` — what is being checked, for the report

### <a id="test.isNull"></a>`isNull`

```ec
fn isNull(t: @Test, actual: <V>?, what: String = "")
```

Checks that a value is null.

**Parameters**

- `t` — the test
- `actual` — what the code gave
- `what` — what is being checked, for the report

### <a id="test.notNull"></a>`notNull`

```ec
fn notNull(t: @Test, actual: <V>?, what: String = "")
```

Checks that a value isn't null.

**Parameters**

- `t` — the test
- `actual` — what the code gave
- `what` — what is being checked, for the report

### <a id="test.hasText"></a>`hasText`

```ec
fn hasText(t: @Test, actual: String, part: String, what: String = "")
```

Checks that a String has another one in it.

**Parameters**

- `t` — the test
- `actual` — what the code gave
- `part` — what should be in it
- `what` — what is being checked, for the report

### <a id="test.fail"></a>`fail`

```ec
fn fail(t: @Test, message: String)
```

Fails the test, for a place the code shouldn't reach, and goes on.

**Parameters**

- `t` — the test
- `message` — what went wrong

### <a id="test.require"></a>`require`

```ec
fn require(t: @Test, condition: Bool, what: String = "") throws Error
```

Checks something the rest of the test can't do without: `try test.require(t, list.len > 0, "the list")`.

**Parameters**

- `t` — the test
- `condition` — what must be true
- `what` — what is being checked, for the report

**Throws**

- `Error` — when it is false, which ends a test that `throws Error`
