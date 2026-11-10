# Bundled C code

C sources that `core:` modules need and neither C nor APR provide. A `use "core:vendor/x.c"` writes the file into the generated C, so a program still builds from one C file, with the usual flags (`eclang run` and `lib` can't link other libraries).

| File | What | From | Licence |
|---|---|---|---|
| `digest.c` | MD5, SHA-1, SHA-256 | [B-Con/crypto-algorithms](https://github.com/B-Con/crypto-algorithms), commit `cfbde48414baacf51fc7c74f275190881f037d32`, by Brad Conte | Public domain; acknowledgement requested |

`digest.c` differs from upstream only so that it can share a file with any program. `md5`, `sha1` and `sha256` (`.c` and `.h`) are joined, every function is `static`, all names start with `__ec_` (`sha256_init` is `__ec_sha256_init`, `BYTE` is `__ec_byte`, the table `k` is `__ec_sha256_k`), the `#include`s are gone, each algorithm `#undef`s its macros (`F`, `G`, `H`, `I`, `ROTLEFT`, ...), and `-Wconversion` is off for the file. The `__ec_digest_*` functions at the end are new. Upstream says it isn't hardened against side channels, which `core:hash.ec` repeats.

A bundled file shares the generated C with the program's own names, so it must:

- declare nothing outside the `__ec_` prefix, and keep every function and variable `static`;
- `#undef` every macro it defines;
- compile with `-Wall -Wextra -Werror` (a `static` function that a program may not reach needs `__attribute__((unused))`);
- use only the headers every generated file includes (see `IRProgram`).

Its `declare fn`s go in a `.ec` file next to it, and `tests/run.sh` checks those like the other bindings.
