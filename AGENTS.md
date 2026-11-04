# AGENTS.md

eclang ("Easy C Lang"): a systems language compiled to **human-readable C**, aiming for full C interop. The compiler is Kotlin with an ANTLR 4 grammar that uses Python-style indentation (antlr-denter). Experimental; [README.md](README.md) is the language reference. Read it only for syntax questions.

**Goals:** C interop · readable C output · union types, duck typing, interfaces · overloads, functional style · null safety · APR memory pools, `defer`, deep clone · modern syntax (Python/Kotlin/Go/Elixir) · errors as values · production-grade but beginner-friendly (errors explain how to fix them).

If you find any out of scope issues, document them in `KNOWN_ISSUES.md`.

## Build / run / verify
```
mvn clean package                                   # -> target/eclang-0.1.0.jar
tests/run.sh [filter]                               # ect2 + compiler unit tests + `run` + C binding check + tests/pass (compile, gcc -Werror, compare .out) + tests/doc (`doc`, compare .md/.err) + tests/panic (expected runtime stop) + tests/fail (expected error)
java -jar target/eclang-0.1.0.jar -c examples/fib.ec -o out.c [-d debug dump] [-t time]
gcc out.c -o out -I/usr/include/apr-1.0 -L/usr/lib -lapr-1 -lm && ./out
java -jar target/eclang-0.1.0.jar run examples/fib.ec [-k dir]   # compile + gcc + run
java -jar target/eclang-0.1.0.jar doc examples/fib.ec [-o docs.md]   # Markdown from /** ecdocs */
java -jar target/eclang-0.1.0.jar check   # compiles on request over stdin, for editors (Checker.kt)
```
Always run `tests/run.sh` after a change. Add a `tests/pass/*.ec` + `.out` for new features, a `tests/fail/*.ec` (first line `// error: <expected text>`) for new diagnostics, and a `tests/panic/*.ec` (first line `// panic: <expected text>`) for new runtime checks.

## Pipeline (src/main/kotlin/github/mrh0/eclang/)
0. `Compiler.compile(file)`: one compilation, with its own `CompileData`; it returns the C or the collected errors.
1. `Compiler.tokenizeStream`: ANTLR lexer/parser (the first syntax error becomes an `EcError`, worded by `SyntaxHints`), then `ast/Visitor.kt` maps the parse tree to `T*` tokens (`ast/token/**`).
2. `TProgram.process`: loads `use "*.ec"` files recursively (each declaration remembers its file's module), then works in phases: type names → aliases → record/interface fields → function signatures (`cd.functions.declare`) → `const`s → other globals → bodies (`cd.functions.buildAll`, a worklist that also builds generic instances).
3. Each `ITok.process(cd: CompileData, hint: Type): Pair<Type, IIR>` type-checks and lowers in one pass. `hint` is the expected type (`Types.None` = none).
4. IR (`ir/**`) emits C via `toC(CSourceBuilder, Context)`. `IRProgram` renders the code first, then writes includes, runtime prelude, types, atoms, helpers, prototypes, globals, functions and the C `main`.

Key places:
- `doc/`: ecdocs (`/** */`, Javadoc-style tags). The lexer sets them aside; `DocExtractor` attaches them to declarations (`Compiler.parse` → `TProgram.docs`), and `Markdown` renders them for `eclang doc`.
- `ect2/`: the type system. `TypeContext` has interned types, `cost(from, to)` (0 = fits, 1+ = lossless conversion, NO), `explain`, `storageProblem`, `resolve`, `bind`/`substitute`.
- `output/c/CTypes.kt`: the C representation of every type, plus generated typedefs and helpers.
- `context/`: `CompileData` (`expect`/`storable`/`usable` checks, modules and name lookup, hoisting, loops), `function/Functions.kt` (overloads, calls, generics), `Narrowing.kt` (flow typing), `ContextStack` (scopes, defers, narrowing), `Module.kt`.
- `src/main/resources/`: the `core:` stdlib (no prelude): `string.ec`, `errors.ec`, `array.ec` and `structures.ec` (top level, so their functions work as methods from any module; `Error` is in `errors.ec`), the modules `math.ec`, `io.ec` (`io.log`), `time.ec`, `thread.ec`, `encoding.ec`, and binding files, one per C header (`c/*.ec`) and per APR header (`apr/*.ec`). `tools/check-bindings.py` checks every `declare fn` of them against its C header (`tests/run.sh` runs it on all of them).

Details: [docs/architecture.md](docs/architecture.md). Recipes for adding syntax, types or IR: [docs/howto.md](docs/howto.md).

## Gotchas
- **Grammar edits:** the code uses the **checked-in** parser in `src/main/java/github/mrh0/eclang/antlr/`. Maven's own ANTLR output has no package and is unused. Run `tools/regen-parser.sh` after editing `Eclang.g4`. In `expr`, alternatives listed first bind tightest (precedence).
- **VS Code extension (`ls/vscode`):** its language server is a TypeScript port of the front end (grammar, `SyntaxHints`, `doc/`). After changing those, run `npm run gen` there if the grammar changed, update the port, and run `npm test` there: it compares the port with the compiler jar. See [ls/vscode/README.md](ls/vscode/README.md).
- **Type checks go through `CompileData`:** use `cd.expect(loc, value, type, what)` to convert a value to a type (it returns the IR with the conversion), `cd.storable` for variable, field and return types, and `cd.usable` to reject `None` and unhandled `Result`s. Error text should say how to fix the problem; reuse `types.explain`.
- **Union parameters split a function into per-type copies** (`Overload.isUnionCopy`). In those copies `is`/`== null` checks may be constant: `Narrowing.constant` skips the dead branch, and "always false" errors are suppressed.
- **Generics:** `<T>` functions (`Overload.isTemplate`, also for a `<T>` only in the result or a variadic) get a copy per call (`Functions.instantiate`). `rec List<T>` is a `GenericRecord`; each `List<Int>` is an ordinary `Record` made by `TTypeRecord.instantiate`, so the back end needs nothing for it. `List<T>` in a signature is an `Applied` type until `bind`/`substitute` resolve it. A call's expected type decides a `<T>` only the result has (`Functions.call`'s `expected`); lambdas without parameter types are compiled after the other arguments (`Functions.lambdaHint`).
- **`>>` is two `>` tokens** in the grammar (`shr` in `exprBinOp`), so `List<List<Int>>` closes; the Visitor checks they touch. The record-literal alternatives of `expr` come before `NAME`, or `Box<Int> { 1 }` parses as a comparison.
- **Hoisting:** `try`, `??` on a `Result`, `?? ret` and `when` as a value run before the current statement (`cd.hoist`). Code that runs conditionally or repeatedly wraps itself in `cd.noHoist(reason)`. A value read twice that must work there too declares a temporary with `cd.declareTemp` and sets it in place (`IRTempSet`).
- **Iteration:** `for x in v` on anything but an array or String calls `next(@X)` (a type is its own iterator) or `iter(v)`; `for a, b in v` tries `pairs(v)` first. See `TStatementForeachIn.loopOver` and `Functions.protocolCall`. `it` is a keyword, so a `next` can't name its parameter `it`.
- **Statements ending in a value** take `tail`, not `expr NL`: a `when` ends with a DEDENT, after which the denter gives no NL.
- **Never:** a new statement form that evaluates a call must `setReturned` on a `Types.Never` value (`TStatement`, `TStatementCall`), or flow typing and the missing-`ret` check won't see that it leaves.
- **Exits of a scope** run deferred statements through `cd.compileDeferred`, with `TErrDefer.normal` unless the exit is an error.
- **`distinct` types:** code that needs a `Prim` goes through `Ops.base`, because `Ops.isNumeric` and `isInteger` are also true for a `Distinct`.
- **Modules:** run code of a module inside `cd.inModule(m)`. Unqualified names resolve in the current module, then the top level (`cd.findVar`, `Functions.visible`, `TypeContext.lookup`); `x.name` with `x` a module (and not a variable) is a qualified name. C names come from `Module.cName`/`Declared.cName` (`geo__make_0`); global C names are reserved with `cd.claimCName` (a name the C headers already use, listed in `src/main/resources/c-reserved.txt` from `tools/gen-c-reserved.py`, gets the `__ec_v_` prefix). Locals and parameters take their C name from `cd.localCName`, which adds that prefix when the name would hide a file-scope C name.
- **Errors are collected:** `cd.report`/`cd.recover` record an `EcError` and go on, per declaration (`TProgram`), per function body (`buildAll`) and per statement (`TBlock`, which unwinds the scopes; only calls, assignments and unconditional `ret` let the rest of the block be checked). `cd.checkpoint()` stops between phases with errors (`EcAbort`). After an error, `cd.functionHasErrors` turns off the unreachable-code and missing-`ret` checks for that function. `cd.warn` adds warnings.
- **Pools and lifetimes:** allocation goes through the current pool (`__ec_pool`, `pool` in eclang) or an explicit `Pool`; see `TStatementPool`, `TNew`, `Builtins`. A new expression form that passes a pointer through must be added to `Lifetimes.flows` (and one that allocates to `Lifetimes.value`/`allocations`), or the dangling-pointer and freed-pool checks won't see through it. A call's result is assumed to point into its arguments (`TExprCall.aliased`). Generated allocations call `__ec_palloc`, not `apr_palloc`.
- **Freed pools:** `ContextStack.Scope` tracks, per scope, which named pool each variable's value was allocated in (`Alloc`) and the last `clearPool`/`freePool` of each pool (`Kill`); `TNamed` calls `Lifetimes.checkAlive`. A scope's knowledge merges into its parent in `pop` unless it left early. A loop kills the pools its body clears (the Visitor adds them to the loop's assigned names).
- **Narrowing and `addrof`:** the Visitor records the locals whose address a function takes (`TFunc.addressTaken` → `cd.addressTaken`); `Narrowing` doesn't narrow them, because a write through the pointer could undo the check.
- **Printing and hashing:** `${x}` of a record or tuple is `IRShow` → `CTypes.show`, built from `format` helpers; `Functions.resolvePrinters` runs at each `${...}` to find the `str` overrides of everything the value holds and `use` them (a helper can't do that, the back end runs after culling). A `str` is looked up in the type's own module and the top level only, so `Functions.printers` can hold one per type. `hash` is a builtin that also applies when no `hash` overload takes the argument (`TExprCall.callFunction`), and takes exactly what `equalityProblem` allows: a type `==` learns to compare must be added to `CTypes.hashed`, and hashing must stay field by field (padding) and agree with `equals` (`0.0 == -0.0`).
- **Runtime checks:** `/`, `%` and shifts by a computed count become `IRCheckedArith` (`RuntimeChecks.needed`, `CTypes.checkedArith`); `--overflow-check` adds `+ - *`. Pointer results of `declare fn` are wrapped in `IRCNotNull`. Panics flush stdout first.
- No global state: types, modules and atoms (`cd.atoms`) are per `CompileData`, so one JVM can compile many times (`CompilerTest.kt`).
- **Culling:** `Functions.use` records which C functions each function (or lambda, or global code) uses; `reachable` keeps only those reachable from `main` and globals. A new way of referring to a function must call `use`, or its C function is left out.
- Generated C must compile with `-Wall -Wextra -Werror` except unused locals: a parameter the C body never mentions gets `(void)p;` (`IRFunctionOverride`).
- Identifiers that are C keywords get a `_` prefix (`Util.testIdentifier`). The `__ec_` prefix is reserved for generated code, and `__` separates a module from a name in C.
- eclang has no `const`: calls of `declare fn` functions cast pointer-to-pointer and function-pointer arguments to `void*`, and pointer results to eclang's type (`Functions.constSensitive`, `IRCCast`).
- `out`, `out.c` and `target/` (including `.kt` copies) are tracked in git. They are build artifacts, so don't hand-edit them; `mvn clean` deletes the tracked `target/` files.
- `examples/test.ec` is an old scratch file and doesn't compile. `NOTES.md` holds early design sketches.
