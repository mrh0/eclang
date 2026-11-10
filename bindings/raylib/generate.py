#!/usr/bin/env python3
"""Generates raylib.ec, the eclang binding of raylib.h, from the header.

The header gives the structs, enums, callback types and functions with their comments; this script maps their C
types to eclang's. Where C's type doesn't say enough (is an `int` an enum? is a `T*` one value, an array, or
something the function fills in? can a result be NULL?), the tables below decide. They were written against
raylib 5.5: after updating raylib, run this again and check what `tools/check-bindings.py` says about the result.

Usage: bindings/raylib/generate.py path/to/raylib.h > bindings/raylib/raylib.ec
"""
import re
import sys

# eclang's keywords. Words that only mean something in one position (`extern`, `struct`, `where`, `it`, `here`, ...) aren't.
KEYWORDS = set("""addrof alignof and as assert break catch const continue declare defer destroy do else enum errdefer
extends false fn for if in inline inter is mut new not null offsetof or pass priv rec ret
sizeof throw throws to true try type typeof unsafe use val var volatile when while xor yield""".split())

PRIMITIVES = {
    "bool": "Bool", "char": "Char", "unsigned char": "UChar", "short": "Short", "unsigned short": "UShort",
    "int": "Int", "unsigned int": "UInt", "long": "Long", "unsigned long": "ULong", "float": "Float", "double": "Double",
}

# C enums whose values eclang code passes as atoms (`IsKeyDown(:space)`): the enum and the prefix its atoms drop.
ATOM_ENUMS = {
    "KeyboardKey": "KEY_", "MouseButton": "MOUSE_BUTTON_", "MouseCursor": "MOUSE_CURSOR_",
    "GamepadButton": "GAMEPAD_BUTTON_", "GamepadAxis": "GAMEPAD_AXIS_", "TraceLogLevel": "LOG_",
    "PixelFormat": "PIXELFORMAT_", "TextureFilter": "TEXTURE_FILTER_", "TextureWrap": "TEXTURE_WRAP_",
    "CubemapLayout": "CUBEMAP_LAYOUT_", "FontType": "FONT_", "BlendMode": "BLEND_", "CameraMode": "CAMERA_",
    "ShaderUniformDataType": "SHADER_UNIFORM_",
}
# C enums that stay numbers: bit flags (combined with `|`), and values stored in struct fields or used as indexes,
# which eclang doesn't convert from atoms. PixelFormat is both: an atom for functions, a number in Image.format.
VALUE_ENUMS = {
    "ConfigFlags": "UInt", "Gesture": "UInt", "MaterialMapIndex": "Int", "ShaderLocationIndex": "Int",
    "ShaderAttributeDataType": "Int", "CameraProjection": "Int", "NPatchLayout": "Int", "PixelFormat": "Int",
}

# `int` parameters that are one value of an enum, by (function, parameter); "*" matches any function.
ENUM_PARAMS = {
    ("*", "key"): "KeyboardKey",
    ("IsMouseButtonPressed", "button"): "MouseButton", ("IsMouseButtonDown", "button"): "MouseButton",
    ("IsMouseButtonReleased", "button"): "MouseButton", ("IsMouseButtonUp", "button"): "MouseButton",
    ("IsGamepadButtonPressed", "button"): "GamepadButton", ("IsGamepadButtonDown", "button"): "GamepadButton",
    ("IsGamepadButtonReleased", "button"): "GamepadButton", ("IsGamepadButtonUp", "button"): "GamepadButton",
    ("GetGamepadAxisMovement", "axis"): "GamepadAxis",
    ("SetMouseCursor", "cursor"): "MouseCursor",
    ("*", "logLevel"): "TraceLogLevel",
    ("UpdateCamera", "mode"): "CameraMode",
    ("BeginBlendMode", "mode"): "BlendMode",
    ("*", "uniformType"): "ShaderUniformDataType",
    ("LoadImageRaw", "format"): "PixelFormat", ("ImageFormat", "newFormat"): "PixelFormat",
    ("GetPixelColor", "format"): "PixelFormat", ("SetPixelColor", "format"): "PixelFormat",
    ("GetPixelDataSize", "format"): "PixelFormat",
    ("SetTextureFilter", "filter"): "TextureFilter", ("SetTextureWrap", "wrap"): "TextureWrap",
    ("LoadTextureCubemap", "layout"): "CubemapLayout",
    ("LoadFontData", "type"): "FontType",
}
# Functions taking a key that also get an overload taking the key's number (as GetKeyPressed returns it).
KEY_NUMBER_OVERLOADS = {"IsKeyPressed", "IsKeyPressedRepeat", "IsKeyDown", "IsKeyReleased", "IsKeyUp", "SetExitKey"}

# Pointer parameters, by (function, parameter) or ("*", parameter): the eclang parameter, with its mode word
# (`mut`: C reads and changes it, `out`: C only writes it, `escaping`: C keeps the pointer).
POINTER_PARAMS = {
    ("*", "dataSize"): "out dataSize: Int", ("*", "compDataSize"): "out compDataSize: Int",
    ("*", "outputSize"): "out outputSize: Int", ("*", "fileSize"): "out fileSize: Int",
    ("*", "frames"): "out frames: Int", ("*", "colorCount"): "out colorCount: Int",
    ("*", "count"): "out count: Int", ("*", "codepointSize"): "out codepointSize: Int",
    ("*", "utf8Size"): "out utf8Size: Int", ("*", "materialCount"): "out materialCount: Int",
    ("*", "animCount"): "out animCount: Int",
    ("TextAppend", "position"): "mut position: Int",
    ("TextAppend", "text"): "text: Char[]c", ("TextCopy", "dst"): "dst: Char[]c",
    ("CheckCollisionLines", "collisionPoint"): "mut collisionPoint: Vector2",
    ("SetAutomationEventList", "list"): "escaping list: @AutomationEventList",
    ("GenImageFontAtlas", "glyphRecs"): "out glyphRecs: Rectangle[]c",
    ("LoadShader", "vsFileName"): "vsFileName: CString?", ("LoadShader", "fsFileName"): "fsFileName: CString?",
    ("LoadShaderFromMemory", "vsCode"): "vsCode: CString?", ("LoadShaderFromMemory", "fsCode"): "fsCode: CString?",
    ("LoadDirectoryFilesEx", "filter"): "filter: CString?",
    ("LoadFontEx", "codepoints"): "codepoints: Int[]c?", ("LoadFontFromMemory", "codepoints"): "codepoints: Int[]c?",
    ("LoadFontData", "codepoints"): "codepoints: Int[]c?",
    ("MemRealloc", "ptr"): "ptr: @Any?", ("MemFree", "ptr"): "ptr: @Any?",
}
# A struct pointer with one of these names is a value the function changes in place (`ImageCrop(mut image, ...)`).
MUT_STRUCT_PARAMS = {"image", "dst", "camera", "texture", "mesh", "wave", "material", "model"}

# Pointer results that are NULL when the function fails (checked against raylib 5.5's sources).
NULLABLE_RESULTS = {
    "GetWindowHandle", "GetClipboardText", "LoadRandomSequence", "MemAlloc", "MemRealloc", "LoadFileData",
    "LoadFileText", "GetFileExtension", "CompressData", "DecompressData", "EncodeDataBase64", "DecodeDataBase64",
    "ExportImageToMemory", "LoadImageColors", "LoadImagePalette", "LoadFontData", "TextReplace", "LoadMaterials",
    "LoadModelAnimations",
}
# Pointer fields that a loaded value always has; other pointer fields may be NULL (a mesh without normals, an image
# that failed to load).
NON_NULL_FIELDS = {
    ("Font", "recs"), ("Font", "glyphs"), ("Shader", "locs"), ("Material", "maps"), ("Model", "meshes"),
    ("Model", "materials"), ("Model", "meshMaterial"), ("ModelAnimation", "bones"),
    ("ModelAnimation", "framePoses"), ("FilePathList", "paths"), ("AutomationEventList", "events"),
}

# Callback types, written by hand: C's function pointer syntax isn't worth parsing for five lines.
CALLBACKS = {
    "LoadFileDataCallback": "(fileName: CString, dataSize: @Int) => UChar[]c?",
    "SaveFileDataCallback": "(fileName: CString, data: @Any, dataSize: Int) => Bool",
    "LoadFileTextCallback": "(fileName: CString) => CString?",
    "SaveFileTextCallback": "(fileName: CString, text: CString) => Bool",
    "AudioCallback": "(bufferData: @Any, frames: UInt) => None",
}
# The header's macros that are values (other than colors).
CONSTANTS = [
    ("RAYLIB_VERSION_MAJOR", "Int", ""), ("RAYLIB_VERSION_MINOR", "Int", ""), ("RAYLIB_VERSION_PATCH", "Int", ""),
    ("RAYLIB_VERSION", "CString", ""),
    ("PI", "Float", ""), ("DEG2RAD", "Float", "Multiply degrees by it to get radians"),
    ("RAD2DEG", "Float", "Multiply radians by it to get degrees"),
]

# Left out: they need a C va_list, which eclang can't make or read.
SKIPPED = {"SetTraceLogCallback"}


def doc(comment, indent=""):
    comment = comment.strip().rstrip(".")
    return f"{indent}/** {comment} */\n" if comment else ""


def safe_name(name):
    return name + "_" if name in KEYWORDS else name


def split_decl(decl):
    """'const unsigned char *data' -> ('unsigned char', 1, 'data'); 'float params[4]' -> ('float', 0, 'params[4]')."""
    decl = re.sub(r"\bconst\b", "", decl).strip()
    m = re.match(r"^(.*?)([\s*]+)(\w+(?:\[\d+\])?)$", decl)
    base, stars, name = m.group(1).strip(), m.group(2).count("*"), m.group(3)
    return " ".join(base.split()), stars, name


class Binding:
    def __init__(self, header):
        self.header = header
        self.structs = []       # (name, [(field decl, comment)], comment)
        self.opaque = []
        self.aliases = []       # (alias, struct, comment)
        self.enums = []         # (name, [(member, comment)], comment)
        self.colors = []        # (name, comment)
        self.out = []

    def parse(self):
        h = self.header
        for m in re.finditer(r"^#define (\w+)\s+CLITERAL\(Color\)\{[^}]*\}\s*//\s*(.*)$", h, re.M):
            self.colors.append((m.group(1), m.group(2)))
        for m in re.finditer(r"(?:^// ([^\n]*)\n)?typedef struct (\w+) \{([^}]*)\} \2;", h, re.M):
            fields = []
            for line in m.group(3).split("\n"):
                f = re.match(r"^\s*([^/;]+);\s*(?://\s*(.*))?$", line)
                if f:
                    fields.append((f.group(1).strip(), f.group(2) or ""))
            self.structs.append((m.group(2), fields, m.group(1) or ""))
        self.opaque = re.findall(r"^typedef struct (\w+) \1;", h, re.M)
        names = {s[0] for s in self.structs}
        for m in re.finditer(r"^typedef (\w+) (\w+);\s*(?://\s*(.*))?$", h, re.M):
            if m.group(1) in names:
                self.aliases.append((m.group(2), m.group(1), m.group(3) or ""))
        for m in re.finditer(r"(?:^// ([^\n]*)\n)?(?://[^\n]*\n)*typedef enum \{([^}]*)\} (\w+);", h, re.M):
            members = []
            for line in m.group(2).split("\n"):
                e = re.match(r"^\s*(\w+)\s*(?:=[^,/]*)?,?\s*(?://\s*(.*))?$", line)
                if e:
                    members.append((e.group(1), e.group(2) or ""))
            self.enums.append((m.group(3), members, m.group(1) or ""))

    def emit(self, text=""):
        self.out.append(text)

    # Types

    def field_type(self, struct, decl):
        base, stars, name = split_decl(decl)
        size = re.search(r"\[(\d+)\]$", name)
        name = re.sub(r"\[\d+\]$", "", name)
        if stars == 0:
            t = PRIMITIVES.get(base, base)
        elif base == "void":
            t = "@Any"
        elif base in self.opaque:
            t = f"@{base}"
        elif base == "char" and stars == 2:
            t = "CString[]c"
        else:
            t = PRIMITIVES.get(base, base) + "[]c" * stars
        if stars and (struct, name) not in NON_NULL_FIELDS:
            t += "?"
        if size:
            t += f"[{size.group(1)}]"
        cname = name
        name = safe_name(name)
        return (f'{name} extern "{cname}"' if name != cname else name), t

    def param(self, fn, decl):
        base, stars, name = split_decl(decl)
        enum = ENUM_PARAMS.get((fn, name)) or ENUM_PARAMS.get(("*", name))
        if enum and base == "int" and stars == 0:
            return f"{safe_name(name)}: {enum}"
        if stars == 0:
            return f"{safe_name(name)}: {PRIMITIVES.get(base, base)}"
        given = POINTER_PARAMS.get((fn, name)) or POINTER_PARAMS.get(("*", name))
        if given:
            return given
        if base == "char" and stars == 1:
            return f"{safe_name(name)}: CString"
        if base == "char" and stars == 2:
            return f"{safe_name(name)}: CString[]c"
        if base == "void":
            return f"{safe_name(name)}: @Any"
        if stars == 1 and name in MUT_STRUCT_PARAMS and base not in PRIMITIVES:
            return f"mut {safe_name(name)}: {base}"
        if stars == 1:  # an array (with a count parameter next to it)
            return f"{safe_name(name)}: {PRIMITIVES.get(base, base)}[]c"
        raise SystemExit(f"generate.py: no rule for parameter `{decl}` of {fn}; add one to POINTER_PARAMS")

    def result(self, fn, ret):
        base, stars, _ = split_decl(ret + " x")
        if stars == 0:
            return "" if base == "void" else PRIMITIVES.get(base, base)
        if base == "void":
            t = "@Any"
        elif base == "char":
            t = "CString" + "[]c" * (stars - 1)
        else:
            t = PRIMITIVES.get(base, base) + "[]c" * stars
        return t + "?" if fn in NULLABLE_RESULTS else t

    # Sections

    def types(self):
        self.emit("// ---------------------------------------------------------------------------------------------------------------------")
        self.emit("// Structures")
        self.emit("// ---------------------------------------------------------------------------------------------------------------------")
        self.emit()
        for name in self.opaque:
            self.emit(f"/** Opaque: defined inside raylib */\ndeclare rec {name}")
        self.emit()
        aliases = {}
        for alias, struct, comment in self.aliases:
            aliases.setdefault(struct, []).append((alias, comment))
        for name, fields, comment in self.structs:
            self.emit(doc(comment) + f"declare rec {name} as")
            for decl, fcomment in fields:
                # `float m0, m4, m8, m12;`: one type, several fields
                first, *more = [p.strip() for p in decl.split(",")]
                prefix = first[: len(first) - len(re.search(r"[\s*]*\w+(\[\d+\])?$", first).group(0))]
                for i, d in enumerate([first] + [prefix + " " + p for p in more]):
                    fname, ftype = self.field_type(name, d)
                    self.emit((doc(fcomment, "    ") if i == 0 else "") + f"    {fname}: {ftype}")
            for alias, acomment in aliases.get(name, []):
                self.emit(doc(acomment) + f"declare type {alias} as {name}")
            self.emit()

    def constants(self):
        """The header's macros that are values. Declaring them also tells the compiler that C has these names."""
        self.emit("// Constants")
        for name, kind, comment in CONSTANTS:
            if not re.search(rf"#define {name}\b", self.header):
                raise SystemExit(f"generate.py: raylib.h no longer defines {name}")
            self.emit(doc(comment) + f"declare val {name} as {kind}")
        self.emit()

    def colors_section(self):
        self.emit("// Colors (raylib's palette)")
        for name, comment in self.colors:
            self.emit(doc(comment) + f"declare val {name} as Color")
        self.emit()

    def enums_section(self):
        self.emit("// ---------------------------------------------------------------------------------------------------------------------")
        self.emit("// Enumerators")
        self.emit("//")
        self.emit("// Functions take most enums as atoms: IsKeyDown(:space), SetTraceLogLevel(:warning), UpdateCamera(mut camera, :orbital).")
        self.emit("// Flags, which combine with |, and values that structs store or index with, are numbers named as in C:")
        self.emit("// SetConfigFlags(FLAG_VSYNC_HINT | FLAG_WINDOW_RESIZABLE), camera.projection = CAMERA_PERSPECTIVE.")
        self.emit("// ---------------------------------------------------------------------------------------------------------------------")
        self.emit()
        aliases = re.findall(r"^#define (\w+)\s+(\w+)\s*$", self.header, re.M)
        for name, members, comment in self.enums:
            if name in ATOM_ENUMS:
                prefix = ATOM_ENUMS[name]
                self.emit(doc(comment) + f"declare enum {name} as")
                for member, mcomment in members:
                    self.emit(doc(mcomment, "    ") + f"    :{member[len(prefix):].lower()} = {member}")
                self.emit()
            if name in VALUE_ENUMS:
                kind = VALUE_ENUMS[name]
                self.emit(f"// {name}" + (f": {comment}" if comment else ""))
                names = {m for m, _ in members}
                for member, mcomment in members:
                    self.emit(doc(mcomment) + f"declare val {member} as {kind}")
                for alias, target in aliases:
                    if target in names:
                        self.emit(f"/** Same as {target} */\ndeclare val {alias} as {kind}")
                self.emit()
            if name not in ATOM_ENUMS and name not in VALUE_ENUMS:
                raise SystemExit(f"generate.py: decide how to bind the enum {name} (ATOM_ENUMS or VALUE_ENUMS)")

    def callbacks(self):
        self.emit("// Callbacks. An eclang function given to C is a plain C function pointer: `fn name`, or a lambda that uses no")
        self.emit("// local variables. AudioCallback runs on raylib's audio thread.")
        for name, sig in CALLBACKS.items():
            self.emit(f"declare type {name} as {sig}")
        self.emit()

    def functions(self):
        start = self.header.index("// Window and Graphics Device Functions (Module: core)")
        region = self.header[start:].split("\n")
        for line in region:
            line = line.strip()
            if line.startswith("//---") or line.startswith("#") or line in ("", "}", "#endif"):
                continue
            if line.startswith("extern \"C\"") or line.startswith("typedef"):
                continue
            if line.startswith("//"):
                text = line[2:].strip()
                if "(Module:" in text:
                    self.emit()
                    self.emit("// ---------------------------------------------------------------------------------------------------------------------")
                    self.emit(f"// {text}")
                    self.emit("// ---------------------------------------------------------------------------------------------------------------------")
                else:
                    self.emit()
                    self.emit(f"// {text}")
                continue
            m = re.match(r"^RLAPI (.*?)\b(\w+)\((.*)\);\s*(?://\s*(.*))?$", line)
            if not m:
                raise SystemExit(f"generate.py: can't read `{line}`")
            ret, name, params, comment = m.group(1).strip(), m.group(2), m.group(3).strip(), m.group(4) or ""
            if name in SKIPPED:
                self.emit(f"// {name} is left out: its callback takes a C va_list.")
                continue
            if params in ("void", ""):
                plist = []
            else:
                plist = [p.strip() for p in params.split(",")]
            ec = ["..." if p == "..." else self.param(name, p) for p in plist]
            r = self.result(name, ret)
            line = doc(comment) + f"declare fn {name} as ({', '.join(ec)})" + (f": {r}" if r else "")
            self.emit(line)
            if name in KEY_NUMBER_OVERLOADS:
                self.emit(f"/** {name} with the key's number, as GetKeyPressed returns it */")
                self.emit(f"declare fn {name} as ({', '.join(e.replace('KeyboardKey', 'Int') for e in ec)})" + (f": {r}" if r else ""))
            if name == "GetCharPressed":
                self.emit("/** The number of a key, to compare with what GetKeyPressed returns: GetKeyPressed() == KeyCode(:enter). (C's abs, which")
                self.emit(" * returns a key's number unchanged, is just the way to get it: the call converts the atom to its C constant.) */")
                self.emit('declare fn KeyCode extern "abs" as (key: KeyboardKey): Int')

    def generate(self):
        version = re.search(r'#define RAYLIB_VERSION\s+"([^"]+)"', self.header).group(1)
        self.emit("/**")
        self.emit(f" * Bindings for raylib {version} (raylib.h), a library for making games: https://www.raylib.com")
        self.emit(" *")
        self.emit(" * Generated from raylib.h by bindings/raylib/generate.py; change that script, not this file. The names are")
        self.emit(" * raylib's own, so its cheatsheet (https://www.raylib.com/cheatsheet/cheatsheet.html) and examples apply. See")
        self.emit(" * bindings/raylib/README.md for how C's types map to eclang's.")
        self.emit(" */")
        self.emit('use "raylib.h"')
        self.emit('use "lib:raylib"')
        self.emit()
        self.types()
        self.constants()
        self.colors_section()
        self.enums_section()
        self.callbacks()
        self.functions()
        text = "\n".join(self.out)
        text = re.sub(r"\n{3,}", "\n\n", text).strip() + "\n"
        return text


def main():
    with open(sys.argv[1]) as f:
        b = Binding(f.read())
    b.parse()
    sys.stdout.write(b.generate())


main()
