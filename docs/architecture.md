# Compiler architecture

Paths are relative to `src/main/kotlin/github/mrh0/`.

## Data flow
```
.ec --ANTLR (EclangLexer/Parser + DenterHelper INDENT/DEDENT)--> parse tree
    --eclang/ast/Visitor.kt--> T* tokens (ITok)
    --ITok.process(cd, hint)--> (ect2.Type, IIR)   # type check + lowering, one pass
    --IIR.toC(sb, ctx)--> C text (CSourceBuilder + CTypes)
```

## Types: `ect2/`
- `Type` is a sealed hierarchy: `Prim` (numbers, Bool, CString, String, Atom, Any, None, Never, Null, in `Types`), `Pointer`, `ArrayType` (`T[]`, `T[]c`), `Tuple`, `Union`, `FunctionType`, `Result` (`T throws E`), `IntLiteral`/`FloatLiteral` (untyped literals), `AtomLiteral`, `TypeParam` (`<T>`), and `Declared` = `Record` (nominal, `parent`, `externalName`/`isExternal`/`isCStruct`) | `Interface` (structural).
- `TypeContext` (one per compilation, in `CompileData.types`) creates types and interns them, so equality is `===` and `id` is a dense cache key. `union()` normalizes: it flattens, deduplicates, drops `Never`, lets `Any` absorb everything, drops subsumed members, and sorts by id.
- `cost(from, to)` is the single compatibility rule, cached per id pair. It returns 0 when `from` already is a `to` (same type, union member, record parent, interface), 1+ for a lossless conversion (smaller is closer), and `NO` otherwise. `accepts`, `isSubtype`, union normalization and overload resolution all build on it. Number conversions are only lossless widenings. Literals fit any type that holds the value exactly, preferring Int. Composite types (arrays, tuples, function types) must match exactly, because their C layout depends on them.
- `explain(from, to)` gives the human reason plus a fix. `storageProblem(t)` reports types that can't be stored (`Any`, interfaces, `<T>`, `Result`). `concrete(t)` gives a literal its default type (Int/Long/Double). `bind`/`substitute` handle generics.
- `ect2/Test.kt` holds the unit tests (`java -cp target/eclang-0.1.0.jar github.mrh0.ect2.TestKt`).

## AST tokens: `eclang/ast/`
- `Visitor` has one `visitX` per labelled grammar alternative. Parsed-but-unsupported syntax throws "not supported yet".
- `Tok(location) : ITok`. `process(cd, hint)` returns the type and IR. Statements return `Types.None`.
- Shared rules: `ast/token/op/Ops.kt` (`common` type of two operands, `arithmetic` with constant folding of literals, `comparison`, `nullComparison`, `requireBool`, `shortCircuitRight`), `statement/Defines.kt` (`defineValue`), `statement/Returns.kt` (`returnWithDefers`), `data/Arrays.kt`, `data/record/Records.kt`, `type/Declarations.kt`.
- **Assignment** (`statement/TStatementAssign.kt`): a plain name is handled directly (it ends narrowing). Any other target must implement `IAssignable` (`ast/IAssignable.kt`: `TNamed`, `TAccessorNamed`, `TAccessor`, `TReadPointer`), whose `place` returns a `Place`: the type, a C lvalue IR, and `readOnly`, the full error when it can't be changed. Field places inherit `readOnly` from their base unless they go through a pointer, which is how a field of a `val` record is rejected. `x op= v` is emitted as C's compound operator; on a narrowed name it uses the narrowed lvalue (`x.v.m0 += 1`).
- **Built-in functions** (`function/call/Builtins.kt`): `format`, `concat`, `clone`, `newPool`, `clearPool`, `freePool`, used only when no function of that name is visible. Those that allocate take an optional pool (first, or second for `clone`) and otherwise use the current pool. A String literal with `${...}` is a `TInterpolated` (the Visitor splits it with `StringLiteral.splitInterpolation` and parses each expression with `Compiler.parseInterpolation`, the `interpolation` grammar rule); it is built in the current pool, like `format("...")`. That becomes `IRFormat`, a call of a generated `__ec_format_N` helper (`CTypes.format`) that uses `snprintf` twice and `__ec_palloc`.
- Type declarations implement `ITypeDeclaration` (`declareType`, then `defineType`), so records can reference each other in any order.

## CompileData: `eclang/context/CompileData.kt`
- Holds `types`, `functions`, the current `function` (an `Overload`), the scopes (`ctx()`), `genericContext` (name → bound type in a generic instance), the current `module`, and the loop stack.
- `expect(loc, value, to, what)`: checks with `cost` and returns the IR wrapped in `IRConvert(IMPLICIT)` when a conversion is needed. It throws `EcTypeMismatchError` with `explain`.
- `storable` checks a type can be stored and returns it concrete; `usable` rejects `None` and `Result`; `lookupType` adds "did you mean" suggestions.
- **Hoisting:** `hoist(loc, ir)` adds a statement before the current one (used by `try` and by `??` on a `Result`). `TBlock` wraps each statement in `collectHoisted`. Code that may not run, or runs repeatedly (`and`/`or` right sides, `else if`, `while` conditions, inline `if`, `defer`), uses `noHoist(reason)`. `TTailIf` collects its body's hoists inside its own `if`.

## Modules: `eclang/context/Module.kt`
- `TProgram.collect` gives each top-level declaration the module of its file (`module "name"`, or `cd.topLevel`, named ""). Every phase compiles a declaration inside `cd.inModule(m)`, and `Functions.build` compiles a body inside its overload's module.
- Lookup of an unqualified name: locals, then the current module, then the top level (`cd.findVar`, `cd.visibleModules()`); for types `TypeContext.lookup(namespace, name)` (namespace, then "", then "core"). A name found only in another module gives the hint "write `geo.name`" (`cd.whereElse`).
- Qualified names: `geo.make(…)` is a method-style `TExprCall` whose receiver names a module (and no variable); `geo.origin` is `TAccessorNamed` the same way; types and record literals pass "geo.Point" to `cd.lookupType`; `fn geo.make` is `TNamedFunction(qualifier)`.
- C names: `Module.cName` for functions and globals, `Declared.cName` for records (`geo__Point`). `cd.claimCName` rejects two definitions that end up with the same C name, and prefixes a global whose name C's headers already use with `__ec_v_` (records get an error instead). Locals and parameters use `cd.localCName`, which does the same when the name would hide a file-scope name (a C function, a generated `log_6`, a global). Globals whose initializer isn't a C constant (`IIR.isConstant`) are assigned at the start of the C `main` (`IRGlobalDefine.toCInit`).
- `use "x.ec"` resolves next to the using file (inside `core:` for bundled files), then in the working directory (`Util.resolveUse`). Files are deduplicated by canonical path.

## Functions: `eclang/context/function/Functions.kt`
- `declare(TFunc)` expands each function into `Overload`s, one per combination of union-parameter members (`variants`: atoms stay together, `T?` stays one, a nullable union adds a `Null` variant) and present/absent default parameters. Duplicates are errors. C names are `name_N`, or the extern name.
- Parameters typed `<T>`, an interface (or a pointer to one, `@Named`) or `Any` (in non-external functions) make an overload a **template**. Each call calls `instantiate`, which compiles a copy for the argument types (`name_N_gK`).
- Overloads are stored per module. `call(loc, name, args, qualifier)` takes the visible ones (the current module's and the top level's, or only the qualifier's), scores each with `cost` (templates pay a penalty), picks the lowest, and reports ties as ambiguous (a concrete function beats a template, the current module beats the top level). It converts the arguments and handles typed and C varargs. Errors explain the closest candidate.
- Calls of `declare fn` functions bridge C's `const`: arguments that are pointers to pointers or function pointers are cast to `void*`, and pointer results to eclang's C type (`IRCCast`).
- `TExprCall` first tries, in order: a qualified call, a function-typed field of the receiver (`b.onClick(x)`), a variable of function type (`f(x)`, `IRCallValue`), and then overload resolution.
- `buildAll()` is a worklist: each body is compiled once, and an error in one is reported (`cd.recover`) before the next is built. Missing `ret` on a path is an error. `isUnionCopy` marks per-type copies. More than `MANY_COPIES` (16) copies of one function give a warning.
- `use(cName)` records a call graph (from `call`, `reference` and `lambda`, keyed by the C name of the function or lambda being compiled, or `ROOT` for globals). `reachable(main, irs)` drops the C functions nothing reaches, typically unused union copies; they were still type-checked.
- Lambdas (`TLambda`, `fn (x: Int) = x + 1`) are compiled where they appear, inside `cd.isolated` (a fresh scope with no loops, hoisting or `cd.function`), and become plain C functions named after the function around them (`main_0_lambda0`, `Functions.lambda`); `buildAll` returns them after the bodies. Their value is the C function's name. They don't capture: a name that is a local of an enclosing function gets a dedicated error (`CompileData.capturedProblem`).
- `x |> stage` (`TPipe`): the visitor counts the `it`s in each stage and rewrites a stage without `it` (`f(2)` → `f(it, 2)`, `f` → `f(it)`, a lambda → `TCallValue`). `TPipe` pushes the value on `cd.pipeValues`, which `TIt` reads; a value used more than once is hoisted into a temporary first unless it's a variable or literal.

## Flow typing: `eclang/context/Narrowing.kt` + `ContextStack`
- `Narrowing.of(cd, condition, whenTrue)` returns name → narrower type for `x == null`/`!= null`, `x is T`, `!!x`, and `not`/`and`/`or` combinations, for local names only.
- `ContextStack.Scope.narrowed` is copied into child scopes. `getRaw` returns a `NarrowedVar`, which reads through `IRConvert(NARROW)`. Assignment calls `forget`.
- `TStatementIf`, `TTailIf`, `TInlineIf`, `TStatementWhile`, `for ... where`, `assert` and `and`/`or` apply narrowings. After an `if` whose bodies all leave (`Scope.returned`, which also covers break/continue), the false-narrowings apply to the rest of the block.
- `Narrowing.constant` decides checks at compile time, only inside union copies. The dead branch is skipped, and code after an always-taken leaving branch is skipped (`returnedStatically`) instead of being reported as unreachable.

## Pools and lifetimes
- `Pool` is a built-in type, `TypeContext.pool` = `@apr_pool_t` (the `apr_pool_t` record is in the `core` namespace, so `apr/pools.ec` doesn't declare it). The current pool is the C thread-local `__ec_pool` (runtime prelude in `IRProgram`), created by the C `main` before the global initializers. `pool` in code is `TNamed` falling back to it when no variable has that name (`TNamed.isCurrentPool`); `pool` is not a keyword.
- `pool do` / `pool name do` (`TStatementPool`, grammar `keyword=NAME name=NAME? 'do'`, checked in the Visitor) pushes a scope, saves the current pool in a `__ec_parentN` temp, and defers an `IRPoolLeave` (destroy, then restore), so every exit frees the pool through the usual defer machinery. `IRPoolBlock` writes the C block.
- `new` (`op/TNew`, `IRNew`): `new value` calls `__ec_new_<T>(pool, v)`; `new T` passes `CTypes.zero(T)`; `new T[n]` calls `__ec_alloc_<A>(pool, n)`. A `TNamed` (or `module.Name`) that is a type and not a variable is read as a type.
- `clone` (`IRClone`, `CTypes.clone`) generates `__ec_clone_<T>` helpers per type, with prototypes written before all helpers (they recurse for recursive types). A pointer to an extended record dispatches on `__ec_tid`. Types that hold no copyable memory (`ownsMemory`) are returned as is.
- **Lifetimes** (`context/Lifetimes.kt`): `value(cd, tok)` is the `Local` memory (a scope depth plus wording) a value may point into, found from the tokens just compiled: `addrof` of a local place, array literals, record literals, `new`, calls (`TExprCall.pools`: pool arguments, and the current pool for eclang functions and allocating builtins), `"${...}"`, and the pass-through operators. Variables remember it (`ContextStack.setOrigin`, set by the `val`/`var` definitions and assignments). `checkReturn` (`ret`, `throw`, lambda results) rejects any `Local`; `assign` (from `TStatementAssign`) rejects a value whose memory is freed before the target's scope ends (a global or a place behind a pointer outlives the function). Only types for which `holdsPointer` is true are checked. Inside a `pool do` block, `cd.poolBlocks` holds the blocks' `Local`s, so allocations in the current pool get the innermost block's lifetime, and `TTry` copies a propagated error into `cd.poolEntry` (`IRTryPropagate.copyInto`).

## Errors as values
- `fn f(): T throws E` returns `Result(T, E)`. `TExprCall` returns that type, and only four things accept it: `try` (`TTry`, which hoists `IRTryPropagate`: on error it runs the defers and returns the error converted to the outer function's error type), `??` (a hoisted temp plus `IRInlineIf`), `try … catch e do` (`TStatementTryCatch` → `IRTryCatch`), and `if val x = f() do` (`TStatementLetIf`, a hoisted temp tested with `.code == 0`; the same token handles nullable values).
- `throw`/`ret` in a throwing function build `IRResultValue`. A `None throws E` function gets an implicit success return at its end.

## C output: `eclang/output/c/`
- `CTypes` is the only place that knows the representation:
  - Primitives map to C directly.
  - `T?` of a pointer-like type, and unions of atoms, are plain pointers that may be NULL.
  - Other unions are `struct { int tag; union { … } v; }`, with the tag being the member index.
  - Arrays (`{size_t len; T* data}`), tuples and results are generated structs.
  - Records are structs with the parent's fields first, plus `__ec_tid` (the record id) when they're in an `extends` hierarchy.
  - Function types are typedefs.
- Names are readable mangles (`__ec_union_Int_CString`). Types are forward-declared, and definitions are completed in dependency order (`definitions()`).
- `convert`/`narrow`/`isCheck` produce C expressions. Checked conversions (union → member, `x!`, downcasts) call generated `static inline` helpers that `__ec_panic("file:line", msg)` on failure.
- `a[i]` on arrays and Strings calls `__ec_at_<type>` (`CTypes.elementAt`), which checks the bounds and returns a pointer, so `(*__ec_at_AInt(a, i, "f.ec:3"))` is still an lvalue; `cd.boundsChecks` (`--no-bounds-check`) turns it into `a.data[i]`. Slices (`a[x..<y]`, `TRange` inside `TAccessor`) call `__ec_slice_<type>`. Record `==` calls `__ec_eq_<record>` (`CTypes.recordEquals`); `equalityProblem` in `Ops.kt` decides which field types it supports.
- `x?.field` calls a generated `__ec_safe_<type>__<field>` helper (`CTypes.safeAccess`), so `x` is evaluated once without hoisting.
- Loops with an `else` block (`compileLoop` in `ast/token/loop/Loops.kt`) emit the block after the loop; a `break` then becomes `goto __ec_loop_endN` (`cd.loop`, `cd.breakLabel`), and the label is only written when used. `break` and `continue` first run the statements deferred inside the loop body (`cd.loopDeferred`), and a loop forgets the narrowing of every variable its body assigns (`cd.forgetAssigned`, names collected by the `Visitor`) before compiling its condition and body.
- Bitwise operators (`TBitwise`, `IRBitwise`) are always parenthesized in C, because C's `&`/`|`/`^` bind looser than `==`.
- `IRProgram` renders the code before the type definitions and helpers. The runtime prelude has `__ec_string_t`, `__ec_panic` (`_Noreturn`), `__ec_panic_index`/`__ec_panic_slice`, `__ec_nn`, `__ec_str_eq` and `__ec_str_concat` (allocates in an APR pool; `apr_pools.h` is always included).
- C qualifiers: `val` and parameters are plain C (eclang enforces immutability).
- `const` (`statement/Constants.kt`, `CompileTimeConstant`): the value must be compile-time (literals, `IRConstantRef`, folded arithmetic). Untyped numbers keep their literal type, so uses fold and adapt. A top-level one is `#define NAME (value)` (`IRDefineConstant`), processed before other globals; a local one is inlined. `defer` runs in reverse order (last deferred first).
