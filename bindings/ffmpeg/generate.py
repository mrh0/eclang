#!/usr/bin/env python3
"""Generates constants.ec, the enums and #define constants of the FFmpeg bindings, from FFmpeg's headers.

The rest of the bindings (structs, functions, helpers) is written by hand in avutil.ec, avcodec.ec, avformat.ec,
swscale.ec and swresample.ec, because what C's types don't say (can a pointer be NULL? does the function fill it
in?) needs a decision per function. The constants need none, and there are over a thousand of them (codec IDs,
pixel formats, flags), so they come from the headers of the installed version:

- the enums below, with every member the preprocessed headers define (members ending in `_NB` and those starting
  with `FF_` aren't part of the API and are left out);
- the #define constants starting with a prefix below, with the eclang type of what they expand to, which a small C
  program asks gcc with `_Generic` (`AV_CODEC_FLAG_CLOSED_GOP` is `1U << 31`, a UInt).

Usage: bindings/ffmpeg/generate.py [INCLUDE_DIR] > bindings/ffmpeg/constants.ec
INCLUDE_DIR is the directory holding libavutil/, libavcodec/, ...; without it, gcc's own search path is used.
"""
import os
import re
import subprocess
import sys
import tempfile

HEADERS = [
    "libavutil/avutil.h", "libavutil/error.h", "libavutil/frame.h", "libavutil/dict.h", "libavutil/pixfmt.h",
    "libavutil/samplefmt.h", "libavutil/channel_layout.h", "libavutil/log.h", "libavutil/opt.h",
    "libavutil/mathematics.h", "libavcodec/avcodec.h", "libavformat/avformat.h", "libswscale/swscale.h",
    "libswresample/swresample.h",
]

# The enums to bind: each becomes a `declare type NAME as Int` and a `declare val` per member. eclang's atoms
# (`declare enum`) aren't used, because FFmpeg keeps most of these in struct fields (`codecpar.format`,
# `ctx.pix_fmt`), which eclang doesn't convert from atoms, and combines some with `|` (SwsFlags, AVRounding).
ENUMS = [
    ("AVMediaType", "libavutil", "What a stream holds: video, audio, subtitles, ..."),
    ("AVPictureType", "libavutil", "The type of a video frame: I, P, B, ..."),
    ("AVRounding", "libavutil", "How av_rescale_rnd and av_rescale_q_rnd round; AV_ROUND_PASS_MINMAX combines with `|`."),
    ("AVPixelFormat", "libavutil", "Pixel formats, as in AVFrame.format, AVCodecContext.pix_fmt and AVCodecParameters.format."),
    ("AVColorPrimaries", "libavutil", "Color primaries (ISO/IEC 23091-2)."),
    ("AVColorTransferCharacteristic", "libavutil", "Color transfer characteristics (ISO/IEC 23091-2)."),
    ("AVColorSpace", "libavutil", "YUV color spaces (ISO/IEC 23091-2)."),
    ("AVColorRange", "libavutil", "Visual content value range: limited (MPEG) or full (JPEG)."),
    ("AVChromaLocation", "libavutil", "Where chroma samples are, relative to luma."),
    ("AVChannelOrder", "libavutil", "How an AVChannelLayout says which channel is which."),
    ("AVSampleFormat", "libavutil", "Audio sample formats; the ...P ones are planar (one plane per channel)."),
    ("AVOptionType", "libavutil", "Types of AVOptions."),
    ("AVCodecID", "libavcodec", "Identifies a codec (a format of encoded data), not one implementation of it."),
    ("AVFieldOrder", "libavcodec", "The order of fields in interlaced video."),
    ("AVDiscard", "libavcodec", "Which packets or frames a demuxer or decoder may drop."),
    ("AVCodecConfig", "libavcodec", "What avcodec_get_supported_config lists."),
    ("SwsDither", "libswscale", "Dithering of libswscale."),
    ("SwsAlphaBlend", "libswscale", "How libswscale blends alpha away."),
    ("SwsFlags", "libswscale", "Flags of libswscale (combine them with `|`): the scaling algorithm and more."),
    ("SwsIntent", "libswscale", "Rendering intent of libswscale's color mapping."),
    ("SwrDitherType", "libswresample", "Dithering of libswresample."),
    ("SwrEngine", "libswresample", "Resampling engines."),
    ("SwrFilterType", "libswresample", "Resampling filter types."),
]

# #define constants: (regex of names, heading, enum type for those of an enum's own type).
MACROS = [
    (r"LIB(AVUTIL|AVCODEC|AVFORMAT|SWSCALE|SWRESAMPLE)_VERSION_(MAJOR|MINOR|MICRO)", "Versions of the headers", None),
    (r"AV_PIX_FMT_\w+", "Pixel formats in the machine's byte order (AV_PIX_FMT_RGB32 is AV_PIX_FMT_BGRA on little-endian)", "AVPixelFormat"),
    (r"AVERROR_\w+", "Error codes (negative), besides AVERROR(errno)", None),
    (r"AV_LOG_\w+", "Log levels (av_log_set_level) and av_log_set_flags flags", None),
    (r"AV_(NOPTS_VALUE|TIME_BASE|NUM_DATA_POINTERS|ERROR_MAX_STRING_SIZE|INPUT_BUFFER_PADDING_SIZE)", "Numbers", None),
    (r"AV_DICT_\w+", "Flags of av_dict_set and av_dict_get", None),
    (r"AV_OPT_SEARCH_\w+", "Flags of av_opt_set", None),
    (r"AV_FRAME_FLAG_\w+", "AVFrame.flags", None),
    (r"AV_CH_\w+", "Channel masks (av_channel_layout_from_mask)", None),
    (r"AV_CODEC_FLAG_\w+", "AVCodecContext.flags", None),
    (r"AV_CODEC_FLAG2_\w+", "AVCodecContext.flags2", None),
    (r"AV_CODEC_CAP_\w+", "AVCodec.capabilities", None),
    (r"AV_PKT_FLAG_\w+", "AVPacket.flags", None),
    (r"FF_THREAD_\w+", "AVCodecContext.thread_type", None),
    (r"FF_COMPLIANCE_\w+", "AVCodecContext.strict_std_compliance", None),
    (r"AV_DISPOSITION_\w+", "AVStream.disposition", None),
    (r"AVFMT_\w+", "Flags of AVInputFormat, AVOutputFormat and AVFormatContext", None),
    (r"AVIO_FLAG_\w+", "Flags of avio_open", None),
    (r"AVSEEK_\w+", "Flags of av_seek_frame and avio_seek", None),
    (r"SWS_\w+", "libswscale numbers", None),
]
# Macros left out: function-like ones, and those that aren't constants of an eclang type.
SKIP_MACROS = {"AV_TIME_BASE_Q", "AV_LOG_C", "AV_PIX_FMT_NE", "AV_PIX_FMT_FLAG_BE"}

C_TYPES = {"int": "Int", "unsigned": "UInt", "long": "Long", "unsigned long": "ULong", "long long": "Long",
           "unsigned long long": "ULong", "double": "Double", "float": "Float"}


def gcc(args, source):
    with tempfile.NamedTemporaryFile("w", suffix=".c", delete=False) as f:
        f.write(source)
    try:
        return subprocess.run(["gcc"] + args + [f.name], capture_output=True, text=True, check=True).stdout
    finally:
        os.unlink(f.name)


def main():
    include = ["-I" + sys.argv[1]] if len(sys.argv) > 1 else []
    includes = "".join(f"#include <{h}>\n" for h in HEADERS)
    code = gcc(["-E", "-P"] + include, includes)
    code = re.sub(r"__attribute__\s*\(\((?:[^()]|\((?:[^()]|\([^()]*\))*\))*\)\)", "", code)
    defines = gcc(["-E", "-dM"] + include, includes)

    enums = {}
    for m in re.finditer(r"enum\s+(\w+)\s*\{([^}]*)\}", code):
        members = [x.split("=")[0].strip() for x in m.group(2).split(",") if x.strip()]
        enums[m.group(1)] = [x for x in members if not x.endswith("_NB") and not x.startswith("FF_")]
    enum_members = {x for members in enums.values() for x in members}

    macros = {}  # name -> heading index
    for line in defines.splitlines():
        m = re.match(r"#define (\w+)(\(?)", line)
        if not m or m.group(2) or m.group(1) in SKIP_MACROS or m.group(1) in enum_members:
            continue
        for i, (pattern, _, _) in enumerate(MACROS):
            if re.fullmatch(pattern, m.group(1)):
                macros[m.group(1)] = i
                break

    # The type of each macro, from gcc: a program printing `_Generic((NAME), ...)` per macro.
    names = sorted(macros)
    cases = ", ".join(f'{c}: "{e}"' for c, e in C_TYPES.items())
    probe = includes + "#include <stdio.h>\nint main(void) {\n" + "".join(
        f'    printf("%s\\n", _Generic(({n}), {cases}, default: "?"));\n' for n in names) + "    return 0;\n}\n"
    with tempfile.TemporaryDirectory() as tmp:
        exe = os.path.join(tmp, "probe")
        gcc(include + ["-w", "-o", exe], probe)
        types = dict(zip(names, subprocess.run([exe], capture_output=True, text=True, check=True).stdout.split()))
    version = {n: re.search(rf"#define {n} (\d+)", defines).group(1) for n in names if n.startswith("LIB")}

    out = []
    w = out.append
    libs = ", ".join(f"lib{lib.lower()} {version[f'LIB{lib}_VERSION_MAJOR']}.{version[f'LIB{lib}_VERSION_MINOR']}."
                     f"{version[f'LIB{lib}_VERSION_MICRO']}"
                     for lib in ("AVUTIL", "AVCODEC", "AVFORMAT", "SWSCALE", "SWRESAMPLE"))
    w("/**")
    w(f" * Constants of the FFmpeg bindings: {libs}.")
    w(" *")
    w(" * Generated from FFmpeg's headers by bindings/ffmpeg/generate.py; run it again rather than changing this file.")
    w(" * Enums are numbers with their C names (`AV_PIX_FMT_YUV420P`), as FFmpeg keeps them in struct fields.")
    w(" */")
    for h in HEADERS:
        w(f'use "{h}"')
    for name, lib, doc in ENUMS:
        if name not in enums:
            sys.exit(f"enum {name} isn't in these headers: remove it from ENUMS in {sys.argv[0]}")
        w("")
        w(f"/** {doc} */")
        w(f"declare type {name} as Int")
        for member in enums[name]:
            w(f"declare val {member} as {name}")
    for i, (_, heading, enum_type) in enumerate(MACROS):
        group = [n for n in names if macros[n] == i]
        if not group:
            continue
        w("")
        w(f"// {heading}")
        for n in group:
            t = enum_type or types[n]
            if t == "?":
                continue
            w(f"declare val {n} as {t}")
    print("\n".join(out))


main()
