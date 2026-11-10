# raylib bindings

eclang bindings for [raylib](https://www.raylib.com) 5.5, a library for making games. They cover all of `raylib.h`,
what [the cheatsheet](https://www.raylib.com/cheatsheet/cheatsheet.html) lists: window, input, shapes, textures,
text, models and audio. The names are raylib's own, so its docs and examples carry over almost line by line.

They aren't part of `core:`. [raylib.ec](raylib.ec) is generated from `raylib.h` by [generate.py](generate.py).

## Setup

Install raylib 5.5: your package manager's `libraylib-dev` (or similar), or a [release](https://github.com/raysan5/raylib/releases/tag/5.5).
When it isn't in the usual directories, tell the C compiler where it is:

```
export C_INCLUDE_PATH=/path/to/raylib/include
export LIBRARY_PATH=/path/to/raylib/lib
export LD_LIBRARY_PATH=/path/to/raylib/lib     # to run a program linked to libraylib.so
java -jar target/eclang-0.1.0.jar run bindings/raylib/examples/bounce.ec
```

`raylib.ec` says `use "lib:raylib"`, so `run` and `lib` link it. Linking the static `libraylib.a` by hand also needs
`-lGL -lm -lpthread -ldl -lrt -lX11`.

## Usage

`use` the file by its path, relative to your file or to the working directory:

```plaintext
use "bindings/raylib/raylib.ec"

fn main() do
    InitWindow(800, 450, "hello"c)
    defer CloseWindow()
    SetTargetFPS(60)
    while not WindowShouldClose() do
        BeginDrawing()
        ClearBackground(RAYWHITE)
        DrawText("Hello, raylib!"c, 190, 200, 20, LIGHTGRAY)
        EndDrawing()
```

[examples/](examples) has a 2D program ([bounce.ec](examples/bounce.ec): input, shapes, text) and a 3D one
([camera3d.ec](examples/camera3d.ec): camera, picking with a ray).

## From C to eclang

| C | eclang |
| --- | --- |
| `typedef struct Vector2 {...} Vector2` | `declare rec Vector2`, passed by value as in C: `Vector2 { 1f, 2f }`, `Vector2 { x = 1f, y = 2f }` |
| `Texture2D`, `Camera`, `Quaternion`, ... | other names for `Texture`, `Camera3D`, `Vector4`, ... |
| `RED`, `RAYWHITE`, ... | `Color` values with the same names |
| `PI`, `DEG2RAD`, `RAD2DEG`, `RAYLIB_VERSION` | the same names (`Float`s, and a `CString`) |
| `const char *text` | `CString`: a literal `"text"c`, or `cstr(s)` of a String |
| `IsKeyDown(KEY_SPACE)` | `IsKeyDown(:space)`: functions take enums as atoms (below) |
| `SetConfigFlags(FLAG_VSYNC_HINT \| FLAG_MSAA_4X_HINT)` | the same: flags are numbers |
| `ImageResize(&image, 64, 64)` | `ImageResize(mut image, 64, 64)`: a struct the function changes |
| `UpdateCamera(&camera, CAMERA_ORBITAL)` | `UpdateCamera(mut camera, :orbital)` |
| `LoadFileData("f", &size)` | `LoadFileData("f"c, mut size)`: a number the function sets |
| `DrawLineStrip(points, count, RED)` | `DrawLineStrip(points.data, points.len as Int, RED)`: arrays are `T[]c` |
| `model.materials[0].maps[MATERIAL_MAP_DIFFUSE].texture = t` | the same inside `unsafe`: a `T[]c` has no length to check an index against |

**Enums.** Each enum a function takes is an atom: its C name without the prefix, in lower case. `KEY_LEFT_SHIFT` is
`:left_shift`, `MOUSE_BUTTON_LEFT` is `:left`, `LOG_WARNING` is `:warning`, `BLEND_ADDITIVE` is `:additive`,
`PIXELFORMAT_UNCOMPRESSED_R8G8B8A8` is `:uncompressed_r8g8b8a8`. A misspelled atom is a compile error.

Some enums stay numbers named as in C: flags, which combine with `|` (`FLAG_*`, `GESTURE_*`), and values stored in
structs or used as indexes (`CAMERA_PERSPECTIVE` for `camera.projection`, `PIXELFORMAT_*` for `image.format`,
`MATERIAL_MAP_*`, `SHADER_LOC_*`, `NPATCH_*`). `GetGestureDetected` returns an `Int`, as in C, while `GESTURE_*` are
`UInt`s, as `SetGesturesEnabled` takes them: compare with `GetGestureDetected() as UInt == GESTURE_TAP`.

**Keys as numbers.** `GetKeyPressed` returns a number, because a keyboard can have keys raylib's enum doesn't list.
`KeyCode(:enter)` is the number of a key, to compare with it, and the functions taking a key also take its number:

```plaintext
val key = GetKeyPressed()
if key == KeyCode(:enter) do
    submit()
else if key != 0 and IsKeyDown(key) do
    remember(key)
```

**NULL.** A function that returns NULL when it fails has a nullable result, so the compiler makes you check it:

```plaintext
var size = 0
if val data = LoadFileData("save.bin"c, mut size) do
    defer UnloadFileData(data)
    load(data, size)
```

These are `LoadFileData`, `LoadFileText`, `LoadImageColors`, `LoadImagePalette`, `LoadMaterials`,
`LoadModelAnimations`, `LoadFontData`, `LoadRandomSequence`, `GetFileExtension` (a name without a dot),
`GetClipboardText`, `TextReplace`, `CompressData`, `DecompressData`, `EncodeDataBase64`, `DecodeDataBase64`,
`ExportImageToMemory`, `MemAlloc`, `MemRealloc` and `GetWindowHandle`. Others, such as `LoadImage` and `LoadTexture`,
return a struct either way: check it with `IsImageValid`, `IsTextureValid`, ...

Pointer fields a struct may leave NULL are nullable too: `image.data`, `wave.data`, most fields of a `Mesh`
(`mesh.normals`, `mesh.indices`, ...), `model.bones`, `model.bindPose`. The arrays of a loaded `Model`, `Font`,
`Material` and `Shader` (`model.meshes`, `font.glyphs`, `material.maps`, `shader.locs`, ...) aren't; reading one that
is NULL stops the program with a message saying so, instead of crashing later.

**Callbacks.** `SetLoadFileDataCallback` and the other file callbacks, and the audio ones (`SetAudioStreamCallback`,
`AttachAudioStreamProcessor`, ...), take a C function pointer: `fn name`, or a lambda that uses no local variables.
An `AudioCallback` runs on raylib's audio thread, not the one that called it.

**Left out.** `SetTraceLogCallback`, whose callback takes a C `va_list`; the deprecated `GetMouseRay` (use
`GetScreenToWorldRay`); and raylib's other headers, `raymath.h` and `rlgl.h`. eclang has `+` and the like only for
numbers, so vector math is written per field or with functions of your own. `TextFormat` works (it takes C's `...`),
but `${...}` in a String and `cstr` are usually simpler. A `cstr` each frame allocates each frame, so put it in a
`pool do` block, as [bounce.ec](examples/bounce.ec) does.

## Regenerating and testing

```
bindings/raylib/generate.py /path/to/raylib.h > bindings/raylib/raylib.ec
```

What the C types don't say (whether an `int` is an enum, whether a pointer is an array, a value the function
changes or one it fills in, whether a result can be NULL) is decided by the tables at the top of `generate.py`, so
change those rather than `raylib.ec`. For a new raylib version, the script stops at an enum or pointer parameter it
has no rule for. Check the NULL results against the new sources.

`tests/run.sh` (or `tests/run.sh raylib`) checks the bindings when it finds `raylib.h` of the same version, installed
or on `C_INCLUDE_PATH`. It checks that `raylib.ec` is what `generate.py` makes of the header, that every `declare fn`
matches its C declaration (`tools/check-bindings.py`), and that the examples compile. Without the header it prints a
`skip` line.
