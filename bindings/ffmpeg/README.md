# FFmpeg bindings

eclang bindings for [FFmpeg](https://ffmpeg.org) 8.0, the libraries behind the `ffmpeg` tool: reading and writing
media files, encoding and decoding audio and video, and converting pictures and sound. The names are FFmpeg's own, so
its [API documentation](https://ffmpeg.org/doxygen/8.0/) and examples carry over almost line by line.

They aren't part of `core:`. One file per library, each linking it:

| File | Library | What for |
| --- | --- | --- |
| [avutil.ec](avutil.ec) | libavutil | frames, errors, dictionaries, rational numbers, time stamps, logging, options, pixel and sample formats, channel layouts, audio FIFOs |
| [avcodec.ec](avcodec.ec) | libavcodec | encoders and decoders, packets, codec parameters, parsers |
| [avformat.ec](avformat.ec) | libavformat | containers (demuxing and muxing: MP4, MKV, WAV, ...), streams, I/O |
| [swscale.ec](swscale.ec) | libswscale | scaling pictures and converting pixel formats |
| [swresample.ec](swresample.ec) | libswresample | resampling audio and converting sample formats and channel layouts |
| [constants.ec](constants.ec) | all of them | the enums and constants, generated from the headers by [generate.py](generate.py) |
| [ffmpeg.ec](ffmpeg.ec) | all of them | `use`s the five files above |

## Setup

Install FFmpeg 8's development files: `libavformat-dev libavcodec-dev libavutil-dev libswscale-dev libswresample-dev`
on Debian and Ubuntu, `ffmpeg-devel` on Fedora, `ffmpeg` on Arch and with Homebrew. When they aren't in the usual
directories, tell the C compiler where they are:

```
export C_INCLUDE_PATH=/path/to/ffmpeg/include
export LIBRARY_PATH=/path/to/ffmpeg/lib
export LD_LIBRARY_PATH=/path/to/ffmpeg/lib     # to run the program
java -jar target/eclang-0.1.0.jar run bindings/ffmpeg/examples/encode.ec -- out.mp4
```

Each file says `use "lib:avformat"` (and so on), so `run` and `lib` link what a program uses.

## Usage

`use` [ffmpeg.ec](ffmpeg.ec) (or only the libraries you need) by its path, relative to your file or to the working
directory. Most FFmpeg functions return a negative error code when they fail; `avCheck` turns that into an error
value, so `try` works:

```plaintext
use "bindings/ffmpeg/ffmpeg.ec"
use "core:io.ec"

fn streams(path: String) throws AVError do
    var opened: @AVFormatContext? = null
    try avCheck(avformat_open_input(mut opened, cstr(path), null, null), "can't open ${path}")
    val input = opened!
    defer avformat_close_input(input)
    try avCheck(avformat_find_stream_info(input, null), "can't read ${path}")
    for i in 0..<input.nb_streams as Int do
        val par = avStream(input, i).codecpar
        io.println("${i}: ${avcodec_get_name(par.codec_id)}")

fn main() throws Error do
    try streams("movie.mp4")
```

[examples/](examples) has four programs, each one step of a typical media program:

- [probe.ec](examples/probe.ec): what a file holds (format, duration, metadata, streams), like `ffprobe`.
- [decode.ec](examples/decode.ec): decodes a video, counts its frames, and saves the first one as a picture (libswscale to RGB).
- [encode.ec](examples/encode.ec): draws a moving picture and encodes it into any video format FFmpeg can write (libswscale to the encoder's pixel format).
- [tone.ec](examples/tone.ec): makes a chord and encodes it into any audio format (libswresample, and an `AVAudioFifo` for the encoder's frame size).

## The FFmpeg API in brief

- **Libraries.** libavformat reads and writes containers: it splits a file into *packets* (`AVPacket`, encoded
  data) of its *streams* (`AVStream`), or puts packets into one. libavcodec turns packets into *frames* (`AVFrame`,
  raw pictures or sound) and back. libswscale and libswresample convert frames between sizes, pixel formats, sample
  formats and rates. libavutil holds what they share.
- **Reading:** `avformat_open_input` → `avformat_find_stream_info` → `av_find_best_stream` (or look at each
  `codecpar`) → a decoder: `avcodec_alloc_context3`, `avcodec_parameters_to_context`, `avcodec_open2` → then
  `av_read_frame` until `AVERROR_EOF`, giving each packet of the stream to `avcodec_send_packet` and taking frames
  with `avcodec_receive_frame` until it says `AVERROR(EAGAIN)`. At the end, `avcodec_send_packet(dec, null)` and take
  the frames it still holds, until `AVERROR_EOF`.
- **Writing:** `avformat_alloc_output_context2` → `avformat_new_stream` → an encoder (`avcodec_find_encoder`, set
  `width`/`height`/`pix_fmt` or `sample_rate`/`ch_layout`/`sample_fmt`, and `time_base`; `AV_CODEC_FLAG_GLOBAL_HEADER`
  when the format has `AVFMT_GLOBALHEADER`) → `avcodec_open2` → `avcodec_parameters_from_context` →
  `avio_open` (unless `AVFMT_NOFILE`) → `avformat_write_header` → per frame `avcodec_send_frame` and
  `avcodec_receive_packet`, `av_packet_rescale_ts` to the stream's time base and `av_interleaved_write_frame` →
  `avcodec_send_frame(enc, null)` to flush → `av_write_trailer` → `avio_closep` and `avformat_free_context`.
- **Time.** Timestamps (`pts`, `dts`, `duration`) are whole numbers in a *time base*, an `AVRational` such as 1/90000
  for a stream or 1/25 for an encoder: seconds = `pts * av_q2d(time_base)`. `av_rescale_q` converts between time
  bases. The durations of an `AVFormatContext` are in `AV_TIME_BASE` (microseconds). `AV_NOPTS_VALUE` means unknown.
- **Ownership.** Frames and packets are reference-counted: `av_frame_alloc` makes an empty one, `av_frame_get_buffer`
  or a decoder gives it data, `av_frame_unref` lets go of the data so the frame can be used again, and
  `av_frame_free` frees it. An encoder may keep a frame's buffers, so call `av_frame_make_writable` before drawing
  into a frame again.
- **Options.** Codecs, formats and converters are configured by fields and by named options:
  `av_opt_set(enc, "preset"c, "slow"c, AV_OPT_SEARCH_CHILDREN)` for libx264's `-preset`, or a dictionary given to
  `avcodec_open2`/`avformat_write_header` (`av_dict_set(mut opts, "crf"c, "23"c, 0)`).
- **New in FFmpeg 7 and 8**, which older tutorials don't use: channel layouts are `AVChannelLayout` (`ch_layout`,
  not `channels`/`channel_layout`); an encoder's supported formats come from `avcodec_get_supported_config` (here
  also `avSupportedPixelFormats`, `avSupportedSampleFormats`, `avSupportedSampleRates`), not `AVCodec.pix_fmts`; a
  key frame is `frame.flags & AV_FRAME_FLAG_KEY`, not `key_frame`; and libswscale converts whole frames with
  `sws_alloc_context` and `sws_scale_frame`, with no sizes to give ahead of time.

## From C to eclang

| C | eclang |
| --- | --- |
| `AVFormatContext *ctx = NULL; avformat_open_input(&ctx, url, NULL, NULL)` | `var ctx: @AVFormatContext? = null`, `avformat_open_input(mut ctx, cstr(url), null, null)` |
| `av_frame_free(&frame)` | `av_frame_free(mut frame)` on a `@AVFrame?` variable, or `av_frame_free(frame)` on an `@AVFrame` (below) |
| `ctx->streams[i]` | `avStream(ctx, i)`, which checks `i` against `nb_streams` |
| `frame->data[0][y * frame->linesize[0] + x] = v` | `val p = frame.data[0]!`, then `unsafe p[y * frame.linesize[0] + x] = v` |
| `const char *` | `CString`: a literal `"text"c`, or `cstr(s)` of a String; `fromC(c)` makes a String |
| `ctx->pix_fmt = AV_PIX_FMT_YUV420P` | the same: enums are numbers named as in C, of a type named like the enum (`AVPixelFormat`, an `Int`) |
| `AVERROR(EAGAIN)`, `AVERROR_EOF` | the same |
| `av_err2str(err)` | the same, a function giving a String |
| `(AVRational){1, 25}` | `AVRational { 1, 25 }` |
| `codec->type` | `codec.media_type`: `type` is an eclang keyword |
| `sws_scale(sws, src->data, src->linesize, 0, h, dst->data, dst->linesize)` | `sws_scale(sws, src, 0, h, dst)`; `sws_scale_frame(sws, dst, src)` is simpler still |
| `av_audio_fifo_write(fifo, (void **)frame->data, frame->nb_samples)` | `av_audio_fifo_write(fifo, frame)` |

**Errors.** `avCheck(code, what)` returns the code when it is 0 or more, and otherwise throws an `AVError`, an `Error`
(from `core:errors.ec`) whose `message` is "what: FFmpeg's text" and whose `averror` is the code. `code` sorts it into
an `ERR_*` kind: `AVERROR(ENOENT)` and the `AVERROR_*_NOT_FOUND`s are `ERR_NOT_FOUND`, `AVERROR_INVALIDDATA` is
`ERR_PARSE`, and so on. `avError(code, what)` makes one, for `?? throw`:
`val frame = av_frame_alloc() ?? throw avError(AVERROR(ENOMEM), "can't make a frame")`. A receive returning
`AVERROR(EAGAIN)` or `AVERROR_EOF` isn't a failure, so compare with those before calling `avCheck`.

**Freeing.** C's `*_free(&x)` functions take a `mut` variable, which they set to null. Each also has an overload
taking the value, which suits a `defer` after a checked allocation: `val frame = av_frame_alloc() ?? throw ...`, then
`defer av_frame_free(frame)`. These are `av_frame_free`, `av_packet_free`, `avcodec_free_context`,
`avcodec_parameters_free`, `avformat_close_input`, `sws_free_context` and `swr_free`. `avio_closep(mut ctx.pb)` takes
the field and does nothing when it is null.

**NULL.** A function that returns NULL when it fails (or finds nothing) has a nullable result: the `*_alloc`
functions, `avcodec_find_*`, `av_guess_format`, `avformat_new_stream`, `av_dict_get`, `av_get_pix_fmt_name`, ...
Pointer fields that may be NULL are nullable too (`frame.data[i]`, `packet.data`, `ctx.pb`, `format.long_name`,
`ctx.streams` while there are none); reading one that isn't nullable and is NULL anyway stops the program with a
message. Parameters C allows NULL for are nullable (`avformat_open_input`'s format, a dictionary of options).

**Options as dictionaries.** A function taking `AVDictionary **options` has two overloads: one taking `mut opts` (a
`@AVDictionary?` variable, which FFmpeg leaves holding the options it didn't use) and one taking `null`. Free the
dictionary with `av_dict_free(mut opts)`.

**Enums.** FFmpeg keeps enums in struct fields (`codecpar.codec_type`, `frame.format`) and combines some with `|`,
so they are numbers named as in C (`AVMEDIA_TYPE_VIDEO`, `AV_PIX_FMT_RGB24`, `AV_CODEC_ID_H264`, `SWS_BICUBIC`),
not atoms. `frame.format` is an `Int` holding an `AVPixelFormat` or an `AVSampleFormat`, as in C. Flags have the
type C gives them: `AV_CODEC_FLAG_CLOSED_GOP` is `1U << 31`, a `UInt`, while `AVCodecContext.flags` is an `Int`.

**Planes.** C's arrays of planes (`uint8_t *data[8]`) are `UChar[]c?[8]`: `frame.data[0]` is a `UChar[]c?`,
indexed inside `unsafe`, as a C array has no length. Where C takes them as `const uint8_t* const*` (`sws_scale`'s
input, `av_image_copy_to_buffer`, `swr_convert`'s input), they are `UChar[]c?[]c` too: pass `frame.data.data`, and
the call casts away the `const`, which eclang doesn't have.

**Structs** list the fields programs commonly use; C's layout comes from its headers, so more can be added to a
`declare rec` without the others. `AVCodec`, `AVInputFormat` and `AVOutputFormat` are FFmpeg's own: read them, don't
change them.

**Left out** (so far): libavfilter, libavdevice, hardware decoding, side data, subtitles, bitstream filters, custom
I/O callbacks, and `av_log_set_callback` (it takes a C `va_list`). See [KNOWN_ISSUES.md](../../KNOWN_ISSUES.md).

## Regenerating and testing

```
bindings/ffmpeg/generate.py [/path/to/include] > bindings/ffmpeg/constants.ec
```

[constants.ec](constants.ec) comes from the installed headers: every member of the enums listed in `generate.py`,
and the `#define`s with the prefixes listed there, with the type gcc gives them. The other files are written by hand:
what C's types don't say (whether a pointer may be NULL, whether a function fills it in, whether it is an array) was
decided per function from FFmpeg's documentation.

`tests/run.sh` (or `tests/run.sh ffmpeg`) checks the bindings when it finds FFmpeg's headers of the same major
versions, installed or on `C_INCLUDE_PATH`. It checks that every `declare fn` matches its C declaration
(`tools/check-bindings.py`), that `constants.ec` is what `generate.py` makes of headers of the same version, and that
the examples build with `-Werror` and work: it encodes a video, decodes it, makes a WAV file, and probes both.
Without the headers it prints a `skip` line. For a new major version of FFmpeg, run `generate.py`, then
`tools/check-bindings.py` on the five files, and check the structs' fields against the new headers.
