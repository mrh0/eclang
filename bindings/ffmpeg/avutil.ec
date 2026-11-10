/**
 * Bindings for libavutil, FFmpeg's utility library: frames, dictionaries, rational numbers, errors, logging,
 * options, pixel and sample formats, channel layouts. https://ffmpeg.org/doxygen/8.0/group__lavu.html
 *
 * Written for FFmpeg 8.0 (libavutil 60). The names are FFmpeg's own; see bindings/ffmpeg/README.md for how its C
 * types map to eclang's. The enums and constants are in constants.ec.
 */
use "libavutil/avutil.h"
use "libavutil/error.h"
use "libavutil/frame.h"
use "libavutil/dict.h"
use "libavutil/mem.h"
use "libavutil/rational.h"
use "libavutil/mathematics.h"
use "libavutil/pixdesc.h"
use "libavutil/samplefmt.h"
use "libavutil/channel_layout.h"
use "libavutil/imgutils.h"
use "libavutil/log.h"
use "libavutil/opt.h"
use "libavutil/time.h"
use "libavutil/audio_fifo.h"
use "lib:avutil"
use "core:errors.ec"
use "core:string.ec"
use "constants.ec"

// ---------------------------------------------------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------------------------------------------------

/** A rational number, as FFmpeg gives time bases and frame rates: `AVRational { 1, 25 }`. */
declare rec AVRational as
    /** Numerator */
    num: Int
    /** Denominator */
    den: Int

/** AV_TIME_BASE as a rational number: 1/1000000, the time base of AVFormatContext.duration and start_time. */
declare val AV_TIME_BASE_Q as AVRational

/** A set of key-value pairs (metadata, options). Opaque: use av_dict_set, av_dict_get and av_dict_iterate. */
declare rec AVDictionary

/** One pair of an AVDictionary. */
declare rec AVDictionaryEntry as
    key: CString
    value: CString

/** A reference to a reference-counted buffer. */
declare rec AVBufferRef

/** Which channels audio has, and in what order. Make one with av_channel_layout_default or _from_string. */
declare rec AVChannelLayout as
    /** How the channels are given (an AVChannelOrder). */
    order: Int
    /** The number of channels. */
    nb_channels: Int

/**
 * Raw (decoded) audio or video. Make it with av_frame_alloc and free it with av_frame_free; the data buffers are
 * reference-counted, and av_frame_unref lets go of them so the frame can be used again.
 */
declare mut rec AVFrame as
    /** The planes of the picture, or of audio; a plane not in use is null. */
    data: UChar[]c?[8]
    /** For video, the size in bytes of each picture line (it can be more than the width, or negative); for audio, the size in bytes of each plane. */
    linesize: Int[8]
    /** For audio, a pointer to each plane (data has only 8); for video, the same as data. Null until the frame has buffers. */
    extended_data: UChar[]c?[]c?
    /** Video: width of the picture in pixels. */
    width: Int
    /** Video: height of the picture in pixels. */
    height: Int
    /** Audio: the number of samples in each channel. */
    nb_samples: Int
    /** An AVPixelFormat for video, an AVSampleFormat for audio; -1 when not set. */
    format: Int
    /** Video: the frame's type (an AVPictureType). */
    pict_type: Int
    /** Video: the sample aspect ratio, 0/1 when unknown. */
    sample_aspect_ratio: AVRational
    /** When to present the frame, in time_base units (of the codec context, for a decoder). */
    pts: Long
    /** The dts of the packet that started this frame. */
    pkt_dts: Long
    /** The time base of pts and duration; not used by every API. */
    time_base: AVRational
    /** Quality (between 1 (good) and FF_LAMBDA_MAX (bad)). */
    quality: Int
    /** Video: how much the frame's display should be extended (repeat_pict / (2 * fps) seconds). */
    repeat_pict: Int
    /** Audio: the sample rate. */
    sample_rate: Int
    /** Flags (AV_FRAME_FLAG_*): AV_FRAME_FLAG_KEY for a key frame, ... */
    flags: Int
    /** An AVColorRange. */
    color_range: Int
    /** An AVColorPrimaries. */
    color_primaries: Int
    /** An AVColorTransferCharacteristic. */
    color_trc: Int
    /** An AVColorSpace. */
    colorspace: Int
    /** An AVChromaLocation. */
    chroma_location: Int
    /** The frame's timestamp, as the decoder best guesses it, in time_base units. */
    best_effort_timestamp: Long
    /** Metadata of the frame. */
    metadata: @AVDictionary?
    /** Errors of decoding the frame (FF_DECODE_ERROR_*). */
    decode_error_flags: Int
    /** Pixels to crop at the top. */
    crop_top: Size
    /** Pixels to crop at the bottom. */
    crop_bottom: Size
    /** Pixels to crop at the left. */
    crop_left: Size
    /** Pixels to crop at the right. */
    crop_right: Size
    /** Audio: the channel layout. */
    ch_layout: AVChannelLayout
    /** How long the frame lasts, in time_base units; 0 if unknown. */
    duration: Long

// ---------------------------------------------------------------------------------------------------------------------
// Version and errors
// ---------------------------------------------------------------------------------------------------------------------

/** The FFmpeg version, such as "8.0.1". */
declare fn av_version_info as (): CString
/** The version of libavutil, as LIBAVUTIL_VERSION_INT: major << 16 | minor << 8 | micro. */
declare fn avutil_version as (): UInt
/** The build configuration of libavutil (the `./configure` options). */
declare fn avutil_configuration as (): CString
/** The license of libavutil. */
declare fn avutil_license as (): CString

/** Writes the text of an error code into errbuf; less than 0 if the code is unknown (it still writes a text). */
declare fn av_strerror as (errnum: Int, errbuf: Char[]c, errbuf_size: Size): Int
/** The error code of a POSIX error number: AVERROR(EAGAIN), which is negative. */
declare fn AVERROR as (e: Int): Int
/** The POSIX error number of an error code (the opposite of AVERROR). */
declare fn AVUNERROR as (e: Int): Int

/** "Try again": avcodec_receive_frame and _packet return AVERROR(EAGAIN) when they need more input. */
declare val EAGAIN as Int
/** Out of memory: AVERROR(ENOMEM). */
declare val ENOMEM as Int
/** Invalid argument: AVERROR(EINVAL). */
declare val EINVAL as Int
/** No such file: AVERROR(ENOENT). */
declare val ENOENT as Int
/** Permission denied: AVERROR(EACCES). */
declare val EACCES as Int
/** Not implemented: AVERROR(ENOSYS). */
declare val ENOSYS as Int

/**
 * The error of an FFmpeg function, as {@link avCheck} throws it. `averror` is FFmpeg's (negative) code, which
 * `code` sorts into one of the ERR_* kinds of core:errors.ec.
 */
rec AVError extends Error as
    /** The code FFmpeg returned: AVERROR_EOF, AVERROR(ENOENT), ... */
    averror: Int

/** The text of an FFmpeg error code, as C's av_err2str macro gives it: av_err2str(AVERROR_EOF) is "End of file". */
fn av_err2str(code: Int): String do
    var buf: Char[64]
    av_strerror(code, buf.data, 64)
    ret fromC(cstr(buf))

/** The ERR_* kind of an FFmpeg error code, for {@link AVError}. */
fn avErrorKind(code: Int): Int do
    ret ERR_NOT_FOUND if code == AVERROR(ENOENT) or code == AVERROR_STREAM_NOT_FOUND or code == AVERROR_DECODER_NOT_FOUND or code == AVERROR_ENCODER_NOT_FOUND or code == AVERROR_DEMUXER_NOT_FOUND or code == AVERROR_MUXER_NOT_FOUND or code == AVERROR_PROTOCOL_NOT_FOUND or code == AVERROR_OPTION_NOT_FOUND or code == AVERROR_FILTER_NOT_FOUND or code == AVERROR_BSF_NOT_FOUND
    ret ERR_PARSE if code == AVERROR_INVALIDDATA
    ret ERR_MEMORY if code == AVERROR(ENOMEM)
    ret ERR_ARGUMENT if code == AVERROR(EINVAL)
    ret ERR_PERMISSION if code == AVERROR(EACCES)
    ret ERR_UNSUPPORTED if code == AVERROR(ENOSYS) or code == AVERROR_PATCHWELCOME
    ret ERR_STATE if code == AVERROR(EAGAIN) or code == AVERROR_EOF
    ret ERR_FAILED

/** An AVError for an FFmpeg error code: "what: the error's text". */
fn avError(code: Int, what: String): AVError = AVError { code = avErrorKind(code), message = "${what}: ${av_err2str(code)}", averror = code }

/**
 * `code` when it isn't an error (0 or more), or else an {@link AVError} saying what failed. For the functions that
 * return a negative AVERROR code when they fail: `try avCheck(avformat_open_input(...), "can't open ${path}")`.
 */
fn avCheck(code: Int, what: String): Int throws AVError do
    throw avError(code, what) if code < 0
    ret code

// ---------------------------------------------------------------------------------------------------------------------
// Frames
// ---------------------------------------------------------------------------------------------------------------------

/** A new frame with its fields set to defaults and no data; free it with av_frame_free. Null when out of memory. */
declare fn av_frame_alloc as (): @AVFrame?
/** Frees a frame and what it references, and sets the variable to null. */
declare fn av_frame_free as (mut frame: @AVFrame?)
/** Frees a frame and what it references. */
fn av_frame_free(frame: @AVFrame) do
    var f: @AVFrame? = frame
    av_frame_free(mut f)
/** Makes dst a new reference to src's data, with src's properties. dst must have no data. */
declare fn av_frame_ref as (dst: @AVFrame, src: @AVFrame): Int
/** Lets go of the frame's data and resets its fields, so it can be used again. */
declare fn av_frame_unref as (frame: @AVFrame)
/** Moves src's data and properties to dst (which must have none), and resets src. */
declare fn av_frame_move_ref as (dst: @AVFrame, src: @AVFrame)
/** A new frame referencing src's data; null when out of memory. */
declare fn av_frame_clone as (src: @AVFrame): @AVFrame?
/** Allocates the data of a frame whose format, width and height (video) or nb_samples and ch_layout (audio) are set. align 0 picks one for the CPU. */
declare fn av_frame_get_buffer as (frame: @AVFrame, align: Int): Int
/** Makes sure the frame's data can be written, copying it if it is shared with another reference. */
declare fn av_frame_make_writable as (frame: @AVFrame): Int
/** 1 if the frame's data can be written (no other reference shares it), else 0. */
declare fn av_frame_is_writable as (frame: @AVFrame): Int
/** Copies the data of src into dst, which must have buffers of the same format and size. */
declare fn av_frame_copy as (dst: @AVFrame, src: @AVFrame): Int
/** Copies the properties (timestamps, metadata, color, ...) of src to dst, not the data. */
declare fn av_frame_copy_props as (dst: @AVFrame, src: @AVFrame): Int

// ---------------------------------------------------------------------------------------------------------------------
// Memory
// ---------------------------------------------------------------------------------------------------------------------

/** Memory aligned for FFmpeg's SIMD code; free it with av_free. Null when out of memory. */
declare fn av_malloc as (size: Size): @Any?
/** av_malloc, with the memory set to zero. */
declare fn av_mallocz as (size: Size): @Any?
/** Frees memory of av_malloc (or a string FFmpeg says to free with av_free). */
declare fn av_free as (ptr: @Any?)
/** A copy of s made with av_malloc; null when out of memory. */
declare fn av_strdup as (s: CString): CString?

// ---------------------------------------------------------------------------------------------------------------------
// Dictionaries
// ---------------------------------------------------------------------------------------------------------------------

/**
 * Sets key to value (copying both), making the dictionary when the variable is null; a null value deletes the
 * key. flags: AV_DICT_*.
 */
declare fn av_dict_set as (mut pm: @AVDictionary?, key: CString, value: CString?, flags: Int): Int
/** Sets key to a number. */
declare fn av_dict_set_int as (mut pm: @AVDictionary?, key: CString, value: Long, flags: Int): Int
/** The entry of key after prev (null: from the start); null when there is none. flags: AV_DICT_MATCH_CASE, AV_DICT_IGNORE_SUFFIX. */
declare fn av_dict_get as (m: @AVDictionary?, key: CString, prev: @AVDictionaryEntry?, flags: Int): @AVDictionaryEntry?
/** The entry after prev (null: the first one); null after the last. */
declare fn av_dict_iterate as (m: @AVDictionary?, prev: @AVDictionaryEntry?): @AVDictionaryEntry?
/** The number of entries. */
declare fn av_dict_count as (m: @AVDictionary?): Int
/** Copies the entries of src into dst, making it when the variable is null. */
declare fn av_dict_copy as (mut dst: @AVDictionary?, src: @AVDictionary?, flags: Int): Int
/** Frees the dictionary and sets the variable to null. */
declare fn av_dict_free as (mut m: @AVDictionary?)

// ---------------------------------------------------------------------------------------------------------------------
// Rational numbers and time stamps
// ---------------------------------------------------------------------------------------------------------------------

/** A rational number as a Double: av_q2d(AVRational { 1, 25 }) is 0.04. */
declare fn av_q2d as (a: AVRational): Double
/** AVRational { num, den }. */
declare fn av_make_q as (num: Int, den: Int): AVRational
/** 1 / q. */
declare fn av_inv_q as (q: AVRational): AVRational
/** -1, 0 or 1 when a is smaller than, equal to or bigger than b; INT_MIN when either is 0/0. */
declare fn av_cmp_q as (a: AVRational, b: AVRational): Int
/** b * c. */
declare fn av_mul_q as (b: AVRational, c: AVRational): AVRational
/** b / c. */
declare fn av_div_q as (b: AVRational, c: AVRational): AVRational
/** b + c. */
declare fn av_add_q as (b: AVRational, c: AVRational): AVRational
/** b - c. */
declare fn av_sub_q as (b: AVRational, c: AVRational): AVRational
/** The rational nearest to d with numerator and denominator at most max. */
declare fn av_d2q as (d: Double, max: Int): AVRational
/** a * b / c, rounded to the nearest, without overflowing on the way. */
declare fn av_rescale as (a: Long, b: Long, c: Long): Long
/** a * b / c, rounded as rnd says (an AVRounding, maybe `| AV_ROUND_PASS_MINMAX`). */
declare fn av_rescale_rnd as (a: Long, b: Long, c: Long, rnd: AVRounding): Long
/** A time stamp a in time base bq, in time base cq instead: av_rescale_q(pts, stream.time_base, AV_TIME_BASE_Q). */
declare fn av_rescale_q as (a: Long, bq: AVRational, cq: AVRational): Long
/** av_rescale_q, rounded as rnd says. */
declare fn av_rescale_q_rnd as (a: Long, bq: AVRational, cq: AVRational, rnd: AVRounding): Long
/** -1, 0 or 1 when time stamp ts_a (in tb_a) is before, at or after ts_b (in tb_b). */
declare fn av_compare_ts as (ts_a: Long, tb_a: AVRational, ts_b: Long, tb_b: AVRational): Int
/** The current time in microseconds since 1970. */
declare fn av_gettime as (): Long
/** A monotonic time in microseconds, to measure durations. */
declare fn av_gettime_relative as (): Long
/** Sleeps for usec microseconds. */
declare fn av_usleep as (usec: UInt): Int

// ---------------------------------------------------------------------------------------------------------------------
// Media types, pixel and sample formats
// ---------------------------------------------------------------------------------------------------------------------

/** "video", "audio", ...; null for a type it doesn't know. */
declare fn av_get_media_type_string as (media_type: AVMediaType): CString?
/** 'I', 'P', 'B', ... for an AVPictureType; '?' for none. */
declare fn av_get_picture_type_char as (pict_type: AVPictureType): Char
/** The name of a pixel format, such as "yuv420p"; null for an unknown one (or AV_PIX_FMT_NONE). */
declare fn av_get_pix_fmt_name as (pix_fmt: AVPixelFormat): CString?
/** The pixel format of a name such as "rgb24"; AV_PIX_FMT_NONE if there is none. */
declare fn av_get_pix_fmt as (name: CString): AVPixelFormat
/** The name of a sample format, such as "fltp"; null for an unknown one. */
declare fn av_get_sample_fmt_name as (sample_fmt: AVSampleFormat): CString?
/** The sample format of a name such as "s16"; AV_SAMPLE_FMT_NONE if there is none. */
declare fn av_get_sample_fmt as (name: CString): AVSampleFormat
/** The size in bytes of one sample: 2 for AV_SAMPLE_FMT_S16; 0 for an unknown format. */
declare fn av_get_bytes_per_sample as (sample_fmt: AVSampleFormat): Int
/** 1 if the format is planar (one plane per channel), else 0. */
declare fn av_sample_fmt_is_planar as (sample_fmt: AVSampleFormat): Int
/** The packed (interleaved) form of a sample format: AV_SAMPLE_FMT_FLT for AV_SAMPLE_FMT_FLTP. */
declare fn av_get_packed_sample_fmt as (sample_fmt: AVSampleFormat): AVSampleFormat
/** The planar form of a sample format. */
declare fn av_get_planar_sample_fmt as (sample_fmt: AVSampleFormat): AVSampleFormat
/** The bytes needed for this much audio (and the size of a plane in linesize, if given); negative on error. */
declare fn av_samples_get_buffer_size as (linesize: @Int?, nb_channels: Int, nb_samples: Int, sample_fmt: AVSampleFormat, align: Int): Int

// ---------------------------------------------------------------------------------------------------------------------
// Channel layouts
// ---------------------------------------------------------------------------------------------------------------------

/** Sets the layout to the usual one for this many channels (mono, stereo, 5.1, ...). */
declare fn av_channel_layout_default as (mut ch_layout: AVChannelLayout, nb_channels: Int)
/** Sets the layout from channel mask bits: av_channel_layout_from_mask(mut l, AV_CH_LAYOUT_STEREO). */
declare fn av_channel_layout_from_mask as (mut channel_layout: AVChannelLayout, mask: ULong): Int
/** Sets the layout from a description such as "stereo", "5.1" or "FL+FR". */
declare fn av_channel_layout_from_string as (mut channel_layout: AVChannelLayout, str: CString): Int
/** Makes dst a copy of src: copying a layout with `=` would share a custom one's channel map. */
declare fn av_channel_layout_copy as (mut dst: AVChannelLayout, src: @AVChannelLayout): Int
/** Frees what the layout allocated, and resets it. */
declare fn av_channel_layout_uninit as (mut channel_layout: AVChannelLayout)
/** Writes a description such as "stereo" into buf; the length it needs, or a negative error code. */
declare fn av_channel_layout_describe as (channel_layout: @AVChannelLayout, buf: Char[]c, buf_size: Size): Int
/** 0 if the layouts are the same, 1 if not, negative on error. */
declare fn av_channel_layout_compare as (chl: @AVChannelLayout, chl1: @AVChannelLayout): Int
/** 1 if the layout is valid, else 0. */
declare fn av_channel_layout_check as (channel_layout: @AVChannelLayout): Int

/** A description of a channel layout, such as "stereo" or "5.1(side)". */
fn av_channel_layout_name(layout: @AVChannelLayout): String do
    var buf: Char[64]
    ret "?" if av_channel_layout_describe(layout, buf.data, 64) < 0
    ret fromC(cstr(buf))

// ---------------------------------------------------------------------------------------------------------------------
// Images
// ---------------------------------------------------------------------------------------------------------------------

/** The bytes a picture needs in one buffer, with lines aligned to align bytes; negative on error. */
declare fn av_image_get_buffer_size as (pix_fmt: AVPixelFormat, width: Int, height: Int, align: Int): Int
/** The size in bytes of one line of a plane of a picture this wide; negative on error. */
declare fn av_image_get_linesize as (pix_fmt: AVPixelFormat, width: Int, plane: Int): Int
/**
 * Copies a picture's planes into one buffer of dst_size bytes; the bytes written, or a negative error code. C takes
 * the planes as `const uint8_t* const*`, so they are a pointer here: the overload taking a frame does that for you.
 */
declare fn av_image_copy_to_buffer as (dst: UChar[]c, dst_size: Int, src_data: UChar[]c?[]c, src_linesize: Int[]c, pix_fmt: AVPixelFormat, width: Int, height: Int, align: Int): Int
/** Copies the picture of a frame into one buffer of dst_size bytes, lines aligned to align bytes (1: none). */
fn av_image_copy_to_buffer(dst: UChar[]c, dst_size: Int, src: @AVFrame, align: Int): Int = av_image_copy_to_buffer(dst, dst_size, src.data.data, src.linesize.data, src.format, src.width, src.height, align)
/** Sets dst_data and dst_linesize (4 each) to the planes of a picture stored in src; the size of the picture, or a negative error code. */
declare fn av_image_fill_arrays as (dst_data: UChar[]c?[]c, dst_linesize: Int[]c, src: UChar[]c?, pix_fmt: AVPixelFormat, width: Int, height: Int, align: Int): Int

// ---------------------------------------------------------------------------------------------------------------------
// Logging and options
// ---------------------------------------------------------------------------------------------------------------------

/** Logs only messages at this level or more important: AV_LOG_QUIET, AV_LOG_ERROR, AV_LOG_INFO (the default), ... */
declare fn av_log_set_level as (level: Int)
/** The current log level. */
declare fn av_log_get_level as (): Int
/** AV_LOG_SKIP_REPEATED, AV_LOG_PRINT_LEVEL, ... */
declare fn av_log_set_flags as (arg: Int)
/** Logs a message, printf-style, for a context (an AVCodecContext, AVFormatContext, ... or null). */
declare fn av_log as (avcl: @Any?, level: Int, fmt: CString, ...)

/** Sets an option of an FFmpeg object (an AVCodecContext, AVFormatContext, SwsContext, ...) from text. search_flags: AV_OPT_SEARCH_CHILDREN to look in the codec's private options too ("preset", "crf", ...). */
declare fn av_opt_set as (obj: @Any, name: CString, value: CString, search_flags: Int): Int
/** Sets a number option. */
declare fn av_opt_set_int as (obj: @Any, name: CString, value: Long, search_flags: Int): Int
/** Sets a Double option. */
declare fn av_opt_set_double as (obj: @Any, name: CString, value: Double, search_flags: Int): Int
/** Sets a rational option. */
declare fn av_opt_set_q as (obj: @Any, name: CString, value: AVRational, search_flags: Int): Int
/** Sets a channel layout option. */
declare fn av_opt_set_chlayout as (obj: @Any, name: CString, layout: @AVChannelLayout, search_flags: Int): Int
/** Sets the options of a dictionary, and leaves in it those the object doesn't have. */
declare fn av_opt_set_dict as (obj: @Any, mut options: @AVDictionary?): Int

// ---------------------------------------------------------------------------------------------------------------------
// Audio FIFOs
// ---------------------------------------------------------------------------------------------------------------------

/**
 * A queue of audio samples, to regroup audio into frames of another size: an encoder wants frame_size samples per
 * frame, a resampler gives what it gives. Opaque.
 */
declare rec AVAudioFifo

/** A new queue of samples of a format, with room for nb_samples to begin with (it grows); null when out of memory. */
declare fn av_audio_fifo_alloc as (sample_fmt: AVSampleFormat, channels: Int, nb_samples: Int): @AVAudioFifo?
/** Frees a queue. */
declare fn av_audio_fifo_free as (af: @AVAudioFifo?)
/** Adds nb_samples samples of the planes data (a frame's extended_data, cast: see the overload taking a frame); the number added, or a negative error code. */
declare fn av_audio_fifo_write as (af: @AVAudioFifo, data: @(@Any?), nb_samples: Int): Int
/** Takes up to nb_samples samples into the planes data (see the overload taking a frame); the number taken, or a negative error code. */
declare fn av_audio_fifo_read as (af: @AVAudioFifo, data: @(@Any?), nb_samples: Int): Int
/** Copies up to nb_samples samples into the planes data, leaving them in the queue. */
declare fn av_audio_fifo_peek as (af: @AVAudioFifo, data: @(@Any?), nb_samples: Int): Int
/** Drops nb_samples samples. */
declare fn av_audio_fifo_drain as (af: @AVAudioFifo, nb_samples: Int): Int
/** Drops all the samples. */
declare fn av_audio_fifo_reset as (af: @AVAudioFifo)
/** The number of samples in the queue. */
declare fn av_audio_fifo_size as (af: @AVAudioFifo): Int
/** Room for how many more samples, before it grows. */
declare fn av_audio_fifo_space as (af: @AVAudioFifo): Int

/** Adds the samples of a frame (nb_samples per channel); the number added, or a negative error code. */
fn av_audio_fifo_write(af: @AVAudioFifo, frame: @AVFrame): Int do
    val planes = frame.extended_data ?? ret AVERROR(EINVAL)
    ret av_audio_fifo_write(af, unsafe planes !as @(@Any?), frame.nb_samples)

/**
 * Takes up to nb_samples samples into a frame with buffers for that many (av_frame_get_buffer), and sets its
 * nb_samples to the number taken; that number, or a negative error code.
 */
fn av_audio_fifo_read(af: @AVAudioFifo, frame: @AVFrame, nb_samples: Int): Int do
    val planes = frame.extended_data ?? ret AVERROR(EINVAL)
    val n = av_audio_fifo_read(af, unsafe planes !as @(@Any?), nb_samples)
    if n >= 0 do
        frame.nb_samples = n
    ret n
