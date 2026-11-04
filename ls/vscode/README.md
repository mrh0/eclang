# eclang for VS Code

Language support for eclang (`.ec` files):

- **Syntax highlighting**, including `${...}` in strings and tags in ecdocs.
- **Compiler errors as you type**: mismatched types, null safety, dangling pointers and everything else the compiler reports, unsaved changes included.
- **Syntax errors** instantly, with the compiler's wording and fix suggestions.
- **Ecdoc tooltips**: hover a name for its signature and `/** ... */` documentation, across `use`d files and the `core:` library.
- **Go to definition**: Ctrl+click (or F12) a function, type, field, variable, parameter, module or `use` path to open where it's declared.
- **Ecdoc warnings** (`@param` naming no parameter, an ecdoc that documents nothing, ...), and `use`s of files that don't exist.

Everything except compiler errors runs in Node.js inside VS Code. Compiler errors need Java 17 or later (the compiler comes with the
extension). Without Java you get a warning, and the rest keeps working.

Settings: `eclang.typeCheck.enable`, `eclang.java.path`, and `eclang.compiler.jar` to check with your own build of the compiler.
The last two can only be set for the machine, not per workspace, so that opening a project can't make the extension run its code.

## Build and install

```
npm install
npm run package                          # eclang-0.1.0.vsix
code --install-extension eclang-0.1.0.vsix
```

To try changes, open this folder in VS Code and press F5.

## How it works

`src/client` starts the language server, `src/server`. Type errors come from the compiler itself: `checker.ts` keeps an `eclang check`
process running (`Checker.kt`) and sends it the open files. It compiles the file without needing a `main`, so libraries work. A file
is compiled with what it `use`s, as `eclang -c` would, and an error in a used file is shown on the `use` line too.

For everything that must be instant the server has a TypeScript port of the compiler's front end:

| Server | Compiler |
| --- | --- |
| `antlr/` (generated) | `src/main/antlr4/Eclang.g4` |
| `lexer.ts` | the grammar's `@lexer::members`, `DenterHelper` from antlr-denter |
| `syntaxHints.ts` | `SyntaxHints.kt` |
| `ecdoc.ts`, `docs.ts` | `doc/Ecdoc.kt`, `doc/DocExtractor.kt` |
| `project.ts` | `use` resolution in `Util.kt` |

`dist/core` is a copy of `src/main/resources`, made by the build.

Hovers and go to definition have no type information: `x.name` matches every field and function called `name`, and a call matches all
overloads (VS Code lists them to pick from). `core:` files open from the extension's own copy.

## When the compiler changes

- After editing `Eclang.g4`, run `npm run gen` (needs `mvn package` to have run once) and update `syntaxes/eclang.tmLanguage.json`
  if keywords changed.
- After editing a file in the table above, make the same change to its port.

`npm test` (needs `target/eclang-0.1.0.jar`) catches both. It also checks that every `tests/fail` case gets its error in the editor. It runs the real compiler and the port over every `.ec` file of
the repository and damaged copies of them, and fails if their tokens, syntax errors or ecdocs differ. It also fails if a keyword of the
grammar isn't highlighted.
