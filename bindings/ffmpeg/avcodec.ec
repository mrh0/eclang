/**
 * Bindings for libavcodec, FFmpeg's encoders and decoders. https://ffmpeg.org/doxygen/8.0/group__libavc.html
 *
 * Written for FFmpeg 8.0 (libavcodec 62). Decoding sends packets and receives frames (avcodec_send_packet,
 * avcodec_receive_frame); encoding sends frames and receives packets (avcodec_send_frame, avcodec_receive_packet).
 * A receive returns AVERROR(EAGAIN) when it needs more input, and AVERROR_EOF when it has given all of it after
 * a send of null.
 */
use "libavcodec/avcodec.h"
use "lib:avcodec"
use "avutil.ec"

// ---------------------------------------------------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------------------------------------------------

/** An encoder or decoder. FFmpeg owns it: find one with avcodec_find_decoder, _encoder or av_codec_iterate. */
declare rec AVCodec as
    /** A short name, such as "h264" or "libx264". */
    name: CString
    /** A descriptive name; null in some builds. */
    long_name: CString?
    /** What it codes: an AVMediaType (`type` in C). */
    media_type extern "type": AVMediaType
    /** The format it codes. */
    id: AVCodecID
    /** What it can do: AV_CODEC_CAP_* flags. */
    capabilities: Int
    /** The name of the external library it wraps ("libx264", ...), or null. */
    wrapper_name: CString?

/** Codec parameters of a stream (AVStream.codecpar): what a decoder needs to know, or a muxer to write. */
declare rec AVCodecParameters as
    /** An AVMediaType. */
    codec_type: AVMediaType
    /** The format of the data. */
    codec_id: AVCodecID
    /** A fourcc of the codec, if the container has one. */
    codec_tag: UInt
    /** Data the decoder needs, such as an h264 SPS/PPS; null when there is none. */
    extradata: UChar[]c?
    /** The size in bytes of extradata. */
    extradata_size: Int
    /** An AVPixelFormat for video, an AVSampleFormat for audio. */
    format: Int
    /** The average bit rate in bits per second. */
    bit_rate: Long
    /** Bits per sample of the encoded data, if the codec has a fixed one. */
    bits_per_coded_sample: Int
    /** Bits per sample of the raw data, if different from what the format implies. */
    bits_per_raw_sample: Int
    /** The codec's profile (AV_PROFILE_*). */
    profile: Int
    /** The codec's level. */
    level: Int
    /** Video: the width in pixels. */
    width: Int
    /** Video: the height in pixels. */
    height: Int
    /** Video: the sample aspect ratio, 0/1 when unknown. */
    sample_aspect_ratio: AVRational
    /** Video: the frame rate, 0/1 when unknown. */
    framerate: AVRational
    /** Video: an AVFieldOrder. */
    field_order: Int
    /** Video: an AVColorRange. */
    color_range: Int
    /** Video: an AVColorPrimaries. */
    color_primaries: Int
    /** Video: an AVColorTransferCharacteristic. */
    color_trc: Int
    /** Video: an AVColorSpace. */
    color_space: Int
    /** Video: an AVChromaLocation. */
    chroma_location: Int
    /** Video: the number of frames the decoder delays its output by. */
    video_delay: Int
    /** Audio: the channel layout. */
    ch_layout: AVChannelLayout
    /** Audio: samples per second. */
    sample_rate: Int
    /** Audio: bytes per coded block, for some codecs. */
    block_align: Int
    /** Audio: samples per channel in a frame, if fixed. */
    frame_size: Int
    /** Audio: samples the encoder added at the start. */
    initial_padding: Int
    /** Audio: samples the encoder added at the end. */
    trailing_padding: Int
    /** Audio: samples to skip after seeking. */
    seek_preroll: Int

/**
 * The state of an encoder or decoder. Make it with avcodec_alloc_context3, set it up (from a stream with
 * avcodec_parameters_to_context, or by hand for an encoder), open it with avcodec_open2, and free it with
 * avcodec_free_context.
 */
declare mut rec AVCodecContext as
    /** An AVMediaType. */
    codec_type: AVMediaType
    /** The codec it was opened with; null before avcodec_open2. */
    codec: @AVCodec?
    /** The format it codes. */
    codec_id: AVCodecID
    /** A fourcc of the codec. */
    codec_tag: UInt
    /** The codec's private options (an AVOptions object, for av_opt_set); null for a codec without any. */
    priv_data: @Any?
    /** Encoding: the average bit rate in bits per second; 0 lets the encoder decide. */
    bit_rate: Long
    /** AV_CODEC_FLAG_* flags. */
    flags: Int
    /** AV_CODEC_FLAG2_* flags. */
    flags2: Int
    /** Data the decoder needs, or that the encoder made (with AV_CODEC_FLAG_GLOBAL_HEADER); null when there is none. */
    extradata: UChar[]c?
    /** The size in bytes of extradata. */
    extradata_size: Int
    /** Encoding: the time base of the frames' pts, which the encoder must be given; for video often 1/framerate. */
    time_base: AVRational
    /** Decoding: the time base of the packets' timestamps (the stream's time_base). */
    pkt_timebase: AVRational
    /** Video: the frame rate. */
    framerate: AVRational
    /** Video: the width in pixels. */
    width: Int
    /** Video: the height in pixels. */
    height: Int
    /** Video: the width of the coded picture, which can be bigger than width. */
    coded_width: Int
    /** Video: the height of the coded picture. */
    coded_height: Int
    /** Video: the sample aspect ratio, 0/1 when unknown. */
    sample_aspect_ratio: AVRational
    /** Video: the pixel format of the frames; for an encoder, one it supports (avcodec_get_supported_config). */
    pix_fmt: AVPixelFormat
    /** Video: an AVColorPrimaries. */
    color_primaries: Int
    /** Video: an AVColorTransferCharacteristic. */
    color_trc: Int
    /** Video: an AVColorSpace. */
    colorspace: Int
    /** Video: an AVColorRange. */
    color_range: Int
    /** Video: an AVFieldOrder. */
    field_order: Int
    /** Video: frames the decoder delays its output by, at most. */
    has_b_frames: Int
    /** Encoding video: the most B-frames between other frames. */
    max_b_frames: Int
    /** Encoding video: frames from one key frame to the next, at most. */
    gop_size: Int
    /** Encoding video: frames from one key frame to the next, at least. */
    keyint_min: Int
    /** Audio: samples per second. */
    sample_rate: Int
    /** Audio: the format of the samples; for an encoder, one it supports. */
    sample_fmt: AVSampleFormat
    /** Audio: the channel layout. */
    ch_layout: AVChannelLayout
    /** Audio: samples per channel in a frame; for an encoder, what each frame must have (unless AV_CODEC_CAP_VARIABLE_FRAME_SIZE). */
    frame_size: Int
    /** Encoding: quality for a fixed quality scale (with AV_CODEC_FLAG_QSCALE). */
    global_quality: Int
    /** Encoding: how hard to compress, for codecs with a level (flac, png, ...). */
    compression_level: Int
    /** Encoding: the smallest quantizer. */
    qmin: Int
    /** Encoding: the biggest quantizer. */
    qmax: Int
    /** Encoding: the most bits per second. */
    rc_max_rate: Long
    /** Encoding: the size of the rate control buffer, in bits. */
    rc_buffer_size: Int
    /** How closely to follow the standards: FF_COMPLIANCE_*. FF_COMPLIANCE_EXPERIMENTAL allows experimental codecs. */
    strict_std_compliance: Int
    /** The number of threads; 0 picks one. */
    thread_count: Int
    /** FF_THREAD_FRAME, FF_THREAD_SLICE, or both. */
    thread_type: Int
    /** The codec's profile (AV_PROFILE_*). */
    profile: Int
    /** The codec's level. */
    level: Int
    /** Decoding: which frames to skip (an AVDiscard), such as AVDISCARD_NONKEY for key frames only. */
    skip_frame: Int
    /** Decoding audio: the sample format to ask the decoder for, if it can give several. */
    request_sample_fmt: AVSampleFormat
    /** The frames sent (encoding) or received (decoding) so far. */
    frame_num: Long

/**
 * Encoded data: a frame of video or audio, or part of one. Make it with av_packet_alloc and free it with
 * av_packet_free; av_read_frame and avcodec_receive_packet fill it, and av_packet_unref empties it again.
 */
declare mut rec AVPacket as
    /** The reference-counted buffer of data; null if the data isn't reference-counted. */
    buf: @AVBufferRef?
    /** When to present the decoded frame, in the stream's time_base units; AV_NOPTS_VALUE if unknown. */
    pts: Long
    /** When to decode the packet, in the stream's time_base units; AV_NOPTS_VALUE if unknown. */
    dts: Long
    /** The data; null in an empty packet. */
    data: UChar[]c?
    /** The size in bytes of data. */
    size: Int
    /** The index of the stream it belongs to. */
    stream_index: Int
    /** AV_PKT_FLAG_*: AV_PKT_FLAG_KEY for a key frame. */
    flags: Int
    /** How long it lasts, in the stream's time_base units; 0 if unknown. */
    duration: Long
    /** Where it is in the stream, in bytes; -1 if unknown. */
    pos: Long
    /** The time base of the timestamps, for APIs that use it. */
    time_base: AVRational

/** Splits a stream of raw encoded data (a .h264 file, ...) into packets. */
declare rec AVCodecParserContext as
    /** An AVPictureType. */
    pict_type: Int
    /** pts of the packet the parser returned. */
    pts: Long
    /** dts of the packet the parser returned. */
    dts: Long
    /** Where the packet starts in the input, in bytes. */
    pos: Long
    /** 1 for a key frame, 0 if not, -1 if unknown. */
    key_frame: Int
    /** The duration of the packet, in the codec's time base. */
    duration: Int
    /** Video: the width, if the parser found it. */
    width: Int
    /** Video: the height, if the parser found it. */
    height: Int
    /** An AVPixelFormat or AVSampleFormat, if the parser found it. */
    format: Int

// ---------------------------------------------------------------------------------------------------------------------
// Codecs
// ---------------------------------------------------------------------------------------------------------------------

/** The version of libavcodec: major << 16 | minor << 8 | micro. */
declare fn avcodec_version as (): UInt
/** The build configuration of libavcodec. */
declare fn avcodec_configuration as (): CString
/** A decoder for a format; null if there is none. */
declare fn avcodec_find_decoder as (id: AVCodecID): @AVCodec?
/** An encoder for a format; null if there is none. */
declare fn avcodec_find_encoder as (id: AVCodecID): @AVCodec?
/** The decoder with this name ("h264", "libdav1d", ...); null if there is none. */
declare fn avcodec_find_decoder_by_name as (name: CString): @AVCodec?
/** The encoder with this name ("libx264", "aac", ...); null if there is none. */
declare fn avcodec_find_encoder_by_name as (name: CString): @AVCodec?
/** The codec after the one opaque points to (start with a null variable); null after the last. */
declare fn av_codec_iterate as (mut opaque: @Any?): @AVCodec?
/** Non-zero for an encoder. */
declare fn av_codec_is_encoder as (codec: @AVCodec): Int
/** Non-zero for a decoder. */
declare fn av_codec_is_decoder as (codec: @AVCodec): Int
/** The name of a format, such as "h264"; "unknown_codec" for one it doesn't know. */
declare fn avcodec_get_name as (id: AVCodecID): CString
/** What a format codes: an AVMediaType. */
declare fn avcodec_get_type as (codec_id: AVCodecID): AVMediaType
/** The name of a profile of a format, or null. */
declare fn avcodec_profile_name as (codec_id: AVCodecID, profile: Int): CString?
/**
 * What an encoder (or the codec of a context) supports: out_configs is set to an array of out_num_configs values
 * (AVPixelFormat, AVRational, Int sample rates, ... depending on config), or to null when anything goes.
 */
declare fn avcodec_get_supported_config as (avctx: @AVCodecContext?, codec: @AVCodec?, config: AVCodecConfig, flags: UInt, out out_configs: @Any, out out_num_configs: Int): Int

/**
 * The pixel formats an encoder supports, best first, from avcodec_get_supported_config; empty when it didn't say
 * (any may work). Allocated in the current pool.
 */
fn avSupportedPixelFormats(codec: @AVCodec): AVPixelFormat[] do
    var configs: @Any? = null
    var count = 0
    if avcodec_get_supported_config(null, codec, AV_CODEC_CONFIG_PIX_FORMAT, 0, mut configs, mut count) < 0 or count <= 0 do
        ret new AVPixelFormat[0]
    val formats = unsafe configs! !as AVPixelFormat[]c
    val result = new AVPixelFormat[count as Size]
    for i in 0..<count do
        result[i] = unsafe formats[i]
    ret result

/**
 * The sample formats an encoder supports, best first; empty when it didn't say (any may work). Allocated in the
 * current pool.
 */
fn avSupportedSampleFormats(codec: @AVCodec): AVSampleFormat[] do
    var configs: @Any? = null
    var count = 0
    if avcodec_get_supported_config(null, codec, AV_CODEC_CONFIG_SAMPLE_FORMAT, 0, mut configs, mut count) < 0 or count <= 0 do
        ret new AVSampleFormat[0]
    val formats = unsafe configs! !as AVSampleFormat[]c
    val result = new AVSampleFormat[count as Size]
    for i in 0..<count do
        result[i] = unsafe formats[i]
    ret result

/**
 * The sample rates an encoder supports; empty when it didn't say (any may work). Allocated in the current pool.
 */
fn avSupportedSampleRates(codec: @AVCodec): Int[] do
    var configs: @Any? = null
    var count = 0
    if avcodec_get_supported_config(null, codec, AV_CODEC_CONFIG_SAMPLE_RATE, 0, mut configs, mut count) < 0 or count <= 0 do
        ret new Int[0]
    val rates = unsafe configs! !as Int[]c
    val result = new Int[count as Size]
    for i in 0..<count do
        result[i] = unsafe rates[i]
    ret result

// ---------------------------------------------------------------------------------------------------------------------
// Codec parameters
// ---------------------------------------------------------------------------------------------------------------------

/** New codec parameters with defaults; free them with avcodec_parameters_free. Null when out of memory. */
declare fn avcodec_parameters_alloc as (): @AVCodecParameters?
/** Frees codec parameters and sets the variable to null. */
declare fn avcodec_parameters_free as (mut par: @AVCodecParameters?)
/** Frees codec parameters. */
fn avcodec_parameters_free(par: @AVCodecParameters) do
    var p: @AVCodecParameters? = par
    avcodec_parameters_free(mut p)
/** Copies src into dst. */
declare fn avcodec_parameters_copy as (dst: @AVCodecParameters, src: @AVCodecParameters): Int
/** Sets up a codec context from a stream's parameters, before avcodec_open2. */
declare fn avcodec_parameters_to_context as (codec: @AVCodecContext, par: @AVCodecParameters): Int
/** Sets a stream's parameters from an opened encoder, for a muxer. */
declare fn avcodec_parameters_from_context as (par: @AVCodecParameters, codec: @AVCodecContext): Int

// ---------------------------------------------------------------------------------------------------------------------
// Encoding and decoding
// ---------------------------------------------------------------------------------------------------------------------

/** A new codec context with the codec's defaults (null codec: none); free it with avcodec_free_context. Null when out of memory. */
declare fn avcodec_alloc_context3 as (codec: @AVCodec?): @AVCodecContext?
/** Closes and frees a codec context, and sets the variable to null. */
declare fn avcodec_free_context as (mut avctx: @AVCodecContext?)
/** Closes and frees a codec context. */
fn avcodec_free_context(avctx: @AVCodecContext) do
    var c: @AVCodecContext? = avctx
    avcodec_free_context(mut c)
/** Opens a codec context with the codec, and these options (codec_ctx options and the codec's private ones); those it didn't use stay in the dictionary. */
declare fn avcodec_open2 as (avctx: @AVCodecContext, codec: @AVCodec?, mut options: @AVDictionary?): Int
/** Opens a codec context with the codec; pass null for the options. */
declare fn avcodec_open2 as (avctx: @AVCodecContext, codec: @AVCodec?, options: @(@AVDictionary?)?): Int
/**
 * Gives a decoder a packet; null (or an empty packet) says there are no more, so that it gives the frames it
 * still holds. AVERROR(EAGAIN): receive frames first.
 */
declare fn avcodec_send_packet as (avctx: @AVCodecContext, avpkt: @AVPacket?): Int
/** Takes a decoded frame from a decoder: 0, AVERROR(EAGAIN) when it needs another packet, AVERROR_EOF when it has no more. */
declare fn avcodec_receive_frame as (avctx: @AVCodecContext, frame: @AVFrame): Int
/** Gives an encoder a frame; null says there are no more. AVERROR(EAGAIN): receive packets first. */
declare fn avcodec_send_frame as (avctx: @AVCodecContext, frame: @AVFrame?): Int
/** Takes an encoded packet from an encoder: 0, AVERROR(EAGAIN) when it needs another frame, AVERROR_EOF when it has no more. */
declare fn avcodec_receive_packet as (avctx: @AVCodecContext, avpkt: @AVPacket): Int
/** Drops what a codec holds, as after seeking. */
declare fn avcodec_flush_buffers as (avctx: @AVCodecContext)

// ---------------------------------------------------------------------------------------------------------------------
// Packets
// ---------------------------------------------------------------------------------------------------------------------

/** A new empty packet; free it with av_packet_free. Null when out of memory. */
declare fn av_packet_alloc as (): @AVPacket?
/** Frees a packet and what it references, and sets the variable to null. */
declare fn av_packet_free as (mut pkt: @AVPacket?)
/** Frees a packet and what it references. */
fn av_packet_free(pkt: @AVPacket) do
    var p: @AVPacket? = pkt
    av_packet_free(mut p)
/** A new packet referencing src's data; null when out of memory. */
declare fn av_packet_clone as (src: @AVPacket): @AVPacket?
/** Makes dst (empty) a new reference to src's data. */
declare fn av_packet_ref as (dst: @AVPacket, src: @AVPacket): Int
/** Lets go of the packet's data and resets it, so it can be used again. */
declare fn av_packet_unref as (pkt: @AVPacket)
/** Moves src's data to dst (which must be empty), and resets src. */
declare fn av_packet_move_ref as (dst: @AVPacket, src: @AVPacket)
/** Converts the packet's timestamps (pts, dts, duration) from time base tb_src to tb_dst: from an encoder's to a stream's. */
declare fn av_packet_rescale_ts as (pkt: @AVPacket, tb_src: AVRational, tb_dst: AVRational)
/** Gives an empty packet data of size bytes (and AV_INPUT_BUFFER_PADDING_SIZE zeros after it). */
declare fn av_new_packet as (pkt: @AVPacket, size: Int): Int
/** Makes the packet's data bigger by grow_by bytes. */
declare fn av_grow_packet as (pkt: @AVPacket, grow_by: Int): Int
/** Makes the packet's data size bytes. */
declare fn av_shrink_packet as (pkt: @AVPacket, size: Int)

// ---------------------------------------------------------------------------------------------------------------------
// Parsers
// ---------------------------------------------------------------------------------------------------------------------

/** A parser for a format; null if there is none. Free it with av_parser_close. */
declare fn av_parser_init as (codec_id: AVCodecID): @AVCodecParserContext?
/**
 * Parses buf_size bytes of buf: sets poutbuf/poutbuf_size to a whole packet once it has one (size 0: not yet), and
 * returns the bytes it used. Call it with buf_size 0 at the end to get the last packet.
 */
declare fn av_parser_parse2 as (s: @AVCodecParserContext, avctx: @AVCodecContext, out poutbuf: UChar[]c, out poutbuf_size: Int, buf: UChar[]c?, buf_size: Int, pts: Long, dts: Long, pos: Long): Int
/** Frees a parser. */
declare fn av_parser_close as (s: @AVCodecParserContext?)
