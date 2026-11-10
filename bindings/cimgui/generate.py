#!/usr/bin/env python3
"""Generates cimgui.ec, the eclang binding of Dear ImGui through cimgui, from cimgui's metadata.

cimgui (https://github.com/cimgui/cimgui) wraps Dear ImGui's C++ API in C functions (`igButton`, `ImDrawList_AddLine`)
and describes them, with the structs and enums, in JSON files (generator/output/). This script reads those and
maps the C types to eclang's. It binds the public API, that of imgui.h; imgui_internal.h and the templated helpers
(ImVector's methods, ImPool, ...) are left out. The comment after each declaration in imgui.h becomes its ecdoc.

Where C's type doesn't say enough, rules and the tables below decide:

- `float*`, `int*`, `bool*`, ... parameters are values the function reads and changes (`igSliderFloat("speed"c, mut
  speed, 0f, 10f)`): `mut`, or `out` when the name starts with `out_`. With a NULL default they are optional
  pointers (`p_open: @Bool? = null`, passed as `addrof open`). `const float*` and the like are arrays (`T[]c`).
- `const T*` of a struct followed by a count (`points, num_points`) is an array; other struct pointers are one value.
- Pointer results and fields are never NULL unless NULLABLE_RESULTS or NULLABLE_FIELDS (or, for fields, the type:
  strings, `void*` and function pointers) say they may be.
- Default values carry over (`igButton("OK"c)` has `size = ImVec2 { 0f, 0f }`).
- Overloads keep cimgui's names (`igPushID_Str`, `igPushID_Int`) and also get the plain one (`igPushID`) as eclang
  overloads, unless two would take the same arguments.

Written against cimgui for Dear ImGui 1.93.0. After updating cimgui, run this again: it stops at a C type it has
no rule for, naming the function or field.

Usage: bindings/cimgui/generate.py path/to/cimgui > bindings/cimgui/cimgui.ec
(the directory of a cimgui checkout, with cimgui.h, generator/output/ and imgui/imgui.h)
"""
import json
import os
import re
import sys

# eclang's keywords. Words that only mean something in one position (`extern`, `struct`, `where`, `it`, `here`, ...) aren't.
KEYWORDS = set("""addrof alignof and as assert break catch const continue declare defer destroy do else enum errdefer
extends false fn for if in inline inter is mut new not null offsetof or pass priv rec ret
sizeof throw throws to true try type typeof unsafe use val var volatile when while xor yield""".split())

PRIMITIVES = {
    "bool": "Bool", "char": "Char", "signed char": "Char", "unsigned char": "UChar", "short": "Short",
    "signed short": "Short", "unsigned short": "UShort", "int": "Int", "signed int": "Int", "unsigned int": "UInt",
    "unsigned": "UInt", "float": "Float", "double": "Double", "size_t": "Size",
    # eclang's Long is C's `long`: the same size as `long long` here, but a pointer to one isn't a pointer to the other.
    "signed long long": "Long", "long long": "Long", "unsigned long long": "ULong",
}

# Structs cimgui writes with a `_c` suffix (ImVec2_c), which cimgui.h also names without it (typedef struct ImVec2_c ImVec2).
SUFFIXED = {"ImVec2_c": "ImVec2", "ImVec4_c": "ImVec4", "ImColor_c": "ImColor", "ImTextureRef_c": "ImTextureRef",
            "ImVec2i_c": "ImVec2i", "ImRect_c": "ImRect"}

# Structs used by pointer only: no fields eclang can see.
OPAQUE = {"ImGuiContext", "ImDrawListSharedData", "ImFontAtlasBuilder", "ImFontLoader", "ImGuiTextFilterItem",
          "ImFontBakedSrc"}

# Pointer results that are NULL when there is nothing to return (checked against imgui.cpp and imgui_draw.cpp).
NULLABLE_RESULTS = {
    "igGetCurrentContext", "igGetDrawData", "igGetClipboardText", "igGetDragDropPayload", "igAcceptDragDropPayload",
    "igTableGetSortSpecs", "igTableGetColumnName_Int", "igMemAlloc", "ImFontAtlas_AddFont", "ImFontAtlas_AddFontDefault",
    "ImFontAtlas_AddFontDefaultBitmap", "ImFontAtlas_AddFontDefaultVector", "ImFontAtlas_AddFontFromFileTTF",
    "ImFontAtlas_AddFontFromMemoryTTF", "ImFontAtlas_AddFontFromMemoryCompressedTTF",
    "ImFontAtlas_AddFontFromMemoryCompressedBase85TTF", "ImFontBaked_FindGlyph", "ImFontBaked_FindGlyphNoFallback",
    "ImGuiStorage_GetVoidPtr", "ImTextureData_GetPixels", "ImTextureData_GetPixelsAt", "igBeginMultiSelect",
    "igEndMultiSelect",
}
# Pointer fields of a struct that may be NULL. Strings, `void*` and function pointers always may.
NULLABLE_FIELDS = {
    ("ImGuiIO", "FontDefault"), ("ImGuiIO", "Ctx"), ("ImDrawData", "Textures"), ("ImDrawData", "OwnerViewport"),
    ("ImFontAtlas", "TexData"), ("ImFontAtlas", "TexUvLines"), ("ImFontAtlas", "FontLoader"), ("ImFontAtlas", "Builder"),
    ("ImFontAtlas", "FontLoaderData"), ("ImFontConfig", "GlyphRanges"), ("ImFontConfig", "GlyphExcludeRanges"),
    ("ImFontConfig", "DstFont"), ("ImFontConfig", "FontLoader"), ("ImFontConfig", "FontData"),
    ("ImFontBaked", "ContainerFont"), ("ImFont", "OwnerAtlas"), ("ImFont", "LastBaked"), ("ImFont", "FontLoader"),
    ("ImGuiInputTextCallbackData", "Ctx"), ("ImGuiListClipper", "Ctx"), ("ImGuiListClipper", "TempData"),
    ("ImGuiTableSortSpecs", "Specs"), ("ImGuiMultiSelectIO", "Requests"), ("ImGuiPlatformIO", "Renderer_RenderState"),
    ("ImTextureData", "Pixels"), ("ImDrawList", "_OwnerName"),
}

# Functions left out: they need a C va_list, which eclang can't make, or a `void**`.
SKIP_FUNCTIONS = {"igGetAllocatorFunctions", "ImGuiStorage_GetVoidPtrRef"}


def fail(message):
    sys.exit(f"{sys.argv[0]}: {message}")


class Generator:
    def __init__(self, root):
        out = os.path.join(root, "generator", "output")
        with open(os.path.join(out, "definitions.json")) as f:
            self.definitions = json.load(f)
        with open(os.path.join(out, "structs_and_enums.json")) as f:
            se = json.load(f)
        with open(os.path.join(out, "typedefs_dict.json")) as f:
            self.typedefs = json.load(f)
        with open(os.path.join(root, "imgui", "imgui.h")) as f:
            self.imgui_h = f.read().splitlines()
        with open(os.path.join(root, "cimgui.h")) as f:
            m = re.search(r'based on imgui\.h file version "([^"]+)" (\d+)', f.read())
        if not m:
            fail("cimgui.h doesn't say which version of imgui.h it was made from")
        self.version = m.group(1)
        self.locations = se["locations"]
        self.structs = {k: v for k, v in se["structs"].items() if self.public(k)}
        self.enums = {k: v for k, v in se["enums"].items() if self.public(k)}
        # Enums whose name is also their type (`typedef enum { ... } ImGuiKey;`), and the int typedefs of the others.
        self.enum_types = {}
        for e in self.enums:
            self.enum_types[e] = e[:-1] if e.endswith("_") else e
        self.problems = []
        self.used_vectors = {}  # ImVector_X -> element type (C)
        self.used_typedefs = set()
        self.fn_types = {}  # C typedef name of a function pointer -> eclang type

    def public(self, name):
        return self.locations.get(name, "").startswith("imgui:")

    def doc(self, location):
        """The comment after a declaration in imgui.h (its line, from `imgui:LINE`), or None."""
        m = re.match(r"imgui:(\d+)$", location or "")
        if not m:
            return None
        line = self.imgui_h[int(m.group(1)) - 1]
        if "//" not in line:
            return None
        text = line.split("//", 1)[1].strip().replace("*/", "* /")
        if not text:
            return None
        return "`" + text + "`" if text.startswith("@") else text

    # --- types -------------------------------------------------------------------------------------------------

    def resolve_typedef(self, name):
        """The primitive an integer typedef stands for (ImGuiWindowFlags -> int), or None."""
        seen = 0
        t = name
        while t in self.typedefs and seen < 10:
            t = self.typedefs[t].strip().rstrip(";")
            seen += 1
        return t if t in PRIMITIVES else None

    def base(self, c, where):
        """The eclang type of a C type without pointers: a primitive, struct, typedef or enum."""
        c = c.replace("const ", "").strip()
        c = SUFFIXED.get(c, c)
        if c in PRIMITIVES:
            return PRIMITIVES[c]
        if c in self.structs or c in OPAQUE:
            return c
        if c.startswith("ImVector_"):
            return self.vector(c, where)
        if c in self.enum_types.values():
            return c
        if c in self.typedefs:
            value = self.typedefs[c].strip()
            if "(*)" in value:
                self.fn_types[c] = self.function_type(value.rstrip(";"), where)
                return c
            if self.resolve_typedef(c):
                self.used_typedefs.add(c)
                return c
        self.problems.append(f"{where}: no rule for the C type `{c}`")
        return "Any"

    def vector(self, c, where):
        """ImVector_X, the struct cimgui writes for an ImVector<X>: Size, Capacity and Data."""
        element = c[len("ImVector_"):]
        if element.endswith("Ptr"):
            element = element[:-3] + "*"
        if c not in self.used_vectors:
            self.used_vectors[c] = element
            self.type(element, f"{where} ({c})")
        return c

    def function_type(self, c, where):
        """`ret (*)(T a, U b)` -> `(a: T, b: U) => Ret`, a C function pointer type."""
        m = re.fullmatch(r"(.+?)\s*\(\*\)\s*\((.*)\)", c.strip())
        if not m:
            self.problems.append(f"{where}: can't read the function pointer type `{c}`")
            return "Any"
        params = []
        for i, p in enumerate(x.strip() for x in m.group(2).split(",") if x.strip() and x.strip() != "void"):
            pm = re.fullmatch(r"(.*?[\s*])(\w+)", p)
            ctype, name = (pm.group(1), pm.group(2)) if pm else (p, f"p{i}")
            params.append(f"{self.safe(name)}: {self.type(ctype, where, nullable_pointer=True)}")
        ret = self.type(m.group(1), where, nullable_pointer=True)
        return f"({', '.join(params)}) => {'None' if ret == 'None' else ret}"

    def type(self, c, where, nullable_pointer=False):
        """The eclang type of a C type, for a field, function pointer or result: pointers become `@T` (or `T[]c`)."""
        c = c.strip()
        if c == "void":
            return "None"
        if "(*)" in c:
            return f"({self.function_type(c, where)})?"
        stars = c.count("*")
        bare = c.replace("*", "").replace("const ", "").strip()
        if stars == 0:
            t = self.base(bare, where)
            return t + "?" if t in self.fn_types and nullable_pointer else t
        q = "?" if nullable_pointer else ""
        if stars == 1:
            if bare == "char":
                return "CString" + q
            if bare == "void":
                return "@Any?"
            t = self.base(bare, where)
            if self.number(t, bare):
                return f"{t}[]c" + q
            return f"@{t}" + q
        if stars == 2 and bare not in ("void", "char"):
            return f"@{self.base(bare, where)}[]c" + q
        self.problems.append(f"{where}: no rule for the C type `{c}`")
        return "Any"

    def number(self, t, bare):
        """Whether a C type (bare, its eclang type t) is a number: a primitive, an integer typedef or an enum."""
        return t in PRIMITIVES.values() or bare in self.used_typedefs or bare in self.enum_types.values()

    # --- declarations ------------------------------------------------------------------------------------------

    @staticmethod
    def safe(name):
        return name + "_" if name in KEYWORDS else name

    def field(self, struct, f):
        name = f["name"]
        size = f.get("size")
        if size is not None:
            name = name.split("[", 1)[0]
        if name.startswith("_") or f["type"].startswith("union") or f["type"].startswith("ImSmallStack"):
            return None  # Private (ImGui's own use), or a C union eclang doesn't declare here.
        where = f"{struct}.{name}"
        c = f["type"]
        if "(*)" in c:
            t = f"({self.function_type(c, where)})?"
        else:
            nullable = (struct, name) in NULLABLE_FIELDS
            pointer = c.replace("const ", "").strip() == "char*" or c.strip() in self.typedefs and "(*)" in self.typedefs[c.strip()]
            t = self.type(c, where, nullable_pointer=nullable or pointer)
            if size is not None:
                t = f"{t}[{size}]"
        bits = f" : {f['bitfield']}" if f.get("bitfield") else ""
        declared = f"{name}_ extern \"{name}\"" if name in KEYWORDS else name
        return f"    {declared}: {t}{bits}"

    def default(self, value, t):
        """An eclang expression for a C++ default value of a parameter of eclang type t."""
        v = value.strip()
        if v in ("NULL", "((void*)0)", "nullptr"):
            return "null"
        if v in ("true", "false"):
            return v
        if v.startswith('"'):
            return v + "c"
        if v == "sizeof(float)":
            return "sizeof(Float) as Int"
        if v in ("FLT_MAX", "-FLT_MAX", "FLT_MIN", "-FLT_MIN"):
            return v
        m = re.fullmatch(r"ImVec([24])\((.*)\)", v)
        if m:
            parts = [self.default(p, "Float") for p in m.group(2).split(",")]
            return f"ImVec{m.group(1)} {{ {', '.join(parts)} }}"
        m = re.fullmatch(r"([+-]?)(\d+\.?\d*|\.\d+)(?:f)?", v)
        if m:
            sign = "-" if m.group(1) == "-" else ""
            number = m.group(2)
            if t in ("Float", "Double"):
                text = number if "." in number else number + ".0"
                text = text + "0" if text.endswith(".") else text
                return f"{sign}{text}{'f' if t == 'Float' else ''}"
            return sign + number
        if re.fullmatch(r"Im\w+", v):
            return v
        self.problems.append(f"no rule for the default value `{value}`")
        return v

    def params(self, o):
        """The eclang parameters of a function, or None to leave it out."""
        name = o["ov_cimguiname"]
        args = o["argsT"]
        out = []
        for i, a in enumerate(args):
            c, pname = a["type"].strip(), self.safe(a["name"])
            where = f"{name}({a['name']})"
            if c == "...":
                out.append("...")
                continue
            if c == "va_list" or "ImVector_T" in c or c in ("const T", "const T*", "T*") or "ImSmallStack" in c or c == "ImVector*" or c == "void**":
                return None
            default = o["defaults"].get(a["name"])
            following = args[i + 1]["name"] if i + 1 < len(args) else ""
            m = re.fullmatch(r"(.*)\[(\d*)\]", c)
            if m:  # float v[3], const char* const items[]
                element = self.type(m.group(1).replace(" const", ""), where)
                out.append(f"{pname}: {element}[]c")
                continue
            if "(*)" in c:
                t = self.function_type(c, where)
                out.append(f"{pname}: ({t})? = null" if default else f"{pname}: {t}")
                continue
            stars = c.count("*")
            const = c.startswith("const ")
            bare = c.replace("*", "").replace("const ", "").strip()
            optional = default in ("NULL", "((void*)0)")
            text = None
            if stars == 0:
                t = self.base(bare, where)
                text = f"{pname}: {t}" + ("?" if optional and t in self.fn_types else "")
            elif stars == 1 and bare == "char":
                text = f"{pname}: CString" + ("?" if optional else "") if const else f"{pname}: Char[]c"
            elif stars == 1 and bare == "void":
                text = f"{pname}: @Any?"
            elif stars == 1:
                t = self.base(bare, where)
                primitive = self.number(t, bare)
                if a["name"] == "self":
                    text = f"{pname}: @{t}"
                elif primitive and const:
                    text = f"{pname}: {t}[]c" + ("?" if optional else "")
                elif optional:
                    text = f"{pname}: @{t}?"
                elif primitive or (bare in ("ImVec2", "ImVec4", "ImColor") and not const):
                    mode = "out" if a["name"].startswith("out_") or a["name"] == "pOut" else "mut"
                    text = f"{mode} {pname}: {t}"
                elif const and (following.startswith("num_") or following.endswith("_count") or following == "count"):
                    text = f"{pname}: {t}[]c"
                else:
                    text = f"{pname}: @{t}"
            elif stars == 2 and bare == "char" and optional:
                text = f"{pname}: @CString?"
            if text is None:
                self.problems.append(f"{where}: no rule for the C parameter type `{c}`")
                return None
            if default is not None and not text.startswith(("mut ", "out ")):
                eclang_type = text.split(": ", 1)[1]
                text += f" = {self.default(default, eclang_type)}"
            out.append(text)
        return out

    def result(self, o):
        name = o["ov_cimguiname"]
        if o.get("constructor"):
            return f"@{o['stname']}"
        r = o.get("ret", "void").strip()
        if r == "void**":
            return None
        t = self.type(r, f"{name}(result)", nullable_pointer=name in NULLABLE_RESULTS)
        return t

    def functions(self):
        """(location line, eclang declarations) per public function."""
        out = []
        by_alias = {}  # plain name -> list of parameter type lists it already takes
        for overloads in self.definitions.values():
            for o in overloads:
                name = o["ov_cimguiname"]
                if not (o.get("location") or "").startswith("imgui:") and not o.get("destructor"):
                    continue
                if o.get("destructor") and not self.public(o["stname"]):
                    continue
                if o.get("templated") or o.get("isVARG0") or name in SKIP_FUNCTIONS:
                    continue
                if o["stname"] and o["stname"] not in self.structs:
                    continue
                params = self.params(o)
                if params is None:
                    continue
                result = self.result(o)
                if result is None:
                    continue
                ret = "" if result == "None" else f": {result}"
                line = int(o["location"].split(":")[1]) if o.get("location") else 0
                lines = []
                doc = self.doc(o.get("location"))
                if o.get("constructor"):
                    doc = f"A new {o['stname']} with ImGui's defaults, allocated by ImGui; free it with {o['stname']}_destroy."
                elif o.get("destructor"):
                    doc = f"Frees a {o['stname']} made by its constructor."
                if doc:
                    lines.append(f"/** {doc} */")
                lines.append(f"declare fn {name} as ({', '.join(params)}){ret}")
                # The plain name of an overload (igPushID for igPushID_Str), unless an earlier one takes the same arguments.
                plain = o["cimguiname"] if o.get("constructor") is None else None
                if plain and plain != name and not o.get("destructor"):
                    signatures = self.signatures(params)
                    taken = by_alias.setdefault(plain, [])
                    if not any(s in taken for s in signatures):
                        taken.extend(signatures)
                        lines.append(f"declare fn {plain} extern \"{name}\" as ({', '.join(params)}){ret}")
                out.append((line, name, lines))
        return out

    @staticmethod
    def signatures(params):
        """The parameter type lists a call can give: each way of leaving out trailing parameters with defaults."""
        types = [p.split(": ", 1)[1].split(" = ")[0] if p != "..." else "..." for p in params]
        modes = [p.split()[0] if p.split()[0] in ("mut", "out") else "" for p in params]
        full = [m + t for m, t in zip(modes, types)]
        required = len(params)
        while required > 0 and " = " in params[required - 1]:
            required -= 1
        return [tuple(full[:n]) for n in range(required, len(params) + 1)]

    # --- output --------------------------------------------------------------------------------------------------

    def generate(self):
        functions = self.functions()
        structs = []
        for s in sorted(self.structs, key=lambda k: int(self.locations[k].split(":")[1])):
            fields = [x for x in (self.field(s, f) for f in self.structs[s]) if x]
            structs.append((s, fields))
        if self.problems:
            fail("\n".join(sorted(set(self.problems))))

        out = []
        w = out.append
        w("/**")
        w(f" * Bindings for Dear ImGui {self.version} through cimgui, its C API: immediate-mode user interfaces.")
        w(" * https://github.com/ocornut/imgui, https://github.com/cimgui/cimgui")
        w(" *")
        w(" * Generated from cimgui's metadata by bindings/cimgui/generate.py; run it again rather than changing this")
        w(" * file. Names are cimgui's: ImGui::Button is igButton, ImDrawList::AddLine is ImDrawList_AddLine (taking the")
        w(" * list first). Overloads that cimgui names apart (igPushID_Str, igPushID_Int) are also eclang overloads of the")
        w(" * plain name (igPushID). See README.md.")
        w(" */")
        w('use "define:CIMGUI_DEFINE_ENUMS_AND_STRUCTS"')
        w('use "cimgui.h"')
        w('use "float.h"')
        w('use "lib:cimgui"')
        w("")
        w("/** The largest Float (C's FLT_MAX): ImGui's \"no limit\". */")
        w("declare val FLT_MAX as Float")
        w("/** The smallest positive normal Float (C's FLT_MIN): a width of -FLT_MIN means \"to the right edge\". */")
        w("declare val FLT_MIN as Float")

        w("")
        w("// " + "-" * 117)
        w("// Types")
        w("// " + "-" * 117)
        w("")
        for t in sorted(self.used_typedefs):
            w(f"declare type {t} as {PRIMITIVES[self.resolve_typedef(t)]}")
        for t, value in sorted(self.fn_types.items()):
            w(f"declare type {t} as {value}")
        for t in sorted(OPAQUE):
            w(f"declare rec {t}")
        for v, element in sorted(self.used_vectors.items()):
            et = self.type(element, v)
            w(f"/** An ImVector<{element}>: Size elements of Data (null while empty), with room for Capacity. */")
            w(f"declare mut rec {v} as")
            w("    Size: Int")
            w("    Capacity: Int")
            w(f"    Data: {et}[]c?")

        w("")
        w("// " + "-" * 117)
        w("// Enums: numbers named as in C, of the int type ImGui gives them (ImGuiWindowFlags), combined with `|`")
        w("// " + "-" * 117)
        for e in sorted(self.enums, key=lambda k: int(self.locations[k].split(":")[1])):
            t = self.enum_types[e]
            w("")
            doc = self.doc(self.locations[e])
            if doc:
                w(f"// {doc}")
            if t == e or t not in self.used_typedefs:
                if t not in self.used_typedefs:
                    w(f"declare type {t} as Int")
            for member in self.enums[e]:
                w(f"declare val {member['name']} as {t}")

        w("")
        w("// " + "-" * 117)
        w("// Structs")
        w("// " + "-" * 117)
        for s, fields in structs:
            w("")
            doc = self.doc(self.locations[s])
            if doc:
                w(f"/** {doc} */")
            w(f"declare mut rec {s}" + (" as" if fields else ""))
            for f in fields:
                w(f)

        w("")
        w("// " + "-" * 117)
        w("// Functions, in the order of imgui.h")
        w("// " + "-" * 117)
        for _, _, lines in sorted(functions, key=lambda x: (x[0], x[1])):
            for line in lines:
                w(line)
        if self.problems:
            fail("\n".join(sorted(set(self.problems))))
        print("\n".join(out))


def main():
    if len(sys.argv) != 2:
        fail("usage: generate.py path/to/cimgui > cimgui.ec")
    Generator(sys.argv[1]).generate()


main()
