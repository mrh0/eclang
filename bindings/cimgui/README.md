# Dear ImGui bindings (through cimgui)

eclang bindings for [Dear ImGui](https://github.com/ocornut/imgui) 1.93, a library for immediate-mode user
interfaces (tools, editors, debug windows), through [cimgui](https://github.com/cimgui/cimgui), its C API. They cover
`imgui.h`, the public API: windows, widgets, tables, menus, popups, drag and drop, styles, fonts, draw lists and
the input and texture protocol a backend uses. `imgui_internal.h` and the templated helpers are left out.

They aren't part of `core:`. [cimgui.ec](cimgui.ec) is generated from cimgui's metadata by [generate.py](generate.py).

## Setup

cimgui has no packages, so build it as a shared library for the ImGui version of the bindings (cimgui's `master`,
for Dear ImGui 1.93.0):

```
git clone --recursive https://github.com/cimgui/cimgui.git
cd cimgui && cmake -B build && cmake --build build
mkdir -p ~/.local/cimgui/include ~/.local/cimgui/lib
cp cimgui.h ~/.local/cimgui/include/
cp build/cimgui.so ~/.local/cimgui/lib/libcimgui.so    # cimgui's CMake leaves out the `lib` that -lcimgui looks for
```

Then tell the C compiler where it is:

```
export C_INCLUDE_PATH=~/.local/cimgui/include
export LIBRARY_PATH=~/.local/cimgui/lib
export LD_LIBRARY_PATH=~/.local/cimgui/lib     # to run the program
java -jar target/eclang-0.1.0.jar run bindings/cimgui/examples/headless.ec
```

`cimgui.ec` says `use "lib:cimgui"`, so `run` and `lib` link it. It also says
`use "define:CIMGUI_DEFINE_ENUMS_AND_STRUCTS"`, which `cimgui.h` needs to declare its structs and enums for C.

ImGui draws nothing by itself. A program gives it input and draws the triangles it returns, through a *backend*.
cimgui can build ImGui's own backends (GLFW, SDL, OpenGL, Vulkan, ...) into the library, and they can be bound with
`declare fn` from `cimgui_impl.h`. [raylib.ec](examples/raylib.ec) is a backend written in eclang instead, on the
[raylib bindings](../raylib/README.md).

## Usage

`use` the file by its path, relative to your file or to the working directory. Each frame: input, `igNewFrame`, the
user interface, `igRender`, then draw `igGetDrawData()`:

```plaintext
use "bindings/cimgui/cimgui.ec"

fn frame(mut volume: Float, mut muted: Bool) do
    igNewFrame()
    if igBegin("Mixer"c) do
        igSliderFloat("Volume"c, mut volume, 0f, 1f)
        igCheckbox("Mute"c, mut muted)
        if igButton("Reset"c) do
            volume = 0.5f
    igEnd()
    igRender()
```

[examples/](examples) has two programs:

- [headless.ec](examples/headless.ec): ImGui without a window. Made-up mouse input clicks a button and a checkbox,
  and a do-nothing renderer answers ImGui's texture requests. It shows the protocol, and it runs in the tests.
- [raylib.ec](examples/raylib.ec): ImGui in a raylib window. raylib's input goes to ImGui, its textures and triangles
  are drawn with rlgl, and the controls change a scene raylib draws, next to ImGui's demo window.

## From C++ to eclang

| C++ | eclang |
| --- | --- |
| `ImGui::Button("OK")` | `igButton("OK"c)`: the functions of the `ImGui` namespace start with `ig` |
| `ImGui::Button("OK", ImVec2(80, 0))` | `igButton("OK"c, ImVec2 { 80f, 0f })`: default values work as in C++ |
| `ImGui::SliderFloat("x", &x, 0, 1)` | `igSliderFloat("x"c, mut x, 0f, 1f)`: a value the widget changes is `mut` |
| `ImGui::Begin("Tools", &open)` | `igBegin("Tools"c, addrof open)`: an optional pointer (NULL by default) is a `@T?` |
| `ImGui::ColorEdit3("c", color)` with `float color[3]` | `igColorEdit3("c"c, color.data)` with `color: Float[3]` |
| `ImGui::InputText("name", buf, sizeof(buf))` | `igInputText("name"c, buf.data, buf.len)` with `buf: Char[64]` |
| `ImGui::Text("%d items", n)` | `igText("%d items"c, n)`; `igTextUnformatted(cstr(s))` for text that may hold a `%` |
| `ImGui::PushID(i)`, `ImGui::PushID("x")` | `igPushID(i)`, `igPushID("x"c)`, or cimgui's `igPushID_Int`, `igPushID_Str` |
| `ImGui::GetIO().DeltaTime = dt` | `val io = igGetIO()`, `io.DeltaTime = dt`: structs are `declare mut rec`s, behind pointers |
| `draw_list->AddLine(a, b, col)` | `ImDrawList_AddLine(list, a, b, col)`: methods take the object first |
| `ImGuiWindowFlags_NoResize \| ImGuiWindowFlags_NoMove` | the same: enums are numbers named as in C, of their int types |
| `IM_COL32(255, 0, 0, 255)` | `0xff0000ff as ImU32` (0xAABBGGRR), or `igGetColorU32(...)` |
| `ImVector<T>` fields (`draw_list->VtxBuffer`) | `ImVector_T` records: `Size`, `Capacity` and `Data` (a `T[]c?`, null while empty) |

**Overloads.** cimgui names C++ overloads apart: `igPushID_Str`, `igPushID_Int`, `igBeginChild_Str`,
`igBeginChild_ID`. Those names are bound, and each is also an eclang overload of the plain name (`igPushID`,
`igBeginChild`), which picks by the arguments. Two overloads that take the same arguments keep only cimgui's names.

**NULL.** Pointer results are never null unless ImGui returns NULL for "nothing": `igGetDrawData` (before
`igRender`), `igGetDragDropPayload`, `igAcceptDragDropPayload`, `igTableGetSortSpecs`, `igGetClipboardText`, the
`ImFontAtlas_AddFont*` functions (a file that doesn't load), `ImFontBaked_FindGlyph*`, `ImTextureData_GetPixels`,
... (see `NULLABLE_RESULTS` in `generate.py`). Pointer fields that may be NULL are nullable: strings, `void*`, function
pointers and those listed in `NULLABLE_FIELDS`. A pointer the bindings say isn't NULL and is NULL anyway stops the
program with a message saying so.

**Callbacks** (`ImGuiInputTextCallback`, `ImGuiSizeCallback`, the getters of `igCombo`, `igListBox` and
`igPlotLines`, the `Platform_*` functions of `ImGuiPlatformIO`) are C function pointers: `fn name`, or a lambda that
uses no local variables.

**Asserts.** ImGui checks how it is used with `IM_ASSERT`, which stops the program (a missing `igEnd`, an
`igNewFrame` without a display size, ...). Read the message it prints: it says what is wrong.

**Left out**: `imgui_internal.h`, ImGui's backends (`cimgui_impl.h`), the functions that take a C `va_list`
(`igTextV`, ...), those of the templated types (`ImVector_*` methods, `ImPool`, `ImSpan`), `igGetAllocatorFunctions`
and `ImGuiStorage_GetVoidPtrRef` (a `void**`), anonymous unions in structs (`ImGuiStoragePair`'s value), and fields
whose names start with `_` (ImGui's own). `IM_COL32` and the other macros aren't functions, so they aren't bound.

## Regenerating and testing

```
bindings/cimgui/generate.py /path/to/cimgui > bindings/cimgui/cimgui.ec
```

The argument is a cimgui checkout: it reads `generator/output/*.json`, `cimgui.h` (for the version) and
`imgui/imgui.h` (each declaration's comment becomes its ecdoc). What C's types don't say is decided by rules and
tables at the top of `generate.py` (which pointers are arrays, values a function changes, or nullable), so change
those rather than `cimgui.ec`. For a new ImGui version, the script stops at a C type it has no rule for.

`tests/run.sh` (or `tests/run.sh cimgui`) checks the bindings when it finds `cimgui.h` of the same version, installed
or on `C_INCLUDE_PATH`. It checks that every `declare fn` matches its C declaration and that every field of every
struct has its C type (`tools/check-bindings.py --fields`). It checks that `cimgui.ec` is what `generate.py` makes,
when the header's directory is a cimgui checkout. Then it compiles both examples with `-Werror`, and runs
`headless.ec` when `libcimgui` links. Without the header it prints a `skip` line.
