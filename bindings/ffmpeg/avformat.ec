/**
 * Bindings for libavformat, FFmpeg's demuxers and muxers (reading and writing containers such as MP4, MKV, WAV)
 * and its I/O. https://ffmpeg.org/doxygen/8.0/group__libavf.html
 *
 * Written for FFmpeg 8.0 (libavformat 62). Reading: avformat_open_input, avformat_find_stream_info, then
 * av_read_frame until it returns AVERROR_EOF, and avformat_close_input. Writing: avformat_alloc_output_context2,
 * avformat_new_stream per stream, avio_open (unless AVFMT_NOFILE), avformat_write_header,
 * av_interleaved_write_frame per packet, av_write_trailer, avio_closep and avformat_free_context.
 */
use "libavformat/avformat.h"
use "libavformat/avio.h"
use "lib:avformat"
use "avcodec.ec"

// ---------------------------------------------------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------------------------------------------------

/** Buffered I/O, of a file or a network stream. Opaque: use the avio_* functions. */
declare rec AVIOContext

/** A demuxer (an input format). FFmpeg owns it. */
declare rec AVInputFormat as
    /** Short names, separated by commas: "mov,mp4,m4a,3gp,3g2,mj2". */
    name: CString
    /** A descriptive name; null in some builds. */
    long_name: CString?
    /** AVFMT_* flags. */
    flags: Int
    /** File name extensions, separated by commas; null if it has none. */
    extensions: CString?
    /** MIME types, separated by commas; null if it has none. */
    mime_type: CString?

/** A muxer (an output format). FFmpeg owns it. */
declare rec AVOutputFormat as
    /** A short name: "mp4", "matroska", ... */
    name: CString
    /** A descriptive name; null in some builds. */
    long_name: CString?
    /** MIME type, or null. */
    mime_type: CString?
    /** File name extensions, separated by commas, or null. */
    extensions: CString?
    /** The usual audio codec of the format. */
    audio_codec: AVCodecID
    /** The usual video codec of the format. */
    video_codec: AVCodecID
    /** The usual subtitle codec of the format. */
    subtitle_codec: AVCodecID
    /** AVFMT_* flags: AVFMT_NOFILE (no file to open with avio_open), AVFMT_GLOBALHEADER, ... */
    flags: Int

/** A stream of a container: video, audio, subtitles, ... */
declare mut rec AVStream as
    /** Its index in AVFormatContext.streams. */
    index: Int
    /** The format's own ID of the stream. */
    id: Int
    /** The codec of the stream: what a decoder needs, or what a muxer writes. */
    codecpar: @AVCodecParameters
    /** The time base of the timestamps of its packets. A muxer may change it in avformat_write_header. */
    time_base: AVRational
    /** The pts of its first frame, in time_base units; AV_NOPTS_VALUE if unknown. */
    start_time: Long
    /** How long it lasts, in time_base units; AV_NOPTS_VALUE if unknown. */
    duration: Long
    /** The number of frames, or 0 if unknown. */
    nb_frames: Long
    /** AV_DISPOSITION_* flags: default, forced, attached picture, ... */
    disposition: Int
    /** Which packets to drop when demuxing (an AVDiscard): AVDISCARD_ALL skips the stream. */
    discard: Int
    /** Video: the sample aspect ratio, 0/1 when unknown. */
    sample_aspect_ratio: AVRational
    /** Metadata: "language", "title", ... */
    metadata: @AVDictionary?
    /** Video: the average frame rate, 0/1 when unknown. */
    avg_frame_rate: AVRational
    /** Video: the lowest frame rate that has every timestamp; for a constant frame rate, the frame rate. */
    r_frame_rate: AVRational

/** A chapter of a container. */
declare rec AVChapter as
    /** A unique ID. */
    id: Long
    /** The time base of start and end. */
    time_base: AVRational
    /** Where it starts. */
    start: Long
    /** Where it ends. */
    end: Long
    /** Metadata: "title", ... */
    metadata: @AVDictionary?

/** A container being read or written: its format, streams and metadata. */
declare mut rec AVFormatContext as
    /** Reading: the demuxer. */
    iformat: @AVInputFormat?
    /** Writing: the muxer. */
    oformat: @AVOutputFormat?
    /** The I/O; writing: open it with avio_open(mut ctx.pb, ...) unless the format has AVFMT_NOFILE. */
    pb: @AVIOContext?
    /** AVFMTCTX_* flags. */
    ctx_flags: Int
    /** The number of streams. */
    nb_streams: UInt
    /** The streams; null while there are none. Use avStream(ctx, i), which checks i. */
    streams: @AVStream[]c?
    /** The number of chapters. */
    nb_chapters: UInt
    /** The chapters; null while there are none. Use avChapter(ctx, i), which checks i. */
    chapters: @AVChapter[]c?
    /** The URL or file name. */
    url: CString?
    /** Reading: when the first frame starts, in AV_TIME_BASE units (microseconds); AV_NOPTS_VALUE if unknown. */
    start_time: Long
    /** Reading: how long it lasts, in AV_TIME_BASE units (microseconds); AV_NOPTS_VALUE if unknown. */
    duration: Long
    /** The total bit rate in bits per second; 0 if unknown. */
    bit_rate: Long
    /** AVFMT_FLAG_* flags. */
    flags: Int
    /** Reading: the most bytes to read to find the format. */
    probesize: Long
    /** Reading: the most to read in avformat_find_stream_info, in AV_TIME_BASE units. */
    max_analyze_duration: Long
    /** Metadata of the whole file: "title", "artist", ... Set it before avformat_write_header when writing. */
    metadata: @AVDictionary?

// ---------------------------------------------------------------------------------------------------------------------
// General
// ---------------------------------------------------------------------------------------------------------------------

/** The version of libavformat: major << 16 | minor << 8 | micro. */
declare fn avformat_version as (): UInt
/** The build configuration of libavformat. */
declare fn avformat_configuration as (): CString
/** Sets up networking (for URLs such as http:// or rtmp://); optional, but avoids a warning. */
declare fn avformat_network_init as (): Int
/** Undoes avformat_network_init. */
declare fn avformat_network_deinit as (): Int
/** A new, empty format context; null when out of memory. */
declare fn avformat_alloc_context as (): @AVFormatContext?
/** Frees a format context and its streams (it doesn't close pb). */
declare fn avformat_free_context as (s: @AVFormatContext?)
/** A new stream of the context; null when out of memory. The codec, if given, sets up its codecpar defaults. */
declare fn avformat_new_stream as (s: @AVFormatContext, c: @AVCodec?): @AVStream?
/** Prints what the context holds, as `ffmpeg -i` does, to the log (stderr). index: of the stream to print, or 0; is_output: 1 when writing. */
declare fn av_dump_format as (ic: @AVFormatContext, index: Int, url: CString, is_output: Int)
/** The demuxer after the one opaque points to (start with a null variable); null after the last. */
declare fn av_demuxer_iterate as (mut opaque: @Any?): @AVInputFormat?
/** The muxer after the one opaque points to (start with a null variable); null after the last. */
declare fn av_muxer_iterate as (mut opaque: @Any?): @AVOutputFormat?

/** Stream i of the context, checked against nb_streams (C's s->streams[i]). */
fn avStream(s: @AVFormatContext, i: Int): @AVStream do
    if i < 0 or i as UInt >= s.nb_streams do
        panic("stream ${i} doesn't exist: there are ${s.nb_streams}")
    ret unsafe s.streams![i]

/** Chapter i of the context, checked against nb_chapters (C's s->chapters[i]). */
fn avChapter(s: @AVFormatContext, i: Int): @AVChapter do
    if i < 0 or i as UInt >= s.nb_chapters do
        panic("chapter ${i} doesn't exist: there are ${s.nb_chapters}")
    ret unsafe s.chapters![i]

// ---------------------------------------------------------------------------------------------------------------------
// Reading (demuxing)
// ---------------------------------------------------------------------------------------------------------------------

/** The demuxer with this short name ("mp4", "v4l2", ...); null if there is none. */
declare fn av_find_input_format as (short_name: CString): @AVInputFormat?
/**
 * Opens a file or URL and reads its header, setting the variable to the new context (it may also hold one made
 * with avformat_alloc_context). fmt: a demuxer, or null to find it. On failure the context is freed and the
 * variable null. Close it with avformat_close_input.
 */
declare fn avformat_open_input as (mut ps: @AVFormatContext?, url: CString, fmt: @AVInputFormat?, mut options: @AVDictionary?): Int
/** Opens a file or URL; pass null for the options. */
declare fn avformat_open_input as (mut ps: @AVFormatContext?, url: CString, fmt: @AVInputFormat?, options: @(@AVDictionary?)?): Int
/** Reads some packets to find out what the streams hold, for formats whose header doesn't say. Pass null for the options. */
declare fn avformat_find_stream_info as (ic: @AVFormatContext, options: @(@AVDictionary?)?): Int
/**
 * The index of the best stream of a type (an AVMediaType), or AVERROR_STREAM_NOT_FOUND or
 * AVERROR_DECODER_NOT_FOUND. wanted_stream_nb and related_stream: -1 for any. decoder_ret is set to its decoder.
 */
declare fn av_find_best_stream as (ic: @AVFormatContext, media_type: AVMediaType, wanted_stream_nb: Int, related_stream: Int, out decoder_ret: @AVCodec, flags: Int): Int
/** av_find_best_stream without the decoder: pass null for decoder_ret. */
declare fn av_find_best_stream as (ic: @AVFormatContext, media_type: AVMediaType, wanted_stream_nb: Int, related_stream: Int, decoder_ret: @(@AVCodec?)?, flags: Int): Int
/**
 * Reads the next packet of any stream into pkt (stream_index says which): 0, AVERROR_EOF at the end, or another
 * error. The packet must be av_packet_unref'd after use.
 */
declare fn av_read_frame as (s: @AVFormatContext, pkt: @AVPacket): Int
/** Seeks a stream (-1: the default one, timestamp in AV_TIME_BASE units) to the key frame at timestamp. flags: AVSEEK_FLAG_BACKWARD, ... */
declare fn av_seek_frame as (s: @AVFormatContext, stream_index: Int, timestamp: Long, flags: Int): Int
/** Seeks to a timestamp between min_ts and max_ts, as near to ts as it can. */
declare fn avformat_seek_file as (s: @AVFormatContext, stream_index: Int, min_ts: Long, ts: Long, max_ts: Long, flags: Int): Int
/** The frame rate of a video stream, as best FFmpeg can guess it. */
declare fn av_guess_frame_rate as (ctx: @AVFormatContext, stream: @AVStream, frame: @AVFrame?): AVRational
/** Closes an input and frees its context, and sets the variable to null. */
declare fn avformat_close_input as (mut s: @AVFormatContext?)
/** Closes an input and frees its context. */
fn avformat_close_input(s: @AVFormatContext) do
    var c: @AVFormatContext? = s
    avformat_close_input(mut c)

// ---------------------------------------------------------------------------------------------------------------------
// Writing (muxing)
// ---------------------------------------------------------------------------------------------------------------------

/** The muxer for a short name, file name (by its extension) or MIME type, any of which may be null; null if none fits. */
declare fn av_guess_format as (short_name: CString?, filename: CString?, mime_type: CString?): @AVOutputFormat?
/**
 * Makes a context to write a file, setting the variable to it: its muxer is oformat, or the one named
 * format_name, or the one for filename's extension. Free it with avformat_free_context.
 */
declare fn avformat_alloc_output_context2 as (out ctx: @AVFormatContext, oformat: @AVOutputFormat?, format_name: CString?, filename: CString?): Int
/** 1 if the muxer can hold the codec, 0 if not, negative if it can't tell. std_compliance: FF_COMPLIANCE_*. */
declare fn avformat_query_codec as (ofmt: @AVOutputFormat, codec_id: AVCodecID, std_compliance: Int): Int
/** Sets up the muxer without writing the header, with options; avformat_write_header then writes it. */
declare fn avformat_init_output as (s: @AVFormatContext, mut options: @AVDictionary?): Int
/** Sets up the muxer without writing the header; pass null for the options. */
declare fn avformat_init_output as (s: @AVFormatContext, options: @(@AVDictionary?)?): Int
/** Writes the header, with the muxer's options; the streams' time_base may change. */
declare fn avformat_write_header as (s: @AVFormatContext, mut options: @AVDictionary?): Int
/** Writes the header; pass null for the options. */
declare fn avformat_write_header as (s: @AVFormatContext, options: @(@AVDictionary?)?): Int
/**
 * Writes a packet (in its stream's time_base), putting packets of the streams in the order of their dts; it
 * takes the packet's data and leaves it empty. Null flushes the packets held back.
 */
declare fn av_interleaved_write_frame as (s: @AVFormatContext, pkt: @AVPacket?): Int
/** Writes a packet as it is, without ordering; the caller interleaves. Null flushes the muxer. */
declare fn av_write_frame as (s: @AVFormatContext, pkt: @AVPacket?): Int
/** Writes the end of the file. Call it after the last packet, before avio_closep. */
declare fn av_write_trailer as (s: @AVFormatContext): Int

// ---------------------------------------------------------------------------------------------------------------------
// I/O
// ---------------------------------------------------------------------------------------------------------------------

/** Opens a file or URL for reading or writing (AVIO_FLAG_READ, AVIO_FLAG_WRITE): avio_open(mut ctx.pb, url, AVIO_FLAG_WRITE). */
declare fn avio_open as (out s: @AVIOContext, url: CString, flags: Int): Int
/** Closes and frees an AVIOContext, and sets the variable (or field) to null. */
declare fn avio_closep as (mut s: @AVIOContext?): Int
/** Writes size bytes of buf. */
declare fn avio_write as (s: @AVIOContext, buf: UChar[]c, size: Int)
/** Writes one byte. */
declare fn avio_w8 as (s: @AVIOContext, b: Int)
/** Writes 16 bits, little-endian. */
declare fn avio_wl16 as (s: @AVIOContext, value: UInt)
/** Writes 32 bits, little-endian. */
declare fn avio_wl32 as (s: @AVIOContext, value: UInt)
/** Writes 64 bits, little-endian. */
declare fn avio_wl64 as (s: @AVIOContext, value: ULong)
/** Writes 16 bits, big-endian. */
declare fn avio_wb16 as (s: @AVIOContext, value: UInt)
/** Writes 32 bits, big-endian. */
declare fn avio_wb32 as (s: @AVIOContext, value: UInt)
/** Writes a C string and its 0 byte; the bytes written. */
declare fn avio_put_str as (s: @AVIOContext, str: CString): Int
/** Writes out what is buffered. */
declare fn avio_flush as (s: @AVIOContext)
/** Reads up to size bytes into buf: the number read, or a negative error code (AVERROR_EOF at the end). */
declare fn avio_read as (s: @AVIOContext, buf: UChar[]c, size: Int): Int
/** Moves to offset (whence: SEEK_SET, SEEK_CUR, SEEK_END, or AVSEEK_SIZE); the new position, or a negative error code. */
declare fn avio_seek as (s: @AVIOContext, offset: Long, whence: Int): Long
/** The current position. */
declare fn avio_tell as (s: @AVIOContext): Long
/** The size of the file, or a negative error code. */
declare fn avio_size as (s: @AVIOContext): Long
