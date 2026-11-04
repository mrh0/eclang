# How-to recipes

Paths are relative to `src/main/kotlin/github/mrh0/eclang/` unless they start with `src/`, `tests/` or `tools/`. Finish every change with `mvn clean package && tests/run.sh`.

## Add syntax (statement, expression or operator)
1. Add a labelled alternative to `src/main/antlr4/Eclang.g4` (`... #myThing`). Blocks use `INDENT`/`DEDENT` from antlr-denter; keep the `do` + indented-block style. In `expr`, where you put the alternative sets its precedence (earlier binds tighter).
2. Run `tools/regen-parser.sh`. Maven's own ANTLR output is **not** used.
3. Create a token `ast/token/<group>/TMyThing.kt : Tok(location)`. In `process(cd, hint)`:
   - Process children with a sensible hint (`Types.None` when nothing is expected).
   - Check values with `cd.expect` (conversion), `cd.usable` (not None, not an unhandled Result) and `cd.storable` (for variable, field or return types).
   - Throw `EcError(location, "<what's wrong>. <how to fix it>")` for anything else.
   - Return `(type, IR)`.
4. Override `visitMyThing` in `ast/Visitor.kt`, building the token with `loc(ctx)`.
5. Create `ir/<group>/IRMyThing.kt : IR(location)` with `toC`. Use `sb.putStatement()`/`sb.endStatement()` for statements, `sb.put` for expressions, and `sb.types.cName(type)` for C type names.
6. Add `tests/pass/<name>.ec` + `.out` and, for new errors, `tests/fail/<name>.ec` (first line `// error: <expected text>`). Document the feature in README.md.

If the new code only sometimes runs, or runs repeatedly, wrap the processing in `cd.noHoist("why try isn't allowed here")`. If it introduces a scope or a condition, look at how `TStatementWhile` pushes the scope and applies `Narrowing`. A new loop goes through `compileLoop` (`ast/token/loop/Loops.kt`), which handles `break`, `continue` and an `else` block.

## Add or change a type rule
- Compatibility is `TypeContext.cost` (`ect2/TypeContext.kt`). Add the case to `computeCost`, add a matching message to `explain`, and add unit tests to `ect2/Test.kt`.
- A new kind of type: add a class to `ect2/Type.kt` with a factory in `TypeContext` (use `intern` so it is unique), handle it in `CTypes.cName`/`mangle`/`complete` (`output/c/CTypes.kt`), and in `storageProblem` if it can't be stored.
- A new built-in primitive: add it to `ect2/Types.kt` (it is registered by name automatically) and map it in `CTypes.primName`.
- If values need converting at runtime, extend `CTypes.convert`. Write C helpers with `helper(name) { name -> "static inline ..." }`.

## Add a type-specialised operator
Use the shared helpers in `ast/token/op/Ops.kt` (`arithmetic`, `comparison`), and choose the IR class from the operand types, as `TEquals` does for CStrings.

## Bind a C library
Add `src/main/resources/<lib>.ec` with `use "header.h"` and `declare fn/val/var/rec/struct/type` (see `c/*.ec` and `apr/*.ec`, one file per header; a library built on them, such as `math.ec`, can be a `module`). Users load it with `use "core:<lib>.ec"`. Inside a bundled file, `use "other.ec"` means another bundled file in the same folder.
- `...` for C varargs, `@Any` for `void*`, `CString` for `char*` and `const char*` (eclang has no `const`).
- A C function-pointer typedef is a function type: `declare type cb_t as (@Any, Int) => Int`, and a `cb_t*` parameter is plain `cb_t`.
- Opaque C types (`DIR`, `pthread_t`, `struct tm`) are `declare rec name` (or `declare struct name` for `struct name`): they keep their C name and can be passed around.
- A pointer result that may be `NULL` gets a nullable type (`CString?`, `@T?`), so callers must check it.
- C enums used as parameters or results of `declare fn` are `declare enum x_e as` with lines `:atom = C_CONSTANT`, so eclang code passes atoms. In struct fields and `declare var`/`val` they aren't converted, so there use `declare type x_e as Int`; for a parameter `x_e*`, use `@Any`.
- Check the bindings against the headers: `tools/check-bindings.py OUT_DIR core:<lib>.ec` (`tests/run.sh` runs it on every bundled file). It compiles a wrapper for every non-variadic `declare fn` with `-Werror -Wconversion`, ignoring only `const`/`volatile`. While iterating, compile with the resources from `src` instead of the jar: `java -cp src/main/resources:target/eclang-0.1.0.jar github.mrh0.eclang.MainKt -c file.ec`.

## Debug a miscompile
- `java -jar target/eclang-0.1.0.jar -c file.ec -d` prints the tokens, the IR and the C. A compile error prints its stack trace too with `-d`.
- `tests/run.sh name` runs only the tests whose names contain `name`.
- A gcc error in generated C is a compiler bug: reduce it to a small `tests/pass` program first.
